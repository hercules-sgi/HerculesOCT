package es.um.atica.sai.das.interfaces;

import java.util.List;

import javax.ejb.Local;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.ReservableHorario;
import es.um.atica.sai.entities.TipoReservable;
import es.um.atica.sai.exceptions.SaiException;

@Local
public interface ReservableHorarioDAS extends DataAccessService<ReservableHorario>{
	public static final String NAME = "reservableHorarioDAS";
	
	void crear(ReservableHorario rh) throws SaiException;
	void eliminar(ReservableHorario rh) throws SaiException;

	List<ReservableHorario> getListaReservableHorarioByTipoReservable(TipoReservable tr);
	List<ReservableHorario> getListaReservableHorarioByProducto(Producto producto);
	boolean existeTiporeservableHorarioSolapado(ReservableHorario rh);
	boolean existeProductoHorarioSolapado(ReservableHorario rh);
}
