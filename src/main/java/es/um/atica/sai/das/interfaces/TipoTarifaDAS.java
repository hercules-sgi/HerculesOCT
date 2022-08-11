package es.um.atica.sai.das.interfaces;

import java.util.List;

import javax.ejb.Local;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.entities.TipoTarifa;
import es.um.atica.sai.exceptions.SaiException;


@Local
public interface TipoTarifaDAS extends DataAccessService<TipoTarifa>{
    
	List<TipoTarifa> getTarifas();
	void crear( TipoTarifa tt ) throws SaiException;
	void modificar( TipoTarifa tt ) throws SaiException;
	void eliminar( TipoTarifa tt ) throws SaiException;
}
