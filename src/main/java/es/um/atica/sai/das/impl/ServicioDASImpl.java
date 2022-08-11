package es.um.atica.sai.das.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.jboss.seam.annotations.Name;

import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.sai.das.interfaces.ServicioSaiDAS;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;

@Name("servicioSaiDAS")
@Stateless
@Local(ServicioSaiDAS.class)
public class ServicioDASImpl extends  DataAccessServiceImpl<Servicio> implements ServicioSaiDAS {
    
    @Override
    public List<Servicio> getListaServicios() 
    {      
    	try
    	{
    		return entityManager.createNamedQuery(Servicio.GET_SERVICIOS).getResultList();
    	}
    	catch(Exception e)
    	{
    		return new ArrayList<>(0);
    	}
    }

    @Override
    public List<Servicio> getListaServiciosConEmail()
    {
    	try
    	{
    		return entityManager.createNamedQuery(Servicio.GET_SERVICIOS_CON_EMAIL).getResultList();
    	}
    	catch(Exception e)
    	{
    		return new ArrayList<>(0);
    	}
    }

    @Override
    public List<Servicio> getListaServiciosEsSupervisor(Usuario usuario) 
    {      
    	try
    	{
    		return entityManager.createNamedQuery(Servicio.GET_SERVICIOS_ES_SUPERVISOR).setParameter("usuario", usuario).setParameter("fechaactual", new Date()).getResultList();
    	}
    	catch(Exception e)
    	{
    		return new ArrayList<>(0);
    	}
    }

    
    @Override
    public List<Servicio> getListaServiciosEsIr(Usuario usuario) 
    {      
    	try
    	{
    		return entityManager.createNamedQuery(Servicio.GET_SERVICIOS_ES_IR).setParameter("usuario", usuario).setParameter("fechaactual", new Date()).getResultList();
    	}
    	catch(Exception e)
    	{
    		return new ArrayList<>(0);
    	}
    }

    @Override
    public List<Servicio> getListaServiciosEsIrConMiembros(Usuario usuario) 
    {      
    	try
    	{
    		return entityManager.createNamedQuery(Servicio.GET_SERVICIOS_ES_IR_CON_MIEMBROS).setParameter("usuario", usuario).setParameter("fechaactual", new Date()).getResultList();
    	}
    	catch(Exception e)
    	{
    		return new ArrayList<>(0);
    	}
    }
    
    @Override
    public List<Servicio> getListaServiciosEsMiembro(Usuario usuario) 
    {      
    	try
    	{
    		return entityManager.createNamedQuery(Servicio.GET_SERVICIOS_ES_MIEMBRO).setParameter("usuario", usuario).setParameter("fechaactual", new Date()).getResultList();
    	}
    	catch(Exception e)
    	{
    		return new ArrayList<>(0);
    	}
    }

    @Override
    public List<Servicio> getListaServiciosEsTecnico(Usuario usuario) 
    {      
    	try
    	{
    		return entityManager.createNamedQuery(Servicio.GET_SERVICIOS_ES_TECNICO).setParameter("usuario", usuario).setParameter("fechaactual", new Date()).getResultList();
    	}
    	catch(Exception e)
    	{
    		return new ArrayList<>(0);
    	}
    }
    
    @Override
    public List<Servicio> getListaServiciosEsTecnicoConVisibilidadSolicitudes(Usuario usuario) 
    {      
    	try
    	{
    		return entityManager.createNamedQuery(Servicio.GET_SERVICIOS_ES_TECNICO_CON_VISIBILIDAD_SOLICITUDES).setParameter("usuario", usuario).setParameter("fechaactual", new Date()).getResultList();
    	}
    	catch(Exception e)
    	{
    		return new ArrayList<>(0);
    	}
    }
    
    @Override
    public List<Servicio> getListaServiciosEsTecnicoSinVisibilidadSolicitudes(Usuario usuario) 
    {      
    	try
    	{
    		return entityManager.createNamedQuery(Servicio.GET_SERVICIOS_ES_TECNICO_SIN_VISIBILIDAD_SOLICITUDES).setParameter("usuario", usuario).setParameter("fechaactual", new Date()).getResultList();
    	}
    	catch(Exception e)
    	{
    		return new ArrayList<>(0);
    	}
    }
    
    @Override
    public void crear(Servicio servicio) throws SaiException 
    {
        try
        {
        	this.persist(servicio, true);
        }
        catch(Exception e){            
            log.error( "Error al crear servicio: ", e );
            throw new SaiException(e.getMessage());
        }
    }

    @Override
    public void modificar(Servicio servicio) throws SaiException 
    {
        try
        {
        	this.update(servicio, true);
        }
        catch(Exception e){            
            log.error( "Error al modificar servicio: ", e );
            throw new SaiException(e.getMessage());
        }
    }

    @Override
    public void eliminar( Servicio serv ) throws SaiException 
    {
    	try
    	{
    		this.delete( serv, true);
    	}
    	catch(Exception e)
    	{
    		log.error( "Error al eliminar servicio: ", e );
    		throw new SaiException(e.getMessage());
    	}
    }

        
}
