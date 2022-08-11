package es.um.atica.sai.dtos;


public class Dependencia {

	String tipo;
    String codigo;
    String bloque;
    String planta;
    Integer numDep;
	
    public Dependencia() {
		super();
	}

	public Dependencia( String tipo, String codigo, String bloque, String planta, Integer numDep ) {
		super();
		this.tipo = tipo;
		this.codigo = codigo;
		this.bloque = bloque;
		this.planta = planta;
		this.numDep = numDep;
	}

	
	public String getTipo() {
		return tipo;
	}

	
	public void setTipo( String tipo ) {
		this.tipo = tipo;
	}

	
	public String getCodigo() {
		return codigo;
	}

	
	public void setCodigo( String codigo ) {
		this.codigo = codigo;
	}

	
	public String getBloque() {
		return bloque;
	}

	
	public void setBloque( String bloque ) {
		this.bloque = bloque;
	}

	
	public String getPlanta() {
		return planta;
	}

	
	public void setPlanta( String planta ) {
		this.planta = planta;
	}

	
	public Integer getNumDep() {
		return numDep;
	}

	
	public void setNumDep( Integer numDep ) {
		this.numDep = numDep;
	}
	

	
}
