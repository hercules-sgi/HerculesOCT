package es.um.atica.sai.entities;
// Generated 26-oct-2009 12:54:31 by Hibernate Tools 3.2.2.GA

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

@Entity
@Table(name = "HORARIO_DIA")
@SequenceGenerator(name="SEQ_HORARIODIA",sequenceName="SAI_HORARIODIA",initialValue=0,allocationSize=1)

@NamedQueries({
	@NamedQuery(name = HorarioDia.GET_HORARIODIA_X_TIPOHORARIO_DIA, query = "SELECT hd FROM HorarioDia hd WHERE hd.tipoHorario=:tipohorario AND hd.dia=:dia"),
	@NamedQuery(name = HorarioDia.GET_HORARIODIA_X_TIPOHORARIO, query = "SELECT hd FROM HorarioDia hd WHERE hd.tipoHorario=:tipohorario AND ((hd.horaIniManana IS NOT NULL AND hd.horaFinManana IS NOT NULL) OR (hd.horaIniTarde IS NOT NULL AND hd.horaFinTarde IS NOT NULL)) ORDER BY hd.dia"),
})
public class HorarioDia implements java.io.Serializable {

    private static final long serialVersionUID = 7477674325810420319L;

    public static final String GET_HORARIODIA_X_TIPOHORARIO_DIA = "HORARIO_DIA.GET_HORARIODIA_X_TIPOHORARIO_DIA";
    public static final String GET_HORARIODIA_X_TIPOHORARIO = "HORARIO_DIA.GET_HORARIODIA_X_TIPOHORARIO";
    

    private Long cod;
	private TipoHorario tipoHorario;
	private int dia;
	private String horaIniManana;
	private String horaFinManana;
	private String horaIniTarde;
	private String horaFinTarde;
    
	public HorarioDia() {
	}    

    public HorarioDia(TipoHorario tipoHorario, int dia, String horaIniManana, String horaFinManana,
			String horaIniTarde, String horaFinTarde ) {
		this.tipoHorario = tipoHorario;
		this.dia = dia;
		this.horaIniManana = horaIniManana;
		this.horaFinManana = horaFinManana;
		this.horaIniTarde = horaIniTarde;
		this.horaFinTarde = horaFinTarde;
	}

	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_HORARIODIA")
    @Column(name = "COD", nullable = false, precision=19, scale = 0)
    @NotNull	
	public Long getCod() {
		return cod;
	}
	
	public void setCod( Long cod ) {
		this.cod = cod;
	}

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COD_TIPO_HORARIO", nullable = false)
    @NotNull
	public TipoHorario getTipoHorario() {
		return tipoHorario;
	}
	
	public void setTipoHorario( TipoHorario tipoHorario ) {
		this.tipoHorario = tipoHorario;
	}

    @Column(name = "DIA", nullable = false, precision = 1, scale = 0)
    @NotNull
	public int getDia() {
		return dia;
	}
	
	public void setDia( int dia ) {
		this.dia = dia;
	}
	
	@Column(name = "HORA_INI_MANANA", length = 5)	
	public String getHoraIniManana() {
		return horaIniManana;
	}
	
	public void setHoraIniManana( String horaIniManana ) {
		this.horaIniManana = horaIniManana;
	}

	@Column(name = "HORA_FIN_MANANA", length = 5)	
	public String getHoraFinManana() {
		return horaFinManana;
	}
	
	public void setHoraFinManana( String horaFinManana ) {
		this.horaFinManana = horaFinManana;
	}

	@Column(name = "HORA_INI_TARDE", length = 5)	
	public String getHoraIniTarde() {
		return horaIniTarde;
	}

	public void setHoraIniTarde( String horaIniTarde ) {
		this.horaIniTarde = horaIniTarde;
	}

	@Column(name = "HORA_FIN_TARDE", length = 5)		
	public String getHoraFinTarde() {
		return horaFinTarde;
	}
	
	public void setHoraFinTarde( String horaFinTarde ) {
		this.horaFinTarde = horaFinTarde;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( cod == null ) ? 0 : cod.hashCode() );
		result = prime * result + dia;
		result = prime * result + ( ( horaFinManana == null ) ? 0 : horaFinManana.hashCode() );
		result = prime * result + ( ( horaFinTarde == null ) ? 0 : horaFinTarde.hashCode() );
		result = prime * result + ( ( horaIniManana == null ) ? 0 : horaIniManana.hashCode() );
		result = prime * result + ( ( horaIniTarde == null ) ? 0 : horaIniTarde.hashCode() );
		result = prime * result + ( ( tipoHorario == null ) ? 0 : tipoHorario.hashCode() );
		return result;
	}



	@Override
	public boolean equals( Object obj ) {
		if ( this == obj )
			return true;
		if ( !( obj instanceof HorarioDia ) )
			return false;
		HorarioDia other = ( HorarioDia ) obj;
		if ( cod == null ) {
			if ( other.cod != null )
				return false;
		} else if ( !cod.equals( other.cod ) )
			return false;
		if ( dia != other.dia )
			return false;
		if ( horaFinManana == null ) {
			if ( other.horaFinManana != null )
				return false;
		} else if ( !horaFinManana.equals( other.horaFinManana ) )
			return false;
		if ( horaFinTarde == null ) {
			if ( other.horaFinTarde != null )
				return false;
		} else if ( !horaFinTarde.equals( other.horaFinTarde ) )
			return false;
		if ( horaIniManana == null ) {
			if ( other.horaIniManana != null )
				return false;
		} else if ( !horaIniManana.equals( other.horaIniManana ) )
			return false;
		if ( horaIniTarde == null ) {
			if ( other.horaIniTarde != null )
				return false;
		} else if ( !horaIniTarde.equals( other.horaIniTarde ) )
			return false;
		if ( tipoHorario == null ) {
			if ( other.tipoHorario != null )
				return false;
		} else if ( !tipoHorario.equals( other.tipoHorario ) )
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return String.valueOf(this.cod);
	}
	
	
}
