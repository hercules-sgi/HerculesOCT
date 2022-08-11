package es.um.atica.sai.das.interfaces;

import java.util.List;

import javax.ejb.Local;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.Proyecto;
import es.um.atica.sai.entities.ProyectoProducto;
import es.um.atica.sai.exceptions.SaiException;

@Local
public interface ProyectoProductoDAS extends DataAccessService<ProyectoProducto> {

	public static final String NAME = "proyectoProductoDAS";

	void crear(ProyectoProducto pp) throws SaiException;
	void modificar(ProyectoProducto pp) throws SaiException;
	void eliminar(ProyectoProducto pp) throws SaiException;
	boolean existeProyectoProducto(Proyecto proyecto, Producto producto); 
	ProyectoProducto getProyectoProductoByProyectoProducto(Proyecto proyecto, Producto producto);
	List<ProyectoProducto> getListaProyectoProductoByProyecto(Proyecto proyecto);
}
