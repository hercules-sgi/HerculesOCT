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
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;


@Entity
@Table(name = "RESERVABLE")
@SequenceGenerator(name="SEQ_RESERVABLE",sequenceName="SAI_RESERVABLE",initialValue=0,allocationSize=1)
@NamedQueries ( {
	@NamedQuery(name = Equipo.GET_RESERVABLE_BY_TIPO, query = "SELECT res FROM Equipo res WHERE res.estado='ALTA' AND res.tipoReservable=:tipo ORDER BY res.descripcion" ),
	@NamedQuery(name = Equipo.GET_EQUIPOS_BY_SERVICIO, query = "SELECT eq FROM Equipo eq WHERE eq.servicio=:servicio AND eq.estado='ALTA' ORDER BY eq.descripcion" ),
	@NamedQuery(	name = Equipo.EXISTE_EQUIPO,
						query = "SELECT eq FROM Equipo eq WHERE eq.descripcion=:desc AND eq.estado='ALTA' AND eq.servicio=:servicio " ),
	@NamedQuery(	name = Equipo.EXISTE_OTRO_EQUIPO,
						query = "SELECT eq FROM Equipo eq WHERE eq.descripcion=:desc AND eq.estado='ALTA' AND eq.cod<>:codigo AND eq.servicio=:servicio" ),
})

public class Equipo implements java.io.Serializable {

	private static final long serialVersionUID = 8165837844053336999L;

	public static final String GET_RESERVABLE_BY_TIPO = "RESERVABLE.GET_RESERVABLE_BY_TIPO";
	public static final String GET_EQUIPOS_BY_SERVICIO = "RESERVABLE.GET_EQUIPOS_BY_SERVICIO";
	public static final String EXISTE_EQUIPO = "RESERVABLE.EXISTE_EQUIPO";
	public static final String EXISTE_OTRO_EQUIPO = "RESERVABLE.EXISTE_OTRO_EQUIPO";

	private Long cod;
	private TipoReservable tipoReservable;
	private String descripcion;
	private String estado;
	private Servicio servicio;
	private Date fechaCompra;
	private Date fechaBaja;
	private String tipoDep;
	private String codigoDep;
	private String bloqueDep;
	private String plantaDep;
	private Integer numDep;
	private List<Reservas> reservases = new ArrayList<>(0);
	private List<ConsumoEquipo> listaConsumos;
	private List<ProductoEquipo> listaProductos;
	private List<Intervencion> listaIntervenciones;

	public Equipo() {
		// constructor por defecto
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_RESERVABLE")
	@Column(name = "COD", nullable = false, precision = 19, scale = 0)
	public Long getCod() {
		return cod;
	}

	public void setCod( Long cod ) {
		this.cod = cod;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COD_TIPO_RESERVABLE")
	public TipoReservable getTipoReservable() {
		return tipoReservable;
	}

	public void setTipoReservable( TipoReservable tipoReservable ) {
		this.tipoReservable = tipoReservable;
	}

	@Column(name = "DESCRIPCION", nullable = false, length = 100)
	@NotNull
	@Length(max = 100)
	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion( String descripcion ) {
		this.descripcion = descripcion;
	}

	@Column(name = "ESTADO", nullable = false, length = 10)
	@NotNull
	@Length(max = 10)
	public String getEstado() {
		return estado;
	}

	public void setEstado( String estado ) {
		this.estado = estado;
	}

	@ManyToOne( fetch = FetchType.EAGER )
	@JoinColumn( name = "COD_SERVICIO", nullable = false )
	@NotNull
	public Servicio getServicio() {
		return servicio;
	}

	public void setServicio( Servicio servicio ) {
		this.servicio = servicio;
	}

	@Temporal( TemporalType.DATE )
	@Column( name = "FECHA_COMPRA", length = 7 )
	public Date getFechaCompra() {
		return fechaCompra;
	}

	public void setFechaCompra( Date fechaCompra ) {
		this.fechaCompra = fechaCompra;
	}

	@Temporal( TemporalType.DATE )
	@Column( name = "FECHA_BAJA", length = 7 )
	public Date getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja( Date fechaBaja ) {
		this.fechaBaja = fechaBaja;
	}

	@Column( name = "EXT_ESPACIO_TIPO", length = 2 )
	@Length( max = 2 )
	public String getTipoDep() {
		return tipoDep;
	}

	public void setTipoDep( String tipoDep ) {
		this.tipoDep = tipoDep;
	}

	@Column( name = "EXT_ESPACIO_CODIGO", length = 3 )
	@Length( max = 3 )
	public String getCodigoDep() {
		return codigoDep;
	}

	public void setCodigoDep( String codigoDep ) {
		this.codigoDep = codigoDep;
	}

	@Column( name = "EXT_ESPACIO_BLOQUE", length = 2 )
	@Length( max = 2 )
	public String getBloqueDep() {
		return bloqueDep;
	}

	public void setBloqueDep( String bloqueDep ) {
		this.bloqueDep = bloqueDep;
	}

	@Column( name = "EXT_ESPACIO_PLANTA", length = 8 )
	@Length( max = 8 )
	public String getPlantaDep() {
		return plantaDep;
	}

	public void setPlantaDep( String plantaDep ) {
		this.plantaDep = plantaDep;
	}

	@Column( name = "EXT_ESPACIO_DEPENDENCIA", precision = 5, scale = 0 )
	public Integer getNumDep() {
		return numDep;
	}

	public void setNumDep( Integer numDep ) {
		this.numDep = numDep;
	}

	@OneToMany( fetch = FetchType.LAZY, mappedBy = "reservable" )
	public List<Reservas> getReservases() {
		return reservases;
	}

	public void setReservases( List<Reservas> reservases ) {
		this.reservases = reservases;
	}

	@OneToMany( fetch = FetchType.LAZY, mappedBy = "equipo" )
	public List<ConsumoEquipo> getListaConsumos() {
		return listaConsumos;
	}

	public void setListaConsumos( List<ConsumoEquipo> listaConsumos ) {
		this.listaConsumos = listaConsumos;
	}

	@OneToMany( fetch = FetchType.LAZY, mappedBy = "equipo" )
	public List<ProductoEquipo> getListaProductos() {
		return listaProductos;
	}

	public void setListaProductos( List<ProductoEquipo> listaProductos ) {
		this.listaProductos = listaProductos;
	}

	@OneToMany( fetch = FetchType.LAZY, mappedBy = "equipo" )
	public List<Intervencion> getListaIntervenciones() {
		return listaIntervenciones;
	}

	public void setListaIntervenciones( List<Intervencion> listaIntervenciones ) {
		this.listaIntervenciones = listaIntervenciones;
	}

	@Transient
	public Boolean getActive(){
		return "ALTA".equalsIgnoreCase(getEstado());
	}

	@Transient
	public void setActive(Boolean active){
		if (active != null){
			if (active) {
				setEstado("ALTA");
			} else {
				setEstado("BAJA");
			}
		}
	}

	public Equipo changeStatus() {
		setActive( ! getActive() );

		return this;
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
		if ( !( obj instanceof Equipo ) ) {
			return false;
		}
		final Equipo other = ( Equipo ) obj;
		if ( cod == null ) {
			if ( other.getCod() != null ) {
				return false;
			}
		} else if ( !cod.equals( other.getCod() ) ) {
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return String.valueOf(getCod());
	}
}
