package es.um.atica.sai.das.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.jboss.seam.annotations.Name;

import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.sai.das.interfaces.ProyectoProductoDAS;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.Proyecto;
import es.um.atica.sai.entities.ProyectoProducto;
import es.um.atica.sai.exceptions.SaiException;

@Name(ProyectoProductoDAS.NAME)
@Stateless
@Local(ProyectoProductoDAS.class)
public class ProyectoProductoDASImpl  extends  DataAccessServiceImpl<ProyectoProducto>  implements ProyectoProductoDAS {


	@Override
	public void crear(ProyectoProducto pp) throws SaiException {
		try {
			this.persist( pp, true );
		} catch ( Exception e ) {

			log.error( "Error inesperado. No se ha podido crear la asociación entre proyecto y producto:", e );
			throw new SaiException(e.getMessage());
		}
	}

	@Override
	public void modificar(ProyectoProducto pp) throws SaiException {
		try {
			this.update(pp, true );
		} catch ( Exception e ) {

			log.error( "Error inesperado. No se ha podido modificar la asociación entre proyecto y producto:", e );
			throw new SaiException(e.getMessage());
		}
	}

	@Override
	public void eliminar(ProyectoProducto pp) throws SaiException {
		try {
			this.delete( pp, true );
		} catch ( Exception e ) {

			log.error( "Error inesperado. No se ha podido eliminar la asociación entre proyecto y producto:", e );
			throw new SaiException(e.getMessage());
		}
	}

	@Override
	public boolean existeProyectoProducto(Proyecto proyecto, Producto producto) 
	{		                 	    
	     try 
	     {
	    	 return (Long)entityManager.createNamedQuery(ProyectoProducto.EXISTE_PROYECTO_PRODUCTO).setParameter("proyecto", proyecto).setParameter("producto",producto).getSingleResult()>0; 
	     }
	     catch (Exception ex) 
	     {
	    	 return false;
	     }
	}
	
	@Override
	public ProyectoProducto getProyectoProductoByProyectoProducto(Proyecto proyecto, Producto producto)
	{
	     try 
	     {
	    	 return (ProyectoProducto)entityManager.createNamedQuery(ProyectoProducto.GET_PROYECTOPRODUCTO_X_PROYECTO_PRODUCTO).setParameter("proyecto", proyecto).setParameter("producto",producto).getSingleResult(); 
	     }
	     catch (Exception ex) 
	     {
	    	 return null;
	     }
		
	}
	
	@Override
	public List<ProyectoProducto> getListaProyectoProductoByProyecto(Proyecto proyecto)
	{
	     try 
	     {
	    	 return entityManager.createNamedQuery(ProyectoProducto.GET_PRODUCTOS_PROYECTO).setParameter("proyecto", proyecto).getResultList(); 
	     }
	     catch (Exception ex) 
	     {
	    	 return new ArrayList<>();
	     }
	}

}
