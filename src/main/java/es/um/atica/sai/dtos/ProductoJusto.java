package es.um.atica.sai.dtos;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductoJusto implements Serializable {
	
    /**
     * 
     */
    private static final long serialVersionUID = 5771914531145970884L;
    
	private String codProductoJusto;
	private String unidadAdministrativa;
	private String descripcion;
	private BigDecimal precio;
	
	public ProductoJusto(String cpj, String ua, String descrip, BigDecimal precio){
		this.codProductoJusto= cpj;
		this.unidadAdministrativa=ua;
		this.descripcion=descrip;
		this.precio=precio;
	}
	
	
	
	public ProductoJusto(){
		this.codProductoJusto=null;
		this.unidadAdministrativa=null;
		this.descripcion=null;
		this.precio= new BigDecimal(0);
	}

	public String getCodProductoJusto() {
		return codProductoJusto;
	}

	public void setCodProductoJusto(String codProductoJusto) {
		this.codProductoJusto = codProductoJusto;
	}

	public String getUnidadAdministrativa() {
		return unidadAdministrativa;
	}

	public void setUnidadAdministrativa(String unidadAdministrativa) {
		this.unidadAdministrativa = unidadAdministrativa;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public BigDecimal getPrecio() {
		return precio;
	}

	public String getTarifaCombo() {
		return new StringBuilder(descripcion).append(" - ").append(precio.toString()).append(" Euros").toString();
	}
	
	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( codProductoJusto == null ) ? 0 : codProductoJusto.hashCode() );
		result = prime * result + ( ( unidadAdministrativa == null ) ? 0 : unidadAdministrativa.hashCode() );
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
		ProductoJusto other = ( ProductoJusto ) obj;
		if ( codProductoJusto == null ) {
			if ( other.codProductoJusto != null )
				return false;
		} else if ( !codProductoJusto.equals( other.codProductoJusto ) )
			return false;
		if ( unidadAdministrativa == null ) {
			if ( other.unidadAdministrativa != null )
				return false;
		} else if ( !unidadAdministrativa.equals( other.unidadAdministrativa ) )
			return false;
		return true;
	}
	
	
}
