package es.um.atica.sai.backbeans;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import es.um.atica.sai.dtos.TipoPerfil;
import es.um.atica.sai.entities.Consumo;
import es.um.atica.sai.entities.EntidadPagadora;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.ConsumoService;
import es.um.atica.sai.services.interfaces.ServicioService;
import es.um.atica.sai.services.interfaces.TarifaService;
import es.um.atica.sai.services.interfaces.UsuarioService;

@Name("estimacion")
@Scope(ScopeType.CONVERSATION)
public class EstimacionBean {
    @Logger
    private Log log;        
        
    @In("org.jboss.seam.security.identity")
    SaiIdentity identity;

    @In(create = true, required = true)
    UsuarioService usuarioService;
 
    @In(create = true, required = true)
    ServicioService servicioService;
    
    @In(create = true, required = true)
    ConsumoService consumoService;
    
    @In(create = true, required = true)
    TarifaService tarifaService;
    
    private List<Usuario> listaUsuariosIr;
    private List<EntidadPagadora> listaEntidades;
    private List<Servicio> listaServicios;
    private Usuario usuarioIrFiltrar;
    private EntidadPagadora entidadFiltrar;
    private Servicio servicioFiltrar;
    private String estadoFiltrar;
    private String tipoFiltrar;
    private boolean busquedaRealizada;

    private List<Consumo> listaConsumos;
    
	public Log getLog() {
		return log;
	}
	
	public void setLog( Log log ) {
		this.log = log;
	}
	
	public List<Usuario> getListaUsuariosIr() {
		return listaUsuariosIr;
	}
	
	public void setListaUsuariosIr( List<Usuario> listaUsuariosIr ) {
		this.listaUsuariosIr = listaUsuariosIr;
	}

	public List<EntidadPagadora> getListaEntidades() {
		return listaEntidades;
	}
	
	public void setListaEntidades( List<EntidadPagadora> listaEntidades ) {
		this.listaEntidades = listaEntidades;
	}
	
	public List<Servicio> getListaServicios() {
		return listaServicios;
	}

	public void setListaServicios( List<Servicio> listaServicios ) {
		this.listaServicios = listaServicios;
	}
	
	public Usuario getUsuarioIrFiltrar() {
		return usuarioIrFiltrar;
	}
	
	public void setUsuarioIrFiltrar( Usuario usuarioIrFiltrar ) {
		this.usuarioIrFiltrar = usuarioIrFiltrar;
	}
	
	public EntidadPagadora getEntidadFiltrar() {
		return entidadFiltrar;
	}
	
	public void setEntidadFiltrar( EntidadPagadora entidadFiltrar ) {
		this.entidadFiltrar = entidadFiltrar;
	}
	
	public Servicio getServicioFiltrar() {
		return servicioFiltrar;
	}

	public void setServicioFiltrar( Servicio servicioFiltrar ) {
		this.servicioFiltrar = servicioFiltrar;
	}

	public String getEstadoFiltrar() {
		return estadoFiltrar;
	}
	
	public void setEstadoFiltrar( String estadoFiltrar ) {
		this.estadoFiltrar = estadoFiltrar;
	}

	public String getTipoFiltrar() {
		return tipoFiltrar;
	}
	
	public void setTipoFiltrar( String tipoFiltrar ) {
		this.tipoFiltrar = tipoFiltrar;
	}
	
	public boolean isBusquedaRealizada() {
		return busquedaRealizada;
	}
	
	public void setBusquedaRealizada( boolean busquedaRealizada ) {
		this.busquedaRealizada = busquedaRealizada;
	}

	public List<Consumo> getListaConsumos() {
		return listaConsumos;
	}
	
	public void setListaConsumos( List<Consumo> listaConsumos ) {
		this.listaConsumos = listaConsumos;
	}

	@Create
    public void inicializar() 
	{
	   this.listaUsuariosIr = usuarioService.getIrsParaEstimacion();
	   if (this.listaUsuariosIr.size()==1)
	   {
		   this.usuarioIrFiltrar = this.listaUsuariosIr.get(0);
		   this.listaEntidades = tarifaService.getEntidadesByIr(this.usuarioIrFiltrar);
	   }
       this.listaServicios = identity.getServiciosPerfil(TipoPerfil.IR);
       if (this.listaServicios.size() == 1)
       {
    	   this.servicioFiltrar = this.listaServicios.get(0);
       }
       this.busquedaRealizada = false;
    }
     
	public void seleccionadoUsuarioIr()
	{
		this.listaEntidades = tarifaService.getEntidadesByIr(this.usuarioIrFiltrar);
	}
	
    public void filtrar() 
    {
    	this.listaConsumos = consumoService.busquedaConsumosEstimacion();
    	this.busquedaRealizada = true;
    }
    
    public void limpiar()
    {
    	this.listaConsumos = null;
    	if (this.listaUsuariosIr.size()==1)
    	{
    		this.usuarioIrFiltrar = this.listaUsuariosIr.get(0);
    		this.listaEntidades = tarifaService.getEntidadesByIr(this.usuarioIrFiltrar);
    	}
    	else
    	{
    		this.usuarioIrFiltrar = null;
    	}
    	this.entidadFiltrar = null;
    	if (this.listaServicios.size() > 1)
    	{
    		this.servicioFiltrar = null;
    	}
    	this.tipoFiltrar = null;
    	this.estadoFiltrar = null;
    	this.busquedaRealizada = false;
    }
    
    public String getTarifa(Consumo consumo) 
    {
        if (consumo.getEntidadPagadora() != null) 
        {
        	BigDecimal tarifa = tarifaService.obtenerTarifaConsumo(consumo, consumo.getEntidadPagadora());
        	if (tarifa == null)
        	{
        		return null;
        	}
            return tarifa.toString();
        }
        else
        {
            return null;
        }
    }
    
    public BigDecimal getPrecioConsumo(Consumo consumo)
    {
    	return tarifaService.obtenerPrecioConsumo( consumo, consumo.getEntidadPagadora());
    }
    
    public BigDecimal getTotal()
    {
    	BigDecimal total = new BigDecimal(0);
    	if (this.listaConsumos == null || this.listaConsumos.isEmpty())
    	{
    		return new BigDecimal(0);
    	}
    	Iterator<Consumo> it = this.listaConsumos.iterator();
    	while ( it.hasNext() ) 
    	{
    		Consumo consumo = it.next();
    		BigDecimal precio = this.getPrecioConsumo(consumo);
    		if ( precio != null )
    		{
    			total = total.add(precio);
    		}
    	}
    	return total;
    }
    
	public String getDescripcionTipoConsumo(String tipo)
	{
		return consumoService.getDescripcionTipoConsumo( tipo );
	}
    
    public String getDescripcionEntidadPagadora(EntidadPagadora ep)
    {
    	return tarifaService.getDescripcionEntidadPagadora( ep );
    }
    
}
