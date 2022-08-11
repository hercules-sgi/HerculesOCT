package es.um.atica.sai.das.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.jboss.seam.annotations.Name;
import org.primefaces.model.SortOrder;

import es.um.atica.commons.utils.UtilString;
import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.jpa.das.ResultQuery;
import es.um.atica.sai.das.interfaces.ConsumoDAS;
import es.um.atica.sai.entities.Consumo;
import es.um.atica.sai.entities.EntidadPagadora;
import es.um.atica.sai.entities.Equipo;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.Proyecto;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;
import es.um.atica.seam.framework.Query.QueryPriority;


@Name("consumoDAS")
@Stateless
@Local(ConsumoDAS.class)
public class ConsumoDASImpl extends  DataAccessServiceImpl<Consumo> implements ConsumoDAS {

	private static final String PRODUCTO_SERVICIO = "con.producto.servicio";
	private static final String PRODUCTO_USUARIOIR = "con.usuarioIrAsociado";
	private static final String FECHA_SOLICITUD_MAYOR = "con.fechaSolicitud >=";
	private static final String FECHA_SOLICITUD_MENOR = "con.fechaSolicitud <=";
	private static final String TO_DATE = "TO_DATE(";
	private static final String DATE_FORMAT = ",'dd/MM/yyyy')";

	private static final String FECHA_SOLICITUD_MAYOR_NATIVA = " AND c.fecha_solicitud >=";
	private static final String FECHA_SOLICITUD_MENOR_NATIVA = " AND c.fecha_solicitud <";
	private static final String TO_DATE_COMILLA = "TO_DATE('";
	private static final String DATE_FORMAT_NATIVA = "', 'dd/mm/yyyy')";
	private static final String DATE_FORMAT_COMILLA = "', 'dd/MM/yyyy')";
	
	private static final String PARAM_CODEQUIPO = "codequipo";
	
	private static final String[] RESTRICCIONES = {
			UtilString.cmpNumberEjbql("con.cod","#{misSolicitudes.codigoBuscar}"),
			UtilString.cmpTextFilterEjbql("con.tipo", "#{misSolicitudes.tipoBuscar}"),
			UtilString.cmpNumberEjbql( PRODUCTO_SERVICIO, "#{misSolicitudes.servicioBuscar}" ),
			UtilString.cmpNumberEjbql("con.producto","#{misSolicitudes.productoBuscar}"),
			UtilString.cmpTextFilterEjbql("con.interno", "#{misSolicitudes.consumoInternoBuscar}"),
			UtilString.cmpNumberEjbql("con.usuarioTecnicoAsignado","#{misSolicitudes.tecnicoAsignadoBuscar}"),
			UtilString.cmpNumberEjbql( PRODUCTO_USUARIOIR, "#{misSolicitudes.irAsignadoBuscar}" ),
			UtilString.cmpNumberEjbql("con.usuarioSolicitante","#{misSolicitudes.solicitanteBuscar}"),
			UtilString.cmpTextFilterEjbql("con.estado", "#{misSolicitudes.estadoBuscar}"),
			UtilString.cmpTextFilterEjbql("con.estadoPresupuesto", "#{misSolicitudes.estadoPresupuestoBuscar}"),
			new StringBuilder(FECHA_SOLICITUD_MAYOR).append(TO_DATE).append("#{misSolicitudes.fechaSolicitudDesdeBuscarString}").append(DATE_FORMAT).toString(),
			new StringBuilder(FECHA_SOLICITUD_MENOR).append(TO_DATE).append("#{misSolicitudes.fechaSolicitudHastaBuscarString}").append(DATE_FORMAT).toString(),
			new StringBuilder("con.fechaResolucion >=").append(TO_DATE).append("#{misSolicitudes.fechaResolucionDesdeBuscarString}").append(DATE_FORMAT).toString(),
			new StringBuilder("con.fechaResolucion <=").append(TO_DATE).append("#{misSolicitudes.fechaResolucionHastaBuscarString}").append(DATE_FORMAT).toString(),
			new StringBuilder("EXISTS (SELECT res FROM Reservas res WHERE res.consumo=con AND res.estado != 'Anulado' AND res.fechaFin>").append(TO_DATE).append("#{misSolicitudes.fechaReservasDesdeBuscarString}").append(",'dd/MM/yyyy'))").toString(),
			new StringBuilder("EXISTS (SELECT res FROM Reservas res WHERE res.consumo=con AND res.estado != 'Anulado' AND res.fechaInicio<").append(TO_DATE).append("#{misSolicitudes.fechaReservasHastaBuscarString}").append(",'dd/MM/yyyy'))").toString(),
			UtilString.cmpTextFilterEjbql("con.presupuesto", "#{misSolicitudes.presupuestoBuscar}")
	};

	private static final String[] RESTRICCIONES_PENDIENTES_FACTURACION = {
			"(con.cod=#{facturacionEdit.codFiltrarConsumosPendientes} OR con.consumoPadre.cod=#{facturacionEdit.codFiltrarConsumosPendientes})",
			UtilString.cmpTextFilterEjbql("con.producto.tipo", "#{facturacionEdit.tipoFiltrarConsumosPendientes}"),
			new StringBuilder(FECHA_SOLICITUD_MAYOR).append(TO_DATE).append("#{facturacionEdit.fechaDesdeStringFiltrarConsumosPendientes}").append(DATE_FORMAT).toString(),
			new StringBuilder(FECHA_SOLICITUD_MENOR).append(TO_DATE).append("#{facturacionEdit.fechaHastaStringFiltrarConsumosPendientes}").append(DATE_FORMAT).toString()
	};

	private static final String [] RESTRICCIONES_ESTIMACION = {
			UtilString.cmpNumberEjbql( PRODUCTO_USUARIOIR, "#{estimacion.usuarioIrFiltrar}" ),
			UtilString.cmpNumberEjbql("con.entidadPagadora","#{estimacion.entidadFiltrar}"),
			UtilString.cmpTextFilterEjbql( PRODUCTO_SERVICIO, "#{estimacion.servicioFiltrar}" ),
			UtilString.cmpTextFilterEjbql("con.estado","#{estimacion.estadoFiltrar}" ),
			UtilString.cmpTextFilterEjbql("con.tipo", "#{estimacion.tipoFiltrar}"),
	};


	private static List<String> getListaRestricciones()
	{
		return Arrays.asList(RESTRICCIONES);
	}

	private static List<String> getListaRestriccionesPendientesFacturacion()
	{
		return Arrays.asList(RESTRICCIONES_PENDIENTES_FACTURACION);
	}

	private static List<String> getListaRestriccionesEstimacion()
	{
		return Arrays.asList(RESTRICCIONES_ESTIMACION);
	}

	@Override
	public List<Consumo> busquedaConsumosEstimacion()
	{
		final Map<String, Object> parametros = new HashMap<>();
		try
		{
			return this.findByEntityNamedQueryWithDinamicFilter( Consumo.GET_CONSUMOS_ESTIMACION, getListaRestriccionesEstimacion(), parametros, null, null, null, "and", null , null, null, null );
		}
		catch (final Exception ex)
		{
			log.error("Error en getConsumosEstimacion: #0",ex.toString());
			return new ArrayList<>();
		}
	}

	@Override
	public void guardar(Consumo consumo) throws SaiException {
		try{
			if (consumo.getCod() == null)
			{
				this.persist( consumo, true);
			}
			else
			{
				this.update( consumo, true );
				refresh( consumo );
			}
			log.info( "Consumo guardado correctamente cod=#0", consumo.getCod() );

		} catch ( final Exception e ) {
			log.error( "Error inesperado. No se puede guardar el consumo. Error:", e );
			throw new SaiException( "Error al guardar un consumo: " + e.getMessage() );
		}
	}

	@Override
	public void crear(Consumo consumo) throws SaiException
	{
		try
		{
			this.persist( consumo, true);
		}
		catch ( final Exception e )
		{
			log.error( "Error al crear consumo:", e );
			throw new SaiException(e.getMessage());
		}
	}

	@Override
	public void modificar(Consumo consumo) throws SaiException
	{
		try
		{
			this.update( consumo, true);
		}
		catch ( final Exception e )
		{
			log.error( "Error al modificar consumo:", e );
			throw new SaiException(e.getMessage());
		}
	}

	@Override
	public void eliminar(Consumo consumo) throws SaiException
	{
		try
		{
			this.delete( consumo, true );
		}
		catch ( final Exception e )
		{
			log.error( "Error al eliminar consumo:", e );
			throw new SaiException(e.getMessage());
		}
	}

	@SuppressWarnings( "unchecked" )
	@Override
	public Consumo getConsumoGeneradoFromPresupuesto(Consumo presupuesto)
	{
		try 
		{
			return (Consumo)entityManager.createNamedQuery(Consumo.GET_CONSUMO_GENERADO_FROM_PRESUPUESTO).setParameter("presupuesto", presupuesto).getSingleResult();
		}
		catch(final Exception ex) 
		{
			return null;
		}
	}

	@SuppressWarnings( "unchecked" )
	@Override
	public List<Consumo> getFungiblesRelacionados(Date fecha, Usuario usuarioSolicitante )
	{
		log.info( "Recuperando fungibles relacionados para fecha #0 y usuario #1", fecha.toString(), usuarioSolicitante.getDni() );
		try {
			return entityManager.createNamedQuery(Consumo.GET_FUNGIBLES_BY_DATE_SOLIC)
					.setParameter("fecha", fecha)
					.setParameter("usuariosolicitante", usuarioSolicitante)
					.getResultList();
		}
		catch(final Exception ex)
		{
			log.error( "Error al recuperar fungibles relacionados #0", ex.toString() );
			return new ArrayList<>();
		}
	}

	@Override
	public List<Consumo> getConsumosPendientesFacturar(Usuario investigador,
			Servicio servicio,
			EntidadPagadora entidadPagadora,
			boolean soloEstadosFacturables)
	{
		String namedQuery = Consumo.GET_CONSUMOS_PENDIENTES_IR_SERVICIO_ENTIDAD;
		if (soloEstadosFacturables)
		{
			namedQuery = Consumo.GET_CONSUMOS_PENDIENTES_IR_SERVICIO_ENTIDAD_SOLO_ESTADOS_FACTURABLES;
		}
		final Map<String, Object> parametros = new HashMap<>();
		parametros.put("usuarioir", investigador );
		parametros.put("servicio", servicio );
		parametros.put("entidadpagadora", entidadPagadora);
		try
		{
			return this.findByEntityNamedQueryWithDinamicFilter(namedQuery , getListaRestriccionesPendientesFacturacion(), parametros, null, null, null, "and", null , null, null, null );
		}
		catch (final Exception ex)
		{
			log.error("Error en getConsumosPendientesFacturar: #0",ex.toString());
			return new ArrayList<>();
		}
	}

	@SuppressWarnings( "unchecked" )
	@Override
	public List<Consumo> getConsumosHijos( Consumo c ) 
	{
		try 
		{
			return entityManager.createNamedQuery(Consumo.GET_CONSUMOS_HIJOS).setParameter("consumo", c).getResultList();
		}
		catch(final Exception ex) 
		{
			return new ArrayList<>();
		}
	}
	
	@SuppressWarnings( "unchecked" )
	@Override
	public List<Consumo> getPresupuestosHijosTipoReservaByConsumoGenerado(Consumo c) 
	{
		try 
		{
			return entityManager.createNamedQuery(Consumo.GET_PRESUPUESTOSHIJOS_TIPORESERVA_BY_PRESUPUESTO).setParameter("presupuestoorigen", c.getPresupuestoOrigen()).getResultList();
		}
		catch(final Exception ex) 
		{
			return new ArrayList<>();
		}
	}
	

	@Override
	public List<Consumo> getConsumosActivosByProyecto(Proyecto proyecto)
	{
		try 
		{
			return entityManager.createNamedQuery(Consumo.GET_CONSUMOS_ACTIVOS_BY_PROYECTO).setParameter("proyecto", proyecto).getResultList();
		}
		catch(final Exception ex) 
		{
			return new ArrayList<>();
		}
	}

	@Override
	public List<Consumo> getConsumosAnuladosByProyecto(Proyecto proyecto)
	{
		try 
		{
			return entityManager.createNamedQuery(Consumo.GET_CONSUMOS_ANULADOS_BY_PROYECTO).setParameter("proyecto", proyecto).getResultList();
		}
		catch(final Exception ex) 
		{
			return new ArrayList<>();
		}
	}
	
	@Override
	public List<Consumo> getConsumosActivosByProyectoProducto(Proyecto proyecto, Producto producto)
	{
		try 
		{
			return entityManager.createNamedQuery(Consumo.GET_CONSUMOS_ACTIVOS_BY_PROYECTO_PRODUCTO).setParameter("proyecto", proyecto).setParameter("producto", producto).getResultList();
		}
		catch(final Exception ex) 
		{
			return new ArrayList<>();
		}
	}

	@Override
	public List<Consumo> getConsumosAnuladosByProyectoProducto(Proyecto proyecto, Producto producto)
	{
		try 
		{
			return entityManager.createNamedQuery(Consumo.GET_CONSUMOS_ANULADOS_BY_PROYECTO_PRODUCTO).setParameter("proyecto", proyecto).setParameter("producto", producto).getResultList();
		}
		catch(final Exception ex) 
		{
			return new ArrayList<>();
		}
	}

	@Override
	public BigDecimal getCantidadConsumidaByProyecto(Proyecto proyecto)
	{
		try 
		{
			BigDecimal cantidadConsumida = (BigDecimal)entityManager.createNamedQuery(Consumo.GET_CANTIDAD_CONSUMIDA_BY_PROYECTO).setParameter("proyecto", proyecto).getSingleResult();
			if (cantidadConsumida == null)
			{
				cantidadConsumida = BigDecimal.valueOf(0);
			}
			return cantidadConsumida;
		}
		catch(final Exception ex) 
		{
			return BigDecimal.valueOf(0);
		}
	}
	
	@Override
	public BigDecimal getCantidadConsumidaByProyectoProducto(Proyecto proyecto, Producto producto)
	{
		try 
		{
			BigDecimal cantidadConsumida = (BigDecimal)entityManager.createNamedQuery(Consumo.GET_CANTIDAD_CONSUMIDA_BY_PROYECTO_PRODUCTO).setParameter("proyecto", proyecto).setParameter("producto", producto).getSingleResult();
			if (cantidadConsumida == null)
			{
				cantidadConsumida = BigDecimal.valueOf(0);
			}
			return cantidadConsumida;
		}
		catch(final Exception ex) 
		{
			return BigDecimal.valueOf(0);
		}
	}
	
	@Override
	public BigDecimal getCantidadConsumidaByProyectoProductoEdicion(Proyecto proyecto, Producto producto, List<Long> listaCodsConsumo)
	{
		try 
		{
			BigDecimal cantidadConsumida = (BigDecimal)entityManager.createNamedQuery(Consumo.GET_CANTIDAD_CONSUMIDA_BY_PROYECTO_PRODUCTO_NOT_LISTACONSUMOS).setParameter("proyecto", proyecto).setParameter("producto", producto).setParameter("listacodsconsumo", listaCodsConsumo).getSingleResult();
			if (cantidadConsumida == null)
			{
				cantidadConsumida = BigDecimal.valueOf(0);
			}
			return cantidadConsumida;
		}
		catch(final Exception ex) 
		{
			return BigDecimal.valueOf(0);
		}
	}
	
	@Override
	public BigDecimal getStockConsumidoByProducto(Producto producto)
	{
		try 
		{
			BigDecimal stockConsumido = (BigDecimal)entityManager.createNamedQuery(Consumo.GET_STOCK_CONSUMIDO_BY_PRODUCTO).setParameter("producto", producto).getSingleResult();
			if (stockConsumido == null)
			{
				stockConsumido = BigDecimal.valueOf(0);
			}
			return stockConsumido;
		}
		catch(final Exception ex) 
		{
			return BigDecimal.valueOf(0);
		}
	}
	
	@Override
	public BigDecimal getStockConsumidoByProductoEdicion(Producto producto, List<Long> listaCodsConsumo)
	{
		try 
		{
			BigDecimal stockConsumido = (BigDecimal)entityManager.createNamedQuery(Consumo.GET_STOCK_CONSUMIDO_BY_PRODUCTO_NOT_LISTACONSUMOS).setParameter("producto", producto).setParameter("listacodsconsumo", listaCodsConsumo).getSingleResult();
			if (stockConsumido == null)
			{
				stockConsumido = BigDecimal.valueOf(0);
			}
			return stockConsumido;
		}
		catch(final Exception ex) 
		{
			return BigDecimal.valueOf(0);
		}		
	}

	
	@Override
	public ResultQuery<Consumo> busquedaConsumos(String sql, int first, int pageSize, String sortField, SortOrder sortOrder)
	{
		if (sql == null)
		{
			return null;
		}
		final HashMap<String,Object> parameters  = new HashMap<>();
		final HashMap<String,Object> hints  = new HashMap<>();
		try
		{
			return this.resultsByEntityQueryWithDinamicFilter(sql, getListaRestricciones(), parameters, first, pageSize, sortField, sortOrder.name(), "and", hints, null, QueryPriority.BOTH, QueryPriority.QUERY);
		}
		catch(final Exception ex)
		{
			log.error("Error en busquedaConsumos: ", ex );
			return null;
		}
	}

	@Override
	public Long getCountBusquedaConsumos(String sql)
	{
		if (sql == null)
		{
			return ( long ) 0;
		}
		final HashMap<String,Object> parameters  = new HashMap<>();
		final HashMap<String,Object> hints  = new HashMap<>();
		try
		{
			return this.countTotalRecordByEntityQuery(sql, getListaRestricciones(), parameters, "and", hints );
		}
		catch(final Exception ex)
		{
			return ( long ) 0;
		}
	}
	
	
	@Override
	public Integer busquedaSumMinutosReservaNoInternosByEquipo(Equipo equipo, String fechaDesde, String fechaHasta)
	{
		String sql = this.getSqlNativaGetSumMinutosReservaNoInternosByEquipo(fechaDesde, fechaHasta);
		try
		{
			BigDecimal resultado = (BigDecimal)getEntityManager().createNativeQuery(sql).setParameter(PARAM_CODEQUIPO, equipo.getCod()).getSingleResult();
			if (resultado == null)
			{
				return Integer.valueOf(0);
			}
			return resultado.intValue();
		}
		catch (Exception ex)
		{
			return Integer.valueOf(0);
		}
	}
	
	private String getSqlNativaGetSumMinutosReservaNoInternosByEquipo(String fechaDesde, String fechaHasta)
	{
		StringBuilder sql = new StringBuilder("SELECT SUM((R.fecha_fin - R.fecha_inicio)*24*60) FROM sai.consumo C, sai.reservas R WHERE C.cod=R.cod_consumo AND C.estado='Finalizado' AND C.interno='NO' AND C.presupuesto='NO' AND R.estado='Aceptada' AND R.cod_reservable=:codequipo");
		if (fechaDesde != null)
		{
			sql.append(FECHA_SOLICITUD_MAYOR_NATIVA).append(TO_DATE_COMILLA).append(fechaDesde).append(DATE_FORMAT_NATIVA);
		}
		if (fechaHasta != null)
		{
			sql.append(FECHA_SOLICITUD_MENOR_NATIVA).append(TO_DATE_COMILLA).append(fechaHasta).append(DATE_FORMAT_NATIVA).append(" +1");
		}
		return sql.toString();
	}

	
	@Override
	public Integer busquedaSumMinutosPrestacionNoInternosByEquipo(Equipo equipo, String fechaDesde, String fechaHasta)
	{
		String sql = this.getSqlNativaGetSumMinutosPrestacionNoInternosByEquipo(fechaDesde, fechaHasta);
		try
		{
			BigDecimal resultado = (BigDecimal)getEntityManager().createNativeQuery(sql).setParameter(PARAM_CODEQUIPO, equipo.getCod()).getSingleResult();
			if (resultado == null)
			{
				return Integer.valueOf(0);
			}
			return resultado.intValue();
		}
		catch (Exception ex)
		{
			return Integer.valueOf(0);
		}
	}
	
	private String getSqlNativaGetSumMinutosPrestacionNoInternosByEquipo(String fechaDesde, String fechaHasta)
	{
		StringBuilder sql = new StringBuilder("SELECT SUM(PE.minutos * C.cantidad) FROM sai.consumo C, sai.producto_equipo PE WHERE C.cod_producto = PE.cod_producto AND C.estado='Finalizado' AND C.interno='NO' AND C.presupuesto='NO' AND PE.cod_equipo=:codequipo");
		if (fechaDesde != null)
		{
			sql.append(FECHA_SOLICITUD_MAYOR_NATIVA).append(TO_DATE_COMILLA).append(fechaDesde).append(DATE_FORMAT_NATIVA);
		}
		if (fechaHasta != null)
		{
			sql.append(FECHA_SOLICITUD_MENOR_NATIVA).append(TO_DATE_COMILLA).append(fechaHasta).append(DATE_FORMAT_NATIVA).append(" +1");
		}
		return sql.toString();
	}

	@Override
	public Integer busquedaNumUnidadesReservaNoInternosByEquipo(Equipo equipo, String fechaDesde, String fechaHasta)
	{
		String sql = this.getSqlNativaGetNumUnidadesReservaNoInternosByEquipo(fechaDesde, fechaHasta);
		try
		{
			BigDecimal resultado = (BigDecimal)getEntityManager().createNativeQuery(sql).setParameter(PARAM_CODEQUIPO, equipo.getCod()).getSingleResult();
			if (resultado == null)
			{
				return Integer.valueOf(0);
			}
			return resultado.intValue();
		}
		catch (Exception ex)
		{
			return Integer.valueOf(0);
		}
	}
	
	private String getSqlNativaGetNumUnidadesReservaNoInternosByEquipo(String fechaDesde, String fechaHasta)
	{
		StringBuilder sql = new StringBuilder("SELECT COUNT(DISTINCT R.cod) FROM sai.consumo C, sai.reservas R WHERE C.cod=R.cod_consumo AND C.estado='Finalizado' AND C.interno='NO' AND C.presupuesto='NO' AND R.estado='Aceptada' AND R.cod_reservable=:codequipo");
		if (fechaDesde != null)
		{
			sql.append(FECHA_SOLICITUD_MAYOR_NATIVA).append(TO_DATE_COMILLA).append(fechaDesde).append(DATE_FORMAT_NATIVA);
		}
		if (fechaHasta != null)
		{
			sql.append(FECHA_SOLICITUD_MENOR_NATIVA).append(TO_DATE_COMILLA).append(fechaHasta).append(DATE_FORMAT_NATIVA).append(" +1");
		}
		return sql.toString();
	}
	
	@Override
	public Integer busquedaNumUnidadesPrestacionNoInternosByEquipo(Equipo equipo, String fechaDesde, String fechaHasta)
	{
		String sql = this.getSqlNativaGetNumUnidadesPrestacionNoInternosByEquipo(fechaDesde, fechaHasta);
		try
		{
			BigDecimal resultado = (BigDecimal)getEntityManager().createNativeQuery(sql).setParameter(PARAM_CODEQUIPO, equipo.getCod()).getSingleResult();
			if (resultado == null)
			{
				return Integer.valueOf(0);
			}
			return resultado.intValue();
		}
		catch (Exception ex)
		{
			return Integer.valueOf(0);
		}
	}
	
	private String getSqlNativaGetNumUnidadesPrestacionNoInternosByEquipo(String fechaDesde, String fechaHasta)
	{
		StringBuilder sql = new StringBuilder("SELECT SUM(C.cantidad) FROM sai.consumo C, sai.producto_equipo PE WHERE C.cod_producto = PE.cod_producto AND C.estado='Finalizado' AND C.interno='NO' AND C.presupuesto='NO' AND PE.cod_equipo=:codequipo");
		if (fechaDesde != null)
		{
			sql.append(FECHA_SOLICITUD_MAYOR_NATIVA).append(TO_DATE_COMILLA).append(fechaDesde).append(DATE_FORMAT_NATIVA);
		}
		if (fechaHasta != null)
		{
			sql.append(FECHA_SOLICITUD_MENOR_NATIVA).append(TO_DATE_COMILLA).append(fechaHasta).append(DATE_FORMAT_NATIVA).append(" +1");
		}
		return sql.toString();
	}
	
	public List<Consumo> busquedaConsumosReservaNoInternosByEquipo(Equipo equipo, String fechaDesde, String fechaHasta)
	{
		String sql = this.getSqlGetConsumosReservaNoInternosByEquipo(fechaDesde, fechaHasta);
		try
		{
			return (List<Consumo>)getEntityManager().createQuery(sql).setParameter(PARAM_CODEQUIPO, equipo.getCod()).getResultList();
		}
		catch (Exception ex)
		{
			return new ArrayList<>();
		}
	}
	
	private String getSqlGetConsumosReservaNoInternosByEquipo(String fechaDesde, String fechaHasta)
	{
		StringBuilder sql = new StringBuilder("SELECT DISTINCT res.consumo FROM Reservas res WHERE res.consumo.estado='Finalizado' AND res.consumo.interno='NO' AND res.consumo.presupuesto='NO' AND res.estado='Aceptada' AND res.reservable.cod=:codequipo");
		if (fechaDesde != null)
		{
			sql.append(" AND res.consumo.fechaSolicitud >=").append(TO_DATE_COMILLA).append(fechaDesde).append(DATE_FORMAT_COMILLA);
		}
		if (fechaHasta != null)
		{
			sql.append(" AND res.consumo.fechaSolicitud <").append(TO_DATE_COMILLA).append(fechaHasta).append(DATE_FORMAT_COMILLA).append(" +1");
		}
		return sql.toString();
	}
	
	public List<Consumo> busquedaConsumosPrestacionNoInternosByEquipo(Equipo equipo, String fechaDesde, String fechaHasta)
	{
		String sql = this.getSqlGetConsumosPrestacionNoInternosByEquipo(fechaDesde, fechaHasta);
		try
		{
			return (List<Consumo>)getEntityManager().createQuery(sql).setParameter(PARAM_CODEQUIPO, equipo.getCod()).getResultList();
		}
		catch (Exception ex)
		{
			return new ArrayList<>();
		}
	}
	
	private String getSqlGetConsumosPrestacionNoInternosByEquipo(String fechaDesde, String fechaHasta)
	{
		StringBuilder sql = new StringBuilder("SELECT DISTINCT con FROM Consumo con, ProductoEquipo pe WHERE con.producto = pe.producto AND con.estado='Finalizado' AND con.interno='NO' AND con.presupuesto='NO' AND pe.equipo.cod=:codequipo");
		if (fechaDesde != null)
		{
			sql.append(" AND con.fechaSolicitud >=").append(TO_DATE_COMILLA).append(fechaDesde).append(DATE_FORMAT_COMILLA);
		}
		if (fechaHasta != null)
		{
			sql.append(" AND con.fechaSolicitud <").append(TO_DATE_COMILLA).append(fechaHasta).append(DATE_FORMAT_COMILLA).append(" +1");
		}
		return sql.toString();
	}
}
