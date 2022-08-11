package es.um.atica.sai.das.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.jboss.seam.annotations.Name;

import es.um.atica.commons.utils.UtilString;
import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.sai.das.interfaces.EntidadPagadoraDAS;
import es.um.atica.sai.entities.EntidadPagadora;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;
import es.um.atica.sai.utils.Utilidades;

@Name(EntidadPagadoraDAS.NAME)
@Stateless
@Local(EntidadPagadoraDAS.class)
public class EntidadPagadoraDASImp extends  DataAccessServiceImpl<EntidadPagadora> implements EntidadPagadoraDAS {

    private static final String[] RESTRICCIONES = {                                                   
            UtilString.cmpTextFilterEjbql("e.cif", "#{entidadesPagadoras.cif}"), 
            UtilString.cmpTextFilterEjbql("e.unidadAdministrativa", "#{entidadesPagadoras.codDepartamento}"), 
            Utilidades.cmpTextFilterEjbqlCompletoIgnoraTildes("e.localidad", "#{entidadesPagadoras.localidad}"),                     
            Utilidades.cmpTextFilterEjbqlCompletoIgnoraTildes("e.nombre", "#{entidadesPagadoras.nombre}"),
            Utilidades.cmpTextFilterEjbqlCompletoIgnoraTildes("e.email", "#{entidadesPagadoras.email}"),
            UtilString.cmpTextFilterEjbql("e.telefono", "#{entidadesPagadoras.telefono}"),
            UtilString.cmpTextFilterEjbql("e.estado", "#{entidadesPagadoras.estado}")
            };

    private static List<String> getListaRestricciones()
    {
     	return Arrays.asList(RESTRICCIONES);
    }
    
    @Override
    public List<EntidadPagadora> getListaEntidadesPagadorasActivas() 
    {
        try
        {
        	return entityManager.createNamedQuery(EntidadPagadora.GET_ENTIDADES_PAGADORAS_ACTIVAS).getResultList();
        }
        catch(Exception e)
        {
        	return new ArrayList<>(0);
        }
    }    


    @Override
    public List<EntidadPagadora> buscarEntidadesPagadoras()
    {        
        try
        {
        	return this.findByEntityNamedQueryWithDinamicFilter(EntidadPagadora.GET_ENTIDADES_PAGADORAS_BUSQUEDA, getListaRestricciones(), null, null, null, null, "and", null );
        }
        catch(Exception e)
        {
        	return new ArrayList<>(0);
        }
    }

	@Override
	public void crear( EntidadPagadora entidad ) throws SaiException {
		try {
			this.persist( entidad, true );
		} catch ( Exception e ) {

			log.error( "No se ha podido crear la entidad pagadora:", e );
			throw new SaiException(e.getMessage());
		}
	}
    
	@Override
	public void modificar( EntidadPagadora entidad ) throws SaiException {
		try {
			this.update( entidad, true );
		} catch ( Exception e ) {

			log.error( "No se ha podido modificar la entidad pagadora:", e );
			throw new SaiException(e.getMessage());
		}
	}
	
	@Override
	public void eliminar( EntidadPagadora entidad ) throws SaiException {
		try {
			this.delete( entidad, true );
		} catch ( Exception e ) {

			log.error( "No se ha podido modificar la entidad pagadora:", e );
			throw new SaiException(e.getMessage());
		}
	}
    
    @Override
    public List<EntidadPagadora> getEntidadesByIr(Usuario usuario) 
    {
        try 
        {        
            return entityManager.createNamedQuery(EntidadPagadora.GET_ENTIDADES_BY_IR).setParameter( "usuario", usuario ).getResultList();                        
        }
        catch (Exception ex) 
        {
            return new ArrayList<>();
        }
    }
    
    public boolean existeEntidadCif(String cif, Integer codSubtercero)
    {
    	try 
    	{        
    		return (Long)entityManager.createNamedQuery(EntidadPagadora.EXISTE_ENTIDAD_CIF).setParameter("cif", cif).setParameter("codsubtercero", codSubtercero).getSingleResult()>0;                        
    	}
    	catch (Exception ex) 
    	{
    		return false;
    	}
    }
    
    public boolean existeOtraEntidadCif(EntidadPagadora ep)
    {
    	try 
    	{        
    		return (Long)entityManager.createNamedQuery(EntidadPagadora.EXISTE_ENTIDAD_CIF_OTRA).setParameter("cif", ep.getCif()).setParameter("codsubtercero", ep.getCodSubtercero()).setParameter("codentidadp", ep.getCod()).getSingleResult()>0;                        
    	}
    	catch (Exception ex) 
    	{
    		return false;
    	}

    }
    
    public boolean existeEntidadUnidadAdministrativa(String udadmin)
    {
    	try 
    	{        
    		return (Long)entityManager.createNamedQuery(EntidadPagadora.EXISTE_ENTIDAD_UDADMIN).setParameter("udadmin", udadmin).getSingleResult()>0;                        
    	}
    	catch (Exception ex) 
    	{
    		return false;
    	}
    }
    
    public EntidadPagadora getEntidadPagadoraByCif(String cif, Integer codSubtercero)
    {
    	try 
    	{        
    		return (EntidadPagadora)entityManager.createNamedQuery(EntidadPagadora.GET_ENTIDAD_X_CIF).setParameter("cif", cif).setParameter("codsubtercero", codSubtercero).getSingleResult();                        
    	}
    	catch (Exception ex) 
    	{
    		return null;
    	}
    }
    
    public EntidadPagadora getEntidadPagadoraByUdadmin(String udadmin)
    {
    	try 
    	{        
    		return (EntidadPagadora)entityManager.createNamedQuery(EntidadPagadora.GET_ENTIDAD_X_UDADMIN).setParameter("udadmin", udadmin).getSingleResult();                        
    	}
    	catch (Exception ex) 
    	{
    		return null;
    	}
    }
}
