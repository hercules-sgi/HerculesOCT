package es.um.atica.sai.backbeans;

import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.SeamResourceBundle;
import org.jboss.seam.log.Log;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.LazyDataModel;

import es.um.atica.commons.utils.UtilDate;
import es.um.atica.sai.backbeans.lazydatamodel.FacturaLazyDataModel;
import es.um.atica.sai.dtos.TipoPerfil;
import es.um.atica.sai.entities.EntidadPagadora;
import es.um.atica.sai.entities.Factura;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.ConsumoService;
import es.um.atica.sai.services.interfaces.ProductoService;
import es.um.atica.sai.services.interfaces.ServicioService;
import es.um.atica.sai.services.interfaces.TarifaService;
import es.um.atica.sai.services.interfaces.UsuarioService;

@Name("facturacion")
@Scope(ScopeType.CONVERSATION)
public class FacturacionBean {

	ResourceBundle srb = SeamResourceBundle.getBundle();
  
    @Logger
    private Log log;
    
    @In(create = true, required = true)
    ServicioService servicioService;

    @In(create = true, required = true)
    UsuarioService usuarioService;
    
    @In(create = true)
    TarifaService tarifaService;
    
    @In(create = true)
    ConsumoService consumoService;
    
    @In(create = true)
    ProductoService productoService;
    
    @In(value="org.jboss.seam.security.identity", required=true)
    SaiIdentity identity;

    // Consulta de facturas    
    private Servicio servicioBuscar;
    private Usuario usuarioIrBuscar;
    private EntidadPagadora entidadPagadoraBuscar;
    private Date fechaDesdeBuscar;
    private Date fechaHastaBuscar;
    private String fechaDesdeStringBuscar;
    private String fechaHastaStringBuscar;    
    private String anoFacturaBuscar;
    private String numFacturaBuscar;

    private List<Servicio> listaServicios;
    private List<Usuario> listaUsuariosIr;
    private List<EntidadPagadora> listaEntidadesPagadoras;
    private LazyDataModel<Factura> lazyDataModel;
    
	private int first;
	private int pageSize;
    
	public Servicio getServicioBuscar() {
		return servicioBuscar;
	}
	
	public void setServicioBuscar( Servicio servicioBuscar ) {
		this.servicioBuscar = servicioBuscar;
	}
	
	public Usuario getUsuarioIrBuscar() {
		return usuarioIrBuscar;
	}
	
	public void setUsuarioIrBuscar( Usuario usuarioIrBuscar ) {
		this.usuarioIrBuscar = usuarioIrBuscar;
	}
	
	public EntidadPagadora getEntidadPagadoraBuscar() {
		return entidadPagadoraBuscar;
	}
	
	public void setEntidadPagadoraBuscar( EntidadPagadora entidadPagadoraBuscar ) {
		this.entidadPagadoraBuscar = entidadPagadoraBuscar;
	}
	
	public Date getFechaDesdeBuscar() {
		return fechaDesdeBuscar;
	}
	
	public void setFechaDesdeBuscar( Date fechaDesdeBuscar ) {
		this.fechaDesdeBuscar = fechaDesdeBuscar;
		this.fechaDesdeStringBuscar = UtilDate.convertirDateFechaToString(fechaDesdeBuscar);
	}
	
	public Date getFechaHastaBuscar() {
		return fechaHastaBuscar;
	}
	
	public void setFechaHastaBuscar( Date fechaHastaBuscar ) {
		this.fechaHastaBuscar = fechaHastaBuscar;
		this.fechaHastaStringBuscar = UtilDate.convertirDateFechaToString(fechaHastaBuscar);
	}
	
	public String getFechaDesdeStringBuscar() {
		return fechaDesdeStringBuscar;
	}
	
	public void setFechaDesdeStringBuscar( String fechaDesdeStringBuscar ) {
		this.fechaDesdeStringBuscar = fechaDesdeStringBuscar;
	}
	
	public String getFechaHastaStringBuscar() {
		return fechaHastaStringBuscar;
	}
	
	public void setFechaHastaStringBuscar( String fechaHastaStringBuscar ) {
		this.fechaHastaStringBuscar = fechaHastaStringBuscar;
	}

	public List<Servicio> getListaServicios() {
		return listaServicios;
	}
	
	public void setListaServicios( List<Servicio> listaServicios ) {
		this.listaServicios = listaServicios;
	}

	public List<Usuario> getListaUsuariosIr() {
		return listaUsuariosIr;
	}
	
	public void setListaUsuariosIr( List<Usuario> listaUsuariosIr ) {
		this.listaUsuariosIr = listaUsuariosIr;
	}

	public List<EntidadPagadora> getListaEntidadesPagadoras() {
		return listaEntidadesPagadoras;
	}
	
	public void setListaEntidadesPagadoras( List<EntidadPagadora> listaEntidadesPagadoras ) {
		this.listaEntidadesPagadoras = listaEntidadesPagadoras;
	}

	public String getAnoFacturaBuscar() {
		return anoFacturaBuscar;
	}
	
	public void setAnoFacturaBuscar( String anoFacturaBuscar ) {
		this.anoFacturaBuscar = anoFacturaBuscar;
	}
	
	public String getNumFacturaBuscar() {
		return numFacturaBuscar;
	}
	
	public void setNumFacturaBuscar( String numFacturaBuscar ) {
		this.numFacturaBuscar = numFacturaBuscar;
	}

	public LazyDataModel<Factura> getLazyDataModel() {
		return lazyDataModel;
	}
	
	public void setLazyDataModel( LazyDataModel<Factura> lazyDataModel ) {
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

	// Pantalla de búsqueda
	@Create
    public void inicializa()
    {
		this.listaServicios = identity.getServiciosPerfil(TipoPerfil.ADMINISTRATIVO);
		this.listaEntidadesPagadoras = tarifaService.getListaEntidadesPagadorasActivas();
    	this.pageSize = 25;
		buscar();
    }
    
    public void seleccionadoServicio()
    {
    	if (this.servicioBuscar != null)
    	{
    		this.listaUsuariosIr = usuarioService.getIrsByServicio(this.servicioBuscar);
    	}
    }
        
    public void seleccionadoUsuarioIr()
    {
    	if (this.usuarioIrBuscar != null)
    	{
    		this.listaEntidadesPagadoras = tarifaService.getEntidadesByIr(this.usuarioIrBuscar);
    	}
    	else
    	{
    		this.listaEntidadesPagadoras = tarifaService.getListaEntidadesPagadorasActivas();
    	}
    }
    
    public void buscar()
    {
    	DataTable tablaListaProductos = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formResultados:facturaTable");
    	if (tablaListaProductos != null)
    	{
    		tablaListaProductos.setFirst(0);
    	}
    	this.lazyDataModel = new FacturaLazyDataModel();
    }
    
    public void limpiar()
    {
    	this.servicioBuscar = null;
    	this.listaEntidadesPagadoras = tarifaService.getListaEntidadesPagadorasActivas();
    	this.listaUsuariosIr = null;
    	this.entidadPagadoraBuscar = null;
    	this.setFechaDesdeBuscar(null);
    	this.setFechaHastaBuscar(null);
    	this.anoFacturaBuscar = null;
    	this.numFacturaBuscar = null;
    	buscar();
    }
    
    public String getTituloEnlaceEditar(Factura fac)
    {
    	if (fac.getNumeroJusto() == null)
    	{
    		return new StringBuilder(srb.getString("factura.editar")).append(" ").append(fac.getCod().toString()).toString();
    	}
    	else
    	{
    		return new StringBuilder(srb.getString("factura.detalles")).append(" ").append(fac.getAnoJusto()).append("/").append(fac.getNumeroJusto()).toString();
    	}
    }
    
    public String getDescripcionEntidadPagadora(EntidadPagadora ep)
    {
    	return tarifaService.getDescripcionEntidadPagadora( ep );
    }
    
}
