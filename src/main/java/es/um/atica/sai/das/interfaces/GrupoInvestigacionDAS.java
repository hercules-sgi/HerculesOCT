package es.um.atica.sai.das.interfaces;

import javax.ejb.Local;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.entities.EntidadPagadora;
import es.um.atica.sai.entities.GrupoInvestigacion;
import es.um.atica.sai.exceptions.SaiException;

@Local
public interface GrupoInvestigacionDAS extends DataAccessService<GrupoInvestigacion> {

	public static final String NAME = "grupoInvestigacionDAS";

	GrupoInvestigacion getGrupoInvestigacionByEntidadPagadora(EntidadPagadora ep) throws SaiException;
}
