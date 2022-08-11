package es.um.atica.sai.services.impl;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.validation.constraints.NotNull;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.SeamResourceBundle;
import org.jboss.seam.log.Log;
import org.primefaces.model.SortOrder;
import org.umu.atica.servicios.gesper.gente.entity.CorreoElectronico;
import org.umu.atica.servicios.gesper.gente.entity.Persona;
import org.umu.atica.servicios.gesper.gente.exceptions.CorreoElectronicoException;
import org.umu.atica.servicios.gesper.gente.exceptions.CorreoElectronicoNotFoundException;

import es.um.atica.commons.utils.UtilDate;
import es.um.atica.jdbc.exceptions.FundeWebJdbcRollBackException;
import es.um.atica.jpa.das.ResultQuery;
import es.um.atica.sai.das.interfaces.ConsumoDAS;
import es.um.atica.sai.das.interfaces.DatosUsuarioViewDAS;
import es.um.atica.sai.das.interfaces.EntidadPagadoraDAS;
import es.um.atica.sai.das.interfaces.EntidadesIrDAS;
import es.um.atica.sai.das.interfaces.GrupoInvestigacionDAS;
import es.um.atica.sai.das.interfaces.PerfilDAS;
import es.um.atica.sai.das.interfaces.ServicioSaiDAS;
import es.um.atica.sai.das.interfaces.TecnicoProductoDAS;
import es.um.atica.sai.das.interfaces.UsuarioDAS;
import es.um.atica.sai.das.interfaces.UsuarioPerfilDAS;
import es.um.atica.sai.dtos.EntidadRespuesta;
import es.um.atica.sai.dtos.TipoPerfil;
import es.um.atica.sai.entities.Consumo;
import es.um.atica.sai.entities.DatosUsuarioView;
import es.um.atica.sai.entities.EntidadPagadora;
import es.um.atica.sai.entities.EntidadesIr;
import es.um.atica.sai.entities.Factura;
import es.um.atica.sai.entities.GrupoInvestigacion;
import es.um.atica.sai.entities.Perfil;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.entities.UsuarioPerfil;
import es.um.atica.sai.exceptions.SaiException;
import es.um.atica.sai.paos.interfaces.SaiPao;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.MensajeService;
import es.um.atica.sai.services.interfaces.ServicioService;
import es.um.atica.sai.services.interfaces.TarifaService;
import es.um.atica.sai.services.interfaces.UsuarioService;
import es.um.atica.sai.utils.Utilidades;
import es.um.atica.seam.factories.ServicioGenteFactory;

@Name(UsuarioService.NAME)
@Stateless
@Local(UsuarioService.class)
public class UsuarioServiceImpl implements UsuarioService{

	@Logger
	private Log log;

	@In(value= DatosUsuarioViewDAS.NAME, create=true)
	private DatosUsuarioViewDAS datosUsuarioViewDAS;

	@In(value= UsuarioDAS.NAME, create=true)
	private UsuarioDAS usuarioDAS;

	@In(value = "servicioSaiDAS", create = true)
	private ServicioSaiDAS servicioSaiDAS;

	@In(value= UsuarioPerfilDAS.NAME, create=true)
	private UsuarioPerfilDAS usuarioServicioDAS;

	@In( value = MensajeServiceImpl.NAME, create = true )
	private MensajeService mensajeService;

	@In(value= ServicioService.NAME, create=true)
	private ServicioService servicioService;

	@In(value= TarifaService.NAME, create=true)
	private TarifaService tarifaService;

	@In(value= EntidadPagadoraDAS.NAME, create=true)
	private EntidadPagadoraDAS entidadPagadoraDAS;

	@In(value= EntidadesIrDAS.NAME, create=true)
	private EntidadesIrDAS entidadesIrDAS;

	@In(value= GrupoInvestigacionDAS.NAME, create=true)
	private GrupoInvestigacionDAS grupoInvestigacionDAS;
	
	@In(value= PerfilDAS.NAME, create=true)
	private PerfilDAS perfilDAS;

	@In(create=true)
	private ConsumoDAS consumoDAS;

	@In(create=true)
	private TecnicoProductoDAS tecnicoDAS;

	@In(create=true)
	private UsuarioPerfilDAS usuarioPerfilDAS;

	@In("org.jboss.seam.security.identity")
	SaiIdentity identity;

	@In(create=true)
	private SaiPao saiPao;

	ResourceBundle srb = SeamResourceBundle.getBundle();


	@Override
	public Usuario autenticacionUsuario(String login)
	{
		// Comprobamos primero si existe la fila de usuario con el login
		Usuario usuario = usuarioDAS.findUsuarioByLogin(login);
		if (usuario != null)
		{
			return usuario;
		}
		// Obtenemos el dni del usuario y buscamos el usuario por dni (puede haberse autenticado con un email UM no prioritario)
		CorreoElectronico ce = null;
		try
		{
			ce = ServicioGenteFactory.instance().getServicioGenteUmu().getIdentificador( login );
		}
		catch ( CorreoElectronicoException | CorreoElectronicoNotFoundException ex )
		{
			log.error("No se ha podido encontrar ningún usuario con el email #0",login, ex);
			return null;
		}
		usuario = usuarioDAS.findUsuarioByDni(ce.getIdentificador());
		if (usuario != null)
		{
			return usuario;
		}
		// Si no existe la fila de usuario tenemos que darla de alta
		final Usuario nuevoUsuario = new Usuario();
		nuevoUsuario.setDni(ce.getIdentificador());
		nuevoUsuario.setEstado("PEND");
		nuevoUsuario.setAcepta("NO");
		try
		{
			guardarUsuario(nuevoUsuario);
		}
		catch ( final SaiException ex )
		{
			log.error("No se ha podido crear una fila de usuario con el dni #0", ce.getIdentificador(), ex);
			return null;
		}
		log.info("Nueva fila de usuario #0 dada de alta", login );
		recargarUsuario( nuevoUsuario );
		return nuevoUsuario;
	}


	@Override
	public Usuario findUsuarioByLogin(String login){
		return usuarioDAS.findUsuarioByLogin(login);
	}


	@Override
	public void guardarUsuario( Usuario usuario ) throws SaiException
	{
		if (usuario.getCod()==null)
		{
			//Comprobamos que no exista ya el usuario
			Usuario usuarioAux = findUsuarioByDni(usuario.getDni());
			if (usuarioAux != null)
			{
				throw new SaiException(new StringBuilder("Ya existe un usuario con el DNI ").append(usuario.getDni()).toString());
			}
			usuarioAux = findUsuarioByEmail(usuario.getEmail());
			if (usuarioAux != null)
			{
				throw new SaiException(new StringBuilder("Ya existe un usuario con el email ").append(usuario.getEmail()).toString());
			}
			usuario.setAcepta("NO");
			if (existeEnGente(usuario.getDni()))
			{
				usuario.setNombre(null);
				usuario.setApellidos(null);
			}
			if (esTrabajadorUmu(usuario.getDni()))
			{
				usuario.setEmail(null);
				usuario.setEstado("ALTA");
				try
				{
					usuario.setCaduca(saiPao.getFechafinEstrabajadorumu(usuario.getDni()) );
				}
				catch ( final FundeWebJdbcRollBackException e )
				{
					throw new SaiException("No se ha podido obtener la caducidad de la relación");
				}
			}
			else
			{
				usuario.setEstado("PEND" );
				usuario.setCaduca(UtilDate.sumarDias(new Date(), 15));
			}
			usuarioDAS.crear(usuario);
		}
		else
		{
			usuarioDAS.modificar( usuario );
		}
	}

	@Override
	public Usuario findUsuarioByDni( String dniStr ) {
		return usuarioDAS.findUsuarioByDni( dniStr );
	}

	@Override
	public Usuario findUsuarioByEmail( String emailStr ) {
		return usuarioDAS.findUsuarioByEmail( emailStr );
	}

	@Override
	public DatosUsuarioView findDatosUsuarioByDni( String dniStr ) {
		return datosUsuarioViewDAS.findUsuarioViewByDni( dniStr );
	}

	@Override
	public DatosUsuarioView findDatosUsuarioByEmail( String emailStr ) {
		return datosUsuarioViewDAS.findUsuarioViewByEmail( emailStr );
	}

	@Override
	public List<Usuario> getUsuariosPendientes()
	{
		return usuarioDAS.getUsuariosPendientes();
	}

	@Override
	public Usuario getUsuarioById(Long cod) {
		return usuarioDAS.getUsuarioById( cod );
	}

	@Override
	public DatosUsuarioView getDatosUsuarioById( @NotNull Long cod ) {

		return datosUsuarioViewDAS.find( cod );
	}


	@Override
	public List<Usuario> getIrsByMiembroServicio(Usuario usuario, Servicio servicio) {
		try {
			final List<Usuario> irs = usuarioDAS.getIrsByMiembroServicio( usuario, servicio );
			Collections.sort( irs );
			return irs;
		}catch (final Exception ex) {
			log.error( "Error en la recuperación de Ir por usuario #0", ex );
			return new ArrayList<>();
		}
	}

	@Override
	public List<Usuario> getUsuariosByServicio( Servicio servicio ) {
		try
		{
			final List<Usuario> listaUsuarios = usuarioDAS.getUsuariosByServicio( servicio );
			Collections.sort(listaUsuarios);
			return listaUsuarios;
		}
		catch (final Exception ex)
		{
			log.error( "Error en la recuperación de usuarios por servicio #0", ex );
			return new ArrayList<>();
		}
	}

	@Override
	public List<Usuario> getUsuariosPuedoSolicitarEnSuNombreByServicio(Servicio servicio)
	{
		if (!identity.tienePerfilEnServicio(TipoPerfil.SUPERVISOR, servicio.getCod()) &&
				!identity.tienePerfilEnServicio(TipoPerfil.TECNICO, servicio.getCod()))
		{
			// Si el usuario no es supervisor o tecnico no puede solicitar en nombre de otra persona. No se debería llamar en ese
			// caso a esta función pero dejo la comprobación por si acaso
			return new ArrayList<>();
		}
		try
		{
			final List<Usuario> listaUsuarios = usuarioDAS.getUsuariosPuedoSolicitarEnSuNombreByServicio(servicio);
			Collections.sort(listaUsuarios);
			return listaUsuarios;
		}
		catch (final Exception ex)
		{
			log.error( "Error en getUsuariosPuedoSolicitarEnSuNombreByServicio #0", ex );
			return new ArrayList<>();
		}
	}
	@Override
	public List<Usuario> getIrsByServicio(Servicio servicio)
	{
		return usuarioDAS.getIrsByServicio( servicio );
	}

	@Override
	public List<Usuario> getIrsByListaServicios(List<Servicio> listaServicios)
	{
		List<Usuario> listaUsuarios = usuarioDAS.getIrsByListaServicios(listaServicios);
		Collections.sort(listaUsuarios);
		return listaUsuarios;
	}
	
	@Override
	public List<Usuario> getIrsParaEstimacion()
	{
		List<Usuario> listaIr = new ArrayList<>();
		if (identity.tienePerfil(TipoPerfil.GESTOR))
		{
			listaIr = usuarioDAS.getIrsTodos();
			Collections.sort(listaIr);
		}
		else if (identity.tienePerfil(TipoPerfil.IR ))
		{

			listaIr.add(identity.getUsuarioConectado());
		}
		return listaIr;
	}

	@Override
	public List<Usuario> getIrsConConsumosPendientes(Servicio servicio)
	{
		final List<Usuario> listaUsuarios = usuarioDAS.getIrsConConsumosPendientes( servicio );
		Collections.sort( listaUsuarios );
		return listaUsuarios;
	}

	private String listaUsuariosToListaCods(List<Usuario> listaUsuarios)
	{
		if (( listaUsuarios == null ) || listaUsuarios.isEmpty())
		{
			return null;
		}
		final StringBuilder listaCods = new StringBuilder("");
		final Iterator<Usuario> itUs = listaUsuarios.iterator();
		while (itUs.hasNext())
		{
			final Usuario us = itUs.next();
			listaCods.append(us.getCod().toString()).append(",");
		}
		return listaCods.substring(0, listaCods.length()-1);
	}

	@Override
	public List<Usuario> getMiembrosByIr(Usuario usuarioIr)
	{
		List<Usuario> listaUsuarios = usuarioDAS.getMiembrosByIr(usuarioIr);
		Collections.sort(listaUsuarios);
		return listaUsuarios;
	}

	@Override
	public List<Usuario> getMiembrosByIrServicio(Usuario usuarioIr, Servicio servicio)
	{
		return usuarioDAS.getMiembrosByIrServicio( usuarioIr, servicio );
	}

	@Override
	public String getListaCodsMiembroByIrServicio(Usuario usuarioIr, Servicio servicio)
	{
		return listaUsuariosToListaCods(usuarioDAS.getMiembrosByIrServicio(usuarioIr, servicio));
	}

	@Override
	public List<Usuario> getListaUsuariosSolicitantes()
	{
		final List<DatosUsuarioView> listaDatosUsuario = datosUsuarioViewDAS.getListaUsuariosSolicitantes(getSqlBusquedaUsuarios());
		final List<Usuario> listaUsuarios = new ArrayList<>();
		if (( listaDatosUsuario != null ) && !listaDatosUsuario.isEmpty())
		{
			final Iterator<DatosUsuarioView> itDuv = listaDatosUsuario.iterator();
			while (itDuv.hasNext())
			{
				final DatosUsuarioView duv = itDuv.next();
				listaUsuarios.add(duv.getUsuario());
			}
			Collections.sort( listaUsuarios );
		}
		return listaUsuarios;
	}

	@Override
	public List<Usuario> getTecnicosByServicio( Servicio servicio ) 
	{
		try 
		{
			log.info( "Recuperando Técnicos por servicio en servicio Usuario" );
			final List<Usuario> tecnicos = usuarioDAS.getTecnicosByService( servicio );
			Collections.sort( tecnicos );
			return tecnicos;
		}
		catch(final Exception ex) 
		{
			log.error( "Error en getTecnicosByServicio " + ex.getMessage()  );
			return new ArrayList<>();
		}
	}

	@Override
	public List<Usuario> getTecnicosByServicioProd(Servicio servicio) 
	{
		return usuarioDAS.getTecnicosByServiceProd( servicio );
	}

	@Override
	public List<Usuario> getTecnicosByProducto(Producto producto) 
	{
		return usuarioDAS.getTecnicosByProducto( producto );
	}



	@Override
	public List<Usuario> getUsuariosTodos()
	{
		return usuarioDAS.getUsuariosTodos();
	}

	@Override
	public void recargarUsuario(Usuario usuario)
	{
		usuarioDAS.refresh( usuario );
		datosUsuarioViewDAS.refresh(usuario.getDatosUsuario());
	}

	@Override
	public List<EntidadesIr> getEntidadesIrByUsuario(Usuario usuario)
	{
		return entidadesIrDAS.getEntidadesIrByUsuario( usuario );
	}

	@Override
	public List<GrupoInvestigacion> getGruposInvestigacionByUsuarioEntidadPagadora(Usuario usuario, EntidadPagadora ep)
	{
		if (ep.getCif() != null)
		{
			try 
			{
				GrupoInvestigacion gi = grupoInvestigacionDAS.getGrupoInvestigacionByEntidadPagadora(ep);
				List<GrupoInvestigacion> listaGi = new ArrayList<>();
				listaGi.add(gi);
				return listaGi;
			} 
			catch ( SaiException ex ) 
			{
				log.error("Error obteniendo grupo de investigación a partir de entidad pagadora externa", ex);
			}
		}
		try 
		{
			List<HashMap<String, Object>> listaHmGi = saiPao.getGruposInvestigacion(usuario.getDatosUsuario().getDni());
			return this.listaHashMapToListGruposInvestigacion(listaHmGi);
		} 
		catch ( Exception ex ) 
		{
			log.error("Error en getGruposInvestigacionByUsuario.", ex);
			return new ArrayList<>();
		}
	}
	
	private List<GrupoInvestigacion> listaHashMapToListGruposInvestigacion(List<HashMap<String, Object>> listaHmGi)
	{
		List<GrupoInvestigacion> listaGi = new ArrayList<>();
		if (listaHmGi == null) 
		{
			return listaGi;
		}
		try
		{
			for (final HashMap<String, Object> hmGi: listaHmGi)
			{
				final Long cod = ((BigDecimal)hmGi.get("COD")).longValue();
				GrupoInvestigacion gi = grupoInvestigacionDAS.find(cod);
				listaGi.add(gi);
			}
		}
		catch (final Exception e)
		{
			log.error("Error extrayendo lista de grupos de investigación desde cursor:", e);
		}
		return listaGi;
	}
	
	@Override
	public void crearEntidadesIr(EntidadesIr eir) throws SaiException
	{
		if (entidadesIrDAS.existeEntidadIr(eir))
		{
			throw new SaiException("Ya existe la asociación entre entidad pagadora y usuario");
		}
		entidadesIrDAS.crear( eir );
		if (( eir.getUsuario().getEntidadPagadoraSolicita() != null ) && eir.getEntidadPagadora().equals(eir.getUsuario().getEntidadPagadoraSolicita()))
		{
			// Si se acaba de asignar la entidad que el usuario IR solicitó, la borramos de la fila de usuario
			eir.getUsuario().setEntidadPagadoraSolicita(null);
			usuarioDAS.modificar(eir.getUsuario());
		}
	}

	@Override
	public void eliminarEntidadesIr(EntidadesIr eir) throws SaiException
	{
		entidadesIrDAS.eliminar( eir );
	}

	@Override
	public Usuario getUsuarioByConsumo( Consumo c ) {
		try {
			return usuarioDAS.getUsuarioByConsumo(c);
		}catch (final Exception ex) {
			log.error( "Error al recuperar un usuario por consumo #0", ex.toString() );
			return null;
		}
	}

	@Override
	public void solicitudRegistroIr(Usuario usuario, EntidadPagadora entidad) throws SaiException
	{
		EntidadPagadora entidadExistente = null;
		if (entidad.getCif()!=null)
		{
			entidadExistente = entidadPagadoraDAS.getEntidadPagadoraByCif(entidad.getCif(),entidad.getCodSubtercero());
		}
		if (entidad.getUnidadAdministrativa()!=null)
		{
			entidadExistente = entidadPagadoraDAS.getEntidadPagadoraByUdadmin(entidad.getUnidadAdministrativa());
		}
		if (entidadExistente != null)
		{
			usuario.setEntidadPagadoraSolicita(entidadExistente);
		}
		else
		{
			if (entidad.getUnidadAdministrativa()!=null)
			{
				String nombreDep = tarifaService.getNombreDepartamento(entidad.getUnidadAdministrativa());
				if (( nombreDep != null ) && ( nombreDep.length()>=199 ))
				{
					nombreDep = nombreDep.substring( 0,198 );
				}
				entidad.setNombre(nombreDep);
			}
			entidad.setEstado("S");
			entidadPagadoraDAS.crear( entidad );
			usuario.setEntidadPagadoraSolicita(entidad);
		}
		usuarioDAS.modificar(usuario);
	}

	@Override
	public void solicitudRegistroNuevoIrExterno(Usuario usuario, String password, EntidadPagadora entidad) throws SaiException
	{
		Usuario usuarioAux = findUsuarioByDni(usuario.getDni());
		if (usuarioAux != null)
		{
			throw new SaiException(new StringBuilder("El DNI ").append(usuario.getDni()).append(" ya está dado de alta como usuario de la aplicación GSAI").toString());
		}
		if (esTrabajadorUmu(usuario.getDni()))
		{
			throw new SaiException(new StringBuilder("El DNI ").append(usuario.getDni()).append(" pertenece a un trabajador de la UM. Debe auntenticarse en la aplicación con su cuenta @um.es").toString());
		}
		usuarioAux = findUsuarioByLogin(usuario.getEmail());
		if (usuarioAux != null)
		{
			throw new SaiException(new StringBuilder("El email ").append(usuario.getEmail()).append(" pertenece a un usuario ya dado de alta en la aplicación").toString());
		}
		final String passwordCifrado = Utilidades.getStringCifrado(password);
		usuario.setContrasena(passwordCifrado);
		guardarUsuario(usuario);

		final EntidadPagadora entidadExistente = entidadPagadoraDAS.getEntidadPagadoraByCif(entidad.getCif(), entidad.getCodSubtercero());
		if (entidadExistente != null)
		{
			usuario.setEntidadPagadoraSolicita(entidadExistente);
		}
		else
		{
			entidad.setEstado("S");
			entidadPagadoraDAS.crear( entidad );
			usuario.setEntidadPagadoraSolicita(entidad);
		}
		usuarioDAS.modificar(usuario);
	}

	@Override
	public EntidadRespuesta generarNuevoPassword(String email)
	{
		if (email.contains("@um.es") || email.contains("@ticarum.es" ))
		{
			return new EntidadRespuesta(null, srb.getString("login.recuperacion.error.emailumu"));
		}
		final Usuario usuario = usuarioDAS.findUsuarioByLogin(email);
		if (usuario == null)
		{
			return new EntidadRespuesta(null, MessageFormat.format(srb.getString("login.recuperacion.error.noencontrado"),email));
		}
		final String password = Utilidades.obtenerStringAleatorio(8);
		final String passwordCifrado = Utilidades.getStringCifrado(password);
		usuario.setContrasena(passwordCifrado);
		try
		{
			guardarUsuario( usuario );
			return new EntidadRespuesta(password, srb.getString("login.recuperacion.ok" ));
		}
		catch (final Exception ex)
		{
			return new EntidadRespuesta(null, ex.getMessage());
		}

	}

	@Override
	public EntidadRespuesta restablecerPasswordAutenticado(String passwordViejo, String passwordNuevo)
	{
		final Usuario usuario = identity.getUsuarioConectado();
		return restablecerPasswordNoAutenticado(usuario.getEmail(), passwordViejo, passwordNuevo);
	}

	@Override
	public EntidadRespuesta restablecerPasswordNoAutenticado(String email, String passwordViejo, String passwordNuevo)
	{
		if (email.contains("@um.es") || email.contains("@ticarum.es" ))
		{
			return new EntidadRespuesta(null, srb.getString("login.recuperacion.error.emailumu"));
		}
		final Usuario usuario = findUsuarioByLogin(email);
		if (usuario == null)
		{
			return new EntidadRespuesta(null, MessageFormat.format(srb.getString("login.recuperacion.error.noencontrado"),email));
		}
		final String passwordViejoCifrado = Utilidades.getStringCifrado(passwordViejo);
		if (!passwordViejoCifrado.equals(usuario.getContrasena()))
		{
			return new EntidadRespuesta(null, srb.getString("login.nuevopass.error.passwordviejoerror"));
		}
		final String passwordNuevoCifrado = Utilidades.getStringCifrado(passwordNuevo);
		usuario.setContrasena(passwordNuevoCifrado);
		try
		{
			guardarUsuario( usuario );
			return new EntidadRespuesta(usuario.getEmail(), srb.getString("login.recuperacion.ok" ));
		}
		catch (final Exception ex)
		{
			return new EntidadRespuesta(null, ex.getMessage());
		}
	}



	@Override
	public boolean puedeEliminarPerfil(UsuarioPerfil up)
	{
		// Un SUPERGESTOR puede eliminar cualquier perfil
		if (identity.tienePerfil(TipoPerfil.SUPERGESTOR))
		{
			return true;
		}
		// Un GESTOR puede eliminar cualquier perfil menos SUPERGESTOR
		if (identity.tienePerfil(TipoPerfil.GESTOR) && !up.getPerfil().getTag().equals(TipoPerfil.SUPERGESTOR))
		{
			return true;
		}
		// Un SUPERVISOR de servicio puede eliminar cualquier perfil de TECNICO, IR o MIEMBRO de su servicio
		// Un IR de servicio puede eliminar cualquier perfil de MIEMBRO en el que es su IR asignado
		return (( up.getServicio() != null ) &&
				((identity.tienePerfilEnServicio(TipoPerfil.SUPERVISOR, up.getServicio().getCod()) && !up.getPerfil().getTag().equals(TipoPerfil.SUPERVISOR)) ||
						(identity.tienePerfilEnServicio(TipoPerfil.IR, up.getServicio().getCod()) && identity.getUsuarioConectado().equals(up.getUsuarioIrResponsable()))));
	}

	@Override
	public List<UsuarioPerfil> getPerfilesUsuario(Usuario usuario)
	{
		return usuarioPerfilDAS.getPerfilesUsuario( usuario );
	}

	private boolean puedeVerPerfil(UsuarioPerfil up)
	{
		// Si el usuario conectado es SUPERGESTOR no se llama a esta función
		// Un GESTOR puede ver cualquier perfil menos SUPERGESTOR
		// Un SUPERVISOR de servicio puede ver cualquier SUPERVISOR, TECNICO, IR o MIEMBRO del servicio
		// Un IR de servicio puede ver su propio perfil de IR además de cualquier MIEMBRO del que es responsable
		return (!TipoPerfil.SUPERGESTOR.equals(up.getPerfil().getTag()) && identity.tienePerfil(TipoPerfil.GESTOR)) ||
				(( up.getServicio()!=null ) && identity.tienePerfilEnServicio(TipoPerfil.SUPERVISOR, up.getServicio().getCod())) ||
				(( up.getServicio()!=null ) && identity.tienePerfilEnServicio(TipoPerfil.IR, up.getServicio().getCod()) && (up.getUsuario().equals(identity.getUsuarioConectado()) || (up.getPerfil().getTag().equals(TipoPerfil.MIEMBRO) && up.getUsuarioIrResponsable().equals(identity.getUsuarioConectado()))));
	}


	@Override
	public List<UsuarioPerfil> getPerfilesUsuarioPuedeVerUsuarioConectado(Usuario usuario)
	{
		final List<UsuarioPerfil> listaPerfilesProcesada = new ArrayList<>();
		final List<UsuarioPerfil> listaPerfiles = usuarioPerfilDAS.getPerfilesUsuario( usuario );
		if (identity.tienePerfil(TipoPerfil.SUPERGESTOR))
		{
			return listaPerfiles;
		}
		if (( listaPerfiles != null ) && !listaPerfiles.isEmpty())
		{

			final Iterator<UsuarioPerfil> itUp = listaPerfiles.iterator();
			while (itUp.hasNext())
			{
				final UsuarioPerfil up = itUp.next();
				if (puedeVerPerfil(up))
				{
					listaPerfilesProcesada.add(up);
				}
			}
		}
		return listaPerfilesProcesada;
	}

	@Override
	public List<UsuarioPerfil> getPerfilesUsuarioIgnoraEstado(Usuario usuario)
	{
		return usuarioPerfilDAS.getPerfilesUsuarioIgnoraEstado( usuario );
	}

	@Override
	public List<UsuarioPerfil> getPerfilesUsuarioIgnoraEstadoPuedeVerUsuarioConectado(Usuario usuario)
	{
		final List<UsuarioPerfil> listaPerfilesProcesada = new ArrayList<>();
		final List<UsuarioPerfil> listaPerfiles = usuarioPerfilDAS.getPerfilesUsuarioIgnoraEstado( usuario );
		if (identity.tienePerfil(TipoPerfil.SUPERGESTOR))
		{
			return listaPerfiles;
		}
		if (( listaPerfiles != null ) && !listaPerfiles.isEmpty())
		{

			final Iterator<UsuarioPerfil> itUp = listaPerfiles.iterator();
			while (itUp.hasNext())
			{
				final UsuarioPerfil up = itUp.next();
				if (puedeVerPerfil(up))
				{
					listaPerfilesProcesada.add(up);
				}
			}
		}
		return listaPerfilesProcesada;
	}

	@Override
	public boolean tienePerfil(Usuario usuario, String tagPerfil)
	{
		if (usuarioDAS.tienePerfil(usuario, TipoPerfil.SUPERGESTOR))
		{
			return true;
		}
		if (!tagPerfil.equals(TipoPerfil.SUPERGESTOR) && usuarioDAS.tienePerfil(usuario, TipoPerfil.GESTOR))
		{
			return true;
		}
		return usuarioDAS.tienePerfil(usuario, tagPerfil);
	}

	@Override
	public boolean tienePerfilEnServicio(Usuario usuario, String tagPerfil, Servicio servicio)
	{
		if (usuarioDAS.tienePerfil(usuario, TipoPerfil.SUPERGESTOR))
		{
			return true;
		}
		if (!tagPerfil.equals(TipoPerfil.SUPERGESTOR) && usuarioDAS.tienePerfil(usuario, TipoPerfil.GESTOR))
		{
			return true;
		}
		return usuarioDAS.tienePerfilEnServicio(usuario, tagPerfil, servicio);
	}

	@Override
	public boolean tienePerfilIrIgnoraEstado(Usuario usuario)
	{
		return usuarioPerfilDAS.tienePerfilIrIgnoraEstado( usuario );
	}

	@Override
	public String getDescripcionUsuarioPerfil(UsuarioPerfil up)
	{
		final StringBuilder descripcion = new StringBuilder(up.getPerfil().getNombre());
		if (up.getServicio()!=null)
		{
			descripcion.append(" en ").append(up.getServicio().getNombre());
		}
		return descripcion.toString();
	}

	@Override
	public List<String> getListaDescripcionesUsuarioPerfil(Usuario usuario)
	{
		final List<UsuarioPerfil> listaUp = usuarioPerfilDAS.getPerfilesUsuario(usuario);
		final List<String> listaDescripcionesUp = new ArrayList<>( 0 );
		final Iterator<UsuarioPerfil> itUp = listaUp.iterator();
		while (itUp.hasNext())
		{
			final UsuarioPerfil up = itUp.next();
			final String descripcion = getDescripcionUsuarioPerfil(up);
			if (!listaDescripcionesUp.contains(descripcion))
			{
				listaDescripcionesUp.add(descripcion);
			}
		}
		return listaDescripcionesUp;
	}

	@Override
	public List<String> getListaDescripcionesUsuarioPerfilPuedeVerUsuarioConectado(Usuario usuario)
	{
		final List<UsuarioPerfil> listaUp = getPerfilesUsuarioPuedeVerUsuarioConectado(usuario);
		final List<String> listaDescripcionesUp = new ArrayList<>( 0 );
		final Iterator<UsuarioPerfil> itUp = listaUp.iterator();
		while (itUp.hasNext())
		{
			final UsuarioPerfil up = itUp.next();
			final String descripcion = getDescripcionUsuarioPerfil(up);
			if (!listaDescripcionesUp.contains(descripcion))
			{
				listaDescripcionesUp.add(descripcion);
			}
		}
		return listaDescripcionesUp;
	}

	@Override
	public List<Perfil> getPerfilesPuedeCrearByPerfil(Perfil perfil)
	{
		return usuarioPerfilDAS.getPerfilesPuedeCrearByPerfil( perfil );
	}

	@Override
	public Usuario getUsuarioByCod(Long codUsuario)
	{
		return usuarioDAS.find( codUsuario );
	}

	@Override
	public void aceptaCondicionesActi() throws SaiException
	{
		identity.getUsuarioConectado().setAcepta("SI");
		usuarioDAS.modificar(identity.getUsuarioConectado());
	}
	@Override
	public String getSqlBusquedaUsuarios()
	{
		if (identity.tienePerfil(TipoPerfil.SUPERGESTOR) || identity.tienePerfil(TipoPerfil.GESTOR))
		{
			// La consulta base es diferente para que aparezcan los usuarios que no tienen asignado ningún perfil
			return "SELECT duv FROM DatosUsuarioView duv";
		}
		final StringBuilder sql = new StringBuilder("SELECT DISTINCT up.usuario.datosUsuario FROM UsuarioPerfil up");
		String serviciosEsSupervisor = null;
		String serviciosEsIr = null;
		if (identity.tienePerfil(TipoPerfil.SUPERVISOR))
		{
			serviciosEsSupervisor = servicioService.getListaCodsServicioEsSupervisor(identity.getUsuarioConectado());
			if (serviciosEsSupervisor != null)
			{
				sql.append(" WHERE (up.servicio.cod IN (").append(serviciosEsSupervisor).append(")");
			}
		}
		if (identity.tienePerfil(TipoPerfil.IR))
		{
			serviciosEsIr = servicioService.getListaCodsServicioEsIr(identity.getUsuarioConectado());
			if (serviciosEsIr != null)
			{
				if (serviciosEsSupervisor == null)
				{
					sql.append(" WHERE (up.servicio.cod IN (").append(serviciosEsIr).append(") AND up.usuarioIrResponsable=").append(identity.getUsuarioConectado().getCod()).append(")");
				}
				else
				{
					sql.append(" OR (up.servicio.cod IN (").append(serviciosEsIr).append(") AND up.usuarioIrResponsable=").append(identity.getUsuarioConectado().getCod()).append(")");
				}
			}
		}
		if (( serviciosEsSupervisor == null ) && ( serviciosEsIr == null ))
		{
			return null;
		}
		else
		{
			sql.append(")");
		}
		return sql.toString();
	}

	@Override
	public ResultQuery<DatosUsuarioView> busquedaUsuarios(int first, int pageSize, String sortField, SortOrder sortOrder)
	{
		return datosUsuarioViewDAS.busquedaUsuarios(getSqlBusquedaUsuarios(), first, pageSize, sortField, sortOrder);
	}

	@Override
	public Long getCountBusquedaUsuarios()
	{
		return datosUsuarioViewDAS.getCountBusquedaUsuarios(getSqlBusquedaUsuarios());
	}

	@Override
	public ResultQuery<DatosUsuarioView> busquedaUsuariosIr(int first, int pageSize, String sortField, SortOrder sortOrder)
	{
		return datosUsuarioViewDAS.busquedaUsuariosIr( first, pageSize, sortField, sortOrder );
	}

	@Override
	public Long getCountBusquedaUsuariosIr()
	{
		return datosUsuarioViewDAS.getCountBusquedaUsuariosIr();
	}

	// Control de búsqueda de usuarios
	private List <String> getListaDescripcionesUsuarios(List<HashMap<String,Object>> hmUsuarios)
	{
		final List<String> listaDescUsuarios = new ArrayList<>(0);
		if (hmUsuarios == null) 
		{
			return listaDescUsuarios;
		}
		try
		{
			for (final HashMap<String, Object> descUsuario: hmUsuarios)
			{
				final String desc = (String) descUsuario.get("DESC_USUARIO");
				listaDescUsuarios.add(desc);
			}
		}
		catch (final Exception e)
		{
			log.error("Error extrayendo lista de descripciones de Usuario desde cursor:", e);
		}
		return listaDescUsuarios;
	}

	@Override
	public boolean existeEnGente(String identificador)
	{
		try
		{
			final Persona persona = ServicioGenteFactory.instance().getServicioGenteUmu().getPersona("SAI", identificador);
			return persona != null;
		}
		catch ( final Exception ex )
		{
			return false;
		}
	}

	@Override
	public List<String> buscaUsuariosByPatron(String patron)
	{
		List<HashMap<String, Object>> hmUsuarios;
		try
		{
			hmUsuarios = saiPao.busquedaUsuariosXPatron(patron);
		}
		catch ( final FundeWebJdbcRollBackException e )
		{
			log.error("Error en buscaUsuariosByPatron: ", e );
			hmUsuarios = null;
		}
		return getListaDescripcionesUsuarios(hmUsuarios);
	}

	@Override
	public List<String> buscaUsuariosACTIByPatron( String patron ) {
		return usuarioDAS.buscaUsuariosACTIByPatron( patron );
	}

	@Override
	public boolean esTrabajadorUmuByDescripcion(String descripcion)
	{
		String[] datosUsuario;
		String identificador;
		try
		{
			datosUsuario= descripcion.split( " - " );
			identificador = datosUsuario[1];
		}
		catch(final Exception ex)
		{
			log.error("Error comprobando si es trabajador UM a partir de descripción obtenida en búsqueda por patrón", ex );
			return false;
		}
		try
		{
			return esTrabajadorUmu( identificador );
		}
		catch ( final Exception ex )
		{
			log.error("Error comprobando si es trabajador UM a partir de identificador #0: #1",identificador, ex );
			return false;
		}
	}

	@Override
	public boolean esIrFactura(Factura factura)
	{
		return factura.getCod()!=null && identity.tienePerfilEnServicio(TipoPerfil.IR, factura.getServicio().getCod()) && identity.getUsuarioConectado().equals(factura.getInvestigador());
	}
	
	@Override
	public boolean esSupervisorFactura(Factura factura)
	{
		return factura.getCod()!=null && identity.tienePerfilEnServicio(TipoPerfil.SUPERVISOR, factura.getServicio().getCod());
	}

	
	@Override
	public Persona getPersonaByDescripcion(String descripcion)
	{
		String[] datosUsuario;
		String identificador;
		try
		{
			datosUsuario= descripcion.split( " - " );
			identificador = datosUsuario[1];
		}
		catch(final Exception ex)
		{
			log.error("Error recuperando Persona a partir de descripción obtenida en búsqueda por patrón", ex );
			return null;
		}
		try
		{
			return ServicioGenteFactory.instance().getServicioGenteUmu().getPersona("SAI", identificador);
		}
		catch ( final Exception ex )
		{
			log.error("Error recuperando Persona a partir de identificador #0: #1",identificador, ex );
			return null;
		}
	}

	@Override
	public Usuario getUsuarioByDescripcion(String descripcion)
	{
		String[] datosUsuario;
		String identificador;
		try
		{
			datosUsuario= descripcion.split( " - " );
			identificador = datosUsuario[1];
		}
		catch(final Exception ex)
		{
			log.error("Error recuperando Usuario a partir de descripción obtenida en búsqueda por patrón", ex );
			return null;
		}
		try
		{
			return findUsuarioByDni(identificador );
		}
		catch ( final Exception ex )
		{
			log.error("Error recuperando Usuario a partir de identificador #0: #1",identificador, ex );
			return null;
		}
	}

	@Override
	public String getEmailByDescripcion(String descripcion)
	{
		String[] datosUsuario;
		try
		{
			datosUsuario = descripcion.split( " - " );
			return datosUsuario[2];
		}
		catch(final Exception ex)
		{
			log.error("Error recuperando email a partir de descripción obtenida en búsqueda por patrón", ex );
			return null;
		}
	}

	@Override
	public boolean esTrabajadorUmu(String dni)
	{
		try
		{
			final String resultado = saiPao.esTrabajadorUmu(dni);
			return "SI".equals(resultado);
		}
		catch ( final FundeWebJdbcRollBackException e )
		{
			log.error("Error en esTrabajadorUmu #0: #1",dni, e );
			return false;
		}
	}

	private String getSqlPerfilesPuedeFiltrar()
	{
		final StringBuilder sql = new StringBuilder("SELECT p FROM Perfil p");
		final String ordenacion = " ORDER BY p.cod";
		if (identity.tienePerfil(TipoPerfil.SUPERGESTOR))
		{
			return sql.append(ordenacion).toString();
		}
		if (identity.tienePerfil(TipoPerfil.GESTOR))
		{
			return sql.append(" WHERE p.tag<>'").append(TipoPerfil.SUPERGESTOR).append("'").append(ordenacion).toString();
		}
		if (identity.tienePerfil(TipoPerfil.SUPERVISOR))
		{
			return sql.append(" WHERE p.tag NOT IN ('").append(TipoPerfil.SUPERGESTOR).append("', '").append(TipoPerfil.GESTOR).append("', '").append(TipoPerfil.ADMINISTRATIVO).append("')").append(ordenacion).toString();
		}
		return sql.append(" WHERE p.tag NOT IN ('").append(TipoPerfil.SUPERGESTOR).append("', '").append(TipoPerfil.GESTOR).append("', '").append(TipoPerfil.ADMINISTRATIVO).append("', '").append(TipoPerfil.SUPERVISOR).append("', '").append(TipoPerfil.TECNICO).append("')").append(ordenacion).toString();
	}

	@Override
	public List<Perfil> getPerfilesPuedeFiltrar()
	{
		return perfilDAS.getPerfilesPuedeFiltrar(getSqlPerfilesPuedeFiltrar());
	}

	@Override
	public List<Perfil> getListaPerfiles()
	{
		return perfilDAS.getListaPerfiles();
	}

	@Override
	public Perfil getPerfilByTag(String tagPerfil)
	{
		return perfilDAS.getPerfilByTag( tagPerfil );
	}

	private void crearUsuarioPerfil(UsuarioPerfil up) throws SaiException
	{
		up.setFechaAlta(new Date());
		up.setPendienteValidarIr(up.getPerfil().getPendienteValidarInicial());
		if (( up.getServicio()!=null ) && up.getPerfil().getPendienteValidarInicial().equals("SI") &&
				identity.tienePerfilEnServicio(TipoPerfil.SUPERVISOR, up.getServicio().getCod()))
		{
			up.setPendienteValidarIr("NO");
			up.setUsuarioSupervisorValidador(identity.getUsuarioConectado());
		}
		UsuarioPerfil upAux = null;
		if (up.getPerfil().getRequiereServicio().equals("NO"))
		{
			upAux = usuarioPerfilDAS.getUsuarioPerfilNoRequiereServicio(up);
		}
		else if (up.getPerfil().getRequiereIr().equals("NO"))
		{
			upAux = usuarioPerfilDAS.getUsuarioPerfilSiRequiereServicioNoRequiereIr(up);
		}
		else
		{
			upAux = usuarioPerfilDAS.getUsuarioPerfilSiRequiereServicioSiRequiereIr(up);
		}
		if (upAux == null)
		{
			usuarioPerfilDAS.crear( up );
		}
		else
		{
			if (up.getPendienteValidarIr().equals("SI"))
			{
				throw new SaiException("El usuario ya tiene ese perfil asignado pero está pendiente de validar por un supervisor del servicio");
			}
			else
			{
				throw new SaiException("El usuario ya tiene ese perfil asignado");
			}
		}
	}

	@Override
	public void guardarUsuarioPerfil(UsuarioPerfil up) throws SaiException
	{
		if (up.getCod() == null)
		{
			crearUsuarioPerfil(up);
		}
		else
		{
			usuarioPerfilDAS.modificar( up );
		}
	}

	@Override
	public void eliminarUsuarioPerfil(UsuarioPerfil up) throws SaiException
	{
		usuarioPerfilDAS.eliminar( up );
	}

	@Override
	public List<Usuario> getListaIrsPuedoAsignarEnSolicitud(Usuario usuarioSolicitante, Servicio servicio)
	{
		// Obtenemos primero los IR's del solicitante
		final List<Usuario> listaIrs = usuarioDAS.getIrsByMiembroServicio( usuarioSolicitante, servicio );
		// Comprobamos el perfil con la función de UsuarioDAS porque aquí no nos vale alguien que sea sólo GESTOR o SUPERGESTOR
		if (usuarioDAS.tienePerfilEnServicio(usuarioSolicitante, TipoPerfil.IR, servicio) && !listaIrs.contains(usuarioSolicitante))
		{
			// Si el solicitante es IR y no está en la lista (porque no es también miembro) lo añadimos
			listaIrs.add(usuarioSolicitante);
		}
		Collections.sort( listaIrs );
		return listaIrs;
	}

	@Override
	public List<Usuario> getUsuariosByListaServicio( List<Servicio> servicios ) 
	{
		List<Usuario> listaUsuarios = usuarioDAS.getUsuariosByListaServicio(servicios);
		Collections.sort(listaUsuarios);
		return listaUsuarios;
	}
}
