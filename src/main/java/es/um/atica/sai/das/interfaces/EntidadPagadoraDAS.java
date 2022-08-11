package es.um.atica.sai.das.interfaces;

import java.util.List;

import javax.ejb.Local;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.entities.EntidadPagadora;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;


@Local
public interface EntidadPagadoraDAS extends DataAccessService<EntidadPagadora> {

    public static final String NAME = "entidadPagadoraDAS";
    
    void crear( EntidadPagadora entidad ) throws SaiException;
    void modificar( EntidadPagadora entidad ) throws SaiException;
    void eliminar( EntidadPagadora entidad ) throws SaiException;
    
    List<EntidadPagadora> getListaEntidadesPagadorasActivas();
    List<EntidadPagadora> buscarEntidadesPagadoras();
    List<EntidadPagadora> getEntidadesByIr(Usuario usuario);
    boolean existeEntidadCif(String cif, Integer codSubtercero);
    boolean existeOtraEntidadCif(EntidadPagadora ep);
    boolean existeEntidadUnidadAdministrativa(String udadmin);
    EntidadPagadora getEntidadPagadoraByCif(String cif, Integer codSubtercero);
    EntidadPagadora getEntidadPagadoraByUdadmin(String udadmin);
    
}
