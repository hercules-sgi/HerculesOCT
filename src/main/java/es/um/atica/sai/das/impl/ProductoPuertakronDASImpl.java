package es.um.atica.sai.das.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.jboss.seam.annotations.Name;

import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.sai.das.interfaces.ProductoPuertakronDAS;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.ProductoPuertakron;
import es.um.atica.sai.exceptions.SaiException;

@Name(ProductoPuertakronDAS.NAME)
@Stateless
@Local(ProductoPuertakronDAS.class)
public class ProductoPuertakronDASImpl extends DataAccessServiceImpl<ProductoPuertakron> implements ProductoPuertakronDAS {

	@Override
	public void crear(ProductoPuertakron p) throws SaiException
	{
		try 
		{
			this.persist( p, true );
		}
		catch(Exception ex) 
		{
			log.error( "Error guardando un ProductoPuertakron #0", ex);
			throw new SaiException(ex.getMessage());
		}		
	}


	@Override
	public void eliminar( ProductoPuertakron p ) throws SaiException
	{
		try 
		{
			this.delete( p, true );
		}
		catch(Exception ex) 
		{
			log.error( "Error eliminando un ProductoPuertakron #0", ex);
			throw new SaiException(ex.getMessage());
		}		
	}

	@Override
	public List<ProductoPuertakron> getListaProductoPuertakronByProducto( Producto p ) 
	{
	     try 
	     {
	    	 return this.entityManager.createNamedQuery(ProductoPuertakron.GET_PRODUCTO_PUERTAKRON_BY_PRODUCTO).setParameter("producto", p).getResultList(); 	    	 
	     }
	     catch (Exception ex) 
	     {
	    	 return new ArrayList<>();
	     }
	}

	@Override
	public boolean existeProductoPuertakron(ProductoPuertakron ppk) 
	{
	     try 
	     {
	    	 return ((Long)this.entityManager.createNamedQuery(ProductoPuertakron.EXISTE_PUERTAKRON)
	    			 						 .setParameter("producto", ppk.getProducto())
	    			 						 .setParameter("puertakron", ppk.getPuertaKron())
	    			 						 .getSingleResult())>0; 	    	 
	     }
	     catch (Exception ex) 
	     {
	    	 log.error( "Error en existeProductoPuertakron #0", ex);
	    	 return false;
	     }
	}

}
