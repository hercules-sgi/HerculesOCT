package es.um.atica.sai.das.interfaces;

import java.util.List;

import javax.ejb.Local;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.entities.ReservaEspera;
import es.um.atica.sai.entities.Reservas;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;

@Local
public interface ReservaEsperaDAS extends DataAccessService<ReservaEspera>{
	
	public static final String NAME = "reservaEsperaDAS";

	void crear(ReservaEspera r) throws SaiException;
	void modificar(ReservaEspera r) throws SaiException;
	void eliminar(ReservaEspera r) throws SaiException;
	
	List<ReservaEspera> getReservaEsperaByReserva(Reservas r);
	
	ReservaEspera getReservaEsperaByUsuarioReserva(Usuario u, Reservas r);
	
}
