package es.um.atica.sai.das.interfaces;

import java.util.List;

import javax.ejb.Local;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.entities.EntidadesIr;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;


@Local
public interface EntidadesIrDAS extends DataAccessService<EntidadesIr> {

	public static final String NAME = "entidadesIrDAS";
	
    List<EntidadesIr> getEntidadesIrByUsuario(Usuario usuario);
    boolean existeEntidadIr(EntidadesIr entidadIr);
    void crear( EntidadesIr eir ) throws SaiException;
    void eliminar( EntidadesIr eir ) throws SaiException;
}
