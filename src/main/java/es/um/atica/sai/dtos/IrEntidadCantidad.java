package es.um.atica.sai.dtos;

import java.math.BigDecimal;

public class IrEntidadCantidad {
	private Long codusuarioir;
	private String nombreir;
	private Long codentidadpagadora;
	private String nombreentidadpagadora;
	private BigDecimal cantidad;

	public IrEntidadCantidad() {
	}

	public IrEntidadCantidad( Long codusuarioir, String nombreir, Long codentidadpagadora, String nombreentidadpagadora,
			BigDecimal cantidad ) {
		super();
		this.codusuarioir = codusuarioir;
		this.nombreir = nombreir;
		this.codentidadpagadora = codentidadpagadora;
		this.nombreentidadpagadora = nombreentidadpagadora;
		this.cantidad = cantidad;
	}

	
	public Long getCodusuarioir() {
		return codusuarioir;
	}

	
	public void setCodusuarioir( Long codusuarioir ) {
		this.codusuarioir = codusuarioir;
	}

	
	public String getNombreir() {
		return nombreir;
	}

	
	public void setNombreir( String nombreir ) {
		this.nombreir = nombreir;
	}

	
	public Long getCodentidadpagadora() {
		return codentidadpagadora;
	}

	
	public void setCodentidadpagadora( Long codentidadpagadora ) {
		this.codentidadpagadora = codentidadpagadora;
	}

	
	public String getNombreentidadpagadora() {
		return nombreentidadpagadora;
	}

	
	public void setNombreentidadpagadora( String nombreentidadpagadora ) {
		this.nombreentidadpagadora = nombreentidadpagadora;
	}

	
	public BigDecimal getCantidad() {
		return cantidad;
	}

	
	public void setCantidad( BigDecimal cantidad ) {
		this.cantidad = cantidad;
	}



}
