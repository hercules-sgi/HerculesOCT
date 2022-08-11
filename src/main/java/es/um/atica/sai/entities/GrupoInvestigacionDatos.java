package es.um.atica.sai.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table( name = "GRUPO_INVESTIGACION_DATOS" )
public class GrupoInvestigacionDatos implements java.io.Serializable {

	private static final long serialVersionUID = 8786675773351524641L;

	private Long cod;
	private EntidadPagadora entidadPagadora;
	private Long codGrupoInvestigacion;
	private String nombre;
	private GrupoInvestigacion grupoInvestigacion;
	private List<Consumo> consumos;
	
	public GrupoInvestigacionDatos() {
		super();
	}

	@Id
	@Column( name = "COD", unique = true, nullable = false, scale = 0 )
	public Long getCod() {
		return cod;
	}

	public void setCod( Long cod ) {
		this.cod = cod;
	}

	@ManyToOne( fetch = FetchType.EAGER )
	@JoinColumn( name = "COD_ENTIDAD_PAGADORA", nullable = false )
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

	@Column(name = "NOMBRE", length = 255)
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre( String nombre ) {
		this.nombre = nombre;
	}

	@OneToOne (mappedBy = "grupoInvestigacionDatos", fetch = FetchType.LAZY, optional = false)
	public GrupoInvestigacion getGrupoInvestigacion() {
		return grupoInvestigacion;
	}

	
	public void setGrupoInvestigacion( GrupoInvestigacion grupoInvestigacion ) {
		this.grupoInvestigacion = grupoInvestigacion;
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
		if ( !( obj instanceof GrupoInvestigacionDatos ) ) {
			return false;
		}
		GrupoInvestigacionDatos other = ( GrupoInvestigacionDatos ) obj;
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
