package es.um.atica.sai.das.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.jboss.seam.annotations.Name;
import org.primefaces.model.SortOrder;

import es.um.atica.commons.utils.UtilString;
import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.sai.das.interfaces.ConsumoCantidadDAS;
import es.um.atica.sai.dtos.ConsumoCantidad;
import es.um.atica.sai.entities.Consumo;
import es.um.atica.seam.framework.Query.QueryPriority;

@Local(ConsumoCantidadDAS.class)
@Name("consumoCantidadDAS")
@Stateless
public class ConsumoCantidadDASImpl extends  DataAccessServiceImpl<ConsumoCantidad> implements ConsumoCantidadDAS {

   private static final String[] RESTRICCIONESBUSQUEDA = {
		   UtilString.cmpTextFilterEjbql("CO.tipo", "#{consumoResumen.tipo}"),
		   UtilString.cmpNumberEjbql("PR.cod_servicio","#{consumoResumen.servicio.cod}"),
		   UtilString.cmpNumberEjbql("CO.cod_producto","#{consumoResumen.producto.cod}"),
		   UtilString.cmpTextFilterEjbql("CO.interno", "#{consumoResumen.consumoInterno}"),
		   UtilString.cmpTextFilterEjbql("CO.ir_asociado", "#{consumoResumen.usuarioInvestigador.cod}"),
		   UtilString.cmpTextFilterEjbql("CO.cod_usuario_solic", "#{consumoResumen.usuarioSolicitante.cod}"),
		   UtilString.cmpTextFilterEjbql("CO.estado", "#{consumoResumen.estado}"),
           new StringBuilder("CO.fecha_solicitud >=").append("TO_DATE(").append( "#{consumoResumen.fechaDesdeString}").append( ",'dd/MM/yyyy')" ).toString(),
           new StringBuilder("CO.fecha_solicitud <").append("TO_DATE(").append( "#{consumoResumen.fechaHastaString}").append( ",'dd/MM/yyyy') +1" ).toString(), };  
   
   private static List<String> getListaRestriccionesbusqueda()
   {
       return Arrays.asList(RESTRICCIONESBUSQUEDA);
   }
   
   private String getNamedQueryBusquedaConsumoCantidad(String estadoFacturacion)
   {
	   if (estadoFacturacion == null)
	   {
		   return Consumo.GET_CONSUMOS_RESUMEN;
	   }
	   else if ("P".equals(estadoFacturacion))
	   {
		   return Consumo.GET_CONSUMOS_RESUMEN_PENDIENTES_FACTURAR;
	   }
	   else
	   {
		   return Consumo.GET_CONSUMOS_RESUMEN_FACTURADOS;
	   }
   }
   
   public List<ConsumoCantidad> busquedaConsumoCantidad(String estadoFacturacion, int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters)
   {
	   Map<String, Object> params = new HashMap<>();
	   Map<String, Object> hints = new HashMap<>();

	   try
	   {
		   return this.findByDtoNamedNativeQueryWithDinamicFilter(this.getNamedQueryBusquedaConsumoCantidad(estadoFacturacion), getListaRestriccionesbusqueda(), params, first, pageSize, sortField, sortOrder.name(), "and", hints, null, QueryPriority.BOTH, QueryPriority.QUERY);
	   }
	   catch (Exception ex)
	   {
		   log.error("Error en busquedaConsumoCantidad: #0",ex.toString());
		   return new ArrayList<>();
	   }
   }
   
   public int getCountBusquedaConsumoCantidad(String estadoFacturacion)
   {
	   Map<String, Object> params = new HashMap<>();
	   Map<String, Object> hints = new HashMap<>();
	   try
	   {
		   List<ConsumoCantidad> listaConsumoCantidad = this.findByDtoNamedNativeQueryWithDinamicFilter(this.getNamedQueryBusquedaConsumoCantidad(estadoFacturacion), getListaRestriccionesbusqueda(), params, null, null, null, null, "and", hints, null, QueryPriority.BOTH, QueryPriority.QUERY);            
		   return listaConsumoCantidad.size();
	   }
	   catch (Exception ex)
	   {
		   log.error("Error en busquedaConsumoCantidad: #0",ex.toString());
		   return 0;
	   }   
   }
  
   public Long getNumIrsConsumoResumen(String estadoFacturacion)
   {
	   Map<String, Object> params = new HashMap<>();
	   Map<String, Object> hints = new HashMap<>();
	   String namedQuery = null;
	   if (estadoFacturacion == null)
	   {
		   namedQuery = Consumo.GET_IRS_CONSUMOS_RESUMEN;
	   }
	   else if ("P".equals(estadoFacturacion))
	   {
		   namedQuery = Consumo.GET_IRS_CONSUMOS_RESUMEN_PENDIENTES_FACTURAR;
	   }
	   else
	   {
		   namedQuery = Consumo.GET_IRS_CONSUMOS_RESUMEN_FACTURADOS;
	   }
	   try
	   {
		   return this.countTotalRecordByMapNamedNativeQuery(namedQuery, getListaRestriccionesbusqueda(), params, "and", hints);
	   }
	   catch (Exception ex)
	   {
		   log.error("Error en getNumIrsConsumoResumen: #0",ex.toString());
		   return null;
	   }   
   }

   public Long getNumMiembrosConsumoResumen(String estadoFacturacion)
   {
	   Map<String, Object> params = new HashMap<>();
	   Map<String, Object> hints = new HashMap<>();
	   String namedQuery = null;
	   if (estadoFacturacion == null)
	   {
		   namedQuery = Consumo.GET_MIEMBROS_CONSUMOS_RESUMEN;
	   }
	   else if ("P".equals(estadoFacturacion))
	   {
		   namedQuery = Consumo.GET_MIEMBROS_CONSUMOS_RESUMEN_PENDIENTES_FACTURAR;
	   }
	   else
	   {
		   namedQuery = Consumo.GET_MIEMBROS_CONSUMOS_RESUMEN_FACTURADOS;
	   }
	   try
	   {
		   return this.countTotalRecordByMapNamedNativeQuery(namedQuery, getListaRestriccionesbusqueda(), params, "and", hints);
	   }
	   catch (Exception ex)
	   {
		   log.error("Error en getNumMiembrosConsumoResumen: #0",ex.toString());
		   return null;
	   }   
   }
}
