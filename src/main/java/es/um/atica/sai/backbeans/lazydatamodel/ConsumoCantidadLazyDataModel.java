package es.um.atica.sai.backbeans.lazydatamodel;

import java.util.List;
import java.util.Map;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import es.um.atica.sai.dtos.ConsumoCantidad;
import es.um.atica.sai.services.interfaces.ConsumoService;

@Name("ccLDM")
public class ConsumoCantidadLazyDataModel extends LazyDataModel<ConsumoCantidad> {


	private static final long serialVersionUID = 7564728788882864392L;

	ConsumoService consumoService;
	private List<ConsumoCantidad> listaConsumoCantidad;
    private Integer rowsPerPage;
    private String estadoFacturacion;

	public ConsumoCantidadLazyDataModel() 
	{
		// Constructor vacío
	}
	
	public ConsumoCantidadLazyDataModel(String estadoFacturacion) 
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
    public Object getRowKey(ConsumoCantidad cc) {
      return cc.getCodproducto();
    }
    
   
    @Override
    public List<ConsumoCantidad> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) 
    {
        this.setRowsPerPage(pageSize);
        this.setRowCount(getConsumoService().getCountBusquedaConsumoCantidad(this.estadoFacturacion));
        if (this.getRowCount()>0)
        {
            this.listaConsumoCantidad = getConsumoService().busquedaConsumoCantidad(this.estadoFacturacion, first, pageSize, sortField, sortOrder, filters ); 
        }
        else
        {
            this.listaConsumoCantidad = null;
        }
        
        return listaConsumoCantidad;
    }
   
    
    @Override
    public ConsumoCantidad getRowData(String rowKey)
    {
    	for (ConsumoCantidad cc: listaConsumoCantidad)
    	{
    		if (Long.toString(cc.getCodproducto()).equals( rowKey ))
    		{
    			return cc;
    		}
    	}
    	return null;
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
