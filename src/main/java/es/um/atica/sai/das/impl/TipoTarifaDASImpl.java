package es.um.atica.sai.das.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.jboss.seam.annotations.Name;

import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.sai.das.interfaces.TipoTarifaDAS;
import es.um.atica.sai.entities.TipoTarifa;
import es.um.atica.sai.exceptions.SaiException;

@Name("tipoTarifaDAS")
@Stateless
@Local(TipoTarifaDAS.class)
public class TipoTarifaDASImpl extends  DataAccessServiceImpl<TipoTarifa> implements TipoTarifaDAS{

    
    @Override
    public List<TipoTarifa> getTarifas() {
        log.info("Recuperando tarifas");
        Query consulta = entityManager.createNamedQuery(TipoTarifa.GET_TARIFAS);                                        
        return consulta.getResultList();
    }

	@Override
	public void crear( TipoTarifa tt ) throws SaiException {
		try {
			this.persist( tt, true );
		} catch ( Exception e ) {

			log.error( "Error inesperado. No se ha podido crear el tipo de tarifa:", e );
			throw new SaiException(e.getMessage());
		}
	}

	@Override
	public void modificar( TipoTarifa tt ) throws SaiException {
		try {
			this.update( tt, true );
		} catch ( Exception e ) {

			log.error( "Error inesperado. No se ha podido modificar el tipo de tarifa:", e );
			throw new SaiException(e.getMessage());
		}
	}

	@Override
	public void eliminar( TipoTarifa tt ) throws SaiException {
		try {
			this.delete( tt, true );
		} catch ( Exception e ) {

			log.error( "Error inesperado. No se ha podido eliminar el tipo de tarifa:", e );
			throw new SaiException(e.getMessage());
		}
	}
}
