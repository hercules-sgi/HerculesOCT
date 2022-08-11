package es.um.atica.sai.backbeans.lazydatamodel;

import java.util.List;
import java.util.Map;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.services.interfaces.ProductoService;

@Name("productoLDM")
public class ProductoLazyDataModel extends LazyDataModel<Producto> {

	

	private static final long serialVersionUID = -1062694634307587779L;

	private static final String CAMPOS_FUNGIBLE = "pro.nivel1.nombre, pro.controlStock, pro.stock, pro.unidadMedida.descripcion";
	private static final String CAMPOS_RESERVA = "pro.tipoReservable.descripcion, pro.requiereTecnico, pro.requiereValidacion";
	private static final String CAMPOS_PRESTACION = "pro.consumoAsociado, pro.requiereValidacion";
	private static final String COMPARTIDO_RESERVA_PRESTACION = "pro.requiereValidacion";
	private static final String DEFAULT_SORT = "pro.descripcion";
	
	ProductoService productoService;
	private Long codUsuarioSupervisor;
	private String tipoBuscar;
	private List<Producto> listaProductos;
    
    public ProductoLazyDataModel() {
	}

	public ProductoLazyDataModel(Long codUsuarioSupervisor, String tipoBuscar) {
    	this.codUsuarioSupervisor = codUsuarioSupervisor;
    	this.tipoBuscar = tipoBuscar;
	}
    
    @Override
    public Object getRowKey(Producto prod) {
      return prod.getCod();
    }
    

    private String fixSortField(String sortField)
    {
    	if (sortField.isEmpty())
    	{
    		return sortField;
    	}
        if (!sortField.contains("pro." ))
        {
        	sortField = new StringBuilder("pro.").append(sortField).toString();
        }
    	if (("F".equals(this.tipoBuscar) && (CAMPOS_RESERVA.contains( sortField ) || CAMPOS_PRESTACION.contains( sortField ))) ||
    	    ("R".equals(this.tipoBuscar) && !COMPARTIDO_RESERVA_PRESTACION.equals(sortField) && (CAMPOS_FUNGIBLE.contains( sortField ) || CAMPOS_PRESTACION.contains( sortField ))) ||
    	    ("P".equals(this.tipoBuscar) && !COMPARTIDO_RESERVA_PRESTACION.equals(sortField) && (CAMPOS_FUNGIBLE.contains( sortField ) || CAMPOS_RESERVA.contains( sortField ))))
    	{
   			sortField = DEFAULT_SORT;
    	}
    	return sortField;
    }
    
    @Override
    public List<Producto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) 
    {
    	sortField = this.fixSortField( sortField );
        this.setRowCount(getProductoService().getCountBusquedaProductos(this.codUsuarioSupervisor));
        if (this.getRowCount()>0)
        {
            this.listaProductos = getProductoService().busquedaProductos(this.codUsuarioSupervisor, first, pageSize, sortField, sortOrder, filters ); 
        }
        else
        {
            this.listaProductos = null;
        }
        
        return listaProductos;
    }
   
    
    @Override
    public Producto getRowData(String rowKey)
    {
    	for (Producto prod: this.listaProductos)
    	{
    		if (Long.toString(prod.getCod()).equals( rowKey ))
    		{
    			return prod;
    		}
    	}
    	return null;
    }

	private ProductoService getProductoService()
	{
		if (this.productoService == null)
		{
			this.productoService = (ProductoService)Component.getInstance("productoService");
		}
		return this.productoService;
	}
}