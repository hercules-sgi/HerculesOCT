package es.um.atica.sai.das.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.primefaces.model.SortOrder;

import es.um.atica.commons.utils.UtilString;
import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.sai.das.interfaces.EquipoDAS;
import es.um.atica.sai.dtos.TipoPerfil;
import es.um.atica.sai.entities.Equipo;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.TipoReservable;
import es.um.atica.sai.exceptions.SaiException;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.utils.Utilidades;
import es.um.atica.seam.framework.Query.QueryPriority;

@Name( EquipoDASImpl.NAME )
@Stateless
@Local(EquipoDAS.class)
public class EquipoDASImpl  extends  DataAccessServiceImpl<Equipo>  implements EquipoDAS {

	public static final String NAME = "equipoDAS";

	@In( "org.jboss.seam.security.identity" )
	SaiIdentity identity;

	private static final String[] RESTRICCIONESBUSQUEDA = {
			Utilidades.cmpTextFilterEjbqlCompletoIgnoraTildes( "r.descripcion", "#{equipos.descripcionBuscar}" ),
			new StringBuilder( "(r.fecha_compra IS NULL OR r.fecha_compra >=" ).append( "TO_DATE(" ).append( "#{equipos.fechaCompraDesdeString}" ).append( ",'dd/mm/yyyy'))" ).toString(),
			new StringBuilder( "(r.fecha_compra IS NULL OR r.fecha_compra <=" ).append( "TO_DATE(" ).append( "#{equipos.fechaCompraHastaString}" ).append( ",'dd/mm/yyyy'))" ).toString(),
			"r.cod_servicio = #{equipos.servicioBuscar.cod}",
			UtilString.cmpTextFilterEjbql( "r.estado", "#{equipos.estadoBuscar}" ),
	};

	private static List<String> getListaRestriccionesbusqueda() {
		return Arrays.asList( RESTRICCIONESBUSQUEDA );
	}

	@Override
	public List<Equipo> getReservablesByTipoReservable( TipoReservable t ) {
		try
		{
			return entityManager.createNamedQuery(Equipo.GET_RESERVABLE_BY_TIPO).setParameter( "tipo", t ).getResultList();
		}
		catch (final Exception ex)
		{
			return new ArrayList<>();
		}
	}

	@Override
	public List<Equipo> getEquiposByServicio(Servicio servicio)
	{
		try
		{
			return entityManager.createNamedQuery(Equipo.GET_EQUIPOS_BY_SERVICIO).setParameter("servicio", servicio).getResultList();
		}
		catch (final Exception ex)
		{
			return new ArrayList<>();
		}
	}

	@Override
	public void crear(Equipo r) throws SaiException {
		try {
			this.persist( r, true );
		} catch ( final Exception e ) {

			log.error( "Error inesperado. No se ha podido crear el reservable:", e );
			throw new SaiException(e.getMessage());
		}
	}

	@Override
	public void modificar(Equipo r) throws SaiException {
		try {
			this.update(r, true );
		} catch ( final Exception e ) {

			log.error( "Error inesperado. No se ha podido modificar el reservable:", e );
			throw new SaiException(e.getMessage());
		}
	}

	@Override
	public void eliminar(Equipo r) throws SaiException {
		try {
			this.delete( r, true );
		} catch ( final Exception e ) {

			log.error( "Error inesperado. No se ha podido eliminar el reservable:", e );
			throw new SaiException(e.getMessage());
		}
	}

	@Override
	public List<Equipo> busquedaReservables(Long codTipo, int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters ) 
	{
		final Map<String, Object> params = new HashMap<>();
		final Map<String, Object> hints = new HashMap<>();
		final StringBuilder consulta = new StringBuilder();
		if ( identity.tienePerfil( TipoPerfil.GESTOR ) ) 
		{
			consulta.append( "SELECT r.* FROM SAI.RESERVABLE r " );
			if ( ( codTipo != null ) && codTipo.equals( ( long ) 0 ) ) 
			{
				consulta.append( "WHERE r.cod_tipo_reservable IS NULL" );
			} 
			else if ( ( codTipo != null ) && ( codTipo > 0 ) ) 
			{
				consulta.append( "WHERE r.cod_tipo_reservable = " ).append( codTipo.toString() );
			}
		} 
		else 
		{
			final List<Servicio> listaServiciosBuscar = identity.getServiciosPerfil( TipoPerfil.SUPERVISOR );
			final List<BigDecimal> listaCodigos = new ArrayList<>();
			for ( final Servicio s : listaServiciosBuscar ) 
			{
				listaCodigos.add( BigDecimal.valueOf( s.getCod() ) );
			}
			params.put( "servicios", listaCodigos );
			consulta.append( "SELECT r.* FROM SAI.RESERVABLE r WHERE r.cod_servicio IN (:servicios)" );
			if ( ( codTipo != null ) && codTipo.equals( ( long ) 0 ) ) 
			{
				consulta.append( "AND r.cod_tipo_reservable IS NULL" );
			} 
			else if ( ( codTipo != null ) && ( codTipo > 0 ) ) 
			{
				consulta.append( "AND r.cod_tipo_reservable = " ).append( codTipo.toString() );
			}
		}
		try {
			return this.findByEntityNativeQueryWithDinamicFilter( consulta.toString(), getListaRestriccionesbusqueda(),
					params,
					first, pageSize, sortField, sortOrder.name(), "and", hints, null, QueryPriority.JAVA,
					QueryPriority.QUERY );
		} catch ( final Exception ex ) {
			log.error( "Error en busquedaReservables: #0", ex.toString() );
			return new ArrayList<>();
		}
	}

	@Override
	public int getCountBusquedaReservables( Long codTipo ) {
		final Map<String, Object> params = new HashMap<>();
		final Map<String, Object> hints = new HashMap<>();
		final StringBuilder consulta = new StringBuilder();
		if ( identity.tienePerfil( TipoPerfil.GESTOR ) ) 
		{
			consulta.append( "SELECT r.* FROM SAI.RESERVABLE r " );
			if ( ( codTipo != null ) && codTipo.equals( ( long ) 0 ) ) 
			{
				consulta.append( "WHERE r.cod_tipo_reservable IS NULL" );
			} 
			else if ( ( codTipo != null ) && ( codTipo > 0 ) ) 
			{
				consulta.append( "WHERE r.cod_tipo_reservable = " ).append( codTipo.toString() );
			}
		} 
		else 
		{
			final List<Servicio> listaServiciosBuscar = identity.getServiciosPerfil( TipoPerfil.SUPERVISOR );
			final List<BigDecimal> listaCodigos = new ArrayList<>();
			for ( final Servicio s : listaServiciosBuscar ) 
			{
				listaCodigos.add( BigDecimal.valueOf( s.getCod() ) );
			}
			params.put( "servicios", listaCodigos );
			consulta.append( "SELECT r.* FROM SAI.RESERVABLE r WHERE r.cod_servicio IN (:servicios)" );
			if ( ( codTipo != null ) && codTipo.equals( ( long ) 0 ) ) 
			{
				consulta.append( "AND r.cod_tipo_reservable IS NULL" );
			} 
			else if ( ( codTipo != null ) && ( codTipo > 0 ) ) 
			{
				consulta.append( "AND r.cod_tipo_reservable = " ).append( codTipo.toString() );

			}
		}
		try {
			final List<Equipo> listaReservables = this.findByEntityNativeQueryWithDinamicFilter( consulta.toString(),
					getListaRestriccionesbusqueda(), params, null, null, null, null, "and", hints, null,
					QueryPriority.JAVA, QueryPriority.QUERY );
			return listaReservables.size();
		} catch ( final Exception ex ) {
			log.error( "Error en getCountBusquedaReservables: #0", ex.toString() );
			return 0;
		}
	}

	@Override
	public boolean existeEquipobyDescripcion( Equipo e ) {
		try {
			return !entityManager.createNamedQuery( Equipo.EXISTE_EQUIPO ).setParameter( "desc", e.getDescripcion() )
					.setParameter( "servicio", e.getServicio() )
					.getResultList().isEmpty();
		} catch ( final Exception ex ) {
			return false;
		}
	}

	@Override
	public boolean existeOtroEquipobyDescripcion( Equipo e ) {
		try {
			return !entityManager.createNamedQuery( Equipo.EXISTE_OTRO_EQUIPO )
					.setParameter( "servicio", e.getServicio() )
					.setParameter( "desc", e.getDescripcion() ).setParameter( "codigo", e.getCod() ).getResultList()
					.isEmpty();
		} catch ( final Exception ex ) {
			return false;
		}
	}
}
