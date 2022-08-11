package es.um.atica.sai.services.interfaces;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.primefaces.model.SortOrder;
import org.primefaces.model.UploadedFile;

import es.um.atica.jpa.das.ResultQuery;
import es.um.atica.sai.dtos.ConsumoCantidad;
import es.um.atica.sai.dtos.EntidadRespuesta;
import es.um.atica.sai.dtos.EstadisticaConsumo;
import es.um.atica.sai.dtos.IrEntidadCantidad;
import es.um.atica.sai.dtos.IrGrupoinvestigacionCantidad;
import es.um.atica.sai.dtos.LineaFungible;
import es.um.atica.sai.dtos.OcupacionTecnico;
import es.um.atica.sai.dtos.OpcionSolicitudTecnico;
import es.um.atica.sai.dtos.OperacionConsumo;
import es.um.atica.sai.dtos.OperacionPresupuesto;
import es.um.atica.sai.dtos.Partida;
import es.um.atica.sai.dtos.ResumenConsumosExcelRespuesta;
import es.um.atica.sai.dtos.Subtercero;
import es.um.atica.sai.entities.Anexo;
import es.um.atica.sai.entities.Consumo;
import es.um.atica.sai.entities.ConsumoEquipo;
import es.um.atica.sai.entities.EntidadPagadora;
import es.um.atica.sai.entities.Equipo;
import es.um.atica.sai.entities.Factura;
import es.um.atica.sai.entities.Muestra;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.Reservas;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.TecnicoProducto;
import es.um.atica.sai.entities.TipoCertificacion;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;


public interface ConsumoService {

	// Consumos genéricos
	String getSqlBusquedaConsumos(String presupuesto, boolean consultarTodasParaTecnico);
	ResultQuery<Consumo> busquedaConsumos(String presupuesto, boolean consultarTodasParaTecnico, int first, int pageSize, String sortField, SortOrder sortOrder);
	Long getCountBusquedaConsumos(String presupuesto, boolean consultarTodasParaTecnico);
	void guardarConsumo(Consumo consumo) throws SaiException;
	void guardarEstadoPresupuestoEHijos(Consumo consumo) throws SaiException;
	void guardarListaConsumos(List<Consumo> listaConsumos);
	Consumo getConsumoById(Long consumo);
	Consumo getConsumoGeneradoFromPresupuesto(Consumo presupuesto);
	byte[] getConsumoHtml(Long codConsumo);
	TecnicoProducto getTecnicoById(long codTecnico);
	Usuario obtenerTecnicoAsignarPrestacion(Consumo consumo);
	Usuario obtenerTecnicoAsignarReserva(Consumo consumo, List<Reservas> listaReservas);
	boolean disponibleTecnicoListaReservas(Usuario usuarioTecnico, List<Reservas> listaReservas);
	boolean requiereTecnico(Consumo consumo);
	boolean esTecnicoSolicitud(Consumo consumo, Servicio servicio);
	boolean esMiembroSolicitud(Consumo consumo, Servicio servicio);
	boolean esIrSolicitud(Consumo consumo, Servicio servicio);
	boolean permitidaOperacion(Consumo c, Servicio s, String tipo, OperacionConsumo operacion);
	boolean permitidaOperacionPresupuesto(Consumo c, Servicio s, OperacionPresupuesto operacion);
	boolean preguntarEnvioMailUsuario(Consumo consumo);
	boolean avisarIrNuevaSolicitudMiembro(Usuario usuarioMiembro, Usuario usuarioIr, Servicio servicio);
	String getDescripcionTipoConsumo(String tipo);
	String getColorEstadoConsumo(Consumo consumo);
	String getColorEstadoPresupuesto(Consumo consumo);
	String getDescripcionEstadoConsumo(Consumo consumo);
	boolean requiereAnexoNivelBioseguridad(Consumo consumo);
	Usuario obtenerSolicitante(Consumo consumo);
	TipoCertificacion obtenerCertificacionNecesaria(Usuario usuario, Producto producto);
	Consumo guardaPresupuestoYGeneraConsumoFromPresupuesto(Consumo presupuesto, 
														   UploadedFile ficheroAnexoBioseguridad, 
														   Consumo consumoPadre,
														   Date fechaActual) throws SaiException;
	Consumo clonaPresupuesto(Consumo presupuesto, 
							 UploadedFile ficheroAnexoBioseguridad,
							 Consumo presupuestoPadre,
							 Date fechaActual) throws SaiException;
	void modificarEntidadPagadoraPresupuesto(Consumo presupuesto) throws SaiException;
	void recargarConsumo(Consumo consumo);

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


	// Consumos fungibles
	void guardarPedidoFungible(List<Consumo> listaConsumos, List<LineaFungible> listaLineasEliminadas) throws SaiException;
	void guardarPedidoFungibleYConsumoPadre(Consumo consumoPadre, List<Consumo> listaConsumos, List<LineaFungible> listaLineasEliminadas) throws SaiException;
	List<Consumo> getFungiblesRelacionados(Date fecha, Usuario usuarioSolicitante);

	// Consumos reservas
	OcupacionTecnico obtenerOcupacionTecnico(Consumo consumo, Date fechaInicio, Date fechaFin);
	String getDescripcionOpcionSolicitudTecnico(Consumo consumo);
	List<OpcionSolicitudTecnico> getListaOpcionesSolicitudTecnico(Consumo consumo);
	void crearPedidoReserva(Consumo consumo, List<Reservas> listaReservas, UploadedFile ficheroAnexo, UploadedFile ficheroAnexoBioseguridad) throws SaiException;
	void modificarPedidoReserva(Consumo consumo, UploadedFile ficheroAnexoBioseguridad, List<Reservas> listaReservas, List<Reservas> listaReservasEliminadas) throws SaiException;
	BigDecimal busquedaImporteConsumosReservaByEquipo(Equipo equipo, String fechaDesde, String fechaHasta);

	// Consumos prestaciones
	void crearPedidoPrestacion(Consumo consumo, UploadedFile ficheroAnexo, UploadedFile ficheroAnexoBioseguridad) throws SaiException;
	void modificarPedidoPrestacion(Consumo consumo, UploadedFile ficheroAnexoBioseguridad) throws SaiException;
	void modificarPresupuestoPrestacion(Consumo consumo, UploadedFile ficheroAnexoBioseguridad) throws SaiException;
	void eliminarMuestra(Muestra m) throws SaiException;
	void guardarMuestra(Muestra m) throws SaiException;
	List<Consumo> getConsumosHijos(Consumo c);
	List<Consumo> getPresupuestosHijosTipoReservaByConsumoGenerado(Consumo c);
	List<ConsumoEquipo> getEquiposByConsumo(Consumo consumo);
	void crearConsumoEquipo(ConsumoEquipo ce) throws SaiException;
	void eliminarConsumoEquipo(ConsumoEquipo ce) throws SaiException;
	BigDecimal busquedaImporteConsumosPrestacionByEquipo(Equipo equipo, String fechaDesde, String fechaHasta);
	
	// Anexos
	void eliminarAnexo(Anexo a) throws SaiException;
	void guardarAnexo(Anexo a) throws SaiException;
	Anexo getAnexoById( Long cod );
	Anexo getAnexoGeneralBioseguridad();
	List<Anexo> getAnexosByConsumo( Consumo c );
	Anexo getAnexoBioseguridadByConsumo( Consumo c );
	List<Anexo> getAnexosConsumoByConsumo( Consumo c );

	// Facturas
	Factura getFacturaByCod(Long codFactura);
	List<Consumo> getConsumosPendientesFacturar(Usuario investigador,
			Servicio servicio,
			EntidadPagadora entidadPagadora,
			boolean soloEstadosFacturables);
	List<Factura> busquedaFactura(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters);
	int getCountBusquedaFactura();
	void crearFactura( Factura factura , List<Consumo> listaConsumosFactura) throws SaiException;
	void modificarFactura( Factura factura, List<Consumo> listaConsumosFactura) throws SaiException;
	void eliminarFactura(Factura factura) throws SaiException;
	EntidadRespuesta enviarFacturaJusto(Factura factura, BigDecimal importe, Partida partida, Subtercero subtercero);
	EntidadRespuesta anularFacturaJusto(Factura factura);
	void recargarFactura(Factura factura);

	// Estimación
	List<Consumo> busquedaConsumosEstimacion();

	// Resumen de consumos
	List<ConsumoCantidad> busquedaConsumoCantidad(String estadoFacturacion, int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters);
	int getCountBusquedaConsumoCantidad(String estadoFacturacion);
	Long getNumIrsConsumoResumen(String estadoFacturacion);
	Long getNumMiembrosConsumoResumen(String estadoFacturacion);
	List<IrEntidadCantidad> busquedaIrEntidadCantidad(String estadoFacturacion, int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters);
	int getCountBusquedaIrEntidadCantidad(String estadoFacturacion);
	List<IrGrupoinvestigacionCantidad> busquedaIrGrupoinvestigacionCantidad(String estadoFacturacion, int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters);
	int getCountBusquedaIrGrupoinvestigacionCantidad(String estadoFacturacion);
	ResumenConsumosExcelRespuesta getResumenConsumosExcel(String tipoDesglose, 
			 											  String tipo,
			 											  Servicio servicio,
			 											  Producto producto,
			 											  String consumoInterno,
			 											  Usuario usuarioInvestigador,
			 											  Usuario usuarioSolicitante,
			 											  String estado,
			 											  String estadoFacturacion,
			 											  String fechaDesdeString,
			 											  String fechaHastaString);
}
