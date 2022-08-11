package es.um.atica.sai.services.interfaces;

import java.util.List;

import javax.ejb.Local;

import org.primefaces.model.UploadedFile;

import es.um.atica.sai.entities.Certificacion;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.ProductoTipoCertificacion;
import es.um.atica.sai.entities.TipoCertificacion;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;

@Local
public interface CertificacionesService {

	/**
	 * Elimna un {@link TipoCertificacion} pasado por parametro
	 *
	 * @param t
	 *          {@link TipoCertificacion}
	 * @throws SaiException
	 */
	void eliminarTipoCertificacion( TipoCertificacion t ) throws SaiException;

	/**
	 * Guarda un {@link TipoCertificacion} pasado por parametro
	 *
	 * @param t
	 *          {@link TipoCertificacion}
	 * @throws SaiException
	 */
	void guardarTipoCertificacion( TipoCertificacion t ) throws SaiException;

	/**
	 * Devuelve una lista con todos los {@link TipoCertificacion}
	 *
	 * @return {@link List}{@link TipoCertificacion}
	 */
	List<TipoCertificacion> getListaTiposCertificaciones();
	
	List<TipoCertificacion> getListaTiposCertificacionesByProducto(Producto producto);

	/**
	 * Hace una busqueda de {@link Certificacion} en función de los filtros definidos en el DAS y el Bean
	 *
	 * @return {@link List}{@link Certificacion}
	 */
	List<Certificacion> busquedaCertificaciones();

	List<Certificacion> busquedaCertificacionesByListaUsuarios(List<Usuario> listaUsuarios);
	List<Certificacion> getListaCertificacionesByUsuario(Usuario usuario);
	boolean existeCertificacionPendienteByTipoAndUsuario(TipoCertificacion tc, Usuario usuario);
	boolean existeCertificacionActivaByTipoAndUsuario(TipoCertificacion tc, Usuario usuario);
	List<Usuario> getListaUsuariosPuedoSolicitarCertificacion();
	String getDescripcionEstadoCertificacion(Certificacion cert);
	String getColorEstadoCertificacion(String estado);
	
	void crearCertificacion(Certificacion c, UploadedFile fichero) throws SaiException; 
	/**
	 * Guarda una {@link Certificacion} en la BBDD
	 *
	 * @param c
	 *          {@link Certificacion}
	 * @throws SaiException
	 */
	void modificarCertificacion( Certificacion c ) throws SaiException;

	/**
	 * Elimina una {@link Certificacion} en la BBDD
	 *
	 * @param c
	 *          {@link Certificacion}
	 * @throws SaiException
	 */
	void eliminarCertificacion( Certificacion c ) throws SaiException;

	/**
	 * Crea un {@link ProductoTipoCertificacion} en la BBDD
	 *
	 * @param p
	 *          {@link ProductoTipoCertificacion}
	 * @throws SaiException
	 */
	void guardarProductoTipoCertificacion( ProductoTipoCertificacion p ) throws SaiException;

	/**
	 * Elimina un {@link ProductoTipoCertificacion} de la BBDD
	 *
	 * @param p
	 *          {@link ProductoTipoCertificacion}
	 * @throws SaiException
	 */
	void eliminarProductoTipoCertificacion( ProductoTipoCertificacion p ) throws SaiException;


}
