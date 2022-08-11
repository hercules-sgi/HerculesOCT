package es.um.atica.sai.dtos;


public class EntidadRespuesta {
	private Object entidad;
	private String respuesta;

	public EntidadRespuesta() {
	}

	public EntidadRespuesta( Object entidad, String respuesta ) {
		this.entidad = entidad;
		this.respuesta = respuesta;
	}
	
	public Object getEntidad() {
		return entidad;
	}
	
	public void setEntidad( Object entidad ) {
		this.entidad = entidad;
	}
	
	public String getRespuesta() {
		return respuesta;
	}
	
	public void setRespuesta( String respuesta ) {
		this.respuesta = respuesta;
	}


	
	
}
