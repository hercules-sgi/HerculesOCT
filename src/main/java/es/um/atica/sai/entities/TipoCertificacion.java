package es.um.atica.sai.entities;
// Generated 24-mar-2021 8:40:48 by Hibernate Tools 4.3.5.Final

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * TipoCertificacion generated by hbm2java
 */
@Entity
@Table( name = "TIPO_CERTIFICACION", uniqueConstraints = @UniqueConstraint( columnNames = "NOMBRE" ) )
@NamedQueries( {
		@NamedQuery(	name = TipoCertificacion.GET_LISTA_TIPOSCERTIFICACIONES,
						query = "SELECT t FROM TipoCertificacion t WHERE t.fechaBaja IS NULL ORDER BY t.nombre asc" ),
		@NamedQuery(	name = TipoCertificacion.GET_LISTA_TIPOSCERTIFICACIONES_X_PRODUCTO,
						query = "SELECT ptc.tipoCertificacion FROM ProductoTipoCertificacion ptc WHERE ptc.producto=:producto AND ptc.tipoCertificacion.fechaBaja IS NULL ORDER BY ptc.tipoCertificacion.nombre asc" ),
		@NamedQuery(	name = TipoCertificacion.EXISTE_TIPOCERTIFICACION,
						query = "SELECT COUNT( t ) FROM TipoCertificacion t WHERE t.nombre=:nombre" ),
		@NamedQuery(	name = TipoCertificacion.EXISTE_OTRO_TIPO_CERTIFICACION,
						query = "SELECT COUNT( t ) FROM TipoCertificacion t WHERE t.nombre=:nombre AND t.cod<>:codigo" ),
} )
@SequenceGenerator( name = "SQ_TIPO_CERTIFICACION",
					sequenceName = "SQ_TIPO_CERTIFICACION",
					initialValue = 0,
					allocationSize = 1 )
public class TipoCertificacion implements java.io.Serializable {

	private static final long serialVersionUID = 30261195577158115L;

	public static final String GET_LISTA_TIPOSCERTIFICACIONES = "TIPO_CERTIFICACION.GET_LISTA_TIPOSCERTIFICACIONES";
	public static final String GET_LISTA_TIPOSCERTIFICACIONES_X_PRODUCTO = "TIPO_CERTIFICACION.GET_LISTA_TIPOSCERTIFICACIONES_X_PRODUCTO";
	public static final String EXISTE_TIPOCERTIFICACION = "TIPO_CERTIFICACION.EXISTE_TIPOCERTIFICACION";
	public static final String EXISTE_OTRO_TIPO_CERTIFICACION = "TIPO_CERTIFICACION.EXISTE_OTRO_TIPO_CERTIFICACION";

	private Long cod;
	private String nombre;
	private String caduca;
	private Date fechaBaja;
	private List<ProductoTipoCertificacion> listaProductos;
	private List<Certificacion> listaCertificaciones;

	public TipoCertificacion() {}

	public TipoCertificacion( Long cod, String nombre, String caduca, List<ProductoTipoCertificacion> listaProductos,
			List<Certificacion> listaCertificaciones ) {
		this.cod = cod;
		this.nombre = nombre;
		this.caduca = caduca;
		this.listaProductos = listaProductos;
		setListaCertificaciones( listaCertificaciones );
	}

	public TipoCertificacion( Long cod, String nombre, String caduca, Date fechaBaja ) {
		this.cod = cod;
		this.nombre = nombre;
		this.caduca = caduca;
		this.fechaBaja = fechaBaja;
	}

	@Id
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "SQ_TIPO_CERTIFICACION" )
	@Column( name = "COD", unique = true, nullable = false, scale = 0 )
	public Long getCod() {
		return cod;
	}

	public void setCod( Long cod ) {
		this.cod = cod;
	}

	@Column( name = "NOMBRE", unique = true, nullable = false, length = 100 )
	public String getNombre() {
		return nombre;
	}

	public void setNombre( String nombre ) {
		this.nombre = nombre;
	}

	@Column( name = "CADUCA", nullable = false, length = 2 )
	public String getCaduca() {
		return caduca;
	}

	public void setCaduca( String caduca ) {
		this.caduca = caduca;
	}

	@Temporal( TemporalType.DATE )
	@Column( name = "FECHA_BAJA", length = 7 )
	public Date getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja( Date fechaBaja ) {
		this.fechaBaja = fechaBaja;
	}

	@OneToMany( fetch = FetchType.LAZY, mappedBy = "tipoCertificacion" )
	public List<ProductoTipoCertificacion> getListaProductos() {
		return listaProductos;
	}

	public void setListaProductos( List<ProductoTipoCertificacion> listaProductos ) {
		this.listaProductos = listaProductos;
	}

	@OneToMany( fetch = FetchType.LAZY, mappedBy = "tipoCertificacion" )
	public List<Certificacion> getListaCertificaciones() {
		return listaCertificaciones;
	}

	public void setListaCertificaciones( List<Certificacion> listaCertificaciones ) {
		this.listaCertificaciones = listaCertificaciones;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = ( prime * result ) + ( ( cod == null ) ? 0 : cod.hashCode() );
		return result;
	}

	@Override
	public boolean equals( Object obj ) {
		if ( this == obj ) {
			return true;
		}
		if ( !( obj instanceof TipoCertificacion ) ) {
			return false;
		}
		final TipoCertificacion other = ( TipoCertificacion ) obj;
		if ( getCod() == null ) {
			if ( other.getCod() != null ) {
				return false;
			}
		} else if ( !getCod().equals( other.getCod() ) ) {
			return false;
		}
		return true;
	}

}
