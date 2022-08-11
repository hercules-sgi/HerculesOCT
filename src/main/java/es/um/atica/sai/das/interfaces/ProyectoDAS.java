package es.um.atica.sai.das.interfaces;

import java.util.List;

import javax.ejb.Local;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.Proyecto;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;

@Local
public interface ProyectoDAS extends DataAccessService<Proyecto>{
	
	public static final String NAME = "proyectoDAS";
	
	List<Proyecto> busquedaGestor();
	List<Proyecto> busquedaSupervisorIrUsuario(List<Servicio> listaServiciosSupervisor, List<Usuario> listaIrs);
	List<Proyecto> getListaProyectosByProductoIr(Producto producto, Usuario usuarioIr);
	void crear(Proyecto proyecto) throws SaiException;
	void modificar(Proyecto proyecto) throws SaiException;
	void eliminar(Proyecto proyecto) throws SaiException;
	
	
}
