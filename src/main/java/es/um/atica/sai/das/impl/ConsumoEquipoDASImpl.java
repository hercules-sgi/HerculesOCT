package es.um.atica.sai.das.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.jboss.seam.annotations.Name;

import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.sai.das.interfaces.ConsumoEquipoDAS;
import es.um.atica.sai.entities.Consumo;
import es.um.atica.sai.entities.ConsumoEquipo;
import es.um.atica.sai.entities.Equipo;
import es.um.atica.sai.exceptions.SaiException;



@Name(ConsumoEquipoDAS.NAME)
@Stateless
@Local(ConsumoEquipoDAS.class)
public class ConsumoEquipoDASImpl extends  DataAccessServiceImpl<ConsumoEquipo> implements ConsumoEquipoDAS{

    @Override
    public List<ConsumoEquipo> getEquiposByConsumo(Consumo consumo)
    {
        try
        {
        	return getEntityManager().createNamedQuery(ConsumoEquipo.GET_EQUIPOS_BY_CONSUMO).setParameter("consumo", consumo).getResultList();
        }
        catch (final Exception e)
        {
        	log.error( "Error inesperado getEquiposByConsumo:", e );
        	return new ArrayList<>();
        }
    }
            
    @Override
	public boolean existeConsumoEquipo(Consumo consumo, Equipo equipo) 
    {
    	try
    	{
    		return ((Long)getEntityManager().createNamedQuery(ConsumoEquipo.EXISTE_CONSUMOEQUIPO).setParameter("consumo", consumo).setParameter("equipo", equipo).getSingleResult()).intValue()>0;
    	}
    	catch (final Exception e)
    	{
    		log.error( "Error inesperado existeConsumoEquipo:", e );
    		return false;
    	}
    }
    

	@Override
	public void crear(ConsumoEquipo ce) throws SaiException 
	{
		try 
		{
			this.persist( ce, true );
		} 
		catch ( final Exception e ) 
		{
			log.error( "Error al crear ConsumoEquipo:", e );
			throw new SaiException(e.getMessage());
		}
	}
	
	@Override
	public void modificar(ConsumoEquipo ce) throws SaiException 
	{
		try 
		{
			this.update(ce, true);
		} 
		catch ( final Exception e ) 
		{
			log.error( "Error al modificar ConsumoEquipo:", e );
			throw new SaiException(e.getMessage());
		}
	}
	
    @Override
	public void eliminar(ConsumoEquipo ce) throws SaiException
    {
		try 
		{
			this.delete(ce, true);
		} 
		catch ( final Exception e ) 
		{
			log.error( "Error al eliminar ConsumoEquipo:", e );
			throw new SaiException(e.getMessage());
		}    
	}


}
