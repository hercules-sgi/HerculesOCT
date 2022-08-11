package es.um.atica.sai.backbeans;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.jboss.seam.log.Log;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.data.PageEvent;

import es.um.atica.commons.utils.UtilDate;
import es.um.atica.sai.backbeans.lazydatamodel.ConsumoLazyDataModel;
import es.um.atica.sai.dtos.TipoPerfil;
import es.um.atica.sai.entities.Consumo;
import es.um.atica.sai.entities.EntidadPagadora;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.Reservas;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.ConsumoService;
import es.um.atica.sai.services.interfaces.ProductoService;
import es.um.atica.sai.services.interfaces.ReservableService;
import es.um.atica.sai.services.interfaces.ServicioService;
import es.um.atica.sai.services.interfaces.TarifaService;
import es.um.atica.sai.services.interfaces.UsuarioService;
import es.um.atica.sai.utils.Utilidades;

@Name("misSolicitudes")
@Scope(ScopeType.CONVERSATION)
public class MisSolicitudesBean {

	@Logger
	private Log log;

	@In(create = true, required = true)
	ServicioService servicioService;

	@In(create = true, required = true)
	ProductoService productoService;

	@In(create = true, required = true)
	UsuarioService usuarioService;

	@In(create = true, required = true)
	ConsumoService consumoService;
	
	@In(create = true, required = true)
	TarifaService tarifaService;
	
	@In(create = true, required = true)
	ReservableService reservableService;

	@In(value="org.jboss.seam.security.identity", required=true)
	SaiIdentity identity;

	@In(create=true)
	protected FacesMessages facesMessages;

	@RequestParameter
	private String esPresupuesto;
	private String presupuestoBuscar;
	private String tipoBuscar;
	private Long codigoBuscar;
	private Servicio servicioBuscar;
	private Producto productoBuscar;
	private String consumoInternoBuscar;
	private String estadoBuscar;
	private String estadoPresupuestoBuscar;
	private Usuario tecnicoAsignadoBuscar;
	private Usuario irAsignadoBuscar;
	private Usuario solicitanteBuscar;
	private Date fechaSolicitudDesdeBuscar;
	private Date fechaSolicitudHastaBuscar;
	private String fechaSolicitudDesdeBuscarString;
	private String fechaSolicitudHastaBuscarString;
	private Date fechaResolucionDesdeBuscar;
	private Date fechaResolucionHastaBuscar;
	private String fechaResolucionDesdeBuscarString;
	private String fechaResolucionHastaBuscarString;
	private Date fechaReservasDesdeBuscar;
	private Date fechaReservasHastaBuscar;
	private String fechaReservasDesdeBuscarString;
	private String fechaReservasHastaBuscarString;
	private boolean consultarTodasParaTecnico;

	private List<Servicio> listaServicios;
	private List<Producto> listaProductos;
	private List<Usuario> listaTecnicos;
	private List<Usuario> listaIrs;
	private List<Usuario> listaSolicitantes;

	private ConsumoLazyDataModel consumosLdm;
	private int first;
	private int pageSize;
	
	//Navegaciones
	private Long prestacion;

	public String getEsPresupuesto() {
		return esPresupuesto;
	}
	
	public void setEsPresupuesto( String esPresupuesto ) {
		this.esPresupuesto = esPresupuesto;
	}

	public String getPresupuestoBuscar() {
		return presupuestoBuscar;
	}
	
	public void setPresupuestoBuscar( String presupuestoBuscar ) {
		this.presupuestoBuscar = presupuestoBuscar;
	}

	public String getTipoBuscar() {
		return tipoBuscar;
	}

	public void setTipoBuscar( String tipoBuscar ) {
		this.tipoBuscar = tipoBuscar;
	}

	public Long getCodigoBuscar() {
		return codigoBuscar;
	}

	public void setCodigoBuscar( Long codigoBuscar ) {
		this.codigoBuscar = codigoBuscar;
	}

	public Servicio getServicioBuscar() {
		return servicioBuscar;
	}

	public void setServicioBuscar( Servicio servicioBuscar ) {
		this.servicioBuscar = servicioBuscar;
	}

	public Producto getProductoBuscar() {
		return productoBuscar;
	}

	public void setProductoBuscar( Producto productoBuscar ) {
		this.productoBuscar = productoBuscar;
	}

	public String getConsumoInternoBuscar() {
		return consumoInternoBuscar;
	}

	public void setConsumoInternoBuscar( String consumoInternoBuscar ) {
		this.consumoInternoBuscar = consumoInternoBuscar;
	}

	public String getEstadoBuscar() {
		return estadoBuscar;
	}

	public void setEstadoBuscar( String estadoBuscar ) {
		this.estadoBuscar = estadoBuscar;
	}

	public String getEstadoPresupuestoBuscar() {
		return estadoPresupuestoBuscar;
	}
	
	public void setEstadoPresupuestoBuscar( String estadoPresupuestoBuscar ) {
		this.estadoPresupuestoBuscar = estadoPresupuestoBuscar;
	}

	public Usuario getTecnicoAsignadoBuscar() {
		return tecnicoAsignadoBuscar;
	}

	public void setTecnicoAsignadoBuscar( Usuario tecnicoAsignadoBuscar ) {
		this.tecnicoAsignadoBuscar = tecnicoAsignadoBuscar;
	}

	public Usuario getIrAsignadoBuscar() {
		return irAsignadoBuscar;
	}

	public void setIrAsignadoBuscar( Usuario irAsignadoBuscar ) {
		this.irAsignadoBuscar = irAsignadoBuscar;
	}

	public Usuario getSolicitanteBuscar() {
		return solicitanteBuscar;
	}

	public void setSolicitanteBuscar( Usuario solicitanteBuscar ) {
		this.solicitanteBuscar = solicitanteBuscar;
	}

	public Date getFechaSolicitudDesdeBuscar() {
		return fechaSolicitudDesdeBuscar;
	}

	public void setFechaSolicitudDesdeBuscar( Date fechaSolicitudDesdeBuscar ) {
		this.fechaSolicitudDesdeBuscar = fechaSolicitudDesdeBuscar;
		fechaSolicitudDesdeBuscarString = UtilDate.convertirDateFechaToString(this.fechaSolicitudDesdeBuscar);
	}

	public Date getFechaSolicitudHastaBuscar() {
		return fechaSolicitudHastaBuscar;
	}

	public void setFechaSolicitudHastaBuscar( Date fechaSolicitudHastaBuscar ) {
		this.fechaSolicitudHastaBuscar = fechaSolicitudHastaBuscar;
		fechaSolicitudHastaBuscarString = UtilDate.convertirDateFechaToString(this.fechaSolicitudHastaBuscar);
	}

	public String getFechaSolicitudDesdeBuscarString() {
		return fechaSolicitudDesdeBuscarString;
	}

	public void setFechaSolicitudDesdeBuscarString( String fechaSolicitudDesdeBuscarString ) {
		this.fechaSolicitudDesdeBuscarString = fechaSolicitudDesdeBuscarString;
	}

	public String getFechaSolicitudHastaBuscarString() {
		return fechaSolicitudHastaBuscarString;
	}

	public void setFechaSolicitudHastaBuscarString( String fechaSolicitudHastaBuscarString ) {
		this.fechaSolicitudHastaBuscarString = fechaSolicitudHastaBuscarString;
	}

	public Date getFechaResolucionDesdeBuscar() {
		return fechaResolucionDesdeBuscar;
	}

	public void setFechaResolucionDesdeBuscar( Date fechaResolucionDesdeBuscar ) {
		this.fechaResolucionDesdeBuscar = fechaResolucionDesdeBuscar;
		fechaResolucionDesdeBuscarString = UtilDate.convertirDateFechaToString(this.fechaResolucionDesdeBuscar);
	}

	public Date getFechaResolucionHastaBuscar() {
		return fechaResolucionHastaBuscar;
	}

	public void setFechaResolucionHastaBuscar( Date fechaResolucionHastaBuscar ) {
		this.fechaResolucionHastaBuscar = fechaResolucionHastaBuscar;
		fechaResolucionHastaBuscarString = UtilDate.convertirDateFechaToString(this.fechaResolucionHastaBuscar);
	}

	public String getFechaResolucionDesdeBuscarString() {
		return fechaResolucionDesdeBuscarString;
	}

	public void setFechaResolucionDesdeBuscarString( String fechaResolucionDesdeBuscarString ) {
		this.fechaResolucionDesdeBuscarString = fechaResolucionDesdeBuscarString;
	}

	public String getFechaResolucionHastaBuscarString() {
		return fechaResolucionHastaBuscarString;
	}

	public void setFechaResolucionHastaBuscarString( String fechaResolucionHastaBuscarString ) {
		this.fechaResolucionHastaBuscarString = fechaResolucionHastaBuscarString;
	}

	public Date getFechaReservasDesdeBuscar() {
		return fechaReservasDesdeBuscar;
	}

	public void setFechaReservasDesdeBuscar( Date fechaReservasDesdeBuscar ) {
		this.fechaReservasDesdeBuscar = fechaReservasDesdeBuscar;
		fechaReservasDesdeBuscarString = UtilDate.convertirDateFechaToString(this.fechaReservasDesdeBuscar);
	}

	public Date getFechaReservasHastaBuscar() {
		return fechaReservasHastaBuscar;
	}

	public void setFechaReservasHastaBuscar( Date fechaReservasHastaBuscar ) {
		this.fechaReservasHastaBuscar = fechaReservasHastaBuscar;
		fechaReservasHastaBuscarString = UtilDate.convertirDateFechaToString(this.fechaReservasHastaBuscar);
	}

	public String getFechaReservasDesdeBuscarString() {
		return fechaReservasDesdeBuscarString;
	}

	public void setFechaReservasDesdeBuscarString( String fechaReservasDesdeBuscarString ) {
		this.fechaReservasDesdeBuscarString = fechaReservasDesdeBuscarString;
	}

	public String getFechaReservasHastaBuscarString() {
		return fechaReservasHastaBuscarString;
	}

	public void setFechaReservasHastaBuscarString( String fechaReservasHastaBuscarString ) {
		this.fechaReservasHastaBuscarString = fechaReservasHastaBuscarString;
	}
	
	public boolean isConsultarTodasParaTecnico() {
		return consultarTodasParaTecnico;
	}
	
	public void setConsultarTodasParaTecnico( boolean consultarTodasParaTecnico ) {
		this.consultarTodasParaTecnico = consultarTodasParaTecnico;
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

	public List<Usuario> getListaTecnicos() {
		return listaTecnicos;
	}

	public void setListaTecnicos( List<Usuario> listaTecnicos ) {
		this.listaTecnicos = listaTecnicos;
	}

	public List<Usuario> getListaIrs() {
		return listaIrs;
	}

	public void setListaIrs( List<Usuario> listaIrs ) {
		this.listaIrs = listaIrs;
	}

	public List<Usuario> getListaSolicitantes() {
		return listaSolicitantes;
	}

	public void setListaSolicitantes( List<Usuario> listaSolicitantes ) {
		this.listaSolicitantes = listaSolicitantes;
	}

	public ConsumoLazyDataModel getConsumosLdm() {
		return consumosLdm;
	}

	public void setConsumosLdm( ConsumoLazyDataModel consumosLdm ) {
		this.consumosLdm = consumosLdm;
	}

	public int getFirst() {
		return first;
	}
	
	public void setFirst( int first ) {
		this.first = first;
	}
	
	public int getPageSize() {
		return pageSize;
	}
	
	public void setPageSize( int pageSize ) {
		this.pageSize = pageSize;
	}

	public Long getPrestacion() {
		return prestacion;
	}

	public void setPrestacion( Long prestacion ) {
		this.prestacion = prestacion;
	}

	@Create
	public void inicializa()
	{
		this.listaServicios = identity.getServiciosUsuarioConectado();
		this.listaProductos = new ArrayList<>();
		this.listaTecnicos = new ArrayList<>();
		this.listaIrs = new ArrayList<>();
		if ("SI".equals(this.esPresupuesto))
		{
			this.presupuestoBuscar = "SI";
			this.tipoBuscar = "P";
		}
		else
		{
			this.presupuestoBuscar = "NO";
		}
		if (identity.tienePerfil(TipoPerfil.SUPERVISOR) || identity.tienePerfil(TipoPerfil.IR ))
		{
			this.listaSolicitantes = usuarioService.getListaUsuariosSolicitantes();
		}
		if (listaServicios.size() == 1)
		{
			this.servicioBuscar = listaServicios.get(0);
			this.seleccionadoServicio();
		}
		this.pageSize=10;
		buscar();
	}

	private void regenerarComboProductos()
	{
		if (this.tipoBuscar != null)
		{
			this.listaProductos = productoService.getProductosByServicioTipo(servicioBuscar, tipoBuscar, false, false);
		}
		else
		{
			this.listaProductos = productoService.getProductosByServicio(servicioBuscar);
		}
		if (this.productoBuscar != null && !this.listaProductos.contains(productoBuscar))
		{
			this.productoBuscar = null;
		}
	}

	// Para mantener la paginación cuando vuelves de editar un consumo
	public void onPageChange(PageEvent event) 
	{
		this.setFirst(((DataTable) event.getSource()).getFirst());
		this.setPageSize(((DataTable) event.getSource()).getRows());
	}
	
	public void seleccionadoTipo()
	{
		// Borramos los criterios que dependen del tipo
		this.estadoBuscar=null;
		this.tecnicoAsignadoBuscar=null;
		setFechaResolucionDesdeBuscar(null);
		setFechaResolucionHastaBuscar(null);
		setFechaReservasDesdeBuscar(null);
		setFechaReservasHastaBuscar(null);
		if (servicioBuscar != null)
		{
			this.regenerarComboProductos();
		}
	}

	public void seleccionadoServicio()
	{
		if (this.servicioBuscar == null)
		{
			this.listaProductos = new ArrayList<>();
			this.listaTecnicos = new ArrayList<>();
			this.listaIrs = new ArrayList<>();
			this.productoBuscar = null;
			this.tecnicoAsignadoBuscar = null;
			this.irAsignadoBuscar = null;
		}
		else
		{
			this.regenerarComboProductos();
			this.listaTecnicos = usuarioService.getTecnicosByServicio(servicioBuscar);
			this.listaIrs = usuarioService.getIrsByServicio(servicioBuscar);
		}
		this.consultarTodasParaTecnico = false;
	}

	public void buscar()
	{
		final DataTable tablaListaConsumos = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formListado:dataTableConsumos");
		if (tablaListaConsumos != null)
		{
			tablaListaConsumos.setFirst(0);
		}
		this.consumosLdm = new ConsumoLazyDataModel(this.presupuestoBuscar, this.consultarTodasParaTecnico);
	}

	public void limpiarBusqueda()
	{
		this.codigoBuscar=null;
		if ("NO".equals(this.presupuestoBuscar))
		{
			this.tipoBuscar=null;
		}
		if (listaServicios.size() > 1)
		{
			this.servicioBuscar=null;
		}
		this.productoBuscar=null;
		this.estadoBuscar=null;
		this.estadoPresupuestoBuscar=null;
		this.solicitanteBuscar=null;
		this.tecnicoAsignadoBuscar=null;
		this.irAsignadoBuscar=null;
		setFechaSolicitudDesdeBuscar(null);
		setFechaSolicitudHastaBuscar(null);
		setFechaResolucionDesdeBuscar(null);
		setFechaResolucionHastaBuscar(null);
		setFechaReservasDesdeBuscar(null);
		setFechaReservasHastaBuscar(null);
		this.consultarTodasParaTecnico = false;
		this.buscar();
	}

	public List<Reservas> getReservasActivasByConsumo(Consumo consumo)
	{
		return reservableService.getReservasActivasPorConsumo( consumo );
	}
	
	public String getDescripcionFechaReserva(Reservas res)
	{
		return new StringBuilder(UtilDate.dateToString(res.getFechaInicio(), "dd/MM/yyyy")).append(" de ")
					.append(UtilDate.convertirDateHoraToString(res.getFechaInicio(), true)).append(" a ")
					.append(UtilDate.convertirDateHoraToString(res.getFechaFin(), true)).toString();
	}
	
	
	public String colorEstado(Consumo c) 
	{
		return consumoService.getColorEstadoConsumo(c);
	}
	
	public String colorEstadoPresupuesto(Consumo c) 
	{
		return consumoService.getColorEstadoPresupuesto(c);
	}
	
	public String formatCantidad( BigDecimal number ) 
	{
		return Utilidades.formatCantidad(number);	
	}

	public void obtenerConsumoHtml(Long codConsumo) throws IOException
	{
		final byte[] consumoHtml = consumoService.getConsumoHtml(codConsumo);
		try
		{
			log.info("Obtenido el consumoo en html, llamamos a mostrar fichero");
			final String nombreFichero = new StringBuilder("Consumo_").append(codConsumo.toString()).append(".html").toString();
			Utilidades.mostrarFichero(consumoHtml, nombreFichero, "HTML");
		}
		catch(final Exception ex)
		{
			log.error("Error obteniendo calendario en html: #0",ex.toString());
			facesMessages.add(StatusMessage.Severity.ERROR, "generico.error.inesperado", "solicitudes.consumo.gethtml.error", null, null);
		}
	}

	public String getNombreFormateado(String cadena) 
	{
		try 
		{
			final String[] palabras = cadena.split(" ");
			StringBuilder resultado = new StringBuilder("");
			for(final String s: palabras) 
			{
				resultado.append(" ").append(s.substring(0, 1).toUpperCase()).append(s.substring(1).toLowerCase());
			}
			return resultado.toString();
		}
		catch(final Exception ex) 
		{
			return cadena;
		}
	}

	public String getDescripcionTipoConsumo(Consumo consumo)
	{
		if ("SI".equals(consumo.getPresupuesto()))
		{
			return "Presupuesto";
		}
		return consumoService.getDescripcionTipoConsumo(consumo.getTipo());
	}
	
	public String getDescripcionTipoConsumo(String tipo)
	{
		return consumoService.getDescripcionTipoConsumo( tipo );
	}
	
    public String getDescripcionEntidad(EntidadPagadora ep)
    {
    	return tarifaService.getDescripcionEntidadPagadora( ep );
    }
    
    public String getDescripcionEstadoConsumo(Consumo consumo)
    {
    	return consumoService.getDescripcionEstadoConsumo(consumo);
    }

    public boolean esIrFactura(Consumo consumo)
    {
    	return consumo.getFactura() != null && usuarioService.esIrFactura(consumo.getFactura());
    }
    
	public boolean esSupervisorFactura(Consumo consumo)
	{
		return consumo.getFactura() != null && usuarioService.esSupervisorFactura(consumo.getFactura());
	}
    
}
