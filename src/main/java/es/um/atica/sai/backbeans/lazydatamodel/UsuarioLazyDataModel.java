package es.um.atica.sai.backbeans.lazydatamodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.log.Log;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import es.um.atica.faces.model.FundeWebLazyDataModel;
import es.um.atica.jpa.das.ResultQuery;
import es.um.atica.sai.entities.DatosUsuarioView;
import es.um.atica.sai.services.interfaces.UsuarioService;


public class UsuarioLazyDataModel extends FundeWebLazyDataModel<DatosUsuarioView> {
        
    private static final long serialVersionUID = 6724430095707490597L;

    @Logger
    private Log log;
 
    private UsuarioService servicioUsuario;

	@Override
    public List<DatosUsuarioView> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) 
    {
    	ResultQuery<DatosUsuarioView> resultado = getServicioUsuario().busquedaUsuarios( first, pageSize, sortField, sortOrder);
    	this.setRowCount(getServicioUsuario().getCountBusquedaUsuarios().intValue());
    	if (resultado==null)
    	{
    		return new ArrayList<>(0);
    	}
    	return resultado.getResultList();
    }

    
    private UsuarioService getServicioUsuario() {
        if ( servicioUsuario == null ) {
            this.servicioUsuario = ( UsuarioService ) Component.getInstance( UsuarioService.NAME);
        }
        return this.servicioUsuario;
    }
    
    @Override
    public DatosUsuarioView getRowData( String rowKey ) {        
        Long key = new Long(rowKey);
        return this.getServicioUsuario().getDatosUsuarioById( key );                 
    }
            
    @Override
    public Object getRowKey(DatosUsuarioView object) {
        return object.getCod();
    }

    @Override
    protected List<DatosUsuarioView> loadPage( int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters ) {
        return null;
    }
}
