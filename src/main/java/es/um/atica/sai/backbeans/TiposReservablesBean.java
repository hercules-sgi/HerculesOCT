package es.um.atica.sai.backbeans;

import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

import es.um.atica.sai.dtos.TipoPerfil;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.TipoReservable;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.ReservableService;
import es.um.atica.sai.services.interfaces.ServicioService;

@Name( value = TiposReservablesBean.NAME )
@Scope( ScopeType.CONVERSATION )
public class TiposReservablesBean {

    public static final String NAME = "tiposReservables";

    @Logger
    Log log;

    @In(value = ServicioService.NAME, create = true, required = true)
    ServicioService servicioService;
    
    @In(value = ReservableService.NAME, create = true, required = true)
    ReservableService reservableService;

    @In(create=true)
    private SaiIdentity identity;
    
    @In(create=true) 
    protected FacesMessages facesMessages;

    private List<Servicio> listaServicios;
    private String descripcionBuscar;
    private Servicio servicioBuscar;
    private String estadoBuscar;
    private List<TipoReservable> listaTiposReservables;
    
	public String getDescripcionBuscar() {
		return descripcionBuscar;
	}
	
	public void setDescripcionBuscar( String descripcionBuscar ) {
		this.descripcionBuscar = descripcionBuscar;
	}
	
	public List<Servicio> getListaServicios() {
		return listaServicios;
	}
	
	public void setListaServicios( List<Servicio> listaServicios ) {
		this.listaServicios = listaServicios;
	}
	
	public Servicio getServicioBuscar() {
		return servicioBuscar;
	}
	
	public void setServicioBuscar( Servicio servicioBuscar ) {
		this.servicioBuscar = servicioBuscar;
	}

	public String getEstadoBuscar() {
		return estadoBuscar;
	}
	
	public void setEstadoBuscar( String estadoBuscar ) {
		this.estadoBuscar = estadoBuscar;
	}
	
	public List<TipoReservable> getListaTiposReservables() {
		return listaTiposReservables;
	}
	
	public void setListaTiposReservables( List<TipoReservable> listaTiposReservables ) {
		this.listaTiposReservables = listaTiposReservables;
	}

	@Create
    public void inicializar()
	{
		this.listaServicios = identity.getServiciosPerfil(TipoPerfil.SUPERVISOR);
		if (this.listaServicios.size() == 1)
		{
			this.servicioBuscar = this.listaServicios.get(0);
		}
		this.estadoBuscar = "ALTA";
		this.buscar();
    }
    
    public void buscar() 
    {
    	this.listaTiposReservables = reservableService.buscarTiposReservables();
    }
    
    public void limpiar(){
        this.descripcionBuscar = null;
		if (this.listaServicios.size() > 1)
		{
			this.servicioBuscar = null;
		}
        this.estadoBuscar = "ALTA";
        this.buscar();
    }

}
