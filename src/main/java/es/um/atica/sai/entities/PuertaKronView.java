package es.um.atica.sai.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "PUERTA_KRON_VIEW", schema="SAI")

public class PuertaKronView {
	
	private Long cod;
	private String lector;
	private String nombreEdificio;
	private String nombreLector;
	private PuertaKron puertaKron;
	
	public PuertaKronView() {
		
	}
	
	public PuertaKronView(Long cod, String lector, String ip, String nombreEdificio, String nombreLector) {
		this.cod = cod;
		this.lector = lector;
		this.ip = ip;
		this.nombreEdificio = nombreEdificio;
		this.nombreLector = nombreLector;
	}
	
	@Id    
    @Column(name = "COD", nullable = false, precision = 19, scale = 0)
    @NotNull
	public Long getCod() {
		return cod;
	}
	
	public void setCod( Long cod ) {
		this.cod = cod;
	}
	
	@Column(name = "LECTOR", length = 2)
    @NotNull
    @Length(max = 2)
	public String getLector() {
		return lector;
	}
	
	public void setLector( String lector ) {
		this.lector = lector;
	}	
	
	@Column(name = "IP", length = 20)
    @Length(max = 20)
	private String ip;
	public String getIp() {
		return ip;
	}
	
	public void setIp( String ip ) {
		this.ip = ip;
	}	
	
	@Column(name = "NOMBRE_EDIFICIO", nullable = true, length = 60)
	public String getNombreEdificio() {
		return nombreEdificio;
	}
	
	public void setNombreEdificio( String nombreEdificio ) {
		this.nombreEdificio = nombreEdificio;
	}	
	
	@Column(name = "NOMBRE_LECTOR", nullable = true, length = 50)
	public String getNombreLector() {
		return nombreLector;
	}
	
	public void setNombreLector( String nombreLector ) {
		this.nombreLector = nombreLector;
	}

	@OneToOne (mappedBy = "puertaKronView", fetch = FetchType.LAZY, optional = false)
	public PuertaKron getPuertaKron() {
		return puertaKron;
	}

	
	public void setPuertaKron( PuertaKron puertaKron ) {
		this.puertaKron = puertaKron;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( cod == null ) ? 0 : cod.hashCode() );
		return result;
	}

	@Override
	public boolean equals( Object obj ) {
		if ( this == obj ) {
			return true;
		}
		if ( !( obj instanceof PuertaKronView ) ) {
			return false;
		}
		PuertaKronView other = ( PuertaKronView ) obj;
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
