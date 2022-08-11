package es.um.atica.sai.services.interfaces;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.umu.atica.servicios.gesper.gente.entity.Persona;

import es.um.atica.jpa.das.ResultQuery;
import es.um.atica.sai.dtos.EntidadRespuesta;
import es.um.atica.sai.entities.Consumo;
import es.um.atica.sai.entities.DatosUsuarioView;
import es.um.atica.sai.entities.EntidadPagadora;
import es.um.atica.sai.entities.EntidadesIr;
import es.um.atica.sai.entities.Factura;
import es.um.atica.sai.entities.GrupoInvestigacion;
import es.um.atica.sai.entities.Perfil;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.entities.UsuarioPerfil;
import es.um.atica.sai.exceptions.SaiException;


public interface UsuarioService {

	String NAME = "usuarioService";

	// Usuarios
	void guardarUsuario(Usuario usuario) throws SaiException;
	void recargarUsuario(Usuario usuario);

	Usuario autenticacionUsuario(String login);
	Usuario findUsuarioByLogin(String login);
	Usuario findUsuarioByDni(String dniStr);
	Usuario findUsuarioByEmail( String emailStr ) ;
	Usuario getUsuarioById(Long cond);
	Usuario getUsuarioByConsumo(Consumo c);
	Usuario getUsuarioByCod(Long codUsuario);
	DatosUsuarioView findDatosUsuarioByDni(String dniStr);
	DatosUsuarioView findDatosUsuarioByEmail( String emailStr ) ;
	DatosUsuarioView getDatosUsuarioById(Long cod);

	List<Usuario> getUsuariosPendientes();
	List<Usuario> getUsuariosByServicio(Servicio servicio);
	List<Usuario> getUsuariosPuedoSolicitarEnSuNombreByServicio( Servicio servicio);
	List<Usuario> getListaIrsPuedoAsignarEnSolicitud(Usuario usuarioSolicitante, Servicio servicio);
	List<Usuario> getIrsByServicio(Servicio servicio);
	List<Usuario> getIrsByListaServicios(List<Servicio> listaServicios);
	List<Usuario> getIrsParaEstimacion();
	List<Usuario> getIrsConConsumosPendientes(Servicio servicio);
	List<Usuario> getMiembrosByIr(Usuario usuarioIr);
	List<Usuario> getMiembrosByIrServicio(Usuario usuarioIr, Servicio servicio);
	String getListaCodsMiembroByIrServicio(Usuario usuarioIr, Servicio servicio);
	List<Usuario> getListaUsuariosSolicitantes();
	List<Usuario> getTecnicosByServicio(Servicio servicio);
	List<Usuario> getTecnicosByServicioProd(Servicio servicio);
	List<Usuario> getTecnicosByProducto(Producto producto);
	List<Usuario> getUsuariosTodos();
	List<Usuario> getIrsByMiembroServicio(Usuario usuario, Servicio servicio);

	// Acciones usuario
	void aceptaCondicionesActi() throws SaiException;
	void solicitudRegistroIr(Usuario usuario, EntidadPagadora entidad) throws SaiException;
	void solicitudRegistroNuevoIrExterno(Usuario usuario, String password, EntidadPagadora entidad) throws SaiException;
	EntidadRespuesta generarNuevoPassword(String email);
	EntidadRespuesta restablecerPasswordAutenticado(String passwordViejo, String passwordNuevo);
	EntidadRespuesta restablecerPasswordNoAutenticado(String email, String passwordViejo, String passwordNuevo);

	// Asociación entre usuarios IR  y entidades
	List<EntidadesIr> getEntidadesIrByUsuario(Usuario usuario);
	List<GrupoInvestigacion> getGruposInvestigacionByUsuarioEntidadPagadora(Usuario usuario, EntidadPagadora ep);
	void crearEntidadesIr(EntidadesIr eir) throws SaiException;
	void eliminarEntidadesIr(EntidadesIr eir) throws SaiException;

	// UsuarioPerfil y Perfil
	List<Perfil> getPerfilesPuedeFiltrar();
	List<UsuarioPerfil> getPerfilesUsuario(Usuario usuario);
	List<UsuarioPerfil> getPerfilesUsuarioPuedeVerUsuarioConectado(Usuario usuario);
	List<UsuarioPerfil> getPerfilesUsuarioIgnoraEstado(Usuario usuario);
	List<UsuarioPerfil> getPerfilesUsuarioIgnoraEstadoPuedeVerUsuarioConectado(Usuario usuario);
	boolean puedeEliminarPerfil(UsuarioPerfil up);
	boolean tienePerfil(Usuario usuario, String tagPerfil);
	boolean tienePerfilEnServicio(Usuario usuario, String tagPerfil, Servicio servicio);
	boolean tienePerfilIrIgnoraEstado(Usuario usuario);
	boolean existeEnGente(String identificador);
	boolean esTrabajadorUmu(String dni);
	boolean esTrabajadorUmuByDescripcion(String descripcion);
	boolean esIrFactura(Factura factura);
	boolean esSupervisorFactura(Factura factura);
	String getDescripcionUsuarioPerfil(UsuarioPerfil up);
	List<String> getListaDescripcionesUsuarioPerfil(Usuario usuario);
	List<String> getListaDescripcionesUsuarioPerfilPuedeVerUsuarioConectado(Usuario usuario);
	List<Perfil> getPerfilesPuedeCrearByPerfil(Perfil perfil);
	List<Perfil> getListaPerfiles();
	Perfil getPerfilByTag(String tagPerfil);
	void guardarUsuarioPerfil(UsuarioPerfil up) throws SaiException;
	void eliminarUsuarioPerfil(UsuarioPerfil up) throws SaiException;

	// Búsqueda de usuarios
	String getSqlBusquedaUsuarios();
	ResultQuery<DatosUsuarioView> busquedaUsuarios(int first, int pageSize, String sortField, SortOrder sortOrder);
	Long getCountBusquedaUsuarios();
	ResultQuery<DatosUsuarioView> busquedaUsuariosIr(int first, int pageSize, String sortField, SortOrder sortOrder);
	Long getCountBusquedaUsuariosIr();
	List<String> buscaUsuariosByPatron(String patron);

	/**
	 * Busca un usuario en la BBDD dal ACTI que concuerde con el patrón
	 *
	 * @param patron
	 *               {@link String}
	 * @return {@link List}{@link String}
	 */
	List<String> buscaUsuariosACTIByPatron( String patron );
	Persona getPersonaByDescripcion(String descripcion);
	Usuario getUsuarioByDescripcion(String descripcion);
	String getEmailByDescripcion(String descripcion);

	/**
	 * Obtiene una lista de {@link Usuario} en función de una lista de {@link Servicio}
	 *
	 * @param servicios
	 *                  {@link List}{@link Servicio}
	 * @return {@link List}{@link Usuario}
	 */
	List<Usuario> getUsuariosByListaServicio( List<Servicio> servicios );


}
