package es.um.atica.sai.das.interfaces;

import java.util.List;

import javax.ejb.Local;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.entities.Tarifa;
import es.um.atica.sai.exceptions.SaiException;

@Local
public interface TarifaDAS extends DataAccessService<Tarifa>{
    
	boolean existeTarifa( Tarifa tarifa, Long cantidadInicial ); 
	List<Tarifa> getTarifasByProductoCantidad(Long codProducto);
	void crear( Tarifa tar ) throws SaiException;
	void eliminar( Tarifa tar ) throws SaiException;
}
