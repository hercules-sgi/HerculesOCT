package es.um.atica.sai.das.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.TemporalType;

import org.jboss.seam.annotations.Name;

import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.sai.das.interfaces.ReservasDAS;
import es.um.atica.sai.entities.Consumo;
import es.um.atica.sai.entities.Equipo;
import es.um.atica.sai.entities.Reservas;
import es.um.atica.sai.entities.TipoReservable;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;

@Name("reservasDAS")
@Stateless
@Local(ReservasDAS.class)
public class ReservasDASImpl extends DataAccessServiceImpl<Reservas> implements ReservasDAS {

	@Override
	public List<Reservas> getReservasPorTurno(Equipo reservable, Date fechaInicio, Date fechaFin ) 
	{
	     try 
	     {
	    	 return entityManager.createNamedQuery(Reservas.GET_RESERVAS_X_TURNO)
	    			 			 .setParameter("reservable", reservable)
	    	 					 .setParameter("fechainicio", fechaInicio, TemporalType.DATE)
	    	 					 .setParameter("fechafin", fechaFin, TemporalType.DATE)
	    	 					 .getResultList(); 
	     }
	     catch (Exception ex) 
	     {
	    	 return new ArrayList<>();
	     }
	}

	@Override
	public List<Reservas> getReservasPorTurnoByTipoReservable(TipoReservable tr, Date fechaInicio, Date fechaFin ) 
	{
	     try 
	     {
	    	 return entityManager.createNamedQuery(Reservas.GET_RESERVAS_X_TURNO_BY_TIPORESERVABLE)
	    			 			 .setParameter("tiporeservable", tr)
	    	 					 .setParameter("fechainicio", fechaInicio, TemporalType.DATE)
	    	 					 .setParameter("fechafin", fechaFin, TemporalType.DATE)
	    	 					 .getResultList(); 
	     }
	     catch (Exception ex) 
	     {
	    	 return new ArrayList<>();
	     }
	}
	
	@Override
	public List<Reservas> getReservasPorTurnoTecnico(Usuario usuarioTecnicoAsignado, Date fechaInicio, Date fechaFin ) 
	{
	     try 
	     {
	    	 return entityManager.createNamedQuery(Reservas.GET_RESERVAS_X_TURNO_TECNICO)
	    			 			 .setParameter("usuariotecnicoasignado", usuarioTecnicoAsignado )
	    			 			 .setParameter("fechainicio", fechaInicio, TemporalType.DATE )
	    			 			 .setParameter("fechafin", fechaFin, TemporalType.DATE )
	    	 					 .getResultList(); 
	     }
	     catch (Exception ex) 
	     {
	    	 return new ArrayList<>();
	     }
	}
	
	@Override
	public List<Reservas> getReservasPorTurnoTecnicoDisponibilidadMod(Usuario usuarioTecnicoAsignado, Long codReserva, Date fechaInicio, Date fechaFin) 
	{
	     try 
	     {
	    	 return entityManager.createNamedQuery(Reservas.GET_RESERVAS_X_TURNO_TECNICO)
	    			 			 .setParameter("codreserva", codReserva)
	    			 			 .setParameter("usuariotecnicoasignado", usuarioTecnicoAsignado )
	    			 			 .setParameter("fechainicio", fechaInicio, TemporalType.DATE )
	    			 			 .setParameter("fechafin", fechaFin, TemporalType.DATE )
	    	 					 .getResultList(); 
	     }
	     catch (Exception ex) 
	     {
	    	 return new ArrayList<>();
	     }
	}
	
	@Override
	public List<Reservas> getReservasPorConsumo(Consumo c) 
	{
		try 
		{
			 return entityManager.createNamedQuery(Reservas.GET_RESERVAS_CONSUMO).setParameter( "consumo", c ).getResultList(); 
		}
		catch(Exception ex) 
		{
			return new ArrayList<>();
		}
	}

	@Override
	public List<Reservas> getReservasActivasPorConsumo(Consumo c) 
	{
		try 
		{
			 return entityManager.createNamedQuery(Reservas.GET_RESERVAS_ACTIVAS_X_CONSUMO).setParameter( "consumo", c ).getResultList(); 
		}
		catch(Exception ex) 
		{
			return new ArrayList<>();
		}
	}
	
	@Override
	public void crear(Reservas reserva) throws SaiException 
	{
		try
		{
			this.persist(reserva, true);
		}
		catch ( final Exception e ) 
		{
			log.error( "Error al crear reserva:", e );
			throw new SaiException(e.getMessage());
		}
	}

	@Override
	public void modificar(Reservas reserva) throws SaiException 
	{
		try
		{
			this.update(reserva, true);
		}
		catch ( final Exception e ) 
		{
			log.error( "Error al modificar reserva:", e );
			throw new SaiException(e.getMessage());
		}
	}

	@Override
	public void eliminar(Reservas reserva) throws SaiException
	{
		try
		{
			this.delete(reserva, true );
		}
		catch ( final Exception e ) 
		{
			log.error( "Error al eliminar reserva:", e );
			throw new SaiException(e.getMessage());
		}		
	}
    
}
