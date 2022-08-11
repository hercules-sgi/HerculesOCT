package es.um.atica.sai.das.interfaces;

import java.util.List;

import javax.ejb.Local;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.TipoHorario;
import es.um.atica.sai.entities.TipoReservable;
import es.um.atica.sai.exceptions.SaiException;

@Local
public interface TipoReservableDAS extends DataAccessService<TipoReservable> {

	String NAME = "tipoReservableDAS";

	/**
	 * Devuelve una lista de tipos de reservables en funcion del perfil de usuario conectado.
	 *
	 * @return
	 */
	List<TipoReservable> getListaTiposReservableByUsuarioConectado();

	List<TipoReservable> buscar();
	List<TipoReservable> getListaTiposReservableByServicio(Servicio servicio);
	List<TipoReservable> getListaTiposReservableByTipoHorario(TipoHorario tipoHorario);

	void crear(TipoReservable tr) throws SaiException;
	void modificar(TipoReservable tr) throws SaiException;
	void eliminar(TipoReservable tr) throws SaiException;


}
