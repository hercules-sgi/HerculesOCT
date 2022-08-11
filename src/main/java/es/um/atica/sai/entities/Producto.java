package es.um.atica.sai.entities;
// Generated 24-jun-2009 10:06:20 by Hibernate Tools 3.2.2.GA

import java.math.BigDecimal;
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
@Table(name = "PRODUCTO")
@SequenceGenerator(name="SEQ_PRODUCTO",sequenceName="SAI_PRODUCTO",initialValue=0,allocationSize=1)
@NamedQueries ( {
	@NamedQuery (name = Producto.GET_PRODUCTOS, query = "SELECT pro FROM Producto pro "),
	@NamedQuery (name = Producto.GET_PRODUCTOS_X_LISTASERVICIOS, query="SELECT pro FROM Producto pro WHERE pro.servicio in (:listaservicios)"),
	@NamedQuery (name = Producto.GET_PRODUCTOS_ACTIVOS_X_LISTASERVICIOS, query="SELECT pro FROM Producto pro WHERE pro.servicio in (:listaservicios) AND pro.estado='ALTA' ORDER BY pro.descripcion ASC"),
	@NamedQuery (name = Producto.GET_PRODUCTOS_X_SERVICIO, query = "SELECT pro FROM Producto pro WHERE pro.servicio=:servicio AND pro.estado='ALTA' AND (pro.tipo IN ('F','P') OR (pro.tipo='R' AND pro.tipoReservable IS NOT NULL)) ORDER BY pro.descripcion ASC" ),
	@NamedQuery (name = Producto.GET_PRODUCTOS_X_SERVICIO_TIPO, query = "SELECT pro FROM Producto pro WHERE pro.tipo=:tipo AND pro.servicio=:servicio AND pro.estado='ALTA' ORDER BY pro.descripcion ASC" ),
	@NamedQuery (name = Producto.GET_PRODUCTOS_X_SERVICIO_TIPO_NOREQUIEREPROYECTO, query = "SELECT pro FROM Producto pro WHERE pro.tipo=:tipo AND pro.servicio=:servicio AND pro.estado='ALTA' AND pro.requiereProyecto='NO' ORDER BY pro.descripcion ASC" ),
	@NamedQuery (name = Producto.GET_PRODUCTOS_X_SERVICIO_TIPO_NOADMIN, query = "SELECT pro FROM Producto pro WHERE pro.tipo=:tipo AND pro.servicio=:servicio AND pro.estado='ALTA' AND pro.soloAdmin='NO' ORDER BY pro.descripcion ASC" ),
	@NamedQuery (name = Producto.GET_PRODUCTOS_X_SERVICIO_TIPO_NOADMIN_NOREQUIEREPROYECTO, query = "SELECT pro FROM Producto pro WHERE pro.tipo=:tipo AND pro.servicio=:servicio AND pro.estado='ALTA' AND pro.soloAdmin='NO' AND pro.requiereProyecto='NO' ORDER BY pro.descripcion ASC" ),
	@NamedQuery (name = Producto.GET_PRODUCTOS_X_SERVICIO_TIPO_ADMITENPRESUPUESTO, query = "SELECT pro FROM Producto pro WHERE pro.tipo=:tipo AND pro.servicio=:servicio AND pro.estado='ALTA' AND pro.presupuesto='SI' ORDER BY pro.descripcion ASC" ),
	@NamedQuery (name = Producto.GET_PRODUCTOS_X_SERVICIO_TIPO_NOREQUIEREPROYECTO_ADMITENPRESUPUESTO, query = "SELECT pro FROM Producto pro WHERE pro.tipo=:tipo AND pro.servicio=:servicio AND pro.estado='ALTA' AND pro.requiereProyecto='NO' AND pro.presupuesto='SI' ORDER BY pro.descripcion ASC" ),
	@NamedQuery (name = Producto.GET_PRODUCTOS_X_SERVICIO_TIPO_NOADMIN_ADMITENPRESUPUESTO, query = "SELECT pro FROM Producto pro WHERE pro.tipo=:tipo AND pro.servicio=:servicio AND pro.estado='ALTA' AND pro.soloAdmin='NO' AND pro.presupuesto='SI' ORDER BY pro.descripcion ASC" ),
	@NamedQuery (name = Producto.GET_PRODUCTOS_X_SERVICIO_TIPO_NOADMIN_NOREQUIEREPROYECTO_ADMITENPRESUPUESTO, query = "SELECT pro FROM Producto pro WHERE pro.tipo=:tipo AND pro.servicio=:servicio AND pro.estado='ALTA' AND pro.soloAdmin='NO' AND pro.requiereProyecto='NO' AND pro.presupuesto='SI' ORDER BY pro.descripcion ASC" ),
	@NamedQuery (name = Producto.GET_PRODUCTOS_X_SERVICIO_TIPOR, query = "SELECT pro FROM Producto pro WHERE pro.tipo=:tipo AND pro.servicio=:servicio AND pro.estado='ALTA' AND pro.tipoReservable IS NOT NULL ORDER BY pro.descripcion ASC" ),
	@NamedQuery (name = Producto.GET_PRODUCTOS_X_SERVICIO_TIPOR_NOREQUIEREPROYECTO, query = "SELECT pro FROM Producto pro WHERE pro.tipo=:tipo AND pro.servicio=:servicio AND pro.estado='ALTA' AND pro.tipoReservable IS NOT NULL AND pro.requiereProyecto='NO' ORDER BY pro.descripcion ASC" ),
	@NamedQuery (name = Producto.GET_PRODUCTOS_X_SERVICIO_TIPOR_NOADMIN, query = "SELECT pro FROM Producto pro WHERE pro.tipo=:tipo AND pro.servicio=:servicio AND pro.estado='ALTA' AND pro.tipoReservable IS NOT NULL AND pro.soloAdmin='NO' ORDER BY pro.descripcion ASC" ),
	@NamedQuery (name = Producto.GET_PRODUCTOS_X_SERVICIO_TIPOR_NOADMIN_NOREQUIEREPROYECTO, query = "SELECT pro FROM Producto pro WHERE pro.tipo=:tipo AND pro.servicio=:servicio AND pro.estado='ALTA' AND pro.tipoReservable IS NOT NULL AND pro.soloAdmin='NO' AND pro.requiereProyecto='NO' ORDER BY pro.descripcion ASC" ),
	@NamedQuery (name = Producto.GET_PRODUCTOS_X_TIPORESERVABLE, query="SELECT pro FROM Producto pro WHERE pro.tipoReservable=:tiporeservable AND pro.estado='ALTA'"),
	@NamedQuery (name = Producto.GET_RESERVABLES_ALTA, query="select r FROM Producto r where r.tipo='R' and r.estado='ALTA' and r.servicio=:servicio and r.tipoReservable.estado='ALTA'")
})

public class Producto implements java.io.Serializable, Comparable{

	private static final long serialVersionUID = 5981420342755662530L;

	public static final String GET_PRODUCTOS = "PRODUCTO.GET_PRODUCTOS";
	public static final String GET_PRODUCTOS_X_LISTASERVICIOS = "PRODUCTO.GET_PRODUCTOS_X_LISTASERVICIOS";
	public static final String GET_PRODUCTOS_ACTIVOS_X_LISTASERVICIOS = "PRODUCTO.GET_PRODUCTOS_ACTIVOS_X_LISTASERVICIOS";
	public static final String GET_PRODUCTOS_X_SERVICIO = "PRODUCTO.GET_PRODUCTOS_BY_SERVICIO";
	public static final String GET_PRODUCTOS_X_SERVICIO_TIPO = "PRODUCTO.GET_PRODUCTOS_BY_SERVICIO_TIPO";
	public static final String GET_PRODUCTOS_X_SERVICIO_TIPO_NOREQUIEREPROYECTO = "PRODUCTO.GET_PRODUCTOS_X_SERVICIO_TIPO_NOREQUIEREPROYECTO";
	public static final String GET_PRODUCTOS_X_SERVICIO_TIPO_NOADMIN = "PRODUCTO.GET_PRODUCTOS_BY_SERVICIO_TIPO_NOADMIN";
	public static final String GET_PRODUCTOS_X_SERVICIO_TIPO_NOADMIN_NOREQUIEREPROYECTO = "PRODUCTO.GET_PRODUCTOS_X_SERVICIO_TIPO_NOADMIN_NOREQUIEREPROYECTO";
	public static final String GET_PRODUCTOS_X_SERVICIO_TIPO_ADMITENPRESUPUESTO = "PRODUCTO.GET_PRODUCTOS_X_SERVICIO_TIPO_ADMITENPRESUPUESTO";
	public static final String GET_PRODUCTOS_X_SERVICIO_TIPO_NOREQUIEREPROYECTO_ADMITENPRESUPUESTO = "PRODUCTO.GET_PRODUCTOS_X_SERVICIO_TIPO_NOREQUIEREPROYECTO_ADMITENPRESUPUESTO";
	public static final String GET_PRODUCTOS_X_SERVICIO_TIPO_NOADMIN_ADMITENPRESUPUESTO = "PRODUCTO.GET_PRODUCTOS_X_SERVICIO_TIPO_NOADMIN_ADMITENPRESUPUESTO";
	public static final String GET_PRODUCTOS_X_SERVICIO_TIPO_NOADMIN_NOREQUIEREPROYECTO_ADMITENPRESUPUESTO = "PRODUCTO.GET_PRODUCTOS_X_SERVICIO_TIPO_NOADMIN_NOREQUIEREPROYECTO_ADMITENPRESUPUESTO";
	public static final String GET_PRODUCTOS_X_SERVICIO_TIPOR = "PRODUCTO.GET_PRODUCTOS_BY_SERVICIO_TIPOR";
	public static final String GET_PRODUCTOS_X_SERVICIO_TIPOR_NOREQUIEREPROYECTO = "PRODUCTO.GET_PRODUCTOS_BY_SERVICIO_TIPOR_NOREQUIEREPROYECTO";
	public static final String GET_PRODUCTOS_X_SERVICIO_TIPOR_NOADMIN = "PRODUCTO.GET_PRODUCTOS_BY_SERVICIO_TIPOR_NOADMIN";
	public static final String GET_PRODUCTOS_X_SERVICIO_TIPOR_NOADMIN_NOREQUIEREPROYECTO = "PRODUCTO.GET_PRODUCTOS_X_SERVICIO_TIPOR_NOADMIN_NOREQUIEREPROYECTO";
	public static final String GET_PRODUCTOS_X_TIPORESERVABLE = "PRODUCTO.GET_PRODUCTOS_X_TIPORESERVABLE";
	public static final String GET_RESERVABLES_ALTA = "PRODUCTO.GET_RESERVABLES_ALTA";

	private Long cod;
	private Nivel1 nivel1;
	private UnidadMedida unidadMedida;
	private Servicio servicio;
	private String descripcion;
	private String tipo;
	private String soloAdmin;
	private String requiereTecnico;
	private String requiereValidacion;
	private String requiereValidacionIr;
	private String controlStock;
	private BigDecimal stock;
	private String preguntaPersonalizada;
	private String estado;
	private String requiereAnexo;
	private String consumoAsociado;
	private String facturable;
	private BigDecimal factor;
	private TipoReservable tipoReservable;
	private String presupuesto;
	private Long kronReservaMinutoAntelacion;
	private Long kronReservaMinutoPosterior;
	private Long kronPrestacionDias;
	private String kronPrestacionFechaLimite;
	private String pedirNivelBioseguridad;
	private String requiereProyecto;
	private List<TecnicoProducto> tecnicos = new ArrayList<>( 0 );
	private List<Consumo> consumos = new ArrayList<>( 0 );
	private List<Tarifa> tarifas = new ArrayList<>( 0 );
	private List<Anexo> anexos = new ArrayList<>( 0 );
	private List<ProductoTipoCertificacion> listaTiposCertificacion;
	private List<ProyectoProducto> listaProyectos;
	private List<ProductoEquipo> listaEquipos;
	private List<ReservableHorario> listaHorarios;

	public Producto() {
	}

	public Producto( Long cod, Nivel1 nivel1, UnidadMedida unidadMedida, Servicio servicio, String descripcion,
			String tipo, String soloAdmin, String requiereTecnico, String requiereValidacion, String controlStock,
			BigDecimal stock, String preguntaPersonalizada, String estado, String requiereAnexo, String consumoAsociado,
			BigDecimal factor, TipoReservable tipoReservable, List<TecnicoProducto> tecnicos, List<Consumo> consumos,
			List<Tarifa> tarifas, List<Anexo> anexos ) {
		super();
		this.cod = cod;
		this.nivel1 = nivel1;
		this.unidadMedida = unidadMedida;
		this.servicio = servicio;
		this.descripcion = descripcion;
		this.tipo = tipo;
		this.soloAdmin = soloAdmin;
		this.requiereTecnico = requiereTecnico;
		this.requiereValidacion = requiereValidacion;
		this.controlStock = controlStock;
		this.stock = stock;
		this.preguntaPersonalizada = preguntaPersonalizada;
		this.estado = estado;
		this.requiereAnexo = requiereAnexo;
		this.consumoAsociado = consumoAsociado;
		this.factor = factor;
		this.tipoReservable = tipoReservable;
		this.tecnicos = tecnicos;
		this.consumos = consumos;
		this.tarifas = tarifas;
		this.anexos = anexos;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_PRODUCTO")
	@Column(name = "COD",  nullable = false, precision=19, scale = 0)
	public Long getCod() {
		return cod;
	}

	public void setCod( Long cod ) {
		this.cod = cod;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "NIVEL")
	public Nivel1 getNivel1() {
		return nivel1;
	}

	public void setNivel1( Nivel1 nivel1 ) {
		this.nivel1 = nivel1;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COD_UNIDAD_MEDIDA")
	public UnidadMedida getUnidadMedida() {
		return unidadMedida;
	}

	public void setUnidadMedida( UnidadMedida unidadMedida ) {
		this.unidadMedida = unidadMedida;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COD_SERVICIO", nullable = false)
	public Servicio getServicio() {
		return servicio;
	}

	public void setServicio( Servicio servicio ) {
		this.servicio = servicio;
	}

	@Column(name = "DESCRIPCION", nullable = false, length = 100)
	@NotNull
	@Length(max = 100)
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion( String descripcion ) {
		this.descripcion = descripcion;
	}

	@Column(name = "TIPO", nullable = false, length = 1)
	@NotNull
	@Length(max = 1)
	public String getTipo() {
		return tipo;
	}

	public void setTipo( String tipo ) {
		this.tipo = tipo;
	}

	@Column(name = "SOLO_ADMIN", length = 2)
	@NotNull
	@Length(max = 2)
	public String getSoloAdmin() {
		return soloAdmin;
	}

	public void setSoloAdmin( String soloAdmin ) {
		this.soloAdmin = soloAdmin;
	}

	@Column(name = "REQUIERE_TECNICO", length = 2)
	@Length(max = 2)
	public String getRequiereTecnico() {
		return requiereTecnico;
	}

	public void setRequiereTecnico( String requiereTecnico ) {
		this.requiereTecnico = requiereTecnico;
	}

	@Column(name = "REQUIERE_VALIDACION", length = 2)
	@Length(max = 2)
	public String getRequiereValidacion() {
		return requiereValidacion;
	}

	public void setRequiereValidacion( String requiereValidacion ) {
		this.requiereValidacion = requiereValidacion;
	}

	@Column(name = "REQUIERE_VALIDACION_IR", length = 2)
	@Length(max = 2)
	public String getRequiereValidacionIr() {
		return requiereValidacionIr;
	}

	public void setRequiereValidacionIr( String requiereValidacionIr ) {
		this.requiereValidacionIr = requiereValidacionIr;
	}

	@Column(name = "CONTROL_STOCK", length = 2)
	@Length(max = 2)
	public String getControlStock() {
		return controlStock;
	}

	public void setControlStock( String controlStock ) {
		this.controlStock = controlStock;
	}

	@Column(name = "STOCK", precision = 10, scale = 2)
	public BigDecimal getStock() {
		return stock;
	}

	public void setStock( BigDecimal stock ) {
		this.stock = stock;
	}

	@Column(name = "PREGUNTA_PERSONALIZADA", length = 500)
	@Length(max = 500)
	public String getPreguntaPersonalizada() {
		return preguntaPersonalizada;
	}

	public void setPreguntaPersonalizada( String preguntaPersonalizada ) {
		this.preguntaPersonalizada = preguntaPersonalizada;
	}

	@Column(name = "ESTADO", length = 30)
	@Length(max = 30)
	public String getEstado() {
		return estado;
	}

	public void setEstado( String estado ) {
		this.estado = estado;
	}

	@Column(name = "REQUIERE_ANEXO", length = 2)
	@Length(max = 2)
	public String getRequiereAnexo() {
		return requiereAnexo;
	}

	public void setRequiereAnexo( String requiereAnexo ) {
		this.requiereAnexo = requiereAnexo;
	}

	@Column(name = "CONSUMO_ASOCIADO", length = 2)
	@Length(max = 2)
	public String getConsumoAsociado() {
		return consumoAsociado;
	}

	public void setConsumoAsociado( String consumoAsociado ) {
		this.consumoAsociado = consumoAsociado;
	}

	@Column(name = "FACTURABLE", length = 2)
	@Length(max = 2)
	public String getFacturable() {
		return facturable;
	}

	public void setFacturable( String facturable ) {
		this.facturable = facturable;
	}

	@Column(name = "FACTOR", nullable = true, precision = 14, scale = 4)
	public BigDecimal getFactor() {
		return factor;
	}

	public void setFactor( BigDecimal factor ) {
		this.factor = factor;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COD_TIPORESERVABLE", nullable = true)
	public TipoReservable getTipoReservable() {
		return tipoReservable;
	}

	public void setTipoReservable( TipoReservable tipoReservable ) {
		this.tipoReservable = tipoReservable;
	}

	@Column(name = "PRESUPUESTO", length = 10)
	@Length(max = 5)
	public String getPresupuesto() {
		return presupuesto;
	}

	public void setPresupuesto( String presupuesto ) {
		this.presupuesto = presupuesto;
	}

	@Column(name = "KRON_RESERVA_MINUTO_ANTELACION", precision = 10, scale = 0)
	public Long getKronReservaMinutoAntelacion() {
		return kronReservaMinutoAntelacion;
	}

	public void setKronReservaMinutoAntelacion( Long kronReservaMinutoAntelacion ) {
		this.kronReservaMinutoAntelacion = kronReservaMinutoAntelacion;
	}

	@Column(name = "KRON_RESERVA_MINUTO_POSTERIOR", precision = 10, scale = 0)
	public Long getKronReservaMinutoPosterior() {
		return kronReservaMinutoPosterior;
	}

	public void setKronReservaMinutoPosterior( Long kronReservaMinutoPosterior ) {
		this.kronReservaMinutoPosterior = kronReservaMinutoPosterior;
	}

	@Column(name = "KRON_PRESTACION_DIAS", precision = 4, scale = 0)
	public Long getKronPrestacionDias() {
		return kronPrestacionDias;
	}

	public void setKronPrestacionDias( Long kronPrestacionDias ) {
		this.kronPrestacionDias = kronPrestacionDias;
	}

	@Column(name = "KRON_PRESTACION_FECHA_LIMITE", length = 5)
	@Length(max = 5)
	public String getKronPrestacionFechaLimite() {
		return kronPrestacionFechaLimite;
	}

	public void setKronPrestacionFechaLimite( String kronPrestacionFechaLimite ) {
		this.kronPrestacionFechaLimite = kronPrestacionFechaLimite;
	}

	@Column( name = "PEDIR_NIVEL_BIOSEGURIDAD", length = 2 )
	public String getPedirNivelBioseguridad() {
		return pedirNivelBioseguridad;
	}

	public void setPedirNivelBioseguridad( String pedirNivelBioseguridad ) {
		this.pedirNivelBioseguridad = pedirNivelBioseguridad;
	}

	@Column( name = "REQUIERE_PROYECTO", length = 2 )
	public String getRequiereProyecto() {
		return requiereProyecto;
	}

	public void setRequiereProyecto( String requiereProyecto ) {
		this.requiereProyecto = requiereProyecto;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "producto")
	public List<TecnicoProducto> getTecnicos() {
		return tecnicos;
	}

	public void setTecnicos( List<TecnicoProducto> tecnicos ) {
		this.tecnicos = tecnicos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "producto")
	public List<Consumo> getConsumos() {
		return consumos;
	}

	public void setConsumos( List<Consumo> consumos ) {
		this.consumos = consumos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "producto")
	@OrderBy("descripcion ASC")
	public List<Tarifa> getTarifas() {
		return tarifas;
	}

	public void setTarifas( List<Tarifa> tarifas ) {
		this.tarifas = tarifas;
	}

	//@OneToMany(fetch = FetchType.LAZY, mappedBy = "producto" , cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "producto")
	@OrderBy("cod DESC")
	public List<Anexo> getAnexos() {
		return anexos;
	}

	public void setAnexos( List<Anexo> anexos ) {
		this.anexos = anexos;
	}

	@OneToMany( fetch = FetchType.LAZY, mappedBy = "producto" )
	public List<ProductoTipoCertificacion> getListaTiposCertificacion() {
		return listaTiposCertificacion;
	}

	public void setListaTiposCertificacion( List<ProductoTipoCertificacion> listaTiposCertificacion ) {
		this.listaTiposCertificacion = listaTiposCertificacion;
	}

	@OneToMany( fetch = FetchType.LAZY, mappedBy = "producto" )
	public List<ProyectoProducto> getListaProyectos() {
		return listaProyectos;
	}

	public void setListaProyectos( List<ProyectoProducto> listaProyectos ) {
		this.listaProyectos = listaProyectos;
	}

	@OneToMany( fetch = FetchType.LAZY, mappedBy = "producto" )
	public List<ProductoEquipo> getListaEquipos() {
		return listaEquipos;
	}

	public void setListaEquipos( List<ProductoEquipo> listaEquipos ) {
		this.listaEquipos = listaEquipos;
	}

	@OneToMany( fetch = FetchType.LAZY, mappedBy = "producto" )
	public List<ReservableHorario> getListaHorarios() {
		return listaHorarios;
	}
	
	public void setListaHorarios( List<ReservableHorario> listaHorarios ) {
		this.listaHorarios = listaHorarios;
	}

	@Transient
	public boolean isFungible(){
		return "F".equals(getTipo());
	}

	@Transient
	public boolean isReserva(){
		return "R".equals( getTipo() );
	}

	@Transient
	public boolean isPrestacion(){
		return "P".equals( getTipo() );
	}

	public Producto add(Anexo anexo){
		anexo.setProducto( this );
		getAnexos().add( anexo );
		return this;
	}

	public Producto remove(Anexo anexo){
		getAnexos().remove( anexo );
		anexo.setProducto( null );
		return this;
	}

	public Producto add( Tarifa tarifa ) {
		if ( !getTarifas().contains( tarifa ) ) {
			getTarifas().add( tarifa );
		}

		return this;
	}

	public Producto remove( Tarifa tarifa ) {
		getTarifas().remove( tarifa );
		return this;
	}



	@Transient
	public boolean isConfiguracionTecnicoCorrecta(){
		boolean correcto = false;

		if ("SI".equalsIgnoreCase(getRequiereValidacion())){
			correcto = !getTecnicos().isEmpty();
		} else {
			for (final TecnicoProducto tecnico : getTecnicos()){
				correcto = correcto || "SI".equalsIgnoreCase(tecnico.getAutomatico());
			}
		}

		return correcto;
	}


	@Override
	public int compareTo( Object o ) {
		return getDescripcion().toLowerCase().compareTo( ((Producto)o).getDescripcion().toLowerCase() );
	}

	@Transient
	public boolean isDeleteable(){
		return (( getConsumos() == null ) || getConsumos().isEmpty()) ;
	}

	@Override
	public String toString()
	{
		return String.valueOf(getCod());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = ( prime * result ) + ( ( cod == null ) ? 0 : cod.hashCode() );
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
		if ( !( obj instanceof Producto ) ) {
			return false;
		}
		final Producto other = ( Producto ) obj;
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