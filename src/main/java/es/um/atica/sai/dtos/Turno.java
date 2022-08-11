package es.um.atica.sai.dtos;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import es.um.atica.sai.entities.Reservas;

public class Turno implements Serializable{
	

	private static final long serialVersionUID = 6252044188619900717L;

	private String tipo;
	private Date fechaInicio;
	private Date fechaFin;
	private Date fechaInicioTecnico;
	private Date fechaFinTecnico;
	private boolean ocupado;
	private String motivoOcupado;
	private String iconoMotivoOcupado;
	private List<Reservas> listaReservasExistentes;
	
	public Turno() {
	}

	public Turno(String tipo, Date fechaInicio, Date fechaFin ) {
		this.tipo = tipo;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
	}
	
	public String getTipo() {
		return tipo;
	}
	
	public void setTipo( String tipo ) {
		this.tipo = tipo;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}
	
	public void setFechaInicio( Date fechaInicio ) {
		this.fechaInicio = fechaInicio;
	}
	
	public Date getFechaFin() {
		return fechaFin;
	}
	
	public void setFechaFin( Date fechaFin ) {
		this.fechaFin = fechaFin;
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

	public boolean isOcupado() {
		return ocupado;
	}
	
	public void setOcupado( boolean ocupado ) {
		this.ocupado = ocupado;
	}
	
	public String getMotivoOcupado() {
		return motivoOcupado;
	}
	
	public void setMotivoOcupado( String motivoOcupado ) {
		this.motivoOcupado = motivoOcupado;
	}
	
	public String getIconoMotivoOcupado() {
		return iconoMotivoOcupado;
	}
	
	public void setIconoMotivoOcupado( String iconoMotivoOcupado ) {
		this.iconoMotivoOcupado = iconoMotivoOcupado;
	}
	
	public List<Reservas> getListaReservasExistentes() {
		return listaReservasExistentes;
	}
	
	public void setListaReservasExistentes( List<Reservas> listaReservasExistentes ) {
		this.listaReservasExistentes = listaReservasExistentes;
	}
	

}
