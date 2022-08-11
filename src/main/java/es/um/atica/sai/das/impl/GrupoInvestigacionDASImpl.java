package es.um.atica.sai.das.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.jboss.seam.annotations.Name;

import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.sai.das.interfaces.GrupoInvestigacionDAS;
import es.um.atica.sai.entities.EntidadPagadora;
import es.um.atica.sai.entities.GrupoInvestigacion;
import es.um.atica.sai.exceptions.SaiException;

@Name(GrupoInvestigacionDAS.NAME)
@Stateless
@Local(GrupoInvestigacionDAS.class)
public class GrupoInvestigacionDASImpl  extends  DataAccessServiceImpl<GrupoInvestigacion>  implements GrupoInvestigacionDAS {

	@Override
	public GrupoInvestigacion getGrupoInvestigacionByEntidadPagadora(EntidadPagadora ep) throws SaiException
	{
		GrupoInvestigacion gi;
		try
		{
			gi = (GrupoInvestigacion)this.getEntityManager().createNamedQuery(GrupoInvestigacion.GET_GRUPOINVESTIGACION_BY_ENTIDADPAGADORA)
															.setParameter("entidad", ep).getSingleResult();
		}
		catch (Exception ex)
		{
			gi = null;
		}
		if (gi == null)
		{
			gi = new GrupoInvestigacion();
			gi.setEntidadPagadora(ep);
			try
			{
				this.persist(gi, true);
			}
			catch (Exception ex2)
			{
				log.error( "Error inesperado. No se puede crear el grupo de investigación. Error:", ex2 );
				throw new SaiException( "Error al crear grupo de investigación: " + ex2.getMessage() );
			}
		}
		return gi;
	}
}
