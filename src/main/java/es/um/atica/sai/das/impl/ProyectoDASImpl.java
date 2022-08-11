package es.um.atica.sai.das.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import es.um.atica.commons.utils.UtilString;
import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.sai.das.interfaces.ProyectoDAS;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.Proyecto;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;
import es.um.atica.sai.security.authentication.SaiIdentity;

@Name(ProyectoDAS.NAME)
@Stateless
@Local(ProyectoDAS.class)
public class ProyectoDASImpl  extends  DataAccessServiceImpl<Proyecto>  implements ProyectoDAS {

	@In(create=true)
	private SaiIdentity identity;
	
    private static final String[] RESTRICCIONES = {
    		UtilString.cmpNumberEjbql("pro.servicio","#{proyectos.servicioBuscar}"),                                                              
    		new StringBuilder("pro.fechaCaducidad >").append("TO_DATE(").append("#{proyectos.fechaCaducidadDesdeBuscarString}").append(",'dd/MM/yyyy')").toString()
    		};

    private List<String> getListaRestricciones()
    {
    	return Arrays.asList(RESTRICCIONES);
    }
	
    public List<Proyecto> busquedaGestor()
    {
    	Map<String, Object> parameters = new HashMap<>();        
        Map<String,Object> hints = new HashMap<>();
    	try
    	{
	    		return this.findByEntityNamedQueryWithDinamicFilter(Proyecto.GET_LISTA_PROYECTOS,
	    															getListaRestricciones(), 
	    															parameters, null, null, null, "and", hints);
    	}
    	catch(Exception ex)
    	{
    		return new ArrayList<>();
    	}
    }
    
    public List<Proyecto> busquedaSupervisorIrUsuario(List<Servicio> listaServiciosSupervisor, List<Usuario> listaIrs)
    {
    	Map<String, Object> parameters = new HashMap<>();        
        Map<String,Object> hints = new HashMap<>();
    	try
    	{
    		if (!listaServiciosSupervisor.isEmpty() && !listaIrs.isEmpty())
	    	{
	    		parameters.put("listaservicios", listaServiciosSupervisor);
	    		parameters.put("listairs", listaIrs);
	    		return this.findByEntityNamedQueryWithDinamicFilter(Proyecto.GET_LISTA_PROYECTOS_X_LISTASERVICIOS_OR_LISTAIR,
																	getListaRestricciones(), 
																	parameters, null, null, null, "and", hints);
	    	}
    		if (!listaServiciosSupervisor.isEmpty() && listaIrs.isEmpty())
    		{
	    		parameters.put("listaservicios", listaServiciosSupervisor);
	    		return this.findByEntityNamedQueryWithDinamicFilter(Proyecto.GET_LISTA_PROYECTOS_X_LISTASERVICIOS,
																	getListaRestricciones(), 
																	parameters, null, null, null, "and", hints);
    		}
    		if (listaServiciosSupervisor.isEmpty() && !listaIrs.isEmpty())
	    	{
	    		parameters.put("listairs", listaIrs);
	    		return this.findByEntityNamedQueryWithDinamicFilter(Proyecto.GET_LISTA_PROYECTOS_X_LISTAIR,
																	getListaRestricciones(), 
																	parameters, null, null, null, "and", hints);
	    	}
    		return new ArrayList<>();
    	}
    	catch(Exception ex)
    	{
    		return new ArrayList<>();
    	}
    }
    
    @Override
    public List<Proyecto> getListaProyectosByProductoIr(Producto producto, Usuario usuarioIr)
    {
	     try 
	     {
	    	 return entityManager.createNamedQuery(Proyecto.GET_LISTA_PROYECTOS_X_PRODUCTO_IR).setParameter("producto", producto).setParameter("usuarioir", usuarioIr).setParameter("fechaactual", new Date()).getResultList(); 
	     }
	     catch (Exception ex) 
	     {
	    	 return new ArrayList<>();
	     }
    }
    
	@Override
	public void crear(Proyecto proyecto) throws SaiException {
		try {
			this.persist( proyecto, true );
		} catch ( Exception e ) {

			log.error( "Error inesperado. No se ha podido crear el proyecto:", e );
			throw new SaiException(e.getMessage());
		}
	}

	@Override
	public void modificar(Proyecto proyecto) throws SaiException {
		try {
			this.update(proyecto, true );
		} catch ( Exception e ) {

			log.error( "Error inesperado. No se ha podido modificar el proyecto:", e );
			throw new SaiException(e.getMessage());
		}
	}

	@Override
	public void eliminar(Proyecto proyecto) throws SaiException {
		try {
			this.delete( proyecto, true );
		} catch ( Exception e ) {

			log.error( "Error inesperado. No se ha podido eliminar el proyecto:", e );
			throw new SaiException(e.getMessage());
		}
	}



}
