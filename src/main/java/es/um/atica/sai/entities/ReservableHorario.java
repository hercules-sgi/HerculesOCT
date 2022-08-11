package es.um.atica.sai.entities;
// Generated 24-jun-2009 10:06:20 by Hibernate Tools 3.2.2.GA

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "RESERVABLE_HORARIO")
@SequenceGenerator(name="SEQ_RESERVABLEHOR",sequenceName="SAI_RESERVABLEHOR",initialValue=0,allocationSize=1)
@NamedQueries ( {
	@NamedQuery(name=ReservableHorario.GET_RESERVABLEHORARIOS_BY_TIPORESERVABLE, query="SELECT rh from ReservableHorario rh WHERE rh.tipoReservable=:tiporeservable ORDER BY rh.fechaInicio, rh.fechaFin"),    
	@NamedQuery(name=ReservableHorario.GET_RESERVABLEHORARIOS_BY_PRODUCTO, query="SELECT rh from ReservableHorario rh WHERE rh.producto=:producto ORDER BY rh.fechaInicio, rh.fechaFin"),    
	@NamedQuery(name=ReservableHorario.EXISTE_RESERVABLE_HORARIO_SOLAPADO, query="SELECT COUNT(rh) from ReservableHorario rh WHERE rh.tipoReservable=:tiporeservable AND rh.fechaFin>:fechainicio AND rh.fechaInicio<:fechafin"),
    @NamedQuery(name=ReservableHorario.EXISTE_PRODUCTO_HORARIO_SOLAPADO, query="SELECT COUNT(rh) from ReservableHorario rh WHERE rh.producto=:producto AND rh.fechaFin>:fechainicio AND rh.fechaInicio<:fechafin"),
})

@NamedNativeQueries({
    @NamedNativeQuery(name=ReservableHorario.GET_TIPOHORARIO_BY_TIPORESERVABLE_DIAMES, query="SELECT TH.cod FROM sai.tipo_horario TH, sai.reservable_horario RH WHERE TH.cod=RH.cod_horario AND RH.cod_tipo_reservable=:codtiporeservable AND TO_CHAR(RH.fecha_inicio,'mmdd')<=:mesdia AND :mesdia<=TO_CHAR(RH.fecha_fin,'mmdd')"),
    @NamedNativeQuery(name=ReservableHorario.GET_TIPOHORARIO_BY_PRODUCTO_DIAMES, query="SELECT TH.cod FROM sai.tipo_horario TH, sai.reservable_horario RH WHERE TH.cod=RH.cod_horario AND RH.cod_producto=:codproducto AND TO_CHAR(RH.fecha_inicio,'mmdd')<=:mesdia AND :mesdia<=TO_CHAR(RH.fecha_fin,'mmdd')"),
})

public class ReservableHorario implements java.io.Serializable {

    private static final long serialVersionUID = 4952589116707968090L;
    
    public static final String GET_RESERVABLEHORARIOS_BY_TIPORESERVABLE = "RESERVABLE_HORARIO.GET_RESERVABLEHORARIOS_BY_TIPORESERVABLE";
    public static final String GET_RESERVABLEHORARIOS_BY_PRODUCTO = "RESERVABLE_HORARIO.GET_RESERVABLEHORARIOS_BY_PRODUCTO";
    public static final String EXISTE_RESERVABLE_HORARIO_SOLAPADO = "RESERVABLE_HORARIO.EXISTE_RESERVABLE_HORARIO_SOLAPADO";
    public static final String EXISTE_PRODUCTO_HORARIO_SOLAPADO = "RESERVABLE_HORARIO.EXISTE_PRODUCTO_HORARIO_SOLAPADO";
    public static final String GET_TIPOHORARIO_BY_TIPORESERVABLE_DIAMES = "RESERVABLE_HORARIO.GET_TIPOHORARIO_BY_TIPORESERVABLE_DIAMES";
    public static final String GET_TIPOHORARIO_BY_PRODUCTO_DIAMES = "RESERVABLE_HORARIO.GET_TIPOHORARIO_BY_PRODUCTO_DIAMES";

    private Long cod;
	private TipoReservable tipoReservable;
	private Producto producto;
	private TipoHorario tipoHorario;
    private Date fechaInicio;
	private Date fechaFin;

	public ReservableHorario() 
	{
		//Constructor vacío
	}

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_RESERVABLEHOR")
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COD_PRODUCTO")
	public Producto getProducto() {
		return producto;
	}
	
	public void setProducto( Producto producto ) {
		this.producto = producto;
	}

	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COD_HORARIO", nullable = false)
    @NotNull
	public TipoHorario getTipoHorario() {
		return tipoHorario;
	}
	
	public void setTipoHorario( TipoHorario tipoHorario ) {
		this.tipoHorario = tipoHorario;
	}
	
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_INICIO", nullable = false, length = 7)
    @NotNull
	public Date getFechaInicio() {
		return fechaInicio;
	}
	
	public void setFechaInicio( Date fechaInicio ) {
		this.fechaInicio = fechaInicio;
	}
	
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_FIN", length = 7)
	public Date getFechaFin() {
		return fechaFin;
	}
	
	public void setFechaFin( Date fechaFin ) {
		this.fechaFin = fechaFin;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( cod == null ) ? 0 : cod.hashCode() );
		result = prime * result + ( ( fechaFin == null ) ? 0 : fechaFin.hashCode() );
		result = prime * result + ( ( fechaInicio == null ) ? 0 : fechaInicio.hashCode() );
		result = prime * result + ( ( tipoHorario == null ) ? 0 : tipoHorario.hashCode() );
		result = prime * result + ( ( tipoReservable == null ) ? 0 : tipoReservable.hashCode() );
		return result;
	}

	@Override
	public boolean equals( Object obj ) {
		if ( this == obj )
			return true;
		if ( obj == null )
			return false;
		if ( !(obj instanceof ReservableHorario) )
			return false;
		ReservableHorario other = ( ReservableHorario ) obj;
		if ( cod == null ) {
			if ( other.cod != null )
				return false;
		} else if ( !cod.equals( other.getCod() ) )
			return false;
		if ( fechaFin == null ) {
			if ( other.fechaFin != null )
				return false;
		} else if ( !fechaFin.equals( other.getFechaFin() ) )
			return false;
		if ( fechaInicio == null ) {
			if ( other.fechaInicio != null )
				return false;
		} else if ( !fechaInicio.equals( other.getFechaInicio() ) )
			return false;
		if ( tipoHorario == null ) {
			if ( other.tipoHorario != null )
				return false;
		} else if ( !tipoHorario.equals( other.getTipoHorario() ) )
			return false;
		if ( tipoReservable == null ) {
			if ( other.tipoReservable != null )
				return false;
		} else if ( !tipoReservable.equals( other.getTipoReservable() ) )
			return false;
		return true;
	}
   
	@Override
	public String toString()
	{
		return String.valueOf(this.getCod());
	}


	
}
