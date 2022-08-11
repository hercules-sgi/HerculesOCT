package es.um.atica.sai.dtos;

import java.math.BigDecimal;
import java.util.Date;

import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.Proyecto;

public class LineaFungible {
	private Long cod;
	private Producto producto;
	private Proyecto proyecto;
	private BigDecimal cantidad;
	private String estado;
	private Date fechaSolicitud;
	
	
	
	public LineaFungible() {
	}



	public LineaFungible( Long cod, Producto producto, Proyecto proyecto, BigDecimal cantidad, String estado, Date fechaSolicitud ) 
	{
		this.cod = cod;
		this.producto = producto;
		this.proyecto = proyecto;
		this.cantidad = cantidad;
		this.estado = estado;
		this.fechaSolicitud = fechaSolicitud;
	}

	public Long getCod() {
		return cod;
	}

	public void setCod( Long cod ) {
		this.cod = cod;
	}
	
	public Producto getProducto() {
		return producto;
	}

	public void setProducto( Producto producto ) {
		this.producto = producto;
	}
	
	public Proyecto getProyecto() {
		return proyecto;
	}
	
	public void setProyecto( Proyecto proyecto ) {
		this.proyecto = proyecto;
	}

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad( BigDecimal cantidad ) {
		this.cantidad = cantidad;
	}
	
	public String getEstado() {
		return estado;
	}
	
	public void setEstado( String estado ) {
		this.estado = estado;
	}
	
	public Date getFechaSolicitud() {
		return fechaSolicitud;
	}

	public void setFechaSolicitud( Date fechaSolicitud ) {
		this.fechaSolicitud = fechaSolicitud;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( cantidad == null ) ? 0 : cantidad.hashCode() );
		result = prime * result + ( ( cod == null ) ? 0 : cod.hashCode() );
		result = prime * result + ( ( estado == null ) ? 0 : estado.hashCode() );
		result = prime * result + ( ( fechaSolicitud == null ) ? 0 : fechaSolicitud.hashCode() );
		result = prime * result + ( ( producto == null ) ? 0 : producto.hashCode() );
		return result;
	}



	@Override
	public boolean equals( Object obj ) {
		if ( this == obj )
			return true;
		if ( !( obj instanceof LineaFungible ) )
			return false;
		LineaFungible other = ( LineaFungible ) obj;
		if ( cod == null ) {
			if ( other.getCod() != null )
				return false;
		} else if ( !cod.equals( other.getCod() ) )
			return false;
		else
			return true;
		if ( cantidad == null ) {
			if ( other.getCantidad() != null )
				return false;
		} else if ( !cantidad.equals( other.getCantidad() ) )
			return false;
		if ( estado == null ) {
			if ( other.getEstado() != null )
				return false;
		} else if ( !estado.equals( other.getEstado() ) )
			return false;
		if ( fechaSolicitud == null ) {
			if ( other.getFechaSolicitud() != null )
				return false;
		} else if ( !fechaSolicitud.equals( other.getFechaSolicitud() ) )
			return false;
		if ( producto == null ) {
			if ( other.getProducto() != null )
				return false;
		} else if ( !producto.equals( other.getProducto() ) )
			return false;
		return true;
	}

	
	
}
