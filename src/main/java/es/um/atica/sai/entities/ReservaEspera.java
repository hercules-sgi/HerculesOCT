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
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "RESERVA_ESPERA")
@SequenceGenerator(name="SEQ_RESERVA_ESPERA",sequenceName="SQ_RESERVA_ESPERA",initialValue=0,allocationSize=1)
@NamedQueries ( {
    @NamedQuery(name = ReservaEspera.GET_RESERVA_ESPERA_BY_RESERVA, 
    		query="select res from ReservaEspera res where res.reserva=:reserva order by res.cod ASC" ),
    @NamedQuery(name = ReservaEspera.GET_RESERVA_ESPERA_BY_USUARIO_RESERVA, 
	query="select res from ReservaEspera res where res.usuario=:usuario and res.reserva=:reserva order by res.cod ASC" ),
})
public class ReservaEspera implements java.io.Serializable{
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 4123929964651567275L;
	
	public static final String GET_RESERVA_ESPERA_BY_RESERVA = "getReservaEsperaByReserva";
	public static final String GET_RESERVA_ESPERA_BY_USUARIO_RESERVA = "getReservaEsperaByUsuarioReserva";

	@Id
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "SEQ_RESERVA_ESPERA" )
	@Column( name = "COD", nullable = false, precision = 19, scale = 0 )
	@NotNull	
	private Long cod;
	
	@ManyToOne( fetch = FetchType.EAGER )
	@JoinColumn( name = "COD_USUARIO", nullable = false )
	@NotNull
	private Usuario usuario;
	
	@ManyToOne( fetch = FetchType.EAGER )
	@JoinColumn( name = "COD_RESERVA", nullable = false )
	@NotNull
	private Reservas reserva;

	public ReservaEspera() {
		super();
	}
	
	public ReservaEspera(Long cod, Reservas reserva, Usuario usuario) {
		this.cod=cod;
		this.reserva = reserva;
		this.usuario = usuario;
	}
	
	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario( Usuario usuario ) {
		this.usuario = usuario;
	}

	
	public Reservas getReserva() {
		return reserva;
	}

	public void setReserva( Reservas reserva ) {
		this.reserva = reserva;
	}

	public Long getCod() {
		return cod;
	}

	public void setCod( Long cod ) {
		this.cod = cod;
	}
}
