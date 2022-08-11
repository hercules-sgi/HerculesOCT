package es.um.atica.sai.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@SequenceGenerator(name="SEQ_PUERTAKRON",sequenceName="SQ_PUERTA_KRON",allocationSize=1)
@Table(name = "PUERTA_KRON")
@NamedQueries({
    @NamedQuery(name = PuertaKron.GET_ALL, query = "SELECT p FROM PuertaKron p" ),
    @NamedQuery(name = PuertaKron.GET_PUERTA_BY_LECTOR_IP, query = "SELECT p FROM PuertaKron p WHERE p.extPuertaKronLector=:lector AND p.extPuertaKronIp=:ip" ),
})
public class PuertaKron implements java.io.Serializable{
	
	private static final long serialVersionUID = -5959126749559182590L;

	public static final String GET_ALL = "PUERTA_KRON.GET_ALL";
	public static final String GET_PUERTA_BY_LECTOR_IP = "PUERTA_KRON.GET_PUERTA_BY_LECTOR_IP";
	
	private Long cod;
	private String extPuertaKronIp;
	private String extPuertaKronLector;
	private PuertaKronView puertaKronView;
	
	public PuertaKron() {
	}
	
	public PuertaKron(Long cod, String extPuertaKronIp, String extPuertaKronLector) {
		this.cod = cod;
		this.extPuertaKronIp = extPuertaKronIp;
		this.extPuertaKronLector = extPuertaKronLector;
	}
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_PUERTAKRON")
    @Column(name = "COD", nullable = false, precision = 19, scale = 0)
    @NotNull
	public Long getCod() {
		return cod;
	}
	
	public void setCod( Long cod ) {
		this.cod = cod;
	}

	@Column(name = "EXT_PUERTAKRON_IP", length = 20)
	public String getExtPuertaKronIp() {
		return extPuertaKronIp;
	}

	public void setExtPuertaKronIp( String extPuertaKronIp ) {
		this.extPuertaKronIp = extPuertaKronIp;
	}
	
	@Column(name = "EXT_PUERTAKRON_LECTOR", length = 2)
    @NotNull
	public String getExtPuertaKronLector() {
		return extPuertaKronLector;
	}
	
	public void setExtPuertaKronLector( String extPuertaKronLector ) {
		this.extPuertaKronLector = extPuertaKronLector;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COD")
	public PuertaKronView getPuertaKronView() {
		return puertaKronView;
	}
	
	public void setPuertaKronView( PuertaKronView puertaKronView ) {
		this.puertaKronView = puertaKronView;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( cod == null ) ? 0 : cod.hashCode() );
		result = prime * result + ( ( extPuertaKronIp == null ) ? 0 : extPuertaKronIp.hashCode() );
		result = prime * result + ( ( extPuertaKronLector == null ) ? 0 : extPuertaKronLector.hashCode() );
		return result;
	}

	@Override
	public boolean equals( Object obj ) {
		if ( this == obj )
			return true;
		if ( obj == null )
			return false;
		if ( !( obj instanceof PuertaKron ) )
			return false;
		PuertaKron other = ( PuertaKron ) obj;
		if ( cod == null ) {
			if ( other.getCod() != null )
				return false;
		} else if ( !cod.equals( other.getCod() ) )
			return false;
		return true;
	}	
	
	
}
