package es.um.atica.sai.das.interfaces;

import java.util.List;

import org.primefaces.model.SortOrder;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.jpa.das.ResultQuery;
import es.um.atica.sai.entities.DatosUsuarioView;


public interface DatosUsuarioViewDAS extends  DataAccessService<DatosUsuarioView>
{

	public static final String NAME = "datosUsuarioViewDAS";

    DatosUsuarioView findUsuarioViewByDni(String dniStr);
    DatosUsuarioView findUsuarioViewByEmail( String emailStr ) ;
    List<DatosUsuarioView> getListaUsuariosSolicitantes(String sql);
    ResultQuery<DatosUsuarioView> busquedaUsuarios(String sql, int first, int pageSize, String sortField, SortOrder sortOrder);
    Long getCountBusquedaUsuarios(String sql);
    ResultQuery<DatosUsuarioView> busquedaUsuariosIr(int first, int pageSize, String sortField, SortOrder sortOrder);
    Long getCountBusquedaUsuariosIr();
}
