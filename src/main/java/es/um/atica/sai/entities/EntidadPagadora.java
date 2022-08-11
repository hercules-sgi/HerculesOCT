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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "ENTIDAD_PAGADORA")
@NamedQueries ( {
    @NamedQuery (name = EntidadPagadora.GET_ENTIDADES_PAGADORAS_ACTIVAS, query = "SELECT e FROM EntidadPagadora e WHERE e.estado='V' ORDER BY e.nombre asc" ),
    @NamedQuery (name = EntidadPagadora.GET_ENTIDADES_PAGADORAS_BUSQUEDA, query = "select e from EntidadPagadora e " ),
    @NamedQuery (name = EntidadPagadora.GET_ENTIDADES_BY_IR, query = "SELECT eir.entidadPagadora FROM EntidadesIr eir WHERE eir.usuario=:usuario AND eir.entidadPagadora.estado='V' ORDER BY eir.entidadPagadora.nombre"),            
    @NamedQuery (name = EntidadPagadora.EXISTE_ENTIDAD_CIF, query = "SELECT COUNT(e) FROM EntidadPagadora e WHERE e.cif=:cif AND e.codSubtercero=:codsubtercero"),
    @NamedQuery (name = EntidadPagadora.EXISTE_ENTIDAD_CIF_OTRA, query = "SELECT COUNT(e) FROM EntidadPagadora e WHERE e.cif=:cif AND e.codSubtercero=:codsubtercero AND e.cod<>:codentidadp"),
    @NamedQuery (name = EntidadPagadora.EXISTE_ENTIDAD_UDADMIN, query = "SELECT COUNT(e) FROM EntidadPagadora e WHERE e.unidadAdministrativa=:udadmin"),
    @NamedQuery (name = EntidadPagadora.GET_ENTIDAD_X_CIF, query = "SELECT e FROM EntidadPagadora e WHERE e.cif=:cif AND e.codSubtercero=:codsubtercero"),
    @NamedQuery (name = EntidadPagadora.GET_ENTIDAD_X_UDADMIN, query = "SELECT e FROM EntidadPagadora e WHERE e.unidadAdministrativa=:udadmin"),

})
@SequenceGenerator(name="SEQ_ENTIDAD",sequenceName="SAI_ENTIDAD",initialValue=0,allocationSize=1)

public class EntidadPagadora implements java.io.Serializable {
    
	private static final long serialVersionUID = 5263369754184081733L;

	public static final String GET_ENTIDADES_PAGADORAS_ACTIVAS = "ENTIDAD_PAGADORA.GET_ENTIDADES_PAGADORAS_ACTIVAS";
    public static final String GET_ENTIDADES_PAGADORAS_BUSQUEDA = "ENTIDAD_PAGADORA.GET_ENTIDADES_PAGADORAS_BUSQUEDA";
    public static final String GET_ENTIDADES_BY_IR = "ENTIDAD_PAGADORA.GET_ENTIDADES_PAGADORAS_BY_IR";
    public static final String EXISTE_ENTIDAD_CIF = "ENTIDAD_PAGADORA.EXISTE_ENTIDAD_CIF";
    public static final String EXISTE_ENTIDAD_CIF_OTRA = "ENTIDAD_PAGADORA.EXISTE_ENTIDAD_CIF_OTRA";
    public static final String EXISTE_ENTIDAD_UDADMIN = "ENTIDAD_PAGADORA.EXISTE_ENTIDAD_UDADMIN";
    public static final String GET_ENTIDAD_X_CIF = "ENTIDAD_PAGADORA.GET_ENTIDAD_X_CIF";
    public static final String GET_ENTIDAD_X_UDADMIN = "ENTIDAD_PAGADORA.GET_ENTIDAD_X_UDADMIN";

	private Long cod;
	private TipoTarifa tipoTarifa;
	private String cif;
	private Integer codSubtercero;
	private Long cp;
	private String direccion;
	private String email;
	private String localidad;
	private String nombre;
	private String telefono;
	private String observaciones;
	private String unidadAdministrativa;
	private String estado;
	private List<Factura> facturas = new ArrayList<>(0);
	private List<EntidadesIr> entidadesIrs = new ArrayList<>(0);
	private List<Consumo> consumos= new ArrayList<>(0);
	private List<Usuario> usuariosSolicitan=new ArrayList<>();

	public EntidadPagadora() {
	}

	public EntidadPagadora(Long cod, TipoTarifa tipoTarifa, String cif, Integer codSubtercero, Long cp,
			String direccion, String email, String localidad, String nombre,
			String telefono, String unidadAdministrativa) {
		this.cod = cod;
		this.tipoTarifa = tipoTarifa;
		this.cif = cif;
		this.codSubtercero = codSubtercero;
		this.cp = cp;
		this.direccion = direccion;
		this.email = email;
		this.localidad = localidad;
		this.nombre = nombre;
		this.telefono = telefono;
		this.unidadAdministrativa = unidadAdministrativa;
	}


	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_ENTIDAD")
	@Column(name = "COD", nullable = false, precision = 19, scale = 0)
	public Long getCod() {
		return this.cod;
	}

	public void setCod(Long cod) {
		this.cod = cod;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COD_TIPO_TARIFA")
	public TipoTarifa getTipoTarifa() {
		return this.tipoTarifa;
	}

	public void setTipoTarifa(TipoTarifa tipoTarifa) {
		this.tipoTarifa = tipoTarifa;
	}

	@Column(name = "CIF", length = 20)
	public String getCif() {
		return this.cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	@Column(name = "COD_SUBTERCERO", precision = 5, scale = 0)
	public Integer getCodSubtercero() {
		return codSubtercero;
	}
	
	public void setCodSubtercero( Integer codSubtercero ) {
		this.codSubtercero = codSubtercero;
	}

	@Column(name = "CP", nullable = true, precision = 19, scale = 0)
	public Long getCp() {
		return this.cp;
	}

	public void setCp(Long cp) {
		this.cp = cp;
	}

	@Column(name = "DIRECCION", nullable = true, length = 50)
	@Length(max = 50)
	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	@Column(name = "EMAIL", nullable = false, length = 40)
	@NotNull
	@Length(max = 40)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "LOCALIDAD", nullable = true, length = 40)
	@Length(max = 40)
	public String getLocalidad() {
		return this.localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	@Column(name = "NOMBRE", nullable = false, length = 200)
	@NotNull
	@Length(max = 200)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "TELEFONO", nullable = false, length = 20)
	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	public void setUnidadAdministrativa(String ua) {
		this.unidadAdministrativa = ua;
	}

	@Column(name = "UNIDAD_ADMINISTRATIVA", nullable = true, length = 10)
	@Length(max = 10)
	public String getUnidadAdministrativa() {
		return this.unidadAdministrativa;
	}
	
	@Column(name = "OBSERVACIONES", length = 2000)
	@Length(max = 2000)
	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	@Column(name = "ESTADO", nullable = false, length = 1)	
	public String getEstado() {
		return estado;
	}
	
	public void setEstado( String estado ) {
		this.estado = estado;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entidadPagadora")
	public List<Factura> getFacturas() {
		return this.facturas;
	}

	public void setFacturas(List<Factura> facturas) {
		this.facturas = facturas;
	}
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entidadPagadora")
	public List<EntidadesIr> getEntidadesIrs() {
		return this.entidadesIrs;
	}

	public void setEntidadesIrs(List<EntidadesIr> entidadesIr) {
		this.entidadesIrs= entidadesIr;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "entidadPagadora")
    public List<Consumo> getConsumos() {
        return this.consumos;
    }

    public void setConsumos(List<Consumo> consumos) {
        this.consumos = consumos;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "entidadPagadoraSolicita")
	public List<Usuario> getUsuariosSolicitan() {
		return usuariosSolicitan;
	}

	
	public void setUsuariosSolicitan( List<Usuario> usuariosSolicitan ) {
		this.usuariosSolicitan = usuariosSolicitan;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( cif == null ) ? 0 : cif.hashCode() );
		result = prime * result + ( ( cod == null ) ? 0 : cod.hashCode() );
		result = prime * result + ( ( codSubtercero == null ) ? 0 : codSubtercero.hashCode() );
		result = prime * result + ( ( cp == null ) ? 0 : cp.hashCode() );
		result = prime * result + ( ( direccion == null ) ? 0 : direccion.hashCode() );
		result = prime * result + ( ( email == null ) ? 0 : email.hashCode() );
		result = prime * result + ( ( estado == null ) ? 0 : estado.hashCode() );
		result = prime * result + ( ( localidad == null ) ? 0 : localidad.hashCode() );
		result = prime * result + ( ( nombre == null ) ? 0 : nombre.hashCode() );
		result = prime * result + ( ( observaciones == null ) ? 0 : observaciones.hashCode() );
		result = prime * result + ( ( telefono == null ) ? 0 : telefono.hashCode() );
		result = prime * result + ( ( tipoTarifa == null ) ? 0 : tipoTarifa.hashCode() );
		result = prime * result + ( ( unidadAdministrativa == null ) ? 0 : unidadAdministrativa.hashCode() );
		return result;
	}

	@Override
	public boolean equals( Object obj ) {
		if ( this == obj )
			return true;
		if ( !( obj instanceof EntidadPagadora ) )
			return false;
		EntidadPagadora other = ( EntidadPagadora ) obj;
		if ( cod == null ) {
			if ( other.getCod() != null )
				return false;
		} else if ( !cod.equals( other.getCod() ) )
			return false;
		return true;
	}


}
