package es.um.atica.sai.entities;

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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table( name = "GRUPO_INVESTIGACION" )
@SequenceGenerator( name = "SQ_GRUPOINVESTIGACION", sequenceName = "SQ_GRUPOINVESTIGACION", initialValue = 0, allocationSize = 1 )
@NamedQueries ({
    @NamedQuery(name = GrupoInvestigacion.GET_GRUPOINVESTIGACION_BY_ENTIDADPAGADORA, query = "SELECT gi FROM GrupoInvestigacion gi WHERE gi.entidadPagadora=:entidad" ),
})
public class GrupoInvestigacion implements java.io.Serializable {

	private static final long serialVersionUID = -6113589640957649995L;

	public static final String GET_GRUPOINVESTIGACION_BY_ENTIDADPAGADORA = "GRUPO_INVESTIGACION.GET_GRUPOINVESTIGACION_BY_ENTIDADPAGADORA";
	
	private Long cod;
	private EntidadPagadora entidadPagadora;
	private Long codGrupoInvestigacion;
	private GrupoInvestigacionDatos grupoInvestigacionDatos;
	private List<Consumo> consumos;
	
	public GrupoInvestigacion() {
		super();
	}

	@Id
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "SQ_GRUPOINVESTIGACION" )
	@Column( name = "COD", unique = true, nullable = false, scale = 0 )
	public Long getCod() {
		return cod;
	}

	public void setCod( Long cod ) {
		this.cod = cod;
	}

	@ManyToOne( fetch = FetchType.EAGER )
	@JoinColumn( name = "COD_ENTIDAD_PAGADORA" )
	public EntidadPagadora getEntidadPagadora() {
		return entidadPagadora;
	}
	
	public void setEntidadPagadora( EntidadPagadora entidadPagadora ) {
		this.entidadPagadora = entidadPagadora;
	}
	
	@Column(name = "COD_GRUPO_INVESTIGACION", precision = 8)
	public Long getCodGrupoInvestigacion() {
		return codGrupoInvestigacion;
	}
	
	public void setCodGrupoInvestigacion( Long codGrupoInvestigacion ) {
		this.codGrupoInvestigacion = codGrupoInvestigacion;
	}

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COD")
	public GrupoInvestigacionDatos getGrupoInvestigacionDatos() {
		return grupoInvestigacionDatos;
	}
	
	public void setGrupoInvestigacionDatos( GrupoInvestigacionDatos grupoInvestigacionDatos ) {
		this.grupoInvestigacionDatos = grupoInvestigacionDatos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "grupoInvestigacion")
	public List<Consumo> getConsumos() {
		return consumos;
	}
	
	public void setConsumos( List<Consumo> consumos ) {
		this.consumos = consumos;
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
		if ( !( obj instanceof GrupoInvestigacion ) ) {
			return false;
		}
		GrupoInvestigacion other = ( GrupoInvestigacion ) obj;
		if ( cod == null ) {
			if ( other.getCod() != null ) {
				return false;
			}
		} else if ( !cod.equals( other.getCod() ) ) {
			return false;
		}
		return true;
	}



}
