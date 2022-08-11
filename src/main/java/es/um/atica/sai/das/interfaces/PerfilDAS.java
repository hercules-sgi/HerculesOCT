package es.um.atica.sai.das.interfaces;

import java.util.List;

import javax.ejb.Local;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.entities.Perfil;

@Local
public interface PerfilDAS extends DataAccessService<Perfil> {
    

    public static final String NAME = "perfilDAS";

	Perfil getPerfilByTag(String tagPerfil);
	List<Perfil> getListaPerfiles();
	List<Perfil> getPerfilesPuedeFiltrar(String sql);
    
}
