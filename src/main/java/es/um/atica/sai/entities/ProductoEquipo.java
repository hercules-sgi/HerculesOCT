package es.um.atica.sai.entities;
// Generated 15-jun-2021 9:48:31 by Hibernate Tools 4.3.5.Final

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
@Table( name = "PRODUCTO_EQUIPO" )
@SequenceGenerator( name = "SEQ_PRODUCTO_EQUIPO", sequenceName = "SQ_PRODUCTO_EQUIPO", initialValue = 0, allocationSize = 1 )

@NamedQueries ( {
	@NamedQuery(name = ProductoEquipo.GET_PRODUCTOEQUIPOS_BY_PRODUCTO, query = "SELECT pe FROM ProductoEquipo pe WHERE pe.producto=:producto AND pe.equipo.estado='ALTA' ORDER BY pe.equipo.descripcion"),
	@NamedQuery(name = ProductoEquipo.GET_PRODUCTOEQUIPO_BY_PRODUCTO_EQUIPO, query = "SELECT pe FROM ProductoEquipo pe WHERE pe.producto=:producto AND pe.equipo=:equipo"),
	@NamedQuery(name = ProductoEquipo.GET_EQUIPOS_BY_PRODUCTO, query = "SELECT pe.equipo FROM ProductoEquipo pe WHERE pe.producto=:producto AND pe.equipo.estado='ALTA' ORDER BY pe.equipo.descripcion"),
	@NamedQuery(name = ProductoEquipo.EXISTE_PRODUCTOEQUIPO, query = "SELECT COUNT(pe) FROM ProductoEquipo pe WHERE pe.producto=:producto AND pe.equipo=:equipo"),
})
public class ProductoEquipo implements java.io.Serializable {

	private static final long serialVersionUID = 917584997873977733L;
	
	public static final String GET_PRODUCTOEQUIPOS_BY_PRODUCTO = "PRODUCTO_EQUIPO.GET_PRODUCTOEQUIPOS_BY_PRODUCTO";
	public static final String GET_PRODUCTOEQUIPO_BY_PRODUCTO_EQUIPO = "PRODUCTO_EQUIPO.GET_PRODUCTOEQUIPO_BY_PRODUCTO_EQUIPO";
	public static final String GET_EQUIPOS_BY_PRODUCTO = "PRODUCTO_EQUIPO.GET_EQUIPOS_BY_PRODUCTO";
	public static final String EXISTE_PRODUCTOEQUIPO = "PRODUCTO_EQUIPO.EXISTE_PRODUCTOEQUIPO";
	
	private Long cod;
	private Producto producto;
	private Equipo equipo;
	private Integer minutos;

	public ProductoEquipo() {}

	public ProductoEquipo( Long cod, Producto producto, Equipo equipo, Integer minutos ) {
		this.cod = cod;
		this.producto = producto;
		this.equipo = equipo;
		this.minutos = minutos;
	}

	@Id
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "SEQ_PRODUCTO_EQUIPO" )
	@Column( name = "COD", unique = true, nullable = false, scale = 0 )
	public Long getCod() {
		return cod;
	}

	public void setCod( Long cod ) {
		this.cod = cod;
	}

	@ManyToOne( fetch = FetchType.EAGER )
	@JoinColumn( name = "COD_PRODUCTO", nullable = false )
	public Producto getProducto() {
		return producto;
	}

	public void setProducto( Producto producto ) {
		this.producto = producto;
	}

	@ManyToOne( fetch = FetchType.EAGER )
	@JoinColumn( name = "COD_EQUIPO", nullable = false )
	public Equipo getEquipo() {
		return equipo;
	}

	public void setEquipo( Equipo equipo ) {
		this.equipo = equipo;
	}

	@Column( name = "MINUTOS", nullable = false, precision = 5, scale = 0 )
	public Integer getMinutos() {
		return minutos;
	}

	public void setMinutos( Integer minutos ) {
		this.minutos = minutos;
	}

}
