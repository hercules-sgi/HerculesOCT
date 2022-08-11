package es.um.atica.sai.das.interfaces;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.entities.Consumo;
import es.um.atica.sai.entities.ConsumoAutorizacionkron;
import es.um.atica.sai.entities.PuertaKron;
import es.um.atica.sai.exceptions.SaiException;

@Local
public interface ConsumoAutorizacionkronDAS extends DataAccessService<ConsumoAutorizacionkron>{
	
	public static final String NAME = "ConsumoAutorizacionkronDAS";
	
	void crear(ConsumoAutorizacionkron c) throws SaiException;
	void modificar(ConsumoAutorizacionkron c) throws SaiException;
	void eliminar(ConsumoAutorizacionkron c) throws SaiException;
	
	List<ConsumoAutorizacionkron> getAutorizacionesByConsumo(Consumo c);
	List<ConsumoAutorizacionkron> getAutorizacionesByConsumoPuertaFechas(Consumo consumo, PuertaKron pk, Date fechaInicio, Date fechaFin);
	boolean existeAutorizacionSolicitada(ConsumoAutorizacionkron cak);
	boolean existeOtraAutorizacionIgualOtroConsumo(ConsumoAutorizacionkron cak);
		
}
