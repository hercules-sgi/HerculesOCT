package es.um.atica.sai.backbeans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import es.um.atica.sai.dtos.TipoPerfil;
import es.um.atica.sai.entities.Certificacion;
import es.um.atica.sai.entities.TipoCertificacion;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.CertificacionesService;

@Name( CertificacionSolicitudBean.NAME )
@Scope( ScopeType.CONVERSATION )
public class CertificacionSolicitudBean {

	public static final String NAME = "certificacionSolicitud";

	@Logger
	private Log log;

	@In(create = true )
	private CertificacionesService certificacionesService;

	@In(create=true)
	protected FacesMessages facesMessages;


	@In(value="org.jboss.seam.security.identity", required=true)
	SaiIdentity identity;

	private static final String KEY_MSJ_CERTIFICACION_CREAR_ERROR = "certificacion.crear.error";
	
	private List<Usuario> listaUsuarios;
	private Usuario usuarioFiltrar;
	private List<Certificacion> listaCertificaciones;
	private List<TipoCertificacion> listaTiposCertificacion;
	private Certificacion certificacionAdd;
	private UploadedFile ficheroCertificacion;
	
	public List<Usuario> getListaUsuarios() {
		return listaUsuarios;
	}
	
	public void setListaUsuarios( List<Usuario> listaUsuarios ) {
		this.listaUsuarios = listaUsuarios;
	}
	
	public Usuario getUsuarioFiltrar() {
		return usuarioFiltrar;
	}
	
	public void setUsuarioFiltrar( Usuario usuarioFiltrar ) {
		this.usuarioFiltrar = usuarioFiltrar;
	}

	public List<Certificacion> getListaCertificaciones() {
		return listaCertificaciones;
	}
	
	public void setListaCertificaciones( List<Certificacion> listaCertificaciones ) {
		this.listaCertificaciones = listaCertificaciones;
	}
	
	public List<TipoCertificacion> getListaTiposCertificacion() {
		return listaTiposCertificacion;
	}
	
	public void setListaTiposCertificacion( List<TipoCertificacion> listaTiposCertificacion ) {
		this.listaTiposCertificacion = listaTiposCertificacion;
	}

	public Certificacion getCertificacionAdd() {
		return this.certificacionAdd;
	}
	
	public void setCertificacionAdd( Certificacion certificacionAdd ) {
		this.certificacionAdd = certificacionAdd;
	}

	public UploadedFile getFicheroCertificacion() {
		return ficheroCertificacion;
	}
	
	public void setFicheroCertificacion( UploadedFile ficheroCertificacion ) {
		this.ficheroCertificacion = ficheroCertificacion;
	}

	
	@Create
	public void inicializa()
	{
		if (identity.tienePerfil(TipoPerfil.SUPERVISOR))
		{
			this.listaUsuarios = certificacionesService.getListaUsuariosPuedoSolicitarCertificacion();
			this.listaCertificaciones = certificacionesService.busquedaCertificacionesByListaUsuarios(this.listaUsuarios);
		}
		else
		{
			this.listaCertificaciones = certificacionesService.getListaCertificacionesByUsuario(identity.getUsuarioConectado());
		}
		this.listaTiposCertificacion = certificacionesService.getListaTiposCertificaciones();
	}

	public void filtrar()
	{
		if (identity.tienePerfil(TipoPerfil.SUPERVISOR))
		{
			this.listaCertificaciones = certificacionesService.busquedaCertificacionesByListaUsuarios(this.listaUsuarios);
		}
		else
		{
			this.listaCertificaciones = certificacionesService.getListaCertificacionesByUsuario(identity.getUsuarioConectado());
		}
	}
	
	public void limpiar()
	{
		this.usuarioFiltrar = null;
		this.filtrar();
	}
	
	public void establecerCertificacionCreate()
	{
		this.certificacionAdd = new Certificacion();
		this.ficheroCertificacion = null;
		if (!identity.tienePerfil(TipoPerfil.SUPERVISOR))
		{
			this.certificacionAdd.setUsuario(identity.getUsuarioConectado());
		}
	}

	public void crearSolicitud()
	{
		if (this.ficheroCertificacion == null)
		{
			facesMessages.add(StatusMessage.Severity.WARN, KEY_MSJ_CERTIFICACION_CREAR_ERROR, "certificacion.crear.error.sinfichero", null, null);
			return;
		}
		if (certificacionesService.existeCertificacionPendienteByTipoAndUsuario(this.certificacionAdd.getTipoCertificacion(), this.certificacionAdd.getUsuario()))
		{
			facesMessages.add(StatusMessage.Severity.WARN, KEY_MSJ_CERTIFICACION_CREAR_ERROR, "certificacion.crear.error.yaexiste", null, null, this.certificacionAdd.getUsuario().getDatosUsuario().getNombreCompleto(), this.certificacionAdd.getTipoCertificacion().getNombre());
			return;
		}
		try
		{
			certificacionesService.crearCertificacion(this.certificacionAdd, this.ficheroCertificacion);
		}
		catch (Exception ex)
		{
			facesMessages.add(StatusMessage.Severity.ERROR, KEY_MSJ_CERTIFICACION_CREAR_ERROR, null, null, ex.getMessage());
			return;
		}
		this.filtrar();
		facesMessages.add(StatusMessage.Severity.INFO, "certificacion.crear.ok", null, null, this.certificacionAdd.getTipoCertificacion().getNombre());
	}
	
	public void eliminarSolicitud(Certificacion cert)
	{
		try
		{
			certificacionesService.eliminarCertificacion(cert);
		}
		catch (Exception ex)
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "certificacion.eliminar.error", null, null, ex.getMessage());
			return;
		}
		this.listaCertificaciones.remove(cert);
		facesMessages.add(StatusMessage.Severity.INFO, "certificacion.eliminar.ok", null, null, cert.getTipoCertificacion().getNombre());
	}

	public void subidoFicheroCertificacion(FileUploadEvent event) 
	{
		this.ficheroCertificacion = event.getFile();
	}
	
	public StreamedContent descargarFicheroCertificacion(Certificacion cert) 
	{
		try
		{
			final InputStream stream = new ByteArrayInputStream(cert.getFicheroBytes());
			return new DefaultStreamedContent(stream, null, cert.getFicheroName());
		}
		catch (Exception ex)
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "certificacion.descargar.error", null, null, ex.getMessage());
			return null;
		}
	}
	
	public String getDescripcionEstado(Certificacion cert)
	{
		return certificacionesService.getDescripcionEstadoCertificacion(cert);
	}
	
	public String getColorEstado(Certificacion cert)
	{
		return certificacionesService.getColorEstadoCertificacion(this.getDescripcionEstado(cert));
	}
	
}
