package es.um.atica.sai.das.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import es.um.atica.commons.utils.UtilDate;
import es.um.atica.commons.utils.UtilString;
import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.sai.das.interfaces.TipoHorarioDAS;
import es.um.atica.sai.dtos.TipoPerfil;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.ReservableHorario;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.TipoHorario;
import es.um.atica.sai.entities.TipoReservable;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.utils.Utilidades;

@Stateless
@Name(TipoHorarioDAS.NAME)
public class TipoHorarioDASImpl extends DataAccessServiceImpl<TipoHorario> implements TipoHorarioDAS{

	@In(create=true)
	private SaiIdentity identity;

    private static final String[] RESTRICCIONES = {
    		UtilString.cmpNumberEjbql("th.servicio","#{tiposHorarios.servicioBuscar}"),                                                              
            Utilidades.cmpTextFilterEjbqlCompletoIgnoraTildes("th.descripcion", "#{tiposHorarios.descripcionBuscar}"),};

    private List<String> getListaRestricciones()
    {
    	return Arrays.asList(RESTRICCIONES);
    }
    
	@Override
	public void crear(TipoHorario th) throws SaiException
	{
		try
		{
			this.persist(th, true);
		}
		catch (Exception ex) 
		{
			throw new SaiException(ex.getMessage());
		}
	}
    
	@Override
	public void modificar(TipoHorario th) throws SaiException
	{
		try
		{
			this.update(th, true);
		}
		catch (Exception ex) 
		{
			throw new SaiException(ex.getMessage());
		}
	}
	
	@Override
    public void eliminar(TipoHorario th) throws SaiException
	{
		try
		{
			this.delete(th, true);
		}
		catch (Exception ex) 
		{
			throw new SaiException(ex.getMessage());
		}
	}
    
    public List<TipoHorario> buscar()
    {
    	Map<String, Object> parameters = new HashMap<>();        
        Map<String,Object> hints = new HashMap<>();
    	try
    	{
	        if (identity.tienePerfil(TipoPerfil.GESTOR))
	    	{
	    		return this.findByEntityNamedQueryWithDinamicFilter(TipoHorario.GET_TIPOSHORARIO,
	    															getListaRestricciones(), 
	    															parameters, null, null, null, "and", hints);
	    	}
	    	else
	    	{
	    		parameters.put("currentUser", identity.getUsuarioConectado());
	    		return this.findByEntityNamedQueryWithDinamicFilter(TipoHorario.GET_TIPOSHORARIO_X_USUARIO,
																	getListaRestricciones(), 
																	parameters, null, null, null, "and", hints);
	    	}
    	}
    	catch(Exception ex)
    	{
    		return new ArrayList<>();
    	}
    }
	
    @Override
    public List<TipoHorario> getListaTiposHorario() {   
        try
        {
        	return this.getEntityManager().createNamedQuery(TipoHorario.GET_TIPOSHORARIO).getResultList();
        }
        catch(Exception ex)
        {
        	return new ArrayList<>();
        }
    }
	
	@Override
	public List<TipoHorario> getListaTiposHorarioByServicio(Servicio servicio)
	{
        try
        {
        	return this.getEntityManager().createNamedQuery(TipoHorario.GET_TIPOSHORARIO_X_SERVICIO).setParameter("servicio", servicio).getResultList();
        }
        catch(Exception ex)
        {
        	return new ArrayList<>();
        }
	}

    @Override
    public List<TipoHorario> getListaTiposHorarioByUsuario(Usuario usuario) 
    {
        try
        {
        	return this.getEntityManager().createNamedQuery(TipoHorario.GET_TIPOSHORARIO_X_USUARIO).setParameter( "currentUser", usuario).getResultList();
        }
        catch(Exception ex)
        {
        	return new ArrayList<>();
        }
    }    

    @Override
    public TipoHorario getTipoHorarioByTipoReservableFecha(TipoReservable tr, Date fecha)
    {
        String mesDia = UtilDate.dateToString( fecha, "MMdd" );
        try
        {
            BigDecimal codTipoHorario = (BigDecimal)this.getEntityManager().createNamedQuery(ReservableHorario.GET_TIPOHORARIO_BY_TIPORESERVABLE_DIAMES).setParameter("codtiporeservable", tr.getCod()).setParameter("mesdia", mesDia).getSingleResult();
            return this.find(codTipoHorario.longValue());
        }
        catch(Exception ex)
        {
            return null;
        } 
    }
    
    @Override
    public TipoHorario getTipoHorarioByProductoFecha(Producto producto, Date fecha)
    {
        String mesDia = UtilDate.dateToString( fecha, "MMdd" );
        try
        {
            BigDecimal codTipoHorario = (BigDecimal)this.getEntityManager().createNamedQuery(ReservableHorario.GET_TIPOHORARIO_BY_PRODUCTO_DIAMES).setParameter("codproducto", producto.getCod()).setParameter("mesdia", mesDia).getSingleResult();
            return this.find(codTipoHorario.longValue());
        }
        catch(Exception ex)
        {
            return null;
        } 
    }

}
