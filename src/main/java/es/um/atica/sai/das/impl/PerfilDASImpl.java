package es.um.atica.sai.das.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.jboss.seam.annotations.Name;

import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.sai.das.interfaces.PerfilDAS;
import es.um.atica.sai.entities.Perfil;

@Name( PerfilDAS.NAME )
@Stateless
public class PerfilDASImpl extends DataAccessServiceImpl<Perfil> implements PerfilDAS {

    
    @Override
    public Perfil getPerfilByTag(String tagPerfil) 
    {
        try
        {
        	return (Perfil)entityManager.createNamedQuery(Perfil.GET_PERFIL_X_TAG).setParameter("tagperfil", tagPerfil).getSingleResult();
        }
        catch(Exception e)
        {
        	return null;
        }
    }
    
    @Override
    public List<Perfil> getListaPerfiles()
    {
        try
        {
        	return entityManager.createNamedQuery(Perfil.GET_LISTA_PERFILES).getResultList();
        }
        catch(Exception e)
        {
        	return new ArrayList<>();
        }
    }

    public List<Perfil> getPerfilesPuedeFiltrar(String sql)
    {
        try
        {
        	return entityManager.createQuery(sql).getResultList();
        }
        catch(Exception e)
        {
        	return new ArrayList<>();
        }
    	
    }
}
