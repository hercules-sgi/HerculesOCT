package es.um.atica.sai.dtos;

import java.util.Date;

public class OcupacionTecnico {
	
	private Date fechaInicioTecnico;
	private Date fechaFinTecnico;
	
	public OcupacionTecnico() {
	}

	public OcupacionTecnico( Date fechaInicioTecnico, Date fechaFinTecnico ) {
		this.fechaInicioTecnico = fechaInicioTecnico;
		this.fechaFinTecnico = fechaFinTecnico;
	}
	
	public Date getFechaInicioTecnico() {
		return fechaInicioTecnico;
	}
	
	public void setFechaInicioTecnico( Date fechaInicioTecnico ) {
		this.fechaInicioTecnico = fechaInicioTecnico;
	}
	
	public Date getFechaFinTecnico() {
		return fechaFinTecnico;
	}
	
	public void setFechaFinTecnico( Date fechaFinTecnico ) {
		this.fechaFinTecnico = fechaFinTecnico;
	}
	
	
}
