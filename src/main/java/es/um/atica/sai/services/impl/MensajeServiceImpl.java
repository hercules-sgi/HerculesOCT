package es.um.atica.sai.services.impl;

import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.SeamResourceBundle;
import org.jboss.seam.log.Log;

import es.um.atica.commons.utils.UtilDate;
import es.um.atica.sai.das.interfaces.ReservaEsperaDAS;
import es.um.atica.sai.das.interfaces.UsuarioDAS;
import es.um.atica.sai.dtos.EntidadRespuesta;
import es.um.atica.sai.dtos.EstadoConsumo;
import es.um.atica.sai.entities.Consumo;
import es.um.atica.sai.entities.ReservaEspera;
import es.um.atica.sai.entities.Reservas;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;
import es.um.atica.sai.paos.interfaces.SaiPao;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.ConsumoService;
import es.um.atica.sai.services.interfaces.MensajeService;
import es.um.atica.sai.services.interfaces.ReservableService;
import es.um.atica.sai.services.interfaces.ServicioService;
import es.um.atica.sai.services.interfaces.TarifaService;
import es.um.atica.sai.services.interfaces.UsuarioService;
import es.um.atica.sai.utils.Utilidades;

@Name( MensajeServiceImpl.NAME )
@Stateless
@Local(MensajeService.class)
public class MensajeServiceImpl implements MensajeService{

	public static final String NAME = "mensajeService";

	@Logger
	private Log log;

	@In(create=true)
	private SaiPao saiPao;

	@In (value = "org.jboss.seam.security.identity", required = false )
	private SaiIdentity identity;

	@In(value= UsuarioService.NAME, create=true)
	private UsuarioService usuarioService;

	@In(value= UsuarioDAS.NAME, create=true)
	private UsuarioDAS usuarioDAS;

	@In( value = ReservableService.NAME, create = true )
	private ReservableService reservableService;

	@In( value = ConsumoServiceImpl.NAME, create = true )
	private ConsumoService consumoService;

	@In( value = ServicioService.NAME, create = true )
	private ServicioService servicioService;

	@In( value = TarifaService.NAME, create = true )
	private TarifaService tarifaService;

	@In(create = true )
	private ReservaEsperaDAS reservaEsperaDAS;

	ResourceBundle srb = SeamResourceBundle.getBundle();

	private static final String SAI_EMAIL = "sai@um.es";
	private static final String ACTI_ADM_EMAIL = "acti.adm@um.es";
	private static final String NO_REPLY = "noreply@um.es";
	private static final String ESTIMADO = "mensajes.estimado";
	private static final String DOSPUNTOS_DOBLEBR = "mensajes.dospuntos.br.br";
	private static final String RECIBIDA_PETICION = "mensajes.recibida.peticion";
	private static final String MSJ_CREDENCIALES_ACCESO_1 = "mensajes.credenciales.acceso1";
	private static final String MSJ_CREDENCIALES_ACCESO_2 = "mensajes.credenciales.acceso2";
	private static final String MSJ_CREDENCIALES_ACCESO_3 = "mensajes.credenciales.acceso3";
	private static final String MSJ_CREDENCIALES_ACCESO_4 = "mensajes.credenciales.acceso4";
	private static final String MSJ_CREDENCIALES_ACCESO_5 = "mensajes.credenciales.acceso5";
	private static final String MSJ_CREDENCIALES_ACCESO_6 = "mensajes.credenciales.acceso6";
	private static final String MSJ_SALUDO = "mensajes.saludo";
	private static final String FINAL_EMAIL = "mensajes.final.email";
	private static final String SANGRIA = "     ";

	private static final String KEY_PRODUCTO = "mensajes.contenido.producto";
	private static final String KEY_PRODUCTO_PRESUPUESTO = "mensajes.contenido.producto.presupuesto";
	private static final String KEY_ASUNTO_NUEVA_SOLICITUD = "mensajes.asunto.nueva.solicitud";
	private static final String KEY_ASUNTO_INICIO = "mensajes.asunto.inicio";
	private static final String KEY_MENSAJE_CABECERA_NUEVA_SOLICITUD = "mensajes.contenido.cabecera.nueva.solicitud";
	private static final String KEY_MENSAJE_CABECERA_MODIFICAR_SOLICITUD = "mensajes.contenido.cabecera.modificar.solicitud";
	private static final String KEY_COMENTARIOS = "mensajes.contenido.cuerpo.comentarios";
	private static final String KEY_ESTILOS = "mensajes.contenido.estilos";


	private boolean enviaEmail( String envia, String recibe, String copia, String asunto, String mensaje )
	{
		try
		{
			final int resultadoEnvio = saiPao.enviaEmail( envia, recibe, copia, asunto, mensaje, null ).intValue();
			return (resultadoEnvio != -1);
		}
		catch(final Exception ex)
		{
			log.error("Error en enviaEmail: ", ex );
			return false;
		}
	}

	@Override
	public EntidadRespuesta altaUsuario(Usuario usuario, String password)
	{
		final StringBuilder mensaje = new StringBuilder( srb.getString( ESTIMADO ) )
				.append( usuario.getDatosUsuario().getNombreCompleto() ).append( srb.getString( DOSPUNTOS_DOBLEBR ) );
		if ("PEND".equals(usuario.getEstado()))
		{
			// Se trata de alguien que NO es trabajador UMU
			mensaje.append( srb.getString( RECIBIDA_PETICION ) )
			.append( srb.getString( "mensajes.peticion.body1" ) )
			.append( srb.getString( "mensajes.peticion.body2" ) ).append( MensajeServiceImpl.ACTI_ADM_EMAIL )
			.append( srb.getString( "mensajes.peticion.body3" ) ).append( MensajeServiceImpl.ACTI_ADM_EMAIL )
			.append( srb.getString( "mensajes.peticion.body4" ) )
			.append( srb.getString( "mensajes.peticion.body5" ) )
			.append( srb.getString( "mensajes.peticion.body6" ) );
			if (password != null)
			{
				// Enviamos las credenciales en un mensaje aparte para que no las reenvíe el usuario al responder correo
				final StringBuilder mensajeCredenciales = new StringBuilder( srb.getString( ESTIMADO ) )
						.append( usuario.getDatosUsuario().getNombreCompleto() )
						.append( srb.getString( DOSPUNTOS_DOBLEBR ) )
						.append( srb.getString( MSJ_CREDENCIALES_ACCESO_1 ) )
						.append( usuario.getDatosUsuario().getEmail() )
						.append( srb.getString( MSJ_CREDENCIALES_ACCESO_2 ) ).append( password )
						.append( srb.getString( MSJ_CREDENCIALES_ACCESO_3 ) )
						.append( srb.getString( MSJ_CREDENCIALES_ACCESO_4 ) )
						.append( srb.getString( MSJ_CREDENCIALES_ACCESO_5 ) )
						.append( srb.getString( MSJ_CREDENCIALES_ACCESO_6 ) ).append( srb.getString( MSJ_SALUDO ) );
				enviaEmail(MensajeServiceImpl.NO_REPLY,
						usuario.getDatosUsuario().getEmail(),
						null,
						srb.getString( "mensajes.peticion.asunto" ),
						mensajeCredenciales.toString());
			}
		}
		else if ("ALTA".equals(usuario.getEstado()))
		{
			// Se trata de alguien que SÍ es trabajador UMU
			mensaje.append( srb.getString( "mensajes.peticion.body7" ) );
		}
		mensaje.append( srb.getString( MSJ_SALUDO ) );
		final boolean envioCorrecto = enviaEmail(MensajeServiceImpl.ACTI_ADM_EMAIL,
				usuario.getDatosUsuario().getEmail(),
				null,
				srb.getString( "mensajes.peticion.asunto1" ),
				mensaje.toString());
		if (envioCorrecto)
		{
			return new EntidadRespuesta( envioCorrecto, srb.getString( "mensajes.envio.correcto" ) );
		}
		else
		{
			return new EntidadRespuesta( envioCorrecto, srb.getString( "mensajes.envio.fallo" ) );
		}
	}

	@Override
	public EntidadRespuesta renovacionUsuario(Usuario usuario)
	{
		final StringBuilder mensaje = new StringBuilder( srb.getString( ESTIMADO ) )
				.append( usuario.getDatosUsuario().getNombreCompleto() ).append( srb.getString( DOSPUNTOS_DOBLEBR ) )
				.append( srb.getString( "mensajes.renovacion.body" ) )
				.append( srb.getString( MSJ_SALUDO ) );
		final boolean	envioCorrecto = enviaEmail(MensajeServiceImpl.ACTI_ADM_EMAIL,
				usuario.getDatosUsuario().getEmail(),
				null,
				srb.getString( "mensajes.renovacion.asunto" ),
				mensaje.toString());
		if (envioCorrecto)
		{
			return new EntidadRespuesta( envioCorrecto, srb.getString( "mensajes.renovacion.correcto" ) );
		}
		else
		{
			return new EntidadRespuesta( envioCorrecto, srb.getString( "mensajes.renovacion.fallo" ) );
		}
	}

	@Override
	public EntidadRespuesta registroIr(Usuario usuario, String emailServicio)
	{
		final StringBuilder mensaje = new StringBuilder( srb.getString( ESTIMADO ) )
				.append( usuario.getDatosUsuario().getNombreCompleto() ).append( srb.getString( DOSPUNTOS_DOBLEBR ) )
				.append( srb.getString( RECIBIDA_PETICION ) )
				.append( srb.getString( "mensajes.solicitud.ir" ) )
				.append( srb.getString( MSJ_SALUDO ) );
		final boolean	envioCorrecto = enviaEmail(MensajeServiceImpl.ACTI_ADM_EMAIL,
				usuario.getDatosUsuario().getEmail(),
				emailServicio,
				srb.getString( "mensajes.solicitud.ir.asunto" ),
				mensaje.toString());
		if (envioCorrecto)
		{
			return new EntidadRespuesta( envioCorrecto, srb.getString( "mensajes.solicitud.ir.correcta" ) );
		}
		else
		{
			return new EntidadRespuesta( envioCorrecto,
					new StringBuilder( srb.getString( "mensajes.solicitud.ir.fallo" ) ).append( emailServicio )
					.toString() );
		}
	}

	@Override
	public EntidadRespuesta registroNuevoIrExterno(Usuario usuario, String password, String emailServicio)
	{
		// Email enviado desde administración con copia al servicio
		final StringBuilder mensaje = new StringBuilder( srb.getString( ESTIMADO ) )
				.append( usuario.getDatosUsuario().getNombreCompleto() ).append( srb.getString( DOSPUNTOS_DOBLEBR ) )
				.append( srb.getString( RECIBIDA_PETICION ) )
				.append( srb.getString( "mensajes.registro.ir.ext.body1" ) )
				.append( srb.getString( "mensajes.registro.ir.ext.body2" ) ).append( MensajeServiceImpl.ACTI_ADM_EMAIL )
				.append( srb.getString( "mensajes.peticion.body3" ) ).append( MensajeServiceImpl.ACTI_ADM_EMAIL )
				.append( srb.getString( "mensajes.peticion.body4" ) )
				.append( srb.getString( "mensajes.registro.ir.ext.body3" ) )
				.append( srb.getString( MSJ_SALUDO ) );
		final boolean envioCorrecto = enviaEmail(MensajeServiceImpl.ACTI_ADM_EMAIL,
				usuario.getEmail(),
				emailServicio,
				srb.getString( "mensajes.registro.ir.ext.asunto" ),
				mensaje.toString());
		// Email con credenciales
		final StringBuilder mensajeCredenciales = new StringBuilder( srb.getString( ESTIMADO ) )
				.append( usuario.getDatosUsuario().getNombreCompleto() ).append( srb.getString( DOSPUNTOS_DOBLEBR ) )
				.append( srb.getString( MSJ_CREDENCIALES_ACCESO_1 ) ).append( usuario.getEmail() )
				.append( srb.getString( MSJ_CREDENCIALES_ACCESO_2 ) ).append( password )
				.append( srb.getString( MSJ_CREDENCIALES_ACCESO_3 ) )
				.append( srb.getString( MSJ_CREDENCIALES_ACCESO_4 ) )
				.append( srb.getString( MSJ_CREDENCIALES_ACCESO_5 ) )
				.append( srb.getString( MSJ_CREDENCIALES_ACCESO_6 ) ).append( srb.getString( MSJ_SALUDO ) );
		enviaEmail(MensajeServiceImpl.NO_REPLY,
				usuario.getEmail(),
				null,
				srb.getString( "mensajes.credenciales.acceso.asunto" ),
				mensajeCredenciales.toString());
		if (envioCorrecto)
		{
			return new EntidadRespuesta( envioCorrecto, srb.getString( "mensajes.registro.ir.ext.correcto" ) );
		}
		else
		{
			return new EntidadRespuesta( envioCorrecto,
					new StringBuilder( srb.getString( "mensajes.registro.ir.ext.fallo" ) ).append( emailServicio )
					.toString() );
		}
	}

	@Override
	public boolean solicitaNuevoPassword(String email, String password)
	{
		final StringBuilder mensaje = new StringBuilder( srb.getString( MSJ_CREDENCIALES_ACCESO_1 ) ).append( email )
				.append( srb.getString( MSJ_CREDENCIALES_ACCESO_2 ) ).append( password )
				.append( srb.getString( MSJ_CREDENCIALES_ACCESO_3 ) )
				.append( srb.getString( MSJ_CREDENCIALES_ACCESO_4 ) )
				.append( srb.getString( MSJ_CREDENCIALES_ACCESO_5 ) )
				.append( srb.getString( MSJ_CREDENCIALES_ACCESO_6 ) );

		return enviaEmail( MensajeServiceImpl.NO_REPLY, email, null, srb.getString( "mensajes.solicitud.pass.asunto" ),
				mensaje.toString() );

	}

	@Override
	public void notificacionesNuevaSolicitud(Consumo consumo, String enviarEmailUsuario)
	{
		if ("NO".equals(consumo.getPresupuesto()))
		{
			// Notificación email al solicitante, independientemente del tipo de consumo
			if (!identity.getUsuarioConectado().equals(consumo.getUsuarioSolicitante()) && "SI".equals(enviarEmailUsuario))
			{
				enviarUsuarioNuevaSolicitud(consumo);
			}
			// Notificación email al IR. Un fungible nunca estará en estado PEND_VALIDACION_IR
			if (EstadoConsumo.PEND_VALIDACION_IR.equals(consumo.getEstado()))
			{
				enviarIRSolicitudPendiente(consumo);
			}
			else if (( consumo.getUsuarioIrAsociado() != null ) && !identity.getUsuarioConectado().equals(consumo.getUsuarioIrAsociado()) &&
					consumoService.avisarIrNuevaSolicitudMiembro(consumo.getUsuarioSolicitante(), consumo.getUsuarioIrAsociado(), consumo.getProducto().getServicio()))
			{
				enviarIrNuevaSolicitud(consumo);
			}
			// Notificación email a supervisores. Sólo para Reservas y Prestaciones
			if (!"F".equals(consumo.getTipo()) && EstadoConsumo.PENDIENTE.equals(consumo.getEstado()))
			{
				enviarSupervisorNuevaSolicitud(consumo);
			}
			// Notificación al técnico si ha sido asignado automáticamente. Sólo para Reservas y Prestaciones
			if (consumo.getUsuarioTecnicoAsignado() != null)
			{
				enviarTecnicoAsignacionSolicitud(consumo);
			}
		}
		else if ("P".equals(consumo.getTipo()))
		{
			if (consumo.getUsuarioTecnicoAsignado() != null)
			{
				enviarTecnicoAsignacionPresupuesto(consumo);
			}
			else
			{
				enviarSupervisoresAsignacionPresupuesto(consumo);
			}
		}
	}

	@Override
	public void notificacionesModificacionSolicitud(Consumo consumo,
			boolean modificadaListaReservas,
			boolean enviarMensajeAceptacion,
			boolean nuevoTecnicoEstablecido,
			Long codTecnicoAnteriorModificado)
	{
		if ("NO".equals(consumo.getPresupuesto()))
		{
			if ("R".equals(consumo.getTipo()))
			{
				if (identity.getUsuarioConectado().equals(consumo.getUsuarioSolicitante()) && modificadaListaReservas)
				{
					enviarUsuarioModificacionSolicitud(consumo);
				}
				if (enviarMensajeAceptacion)
				{
					enviarUsuarioAceptacionSolicitud(consumo);
				}
			}
			if (nuevoTecnicoEstablecido)
			{
				enviarTecnicoAsignacionSolicitud(consumo);
				if (( codTecnicoAnteriorModificado != null ) && !codTecnicoAnteriorModificado.equals(consumo.getUsuarioTecnicoAsignado().getCod()))
				{
					enviarTecnicoDesasignacionSolicitud(consumo, codTecnicoAnteriorModificado);
				}
			}
		}
		else if ("P".equals(consumo.getTipo()) && nuevoTecnicoEstablecido)
		{
			enviarTecnicoAsignacionPresupuesto(consumo);
		}
	}

	@Override
	public void notificacionesValidacionIrSolicitud(Consumo consumo)
	{
		if (EstadoConsumo.PENDIENTE.equals(consumo.getEstado()))
		{
			enviarSupervisorNuevaSolicitud(consumo);
		}
	}

	@Override
	public void notificacionesAceptacionSolicitud(Consumo consumo, boolean nuevoTecnicoEstablecido, Long codTecnicoAnteriorModificado)
	{
		if ("R".equals(consumo.getTipo()))
		{
			enviarUsuarioAceptacionSolicitud(consumo);
		}
		if (nuevoTecnicoEstablecido)
		{
			enviarTecnicoAsignacionSolicitud(consumo);
			if (( codTecnicoAnteriorModificado != null ) && !codTecnicoAnteriorModificado.equals(consumo.getUsuarioTecnicoAsignado().getCod()))
			{
				enviarTecnicoDesasignacionSolicitud(consumo, codTecnicoAnteriorModificado);
			}

		}
	}

	@Override
	public void notificacionesRechazoSolicitud(Consumo consumo)
	{
		enviarUsuarioRechazoSolicitud(consumo);
		if (( consumo.getUsuarioTecnicoAsignado() != null ) && consumo.getUsuarioTecnicoAsignado().equals(identity.getUsuarioConectado()))
		{
			enviarSupervisorRechazoSolicitud(consumo);
		}
	}

	@Override
	public void notificacionesFinalizacionSolicitud(Consumo consumo)
	{
		if (EstadoConsumo.FINALIZADO.equals(consumo.getEstado()))
		{
			enviarUsuarioFinalizacionSolicitud(consumo);
		}
		else if (EstadoConsumo.FINALIZADO_PARCIAL.equals(consumo.getEstado()))
		{
			enviarSupervisorFinalizacionParcialSolicitud(consumo);
		}
	}

	@Override
	public void avisosEliminacionReserva(Reservas reserva)
	{
		final List<ReservaEspera> listaReservaEsperas = reservaEsperaDAS.getReservaEsperaByReserva(reserva);
		for (final ReservaEspera re : listaReservaEsperas)
		{
			enviaEmail(MensajeServiceImpl.SAI_EMAIL,
					re.getUsuario().getDatosUsuario().getEmail(),
					null,
					new StringBuilder(srb.getString(KEY_ASUNTO_INICIO)).append(" ").append(srb.getString("mensajes.asunto.avisoanulacionreserva")).toString(),
					new StringBuilder("La reserva del equipo ").append(re.getReserva().getReservable().getDescripcion()).append(" el día ")
					.append(UtilDate.dateToString(re.getReserva().getFechaInicio(),"dd/MM/yyyy")).append(" de ")
					.append(UtilDate.convertirDateHoraToString(re.getReserva().getFechaInicio(), true)).append(" a ")
					.append(UtilDate.convertirDateHoraToString(re.getReserva().getFechaFin(), true)).append(" ha sido anulada.")
					.append("\n").append(MensajeServiceImpl.FINAL_EMAIL).toString());
		}
		if (!listaReservaEsperas.isEmpty())
		{
			for (final ReservaEspera re : listaReservaEsperas)
			{
				try
				{
					reservaEsperaDAS.eliminar( re );
				}
				catch ( final SaiException ex )
				{
					// Capturamos la excepción porque no haremos ROLLBACK si no se puede realizar la eliminación del aviso
					log.error("Error eliminando fila de ReservaEspera: ", ex);
				}
			}
		}
	}

	private boolean enviarUsuarioNuevaSolicitud( Consumo c ) {
		log.info( "Generando correo de nueva solicitud para usuario #0", c.getCod() );
		final StringBuilder asunto = new StringBuilder(srb.getString(KEY_ASUNTO_INICIO));
		StringBuilder mensaje;

		if ( c.getTipo().equals( "F" ) ) {
			asunto.append( MessageFormat.format( srb.getString( KEY_ASUNTO_NUEVA_SOLICITUD ),
					c.getProducto().getServicio().getNombre() ) );
		} else {
			asunto.append( MessageFormat.format( srb.getString( KEY_ASUNTO_NUEVA_SOLICITUD ),
					c.getProducto().getDescripcion() ) );
		}
		mensaje = new StringBuilder( srb.getString( KEY_MENSAJE_CABECERA_NUEVA_SOLICITUD ) )
				.append( construirCuerpoEmail( c ) );
		return enviaEmail( MensajeServiceImpl.NO_REPLY, c.getUsuarioSolicitante().getDatosUsuario().getEmail(), null,
				asunto.toString(),
				mensaje.toString() );
	}

	private boolean enviarUsuarioAceptacionSolicitud( Consumo c ) {
		log.info( "Generando correo de aceptación de solicitud #0", c.getCod() );
		final StringBuilder asunto = new StringBuilder(srb.getString(KEY_ASUNTO_INICIO));
		StringBuilder mensaje;

		asunto.append( MessageFormat.format( srb.getString( "mensajes.asunto.aceptar.solicitud" ),
				c.getProducto().getDescripcion() ) );
		mensaje = new StringBuilder( srb.getString( KEY_ESTILOS ) )
				.append( srb.getString( "mensajes.contenido.cabecera.aceptar.solicitud" ) )
				.append( construirCuerpoEmail( c ) );
		return enviaEmail( MensajeServiceImpl.NO_REPLY, c.getUsuarioSolicitante().getDatosUsuario().getEmail(), null,
				asunto.toString(),
				mensaje.toString() );
	}

	private boolean enviarUsuarioRechazoSolicitud( Consumo c ) {
		log.info( "Generando correo de rechazo de solicitud #0", c.getCod() );
		final StringBuilder asunto = new StringBuilder(srb.getString(KEY_ASUNTO_INICIO));
		StringBuilder mensaje;

		asunto.append( MessageFormat.format( srb.getString( "mensajes.asunto.rechazo.solicitud" ),
				c.getProducto().getDescripcion() ) );
		mensaje = new StringBuilder( srb.getString( KEY_ESTILOS ) )
				.append( srb.getString( "mensajes.contenido.cabecera.rechazo.solicitud" ) )
				.append( construirCuerpoEmail( c ) );
		return enviaEmail( MensajeServiceImpl.NO_REPLY, c.getUsuarioSolicitante().getDatosUsuario().getEmail(), null,
				asunto.toString(),
				mensaje.toString() );
	}

	private boolean enviarUsuarioModificacionSolicitud( Consumo c ) {
		log.info( "Generando correo de modificación de solicitud #0", c.getCod() );
		final StringBuilder asunto = new StringBuilder(srb.getString(KEY_ASUNTO_INICIO));
		StringBuilder mensaje;

		if ( c.getTipo().equals( "F" ) ) {
			asunto.append( MessageFormat.format( srb.getString( "mensajes.asunto.modificar.solicitud" ),
					c.getProducto().getServicio().getNombre() ) );
		} else {
			asunto.append( MessageFormat.format( srb.getString( "mensajes.asunto.modificar.solicitud" ),
					c.getProducto().getDescripcion() ) );
		}
		mensaje = new StringBuilder( srb.getString( KEY_ESTILOS ) )
				.append( srb.getString( KEY_MENSAJE_CABECERA_MODIFICAR_SOLICITUD ) )
				.append( construirCuerpoEmail( c ) );
		return enviaEmail( MensajeServiceImpl.NO_REPLY, c.getUsuarioSolicitante().getDatosUsuario().getEmail(), null,
				asunto.toString(),
				mensaje.toString() );
	}

	private boolean enviarUsuarioFinalizacionSolicitud( Consumo c ) {
		log.info( "Generando correo de finalización de solicitud al usuario #0", c.getCod() );
		final StringBuilder asunto = new StringBuilder(srb.getString(KEY_ASUNTO_INICIO));
		StringBuilder mensaje;

		asunto.append( MessageFormat.format( srb.getString( "mensajes.asunto.finalizar.solicitud" ),
				c.getProducto().getDescripcion() ) );
		mensaje = new StringBuilder( srb.getString( KEY_ESTILOS ) )
				.append( srb.getString( "mensajes.contenido.cabecera.finalizar.solicitud" ) )
				.append( construirCuerpoEmail( c ) );
		return enviaEmail( MensajeServiceImpl.NO_REPLY, c.getUsuarioSolicitante().getDatosUsuario().getEmail(), null,
				asunto.toString(),
				mensaje.toString() );
	}

	@Override
	public boolean enviarUsuarioComentarioSolicitud( Consumo c ) 
	{
		log.info( "Generando correo de comentario en solicitud #0", c.getCod() );
		final StringBuilder asunto = new StringBuilder(srb.getString(KEY_ASUNTO_INICIO));
		StringBuilder mensaje;
		asunto.append(MessageFormat.format(srb.getString("mensajes.asunto.comentario.usuario"), c.getProducto().getDescripcion()));
		mensaje = new StringBuilder(srb.getString(KEY_ESTILOS)).append(srb.getString("mensajes.contenido.cabecera.comentario.solicitud"))
					.append(construirCuerpoEmail(c)).append(MessageFormat.format(srb.getString(KEY_COMENTARIOS), c.getBitacoraUsuario()));
		return enviaEmail(MensajeServiceImpl.NO_REPLY, c.getUsuarioSolicitante().getDatosUsuario().getEmail(), null, asunto.toString(), mensaje.toString());
	}

	private boolean enviarSupervisorNuevaSolicitud( Consumo c ) {
		log.info( "Generando correo de nueva solicitud para supervisor #0", c.getCod() );
		final StringBuilder asunto = new StringBuilder(srb.getString(KEY_ASUNTO_INICIO));
		StringBuilder mensaje;

		if ( c.getTipo().equals( "F" ) ) {
			asunto.append( MessageFormat.format( srb.getString( KEY_ASUNTO_NUEVA_SOLICITUD ),
					c.getProducto().getServicio().getNombre() ) );
		} else {
			asunto.append(
					MessageFormat.format( srb.getString( KEY_ASUNTO_NUEVA_SOLICITUD ),
							c.getProducto().getDescripcion() ) );
		}
		mensaje = new StringBuilder( srb.getString( KEY_ESTILOS ) )
				.append( srb.getString( KEY_MENSAJE_CABECERA_NUEVA_SOLICITUD ) )
				.append( construirCuerpoEmail( c ) );
		final String destinatarios = getlistaEmails(
				servicioService.getListaSupervisoresServicio( c.getProducto().getServicio() ) );
		if ( !destinatarios.equals( "" ) ) {
			return enviaEmail( MensajeServiceImpl.NO_REPLY, destinatarios, null, asunto.toString(),
					mensaje.toString() );
		}
		return false;
	}

	private boolean enviarSupervisorRechazoSolicitud( Consumo c ) {
		log.info( "Generando correo de rechazo de solicitud #0", c.getCod() );
		final StringBuilder asunto = new StringBuilder(srb.getString(KEY_ASUNTO_INICIO));
		StringBuilder mensaje;

		asunto.append( MessageFormat.format( srb.getString( "mensajes.asunto.rechazo.solicitud" ),
				c.getProducto().getDescripcion() ) );
		mensaje = new StringBuilder( srb.getString( KEY_ESTILOS ) )
				.append( srb.getString( "mensajes.contenido.cabecera.rechazo.solicitud" ) )
				.append( construirCuerpoEmail( c ) );
		final String destinatarios = getlistaEmails(
				servicioService.getListaSupervisoresServicio( c.getProducto().getServicio() ) );
		if ( !destinatarios.equals( "" ) ) {
			return enviaEmail( MensajeServiceImpl.NO_REPLY, destinatarios, null, asunto.toString(),
					mensaje.toString() );
		}
		return false;
	}

	private boolean enviarSupervisorFinalizacionParcialSolicitud( Consumo c ) {
		log.info( "Generando correo de finalización parcial de solicitud para supervisor #0", c.getCod() );
		final StringBuilder asunto = new StringBuilder(srb.getString(KEY_ASUNTO_INICIO));
		StringBuilder mensaje;

		asunto.append( MessageFormat.format( srb.getString( "mensajes.asunto.finalizacion.parcial.solicitud" ),
				c.getProducto().getDescripcion() ) );
		mensaje = new StringBuilder( srb.getString( KEY_ESTILOS ) )
				.append( srb.getString( "mensajes.contenido.cabecera.finalizacion.parcial.solicitud" ) )
				.append( construirCuerpoEmail( c ) );
		final String destinatarios = getlistaEmails(
				servicioService.getListaSupervisoresServicio( c.getProducto().getServicio() ) );
		if ( !destinatarios.equals( "" ) ) {
			return enviaEmail( MensajeServiceImpl.NO_REPLY, destinatarios, null, asunto.toString(),
					mensaje.toString() );
		}
		return false;
	}

	private boolean enviarIrNuevaSolicitud( Consumo c ) {
		log.info( "Generando correo de nueva solicitud para IR #0", c.getCod() );
		final StringBuilder asunto = new StringBuilder(srb.getString(KEY_ASUNTO_INICIO));
		StringBuilder mensaje;

		if ( c.getTipo().equals( "F" ) ) {
			asunto.append( MessageFormat.format( srb.getString( KEY_ASUNTO_NUEVA_SOLICITUD ),
					c.getProducto().getServicio().getNombre() ) );
		} else {
			asunto.append(
					MessageFormat.format( srb.getString( KEY_ASUNTO_NUEVA_SOLICITUD ),
							c.getProducto().getDescripcion() ) );
		}
		if ( c.getUsuarioIrAsociado() != null ) {
			mensaje = new StringBuilder( srb.getString( KEY_ESTILOS ) )
					.append( srb.getString( KEY_MENSAJE_CABECERA_NUEVA_SOLICITUD ) )
					.append( construirCuerpoEmail( c ) );
			return enviaEmail( MensajeServiceImpl.NO_REPLY, c.getUsuarioIrAsociado().getDatosUsuario().getEmail(), null,
					asunto.toString(), mensaje.toString() );
		}
		return false;
	}

	private boolean enviarIRSolicitudPendiente( Consumo c ) {
		log.info( "Generando correo de solicitud pendiente #0", c.getCod() );
		final StringBuilder asunto = new StringBuilder(srb.getString(KEY_ASUNTO_INICIO));
		StringBuilder mensaje;

		asunto.append( MessageFormat.format( srb.getString( "mensajes.asunto.pendiente.solicitud" ),
				c.getProducto().getDescripcion() ) );
		if ( c.getUsuarioIrAsociado() != null ) {
			mensaje = new StringBuilder( srb.getString( KEY_ESTILOS ) )
					.append( srb.getString( "mensajes.contenido.cabecera.pendiente.solicitud" ) )
					.append( construirCuerpoEmail( c ) ).append( MessageFormat.format(
							srb.getString( "mensajes.contenido.cuerpo.ir" ),
							c.getUsuarioIrAsociado().getDatosUsuario().getFullName() ) );
			return enviaEmail( MensajeServiceImpl.NO_REPLY, c.getUsuarioIrAsociado().getDatosUsuario().getEmail(), null,
					asunto.toString(), mensaje.toString() );
		}
		return false;
	}

	private boolean enviarTecnicoAsignacionSolicitud( Consumo c ) {
		log.info( "Generando correo de asignación de solicitud #0", c.getCod() );
		final StringBuilder asunto = new StringBuilder(srb.getString(KEY_ASUNTO_INICIO));
		StringBuilder mensaje;

		asunto.append( MessageFormat.format( srb.getString( "mensajes.asunto.asignar.solicitud" ),
				c.getProducto().getDescripcion() ) );
		mensaje = new StringBuilder( srb.getString( KEY_ESTILOS ) )
				.append( srb.getString( "mensajes.contenido.cabecera.asignar.solicitud" ) )
				.append( construirCuerpoEmail( c ) );
		return enviaEmail( MensajeServiceImpl.NO_REPLY, c.getUsuarioTecnicoAsignado().getDatosUsuario().getEmail(),
				null, asunto.toString(), mensaje.toString() );
	}

	private boolean enviarTecnicoDesasignacionSolicitud(Consumo c, Long codTecnicoAnterior)
	{
		log.info( "Generando correo de desasignación de solicitud #0", c.getCod() );

		final Usuario usuarioTecnicoAnterior = usuarioService.getUsuarioByCod(codTecnicoAnterior);
		final StringBuilder asunto = new StringBuilder(srb.getString(KEY_ASUNTO_INICIO));
		StringBuilder mensaje;

		asunto.append( MessageFormat.format( srb.getString( "mensajes.asunto.desasignar.solicitud" ),
				c.getProducto().getDescripcion() ) );
		mensaje = new StringBuilder( srb.getString( KEY_ESTILOS ) )
				.append( srb.getString( "mensajes.contenido.cabecera.desasignar.solicitud" ) )
				.append( construirCuerpoEmail( c ) );
		return enviaEmail( MensajeServiceImpl.NO_REPLY, usuarioTecnicoAnterior.getDatosUsuario().getEmail(),
				null, asunto.toString(), mensaje.toString() );
	}

	private boolean enviarTecnicoAsignacionPresupuesto( Consumo c ) {
		log.info( "Generando correo de asignación de presupuesto #0", c.getCod() );
		final StringBuilder asunto = new StringBuilder( srb.getString( KEY_ASUNTO_INICIO ) );
		StringBuilder mensaje;

		asunto.append( MessageFormat.format( srb.getString( "mensajes.asunto.asignar.presupuesto" ),
				c.getProducto().getDescripcion() ) );
		mensaje = new StringBuilder( srb.getString( KEY_ESTILOS ) )
				.append( srb.getString( "mensajes.contenido.cabecera.asignar.presupuesto" ) )
				.append( construirCuerpoEmail( c ) );
		return enviaEmail( MensajeServiceImpl.NO_REPLY, c.getUsuarioTecnicoAsignado().getDatosUsuario().getEmail(),
				null,
				asunto.toString(), mensaje.toString() );
	}

	private boolean enviarSupervisoresAsignacionPresupuesto( Consumo c ) {
		log.info( "Generando correo de asignación de presupuesto #0", c.getCod() );
		final StringBuilder asunto = new StringBuilder( srb.getString( KEY_ASUNTO_INICIO ) );
		StringBuilder mensaje;

		asunto.append( MessageFormat.format( srb.getString( "mensajes.asunto.asignar.presupuesto" ),
				c.getProducto().getDescripcion() ) );
		mensaje = new StringBuilder( srb.getString( KEY_ESTILOS ) )
				.append( srb.getString( "mensajes.contenido.cabecera.asignar.presupuesto" ) )
				.append( construirCuerpoEmail( c ) );
		final String destinatarios = getlistaEmails(
				servicioService.getListaSupervisoresServicio( c.getProducto().getServicio() ) );
		if ( !destinatarios.equals( "" ) ) {
			return enviaEmail( MensajeServiceImpl.NO_REPLY, destinatarios, null, asunto.toString(),
					mensaje.toString() );
		}
		return false;
	}

	@Override
	public boolean enviarIrMasSolicitantePresupuestoFinalizado( Consumo c ) {
		log.info( "Generando correo de finalizacion de presupuesto al solicitante y al ir #0", c.getCod() );
		final StringBuilder asunto = new StringBuilder( srb.getString( KEY_ASUNTO_INICIO ) );
		StringBuilder mensaje;

		asunto.append( MessageFormat.format( srb.getString( "mensajes.asunto.finalizar.presupuesto" ),
				c.getProducto().getDescripcion() ) );
		mensaje = new StringBuilder( srb.getString( KEY_ESTILOS ) )
				.append( srb.getString( "mensajes.contenido.cabecera.finalizar.presupuesto" ) )
				.append( construirCuerpoEmail( c ) );
		final StringBuilder destinatarios = new StringBuilder( c.getUsuarioSolicitante().getDatosUsuario().getEmail() )
				.append( " ," );
		if ( c.getUsuarioIrAsociado() != null ) {
			destinatarios.append( c.getUsuarioIrAsociado().getDatosUsuario().getEmail() );
		}
		return enviaEmail( MensajeServiceImpl.NO_REPLY, destinatarios.toString(), null, asunto.toString(),
				mensaje.toString() );
	}

	@Override
	public boolean enviarTecnicoYSupervisoresRechazoPresupuesto( Consumo c, String comentario ) {
		log.info( "Generando correo de rechazo de presupuesto al técnico y los supervisores #0", c.getCod() );
		final StringBuilder asunto = new StringBuilder( srb.getString( KEY_ASUNTO_INICIO ) );
		StringBuilder mensaje;

		asunto.append( MessageFormat.format( srb.getString( "mensajes.asunto.rechazar.presupuesto" ),
				c.getProducto().getDescripcion() ) );
		mensaje = new StringBuilder( srb.getString( KEY_ESTILOS ) )
				.append( srb.getString( "mensajes.contenido.cabecera.rechazar.presupuesto" ) )
				.append( construirCuerpoEmail( c ) )
				.append( MessageFormat.format( srb.getString( "mensajes.contenido.presupuesto.comentario.rechazo" ),
						comentario ) );
		final StringBuilder destinatarios = new StringBuilder();
		final String supervisores = getlistaEmails(
				servicioService.getListaSupervisoresServicio( c.getProducto().getServicio() ) );
		if ( ( supervisores != null ) && !"".equals( supervisores ) ) {
			destinatarios.append( supervisores ).append( " ," );
		}
		if ( c.getUsuarioTecnicoAsignado() != null ) {
			destinatarios.append( c.getUsuarioIrAsociado().getDatosUsuario().getEmail() );
		}
		return enviaEmail( MensajeServiceImpl.NO_REPLY, destinatarios.toString(), null, asunto.toString(),
				mensaje.toString() );
	}

	@Override
	public boolean enviarTecnicoReabrirPresupuesto( Consumo c, String comentario ) {
		log.info( "Generando correo de reabrir presupuesto al técnico #0", c.getCod() );
		final StringBuilder asunto = new StringBuilder( srb.getString( KEY_ASUNTO_INICIO ) );
		StringBuilder mensaje;

		asunto.append( MessageFormat.format( srb.getString( "mensajes.asunto.reabrir.presupuesto" ),
				c.getProducto().getDescripcion() ) );
		mensaje = new StringBuilder( srb.getString( KEY_ESTILOS ) )
				.append( srb.getString( "mensajes.contenido.cabecera.reabrir.presupuesto" ) )
				.append( construirCuerpoEmail( c ) )
				.append( MessageFormat.format( srb.getString( "mensajes.contenido.presupuesto.comentario.reapertura" ),
						comentario ) );
		if ( c.getUsuarioTecnicoAsignado() != null ) {
			return enviaEmail( MensajeServiceImpl.NO_REPLY, c.getUsuarioTecnicoAsignado().getDatosUsuario().getEmail(),
					null, asunto.toString(), mensaje.toString() );
		}
		return false;
	}

	@Override
	public boolean enviarTecnicoAceptarPresupuesto( Consumo c ) {
		log.info( "Generando correo de reabrir presupuesto al técnico #0", c.getCod() );
		final StringBuilder asunto = new StringBuilder( srb.getString( KEY_ASUNTO_INICIO ) );
		StringBuilder mensaje;

		asunto.append( MessageFormat.format( srb.getString( "mensajes.asunto.aceptar.presupuesto" ),
				c.getProducto().getDescripcion() ) );
		mensaje = new StringBuilder( srb.getString( KEY_ESTILOS ) )
				.append( srb.getString( "mensajes.contenido.cabecera.aceptar.presupuesto" ) )
				.append( construirCuerpoEmail( c ) );
		if ( c.getUsuarioTecnicoAsignado() != null ) {
			return enviaEmail( MensajeServiceImpl.NO_REPLY, c.getUsuarioTecnicoAsignado().getDatosUsuario().getEmail(),
					null, asunto.toString(), mensaje.toString() );
		}
		return false;
	}

	@Override
	public boolean enviarTecnicoOSupervisorComentarioSolicitud( Consumo c ) 
	{
		log.info( "Generando correo de comentario en solicitud al técnico #0", c.getCod() );
		final StringBuilder asunto = new StringBuilder(srb.getString(KEY_ASUNTO_INICIO));
		StringBuilder mensaje;

		asunto.append(MessageFormat.format(srb.getString("mensajes.asunto.comentario.tecnico"), c.getProducto().getDescripcion()));
		mensaje = new StringBuilder(srb.getString(KEY_ESTILOS)).append(srb.getString("mensajes.contenido.cabecera.comentario.solicitud"))
					.append(construirCuerpoEmail(c) ).append(MessageFormat.format(srb.getString( KEY_COMENTARIOS ), c.getBitacoraUsuario()));
		String destinatario;
		if (c.getUsuarioTecnicoAsignado() == null) 
		{
			destinatario = getlistaEmails(servicioService.getListaSupervisoresServicio(c.getProducto().getServicio()));
		} 
		else 
		{
			destinatario = c.getUsuarioTecnicoAsignado().getDatosUsuario().getEmail();
		}
		if (destinatario != null && !destinatario.isEmpty()) 
		{
			return enviaEmail( MensajeServiceImpl.NO_REPLY, destinatario, null, asunto.toString(), mensaje.toString() );
		}
		return false;
	}

	private String getlistaEmails( List<Usuario> lista ) {
		final StringBuilder sbEnvio = new StringBuilder();
		if ( !lista.isEmpty() ) {
			for ( final Usuario u : lista ) {
				sbEnvio.append( u.getDatosUsuario().getEmail() ).append( "," );
			}
			return sbEnvio.substring( 0, sbEnvio.length() - 1 );
		}
		return "";
	}

	private String construirCuerpoEmail( Consumo c ) 
	{
		final StringBuilder body;
		if (c.getTipo().equals( "P" ) && c.getPresupuesto().equals( "SI" )) 
		{
			body = new StringBuilder(MessageFormat.format( srb.getString( "mensajes.contenido.cuerpo.presupuesto" ), c.getCod().toString(), c.getFechaSolicitud(),	c.getUsuarioSolicitante().getDatosUsuario().getFullName()));
		} 
		else 
		{
			body = new StringBuilder(MessageFormat.format(srb.getString( "mensajes.contenido.cuerpo1" ), c.getCod().toString(), c.getFechaSolicitud(), c.getUsuarioSolicitante().getDatosUsuario().getFullName()));
		}
		if (c.getUsuarioIrAsociado() != null) 
		{
			body.append(c.getUsuarioIrAsociado().getDatosUsuario().getFullName());
		}
		if (c.getEntidadPagadora() != null) 
		{
			body.append(" ").append("(").append(c.getEntidadPagadora().getNombre()).append( ")");
		}
		body.append(MessageFormat.format(srb.getString("mensajes.contenido.cuerpo2"), c.getProducto().getServicio().getNombre()));
		if (c.getUsuarioTecnicoAsignado() != null) 
		{
			body.append(MessageFormat.format(srb.getString("mensajes.contenido.cuerpo.tecnico"), c.getUsuarioTecnicoAsignado().getDatosUsuario().getFullName()));
		}
		if (c.getTipo().equals("P") && c.getPresupuesto().equals("SI")) 
		{
			construirProductosPresupuesto(c, body);
		} 
		else 
		{
			construirProductos(c, body);
		}
		body.append(srb.getString("mensajes.contenido.br"));
		if (c.getTipo().equals("R")) 
		{
			body.append( srb.getString( "mensajes.contenido.reservas" ) );
			body.append( srb.getString( "mensajes.contenido.reservas.cabeceras" ) );
			for (final Reservas r : reservableService.getReservasPorConsumo(c)) 
			{
				body.append(MessageFormat.format(srb.getString("mensajes.contenido.cuerpo.reserva"), UtilDate.convertirDateToString(r.getFechaInicio(), false),	
							MessageFormat.format(srb.getString("mensajes.contenido.reservas.de.a"), UtilDate.convertirDateHoraToString(r.getFechaInicio(), true), UtilDate.convertirDateHoraToString(r.getFechaFin(), true)),
							r.getFechaInicioTecnico() != null ? MessageFormat.format(srb.getString("mensajes.contenido.reservas.de.a"), UtilDate.convertirDateHoraToString(r.getFechaInicioTecnico(), true), UtilDate.convertirDateHoraToString(r.getFechaFinTecnico(), true)) : "-",
						    r.getReservable().getDescripcion() ) );
			}
			body.append(srb.getString("mensajes.contenido.final.tabla"));
		}
		if ((c.getEstado().equals("Finalizado") || c.getEstado().equals("Rechazado")) && (c.getComentarioResolucion() != null)) 
		{
			body.append(MessageFormat.format(srb.getString("mensajes.contenido.cuerpo.comentarios.resolucion"), c.getComentarioResolucion()));
		}
		return body.toString();
	}

	private void construirProductos( Consumo c, StringBuilder body ) {
		body.append( srb.getString( "mensajes.contenido.productos" ) );
		for ( final Consumo ch : consumoService.getConsumosHijos( c ) ) {
			if ( tarifaService.obtenerPrecioConsumo( c, c.getEntidadPagadora() ) != null ) {
				body.append( SANGRIA ).append( MessageFormat.format( srb.getString( KEY_PRODUCTO ),
						ch.getProducto().getDescripcion(), ch.getCantidad() ) );
			}
		}
		body.append( SANGRIA ).append( MessageFormat.format( srb.getString( KEY_PRODUCTO ),
				c.getProducto().getDescripcion() + " (PRINCIPAL)", c.getCantidad() ) );
		body.append( srb.getString( "mensajes.contenido.final.tabla" ) );
	}

	private void construirProductosPresupuesto( Consumo c, StringBuilder body ) {
		body.append( srb.getString( "mensajes.contenido.productos" ) );
		for ( final Consumo ch : consumoService.getConsumosHijos( c ) ) {
			if (tarifaService.obtenerPrecioConsumo( c, c.getEntidadPagadora() )!=null) {
				body.append( SANGRIA ).append( MessageFormat.format( srb.getString( KEY_PRODUCTO_PRESUPUESTO ),
						ch.getProducto().getDescripcion(), ch.getCantidad(), Utilidades.formatCantidadDosDecimales(
								tarifaService.obtenerPrecioConsumo( ch, ch.getEntidadPagadora() ) ) ) );
			}
		}
		body.append( SANGRIA )
		.append( MessageFormat.format( srb.getString( KEY_PRODUCTO_PRESUPUESTO ),
				c.getProducto().getDescripcion() + " (PRINCIPAL)", c.getCantidad(),
				Utilidades.formatCantidadDosDecimales(
						tarifaService.obtenerPrecioConsumo( c, c.getEntidadPagadora() ) ) ) );
		body.append( MessageFormat.format( srb.getString( "mensajes.contenido.presupuesto.final" ), Utilidades
				.formatCantidadDosDecimales(
						tarifaService.obtenerPrecioConsumoConHijos( c, c.getEntidadPagadora() ) ) ) );
	}
}
