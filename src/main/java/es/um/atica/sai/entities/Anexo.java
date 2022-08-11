package es.um.atica.sai.entities;
// Generated 08-oct-2009 10:08:03 by Hibernate Tools 3.2.2.GA

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@NamedQueries( {
	@NamedQuery(name = Anexo.GET_ANEXOS_X_CONSUMO, query = "SELECT a FROM Anexo a WHERE a.consumo=:consumo" ),
	@NamedQuery(name = Anexo.GET_ANEXOS_BIOSEGURIDAD_X_CONSUMO, query = "SELECT a from Anexo a WHERE a.consumo=:consumo AND a.tipo='CONSUMO_BIOSEG'" ),
	@NamedQuery(name = Anexo.GET_ANEXOS_CONSUMO_X_CONSUMO, query = "SELECT a from Anexo a WHERE a.consumo=:consumo AND a.tipo='CONSUMO'" ),
	@NamedQuery(name = Anexo.GET_ANEXO_BY_TAG, query = "SELECT a FROM Anexo a WHERE a.tag=:tag" ),
} )
@Entity
@Table(name = "ANEXO")
@SequenceGenerator(name="SEQ_ANEXO",sequenceName="SAI_ANEXO",initialValue=0,allocationSize=1)
public class Anexo implements java.io.Serializable {

	private static final long serialVersionUID = 2071346691040837570L;

	public static final String GET_ANEXOS_X_CONSUMO = "ANEXO.GET_ANEXOS_X_CONSUMO";
	public static final String GET_ANEXOS_BIOSEGURIDAD_X_CONSUMO = "ANEXO.GET_ANEXOS_BIOSEGURIDAD_X_CONSUMO";
	public static final String GET_ANEXOS_CONSUMO_X_CONSUMO = "ANEXO.GET_ANEXOS_CONSUMO_X_CONSUMO";
	public static final String GET_ANEXO_BY_TAG = "ANEXO.GET_ANEXO_BY_TAG";

	private Long cod;
	private Consumo consumo;
	private Producto producto;
	private String tag;
	private String comentario;
	private String tipo;
	private byte[] documento;
	private String nomDocumento;

	public Anexo() {
	}

	public Anexo(Long cod, Consumo consumo, String tipo, Producto producto) {
		this.cod = cod;
		this.consumo = consumo;
		this.tipo = tipo;
		this.producto = producto;
	}
	public Anexo(Long cod, Consumo consumo, Producto producto, String comentario,
			String tipo, byte[] documento) {
		this.cod = cod;
		this.consumo = consumo;
		this.comentario = comentario;
		this.tipo = tipo;
		this.documento = documento;
		this.producto = producto;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_ANEXO")
	@Column(name = "COD", nullable = false, precision=19, scale = 0)
	public Long getCod() {
		return cod;
	}

	public void setCod(Long cod) {
		this.cod = cod;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COD_PRESTACION", nullable = true)
	public Consumo getConsumo() {
		return consumo;
	}

	public void setConsumo(Consumo consumo) {
		this.consumo = consumo;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "COD_PRODUCTO", nullable = true)
	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	@Column(name = "TAG", length = 30)
	public String getTag() {
		return tag;
	}
	
	public void setTag( String tag ) {
		this.tag = tag;
	}

	@Column(name = "COMENTARIO", length = 400)
	@Length(max = 400)
	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	@Column(name = "TIPO", nullable = false, length = 15)
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	@Lob
	@Column(name = "DOCUMENTO")
	@Basic(fetch=FetchType.LAZY)
	public byte[] getDocumento() {
		return documento;
	}

	public void setDocumento(byte[] documento) {
		this.documento = documento;
	}

	@Column(name = "NOM_DOCUMENTO", nullable = false)
	@NotNull
	public String getNomDocumento() {
		return nomDocumento;
	}

	public void setNomDocumento(String nomDocumento) {
		this.nomDocumento = nomDocumento;
	}


}
