package es.um.atica.sai.backbeans.lazydatamodel;

import java.util.List;
import java.util.Map;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import es.um.atica.sai.entities.Factura;
import es.um.atica.sai.services.interfaces.ConsumoService;

@Name("facturaLDM")
public class FacturaLazyDataModel extends LazyDataModel<Factura> {

	ConsumoService consumoService;
	private List<Factura> listaFactura;

    public FacturaLazyDataModel() {
    	//Constructor vacío
	}

    @Override
    public Object getRowKey(Factura fac) {
      return fac.getCod();
    }
    
    @Override
    public List<Factura> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) 
    {
        this.setRowCount(getConsumoService().getCountBusquedaFactura());
        if (this.getRowCount()>0)
        {
            this.listaFactura = getConsumoService().busquedaFactura( first, pageSize, sortField, sortOrder, filters ); 
        }
        else
        {
            this.listaFactura = null;
        }
        
        return listaFactura;
    }
   
    
    @Override
    public Factura getRowData(String rowKey)
    {
    	for (Factura fac: listaFactura)
    	{
    		if (Long.toString(fac.getCod()).equals( rowKey ))
    		{
    			return fac;
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
