package es.um.atica.sai.entities;

import java.math.BigDecimal;

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
import javax.persistence.UniqueConstraint;


@Entity
@Table( name = "PROYECTO_PRODUCTO", uniqueConstraints = @UniqueConstraint( columnNames = { "COD_PROYECTO", "COD_PRODUCTO"} ) )
@SequenceGenerator( name = "SQ_PROYECTO_PRODUCTO", sequenceName = "SQ_PROYECTO_PRODUCTO", initialValue = 0, allocationSize = 1 )
@NamedQueries ( {
    @NamedQuery(name = ProyectoProducto.EXISTE_PROYECTO_PRODUCTO, query="SELECT COUNT(pp) FROM ProyectoProducto pp WHERE pp.proyecto=:proyecto AND pp.producto=:producto"),
    @NamedQuery(name = ProyectoProducto.GET_PRODUCTOS_PROYECTO, query="SELECT pp FROM ProyectoProducto pp WHERE pp.proyecto=:proyecto"),
    @NamedQuery(name = ProyectoProducto.GET_PROYECTOPRODUCTO_X_PROYECTO_PRODUCTO, query="SELECT pp FROM ProyectoProducto pp WHERE pp.proyecto=:proyecto AND pp.producto=:producto"),
})

public class ProyectoProducto implements java.io.Serializable {

	private static final long serialVersionUID = 6725020057141046320L;

	public static final String EXISTE_PROYECTO_PRODUCTO = "PROYECTO_PRODUCTO.EXISTE_PROYECTO_PRODUCTO";
	public static final String GET_PRODUCTOS_PROYECTO = "PROYECTO_PRODUCTO.GET_PRODUCTOS_PROYECTO";
	public static final String GET_PROYECTOPRODUCTO_X_PROYECTO_PRODUCTO = "PROYECTO_PRODUCTO.GET_PROYECTOPRODUCTO_X_PROYECTO_PRODUCTO";
	
	private Long cod;
	private Proyecto proyecto;
	private Producto producto;
	private BigDecimal cantidad;
	
	public ProyectoProducto() {
		super();
	}

	@Id
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "SQ_PROYECTO_PRODUCTO" )
	@Column( name = "COD", unique = true, nullable = false, scale = 0 )
	public Long getCod() {
		return cod;
	}

	public void setCod( Long cod ) {
		this.cod = cod;
	}

	@ManyToOne( fetch = FetchType.EAGER )
	@JoinColumn( name = "COD_PROYECTO", nullable = false )
	public Proyecto getProyecto() {
		return proyecto;
	}
	
	public void setProyecto( Proyecto proyecto ) {
		this.proyecto = proyecto;
	}

	@ManyToOne( fetch = FetchType.EAGER )
	@JoinColumn( name = "COD_PRODUCTO", nullable = false )
	public Producto getProducto() {
		return producto;
	}

	public void setProducto( Producto producto ) {
		this.producto = producto;
	}

	@Column(name = "CANTIDAD", nullable = false, precision = 10, scale = 2)
	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad( BigDecimal cantidad ) {
		this.cantidad = cantidad;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( cantidad == null ) ? 0 : cantidad.hashCode() );
		result = prime * result + ( ( cod == null ) ? 0 : cod.hashCode() );
		result = prime * result + ( ( producto == null ) ? 0 : producto.hashCode() );
		result = prime * result + ( ( proyecto == null ) ? 0 : proyecto.hashCode() );
		return result;
	}


	@Override
	public boolean equals( Object obj ) {
		if ( this == obj )
			return true;
		if ( !( obj instanceof ProyectoProducto ) )
			return false;
		ProyectoProducto other = ( ProyectoProducto ) obj;
		if ( cantidad == null ) {
			if ( other.getCantidad() != null )
				return false;
		} else if ( !cantidad.equals( other.getCantidad() ) )
			return false;
		if ( cod == null ) {
			if ( other.getCod() != null )
				return false;
		} else if ( !cod.equals( other.getCod() ) )
			return false;
		if ( producto == null ) {
			if ( other.getProducto() != null )
				return false;
		} else if ( !producto.equals( other.getProducto() ) )
			return false;
		if ( proyecto == null ) {
			if ( other.getProyecto() != null )
				return false;
		} else if ( !proyecto.equals( other.getProyecto() ) )
			return false;
		return true;
	}
	
	

}
