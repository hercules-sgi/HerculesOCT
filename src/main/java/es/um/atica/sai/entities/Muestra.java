package es.um.atica.sai.entities;
// Generated 24-jun-2009 10:06:20 by Hibernate Tools 3.2.2.GA

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "MUESTRA")
@SequenceGenerator(name="SEQ_MUESTRA",sequenceName="SAI_MUESTRA",initialValue=0,allocationSize=1)
public class Muestra implements java.io.Serializable {

	private static final long serialVersionUID = 4908770029066063621L;

	private Long cod;
	private Consumo consumo;
	private String descripcion;
	private Date fechaEntrega;
	private String referencia;

	public Muestra() {
	}

	public Muestra(Long cod, Consumo consumo, String referencia) {
		this.cod = cod;
		this.consumo = consumo;
		this.referencia = referencia;
	}
	public Muestra(Long cod, Consumo consumo, String descripcion,
			Date fechaEntrega, String referencia) {
		this.cod = cod;
		this.consumo = consumo;
		this.descripcion = descripcion;
		this.fechaEntrega = fechaEntrega;
		this.referencia = referencia;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_MUESTRA")
	@Column(name = "COD",  nullable = false,precision=19, scale = 0)
	@NotNull
	public Long getCod() {
		return this.cod;
	}

	public void setCod(Long cod) {
		this.cod = cod;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COD_PRESTACION", nullable = false)
	@NotNull
	public Consumo getConsumo() {
		return this.consumo;
	}

	public void setConsumo(Consumo consumo) {
		this.consumo = consumo;
	}

	@Column(name = "DESCRIPCION", length = 200)
	@Length(max = 200)
	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_ENTREGA", length = 7)
	public Date getFechaEntrega() {
		return this.fechaEntrega;
	}

	public void setFechaEntrega(Date fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}

	@Column(name = "REFERENCIA", nullable = false, length = 10)
	@NotNull
	@Length(max = 10)
	public String getReferencia() {
		return this.referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

}
