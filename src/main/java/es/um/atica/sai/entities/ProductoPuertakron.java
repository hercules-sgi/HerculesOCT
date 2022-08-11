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
@SequenceGenerator(name="SEQ_PRODUCTO_PUERTA",sequenceName="SQ_PRODUCTO_PUERTA",allocationSize=1)
@Table(name = "PRODUCTO_PUERTA")
@NamedQueries ( {
    @NamedQuery (name = ProductoPuertakron.GET_PRODUCTO_PUERTAKRON_BY_PRODUCTO, query = "SELECT ppk FROM ProductoPuertakron ppk WHERE ppk.producto=:producto ORDER BY ppk.puertaKron.puertaKronView.nombreEdificio, ppk.puertaKron.puertaKronView.nombreLector"),
    @NamedQuery (name = ProductoPuertakron.EXISTE_PUERTAKRON, query = "SELECT COUNT(ppk) FROM ProductoPuertakron ppk WHERE ppk.producto=:producto AND ppk.puertaKron=:puertakron" ),
})
public class ProductoPuertakron implements java.io.Serializable {
	
	private static final long serialVersionUID = -4974098656537479565L;
	
	public static final String GET_PRODUCTO_PUERTAKRON_BY_PRODUCTO = "PRODUCTO_PUERTA.GET_PRODUCTO_PUERTAKRON_BY_PRODUCTO";
	public static final String EXISTE_PUERTAKRON = "PRODUCTO_PUERTA.EXISTE_PUERTAKRON";
	
	private Long cod;
	private Producto producto;
	private PuertaKron puertaKron;
	
 	public ProductoPuertakron() {
 		
 	}
 	
 	public ProductoPuertakron(Long cod, Producto producto, PuertaKron puerta) {
 		this.cod = cod;
 		this.producto = producto;
 		this.puertaKron = puerta;
 	}
	
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_PRODUCTO_PUERTA")
    @Column(name = "COD", nullable = false, precision = 19, scale = 0)
	public Long getCod() {
		return cod;
	}
	
	public void setCod( Long cod ) {
		this.cod = cod;
	}

	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COD_PRODUCTO", nullable = false)
	public Producto getProducto() {
		return producto;
	}
	
	public void setProducto( Producto producto ) {
		this.producto = producto;
	}
 	
 	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COD_PUERTA", nullable = false)
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
		if ( !( obj instanceof ProductoPuertakron ) ) {
			return false;
		}
		ProductoPuertakron other = ( ProductoPuertakron ) obj;
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
