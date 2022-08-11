package es.um.atica.sai.backbeans;

import java.math.BigDecimal;
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

import es.um.atica.sai.backbeans.lazydatamodel.ProductoLazyDataModel;
import es.um.atica.sai.dtos.TipoPerfil;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.FungibleService;
import es.um.atica.sai.services.interfaces.KronService;
import es.um.atica.sai.services.interfaces.ProductoService;
import es.um.atica.sai.services.interfaces.ServicioService;
import es.um.atica.sai.services.interfaces.TarifaService;
import es.um.atica.sai.services.interfaces.UsuarioService;

@Name( ProductosBean.NAME )
@Scope( ScopeType.CONVERSATION )
public class ProductosBean {

	public static final String NAME = "productos";

	@Logger
	private Log log;

	@In(create = true )
	private ProductoService productoService;

	@In(create = true )
	private ServicioService servicioService;

	@In(create = true )
	private FungibleService fungibleService;

	@In(create = true )
	private UsuarioService usuarioService;

	@In(create = true )
	private KronService kronService;

	@In(create = true )
	private TarifaService tarifaService;

	@In(create=true)
	protected FacesMessages facesMessages;

	@In(value="org.jboss.seam.security.identity", required=true)
	SaiIdentity identity;

	private String descripcionBuscar;
	private String tipoBuscar;
	private Servicio servicioBuscar;
	private String estadoBuscar;
	private List<Servicio> listaServicios;
	private LazyDataModel<Producto> lazyDataModel;

	private int first;
	private int pageSize;
	
	public String getDescripcionBuscar() {
		return descripcionBuscar;
	}

	public void setDescripcionBuscar( String descripcionBuscar ) {
		this.descripcionBuscar = descripcionBuscar;
	}

	public String getTipoBuscar() {
		return tipoBuscar;
	}

	public void setTipoBuscar( String tipoBuscar ) {
		this.tipoBuscar = tipoBuscar;
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

	public List<Servicio> getListaServicios() {
		return listaServicios;
	}

	public void setListaServicios( List<Servicio> listaServicios ) {
		this.listaServicios = listaServicios;
	}

	public LazyDataModel<Producto> getLazyDataModel() {
		return lazyDataModel;
	}

	public void setLazyDataModel( LazyDataModel<Producto> lazyDataModel ) {
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
	public void inicializa()
	{
		this.listaServicios = identity.getServiciosPerfil(TipoPerfil.SUPERVISOR);
		this.estadoBuscar = "ALTA";
		if (this.listaServicios.size() == 1)
		{
			this.servicioBuscar = this.listaServicios.get(0);
		}
		this.pageSize = 25;
		buscarProductos();
	}


	public void buscarProductos()
	{
    	DataTable tablaListaProductos = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formProductos:tablaProductos");
    	if (tablaListaProductos != null)
    	{
    		tablaListaProductos.setFirst(0);
    	}
		this.lazyDataModel = new ProductoLazyDataModel(identity.getUsuarioConectado().getCod(), tipoBuscar);
	}

	public void limpiarBusqueda()
	{
		this.tipoBuscar = null;
		this.descripcionBuscar = null;
		if (this.listaServicios.size() > 1)
		{
			this.servicioBuscar = null;
		}
		this.lazyDataModel = null;
		this.estadoBuscar = "ALTA";
		buscarProductos();
	}

	public String getDescripcionTipoProducto(String tipo)
	{
		return productoService.getDescripcionTipoProducto( tipo );
	}

	public void eliminarProducto(Producto prod)
	{
		final String descripcion = prod.getDescripcion();
		try
		{
			if (( prod.getAnexos() != null ) && !prod.getAnexos().isEmpty())
			{
				productoService.eliminarAnexo(prod.getAnexos().get(0));
				prod.setAnexos(null);
			}
			productoService.eliminarProducto(prod);
		}
		catch ( final Exception ex )
		{
			facesMessages.add(StatusMessage.Severity.ERROR,
					"producto.eliminar.error",
					null, null,
					ex.getMessage());
			return;
		}
		facesMessages.add(StatusMessage.Severity.INFO,
				"producto.eliminar.ok",
				null, null,
				descripcion);
		lazyDataModel = new ProductoLazyDataModel(identity.getUsuarioConectado().getCod(), tipoBuscar);
	}

	public BigDecimal getStockDisponibleByProducto(Producto producto)
	{
		return productoService.getStockDisponibleByProducto(producto);
	}
	
}
