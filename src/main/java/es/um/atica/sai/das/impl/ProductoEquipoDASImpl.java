package es.um.atica.sai.das.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.jboss.seam.annotations.Name;

import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.sai.das.interfaces.ProductoEquipoDAS;
import es.um.atica.sai.entities.Equipo;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.ProductoEquipo;
import es.um.atica.sai.exceptions.SaiException;



@Name("productoEquipoDAS")
@Stateless
@Local(ProductoEquipoDAS.class)
public class ProductoEquipoDASImpl extends  DataAccessServiceImpl<ProductoEquipo> implements ProductoEquipoDAS{

    @Override
    public List<ProductoEquipo> getProductoEquiposByProducto(Producto producto)
    {
        try
        {
        	return getEntityManager().createNamedQuery(ProductoEquipo.GET_PRODUCTOEQUIPOS_BY_PRODUCTO).setParameter("producto", producto).getResultList();
        }
        catch (final Exception e)
        {
        	log.error( "Error inesperado getEquiposByProducto:", e );
        	return new ArrayList<>();
        }
    }
    
    @Override
    public List<Equipo> getEquiposByProducto(Producto producto)
    {
        try
        {
        	return getEntityManager().createNamedQuery(ProductoEquipo.GET_EQUIPOS_BY_PRODUCTO).setParameter("producto", producto).getResultList();
        }
        catch (final Exception e)
        {
        	log.error( "Error inesperado getEquiposByProducto:", e );
        	return new ArrayList<>();
        }
    }
    
    @Override
    public ProductoEquipo getProductoEquipoByProductoEquipo(Producto producto, Equipo equipo)
    {
    	try
    	{
    		return (ProductoEquipo)getEntityManager().createNamedQuery(ProductoEquipo.GET_PRODUCTOEQUIPO_BY_PRODUCTO_EQUIPO).setParameter("producto", producto).setParameter("equipo", equipo).getSingleResult();
    	}
    	catch (final Exception e)
    	{
    		log.error( "Error inesperado getProductoEquipoByProductoEquipo:", e );
    		return null;
    	}
    }
    
    @Override
	public boolean existeProductoEquipo(Producto producto, Equipo equipo) 
    {
    	try
    	{
    		return ((Long)getEntityManager().createNamedQuery(ProductoEquipo.EXISTE_PRODUCTOEQUIPO).setParameter("producto", producto).setParameter("equipo", equipo).getSingleResult()).intValue()>0;
    	}
    	catch (final Exception e)
    	{
    		log.error( "Error inesperado existeProductoEquipo:", e );
    		return false;
    	}
    }

	@Override
	public void crear(ProductoEquipo pe) throws SaiException 
	{
		try 
		{
			this.persist( pe, true );
		} 
		catch ( final Exception e ) 
		{
			log.error( "Error al crear ProductoEquipo:", e );
			throw new SaiException(e.getMessage());
		}
	}
	
	@Override
	public void modificar(ProductoEquipo pe) throws SaiException 
	{
		try 
		{
			this.update(pe, true);
		} 
		catch ( final Exception e ) 
		{
			log.error( "Error al modificar ProductoEquipo:", e );
			throw new SaiException(e.getMessage());
		}
	}
	
    @Override
	public void eliminar(ProductoEquipo pe) throws SaiException
    {
		try 
		{
			this.delete(pe, true);
		} 
		catch ( final Exception e ) 
		{
			log.error( "Error al eliminar ProductoEquipo:", e );
			throw new SaiException(e.getMessage());
		}    
	}


}
