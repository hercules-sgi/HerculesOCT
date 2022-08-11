package es.um.atica.sai.das.interfaces;

import javax.ejb.Local;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.entities.ProductoTipoCertificacion;
import es.um.atica.sai.exceptions.SaiException;

@Local
public interface ProductoTipoCertificacionDAS extends DataAccessService<ProductoTipoCertificacion> {


	/**
	 * Crea un {@link ProductoTipoCertificacion} en la BBDD
	 *
	 * @param p
	 *          {@link ProductoTipoCertificacion}
	 * @throws SaiException
	 */
	void guardar( ProductoTipoCertificacion p ) throws SaiException;

	/**
	 * Elimina un {@link ProductoTipoCertificacion} de la BBDD
	 *
	 * @param p
	 *          {@link ProductoTipoCertificacion}
	 * @throws SaiException
	 */
	void eliminar( ProductoTipoCertificacion p ) throws SaiException;

}
