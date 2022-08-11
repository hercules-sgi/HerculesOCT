package es.um.atica.sai.das.interfaces;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.entities.Muestra;
import es.um.atica.sai.exceptions.SaiException;

public interface MuestraDAS extends DataAccessService<Muestra>{
	
	public static String NAME = "muestraDAS";
	
	public void eliminar(Muestra m) throws SaiException;
	public void crear(Muestra m) throws SaiException;

}
