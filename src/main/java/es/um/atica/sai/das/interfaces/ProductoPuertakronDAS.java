package es.um.atica.sai.das.interfaces;

import java.util.List;

import javax.ejb.Local;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.ProductoPuertakron;
import es.um.atica.sai.exceptions.SaiException;

@Local
public interface ProductoPuertakronDAS extends DataAccessService<ProductoPuertakron>{
	
	public static final String NAME = "productoPuertakronDAS";
	
	void crear(ProductoPuertakron p) throws SaiException;
	void eliminar(ProductoPuertakron p) throws SaiException;
	
	List<ProductoPuertakron> getListaProductoPuertakronByProducto(Producto p);
	boolean existeProductoPuertakron(ProductoPuertakron ppk);
}
