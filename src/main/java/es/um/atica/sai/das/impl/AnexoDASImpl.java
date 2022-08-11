package es.um.atica.sai.das.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.jboss.seam.annotations.Name;

import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.sai.das.interfaces.AnexoDAS;
import es.um.atica.sai.entities.Anexo;
import es.um.atica.sai.entities.Consumo;
import es.um.atica.sai.exceptions.SaiException;

@Name( value = AnexoDAS.NAME )
@Stateless
@Local(AnexoDAS.class)
public class AnexoDASImpl extends DataAccessServiceImpl<Anexo> implements AnexoDAS {

	@Override
	public void eliminar(Anexo a) throws SaiException
	{
		try
		{
			this.delete( a, true );
		}
		catch (final Exception ex)
		{
			log.error( "Error inesperado. No se ha podido eliminar el anexo:", ex );
			throw new SaiException(ex.getMessage());
		}
	}

	@Override
	public void crear(Anexo a) throws SaiException
	{
		try
		{
			this.persist( a, true );
		}
		catch (final Exception ex)
		{
			log.error( "Error inesperado. No se ha podido crear el anexo:", ex );
			throw new SaiException(ex.getMessage());
		}
	}

	@Override
	public void modificar(Anexo a) throws SaiException
	{
		try
		{
			this.update( a, true );
		}
		catch (final Exception ex)
		{
			log.error( "Error inesperado. No se ha podido modificar el anexo:", ex );
			throw new SaiException(ex.getMessage());
		}
	}
	
	@Override
	public List<Anexo> getAnexosByConsumo( Consumo c ) 
	{
		try 
		{
			return entityManager.createNamedQuery(Anexo.GET_ANEXOS_X_CONSUMO).setParameter("consumo", c).getResultList();
		} 
		catch ( final Exception ex ) 
		{
			return new ArrayList<>();
		}
	}
	
	@Override
	public List<Anexo> getAnexosBioseguridadByConsumo( Consumo c ) 
	{
		try 
		{
			return entityManager.createNamedQuery(Anexo.GET_ANEXOS_BIOSEGURIDAD_X_CONSUMO).setParameter("consumo", c).getResultList();
		} 
		catch ( final Exception ex ) 
		{
			return new ArrayList<>();
		}
	}
	
	@Override
	public List<Anexo> getAnexosConsumoByConsumo( Consumo c ) 
	{
		try 
		{
			return entityManager.createNamedQuery(Anexo.GET_ANEXOS_CONSUMO_X_CONSUMO).setParameter("consumo", c).getResultList();
		} 
		catch ( final Exception ex ) 
		{
			return new ArrayList<>();
		}
	}
	
	public Anexo getAnexoByTag(String tag)
	{
		try 
		{
			return (Anexo)entityManager.createNamedQuery(Anexo.GET_ANEXO_BY_TAG).setParameter("tag", tag).getSingleResult();
		} 
		catch ( final Exception ex ) 
		{
			return null;
		}
	}


}
