package es.um.atica.sai.dtos;

public class Subtercero {
	private String codigo;
	private String nombre;

	public Subtercero() {
	}

	public Subtercero( String codigo, String nombre ) {
		this.codigo = codigo;
		this.nombre = nombre;
	}

	
	public String getCodigo() {
		return codigo;
	}

	
	public void setCodigo( String codigo ) {
		this.codigo = codigo;
	}

	
	public String getNombre() {
		return nombre;
	}

	
	public void setNombre( String nombre ) {
		this.nombre = nombre;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( codigo == null ) ? 0 : codigo.hashCode() );
		result = prime * result + ( ( nombre == null ) ? 0 : nombre.hashCode() );
		return result;
	}

	@Override
	public boolean equals( Object obj ) {
		if ( this == obj )
			return true;
		if ( obj == null )
			return false;
		if ( getClass() != obj.getClass() )
			return false;
		Subtercero other = ( Subtercero ) obj;
		if ( codigo == null ) {
			if ( other.codigo != null )
				return false;
		} else if ( !codigo.equals( other.codigo ) )
			return false;
		if ( nombre == null ) {
			if ( other.nombre != null )
				return false;
		} else if ( !nombre.equals( other.nombre ) )
			return false;
		return true;
	}


	
}