package es.um.atica.sai.das.interfaces;

import java.util.List;

import javax.ejb.Local;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.entities.Certificacion;
import es.um.atica.sai.entities.TipoCertificacion;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;

@Local
public interface CertificacionDAS extends DataAccessService<Certificacion> {

	/**
	 * Modifica una {@link Certificacion} en la BBDD
	 *
	 * @param c
	 *          {@link Certificacion}
	 * @throws SaiException
	 */
	void modificar( Certificacion c ) throws SaiException;

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

}
