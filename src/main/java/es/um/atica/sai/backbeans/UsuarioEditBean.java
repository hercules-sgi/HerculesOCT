package es.um.atica.sai.backbeans;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.jboss.seam.log.Log;
import org.umu.atica.servicios.gesper.gente.entity.Persona;

import es.um.atica.sai.dtos.EntidadRespuesta;
import es.um.atica.sai.dtos.TipoPerfil;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.entities.UsuarioPerfil;
import es.um.atica.sai.exceptions.SaiException;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.impl.MensajeServiceImpl;
import es.um.atica.sai.services.interfaces.MensajeService;
import es.um.atica.sai.services.interfaces.ServicioService;
import es.um.atica.sai.services.interfaces.TarifaService;
import es.um.atica.sai.services.interfaces.UsuarioService;
import es.um.atica.sai.utils.Utilidades;

@Name("usuarioEdit")
@Scope(ScopeType.CONVERSATION)
public class UsuarioEditBean {

	@Logger
	private Log log;

	@In(create=true)
	private FacesMessages facesMessages;

	@In( value = UsuarioService.NAME, create = true )
	private UsuarioService usuarioService;

	@In( value = ServicioService.NAME, create = true )
	private ServicioService servicioService;

	@In( value = TarifaService.NAME, create = true )
	private TarifaService tarifaService;

	@In( value = MensajeServiceImpl.NAME, create = true )
	private MensajeService mensajeService;

	@In(value="org.jboss.seam.security.identity")
	SaiIdentity identity;

	private Usuario usuarioEdit;
	private String descripcionUsuarioBuscar;
	private Persona personaEncontrada;
	boolean existeEnGente;
	boolean esTrabajadorUmu;
	private boolean introducirDatos;
	private UsuarioPerfil usuarioPerfilEdit;
	private List<Usuario> listaIrsPerfilAdd;

	public Usuario getUsuarioEdit() {
		return usuarioEdit;
	}

	public void setUsuarioEdit( Usuario usuarioEdit ) {
		this.usuarioEdit = usuarioEdit;
	}

	public String getDescripcionUsuarioBuscar() {
		return descripcionUsuarioBuscar;
	}

	public void setDescripcionUsuarioBuscar( String descripcionUsuarioBuscar ) {
		this.descripcionUsuarioBuscar = descripcionUsuarioBuscar;
	}

	public Persona getPersonaEncontrada() {
		return personaEncontrada;
	}

	public void setPersonaEncontrada( Persona personaEncontrada ) {
		this.personaEncontrada = personaEncontrada;
	}

	public boolean isExisteEnGente() {
		return existeEnGente;
	}

	public void setExisteEnGente( boolean existeEnGente ) {
		this.existeEnGente = existeEnGente;
	}

	public boolean isEsTrabajadorUmu() {
		return esTrabajadorUmu;
	}

	public void setEsTrabajadorUmu( boolean esTrabajadorUmu ) {
		this.esTrabajadorUmu = esTrabajadorUmu;
	}

	public boolean isIntroducirDatos() {
		return introducirDatos;
	}

	public void setIntroducirDatos( boolean introducirDatos ) {
		this.introducirDatos = introducirDatos;
	}

	public UsuarioPerfil getUsuarioPerfilEdit() {
		return usuarioPerfilEdit;
	}

	public void setUsuarioPerfilEdit( UsuarioPerfil usuarioPerfilEdit ) {
		this.usuarioPerfilEdit = usuarioPerfilEdit;
	}

	public List<Usuario> getListaIrsPerfilAdd() {
		return listaIrsPerfilAdd;
	}

	public void setListaIrsPerfilAdd( List<Usuario> listaIrsPerfilAdd ) {
		this.listaIrsPerfilAdd = listaIrsPerfilAdd;
	}


	public String establecerUsuarioCreate()
	{
		descripcionUsuarioBuscar = null;
		usuarioEdit = new Usuario();
		existeEnGente = false;
		esTrabajadorUmu = false;
		introducirDatos = false;
		usuarioPerfilEdit = new UsuarioPerfil();
		listaIrsPerfilAdd = null;
		return "usuarioEdit";
	}

	public String establecerUsuarioEdit(Usuario usuario)
	{
		usuarioEdit = usuario;
		existeEnGente = usuarioService.existeEnGente(usuario.getDni());
		esTrabajadorUmu = usuarioService.esTrabajadorUmu(usuario.getDni());
		if (existeEnGente)
		{
			usuarioEdit.setTelefono(usuarioEdit.getDatosUsuario().getTelefono());
			if (!esTrabajadorUmu)
			{
				usuarioEdit.setEmail(usuarioEdit.getDatosUsuario().getEmail());
			}
		}
		introducirDatos = true;
		usuarioPerfilEdit = new UsuarioPerfil();
		listaIrsPerfilAdd = null;
		return "usuarioEdit";
	}

	public List<String> buscaUsuariosByPatron(String patron)
	{
		final List<String> listaUsuariosEncontrados =  usuarioService.buscaUsuariosByPatron( patron );
		if (( listaUsuariosEncontrados==null ) || listaUsuariosEncontrados.isEmpty())
		{
			facesMessages.add(StatusMessage.Severity.INFO,
					"usuario.busqueda.patron.noencontrado",
					"usuario.busqueda.patron.noencontrado.detalles",
					null, null);
		}
		return listaUsuariosEncontrados;
	}

	public void seleccionadoUsuario()
	{
		//Comprobamos si existe en gente
		personaEncontrada = usuarioService.getPersonaByDescripcion(descripcionUsuarioBuscar);
		existeEnGente = personaEncontrada != null;
		esTrabajadorUmu = usuarioService.esTrabajadorUmuByDescripcion(descripcionUsuarioBuscar);
		// Comprobamos si existe como usuario
		final Usuario usuarioAux = usuarioService.getUsuarioByDescripcion(descripcionUsuarioBuscar);
		if (usuarioAux != null)
		{
			// Intentan dar de alta un usuario que ya está dado de alta, pasamos a modo edición
			establecerUsuarioEdit(usuarioAux);
			facesMessages.add(StatusMessage.Severity.INFO,
					"usuario.alta.yaexiste",
					null, null,
					usuarioEdit.getDatosUsuario().getNombreCompleto());
		}
		else if (existeEnGente)
		{
			usuarioEdit.setDni(personaEncontrada.getIdentificador());
			usuarioEdit.setNombre(personaEncontrada.getNombre() );
			usuarioEdit.setApellidos(personaEncontrada.getApellidos());
			usuarioEdit.setEmail(usuarioService.getEmailByDescripcion(descripcionUsuarioBuscar));
			usuarioEdit.setTelefono(personaEncontrada.getTelefonoMovil());
		}
		introducirDatos = true;
	}

	public void pulsadoCrearNuevoUsuario()
	{
		usuarioEdit = new Usuario();
		descripcionUsuarioBuscar = null;
		existeEnGente = false;
		esTrabajadorUmu = false;
		introducirDatos = true;
	}

	public void cancelarAltaUsuario()
	{
		establecerUsuarioCreate();
	}

	public void guardarUsuario()
	{
		String password = null;
		String passwordCifrado = null;
		if (usuarioEdit.getCod()==null)
		{
			Usuario usuarioAux = usuarioService.findUsuarioByDni(usuarioEdit.getDni());
			if (usuarioAux != null)
			{
				// Pasamos a modo edición del usuario que tiene ese DNI
				establecerUsuarioEdit(usuarioAux);
				facesMessages.add(StatusMessage.Severity.INFO,
						"usuario.guardar.yaexistedni",
						null, null,
						usuarioEdit.getDni());
				return;
			}
			usuarioAux = usuarioService.findUsuarioByEmail(usuarioEdit.getEmail());
			if (usuarioAux != null)
			{
				facesMessages.add(StatusMessage.Severity.ERROR,
						"usuario.guardar.error",
						"usuario.guardar.yaexisteemail",
						null, null,
						usuarioEdit.getEmail());
				return;
			}
			if (!existeEnGente || (( usuarioEdit.getEmail()!=null ) && !usuarioEdit.getEmail().contains("@um.es") && !usuarioEdit.getEmail().contains("@ticarum.es")))
			{
				// Generamos contraseña y asignamos al usuario
				password = Utilidades.obtenerStringAleatorio(8);
				passwordCifrado = Utilidades.getStringCifrado(password);
				usuarioEdit.setContrasena(passwordCifrado);
			}
		}
		try
		{
			usuarioService.guardarUsuario(usuarioEdit);
			facesMessages.add(StatusMessage.Severity.INFO,
					"usuario.guardar.ok",
					null, null,
					usuarioEdit.getDni());
		}
		catch ( final SaiException e )
		{
			facesMessages.add(StatusMessage.Severity.ERROR,
					"usuario.guardar.error",
					null, null,
					e.getMessage());
			return;
		}
		//Notificamos al usuario mediante email
		usuarioService.recargarUsuario(usuarioEdit);
		final EntidadRespuesta er = mensajeService.altaUsuario(usuarioEdit, password);
		if ((boolean)er.getEntidad())
		{
			facesMessages.add(StatusMessage.Severity.INFO,
					"usuario.guardar.ok.notificar.ok",
					null, null,
					er.getRespuesta());
		}
		else
		{
			facesMessages.add(StatusMessage.Severity.WARN,
					"usuario.guardar.ok.notificar.error",
					null, null,
					er.getRespuesta());
		}
		establecerUsuarioEdit(usuarioEdit);
	}

	public void bajaUsuario()
	{
		try
		{
			usuarioEdit.setEstado("BAJA");
			usuarioService.guardarUsuario(usuarioEdit);
			facesMessages.add(StatusMessage.Severity.INFO,
					"usuario.dar.baja.ok",
					null, null,
					usuarioEdit.getDatosUsuario().getNombreCompleto());
		}
		catch ( final SaiException e )
		{
			facesMessages.add(StatusMessage.Severity.ERROR,
					"usuario.dar.baja.error",
					null, null,
					e.getMessage());
		}
	}

	public void altaUsuario()
	{
		try
		{
			usuarioEdit.setEstado("ALTA");
			usuarioService.guardarUsuario(usuarioEdit);
			facesMessages.add(StatusMessage.Severity.INFO,
					"usuario.dar.alta.ok",
					null, null,
					usuarioEdit.getDatosUsuario().getNombreCompleto());
		}
		catch ( final SaiException e )
		{
			facesMessages.add(StatusMessage.Severity.ERROR,
					"usuario.dar.alta.error",
					null, null,
					e.getMessage());
		}
	}

	public String volver()
	{
		return "usuarios";
	}

	public void establecerUsuarioPerfilCreate()
	{
		this.usuarioPerfilEdit = new UsuarioPerfil();
	}
	
	public void establecerUsuarioPerfilEdit(UsuarioPerfil up)
	{
		this.usuarioPerfilEdit = up;
	}
	
	public void seleccionadoPerfilAddPerfil()
	{
		usuarioPerfilEdit.setServicio( null );
		usuarioPerfilEdit.setUsuarioIrResponsable( null );
	}

	public void seleccionadoServicioAddPerfil()
	{
		if (usuarioPerfilEdit.getServicio() == null)
		{
			listaIrsPerfilAdd = null;
			return;
		}
		if (usuarioPerfilEdit.getPerfil().getRequiereIr().equals("SI"))
		{
			if (identity.tienePerfilEnServicio(TipoPerfil.GESTOR, usuarioPerfilEdit.getServicio().getCod()))
			{
				listaIrsPerfilAdd = usuarioService.getIrsByServicio(usuarioPerfilEdit.getServicio());
			}
			else if (identity.tienePerfilEnServicio(TipoPerfil.IR, usuarioPerfilEdit.getServicio().getCod()))
			{
				listaIrsPerfilAdd = new ArrayList<>();
				getListaIrsPerfilAdd().add(identity.getUsuarioConectado());
				usuarioPerfilEdit.setUsuarioIrResponsable(identity.getUsuarioConectado());
			}
			else
			{
				//Aquí no debe llegarse nunca
				listaIrsPerfilAdd = null;
			}
		}
	}

	public void guardarUsuarioPerfil()
	{
		usuarioPerfilEdit.setUsuario(usuarioEdit);

		try
		{
			usuarioService.guardarUsuarioPerfil(usuarioPerfilEdit);
		}
		catch (final SaiException e)
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "usuario.add.perfil.error", null, null, e.getMessage());
			return;
		}
		facesMessages.add(StatusMessage.Severity.INFO, "usuario.add.perfil.ok", null, null, getDescripcionUsuarioPerfil(usuarioPerfilEdit));
		if (usuarioPerfilEdit.getPendienteValidarIr().equals("SI"))
		{
			facesMessages.add(StatusMessage.Severity.INFO, "value.attention", "usuario.guardar.pendientevalidarir", null, null);
		}

	}

	public void eliminarPerfil(UsuarioPerfil up)
	{
		final String descripcionPerfil = getDescripcionUsuarioPerfil(up);
		try
		{
			usuarioService.eliminarUsuarioPerfil( up );
		}
		catch ( final SaiException e )
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "usuario.delete.perfil.error", null, null, e.getMessage());
			return;
		}
		facesMessages.add(StatusMessage.Severity.INFO, "usuario.delete.perfil.ok", null, null, descripcionPerfil);
	}

	public boolean puedeEliminarPerfil(UsuarioPerfil up)
	{
		return usuarioService.puedeEliminarPerfil( up );
	}

	public List<UsuarioPerfil> getPerfilesUsuarioIgnoraEstado()
	{
		return usuarioService.getPerfilesUsuarioIgnoraEstadoPuedeVerUsuarioConectado(usuarioEdit);
	}

	public boolean tienePerfilIrIgnoraEstado()
	{
		return usuarioService.tienePerfilIrIgnoraEstado(usuarioEdit);
	}

	public String getDescripcionUsuarioPerfil(UsuarioPerfil up)
	{
		return usuarioService.getDescripcionUsuarioPerfil( up );
	}

	public List<Usuario> getMiembrosByIrServicio(UsuarioPerfil up)
	{
		if (up == null)
		{
			return new ArrayList<>();
		}
		return usuarioService.getMiembrosByIrServicio(up.getUsuario(), up.getServicio());
	}

}
