package es.um.atica.sai.das.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.jboss.seam.annotations.Name;

import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.sai.das.interfaces.MuestraDAS;
import es.um.atica.sai.entities.Muestra;
import es.um.atica.sai.exceptions.SaiException;

@Name( value = MuestraDAS.NAME )
@Stateless
@Local(MuestraDAS.class)
public class MuestraDASImpl extends DataAccessServiceImpl<Muestra> implements MuestraDAS {

	@Override
	public void eliminar(Muestra m) throws SaiException
	{
		try
		{
			this.delete( m, true );
		}
		catch (Exception ex)
		{
			log.error( "Error inesperado. No se ha podido eliminar la muestra:", ex );
			throw new SaiException(ex.getMessage());
		}
	}

	@Override
	public void crear(Muestra m) throws SaiException 
	{
		try
		{
			persist(m, true);
		}
		catch (Exception ex)
		{
			log.error( "Error inesperado. No se ha podido crear la muestra:", ex );
			throw new SaiException(ex.getMessage());
		}
	}

}
