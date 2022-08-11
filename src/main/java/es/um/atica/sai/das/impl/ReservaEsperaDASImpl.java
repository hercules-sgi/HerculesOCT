package es.um.atica.sai.das.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.jboss.seam.annotations.Name;

import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.sai.das.interfaces.ReservaEsperaDAS;
import es.um.atica.sai.entities.ReservaEspera;
import es.um.atica.sai.entities.Reservas;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;

@Name(ReservaEsperaDAS.NAME)
@Stateless
@Local(ReservaEsperaDAS.class)
public class ReservaEsperaDASImpl extends DataAccessServiceImpl<ReservaEspera> implements ReservaEsperaDAS {

	@Override
	public void crear(ReservaEspera r) throws SaiException 
	{
		try 
		{			
			this.persist( r, true );						
		}
		catch(Exception ex) {
			log.error( "Error al guardar una reservaEspera: #0", ex.toString() );
			throw new SaiException(ex.getMessage());
		}
	}

	@Override
	public void modificar( ReservaEspera r ) throws SaiException 
	{
		try 
		{			
			this.update(r, true);						
		}
		catch(Exception ex) 
		{
			log.error( "Error al modificar una reservaEspera: #0", ex.toString() );
			throw new SaiException(ex.getMessage());
		}		
	}

	@Override
	public void eliminar( ReservaEspera r ) throws SaiException
	{
		try 
		{			
			this.delete( r, true );						
		}
		catch(Exception ex) {
			log.error( "Error al eliminar una reservaEspera: #0", ex.toString() );
			throw new SaiException(ex.getMessage());
		}		
	}

	@Override
	public List<ReservaEspera> getReservaEsperaByReserva( Reservas r ) {
		log.info( "Recuperando reservaEspera por reserva #0", r.getCod() );
		try {
			 return entityManager.createNamedQuery(ReservaEspera.GET_RESERVA_ESPERA_BY_RESERVA)
					 .setParameter( "reserva", r ).getResultList();		    		     
		}catch(Exception ex) {
			log.error( "Error al recuperar reservaEspera en getReservaEsperaByReserva #0", ex.toString() );
			return new ArrayList<>();
		}
	}

	@Override
	public ReservaEspera getReservaEsperaByUsuarioReserva( Usuario u, Reservas r ) {		
		log.info( "Recuperando reservaEspera por usuario #0 y reserva #1", u.getDatosUsuario().getEmail(), r.getCod() );
		try {
			 List<ReservaEspera> result = entityManager.createNamedQuery(ReservaEspera.GET_RESERVA_ESPERA_BY_USUARIO_RESERVA)
					 .setParameter( "usuario", u )
					 .setParameter( "reserva", r ).getResultList();
			 if (result.isEmpty()) {
				 return null;
			 }else {
				 return result.get( 0 );
			 }
		}catch(Exception ex) {
			log.error( "Error al recuperar reservaEspera en getReservaEsperaByReserva #0", ex.toString() );
			return null;
		}
	}	

	
}
