package es.um.atica.sai.entities;
// Generated 24-jun-2009 10:06:20 by Hibernate Tools 3.2.2.GA

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


@Entity
@Table(name = "PROYECTO")
@SequenceGenerator(name="SQ_PROYECTO",sequenceName="SQ_PROYECTO",initialValue=0,allocationSize=1)
@NamedQueries ( {
    @NamedQuery(name = Proyecto.GET_LISTA_PROYECTOS, query="SELECT pro FROM Proyecto pro"),
    @NamedQuery(name = Proyecto.GET_LISTA_PROYECTOS_X_LISTASERVICIOS, query="SELECT pro FROM Proyecto pro WHERE pro.servicio IN (:listaservicios)"),
    @NamedQuery(name = Proyecto.GET_LISTA_PROYECTOS_X_LISTAIR, query="SELECT pro FROM Proyecto pro WHERE pro.usuarioIr IN (:listairs)"),
    @NamedQuery(name = Proyecto.GET_LISTA_PROYECTOS_X_LISTASERVICIOS_OR_LISTAIR, query="SELECT pro FROM Proyecto pro WHERE (pro.servicio IN (:listaservicios) OR pro.usuarioIr IN (:listairs))"),
    @NamedQuery(name = Proyecto.GET_LISTA_PROYECTOS_X_PRODUCTO_IR, query="SELECT pp.proyecto FROM ProyectoProducto pp WHERE pp.producto=:producto AND pp.proyecto.usuarioIr=:usuarioir AND (pp.proyecto.fechaCaducidad IS NULL OR pp.proyecto.fechaCaducidad>:fechaactual) ORDER BY pp.proyecto.nombre"),

})

public class Proyecto implements java.io.Serializable {

	private static final long serialVersionUID = 7133946385200325522L;
	
	public static final String GET_LISTA_PROYECTOS = "PROYECTO.GET_LISTA_PROYECTOS";
	public static final String GET_LISTA_PROYECTOS_X_LISTASERVICIOS = "PROYECTO.GET_LISTA_PROYECTOS_X_LISTASERVICIOS";
	public static final String GET_LISTA_PROYECTOS_X_LISTAIR = "PROYECTO.GET_LISTA_PROYECTOS_X_LISTAIR";
	public static final String GET_LISTA_PROYECTOS_X_LISTASERVICIOS_OR_LISTAIR = "PROYECTO.GET_LISTA_PROYECTOS_X_LISTASERVICIOSSUPERVISOR_O_LISTAIR";
	public static final String GET_LISTA_PROYECTOS_X_PRODUCTO_IR = "PROYECTO.GET_LISTA_PROYECTOS_X_PRODUCTO_IR";

	private Long cod;
	private String nombre;
	private Servicio servicio;
	private Usuario usuarioIr;
	private Date fechaAlta;
	private Date fechaCaducidad;
	private List<ProyectoProducto> listaProductos;
	private List<Consumo> listaConsumos;
	
    public Proyecto() {
    	super();
	}

	public Proyecto( Long cod, String nombre, Servicio servicio, Usuario usuarioIr, Date fechaAlta,
			Date fechaCaducidad ) {
		super();
		this.cod = cod;
		this.nombre = nombre;
		this.servicio = servicio;
		this.usuarioIr = usuarioIr;
		this.fechaAlta = fechaAlta;
		this.fechaCaducidad = fechaCaducidad;
	}

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SQ_PROYECTO")
    @Column(name = "COD", nullable = false, precision = 19, scale = 0)
	public Long getCod() {
		return cod;
	}
	
	public void setCod( Long cod ) {
		this.cod = cod;
	}

    @Column(name = "NOMBRE", nullable = false, length = 100)
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre( String nombre ) {
		this.nombre = nombre;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COD_SERVICIO", nullable = false)
	public Servicio getServicio() {
		return servicio;
	}
	
	public void setServicio( Servicio servicio ) {
		this.servicio = servicio;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COD_USUARIO_IR", nullable = true)
	public Usuario getUsuarioIr() {
		return usuarioIr;
	}
	
	public void setUsuarioIr( Usuario usuarioIr ) {
		this.usuarioIr = usuarioIr;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_ALTA", nullable = false, length = 7)
	public Date getFechaAlta() {
		return fechaAlta;
	}
	
	public void setFechaAlta( Date fechaAlta ) {
		this.fechaAlta = fechaAlta;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_CADUCIDAD", nullable = false, length = 7)
	public Date getFechaCaducidad() {
		return fechaCaducidad;
	}
	
	public void setFechaCaducidad( Date fechaCaducidad ) {
		this.fechaCaducidad = fechaCaducidad;
	}
	
	@OneToMany( fetch = FetchType.LAZY, mappedBy = "proyecto" )
	public List<ProyectoProducto> getListaProductos() {
		return listaProductos;
	}

	public void setListaProductos( List<ProyectoProducto> listaProductos ) {
		this.listaProductos = listaProductos;
	}

	@OneToMany( fetch = FetchType.LAZY, mappedBy = "proyecto" )
	public List<Consumo> getListaConsumos() {
		return listaConsumos;
	}
	
	public void setListaConsumos( List<Consumo> listaConsumos ) {
		this.listaConsumos = listaConsumos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( cod == null ) ? 0 : cod.hashCode() );
		result = prime * result + ( ( fechaAlta == null ) ? 0 : fechaAlta.hashCode() );
		result = prime * result + ( ( fechaCaducidad == null ) ? 0 : fechaCaducidad.hashCode() );
		result = prime * result + ( ( nombre == null ) ? 0 : nombre.hashCode() );
		result = prime * result + ( ( servicio == null ) ? 0 : servicio.hashCode() );
		result = prime * result + ( ( usuarioIr == null ) ? 0 : usuarioIr.hashCode() );
		return result;
	}

	@Override
	public boolean equals( Object obj ) {
		if ( this == obj )
			return true;
		if ( !( obj instanceof Proyecto ) )
			return false;
		Proyecto other = ( Proyecto ) obj;
		if ( cod == null ) {
			if ( other.getCod() != null )
				return false;
		} else if ( !cod.equals( other.getCod() ) )
			return false;
		if ( fechaAlta == null ) {
			if ( other.getFechaAlta() != null )
				return false;
		} else if ( !fechaAlta.equals( other.getFechaAlta() ) )
			return false;
		if ( fechaCaducidad == null ) {
			if ( other.getFechaCaducidad() != null )
				return false;
		} else if ( !fechaCaducidad.equals( other.getFechaCaducidad() ) )
			return false;
		if ( nombre == null ) {
			if ( other.getNombre() != null )
				return false;
		} else if ( !nombre.equals( other.getNombre() ) )
			return false;
		if ( servicio == null ) {
			if ( other.getServicio() != null )
				return false;
		} else if ( !servicio.equals( other.getServicio() ) )
			return false;
		if ( usuarioIr == null ) {
			if ( other.getUsuarioIr() != null )
				return false;
		} else if ( !usuarioIr.equals( other.getUsuarioIr() ) )
			return false;
		return true;
	}

	
	
}
