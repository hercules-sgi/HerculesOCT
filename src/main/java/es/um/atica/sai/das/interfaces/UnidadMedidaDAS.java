package es.um.atica.sai.das.interfaces;

import java.util.List;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.entities.UnidadMedida;


public interface UnidadMedidaDAS extends DataAccessService<UnidadMedida> {

    public static final String NAME = "unidadMedidaDAS";
    
    public List<UnidadMedida> list();
    public void guardar(UnidadMedida um);
    public void eliminar(UnidadMedida um);
}
