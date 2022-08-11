package es.um.atica.sai.dtos;

public class Partida {
	private String eje;
	private String ejeproce;
	private String vic;
	private String unid;
	private String pro;
	private String eco;
	private String numproy;

	public Partida() {
	}

	public Partida(String eje, String ejeproce, String vic, String unid, String pro, String eco, String numproy){
		this.eje=eje;
		this.ejeproce=ejeproce;
		this.vic=vic;
		this.unid=unid;
		this.pro=pro;
		this.eco=eco;
		this.numproy=numproy;		
	}

	public String getEje() {
		return eje;
	}

	public void setEje(String eje) {
		this.eje = eje;
	}

	public String getEjeproce() {
		return ejeproce;
	}

	public void setEjeproce(String ejeproce) {
		this.ejeproce = ejeproce;
	}

	public String getVic() {
		return vic;
	}

	public void setVic(String vic) {
		this.vic = vic;
	}

	public String getUnid() {
		return unid;
	}

	public void setUnid(String unid) {
		this.unid = unid;
	}

	public String getPro() {
		return pro;
	}

	public void setPro(String pro) {
		this.pro = pro;
	}

	public String getEco() {
		return eco;
	}

	public void setEco(String eco) {
		this.eco = eco;
	}

	public String getNumproy() {
		return numproy;
	}

	public void setNumproy(String numproy) {
		this.numproy = numproy;
	}
	
	/**Devuelve la partida en formato string para mostrar en un select one
	 * 
	 * @return String descripci�n de la partida destino/ingreso
	 */
	public String getPartidaStr(){
		return this.getEje()+" "+this.getEjeproce()+" "+this.getVic()+" "+this.getUnid()+" "+this.getPro()+" "+this.getEco()+" "+this.getNumproy();
	}
	
	public boolean equals(Object other) {
        // Codigo que comprueba si las dos entidades son iguales
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof Partida))
            return false;
        Partida castOther = (Partida) other;
        return (this.getPartidaStr().equalsIgnoreCase(castOther.getPartidaStr()));
    }
    
    public int hashCode() {        
        int result = 17;
        try{
        result = 37 * result
                + (Integer.parseInt(this.getPartidaStr()));
        }catch(Exception e){
        	
        }
        return result;
    }
	
}