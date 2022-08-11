package es.um.atica.sai.dtos;


public class ResumenConsumosExcelRespuesta {
   
	private int codRespuesta;
    private String menRespuesta;
    private byte[] ficheroExcel;
    
	public ResumenConsumosExcelRespuesta() {
	}

	public ResumenConsumosExcelRespuesta( int codRespuesta, String menRespuesta, byte[] ficheroExcel ) {
		this.codRespuesta = codRespuesta;
		this.menRespuesta = menRespuesta;
		this.ficheroExcel = ficheroExcel;
	}
	
	public int getCodRespuesta() {
		return codRespuesta;
	}
	
	public void setCodRespuesta( int codRespuesta ) {
		this.codRespuesta = codRespuesta;
	}
	
	public String getMenRespuesta() {
		return menRespuesta;
	}
	
	public void setMenRespuesta( String menRespuesta ) {
		this.menRespuesta = menRespuesta;
	}
	
	public byte[] getFicheroExcel() {
		return ficheroExcel;
	}
	
	public void setFicheroExcel( byte[] ficheroExcel ) {
		this.ficheroExcel = ficheroExcel;
	}
    
}
