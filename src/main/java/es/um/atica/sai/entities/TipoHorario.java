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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;


@Entity
@Table(name = "TIPO_HORARIO" )
@SequenceGenerator(name = "SEQ_TIPOHOR", sequenceName = "SAI_TIPOHOR", initialValue = 0, allocationSize = 1 )
@NamedQueries( {
      @NamedQuery(name = TipoHorario.GET_TIPOSHORARIO, query = "SELECT th FROM TipoHorario th ORDER BY th.descripcion" ),
      @NamedQuery(name = TipoHorario.GET_TIPOSHORARIO_X_USUARIO, query = "SELECT th FROM TipoHorario th WHERE (th.servicio IS NULL OR th.servicio IN (SELECT up.servicio FROM UsuarioPerfil up WHERE up.perfil.tag='SUPERVISOR' AND up.usuario = :currentUser)) ORDER BY th.descripcion" ),
      @NamedQuery(name = TipoHorario.GET_TIPOSHORARIO_X_SERVICIO, query = "SELECT th from TipoHorario th WHERE th.servicio=:servicio OR th.servicio IS NULL ORDER BY th.descripcion" ),

})
public class TipoHorario implements java.io.Serializable {

    private static final long serialVersionUID = 2267903025372045155L;

    public static final String GET_TIPOSHORARIO = "TIPO_HORARIO.GET_TIPOSHORARIO";
    public static final String GET_TIPOSHORARIO_X_USUARIO = "TIPO_HORARIO.GET_TIPOSHORARIO_X_USUARIO";
    public static final String GET_TIPOSHORARIO_X_SERVICIO = "TIPO_HORARIO.GET_TIPOSHORARIO_X_SERVICIO";
    
    private Long cod;
    private String descripcion;
    private Servicio servicio;
    private List<HorarioDia> horarioDias = new ArrayList<>( 7 );
    private List<ReservableHorario> reservableHorarios = new ArrayList<>( 0 );
    

    public TipoHorario() {
    }

    public TipoHorario( Long cod, String descripcion, Servicio servicio ) {
        this.cod = cod;
        this.descripcion = descripcion;
        this.servicio = servicio;
    }

    public TipoHorario( Long cod, String descripcion, List<HorarioDia> horarioDias,
                        List<ReservableHorario> reservableHorarios ) {
        this.cod = cod;
        this.descripcion = descripcion;
        this.horarioDias = horarioDias;
        this.reservableHorarios = reservableHorarios;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TIPOHOR" )
    @Column(name = "COD", nullable = false, precision = 19, scale = 0 )
	public Long getCod() {
		return cod;
	}

	
	public void setCod( Long cod ) {
		this.cod = cod;
	}

    @Column(name = "DESCRIPCION", nullable = false, length = 100 )
    @NotNull
    @Length(max = 100 )
	public String getDescripcion() {
		return descripcion;
	}

	
	public void setDescripcion( String descripcion ) {
		this.descripcion = descripcion;
	}

    @ManyToOne(fetch = FetchType.EAGER )
    @JoinColumn(name = "COD_SERVICIO", nullable = true )
	public Servicio getServicio() {
		return servicio;
	}
	
	public void setServicio( Servicio servicio ) {
		this.servicio = servicio;
	}

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoHorario" )
    @OrderBy( "dia ASC" )
	public List<HorarioDia> getHorarioDias() {
		return horarioDias;
	}

	
	public void setHorarioDias( List<HorarioDia> horarioDias ) {
		this.horarioDias = horarioDias;
	}

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoHorario" )
	public List<ReservableHorario> getReservableHorarios() {
		return reservableHorarios;
	}

	
	public void setReservableHorarios( List<ReservableHorario> reservableHorarios ) {
		this.reservableHorarios = reservableHorarios;
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
		if ( !( obj instanceof TipoHorario ) ) {
			return false;
		}
		TipoHorario other = ( TipoHorario ) obj;
		if ( cod == null ) {
			if ( other.getCod() != null ) {
				return false;
			}
		} else if ( !cod.equals( other.getCod() ) ) {
			return false;
		}
		return true;
	}

	public TipoHorario add(HorarioDia horario){
        this.getHorarioDias().add( horario );
        horario.setTipoHorario( this );
        
        return this;
    }
    
    @Transient
    public boolean isErasable(){
        return this.getReservableHorarios().isEmpty();
    }
    
    @Override
    public String toString()
    {
    	return String.valueOf(this.getCod());
    }

}
