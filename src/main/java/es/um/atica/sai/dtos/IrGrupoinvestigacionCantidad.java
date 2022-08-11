package es.um.atica.sai.dtos;

import java.math.BigDecimal;

public class IrGrupoinvestigacionCantidad {
	private Long codusuarioir;
	private String nombreir;
	private Long codgrupoinvestigacion;
	private String nombregrupoinvestigacion;
	private BigDecimal cantidad;

	public IrGrupoinvestigacionCantidad() {
	}

	public IrGrupoinvestigacionCantidad( Long codusuarioir, String nombreir, Long codgrupoinvestigacion, String nombregrupoinvestigacion,
			BigDecimal cantidad ) {
		super();
		this.codusuarioir = codusuarioir;
		this.nombreir = nombreir;
		this.codgrupoinvestigacion = codgrupoinvestigacion;
		this.nombregrupoinvestigacion = nombregrupoinvestigacion;
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

	public Long getCodgrupoinvestigacion() {
		return codgrupoinvestigacion;
	}
	
	public void setCodgrupoinvestigacion( Long codgrupoinvestigacion ) {
		this.codgrupoinvestigacion = codgrupoinvestigacion;
	}

	
	public String getNombregrupoinvestigacion() {
		return nombregrupoinvestigacion;
	}

	
	public void setNombregrupoinvestigacion( String nombregrupoinvestigacion ) {
		this.nombregrupoinvestigacion = nombregrupoinvestigacion;
	}

	public BigDecimal getCantidad() {
		return cantidad;
	}

	
	public void setCantidad( BigDecimal cantidad ) {
		this.cantidad = cantidad;
	}



}
