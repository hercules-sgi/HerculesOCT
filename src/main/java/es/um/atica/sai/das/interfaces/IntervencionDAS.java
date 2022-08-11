package es.um.atica.sai.das.interfaces;

import java.math.BigDecimal;

import javax.ejb.Local;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.entities.Equipo;
import es.um.atica.sai.entities.Intervencion;
import es.um.atica.sai.exceptions.SaiException;


@Local
public interface IntervencionDAS extends DataAccessService<Intervencion> {

	/**
	 * Guarda una {@link Intervencion} en BBDD esté o no ya en ella
	 *
	 * @param i
	 *          {@link Intervencion}
	 * @throws SaiException
	 */
	void guardar( Intervencion i ) throws SaiException;

	/**
	 * Elimina una {@link Intervencion} de la BBDD
	 *
	 * @param i
	 *          {@link Intervencion}
	 * @throws SaiException
	 */
	void eliminar( Intervencion i ) throws SaiException;

	BigDecimal busquedaSumImporteByEquipoTipo(Equipo equipo, String tipo, String fechaDesde, String fechaHasta);
}
