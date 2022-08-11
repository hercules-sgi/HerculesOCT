package es.um.atica.sai.das.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.jboss.seam.annotations.Name;

import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.sai.das.interfaces.HorarioDiaDAS;
import es.um.atica.sai.entities.HorarioDia;
import es.um.atica.sai.entities.TipoHorario;
import es.um.atica.sai.entities.TipoReservable;
import es.um.atica.sai.exceptions.SaiException;

@Name("horarioDiaDAS")
@Stateless
@Local(HorarioDiaDAS.class)
public class HorarioDiaDASImpl  extends  DataAccessServiceImpl<HorarioDia>  implements HorarioDiaDAS {

	private static final String PARAM_CODTIPORESERVABLE = "codtiporeservable";

	private static final String SQL_GET_MIN_HORAINIMANANA = "SELECT MIN(hora_ini_manana) FROM sai.horario_dia HD, sai.reservable_horario RH WHERE HD.cod_tipo_horario=RH.cod_horario AND RH.cod_tipo_reservable=:codtiporeservable AND HD.hora_ini_manana IS NOT NULL";
	private static final String SQL_GET_MIN_HORAINITARDE = "SELECT MIN(hora_ini_tarde) FROM sai.horario_dia HD, sai.reservable_horario RH WHERE HD.cod_tipo_horario=RH.cod_horario AND RH.cod_tipo_reservable=:codtiporeservable AND HD.hora_ini_tarde IS NOT NULL";
	private static final String SQL_GET_MAX_HORAFINMANANA = "SELECT MAX(hora_fin_manana) FROM sai.horario_dia HD, sai.reservable_horario RH WHERE HD.cod_tipo_horario=RH.cod_horario AND RH.cod_tipo_reservable=:codtiporeservable AND HD.hora_fin_manana IS NOT NULL";
	private static final String SQL_GET_MAX_HORAFINTARDE = "SELECT MAX(hora_fin_tarde) FROM sai.horario_dia HD, sai.reservable_horario RH WHERE HD.cod_tipo_horario=RH.cod_horario AND RH.cod_tipo_reservable=:codtiporeservable AND HD.hora_fin_tarde IS NOT NULL";
	private static final String SQL_EXISTE_HORARIODIA = "SELECT COUNT(*) FROM sai.horario_dia HD, sai.reservable_horario RH WHERE HD.cod_tipo_horario=RH.cod_horario AND RH.cod_tipo_reservable=:codtiporeservable AND ((HD.hora_ini_manana IS NOT NULL AND HD.hora_fin_manana IS NOT NULL) OR (HD.hora_ini_tarde IS NOT NULL AND HD.hora_fin_tarde IS NOT NULL))"; 

	public void crear(HorarioDia hd) throws SaiException
	{
		try
		{
			this.persist(hd, true);
		}
		catch(Exception ex)
		{
			throw new SaiException(ex.getMessage());
		}
	}
	
	public void modificar(HorarioDia hd) throws SaiException
	{
		try
		{
			this.update(hd, true);
		}
		catch(Exception ex)
		{
			throw new SaiException(ex.getMessage());
		}
	}
	
	public void eliminar(HorarioDia hd) throws SaiException
	{
		try
		{
			this.delete(hd, true);
		}
		catch(Exception ex)
		{
			throw new SaiException(ex.getMessage());
		}
	}

	
	public String getMinHoraInicioManana(TipoReservable tr)
	{
		try 
		{
			return (String) this.getEntityManager().createNativeQuery(SQL_GET_MIN_HORAINIMANANA).setParameter(PARAM_CODTIPORESERVABLE, tr.getCod()).getSingleResult();
		}
		catch (Exception ex) 
		{
   	   	 	return null;
		}
	}
	
	public String getMinHoraInicioTarde(TipoReservable tr)
	{
		try 
		{
			return (String) this.getEntityManager().createNativeQuery(SQL_GET_MIN_HORAINITARDE).setParameter(PARAM_CODTIPORESERVABLE, tr.getCod()).getSingleResult();
		}
		catch (Exception ex) 
		{
   	   	 	return null;
		}
	}
	
	public String getMaxHoraFinManana(TipoReservable tr)
	{
		try 
		{
			return (String) this.getEntityManager().createNativeQuery(SQL_GET_MAX_HORAFINMANANA).setParameter(PARAM_CODTIPORESERVABLE, tr.getCod()).getSingleResult();
		}
		catch (Exception ex) 
		{
   	   	 	return null;
		}
	}

	public String getMaxHoraFinTarde(TipoReservable tr)
	{
		try 
		{
			return (String) this.getEntityManager().createNativeQuery(SQL_GET_MAX_HORAFINTARDE).setParameter(PARAM_CODTIPORESERVABLE, tr.getCod()).getSingleResult();
		}
		catch (Exception ex) 
		{
   	   	 	return null;
		}
	}

	public boolean existeHorarioDia(TipoReservable tr)
	{
		try 
		{
			int numHd = ((BigDecimal)this.getEntityManager().createNativeQuery(SQL_EXISTE_HORARIODIA).setParameter(PARAM_CODTIPORESERVABLE, tr.getCod()).getSingleResult()).intValue();
			return numHd>0;
		}
		catch (Exception ex) 
		{
   	   	 	return false;
		}
	}
	
	public HorarioDia getHorarioDiaByTipohorarioDia(TipoHorario th, int dia)
	{
		try 
		{
			return (HorarioDia) this.getEntityManager().createNamedQuery(HorarioDia.GET_HORARIODIA_X_TIPOHORARIO_DIA).setParameter("tipohorario", th).setParameter("dia", dia).getSingleResult();
		}
		catch (Exception ex) 
		{
   	   	 	return null;
		}
	}
	
	public List<HorarioDia> getListaHorarioDiaByTipohorario(TipoHorario th)
	{
		try 
		{
			return this.getEntityManager().createNamedQuery(HorarioDia.GET_HORARIODIA_X_TIPOHORARIO).setParameter("tipohorario", th).getResultList();
		}
		catch (Exception ex) 
		{
   	   	 	return new ArrayList<>();
		}
	}

}
