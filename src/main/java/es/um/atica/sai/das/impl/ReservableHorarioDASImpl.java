package es.um.atica.sai.das.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.jboss.seam.annotations.Name;

import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.sai.das.interfaces.ReservableHorarioDAS;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.ReservableHorario;
import es.um.atica.sai.entities.TipoReservable;
import es.um.atica.sai.exceptions.SaiException;

@Name("reservableHorarioDAS")
@Stateless
@Local(ReservableHorarioDAS.class)
public class ReservableHorarioDASImpl extends  DataAccessServiceImpl<ReservableHorario>  implements ReservableHorarioDAS{

	@Override
	public void crear(ReservableHorario rh) throws SaiException 
	{
		try 
		{
	    	this.persist( rh, true);
	    }
		catch(Exception e)
		{            
	        log.error( "Error al crear ReservableHorario:", e );
	        throw new SaiException(e.getMessage());
	    }
	}

	@Override
	public void eliminar(ReservableHorario rh) throws SaiException 
	{
		try 
		{
	    	this.delete(rh, true);
	    }
		catch(Exception e)
		{            
	        log.error( "Error al eliminar ReservableHorario:", e );
	        throw new SaiException(e.getMessage());
	    }
	}

	@Override
	public List<ReservableHorario> getListaReservableHorarioByTipoReservable(TipoReservable tr)
	{
		try
		{
			return this.entityManager.createNamedQuery(ReservableHorario.GET_RESERVABLEHORARIOS_BY_TIPORESERVABLE).setParameter("tiporeservable", tr).getResultList();
		}
		catch (Exception ex) 
		{
			return new ArrayList<>();
		}
	}

	@Override
	public List<ReservableHorario> getListaReservableHorarioByProducto(Producto producto)
	{
		try
		{
			return this.entityManager.createNamedQuery(ReservableHorario.GET_RESERVABLEHORARIOS_BY_PRODUCTO).setParameter("producto", producto).getResultList();
		}
		catch (Exception ex) 
		{
			return new ArrayList<>();
		}
	}
	
	@Override
	public boolean existeTiporeservableHorarioSolapado(ReservableHorario rh)
	{
		try
		{
			return ((Long)this.entityManager.createNamedQuery(ReservableHorario.EXISTE_RESERVABLE_HORARIO_SOLAPADO)
											 .setParameter("tiporeservable", rh.getTipoReservable())
											 .setParameter("fechainicio", rh.getFechaInicio())
											 .setParameter("fechafin", rh.getFechaFin())
											 .getSingleResult())>0;
		}
		catch (Exception ex) 
		{
			log.error( "Error en existeTiporeservableHorarioSolapado:", ex );
			return false;
		}
	}
	
	@Override
	public boolean existeProductoHorarioSolapado(ReservableHorario rh)
	{
		try
		{
			return ((Long)this.entityManager.createNamedQuery(ReservableHorario.EXISTE_PRODUCTO_HORARIO_SOLAPADO)
											 .setParameter("producto", rh.getProducto())
											 .setParameter("fechainicio", rh.getFechaInicio())
											 .setParameter("fechafin", rh.getFechaFin())
											 .getSingleResult())>0;
		}
		catch (Exception ex) 
		{
			log.error( "Error en existeProductoHorarioSolapado:", ex );
			return false;
		}
	}

}
