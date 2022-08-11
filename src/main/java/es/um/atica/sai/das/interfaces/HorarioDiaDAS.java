package es.um.atica.sai.das.interfaces;

import java.util.List;

import javax.ejb.Local;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.entities.HorarioDia;
import es.um.atica.sai.entities.TipoHorario;
import es.um.atica.sai.entities.TipoReservable;
import es.um.atica.sai.exceptions.SaiException;

@Local
public interface HorarioDiaDAS extends DataAccessService<HorarioDia>{
	
	public static final String NAME = "horarioDiaDAS";
	
	void crear(HorarioDia hd) throws SaiException;
	void modificar(HorarioDia hd) throws SaiException;
	void eliminar(HorarioDia hd) throws SaiException;
	String getMinHoraInicioManana(TipoReservable tr);
	String getMinHoraInicioTarde(TipoReservable tr);
	String getMaxHoraFinManana(TipoReservable tr);
	String getMaxHoraFinTarde(TipoReservable tr);
	boolean existeHorarioDia(TipoReservable tr);
	HorarioDia getHorarioDiaByTipohorarioDia(TipoHorario th, int dia);
	List<HorarioDia> getListaHorarioDiaByTipohorario(TipoHorario th);
	
}
