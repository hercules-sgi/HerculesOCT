package es.um.atica.sai.das.impl;

import java.math.BigDecimal;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.jboss.seam.annotations.Name;

import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.sai.das.interfaces.IntervencionDAS;
import es.um.atica.sai.entities.Equipo;
import es.um.atica.sai.entities.Intervencion;
import es.um.atica.sai.exceptions.SaiException;

@Name( IntervencionDASImpl.NAME )
@Stateless
@Local( IntervencionDAS.class )
public class IntervencionDASImpl extends DataAccessServiceImpl<Intervencion> implements IntervencionDAS {

	public static final String NAME = "intervencionDAS";
	private static final String SQL_BASE_SUM = "SELECT SUM(i.precioBase) FROM Intervencion i WHERE i.equipo=:equipo AND i.tipo=:tipo";
	private static final String TO_DATE = "TO_DATE('";
	private static final String DATE_FORMAT = "' ,'dd/MM/yyyy')";
	
	@Override
	public void guardar( Intervencion i ) throws SaiException {
		try {
			if ( i.getCod() == null ) {
				this.persist( i, true );
			} else {
				this.merge( i, true );
			}
		} catch ( final Exception e ) {

			log.error( "No se ha podido guardar Intervención:", e );
			throw new SaiException(e.getMessage());
		}
	}

	@Override
	public void eliminar( Intervencion i ) throws SaiException {
		try {
			this.delete( i, true );
		} catch ( final Exception e ) {

			log.error( "No se ha podido eliminar la Intervención:", e );
			throw new SaiException(e.getMessage());
		}
	}
	
	@Override
	public BigDecimal busquedaSumImporteByEquipoTipo(Equipo equipo, String tipo, String fechaDesde, String fechaHasta)
	{
		String sql = this.getSqlGetSumImporteByEquipoTipo(fechaDesde, fechaHasta);
		try
		{
			BigDecimal resultado = (BigDecimal)getEntityManager().createQuery(sql).setParameter("equipo", equipo).setParameter("tipo", tipo).getSingleResult();
			if (resultado == null)
			{
				return BigDecimal.valueOf(0);
			}
			return resultado;
		}
		catch (Exception ex)
		{
			return BigDecimal.valueOf(0);
		}
	}
	
	private String getSqlGetSumImporteByEquipoTipo(String fechaDesde, String fechaHasta)
	{
		StringBuilder sql = new StringBuilder(SQL_BASE_SUM);
		if (fechaDesde != null)
		{
			sql.append(" AND i.fecha >=").append(TO_DATE).append(fechaDesde).append(DATE_FORMAT);
		}
		if (fechaHasta != null)
		{
			sql.append(" AND i.fecha <").append(TO_DATE).append(fechaHasta).append(DATE_FORMAT).append(" +1");
		}
		return sql.toString();
	}
}
