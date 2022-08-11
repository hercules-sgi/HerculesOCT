package es.um.atica.sai.backbeans.lazydatamodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import es.um.atica.jpa.das.ResultQuery;
import es.um.atica.sai.entities.Consumo;
import es.um.atica.sai.services.interfaces.ConsumoService;

@Name("consumoLDM")
public class ConsumoLazyDataModel extends LazyDataModel<Consumo>{

	private static final long serialVersionUID = 5209326263855118081L;
	
	ConsumoService consumoService;
	private String presupuesto;
	private boolean consultarTodasParaTecnico;
	


	public ConsumoLazyDataModel() {
	}

	public ConsumoLazyDataModel(String presupuesto, boolean consultarTodasParaTecnico) 
	{
		this.presupuesto = presupuesto;
		this.consultarTodasParaTecnico = consultarTodasParaTecnico;
	}

	public String getPresupuesto() {
		return presupuesto;
	}
	
	public void setPresupuesto( String presupuesto ) {
		this.presupuesto = presupuesto;
	}

	public boolean isConsultarTodasParaTecnico() {
		return consultarTodasParaTecnico;
	}
	
	public void setConsultarTodasParaTecnico( boolean consultarTodasParaTecnico ) {
		this.consultarTodasParaTecnico = consultarTodasParaTecnico;
	}
	
	private ConsumoService getConsumoService() 
	{
		if ( this.consumoService == null ) 
		{
			this.consumoService = ( ConsumoService ) Component.getInstance( "consumoService" );
		}
		return this.consumoService;
	}

	
    @Override
    public Consumo getRowData( String rowKey ) {        
        Long key = new Long(rowKey);
        return this.getConsumoService().getConsumoById(key);                 
    }
    
	@Override
	public Object getRowKey( Consumo c ) {
		return c.getCod();
	}


    @Override
    public List<Consumo> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) 
    {
    	this.setRowCount(getConsumoService().getCountBusquedaConsumos(presupuesto, consultarTodasParaTecnico).intValue());
    	ResultQuery<Consumo> resultado = getConsumoService().busquedaConsumos(presupuesto, consultarTodasParaTecnico, first, pageSize, sortField, sortOrder);
    	if (resultado==null)
    	{
    		return new ArrayList<>(0);
    	}
    	return resultado.getResultList();
    }
    

}
