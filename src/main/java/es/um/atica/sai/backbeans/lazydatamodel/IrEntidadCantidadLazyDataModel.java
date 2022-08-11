package es.um.atica.sai.backbeans.lazydatamodel;

import java.util.List;
import java.util.Map;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import es.um.atica.sai.dtos.IrEntidadCantidad;
import es.um.atica.sai.services.interfaces.ConsumoService;

@Name("iecLDM")
public class IrEntidadCantidadLazyDataModel extends LazyDataModel<IrEntidadCantidad> {

	private static final long serialVersionUID = -4783440279603003754L;
	ConsumoService consumoService;

	private List<IrEntidadCantidad> listaIrEntidadCantidad;
    private Integer rowsPerPage;
    private String estadoFacturacion;

    public IrEntidadCantidadLazyDataModel() 
    {
    	// Constructor vacío
	}
    
    public IrEntidadCantidadLazyDataModel(String estadoFacturacion) 
    {
    	this.estadoFacturacion = estadoFacturacion;
	}
    
    public Integer getRowsPerPage() 
    {
        if (this.rowsPerPage == null)
        {
            this.rowsPerPage = 10;
        }
        return rowsPerPage;
    }
    
    public void setRowsPerPage( Integer rowsPerPage ) 
    {
        this.rowsPerPage = rowsPerPage;
    }
    
	public String getEstadoFacturacion() {
		return estadoFacturacion;
	}
	
	public void setEstadoFacturacion( String estadoFacturacion ) {
		this.estadoFacturacion = estadoFacturacion;
	}

	@Override
    public Object getRowKey(IrEntidadCantidad iec) {
      return iec;
    }
    
   
    @Override
    public List<IrEntidadCantidad> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) 
    {
        this.setRowsPerPage(pageSize);
        this.setRowCount(getConsumoService().getCountBusquedaIrEntidadCantidad(this.estadoFacturacion));
        if (this.getRowCount()>0)
        {
            this.listaIrEntidadCantidad = getConsumoService().busquedaIrEntidadCantidad(this.estadoFacturacion, first, pageSize, sortField, sortOrder, filters ); 
        }
        else
        {
            this.listaIrEntidadCantidad = null;
        }
        
        return listaIrEntidadCantidad;
    }

	private ConsumoService getConsumoService()
	{
		if (this.consumoService == null)
		{
			this.consumoService = (ConsumoService)Component.getInstance("consumoService");
		}
		return this.consumoService;
	}
	
}
