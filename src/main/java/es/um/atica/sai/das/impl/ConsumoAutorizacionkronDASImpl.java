package es.um.atica.sai.das.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.jboss.seam.annotations.Name;

import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.sai.das.interfaces.ConsumoAutorizacionkronDAS;
import es.um.atica.sai.entities.Consumo;
import es.um.atica.sai.entities.ConsumoAutorizacionkron;
import es.um.atica.sai.entities.PuertaKron;
import es.um.atica.sai.exceptions.SaiException;


@Name(ConsumoAutorizacionkronDAS.NAME)
@Stateless
@Local(ConsumoAutorizacionkronDAS.class)
public class ConsumoAutorizacionkronDASImpl extends DataAccessServiceImpl<ConsumoAutorizacionkron> implements ConsumoAutorizacionkronDAS{

	
	@Override
	public void crear(ConsumoAutorizacionkron c) throws SaiException 
	{
		try 
		{
			this.persist( c, true );
		}
		catch(Exception ex) 
		{
			log.error("Error creando ConsumoAutorizacionkron #0", ex);
			throw new SaiException(ex.getMessage());
		}		
	}

	@Override
	public void modificar(ConsumoAutorizacionkron c) throws SaiException 
	{
		try 
		{
			this.update( c, true );
		}
		catch(Exception ex) 
		{
			log.error("Error modificando ConsumoAutorizacionkron #0", ex);
			throw new SaiException(ex.getMessage());
		}		
	}
	
	@Override
	public void eliminar(ConsumoAutorizacionkron c) throws SaiException
	{
		try 
		{
			this.delete( c, true );
		}
		catch(Exception ex) 
		{
			log.error("Error eliminando ConsumoAutorizacionkron #0", ex);
			throw new SaiException(ex.getMessage());
		}		
	}

	@Override
	public List<ConsumoAutorizacionkron> getAutorizacionesByConsumo( Consumo c ) 
	{
		try 
		{
			return this.entityManager.createNamedQuery(ConsumoAutorizacionkron.GET_AUTORIZACIONES_BY_CONSUMO).setParameter("consumo", c).getResultList(); 	    	 
		}
		catch (Exception ex) 
		{
			return new ArrayList<>();
		}
	}
	
	@Override
	public List<ConsumoAutorizacionkron> getAutorizacionesByConsumoPuertaFechas(Consumo consumo, PuertaKron pk, Date fechaInicio, Date fechaFin)
	{
		try
		{
			return this.entityManager.createNamedQuery(ConsumoAutorizacionkron.GET_AUTORIZACIONES_BY_CONSUMO_PUERTA_FECHAS)
							  .setParameter("consumo", consumo)
							  .setParameter("puertakron", pk)
							  .setParameter("fechainicio", fechaInicio)
							  .setParameter("fechafin", fechaFin)
							  .getResultList();
		}
		catch(Exception ex)
		{
			log.error( "Error getAutorizacionesByConsumoPuertaFechas", ex.getMessage());
			return new ArrayList<>();
		}
	}
	
	@Override
	public boolean existeAutorizacionSolicitada(ConsumoAutorizacionkron cak)
	{
		try
		{
			return ((Long)this.entityManager.createNamedQuery(ConsumoAutorizacionkron.EXISTE_AUTORIZACION_SOLICITADA)
							  .setParameter("consumo", cak.getConsumo())
							  .setParameter("puertakron", cak.getPuertaKron())
							  .setParameter("fechainicio", cak.getFechaInicio())
							  .setParameter("fechafin", cak.getFechaFin())
							  .getSingleResult())>0;
		}
		catch(Exception ex)
		{
			log.error( "Error existeAutorizacionSolicitada", ex.getMessage());
			return false;
		}
	}
	
	@Override
	public boolean existeOtraAutorizacionIgualOtroConsumo(ConsumoAutorizacionkron cak)
	{
		try
		{
			return ((Long)this.entityManager.createNamedQuery(ConsumoAutorizacionkron.EXISTE_OTRA_AUTORIZACION_IGUAL_OTRO_CONSUMO)
							  .setParameter("consumo", cak.getConsumo())
							  .setParameter("usuario", cak.getConsumo().getUsuarioSolicitante())
							  .setParameter("puertakron", cak.getPuertaKron())
							  .setParameter("fechainicio", cak.getFechaInicio())
							  .setParameter("fechafin", cak.getFechaFin())
							  .getSingleResult())>0;
		}
		catch(Exception ex)
		{
			log.error( "Error existeOtraAutorizacionIgualOtroConsumo", ex.getMessage());
			return false;
		}
	}


}
