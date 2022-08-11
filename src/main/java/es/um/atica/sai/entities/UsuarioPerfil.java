package es.um.atica.sai.entities;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "USUARIO_PERFIL" )
@SequenceGenerator(name = "SQ_USUARIO_PERFIL", sequenceName = "SQ_USUARIO_PERFIL", initialValue = 0, allocationSize = 1 )
@NamedQueries( { 
    @NamedQuery(name = UsuarioPerfil.GET_PERFILES_USUARIO, query = "SELECT up FROM UsuarioPerfil up WHERE up.usuario=:usuario AND up.pendienteValidarIr='NO' AND up.usuario.estado='ALTA' AND up.usuario.acepta='SI' AND (up.usuario.caduca IS NULL OR up.usuario.caduca>:fechaactual) ORDER BY up.perfil.cod, up.servicio"),
    @NamedQuery(name = UsuarioPerfil.GET_PERFILES_USUARIO_X_SERVICIO, query = "SELECT up FROM UsuarioPerfil up WHERE up.usuario=:usuario AND up.servicio=:servicio AND up.pendienteValidarIr='NO' AND up.usuario.estado='ALTA' AND up.usuario.acepta='SI' AND (up.usuario.caduca IS NULL OR up.usuario.caduca>:fechaactual) ORDER BY up.perfil.cod"),
    @NamedQuery(name = UsuarioPerfil.GET_PERFILES_USUARIO_IGNORA_ESTADO, query = "SELECT up FROM UsuarioPerfil up WHERE up.usuario=:usuario ORDER BY up.perfil.cod, up.servicio"),
    @NamedQuery(name = UsuarioPerfil.TIENE_PERFIL_IR_IGNORA_ESTADO, query = "SELECT COUNT(up) FROM UsuarioPerfil up WHERE up.usuario=:usuario AND up.perfil.tag='IR'"),
    @NamedQuery(name = UsuarioPerfil.GET_PERFILESPUEDECREAR_X_PERFIL, query = "SELECT ppc.perfilHijo FROM PerfilPuedeCrear ppc WHERE ppc.perfilPadre=:perfil"),
    @NamedQuery(name = UsuarioPerfil.GET_SUPERVISOR_SERVICIO, query = "SELECT up FROM UsuarioPerfil up WHERE up.usuario=:usuario AND up.servicio=:servicio AND up.perfil.tag='SUPERVISOR' AND up.pendienteValidarIr='NO' AND up.usuario.estado='ALTA' AND up.usuario.acepta='SI' AND (up.usuario.caduca IS NULL OR up.usuario.caduca>:fechaactual)"),
    @NamedQuery(name = UsuarioPerfil.GET_SUPERVISORES_SERVICIO, query = "SELECT up FROM UsuarioPerfil up WHERE up.servicio=:servicio AND up.perfil.tag='SUPERVISOR' AND up.pendienteValidarIr='NO' AND up.usuario.estado='ALTA' AND up.usuario.acepta='SI' AND (up.usuario.caduca IS NULL OR up.usuario.caduca>:fechaactual)"),
    @NamedQuery(name = UsuarioPerfil.GET_USUARIOPERFIL_NOREQUIERESERVICIO, query = "SELECT up FROM UsuarioPerfil up WHERE up.usuario=:usuario AND up.perfil=:perfil"),
    @NamedQuery(name = UsuarioPerfil.GET_USUARIOPERFIL_SIREQUIERESERVICIO_NOREQUIEREIR, query = "SELECT up FROM UsuarioPerfil up WHERE up.usuario=:usuario AND up.perfil=:perfil AND up.servicio=:servicio"),
    @NamedQuery(name = UsuarioPerfil.GET_USUARIOPERFIL_SIREQUIERESERVICIO_SIREQUIEREIR, query = "SELECT up FROM UsuarioPerfil up WHERE up.usuario=:usuario AND up.perfil=:perfil AND up.servicio=:servicio AND up.usuarioIrResponsable=:usuarioirresponsable")

})

public class UsuarioPerfil implements Serializable{

	private static final long serialVersionUID = 2938847201640254809L;
	
	public static final String GET_PERFILES_USUARIO = "USUARIO_PERFIL.GET_PERFILES_USUARIO";
	public static final String GET_PERFILES_USUARIO_X_SERVICIO = "USUARIO_PERFIL.GET_PERFILES_USUARIO_X_SERVICIO";
	public static final String GET_PERFILES_USUARIO_IGNORA_ESTADO = "USUARIO_PERFIL.GET_PERFILES_USUARIO_IGNORA_ESTADO";
	public static final String TIENE_PERFIL_IR_IGNORA_ESTADO = "USUARIO_PERFIL.TIENE_PERFIL_IR_IGNORA_ESTADO";
	public static final String GET_PERFILESPUEDECREAR_X_PERFIL = "USUARIO_PERFIL.GET_PERFILESPUEDECREAR_X_PERFIL";
	public static final String GET_SUPERVISOR_SERVICIO = "USUARIO_PERFIL.GET_SUPERVISOR_SERVICIO";
	public static final String GET_SUPERVISORES_SERVICIO = "USUARIO_PERFIL.GET_SUPERVISORES_SERVICIO";
	public static final String GET_USUARIOPERFIL_NOREQUIERESERVICIO = "USUARIO_PERFIL.GET_USUARIOPERFIL_NOREQUIERESERVICIO";
	public static final String GET_USUARIOPERFIL_SIREQUIERESERVICIO_NOREQUIEREIR = "USUARIO_PERFIL.GET_USUARIOPERFIL_SIREQUIERESERVICIO_NOREQUIEREIR";
	public static final String GET_USUARIOPERFIL_SIREQUIERESERVICIO_SIREQUIEREIR = "USUARIO_PERFIL.GET_USUARIOPERFIL_SIREQUIERESERVICIO_SIREQUIEREIR";
	
	private Long cod;
	private Perfil perfil;
	private Usuario usuario;
	private Servicio servicio;
	private Usuario usuarioIrResponsable;
	private String avisarIrSolicitud;
	private Usuario usuarioSupervisorValidador;
	private String pendienteValidarIr;
	private Date fechaAlta;
	
	public UsuarioPerfil() {}
	
    public UsuarioPerfil(Perfil perfil, Usuario usuario, Servicio servicio, Usuario usuarioIrResponsable,
						 Usuario usuarioSupervisorValidador, String pendienteValidarIr, Date fechaAlta ) {
		this.perfil = perfil;
		this.usuario = usuario;
		this.servicio = servicio;
		this.usuarioIrResponsable = usuarioIrResponsable;
		this.usuarioSupervisorValidador = usuarioSupervisorValidador;
		this.pendienteValidarIr = pendienteValidarIr;
		this.fechaAlta = fechaAlta;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_USUARIO_PERFIL" )
    @Column(name = "COD", nullable = false, precision = 19, scale = 0 )
    @NotNull
	public Long getCod() {
		return cod;
	}
	
	public void setCod( Long cod ) {
		this.cod = cod;
	}
	
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COD_PERFIL")
    @NotNull
	public Perfil getPerfil() {
		return perfil;
	}
	
	public void setPerfil( Perfil perfil ) {
		this.perfil = perfil;
	}

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COD_USUARIO")
    @NotNull
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario( Usuario usuario ) {
		this.usuario = usuario;
	}

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COD_SERVICIO")
	public Servicio getServicio() {
		return servicio;
	}
	
	public void setServicio( Servicio servicio ) {
		this.servicio = servicio;
	}

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COD_IR_RESPONSABLE")
	public Usuario getUsuarioIrResponsable() {
		return usuarioIrResponsable;
	}
	
	public void setUsuarioIrResponsable( Usuario usuarioIrResponsable ) {
		this.usuarioIrResponsable = usuarioIrResponsable;
	}
	
	@Column(name = "AVISAR_IR_SOLICITUD", length = 2 )
	public String getAvisarIrSolicitud() {
		return avisarIrSolicitud;
	}
	
	public void setAvisarIrSolicitud( String avisarIrSolicitud ) {
		this.avisarIrSolicitud = avisarIrSolicitud;
	}

	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COD_SUPERVISOR_VALIDADOR")
	public Usuario getUsuarioSupervisorValidador() {
		return usuarioSupervisorValidador;
	}
	
	public void setUsuarioSupervisorValidador( Usuario usuarioSupervisorValidador ) {
		this.usuarioSupervisorValidador = usuarioSupervisorValidador;
	}

	@Column(name = "PENDIENTE_VALIDAR_IR", length = 2 )
	public String getPendienteValidarIr() {
		return pendienteValidarIr;
	}

	
	public void setPendienteValidarIr( String pendienteValidarIr ) {
		this.pendienteValidarIr = pendienteValidarIr;
	}

    @Temporal( TemporalType.DATE )
    @Column(name = "FECHA_ALTA", length = 7, nullable = false )
	public Date getFechaAlta() {
		return fechaAlta;
	}

	
	public void setFechaAlta( Date fechaAlta ) {
		this.fechaAlta = fechaAlta;
	}

}
