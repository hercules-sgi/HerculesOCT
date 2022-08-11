package es.um.atica.sai.das.interfaces;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.dtos.TerminalKron;
import es.um.atica.sai.entities.PuertaKron;
import es.um.atica.sai.exceptions.SaiException;

public interface PuertaKronDAS extends DataAccessService<PuertaKron>{
	
	public static final String NAME = "puertaKronDAS";	
	
	PuertaKron getPuertaKronByTerminalKron(TerminalKron tk);
	void crear(PuertaKron pk) throws SaiException;
	
}
