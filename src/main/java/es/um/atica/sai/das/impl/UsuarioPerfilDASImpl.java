package es.um.atica.sai.das.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import org.jboss.seam.annotations.Name;

import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.sai.das.interfaces.UsuarioPerfilDAS;
import es.um.atica.sai.entities.Perfil;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.entities.UsuarioPerfil;
import es.um.atica.sai.exceptions.SaiException;

@Name( UsuarioPerfilDAS.NAME )
@Stateless
public class UsuarioPerfilDASImpl extends DataAccessServiceImpl<UsuarioPerfil> implements UsuarioPerfilDAS {

    @Override
    public List<UsuarioPerfil> getPerfilesUsuario(Usuario usuario) 
    {
        try
        {
        	return entityManager.createNamedQuery(UsuarioPerfil.GET_PERFILES_USUARIO).setParameter("usuario", usuario).setParameter("fechaactual", new Date()).getResultList();
        }
        catch(Exception e)
        {
        	return new ArrayList<>(0);
        }
    }
    
    @Override
    public List<UsuarioPerfil> getPerfilesUsuarioByServicio(Usuario usuario, Servicio servicio) 
    {
        try
        {
        	return entityManager.createNamedQuery(UsuarioPerfil.GET_PERFILES_USUARIO_X_SERVICIO).setParameter("usuario", usuario).setParameter("servicio", servicio).setParameter("fechaactual", new Date()).getResultList();
        }
        catch(Exception e)
        {
        	return new ArrayList<>(0);
        }
    }

    @Override
    public List<UsuarioPerfil> getPerfilesUsuarioIgnoraEstado(Usuario usuario) 
    {
        try
        {
        	return entityManager.createNamedQuery(UsuarioPerfil.GET_PERFILES_USUARIO_IGNORA_ESTADO).setParameter("usuario", usuario).getResultList();
        }
        catch(Exception e)
        {
        	return new ArrayList<>(0);
        }
    }
    
    @Override
    public boolean tienePerfilIrIgnoraEstado(Usuario usuario) 
    {
        try
        {
        	return (Long)entityManager.createNamedQuery(UsuarioPerfil.TIENE_PERFIL_IR_IGNORA_ESTADO).setParameter("usuario", usuario).getSingleResult()>0;
        }
        catch(Exception e)
        {
        	return false;
        }
    }
    
    @Override
    public List<Perfil> getPerfilesPuedeCrearByPerfil(Perfil perfil)
    {
        try
        {
        	return entityManager.createNamedQuery(UsuarioPerfil.GET_PERFILESPUEDECREAR_X_PERFIL).setParameter("perfil", perfil).getResultList();
        }
        catch(Exception e)
        {
        	return new ArrayList<>(0);
        }
    }
    
    @Override
    public UsuarioPerfil getSupervisorServicio(Usuario usuario, Servicio servicio) 
    {
        try
        {
        	return (UsuarioPerfil)entityManager.createNamedQuery(UsuarioPerfil.GET_SUPERVISOR_SERVICIO).setParameter("usuario", usuario).setParameter("fechaactual", new Date()).setParameter("servicio", servicio).getSingleResult();
        }
        catch(Exception e)
        {
        	return null;
        }
    }
    
    @Override
    public List<UsuarioPerfil> getSupervisoresServicio(Servicio servicio)
    {
	    try
	    {
	    	return entityManager.createNamedQuery(UsuarioPerfil.GET_SUPERVISORES_SERVICIO).setParameter("servicio", servicio).setParameter("fechaactual", new Date()).getResultList();
	    }
	    catch(Exception e)
	    {
	    	return new ArrayList<>(0);
	    }
    }
    
    public UsuarioPerfil getUsuarioPerfilNoRequiereServicio(UsuarioPerfil up )
    {
    	try
    	{
    		return (UsuarioPerfil)entityManager.createNamedQuery(UsuarioPerfil.GET_USUARIOPERFIL_NOREQUIERESERVICIO).setParameter("usuario", up.getUsuario()).setParameter("perfil", up.getPerfil()).getSingleResult();
    	}
    	catch(Exception e)
    	{
    		return null;
    	}
    }
    
    public UsuarioPerfil getUsuarioPerfilSiRequiereServicioNoRequiereIr(UsuarioPerfil up )
    {
    	try
    	{
    		return (UsuarioPerfil)entityManager.createNamedQuery(UsuarioPerfil.GET_USUARIOPERFIL_SIREQUIERESERVICIO_NOREQUIEREIR).setParameter("usuario", up.getUsuario()).setParameter("perfil", up.getPerfil()).setParameter("servicio", up.getServicio()).getSingleResult();
    	}
    	catch(Exception e)
    	{
    		return null;
    	}
    }
	
    public UsuarioPerfil getUsuarioPerfilSiRequiereServicioSiRequiereIr(UsuarioPerfil up )
    {
    	try
    	{
    		return (UsuarioPerfil)entityManager.createNamedQuery(UsuarioPerfil.GET_USUARIOPERFIL_SIREQUIERESERVICIO_SIREQUIEREIR).setParameter("usuario", up.getUsuario()).setParameter("perfil", up.getPerfil()).setParameter("servicio", up.getServicio()).setParameter("usuarioirresponsable", up.getUsuarioIrResponsable()).getSingleResult();
    	}
    	catch(Exception e)
    	{
    		return null;
    	}
    }
    
    @Override
	public void crear(UsuarioPerfil up) throws SaiException {
		try 
		{
			this.persist(up, true );
		} 
		catch ( Exception e ) 
		{
			log.error( "Error inesperado. No se ha podido crear el perfil de usuario:", e );
			throw new SaiException(e.getMessage());
		}
	}

	@Override
	public void modificar(UsuarioPerfil up) throws SaiException 
	{
		try 
		{
			this.update(up, true );
		} 
		catch ( Exception e ) 
		{
			log.error( "Error inesperado. No se ha podido modificar el perfil de usuario:", e );
			throw new SaiException(e.getMessage());
		}
	}

	@Override
	public void eliminar(UsuarioPerfil up) throws SaiException {
		try 
		{
			this.delete(up, true);
		} 
		catch ( Exception e ) 
		{
			log.error( "Error inesperado. No se ha podido eliminar el perfil de usuario:", e );
			throw new SaiException(e.getMessage());
		}
	}
}
