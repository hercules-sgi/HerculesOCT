package es.um.atica.sai.das.interfaces;

import java.util.List;
import java.util.Map;

import org.primefaces.model.SortOrder;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.entities.Factura;
import es.um.atica.sai.exceptions.SaiException;


public interface FacturaDAS extends DataAccessService<Factura>{
    
    List<Factura> busquedaFactura(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters);
    int getCountBusquedaFactura();
    void crear( Factura fac ) throws SaiException;
    void modificar( Factura fac ) throws SaiException;
    void eliminar( Factura fac ) throws SaiException;
}
