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

import es.um.atica.sai.dtos.TipoPerfil;
import es.um.atica.sai.entities.Nivel1;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.FungibleService;
import es.um.atica.sai.services.interfaces.ProductoService;
import es.um.atica.sai.services.interfaces.ServicioService;

@Name("fungiblesBean")
@Scope(ScopeType.CONVERSATION)
public class FungiblesBean{

    private static final String RETORNO_LISTADO_FUNGIBLES = "fungibleEditOk";
    private static final String EDICION_FUNGIBLE = "fungibleEdit";
    
    @Logger
    private Log log;

    @In(create=true)
    FungibleService fungibleService;
    
    @In(create=true)
    ServicioService servicioService;
    
    @In(create=true)
    ProductoService productoService;

    @In(create=true) 
    protected FacesMessages facesMessages;

    @In(create=true)
    SaiIdentity identity;
    
    private String nombreBuscar;
    private Servicio servicioBuscar;
    private List<Servicio> listaServicios;
    private List<Nivel1> listaFungibles;
    private Nivel1 fungibleEdit;
    
    
	public Log getLog() {
		return log;
	}

	
	public void setLog( Log log ) {
		this.log = log;
	}

	
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	
	public void setNombreBuscar( String nombreBuscar ) {
		this.nombreBuscar = nombreBuscar;
	}

	
	public Servicio getServicioBuscar() {
		return servicioBuscar;
	}

	
	public void setServicioBuscar( Servicio servicioBuscar ) {
		this.servicioBuscar = servicioBuscar;
	}

	
	public List<Servicio> getListaServicios() {
		return listaServicios;
	}

	
	public void setListaServicios( List<Servicio> listaServicios ) {
		this.listaServicios = listaServicios;
	}

	
	public List<Nivel1> getListaFungibles() {
		return listaFungibles;
	}

	
	public void setListaFungibles( List<Nivel1> listaFungibles ) {
		this.listaFungibles = listaFungibles;
	}

	
	public Nivel1 getFungibleEdit() {
		return fungibleEdit;
	}

	
	public void setFungibleEdit( Nivel1 fungibleEdit ) {
		this.fungibleEdit = fungibleEdit;
	}

	@Create
    public void inicializa(){
    	this.listaServicios = identity.getServiciosPerfil(TipoPerfil.SUPERVISOR);
    	if (this.listaServicios.size() == 1)
    	{
    		this.servicioBuscar = this.listaServicios.get(0);
    	}
		this.buscarFungibles();
    }
    
    public void buscarFungibles()
    {
    	this.listaFungibles=fungibleService.buscarFungiblesBySupervisor(identity.getUsuarioConectado().getCod());
    }
    
    public void limpiarBusqueda()
    {
    	this.nombreBuscar=null;
		if (this.listaServicios.size() > 1)
		{
			this.servicioBuscar=null;
		}
    	this.buscarFungibles();
    }
    
    public void eliminarFungible(Nivel1 fungible)
    {
    	String descripcion = fungible.getNombre();
    	try
    	{
    		fungibleService.eliminarFungible( fungible );
    		this.listaFungibles.remove(fungible);
    	}
    	catch(Exception e)
    	{
    		this.facesMessages.add(StatusMessage.Severity.ERROR, 
		  			   			  "fungible.eliminar.error",
		  			   			  null, null,
		  			   			  e.getCause().getMessage()); 
			return;
    	}
		this.facesMessages.add(StatusMessage.Severity.ERROR, 
	   			  			  "fungible.eliminar.ok",
	   			  			  null, null,
	   			  			  descripcion); 
    }
    
    
    public String establecerFungibleCreate()
    {
    	this.fungibleEdit = new Nivel1();
    	if (this.listaServicios.size() == 1)
    	{
    		this.fungibleEdit.setServicio(this.listaServicios.get(0));
    	}
    	return EDICION_FUNGIBLE;
    }
    
    public String establecerFungibleEdit(Nivel1 fungible)
    {
    	this.fungibleEdit = fungible;
    	return EDICION_FUNGIBLE;		
    }
    
	public String cancelarEdicionFungible() 
	{
		return RETORNO_LISTADO_FUNGIBLES;
	}
	
	public String guardarFungible() 
	{
		try 
		{
			fungibleService.guardarFungible( this.fungibleEdit );
		} 
		catch ( Exception ex ) 
		{
    		this.facesMessages.add(StatusMessage.Severity.ERROR, 
		  			   "fungible.guardar.error",
		  			    null, null,  
		  			    ex.getCause().getMessage()); 
			return null;
		}
		this.facesMessages.add(StatusMessage.Severity.INFO, 
	  			   "fungible.guardar.ok",
	  			    null, null,  
	  			    this.fungibleEdit.getNombre()); 
		this.buscarFungibles();
		return RETORNO_LISTADO_FUNGIBLES;
	}
	
    public String getDescripcionTipoProducto(String tipo)
    {
    	return productoService.getDescripcionTipoProducto( tipo );
    }
    

}
