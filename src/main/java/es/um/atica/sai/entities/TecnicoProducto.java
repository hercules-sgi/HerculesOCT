package es.um.atica.sai.entities;
// Generated 24-jun-2009 10:06:20 by Hibernate Tools 3.2.2.GA

import javax.persistence.CascadeType;
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

import org.hibernate.validator.constraints.Length;

/**
 * Tecnico generated by hbm2java
 */
@Entity
@Table(name = "TECNICO_PRODUCTO")
@SequenceGenerator(name = "SQ_TECNICO_PRODUCTO", sequenceName = "SQ_TECNICO_PRODUCTO", initialValue = 0, allocationSize = 1 )
@NamedQueries ( {
	@NamedQuery (name = TecnicoProducto.GET_TECNICOS_BY_SERVICIO, query = "select distinct tec from TecnicoProducto tec where tec.producto.servicio.cod=:servicio"),
	@NamedQuery (name = TecnicoProducto.EXISTE_TECNICO, query = "SELECT tec FROM TecnicoProducto tec WHERE tec.producto.cod=:codproducto AND tec.usuario.cod=:codusuario")
})
public class TecnicoProducto implements java.io.Serializable {

	private static final long serialVersionUID = 4143324257078629116L;

	public static final String GET_TECNICOS_BY_SERVICIO = "tecnicos.byServicio";
	public static final String EXISTE_TECNICO = "existeTecnico";

	private Long cod;
	private Producto producto;
	private Usuario usuario;
	private String automatico;
	private Integer prioridad;

	public TecnicoProducto() {
	}

	public TecnicoProducto(Producto producto, Usuario usuario, String automatico ) {
		this.producto = producto;
		this.usuario = usuario;
		this.automatico = automatico;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_TECNICO_PRODUCTO" )
	@Column(name = "COD", nullable = false, precision = 19, scale = 0 )
	@NotNull
	public Long getCod() {
		return cod;
	}

	public void setCod( Long cod ) {
		this.cod = cod;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn( name = "COD_PRODUCTO", nullable = false ) // , insertable = false, updatable = false)
	@NotNull

	public Producto getProducto() {
		return producto;
	}

	public void setProducto( Producto producto ) {
		this.producto = producto;
	}

	@ManyToOne( fetch = FetchType.EAGER, cascade = CascadeType.REFRESH )
	@JoinColumn( name = "COD_USUARIO", nullable = false ) // , insertable = false, updatable = false)
	@NotNull
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario( Usuario usuario ) {
		this.usuario = usuario;
	}

	@Column(name = "AUTOMATICO", nullable = false, length = 2)
	@NotNull
	@Length(max = 2)
	public String getAutomatico() {
		return automatico;
	}

	public void setAutomatico( String automatico ) {
		this.automatico = automatico;
	}

	@Column(name = "PRIORIDAD", precision = 2, scale = 0 )
	public Integer getPrioridad() {
		return prioridad;
	}
	
	public void setPrioridad( Integer prioridad ) {
		this.prioridad = prioridad;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = ( prime * result ) + ( ( automatico == null ) ? 0 : automatico.hashCode() );
		result = ( prime * result ) + ( ( producto == null ) ? 0 : producto.hashCode() );
		result = ( prime * result ) + ( ( usuario == null ) ? 0 : usuario.hashCode() );
		return result;
	}

	@Override
	public boolean equals( Object obj ) {
		if ( this == obj ) {
			return true;
		}
		if ( obj == null ) {
			return false;
		}
		if ( getClass() != obj.getClass() ) {
			return false;
		}
		final TecnicoProducto other = ( TecnicoProducto ) obj;
		if ( automatico == null ) {
			if ( other.automatico != null ) {
				return false;
			}
		} else if ( !automatico.equals( other.automatico ) ) {
			return false;
		}
		if ( producto == null ) {
			if ( other.producto != null ) {
				return false;
			}
		} else if ( !producto.equals( other.producto ) ) {
			return false;
		}
		if ( usuario == null ) {
			if ( other.usuario != null ) {
				return false;
			}
		} else if ( !usuario.equals( other.usuario ) ) {
			return false;
		}
		return true;
	}


}
