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

import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.ServicioService;
import es.um.atica.sai.services.interfaces.UsuarioService;

@Name("home")
@Scope(ScopeType.CONVERSATION)
public class HomeBean {
    @Logger
    private Log log;        
        
    @In("org.jboss.seam.security.identity")
    SaiIdentity identity;
    
    @In(create=true) 
    protected FacesMessages facesMessages;
            
    @In(value = ServicioService.NAME, required = true, create = true )    
    private ServicioService servicioService;
    
    @In(value = UsuarioService.NAME, required = true, create = true )    
    private UsuarioService usuarioService;
    
    private List<Servicio> servicios;
    
	public List<Servicio> getServicios() {
		return servicios;
	}
	
	public void setServicios( List<Servicio> servicios ) {
		this.servicios = servicios;
	}  
    
    @Create
    public void inicializar() 
    {
    	this.servicios = servicioService.getServiciosConEmail();                       
    }
    
    public void aceptarCondiciones() {
    	try 
    	{
    		usuarioService.aceptaCondicionesActi();
    		identity.cargarRoles(identity.getUsuarioConectado());
    		this.facesMessages.add(StatusMessage.Severity.INFO, 
								   "home.condicionesaceptadas",
								   "home.condicionesaceptadas.detalles",	
								   null, null); 
    	}
    	catch(Exception ex) 
    	{
    		log.error( "Error al aceptar las condiciones de un usuario: #0", ex.getMessage() );
    		this.facesMessages.add(StatusMessage.Severity.WARN, 
								   "home.condicionesaceptadas",
								   "home.condicionesaceptadas.detalles",	
								   null, null); 
    	}		
    }
    
        
}
