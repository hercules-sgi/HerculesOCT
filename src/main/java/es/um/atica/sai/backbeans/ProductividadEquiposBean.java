package es.um.atica.sai.backbeans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

import es.um.atica.commons.utils.UtilDate;
import es.um.atica.sai.dtos.EquipoProductividad;
import es.um.atica.sai.dtos.TipoPerfil;
import es.um.atica.sai.entities.Equipo;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.ReservableService;
import es.um.atica.sai.utils.Utilidades;

@Name( ProductividadEquiposBean.NAME )
@Scope( ScopeType.CONVERSATION )
public class ProductividadEquiposBean {

	public static final String NAME = "productividadEquipos";

	@Logger
	private Log log;

	@In(create = true )
	private ReservableService reservableService;

	@In(create=true)
	protected FacesMessages facesMessages;

	@In(value="org.jboss.seam.security.identity", required=true)
	SaiIdentity identity;

	private List<Servicio> listaServicios;
	private List<Equipo> listaEquipos;
	private List<EquipoProductividad> informe;
	private boolean busquedaRealizada;
	
	// Parámetros de búsqueda
	private Servicio servicioBuscar;
	private Equipo[] arrayEquiposBuscar;
	private Date fechaDesdeBuscar;
	private Date fechaHastaBuscar;
	private String fechaDesdeBuscarString;
	private String fechaHastaBuscarString;
	

	public List<Servicio> getListaServicios() {
		return listaServicios;
	}

	public void setListaServicios( List<Servicio> listaServicios ) {
		this.listaServicios = listaServicios;
	}
	
	public List<Equipo> getListaEquipos() {
		return listaEquipos;
	}
	
	public List<EquipoProductividad> getInforme() {
		return informe;
	}
	
	public void setInforme( List<EquipoProductividad> informe ) {
		this.informe = informe;
	}

	public void setListaEquipos( List<Equipo> listaEquipos ) {
		this.listaEquipos = listaEquipos;
	}
	
	public Servicio getServicioBuscar() {
		return servicioBuscar;
	}
	
	public void setServicioBuscar( Servicio servicioBuscar ) {
		this.servicioBuscar = servicioBuscar;
	}
	
	public Equipo[] getArrayEquiposBuscar() {
		return arrayEquiposBuscar;
	}
	
	public void setArrayEquiposBuscar( Equipo[] arrayEquiposBuscar ) {
		this.arrayEquiposBuscar = arrayEquiposBuscar;
	}
	
	public Date getFechaDesdeBuscar() {
		return fechaDesdeBuscar;
	}
	
	public void setFechaDesdeBuscar( Date fechaDesdeBuscar ) {
		this.fechaDesdeBuscar = fechaDesdeBuscar;
		this.fechaDesdeBuscarString = UtilDate.dateToString(fechaDesdeBuscar, "dd/MM/yyyy");
	}
	
	public Date getFechaHastaBuscar() {
		return fechaHastaBuscar;
	}
	
	public void setFechaHastaBuscar( Date fechaHastaBuscar ) {
		this.fechaHastaBuscar = fechaHastaBuscar;
		// A la fecha hasta le sumamos un día para que se incluyan los consumos del último día
		this.fechaHastaBuscarString = UtilDate.dateToString(fechaHastaBuscar, "dd/MM/yyyy");
	}
	
	public String getFechaDesdeBuscarString() {
		return fechaDesdeBuscarString;
	}
	
	public void setFechaDesdeBuscarString( String fechaDesdeBuscarString ) {
		this.fechaDesdeBuscarString = fechaDesdeBuscarString;
	}
	
	public String getFechaHastaBuscarString() {
		return fechaHastaBuscarString;
	}
	
	public void setFechaHastaBuscarString( String fechaHastaBuscarString ) {
		this.fechaHastaBuscarString = fechaHastaBuscarString;
	}

	public boolean isBusquedaRealizada() {
		return busquedaRealizada;
	}
	
	public void setBusquedaRealizada( boolean busquedaRealizada ) {
		this.busquedaRealizada = busquedaRealizada;
	}

	
	@Create
	public void inicializa() 
	{
		this.listaServicios = identity.getServiciosPerfil(TipoPerfil.SUPERVISOR);
		if (listaServicios.size() == 1)
		{
			this.servicioBuscar = this.listaServicios.get(0);
			this.seleccionadoServicio();
		}
		this.busquedaRealizada = false;
	}

	public void seleccionadoServicio()
	{
		if (this.servicioBuscar != null)
		{
			this.listaEquipos = reservableService.getEquiposByServicio(this.servicioBuscar);
		}
		else
		{
			this.listaEquipos = new ArrayList<>();
		}
		this.arrayEquiposBuscar = new Equipo[0];
	}
	
	public void obtenerInforme()
	{
		this.informe = reservableService.getInformeProductividad(this.servicioBuscar, Arrays.asList(arrayEquiposBuscar), fechaDesdeBuscarString, fechaHastaBuscarString);
		this.busquedaRealizada = true;
	}
	
	public void limpiar() 
	{
		if (this.listaServicios.size() > 1)
		{
			this.servicioBuscar = null;
		}
		this.arrayEquiposBuscar = new Equipo[0];
		this.setFechaDesdeBuscar(null);
		this.setFechaHastaBuscar(null);
		this.informe = null;
		this.busquedaRealizada = false;
	}

	public String mostrarDosDec(BigDecimal importe)
	{
		return Utilidades.formatCantidadDosDecimales(importe);
	}

}
