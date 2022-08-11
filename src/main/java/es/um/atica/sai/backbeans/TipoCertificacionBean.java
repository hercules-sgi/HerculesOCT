package es.um.atica.sai.backbeans;

import java.io.Serializable;
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

import es.um.atica.sai.entities.TipoCertificacion;
import es.um.atica.sai.exceptions.SaiException;
import es.um.atica.sai.services.interfaces.CertificacionesService;

@Name( TipoCertificacionBean.NAME )
@Scope( ScopeType.CONVERSATION )
public class TipoCertificacionBean implements Serializable {

	private static final long serialVersionUID = -2218456626625936245L;

	public static final String NAME = "tipoCertificacionBean";

	@Logger
	private transient Log logger;

	@In( create = true )
	private transient CertificacionesService certificacionesService;

	@In
	protected FacesMessages facesMessages;

	private List<TipoCertificacion> listaTipoCertificaciones;
	private TipoCertificacion tipoCertificacionEdit = new TipoCertificacion();

	@Create
	public void inicializar() {
		listaTipoCertificaciones = certificacionesService.getListaTiposCertificaciones();
	}

	public void establecerTipoCreate() {
		tipoCertificacionEdit = new TipoCertificacion();
	}

	public void establecerTipoEdit( TipoCertificacion t ) {
		tipoCertificacionEdit = t;
	}

	public void guardarTipoEdit() 
	{
		try 
		{
			certificacionesService.guardarTipoCertificacion( tipoCertificacionEdit );
			if ( !listaTipoCertificaciones.contains( tipoCertificacionEdit ) ) 
			{
				listaTipoCertificaciones.add( tipoCertificacionEdit );
			}
			facesMessages.add( StatusMessage.Severity.INFO, "tipoCertificacion.exito.guardar", null, null, tipoCertificacionEdit.getNombre());
		} 
		catch ( final SaiException ex ) 
		{
			logger.error( "Error al guardar tipo de certificacion", ex.getMessage() );
			facesMessages.add( StatusMessage.Severity.ERROR, "tipoCertificacion.error.guardar", null, null, ex.getMessage());
		}
	}

	public void eliminarTipoCertificacion( TipoCertificacion t ) 
	{
		String nombre = t.getNombre();
		try 
		{
			certificacionesService.eliminarTipoCertificacion( t );
			listaTipoCertificaciones.remove( t );
			facesMessages.add( StatusMessage.Severity.INFO, "tipoCertificacion.exito.eliminar", null, null, nombre);
		} 
		catch ( final SaiException ex ) 
		{
			logger.error( "Error al eliminar tipo de certificacion", ex.getMessage() );
			facesMessages.add( StatusMessage.Severity.ERROR, "tipoCertificacion.error.eliminar", null, null, ex.getMessage());
		}
	}

	public List<TipoCertificacion> getListaTipoCertificaciones() {
		return listaTipoCertificaciones;
	}

	public void setListaTipoCertificaciones( List<TipoCertificacion> listaTipoCertificaciones ) {
		this.listaTipoCertificaciones = listaTipoCertificaciones;
	}

	public TipoCertificacion getTipoCertificacionEdit() {
		return tipoCertificacionEdit;
	}

	public void setTipoCertificacionEdit( TipoCertificacion tipoCertificacionEdit ) {
		this.tipoCertificacionEdit = tipoCertificacionEdit;
	}
}
