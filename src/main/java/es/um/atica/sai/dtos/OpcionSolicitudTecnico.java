package es.um.atica.sai.dtos;


public class OpcionSolicitudTecnico {
	private String codigo;
	private String descripcion;
	
	public OpcionSolicitudTecnico() {
	}

	public OpcionSolicitudTecnico( String codigo, String descripcion ) 
	{
		this.codigo = codigo;
		this.descripcion = descripcion;
	}

	public String getCodigo() {
		return codigo;
	}
	
	public void setCodigo( String codigo ) {
		this.codigo = codigo;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion( String descripcion ) {
		this.descripcion = descripcion;
	}
	
	
}
