package es.um.atica.sai.das.interfaces;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import org.primefaces.model.SortOrder;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.TipoReservable;
import es.um.atica.sai.exceptions.SaiException;

@Local
public interface ProductoDAS extends DataAccessService<Producto>{
    
    public static final String NAME = "productoDAS";
      
	void crear( Producto prod ) throws SaiException;
	void modificar( Producto prod ) throws SaiException;
	void eliminar( Producto prod ) throws SaiException;     
	
	List<Producto> getProductosByServicio(Servicio servicio);  
	List<Producto> getProductosByListaServicios(List<Servicio> listaServicios);
	List<Producto> getProductosByServicioTipo(Servicio servicio, String tipo, boolean soloObtenerNoRequierenProyecto, boolean soloObtenerAdmitenPresupuesto);  
    List<Producto> busquedaProductos(Long codUsuario, int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters);
    List<Producto> getProductosByTipoReservable(TipoReservable tr);
    int getCountBusquedaProductos(Long codUsuario);    
    public List<Producto> getReservables(Servicio servicio);
        
}
