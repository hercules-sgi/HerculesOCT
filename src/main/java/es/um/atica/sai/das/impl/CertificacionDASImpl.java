package es.um.atica.sai.das.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import es.um.atica.commons.utils.UtilString;
import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.sai.das.interfaces.CertificacionDAS;
import es.um.atica.sai.entities.Certificacion;
import es.um.atica.sai.entities.TipoCertificacion;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;
import es.um.atica.sai.security.authentication.SaiIdentity;

@Name( CertificacionDASImpl.NAME )
@Stateless
@Local( CertificacionDAS.class )
public class CertificacionDASImpl extends DataAccessServiceImpl<Certificacion> implements CertificacionDAS {

	public static final String NAME = "certificacionDAS";
	private static final String DATE = "TO_DATE(";
	private static final String FORMATO = ",'dd/MM/yyyy')";

	@In("org.jboss.seam.security.identity")
	SaiIdentity identity;

	private static final String[] RESTRICCIONESBUSQUEDA = {
			"c.tipoCertificacion = #{certificacionBusquedaBean.tipoCertificacionBuscar}",
			new StringBuilder( "c.fechaAlta >=" ).append( DATE )
			.append( "#{certificacionBusquedaBean.fechaAltaDesdeBuscarString}" ).append( FORMATO )
			.toString(),
			new StringBuilder( "c.fechaAlta <=" ).append( DATE )
			.append( "#{certificacionBusquedaBean.fechaAltaHastaBuscarString}" )
					.append( FORMATO ).toString(),
			new StringBuilder( "c.fechaCaducidad >=" ).append( DATE )
			.append( "#{certificacionBusquedaBean.fechaCaducidadDesdeBuscarString}" ).append( FORMATO )
			.toString(),
			new StringBuilder( "c.fechaCaducidad <=" ).append( DATE )
			.append( "#{certificacionBusquedaBean.fechaCaducidadHastaBuscarString}" ).append( FORMATO )
			.toString(),
			"c.usuario = #{certificacionBusquedaBean.usuarioBuscar}", "c.estado = #{certificacionBusquedaBean.estado}"
	};

	private static final String[] RESTRICCIONES_SOLICITUD = {
			UtilString.cmpNumberEjbql("c.usuario", "#{certificacionSolicitud.usuarioFiltrar}"),
	};


	private static List<String> getListaRestriccionesbusqueda() {
		return Arrays.asList( RESTRICCIONESBUSQUEDA );
	}

	private static List<String> getListaRestriccionesSolicitud() {
		return Arrays.asList( RESTRICCIONES_SOLICITUD );
	}


	@Override
	public void modificar(Certificacion c) throws SaiException
	{
		try
		{
			this.update( c, true );
		}
		catch ( final Exception e ) {

			log.error( "No se ha podido modificar la certificación:", e );
			throw new SaiException(e.getMessage());
		}
	}

	@Override
	public List<Certificacion> busquedaCertificaciones() {
		final String consulta = Certificacion.GET_CERTIFICACIONES;
		final HashMap<String, Object> parameters = new HashMap<>();
		final HashMap<String, Object> hints = new HashMap<>();
		try {
			return this.findByEntityNamedQueryWithDinamicFilter( consulta, getListaRestriccionesbusqueda(), parameters,
					null, null, null, "and", hints );
		} catch ( final Exception ex ) {
			log.error( "Error en busquedaCertificaciones: #0", ex.toString() );
			return new ArrayList<>();
		}
	}

	@Override
	public List<Certificacion> getListaCertificacionesByUsuario(Usuario usuario)
	{
		try
		{
			return entityManager.createNamedQuery(Certificacion.GET_CERTIFICACIONES_X_USUARIO).setParameter("usuario", usuario).getResultList();
		}
		catch (final Exception ex)
		{
			return new ArrayList<>();
		}
	}

	@Override
	public List<Certificacion> busquedaCertificacionesByListaUsuarios(List<Usuario> listaUsuarios)
	{
		final HashMap<String, Object> params = new HashMap<>();
		params.put("listausuarios", listaUsuarios);
		try
		{
			return this.findByEntityNamedQueryWithDinamicFilter(Certificacion.GET_CERTIFICACIONES_X_LISTAUSUARIOS, getListaRestriccionesSolicitud(), params, null, null, null, "and", null);
		}
		catch (final Exception ex)
		{
			return new ArrayList<>();
		}
	}

	@Override
	public boolean existeCertificacionPendienteByTipoAndUsuario(TipoCertificacion tc, Usuario usuario)
	{
		try
		{
			return (Long)entityManager.createNamedQuery(Certificacion.EXISTE_CERTIFICACION_PENDIENTE_X_TIPO_USUARIO).setParameter("tipocertificacion", tc).setParameter("usuario", usuario).getSingleResult()>0;
		}
		catch (final Exception ex)
		{
			log.error("Error en existeCertificacionPendienteByTipoAndUsuario: ", ex);
			return false;
		}
	}
	
	@Override
	public boolean existeCertificacionActivaByTipoAndUsuario(TipoCertificacion tc, Usuario usuario)
	{
		try
		{
			return (Long)entityManager.createNamedQuery(Certificacion.EXISTE_CERTIFICACION_ACTIVA_X_TIPO_USUARIO).setParameter("tipocertificacion", tc).setParameter("usuario", usuario).getSingleResult()>0;
		}
		catch (final Exception ex)
		{
			log.error("Error en existeCertificacionActivaByTipoAndUsuario: ", ex);
			return false;
		}
	}

}
