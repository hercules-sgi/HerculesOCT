package es.um.atica.sai.backbeans;

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

import es.um.atica.sai.entities.TipoTarifa;
import es.um.atica.sai.services.interfaces.TarifaService;

@Name("tipoTarifa")
@Scope(ScopeType.CONVERSATION)
public class TipoTarifaBean {

    private static final String RETORNO_LISTADO_TIPOTARIFAS = "tipoTarifaEditOk";

    @Logger
    private Log log;
    
	@In( create = true )
	TarifaService tarifaService;

    @In(create=true) 
    protected FacesMessages facesMessages;
	
    private List<TipoTarifa> listaTiposTarifa;
    private TipoTarifa tipoTarifaEdit;

    
	public List<TipoTarifa> getListaTiposTarifa() {
		return listaTiposTarifa;
	}

	
	public void setListaTiposTarifa( List<TipoTarifa> listaTiposTarifa ) {
		this.listaTiposTarifa = listaTiposTarifa;
	}

	
	public TipoTarifa getTipoTarifaEdit() {
		return tipoTarifaEdit;
	}

	
	public void setTipoTarifaEdit( TipoTarifa tipoTarifaEdit ) {
		this.tipoTarifaEdit = tipoTarifaEdit;
	}

	@Create
    public void inicializa()
    {
        log.info("Obteniendo listado de tipos de tarifas...");
        this.listaTiposTarifa = tarifaService.getTiposTarifas();
    }
    
    public String establecerTipoTarifaEdit(TipoTarifa tt)
    {
        this.tipoTarifaEdit = tt;
        return "tipoTarifaEdit";
    }

	public String establecerTipoTarifaCreate() {
		this.tipoTarifaEdit = new TipoTarifa();
		return "tipoTarifaEdit";
	}

	public String crearTipoTarifa() 
	{
		try 
		{
			tarifaService.crearTipoTarifa( this.tipoTarifaEdit );
		} 
		catch ( Exception ex ) 
		{
    		this.facesMessages.add(StatusMessage.Severity.ERROR, 
		   			  				"tipotarifa.guardar.error",
		   			  				null, null,
		   			  				ex.getCause().getMessage()); 
			return null;
		}
		this.facesMessages.add(StatusMessage.Severity.INFO, 
	  				"tipotarifa.guardar.ok",
	  				null, null,
	  				this.tipoTarifaEdit.getDescripcion()); 
		this.listaTiposTarifa = tarifaService.getTiposTarifas();
		return RETORNO_LISTADO_TIPOTARIFAS;
	}

	public String modificarTipoTarifa() 
	{
		try 
		{
			tarifaService.modificarTipoTarifa( this.tipoTarifaEdit );
		} 
		catch ( Exception ex ) 
		{
    		this.facesMessages.add(StatusMessage.Severity.ERROR, 
		  				"tipotarifa.guardar.error",
		  				null, null,
		  				ex.getCause().getMessage()); 
			return null;
		}
		this.facesMessages.add(StatusMessage.Severity.INFO, 
  				"tipotarifa.guardar.ok",
  				null, null,
  				this.tipoTarifaEdit.getDescripcion()); 
		this.listaTiposTarifa = tarifaService.getTiposTarifas();
		return RETORNO_LISTADO_TIPOTARIFAS;	
	}

	public void eliminarTipoTarifa(TipoTarifa tt)
	{
		String descripcion = tt.getDescripcion();
		try 
		{
			tarifaService.eliminarTipoTarifa(tt);
		} 
		catch ( Exception ex ) 
		{
    		this.facesMessages.add(StatusMessage.Severity.ERROR, 
	  				"tipotarifa.eliminar.error",
	  				null, null,
	  				ex.getCause().getMessage()); 
			return;
		}
		this.facesMessages.add(StatusMessage.Severity.INFO, 
  				"tipotarifa.eliminar.ok",
  				null, null,
  				descripcion); 
		this.listaTiposTarifa = tarifaService.getTiposTarifas();
	}
	
	public String cancelarEdicionTipoTarifa() 
	{
		return RETORNO_LISTADO_TIPOTARIFAS;
	}
}
