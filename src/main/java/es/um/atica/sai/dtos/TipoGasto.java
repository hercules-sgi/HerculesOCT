package es.um.atica.sai.dtos;

public class TipoGasto {
	private String tipgasto;
	private String descri;

	public TipoGasto() {
	}
	
	public TipoGasto(String tipgasto, String descri) {
		this.tipgasto=tipgasto;
		this.descri=descri;
	}

	public String getTipgasto() {
		return tipgasto;
	}

	public void setTipgasto(String tipgasto) {
		this.tipgasto = tipgasto;
	}

	public String getDescri() {
		return descri;
	}

	public void setDescri(String descri) {
		this.descri = descri;
	}
	
	public boolean equals(Object other) {
        // Codigo que comprueba si las dos entidades son iguales
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof TipoGasto))
            return false;
        TipoGasto castOther = (TipoGasto) other;
        return (this.getTipgasto().equalsIgnoreCase(castOther.getTipgasto()));
    }
    
    public int hashCode() {        
        int result = 17;
        try{
        result = 37 * result
                + (Integer.parseInt(this.getTipgasto()));
        }catch(Exception e){

        }
        return result;
    }
}