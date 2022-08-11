package es.um.atica.sai.das.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.jboss.seam.annotations.Name;

import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.sai.das.interfaces.ServicioPuertakronDAS;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.ServicioPuertakron;
import es.um.atica.sai.exceptions.SaiException;

@Name(ServicioPuertakronDAS.NAME)
@Stateless
@Local(ServicioPuertakronDAS.class)
public class ServicioPuertakronDASImpl extends DataAccessServiceImpl<ServicioPuertakron> implements ServicioPuertakronDAS {

	@Override
	public void crear( ServicioPuertakron spk ) throws SaiException
	{
		try 
		{
			this.persist( spk, true );
		}
		catch(Exception ex) 
		{
			log.error( "Error guardando un ServicioPuertakron #0", ex);
			throw new SaiException(ex.getMessage());
		}		
	}
	
	@Override
	public void eliminar( ServicioPuertakron spk ) throws SaiException
	{
		try 
		{
			this.delete( spk, true );
		}
		catch(Exception ex) 
		{
			log.error( "Error eliminando un ServicioPuertakron #0", ex);
			throw new SaiException(ex.getMessage());
		}		
	}

	@Override
	public List<ServicioPuertakron> getListaServicioPuertakronByServicio(Servicio s) 
	{
	     try 
	     {
	    	 return this.entityManager.createNamedQuery(ServicioPuertakron.GET_SERVICIO_PUERTAKRON_BY_SERVICIO).setParameter("servicio", s).getResultList(); 	    	 
	     }
	     catch (Exception ex) 
	     {
	    	 return new ArrayList<>();
	     }
	}

	@Override
	public boolean existeServicioPuertakron(ServicioPuertakron spk)
	{
	     try 
	     {
	    	 return ((Long)this.entityManager.createNamedQuery(ServicioPuertakron.EXISTE_SERVICIOPUERTAKRON)
	    			 						 .setParameter("servicio", spk.getServicio())
	    			 						 .setParameter("puertakron", spk.getPuertaKron())
	    			 						 .getSingleResult())>0; 	    	 
	     }
	     catch (Exception ex) 
	     {
	    	 log.error( "Error en existeServicioPuertakron #0", ex);
	    	 return false;
	     }
		
	}
}
