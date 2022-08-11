package es.um.atica.sai.dtos;

import java.math.BigDecimal;

public class EstadisticaConsumo {

	private String nombre;
	private BigDecimal aceptacion;
	private BigDecimal media;
	private Long codigo;

	public EstadisticaConsumo() {}

	public EstadisticaConsumo( String nombre, BigDecimal media ) {
		this.nombre = nombre;
		this.media = media;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre( String nombre ) {
		this.nombre = nombre;
	}

	public BigDecimal getMedia() {
		return media;
	}

	public void setMedia( BigDecimal media ) {
		this.media = media;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo( Long codigo ) {
		this.codigo = codigo;
	}

	public BigDecimal getAceptacion() {
		return aceptacion;
	}

	public void setAceptacion( BigDecimal aceptacion ) {
		this.aceptacion = aceptacion;
	}
}
