package es.um.atica.sai.das.interfaces;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import org.primefaces.model.SortOrder;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.entities.Equipo;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.TipoReservable;
import es.um.atica.sai.exceptions.SaiException;

@Local
public interface EquipoDAS extends DataAccessService<Equipo>{

	List<Equipo> getReservablesByTipoReservable(TipoReservable t);
	List<Equipo> getEquiposByServicio(Servicio servicio);

	void crear(Equipo r) throws SaiException;
	void modificar(Equipo r) throws SaiException;
	void eliminar(Equipo r) throws SaiException;

	/**
	 * Obtiene los resultados para el lazy de {@link Equipo}
	 *
	 * @param codTipo
	 *                  {@link Long}
	 * @param first
	 *                  int
	 * @param pageSize
	 *                  int
	 * @param sortField
	 *                  {@link String}
	 * @param sortOrder
	 *                  {@link SortOrder}
	 * @param filters
	 *                  {@link Map} {@link String} {@link Object}
	 * @return {@link List} {@link Equipo}
	 */
	List<Equipo> busquedaReservables( Long codTipo, int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters );

	/**
	 * Obtiene el número total de elementos obtenidos por la busqueda
	 *
	 * @param codTipo
	 *                {@link Long}
	 * @return int
	 */
	int getCountBusquedaReservables( Long codTipo );

	/**
	 * Comprueba si existe un equipo con esa descripción
	 *
	 * @param e
	 *          {@link Equipo}
	 * @return boolean
	 */
	boolean existeEquipobyDescripcion( Equipo e );

	/**
	 * Comprueba si existe otro equipo con esa descripción
	 *
	 * @param e
	 *          {@link Equipo}
	 * @return boolean
	 */
	boolean existeOtroEquipobyDescripcion( Equipo e );

}
