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
import es.um.atica.sai.entities.HorarioDia;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.TipoHorario;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.ReservableService;

@Name(value = TiposHorariosBean.NAME )
@Scope( ScopeType.CONVERSATION )
public class TiposHorariosBean {

	public static final String NAME = "tiposHorarios";

	@Logger
	private Log log;

	@In(create = true, required = true )
	private ReservableService reservableService;

	@In(create=true)
	protected FacesMessages facesMessages;

	@In(create=true)
	protected SaiIdentity identity;

	private List<Servicio> listaServicios;
	private Servicio servicioBuscar;
	private String descripcionBuscar;
	private List<TipoHorario> listaTiposHorario;

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
	
	public String getDescripcionBuscar() {
		return descripcionBuscar;
	}
	
	public void setDescripcionBuscar( String descripcionBuscar ) {
		this.descripcionBuscar = descripcionBuscar;
	}

	public List<TipoHorario> getListaTiposHorario() {
		return listaTiposHorario;
	}

	public void setListaTiposHorario( List<TipoHorario> listaTiposHorario ) {
		this.listaTiposHorario = listaTiposHorario;
	}

	@Create
	public void inicializa() 
	{
		this.listaServicios = identity.getServiciosPerfil(TipoPerfil.SUPERVISOR);
		if (this.listaServicios.size() == 1)
		{
			this.servicioBuscar = this.listaServicios.get(0);
		}
		this.buscar();
	}

	public void buscar()
	{
		this.listaTiposHorario = reservableService.buscarTiposHorarios();
	}
	
	public void limpiar()
	{
		if (this.listaServicios.size() > 1)
		{
			this.servicioBuscar = null;
		}
		this.descripcionBuscar = null;
		this.buscar();
	}
	
    public List<HorarioDia> getListaHorarioDiaByTipohorario(TipoHorario th)
    {
    	return reservableService.getListaHorarioDiaByTipohorario( th );
    }
	
    public String getDescripcionHorarioDia(HorarioDia hd)
    {
    	return reservableService.getDescripcionHorarioDia( hd );
    }

	public void eliminarTipoHorario(TipoHorario tipoHorario) 
	{
		try 
		{
			reservableService.eliminarTipoHorario( tipoHorario );
		}
		catch (final Exception ex) 
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "tipohorario.eliminar.error", null, null, ex.getMessage());
			return;
		}
		this.buscar();
		facesMessages.add(StatusMessage.Severity.INFO, "tipohorario.eliminar.ok", null, null, tipoHorario.getDescripcion());
	}
	
	public String volverFromEdicion() 
	{
		this.buscar();
		return NAME;
	}


}
