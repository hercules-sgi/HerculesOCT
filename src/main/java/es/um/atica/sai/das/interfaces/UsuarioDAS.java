package es.um.atica.sai.das.interfaces;

import java.util.List;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.entities.Consumo;
import es.um.atica.sai.entities.EntidadPagadora;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;


public interface UsuarioDAS extends DataAccessService<Usuario>{

	String NAME = "usuarioDAS";

	void crear(Usuario usuario) throws SaiException;
	void modificar(Usuario usuario) throws SaiException;
	void eliminarEntidadPagadoraSolicitadaUsuarios(EntidadPagadora entidad) throws SaiException;
	Usuario findUsuarioByLogin(String login);
	Usuario findUsuarioByDni(String dniStr);
	Usuario findUsuarioByEmail( String emailStr ) ;
	Usuario getUsuarioById(long key);
	Usuario getUsuarioByConsumo(Consumo c);
	List<Usuario> getUsuariosPendientes();
	List<Usuario> getIrsByMiembroServicio(Usuario usuarioMiembro, Servicio servicio);
	List<Usuario> getIrsByMiembro(Usuario usuarioMiembro);
	List<Usuario> getIrsConConsumosPendientes(Servicio servicio);
	List<Usuario> getIrsByServicio(Servicio servicio);
	List<Usuario> getIrsByListaServicios(List<Servicio> listaServicios);
	List<Usuario> getIrsTodos();
	List<Usuario> getUsuariosByServicio(Servicio servicio);
	List<Usuario> getUsuariosByListaServicio( List<Servicio> servicios );
	List<Usuario> getUsuariosPuedoSolicitarEnSuNombreByServicio( Servicio servicio);
	List<Usuario> getUsuariosGestorPuedeSolicitarCertificacion();
	List<Usuario> getUsuariosSupervisorPuedeSolicitarCertificacion(List<Servicio> listaServicios);
	List<Usuario> getMiembrosByIr(Usuario usuarioIr);
	List<Usuario> getMiembrosByIrServicio(Usuario usuarioIr, Servicio servicio);
	List<Usuario> getUsuariosTodos();
	List<Usuario> getTecnicosByService(Servicio servicio);
	List<Usuario> getTecnicosByServiceProd(Servicio servicio);
	List<Usuario> getTecnicosByProducto(Producto producto);
	List<Usuario> getTecnicosAutomaticosByProducto(Producto producto);
	List<Usuario> getSupervisoresServicio(Servicio servicio);
	List<Usuario> getMiembrosServicio(Servicio servicio);
	boolean avisarIrNuevaSolicitudMiembro(Usuario usuarioMiembro, Usuario usuarioIr, Servicio servicio);
	boolean tienePerfil(Usuario usuario, String tagPerfil);
	boolean tienePerfilEnServicio(Usuario usuario, String tagPerfil, Servicio servicio);

	/**
	 * Busca un usuario en la BBDD dal ACTI que concuerde con el patrón
	 *
	 * @param patron
	 *               {@link String}
	 * @return {@link List}{@link String}
	 */
	List<String> buscaUsuariosACTIByPatron( String patron );

}
