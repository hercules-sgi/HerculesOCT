package es.um.atica.sai.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.Stateless;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.SeamResourceBundle;
import org.jboss.seam.log.Log;

import es.um.atica.sai.das.interfaces.ConsumoDAS;
import es.um.atica.sai.das.interfaces.ProyectoDAS;
import es.um.atica.sai.das.interfaces.ProyectoProductoDAS;
import es.um.atica.sai.das.interfaces.UsuarioDAS;
import es.um.atica.sai.dtos.TipoPerfil;
import es.um.atica.sai.entities.Consumo;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.Proyecto;
import es.um.atica.sai.entities.ProyectoProducto;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.ProyectoService;

@Stateless
@Name( ProyectoService.NAME )
public class ProyectoServiceImpl implements ProyectoService {

    @Logger
    private Log log;

    @In(create = true )
    private ProyectoDAS proyectoDAS;

    @In(create = true )
    private ProyectoProductoDAS proyectoProductoDAS;

    @In(create = true )
    private ConsumoDAS consumoDAS;
    
    @In(create = true )
    private UsuarioDAS usuarioDAS;
    
	@In(create=true)
	private SaiIdentity identity;
    
    ResourceBundle srb = SeamResourceBundle.getBundle();
    
    @Override
    public void guardarProyecto(Proyecto proyecto) throws SaiException
    {
    	if (proyecto.getCod() == null)
    	{
    		proyecto.setFechaAlta(new Date());
    		proyectoDAS.crear(proyecto);
    	}
    	else
    	{
    		proyectoDAS.modificar(proyecto);
    	}
    }
    
    @Override
    public void eliminarProyecto(Proyecto proyecto) throws SaiException
    {
    	if (!consumoDAS.getConsumosActivosByProyecto(proyecto).isEmpty())
    	{
    		throw new SaiException(srb.getString("proyecto.eliminar.error.consumos"));
    	}
    	if (proyecto.getListaProductos() != null && !proyecto.getListaProductos().isEmpty())
    	{
    		throw new SaiException(srb.getString("proyecto.eliminar.error.productos"));
    	}
    	List<Consumo> listaConsumosAnulados = consumoDAS.getConsumosAnuladosByProyecto(proyecto);
    	if (!listaConsumosAnulados.isEmpty())
    	{
    		Iterator<Consumo> itConsumos = listaConsumosAnulados.iterator();
    		while (itConsumos.hasNext())
    		{
    			Consumo consumo = itConsumos.next();
    			consumo.setProyecto(null);
    			consumoDAS.modificar(consumo);
    		}
    	}
    	proyectoDAS.eliminar(proyecto);
    }

    @Override
    public void guardarProyectoProducto(ProyectoProducto pp) throws SaiException
    {
    	if (pp.getCod() == null)
    	{
    		if (proyectoProductoDAS.existeProyectoProducto(pp.getProyecto(), pp.getProducto()))
    		{
    			throw new SaiException(srb.getString("proyecto.producto.guardar.error.yaexiste"));
    		}
    		proyectoProductoDAS.crear( pp );
    	}
    	else
    	{
    		proyectoProductoDAS.modificar( pp );
    	}
    }
    
    @Override
    public void eliminarProyectoProducto(ProyectoProducto pp) throws SaiException
    {
    	if (!consumoDAS.getConsumosActivosByProyectoProducto(pp.getProyecto(), pp.getProducto()).isEmpty())
    	{
    		throw new SaiException(srb.getString("proyecto.producto.eliminar.error.consumos"));
    	}
    	List<Consumo> listaConsumosAnulados = consumoDAS.getConsumosAnuladosByProyectoProducto(pp.getProyecto(), pp.getProducto());
    	if (!listaConsumosAnulados.isEmpty())
    	{
    		Iterator<Consumo> itConsumos = listaConsumosAnulados.iterator();
    		while (itConsumos.hasNext())
    		{
    			Consumo consumo = itConsumos.next();
    			consumo.setProyecto(null);
    			consumoDAS.modificar(consumo);
    		}
    	}
    	proyectoProductoDAS.eliminar(pp);
    }
    
    @Override
    public List<Proyecto> buscarProyectos()
    {
    	if (identity.tienePerfil(TipoPerfil.GESTOR))
    	{
    		return proyectoDAS.busquedaGestor();
    	}
    	else
    	{
    		List<Servicio> listaServiciosSupervisor = identity.getServiciosPerfil(TipoPerfil.SUPERVISOR);
    		List<Usuario> listaIrs = usuarioDAS.getIrsByMiembro(identity.getUsuarioConectado());
    		if (!listaIrs.contains(identity.getUsuarioConectado()))
    		{
    			listaIrs.add(identity.getUsuarioConectado());
    		}
    		return proyectoDAS.busquedaSupervisorIrUsuario(listaServiciosSupervisor, listaIrs);
    	}

    }
 
    @Override
    public List<Proyecto> getListaProyectosByProductoIr(Producto producto, Usuario usuarioIr)
    {
    	return proyectoDAS.getListaProyectosByProductoIr(producto, usuarioIr);
    }
    
    @Override
    public BigDecimal getCantidadConsumidaByProyecto(Proyecto proyecto)
    {
    	return consumoDAS.getCantidadConsumidaByProyecto(proyecto);
    }
    
    @Override
    public BigDecimal getCantidadConsumidaByProyectoProducto(Proyecto proyecto, Producto producto)
    {
    	return consumoDAS.getCantidadConsumidaByProyectoProducto( proyecto, producto );
    }
    
    @Override
    public BigDecimal getCantidadDisponibleByProyectoProducto(ProyectoProducto pp)
    {
    	BigDecimal cantidadConsumida = consumoDAS.getCantidadConsumidaByProyectoProducto(pp.getProyecto(), pp.getProducto());
    	return pp.getCantidad().subtract(cantidadConsumida);
    }
    
    @Override
    public BigDecimal getCantidadDisponibleByProyectoProducto(Proyecto proyecto, Producto producto)
    {
    	ProyectoProducto pp = proyectoProductoDAS.getProyectoProductoByProyectoProducto(proyecto, producto);
    	return this.getCantidadDisponibleByProyectoProducto(pp);
    }
    
    public BigDecimal getCantidadDisponibleByProyectoProductoEdicionFungible(Proyecto proyecto, Producto producto, Consumo consumo)
    {
    	ProyectoProducto pp = proyectoProductoDAS.getProyectoProductoByProyectoProducto(proyecto, producto);
    	List<Long> listaCodsConsumo = new ArrayList<>();
   		List<Consumo> listaFungiblesRelacionados = consumoDAS.getFungiblesRelacionados(consumo.getFechaSolicitud(), consumo.getUsuarioSolicitante());
   		for (Consumo con: listaFungiblesRelacionados)
   		{
   			listaCodsConsumo.add(con.getCod());
   		}
    	BigDecimal cantidadConsumida = consumoDAS.getCantidadConsumidaByProyectoProductoEdicion(proyecto, producto, listaCodsConsumo);
    	return pp.getCantidad().subtract(cantidadConsumida);
    }
    
    public List<ProyectoProducto> getListaProyectoProductoByProyecto(Proyecto proyecto)
    {
    	return proyectoProductoDAS.getListaProyectoProductoByProyecto( proyecto );
    }
    
    @Override
	public List<Consumo> getConsumosActivosByProyecto(Proyecto proyecto)
	{
		return consumoDAS.getConsumosActivosByProyecto( proyecto );
	}
	
    @Override
	public List<Consumo> getConsumosActivosByProyectoProducto(Proyecto proyecto, Producto producto)
	{
		return consumoDAS.getConsumosActivosByProyectoProducto( proyecto, producto );
	}
    
}
