package es.um.atica.sai.dtos;

import java.math.BigDecimal;

import es.um.atica.sai.entities.Equipo;

public class EquipoProductividad {
	private Equipo equipo;
	private BigDecimal importePreventivas;
	private BigDecimal importeCorrectivas;
	private BigDecimal importeEvolutivas;
	private BigDecimal importeTotalIntervenciones;
	private Integer minutosReserva;
	private Integer minutosPrestacion;
	private Integer minutosTotal;
	private Integer numUnidadesReserva;
	private Integer numUnidadesPrestacion;
	private Integer numUnidadesTotal;
	private BigDecimal importeReservas;
	private BigDecimal importePrestaciones;
	private BigDecimal importeTotalConsumos;

	public EquipoProductividad() {
		super();
	}

	public Equipo getEquipo() {
		return equipo;
	}
	
	public void setEquipo( Equipo equipo ) {
		this.equipo = equipo;
	}

	
	public BigDecimal getImportePreventivas() {
		return importePreventivas;
	}

	
	public void setImportePreventivas( BigDecimal importePreventivas ) {
		this.importePreventivas = importePreventivas;
	}

	
	public BigDecimal getImporteCorrectivas() {
		return importeCorrectivas;
	}

	
	public void setImporteCorrectivas( BigDecimal importeCorrectivas ) {
		this.importeCorrectivas = importeCorrectivas;
	}

	
	public BigDecimal getImporteEvolutivas() {
		return importeEvolutivas;
	}
	
	public void setImporteEvolutivas( BigDecimal importeEvolutivas ) {
		this.importeEvolutivas = importeEvolutivas;
	}

	public BigDecimal getImporteTotalIntervenciones() {
		return importeTotalIntervenciones;
	}
	
	public void setImporteTotalIntervenciones( BigDecimal importeTotalIntervenciones ) {
		this.importeTotalIntervenciones = importeTotalIntervenciones;
	}
	
	public Integer getMinutosReserva() {
		return minutosReserva;
	}
	
	public void setMinutosReserva( Integer minutosReserva ) {
		this.minutosReserva = minutosReserva;
	}
	
	public Integer getMinutosPrestacion() {
		return minutosPrestacion;
	}
	
	public void setMinutosPrestacion( Integer minutosPrestacion ) {
		this.minutosPrestacion = minutosPrestacion;
	}
	
	public Integer getMinutosTotal() {
		return minutosTotal;
	}
	
	public void setMinutosTotal( Integer minutosTotal ) {
		this.minutosTotal = minutosTotal;
	}
	
	public Integer getNumUnidadesReserva() {
		return numUnidadesReserva;
	}

	
	public void setNumUnidadesReserva( Integer numUnidadesReserva ) {
		this.numUnidadesReserva = numUnidadesReserva;
	}

	
	public Integer getNumUnidadesPrestacion() {
		return numUnidadesPrestacion;
	}

	
	public void setNumUnidadesPrestacion( Integer numUnidadesPrestacion ) {
		this.numUnidadesPrestacion = numUnidadesPrestacion;
	}

	
	public Integer getNumUnidadesTotal() {
		return numUnidadesTotal;
	}

	
	public void setNumUnidadesTotal( Integer numUnidadesTotal ) {
		this.numUnidadesTotal = numUnidadesTotal;
	}

	public BigDecimal getImporteReservas() {
		return importeReservas;
	}

	
	public void setImporteReservas( BigDecimal importeReservas ) {
		this.importeReservas = importeReservas;
	}

	
	public BigDecimal getImportePrestaciones() {
		return importePrestaciones;
	}

	
	public void setImportePrestaciones( BigDecimal importePrestaciones ) {
		this.importePrestaciones = importePrestaciones;
	}

	
	public BigDecimal getImporteTotalConsumos() {
		return importeTotalConsumos;
	}

	
	public void setImporteTotalConsumos( BigDecimal importeTotalConsumos ) {
		this.importeTotalConsumos = importeTotalConsumos;
	}

	
	
}
