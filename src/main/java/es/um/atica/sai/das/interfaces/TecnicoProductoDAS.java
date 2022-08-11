package es.um.atica.sai.das.interfaces;

import java.util.List;

import javax.ejb.Local;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.entities.TecnicoProducto;
import es.um.atica.sai.exceptions.SaiException;

@Local
public interface TecnicoProductoDAS extends DataAccessService<TecnicoProducto>{
    
    List<TecnicoProducto> getTecnicosByService(long codServicio);    
    boolean existeTecnico( TecnicoProducto tecnico ); 
	void crear( TecnicoProducto tec ) throws SaiException;
	void modificar( TecnicoProducto tec ) throws SaiException;
	void eliminar( TecnicoProducto tec ) throws SaiException;
	public TecnicoProducto getTecnicoById(long codTecnico);
}
