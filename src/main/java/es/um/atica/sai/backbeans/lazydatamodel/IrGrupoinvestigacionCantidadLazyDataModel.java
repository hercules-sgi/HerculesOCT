package es.um.atica.sai.backbeans.lazydatamodel;

import java.util.List;
import java.util.Map;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import es.um.atica.sai.dtos.IrGrupoinvestigacionCantidad;
import es.um.atica.sai.services.interfaces.ConsumoService;

@Name("igicLDM")
public class IrGrupoinvestigacionCantidadLazyDataModel extends LazyDataModel<IrGrupoinvestigacionCantidad> {

	private static final long serialVersionUID = 1805267273909590122L;

	ConsumoService consumoService;

	private List<IrGrupoinvestigacionCantidad> listaIrGrupoinvestigacionCantidad;
    private Integer rowsPerPage;
    private String estadoFacturacion;

    public IrGrupoinvestigacionCantidadLazyDataModel() 
    {
    	// Constructor vacío
	}
    
    public IrGrupoinvestigacionCantidadLazyDataModel(String estadoFacturacion) 
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
    public Object getRowKey(IrGrupoinvestigacionCantidad igic) {
      return igic;
    }
    
   
    @Override
    public List<IrGrupoinvestigacionCantidad> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) 
    {
        this.setRowsPerPage(pageSize);
        this.setRowCount(getConsumoService().getCountBusquedaIrGrupoinvestigacionCantidad(this.estadoFacturacion));
        if (this.getRowCount()>0)
        {
            this.listaIrGrupoinvestigacionCantidad = getConsumoService().busquedaIrGrupoinvestigacionCantidad(this.estadoFacturacion, first, pageSize, sortField, sortOrder, filters ); 
        }
        else
        {
            this.listaIrGrupoinvestigacionCantidad = null;
        }
        
        return listaIrGrupoinvestigacionCantidad;
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
