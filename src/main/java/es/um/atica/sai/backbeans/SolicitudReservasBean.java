package es.um.atica.sai.backbeans;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import es.um.atica.commons.utils.UtilDate;
import es.um.atica.sai.dtos.EstadoConsumo;
import es.um.atica.sai.dtos.EstadoPresupuesto;
import es.um.atica.sai.dtos.EstadoReserva;
import es.um.atica.sai.dtos.OcupacionTecnico;
import es.um.atica.sai.dtos.OpcionSolicitudTecnico;
import es.um.atica.sai.dtos.OperacionConsumo;
import es.um.atica.sai.dtos.TipoPerfil;
import es.um.atica.sai.dtos.TipohorarioFecha;
import es.um.atica.sai.dtos.Turno;
import es.um.atica.sai.entities.Anexo;
import es.um.atica.sai.entities.Consumo;
import es.um.atica.sai.entities.EntidadPagadora;
import es.um.atica.sai.entities.Equipo;
import es.um.atica.sai.entities.GrupoInvestigacion;
import es.um.atica.sai.entities.HorarioDia;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.Proyecto;
import es.um.atica.sai.entities.ReservaEspera;
import es.um.atica.sai.entities.Reservas;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.TipoCertificacion;
import es.um.atica.sai.entities.TipoHorario;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.ConsumoService;
import es.um.atica.sai.services.interfaces.KronService;
import es.um.atica.sai.services.interfaces.MensajeService;
import es.um.atica.sai.services.interfaces.ProductoService;
import es.um.atica.sai.services.interfaces.ProyectoService;
import es.um.atica.sai.services.interfaces.ReservableService;
import es.um.atica.sai.services.interfaces.ServicioService;
import es.um.atica.sai.services.interfaces.TarifaService;
import es.um.atica.sai.services.interfaces.UsuarioService;
import es.um.atica.sai.utils.Utilidades;
import es.um.atica.seam.components.MenuManagerBean;

@Name("solicitudReservas")
@Scope( ScopeType.CONVERSATION )
public class SolicitudReservasBean {

	@Logger
	private Log log;

	@In( create = true, required = true )
	ServicioService servicioService;

	@In( create = true, required = true )
	ProductoService productoService;
	
	@In( create = true, required = true )
	ProyectoService proyectoService;

	@In( create = true, required = true )
	UsuarioService usuarioService;

	@In( create = true, required = true )
	TarifaService tarifaService;

	@In( create = true, required = true )
	ConsumoService consumoService;

	@In( create = true, required = true )
	ReservableService reservableService;

	@In( create = true, required = true )
	MensajeService mensajeService;

	@In( create = true, required = true )
	KronService kronService;

	@In(create = true)
	SolicitudPrestacionesBean solicitudPrestaciones;
	
	@In( value = "org.jboss.seam.security.identity", required = true )
	SaiIdentity identity;

	@In(create=true)
	protected FacesMessages facesMessages;

	@In(create=true)
	MenuManagerBean menuManagerBean;
	
	private static final String SALTO_LINEA = "</br>";
	private static final String ABRE_NEGRITA = "<b>";
	private static final String CIERRA_NEGRITA = "</b>";
	private static final String ABRE_CORCHETE = "[";
	private static final String CIERRA_CORCHETE = "]";
	private static final String SI = "SI";
	private static final String NO = "NO";
	private static final String VOLVER_LISTADO = "misSolicitudes";
	private static final String EDIT_RESERVA = "editReserva";
	private static final String FORMATO_FECHA = "dd/MM/yyyy";
	private static final String FORMATO_HORA = "HH:mm";
	private static final String FORMATO_FECHAHORA = "dd/MM/yyyy HH:mm";
	private static final String KEY_MSJ_ADDLINEA_ERROR = "reserva.addlinea.error";
	private static final String KEY_MSJ_PRODUCTO_SELECCION_ERROR = "reservas.producto.seleccion.error";
	private static final String KEY_MSJ_COMENTARIO_ADD_ERROR = "reservas.comentario.add.error";
	private static final String KEY_MSJ_RESERVAS_GUARDAR_OK_DETALLES = "reservas.guardar.ok.detalles";
	private static final String KEY_MSJ_RESERVAS_CREAR_ERROR = "reservas.crear.error";
	private static final String KEY_MSJ_RESERVAS_MODIFICAR_ERROR = "reservas.modificar.error";
	private static final String KEY_MSJ_RESERVAS_GUARDAR_ERROR_REQUIEREANEXOBIOSEG = "reservas.guardar.error.requiereanexo.bioseguridad";

	private Consumo reservaEdit;
	private List<Servicio> listaServicios;
	private Servicio servicioReserva;
	private List<Usuario> listaUsuariosSolicitantes;
	private List<Usuario> listaUsuariosIr;
	private List<Usuario> listaUsuariosTecnicos;
	private List<EntidadPagadora> listaEntidades;
	private List<GrupoInvestigacion> listaGruposInvestigacion;
	private List<Producto> listaProductos;
	private List<Proyecto> listaProyectos;
	private List<OpcionSolicitudTecnico> listaOpcionesSolicitudTecnico;
	private List<Equipo> listaReservables;
	private UploadedFile ficheroAnexoReserva;
	private UploadedFile ficheroAnexoBioseguridad;
	private Anexo anexoBioseguridadRellenar;
	private Anexo anexoBioseguridadCompletado;
	private String comentarioAdd;
	private List<Reservas> listaReservas;
	private List<Reservas> listaReservasEliminadas;
	private Equipo reservableReservaEspecialAdd;
	private Date fechaDesdeReservaEspecialAdd;
	private Date fechaHastaReservaEspecialAdd;
	private String horaInicioReservaEspecialAdd;
	private String horaFinReservaEspecialAdd;
	private List<String> listaHorasInicio;
	private List<String> listaHorasFin;
	private List<Date> listaFechasFinReservaOrdinariaAdd;
	private Date fechaInicioReservaOrdinariaAdd;
	private String horaFinReservaOrdinariaAdd;
	private Equipo reservableReservaOrdinariaAdd;
	private List<Equipo> listaReservablesReservaOrdinariaAdd;
	private Date lunesCalendario;
	private Date domingoCalendario;
	private Date fechaCalendario;
	private List<TipohorarioFecha> listaTiposhorarioFechaCalendario;
	private boolean mostrarBotonAddReservas;
	private Date fechaHoy;
	private boolean accesoDesdeListado;
	private String enviaEmail;
	private boolean tecnicoPendiente;
	private Long codUsuarioTecnicoAsignado;
	private String estadoSolicitudTecnico;
	private Integer tabActivo;
	private List<Integer> listaDiasSemana = Arrays.asList(2, 3, 4, 5, 6, 7, 1);
	private int[] diasSeleccionadosReservaEspecialAdd;
	
	public Consumo getReservaEdit() {
		return reservaEdit;
	}

	public void setReservaEdit(Consumo reservaEdit ) {
		this.reservaEdit = reservaEdit;
	}	
	
	public List<Servicio> getListaServicios() {
		return listaServicios;
	}

	public void setListaServicios( List<Servicio> listaServicios ) {
		this.listaServicios = listaServicios;
	}

	public Servicio getServicioReserva() {
		return servicioReserva;
	}

	public void setServicioReserva( Servicio servicioReserva ) {
		this.servicioReserva = servicioReserva;
	}

	public List<Usuario> getListaUsuariosSolicitantes() {
		return listaUsuariosSolicitantes;
	}
	
	public void setListaUsuariosSolicitantes( List<Usuario> listaUsuariosSolicitantes ) {
		this.listaUsuariosSolicitantes = listaUsuariosSolicitantes;
	}
	
	public List<Usuario> getListaUsuariosIr() {
		return listaUsuariosIr;
	}
	
	public void setListaUsuariosIr( List<Usuario> listaUsuariosIr ) {
		this.listaUsuariosIr = listaUsuariosIr;
	}
	
	public List<Usuario> getListaUsuariosTecnicos() {
		return listaUsuariosTecnicos;
	}
	
	public void setListaUsuariosTecnicos( List<Usuario> listaUsuariosTecnicos ) {
		this.listaUsuariosTecnicos = listaUsuariosTecnicos;
	}

	public List<EntidadPagadora> getListaEntidades() {
		return listaEntidades;
	}
	
	public void setListaEntidades( List<EntidadPagadora> listaEntidades ) {
		this.listaEntidades = listaEntidades;
	}

	public List<GrupoInvestigacion> getListaGruposInvestigacion() {
		return listaGruposInvestigacion;
	}
	
	public void setListaGruposInvestigacion( List<GrupoInvestigacion> listaGruposInvestigacion ) {
		this.listaGruposInvestigacion = listaGruposInvestigacion;
	}

	public List<Producto> getListaProductos() {
		return listaProductos;
	}
	
	public void setListaProductos( List<Producto> listaProductos ) {
		this.listaProductos = listaProductos;
	}

	public List<Proyecto> getListaProyectos() {
		return listaProyectos;
	}
	
	public void setListaProyectos( List<Proyecto> listaProyectos ) {
		this.listaProyectos = listaProyectos;
	}

	public List<OpcionSolicitudTecnico> getListaOpcionesSolicitudTecnico() {
		return listaOpcionesSolicitudTecnico;
	}
	
	public void setListaOpcionesSolicitudTecnico( List<OpcionSolicitudTecnico> listaOpcionesSolicitudTecnico ) {
		this.listaOpcionesSolicitudTecnico = listaOpcionesSolicitudTecnico;
	}

	public List<Equipo> getListaReservables() {
		return listaReservables;
	}
	
	public void setListaReservables( List<Equipo> listaReservables ) {
		this.listaReservables = listaReservables;
	}

	public UploadedFile getFicheroAnexoReserva() {
		return ficheroAnexoReserva;
	}
	
	public void setFicheroAnexoReserva( UploadedFile ficheroAnexoReserva ) {
		this.ficheroAnexoReserva = ficheroAnexoReserva;
	}

	public UploadedFile getFicheroAnexoBioseguridad() {
		return ficheroAnexoBioseguridad;
	}
	
	public void setFicheroAnexoBioseguridad( UploadedFile ficheroAnexoBioseguridad ) {
		this.ficheroAnexoBioseguridad = ficheroAnexoBioseguridad;
	}

	public Anexo getAnexoBioseguridadRellenar() {
		return anexoBioseguridadRellenar;
	}
	
	public void setAnexoBioseguridadRellenar( Anexo anexoBioseguridadRellenar ) {
		this.anexoBioseguridadRellenar = anexoBioseguridadRellenar;
	}

	public Anexo getAnexoBioseguridadCompletado() {
		return anexoBioseguridadCompletado;
	}
	
	public void setAnexoBioseguridadCompletado( Anexo anexoBioseguridadCompletado ) {
		this.anexoBioseguridadCompletado = anexoBioseguridadCompletado;
	}

	public String getComentarioAdd() {
		return comentarioAdd;
	}
	
	public void setComentarioAdd( String comentarioAdd ) {
		this.comentarioAdd = comentarioAdd;
	}

	public List<Reservas> getListaReservas() {
		return listaReservas;
	}
	
	public void setListaReservas( List<Reservas> listaReservas ) {
		this.listaReservas = listaReservas;
	}

	public List<Reservas> getListaReservasEliminadas() {
		return listaReservasEliminadas;
	}
	
	public void setListaReservasEliminadas( List<Reservas> listaReservasEliminadas ) {
		this.listaReservasEliminadas = listaReservasEliminadas;
	}

	public Equipo getReservableReservaEspecialAdd() {
		return reservableReservaEspecialAdd;
	}
	
	public void setReservableReservaEspecialAdd( Equipo reservableReservaEspecialAdd ) {
		this.reservableReservaEspecialAdd = reservableReservaEspecialAdd;
	}
	
	public Date getFechaDesdeReservaEspecialAdd() {
		return fechaDesdeReservaEspecialAdd;
	}
	
	public void setFechaDesdeReservaEspecialAdd( Date fechaDesdeReservaEspecialAdd ) {
		this.fechaDesdeReservaEspecialAdd = fechaDesdeReservaEspecialAdd;
	}
	
	public Date getFechaHastaReservaEspecialAdd() {
		return fechaHastaReservaEspecialAdd;
	}
	
	public void setFechaHastaReservaEspecialAdd( Date fechaHastaReservaEspecialAdd ) {
		this.fechaHastaReservaEspecialAdd = fechaHastaReservaEspecialAdd;
	}

	public String getHoraInicioReservaEspecialAdd() {
		return horaInicioReservaEspecialAdd;
	}
	
	public void setHoraInicioReservaEspecialAdd( String horaInicioReservaEspecialAdd ) {
		this.horaInicioReservaEspecialAdd = horaInicioReservaEspecialAdd;
	}
	
	public String getHoraFinReservaEspecialAdd() {
		return horaFinReservaEspecialAdd;
	}
	
	public void setHoraFinReservaEspecialAdd( String horaFinReservaEspecialAdd ) {
		this.horaFinReservaEspecialAdd = horaFinReservaEspecialAdd;
	}

	public List<String> getListaHorasInicio() {
		return listaHorasInicio;
	}
	
	public void setListaHorasInicio( List<String> listaHorasInicio ) {
		this.listaHorasInicio = listaHorasInicio;
	}
	
	public List<String> getListaHorasFin() {
		return listaHorasFin;
	}
	
	public void setListaHorasFin( List<String> listaHorasFin ) {
		this.listaHorasFin = listaHorasFin;
	}

	public List<Date> getListaFechasFinReservaOrdinariaAdd() {
		return listaFechasFinReservaOrdinariaAdd;
	}
	
	public void setListaFechasFinReservaOrdinariaAdd( List<Date> listaFechasFinReservaOrdinariaAdd ) {
		this.listaFechasFinReservaOrdinariaAdd = listaFechasFinReservaOrdinariaAdd;
	}
	
	public Date getFechaInicioReservaOrdinariaAdd() {
		return fechaInicioReservaOrdinariaAdd;
	}

	public void setFechaInicioReservaOrdinariaAdd(Date  fechaInicioReservaOrdinariaAdd ) {
		this.fechaInicioReservaOrdinariaAdd = fechaInicioReservaOrdinariaAdd;
	}

	public String getHoraFinReservaOrdinariaAdd() {
		return horaFinReservaOrdinariaAdd;
	}
	
	public void setHoraFinReservaOrdinariaAdd( String horaFinReservaOrdinariaAdd ) {
		this.horaFinReservaOrdinariaAdd = horaFinReservaOrdinariaAdd;
	}
	
	public Equipo getReservableReservaOrdinariaAdd() {
		return reservableReservaOrdinariaAdd;
	}
	
	public void setReservableReservaOrdinariaAdd( Equipo reservableReservaOrdinariaAdd ) {
		this.reservableReservaOrdinariaAdd = reservableReservaOrdinariaAdd;
	}

	public List<Equipo> getListaReservablesReservaOrdinariaAdd() {
		return listaReservablesReservaOrdinariaAdd;
	}
	
	public void setListaReservablesReservaOrdinariaAdd( List<Equipo> listaReservablesReservaOrdinariaAdd ) {
		this.listaReservablesReservaOrdinariaAdd = listaReservablesReservaOrdinariaAdd;
	}

	public Date getLunesCalendario() {
		return lunesCalendario;
	}
	
	public void setLunesCalendario( Date lunesCalendario ) {
		this.lunesCalendario = lunesCalendario;
	}
	
	public Date getDomingoCalendario() {
		return domingoCalendario;
	}
	
	public void setDomingoCalendario( Date domingoCalendario ) {
		this.domingoCalendario = domingoCalendario;
	}

	public Date getFechaCalendario() {
		return fechaCalendario;
	}
	
	public void setFechaCalendario( Date fechaCalendario ) {
		this.fechaCalendario = fechaCalendario;
	}

	public List<TipohorarioFecha> getListaTiposhorarioFechaCalendario() {
		return listaTiposhorarioFechaCalendario;
	}
	
	public void setListaTiposhorarioFechaCalendario( List<TipohorarioFecha> listaTiposhorarioFechaCalendario ) {
		this.listaTiposhorarioFechaCalendario = listaTiposhorarioFechaCalendario;
	}

	public boolean isMostrarBotonAddReservas() {
		return mostrarBotonAddReservas;
	}
	
	public void setMostrarBotonAddReservas( boolean mostrarBotonAddReservas ) {
		this.mostrarBotonAddReservas = mostrarBotonAddReservas;
	}
	
	public Date getFechaHoy() {
		return fechaHoy;
	}
	
	public void setFechaHoy( Date fechaHoy ) {
		this.fechaHoy = fechaHoy;
	}
	
	public boolean isAccesoDesdeListado() {
		return accesoDesdeListado;
	}
	
	public void setAccesoDesdeListado( boolean accesoDesdeListado ) {
		this.accesoDesdeListado = accesoDesdeListado;
	}
	
	public boolean isTecnicoPendiente() {
		return tecnicoPendiente;
	}
	
	public void setTecnicoPendiente( boolean tecnicoPendiente ) {
		this.tecnicoPendiente = tecnicoPendiente;
	}

	public String getEnviaEmail() {
		return enviaEmail;
	}

	public void setEnviaEmail( String enviaEmail ) {
		this.enviaEmail = enviaEmail;
	}

	public Integer getTabActivo() {
		return tabActivo;
	}

	public void setTabActivo( Integer tabActivo ) {
		this.tabActivo = tabActivo;
	}

	public List<Integer> getListaDiasSemana() {
		return listaDiasSemana;
	}
	
	public void setListaDiasSemana( List<Integer> listaDiasSemana ) {
		this.listaDiasSemana = listaDiasSemana;
	}

	public int[] getDiasSeleccionadosReservaEspecialAdd() {
		return diasSeleccionadosReservaEspecialAdd;
	}
	
	public void setDiasSeleccionadosReservaEspecialAdd( int[] diasSeleccionadosReservaEspecialAdd ) {
		this.diasSeleccionadosReservaEspecialAdd = diasSeleccionadosReservaEspecialAdd;
	}


	@Create
	public void inicializar() 
	{
		this.listaServicios = servicioService.getListaServiciosPuedoHacerSolicitud();
		this.servicioReserva = null;
		this.reservaEdit = new Consumo();
		this.reservaEdit.setTipo("R");
		this.reservaEdit.setPresupuesto(NO);
		this.enviaEmail = NO;
		this.listaReservas = new ArrayList<>();
		this.listaReservasEliminadas = new ArrayList<>();
		this.fechaHoy = new Date();
		this.fechaCalendario = new Date();
		this.comentarioAdd = null;
		this.accesoDesdeListado = false;
		if (this.listaServicios.size()==1)
		{
			this.servicioReserva = this.listaServicios.get(0);
			this.seleccionadoServicio();
		}
	}

	private void inicializacionReservaEdit(Consumo consumo)
	{
		this.reservaEdit = consumo;
		this.ficheroAnexoBioseguridad = null;
		this.anexoBioseguridadRellenar = null;
		this.anexoBioseguridadCompletado = null;
		if (consumoService.requiereTecnico(consumo))
		{
			this.listaUsuariosTecnicos = usuarioService.getTecnicosByProducto( this.reservaEdit.getProducto());
			if (this.reservaEdit.getUsuarioTecnicoAsignado() == null)
			{
				this.tecnicoPendiente = true;
			}
			else
			{
				this.tecnicoPendiente = false;
				this.codUsuarioTecnicoAsignado = this.reservaEdit.getUsuarioTecnicoAsignado().getCod();
			}
			this.estadoSolicitudTecnico = this.reservaEdit.getSolicitudTecnico();
		}
		else
		{
			this.tecnicoPendiente = false;
		}
		// Opciones de solicitud de técnico si el producto requiere que se pregunte al usuario
		this.listaOpcionesSolicitudTecnico = consumoService.getListaOpcionesSolicitudTecnico(this.reservaEdit);
		this.servicioReserva = reservaEdit.getProducto().getServicio();
		this.listaReservas = reservableService.getReservasPorConsumo(this.reservaEdit);
		this.listaReservasEliminadas = new ArrayList<>();
		if (consumoService.requiereAnexoNivelBioseguridad(this.reservaEdit))
		{
			this.anexoBioseguridadRellenar = consumoService.getAnexoGeneralBioseguridad();
			this.anexoBioseguridadCompletado = consumoService.getAnexoBioseguridadByConsumo(this.reservaEdit);
		}
	}
	
	
	public String establecerReservaEdit(Consumo consumo)
	{
		this.inicializacionReservaEdit(consumo);
		this.accesoDesdeListado = true;
		return EDIT_RESERVA;
	}
	
	public String establecerReservaEditFromConsumoPadre(Consumo consumo)
	{
		consumoService.recargarConsumo(consumo);
		this.inicializacionReservaEdit(consumo);
		this.accesoDesdeListado = false;
		return EDIT_RESERVA;
	}
	
	public String establecerReservaCreateFromConsumoPadre(Consumo consumoPadre)
	{
		this.inicializar();
		this.servicioReserva = consumoPadre.getProducto().getServicio();
		this.reservaEdit.setConsumoPadre(consumoPadre);
		this.reservaEdit.setPresupuesto(consumoPadre.getPresupuesto());
		this.seleccionadoServicio();
		return EDIT_RESERVA;
	}
	
	public void seleccionadoServicio() 
	{
		if (this.servicioReserva != null)
		{
			if (identity.tienePerfilEnServicio(TipoPerfil.TECNICO, this.servicioReserva.getCod()) || identity.tienePerfilEnServicio( TipoPerfil.SUPERVISOR, this.servicioReserva.getCod() ))
			{
				this.listaUsuariosSolicitantes=usuarioService.getUsuariosPuedoSolicitarEnSuNombreByServicio(this.servicioReserva);
				this.reservaEdit.setInterno(SI);
			}
			else
			{
				this.reservaEdit.setInterno(NO);
				this.listaUsuariosIr = usuarioService.getListaIrsPuedoAsignarEnSolicitud( identity.getUsuarioConectado(), this.servicioReserva);
				if (this.listaUsuariosIr.size() == 1)
				{
					this.reservaEdit.setUsuarioIrAsociado(this.listaUsuariosIr.get(0));
				}
			}
			boolean soloObtenerProductosNoRequierenProyecto = "SI".equals(this.reservaEdit.getInterno());
			this.listaProductos = productoService.getProductosByServicioTipo(this.servicioReserva, "R", soloObtenerProductosNoRequierenProyecto, false);
			//Si el servicio no tiene productos reservables, notificamos al usuario
			if (this.listaProductos == null || this.listaProductos.isEmpty()) 
			{
				facesMessages.add(StatusMessage.Severity.WARN, "reservas.servicio.seleccion.error", "reservas.servicio.seleccion.error.sinproductos", null, null, this.servicioReserva.getNombre());
			}
		}
		else
		{
			this.reservaEdit.setInterno(SI);
		}
		this.reservaEdit.setUsuarioSolicitante(identity.getUsuarioConectado());
		if (this.listaUsuariosIr==null || (this.reservaEdit.getUsuarioIrAsociado()!=null && !this.listaUsuariosIr.contains(this.reservaEdit.getUsuarioIrAsociado())))
		{
			this.reservaEdit.setUsuarioIrAsociado(null);
			this.seleccionadoUsuarioIr();
		}
		this.reservaEdit.setEntidadPagadora(null);
		if (this.reservaEdit.getUsuarioIrAsociado()!=null)
		{
			this.seleccionadoUsuarioIr();
		}
		this.reservaEdit.setProducto(null);
		this.reservaEdit.setSolicitudTecnico(null);
		this.listaReservas = new ArrayList<>();
	}
	

	public void seleccionadoConsumoInterno() 
	{
		if (NO.equals(this.reservaEdit.getInterno())) 
		{
			if (this.servicioReserva != null) 
			{
				this.listaUsuariosSolicitantes = usuarioService.getUsuariosByServicio(this.servicioReserva);
				if (this.reservaEdit.getUsuarioSolicitante() != null && this.listaUsuariosSolicitantes.contains(this.reservaEdit.getUsuarioSolicitante()))
				{
					this.seleccionadoUsuarioSolicitante();
				}
			}
			this.listaProductos = productoService.getProductosByServicioTipo(this.servicioReserva, "R", false, false);
		}
		else 
		{
			this.reservaEdit.setUsuarioSolicitante(identity.getUsuarioConectado());
			this.reservaEdit.setUsuarioIrAsociado(null);
			this.reservaEdit.setEntidadPagadora(null);
			if (this.reservaEdit.getProducto() != null)
			{
				// Si ya había producto seleccionado y cambiamos el solicitante tenemos que comprobar si tiene la certificación necesaria
				this.seleccionadoUsuarioSolicitante();
			}
			this.listaProductos = productoService.getProductosByServicioTipo(this.servicioReserva, "R", true, false);
		}
	}

	
	public void seleccionadoUsuarioSolicitante() 
	{
		if (this.reservaEdit.getUsuarioSolicitante()!= null)  
		{
			this.listaUsuariosIr = usuarioService.getListaIrsPuedoAsignarEnSolicitud(this.reservaEdit.getUsuarioSolicitante(),this.servicioReserva);
			if (this.listaUsuariosIr.size() == 1)
			{
				this.reservaEdit.setUsuarioIrAsociado(this.listaUsuariosIr.get(0));
			}
			if (this.reservaEdit.getProducto() != null)
			{
				// Si ya había producto seleccionado y cambiamos el solicitante tenemos que comprobar si tiene la certificación necesaria
				this.seleccionadoProducto();
			}
		}
		if (this.listaUsuariosIr==null || (this.reservaEdit.getUsuarioIrAsociado()!=null && !this.listaUsuariosIr.contains(this.reservaEdit.getUsuarioIrAsociado())))
		{
			this.reservaEdit.setUsuarioIrAsociado(null);
			this.seleccionadoUsuarioIr();
		}
		this.reservaEdit.setEntidadPagadora(null);
		if (this.reservaEdit.getUsuarioIrAsociado()!=null)
		{
			this.seleccionadoUsuarioIr();
		}

	}
	
	public void seleccionadoUsuarioIr() 
	{
		if (this.reservaEdit.getUsuarioIrAsociado() != null) 
		{
			this.listaEntidades = tarifaService.getEntidadesByIr(this.reservaEdit.getUsuarioIrAsociado());
			if (this.listaEntidades.size()==1)
			{
				this.reservaEdit.setEntidadPagadora(this.listaEntidades.get(0));
				this.seleccionadaEntidadPagadora();
			}
			if (this.reservaEdit.getProducto() != null)
			{
				this.seleccionadoProducto();
			}
		}
		if (this.listaEntidades==null || (this.reservaEdit.getEntidadPagadora() != null && !this.listaEntidades.contains(this.reservaEdit.getEntidadPagadora())))
		{
			this.reservaEdit.setEntidadPagadora(null);
			this.seleccionadaEntidadPagadora();
		}
	}
	
	public void seleccionadaEntidadPagadora()
	{
		if (this.reservaEdit.getEntidadPagadora() != null)
		{
			this.listaGruposInvestigacion = usuarioService.getGruposInvestigacionByUsuarioEntidadPagadora(this.reservaEdit.getUsuarioIrAsociado(), this.reservaEdit.getEntidadPagadora());
			if (this.listaGruposInvestigacion.size()==1)
			{
				this.reservaEdit.setGrupoInvestigacion(this.listaGruposInvestigacion.get(0));
			}
		}
		else
		{
			this.listaGruposInvestigacion = new ArrayList<>();
			this.reservaEdit.setGrupoInvestigacion(null);
		}
	}

	public void seleccionadoProducto() 
	{
		this.listaReservas = new ArrayList<>();
		this.reservaEdit.setSolicitudTecnico(null);
		this.reservaEdit.setNivelBioseguridad(null);
		this.ficheroAnexoReserva = null;
		this.ficheroAnexoBioseguridad = null;
		this.reservaEdit.setObservaciones(null);
		if (this.reservaEdit.getProducto() != null)
		{
			if (this.reservaEdit.getProducto().getTipoReservable() == null )
			{
				facesMessages.add(StatusMessage.Severity.WARN, KEY_MSJ_PRODUCTO_SELECCION_ERROR, "reservas.producto.seleccion.error.sintiporeservable", null, null, this.reservaEdit.getProducto().getDescripcion());
				this.reservaEdit.setProducto(null);
				return;
			}
			// Comprobamos si requiere proyecto
			if (SI.equals(this.reservaEdit.getProducto().getRequiereProyecto()))
			{
				if (this.reservaEdit.getUsuarioIrAsociado() != null)
				{
					this.listaProyectos = proyectoService.getListaProyectosByProductoIr(this.reservaEdit.getProducto(), this.reservaEdit.getUsuarioIrAsociado());
					if (this.listaProyectos.isEmpty())
					{
						facesMessages.add(StatusMessage.Severity.WARN, KEY_MSJ_PRODUCTO_SELECCION_ERROR, "reservas.producto.seleccion.error.sinproyecto", null, null, this.reservaEdit.getProducto().getDescripcion(), this.reservaEdit.getUsuarioIrAsociado().getDatosUsuario().getNombreCompleto());
						this.reservaEdit.setProducto(null);
						return;
					}
					else if (this.listaProyectos.size() == 1)
					{
						this.reservaEdit.setProyecto(this.listaProyectos.get(0));
					}
				}
				else
				{
					facesMessages.add(StatusMessage.Severity.WARN, KEY_MSJ_PRODUCTO_SELECCION_ERROR, "reservas.producto.seleccion.error.sinproyecto.sinir", null, null, this.reservaEdit.getProducto().getDescripcion());
					this.reservaEdit.setProducto(null);
					return;
				}
			}
			else
			{
				this.reservaEdit.setProyecto(null);
			}
			// Comprobamos si requiere alguna certificación de la que no dispone
			Usuario solicitante = consumoService.obtenerSolicitante(this.reservaEdit);
			TipoCertificacion tc = consumoService.obtenerCertificacionNecesaria(solicitante, this.reservaEdit.getProducto());
			if (tc != null)
			{
				facesMessages.add(StatusMessage.Severity.WARN, KEY_MSJ_PRODUCTO_SELECCION_ERROR, "reservas.producto.seleccion.error.certificacion", null, null, tc.getNombre(), solicitante.getDatosUsuario().getNombreCompleto());
				this.reservaEdit.setProducto(null);
				return;
			}
			this.listaReservables = reservableService.getListaReservablesByProducto(this.reservaEdit.getProducto());
			if (this.listaReservables.isEmpty())
			{
				facesMessages.add(StatusMessage.Severity.WARN, KEY_MSJ_PRODUCTO_SELECCION_ERROR, "reservas.producto.seleccion.error.tiporeservable.sinreservables", null, null, this.reservaEdit.getProducto().getDescripcion());
				this.reservaEdit.setProducto(null);
				return;
			}
			// Comprobamos si existen horarios definidos
			if (this.reservaEdit.getProducto().getTipoReservable().getReservableHorarios() == null || this.reservaEdit.getProducto().getTipoReservable().getReservableHorarios().isEmpty())
			{
				facesMessages.add(StatusMessage.Severity.WARN, KEY_MSJ_PRODUCTO_SELECCION_ERROR, "reservas.producto.seleccion.error.sinhorarios", null, null, this.reservaEdit.getProducto().getDescripcion());
				this.reservaEdit.setProducto(null);
				return;
			}
			// Comprobamos si los horarios definidos tienen los horarios diarios configurados
			if (!reservableService.existeHorarioDia(this.reservaEdit.getProducto().getTipoReservable()))
			{
				facesMessages.add(StatusMessage.Severity.WARN, KEY_MSJ_PRODUCTO_SELECCION_ERROR, "reservas.producto.seleccion.error.sinhorariodias", null, null, this.reservaEdit.getProducto().getDescripcion());
				this.reservaEdit.setProducto(null);
				return;
			}
			// Inicialización de reserva ordinaria
			inicializaDiasReservaOrdinaria();
			// Inicialización de reserva especial
			inicializaHorasReservaEspecial();
			// Opciones de solicitud de técnico si el producto requiere que se pregunte al usuario
			this.listaOpcionesSolicitudTecnico = consumoService.getListaOpcionesSolicitudTecnico(this.reservaEdit);
			this.mostrarBotonAddReservas = false;
		}
	}
	
	public void seleccionadoSolicitudTecnico()
	{
		if ("NO".equals(this.reservaEdit.getSolicitudTecnico()))
		{
			this.reservaEdit.setUsuarioTecnicoAsignado(null);
			for (Reservas reserva: this.listaReservas)
			{				
				reserva.setFechaInicioTecnico(null);
				reserva.setFechaFinTecnico(null);
			}
			return;
		}
		if (this.reservaEdit.getCod() != null)
		{
			// Si se ha pasado de no requerir técnico a sí, puede que tenga que aparecer el combo de técnicos (si es supervisor o técnico)
			this.listaUsuariosTecnicos = usuarioService.getTecnicosByProducto( this.reservaEdit.getProducto());
		}
		// Si hay un tecnico asignado, primero comprobamos si se puede hacer la modificación
		if (this.reservaEdit.getUsuarioTecnicoAsignado() != null)
		{
			for (Reservas reserva: this.listaReservas)
			{				
				OcupacionTecnico ot = consumoService.obtenerOcupacionTecnico(this.reservaEdit, reserva.getFechaInicio(), reserva.getFechaFin());
				if (!reservableService.disponibleTecnicoModificacionReserva(this.reservaEdit.getUsuarioTecnicoAsignado(), reserva.getCod(), ot.getFechaInicioTecnico(), ot.getFechaFinTecnico()))
				{
					// Mostramos mensaje de que no se puede guardar y volvemos a establecer en el combo el valor de solicitudTecnico estaba antes de la modificación 
					facesMessages.add(StatusMessage.Severity.WARN, "reserva.modificarsolicitudtecnico.error", "reserva.guardar.error.tecniconodisponible.detalles", null, null,
							this.reservaEdit.getUsuarioTecnicoAsignado().getDatosUsuario().getNombreCompleto(),	
							UtilDate.dateToString(reserva.getFechaInicioTecnico(), FORMATO_FECHA),
							UtilDate.dateToString(reserva.getFechaInicioTecnico(), FORMATO_HORA),
							UtilDate.dateToString(reserva.getFechaFinTecnico(), FORMATO_HORA));
					this.reservaEdit.setSolicitudTecnico(this.estadoSolicitudTecnico);
					return;	
				}
			}
		}
		// Si seguimos es que se puede modificar
		for (Reservas reserva: this.listaReservas)
		{				
			OcupacionTecnico ot = consumoService.obtenerOcupacionTecnico(this.reservaEdit, reserva.getFechaInicio(), reserva.getFechaFin());
			reserva.setFechaInicioTecnico(ot.getFechaInicioTecnico());
			reserva.setFechaFinTecnico(ot.getFechaFinTecnico());
		}
	}
	
	public void seleccionadoNivelBioseguridad()
	{
		if (this.reservaEdit.getNivelBioseguridad() == null || "NO".equals(this.reservaEdit.getNivelBioseguridad()))
		{
			this.ficheroAnexoBioseguridad = null;
			if (this.reservaEdit.getCod() == null)
			{
				// Si estamos en modo edición no borramos todavía
				this.anexoBioseguridadCompletado = null;
			}
		}
		else
		{
			this.anexoBioseguridadRellenar = consumoService.getAnexoGeneralBioseguridad();
		}
	}

	public void mostrarAddReservas()
	{
		this.mostrarBotonAddReservas = false;
	}
	
	public void subidoFicheroAnexoBioseguridad( FileUploadEvent event ) 
	{
		this.ficheroAnexoBioseguridad = event.getFile();
	}
	
	public void subidoFicheroAnexoReserva( FileUploadEvent event ) 
	{
		this.ficheroAnexoReserva = event.getFile();
	}

	public StreamedContent descargarFicheroAnexo(Anexo anexo) 
	{
		if (anexo.getDocumento() != null) 
		{
			final InputStream stream = new ByteArrayInputStream(anexo.getDocumento());
			return new DefaultStreamedContent(stream, null, anexo.getNomDocumento());
		}
		else 
		{
			facesMessages.add(StatusMessage.Severity.WARN, "prestacion.no.anexo", null, null, anexo.getNomDocumento());
			return null;
		}
	}
	
	public void eliminarAnexoBioseguridadCompletado()
	{
		// Eliminamos el anexo directamente en BBDD
		try 
		{
			consumoService.eliminarAnexo(this.anexoBioseguridadCompletado);
		}
		catch (Exception ex)
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "prestaciones.anexo.del.error", null, null, ex.getMessage());
			return;
		}
		facesMessages.add(StatusMessage.Severity.INFO, "prestaciones.anexo.del.ok", null, null, this.anexoBioseguridadCompletado.getNomDocumento());
		this.anexoBioseguridadCompletado = null;
	}
	
	private void inicializaDiasReservaOrdinaria()
	{
		this.estableceInicioFinCalendarioMostrar(this.fechaCalendario);
		this.listaTiposhorarioFechaCalendario = new ArrayList<>();
		Date fechaIndice = (Date)this.lunesCalendario.clone();
		while(UtilDate.anteriorOrIgual(fechaIndice, this.domingoCalendario))
		{
			TipoHorario th = reservableService.getTipoHorarioByTipoReservableFecha(this.reservaEdit.getProducto().getTipoReservable(), fechaIndice);
			if (th == null)
			{
				// No hay horario definido para la fecha
				this.listaTiposhorarioFechaCalendario.add(new TipohorarioFecha(null, fechaIndice));
			}
			else
			{
				this.listaTiposhorarioFechaCalendario.add(new TipohorarioFecha(th, fechaIndice));
			}
			fechaIndice = UtilDate.sumarDias(fechaIndice, 1);
		}
	}

	private void estableceInicioFinCalendarioMostrar(Date fecha)
	{
		this.fechaCalendario = fecha;
		int diaSemana = UtilDate.getDiaSemana(fecha);
		switch (diaSemana)
		{
			case 1:
				this.lunesCalendario = UtilDate.sumarDias(fecha, -6);
				this.domingoCalendario = (Date)fecha.clone();
				break;
			case 2: 
				this.lunesCalendario = (Date)fecha.clone();
				this.domingoCalendario = UtilDate.sumarDias(fecha, 6);
				break;
			case 3:
				this.lunesCalendario = UtilDate.sumarDias(fecha, -1);
				this.domingoCalendario = UtilDate.sumarDias(fecha, 5);
				break;
			case 4:
				this.lunesCalendario = UtilDate.sumarDias(fecha, -2);
				this.domingoCalendario = UtilDate.sumarDias(fecha, 4);
				break;
			case 5:
				this.lunesCalendario = UtilDate.sumarDias(fecha, -3);
				this.domingoCalendario = UtilDate.sumarDias(fecha, 3);
				break;
			case 6:
				this.lunesCalendario = UtilDate.sumarDias(fecha, -4);
				this.domingoCalendario = UtilDate.sumarDias(fecha, 2);
				break;
			case 7:
				this.lunesCalendario = UtilDate.sumarDias(fecha, -5);
				this.domingoCalendario = UtilDate.sumarDias(fecha, 1);
				break;
			default:
				break;
		}
	}
	
	// Cuando se llama a este método el turno tiene establecidas las fechas de inicio y fin de técnico
	private void estableceOcupacionTurno(Turno turno)
	{
		// Inicializamos el turno como libre
		turno.setOcupado(false);
		// Obtenemos las reservas existentes de todos los reservables en el turno
		List<Reservas> listaReservasExistentes = reservableService.getReservasPorTurnoByTipoReservable(this.reservaEdit.getProducto().getTipoReservable(), turno.getFechaInicio(), turno.getFechaFin());
		if (!listaReservasExistentes.isEmpty() && this.listaReservables.size() <= listaReservasExistentes.size()) 
		{
			turno.setOcupado( true );
			turno.setIconoMotivoOcupado("fa fa-window-close-o");
			turno.setMotivoOcupado("Equipo no disponible" );
		}
		turno.setListaReservasExistentes(listaReservasExistentes);
	}

	
    public List<Turno> getTurnosFecha(TipohorarioFecha thf, boolean establecerOcupacion)
    {
        if (thf.getListaTurnos() != null && !thf.getListaTurnos().isEmpty())
        {
        	// Si ya los hemos obtenido antes no volvemos a obtenerlos
        	return thf.getListaTurnos();
        }
    	List<Turno> listaTurnos = new ArrayList<>(0);
    	if (establecerOcupacion)
    	{
    		thf.setListaTurnos(listaTurnos);
    	}
        if (thf.getTipoHorario() == null)
        {
        	return listaTurnos;
        }
        //Obtenemos el día de la semana correspondiente a la fecha
        int diaSemana = UtilDate.getDiaSemana(thf.getFecha());
        //Obtenemos el HorarioDia para la fecha actual
        HorarioDia  hd = reservableService.getHorarioDiaByTipohorarioDia(thf.getTipoHorario(), diaSemana);
        if (hd == null)
        {
        	return listaTurnos;
        }
        //Añadimos los turnos de mañana
        if (hd.getHoraIniManana()!=null)
        {
        	Date horaIndiceManana = UtilDate.stringToDate(new StringBuilder(UtilDate.dateToString(thf.getFecha(), FORMATO_FECHA)).append(" ").append(hd.getHoraIniManana()).toString(), FORMATO_FECHAHORA);
        	Date horaFinManana = UtilDate.stringToDate(new StringBuilder(UtilDate.dateToString(thf.getFecha(), FORMATO_FECHA)).append(" ").append(hd.getHoraFinManana()).toString(), FORMATO_FECHAHORA);
            if (UtilDate.anterior(horaIndiceManana,horaFinManana))
            {
            	while(horaIndiceManana.before(horaFinManana))
	            {	
	            	Date horaFinTurno = UtilDate.sumarMinutos(horaIndiceManana, this.reservaEdit.getProducto().getTipoReservable().getDuracionMinima());
	            	Turno turno = null;
	            	if (UtilDate.anteriorOrIgual(horaFinTurno, horaFinManana))
	            	{
	            		// Sólo añadimos el turno si la hora de fin es anterior o igual a la hora de fin de mañana 
	            		turno = new Turno("MAÑANA", horaIndiceManana, horaFinTurno);
	            	}
	            	else
	            	{
	            		// O si es el último turno de la mañana lo ponemos con la hora de fin de la mañana
	            		turno = new Turno("MAÑANA", horaIndiceManana, horaFinManana);
	            	}
	                if (establecerOcupacion)
	                {
	                	OcupacionTecnico ot = consumoService.obtenerOcupacionTecnico(this.reservaEdit, turno.getFechaInicio(), turno.getFechaFin());
	                	turno.setFechaInicioTecnico(ot.getFechaInicioTecnico());
	                	turno.setFechaFinTecnico(ot.getFechaFinTecnico());
	                	this.estableceOcupacionTurno(turno);
	                }
	            	listaTurnos.add( turno );
		            horaIndiceManana = UtilDate.sumarMinutos(horaIndiceManana, this.reservaEdit.getProducto().getTipoReservable().getDuracionMinima());
	            }
            }
        }
        //Añadimos los turnos de tarde
        if (hd.getHoraIniTarde()!=null)
        {
        	Date horaIndiceTarde = UtilDate.stringToDate(new StringBuilder(UtilDate.dateToString(thf.getFecha(), FORMATO_FECHA)).append(" ").append(hd.getHoraIniTarde()).toString(), FORMATO_FECHAHORA);
        	Date horaFinTarde = UtilDate.stringToDate(new StringBuilder(UtilDate.dateToString(thf.getFecha(), FORMATO_FECHA)).append(" ").append(hd.getHoraFinTarde()).toString(), FORMATO_FECHAHORA);
            if (UtilDate.anterior(horaIndiceTarde, horaFinTarde))
            {
	            while(horaIndiceTarde.before(horaFinTarde))
	            {
	            	Date horaFinTurno = UtilDate.sumarMinutos(horaIndiceTarde, this.reservaEdit.getProducto().getTipoReservable().getDuracionMinima());
	            	Turno turno = null;
	            	if (UtilDate.anteriorOrIgual(horaFinTurno, horaFinTarde))
	            	{
	            		// Sólo añadimos el turno si la hora de fin es anterior o igual a la hora de fin de tarde
		            	turno = new Turno("TARDE", horaIndiceTarde, horaFinTurno);
	            	}
	            	else
	            	{
	            		// O si es el último turno de la tarde lo ponemos con la hora de fin de la tarde
	            		turno = new Turno("TARDE", horaIndiceTarde, horaFinTarde);
	            	}
	            	if (establecerOcupacion)
	            	{
	                	OcupacionTecnico ot = consumoService.obtenerOcupacionTecnico(this.reservaEdit, turno.getFechaInicio(), turno.getFechaFin());
	                	turno.setFechaInicioTecnico(ot.getFechaInicioTecnico());
	                	turno.setFechaFinTecnico(ot.getFechaFinTecnico());
	            		this.estableceOcupacionTurno(turno);
	            	}
	                listaTurnos.add( turno );
	                horaIndiceTarde = UtilDate.sumarMinutos(horaIndiceTarde, this.reservaEdit.getProducto().getTipoReservable().getDuracionMinima());
	            }
            }
        }
    	if (establecerOcupacion)
    	{
    		thf.setListaTurnos(listaTurnos);
    	}
        return listaTurnos;
    }
		
	private void inicializaHorasReservaEspecial()
	{
		if (this.listaReservables.size()==1)
		{
			this.reservableReservaEspecialAdd =this.listaReservables.get(0);
		}
		String minHoraInicioManana = reservableService.getMinHoraInicioMananaByProductoReservable(this.reservaEdit.getProducto());
		String maxHoraFinManana = reservableService.getMaxHoraFinMananaByProductoReservable(this.reservaEdit.getProducto()); 
		String minHoraInicioTarde = reservableService.getMinHoraInicioTardeByProductoReservable(this.reservaEdit.getProducto());
		String maxHoraFinTarde = reservableService.getMaxHoraFinTardeByProductoReservable(this.reservaEdit.getProducto());
		Date fechaIndice;
		Date maxHoraFin;
		this.listaHorasInicio = new ArrayList<>();
		this.listaHorasFin = new ArrayList<>();
		if (minHoraInicioManana != null)
		{
			fechaIndice = UtilDate.convertirStrHoraToDate(minHoraInicioManana);
			maxHoraFin = UtilDate.convertirStrHoraToDate(maxHoraFinManana);
			int conta = 0;
			while (UtilDate.anterior(fechaIndice, maxHoraFin))
			{
				listaHorasInicio.add(UtilDate.convertirDateHoraToString(fechaIndice, true));
				if (conta>0)
				{
					//No añadimos la primera hora como hora de fin
					listaHorasFin.add(UtilDate.convertirDateHoraToString(fechaIndice, true));
				}
				fechaIndice = UtilDate.sumarMinutos(fechaIndice, this.reservaEdit.getProducto().getTipoReservable().getDuracionMinima());
				conta++;
			}
			// Añadimos la última hora de fin si no se ha añadido ya
			if (!listaHorasFin.contains(UtilDate.convertirDateHoraToString(maxHoraFin, true)))
			{
				listaHorasFin.add(UtilDate.convertirDateHoraToString(maxHoraFin, true));
			}
		}
		if (minHoraInicioTarde != null)
		{
			fechaIndice = UtilDate.convertirStrHoraToDate(minHoraInicioTarde);
			maxHoraFin = UtilDate.convertirStrHoraToDate(maxHoraFinTarde);
			int conta = 0;
			while (UtilDate.anterior(fechaIndice, maxHoraFin))
			{
				listaHorasInicio.add(UtilDate.convertirDateHoraToString(fechaIndice, true));
				if (conta>0)
				{
					listaHorasFin.add(UtilDate.convertirDateHoraToString(fechaIndice, true));
				}
				fechaIndice = UtilDate.sumarMinutos(fechaIndice, this.reservaEdit.getProducto().getTipoReservable().getDuracionMinima());
				conta++;
			}
			// Añadimos la última hora de fin si no se ha añadido ya
			if (!listaHorasFin.contains(UtilDate.convertirDateHoraToString(maxHoraFin, true)))
			{
				listaHorasFin.add(UtilDate.convertirDateHoraToString(maxHoraFin, true));
			}
		}
		if (listaHorasInicio.isEmpty() || listaHorasFin.isEmpty())
		{
			facesMessages.add(StatusMessage.Severity.WARN, "reservas.producto.horario.error", "reservas.producto.horario.error.detalles" , null, null, this.reservaEdit.getProducto().getDescripcion());
		}
	}
	
	public boolean mostrarSolicitudAvisoAnulacion(Reservas res) 
	{
		final Usuario u = this.reservaEdit.getUsuarioSolicitante() == null ? identity.getUsuarioConectado() : this.reservaEdit.getUsuarioSolicitante();
		if (u.equals( res.getUsuario())) 
		{
			return false;
		}
		return (reservableService.getReservaEsperaByUsuarioReserva( u, res ) == null);
	}

	public void solicitarAvisoAnulacion(Reservas res) 
	{
		final Usuario u = this.reservaEdit.getUsuarioSolicitante() == null ? identity.getUsuarioConectado() : this.reservaEdit.getUsuarioSolicitante();
		final ReservaEspera r = new ReservaEspera();
		r.setReserva( res );
		r.setUsuario( u );
		try
		{
			reservableService.guardarReservaEspera(r);
			facesMessages.add(StatusMessage.Severity.INFO, 	"reservas.crearreservaespera.ok", "reservas.crearreservaespera.detalles", null, null, u.getDatosUsuario().getEmail());
		}
		catch (Exception ex)
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "reservas.crearreservaespera.error", null, null, ex.getMessage());
		}
	}
	
	private void establecerListaFechasFinReservaOrdinaria(Turno turno, TipohorarioFecha thf)
	{
		this.listaFechasFinReservaOrdinariaAdd = new ArrayList<>();
		Iterator<Turno> itTur = thf.getListaTurnos().iterator();
		while (itTur.hasNext())
		{
			Turno turnoAux = itTur.next();
			if (turnoAux.getTipo().equals(turno.getTipo()) && turnoAux.getFechaFin().after(turno.getFechaInicio()))
			{
				this.listaFechasFinReservaOrdinariaAdd.add(turnoAux.getFechaFin());
			}
		}
	}
	
	private void establecerListaReservablesReservaOrdinaria(Date fechaInicio, Date fechaFin)
	{
		this.listaReservablesReservaOrdinariaAdd = new ArrayList<>();
		Iterator<Equipo> itRes = this.listaReservables.iterator();
		while (itRes.hasNext())
		{
			Equipo reservable = itRes.next();
			if (reservableService.disponibleReservable(reservable, fechaInicio, fechaFin))
			{
				this.listaReservablesReservaOrdinariaAdd.add(reservable);
			}
		}
		if (this.listaReservablesReservaOrdinariaAdd.size()==1)
		{
			this.reservableReservaOrdinariaAdd = this.listaReservablesReservaOrdinariaAdd.get(0);
		}
	}

	public void cambiadaSemana(int incremento) 
	{
		this.fechaCalendario = UtilDate.sumarDias(this.fechaCalendario, incremento);
		this.estableceInicioFinCalendarioMostrar(this.fechaCalendario);
		if (!identity.tienePerfilEnServicio(TipoPerfil.SUPERVISOR, this.servicioReserva.getCod()) && !identity.tienePerfilEnServicio(TipoPerfil.TECNICO, this.servicioReserva.getCod()))
		{
			if (this.domingoCalendario.before(new Date()))
			{
				// Deshacemos los cambios, no se puede navegar a una semana con todos los días pasados
				this.fechaCalendario = UtilDate.sumarDias(this.fechaCalendario, -incremento);
				this.estableceInicioFinCalendarioMostrar(this.fechaCalendario);
				facesMessages.add(StatusMessage.Severity.WARN, "reservas.cambiosemana.error", "reservas.cambiosemana.error.fechapasada", null, null);
				return;
			}
			if (this.reservaEdit.getProducto().getTipoReservable().getVistaMaxima()!=null && UtilDate.sumarDias(new Date(), this.reservaEdit.getProducto().getTipoReservable().getVistaMaxima()).before(this.lunesCalendario))
			{
				// Deshacemos los cambios, no se puede navegar a una semana más alla de la vista máxima si no eres supervisor o técnico
				this.fechaCalendario = UtilDate.sumarDias(this.fechaCalendario, -incremento);
				this.estableceInicioFinCalendarioMostrar(this.fechaCalendario);
				facesMessages.add(StatusMessage.Severity.WARN, "reservas.cambiosemana.errorr", "reservas.cambiosemana.error.vistamaxima", null, null);
				return;
			}
		}	
		this.inicializaDiasReservaOrdinaria();
	}

	public void cambiadaFechaCalendario() 
	{
		if (this.fechaCalendario == null)
		{
			this.fechaCalendario = new Date();
		}
		this.estableceInicioFinCalendarioMostrar(this.fechaCalendario);
		if (!identity.tienePerfilEnServicio(TipoPerfil.SUPERVISOR, this.servicioReserva.getCod()) && !identity.tienePerfilEnServicio(TipoPerfil.TECNICO, this.servicioReserva.getCod()))
		{
			if (this.domingoCalendario.before(new Date()))
			{
				// Vamos a la semana actual, no se puede navegar a una semana con todos los días pasados
				this.fechaCalendario = new Date();
				this.estableceInicioFinCalendarioMostrar(this.fechaCalendario);
				facesMessages.add(StatusMessage.Severity.WARN, "reservas.cambiosemana.error", "reservas.cambiosemana.error.fechapasada", null, null);
				return;
			}
			if (this.reservaEdit.getProducto().getTipoReservable().getVistaMaxima()!=null && UtilDate.sumarDias(new Date(), this.reservaEdit.getProducto().getTipoReservable().getVistaMaxima()).before(this.lunesCalendario))
			{
				// Vamos a la semana actual, no se puede navegar a una semana más alla de la vista máxima si no eres supervisor o técnico
				this.fechaCalendario = new Date();
				this.estableceInicioFinCalendarioMostrar(this.fechaCalendario);
				facesMessages.add(StatusMessage.Severity.WARN, "reservas.cambiosemana.errorr", "reservas.cambiosemana.error.vistamaxima", null, null);
				return;
			}
		}
		this.inicializaDiasReservaOrdinaria();
		this.fechaCalendario = (Date)this.lunesCalendario.clone();
	}
	
	public void seleccionadoTurno(Turno turno, TipohorarioFecha thf)
	{
		this.fechaInicioReservaOrdinariaAdd = turno.getFechaInicio();
		this.horaFinReservaOrdinariaAdd = UtilDate.convertirDateHoraToString(turno.getFechaFin(), true);
		this.establecerListaFechasFinReservaOrdinaria(turno, thf);
		this.establecerListaReservablesReservaOrdinaria(turno.getFechaInicio(), turno.getFechaFin());
	}
	
	public void seleccionadaHoraFinReservaOrdinaria()
	{
		if (this.horaFinReservaOrdinariaAdd != null)
		{
			this.establecerListaReservablesReservaOrdinaria(this.fechaInicioReservaOrdinariaAdd, UtilDate.stringToDate(new StringBuilder(UtilDate.convertirDateFechaToString(this.fechaInicioReservaOrdinariaAdd)).append(" ").append(this.horaFinReservaOrdinariaAdd).toString(), FORMATO_FECHAHORA ));
			if (this.listaReservablesReservaOrdinariaAdd.isEmpty())
			{
				facesMessages.add(Severity.WARN, "reservas.reservable.nodisponible", "reservas.reservable.nodisponible.horario", null, null, UtilDate.convertirDateHoraToString(this.fechaInicioReservaOrdinariaAdd, true), this.horaFinReservaOrdinariaAdd);
			}
		}
		else
		{
			this.listaReservablesReservaOrdinariaAdd = new ArrayList<>();	
			this.reservableReservaOrdinariaAdd = null;
		}
	}

	public void addReservaOrdinaria()
	{
		Date fechaIndiceInicio = (Date)this.fechaInicioReservaOrdinariaAdd.clone(); 
		Date fechaFin = UtilDate.stringToDate(new StringBuilder(UtilDate.convertirDateFechaToString(this.fechaInicioReservaOrdinariaAdd)).append(" ").append(this.horaFinReservaOrdinariaAdd).toString(), FORMATO_FECHAHORA );
		if (UtilDate.anteriorOrIgual(fechaFin,  fechaIndiceInicio))
		{
			facesMessages.add(StatusMessage.Severity.WARN, KEY_MSJ_ADDLINEA_ERROR, "reserva.addlinea.error.horas", null, null);
			return;
		}
		Date fechaIndiceFin = UtilDate.sumarMinutos(fechaIndiceInicio, this.reservaEdit.getProducto().getTipoReservable().getDuracionMinima());
		int numReservasAdd = 0;
		int numReservasNoPasanComprobacion = 0;
		BigDecimal cantidadDisponibleProyectoProducto = BigDecimal.valueOf(0);
		if (SI.equals(this.reservaEdit.getProducto().getRequiereProyecto()))
		{
			cantidadDisponibleProyectoProducto = proyectoService.getCantidadDisponibleByProyectoProducto(this.reservaEdit.getProyecto(), this.reservaEdit.getProducto());
		}
		while (UtilDate.anterior(fechaIndiceInicio,fechaFin))
		{
			if (UtilDate.posterior(fechaIndiceFin, fechaFin))
			{
				fechaIndiceFin = (Date)fechaFin.clone();
			}
			OcupacionTecnico ot = consumoService.obtenerOcupacionTecnico(this.reservaEdit, fechaIndiceInicio, fechaIndiceFin);
			if (pasaComprobacionesAltaReserva(this.reservableReservaOrdinariaAdd, fechaIndiceInicio, fechaIndiceFin, cantidadDisponibleProyectoProducto))
			{
				Reservas reservaOrdinariaAdd = new Reservas();
				reservaOrdinariaAdd.setConsumo(this.reservaEdit);
				reservaOrdinariaAdd.setFechaInicio(fechaIndiceInicio);
				reservaOrdinariaAdd.setFechaFin(fechaIndiceFin);
				reservaOrdinariaAdd.setFechaInicioTecnico(ot.getFechaInicioTecnico());
				reservaOrdinariaAdd.setFechaFinTecnico(ot.getFechaFinTecnico());
				reservaOrdinariaAdd.setReservable(this.reservableReservaOrdinariaAdd);
				reservaOrdinariaAdd.setTipo("Ordinaria");
				reservaOrdinariaAdd.setEstado(EstadoReserva.PENDIENTE);
				reservaOrdinariaAdd.setUsuario(this.reservaEdit.getUsuarioSolicitante() == null ? identity.getUsuarioConectado() : this.reservaEdit.getUsuarioSolicitante());
				this.listaReservas.add(reservaOrdinariaAdd);
				facesMessages.add(StatusMessage.Severity.INFO, "reserva.addlinea.ok", null, null, this.getDescripcionHoraReserva(reservaOrdinariaAdd));
				numReservasAdd ++;
			}
			else
			{
				numReservasNoPasanComprobacion ++;
			}
			fechaIndiceInicio = UtilDate.sumarMinutos(fechaIndiceInicio, this.reservaEdit.getProducto().getTipoReservable().getDuracionMinima());
			fechaIndiceFin = UtilDate.sumarMinutos(fechaIndiceFin, this.reservaEdit.getProducto().getTipoReservable().getDuracionMinima());
		}
		if (numReservasAdd == 0 && numReservasNoPasanComprobacion == 0)
		{
			facesMessages.add(StatusMessage.Severity.WARN, KEY_MSJ_ADDLINEA_ERROR, "reserva.addlinea.error.horario", null, null);
		}
		if (numReservasAdd>0)
		{
			this.mostrarBotonAddReservas = true;
		}
	}
	
	private boolean estaSeleccionadoDiaSemana(int diaSemana)
	{
		if (this.diasSeleccionadosReservaEspecialAdd == null || this.diasSeleccionadosReservaEspecialAdd.length == 0)
		{
			return false;
		}
		boolean seleccionado = false;
        int indice=0;
        while(indice<this.diasSeleccionadosReservaEspecialAdd.length && !seleccionado)
        {
            seleccionado=this.diasSeleccionadosReservaEspecialAdd[indice]==diaSemana;
            indice++;
        }
        return seleccionado;
	}
	
	private boolean existeReservaSolapadaEnLista(Equipo reservable, Date fechaInicioReserva, Date fechaFinReserva)
	{
		if (this.listaReservas == null || this.listaReservas.isEmpty())
		{
			return false;
		}
		Iterator<Reservas> itReserva = this.listaReservas.iterator();
		while (itReserva.hasNext())
		{
			Reservas reserva = itReserva.next();
			if (reserva.getReservable().equals(reservable) && UtilDate.posterior(reserva.getFechaFin(),fechaInicioReserva) && UtilDate.anterior(reserva.getFechaInicio(), fechaFinReserva))
			{
				return true;
			}
		}
		return false;
	}
	
	private boolean pasaComprobacionesAltaReserva(Equipo reservable, Date fechaInicioReserva, Date fechaFinReserva, BigDecimal cantidadDisponibleProyectoProducto)
	{
		if (!reservableService.disponibleReservable(reservable, fechaInicioReserva, fechaFinReserva ))
		{
			// El equipo no está disponible en esa fecha
			facesMessages.add(StatusMessage.Severity.WARN, KEY_MSJ_ADDLINEA_ERROR, "reserva.addlinea.nodisponible.detalles", null, null, this.getDescripcionHoraReserva(fechaInicioReserva, fechaFinReserva));
			return false;
		}
		if (existeReservaSolapadaEnLista(reservable, fechaInicioReserva, fechaFinReserva))
		{
			// La reserva se solapa con otra reserva de la lista
			facesMessages.add(StatusMessage.Severity.WARN, KEY_MSJ_ADDLINEA_ERROR, "reserva.addlinea.error.solapada", null, null, this.getDescripcionHoraReserva(fechaInicioReserva, fechaFinReserva));
			return false;
		}
		if (this.reservaEdit.getProducto().getTipoReservable().getHorasAntelacionMinima() != null && !identity.tienePerfilEnServicio(TipoPerfil.TECNICO, this.servicioReserva.getCod()) && !identity.tienePerfilEnServicio(TipoPerfil.SUPERVISOR, this.servicioReserva.getCod()) && UtilDate.posterior(UtilDate.sumarHoras(new Date(), this.reservaEdit.getProducto().getTipoReservable().getHorasAntelacionMinima()), fechaInicioReserva))
		{
			// No se ha realizado la reserva con la mínima antelación (y no es supervisor ni técnico)
			facesMessages.add( StatusMessage.Severity.WARN, KEY_MSJ_ADDLINEA_ERROR, "reserva.addlinea.error.minantelacion", null, null, this.getDescripcionHoraReserva(fechaInicioReserva, fechaFinReserva));
			return false;
		}
		if (this.reservaEdit.getProducto().getTipoReservable().getDiasAntelacion() != null && !identity.tienePerfilEnServicio(TipoPerfil.TECNICO, this.servicioReserva.getCod()) && !identity.tienePerfilEnServicio(TipoPerfil.SUPERVISOR, this.servicioReserva.getCod()) && UtilDate.anterior(UtilDate.sumarDias(new Date(), this.reservaEdit.getProducto().getTipoReservable().getDiasAntelacion()), fechaInicioReserva))
		{
			// No se puede hacer la reserva con tanta antelación (y no es supervisor ni técnico)
			facesMessages.add( StatusMessage.Severity.WARN, KEY_MSJ_ADDLINEA_ERROR, "reserva.addlinea.error.maxantelacion", null, null, this.getDescripcionHoraReserva(fechaInicioReserva, fechaFinReserva));
			return false;
		}
		// Comprobamos el stock del proyecto
		if (SI.equals(this.reservaEdit.getProducto().getRequiereProyecto()))
		{
			BigDecimal cantidadDisponible = cantidadDisponibleProyectoProducto.subtract(this.getCantidadConsumidaProyectoReservas());
			if (BigDecimal.valueOf(1).compareTo(cantidadDisponible) > 0)
			{
				facesMessages.add(StatusMessage.Severity.WARN, KEY_MSJ_ADDLINEA_ERROR, "reserva.addlinea.error.stockproyecto", null, null, this.getDescripcionHoraReserva(fechaInicioReserva, fechaFinReserva), this.reservaEdit.getProducto().getDescripcion(), this.reservaEdit.getProyecto().getNombre());
				return false;
			}
		}
		
		return true;
	}
	
	private BigDecimal getCantidadConsumidaProyectoReservas()
	{
		BigDecimal cantidadTotal = BigDecimal.valueOf(0);
		for (Reservas reserva: this.listaReservas)
		{
			if (!EstadoReserva.ANULADA.equals(reserva.getEstado()) && !EstadoReserva.RECHAZADA.equals(reserva.getEstado()))
			{
				cantidadTotal = cantidadTotal.add(BigDecimal.valueOf(1));
			}
		}
		return cantidadTotal;
	}
	
	public void addReservasEspeciales() 
	{
		if (!fechasHorasAddReservasEspecialesOk())
		{
			return;
		}
		int numReservasAdd = 0;
		Date fechaIndice = (Date)this.getFechaDesdeReservaEspecialAdd().clone();
		Date fechaLimiteAddReservas = UtilDate.sumarDias(this.fechaHastaReservaEspecialAdd, 1);
		BigDecimal cantidadDisponibleProyectoProducto = BigDecimal.valueOf(0);
		if (SI.equals(this.reservaEdit.getProducto().getRequiereProyecto()))
		{
			cantidadDisponibleProyectoProducto = proyectoService.getCantidadDisponibleByProyectoProducto(this.reservaEdit.getProyecto(), this.reservaEdit.getProducto());
		}
		while (UtilDate.anterior(fechaIndice, fechaLimiteAddReservas))
		{
			if (estaSeleccionadoDiaSemana(UtilDate.getDiaSemana(fechaIndice)))
			{
				TipoHorario th = reservableService.getTipoHorarioByTipoReservableFecha(this.reservaEdit.getProducto().getTipoReservable(), fechaIndice);
				Date fechaInicioAltaReservasDia = UtilDate.stringToDate(new StringBuilder(UtilDate.convertirDateFechaToString(fechaIndice)).append(" ").append(this.horaInicioReservaEspecialAdd).toString(), FORMATO_FECHAHORA); 
				Date fechaFinAltaReservasDia = UtilDate.stringToDate(new StringBuilder(UtilDate.convertirDateFechaToString(fechaIndice)).append(" ").append(this.horaFinReservaEspecialAdd).toString(), FORMATO_FECHAHORA);
				List<Turno> listaTurnos = this.getTurnosFecha(new TipohorarioFecha(th, fechaIndice), false);
				Iterator<Turno> itTurno = listaTurnos.iterator();
				while (itTurno.hasNext())
				{
					Turno turno = itTurno.next();
					OcupacionTecnico ot = consumoService.obtenerOcupacionTecnico(this.reservaEdit, turno.getFechaInicio(), turno.getFechaFin());
					if (UtilDate.anteriorOrIgual(fechaInicioAltaReservasDia, turno.getFechaInicio()) &&
						UtilDate.anteriorOrIgual(turno.getFechaFin(), fechaFinAltaReservasDia) && 
						pasaComprobacionesAltaReserva(this.reservableReservaEspecialAdd, turno.getFechaInicio(), turno.getFechaFin(), cantidadDisponibleProyectoProducto))
					{
						Reservas reserva = new Reservas();
						reserva.setConsumo(this.reservaEdit);
						reserva.setEstado(EstadoConsumo.PENDIENTE);
						reserva.setFechaInicio(turno.getFechaInicio());
						reserva.setFechaFin(turno.getFechaFin());
						reserva.setFechaInicioTecnico(ot.getFechaInicioTecnico());
						reserva.setFechaFinTecnico(ot.getFechaFinTecnico());
						reserva.setTipo("Especial");
						reserva.setReservable(this.reservableReservaEspecialAdd);
						reserva.setUsuario(this.reservaEdit.getUsuarioSolicitante() == null ? identity.getUsuarioConectado() : this.reservaEdit.getUsuarioSolicitante());
						this.listaReservas.add(reserva);
						numReservasAdd++;
						// Si no se pasan las comprobaciones el propio método se encarga de poner mensajes
					}
				}
			}
			fechaIndice = UtilDate.sumarDias(fechaIndice, 1);
		}
		this.generaMensajeResumenAddReservasEspeciales(numReservasAdd);
	}

	private boolean fechasHorasAddReservasEspecialesOk()
	{
		if (UtilDate.anterior(this.fechaHastaReservaEspecialAdd,  this.fechaDesdeReservaEspecialAdd))
		{
			facesMessages.add(StatusMessage.Severity.WARN, KEY_MSJ_ADDLINEA_ERROR, "reserva.addlinea.error.fechas", null, null);
			return false;
		}
		if (this.horaInicioReservaEspecialAdd.compareTo(this.horaFinReservaEspecialAdd)>=0)
		{
			facesMessages.add(StatusMessage.Severity.WARN, KEY_MSJ_ADDLINEA_ERROR, "reserva.addlinea.error.horas", null, null);
			return false;
		}
		return true;
	}
	
	private void generaMensajeResumenAddReservasEspeciales(int numReservasAdd)
	{
		if (numReservasAdd == 0)
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "reserva.addlineas.error", "reserva.addlineas.error.ninguna", null, null);
		}
		else
		{
			facesMessages.add(StatusMessage.Severity.INFO, "reserva.addlineas.ok", "reserva.addlineas.ok.detalles", null, null, Integer.toString(numReservasAdd));
			this.mostrarBotonAddReservas = true;
		}		
	}

	// Sólo se comprueba este método si el consumo no tiene factura asociada
	public boolean permitidoEliminarReserva(Reservas reserva)
	{
		// Se puede eliminar si se trata de un alta de consumo, 
		// o si es supervisor o técnico de la solicitud
		// o si es el solicitante o IR, la reserva está pendiente o aceptada pero no requiere técnico o no requiere validación y además tiene una fecha posterior a la actual o a las horas permitidas de anulación especificadas en el tipo de reservable
		return this.reservaEdit.getCod() == null || 
		       (identity.tienePerfilEnServicio(TipoPerfil.SUPERVISOR, this.servicioReserva.getCod()) || this.esTecnicoSolicitud()) ||
		       ((EstadoReserva.PENDIENTE.equals(reserva.getEstado()) || (EstadoReserva.ACEPTADA.equals(reserva.getEstado()) && (!consumoService.requiereTecnico(this.reservaEdit) || NO.equals(this.reservaEdit.getProducto().getRequiereValidacion())) && this.reservaEdit.getProducto().getTipoReservable().getHorasAnulacion() !=null && UtilDate.posterior(reserva.getFechaInicio(), UtilDate.sumarHoras(new Date(), this.reservaEdit.getProducto().getTipoReservable().getHorasAnulacion())))) &&
				((identity.tienePerfilEnServicio(TipoPerfil.IR, this.servicioReserva.getCod()) && identity.getUsuarioConectado().equals(this.reservaEdit.getUsuarioIrAsociado())) || (identity.tienePerfilEnServicio(TipoPerfil.MIEMBRO, this.servicioReserva.getCod()) && identity.getUsuarioConectado().equals(this.reservaEdit.getUsuarioSolicitante()))));
	}
	
	public void eliminarReserva(Reservas reserva)
	{
		if (this.reservaEdit.getCod() != null && this.listaReservas.size() == 1)
		{
			if (identity.tienePerfilEnServicio(TipoPerfil.SUPERVISOR, this.servicioReserva.getCod()) || this.esTecnicoSolicitud())
			{
				facesMessages.add(StatusMessage.Severity.WARN, "reserva.dellinea.error", "reserva.dellinea.error.solouna.supervisor", null, null);
			}
			else
			{
				facesMessages.add(StatusMessage.Severity.WARN, "reserva.dellinea.error", "reserva.dellinea.error.solouna", null, null);
			}
			return;
		}
		if (reserva.getCod() != null)
		{
			this.listaReservasEliminadas.add(reserva);
		}
		this.listaReservas.remove(reserva);
		facesMessages.add(StatusMessage.Severity.WARN, "reserva.dellinea.ok", "reserva.dellinea.ok.detalles", null, null);
	}
	
	public void confirmaAsistencia(Reservas reserva, String asistencia ) 
	{
		reserva.setConfirmarAsistencia(asistencia);
		facesMessages.add(StatusMessage.Severity.WARN, "reserva.confirmaasistencia.ok", "reserva.confirmaasistencia.ok.detalles", null, null);
	}
	
	private String getDescripcionHoraReserva(Reservas reserva)
	{
		return getDescripcionHoraReserva(reserva.getFechaInicio(), reserva.getFechaFin());
	}
	
	private String getDescripcionHoraReserva(Date fechaInicio, Date fechaFin)
	{
		return new StringBuilder(UtilDate.dateToString(fechaInicio, FORMATO_FECHA)).append(" de ").append(UtilDate.convertirDateHoraToString(fechaInicio, true)).append(" a ").append(UtilDate.convertirDateHoraToString(fechaFin, true)).toString();
	}

	private String getEstadoCrearPedidoYEstablecerTecnico()
	{
		if (SI.equals(this.reservaEdit.getProducto().getRequiereValidacionIr()) && !identity.getUsuarioConectado().equals(this.reservaEdit.getUsuarioIrAsociado()) && !identity.tienePerfilEnServicio(TipoPerfil.SUPERVISOR, this.servicioReserva.getCod()) && !identity.tienePerfilEnServicio(TipoPerfil.TECNICO, this.servicioReserva.getCod()))
		{
			return EstadoConsumo.PEND_VALIDACION_IR;
		}
		if (consumoService.requiereTecnico(this.reservaEdit))
		{
			Usuario usuarioTecnicoAsignar = consumoService.obtenerTecnicoAsignarReserva(this.reservaEdit, this.listaReservas);
			this.reservaEdit.setUsuarioTecnicoAsignado(usuarioTecnicoAsignar);
		}
		// Establecemos la solicitud de reserva a pendiente si requiere validación o si requiere técnico y no se asigna técnico automático
		if (SI.equals(this.reservaEdit.getProducto().getRequiereValidacion()) || (consumoService.requiereTecnico(this.reservaEdit) && this.reservaEdit.getUsuarioTecnicoAsignado() == null))
		{
			return EstadoConsumo.PENDIENTE;
		}
		else
		{
			return EstadoConsumo.ACEPTADO;
		}
	}
	
	public String crearSolicitud()
	{
		if (SI.equals(this.reservaEdit.getProducto().getRequiereAnexo()) && this.ficheroAnexoReserva == null)
		{
			facesMessages.add(StatusMessage.Severity.WARN, this.getKeyMsj(KEY_MSJ_RESERVAS_CREAR_ERROR), "reservas.guardar.error.requiereanexo", null, null);
			return null;
		}
		if (consumoService.requiereAnexoNivelBioseguridad(this.reservaEdit) && this.ficheroAnexoBioseguridad == null)
		{
			facesMessages.add(StatusMessage.Severity.WARN, this.getKeyMsj(KEY_MSJ_RESERVAS_CREAR_ERROR), KEY_MSJ_RESERVAS_GUARDAR_ERROR_REQUIEREANEXOBIOSEG, null, null);
			return null;
		}
		this.reservaEdit.setFechaSolicitud(new Date());
		this.reservaEdit.setEstado(this.getEstadoCrearPedidoYEstablecerTecnico());
		this.reservaEdit.setUsuarioConectado(identity.getUsuarioConectado());
		if (SI.equals(reservaEdit.getPresupuesto()))
		{
			reservaEdit.setEstadoPresupuesto(this.reservaEdit.getConsumoPadre().getEstadoPresupuesto());
		}
		else
		{
			this.reservaEdit.setCantidad(new BigDecimal(this.listaReservas.size()));
		}
		if (this.comentarioAdd!=null  && !this.comentarioAdd.isEmpty())
		{
			this.reservaEdit.setBitacoraUsuario(new StringBuilder(ABRE_NEGRITA).append(ABRE_CORCHETE).append(UtilDate.dateToString(new Date(), FORMATO_FECHAHORA)).append(CIERRA_CORCHETE).append(" ")
												.append(identity.getUsuarioConectado().getDatosUsuario().getEmail()).append(": ").append(CIERRA_NEGRITA)
												.append(this.comentarioAdd).append(SALTO_LINEA).toString());
		}
		if (this.reservaEdit.getConsumoPadre() != null)
		{
			this.reservaEdit.setEstado(EstadoConsumo.ACEPTADO);
			this.reservaEdit.setInterno(this.reservaEdit.getConsumoPadre().getInterno());
			this.reservaEdit.setUsuarioSolicitante(this.reservaEdit.getConsumoPadre().getUsuarioSolicitante());
			this.reservaEdit.setUsuarioConectado(this.reservaEdit.getConsumoPadre().getUsuarioConectado());
			this.reservaEdit.setUsuarioIrAsociado(this.reservaEdit.getConsumoPadre().getUsuarioIrAsociado());
			this.reservaEdit.setUsuarioTecnicoAsignado(this.reservaEdit.getConsumoPadre().getUsuarioTecnicoAsignado());
			this.reservaEdit.setEntidadPagadora(this.reservaEdit.getConsumoPadre().getEntidadPagadora());
		}
		try 
		{
			consumoService.crearPedidoReserva(this.reservaEdit, this.listaReservas, this.ficheroAnexoReserva, this.ficheroAnexoBioseguridad);
		} 
		catch ( SaiException ex ) 
		{
			log.error("Error al crear consumo de tipo reserva: ", ex);
			facesMessages.add(StatusMessage.Severity.ERROR, this.getKeyMsj(KEY_MSJ_RESERVAS_CREAR_ERROR), null, null, ex.getMessage());
			return null;
		}
		consumoService.recargarConsumo(this.reservaEdit);
		if (NO.equals(this.reservaEdit.getPresupuesto()))
		{
			facesMessages.add(StatusMessage.Severity.INFO, "reservas.crear.ok", KEY_MSJ_RESERVAS_GUARDAR_OK_DETALLES, null, null, this.listaReservas.size());
		}
		else
		{
			facesMessages.add(StatusMessage.Severity.INFO, "reservas.crear.ok.presupuesto", KEY_MSJ_RESERVAS_GUARDAR_OK_DETALLES, null, null, this.reservaEdit.getCantidad().toString());
		}
		if (this.reservaEdit.getConsumoPadre() != null)
		{
			consumoService.recargarConsumo(this.reservaEdit.getConsumoPadre());
			return volverConsumoPadre();
		}
		else 
		{ 
			mensajeService.notificacionesNuevaSolicitud(this.reservaEdit, this.enviaEmail);
		}
		return this.establecerReservaEdit(this.reservaEdit);
	}
	
	
	private boolean disponibilidadTecnicoModificado()
	{
		final Iterator<Reservas> itReserva = this.listaReservas.iterator();
		while (itReserva.hasNext())
		{
			final Reservas reserva = itReserva.next();
			if (!reservableService.disponibleTecnico(this.reservaEdit.getUsuarioTecnicoAsignado(), reserva.getFechaInicioTecnico(), reserva.getFechaFinTecnico()))
			{
				// Mostramos mensaje de que no se puede guardar y volvemos a establecer en el combo el técnico que estaba antes de la modificación 
				facesMessages.add(StatusMessage.Severity.WARN, "reserva.guardar.error.tecniconodisponible", "reserva.guardar.error.tecniconodisponible.detalles", null, null,
															   this.reservaEdit.getUsuarioTecnicoAsignado().getDatosUsuario().getNombreCompleto(),	
															   UtilDate.dateToString(reserva.getFechaInicioTecnico(), FORMATO_FECHA),
															   UtilDate.dateToString(reserva.getFechaInicioTecnico(), FORMATO_HORA),
															   UtilDate.dateToString(reserva.getFechaFinTecnico(), FORMATO_HORA));
				this.restablecerTecnico();
				return false;
			}
		}
		return true;
	}
	
	private void restablecerTecnico()
	{
		if (this.codUsuarioTecnicoAsignado != null)
		{
			this.reservaEdit.setUsuarioTecnicoAsignado(usuarioService.getUsuarioByCod(this.codUsuarioTecnicoAsignado));
		}
		else
		{
			this.reservaEdit.setUsuarioTecnicoAsignado(null);
		}
	}
	
	public String modificarSolicitud()
	{
		boolean enviarMensajeAceptacion = false;
		boolean nuevoTecnicoEstablecido = false;
		if (consumoService.requiereAnexoNivelBioseguridad(this.reservaEdit) && this.ficheroAnexoBioseguridad == null && this.anexoBioseguridadCompletado == null)
		{
			facesMessages.add(StatusMessage.Severity.WARN, this.getKeyMsj(KEY_MSJ_RESERVAS_MODIFICAR_ERROR), KEY_MSJ_RESERVAS_GUARDAR_ERROR_REQUIEREANEXOBIOSEG, null, null);
			return null;
		}
		// Comprobamos asignación de técnico
		if (this.reservaEdit.getConsumoPadre() == null &&
			consumoService.requiereTecnico(this.reservaEdit) && this.reservaEdit.getUsuarioTecnicoAsignado() != null &&
			(this.tecnicoPendiente || !this.reservaEdit.getUsuarioTecnicoAsignado().getCod().equals(this.codUsuarioTecnicoAsignado)))
		{
			// Se acaba de establecer un técnico o se ha modificado el que había
			// Comprobamos disponibilidad del nuevo técnico asignado
			if (!disponibilidadTecnicoModificado())
			{
				// La propia función se encarga de dejar un mensaje si el técnico no está disponible
				return null;
			}
			// Modificamos el estado del consumo si es necesario en función de la selección de técnico
			if (EstadoConsumo.PENDIENTE.equals(this.reservaEdit.getEstado()) && (identity.tienePerfilEnServicio(TipoPerfil.SUPERVISOR, this.servicioReserva.getCod()) || "NO".equals(this.reservaEdit.getProducto().getRequiereValidacion())))
			{
				// Se ha establecido un técnico
				this.reservaEdit.setEstado(EstadoConsumo.ACEPTADO);
				enviarMensajeAceptacion = true;
			}
			nuevoTecnicoEstablecido = true;
		}
		else if (this.reservaEdit.getUsuarioTecnicoAsignado() != null && this.operacionAceptar())
		{
			// El técnico se asignó directamente en la creación de solicitud y el usuario conectado tiene permiso para aceptar. Se debe aceptar automáticamente al guardar
			this.reservaEdit.setEstado(EstadoConsumo.ACEPTADO);
			enviarMensajeAceptacion = true;
		}
		else if (this.reservaEdit.getUsuarioTecnicoAsignado() == null)
		{
			// Se ha modificado el estado de solicitud de técnico o se ha borrado el que había 
			if (EstadoConsumo.ACEPTADO.equals(this.reservaEdit.getEstado()) && consumoService.requiereTecnico(this.reservaEdit))
			{
				// Se ha borrado el técnico en una reserva que requiere técnico
				this.reservaEdit.setEstado(EstadoConsumo.PENDIENTE);
			}
			else
			{
				// Si se ha modificado el estado de solicitud de técnico es posible que se requiera asignación automática de técnico
				if (consumoService.requiereTecnico(this.reservaEdit))
				{
					Usuario usuarioTecnicoAsignar = consumoService.obtenerTecnicoAsignarReserva(this.reservaEdit, this.listaReservas);
					this.reservaEdit.setUsuarioTecnicoAsignado(usuarioTecnicoAsignar);
				}
				// Se pasa de pendiente a aceptado si: 
				// 1. - Se ha asignado un técnico y el usuario es supervisor o no se requiere validación 
				// 2. - La solicitud ya no requiere técnico y no requiere validación 
				if (EstadoConsumo.PENDIENTE.equals(this.reservaEdit.getEstado()) && 
					(this.reservaEdit.getUsuarioTecnicoAsignado()!=null && (identity.tienePerfilEnServicio(TipoPerfil.SUPERVISOR, this.servicioReserva.getCod()) || "NO".equals(this.reservaEdit.getProducto().getRequiereValidacion()))) || 
					(this.reservaEdit.getUsuarioTecnicoAsignado() == null && "NO".equals(this.reservaEdit.getProducto().getRequiereValidacion())))
				{
					// Se ha establecido un técnico
					this.reservaEdit.setEstado(EstadoConsumo.ACEPTADO);
					enviarMensajeAceptacion = true;
				}

			}
		}
		if (SI.equals(reservaEdit.getPresupuesto()))
		{
			reservaEdit.setEstadoPresupuesto(this.reservaEdit.getConsumoPadre().getEstadoPresupuesto());
		}
		else
		{
			this.reservaEdit.setCantidad(new BigDecimal(this.listaReservas.size()));
		}
		try 
		{
			consumoService.modificarPedidoReserva(this.reservaEdit, this.ficheroAnexoBioseguridad, this.listaReservas, this.listaReservasEliminadas);
		} 
		catch ( SaiException ex ) 
		{
			log.error("Error al modificar consumo de tipo reserva: ", ex);
			facesMessages.add(StatusMessage.Severity.ERROR, this.getKeyMsj(KEY_MSJ_RESERVAS_MODIFICAR_ERROR), null, null, ex.getMessage());
			return null;
		}
		consumoService.recargarConsumo(this.reservaEdit);
		mensajeService.notificacionesModificacionSolicitud(this.reservaEdit, this.modificadoListadoReservas(), enviarMensajeAceptacion, nuevoTecnicoEstablecido, this.codUsuarioTecnicoAsignado);
		if (NO.equals(this.reservaEdit.getPresupuesto()))
		{
			facesMessages.add(StatusMessage.Severity.INFO, "reservas.modificar.ok", KEY_MSJ_RESERVAS_GUARDAR_OK_DETALLES, null, null, this.listaReservas.size());
		}
		else
		{
			facesMessages.add(StatusMessage.Severity.INFO, "reservas.modificar.ok.presupuesto", KEY_MSJ_RESERVAS_GUARDAR_OK_DETALLES, null, null, this.reservaEdit.getCantidad().toString());
		}
		return this.establecerReservaEdit(this.reservaEdit);
	}

	
	public String validarIr() 
	{
		if (consumoService.requiereAnexoNivelBioseguridad(this.reservaEdit) && this.ficheroAnexoBioseguridad == null && this.anexoBioseguridadCompletado == null)
		{
			facesMessages.add(StatusMessage.Severity.WARN, KEY_MSJ_RESERVAS_MODIFICAR_ERROR, KEY_MSJ_RESERVAS_GUARDAR_ERROR_REQUIEREANEXOBIOSEG, null, null);
			return null;
		}
		if (consumoService.requiereTecnico(this.reservaEdit))
		{
			Usuario usuarioTecnicoAsignar = consumoService.obtenerTecnicoAsignarReserva(this.reservaEdit, this.listaReservas);
			this.reservaEdit.setUsuarioTecnicoAsignado(usuarioTecnicoAsignar);
		}
		// Pasamos la reserva al estado en la que se debería haber grabado si no requiriera validación de IR
		if (SI.equals(this.reservaEdit.getProducto().getRequiereValidacion()) ||
			(consumoService.requiereTecnico(this.reservaEdit) && this.reservaEdit.getUsuarioTecnicoAsignado() == null)) 
		{
			this.reservaEdit.setEstado(EstadoConsumo.PENDIENTE);
		}
		else 
		{
			this.reservaEdit.setEstado(EstadoConsumo.ACEPTADO);
		}
		try 
		{
			consumoService.modificarPedidoReserva(this.reservaEdit, this.ficheroAnexoBioseguridad, this.listaReservas, this.listaReservasEliminadas);
		}
		catch (final Exception ex) 
		{
			log.error( "Error al validar por IR consumo de tipo reserva #0", ex.getMessage());
			facesMessages.add(StatusMessage.Severity.ERROR, "reservas.validar.error", null, null, ex.getMessage());
			return null;
		}
		consumoService.recargarConsumo(this.reservaEdit);
		mensajeService.notificacionesValidacionIrSolicitud(this.reservaEdit);
		facesMessages.add(StatusMessage.Severity.INFO, "reservas.validar.ok", null, null, this.reservaEdit.getCod().toString());
		return this.establecerReservaEdit(this.reservaEdit);
	}
	
	public String validar() 
	{
		boolean nuevoTecnicoEstablecido = false;
		if (consumoService.requiereAnexoNivelBioseguridad(this.reservaEdit) && this.ficheroAnexoBioseguridad == null && this.anexoBioseguridadCompletado == null)
		{
			facesMessages.add(StatusMessage.Severity.WARN, KEY_MSJ_RESERVAS_MODIFICAR_ERROR, KEY_MSJ_RESERVAS_GUARDAR_ERROR_REQUIEREANEXOBIOSEG, null, null);
			return null;
		}
		// Comprobamos asignación de técnico
		if (this.reservaEdit.getConsumoPadre() == null &&
			consumoService.requiereTecnico(this.reservaEdit) && this.reservaEdit.getUsuarioTecnicoAsignado() != null &&
			(this.tecnicoPendiente || !this.reservaEdit.getUsuarioTecnicoAsignado().getCod().equals(this.codUsuarioTecnicoAsignado)))
		{
			// Se acaba de establecer un técnico o se ha modificado el que había
			// Comprobamos disponibilidad del nuevo técnico asignado
			if (!disponibilidadTecnicoModificado())
			{
				// La propia función se encarga de dejar un mensaje si el técnico no está disponible
				return null;
			}
			nuevoTecnicoEstablecido = true;
		}
		this.reservaEdit.setEstado(EstadoConsumo.ACEPTADO);
		try 
		{
			
			consumoService.modificarPedidoReserva(this.reservaEdit, this.ficheroAnexoBioseguridad, this.listaReservas, this.listaReservasEliminadas);

		} 
		catch ( final Exception ex ) 
		{
			log.error( "Error al validar consumo de tipo reserva #0", ex.getMessage());
			facesMessages.add(StatusMessage.Severity.ERROR, "reservas.validar.error", null, null, ex.getMessage());
			return null;
		}
		consumoService.recargarConsumo(this.reservaEdit);
		mensajeService.notificacionesAceptacionSolicitud(this.reservaEdit, nuevoTecnicoEstablecido, this.codUsuarioTecnicoAsignado);
		facesMessages.add(StatusMessage.Severity.INFO, "reservas.validar.ok", null, null, this.reservaEdit.getCod().toString());
		
		return this.establecerReservaEdit(this.reservaEdit);

	}
	
	public String anularTodas() 
	{
		this.reservaEdit.setEstado(EstadoConsumo.ANULADO);
		try 
		{
			consumoService.modificarPedidoReserva(this.reservaEdit, this.ficheroAnexoBioseguridad, this.listaReservas, this.listaReservasEliminadas);			
		} 
		catch ( final Exception ex ) 
		{
			log.error( "Error al anular todas las reservas #0", ex.getMessage());
			facesMessages.add(StatusMessage.Severity.ERROR, "reservas.anulartodas.error", null, null, ex.getMessage());
			return null;
		}
		this.comentarioAdd = "Anulado consumo y todas sus reservas";
		guardarBitacoraUsuario();
		consumoService.recargarConsumo(this.reservaEdit);
		facesMessages.add(StatusMessage.Severity.INFO, "reservas.anulartodas.ok", null, null, this.reservaEdit.getCod().toString());
		if (this.reservaEdit.getConsumoPadre() == null)
		{
			return this.establecerReservaEdit(this.reservaEdit);
		}
		else
		{
			return solicitudPrestaciones.establecerPrestacionEdit(this.reservaEdit.getConsumoPadre());
		}
	}

	public String rechazarTodas() 
	{
		this.reservaEdit.setEstado(EstadoConsumo.RECHAZADO);
		try 
		{
			consumoService.modificarPedidoReserva(this.reservaEdit, this.ficheroAnexoBioseguridad, this.listaReservas, this.listaReservasEliminadas);
		} 
		catch ( final Exception ex ) 
		{
			log.error( "Error al rechazar todas las reservas #0", ex.getMessage() );
			facesMessages.add(StatusMessage.Severity.ERROR, "reservas.rechazartodas.error", null, null, ex.getMessage());
			return null;
		}
		this.comentarioAdd = "Rechazado consumo y todas sus reservas";
		this.guardarBitacoraUsuario();
		consumoService.recargarConsumo(this.reservaEdit);
		mensajeService.notificacionesRechazoSolicitud(this.reservaEdit);
		facesMessages.add(StatusMessage.Severity.INFO, "reservas.rechazartodas.ok", null, null, this.reservaEdit.getCod().toString());
		return this.establecerReservaEdit(this.reservaEdit);

	}
	
	public String volverListado() 
	{
		return VOLVER_LISTADO;
	}
	
	public String volverConsumoPadre()
	{
		return solicitudPrestaciones.establecerPrestacionEdit(this.reservaEdit.getConsumoPadre());
	}
	
	public boolean requiereTecnico()
	{
		return consumoService.requiereTecnico(this.reservaEdit);
	}
	
	public List<Anexo> getAnexosReserva() 
	{
		return consumoService.getAnexosConsumoByConsumo(this.reservaEdit);
	}
	
	public boolean esTecnicoSolicitud()
	{
		if (this.servicioReserva == null)
		{
			return false;
		}
		return consumoService.esTecnicoSolicitud(this.reservaEdit, this.servicioReserva);
	}
	
	public boolean requerimientosTecnicoValidos()
	{
		return !"PR".equals(this.reservaEdit.getProducto().getRequiereTecnico()) || this.reservaEdit.getSolicitudTecnico() != null;
	}
	
	private boolean puedeHacerOperacion(OperacionConsumo operacion)
	{
		return consumoService.permitidaOperacion(this.reservaEdit, this.servicioReserva, "R", operacion );
	}

	public boolean operacionGuardar()
	{
		return puedeHacerOperacion(OperacionConsumo.GUARDAR);
	}

	public boolean operacionAnular()
	{
		if (NO.equals(reservaEdit.getPresupuesto())) 
		{
			return puedeHacerOperacion(OperacionConsumo.ANULAR);
		}
		else
		{
			return operacionPresupuestoModificar();
		}
	}

	public boolean operacionAceptar()
	{
		return NO.equals(reservaEdit.getPresupuesto()) && puedeHacerOperacion(OperacionConsumo.ACEPTAR);		
	}

	public boolean operacionRechazar()
	{
		return NO.equals(reservaEdit.getPresupuesto()) && puedeHacerOperacion(OperacionConsumo.RECHAZAR);
	}

	public boolean operacionValidar()
	{
		return NO.equals(reservaEdit.getPresupuesto()) && puedeHacerOperacion(OperacionConsumo.VALIDAR);
	}

	public boolean operacionModificar()
	{
		if (NO.equals(reservaEdit.getPresupuesto())) 
		{
			return puedeHacerOperacion(OperacionConsumo.MODIFICAR);
		}
		else
		{
			return operacionPresupuestoModificar();
		}
	}

	private boolean operacionPresupuestoModificar()
	{
		return SI.equals(reservaEdit.getPresupuesto()) && 
			   EstadoPresupuesto.SOLICITADO.equals(reservaEdit.getEstadoPresupuesto()) && 
			   (identity.tienePerfilEnServicio(TipoPerfil.SUPERVISOR, reservaEdit.getConsumoPadre().getProducto().getServicio().getCod()) || 
				(identity.tienePerfilEnServicio(TipoPerfil.TECNICO, reservaEdit.getConsumoPadre().getProducto().getServicio().getCod()) && identity.getUsuarioConectado().equals(reservaEdit.getConsumoPadre().getUsuarioTecnicoAsignado())));
	}
	
	public boolean operacionComentar()
	{
		return puedeHacerOperacion(OperacionConsumo.COMENTAR);
	}

	public boolean operacionComentarFacturacion()
	{
		return NO.equals(reservaEdit.getPresupuesto()) && puedeHacerOperacion(OperacionConsumo.COMENTAR_FACTURACION);
	}
	
	public boolean preguntarEnvioMailUsuario()
	{
		return consumoService.preguntarEnvioMailUsuario(this.reservaEdit);
	}
	
	public boolean requiereAnexoNivelBioseguridad()
	{
		return consumoService.requiereAnexoNivelBioseguridad(this.reservaEdit);
	}
	
	public String obtieneColor(Reservas r) 
	{
		return r.getEstado().toLowerCase();
	}

	public void guardarBitacoraMail() 
	{
		this.guardarBitacoraUsuario();
		mensajeService.enviarUsuarioComentarioSolicitud(this.reservaEdit);
	}

	public void guardarBitacoraUsuario() 
	{
		try 
		{
			StringBuilder comentario = new StringBuilder("");
			if (this.reservaEdit.getBitacoraUsuario() != null)
			{
				comentario.append(this.reservaEdit.getBitacoraUsuario());
			}
			comentario.append(ABRE_NEGRITA).append(ABRE_CORCHETE).append(UtilDate.dateToString(new Date(), FORMATO_FECHAHORA)).append(CIERRA_CORCHETE).append(" ").append(identity.getUsuarioConectado().getDatosUsuario().getEmail()).append(": ").append(CIERRA_NEGRITA)
					  .append(this.comentarioAdd).append(SALTO_LINEA);
			if (comentario.length()>4000)
			{
				facesMessages.add(StatusMessage.Severity.WARN, KEY_MSJ_COMENTARIO_ADD_ERROR, "reservas.comentario.add.error.bitacorallena", null, null);
				return;
			}
			this.reservaEdit.setBitacoraUsuario(comentario.toString());
			consumoService.guardarConsumo(this.reservaEdit);
			if (identity.getUsuarioConectado().equals(this.reservaEdit.getUsuarioSolicitante()) || 
				identity.getUsuarioConectado().equals(this.reservaEdit.getUsuarioIrAsociado()))
			{
				mensajeService.enviarTecnicoOSupervisorComentarioSolicitud(this.reservaEdit);
			}
			facesMessages.add(StatusMessage.Severity.INFO, "reservas.comentario.add.ok", null, null, this.comentarioAdd);
			this.comentarioAdd = null;
		} 
		catch ( final Exception ex ) 
		{
			facesMessages.add(StatusMessage.Severity.ERROR, KEY_MSJ_COMENTARIO_ADD_ERROR, null, null, ex.getMessage());
		}
	}

	public void guardarBitacora() 
	{
		try 
		{
			StringBuilder comentario = new StringBuilder("");
			if (this.reservaEdit.getBitacora() != null)
			{
				comentario.append(this.reservaEdit.getBitacora());
			}
			comentario.append(ABRE_NEGRITA).append(ABRE_CORCHETE).append(UtilDate.dateToString(new Date(), FORMATO_FECHAHORA)).append(CIERRA_CORCHETE).append(" ").append(identity.getUsuarioConectado().getDatosUsuario().getEmail()).append(": ")
					  .append(CIERRA_NEGRITA).append(this.comentarioAdd).append(SALTO_LINEA);
			if (comentario.length()>4000)
			{
				facesMessages.add(StatusMessage.Severity.WARN, KEY_MSJ_COMENTARIO_ADD_ERROR, "reservas.comentario.add.error.bitacorallena", null, null);
				return;
			}
			this.reservaEdit.setBitacora(comentario.toString());
			consumoService.guardarConsumo(this.reservaEdit);
			facesMessages.add(StatusMessage.Severity.INFO, "reservas.comentario.add.ok", null, null, this.comentarioAdd);
			this.comentarioAdd = null;
		} 
		catch ( final Exception ex ) 
		{
			facesMessages.add(StatusMessage.Severity.ERROR, KEY_MSJ_COMENTARIO_ADD_ERROR, null, null, ex.getMessage() );
		}
	}

	public String getNombreDiaSemana(int dia) 
	{
		return reservableService.getNombreDiaSemana( dia );
	}
	
	public String getNombreDiaSemanaByFecha( Date fecha ) 
	{
		int dia = UtilDate.getDiaSemana(fecha);
		return getNombreDiaSemana(dia);
	}

    public String getDescripcionEntidad(EntidadPagadora ep)
    {
    	return tarifaService.getDescripcionEntidadPagadora( ep );
    }
    
    public String getDescripcionSolicitudTecnico()
    {
    	return consumoService.getDescripcionOpcionSolicitudTecnico(this.reservaEdit);
    }
    
	public String getColorEstado()
	{
		return consumoService.getColorEstadoConsumo(this.reservaEdit);
	}
	
	public String getColorEstadoReserva(String estado)
	{
		if (estado == null)
		{
			return null;
		}
		if (estado.equals(EstadoReserva.PENDIENTE))
		{
			return "amarillo";
		}
		else if (estado.equals(EstadoReserva.ACEPTADA)) 
		{
			return "verde";
		}
		else if (estado.equals(EstadoReserva.ANULADA) || estado.equals(EstadoReserva.RECHAZADA)) 
		{
			return "rojo";
		}
		return null;

	}
	
	public String getHoraByFecha(Date fecha)
	{
		return UtilDate.convertirDateHoraToString(fecha, true);
	}
	
	public void onTabChange(TabChangeEvent event) 
	{
		try
		{
			if (event.getTab().getId().equals("tabReservaOrdinaria"))
			{
				this.tabActivo = 0;
			}
			else if (event.getTab().getId().equals("tabReservaEspecial"))
			{
				this.tabActivo = 1;
			}
		}
		catch(Exception ex)
		{
			if (this.tabActivo == 0)
			{
				this.tabActivo = 1;
			}
			else
			{
				this.tabActivo = 0;
			}
		}
	}
	
	
	private boolean modificadoListadoReservas()
	{
		if (!this.listaReservasEliminadas.isEmpty())
		{
			return true;
		}
		for (Reservas reserva: this.listaReservas)
		{
			if (reserva.getCod() == null)
			{
				return true;
			}
		}
		return false;
	}
	
	public String formatCantidad( BigDecimal number ) 
	{
		return Utilidades.formatCantidad(number);	
	}

	private String getKeyMsj(String key)
	{
		if ("SI".equals(this.reservaEdit.getPresupuesto()))
		{
			return new StringBuilder(key).append(".presupuesto").toString();
		}
		return key;
	}
	
}
