package es.um.atica.sai.backbeans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.SQLException;
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
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import es.um.atica.sai.entities.Certificacion;
import es.um.atica.sai.entities.TipoCertificacion;
import es.um.atica.sai.exceptions.SaiException;
import es.um.atica.sai.services.interfaces.CertificacionesService;
import es.um.atica.sai.services.interfaces.UsuarioService;

@Name( CertificacionEditBean.NAME )
@Scope( ScopeType.CONVERSATION )
public class CertificacionEditBean implements Serializable {


	/**
	 *
	 */
	private static final long serialVersionUID = -4648498140327786566L;

	public static final String NAME = "certificacionEditBean";

	@Logger
	private transient Log logger;

	@In( create = true )
	private transient CertificacionesService certificacionesService;

	@In( create = true )
	private transient UsuarioService usuarioService;

	@In
	protected FacesMessages facesMessages;

	private static final String EDITAR = "editar";

	private Certificacion certificacionEdit;
	private List<TipoCertificacion> listaTipoCertificaciones;

	@Create
	public void inicializar() {
		listaTipoCertificaciones = certificacionesService.getListaTiposCertificaciones();
	}

	public String establecerCertificacion( Certificacion c ) {
		certificacionEdit = c;
		return EDITAR;
	}

	public void validarCertificacion() {
		certificacionEdit.setFechaValidaDeniega( new Date() );
		certificacionEdit.setEstado( "ACTIVA" );
		certificacionEdit.setMotivoDenegacion( null );
		try {
			certificacionesService.modificarCertificacion( certificacionEdit );
			facesMessages.add( StatusMessage.Severity.INFO, "certificacion.exito.activar", null, null,
					certificacionEdit.getUsuario().getFullName() );
		} catch ( final SaiException ex ) {
			logger.error( "Error al activar la certificacion", ex.getMessage() );
			facesMessages.add( StatusMessage.Severity.ERROR, "certificacion.error.activar", null, null,
					ex.getMessage() );
		}
	}

	public void denegarCertificacion() {
		certificacionEdit.setFechaValidaDeniega( new Date() );
		certificacionEdit.setEstado( "DENEGADA" );
		try {
			certificacionesService.modificarCertificacion( certificacionEdit );
			facesMessages.add( StatusMessage.Severity.INFO, "certificacion.exito.denegar", null, null,
					certificacionEdit.getUsuario().getFullName() );
		} catch ( final SaiException ex ) {
			logger.error( "Error al denegar la certificacion", ex.getMessage() );
			facesMessages.add( StatusMessage.Severity.ERROR, "certificacion.error.denegar", null, null,
					ex.getMessage() );
		}
	}

	public void guardarCertificacion() {
		try {
			certificacionesService.modificarCertificacion( certificacionEdit );
			facesMessages.add( StatusMessage.Severity.INFO, "certificacion.exito.guardar", null, null,
					certificacionEdit.getUsuario().getFullName() );
		} catch ( final SaiException ex ) {
			logger.error( "Error al guardar la certificacion", ex.getMessage() );
			facesMessages.add( StatusMessage.Severity.ERROR, "certificacion.error.guardar", null, null,
					ex.getMessage() );
		}
	}

	public StreamedContent getFichero() {
		try {
			final InputStream is = new ByteArrayInputStream( certificacionEdit.getFicheroBytes() );
			return new DefaultStreamedContent( is );
		} catch ( final SQLException ex ) {
			logger.error( "error al leer el fichero" );
		}
		return new DefaultStreamedContent();
	}

	public StreamedContent descargarFicheroCertificacion() {
		try {
			final InputStream stream = new ByteArrayInputStream( certificacionEdit.getFicheroBytes() );
			return new DefaultStreamedContent( stream, "IN", certificacionEdit.getFicheroName() );
		} catch ( final Exception ex ) {
			facesMessages.add( StatusMessage.Severity.ERROR, "certificacion.descargar.error", null, null,
					ex.getMessage() );
			return null;
		}
	}

	public String getDescripcionEstado()
	{
		return certificacionesService.getDescripcionEstadoCertificacion(this.certificacionEdit);
	}
	
	public String getColorEstado()
	{
		return certificacionesService.getColorEstadoCertificacion(this.getDescripcionEstado());
	}
	
	public Certificacion getCertificacionEdit() {
		return certificacionEdit;
	}

	public void setCertificacionEdit( Certificacion certificacionEdit ) {
		this.certificacionEdit = certificacionEdit;
	}

	public List<TipoCertificacion> getListaTipoCertificaciones() {
		return listaTipoCertificaciones;
	}

	public void setListaTipoCertificaciones( List<TipoCertificacion> listaTipoCertificaciones ) {
		this.listaTipoCertificaciones = listaTipoCertificaciones;
	}
}
