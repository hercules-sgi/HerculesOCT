package es.um.atica.sai.das.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.primefaces.model.SortOrder;

import es.um.atica.commons.utils.UtilString;
import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.sai.das.interfaces.ProductoDAS;
import es.um.atica.sai.dtos.TipoPerfil;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.TipoReservable;
import es.um.atica.sai.exceptions.SaiException;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.utils.Utilidades;
import es.um.atica.seam.framework.Query.QueryPriority;

@Name( ProductoDAS.NAME )
@Stateless
@Local( ProductoDAS.class )
public class ProductoDASImpl extends DataAccessServiceImpl<Producto> implements ProductoDAS {

    @In("org.jboss.seam.security.identity")    
    SaiIdentity identity;
	
	private static final String[] RESTRICCIONESBUSQUEDA = {
			Utilidades.cmpTextFilterEjbqlCompletoIgnoraTildes("pro.descripcion", "#{productos.descripcionBuscar}" ),
			UtilString.cmpTextFilterEjbql( "pro.tipo", "#{productos.tipoBuscar}" ),
			UtilString.cmpNumberTextFilterEjbql( "pro.servicio", "#{productos.servicioBuscar}" ),
			UtilString.cmpTextFilterEjbql( "pro.estado", "#{productos.estadoBuscar}" )
	};

	private static List<String> getListaRestriccionesbusqueda() {
		return Arrays.asList( RESTRICCIONESBUSQUEDA );
	}
	

	public void crear( Producto prod ) throws SaiException {
		try 
		{
			this.persist( prod, true );
		} 
		catch ( Exception e ) {

			log.error( "No se ha podido crear el producto:", e );
			throw new SaiException(e.getMessage());
		}
	}

	public void modificar( Producto prod ) throws SaiException {
		try 
		{
			this.update( prod, true );
		} 
		catch ( Exception e ) {

			log.error( "No se ha podido modificar el producto:", e );
			throw new SaiException(e.getMessage());
		}
	}

	@Override
	public void eliminar( Producto prod ) throws SaiException {
		try 
		{
			this.delete( prod, true );
		} 
		catch ( Exception e ) {

			log.error( "No se ha podido eliminar el producto:", e );
			throw new SaiException(e.getMessage());
		}
	}
	
	@Override
	public List<Producto> getProductosByServicio(Servicio servicio)
	{
		try
		{
			return entityManager.createNamedQuery(Producto.GET_PRODUCTOS_X_SERVICIO).setParameter("servicio", servicio).getResultList();
		}
        catch(Exception ex)
        {
        	log.error("Error en getProductosByServicio", ex);
        	return new ArrayList<>();
        }
	}
	
	@Override
	public List<Producto> getProductosByListaServicios(List<Servicio> listaServicios)
	{
		try
		{
			return entityManager.createNamedQuery(Producto.GET_PRODUCTOS_ACTIVOS_X_LISTASERVICIOS).setParameter("listaservicios", listaServicios).getResultList();
		}
        catch(Exception ex)
        {
        	log.error("Error en getProductosByListaServicios", ex);
        	return new ArrayList<>();
        }
	}
	
	private String getConsultaAdminProductosByServicioTipo(String tipo, boolean soloObtenerNoRequierenProyecto, boolean soloObtenerAdmitenPresupuesto)
	{
		String consulta = null;
		if (tipo.equals("R"))
       	{
      		if (soloObtenerNoRequierenProyecto)
      		{
      			consulta = Producto.GET_PRODUCTOS_X_SERVICIO_TIPOR_NOREQUIEREPROYECTO;
      		}
      		else
      		{
      			consulta = Producto.GET_PRODUCTOS_X_SERVICIO_TIPOR;
      		}
       	}
    	else if (tipo.equals("P"))
    	{
      		if (soloObtenerNoRequierenProyecto)
      		{
      			if (soloObtenerAdmitenPresupuesto)
      			{
      				consulta = Producto.GET_PRODUCTOS_X_SERVICIO_TIPO_NOREQUIEREPROYECTO_ADMITENPRESUPUESTO;
      			}
      			else
      			{
      				consulta = Producto.GET_PRODUCTOS_X_SERVICIO_TIPO_NOREQUIEREPROYECTO;
      			}
      		}
      		else
      		{
      			if (soloObtenerAdmitenPresupuesto)
      			{
      				consulta = Producto.GET_PRODUCTOS_X_SERVICIO_TIPO_ADMITENPRESUPUESTO;
      			}
      			else
      			{
      				consulta = Producto.GET_PRODUCTOS_X_SERVICIO_TIPO;
      			}
      		}
    	}
       	else
       	{
      		if (soloObtenerNoRequierenProyecto)
      		{
      			consulta = Producto.GET_PRODUCTOS_X_SERVICIO_TIPO_NOREQUIEREPROYECTO;
      		}
      		else
      		{
      			consulta = Producto.GET_PRODUCTOS_X_SERVICIO_TIPO;
      		}
       	}
		return consulta;
	}
	
	private String getConsultaUsuarioProductosByServicioTipo(String tipo, 
															 boolean soloObtenerNoRequierenProyecto, 
															 boolean soloObtenerAdmitenPresupuesto)
	{
		String consulta = null;
    	if (tipo.equals("R"))
    	{
      		if (soloObtenerNoRequierenProyecto)
      		{
      			consulta = Producto.GET_PRODUCTOS_X_SERVICIO_TIPOR_NOADMIN_NOREQUIEREPROYECTO;
      		}
      		else
      		{
      			consulta = Producto.GET_PRODUCTOS_X_SERVICIO_TIPOR_NOADMIN;
      		}
        }
    	else if (tipo.equals("P"))
    	{
      		if (soloObtenerNoRequierenProyecto)
      		{
      			if (soloObtenerAdmitenPresupuesto)
      			{
      				consulta = Producto.GET_PRODUCTOS_X_SERVICIO_TIPO_NOADMIN_NOREQUIEREPROYECTO_ADMITENPRESUPUESTO;
      			}
      			else
      			{
      				consulta = Producto.GET_PRODUCTOS_X_SERVICIO_TIPO_NOADMIN_NOREQUIEREPROYECTO;
      			}
      		}
      		else
      		{
      			if (soloObtenerAdmitenPresupuesto)
      			{
      				consulta = Producto.GET_PRODUCTOS_X_SERVICIO_TIPO_NOADMIN_ADMITENPRESUPUESTO;
      			}
      			else
      			{
      				consulta = Producto.GET_PRODUCTOS_X_SERVICIO_TIPO_NOADMIN;
      			}
      		}
    	}
    	else
        {
      		if (soloObtenerNoRequierenProyecto)
      		{
      			consulta = Producto.GET_PRODUCTOS_X_SERVICIO_TIPO_NOADMIN_NOREQUIEREPROYECTO;
      		}
      		else
      		{
      			consulta = Producto.GET_PRODUCTOS_X_SERVICIO_TIPO_NOADMIN;
      		}
        }

		return consulta;
	}
	
	private String getConsultaProductosByServicioTipo(Servicio servicio, 
													  String tipo, 
													  boolean soloObtenerNoRequierenProyecto, 
												  	  boolean soloObtenerAdmitenPresupuesto)
	{
        if (identity.tienePerfilEnServicio(TipoPerfil.SUPERVISOR, servicio.getCod()) || identity.tienePerfilEnServicio(TipoPerfil.TECNICO, servicio.getCod()))
        {
        	return this.getConsultaAdminProductosByServicioTipo(tipo, soloObtenerNoRequierenProyecto, soloObtenerAdmitenPresupuesto);
        }
        else
        {
        	return this.getConsultaUsuarioProductosByServicioTipo(tipo, soloObtenerNoRequierenProyecto, soloObtenerAdmitenPresupuesto);
        }
	}
	
	@Override
    public List<Producto> getProductosByServicioTipo(Servicio servicio, 
    												 String tipo, 
    												 boolean soloObtenerNoRequierenProyecto, 
    												 boolean soloObtenerAdmitenPresupuesto) 
	{
		String consulta = this.getConsultaProductosByServicioTipo(servicio, tipo, soloObtenerNoRequierenProyecto, soloObtenerAdmitenPresupuesto);
        try
        {
        	return this.getEntityManager().createNamedQuery(consulta).setParameter("tipo", tipo).setParameter("servicio", servicio).getResultList();
        }
        catch(Exception ex)
        {
        	log.error("Error en getProductosByServicioTipo", ex);
        	return new ArrayList<>();
        }
    }
	
	public List<Producto> getProductosByTipoReservable(TipoReservable tr)
	{
		try
		{
			return entityManager.createNamedQuery(Producto.GET_PRODUCTOS_X_TIPORESERVABLE).setParameter("tiporeservable", tr).getResultList();
		}
        catch(Exception ex)
        {
        	return new ArrayList<>();
        }		
	}
	
    public List<Producto> busquedaProductos(Long codUsuario, int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters)
    {
  	   Map<String, Object> params = new HashMap<>();
  	   Map<String, Object> hints = new HashMap<>();
   	   String consulta = null;
   	   if (identity.tienePerfil(TipoPerfil.GESTOR))
   	   {
   		   consulta = Producto.GET_PRODUCTOS;
   	   }
   	   else
   	   {
   		   List<Servicio> listaServiciosBuscar = identity.getServiciosPerfil(TipoPerfil.SUPERVISOR);
   		   params.put("listaservicios", listaServiciosBuscar);
   		   consulta = Producto.GET_PRODUCTOS_X_LISTASERVICIOS;
   	   }
  	   try
  	   {
  		   return this.findByEntityNamedQueryWithDinamicFilter(consulta, getListaRestriccionesbusqueda(), params, first, pageSize, sortField, sortOrder.name(), "and", hints, null, QueryPriority.JAVA, QueryPriority.QUERY);
  	   }
  	   catch (Exception ex)
  	   {
  		   log.error("Error en busquedaProductos: #0",ex.toString());
  		   return new ArrayList<>();
  	   }    	
    }
    
    public int getCountBusquedaProductos(Long codUsuario)
    {
   	   Map<String, Object> params = new HashMap<>();
   	   Map<String, Object> hints = new HashMap<>();
   	   String consulta = null;
   	   if (identity.tienePerfil(TipoPerfil.GESTOR))
   	   {
   		   consulta = Producto.GET_PRODUCTOS;
   	   }
   	   else
   	   {
   		   List<Servicio> listaServiciosBuscar = identity.getServiciosPerfil(TipoPerfil.SUPERVISOR);
   		   params.put("listaservicios", listaServiciosBuscar);
   		   consulta = Producto.GET_PRODUCTOS_X_LISTASERVICIOS;
   	   }
   	   try
   	   {
   		   List<Producto> listaProductos = this.findByEntityNamedQueryWithDinamicFilter(consulta, getListaRestriccionesbusqueda(), params, null, null, null, null, "and", hints, null, QueryPriority.JAVA, QueryPriority.QUERY);
   		   return listaProductos.size();
   	   }
   	   catch (Exception ex)
   	   {
   		   log.error("Error en getCountBusquedaProductos: #0",ex.toString());
   		   return 0;
   	   }    	
    }

	@Override
	public List<Producto> getReservables(Servicio servicio) {
		 log.info( "Realizando getReservables " );	   
		 try {
			 Query consulta = entityManager.createNamedQuery( Producto.GET_RESERVABLES_ALTA )
					 .setParameter( "servicio", servicio );			 
			 return consulta.getResultList();
		 }catch (Exception ex) {
			 log.error( "Error al recuperar los reservables en estado ALTA #0", ex.toString() );
			 return new ArrayList<>();
		 }
	}


}
