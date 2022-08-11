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
import es.um.atica.sai.das.interfaces.IrGrupoinvestigacionCantidadDAS;
import es.um.atica.sai.dtos.IrGrupoinvestigacionCantidad;
import es.um.atica.sai.entities.Consumo;
import es.um.atica.seam.framework.Query.QueryPriority;

@Local(IrGrupoinvestigacionCantidadDAS.class)
@Name("irGrupoinvestigacionCantidadDAS")
@Stateless
public class IrGrupoinvestigacionCantidadDASImpl extends  DataAccessServiceImpl<IrGrupoinvestigacionCantidad> implements IrGrupoinvestigacionCantidadDAS {

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
   
   private String getNamedQueryBusquedaIrGrupoinvestigacionCantidad(String estadoFacturacion)
   {
	   if (estadoFacturacion == null)
	   {
		   return Consumo.GET_CONSUMOS_RESUMEN_X_IR_GRUPOINVESTIGACION;
	   }
	   else if ("P".equals(estadoFacturacion))
	   {
		   return Consumo.GET_CONSUMOS_RESUMEN_X_IR_GRUPOINVESTIGACION_PENDIENTES_FACTURAR;
	   }
	   else
	   {
		   return Consumo.GET_CONSUMOS_RESUMEN_X_IR_GRUPOINVESTIGACION_FACTURADOS;
	   }
   }
   
   public List<IrGrupoinvestigacionCantidad> busquedaIrGrupoinvestigacionCantidad(String estadoFacturacion, int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters)
   {
	   Map<String, Object> params = new HashMap<>();
	   Map<String, Object> hints = new HashMap<>();
	   try
	   {
		   return this.findByDtoNamedNativeQueryWithDinamicFilter(this.getNamedQueryBusquedaIrGrupoinvestigacionCantidad(estadoFacturacion), getListaRestriccionesbusqueda(), params, first, pageSize, sortField, sortOrder.name(), "and", hints, null, QueryPriority.BOTH, QueryPriority.QUERY);
	   }
	   catch (Exception ex)
	   {
		   log.error("Error en busquedaIrGrupoinvestigacionCantidad: #0",ex.toString());
		   return new ArrayList<>();
	   }
   }
   
   public int getCountBusquedaIrGrupoinvestigacionCantidad(String estadoFacturacion)
   {
	   Map<String, Object> params = new HashMap<>();
	   Map<String, Object> hints = new HashMap<>();
	   try
	   {
		   List<IrGrupoinvestigacionCantidad> listaIrGrupoinvestigacionCantidad = this.findByDtoNamedNativeQueryWithDinamicFilter(this.getNamedQueryBusquedaIrGrupoinvestigacionCantidad(estadoFacturacion), getListaRestriccionesbusqueda(), params, null, null, null, null, "and", hints, null, QueryPriority.BOTH, QueryPriority.QUERY);            
		   return listaIrGrupoinvestigacionCantidad.size();
	   }
	   catch (Exception ex)
	   {
		   log.error("Error en getCountBusquedaIrGrupoinvestigacionCantidad: #0",ex.toString());
		   return 0;
	   }   
   }
 

}
