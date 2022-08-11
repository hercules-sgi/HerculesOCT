package es.um.atica.sai.das.interfaces;

import java.util.List;

import javax.ejb.Local;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.TipoCertificacion;
import es.um.atica.sai.exceptions.SaiException;

@Local
public interface TipoCertificacionDAS extends DataAccessService<TipoCertificacion> {

	/**
	 * Elimna un {@link TipoCertificacion} pasado por parametro
	 *
	 * @param t
	 *          {@link TipoCertificacion}
	 * @throws SaiException
	 */
	void eliminar( TipoCertificacion t ) throws SaiException;

	/**
	 * Crea un {@link TipoCertificacion} pasado por parametro
	 *
	 * @param t
	 *          {@link TipoCertificacion}
	 * @throws SaiException
	 */
	void crear( TipoCertificacion t ) throws SaiException;

	/**
	 * Modifica un {@link TipoCertificacion} pasado por parametro
	 *
	 * @param t
	 *          {@link TipoCertificacion}
	 * @throws SaiException
	 */
	void modificar( TipoCertificacion t ) throws SaiException;

	/**
	 * Devuelve una lista con todos los {@link TipoCertificacion}
	 *
	 * @return {@link List}{@link TipoCertificacion}
	 */
	List<TipoCertificacion> getListaTiposCertificaciones();

	List<TipoCertificacion> getListaTiposCertificacionesByProducto(Producto producto);
	
	/**
	 * Comprueba si ya existe un {@link TipoCertificacion} con ese nombre
	 *
	 * @param nombre
	 *               {@link String}
	 * @return boolean
	 */
	boolean existeTipoByNombre( String nombre );

	/**
	 * Comprueba si existe otra entrada con ese nombre en base de datos
	 *
	 * @param t
	 *          {@link TipoCertificacion}
	 * @return boolean
	 */
	boolean existeOtroTipoByNombre( TipoCertificacion t );
}
