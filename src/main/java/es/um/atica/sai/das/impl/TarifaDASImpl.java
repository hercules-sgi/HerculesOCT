package es.um.atica.sai.das.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.jboss.seam.annotations.Name;

import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.sai.das.interfaces.TarifaDAS;
import es.um.atica.sai.entities.Tarifa;
import es.um.atica.sai.exceptions.SaiException;



@Name("tarifaDAS")
@Stateless
@Local(TarifaDAS.class)
public class TarifaDASImpl extends  DataAccessServiceImpl<Tarifa> implements TarifaDAS{
	
    @Override
	public boolean existeTarifa( Tarifa tarifa, Long cantidadInicial ) 
    {
    	try
    	{
    		List<Tarifa> tarifas =  this.getEntityManager().createNamedQuery( Tarifa.EXISTE_TARIFA )
    				.setParameter( "codproducto", tarifa.getProducto().getCod())
    				.setParameter("codtipotarifa", tarifa.getTipoTarifa().getCod())    				
    				.getResultList();
    		for(Tarifa t: tarifas) {
    			if (t.getCantidadInicial().equals( cantidadInicial )) {
    				return true;
    			}
    		}
    		return false;
    	}
    	catch (Exception e)
    	{
    		log.error( "Error inesperado existeTarifa:", e );
    		return false;
    	}
    }
	
	@Override
	public void crear( Tarifa tar ) throws SaiException {
		try {
			this.persist( tar, true );
		} catch ( Exception e ) {

			log.error( "Error inesperado. No se ha podido crear la tarifa:", e );
			throw new SaiException(e.getMessage());
		}
	}

	@Override
	public void eliminar( Tarifa tar ) throws SaiException {
		try {
			this.delete( tar, true );
		} catch ( Exception e ) {

			log.error( "Error inesperado. No se ha podido eliminar la tarifa:", e );
			throw new SaiException(e.getMessage());
		}
	}

	@Override
	public List<Tarifa> getTarifasByProductoCantidad( Long codProducto ) {
		try
    	{
    		return this.getEntityManager().createNamedQuery( Tarifa.GET_TARIFAS_BY_PRODUCTO_CANTIDAD )
    				.setParameter( "producto", codProducto)    				    			
    				.getResultList();    		
    	}
    	catch (Exception e)
    	{
    		log.error( "Error inesperado getTarifasByProductoCantidad:", e );
    		return new ArrayList<>();
    	}
	}    
}
