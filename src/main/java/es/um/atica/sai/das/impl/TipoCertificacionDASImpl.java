package es.um.atica.sai.das.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.jboss.seam.annotations.Name;

import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.sai.das.interfaces.TipoCertificacionDAS;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.TipoCertificacion;
import es.um.atica.sai.exceptions.SaiException;

@Name( value = TipoCertificacionDASImpl.NAME )
@Stateless
@Local( TipoCertificacionDAS.class )
public class TipoCertificacionDASImpl extends DataAccessServiceImpl<TipoCertificacion> implements TipoCertificacionDAS {

	public static final String NAME = "tipoCertificacionDAS";

	@Override
	public void eliminar( TipoCertificacion t ) throws SaiException
	{
		try
		{
			this.delete( t, true );
		}
		catch (final Exception ex)
		{
			log.error( "Error inesperado. No se ha podido eliminar el tipo certificacion:", ex );
			throw new SaiException(ex.getMessage());
		}
	}

	@Override
	public void crear( TipoCertificacion t ) throws SaiException
	{
		try
		{
			this.persist( t, true );
		}
		catch (final Exception ex)
		{
			log.error( "Error inesperado. No se ha podido crear el tipo certificacion:", ex );
			throw new SaiException(ex.getMessage());
		}
	}

	@Override
	public void modificar( TipoCertificacion t ) throws SaiException
	{
		try
		{
			this.update( t, true );
		}
		catch (final Exception ex)
		{
			log.error( "Error inesperado. No se ha podido modificar el tipo certificacion:", ex );
			throw new SaiException(ex.getMessage());
		}
	}

	@SuppressWarnings( "unchecked" )
	@Override
	public List<TipoCertificacion> getListaTiposCertificaciones()
	{
		try
		{
			return entityManager.createNamedQuery( TipoCertificacion.GET_LISTA_TIPOSCERTIFICACIONES).getResultList();
		} catch ( final Exception ex ) {
			return new ArrayList<>();
		}
	}

	@SuppressWarnings( "unchecked" )
	@Override
	public List<TipoCertificacion> getListaTiposCertificacionesByProducto(Producto producto)
	{
		try
		{
			return entityManager.createNamedQuery(TipoCertificacion.GET_LISTA_TIPOSCERTIFICACIONES_X_PRODUCTO).setParameter("producto", producto).getResultList();
		} catch ( final Exception ex ) {
			return new ArrayList<>();
		}
	}
	
	@Override
	public boolean existeTipoByNombre( String nombre ) {
		log.info( "Realizando existeTipoByNombre " );
		try {
			return ( Long ) entityManager.createNamedQuery( TipoCertificacion.EXISTE_TIPOCERTIFICACION)
					.setParameter( "nombre", nombre ).getSingleResult() > 0;
		} catch ( final Exception ex ) {
			log.error( "Error en existeTipoByNombre", ex.toString() );
			return true;
		}
	}

	@Override
	public boolean existeOtroTipoByNombre( TipoCertificacion t ) {
		log.info( "Realizando existeTipoByNombre " );
		try {
			return ( Long ) entityManager.createNamedQuery( TipoCertificacion.EXISTE_OTRO_TIPO_CERTIFICACION )
					.setParameter( "nombre", t.getNombre() ).setParameter( "codigo", t.getCod() ).getSingleResult() > 0;
		} catch ( final Exception ex ) {
			log.error( "Error en existeOtroTipoByNombre #0", ex.toString() );
			return true;
		}
	}

}
