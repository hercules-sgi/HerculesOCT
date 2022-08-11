package es.um.atica.sai.das.interfaces;

import java.util.List;
import java.util.Map;

import org.primefaces.model.SortOrder;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.dtos.IrGrupoinvestigacionCantidad;


public interface IrGrupoinvestigacionCantidadDAS extends DataAccessService<IrGrupoinvestigacionCantidad>{
    
    List<IrGrupoinvestigacionCantidad> busquedaIrGrupoinvestigacionCantidad(String estadoFacturacion, int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters);
    int getCountBusquedaIrGrupoinvestigacionCantidad(String estadoFacturacion);

}
