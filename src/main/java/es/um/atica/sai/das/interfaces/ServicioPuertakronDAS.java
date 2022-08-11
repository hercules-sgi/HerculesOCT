package es.um.atica.sai.das.interfaces;

import java.util.List;

import javax.ejb.Local;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.ServicioPuertakron;
import es.um.atica.sai.exceptions.SaiException;

@Local
public interface ServicioPuertakronDAS extends DataAccessService<ServicioPuertakron>{
	
	public static final String NAME = "servicioPuertakronDAS";
	
	void crear(ServicioPuertakron spk) throws SaiException;
	void eliminar(ServicioPuertakron spk) throws SaiException;
	
	List<ServicioPuertakron> getListaServicioPuertakronByServicio(Servicio s);
	boolean existeServicioPuertakron(ServicioPuertakron spk);
}
