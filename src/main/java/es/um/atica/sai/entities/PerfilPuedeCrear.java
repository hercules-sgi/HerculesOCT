package es.um.atica.sai.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "PERFIL_PUEDE_CREAR" )

public class PerfilPuedeCrear implements Serializable{
	
	private static final long serialVersionUID = 8073144377849433875L;

	private Long cod;
	private Perfil perfilPadre;
	private Perfil perfilHijo;
	
	public PerfilPuedeCrear() {}
	
    public PerfilPuedeCrear( Long cod, Perfil perfilPadre, Perfil perfilHijo ) {
		this.cod = cod;
		this.perfilPadre = perfilPadre;
		this.perfilHijo = perfilHijo;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COD_PERFIL_PADRE")
    @NotNull
	public Perfil getPerfilPadre() {
		return perfilPadre;
	}
	
	public void setPerfilPadre( Perfil perfilPadre ) {
		this.perfilPadre = perfilPadre;
	}

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COD_PERFIL_HIJO")
    @NotNull
	public Perfil getPerfilHijo() {
		return perfilHijo;
	}
	
	public void setPerfilHijo( Perfil perfilHijo ) {
		this.perfilHijo = perfilHijo;
	}
	
}
