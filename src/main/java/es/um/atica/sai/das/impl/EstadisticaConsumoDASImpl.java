package es.um.atica.sai.das.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.jboss.seam.annotations.Name;

import es.um.atica.commons.utils.UtilString;
import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.sai.das.interfaces.EstadisticaConsumoDAS;
import es.um.atica.sai.dtos.EstadisticaConsumo;
import es.um.atica.sai.entities.Consumo;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.seam.framework.Query.QueryPriority;

@Local( EstadisticaConsumoDAS.class )
@Name( "estadisticaConsumoDAS" )
@Stateless
public class EstadisticaConsumoDASImpl extends DataAccessServiceImpl<EstadisticaConsumo>
implements EstadisticaConsumoDAS {

	private static final String PRODUCTO_SERVICIO = "prod.cod_servicio";
	private static final String FECHA_SOLICITUD_MAYOR = "con.fecha_solicitud >=";
	private static final String FECHA_SOLICITUD_MENOR = "con.fecha_solicitud <=";
	private static final String TO_DATE = "TO_DATE(";
	private static final String DATE_FORMAT = ",'dd/MM/yyyy')";

	private static final String[] RESTRICCIONES_ESTADISTICAS = {
			UtilString.cmpNumberEjbql("con.ir_asociado", "#{estadisticaConsumo.usuarioIRBuscar.cod}" ),
			UtilString.cmpNumberEjbql("con.cod_usuario_solic", "#{estadisticaConsumo.solicitanteBuscar.cod}" ),
			UtilString.cmpTextFilterEjbql(PRODUCTO_SERVICIO, "#{estadisticaConsumo.servicioBuscar.cod}" ),
			new StringBuilder(FECHA_SOLICITUD_MAYOR).append(TO_DATE).append("#{estadisticaConsumo.fechaSolicitudDesdeBuscarString}").append(DATE_FORMAT).toString(),
			new StringBuilder(FECHA_SOLICITUD_MENOR).append(TO_DATE).append("#{estadisticaConsumo.fechaSolicitudHastaBuscarString}").append(DATE_FORMAT).toString(),
	};

	private static List<String> getListaRestriccionesEstadisticas() {
		return Arrays.asList( RESTRICCIONES_ESTADISTICAS );
	}

	@Override
	public List<EstadisticaConsumo> estadisticasConsumos( boolean byProducto, List<Servicio> listaServicios ) {
		final Map<String, Object> parametros = new HashMap<>();
		final Map<String, Object> hints = new HashMap<>();
		final List<Long> listaCodigos = new ArrayList<>();
		for ( final Servicio s : listaServicios ) {
			listaCodigos.add( s.getCod() );
		}
		try {
			parametros.put( "cod_servicios", listaCodigos );
			if ( byProducto ) {
				return this.findByDtoNamedNativeQueryWithDinamicFilter( Consumo.GET_ESTADISTICAS_GROUPBY_PRODUCTO,
						getListaRestriccionesEstadisticas(), parametros, null, null, null, "and", hints, null,
						QueryPriority.BOTH, QueryPriority.QUERY );
			} else {
				return this.findByDtoNamedNativeQueryWithDinamicFilter( Consumo.GET_ESTADISTICAS_GROUPBY_SERVICIO,
						getListaRestriccionesEstadisticas(), parametros, null, null, null, "and", hints, null,
						QueryPriority.BOTH, QueryPriority.QUERY );
			}
		} catch ( final Exception ex ) {
			log.error( "Error en estadisticasConsumos: #0", ex.toString() );
			return new ArrayList<>();
		}
	}
}
