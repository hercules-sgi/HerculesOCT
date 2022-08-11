package es.um.atica.sai.backbeans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.SeamResourceBundle;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.jboss.seam.log.Log;

import es.um.atica.sai.dtos.TipoPerfil;
import es.um.atica.sai.entities.Consumo;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.Proyecto;
import es.um.atica.sai.entities.ProyectoProducto;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.ConsumoService;
import es.um.atica.sai.services.interfaces.ProductoService;
import es.um.atica.sai.services.interfaces.ProyectoService;
import es.um.atica.sai.services.interfaces.UsuarioService;
import es.um.atica.sai.utils.Utilidades;

@Name(value = ProyectoEditBean.NAME )
@Scope(value = ScopeType.CONVERSATION)
public class ProyectoEditBean {

    public static final String NAME = "proyectoEdit";

    @Logger
    private Log log;
    
    @In(create=true) 
    protected FacesMessages facesMessages;

    @In(create=true)
    private SaiIdentity identity;
    
    @In(create=true)
    private UsuarioService usuarioService;
    
    @In(create=true)
    private ProyectoService proyectoService;
    
    @In(create=true)
    private ConsumoService consumoService;

    @In(create=true)
    private ProductoService productoService;
    
    ResourceBundle srb = SeamResourceBundle.getBundle();
    
    private Proyecto proyectoEdit;
	private List<Servicio> listaServicios;
	private List<Usuario> listaUsuariosIr;
	private ProyectoProducto proyectoProductoEdit;
	private String tipo;
	private BigDecimal cantidadAntesModificacion;
	private List<Producto> listaProductosAdd;
	private List<ProyectoProducto> listaProductosProyecto;
	private List<Consumo> listaConsumosProyecto;
	private BigDecimal totalConsumidoProyecto;

	public Proyecto getProyectoEdit() {
		return proyectoEdit;
	}
	
	public void setProyectoEdit( Proyecto proyectoEdit ) {
		this.proyectoEdit = proyectoEdit;
	}
	
	public List<Servicio> getListaServicios() {
		return listaServicios;
	}
	
	public void setListaServicios( List<Servicio> listaServicios ) {
		this.listaServicios = listaServicios;
	}

	public List<Usuario> getListaUsuariosIr() {
		return listaUsuariosIr;
	}
	
	public void setListaUsuariosIr( List<Usuario> listaUsuariosIr ) {
		this.listaUsuariosIr = listaUsuariosIr;
	}

	public ProyectoProducto getProyectoProductoEdit() {
		return proyectoProductoEdit;
	}
	
	public void setProyectoProductoEdit( ProyectoProducto proyectoProductoEdit ) {
		this.proyectoProductoEdit = proyectoProductoEdit;
	}

	public String getTipo() {
		return tipo;
	}
	
	public void setTipo( String tipo ) {
		this.tipo = tipo;
	}

	public BigDecimal getCantidadAntesModificacion() {
		return cantidadAntesModificacion;
	}
	
	public void setCantidadAntesModificacion( BigDecimal cantidadAntesModificacion ) {
		this.cantidadAntesModificacion = cantidadAntesModificacion;
	}

	public List<Producto> getListaProductosAdd() {
		return listaProductosAdd;
	}

	public void setListaProductosAdd( List<Producto> listaProductosAdd ) {
		this.listaProductosAdd = listaProductosAdd;
	}
	
	public List<ProyectoProducto> getListaProductosProyecto() {
		return listaProductosProyecto;
	}
	
	public void setListaProductosProyecto( List<ProyectoProducto> listaProductosProyecto ) {
		this.listaProductosProyecto = listaProductosProyecto;
	}

	public List<Consumo> getListaConsumosProyecto() {
		return listaConsumosProyecto;
	}
	
	public void setListaConsumosProyecto( List<Consumo> listaConsumosProyecto ) {
		this.listaConsumosProyecto = listaConsumosProyecto;
	}

	public BigDecimal getTotalConsumidoProyecto() {
		return totalConsumidoProyecto;
	}
	
	public void setTotalConsumidoProyecto( BigDecimal totalConsumidoProyecto ) {
		this.totalConsumidoProyecto = totalConsumidoProyecto;
	}


	public String establecerProyectoCreate()
	{
        this.proyectoEdit = new Proyecto();
        this.listaServicios = identity.getServiciosPerfil(TipoPerfil.SUPERVISOR);
        if (this.listaServicios != null && this.listaServicios.size() == 1)
        {
        	this.proyectoEdit.setServicio(this.listaServicios.get(0));
        	this.seleccionadoServicio();
        }
        return NAME;
	}
	
	public String establecerProyectoEdit(Proyecto proyecto)
	{
		this.proyectoEdit = proyecto;
		this.listaUsuariosIr = usuarioService.getIrsByServicio(this.proyectoEdit.getServicio());
		this.listaProductosProyecto = proyectoService.getListaProyectoProductoByProyecto(this.proyectoEdit);
		this.listaConsumosProyecto = proyectoService.getConsumosActivosByProyecto(this.proyectoEdit);
		this.totalConsumidoProyecto = proyectoService.getCantidadConsumidaByProyecto(this.proyectoEdit);
		return NAME;
	}
	
	public void seleccionadoServicio()
	{
		if (this.proyectoEdit.getServicio() == null)
		{
			this.proyectoEdit.setUsuarioIr(null);
			this.listaUsuariosIr = null;
		}
		else
		{
			this.listaUsuariosIr = usuarioService.getIrsByServicio(this.proyectoEdit.getServicio());
		}
	}
	
	public String guardar()
    {
		try 
        {
        	proyectoService.guardarProyecto(this.proyectoEdit);
        
        }
        catch (Exception ex) 
        {
        	log.error( "Error al guardar proyecto: #0", ex.getMessage() );
        	facesMessages.add(StatusMessage.Severity.ERROR, "proyecto.guardar.error", null, null, ex.getMessage()); 
        	return null;
        }
       	this.facesMessages.add(StatusMessage.Severity.INFO, "proyecto.guardar.ok", "proyecto.guardar.ok.detalles", null, null, this.proyectoEdit.getNombre());
       	return this.establecerProyectoEdit(this.proyectoEdit);
    }
	
    public String volver() 
    {
    	return "proyectos";
    }
    
    public void establecerProyectoProductoCreate()
    {
    	this.tipo = null;
    	this.proyectoProductoEdit = new ProyectoProducto();
    	this.proyectoProductoEdit.setProyecto(this.proyectoEdit);
    }
    
    public void establecerProyectoProductoEdit(ProyectoProducto pp)
    {
    	this.proyectoProductoEdit = pp;
    	this.cantidadAntesModificacion = pp.getCantidad();
    }
    
    public void seleccionadoTipo()
    {
    	if (this.tipo == null)
    	{
    		this.listaProductosAdd = new ArrayList<>();
    	}
    	else
    	{
    		this.listaProductosAdd = productoService.getProductosByServicioTipo(this.proyectoEdit.getServicio(), this.tipo, false, false);
    	}
    }

    public void guardarProyectoProducto()
    {
    	if (this.proyectoProductoEdit.getCod() != null && this.cantidadAntesModificacion.compareTo(this.proyectoProductoEdit.getCantidad())>0)
    	{
    		// Se ha reducido la cantidad inicial, tenemos que comprobar si es posible
    		BigDecimal cantidadConsumida = proyectoService.getCantidadConsumidaByProyectoProducto(this.proyectoEdit, this.proyectoProductoEdit.getProducto());
    		if (cantidadConsumida.compareTo(this.proyectoProductoEdit.getCantidad())>0)
    		{
    			facesMessages.add(StatusMessage.Severity.WARN, "proyecto.producto.guardar.error", "proyecto.producto.guardar.error.cantidadconsumida", null, null, cantidadConsumida.toString());
    			this.proyectoProductoEdit.setCantidad(this.cantidadAntesModificacion);
    			return;
    		}
    	}
    	try
    	{
    		proyectoService.guardarProyectoProducto(this.proyectoProductoEdit);
    	}
        catch (Exception ex) 
        {
        	log.error( "Error al guardar ProyectoProducto: #0", ex.getMessage() );
        	facesMessages.add(StatusMessage.Severity.ERROR, "proyecto.producto.guardar.error", null, null, ex.getMessage());
        	return;
        }
    	this.listaProductosProyecto = proyectoService.getListaProyectoProductoByProyecto(this.proyectoEdit);
    	this.facesMessages.add(StatusMessage.Severity.INFO, "proyecto.producto.guardar.ok", null, null, this.proyectoProductoEdit.getProducto().getDescripcion());
    }
    
    public void eliminarProyectoProducto(ProyectoProducto pp)
    {
    	try 
    	{
    		proyectoService.eliminarProyectoProducto(pp);
    	}
    	catch (Exception ex) 
    	{
	    	log.error( "Error al eliminar ProyectoProducto: #0", ex.getMessage() );
	    	facesMessages.add(StatusMessage.Severity.ERROR, "proyecto.producto.eliminar.error", null, null, ex.getMessage()); 
	    	return;
    	}
    	this.listaProductosProyecto.remove(pp);
    	this.facesMessages.add(StatusMessage.Severity.INFO, "proyecto.producto.eliminar.ok", null, null, pp.getProducto().getDescripcion());
    }
    
    public BigDecimal getCantidadConsumidaByProyectoProducto(ProyectoProducto pp)
    {
    	return proyectoService.getCantidadConsumidaByProyectoProducto(pp.getProyecto(), pp.getProducto());
    }
    
    public BigDecimal getCantidadDisponibleByProyectoProducto(ProyectoProducto pp)
    {
    	return proyectoService.getCantidadDisponibleByProyectoProducto(pp);
    }
    
	public String getDescripcionTipo(String tipo)
	{
		return consumoService.getDescripcionTipoConsumo( tipo );
	}
 
	public String colorEstado(Consumo c) 
	{
		return consumoService.getColorEstadoConsumo(c);
	}
	
	public String formatCantidad( BigDecimal number ) 
	{
		return Utilidades.formatCantidad(number);	
	}
	
	public String getSubtitulo()
	{
		if (identity.tienePerfilEnServicio(TipoPerfil.SUPERVISOR, this.proyectoEdit.getServicio().getCod()))
		{
			return srb.getString("proyectos.subtitle.edicion");
		}
		else
		{
			return srb.getString("proyectos.subtitle.consulta");
		}
	}
}
