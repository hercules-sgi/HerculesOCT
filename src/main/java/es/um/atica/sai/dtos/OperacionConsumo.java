package es.um.atica.sai.dtos;


public enum OperacionConsumo {

	MODIFICAR, GUARDAR, ENTREGAR, ACEPTAR, VALIDAR, FINALIZAR, RECHAZAR, REACTIVAR, READMITIR, ANULAR, MUESTRA, ANEXO,
	SUBCONSUMO, COMENTAR, COMENTAR_FACTURACION, AVISAR, SET_EQUIPOS;

	public String getName() {
		return name().toLowerCase();
	}

	public static OperacionConsumo getGuardar() {
		return OperacionConsumo.GUARDAR;
	}

	public static OperacionConsumo getModificar() {
		return OperacionConsumo.MODIFICAR;
	}

	public static OperacionConsumo getEntregar() {
		return OperacionConsumo.ENTREGAR;
	}

	public static OperacionConsumo getAceptar() {
		return OperacionConsumo.ACEPTAR;
	}

	public static OperacionConsumo getValidar() {
		return OperacionConsumo.VALIDAR;
	}

	public static OperacionConsumo getFinalizar() {
		return OperacionConsumo.FINALIZAR;
	}

	public static OperacionConsumo getRechazar() {
		return OperacionConsumo.RECHAZAR;
	}

	public static OperacionConsumo getReactivar() {
		return OperacionConsumo.REACTIVAR;
	}

	public static OperacionConsumo getReadmitir() {
		return OperacionConsumo.READMITIR;
	}

	public static OperacionConsumo getAnular() {
		return OperacionConsumo.ANULAR;
	}

	public static OperacionConsumo getMuestra() {
		return OperacionConsumo.MUESTRA;
	}

	public static OperacionConsumo getAnexo() {
		return OperacionConsumo.ANEXO;
	}

	public static OperacionConsumo getSubconsumo() {
		return OperacionConsumo.SUBCONSUMO;
	}

	public static OperacionConsumo getComentar() {
		return OperacionConsumo.COMENTAR;
	}

	public static OperacionConsumo getComentarFacturacion() {
		return OperacionConsumo.COMENTAR_FACTURACION;
	}
	
	public static OperacionConsumo getAvisar() {
		return OperacionConsumo.AVISAR;
	}
	
	public static OperacionConsumo getSetEquipos() {
		return OperacionConsumo.SET_EQUIPOS;
	}
}
