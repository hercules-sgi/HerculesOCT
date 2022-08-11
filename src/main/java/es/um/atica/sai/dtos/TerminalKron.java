package es.um.atica.sai.dtos;


public class TerminalKron {
	private String ip;
	private String lector;
	private String nombreEdificio;
	private String nombreLector;
	
	public TerminalKron() {
	}

	public TerminalKron( String ip, String lector, String nombreEdificio, String nombreLector ) {
		this.ip = ip;
		this.lector = lector;
		this.nombreEdificio = nombreEdificio;
		this.nombreLector = nombreLector;
	}

	public String getIp() {
		return ip;
	}
	
	public void setIp( String ip ) {
		this.ip = ip;
	}
	
	public String getLector() {
		return lector;
	}
	
	public void setLector( String lector ) {
		this.lector = lector;
	}

	
	public String getNombreEdificio() {
		return nombreEdificio;
	}

	
	public void setNombreEdificio( String nombreEdificio ) {
		this.nombreEdificio = nombreEdificio;
	}

	
	public String getNombreLector() {
		return nombreLector;
	}

	
	public void setNombreLector( String nombreLector ) {
		this.nombreLector = nombreLector;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( ip == null ) ? 0 : ip.hashCode() );
		result = prime * result + ( ( lector == null ) ? 0 : lector.hashCode() );
		return result;
	}

	@Override
	public boolean equals( Object obj ) {
		if ( this == obj ) {
			return true;
		}
		if ( !( obj instanceof TerminalKron ) ) {
			return false;
		}
		TerminalKron other = ( TerminalKron ) obj;
		if ( ip == null ) {
			if ( other.getIp() != null ) {
				return false;
			}
		} else if ( !ip.equals( other.getIp() ) ) {
			return false;
		}
		if ( lector == null ) {
			if ( other.getLector() != null ) {
				return false;
			}
		} else if ( !lector.equals( other.getLector() ) ) {
			return false;
		}
		return true;
	}
	
	
}
