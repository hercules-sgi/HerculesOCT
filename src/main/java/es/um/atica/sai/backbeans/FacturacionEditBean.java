package es.um.atica.sai.backbeans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
import org.primefaces.component.export.PDFOptions;

import es.um.atica.commons.utils.UtilDate;
import es.um.atica.sai.backbeans.converter.PartidaConverter;
import es.um.atica.sai.dtos.EntidadRespuesta;
import es.um.atica.sai.dtos.EstadoConsumo;
import es.um.atica.sai.dtos.Partida;
import es.um.atica.sai.dtos.Subtercero;
import es.um.atica.sai.dtos.TipoGasto;
import es.um.atica.sai.dtos.TipoPerfil;
import es.um.atica.sai.entities.Consumo;
import es.um.atica.sai.entities.EntidadPagadora;
import es.um.atica.sai.entities.Factura;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.ConsumoService;
import es.um.atica.sai.services.interfaces.ProductoService;
import es.um.atica.sai.services.interfaces.ServicioService;
import es.um.atica.sai.services.interfaces.TarifaService;
import es.um.atica.sai.services.interfaces.UsuarioService;

@Name("facturacionEdit")
@Scope(ScopeType.CONVERSATION)
public class FacturacionEditBean {

	ResourceBundle srb = SeamResourceBundle.getBundle();
  
    @Logger
    private Log log;
    
    @In(create = true, required = true)
    ServicioService servicioService;

    @In(create = true, required = true)
    UsuarioService usuarioService;
    
    @In(create = true)
    TarifaService tarifaService;
    
    @In(create = true)
    ConsumoService consumoService;
    
    @In(create = true)
    ProductoService productoService;
    
    @In(value="org.jboss.seam.security.identity", required=true)
    SaiIdentity identity;
    
    @In(create=true) 
    protected FacesMessages facesMessages;
    
    private static final String FACTURA_GUARDAR_OK = "factura.guardar.ok";
    private static final String FACTURA_CONSUMO_ADD_ERROR = "factura.consumo.add.error";
   
    // Creación de factura 
	private Factura facturaEdit;
    private List<Servicio> serviciosDisponiblesParaFacturar;
    private List<Usuario> listaUsuariosIrCrearFactura;
    private List<EntidadPagadora> listaEntidadesPagadoras;
    private List<TipoGasto> listaTiposGasto;
    private List<Partida> listaPartidas;
    private boolean cambiosGuardados;

    // Filtrar consumos pendientes
    private List<Consumo> listaConsumosPendientesFacturar;
    private Long codFiltrarConsumosPendientes;
    private String tipoFiltrarConsumosPendientes;
    private Date fechaDesdeFiltrarConsumosPendientes;
    private Date fechaHastaFiltrarConsumosPendientes;
    private String fechaDesdeStringFiltrarConsumosPendientes;
    private String fechaHastaStringFiltrarConsumosPendientes;
    private boolean soloEstadosFacturablesFiltrarConsumosPendientes;
	private boolean consumosPendientesFiltrados;  
	private List<Consumo> listaConsumosFactura;  

	private PDFOptions pdfOpt;
	
	public PDFOptions getPdfOpt() {
		return pdfOpt;
	}
	
	public void setPdfOpt( PDFOptions pdfOpt ) {
		this.pdfOpt = pdfOpt;
	}

	public Log getLog() {
		return log;
	}	
	
	public void setLog( Log log ) {
		this.log = log;
	}
	
	public Factura getFacturaEdit() {
		return facturaEdit;
	}
	
	public void setFacturaEdit( Factura facturaEdit ) {
		this.facturaEdit = facturaEdit;
	}
	
	public List<Servicio> getServiciosDisponiblesParaFacturar() {
		return serviciosDisponiblesParaFacturar;
	}
	
	public void setServiciosDisponiblesParaFacturar( List<Servicio> serviciosDisponiblesParaFacturar ) {
		this.serviciosDisponiblesParaFacturar = serviciosDisponiblesParaFacturar;
	}

	public List<Usuario> getListaUsuariosIrCrearFactura() {
		return listaUsuariosIrCrearFactura;
	}

	public void setListaUsuariosIrCrearFactura( List<Usuario> listaUsuariosIrCrearFactura ) {
		this.listaUsuariosIrCrearFactura = listaUsuariosIrCrearFactura;
	}
	
	public List<EntidadPagadora> getListaEntidadesPagadoras() {
		return listaEntidadesPagadoras;
	}
	
	public void setListaEntidadesPagadoras( List<EntidadPagadora> listaEntidadesPagadoras ) {
		this.listaEntidadesPagadoras = listaEntidadesPagadoras;
	}

	public List<TipoGasto> getListaTiposGasto() {
		return listaTiposGasto;
	}
	
	public void setListaTiposGasto( List<TipoGasto> listaTiposGasto ) {
		this.listaTiposGasto = listaTiposGasto;
	}
	
	public List<Partida> getListaPartidas() {
		return listaPartidas;
	}
	
	public void setListaPartidas( List<Partida> listaPartidas ) {
		this.listaPartidas = listaPartidas;
	}
	
	public boolean isCambiosGuardados() {
		return cambiosGuardados;
	}
	
	public void setCambiosGuardados( boolean cambiosGuardados ) {
		this.cambiosGuardados = cambiosGuardados;
	}
	
	public List<Consumo> getListaConsumosPendientesFacturar() {
		return listaConsumosPendientesFacturar;
	}

	public void setListaConsumosPendientesFacturar( List<Consumo> listaConsumosPendientesFacturar ) {
		this.listaConsumosPendientesFacturar = listaConsumosPendientesFacturar;
	}

	public Long getCodFiltrarConsumosPendientes() {
		return codFiltrarConsumosPendientes;
	}
	
	public void setCodFiltrarConsumosPendientes( Long codFiltrarConsumosPendientes ) {
		this.codFiltrarConsumosPendientes = codFiltrarConsumosPendientes;
	}

	public String getTipoFiltrarConsumosPendientes() {
		return tipoFiltrarConsumosPendientes;
	}
	
	public void setTipoFiltrarConsumosPendientes( String tipoFiltrarConsumosPendientes ) {
		this.tipoFiltrarConsumosPendientes = tipoFiltrarConsumosPendientes;
	}
	
	public String getFechaDesdeStringFiltrarConsumosPendientes() {
		return fechaDesdeStringFiltrarConsumosPendientes;
	}
	
	public void setFechaDesdeStringFiltrarConsumosPendientes( String fechaDesdeStringFiltrarConsumosPendientes ) {
		this.fechaDesdeStringFiltrarConsumosPendientes = fechaDesdeStringFiltrarConsumosPendientes;
	}
	
	public String getFechaHastaStringFiltrarConsumosPendientes() {
		return fechaHastaStringFiltrarConsumosPendientes;
	}
	
	public void setFechaHastaStringFiltrarConsumosPendientes( String fechaHastaStringFiltrarConsumosPendientes ) {
		this.fechaHastaStringFiltrarConsumosPendientes = fechaHastaStringFiltrarConsumosPendientes;
	}
	
	public boolean isSoloEstadosFacturablesFiltrarConsumosPendientes() {
		return soloEstadosFacturablesFiltrarConsumosPendientes;
	}
	
	public void setSoloEstadosFacturablesFiltrarConsumosPendientes(
			boolean soloEstadosFacturablesFiltrarConsumosPendientes ) {
		this.soloEstadosFacturablesFiltrarConsumosPendientes = soloEstadosFacturablesFiltrarConsumosPendientes;
	}

	public boolean isConsumosPendientesFiltrados() {
		return consumosPendientesFiltrados;
	}
	
	public void setConsumosPendientesFiltrados( boolean consumosPendientesFiltrados ) {
		this.consumosPendientesFiltrados = consumosPendientesFiltrados;
	}
	
	public List<Consumo> getListaConsumosFactura() {
		return listaConsumosFactura;
	}
	
	public void setListaConsumosFactura( List<Consumo> listaConsumosFactura ) {
		this.listaConsumosFactura = listaConsumosFactura;
	}

	public Date getFechaDesdeFiltrarConsumosPendientes() {
		return fechaDesdeFiltrarConsumosPendientes;
	}

	public void setFechaDesdeFiltrarConsumosPendientes( Date fechaDesdeFiltrarConsumosPendientes ) {
		this.fechaDesdeStringFiltrarConsumosPendientes = UtilDate.convertirDateFechaToString(fechaDesdeFiltrarConsumosPendientes);
		this.fechaDesdeFiltrarConsumosPendientes = fechaDesdeFiltrarConsumosPendientes;
	}

	public Date getFechaHastaFiltrarConsumosPendientes() {
		return fechaHastaFiltrarConsumosPendientes;
	}
	
	public void setFechaHastaFiltrarConsumosPendientes( Date fechaHastaFiltrarConsumosPendientes ) {
		this.fechaHastaStringFiltrarConsumosPendientes = UtilDate.convertirDateFechaToString(fechaHastaFiltrarConsumosPendientes);
		this.fechaHastaFiltrarConsumosPendientes = fechaHastaFiltrarConsumosPendientes;
	}

    public String establecerFacturaCrear()
    {
    	this.facturaEdit = new Factura();
    	this.facturaEdit.setFechaGeneracion(new Date());
    	this.serviciosDisponiblesParaFacturar = identity.getServiciosPerfil(TipoPerfil.ADMINISTRATIVO);
    	this.consumosPendientesFiltrados=false;
		this.listaEntidadesPagadoras = null;
		this.listaTiposGasto = null;
		this.listaPartidas = null;
		this.listaConsumosPendientesFacturar = new ArrayList<>();
		this.listaConsumosFactura = new ArrayList<>();
		this.cambiosGuardados = false;
		establecerOpcionesPdf();
    	return "facturaEdit";
    }
    
    public String establecerFacturaEdit(Factura factura)
    {
    	this.facturaEdit = factura;
       	this.consumosPendientesFiltrados=false;
    	if (this.facturaEdit.getConsumos()!=null && !this.facturaEdit.getConsumos().isEmpty())
    	{
    		this.listaConsumosFactura = new ArrayList<>();
    		Iterator<Consumo> itConsumos = this.facturaEdit.getConsumos().iterator();
    		while (itConsumos.hasNext())
    		{
    			this.listaConsumosFactura.add(itConsumos.next());
    		}
    	}
    	this.cambiosGuardados = true;
    	establecerOpcionesPdf();
    	return "facturaEdit";
    }

    
    private void establecerOpcionesPdf()
    {
    	this.pdfOpt = new PDFOptions();
    	this.pdfOpt.setCellFontSize("6");
    	this.pdfOpt.setFacetFontSize("8");
    }
    
    // Pantalla de edición
    public void seleccionadoServicioCreacion()
    {
    	if (this.facturaEdit.getServicio() != null)
    	{
    		this.listaUsuariosIrCrearFactura = usuarioService.getIrsConConsumosPendientes(this.facturaEdit.getServicio());
    		if (this.listaUsuariosIrCrearFactura.size()==1)
    		{
    			this.facturaEdit.setInvestigador(this.listaUsuariosIrCrearFactura.get(0));
    		}
    	}
    	else
    	{
    		this.listaUsuariosIrCrearFactura = null;
    	}
    	this.consumosPendientesFiltrados=false;
		this.listaEntidadesPagadoras=null;
		if (this.listaUsuariosIrCrearFactura == null ||(this.facturaEdit.getInvestigador()!=null && this.listaUsuariosIrCrearFactura.contains(this.facturaEdit.getInvestigador())))
		{
			this.facturaEdit.setInvestigador(null);
			this.seleccionadoIrCreacion();
		}
		this.facturaEdit.setEntidadPagadora(null);
		this.listaTiposGasto=null;
		this.listaPartidas=null;
		this.facturaEdit.setEntidadPagadora(null);
		this.facturaEdit.setTipoGasto(null);
		this.facturaEdit.setSerie(null);
		this.facturaEdit.setPartida(null);
		this.listaConsumosPendientesFacturar=null;
		this.listaConsumosFactura=null;
		this.cambiosGuardados = false;
		if (this.facturaEdit.getInvestigador() != null)
		{
			this.seleccionadoIrCreacion();
		}
    }
    
    public void seleccionadoIrCreacion()
    {
    	if (this.facturaEdit.getInvestigador() != null)
    	{
    		this.listaEntidadesPagadoras = tarifaService.getEntidadesByIr(this.facturaEdit.getInvestigador());
    		if (this.listaEntidadesPagadoras.size()==1)
    		{
    			this.facturaEdit.setEntidadPagadora(this.listaEntidadesPagadoras.get(0));
    		}
    	}
    	else
    	{
    		this.listaEntidadesPagadoras=null;
    	}
    	this.consumosPendientesFiltrados=false;
		this.setListaTiposGasto(null);
		this.setListaPartidas(null);
		if (this.listaEntidadesPagadoras==null || (this.facturaEdit.getEntidadPagadora()!=null && !this.listaEntidadesPagadoras.contains(this.facturaEdit.getEntidadPagadora())))
		{
			this.facturaEdit.setEntidadPagadora(null);
			this.seleccionadaEntidadPagadoraCreacion();
		}
		this.facturaEdit.setTipoGasto(null);
		this.facturaEdit.setSerie(null);
		this.facturaEdit.setPartida(null);
		this.listaConsumosPendientesFacturar=null;
		this.listaConsumosFactura=null;
		this.cambiosGuardados = false;
		if (this.facturaEdit.getEntidadPagadora()!=null)
		{
			this.seleccionadaEntidadPagadoraCreacion();
		}
    }
    
    public void seleccionadaEntidadPagadoraCreacion()
    {
    	if (this.facturaEdit.getEntidadPagadora() != null)
    	{
    		if (this.facturaEdit.getEntidadPagadora().getUnidadAdministrativa() != null)
    		{
    			this.setListaTiposGasto(tarifaService.getTiposGasto());
    		}
    		this.setListaPartidas(tarifaService.getPartidasByEntidadPagadora(this.facturaEdit.getEntidadPagadora()));
    	}
    	else
    	{
    		this.setListaTiposGasto(null);
    		this.setListaPartidas(null);
    	}
    	this.facturaEdit.setTipoGasto(null);
    	this.facturaEdit.setSerie(null);
    	this.facturaEdit.setPartida(null);
    	this.consumosPendientesFiltrados=false;
    	this.soloEstadosFacturablesFiltrarConsumosPendientes=false;
		this.listaConsumosPendientesFacturar=null;
		this.listaConsumosFactura=null;
    	this.cambiosGuardados = false;
    }
    
    public void filtrarConsumosPendientes()
    {
    	this.setListaConsumosPendientesFacturar(consumoService.getConsumosPendientesFacturar(this.facturaEdit.getInvestigador(), this.facturaEdit.getServicio(), this.facturaEdit.getEntidadPagadora(),this.soloEstadosFacturablesFiltrarConsumosPendientes));
    	this.consumosPendientesFiltrados=true;
    }
    
    public BigDecimal obtenerTarifaByConsumo(Consumo consumo)
    {
    	if (consumo.getFactura()!=null && consumo.getFactura().getNumeroJusto()!=null)
    	{
    		return consumo.getImporteTarifaFactura();
    	}
    	else
    	{
    		return tarifaService.obtenerTarifaConsumo(consumo,this.facturaEdit.getEntidadPagadora());
	    }
    }
    
    public BigDecimal getSubtotalConsumo(Consumo consumo)
    {
    	if (consumo.getFactura()!=null && consumo.getFactura().getNumeroJusto()!=null)
    	{
    		return tarifaService.obtenerPrecioConsumoEnviadoJusto( consumo );
    	}
    	else
    	{
    		return tarifaService.obtenerPrecioConsumo( consumo, this.facturaEdit.getEntidadPagadora());
    	}
    }
    
    public BigDecimal getImporteTotalFactura()
    {
    	if (this.facturaEdit.getNumeroJusto()!=null)
    	{
    		return tarifaService.getImporteTotalFacturaEnviadaJusto(this.facturaEdit);
    	}
    	else
    	{	
    		return tarifaService.getImporteTotalFactura(this.listaConsumosFactura, this.getFacturaEdit().getEntidadPagadora());
    	}
    }
    
     
    public void seleccionarConsumosPendientes()
    {
    	Iterator<Consumo> itConsumo = this.listaConsumosPendientesFacturar.iterator();
    	while (itConsumo.hasNext())
    	{
    		Consumo consumo = itConsumo.next();
    		if (tarifaService.obtenerTarifaConsumo( consumo, this.facturaEdit.getEntidadPagadora()) != null)
    		{
    			consumo.setSeleccionado(true);
    		}
    	}
    }
    
    public void deseleccionarConsumosPendientes()
    {
    	Iterator<Consumo> itConsumo = this.listaConsumosPendientesFacturar.iterator();
    	while (itConsumo.hasNext())
    	{
    		itConsumo.next().setSeleccionado(false);
    	}
    }
    
    public boolean permitidoAddConsumoFactura(Consumo consumo)
    {
    	return EstadoConsumo.FINALIZADO.equals(consumo.getEstado());
    }
    
    public void addConsumoFactura(Consumo consumo)
    {
    	if (this.listaConsumosFactura == null)
    	{
    		this.listaConsumosFactura = new ArrayList<>();
    	}
    	if (!permitidoAddConsumoFactura(consumo))
    	{
    		this.facesMessages.add( StatusMessage.Severity.WARN, 
    								FACTURA_CONSUMO_ADD_ERROR,
    								"factura.consumo.add.error.estadonovalido",
    								null, null,
    								consumo.getEstado());
    		return;
    	}
    	
    	if (this.listaConsumosFactura.contains(consumo))
    	{
    		this.facesMessages.add(StatusMessage.Severity.WARN, 
    							   FACTURA_CONSUMO_ADD_ERROR,
    	 			  			   "factura.consumo.add.yaexiste",	
    	 			  			   null, null,  
    	 			  			   consumo.getCod().toString()); 
    		return;
    	}
    	BigDecimal totalConsumo = tarifaService.obtenerPrecioConsumo(consumo, this.facturaEdit.getEntidadPagadora());
    	if (totalConsumo == null || totalConsumo.compareTo(new BigDecimal(0))==0)
    	{
    		this.facesMessages.add(StatusMessage.Severity.WARN, 
    							   FACTURA_CONSUMO_ADD_ERROR,
    							   "factura.consumo.add.error.notarifa",	
    							   null, null,  
    							   consumo.getCod().toString()); 
			return;
    	}
   		// Añadimos el consumo
   		this.listaConsumosFactura.add(consumo);
		this.facesMessages.add(StatusMessage.Severity.INFO, 
				   				"factura.consumo.add.ok",
				   				null, null,  
				   				consumo.getCod().toString()); 
		this.cambiosGuardados = false;
    }
    
    public void addConsumosSeleccionadosFactura()
    {
    	if (this.listaConsumosFactura == null)
    	{
    		this.listaConsumosFactura = new ArrayList<>();
    	}
    	int consumosSeleccionados = 0;
    	int consumosOk = 0;
    	int consumosError = 0;
    	Iterator<Consumo> itConsumo = this.listaConsumosPendientesFacturar.iterator();
    	while (itConsumo.hasNext())
    	{
    		Consumo consumo = itConsumo.next();
    		if (consumo.isSeleccionado())
    		{
    			consumosSeleccionados++;
    			BigDecimal totalConsumo = tarifaService.obtenerPrecioConsumo(consumo, this.facturaEdit.getEntidadPagadora());
    			if (!permitidoAddConsumoFactura(consumo) || this.listaConsumosFactura.contains(consumo) || totalConsumo == null || totalConsumo.compareTo(new BigDecimal(0))==0)
    			{
    				consumosError++;
    			}
    			else
    			{
    				this.listaConsumosFactura.add( consumo );
    				consumosOk++;
    			}
    		}
    	}
    	if (consumosSeleccionados == 0)
    	{
    		this.facesMessages.add(StatusMessage.Severity.WARN, 
					   				"factura.consumo.add.noseleccion",
					   				"factura.consumo.add.noseleccion.detalles",	
					   				null, null); 
    	}
    	else if (consumosError==0)
    	{
    		this.facesMessages.add(StatusMessage.Severity.INFO, 
	   								"factura.consumos.add.ok",
	   								"factura.consumos.add.ok.detalles",	
	   								null, null,
	   								Integer.toString(consumosOk)); 
    		this.cambiosGuardados = false;		   
    	}
    	else if (consumosOk==0)
    	{
    		this.facesMessages.add(StatusMessage.Severity.WARN, 
	   							"factura.consumos.add.error",
	   							"factura.consumos.add.error.detalles",	
	   							null, null,
	   							Integer.toString(consumosError)); 
    	} 
    	else
    	{
    		this.facesMessages.add(StatusMessage.Severity.WARN, 
						"factura.consumos.add.ok",
						"factura.consumos.add.okerror.detalles",	
						null, null,
						Integer.toString(consumosOk),
						Integer.toString(consumosError)); 
    		this.cambiosGuardados = false;					   
    	}
    }
    
    public void delConsumoFactura(Consumo consumo)
    {
    	try
    	{
    		this.listaConsumosFactura.remove(consumo);
    		if (consumo.getFactura()!=null)
    		{
    			consumo.setFactura( null );
    			consumoService.guardarConsumo(consumo);
    		}
    		this.facesMessages.add(StatusMessage.Severity.INFO, 
						"factura.consumo.del.ok",
						"factura.consumo.del.ok.detalles",	
						null, null,
						consumo.getCod().toString()); 
    		this.cambiosGuardados = false;
    	}
    	catch(Exception ex)
    	{
    		this.facesMessages.add(StatusMessage.Severity.ERROR, 
						"factura.consumo.del.error",
						null, null,
						ex.getCause().getMessage()); 
    	}
    }
    
    public void delConsumosFacturaTodos()
    {
    	try
    	{
	    	this.listaConsumosFactura = new ArrayList<>();
			Iterator<Consumo> itConsumosFactura = this.listaConsumosFactura.iterator();
			while (itConsumosFactura.hasNext())
			{
				Consumo consumo = itConsumosFactura.next();
				this.listaConsumosFactura.remove(consumo);
		    	if (consumo.getFactura()!=null)
		    	{
		    		consumo.setFactura( null );
		    		consumoService.guardarConsumo(consumo);
		    	}			
			} 
    		this.facesMessages.add(StatusMessage.Severity.INFO, 
								"factura.consumos.del.ok",
								"factura.consumos.del.ok.detalles",
								null, null); 
			this.cambiosGuardados = false;
    	}
    	catch (Exception ex)
    	{
    		this.facesMessages.add(StatusMessage.Severity.ERROR, 
								  "generico.error.inesperado",
								  	null, null,
								  	ex.getCause().getMessage()); 
    	}
    }
    
    public void crearFactura()
    {
    	try
    	{
	    	consumoService.crearFactura(this.facturaEdit, this.listaConsumosFactura);
			if (this.listaConsumosFactura != null && !this.listaConsumosFactura.isEmpty())
			{
	    		this.facesMessages.add(StatusMessage.Severity.INFO, 
						  			   FACTURA_GUARDAR_OK,
						  			   "factura.guardar.ok.conconsumos",
						  			   null, null,
						  			    String.valueOf(this.listaConsumosFactura.size())); 
			}
			else
			{
	    		this.facesMessages.add(StatusMessage.Severity.INFO, 
			  			   				FACTURA_GUARDAR_OK,
			  			   				"factura.guardar.ok.sinconsumos",
			  			   				null, null); 
			}
			this.cambiosGuardados = true;
    	}
    	catch(Exception ex)
    	{
    		this.facesMessages.add(StatusMessage.Severity.ERROR, 
		   						"factura.guardar.error",
		   						null, null,
		   						ex.getCause().getMessage()); 
    	}	 
    }
    
    public void modificarFactura()
    {
    	try
    	{
	    	consumoService.modificarFactura(this.facturaEdit, this.listaConsumosFactura);
			if (this.listaConsumosFactura != null && !this.listaConsumosFactura.isEmpty())
			{
	    		this.facesMessages.add(StatusMessage.Severity.INFO, 
			  			   FACTURA_GUARDAR_OK,
			  			   "factura.guardar.ok.conconsumos",
			  			   null, null,
			  			    String.valueOf(this.listaConsumosFactura.size())); 
			}
			else
			{
	    		this.facesMessages.add(StatusMessage.Severity.INFO, 
			   				FACTURA_GUARDAR_OK,
			   				"factura.guardar.ok.sinconsumos",
			   				null, null); 
			}
			this.cambiosGuardados = true;
    	}
    	catch(Exception ex)
    	{
    		this.facesMessages.add(StatusMessage.Severity.ERROR, 
    								"factura.guardar.error",
    								null, null,
    								ex.getCause().getMessage()); 
    	}	 
    }
    
    public String eliminarFactura()
    {
    	try
    	{
	    	consumoService.eliminarFactura(this.facturaEdit);
    		this.facesMessages.add(StatusMessage.Severity.INFO, 
	   							  "factura.eliminar.ok",
	   							  "factura.eliminar.ok.detalles",
	   							  null, null); 
			return "facturaListado";
    	}
    	catch(Exception ex)
    	{
    		this.facesMessages.add(StatusMessage.Severity.ERROR, 
								  "factura.eliminar.error",
								  null, null,
								  ex.getCause().getMessage()); 
			return null;
    	}	    
    }
    
    public void enviarFacturaJusto()
    {
		// Obtenemos el importe
		EntidadRespuesta permitidoFacturarRespuesta = tarifaService.permitidoEnvioFacturaJusto( this.listaConsumosFactura, this.facturaEdit.getEntidadPagadora());
		if (!(boolean)permitidoFacturarRespuesta.getEntidad())
		{
    		this.facesMessages.add(StatusMessage.Severity.WARN, 
						  		  "factura.enviarjusto.error",
						  		   null, null,
						  		   permitidoFacturarRespuesta.getRespuesta()); 
    		return;
		}
		BigDecimal importe = tarifaService.getImporteTotalFactura( this.listaConsumosFactura, this.facturaEdit.getEntidadPagadora() );
    	// Obtenemos la partida a partir del String de la factura
    	PartidaConverter pc = new PartidaConverter();
    	Partida partida = pc.getAsPartida(this.facturaEdit.getPartida());
    	Subtercero subtercero = null;
    	if (this.facturaEdit.getEntidadPagadora().getCif() != null && this.facturaEdit.getEntidadPagadora().getCodSubtercero() != null)
    	{
    	  	String nombreSubtercero = tarifaService.getNombreSubtercero(this.facturaEdit.getEntidadPagadora().getCif(), this.facturaEdit.getEntidadPagadora().getCodSubtercero());
    	   	subtercero = new Subtercero(this.facturaEdit.getEntidadPagadora().getCodSubtercero().toString(), nombreSubtercero);
    	}
    	EntidadRespuesta er = consumoService.enviarFacturaJusto(this.facturaEdit, importe, partida, subtercero);
    	if (((BigDecimal)er.getEntidad()).compareTo(new BigDecimal(0)) == 0)
    	{
    		// Obtenemos de nuevo la factura, ya que se ha actualizado con el número de Justo (y año) en el PL/SQL
    		consumoService.recargarFactura(this.facturaEdit);
			// Establecemos el importe de la tarifa aplicada a cada uno de los consumos
			tarifaService.establecerImporteTarifaFactura( this.listaConsumosFactura, this.facturaEdit.getEntidadPagadora() );

    		this.facesMessages.add(StatusMessage.Severity.INFO, 
						  		  "factura.enviarjusto.ok",
						  		   null, null,
						  		   new StringBuilder(this.facturaEdit.getAnoJusto()).append(" / ").append(this.facturaEdit.getNumeroJusto()).toString()); 
    	}
    	else
    	{
    		this.facesMessages.add(StatusMessage.Severity.ERROR, 
			  		  			"factura.enviarjusto.error.generacion",
			  		  			null, null,
			  		  			er.getRespuesta()); 
    	}
    }
 
    
    public void anularFacturaJusto()
    {
    	if (this.facturaEdit.getNumeroJusto() == null)
    	{
    		this.facesMessages.add(StatusMessage.Severity.WARN, 
			  		  			   "factura.anularjusto.error",
			  		  			   "factura.anularjusto.error.noenjusto",
			  		  			   null, null); 
    		return;
    	}
    	EntidadRespuesta er = consumoService.anularFacturaJusto(this.facturaEdit);
    	if (((BigDecimal)er.getEntidad()).compareTo(new BigDecimal(0)) == 0)
    	{
    		// Obtenemos de nuevo la factura, ya que se ha actualizado eliminando el número de Justo (y año) en el PL/SQL
    		consumoService.recargarFactura(this.facturaEdit);
			// Borramos el importe de la tarifa aplicada a cada uno de los consumos
			tarifaService.borrarImporteTarifaFactura(this.listaConsumosFactura);

    		this.facesMessages.add(StatusMessage.Severity.INFO, 
						  		  "factura.anularjusto.ok",
						  		  "factura.anularjusto.ok.detalles",
						  		   null, null); 
    	}
    	else
    	{
    		this.facesMessages.add(StatusMessage.Severity.ERROR, 
			  		  			"factura.anularjusto.error",
			  		  			null, null,
			  		  			er.getRespuesta()); 
    	}
    }
    
    public String getNombreTipoGasto()
    {
    	TipoGasto tg = tarifaService.getTipoGastoByCod(this.facturaEdit.getTipoGasto());
    	if (tg != null)
    	{
    		return tg.getDescri();
    	}
    	return null;
    }
    
    public String getDescripcionTipoConsumo(String tipo)
    {
    	return productoService.getDescripcionTipoProducto( tipo );
    }
    
    public boolean mostrarBotonCrearFactura()
    {
    	return (this.facturaEdit.getCod() == null && 
    			this.facturaEdit.getEntidadPagadora() != null && 
    	    	(this.facturaEdit.getTipoGasto() != null || this.facturaEdit.getSerie() != null) &&
    	    	(this.facturaEdit.getPartida() != null));
    }
    
    public String getTextoCabeceraPdf()
    {
    	StringBuilder texto = new StringBuilder("");
    	String separadorLinea = System.getProperty("line.separator");
    	if (this.facturaEdit.getFechaGeneracion() != null)
    	{
    		texto.append(srb.getString("factura.fechageneracion")).append(": ").append(UtilDate.convertirDateFechaToString(this.facturaEdit.getFechaGeneracion())).append(separadorLinea);
    	}
    	if (this.facturaEdit.getNumeroJusto() != null)
    	{
    		texto.append(srb.getString("factura.identificadorjusto")).append(": ").append(this.facturaEdit.getAnoJusto()).append("/").append(this.facturaEdit.getNumeroJusto()).append(separadorLinea);
    	}
    	if (this.facturaEdit.getServicio() != null)
    	{
    		texto.append(srb.getString("factura.servicio")).append(": ").append(this.facturaEdit.getServicio().getNombre()).append(separadorLinea);
    	}
    	if (this.facturaEdit.getInvestigador() != null)
    	{
    		texto.append(srb.getString("solicitudes.usuarioinvestigador")).append(": ").append(this.facturaEdit.getInvestigador().getDatosUsuario().getFullName()).append(separadorLinea);
    	}
    	if (this.facturaEdit.getEntidadPagadora() != null)
    	{
    		texto.append(srb.getString("factura.entidadpagadora")).append(": ").append(tarifaService.getDescripcionEntidadPagadora(this.facturaEdit.getEntidadPagadora())).append(separadorLinea);
    		if (this.facturaEdit.getSerie() != null)
    		{
    			texto.append(srb.getString("factura.serie")).append(": ").append(this.facturaEdit.getSerie()).append(separadorLinea);
    		}
    		if (this.facturaEdit.getTipoGasto() != null)
    		{
    			texto.append(srb.getString("factura.tipogasto")).append(": ").append(this.getNombreTipoGasto()).append(separadorLinea);
    		}
    		if (this.facturaEdit.getConcepto() != null)
    		{
    			texto.append(srb.getString("factura.concepto")).append(": ").append(this.facturaEdit.getConcepto()).append(separadorLinea);
    		}
    	}
    	return texto.append(separadorLinea).toString();
    }
    
    public String getDescripcionEntidadPagadora(EntidadPagadora ep)
    {
    	return tarifaService.getDescripcionEntidadPagadora( ep );
    }
    
    public boolean esIrFactura()
    {
    	return usuarioService.esIrFactura(this.facturaEdit);
    }
    
	public boolean esSupervisorFactura()
	{
		return usuarioService.esSupervisorFactura(this.facturaEdit);
	}
    
	public String getDescripcionEstadoFactura()
	{
		if (this.facturaEdit.getNumeroJusto() == null)
		{
			return "Pendiente de enviar a Justo";
		}
		else
		{
			return "Enviada a Justo";
		}
	}
	
	public String getColorEstadoFactura()
	{
		if (this.facturaEdit.getNumeroJusto() == null)
		{
			return "amarillo";
		}
		else
		{
			return "verde";
		}
	}
	
}
