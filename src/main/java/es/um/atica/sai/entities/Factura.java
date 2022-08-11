package es.um.atica.sai.entities;
// Generated 24-jun-2009 10:06:20 by Hibernate Tools 3.2.2.GA

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "FACTURA")
@SequenceGenerator(name="SEQ_FACTURA",sequenceName="SAI_FACTURA",initialValue=0,allocationSize=1)
@NamedQueries ( {
    @NamedQuery (name = Factura.GET_FACTURAS, query = "SELECT fac FROM Factura fac"),
})

public class Factura implements java.io.Serializable {

	private static final long serialVersionUID = 2225070046709480135L;

	public static final String GET_FACTURAS = "FACTURA.GET_FACTURAS";

	private Long cod;
	private EntidadPagadora entidadPagadora;
	private Date fechaGeneracion;
	private String concepto;
	private String anoJusto;
	private String numeroJusto;
	private String partida;
	private String serie;
	private String tipoGasto;
	private Servicio servicio;
	private Usuario investigador;
	
	private List<Consumo> consumos = new ArrayList<>(0);

	public Factura() {
	}

	public Factura(Long cod, EntidadPagadora entidadPagadora,
			Date fechaGeneracion, String concepto, String anoJusto,
			String partida, String serie, String tipoGasto,
			String numeroJusto, List<Consumo> consumos,Servicio servicio,Usuario investigador) {
		this.cod = cod;
		this.entidadPagadora = entidadPagadora;
		this.fechaGeneracion = fechaGeneracion;
		this.concepto = concepto;
		this.anoJusto = anoJusto;
		this.numeroJusto = numeroJusto;
		this.consumos = consumos;
		this.partida = partida;
		this.serie = serie;
		this.tipoGasto = tipoGasto;
		this.servicio=servicio;
		this.investigador=investigador;
		
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_FACTURA")
	@Column(name = "COD", nullable = false,precision=19, scale = 0)
	public Long getCod() {
		return this.cod;
	}

	public void setCod(Long cod) {
		this.cod = cod;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COD_ENTIDAD_PAGADORA", nullable = false)
	public EntidadPagadora getEntidadPagadora() {
		return this.entidadPagadora;
	}

	public void setEntidadPagadora(EntidadPagadora entidadPagadora) {
		this.entidadPagadora = entidadPagadora;
	}
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_GENERACION", nullable = false, length = 7)
	public Date getFechaGeneracion() {
		return this.fechaGeneracion;
	}

	public void setFechaGeneracion(Date fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}
	
	@Column(name = "CONCEPTO", length = 4000)
	@Length(max = 4000)
	public String getConcepto() {
		return this.concepto;
	}

	public void setConcepto(String c) {
		this.concepto = c;
	}
	
	@Column(name = "ANO_JUSTO", length = 4)
	@Length(max = 4)
	public String getAnoJusto() {
		return this.anoJusto;
	}

	public void setAnoJusto(String aj) {
		this.anoJusto = aj;
	}
	
	@Column(name = "NUMERO_JUSTO", length = 10)
	@Length(max = 10)
	public String getNumeroJusto() {
		return this.numeroJusto;
	}

	public void setNumeroJusto(String nj) {
		this.numeroJusto = nj;
	}
	
	@Column(name = "PARTIDA", length = 50)
	@Length(max = 50)
	public String getPartida() {
		return this.partida;
	}

	public void setPartida(String nj) {
		this.partida = nj;
	}
	
	@Column(name = "SERIE", length = 2)
	@Length(max = 2)
	public String getSerie() {
		return this.serie;
	}

	public void setSerie(String nj) {
		this.serie = nj;
	}
	
	@Column(name = "TIPO_GASTO", length = 2)
	@Length(max = 2)
	public String getTipoGasto() {
		return this.tipoGasto;
	}

	public void setTipoGasto(String nj) {
		this.tipoGasto = nj;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COD_SERVICIO", nullable = false)
	public Servicio getServicio() {
		return this.servicio;
	}

	public void setServicio(Servicio servicio) {
		this.servicio = servicio;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COD_INVESTIGADOR", nullable = false)
	public Usuario getInvestigador() {
		return this.investigador;
	}

	public void setInvestigador(Usuario investigador) {
		this.investigador = investigador;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "factura")
	public List<Consumo> getConsumos() {
		return this.consumos;
	}

	public void setConsumos(List<Consumo> consumos) {
		this.consumos = consumos;
	}
	
	@Transient
	public String getDniInvestigador(){
	    if (this.getInvestigador()!=null)
	    	return this.getInvestigador().getDni();
	    
	    return null;
	}
	
	@Transient
	public int getNumLineas(){
		return this.consumos.size();
	}
	
}
