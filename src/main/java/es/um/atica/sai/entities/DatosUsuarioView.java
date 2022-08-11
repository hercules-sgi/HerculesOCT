package es.um.atica.sai.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "DATOS_USUARIO_VIEW", schema="SAI" )

@NamedQueries({
	@NamedQuery(name = DatosUsuarioView.GET_DATOSUSUARIO_X_DNI, query = "SELECT duv FROM DatosUsuarioView duv WHERE duv.dni=:dni"),
	@NamedQuery(name = DatosUsuarioView.GET_DATOSUSUARIO_X_EMAIL, query = "SELECT duv FROM DatosUsuarioView duv WHERE duv.email=:mail"),
    @NamedQuery(name = DatosUsuarioView.GET_LISTA_IRS, query = "SELECT DISTINCT up.usuario.datosUsuario FROM UsuarioPerfil up WHERE up.perfil.tag='IR' AND up.usuario.estado<>'BAJA'")
})

public class DatosUsuarioView implements java.io.Serializable {

	private static final long serialVersionUID = 7662145909214447279L;
	
	public static final String GET_DATOSUSUARIO_X_DNI = "DATOS_USUARIO_VIEW.GET_DATOSUSUARIO_X_DNI";
	public static final String GET_DATOSUSUARIO_X_EMAIL = "DATOS_USUARIO_VIEW.GET_DATOSUSUARIO_X_EMAIL";
    public static final String GET_LISTA_IRS = "DATOS_USUARIO_VIEW.GET_LISTA_IRS";
    
    private Long cod;
    private String dni;
    private String nombre;
    private String apellidos;
    private String nombreCompleto;
    private String email;
    private String telefono;
    private Usuario usuario;
    
    public DatosUsuarioView() {
    }
        
    @Id
    @Column(name = "COD", nullable = false, scale = 0 )
	public Long getCod() {
		return cod;
	}
	
	public void setCod( Long cod ) {
		this.cod = cod;
	}

	@Column(name = "DNI", nullable = false, length = 10 )
	public String getDni() {
		return dni;
	}
	
	public void setDni( String dni ) {
		this.dni = dni;
	}
	
	@Column(name = "NOMBRE", length = 30 )
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre( String nombre ) {
		this.nombre = nombre;
	}

	@Column(name = "APELLIDOS", length = 50 )
	public String getApellidos() {
		return apellidos;
	}
	
	public void setApellidos( String apellidos ) {
		this.apellidos = apellidos;
	}

	@Column(name = "NOMBRE_COMPLETO", length = 81 )
	public String getNombreCompleto() {
		return nombreCompleto;
	}
	
	public void setNombreCompleto( String nombreCompleto ) {
		this.nombreCompleto = nombreCompleto;
	}
	
	@Column(name = "EMAIL", nullable = false, length = 40 )
	public String getEmail() {
		return email;
	}
	
	public void setEmail( String email ) {
		this.email = email;
	}

	@Column(name = "TELEFONO", length = 20 )
	public String getTelefono() {
		return telefono;
	}
	
	public void setTelefono( String telefono ) {
		this.telefono = telefono;
	}

	@OneToOne (mappedBy = "datosUsuario", fetch = FetchType.LAZY, optional = false)
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario( Usuario usuario ) {
		this.usuario = usuario;
	}

	@Transient
    public String getFullName(){
        return getNombreCompleto();
    }


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( cod == null ) ? 0 : cod.hashCode() );
		result = prime * result + ( ( dni == null ) ? 0 : dni.hashCode() );
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
		if ( getClass() != obj.getClass() ) {
			return false;
		}
		DatosUsuarioView other = ( DatosUsuarioView ) obj;
		if ( cod == null ) {
			if ( other.cod != null ) {
				return false;
			}
		} else if ( !cod.equals( other.cod ) ) {
			return false;
		}
		if ( dni == null ) {
			if ( other.dni != null ) {
				return false;
			}
		} else if ( !dni.equals( other.dni ) ) {
			return false;
		}
		return true;
	}

	@Override
    public String toString()
    {
    	return String.valueOf(this.cod);
    }
    
}
