package es.um.atica.sai.das.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import es.um.atica.commons.utils.UtilString;
import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.sai.das.interfaces.TipoReservableDAS;
import es.um.atica.sai.dtos.TipoPerfil;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.TipoHorario;
import es.um.atica.sai.entities.TipoReservable;
import es.um.atica.sai.exceptions.SaiException;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.utils.Utilidades;

@Stateless
@Name(value = TipoReservableDAS.NAME)
@AutoCreate
public class TipoReservableDASImpl extends DataAccessServiceImpl<TipoReservable> implements TipoReservableDAS {

	@In(create=true)
	private SaiIdentity identity;

	private static final String SERVICIOS = "listaservicios";

	private static final String[] RESTRICCIONES = {
			UtilString.cmpNumberEjbql("tr.servicio","#{tiposReservables.servicioBuscar}"),
			Utilidades.cmpTextFilterEjbqlCompletoIgnoraTildes("tr.descripcion", "#{tiposReservables.descripcionBuscar}" ),
			UtilString.cmpTextFilterEjbql("tr.estado", "#{tiposReservables.estadoBuscar}" )};

	private List<String> getListaRestricciones()
	{
		return Arrays.asList(RESTRICCIONES);
	}

	@SuppressWarnings( "unchecked" )
	@Override
	public List<TipoReservable> getListaTiposReservableByUsuarioConectado() 
	{
		try 
		{
			if ( identity.tienePerfil( TipoPerfil.GESTOR ) ) 
			{
				return getEntityManager().createNamedQuery( TipoReservable.GET_TIPOSRESERVABLE ).getResultList();
			} 
			else 
			{
				final List<Servicio> listaServiciosBuscar = identity.getServiciosPerfil( TipoPerfil.SUPERVISOR );
				return getEntityManager().createNamedQuery( TipoReservable.GET_TIPOSRESERVABLE_X_LISTASERVICIOS )
						.setParameter( SERVICIOS, listaServiciosBuscar ).getResultList();
			}
		} catch ( final Exception ex ) {
			return new ArrayList<>();
		}
	}

	@Override
	public List<TipoReservable> buscar()
	{
		final Map<String, Object> parameters = new HashMap<>();
		final Map<String,Object> hints = new HashMap<>();
		try
		{
			if (identity.tienePerfil(TipoPerfil.GESTOR))
			{
				return this.findByEntityNamedQueryWithDinamicFilter(TipoReservable.GET_TIPOSRESERVABLE,
						getListaRestricciones(),
						parameters, null, null, null, "and", hints);
			}
			else
			{
				final List<Servicio> listaServiciosBuscar = identity.getServiciosPerfil(TipoPerfil.SUPERVISOR);
				parameters.put( SERVICIOS, listaServiciosBuscar );
				return this.findByEntityNamedQueryWithDinamicFilter(TipoReservable.GET_TIPOSRESERVABLE_X_LISTASERVICIOS,
						getListaRestricciones(),
						parameters, null, null, null, "and", hints);
			}
		}
		catch(final Exception ex)
		{
			return new ArrayList<>();
		}
	}

	@Override
	public List<TipoReservable> getListaTiposReservableByServicio(Servicio servicio)
	{
		try
		{
			return getEntityManager().createNamedQuery(TipoReservable.GET_TIPOSRESERVABLE_X_SERVICIO).setParameter("servicio", servicio ).getResultList();
		}
		catch(final Exception ex)
		{
			log.error("Error en getListaTiposReservableByServicio: #0",ex.toString());
			return new ArrayList<>();
		}
	}

	@Override
	public List<TipoReservable> getListaTiposReservableByTipoHorario(TipoHorario tipoHorario)
	{
		try
		{
			if (identity.tienePerfil(TipoPerfil.GESTOR))
			{
				return getEntityManager().createNamedQuery(TipoReservable.GET_TIPOSRESERVABLE_X_TIPOHORARIO).setParameter("tipohorario", tipoHorario).getResultList();
			}
			else
			{
				final List<Servicio> listaServiciosBuscar = identity.getServiciosPerfil(TipoPerfil.SUPERVISOR);
				return getEntityManager()
						.createNamedQuery( TipoReservable.GET_TIPOSRESERVABLE_X_TIPOHORARIO_LISTASERVICIOS )
						.setParameter( "tipohorario", tipoHorario ).setParameter( SERVICIOS, listaServiciosBuscar )
						.getResultList();
			}
		}
		catch(final Exception ex)
		{
			return new ArrayList<>();
		}
	}

	@Override
	public void crear(TipoReservable tr) throws SaiException
	{
		try
		{
			this.persist(tr, true);
		}
		catch(final Exception ex)
		{
			log.error( "Error inesperado. No se ha podido crear el tipo de reservable:", ex );
			throw new SaiException(ex.getMessage());
		}
	}

	@Override
	public void modificar(TipoReservable tr ) throws SaiException
	{
		try
		{
			this.update( tr, true );
		}
		catch ( final Exception e )
		{

			log.error( "Error inesperado. No se ha podido modificar el tipo de reservable:", e );
			throw new SaiException(e.getMessage());
		}
	}

	@Override
	public void eliminar(TipoReservable tr) throws SaiException
	{
		try
		{
			this.delete(tr, true );
		}
		catch ( final Exception e )
		{
			log.error( "Error inesperado. No se ha podido eliminar el tipo de reservable:", e );
			throw new SaiException(e.getMessage());
		}
	}



}
