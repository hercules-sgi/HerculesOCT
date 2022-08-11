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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "TIPO_TARIFA")
@SequenceGenerator(name="SEQ_TIPOTARIFA",sequenceName="SAI_TIPOTARIFA",initialValue=0,allocationSize=1)
@NamedQueries ( {
    @NamedQuery (name = TipoTarifa.GET_TARIFAS, query = "SELECT t from TipoTarifa t ORDER BY t.cod asc " )
})
public class TipoTarifa implements java.io.Serializable {

    private static final long serialVersionUID = 473358331397024211L;

    public static final String GET_TARIFAS = "tarifas.getALL";    
        
	private Long cod;
	private String descripcion;
	private List<Tarifa> tarifas = new ArrayList<>(0);
	private List<EntidadPagadora> entidadPagadoras = new ArrayList<>(0);

	public TipoTarifa() {
	}

	public TipoTarifa(Long cod, String descripcion) {
		this.cod = cod;
		this.descripcion = descripcion;
	}
	public TipoTarifa(Long cod, String descripcion, List<Tarifa> tarifas, List<EntidadPagadora> entidadPagadoras) {
		this.cod = cod;
		this.descripcion = descripcion;
		this.tarifas = tarifas;
		this.entidadPagadoras = entidadPagadoras;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_TIPOTARIFA")
	@Column(name = "COD", nullable = false, precision = 19, scale = 0)
	public Long getCod() {
		return this.cod;
	}

	public void setCod(Long cod) {
		this.cod = cod;
	}

	@Column(name = "DESCRIPCION", nullable = false, length = 50)
	@Length(max = 50)
	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoTarifa")
	public List<Tarifa> getTarifas() {
		return this.tarifas;
	}

	public void setTarifas(List<Tarifa> tarifas) {
		this.tarifas = tarifas;
	}
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoTarifa")
	public List<EntidadPagadora> getEntidadPagadoras() {
		return this.entidadPagadoras;
	}

	public void setEntidadPagadoras(List<EntidadPagadora> entidadPagadoras) {
		this.entidadPagadoras = entidadPagadoras;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( cod == null ) ? 0 : cod.hashCode() );
		result = prime * result + ( ( descripcion == null ) ? 0 : descripcion.hashCode() );
		return result;
	}

	@Override
	public boolean equals( Object obj ) {
		if ( this == obj )
			return true;
		if ( !( obj instanceof TipoTarifa ) )
			return false;
		TipoTarifa other = ( TipoTarifa ) obj;
		if ( cod == null ) {
			if ( other.getCod() != null )
				return false;
		} else if ( !cod.equals( other.getCod() ) )
			return false;
		if ( descripcion == null ) {
			if ( other.getDescripcion()!= null )
				return false;
		} else if ( !descripcion.equals( other.getDescripcion() ) )
			return false;
		return true;
	}

	@Transient
	public boolean isDeleteable(){
	    return ((this.getEntidadPagadoras() == null || this.getEntidadPagadoras().isEmpty()) && (this.getTarifas() == null || this.getTarifas().isEmpty())) ;
	}
}
