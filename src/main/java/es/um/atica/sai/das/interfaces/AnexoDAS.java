package es.um.atica.sai.das.interfaces;

import java.util.List;

import javax.ejb.Local;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.entities.Anexo;
import es.um.atica.sai.entities.Consumo;
import es.um.atica.sai.exceptions.SaiException;

@Local
public interface AnexoDAS extends DataAccessService<Anexo> {

	String NAME = "anexoDAS";

	void eliminar(Anexo a) throws SaiException;
	void crear(Anexo a) throws SaiException;
	void modificar(Anexo a) throws SaiException;
	List<Anexo> getAnexosByConsumo(Consumo c);
	List<Anexo> getAnexosBioseguridadByConsumo(Consumo c);
	List<Anexo> getAnexosConsumoByConsumo(Consumo c);
	Anexo getAnexoByTag(String tag);
}
