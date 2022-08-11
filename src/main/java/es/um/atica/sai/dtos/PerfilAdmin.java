package es.um.atica.sai.dtos;

import java.io.Serializable;

public class PerfilAdmin implements Serializable{
	
	private static final long serialVersionUID = -522086893760469153L;

	private String tagPerfil;
	private Long codServicio;
		
	
	public PerfilAdmin() {
	}

	public PerfilAdmin( String tagPerfil, Long codServicio ) {
		this.tagPerfil = tagPerfil;
		this.codServicio = codServicio;
	}

	
	public String getTagPerfil() {
		return tagPerfil;
	}

	
	public void setTagPerfil( String tagPerfil ) {
		this.tagPerfil = tagPerfil;
	}

	
	public Long getCodServicio() {
		return codServicio;
	}

	
	public void setCodServicio( Long codServicio ) {
		this.codServicio = codServicio;
	}

	
	
}
