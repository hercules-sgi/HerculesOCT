package es.um.atica.sai.backbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.jboss.seam.log.Log;

import es.um.atica.commons.utils.UtilDate;
import es.um.atica.sai.entities.Certificacion;
import es.um.atica.sai.entities.TipoCertificacion;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.services.interfaces.CertificacionesService;
import es.um.atica.sai.services.interfaces.UsuarioService;

@Name( CertificacionBusquedaBean.NAME )
@Scope( ScopeType.CONVERSATION )
public class CertificacionBusquedaBean implements Serializable {


	/**
	 *
	 */
	private static final long serialVersionUID = -4648498140327786566L;

	public static final String NAME = "certificacionBusquedaBean";

	@Logger
	private transient Log logger;

	@In( create = true )
	private transient CertificacionesService certificacionesService;

	@In( create = true )
	private transient UsuarioService usuarioService;

	@In
	protected FacesMessages facesMessages;

	private List<Certificacion> listaCertificaciones;
	private List<TipoCertificacion> listaTipoCertificaciones;
	private TipoCertificacion tipoCertificacionBuscar;
	private Date fechaAltaDesdeBuscar;
	private String fechaAltaDesdeBuscarString;
	private Date fechaAltaHastaBuscar;
	private String fechaAltaHastaBuscarString;
	private Date fechaCaducidadDesdeBuscar;
	private String fechaCaducidadDesdeBuscarString;
	private Date fechaCaducidadHastaBuscar;
	private String fechaCaducidadHastaBuscarString;
	private Usuario usuarioBuscar;
	private String descripcionUsuarioBuscar;
	private String estado;

	@Create
	public void inicializar() {
		listaTipoCertificaciones = certificacionesService.getListaTiposCertificaciones();
		estado = "PENDIENTE";
		buscar();
	}

	public void buscar() {
		listaCertificaciones = certificacionesService.busquedaCertificaciones();
	}

	public void limpiar() {
		descripcionUsuarioBuscar = "";
		fechaAltaDesdeBuscar = null;
		fechaAltaDesdeBuscarString = "";
		fechaCaducidadDesdeBuscar = null;
		fechaCaducidadDesdeBuscarString = "";
		fechaAltaHastaBuscar = null;
		fechaAltaHastaBuscarString = "";
		fechaCaducidadHastaBuscar = null;
		fechaCaducidadHastaBuscarString = "";
		listaCertificaciones = new ArrayList<>();
		tipoCertificacionBuscar = null;
		usuarioBuscar = null;
		estado = "PENDIENTE";
		buscar();
	}
	public List<String> buscaUsuariosByPatron( String patron ) {
		final List<String> listaUsuariosEncontrados = usuarioService.buscaUsuariosACTIByPatron( patron );
		if ( ( listaUsuariosEncontrados == null ) || listaUsuariosEncontrados.isEmpty() ) {
			facesMessages.add( StatusMessage.Severity.INFO, "usuario.busqueda.patron.noencontrado",
					"usuario.busqueda.patron.noencontrado.detalles", null, null );
		}
		return listaUsuariosEncontrados;
	}

	public void seleccionadoUsuario(){
		usuarioBuscar = usuarioService.getUsuarioByDescripcion( descripcionUsuarioBuscar );
	}

	public String volver() {
		buscar();
		return "listar";
	}

	public String getEstiloFechaCaducidad(Certificacion cert)
	{
		if (cert.getFechaCaducidad() != null && UtilDate.anterior(cert.getFechaCaducidad(), new Date()))
		{
			return "negrita rojo";
		}
		return null;
	}
	
	public String getColorEstado(Certificacion cert)
	{
		return certificacionesService.getColorEstadoCertificacion(certificacionesService.getDescripcionEstadoCertificacion(cert));
	}

	public List<Certificacion> getListaCertificaciones() {
		return listaCertificaciones;
	}

	public void setListaCertificaciones( List<Certificacion> listaCertificaciones ) {
		this.listaCertificaciones = listaCertificaciones;
	}

	public TipoCertificacion getTipoCertificacionBuscar() {
		return tipoCertificacionBuscar;
	}

	public void setTipoCertificacionBuscar( TipoCertificacion tipoCertificacionBuscar ) {
		this.tipoCertificacionBuscar = tipoCertificacionBuscar;
	}

	public List<TipoCertificacion> getListaTipoCertificaciones() {
		return listaTipoCertificaciones;
	}

	public void setListaTipoCertificaciones( List<TipoCertificacion> listaTipoCertificaciones ) {
		this.listaTipoCertificaciones = listaTipoCertificaciones;
	}

	public Usuario getUsuarioBuscar() {
		return usuarioBuscar;
	}

	public void setUsuarioBuscar( Usuario usuarioBuscar ) {
		this.usuarioBuscar = usuarioBuscar;
	}

	public String getDescripcionUsuarioBuscar() {
		return descripcionUsuarioBuscar;
	}

	public void setDescripcionUsuarioBuscar( String descripcionUsuarioBuscar ) {
		this.descripcionUsuarioBuscar = descripcionUsuarioBuscar;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado( String estado ) {
		this.estado = estado;
	}

	public Date getFechaAltaDesdeBuscar() {
		return fechaAltaDesdeBuscar;
	}

	public void setFechaAltaDesdeBuscar( Date fechaAltaDesdeBuscar ) {
		this.fechaAltaDesdeBuscar = fechaAltaDesdeBuscar;
		fechaAltaDesdeBuscarString = UtilDate.convertirDateFechaToString( this.fechaAltaDesdeBuscar );
	}

	public String getFechaAltaDesdeBuscarString() {
		return fechaAltaDesdeBuscarString;
	}

	public void setFechaAltaDesdeBuscarString( String fechaAltaDesdeBuscarString ) {
		this.fechaAltaDesdeBuscarString = fechaAltaDesdeBuscarString;
	}

	public Date getFechaAltaHastaBuscar() {
		return fechaAltaHastaBuscar;
	}

	public void setFechaAltaHastaBuscar( Date fechaAltaHastaBuscar ) {
		this.fechaAltaHastaBuscar = fechaAltaHastaBuscar;
		fechaAltaHastaBuscarString = UtilDate.convertirDateFechaToString( this.fechaAltaHastaBuscar );
	}

	public String getFechaAltaHastaBuscarString() {
		return fechaAltaHastaBuscarString;
	}

	public void setFechaAltaHastaBuscarString( String fechaAltaHastaBuscarString ) {
		this.fechaAltaHastaBuscarString = fechaAltaHastaBuscarString;
	}

	public Date getFechaCaducidadDesdeBuscar() {
		return fechaCaducidadDesdeBuscar;
	}

	public void setFechaCaducidadDesdeBuscar( Date fechaCaducidadDesdeBuscar ) {
		this.fechaCaducidadDesdeBuscar = fechaCaducidadDesdeBuscar;
		fechaCaducidadDesdeBuscarString = UtilDate.convertirDateFechaToString( this.fechaCaducidadDesdeBuscar );
	}

	public String getFechaCaducidadDesdeBuscarString() {
		return fechaCaducidadDesdeBuscarString;
	}

	public void setFechaCaducidadDesdeBuscarString( String fechaCaducidadDesdeBuscarString ) {
		this.fechaCaducidadDesdeBuscarString = fechaCaducidadDesdeBuscarString;
	}

	public Date getFechaCaducidadHastaBuscar() {
		return fechaCaducidadHastaBuscar;
	}

	public void setFechaCaducidadHastaBuscar( Date fechaCaducidadHastaBuscar ) {
		this.fechaCaducidadHastaBuscar = fechaCaducidadHastaBuscar;
		fechaCaducidadHastaBuscarString = UtilDate.convertirDateFechaToString( this.fechaCaducidadHastaBuscar );
	}

	public String getFechaCaducidadHastaBuscarString() {
		return fechaCaducidadHastaBuscarString;
	}

	public void setFechaCaducidadHastaBuscarString( String fechaCaducidadHastaBuscarString ) {
		this.fechaCaducidadHastaBuscarString = fechaCaducidadHastaBuscarString;
	}
}
