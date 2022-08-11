package es.um.atica.sai.entities;
// Generated 24-jun-2009 10:06:20 by Hibernate Tools 3.2.2.GA

import java.util.ArrayList;
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
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;


@Entity
@Table(name = "TIPO_RESERVABLE")
@SequenceGenerator(name="SEQ_TIPORES",sequenceName="SAI_TIPORES",initialValue=0,allocationSize=1)
@NamedQueries({
	@NamedQuery(name = TipoReservable.GET_TIPOSRESERVABLE, query = "SELECT tr FROM TipoReservable tr WHERE tr.estado='ALTA' ORDER BY tr.descripcion" ),
	@NamedQuery(name = TipoReservable.GET_TIPOSRESERVABLE_X_SERVICIO, query = "SELECT tr FROM TipoReservable tr WHERE tr.servicio=:servicio AND tr.estado='ALTA' ORDER BY tr.descripcion" ),
	@NamedQuery(name = TipoReservable.GET_TIPOSRESERVABLE_X_LISTASERVICIOS, query = "SELECT tr FROM TipoReservable tr WHERE tr.servicio IN (:listaservicios) AND tr.estado='ALTA' ORDER BY tr.descripcion" ),
	@NamedQuery(name = TipoReservable.GET_TIPOSRESERVABLE_X_TIPOHORARIO, query = "SELECT DISTINCT rh.tipoReservable FROM ReservableHorario rh WHERE rh.tipoHorario=:tipohorario AND rh.tipoReservable.estado='ALTA'"),
	@NamedQuery(name = TipoReservable.GET_TIPOSRESERVABLE_X_TIPOHORARIO_LISTASERVICIOS, query = "SELECT DISTINCT rh.tipoReservable FROM ReservableHorario rh WHERE rh.tipoHorario=:tipohorario AND rh.tipoReservable.estado='ALTA' AND rh.tipoReservable.servicio IN (:listaservicios)")
})
public class TipoReservable implements java.io.Serializable {

	private static final long serialVersionUID = -5830796582872502033L;

	public static final String GET_TIPOSRESERVABLE = "TIPO_RESERVABLE.GET_TIPOSRESERVABLE";
	public static final String GET_TIPOSRESERVABLE_X_SERVICIO = "TIPO_RESERVABLE.GET_TIPOSRESERVABLE_X_SERVICIO";
	public static final String GET_TIPOSRESERVABLE_X_LISTASERVICIOS = "TIPO_RESERVABLE.GET_TIPOSRESERVABLE_X_LISTASERVICIOS";
	public static final String GET_TIPOSRESERVABLE_X_TIPOHORARIO = "TIPO_RESERVABLE.GET_TIPOSRESERVABLE_X_TIPOHORARIO";
	public static final String GET_TIPOSRESERVABLE_X_TIPOHORARIO_LISTASERVICIOS = "TIPO_RESERVABLE.GET_TIPOSRESERVABLE_X_TIPOHORARIO_LISTASERVICIOS";


	private Long cod;
	private Servicio servicio;
	private String descripcion;
	private Integer duracionMinima;
	private Integer diasAntelacion;
	private Integer horasAntelacionMinima;
	private Integer vistaMaxima;
	private Integer horasAnulacion;
	private String estado;
	private String reservaMultiple = "NO";
	private Integer minutosTecnico;
	private Integer minutosTecnicoBajo;
	private Integer minutosTecnicoMedio;
	private Integer minutosTecnicoAlto;

	private List<Equipo> reservables = new ArrayList<>(0);
	private List<Producto> productos = new ArrayList<>(0);
	private List<ReservableHorario> reservableHorarios = new ArrayList<>(0);

	public TipoReservable() {
	}

	public TipoReservable( Long cod, Servicio servicio, String descripcion, Integer duracionMinima,
			Integer diasAntelacion, Integer horasAntelacionMinima, Integer vistaMaxima, Integer horasAnulacion,
			String estado, String reservaMultiple, List<Equipo> reservables, List<Producto> productos,
			List<ReservableHorario> reservableHorarios ) {
		this.cod = cod;
		this.servicio = servicio;
		this.descripcion = descripcion;
		this.duracionMinima = duracionMinima;
		this.diasAntelacion = diasAntelacion;
		this.horasAntelacionMinima = horasAntelacionMinima;
		this.vistaMaxima = vistaMaxima;
		this.horasAnulacion = horasAnulacion;
		this.estado = estado;
		this.reservaMultiple = reservaMultiple;
		this.reservables = reservables;
		this.productos = productos;
		this.reservableHorarios = reservableHorarios;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_TIPORES")
	@Column(name = "COD", nullable = false, precision = 19, scale = 0)
	public Long getCod() {
		return cod;
	}

	public void setCod( Long cod ) {
		this.cod = cod;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SERVICIO", nullable = false)
	public Servicio getServicio() {
		return servicio;
	}

	public void setServicio( Servicio servicio ) {
		this.servicio = servicio;
	}

	@Column(name = "DESCRIPCION", length = 50)
	@Length(max = 50)
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion( String descripcion ) {
		this.descripcion = descripcion;
	}

	@Column(name = "DURACION_MINIMA", precision = 5, scale = 0)
	public Integer getDuracionMinima() {
		return duracionMinima;
	}

	public void setDuracionMinima( Integer duracionMinima ) {
		this.duracionMinima = duracionMinima;
	}

	@Column(name = "DIAS_ANTELACION", precision = 3, scale = 0)
	public Integer getDiasAntelacion() {
		return diasAntelacion;
	}

	public void setDiasAntelacion( Integer diasAntelacion ) {
		this.diasAntelacion = diasAntelacion;
	}

	@Column(name = "DIAS_ANTELACION_MINIMA", precision = 3, scale = 0)
	public Integer getHorasAntelacionMinima() {
		return horasAntelacionMinima;
	}

	public void setHorasAntelacionMinima( Integer horasAntelacionMinima ) {
		this.horasAntelacionMinima = horasAntelacionMinima;
	}

	@Column(name = "VISTA_MAXIMA", precision = 3, scale = 0)
	public Integer getVistaMaxima() {
		return vistaMaxima;
	}

	public void setVistaMaxima( Integer vistaMaxima ) {
		this.vistaMaxima = vistaMaxima;
	}

	@Column(name = "HORAS_ANULACION", precision = 3, scale = 0)
	public Integer getHorasAnulacion() {
		return horasAnulacion;
	}

	public void setHorasAnulacion( Integer horasAnulacion ) {
		this.horasAnulacion = horasAnulacion;
	}

	@Column(name = "ESTADO", length = 10)
	@Length(max = 10)
	public String getEstado() {
		return estado;
	}

	public void setEstado( String estado ) {
		this.estado = estado;
	}

	@Column(name = "RESERVA_MULTIPLE", length = 2, nullable = false)
	@Length(max = 2)
	public String getReservaMultiple() {
		return reservaMultiple;
	}

	public void setReservaMultiple( String reservaMultiple ) {
		this.reservaMultiple = reservaMultiple;
	}

	@Column(name = "MINUTOS_TECNICO", precision = 4, scale = 0)
	public Integer getMinutosTecnico() {
		return minutosTecnico;
	}

	public void setMinutosTecnico( Integer minutosTecnico ) {
		this.minutosTecnico = minutosTecnico;
	}

	@Column(name = "MINUTOS_TECNICO_BAJO", precision = 4, scale = 0)
	public Integer getMinutosTecnicoBajo() {
		return minutosTecnicoBajo;
	}

	public void setMinutosTecnicoBajo( Integer minutosTecnicoBajo ) {
		this.minutosTecnicoBajo = minutosTecnicoBajo;
	}

	@Column(name = "MINUTOS_TECNICO_MEDIO", precision = 4, scale = 0)
	public Integer getMinutosTecnicoMedio() {
		return minutosTecnicoMedio;
	}

	public void setMinutosTecnicoMedio( Integer minutosTecnicoMedio ) {
		this.minutosTecnicoMedio = minutosTecnicoMedio;
	}

	@Column(name = "MINUTOS_TECNICO_ALTO", precision = 4, scale = 0)
	public Integer getMinutosTecnicoAlto() {
		return minutosTecnicoAlto;
	}

	public void setMinutosTecnicoAlto( Integer minutosTecnicoAlto ) {
		this.minutosTecnicoAlto = minutosTecnicoAlto;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoReservable")
	@OrderBy("descripcion ASC")
	public List<Equipo> getReservables() {
		return reservables;
	}

	public void setReservables( List<Equipo> reservables ) {
		this.reservables = reservables;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoReservable")
	public List<Producto> getProductos() {
		return productos;
	}

	public void setProductos( List<Producto> productos ) {
		this.productos = productos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoReservable" )
	public List<ReservableHorario> getReservableHorarios() {
		return reservableHorarios;
	}

	public void setReservableHorarios( List<ReservableHorario> reservableHorarios ) {
		this.reservableHorarios = reservableHorarios;
	}

	/*
	public TipoReservable remove(ReservableHorario horario){

	    this.getReservableHorarios().remove( horario );
	    return this;
	}

	public TipoReservable add(ReservableHorario horario){

	    horario.setTipoReservable( this );
	    this.getReservableHorarios().add( horario );

	    return this;
	}

	public TipoReservable add(Reservable reservable){

	    reservable.setTipoReservable( this );
	    this.getReservables().add( reservable );

	    return this;
	}
	 */

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = ( prime * result ) + ( ( cod == null ) ? 0 : cod.hashCode() );
		result = ( prime * result ) + ( ( descripcion == null ) ? 0 : descripcion.hashCode() );
		result = ( prime * result ) + ( ( diasAntelacion == null ) ? 0 : diasAntelacion.hashCode() );
		result = ( prime * result ) + ( ( horasAntelacionMinima == null ) ? 0 : horasAntelacionMinima.hashCode() );
		result = ( prime * result ) + ( ( duracionMinima == null ) ? 0 : duracionMinima.hashCode() );
		result = ( prime * result ) + ( ( estado == null ) ? 0 : estado.hashCode() );
		result = ( prime * result ) + ( ( horasAnulacion == null ) ? 0 : horasAnulacion.hashCode() );
		result = ( prime * result ) + ( ( productos == null ) ? 0 : productos.hashCode() );
		result = ( prime * result ) + ( ( reservaMultiple == null ) ? 0 : reservaMultiple.hashCode() );
		result = ( prime * result ) + ( ( reservableHorarios == null ) ? 0 : reservableHorarios.hashCode() );
		result = ( prime * result ) + ( ( reservables == null ) ? 0 : reservables.hashCode() );
		result = ( prime * result ) + ( ( servicio == null ) ? 0 : servicio.hashCode() );
		result = ( prime * result ) + ( ( vistaMaxima == null ) ? 0 : vistaMaxima.hashCode() );
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
		if ( !(obj instanceof TipoReservable) ) {
			return false;
		}
		final TipoReservable other = ( TipoReservable ) obj;
		if ( cod == null ) {
			if ( other.cod != null ) {
				return false;
			}
		} else if ( !cod.equals( other.getCod() ) ) {
			return false;
		}
		if ( descripcion == null ) {
			if ( other.descripcion != null ) {
				return false;
			}
		} else if ( !descripcion.equals( other.getDescripcion() ) ) {
			return false;
		}
		if ( diasAntelacion == null ) {
			if ( other.diasAntelacion != null ) {
				return false;
			}
		} else if ( !diasAntelacion.equals( other.getDiasAntelacion() ) ) {
			return false;
		}
		if ( horasAntelacionMinima == null ) {
			if ( other.horasAntelacionMinima != null ) {
				return false;
			}
		} else if ( !horasAntelacionMinima.equals( other.getHorasAntelacionMinima() ) ) {
			return false;
		}
		if ( duracionMinima == null ) {
			if ( other.duracionMinima != null ) {
				return false;
			}
		} else if ( !duracionMinima.equals( other.getDuracionMinima() ) ) {
			return false;
		}
		if ( estado == null ) {
			if ( other.estado != null ) {
				return false;
			}
		} else if ( !estado.equals( other.getEstado() ) ) {
			return false;
		}
		if ( horasAnulacion == null ) {
			if ( other.horasAnulacion != null ) {
				return false;
			}
		} else if ( !horasAnulacion.equals( other.horasAnulacion ) ) {
			return false;
		}
		if ( productos == null ) {
			if ( other.productos != null ) {
				return false;
			}
		} else if ( !productos.equals( other.productos ) ) {
			return false;
		}
		if ( reservaMultiple == null ) {
			if ( other.reservaMultiple != null ) {
				return false;
			}
		} else if ( !reservaMultiple.equals( other.reservaMultiple ) ) {
			return false;
		}
		if ( reservableHorarios == null ) {
			if ( other.reservableHorarios != null ) {
				return false;
			}
		} else if ( !reservableHorarios.equals( other.reservableHorarios ) ) {
			return false;
		}
		if ( reservables == null ) {
			if ( other.reservables != null ) {
				return false;
			}
		} else if ( !reservables.equals( other.reservables ) ) {
			return false;
		}
		if ( servicio == null ) {
			if ( other.servicio != null ) {
				return false;
			}
		} else if ( !servicio.equals( other.servicio ) ) {
			return false;
		}
		if ( vistaMaxima == null ) {
			if ( other.vistaMaxima != null ) {
				return false;
			}
		} else if ( !vistaMaxima.equals( other.vistaMaxima ) ) {
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return descripcion;
	}

}
