package es.um.atica.sai.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "USUARIO" )
@SequenceGenerator(name = "SEQ_USUARIO", sequenceName = "SAI_USUARIO", initialValue = 0, allocationSize = 1 )

@NamedQueries( { 
    @NamedQuery(name = Usuario.GET_USUARIO_X_LOGIN, query = "SELECT DISTINCT us FROM Usuario us WHERE us.email=:mail or us.datosUsuario.email=:mail" ),
    @NamedQuery(name = Usuario.GET_USUARIO_X_EMAIL, query = "SELECT us FROM Usuario us WHERE us.email=:mail" ),
    @NamedQuery(name = Usuario.GET_USUARIO_X_DNI, query = "SELECT us FROM Usuario us WHERE us.dni=:dni" ),
    @NamedQuery(name = Usuario.GET_USUARIOS_PENDIENTES, query = "SELECT us FROM Usuario us WHERE us.estado IN ('ALTA','PEND')"),
    @NamedQuery(name = Usuario.GET_ALL, query = "SELECT us FROM Usuario us ORDER BY us.datosUsuario.nombreCompleto"),
    @NamedQuery(name = Usuario.GET_IR_BY_MIEMBRO_SERVICIO, query = "SELECT DISTINCT up.usuarioIrResponsable FROM UsuarioPerfil up, UsuarioPerfil up2 WHERE up2.usuario=up.usuarioIrResponsable AND up.usuario=:usuariomiembro AND up.servicio=:servicio AND up.pendienteValidarIr='NO' AND up.usuario.estado='ALTA' AND up.usuario.acepta='SI' AND (up.usuario.caduca IS NULL OR up.usuario.caduca>:fechaactual) AND up2.pendienteValidarIr='NO' AND up2.usuario.estado='ALTA' AND up2.usuario.acepta='SI' AND (up2.usuario.caduca IS NULL OR up2.usuario.caduca>:fechaactual) "),
    @NamedQuery(name = Usuario.GET_IR_BY_MIEMBRO, query = "SELECT DISTINCT up.usuarioIrResponsable FROM UsuarioPerfil up, UsuarioPerfil up2 WHERE up2.usuario=up.usuarioIrResponsable AND up.usuario=:usuariomiembro AND up.pendienteValidarIr='NO' AND up.usuario.estado='ALTA' AND up.usuario.acepta='SI' AND (up.usuario.caduca IS NULL OR up.usuario.caduca>:fechaactual) AND up2.pendienteValidarIr='NO' AND up2.usuario.estado='ALTA' AND up2.usuario.acepta='SI' AND (up2.usuario.caduca IS NULL OR up2.usuario.caduca>:fechaactual) "),
    @NamedQuery(name = Usuario.AVISAR_IR_NUEVA_SOLICITUD_MIEMBRO, query = "SELECT up.avisarIrSolicitud FROM UsuarioPerfil up, UsuarioPerfil up2 WHERE up2.usuario=up.usuarioIrResponsable AND up.usuario=:usuariomiembro AND up.usuarioIrResponsable=:usuarioir AND up.servicio=:servicio AND up.pendienteValidarIr='NO' AND up.usuario.estado='ALTA' AND up.usuario.acepta='SI' AND (up.usuario.caduca IS NULL OR up.usuario.caduca>:fechaactual) AND up2.pendienteValidarIr='NO' AND up2.usuario.estado='ALTA' AND up2.usuario.acepta='SI' AND (up2.usuario.caduca IS NULL OR up2.usuario.caduca>:fechaactual) "),
    @NamedQuery(name = Usuario.GET_USUARIOS_SERVICIO, query = "SELECT DISTINCT up.usuario FROM UsuarioPerfil up WHERE up.servicio=:servicio AND up.pendienteValidarIr='NO' AND up.usuario.acepta='SI' AND (up.usuario.caduca IS NULL OR up.usuario.caduca>:fechaactual) AND up.usuario.estado='ALTA'"),
	@NamedQuery(name = Usuario.GET_USUARIOS_LISTA_SERVICIO, query = "SELECT DISTINCT up.usuario FROM UsuarioPerfil up WHERE up.servicio IN (:servicios) AND up.pendienteValidarIr='NO' AND up.usuario.acepta='SI' AND (up.usuario.caduca IS NULL OR up.usuario.caduca>:fechaactual) AND up.usuario.estado='ALTA'" ),
    @NamedQuery(name = Usuario.GET_USUARIOS_PUEDOSOLICITARENSUNOMBRE_X_SERVICIO, query = "SELECT DISTINCT up.usuario FROM UsuarioPerfil up WHERE (up.perfil.tag='IR' OR up.perfil.tag='MIEMBRO') AND up.servicio=:servicio AND up.pendienteValidarIr='NO' AND up.usuario.acepta='SI' AND (up.usuario.caduca IS NULL OR up.usuario.caduca>:fechaactual) AND up.usuario.estado='ALTA'"),
    @NamedQuery(name = Usuario.GET_USUARIOS_PUEDOSOLICITAR_CERTIFICACION, query = "SELECT DISTINCT up.usuario FROM UsuarioPerfil up WHERE up.perfil.tag<>'SUPERGESTOR' AND up.pendienteValidarIr='NO' AND up.usuario.acepta='SI' AND (up.usuario.caduca IS NULL OR up.usuario.caduca>:fechaactual) AND up.usuario.estado='ALTA'"),
    @NamedQuery(name = Usuario.GET_USUARIOS_PUEDOSOLICITAR_CERTIFICACION_X_LISTASERVICIOS, query = "SELECT DISTINCT up.usuario FROM UsuarioPerfil up WHERE (up.perfil.tag='IR' OR up.perfil.tag='MIEMBRO' OR up.perfil.tag='TECNICO') AND up.servicio IN (:listaservicios) AND up.pendienteValidarIr='NO' AND up.usuario.acepta='SI' AND (up.usuario.caduca IS NULL OR up.usuario.caduca>:fechaactual) AND up.usuario.estado='ALTA'"),
    @NamedQuery(name = Usuario.GET_IR_X_SERVICIO, query = "SELECT up.usuario FROM UsuarioPerfil up WHERE up.servicio=:servicio AND up.perfil.tag='IR' AND up.pendienteValidarIr='NO' AND up.usuario.acepta='SI' AND (up.usuario.caduca IS NULL OR up.usuario.caduca>:fechaactual) AND up.usuario.estado='ALTA' ORDER BY up.usuario.datosUsuario.nombreCompleto"),
    @NamedQuery(name = Usuario.GET_IR_X_LISTASERVICIOS, query = "SELECT DISTINCT up.usuario FROM UsuarioPerfil up WHERE up.servicio IN (:listaservicios) AND up.perfil.tag='IR' AND up.pendienteValidarIr='NO' AND up.usuario.acepta='SI' AND (up.usuario.caduca IS NULL OR up.usuario.caduca>:fechaactual) AND up.usuario.estado='ALTA'"),
    @NamedQuery(name = Usuario.GET_IR_ALL, query = "SELECT DISTINCT up.usuario FROM UsuarioPerfil up WHERE up.perfil.tag='IR' AND up.pendienteValidarIr='NO' AND up.usuario.acepta='SI' AND (up.usuario.caduca IS NULL OR up.usuario.caduca>:fechaactual) AND up.usuario.estado='ALTA'"),
    @NamedQuery(name = Usuario.GET_MIEMBROS_X_IR, query = "SELECT DISTINCT up.usuario FROM UsuarioPerfil up WHERE up.usuarioIrResponsable.cod=:codusuarioir AND up.perfil.tag='MIEMBRO' AND up.pendienteValidarIr='NO' AND up.usuario.estado='ALTA' AND up.usuario.acepta='SI' AND (up.usuario.caduca IS NULL OR up.usuario.caduca>:fechaactual)"),
    @NamedQuery(name = Usuario.GET_MIEMBROS_X_IR_SERVICIO, query = "SELECT up.usuario FROM UsuarioPerfil up WHERE up.usuarioIrResponsable.cod=:codusuarioir AND up.servicio=:servicio AND up.perfil.tag='MIEMBRO' AND up.pendienteValidarIr='NO' AND up.usuario.estado='ALTA' AND up.usuario.acepta='SI' AND (up.usuario.caduca IS NULL OR up.usuario.caduca>:fechaactual) ORDER BY up.usuario.datosUsuario.nombreCompleto"),
    @NamedQuery(name = Usuario.GET_TECNICOS_BY_SERVICIO_PROD, query = "SELECT t.usuario FROM TecnicoProducto t, UsuarioPerfil up WHERE t.producto.servicio=up.servicio AND t.usuario=up.usuario AND up.perfil.tag='TECNICO' AND  t.producto.servicio=:servicio AND up.pendienteValidarIr='NO' AND up.usuario.acepta='SI' AND (up.usuario.caduca IS NULL OR up.usuario.caduca>:fechaactual) AND up.usuario.estado='ALTA' ORDER BY t.usuario.datosUsuario.nombreCompleto"),
    @NamedQuery(name = Usuario.GET_TECNICOS_BY_PROD, query = "SELECT t.usuario FROM TecnicoProducto t WHERE t.producto=:producto AND t.usuario.estado='ALTA' AND t.usuario.acepta='SI' AND (t.usuario.caduca IS NULL OR t.usuario.caduca>:fechaactual) ORDER BY t.usuario.datosUsuario.nombreCompleto"),
    @NamedQuery(name = Usuario.GET_TECNICOS_AUTO_BY_PROD, query = "SELECT t.usuario FROM TecnicoProducto t WHERE t.producto=:producto AND t.automatico='SI' AND t.usuario.estado='ALTA' AND t.usuario.acepta='SI' AND (t.usuario.caduca IS NULL OR t.usuario.caduca>:fechaactual) ORDER BY t.prioridad DESC"),
    @NamedQuery(name = Usuario.GET_TECNICOS_BY_SERVICIO, query = "SELECT up.usuario from UsuarioPerfil up where up.servicio=:servicio and up.perfil.tag='TECNICO' AND up.pendienteValidarIr='NO' AND up.usuario.estado='ALTA' AND up.usuario.acepta='SI' AND (up.usuario.caduca IS NULL OR up.usuario.caduca>:fechaactual) ORDER BY up.usuario.datosUsuario.nombreCompleto"),
    @NamedQuery(name = Usuario.GET_IRS_CON_CONSUMOS_PENDIENTES_BY_SERVICIO, query = "SELECT DISTINCT con.usuarioIrAsociado FROM Consumo con WHERE con.producto.servicio=:servicio AND con.producto.facturable='SI' AND con.factura IS NULL AND con.estado<>'Anulado' AND con.fechaSolicitud>:fechahace3anos" ),
    @NamedQuery(name = Usuario.GET_USUARIOSOL_BY_CONSUMO, query = "SELECT c.usuarioSolicitante from Consumo c where c=:consumo"),
    @NamedQuery(name = Usuario.GET_SUPERVISORES_SERVICIO, query="SELECT DISTINCT up.usuario FROM UsuarioPerfil up WHERE up.servicio=:servicio AND up.perfil.tag='SUPERVISOR' AND up.pendienteValidarIr='NO' AND up.usuario.estado='ALTA' AND up.usuario.acepta='SI' AND (up.usuario.caduca IS NULL OR up.usuario.caduca>:fechaactual)"),
    @NamedQuery(name = Usuario.GET_MIEMBROS_SERVICIO, query="SELECT DISTINCT up.usuario FROM UsuarioPerfil up WHERE up.servicio=:servicio AND up.perfil.tag='MIEMBRO' AND up.pendienteValidarIr='NO' AND up.usuario.estado='ALTA' AND up.usuario.acepta='SI' AND (up.usuario.caduca IS NULL OR up.usuario.caduca>:fechaactual)"),
    @NamedQuery(name = Usuario.TIENE_PERFIL, query="SELECT COUNT(up) FROM UsuarioPerfil up WHERE up.usuario=:usuario AND up.perfil.tag=:tagperfil AND up.pendienteValidarIr='NO' AND up.usuario.acepta='SI' AND (up.usuario.caduca IS NULL OR up.usuario.caduca>:fechaactual) AND up.usuario.estado='ALTA'"),
    @NamedQuery(name = Usuario.TIENE_PERFIL_EN_SERVICIO, query="SELECT COUNT(up) FROM UsuarioPerfil up WHERE up.usuario=:usuario AND up.perfil.tag=:tagperfil AND up.servicio=:servicio AND up.pendienteValidarIr='NO' AND up.usuario.acepta='SI' AND (up.usuario.caduca IS NULL OR up.usuario.caduca>:fechaactual) AND up.usuario.estado='ALTA'"),
	@NamedQuery(name = Usuario.GET_USUARIO_BY_PATRON, query = "SELECT CONCAT(u.datosUsuario.nombreCompleto, ' - ', u.dni, ' - ', u.email) FROM Usuario u WHERE LOWER(CONCAT(u.datosUsuario.nombreCompleto, '%', u.dni, '%', u.email)) LIKE :patron" ),
} )

public class Usuario implements java.io.Serializable, Comparable<Usuario> {

	private static final long serialVersionUID = 1233414837484907604L;

	public static final String GET_USUARIO_X_LOGIN = "USUARIO.GET_USUARIO_X_LOGIN";
	public static final String GET_USUARIO_X_EMAIL = "USUARIO.GET_USUARIO_X_EMAIL";
	public static final String GET_USUARIO_X_DNI = "USUARIO.GET_USUARIO_X_DNI";
	public static final String GET_USUARIOS_PENDIENTES = "USUARIO.GET_USUARIOS_PENDIENTES";
	public static final String GET_ALL = "USUARIO.GET_ALL";
	public static final String GET_IR_BY_MIEMBRO_SERVICIO = "USUARIO.GET_IR_BY_MIEMBRO_SERVICIO";
	public static final String GET_IR_BY_MIEMBRO = "USUARIO.GET_IR_BY_MIEMBRO";
	public static final String AVISAR_IR_NUEVA_SOLICITUD_MIEMBRO = "USUARIO.AVISAR_IR_NUEVA_SOLICITUD_MIEMBRO";
	public static final String GET_USUARIOS_SERVICIO = "USUARIO.GET_USUARIOS_SERVICIO";
	public static final String GET_USUARIOS_LISTA_SERVICIO = "USUARIO.GET_USUARIOS_LISTA_SERVICIO";
	public static final String GET_USUARIOS_PUEDOSOLICITARENSUNOMBRE_X_SERVICIO = "USUARIO.GET_USUARIOS_PUEDOSOLICITARENSUNOMBRE_X_SERVICIO";
	public static final String GET_USUARIOS_PUEDOSOLICITAR_CERTIFICACION = "USUARIO.GET_USUARIOS_PUEDOSOLICITAR_CERTIFICACION";
	public static final String GET_USUARIOS_PUEDOSOLICITAR_CERTIFICACION_X_LISTASERVICIOS = "USUARIO.GET_USUARIOS_PUEDOSOLICITAR_CERTIFICACION_X_LISTASERVICIOS";
	public static final String GET_IR_X_SERVICIO = "USUARIO.GET_IR_X_SERVICIO";
	public static final String GET_IR_X_LISTASERVICIOS = "USUARIO.GET_IR_X_LISTASERVICIOS";
	public static final String GET_IR_ALL = "USUARIO.GET_IR_ALL";
	public static final String GET_MIEMBROS_X_IR = "USUARIO.GET_MIEMBROS_X_IR";
	public static final String GET_MIEMBROS_X_IR_SERVICIO = "USUARIO.GET_MIEMBROS_X_IR_SERVICIO";
	public static final String GET_TECNICOS_BY_SERVICIO_PROD = "USUARIO.GET_TECNICOS_BY_SERVICIO_PROD";
	public static final String GET_TECNICOS_BY_PROD = "USUARIO.GET_TECNICOS_BY_PROD";
	public static final String GET_TECNICOS_AUTO_BY_PROD = "USUARIO.GET_TECNICOS_AUTO_BY_PROD";
	public static final String GET_TECNICOS_BY_SERVICIO = "USUARIO.GET_TECNICOS_BY_SERVICIO";
	public static final String GET_IRS_CON_CONSUMOS = "USUARIO.GET_IRS_CON_CONSUMOS";
	public static final String GET_IRS_CON_CONSUMOS_PENDIENTES_BY_SERVICIO = "USUARIO.GET_IRS_CON_CONSUMOS_PENDIENTES_BY_SERVICIO";
	public static final String GET_USUARIOSOL_BY_CONSUMO = "USUARIO.GET_USUARIOSOL_BY_CONSUMO";
	public static final String GET_SUPERVISORES_SERVICIO = "USUARIO.GET_SUPERVISORES_SERVICIO";
	public static final String GET_MIEMBROS_SERVICIO = "USUARIO.GET_MIEMBROS_SERVICIO";
	public static final String TIENE_PERFIL = "USUARIO.TIENE_PERFIL";
	public static final String TIENE_PERFIL_EN_SERVICIO = "USUARIO.TIENE_PERFIL_EN_SERVICIO";
	public static final String GET_USUARIO_BY_PATRON = "USUARIO.GET_USUARIO_BY_PATRON";

	private Long cod;
	private String dni;
	private String nombre;
	private String apellidos;
	private String email;
	private String contrasena;
	private String estado;
	private String telefono;
	private String acepta;
	private Date caduca;
	private String observaciones;
	private Date fechaAvisoCaducidad;
	private Usuario usuarioValidador;
	private EntidadPagadora entidadPagadoraSolicita;

	private DatosUsuarioView datosUsuario;
	private List<Consumo> consumosEsTecnicoAsignado = new ArrayList<>(0);
	private List<Consumo> consumosEsIrAsociado = new ArrayList<>( 0 );
	private List<Consumo> consumosEsSolicitante = new ArrayList<>( 0 );
	private List<Consumo> consumosEsCreador = new ArrayList<>( 0 );
	private List<Factura> facturasEsInvestigador = new ArrayList<>( 0 );
	private List<TecnicoProducto> tecnicoProductos = new ArrayList<>( 0 );
	private List<Reservas> reservasUsuario = new ArrayList<>( 0 );
	private List<ReservaEspera> reservasEsperaUsuario = new ArrayList<>(0);
	private List<EntidadesIr> entidadesIrs = new ArrayList<>( 0 );
	private List<UsuarioPerfil> perfiles = new ArrayList<>(0);
	private List<UsuarioPerfil> perfilesEsResponsable = new ArrayList<>(0);
	private List<UsuarioPerfil> perfilesEsSupervisorValidador = new ArrayList<>(0);


	public Usuario() {
	}

	public Usuario(Long cod, String dni, String nombre, String apellidos, String email, String contrasena,
			String estado, String telefono, String acepta, Date caduca, String observaciones, Date fechaAvisoCaducidad,
			Usuario usuarioValidador, EntidadPagadora entidadPagadoraSolicita ) {
		this.cod = cod;
		this.dni = dni;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.email = email;
		this.contrasena = contrasena;
		this.estado = estado;
		this.telefono = telefono;
		this.acepta = acepta;
		this.caduca = caduca;
		this.observaciones = observaciones;
		this.fechaAvisoCaducidad = fechaAvisoCaducidad;
		this.usuarioValidador = usuarioValidador;
		this.entidadPagadoraSolicita = entidadPagadoraSolicita;
	}


	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USUARIO" )
	@Column(name = "COD", nullable = false, precision = 19, scale = 0 )
	public Long getCod() {
		return cod;
	}

	public void setCod( Long cod ) {
		this.cod = cod;
	}

	@Column(name = "DNI", nullable = false, length = 10 )
	@Pattern(regexp = "(?:[A-Za-z0-9]{1}[0-9]{7})?", message = "DNI/NIE/NIU no válido" )
	public String getDni() {
		return dni;
	}

	public void setDni( String dni ) {
		this.dni = dni;
	}

	@Column(name = "NOMBRE", length = 30 )
	public String getNombre() {
		return nombre;
	}

	public void setNombre( String nombre ) {
		this.nombre = nombre;
	}

	@Column(name = "APELLIDOS", length = 50 )
	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos( String apellidos ) {
		this.apellidos = apellidos;
	}

	@Column(name = "EMAIL", nullable = false, length = 40 )
	public String getEmail() {
		return email;
	}

	public void setEmail( String email ) {
		this.email = email;
	}

	@Column(name = "CONTRASENA", length = 50)
	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena( String contrasena ) {
		this.contrasena = contrasena;
	}

	@Column(name = "ESTADO", nullable = false, length = 20 )
	public String getEstado() {
		return estado;
	}

	public void setEstado( String estado ) {
		this.estado = estado;
	}

	@Column(name = "TELEFONO", length = 20 )
	public String getTelefono() {
		return telefono;
	}

	public void setTelefono( String telefono ) {
		this.telefono = telefono;
	}

	@Column(name = "ACEPTA", nullable = false, length = 2 )
	public String getAcepta() {
		return acepta;
	}

	public void setAcepta( String acepta ) {
		this.acepta = acepta;
	}

	@Temporal( TemporalType.DATE )
	@Column(name = "CADUCA", length = 7 )
	public Date getCaduca() {
		return caduca;
	}

	public void setCaduca( Date caduca ) {
		this.caduca = caduca;
	}

	@Column(name = "OBSERVACIONES", length = 400 )
	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones( String observaciones ) {
		this.observaciones = observaciones;
	}

	@Temporal( TemporalType.DATE )
	@Column(name = "FECHA_AVISO_CADUCIDAD", length = 7 )
	public Date getFechaAvisoCaducidad() {
		return fechaAvisoCaducidad;
	}

	public void setFechaAvisoCaducidad( Date fechaAvisoCaducidad ) {
		this.fechaAvisoCaducidad = fechaAvisoCaducidad;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COD_USUARIO_VALIDADOR")
	public Usuario getUsuarioValidador() {
		return usuarioValidador;
	}

	public void setUsuarioValidador( Usuario usuarioValidador ) {
		this.usuarioValidador = usuarioValidador;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COD_ENTIDADP_SOLICITA")
	public EntidadPagadora getEntidadPagadoraSolicita() {
		return entidadPagadoraSolicita;
	}

	public void setEntidadPagadoraSolicita( EntidadPagadora entidadPagadoraSolicita ) {
		this.entidadPagadoraSolicita = entidadPagadoraSolicita;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COD")
	public DatosUsuarioView getDatosUsuario() {
		return datosUsuario;
	}

	public void setDatosUsuario( DatosUsuarioView datosUsuario ) {
		this.datosUsuario = datosUsuario;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuarioTecnicoAsignado" )
	public List<Consumo> getConsumosEsTecnicoAsignado() {
		return consumosEsTecnicoAsignado;
	}

	public void setConsumosEsTecnicoAsignado( List<Consumo> consumosEsTecnicoAsignado ) {
		this.consumosEsTecnicoAsignado = consumosEsTecnicoAsignado;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuarioIrAsociado" )
	public List<Consumo> getConsumosEsIrAsociado() {
		return consumosEsIrAsociado;
	}

	public void setConsumosEsIrAsociado( List<Consumo> consumosEsIrAsociado ) {
		this.consumosEsIrAsociado = consumosEsIrAsociado;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuarioSolicitante" )
	public List<Consumo> getConsumosEsSolicitante() {
		return consumosEsSolicitante;
	}

	public void setConsumosEsSolicitante( List<Consumo> consumosEsSolicitante ) {
		this.consumosEsSolicitante = consumosEsSolicitante;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuarioConectado" )
	public List<Consumo> getConsumosEsCreador() {
		return consumosEsCreador;
	}

	public void setConsumosEsCreador( List<Consumo> consumosEsCreador ) {
		this.consumosEsCreador = consumosEsCreador;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "investigador" )
	public List<Factura> getFacturasEsInvestigador() {
		return facturasEsInvestigador;
	}

	public void setFacturasEsInvestigador( List<Factura> facturasEsInvestigador ) {
		this.facturasEsInvestigador = facturasEsInvestigador;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario")
	public List<TecnicoProducto> getTecnicoProductos() {
		return tecnicoProductos;
	}

	public void setTecnicoProductos( List<TecnicoProducto> tecnicoProductos ) {
		this.tecnicoProductos = tecnicoProductos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario" )
	public List<Reservas> getReservasUsuario() {
		return reservasUsuario;
	}

	public void setReservasUsuario( List<Reservas> reservasUsuario ) {
		this.reservasUsuario = reservasUsuario;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario" )
	public List<ReservaEspera> getReservasEsperaUsuario() {
		return reservasEsperaUsuario;
	}

	public void setReservasEsperaUsuario( List<ReservaEspera> reservasEsperaUsuario ) {
		this.reservasEsperaUsuario = reservasEsperaUsuario;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario")
	public List<EntidadesIr> getEntidadesIrs() {
		return entidadesIrs;
	}

	public void setEntidadesIrs( List<EntidadesIr> entidadesIrs ) {
		this.entidadesIrs = entidadesIrs;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario")
	public List<UsuarioPerfil> getPerfiles() {
		return perfiles;
	}

	public void setPerfiles( List<UsuarioPerfil> perfiles ) {
		this.perfiles = perfiles;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuarioIrResponsable")
	public List<UsuarioPerfil> getPerfilesEsResponsable() {
		return perfilesEsResponsable;
	}

	public void setPerfilesEsResponsable( List<UsuarioPerfil> perfilesEsResponsable ) {
		this.perfilesEsResponsable = perfilesEsResponsable;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuarioSupervisorValidador")
	public List<UsuarioPerfil> getPerfilesEsSupervisorValidador() {
		return perfilesEsSupervisorValidador;
	}

	public void setPerfilesEsSupervisorValidador( List<UsuarioPerfil> perfilesEsSupervisorValidador ) {
		this.perfilesEsSupervisorValidador = perfilesEsSupervisorValidador;
	}

	public void aceptaCondiciones() {
		setAcepta( "SI" );
	}

	@Override
	public int compareTo( Usuario o ) {
		final Usuario us = o;
		if (datosUsuario.getNombreCompleto()!=null) {
			return datosUsuario.getNombreCompleto().toLowerCase().compareTo( us.getDatosUsuario().getNombreCompleto().toLowerCase() );
		}
		else if (getFullName()!=null)
		{
			return (getFullName().toLowerCase()).compareTo(o.getFullName().toLowerCase());
		}
		else if (dni != null) {
			return dni.compareTo( us.getDni() );
		}
		else
		{
			return 0;
		}
	}

	@Transient
	public String getFullName() {
		String result = null;
		if ( getDatosUsuario() != null )
		{
			result = getDatosUsuario().getFullName();
		}
		return result;
	}

	@Override
	public String toString()
	{
		return dni;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = ( prime * result ) + ( ( cod == null ) ? 0 : cod.hashCode() );
		return result;
	}

	@Override
	public boolean equals( Object obj ) {
		if ( this == obj ) {
			return true;
		}
		if ( obj == null ) {
			return false;
		}
		if ( !( obj instanceof Usuario ) ) {
			return false;
		}
		final Usuario other = ( Usuario ) obj;
		if ( cod == null ) {
			if ( other.getCod() != null ) {
				return false;
			}
		} else if ( !cod.equals( other.getCod() ) ) {
			return false;
		}
		return true;
	}


}
