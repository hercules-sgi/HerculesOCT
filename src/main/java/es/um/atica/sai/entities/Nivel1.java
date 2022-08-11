package es.um.atica.sai.entities;
// Generated 14-ene-2010 9:05:16 by Hibernate Tools 3.2.2.GA

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "NIVEL1")
@SequenceGenerator(name="SEQ_NIVEL",sequenceName="SAI_NIVEL",initialValue=0,allocationSize=1)

@NamedQueries ( {
    @NamedQuery (name = Nivel1.GET_FUNGIBLES, query = "SELECT f FROM Nivel1 f" ),
    @NamedQuery (name = Nivel1.GET_FUNGIBLES_BY_SERVICIO, query = "SELECT f FROM Nivel1 f WHERE f.servicio=:servicio order by f.nombre ASC "),
    @NamedQuery (name = Nivel1.GET_FUNGIBLES_BY_SUPERVISOR, query = "SELECT f FROM Nivel1 f, UsuarioPerfil up WHERE f.servicio=up.servicio AND up.perfil.tag='SUPERVISOR' AND up.usuario.cod=:codusuario"),
    @NamedQuery (name = Nivel1.GET_NIVEL1_BY_SERVICIO, query = "SELECT n FROM Nivel1 n where n.servicio = :servicio order by n.nombre ASC")
})

public class Nivel1 implements java.io.Serializable, Comparable {

    private static final long serialVersionUID = -3951067125066348706L;
    
    public static final String GET_FUNGIBLES = "getFungibles";
    public static final String GET_FUNGIBLES_BY_SERVICIO = "getFungiblesByServicio";  
    public static final String GET_FUNGIBLES_BY_SUPERVISOR = "getFungiblesBySupervisor";  
    public static final String GET_NIVEL1_BY_SERVICIO = "getNivel1ByService";
    
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_NIVEL")
    @Column(name = "COD", nullable = false, precision=19, scale = 0)
    @NotNull
    private Long cod;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SERVICIO", nullable = false)
    @NotNull
	private Servicio servicio;
	
    @Column(name = "NOMBRE", nullable = false, length = 50)
    @NotNull
    @Length(max = 50)
    private String nombre;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "nivel1")
	private List<Producto> productos = new ArrayList<>(0);

	public Nivel1() {
	}

	public Nivel1( Long cod, Servicio servicio, String nombre, List<Producto> productos ) {
		this.cod = cod;
		this.servicio = servicio;
		this.nombre = nombre;
		this.productos = productos;
	}

	public Long getCod() {
		return cod;
	}
	
	public void setCod( Long cod ) {
		this.cod = cod;
	}
	
	public Servicio getServicio() {
		return servicio;
	}

	public void setServicio( Servicio servicio ) {
		this.servicio = servicio;
	}

	
	public String getNombre() {
		return nombre;
	}

	
	public void setNombre( String nombre ) {
		this.nombre = nombre;
	}

	
	public List<Producto> getProductos() {
		return productos;
	}

	
	public void setProductos( List<Producto> productos ) {
		this.productos = productos;
	}

	@Override
	public int compareTo( Object o ) {		
		return this.getNombre().compareTo( ((Nivel1)o).getNombre() );
	}
	
	@Transient
	public boolean isDeleteable(){
	    return ((this.getProductos() == null || this.getProductos().isEmpty())) ;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( cod == null ) ? 0 : cod.hashCode() );
		result = prime * result + ( ( nombre == null ) ? 0 : nombre.hashCode() );
		result = prime * result + ( ( productos == null ) ? 0 : productos.hashCode() );
		result = prime * result + ( ( servicio == null ) ? 0 : servicio.hashCode() );
		return result;
	}

	@Override
	public boolean equals( Object obj ) {
		if ( this == obj )
			return true;
		if ( obj == null )
			return false;
		if ( getClass() != obj.getClass() )
			return false;
		Nivel1 other = ( Nivel1 ) obj;
		if ( cod == null ) {
			if ( other.cod != null )
				return false;
		} else if ( !cod.equals( other.cod ) )
			return false;
		if ( nombre == null ) {
			if ( other.nombre != null )
				return false;
		} else if ( !nombre.equals( other.nombre ) )
			return false;
		if ( productos == null ) {
			if ( other.productos != null )
				return false;
		} else if ( !productos.equals( other.productos ) )
			return false;
		if ( servicio == null ) {
			if ( other.servicio != null )
				return false;
		} else if ( !servicio.equals( other.servicio ) )
			return false;
		return true;
	}


	
}
