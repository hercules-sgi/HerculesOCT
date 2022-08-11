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
import es.um.atica.sai.das.interfaces.FacturaDAS;
import es.um.atica.sai.entities.Factura;
import es.um.atica.sai.exceptions.SaiException;
import es.um.atica.seam.framework.Query.QueryPriority;

@Local(FacturaDAS.class)
@Name("facturaDAS")
@Stateless
public class FacturaDASImpl extends  DataAccessServiceImpl<Factura> implements FacturaDAS {

   private static final String[] RESTRICCIONESBUSQUEDA = {
		   UtilString.cmpNumberEjbql("fac.servicio","#{facturacion.servicioBuscar}"),
		   UtilString.cmpNumberEjbql("fac.investigador", "#{facturacion.usuarioIrBuscar}"),
		   UtilString.cmpNumberEjbql("fac.entidadPagadora", "#{facturacion.entidadPagadoraBuscar}"),
           new StringBuilder("fac.fechaGeneracion >=").append("TO_DATE(").append( "#{facturacion.fechaDesdeStringBuscar}").append( ",'dd/MM/yyyy')" ).toString(),
           new StringBuilder("fac.fechaGeneracion <=").append("TO_DATE(").append( "#{facturacion.fechaHastaStringBuscar}").append( ",'dd/MM/yyyy')" ).toString(),
           UtilString.cmpNumberTextFilterEjbql("fac.anoJusto", "#{facturacion.anoFacturaBuscar}"),
           UtilString.cmpNumberTextFilterEjbql("fac.numeroJusto", "#{facturacion.numFacturaBuscar}") };  
   
   private static List<String> getListaRestriccionesbusqueda()
   {
       return Arrays.asList(RESTRICCIONESBUSQUEDA);
   }
   
   public List<Factura> busquedaFactura(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters)
   {
	   Map<String, Object> params = new HashMap<>();
	   Map<String, Object> hints = new HashMap<>();
	   try
	   {
		   return this.findByEntityNamedQueryWithDinamicFilter(Factura.GET_FACTURAS, getListaRestriccionesbusqueda(), params, first, pageSize, sortField, sortOrder.name(), "and", hints, null, QueryPriority.BOTH, QueryPriority.QUERY);
	   }
	   catch (Exception ex)
	   {
		   log.error("Error en busquedaFactura: #0",ex.toString());
		   return new ArrayList<>();
	   }
   }
   
   public int getCountBusquedaFactura()
   {
	   Map<String, Object> params = new HashMap<>();
	   Map<String, Object> hints = new HashMap<>();
	   try
	   {
		   List<Factura> listaFactura = this.findByEntityNamedQueryWithDinamicFilter(Factura.GET_FACTURAS, getListaRestriccionesbusqueda(), params, null, null, null, null, "and", hints, null, QueryPriority.BOTH, QueryPriority.QUERY);            
		   return listaFactura.size();
	   }
	   catch (Exception ex)
	   {
		   log.error("Error en getCountBusquedaFactura: #0",ex.toString());
		   return 0;
	   }   
   }
    
	public void crear( Factura fac ) throws SaiException {
		try {
			this.persist( fac, true );
		} catch ( Exception e ) {

			log.error( "No se ha podido crear la factura:", e );
			throw new SaiException(e.getMessage());
		}
	}

	public void modificar( Factura fac ) throws SaiException {
		try {
			this.update( fac, true );
		} catch ( Exception e ) {

			log.error( "No se ha podido modificar la factura:", e );
			throw new SaiException(e.getMessage());
		}
	}

	public void eliminar( Factura fac ) throws SaiException {
		try {
			this.delete( fac, true );
		} catch ( Exception e ) {

			log.error( "No se ha podido eliminar la factura:", e );
			throw new SaiException(e.getMessage());
		}
	} 
}
