package es.um.atica.sai.das.interfaces;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import org.primefaces.model.SortOrder;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.jpa.das.ResultQuery;
import es.um.atica.sai.entities.Consumo;
import es.um.atica.sai.entities.EntidadPagadora;
import es.um.atica.sai.entities.Equipo;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.Proyecto;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;

@Local
public interface ConsumoDAS extends DataAccessService<Consumo>{

	List<Consumo> busquedaConsumosEstimacion();

	void guardar( Consumo consumo ) throws SaiException;

	void crear(Consumo consumo) throws SaiException;
	void modificar(Consumo consumo) throws SaiException;
	void eliminar(Consumo consumo) throws SaiException;

	Consumo getConsumoGeneradoFromPresupuesto(Consumo presupuesto);
	List<Consumo> getFungiblesRelacionados(Date fecha, Usuario usuarioSolicitante );
	List<Consumo> getConsumosPendientesFacturar(Usuario investigador,
												Servicio servicio,
												EntidadPagadora entidadPagadora,
												boolean soloEstadosFacturables);
	List<Consumo> getConsumosHijos(Consumo c);
	List<Consumo> getPresupuestosHijosTipoReservaByConsumoGenerado(Consumo c);
	List<Consumo> getConsumosActivosByProyecto(Proyecto proyecto);
	List<Consumo> getConsumosAnuladosByProyecto(Proyecto proyecto);
	List<Consumo> getConsumosActivosByProyectoProducto(Proyecto proyecto, Producto producto);
	List<Consumo> getConsumosAnuladosByProyectoProducto(Proyecto proyecto, Producto producto);
	BigDecimal getCantidadConsumidaByProyecto(Proyecto proyecto);
	BigDecimal getCantidadConsumidaByProyectoProducto(Proyecto proyecto, Producto producto);
	BigDecimal getCantidadConsumidaByProyectoProductoEdicion(Proyecto proyecto, Producto producto, List<Long> listaCodsConsumo);
	BigDecimal getStockConsumidoByProducto(Producto producto);
	BigDecimal getStockConsumidoByProductoEdicion(Producto producto, List<Long> listaCodsConsumo);
	
	ResultQuery<Consumo> busquedaConsumos(String sql, int first, int pageSize, String sortField, SortOrder sortOrder);
	Long getCountBusquedaConsumos(String sql);
	
	Integer busquedaSumMinutosReservaNoInternosByEquipo(Equipo equipo, String fechaDesde, String fechaHasta);
	Integer busquedaSumMinutosPrestacionNoInternosByEquipo(Equipo equipo, String fechaDesde, String fechaHasta);
	Integer busquedaNumUnidadesReservaNoInternosByEquipo(Equipo equipo, String fechaDesde, String fechaHasta);
	Integer busquedaNumUnidadesPrestacionNoInternosByEquipo(Equipo equipo, String fechaDesde, String fechaHasta);
	List<Consumo> busquedaConsumosReservaNoInternosByEquipo(Equipo equipo, String fechaDesde, String fechaHasta);
	List<Consumo> busquedaConsumosPrestacionNoInternosByEquipo(Equipo equipo, String fechaDesde, String fechaHasta);
}
