package es.um.atica.sai.backbeans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.LazyDataModel;

import es.um.atica.commons.utils.UtilDate;
import es.um.atica.sai.backbeans.lazydatamodel.ConsumoCantidadLazyDataModel;
import es.um.atica.sai.backbeans.lazydatamodel.IrEntidadCantidadLazyDataModel;
import es.um.atica.sai.backbeans.lazydatamodel.IrGrupoinvestigacionCantidadLazyDataModel;
import es.um.atica.sai.dtos.ConsumoCantidad;
import es.um.atica.sai.dtos.IrEntidadCantidad;
import es.um.atica.sai.dtos.IrGrupoinvestigacionCantidad;
import es.um.atica.sai.dtos.ResumenConsumosExcelRespuesta;
import es.um.atica.sai.dtos.TipoPerfil;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.ConsumoService;
import es.um.atica.sai.services.interfaces.ProductoService;
import es.um.atica.sai.services.interfaces.ServicioService;
import es.um.atica.sai.services.interfaces.UsuarioService;
import es.um.atica.sai.utils.Utilidades;

@Name("consumoResumen")
@Scope(ScopeType.CONVERSATION)
public class ConsumoResumenBean {

  
    @Logger
    private Log log;
    
    @In(create = true, required = true)
    ServicioService servicioService;

    @In(create = true, required = true)
    UsuarioService usuarioService;

    @In(create = true, required = true)
    ConsumoService consumoService;
    
    @In(create = true, required = true)
    ProductoService productoService;
    
    @In(value="org.jboss.seam.security.identity", required=true)
    SaiIdentity identity;
    
    private boolean busquedaRealizada;
    private String tipo;
    private Servicio servicio;
    private String consumoInterno;
    private Producto producto;
    private Usuario usuarioInvestigador;
    private Usuario usuarioSolicitante;
    private String estado;
    private String estadoFacturacion;
    private Date fechaDesde;
    private Date fechaHasta;
    private String fechaDesdeString;
    private String fechaHastaString;    
    private List<Servicio> listaServicios;
    private List<Producto> listaProductos;
    private List<Usuario> listaUsuariosIr;
    private List<Usuario> listaUsuariosSolicitantes;
    private LazyDataModel<ConsumoCantidad> consumoCantidadLDM;
    private LazyDataModel<IrEntidadCantidad> irEntidadCantidadLDM;
    private LazyDataModel<IrGrupoinvestigacionCantidad> irGrupoinvestigacionCantidadLDM;
    private Long numIrsDistintos;
    private Long numMiembrosDistintos;
	
	public boolean isBusquedaRealizada() {
		return busquedaRealizada;
	}
	
	public void setBusquedaRealizada( boolean busquedaRealizada ) {
		this.busquedaRealizada = busquedaRealizada;
	}
	
	public String getTipo() {
		return tipo;
	}
	
	public void setTipo( String tipo ) {
		this.tipo = tipo;
	}

	public Servicio getServicio() {
		return servicio;
	}

	public void setServicio( Servicio servicio ) {
		this.servicio = servicio;
	}
	
	public String getConsumoInterno() {
		return consumoInterno;
	}
	
	public void setConsumoInterno( String consumoInterno ) {
		this.consumoInterno = consumoInterno;
	}
	
	public Producto getProducto() {
		return producto;
	}
	
	public void setProducto( Producto producto ) {
		this.producto = producto;
	}

	public Usuario getUsuarioInvestigador() {
		return usuarioInvestigador;
	}
	
	public void setUsuarioInvestigador( Usuario usuarioInvestigador ) {
		this.usuarioInvestigador = usuarioInvestigador;
	}

	public Usuario getUsuarioSolicitante() {
		return usuarioSolicitante;
	}
	
	public void setUsuarioSolicitante( Usuario usuarioSolicitante ) {
		this.usuarioSolicitante = usuarioSolicitante;
	}
	
	public String getEstado() {
		return estado;
	}
	
	public void setEstado( String estado ) {
		this.estado = estado;
	}

	public String getEstadoFacturacion() {
		return estadoFacturacion;
	}
	
	public void setEstadoFacturacion( String estadoFacturacion ) {
		this.estadoFacturacion = estadoFacturacion;
	}

	public String getFechaDesdeString() {
		return fechaDesdeString;
	}
	
	public void setFechaDesdeString( String fechaDesdeString ) {
		this.fechaDesdeString = fechaDesdeString;
	}
	
	public String getFechaHastaString() {
		return fechaHastaString;
	}
	
	public void setFechaHastaString( String fechaHastaString ) {
		this.fechaHastaString = fechaHastaString;
	}
	
	public List<Servicio> getListaServicios() {
		return listaServicios;
	}
	
	public void setListaServicios( List<Servicio> listaServicios ) {
		this.listaServicios = listaServicios;
	}

	public List<Producto> getListaProductos() {
		return listaProductos;
	}
	
	public void setListaProductos( List<Producto> listaProductos ) {
		this.listaProductos = listaProductos;
	}

	public List<Usuario> getListaUsuariosIr() {
		return listaUsuariosIr;
	}
	
	public void setListaUsuariosIr( List<Usuario> listaUsuariosIr ) {
		this.listaUsuariosIr = listaUsuariosIr;
	}
	
	public List<Usuario> getListaUsuariosSolicitantes() {
		return listaUsuariosSolicitantes;
	}
	
	public void setListaUsuariosSolicitantes( List<Usuario> listaUsuariosSolicitantes ) {
		this.listaUsuariosSolicitantes = listaUsuariosSolicitantes;
	}
	
	public LazyDataModel<ConsumoCantidad> getConsumoCantidadLDM() {
		return consumoCantidadLDM;
	}
	
	public void setConsumoCantidadLDM( LazyDataModel<ConsumoCantidad> consumoCantidadLDM ) {
		this.consumoCantidadLDM = consumoCantidadLDM;
	}

	public LazyDataModel<IrEntidadCantidad> getIrEntidadCantidadLDM() {
		return irEntidadCantidadLDM;
	}
	
	public void setIrEntidadCantidadLDM( LazyDataModel<IrEntidadCantidad> irEntidadCantidadLDM ) {
		this.irEntidadCantidadLDM = irEntidadCantidadLDM;
	}

	public LazyDataModel<IrGrupoinvestigacionCantidad> getIrGrupoinvestigacionCantidadLDM() {
		return irGrupoinvestigacionCantidadLDM;
	}
	
	public void setIrGrupoinvestigacionCantidadLDM(
			LazyDataModel<IrGrupoinvestigacionCantidad> irGrupoinvestigacionCantidadLDM ) {
		this.irGrupoinvestigacionCantidadLDM = irGrupoinvestigacionCantidadLDM;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde( Date fechaDesde ) {
		this.fechaDesdeString = UtilDate.convertirDateFechaToString(fechaDesde);
		this.fechaDesde = fechaDesde;
	}
	
	public Date getFechaHasta() {
		return fechaHasta;
	}
	
	public void setFechaHasta( Date fechaHasta ) {
		this.fechaHastaString = UtilDate.convertirDateFechaToString(fechaHasta);
		this.fechaHasta = fechaHasta;
	}

	public Long getNumIrsDistintos() {
		return numIrsDistintos;
	}
	
	public void setNumIrsDistintos( Long numIrsDistintos ) {
		this.numIrsDistintos = numIrsDistintos;
	}

	public Long getNumMiembrosDistintos() {
		return numMiembrosDistintos;
	}
	
	public void setNumMiembrosDistintos( Long numMiembrosDistintos ) {
		this.numMiembrosDistintos = numMiembrosDistintos;
	}

	@Create
    public void inicializa()
    {
    	this.busquedaRealizada = false;
    	this.listaServicios = identity.getServiciosPerfil(TipoPerfil.SUPERVISOR);
    	if (this.listaServicios.size() == 1)
    	{
    		this.servicio = this.listaServicios.get(0);
    		this.seleccionadoServicio();
    	}
    	this.listaProductos = productoService.getProductosByListaServicios(this.listaServicios);
    }
    
    public void seleccionadoServicio()
    {
    	if (this.servicio != null)
    	{
    		List<Usuario> listaUsuariosAux = usuarioService.getIrsByServicio(this.servicio); 
    		this.listaUsuariosIr = listaUsuariosAux;
    		this.listaUsuariosSolicitantes = listaUsuariosAux;
    		this.listaProductos = productoService.getProductosByServicio(this.servicio);
    	}
    	else
    	{
    		this.listaUsuariosIr = new ArrayList<>();
    		this.listaUsuariosSolicitantes = new ArrayList<>();
    		this.listaProductos = new ArrayList<>();
    	}
    }
    
    public void seleccionadoIr()
    {
    	if (this.usuarioInvestigador != null)
    	{
    		this.listaUsuariosSolicitantes = usuarioService.getMiembrosByIrServicio(this.usuarioInvestigador,this.servicio);
    	}
    }
    
    public void buscar()
    {
		final DataTable tablaConsumoResumen = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formResultados:consumoResumenTable");
		if (tablaConsumoResumen != null)
		{
			tablaConsumoResumen.setFirst(0);
		}
		final DataTable tablaIrEntidadResumen = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formResultados:IrEntidadResumenTable");
		if (tablaIrEntidadResumen != null)
		{
			tablaIrEntidadResumen.setFirst(0);
		}    	
		final DataTable tablaIrGrupoinvestigacionResumen = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formResultados:IrGrupoinvestigacionResumenTable");
		if (tablaIrGrupoinvestigacionResumen != null)
		{
			tablaIrGrupoinvestigacionResumen.setFirst(0);
		}    	
    	this.busquedaRealizada = true;
    	this.consumoCantidadLDM = new ConsumoCantidadLazyDataModel(this.estadoFacturacion);
    	this.irEntidadCantidadLDM = new IrEntidadCantidadLazyDataModel(this.estadoFacturacion);
    	this.irGrupoinvestigacionCantidadLDM = new IrGrupoinvestigacionCantidadLazyDataModel(this.estadoFacturacion);
    	this.numIrsDistintos = consumoService.getNumIrsConsumoResumen(this.estadoFacturacion);
    	this.numMiembrosDistintos = consumoService.getNumMiembrosConsumoResumen(this.estadoFacturacion);
    }
    
    public void limpiar()
    {
    	this.busquedaRealizada = false;
    	this.tipo = null;
    	if (this.listaServicios.size() > 1)
    	{
    		this.servicio = null;
    	}
    	this.listaProductos = productoService.getProductosByListaServicios(this.listaServicios);
    	this.producto = null;
    	this.consumoInterno = null;
    	this.listaUsuariosIr = null;
    	this.listaUsuariosSolicitantes = null;
    	this.estado = null;
    	this.estadoFacturacion = null;
    	this.setFechaDesde(null);
    	this.setFechaHasta(null);
    	this.consumoCantidadLDM = null;
    	this.irEntidadCantidadLDM = null;
    	this.irGrupoinvestigacionCantidadLDM = null;
    	this.numIrsDistintos = null;
    	this.numMiembrosDistintos = null;
    }
    
    public void obtenerConsultaExcel(String tipoDesglose)
    {
    	ResumenConsumosExcelRespuesta rcer = consumoService.getResumenConsumosExcel(tipoDesglose, 
    																				this.tipo, 
    																				this.servicio, 
    																				this.producto,
    																				this.consumoInterno, 
    																				this.usuarioInvestigador, 
    																				this.usuarioSolicitante,
    																				this.estado,
    																				this.estadoFacturacion,
    																				this.fechaDesdeString,
    																				this.fechaHastaString);
        if (rcer.getCodRespuesta() != 0)
        {
            log.error("Error obteniendo resumen de consumos en excel: #0",rcer.getMenRespuesta());
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "No se ha podido obtener el listado en excel.", rcer.getMenRespuesta());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }
        try
        {
            log.info("Obtenido el listado de resumen de consumos en excel, llamamos a mostrar fichero");
            String nombreFichero = new StringBuilder("RESUMEN_").append(tipoDesglose).toString();
            Utilidades.mostrarFichero(rcer.getFicheroExcel(), nombreFichero, "EXCEL");
        }
        catch(Exception ex)
        {
            log.error("Error mostrando resumen de consumos en excel: #0",ex.toString());
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se ha podido obtener el listado en excel.",ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
}
