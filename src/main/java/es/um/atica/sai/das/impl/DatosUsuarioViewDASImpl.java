package es.um.atica.sai.das.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.primefaces.model.SortOrder;

import es.um.atica.commons.utils.UtilDate;
import es.um.atica.commons.utils.UtilString;
import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.jpa.das.ResultQuery;
import es.um.atica.sai.das.interfaces.DatosUsuarioViewDAS;
import es.um.atica.sai.dtos.TipoPerfil;
import es.um.atica.sai.entities.DatosUsuarioView;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.utils.Utilidades;
import es.um.atica.seam.framework.Query.QueryPriority;

@Name( DatosUsuarioViewDAS.NAME )
@Stateless
@Local( DatosUsuarioViewDAS.class )
public class DatosUsuarioViewDASImpl extends DataAccessServiceImpl<DatosUsuarioView> implements DatosUsuarioViewDAS {

    @In("org.jboss.seam.security.identity")    
    SaiIdentity identity;
	
    private static final String[] RESTRICCIONES = {
            UtilString.cmpTextFilterEjbql( "up.usuario.dni", "#{usuarios.dniBuscar}" ),
            Utilidades.cmpTextFilterEjbqlCompletoIgnoraTildes("up.usuario.datosUsuario.nombreCompleto", "#{usuarios.nombreBuscar}"),
            UtilString.cmpTextFilterEjbqlCompleto("up.usuario.datosUsuario.email", "#{usuarios.emailBuscar}" ),
            UtilString.cmpTextFilterEjbql( "up.usuario.estado", "#{usuarios.estadoBuscar}" ),
            new StringBuilder("up.perfil.tag=").append("#{usuarios.tagPerfilBuscar}").append(" AND up.pendienteValidarIr='NO' AND up.usuario.acepta='SI' AND (up.usuario.caduca IS NULL OR up.usuario.caduca>").append("TO_DATE('").append(UtilDate.convertirDateFechaToString(new Date())).append("','dd/MM/yyyy')) AND up.usuario.estado='ALTA'").toString()        
    };

    private static final String[] RESTRICCIONES_GESTOR = {
            UtilString.cmpTextFilterEjbql( "duv.dni", "#{usuarios.dniBuscar}" ),
            Utilidades.cmpTextFilterEjbqlCompletoIgnoraTildes("duv.nombreCompleto", "#{usuarios.nombreBuscar}"),
            UtilString.cmpTextFilterEjbqlCompleto("duv.email", "#{usuarios.emailBuscar}" ),
            UtilString.cmpTextFilterEjbql( "duv.usuario.estado", "#{usuarios.estadoBuscar}" ),
            new StringBuilder("EXISTS (SELECT up FROM UsuarioPerfil up WHERE up.usuario=duv.usuario AND up.perfil.tag=").append("#{usuarios.tagPerfilBuscar}").append(" AND up.pendienteValidarIr='NO' AND up.usuario.acepta='SI' AND (up.usuario.caduca IS NULL OR up.usuario.caduca>").append("TO_DATE('").append(UtilDate.convertirDateFechaToString(new Date())).append("','dd/MM/yyyy')) AND up.usuario.estado='ALTA')").toString()
    };
    
    private static final String[] RESTRICCIONES_ADMINISTRATIVO = {
            UtilString.cmpTextFilterEjbql( "up.usuario.dni", "#{usuariosIr.dniBuscar}" ),
            Utilidades.cmpTextFilterEjbqlCompletoIgnoraTildes("up.usuario.datosUsuario.nombreCompleto", "#{usuariosIr.nombreBuscar}"),
            UtilString.cmpTextFilterEjbqlCompleto("up.usuario.datosUsuario.email", "#{usuariosIr.emailBuscar}" ),
    };
    
    private static List<String> getListaRestricciones()
    {
     	return Arrays.asList(RESTRICCIONES);
    }
    
    private static List<String> getListaRestriccionesGestor()
    {
     	return Arrays.asList(RESTRICCIONES_GESTOR);
    }

    private static List<String> getListaRestriccionesAdministrativo()
    {
     	return Arrays.asList(RESTRICCIONES_ADMINISTRATIVO);
    }
    
    private List<String> getRestricciones()
    {
    	if (identity.tienePerfil(TipoPerfil.GESTOR))
    	{ 
    		return getListaRestriccionesGestor();
    	}
    	else
    	{
    		return getListaRestricciones();
    	}
    }
    
    @Override
    public DatosUsuarioView findUsuarioViewByDni( String dniStr ) 
    {
    	try
    	{
    		return (DatosUsuarioView)entityManager.createNamedQuery(DatosUsuarioView.GET_DATOSUSUARIO_X_DNI).setParameter("dni", dniStr).getSingleResult();
    	}
    	catch(Exception ex)
    	{
    		return null;
    	}
     }

    @Override
    public DatosUsuarioView findUsuarioViewByEmail( String emailStr ) 
    {
    	try
    	{
    		return (DatosUsuarioView)entityManager.createNamedQuery(DatosUsuarioView.GET_DATOSUSUARIO_X_EMAIL).setParameter("mail", emailStr).getSingleResult();
    	}
    	catch(Exception ex)
    	{
    		return null;
    	}
    }

    public List<DatosUsuarioView> getListaUsuariosSolicitantes(String sql)
    {
    	try
    	{
    		return entityManager.createQuery(sql).getResultList();
    	}
        catch(Exception ex)
        {
        	log.error("Error en getListaUsuariosSolicitantes: ", ex );
        	return new ArrayList<>();
        }
    }
    
    public ResultQuery<DatosUsuarioView> busquedaUsuarios(String sql, int first, int pageSize, String sortField, SortOrder sortOrder)
    {
    	if (sql == null)
    	{
    		return null;
    	}
    	HashMap<String,Object> parameters  = new HashMap<>();
    	HashMap<String,Object> hints  = new HashMap<>();
    	try
        {
    		return this.resultsByEntityQueryWithDinamicFilter(sql, getRestricciones(), parameters, first, pageSize, sortField, sortOrder.name(), "and", hints, null, QueryPriority.JAVA, QueryPriority.QUERY);
        }
        catch(Exception ex)
        {
        	log.error("Error en busquedaUsuarios: ", ex );
        	return null;
        }
    }

    public Long getCountBusquedaUsuarios(String sql)
    {
    	if (sql == null)
    	{
    		return new Long(0);
    	}
    	HashMap<String,Object> parameters  = new HashMap<>();
    	HashMap<String,Object> hints  = new HashMap<>();
    	try
    	{
    		// Al tener un DISTINCT la consulta tenemos que hacerlo así
    		ResultQuery<DatosUsuarioView> result = this.resultsByEntityQueryWithDinamicFilter(sql, getRestricciones(), parameters, null, null, null, null, "and",hints);
    		return new Long(result.getResultList().size());
    	}
    	catch(Exception ex)
    	{
    		return new Long(0);
    	}
    }
    
    public ResultQuery<DatosUsuarioView> busquedaUsuariosIr(int first, int pageSize, String sortField, SortOrder sortOrder)
    {
    	HashMap<String,Object> parameters  = new HashMap<>();
    	HashMap<String,Object> hints  = new HashMap<>();
    	try
        {
    		return this.resultsByEntityNamedQueryWithDinamicFilter(DatosUsuarioView.GET_LISTA_IRS, getListaRestriccionesAdministrativo(), parameters, first, pageSize, sortField, sortOrder.name(), "and", hints, null, QueryPriority.JAVA, QueryPriority.QUERY);
        }
        catch(Exception ex)
        {
        	log.error("Error en busquedaUsuariosIr: ", ex );
        	return null;
        }
    }

    public Long getCountBusquedaUsuariosIr()
    {
    	HashMap<String,Object> parameters  = new HashMap<>();
    	HashMap<String,Object> hints  = new HashMap<>();
    	try
    	{
    		// Al tener un DISTINCT la consulta tenemos que hacerlo así
    		ResultQuery<DatosUsuarioView> result = this.resultsByEntityNamedQueryWithDinamicFilter(DatosUsuarioView.GET_LISTA_IRS, getListaRestriccionesAdministrativo(), parameters, null, null, null, null, "and", hints);
    		return new Long(result.getResultList().size());
    	}
    	catch(Exception ex)
    	{
    		return new Long(0);
    	}
    }
}
