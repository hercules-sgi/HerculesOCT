package es.um.atica.sai.das.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.jboss.seam.annotations.Name;

import es.um.atica.commons.utils.UtilDate;
import es.um.atica.commons.utils.UtilString;
import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.sai.das.interfaces.UsuarioDAS;
import es.um.atica.sai.entities.Consumo;
import es.um.atica.sai.entities.EntidadPagadora;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;
import es.um.atica.sai.utils.Utilidades;

@Name( UsuarioDAS.NAME )
@Stateless
@Local( UsuarioDAS.class )
public class UsuarioDASImpl extends DataAccessServiceImpl<Usuario> implements UsuarioDAS {

	private static final String[] RESTRICCIONES_USUARIOS_PENDIENTES = {
			UtilString.cmpTextFilterEjbql( "us.dni", "#{usuariosPendientes.dniFiltrar}" ),
			Utilidades.cmpTextFilterEjbqlCompletoIgnoraTildes("us.datosUsuario.nombreCompleto", "#{usuariosPendientes.nombreFiltrar}"),
			UtilString.cmpTextFilterEjbqlCompleto("us.datosUsuario.email", "#{usuariosPendientes.emailFiltrar}" ),
			new StringBuilder("(us.caduca IS NULL OR us.caduca >=").append("TO_DATE(").append( "#{usuariosPendientes.fechaCaducaDesdeFiltrarString}").append( ",'dd/MM/yyyy'))" ).toString(),
			new StringBuilder("(us.caduca IS NULL OR us.caduca <").append("TO_DATE(").append( "#{usuariosPendientes.fechaCaducaHastaFiltrarString}").append( ",'dd/MM/yyyy')+1)" ).toString(),};


	private static List<String> getListaRestriccionesUsuariosPendientes()
	{
		return Arrays.asList(RESTRICCIONES_USUARIOS_PENDIENTES);
	}

	@Override
	public void crear(Usuario usuario) throws SaiException
	{
		try
		{
			this.persist( usuario, true);
		}
		catch(final Exception e)
		{
			log.error( "Error creando usuario:", e );
			throw new SaiException(e.getMessage());
		}
	}

	@Override
	public void modificar(Usuario usuario) throws SaiException
	{
		try
		{
			this.update( usuario, true);
		}
		catch(final Exception e)
		{
			log.error( "Error modificando usuario:", e );
			throw new SaiException(e.getMessage());
		}
	}

	@Override
	public void eliminarEntidadPagadoraSolicitadaUsuarios(EntidadPagadora entidad) throws SaiException
	{
		try {
			final Query query = getEntityManager().createQuery("UPDATE Usuario us SET us.entidadPagadoraSolicita=NULL WHERE us.entidadPagadoraSolicita=:entidad")
					.setParameter("entidad", entidad);
			query.executeUpdate();
			flush();
		} catch ( final Exception e ) {
			log.error( "Error en eliminarEntidadPagadoraSolicitadaUsuarios:", e );
			throw new SaiException(e.getMessage());
		}
	}

	@Override
	public Usuario findUsuarioByLogin( String login )
	{
		try
		{
			return (Usuario)getEntityManager().createNamedQuery(Usuario.GET_USUARIO_X_LOGIN).setParameter("mail", login).getSingleResult();
		}
		catch(final Exception ex)
		{
			return null;
		}
	}


	@Override
	public Usuario findUsuarioByDni(String dniStr)
	{
		try
		{
			return (Usuario)getEntityManager().createNamedQuery(Usuario.GET_USUARIO_X_DNI).setParameter("dni", dniStr).getSingleResult();
		}
		catch(final Exception ex)
		{
			return null;
		}
	}

	@Override
	public Usuario findUsuarioByEmail( String emailStr )
	{
		try
		{
			return (Usuario)getEntityManager().createNamedQuery(Usuario.GET_USUARIO_X_EMAIL).setParameter("mail", emailStr).getSingleResult();
		}
		catch(final Exception ex)
		{
			return null;
		}
	}

	@Override
	public Usuario getUsuarioByConsumo( Consumo c ) {
		try
		{
			return ( Usuario ) getEntityManager().createNamedQuery( Usuario.GET_USUARIOSOL_BY_CONSUMO).setParameter( "consumo", c ).getSingleResult();
		}
		catch (final Exception ex)
		{
			log.error( "Error en getUsuarioByConsumo: " + ex.getMessage() );
			return null;
		}
	}

	@Override
	public List<Usuario> getUsuariosPendientes()
	{
		final HashMap<String,Object> parameters  = new HashMap<>();
		final HashMap<String,Object> hints  = new HashMap<>();
		try
		{
			return this.findByEntityNamedQueryWithDinamicFilter(Usuario.GET_USUARIOS_PENDIENTES, getListaRestriccionesUsuariosPendientes(), parameters, null, null, null, "and", hints );
		}
		catch (final Exception ex)
		{
			return new ArrayList<>();
		}
	}

	@Override
	public Usuario getUsuarioById( long key ) {
		return getEntityManager().find(Usuario.class, key);
	}


	@Override
	public List<Usuario> getIrsByMiembroServicio(Usuario usuarioMiembro, Servicio servicio)
	{
		try
		{
			return entityManager.createNamedQuery(Usuario.GET_IR_BY_MIEMBRO_SERVICIO).setParameter("usuariomiembro", usuarioMiembro).setParameter("servicio", servicio).setParameter("fechaactual", new Date()).getResultList();
		}
		catch (final Exception ex)
		{
			log.error( "Error en getIrsByMiembroServicio: " + ex.getMessage() );
			return new ArrayList<>();
		}
	}

	@Override
	public List<Usuario> getIrsByMiembro(Usuario usuarioMiembro)
	{
		try
		{
			return entityManager.createNamedQuery(Usuario.GET_IR_BY_MIEMBRO).setParameter("usuariomiembro", usuarioMiembro).setParameter("fechaactual", new Date()).getResultList();
		}
		catch (final Exception ex)
		{
			log.error( "Error en getIrsByMiembroServicio: " + ex.getMessage() );
			return new ArrayList<>();
		}
	}
	
	@Override
	public List<Usuario> getUsuariosByServicio( Servicio servicio)
	{
		try {
			return entityManager.createNamedQuery(Usuario.GET_USUARIOS_SERVICIO)
					.setParameter( "servicio", servicio )
					.setParameter("fechaactual", new Date())
					.getResultList();
		}catch (final Exception ex) {
			log.error( "Error al recuperar usuarios por servicio: " + ex.getMessage() );
			return new ArrayList<>();
		}
	}

	@SuppressWarnings( "unchecked" )
	@Override
	public List<Usuario> getUsuariosByListaServicio( List<Servicio> servicios ) {
		try {
			return entityManager.createNamedQuery( Usuario.GET_USUARIOS_LISTA_SERVICIO )
					.setParameter( "servicios", servicios ).setParameter( "fechaactual", new Date() ).getResultList();
		} catch ( final Exception ex ) {
			log.error( "Error al recuperar usuarios por servicio: " + ex.getMessage() );
			return new ArrayList<>();
		}
	}

	@Override
	public List<Usuario> getUsuariosPuedoSolicitarEnSuNombreByServicio( Servicio servicio)
	{
		try
		{
			return entityManager.createNamedQuery(Usuario.GET_USUARIOS_PUEDOSOLICITARENSUNOMBRE_X_SERVICIO).setParameter( "servicio", servicio ).setParameter("fechaactual", new Date()).getResultList();
		}
		catch (final Exception ex)
		{
			log.error( "Error en getUsuariosPuedoSolicitarEnSuNombreByServicio: " + ex.getMessage() );
			return new ArrayList<>();
		}
	}

	@Override
	public List<Usuario> getUsuariosGestorPuedeSolicitarCertificacion()
	{
		try
		{
			return entityManager.createNamedQuery(Usuario.GET_USUARIOS_PUEDOSOLICITAR_CERTIFICACION).setParameter("fechaactual", new Date()).getResultList();
		}
		catch (final Exception ex)
		{
			log.error( "Error en getUsuariosGestorPuedeSolicitarCertificacion: " + ex.getMessage() );
			return new ArrayList<>();
		}
	}

	@Override
	public List<Usuario> getUsuariosSupervisorPuedeSolicitarCertificacion(List<Servicio> listaServicios)
	{
		try
		{
			return entityManager.createNamedQuery(Usuario.GET_USUARIOS_PUEDOSOLICITAR_CERTIFICACION_X_LISTASERVICIOS).setParameter("fechaactual", new Date()).setParameter("listaservicios", listaServicios).getResultList();
		}
		catch (final Exception ex)
		{
			log.error( "Error en getUsuariosSupervisorPuedeSolicitarCertificacion: " + ex.getMessage() );
			return new ArrayList<>();
		}
	}

	@Override
	public List<Usuario> getIrsByServicio(Servicio servicio)
	{
		try
		{
			return entityManager.createNamedQuery(Usuario.GET_IR_X_SERVICIO).setParameter( "servicio", servicio ).setParameter("fechaactual", new Date()).getResultList();
		}
		catch (final Exception ex)
		{
			log.error( "Error en getIrByServicio: " + ex.getMessage() );
			return new ArrayList<>();
		}
	}

	@Override
	public List<Usuario> getIrsByListaServicios(List<Servicio> listaServicios)
	{
		try
		{
			return entityManager.createNamedQuery(Usuario.GET_IR_X_LISTASERVICIOS).setParameter("listaservicios", listaServicios).setParameter("fechaactual", new Date()).getResultList();
		}
		catch (final Exception ex)
		{
			log.error( "Error en getIrByServicio: " + ex.getMessage() );
			return new ArrayList<>();
		}
	}
	
	@Override
	public List<Usuario> getIrsTodos()
	{
		try
		{
			return entityManager.createNamedQuery(Usuario.GET_IR_ALL).setParameter("fechaactual", new Date()).getResultList();
		}
		catch (final Exception ex)
		{
			log.error( "Error en getIrTodos: " + ex.getMessage() );
			return new ArrayList<>();
		}
	}

	@Override
	public List<Usuario> getIrsConConsumosPendientes(Servicio servicio)
	{
		try
		{
			return entityManager.createNamedQuery(Usuario.GET_IRS_CON_CONSUMOS_PENDIENTES_BY_SERVICIO).setParameter("servicio", servicio).setParameter("fechahace3anos", UtilDate.sumarAños(new Date(), -3)).getResultList();
		}
		catch (final Exception ex)
		{
			log.error( "Error en getIrsConConsumosPendientes: " + ex.getMessage() );
			return new ArrayList<>();
		}
	}

	@Override
	public List<Usuario> getMiembrosByIr(Usuario usuarioIr)
	{
		try
		{
			return entityManager.createNamedQuery(Usuario.GET_MIEMBROS_X_IR).setParameter( "codusuarioir", usuarioIr.getCod()).setParameter("fechaactual", new Date()).getResultList();
		}
		catch (final Exception ex)
		{
			log.error( "Error al recuperar usuarios miembros por IR: " + ex.getMessage() );
			return new ArrayList<>();
		}
	}

	@Override
	public List<Usuario> getMiembrosByIrServicio(Usuario usuarioIr, Servicio servicio)
	{
		try
		{
			return entityManager.createNamedQuery(Usuario.GET_MIEMBROS_X_IR_SERVICIO).setParameter( "codusuarioir", usuarioIr.getCod()).setParameter("servicio", servicio).setParameter("fechaactual", new Date()).getResultList();
		}
		catch (final Exception ex)
		{
			log.error( "Error al recuperar usuarios miembros por IR y servicio: " + ex.getMessage() );
			return new ArrayList<>();
		}
	}

	@Override
	public List<Usuario> getUsuariosTodos()
	{
		try
		{
			return entityManager.createNamedQuery(Usuario.GET_ALL).getResultList();
		}
		catch (final Exception ex)
		{
			log.error( "Error en getUsuariosTodos #0", ex.toString() );
			return new ArrayList<>();
		}
	}

	@Override
	public List<Usuario> getTecnicosByService( Servicio servicio )
	{
		try
		{
			return getEntityManager().createNamedQuery( Usuario.GET_TECNICOS_BY_SERVICIO).setParameter( "servicio", servicio ).setParameter("fechaactual",new Date()).getResultList();
		}
		catch (final Exception ex)
		{
			log.error( "Error en getTecnicosByService: " + ex.getMessage() );
			return new ArrayList<>();
		}
	}


	@Override
	public List<Usuario> getTecnicosByServiceProd( Servicio servicio ) {
		try
		{
			return getEntityManager().createNamedQuery( Usuario.GET_TECNICOS_BY_SERVICIO_PROD).setParameter( "servicio", servicio ).setParameter("fechaactual",new Date()).getResultList();
		}
		catch (final Exception ex)
		{
			log.error( "Error en getTecnicosByServiceProd: " + ex.getMessage() );
			return new ArrayList<>();
		}
	}

	@Override
	public List<Usuario> getTecnicosByProducto(Producto producto) {
		try
		{
			return getEntityManager().createNamedQuery( Usuario.GET_TECNICOS_BY_PROD).setParameter( "producto", producto ).setParameter("fechaactual",new Date()).getResultList();
		}
		catch (final Exception ex)
		{
			return new ArrayList<>();
		}
	}

	@Override
	public List<Usuario> getTecnicosAutomaticosByProducto(Producto producto)
	{
		try
		{
			return getEntityManager().createNamedQuery( Usuario.GET_TECNICOS_AUTO_BY_PROD).setParameter( "producto", producto ).setParameter("fechaactual",new Date()).getResultList();
		}
		catch (final Exception ex)
		{
			return new ArrayList<>();
		}
	}

	@Override
	public List<Usuario> getSupervisoresServicio(Servicio servicio) {
		try
		{
			return getEntityManager().createNamedQuery( Usuario.GET_SUPERVISORES_SERVICIO).setParameter("servicio", servicio).setParameter("fechaactual", new Date()).getResultList();
		}
		catch (final Exception ex)
		{
			log.error( "Error en getSupervisoresServicio: " + ex.getMessage() );
			return new ArrayList<>();
		}
	}

	@Override
	public List<Usuario> getMiembrosServicio(Servicio servicio) 
	{
		try
		{
			return getEntityManager().createNamedQuery( Usuario.GET_MIEMBROS_SERVICIO).setParameter("servicio", servicio).setParameter("fechaactual", new Date()).getResultList();
		}
		catch (final Exception ex)
		{
			log.error( "Error en getMiembrosServicio: " + ex.getMessage() );
			return new ArrayList<>();
		}
	}
	
	public boolean avisarIrNuevaSolicitudMiembro(Usuario usuarioMiembro, Usuario usuarioIr, Servicio servicio)
	{
		try
		{
			return "SI".equals((String)getEntityManager().createNamedQuery(Usuario.AVISAR_IR_NUEVA_SOLICITUD_MIEMBRO).setParameter("usuariomiembro", usuarioMiembro).setParameter("usuarioir", usuarioIr).setParameter("servicio", servicio).setParameter("fechaactual", new Date()).getSingleResult());
		}
		catch (final Exception ex)
		{
			log.error( "Error en avisarIrNuevaSolicitudMiembro: " + ex.getMessage() );
			return false;
		}
	}
		
	@Override
	public boolean tienePerfil(Usuario usuario, String tagPerfil)
	{
		try
		{
			return (Long)entityManager.createNamedQuery(Usuario.TIENE_PERFIL).setParameter("usuario", usuario).setParameter("tagperfil", tagPerfil).setParameter("fechaactual",new Date()).getSingleResult()>0;
		}
		catch (final Exception ex)
		{
			return false;
		}
	}

	@Override
	public boolean tienePerfilEnServicio(Usuario usuario, String tagPerfil, Servicio servicio)
	{
		try
		{
			return (Long)entityManager.createNamedQuery(Usuario.TIENE_PERFIL_EN_SERVICIO).setParameter("usuario", usuario).setParameter("tagperfil", tagPerfil).setParameter("servicio", servicio).setParameter("fechaactual",new Date()).getSingleResult()>0;
		}
		catch (final Exception ex)
		{
			return false;
		}
	}

	@SuppressWarnings( "unchecked" )
	@Override
	public List<String> buscaUsuariosACTIByPatron( String patron ) {
		try {
			return getEntityManager()
					.createNamedQuery( Usuario.GET_USUARIO_BY_PATRON ).setParameter( "patron", new StringBuilder( "%" )
							.append( patron.replace( " ", "%" ).toLowerCase() ).append( "%" ).toString() )
					.getResultList();
		} catch ( final Exception ex ) {
			log.error( "Error en buscaPacientesByPatronNombre(#0): #1", patron, ex.toString() );
			return new ArrayList<>();
		}
	}

}
