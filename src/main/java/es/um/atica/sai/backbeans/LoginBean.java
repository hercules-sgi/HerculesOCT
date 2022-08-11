package es.um.atica.sai.backbeans;

import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.services.interfaces.ServicioService;

@Name("login")
@Scope(ScopeType.PAGE)
public class LoginBean {

	@Logger
	private Log log;
	
    private List<Servicio> servicios;
    
    @In(create = true, value = ServicioService.NAME)
    private ServicioService servicioService;
    
	public List<Servicio> getServicios() {
		return servicios;
	}
	
	public void setServicios( List<Servicio> servicios ) {
		this.servicios = servicios;
	}

	@Create
    public void onCreate() 
	{
		this.setServicios( servicioService.getServiciosConEmail());
    }
    
}
