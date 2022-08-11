package es.um.atica.sai.services.interfaces;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import org.primefaces.model.SortOrder;

import es.um.atica.sai.dtos.Dependencia;
import es.um.atica.sai.dtos.EquipoProductividad;
import es.um.atica.sai.entities.Consumo;
import es.um.atica.sai.entities.Equipo;
import es.um.atica.sai.entities.HorarioDia;
import es.um.atica.sai.entities.Intervencion;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.ReservaEspera;
import es.um.atica.sai.entities.ReservableHorario;
import es.um.atica.sai.entities.Reservas;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.TipoHorario;
import es.um.atica.sai.entities.TipoReservable;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;


@Local
public interface ReservableService {

	String NAME = "reservableService";

	List<Producto> getProductosByTipoReservable(TipoReservable tr);

	// Tipos de reservables, reservables y horarios de reservables
	List<TipoReservable> buscarTiposReservables();
	TipoReservable getTipoReservableById(Long codigo);
	void eliminarReservableHorario(ReservableHorario rh) throws SaiException;
	void crearTipoReservableHorario(ReservableHorario rh) throws SaiException;
	void crearProductoHorario(ReservableHorario rh) throws SaiException;
	void guardarEquipo( Equipo equipo ) throws SaiException;
	void eliminarEquipo( Equipo equipo ) throws SaiException;
	void guardarTipoReservable(TipoReservable tr) throws SaiException;
	List<Equipo> getReservablesByTipoReservable(TipoReservable tipoReservable);
	List<ReservableHorario> getListaReservableHorarioByTipoReservable(TipoReservable tr);
	List<ReservableHorario> getListaReservableHorarioByProducto(Producto producto);

	// Equipos para prestaciones
	List<Equipo> getEquiposByServicio(Servicio servicio);

	List<EquipoProductividad> getInformeProductividad(Servicio servicio, List<Equipo> listaEquipos, String fechaDesde, String fechaHasta);

	// Reservas
	boolean existeHorarioDiaByReservableFecha(Equipo reservable, Date fecha);
	HorarioDia getHorarioDiaByTipohorarioDia(TipoHorario th, int dia);
	List<Reservas> getReservasPorTurno(Equipo reservable, Date fechaInicio, Date fechaFin);
	List<Reservas> getReservasPorTurnoByTipoReservable(TipoReservable tr, Date fechaInicio, Date fechaFin);
	List<Reservas> getReservasPorTurnoTecnico(Usuario usuarioTecnicoAsignado, Date fechaInicio, Date fechaFin);
	boolean disponibleReservable(Equipo reservable, Date fechaInicio, Date fechaFin);
	boolean disponibleTecnico(Usuario usuarioTecnico, Date fechaInicio, Date fechaFin);
	boolean disponibleTecnicoModificacionReserva(Usuario usuarioTecnico, Long codReserva, Date fechaInicio, Date fechaFin);
	TipoHorario getTipoHorarioByTipoReservableFecha(TipoReservable tr, Date fecha);
	TipoHorario getTipoHorarioByProductoFecha(Producto pr, Date fecha);
	HorarioDia getHorarioDiaByProductoFecha(Producto pr, Date fecha);
	List<Reservas> getReservasPorConsumo(Consumo c);
	List<Reservas> getReservasActivasPorConsumo(Consumo c);
	Date getMinFechaInicioReservaByConsumo(Consumo c);
	List<ReservaEspera> getReservaEsperaByReserva(Reservas r);
	ReservaEspera getReservaEsperaByUsuarioReserva(Usuario u, Reservas r);
	void guardarReservaEspera(ReservaEspera r) throws SaiException;
	void eliminarReservaEspera(ReservaEspera r) throws SaiException;
	String getMinHoraInicioMananaByProductoReservable(Producto producto);
	String getMinHoraInicioTardeByProductoReservable(Producto producto);
	String getMaxHoraFinMananaByProductoReservable(Producto producto);
	String getMaxHoraFinTardeByProductoReservable(Producto producto);
	boolean existeHorarioDia(TipoReservable tr);
	List<Equipo> getListaReservablesByProducto(Producto producto);

	// Tipos de horarios y horarios diarios
	String getNombreDiaSemana(int dia);
	List<HorarioDia> getListaHorarioDiaByTipohorario(TipoHorario th);
	String getDescripcionHorarioDia(HorarioDia hd);
	String getDescripcionTurno(HorarioDia hd, String tipoTurno);
	List<TipoHorario> buscarTiposHorarios();
	List<TipoHorario> getListaTiposHorario();
	List<TipoHorario> getListaTiposHorarioByServicio(Servicio servicio);
	List<TipoHorario> getListaTipoHorarioByUsuario(Usuario usuario);
	List<TipoReservable> getListaTiposReservableByTipoHorario(TipoHorario tipoHorario);
	void guardarTipoHorario(TipoHorario tipoHorario) throws SaiException;
	void eliminarTipoHorario(TipoHorario tipoHorario) throws SaiException;
	void guardarHorarioDia(HorarioDia hd) throws SaiException;
	void eliminarHorarioDia(HorarioDia hd) throws SaiException;

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
	 * Guarda una {@link Intervencion} en BBDD esté o no ya en ella
	 *
	 * @param i
	 *          {@link Intervencion}
	 * @throws SaiException
	 */
	void guardarIntervencion( Intervencion i ) throws SaiException;

	/**
	 * Elimina una {@link Intervencion} de la BBDD
	 *
	 * @param i
	 *          {@link Intervencion}
	 * @throws SaiException
	 */
	void eliminarIntervencion( Intervencion i ) throws SaiException;

	// Dependencias
	List<String> getListaNombresDependenciaByPatron(String patron);
	Dependencia getDependenciaByDescripcion(String descripcion);
	String getNombreDependencia(String tipo, String codigo, String bloque, String planta, Integer numDep);

	/**
	 * Devuelve una lista de tipos de reservables en funcion del perfil de usuario conectado.
	 *
	 * @return
	 */
	List<TipoReservable> getListaTiposReservableByUsuarioConectado();

	/**
	 * Actualiza una entidad Equipo con la base de datos
	 *
	 * @param e
	 *          {@link Equipo}
	 */
	void refreshEquipo( Equipo e );

}
