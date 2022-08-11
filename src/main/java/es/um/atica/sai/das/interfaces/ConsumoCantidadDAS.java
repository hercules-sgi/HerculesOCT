package es.um.atica.sai.das.interfaces;

import java.util.List;
import java.util.Map;

import org.primefaces.model.SortOrder;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.dtos.ConsumoCantidad;


public interface ConsumoCantidadDAS extends DataAccessService<ConsumoCantidad>{
    
    List<ConsumoCantidad> busquedaConsumoCantidad(String estadoFacturacion, int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters);
    int getCountBusquedaConsumoCantidad(String estadoFacturacion);
    Long getNumIrsConsumoResumen(String estadoFacturacion);
    Long getNumMiembrosConsumoResumen(String estadoFacturacion);
}
