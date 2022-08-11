package es.um.atica.sai.dtos;

import java.math.BigDecimal;

public class ConsumoCantidad {
	private Long codproducto;
	private String descripcionproducto;
	private BigDecimal cantidad;
	private Integer minutosduracion;
	private BigDecimal totalhoras;	
	private Integer minutos;

	public ConsumoCantidad() {
	}

	public ConsumoCantidad(Long codproducto, String descripcionproducto, BigDecimal cantidad ) {
		this.codproducto = codproducto;
		this.descripcionproducto = descripcionproducto;
		this.cantidad = cantidad;		
	}

	public ConsumoCantidad(Long codproducto, String descripcionproducto, BigDecimal cantidad, Integer minutos, Integer minutosduracion, BigDecimal totalhoras ) {
		this.codproducto = codproducto;
		this.descripcionproducto = descripcionproducto;
		this.cantidad = cantidad;
		this.minutosduracion = minutosduracion;
		this.totalhoras = totalhoras;
		this.minutos = minutos;
	}

	
	public Long getCodproducto() {
		return codproducto;
	}

	
	public void setCodproducto( Long codproducto ) {
		this.codproducto = codproducto;
	}

	
	public String getDescripcionproducto() {
		return descripcionproducto;
	}

	
	public void setDescripcionproducto( String descripcionproducto ) {
		this.descripcionproducto = descripcionproducto;
	}

	
	public BigDecimal getCantidad() {
		return cantidad;
	}

	
	public void setCantidad( BigDecimal cantidad ) {
		this.cantidad = cantidad;
	}

	
	public Integer getMinutosduracion() {
		return minutosduracion;
	}

	
	public void setMinutosduracion( Integer minutosduracion ) {
		this.minutosduracion = minutosduracion;
	}

	
	public BigDecimal getTotalhoras() {
		return totalhoras;
	}

	
	public void setTotalhoras( BigDecimal totalhoras ) {
		this.totalhoras = totalhoras;
	}
	
	public Integer getMinutos() {
		return minutos;
	}
	
	public void setMinutos( Integer minutos ) {
		this.minutos = minutos;
	}			

}
