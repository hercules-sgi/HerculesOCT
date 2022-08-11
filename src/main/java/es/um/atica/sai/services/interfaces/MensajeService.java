package es.um.atica.sai.services.interfaces;

import es.um.atica.sai.dtos.EntidadRespuesta;
import es.um.atica.sai.entities.Consumo;
import es.um.atica.sai.entities.Reservas;
import es.um.atica.sai.entities.Usuario;

public interface MensajeService {

	EntidadRespuesta altaUsuario(Usuario usuario, String password);
	EntidadRespuesta renovacionUsuario(Usuario usuario);
	EntidadRespuesta registroIr(Usuario usuario, String emailServicio);
	EntidadRespuesta registroNuevoIrExterno(Usuario usuario, String password, String emailServicio);
	boolean solicitaNuevoPassword(String email, String password);

	void notificacionesNuevaSolicitud(Consumo consumo, String enviarEmailUsuario);
	void notificacionesModificacionSolicitud(Consumo consumo, boolean modificadaListaReservas, boolean enviarMensajeAceptacion, boolean nuevoTecnicoEstablecido, Long codTecnicoAnteriorModificado);
	void notificacionesValidacionIrSolicitud(Consumo consumo);
	void notificacionesAceptacionSolicitud(Consumo consumo, boolean nuevoTecnicoEstablecido, Long codTecnicoAnteriorModificado);
	void notificacionesRechazoSolicitud(Consumo consumo);
	void notificacionesFinalizacionSolicitud(Consumo consumo);
	void avisosEliminacionReserva(Reservas reserva);

	/**
	 * Envía un comentario al usuario que se haya hecho sobre una solicitud
	 *
	 * @param c
	 *          {@link Consumo}
	 * @return boolean
	 */
	boolean enviarUsuarioComentarioSolicitud( Consumo c );

	/**
	 * Envía un correo al tecnico al que se le ha asignado la solicitud con los comentarios del usuario
	 *
	 * @param c
	 *          {@link Consumo}
	 * @return boolean
	 */
	boolean enviarTecnicoOSupervisorComentarioSolicitud( Consumo c );

	/**
	 * Envía la finalización de un presupuesto al solicitante y al IR
	 *
	 * @param c
	 *          {@link Consumo}
	 * @return boolean
	 */
	boolean enviarIrMasSolicitantePresupuestoFinalizado( Consumo c );

	/**
	 * Envía el rechazo de un presupuesto al técnico y a los supervisores del servicio
	 *
	 * @param c
	 *                   {@link Consumo}
	 * @param comentario
	 *                   {@link String}
	 * @return boolean
	 */
	boolean enviarTecnicoYSupervisoresRechazoPresupuesto( Consumo c, String comentario );

	/**
	 * Envia un email al técnico con la reapertura de un presupuesto
	 *
	 * @param c
	 *                   {@link Consumo}
	 * @param comentario
	 *                   {@link String}
	 * @return boolean
	 */
	boolean enviarTecnicoReabrirPresupuesto( Consumo c, String comentario );

	/**
	 * Envía un email al técnico con la aceptación de una presupuesto
	 *
	 * @param c
	 *          {@link Consumo}
	 * @return boolean
	 */
	boolean enviarTecnicoAceptarPresupuesto( Consumo c );



}
