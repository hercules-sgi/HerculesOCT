package es.um.atica.sai.das.interfaces;

import java.util.List;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.dtos.EstadisticaConsumo;
import es.um.atica.sai.entities.Servicio;


public interface EstadisticaConsumoDAS extends DataAccessService<EstadisticaConsumo> {

	/**
	 * Obtiene la lista de los {@link EstadisticaConsumo} para las estadisticas de resolución
	 *
	 * @param byProducto
	 *                       boolean
	 * @param listaServicios
	 *                       {@link List}{@link Servicio}
	 * @return {@link List}{@link EstadisticaConsumo}
	 */
	List<EstadisticaConsumo> estadisticasConsumos( boolean byProducto, List<Servicio> listaServicios );
}
