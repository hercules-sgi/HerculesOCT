package es.um.atica.sai.entities;
// Generated 24-jun-2009 10:06:20 by Hibernate Tools 3.2.2.GA

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

/**
 * Tarifa generated by hbm2java
 */
@Entity
@Table(name = "TARIFA")
@NamedQueries ( { 
	@NamedQuery (name = Tarifa.EXISTE_TARIFA, query = "SELECT tar FROM Tarifa tar WHERE tar.producto.cod=:codproducto AND tar.tipoTarifa.cod=:codtipotarifa"),
	@NamedQuery (name = Tarifa.GET_TARIFAS_BY_PRODUCTO_CANTIDAD, query = "SELECT tar FROM Tarifa tar WHERE tar.producto.cod=:producto ORDER BY tar.cantidadInicial DESC")
})
public class Tarifa implements java.io.Serializable {

    private static final long serialVersionUID = 130957039185222206L;

    public static final String EXISTE_TARIFA = "TARIFA.EXISTE_TARIFA";   
    public static final String GET_TARIFAS_BY_PRODUCTO_CANTIDAD = "TARIFA.GET_TARIFAS_BY_PRODUCTO_CANTIDAD";

	private TarifaId id;
	private Producto producto;
	private TipoTarifa tipoTarifa;
    private Long cantidadInicial;
	private String codTarifaJusto;
	private String codUdGasto;
	private String descripcion;

	public Tarifa() {
	}

	public Tarifa( TarifaId id, Producto producto, TipoTarifa tipoTarifa, String codTarifaJusto, String codUdGasto,	String descripcion ) {
		this.id = id;
		this.producto = producto;
		this.tipoTarifa = tipoTarifa;
		this.codTarifaJusto = codTarifaJusto;
		this.codUdGasto = codUdGasto;
		this.descripcion = descripcion;
	}

    @EmbeddedId
    @AttributeOverrides({           
            @AttributeOverride(name = "codProducto", column = @Column(name = "COD_PRODUCTO", nullable = false, precision = 19, scale = 0)),
            @AttributeOverride(name = "codTipoTarifa", column = @Column(name = "COD_TIPO_TARIFA", nullable = false, precision = 19, scale = 0))})
	public TarifaId getId() {
		return id;
	}

	
	public void setId( TarifaId id ) {
		this.id = id;
	}

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COD_PRODUCTO", nullable = false, insertable = false, updatable = false)
    @NotNull
	public Producto getProducto() {
		return producto;
	}

	public void setProducto( Producto producto ) {
		this.producto = producto;
	}

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COD_TIPO_TARIFA", nullable = false, insertable = false, updatable = false)
    @NotNull
	public TipoTarifa getTipoTarifa() {
		return tipoTarifa;
	}
	
	public void setTipoTarifa( TipoTarifa tipoTarifa ) {
		this.tipoTarifa = tipoTarifa;
	}

    @Column(name = "CANTIDAD_INICIAL", nullable = false, precision = 19, scale = 0, insertable = false, updatable = false)
	public Long getCantidadInicial() {
		return cantidadInicial;
	}
	
	public void setCantidadInicial( Long cantidadInicial ) {
		this.cantidadInicial = cantidadInicial;
	}

    @Column(name = "COD_TARIFA_JUSTO", nullable = false, length = 20)
    @NotNull
    @Length(max = 20)
	public String getCodTarifaJusto() {
		return codTarifaJusto;
	}

	
	public void setCodTarifaJusto( String codTarifaJusto ) {
		this.codTarifaJusto = codTarifaJusto;
	}

    @Column(name = "COD_UD_GASTO", nullable = false, length = 15)
    @NotNull
    @Length(max = 15)
	public String getCodUdGasto() {
		return codUdGasto;
	}

	
	public void setCodUdGasto( String codUdGasto ) {
		this.codUdGasto = codUdGasto;
	}

    @Column(name = "DESCRIPCION", nullable = true, length = 200)
    @Length(max = 200)
	public String getDescripcion() {
		return descripcion;
	}

	
	public void setDescripcion( String descripcion ) {
		this.descripcion = descripcion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( cantidadInicial == null ) ? 0 : cantidadInicial.hashCode() );
		result = prime * result + ( ( codTarifaJusto == null ) ? 0 : codTarifaJusto.hashCode() );
		result = prime * result + ( ( codUdGasto == null ) ? 0 : codUdGasto.hashCode() );
		result = prime * result + ( ( descripcion == null ) ? 0 : descripcion.hashCode() );
		result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
		result = prime * result + ( ( producto == null ) ? 0 : producto.hashCode() );
		result = prime * result + ( ( tipoTarifa == null ) ? 0 : tipoTarifa.hashCode() );
		return result;
	}

	@Override
	public boolean equals( Object obj ) {
		if ( this == obj )
			return true;
		if ( !( obj instanceof Tarifa ) )
			return false;
		Tarifa other = ( Tarifa ) obj;
		if ( cantidadInicial == null ) {
			if ( other.getCantidadInicial() != null )
				return false;
		} else if ( !cantidadInicial.equals( other.getCantidadInicial() ) )
			return false;
		if ( codTarifaJusto == null ) {
			if ( other.getCodTarifaJusto() != null )
				return false;
		} else if ( !codTarifaJusto.equals( other.getCodTarifaJusto() ) )
			return false;
		if ( codUdGasto == null ) {
			if ( other.getCodUdGasto() != null )
				return false;
		} else if ( !codUdGasto.equals( other.getCodUdGasto() ) )
			return false;
		if ( descripcion == null ) {
			if ( other.getDescripcion() != null )
				return false;
		} else if ( !descripcion.equals( other.getDescripcion() ) )
			return false;
		if ( id == null ) {
			if ( other.getId() != null )
				return false;
		} else if ( !id.equals( other.getId() ) )
			return false;
		if ( producto == null ) {
			if ( other.getProducto() != null )
				return false;
		} else if ( !producto.equals( other.getProducto() ) )
			return false;
		if ( tipoTarifa == null ) {
			if ( other.getTipoTarifa() != null )
				return false;
		} else if ( !tipoTarifa.equals( other.getTipoTarifa() ) )
			return false;
		return true;
	}


	
}