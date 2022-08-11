package es.um.atica.sai.entities;

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

@Entity
@SequenceGenerator(name="SEQ_SERVICIO_PUERTAKRON",sequenceName="SQ_SERVICIO_PUERTAKRON",allocationSize=1)
@Table(name = "SERVICIO_PUERTAKRON")
@NamedQueries ( {
    @NamedQuery (name = ServicioPuertakron.GET_SERVICIO_PUERTAKRON_BY_SERVICIO, query = "SELECT spk FROM ServicioPuertakron spk WHERE spk.servicio=:servicio ORDER BY spk.puertaKron.puertaKronView.nombreEdificio, spk.puertaKron.puertaKronView.nombreLector"),
    @NamedQuery (name = ServicioPuertakron.EXISTE_SERVICIOPUERTAKRON, query = "SELECT COUNT(spk) FROM ServicioPuertakron spk WHERE spk.servicio=:servicio AND spk.puertaKron=:puertakron"),
})
public class ServicioPuertakron implements java.io.Serializable{

	private static final long serialVersionUID = -9168417155639104210L;

	public static final String GET_SERVICIO_PUERTAKRON_BY_SERVICIO = "SERVICIO_PUERTAKRON.GET_SERVICIO_PUERTAKRON_BY_SERVICIO";
	public static final String EXISTE_SERVICIOPUERTAKRON = "SERVICIO_PUERTAKRON.EXISTE_SERVICIOPUERTAKRON";

    private Long cod;
    private Servicio servicio;
	private PuertaKron puertaKron;

 	public ServicioPuertakron() {
 		
 	}
 	
 	public ServicioPuertakron(Long cod, Servicio servicio, PuertaKron puertaKron) {
 		this.cod = cod;
 		this.servicio = servicio;
 		this.puertaKron = puertaKron;
 	}

 	@Id
 	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_SERVICIO_PUERTAKRON")
 	@Column(name = "COD", nullable = false, precision = 19, scale = 0)
 	public Long getCod() {
 		return cod;
 	}

 	public void setCod( Long cod ) {
 		this.cod = cod;
 	}

 	@ManyToOne(fetch = FetchType.EAGER)
 	@JoinColumn(name = "COD_SERVICIO", nullable = false)
 	public Servicio getServicio() {
 		return servicio;
 	}

 	public void setServicio( Servicio servicio ) {
 		this.servicio = servicio;
 	}


 	@ManyToOne(fetch = FetchType.EAGER)
 	@JoinColumn(name = "COD_PUERTA_KRON", nullable = false)
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
 		if ( !( obj instanceof ServicioPuertakron ) ) {
 			return false;
 		}
 		ServicioPuertakron other = ( ServicioPuertakron ) obj;
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
