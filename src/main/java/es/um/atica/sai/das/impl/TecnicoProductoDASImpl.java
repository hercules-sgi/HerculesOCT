package es.um.atica.sai.das.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.jboss.seam.annotations.Name;

import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.sai.das.interfaces.TecnicoProductoDAS;
import es.um.atica.sai.entities.TecnicoProducto;
import es.um.atica.sai.exceptions.SaiException;



@Name("tecnicoDAS")
@Stateless
@Local(TecnicoProductoDAS.class)
public class TecnicoProductoDASImpl extends  DataAccessServiceImpl<TecnicoProducto> implements TecnicoProductoDAS{

    @Override
    public List<TecnicoProducto> getTecnicosByService( long codServicio ) {
        try
        {
        	return getEntityManager().createNamedQuery( TecnicoProducto.GET_TECNICOS_BY_SERVICIO ).setParameter( "servicio", codServicio ).getResultList();
        }
        catch (final Exception e)
        {
        	log.error( "Error inesperado getTecnicosByService:", e );
        	return null;
        }
    }
            
    @Override
	public boolean existeTecnico( TecnicoProducto tecnico ) 
    {
    	try
    	{
    		return !getEntityManager().createNamedQuery( TecnicoProducto.EXISTE_TECNICO ).setParameter( "codproducto", tecnico.getProducto().getCod()).setParameter("codusuario", tecnico.getUsuario().getCod()).getResultList().isEmpty();
    	}
    	catch (final Exception e)
    	{
    		log.error( "Error inesperado existeTecnico:", e );
    		return false;
    	}
    }
    
    

	@Override
	public void crear( TecnicoProducto tec ) throws SaiException {
		try {
			this.persist( tec, true );
		} catch ( final Exception e ) {
			log.error( "Error al crear TecnicoProducto:", e );
			throw new SaiException(e.getMessage());
		}
	}
	
	@Override
	public void modificar( TecnicoProducto tec ) throws SaiException {
		try {
			this.update( tec, true );
		} catch ( final Exception e ) {
			log.error( "Error al modificar TecnicoProducto:", e );
			throw new SaiException(e.getMessage());
		}
	}
	
    @Override
	public void eliminar( TecnicoProducto tec ) throws SaiException
    {
		try {
			final Query query = getEntityManager().createQuery("DELETE FROM TecnicoProducto tec WHERE tec.producto.cod=:codproducto AND tec.usuario.cod=:codusuario")
					.setParameter("codproducto", tec.getProducto().getCod()).setParameter("codusuario", tec.getUsuario().getCod());
			query.executeUpdate();
			flush();
		} catch ( final Exception e ) {
			log.error( "Error al eliminar TecnicoProducto:", e );
			throw new SaiException(e.getMessage());
		}    
	}

	@Override
	public TecnicoProducto getTecnicoById( long codTecnico ) {		
		return find( codTecnico );
	}	
}
