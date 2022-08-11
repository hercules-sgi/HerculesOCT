package es.um.atica.sai.entities;

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
@SequenceGenerator(name="SEQ_CONSUMO_AUTORIZACION_KRON",sequenceName="SQ_CONSUMO_AUTORIZACION_KRON",allocationSize=1)
@Table(name = "CONSUMO_AUTORIZACION_KRON")
@NamedQueries({
	@NamedQuery(name=ConsumoAutorizacionkron.GET_AUTORIZACIONES_BY_CONSUMO, query="SELECT ca FROM ConsumoAutorizacionkron ca WHERE ca.consumo=:consumo " ),
    @NamedQuery(name=ConsumoAutorizacionkron.GET_AUTORIZACIONES_BY_CONSUMO_PUERTA_FECHAS, query="SELECT ca FROM ConsumoAutorizacionkron ca WHERE ca.consumo=:consumo AND ca.puertaKron=:puertakron AND ca.fechaInicio=:fechainicio AND ca.fechaFin=:fechafin"),
    @NamedQuery(name=ConsumoAutorizacionkron.EXISTE_AUTORIZACION_SOLICITADA, query="SELECT COUNT(ca) FROM ConsumoAutorizacionkron ca WHERE ca.consumo=:consumo AND ca.puertaKron=:puertakron AND ca.fechaInicio=:fechainicio AND ca.fechaFin=:fechafin AND ca.estado='SA'"),
    @NamedQuery(name=ConsumoAutorizacionkron.EXISTE_OTRA_AUTORIZACION_IGUAL_OTRO_CONSUMO, query="SELECT COUNT(ca) FROM ConsumoAutorizacionkron ca WHERE ca.consumo<>:consumo AND ca.consumo.usuarioSolicitante=:usuario AND ca.puertaKron=:puertakron AND ca.fechaInicio=:fechainicio AND ca.fechaFin=:fechafin")
})
public class ConsumoAutorizacionkron implements java.io.Serializable {
	
	private static final long serialVersionUID = 4367262855135845413L;
	
	public static final String GET_AUTORIZACIONES_BY_CONSUMO = "CONSUMO_AUTORIZACION_KRON.GET_AUTORIZACIONES_BY_CONSUMO";
	public static final String GET_AUTORIZACIONES_BY_CONSUMO_PUERTA_FECHAS = "CONSUMO_AUTORIZACION_KRON.GET_AUTORIZACIONES_BY_CONSUMO_PUERTA_FECHAS";
	public static final String EXISTE_AUTORIZACION_SOLICITADA = "CONSUMO_AUTORIZACION_KRON.EXISTE_AUTORIZACION_SOLICITADA";
	public static final String EXISTE_OTRA_AUTORIZACION_IGUAL_OTRO_CONSUMO = "CONSUMO_AUTORIZACION_KRON.EXISTE_OTRA_AUTORIZACION_IGUAL_OTRO_CONSUMO";

    private Long cod;
	private Consumo consumo;
	private PuertaKron puertaKron;
	private Date fechaInicio;
	private Date fechaFin;
	private String estado;

	public ConsumoAutorizacionkron() {
		
	}
	
	public ConsumoAutorizacionkron(Long cod, Consumo consumo, PuertaKron puerta, Date fechaInicio, Date fechaFin, String estado) {
		this.cod = cod;
		this.consumo = consumo;
		this.puertaKron = puerta;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.estado = estado;
	}

	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_CONSUMO_AUTORIZACION_KRON")
    @Column(name = "COD", nullable = false, precision = 19, scale = 0)
    @NotNull
	public Long getCod() {
		return cod;
	}
	
	public void setCod( Long cod ) {
		this.cod = cod;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COD_CONSUMO", nullable = false)
    @NotNull
	public Consumo getConsumo() {
		return consumo;
	}
	
	public void setConsumo( Consumo consumo ) {
		this.consumo = consumo;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COD_PUERTA", nullable = false)
    @NotNull
	public PuertaKron getPuertaKron() {
		return puertaKron;
	}
	
	public void setPuertaKron( PuertaKron puertaKron ) {
		this.puertaKron = puertaKron;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_INICIO", nullable = false, length = 7)
	@NotNull
	public Date getFechaInicio() {
		return fechaInicio;
	}
	
	public void setFechaInicio( Date fechaInicio ) {
		this.fechaInicio = fechaInicio;
	}		

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_FIN", nullable = false, length = 7)
	@NotNull
	public Date getFechaFin() {
		return fechaFin;
	}
	
	public void setFechaFin( Date fechaFin ) {
		this.fechaFin = fechaFin;
	}
	
	@Column(name = "ESTADO", nullable = false, length = 2)
	public String getEstado() {
		return estado;
	}
	
	public void setEstado( String estado ) {
		this.estado = estado;
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
		if ( this == obj )
			return true;
		if ( obj == null )
			return false;
		if ( !(obj instanceof ConsumoAutorizacionkron) )
			return false;
		ConsumoAutorizacionkron other = ( ConsumoAutorizacionkron ) obj;
		if ( cod == null ) {
			if ( other.getCod() != null )
				return false;
		} else if ( !cod.equals( other.getCod() ) )
			return false;
		return true;
	}

	

	
	
	
}
