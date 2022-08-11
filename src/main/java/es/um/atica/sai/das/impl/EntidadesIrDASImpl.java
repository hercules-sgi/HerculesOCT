package es.um.atica.sai.das.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.jboss.seam.annotations.Name;

import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.sai.das.interfaces.EntidadesIrDAS;
import es.um.atica.sai.entities.EntidadesIr;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;

@Name(EntidadesIrDAS.NAME)
@Stateless
@Local(EntidadesIrDAS.class)
public class EntidadesIrDASImpl extends  DataAccessServiceImpl<EntidadesIr> implements EntidadesIrDAS {

    
    @Override
    public List<EntidadesIr> getEntidadesIrByUsuario(Usuario usuario) {
        try 
        {        
            return entityManager.createNamedQuery(EntidadesIr.QUERY_ENTIDADESIR_X_USUARIO).setParameter("codusuario", usuario.getCod()).getResultList();                        
        }
        catch (Exception ex) 
        {
            return new ArrayList<>(0);
        }
    }
    
    @Override
    public boolean existeEntidadIr(EntidadesIr entidadIr) 
    {
        try 
        {        
            return (Long)entityManager.createNamedQuery(EntidadesIr.EXISTE_ENTIDADIR).setParameter("usuario", entidadIr.getUsuario()).setParameter("entidadpagadora", entidadIr.getEntidadPagadora()).getSingleResult()>0;                        
        }
        catch (Exception ex) 
        {
            return false;
        }
    }
    
	@Override
	public void crear( EntidadesIr eir ) throws SaiException {
		try {
			this.persist( eir, true );
		} catch ( Exception e ) {

			log.error( "No se ha podido crear la asociación entre Entidad pagadora e IR:", e );
			throw new SaiException(e.getMessage());
		}
	}

	@Override
	public void eliminar( EntidadesIr eir ) throws SaiException {
		try {
			this.delete( eir, true );
		} catch ( Exception e ) {

			log.error( "No se ha podido eliminar la asociación entre Entidad pagadora e IR:", e );
			throw new SaiException(e.getMessage());
		}
	}
}
