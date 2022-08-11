package es.um.atica.sai.das.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.jboss.seam.annotations.Name;

import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.sai.das.interfaces.PuertaKronDAS;
import es.um.atica.sai.dtos.TerminalKron;
import es.um.atica.sai.entities.PuertaKron;
import es.um.atica.sai.exceptions.SaiException;

@Name( PuertaKronDASImpl.NAME )
@Stateless
@Local(PuertaKronDAS.class)
public class PuertaKronDASImpl  extends  DataAccessServiceImpl<PuertaKron>  implements PuertaKronDAS {

	public static final String NAME = "puertaKronDAS";


	@Override
	public PuertaKron getPuertaKronByTerminalKron(TerminalKron tk)
	{
		try
		{
			return (PuertaKron)entityManager.createNamedQuery(PuertaKron.GET_PUERTA_BY_LECTOR_IP).setParameter("ip", tk.getIp()).setParameter("lector", tk.getLector()).getSingleResult();
		}
		catch (final Exception ex)
		{
			return null;
		}
	}


	@Override
	public void crear(PuertaKron pk) throws SaiException {
		try 
		{
			this.persist( pk, true );
		} 
		catch ( final Exception e ) {

			log.error( "Error inesperado. No se ha podido crear la puerta Kron:", e );
			throw new SaiException(e.getMessage());
		}
	}
}
