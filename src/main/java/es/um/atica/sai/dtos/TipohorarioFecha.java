package es.um.atica.sai.dtos;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import es.um.atica.sai.entities.TipoHorario;

public class TipohorarioFecha implements Serializable {


	private static final long serialVersionUID = -5145485056392080737L;

	private TipoHorario tipoHorario;
	private Date fecha;
	private List<Turno> listaTurnos;
		
	public TipohorarioFecha() {
	}

	public TipohorarioFecha( TipoHorario tipoHorario, Date fecha ) {
		this.tipoHorario = tipoHorario;
		this.fecha = fecha;
	}
	
	public TipoHorario getTipoHorario() {
		return tipoHorario;
	}
	
	public void setTipoHorario( TipoHorario tipoHorario ) {
		this.tipoHorario = tipoHorario;
	}
	
	public Date getFecha() {
		return fecha;
	}
	
	public void setFecha( Date fecha ) {
		this.fecha = fecha;
	}
	
	public List<Turno> getListaTurnos() {
		return listaTurnos;
	}
	
	public void setListaTurnos( List<Turno> listaTurnos ) {
		this.listaTurnos = listaTurnos;
	}
	
	
}
