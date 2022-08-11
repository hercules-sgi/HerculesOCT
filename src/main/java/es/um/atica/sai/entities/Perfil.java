package es.um.atica.sai.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "PERFIL" )
@NamedQueries( { 
    @NamedQuery(name = Perfil.GET_PERFIL_X_TAG, query = "SELECT p FROM Perfil p WHERE p.tag=:tagperfil"),
    @NamedQuery(name = Perfil.GET_LISTA_PERFILES, query = "SELECT p FROM Perfil p ORDER BY p.cod"),
})
public class Perfil implements Serializable{

	private static final long serialVersionUID = 2938847201640254809L;

	public static final String GET_PERFIL_X_TAG="PERFIL.GET_PERFIL_X_TAG";
	public static final String GET_LISTA_PERFILES="PERFIL.GET_LISTA_PERFILES";
	public static final String GET_LISTA_TAGSPERFIL_REQUIEREN_SERVICIO="PERFIL.GET_LISTA_TAGSPERFIL_REQUIEREN_SERVICIO";
	
	private Long cod;
	private String nombre;
	private String tag;
	private String requiereServicio;
	private String requiereIr;
	private String caduca;
	private String pendienteValidarInicial;
	
	private List<UsuarioPerfil> usuariosPerfil = new ArrayList<>(0);
	private List<PerfilPuedeCrear> perfilesEsPadre = new ArrayList<>(0);
	private List<PerfilPuedeCrear> perfilesEsHijo = new ArrayList<>(0);
	
	public Perfil() {}
	
    public Perfil( Long cod, String nombre, String tag, String requiereServicio, String requiereIr, String caduca, String pendienteValidarInicial ) 
    {
		this.cod = cod;
		this.nombre = nombre;
		this.tag = tag;
		this.requiereServicio = requiereServicio;
		this.requiereIr = requiereIr;
		this.caduca = caduca;
		this.pendienteValidarInicial = pendienteValidarInicial;
	}



	@Id
    @Column(name = "COD", nullable = false, precision = 19, scale = 0 )
    @NotNull
	public Long getCod() {
		return cod;
	}
	
	public void setCod( Long cod ) {
		this.cod = cod;
	}
	
	@Column(name = "NOMBRE", nullable = false, length = 50 )
    @NotNull
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre( String nombre ) {
		this.nombre = nombre;
	}

	@Column(name = "TAG", nullable = false, length = 30 )
    @NotNull
	public String getTag() {
		return tag;
	}
	
	public void setTag( String tag ) {
		this.tag = tag;
	}

	@Column(name = "REQUIERE_SERVICIO", nullable = false, length = 2 )
    @NotNull
	public String getRequiereServicio() {
		return requiereServicio;
	}
	
	public void setRequiereServicio( String requiereServicio ) {
		this.requiereServicio = requiereServicio;
	}

	@Column(name = "REQUIERE_IR", nullable = false, length = 2 )
    @NotNull
	public String getRequiereIr() {
		return requiereIr;
	}
	
	public void setRequiereIr( String requiereIr ) {
		this.requiereIr = requiereIr;
	}
	
	@Column(name = "CADUCA", nullable = false, length = 2 )
    @NotNull
	public String getCaduca() {
		return caduca;
	}
	
	public void setCaduca( String caduca ) {
		this.caduca = caduca;
	}
	
	@Column(name = "PENDIENTE_VALIDAR_INICIAL", nullable = false, length = 2 )
    @NotNull
	public String getPendienteValidarInicial() {
		return pendienteValidarInicial;
	}
	
	public void setPendienteValidarInicial( String pendienteValidarInicial ) {
		this.pendienteValidarInicial = pendienteValidarInicial;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "perfil" )
	public List<UsuarioPerfil> getUsuariosPerfil() {
		return usuariosPerfil;
	}

	
	public void setUsuariosPerfil( List<UsuarioPerfil> usuariosPerfil ) {
		this.usuariosPerfil = usuariosPerfil;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "perfilPadre" )
	public List<PerfilPuedeCrear> getPerfilesEsPadre() {
		return perfilesEsPadre;
	}

	
	public void setPerfilesEsPadre( List<PerfilPuedeCrear> perfilesEsPadre ) {
		this.perfilesEsPadre = perfilesEsPadre;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "perfilHijo" )
	public List<PerfilPuedeCrear> getPerfilesEsHijo() {
		return perfilesEsHijo;
	}

	
	public void setPerfilesEsHijo( List<PerfilPuedeCrear> perfilesEsHijo ) {
		this.perfilesEsHijo = perfilesEsHijo;
	}
	
	
	
	
/*
	public static final String SUPERVISOR = "supervisor";
    public static final String TECNICO = "tecnico";
    public static final String INVESTIGADOR  = "investigador";
    public static final String GESTOR = "gestor";
    public static final String GESTOR_SAI = "gestorSai";
    public static final String ADMINISTRATIVO = "administrativo";
    */   
}
