package es.um.atica.sai.backbeans;

import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.jboss.seam.log.Log;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.LazyDataModel;

import es.um.atica.commons.utils.UtilDate;
import es.um.atica.sai.backbeans.lazydatamodel.ReservableLazyDataModel;
import es.um.atica.sai.dtos.TipoPerfil;
import es.um.atica.sai.entities.Equipo;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.TipoReservable;
import es.um.atica.sai.exceptions.SaiException;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.ProductoService;
import es.um.atica.sai.services.interfaces.ReservableService;

@Name( EquiposBean.NAME )
@Scope( ScopeType.CONVERSATION )
public class EquiposBean {

	public static final String NAME = "equipos";

	@Logger
	private Log log;

	@In(create = true )
	private ReservableService reservableService;

	@In(create = true )
	private ProductoService productoService;
	
	@In(create=true)
	protected FacesMessages facesMessages;

	@In(value="org.jboss.seam.security.identity", required=true)
	SaiIdentity identity;

	private String descripcionBuscar;
	private Long codTipo;
	private Servicio servicioBuscar;
	private String estadoBuscar;
	private Date fechaCompraDesdeBuscar;
	private Date fechaCompraHastaBuscar;
	private String fechaCompraDesdeString;
	private String fechaCompraHastaString;
	private List<Servicio> listaServicios;
	private List<TipoReservable> listaTiposReservable;
	private LazyDataModel<Equipo> lazyDataModel;

	private int first;
	private int pageSize;
	
	public String getDescripcionBuscar() {
		return descripcionBuscar;
	}

	public void setDescripcionBuscar( String descripcionBuscar ) {
		this.descripcionBuscar = descripcionBuscar;
	}

	public Long getCodTipo() {
		return codTipo;
	}

	public void setCodTipo( Long codTipo ) {
		this.codTipo = codTipo;
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

	public Date getFechaCompraDesdeBuscar() {
		return fechaCompraDesdeBuscar;
	}

	public void setFechaCompraDesdeBuscar( Date fechaCompraDesdeBuscar ) {
		this.fechaCompraDesdeBuscar = fechaCompraDesdeBuscar;
		this.fechaCompraDesdeString = UtilDate.dateToString(fechaCompraDesdeBuscar,"dd/MM/yyyy");
	}

	public Date getFechaCompraHastaBuscar() {
		return fechaCompraHastaBuscar;
	}

	public void setFechaCompraHastaBuscar( Date fechaCompraHastaBuscar ) {
		this.fechaCompraHastaBuscar = fechaCompraHastaBuscar;
		this.fechaCompraHastaString = UtilDate.dateToString(fechaCompraHastaBuscar,"dd/MM/yyyy");
	}

	public List<Servicio> getListaServicios() {
		return listaServicios;
	}

	public void setListaServicios( List<Servicio> listaServicios ) {
		this.listaServicios = listaServicios;
	}

	public String getFechaCompraDesdeString() {
		return fechaCompraDesdeString;
	}

	public void setFechaCompraDesdeString( String fechaCompraDesdeString ) {
		this.fechaCompraDesdeString = fechaCompraDesdeString;
	}

	public String getFechaCompraHastaString() {
		return fechaCompraHastaString;
	}

	public void setFechaCompraHastaString( String fechaCompraHastaString ) {
		this.fechaCompraHastaString = fechaCompraHastaString;
	}

	public List<TipoReservable> getListaTiposReservable() {
		return listaTiposReservable;
	}
	
	public void setListaTiposReservable( List<TipoReservable> listaTiposReservable ) {
		this.listaTiposReservable = listaTiposReservable;
	}

	public LazyDataModel<Equipo> getLazyDataModel() {
		return lazyDataModel;
	}

	public void setLazyDataModel( LazyDataModel<Equipo> lazyDataModel ) {
		this.lazyDataModel = lazyDataModel;
	}

	public int getFirst() {
		return first;
	}
	
	public void setFirst( int first ) {
		this.first = first;
	}
	
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize( int pageSize ) {
		this.pageSize = pageSize;
	}

	@Create
	public void inicializa() {
		listaServicios = identity.getServiciosPerfil(TipoPerfil.SUPERVISOR);
		estadoBuscar = "ALTA";
		if (listaServicios.size() == 1)
		{
			servicioBuscar = listaServicios.get(0);
		}
		listaTiposReservable = reservableService.getListaTiposReservableByUsuarioConectado();
		codTipo = ( long ) -1;
		this.pageSize = 25;
		buscarEquipos();
	}

	public void seleccionadoServicio()
	{
		if (this.servicioBuscar == null)
		{
			this.listaTiposReservable = reservableService.getListaTiposReservableByUsuarioConectado();
		}
		else
		{
			this.listaTiposReservable = productoService.getListaTiposReservableByServicio(this.servicioBuscar);
		}
		codTipo = ( long ) -1;
	}
	
	public void buscarEquipos() {
    	DataTable tablaListaProductos = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formEquipos:tablaEquipos");
    	if (tablaListaProductos != null)
    	{
    		tablaListaProductos.setFirst(0);
    	}
		lazyDataModel = new ReservableLazyDataModel( codTipo );
	}

	public void limpiarBusqueda() {
		codTipo = ( long ) -1;
		descripcionBuscar = null;
		if (listaServicios.size() > 1)
		{
			servicioBuscar = null;
		}
		lazyDataModel = null;
		estadoBuscar = "ALTA";
		setFechaCompraDesdeBuscar(null);
		setFechaCompraHastaBuscar(null);
		buscarEquipos();
	}

	public void eliminarEquipo( Equipo e ) {
		try {
			reservableService.eliminarEquipo( e );
			facesMessages.add( StatusMessage.Severity.INFO, "equipos.eliminar.ok", null, null, e.getDescripcion() );
		} catch ( final SaiException ex ) {
			facesMessages.add( StatusMessage.Severity.ERROR, "equipos.eliminar.error", null, null, ex.getMessage() );
		}

	}

	public String getNombreDependencia( Equipo e ) {
		if ( e.getCodigoDep() != null ) {
			return reservableService.getNombreDependencia( e.getTipoDep(), e.getCodigoDep(),
					e.getBloqueDep(),
					e.getPlantaDep(), e.getNumDep() );
		}
		return "";
	}
}
