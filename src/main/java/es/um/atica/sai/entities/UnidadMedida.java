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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "UNIDAD_MEDIDA")
@SequenceGenerator(name="SEQ_UNIDAD",sequenceName="SAI_UNIDAD",initialValue=0,allocationSize=1)

public class UnidadMedida implements java.io.Serializable {

    private static final long serialVersionUID = 2563744331289389730L;
    
    private Long cod;
	private String abreviatura;
	private String descripcion;
    private List<Producto> productos = new ArrayList<>(0);
    
	public UnidadMedida() {
	}

	public UnidadMedida( Long cod, String abreviatura, String descripcion, List<Producto> productos ) {
		this.cod = cod;
		this.abreviatura = abreviatura;
		this.descripcion = descripcion;
		this.productos = productos;
	}

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_UNIDAD")
    @Column(name = "COD",  nullable = false, precision = 19, scale = 0)
	public Long getCod() {
		return cod;
	}
	
	public void setCod( Long cod ) {
		this.cod = cod;
	}

    @Column(name = "ABREVIATURA", nullable = false, length = 10)
    @NotNull
    @Length(max = 10)
	public String getAbreviatura() {
		return abreviatura;
	}
	
	public void setAbreviatura( String abreviatura ) {
		this.abreviatura = abreviatura;
	}

    @Column(name = "DESCRIPCION", nullable = false, length = 40)
    @NotNull
    @Length(max = 40)
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion( String descripcion ) {
		this.descripcion = descripcion;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "unidadMedida")
	public List<Producto> getProductos() {
		return productos;
	}

	public void setProductos( List<Producto> productos ) {
		this.productos = productos;
	}

	@Transient
	public boolean isDeleteable(){
	    return (this.getProductos() == null || this.getProductos().isEmpty());
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
		if ( this == obj )
			return true;
		if ( obj == null )
			return false;
		if ( !( obj instanceof UnidadMedida ) )
			return false;
		UnidadMedida other = ( UnidadMedida ) obj;
		if ( cod == null ) {
			if ( other.getCod() != null )
				return false;
		} else if ( !cod.equals( other.getCod() ) )
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return this.descripcion;
	}
}
