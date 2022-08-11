package es.um.atica.sai.das.interfaces;

import java.util.List;

import javax.ejb.Local;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.entities.Equipo;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.ProductoEquipo;
import es.um.atica.sai.exceptions.SaiException;

@Local
public interface ProductoEquipoDAS extends DataAccessService<ProductoEquipo>{
    
	public static final String NAME = "productoEquipoDAS";
	
    List<ProductoEquipo> getProductoEquiposByProducto(Producto producto);  
    List<Equipo> getEquiposByProducto(Producto producto);  
    ProductoEquipo getProductoEquipoByProductoEquipo(Producto producto, Equipo equipo);
    boolean existeProductoEquipo(Producto producto, Equipo equipo); 
	void crear(ProductoEquipo pe) throws SaiException;
	void modificar(ProductoEquipo pe) throws SaiException;
	void eliminar(ProductoEquipo pe) throws SaiException;
}
