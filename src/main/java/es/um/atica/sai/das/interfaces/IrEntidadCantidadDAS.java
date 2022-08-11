package es.um.atica.sai.das.interfaces;

import java.util.List;
import java.util.Map;

import org.primefaces.model.SortOrder;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.dtos.IrEntidadCantidad;


public interface IrEntidadCantidadDAS extends DataAccessService<IrEntidadCantidad>{
    
    List<IrEntidadCantidad> busquedaIrEntidadCantidad(String estadoFacturacion,int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters);
    int getCountBusquedaIrEntidadCantidad(String estadoFacturacion);

}
