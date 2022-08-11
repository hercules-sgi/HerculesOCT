package es.um.atica.sai.backbeans;

import java.math.BigDecimal;
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
import es.um.atica.sai.entities.Proyecto;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.ProyectoService;
import es.um.atica.sai.utils.Utilidades;

@Name( ProyectosBean.NAME )
@Scope( ScopeType.CONVERSATION )
public class ProyectosBean {

	public static final String NAME = "proyectos";

	@Logger
	private Log log;

	@In(create = true )
	private ProyectoService proyectoService;

	@In(create=true)
	protected FacesMessages facesMessages;

	@In(create=true)
	SaiIdentity identity;

	private List<Servicio> listaServicios;
	private Servicio servicioBuscar;
	private Date fechaCaducidadDesdeBuscar;
	private String fechaCaducidadDesdeBuscarString;
	private List<Proyecto> listaProyectos;

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
	
	public Date getFechaCaducidadDesdeBuscar() {
		return fechaCaducidadDesdeBuscar;
	}

	public void setFechaCaducidadDesdeBuscar(Date fechaCaducidadDesdeBuscar) {
		this.fechaCaducidadDesdeBuscar = fechaCaducidadDesdeBuscar;
		this.fechaCaducidadDesdeBuscarString = UtilDate.dateToString(fechaCaducidadDesdeBuscar, "dd/MM/yyyy");
	}
	
	public String getFechaCaducidadDesdeBuscarString() {
		return fechaCaducidadDesdeBuscarString;
	}
	
	public void setFechaCaducidadDesdeBuscarString( String fechaCaducidadDesdeBuscarString ) {
		this.fechaCaducidadDesdeBuscarString = fechaCaducidadDesdeBuscarString;
	}

	public List<Proyecto> getListaProyectos() {
		return listaProyectos;
	}
	
	public void setListaProyectos( List<Proyecto> listaProyectos ) {
		this.listaProyectos = listaProyectos;
	}

	@Create
	public void inicializa()
	{
		this.listaServicios = identity.getServiciosUsuarioConectado();
		if (this.listaServicios.size() == 1)
		{
			this.servicioBuscar = this.listaServicios.get(0);
		}
		this.setFechaCaducidadDesdeBuscar(new Date());
		buscarProyectos();
	}


	public void buscarProyectos()
	{
		this.listaProyectos = proyectoService.buscarProyectos();
	}

	public void limpiarBusqueda()
	{
		this.servicioBuscar = null;
		if (this.listaServicios.size() == 1)
		{
			this.servicioBuscar = this.listaServicios.get(0);
		}
		this.setFechaCaducidadDesdeBuscar(new Date());
		buscarProyectos();
	}

	public void eliminarProyecto(Proyecto pro)
	{
		try
		{
			proyectoService.eliminarProyecto(pro);
		}
		catch ( final Exception ex )
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "proyecto.eliminar.error", null, null, ex.getMessage());
			return;
		}
		facesMessages.add(StatusMessage.Severity.INFO, "proyecto.eliminar.ok", null, null, pro.getNombre());
		buscarProyectos();
	}
	
	public BigDecimal getCantidadConsumidaByProyecto(Proyecto proyecto)
	{
		return proyectoService.getCantidadConsumidaByProyecto(proyecto);
	}

	public String formatCantidad( BigDecimal number ) 
	{
		return Utilidades.formatCantidad(number);	
	}
	
}
