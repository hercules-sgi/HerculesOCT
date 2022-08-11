package es.um.atica.sai.backbeans;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.SeamResourceBundle;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.jboss.seam.log.Log;
import org.primefaces.event.RowEditEvent;

import es.um.atica.sai.dtos.EstadoConsumo;
import es.um.atica.sai.dtos.EstadoPresupuesto;
import es.um.atica.sai.dtos.LineaFungible;
import es.um.atica.sai.dtos.OperacionConsumo;
import es.um.atica.sai.dtos.TipoPerfil;
import es.um.atica.sai.entities.Consumo;
import es.um.atica.sai.entities.EntidadPagadora;
import es.um.atica.sai.entities.GrupoInvestigacion;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.Proyecto;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.TipoCertificacion;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.ConsumoService;
import es.um.atica.sai.services.interfaces.MensajeService;
import es.um.atica.sai.services.interfaces.ProductoService;
import es.um.atica.sai.services.interfaces.ProyectoService;
import es.um.atica.sai.services.interfaces.ServicioService;
import es.um.atica.sai.services.interfaces.TarifaService;
import es.um.atica.sai.services.interfaces.UsuarioService;
import es.um.atica.sai.utils.Utilidades;
import es.um.atica.seam.components.MenuManagerBean;

@Name("solicitudFungibles")
@Scope(ScopeType.CONVERSATION)
public class SolicitudFungiblesBean {

	@Logger
	private Log log;

	@In(create = true, required = true)
	UsuarioService usuarioService;

	@In(create = true, required = true)
	TarifaService tarifaService;

	@In(create = true, required = true)
	ConsumoService consumoService;

	@In(create = true, required = true)
	ServicioService servicioService;

	@In(create = true, required = true)
	ProductoService productoService;
	
	@In(create = true, required = true)
	ProyectoService proyectoService;

	@In(create = true, required = true)
	MensajeService mensajeService;

	@In(create = true)
	SolicitudPrestacionesBean solicitudPrestaciones;
	
	@In(create=true)
	protected FacesMessages facesMessages;

	@In(value="org.jboss.seam.security.identity", required=true)
	SaiIdentity identity;

	@In(create=true)
	MenuManagerBean menuManagerBean;
	
	ResourceBundle srb = SeamResourceBundle.getBundle();
	
	private static final String SI = "SI";
	private static final String NO = "NO";
	private static final String KEY_MSJ_MODIFICACION = ".modificacion";
	private static final String KEY_MSJ_CREACION = ".creacion";
	private static final String KEY_MSJ_FUNGIBLES_CREAR_ERROR = "fungibles.crear.error";
	private static final String KEY_MSJ_FUNGIBLES_MODIFICAR_ERROR = "fungibles.modificar.error";
	private static final String KEY_MSJ_FUNGIBLES_ADDLINEA_ERROR = "fungible.add.linea.error";
	private static final String KEY_MSJ_CONSUMOS_LINEAS = "consumos.lineas";
	private static final String LOG_FUNGIBLES_GUARDAR_ERROR = "Error al guardar pedido de fungible: ";
	private static final String VOLVER_LISTADO = "misSolicitudes";
	private static final String EDIT_FUNGIBLE = "editFungible";
	

	private Consumo fungibleEdit;
	private List<Servicio> listaServicios;
	private Servicio servicioFungible;
	private String enviaEmail;
	private List<Usuario> listaUsuariosSolicitantes;
	private List<Usuario> listaUsuariosIr;
	private List<EntidadPagadora> listaEntidades;
	private List<GrupoInvestigacion> listaGruposInvestigacion;
	private List<Producto> listaProductos;
	private List<Proyecto> listaProyectos;
	private Producto productoLineaFungibleAdd;
	private Proyecto proyectoLineaFungibleAdd;
	private BigDecimal cantidadLineaFungibleAdd;
	private List<LineaFungible> lineas;
	private List<LineaFungible> lineasEliminadas;
	private boolean accesoDesdeListado;

	public List<Servicio> getListaServicios() 
	{
		return listaServicios;
	}

	public void setListaServicios( List<Servicio> listaServicios ) 
	{
		this.listaServicios = listaServicios;
	}
	
	public List<LineaFungible> getLineas() {
		return lineas;
	}
	
	public void setLineas( List<LineaFungible> lineas ) {
		this.lineas = lineas;
	}

	public List<LineaFungible> getLineasEliminadas() {
		return lineasEliminadas;
	}
	
	public void setLineasEliminadas( List<LineaFungible> lineasEliminadas ) {
		this.lineasEliminadas = lineasEliminadas;
	}

	public Servicio getServicioFungible() {
		return servicioFungible;
	}

	public void setServicioFungible( Servicio servicioFungible ) {
		this.servicioFungible = servicioFungible;
	}

	public List<Usuario> getListaUsuariosIr() 
	{
		return listaUsuariosIr;
	}

	public void setListaUsuariosIr(List<Usuario> listaUsuariosIr) 
	{
		this.listaUsuariosIr = listaUsuariosIr;
	}

	public List<EntidadPagadora> getListaEntidades() 
	{
		return listaEntidades;
	}

	public void setListaEntidades( List<EntidadPagadora> listaEntidades ) 
	{
		this.listaEntidades = listaEntidades;
	}
	
	public List<GrupoInvestigacion> getListaGruposInvestigacion() {
		return listaGruposInvestigacion;
	}
	
	public void setListaGruposInvestigacion( List<GrupoInvestigacion> listaGruposInvestigacion ) {
		this.listaGruposInvestigacion = listaGruposInvestigacion;
	}

	public List<Usuario> getListaUsuariosSolicitantes() 
	{
		return listaUsuariosSolicitantes;
	}

	public void setListaUsuariosSolicitantes(List<Usuario> listaUsuariosSolicitantes) 
	{
		this.listaUsuariosSolicitantes = listaUsuariosSolicitantes;
	}

	public String getEnviaEmail() {
		return enviaEmail;
	}

	public void setEnviaEmail( String enviaEmail ) {
		this.enviaEmail = enviaEmail;
	}

	public List<Producto> getListaProductos() {
		return listaProductos;
	}

	public void setListaProductos( List<Producto> listaProductos ) {
		this.listaProductos = listaProductos;
	}

	public List<Proyecto> getListaProyectos() {
		return listaProyectos;
	}
	
	public void setListaProyectos( List<Proyecto> listaProyectos ) {
		this.listaProyectos = listaProyectos;
	}

	public BigDecimal getCantidadLineaFungibleAdd() {
		return cantidadLineaFungibleAdd;
	}

	public void setCantidadLineaFungibleAdd(BigDecimal cantidadLineaFungibleAdd ) {
		this.cantidadLineaFungibleAdd = cantidadLineaFungibleAdd;
	}

	public Producto getProductoLineaFungibleAdd() {
		return productoLineaFungibleAdd;
	}

	public void setProductoLineaFungibleAdd( Producto productoLineaFungibleAdd ) {
		this.productoLineaFungibleAdd = productoLineaFungibleAdd;
	}

	public Proyecto getProyectoLineaFungibleAdd() {
		return proyectoLineaFungibleAdd;
	}
	
	public void setProyectoLineaFungibleAdd( Proyecto proyectoLineaFungibleAdd ) {
		this.proyectoLineaFungibleAdd = proyectoLineaFungibleAdd;
	}

	public Consumo getFungibleEdit() {
		return fungibleEdit;
	}

	public void setFungibleEdit( Consumo fungibleEdit ) {
		this.fungibleEdit = fungibleEdit;
	}

	public boolean isAccesoDesdeListado() {
		return accesoDesdeListado;
	}
	
	public void setAccesoDesdeListado( boolean accesoDesdeListado ) {
		this.accesoDesdeListado = accesoDesdeListado;
	}

	@Create
	public void inicializar()
	{
		this.listaServicios = servicioService.getListaServiciosPuedoHacerSolicitud();
		this.servicioFungible = null;
		this.fungibleEdit = new Consumo();
		this.fungibleEdit.setTipo("F");
		this.fungibleEdit.setPresupuesto(NO);
		this.cantidadLineaFungibleAdd = BigDecimal.valueOf(1.0);
		this.enviaEmail = NO;
		this.lineas = new ArrayList<>();
		this.lineasEliminadas = new ArrayList<>();
		this.accesoDesdeListado = false;
		if (this.listaServicios.size()==1)
		{
			this.servicioFungible = this.listaServicios.get(0);
			this.seleccionadoServicio();
		}
	}

	private void inicializacionFungibleEdit(Consumo consumo)
	{
		this.fungibleEdit = consumo;
		this.servicioFungible = this.fungibleEdit.getProducto().getServicio();
		boolean soloObtenerProductosNoRequierenProyecto = "SI".equals(this.fungibleEdit.getInterno());
		this.listaProductos = productoService.getProductosByServicioTipo(this.servicioFungible, "F", soloObtenerProductosNoRequierenProyecto, false);
		this.lineas = this.listaConsumoToListaLineaFungible(consumoService.getFungiblesRelacionados(fungibleEdit.getFechaSolicitud(), fungibleEdit.getUsuarioSolicitante()));
		this.lineasEliminadas = new ArrayList<>();
	}
	
	public String establecerFungibleEdit(Consumo consumo)
	{
		this.inicializacionFungibleEdit(consumo);
		this.accesoDesdeListado = true;
		return EDIT_FUNGIBLE;
	}

	public String establecerFungibleEditFromConsumoPadre(Consumo consumo)
	{
		consumoService.recargarConsumo(consumo);
		this.inicializacionFungibleEdit(consumo);
		this.accesoDesdeListado = false;
		return EDIT_FUNGIBLE;
	}
	
	public String establecerFungibleCreateFromConsumoPadre(Consumo consumoPadre)
	{
		this.inicializar();
		this.servicioFungible = consumoPadre.getProducto().getServicio();
		this.fungibleEdit.setConsumoPadre(consumoPadre);
		this.fungibleEdit.setPresupuesto(consumoPadre.getPresupuesto());
		this.seleccionadoServicio();
		return EDIT_FUNGIBLE;
	}
	
	private List<LineaFungible> listaConsumoToListaLineaFungible(List<Consumo> listaConsumos)
	{
		if (listaConsumos == null || listaConsumos.isEmpty())
		{
			return new ArrayList<>();
		}
		List<LineaFungible> listaLineaFungible = new ArrayList<>();
		Iterator<Consumo> itConsumo = listaConsumos.iterator();
		while (itConsumo.hasNext())
		{
			Consumo con = itConsumo.next();
			listaLineaFungible.add(new LineaFungible(con.getCod(),con.getProducto(), con.getProyecto(),con.getCantidad(),con.getEstado(),con.getFechaSolicitud()));
		}
		return listaLineaFungible;
	}

	public void seleccionadoServicio() 
	{
		if (this.servicioFungible != null)
		{
			if (identity.tienePerfilEnServicio(TipoPerfil.TECNICO, this.servicioFungible.getCod()) || identity.tienePerfilEnServicio( TipoPerfil.SUPERVISOR, this.servicioFungible.getCod() ))
			{
				this.listaUsuariosSolicitantes=usuarioService.getUsuariosPuedoSolicitarEnSuNombreByServicio(this.servicioFungible);
				this.fungibleEdit.setInterno(SI);
			}
			else
			{
				this.fungibleEdit.setInterno(NO);
				this.listaUsuariosIr = usuarioService.getListaIrsPuedoAsignarEnSolicitud(identity.getUsuarioConectado(), this.servicioFungible);
				if (this.listaUsuariosIr.size() == 1)
				{
					this.fungibleEdit.setUsuarioIrAsociado(this.listaUsuariosIr.get(0));
				}
			}
			boolean soloObtenerProductosNoRequierenProyecto = "SI".equals(this.fungibleEdit.getInterno());
			this.listaProductos = productoService.getProductosByServicioTipo(this.servicioFungible, "F", soloObtenerProductosNoRequierenProyecto, false);
			//Si el servicio no tiene fungibles, notificamos al usuario
			if (this.listaProductos == null || this.listaProductos.isEmpty()) 
			{
				facesMessages.add(StatusMessage.Severity.WARN, "fungibles.servicio.seleccion.error", "fungibles.servicio.seleccion.error.sinproductos", null, null, this.servicioFungible.getNombre());
			}
		}
		else
		{
			this.fungibleEdit.setInterno(SI);
		}
		this.fungibleEdit.setUsuarioSolicitante(identity.getUsuarioConectado());
		if (this.listaUsuariosIr==null || (this.fungibleEdit.getUsuarioIrAsociado()!=null && !this.listaUsuariosIr.contains(this.fungibleEdit.getUsuarioIrAsociado())))
		{
			this.fungibleEdit.setUsuarioIrAsociado(null);
			this.seleccionadoUsuarioIr();
		}
		this.fungibleEdit.setEntidadPagadora(null);
		if (this.fungibleEdit.getUsuarioIrAsociado()!=null)
		{
			this.seleccionadoUsuarioIr();
		}
	}
	
	public void seleccionadoConsumoInterno() 
	{
		if (NO.equals(this.fungibleEdit.getInterno())) 
		{
			if (this.servicioFungible != null) 
			{
				this.listaUsuariosSolicitantes = usuarioService.getUsuariosByServicio(this.servicioFungible);
				if (this.fungibleEdit.getUsuarioSolicitante() != null && this.listaUsuariosSolicitantes.contains(this.fungibleEdit.getUsuarioSolicitante()))
				{
					this.seleccionadoUsuarioSolicitante();
				}
			}
			this.listaProductos = productoService.getProductosByServicioTipo(this.servicioFungible, "F", false, false);
		}
		else 
		{
			this.fungibleEdit.setUsuarioSolicitante(identity.getUsuarioConectado());
			this.fungibleEdit.setUsuarioIrAsociado(null);
			this.fungibleEdit.setEntidadPagadora(null);
			this.listaProductos = productoService.getProductosByServicioTipo(this.servicioFungible, "F", true, false);
		}
	}

	public void seleccionadoUsuarioSolicitante() 
	{
		if (this.fungibleEdit.getUsuarioSolicitante()!= null) 
		{
			this.listaUsuariosIr = usuarioService.getListaIrsPuedoAsignarEnSolicitud(this.fungibleEdit.getUsuarioSolicitante(),this.servicioFungible);
			if (this.listaUsuariosIr.size() == 1)
			{
				this.fungibleEdit.setUsuarioIrAsociado(this.listaUsuariosIr.get(0));
			}
			if (this.productoLineaFungibleAdd != null)
			{
				// Si ya había producto seleccionado y cambiamos el solicitante tenemos que comprobar proyectos
				this.seleccionadoProducto();
			}
		}
		if (this.listaUsuariosIr==null || (this.fungibleEdit.getUsuarioIrAsociado()!=null && !this.listaUsuariosIr.contains(this.fungibleEdit.getUsuarioIrAsociado())))
		{
			this.fungibleEdit.setUsuarioIrAsociado(null);
			this.seleccionadoUsuarioIr();
		}
		this.fungibleEdit.setEntidadPagadora(null);
		if (this.fungibleEdit.getUsuarioIrAsociado()!=null)
		{
			this.seleccionadoUsuarioIr();
		}
	}
	
	public void seleccionadoUsuarioIr() 
	{
		if (this.fungibleEdit.getUsuarioIrAsociado() != null) 
		{
			this.listaEntidades = tarifaService.getEntidadesByIr(this.fungibleEdit.getUsuarioIrAsociado());
			if (this.listaEntidades.size()==1)
			{
				this.fungibleEdit.setEntidadPagadora(this.listaEntidades.get(0));
				this.seleccionadaEntidadPagadora();
			}
			if (this.productoLineaFungibleAdd != null)
			{
				this.seleccionadoProducto();
			}
		}
		if (this.listaEntidades==null || (this.fungibleEdit.getEntidadPagadora() != null && !this.listaEntidades.contains(this.fungibleEdit.getEntidadPagadora())))
		{
			this.fungibleEdit.setEntidadPagadora(null);
			this.seleccionadaEntidadPagadora();
		}
	}

	public void seleccionadaEntidadPagadora()
	{
		if (this.fungibleEdit.getEntidadPagadora() != null)
		{
			this.listaGruposInvestigacion = usuarioService.getGruposInvestigacionByUsuarioEntidadPagadora(this.fungibleEdit.getUsuarioIrAsociado(), this.fungibleEdit.getEntidadPagadora());
			if (this.listaGruposInvestigacion.size()==1)
			{
				this.fungibleEdit.setGrupoInvestigacion(this.listaGruposInvestigacion.get(0));
			}
		}
		else
		{
			this.listaGruposInvestigacion = new ArrayList<>();
			this.fungibleEdit.setGrupoInvestigacion(null);
		}
	}
	
	public void seleccionadoProducto()
	{
		if (this.productoLineaFungibleAdd != null && SI.equals(this.productoLineaFungibleAdd.getRequiereProyecto()))
		{
			if (this.fungibleEdit.getUsuarioIrAsociado() != null)
			{
				this.listaProyectos = proyectoService.getListaProyectosByProductoIr(this.productoLineaFungibleAdd, this.fungibleEdit.getUsuarioIrAsociado());
				if (this.listaProyectos.isEmpty())
				{
					facesMessages.add(StatusMessage.Severity.WARN, "fungibles.producto.seleccion.error", "fungibles.producto.seleccion.error.sinproyecto", null, null, this.productoLineaFungibleAdd.getDescripcion(), this.fungibleEdit.getUsuarioIrAsociado().getDatosUsuario().getNombreCompleto());
					this.productoLineaFungibleAdd=null;
				}
				else if (this.listaProyectos.size() == 1)
				{
					this.proyectoLineaFungibleAdd = this.listaProyectos.get(0);
				}
			}
			else
			{
				facesMessages.add(StatusMessage.Severity.WARN, "fungibles.producto.seleccion.error", "fungibles.producto.seleccion.error.sinproyecto.sinir", null, null, this.productoLineaFungibleAdd.getDescripcion());
				this.productoLineaFungibleAdd=null;
			}
		}
		else
		{
			this.proyectoLineaFungibleAdd = null;
		}
	}
	
	public void addLinea() 
	{
		if (this.productoLineaFungibleAdd == null) 
		{
			return;
		}
		// Comprobamos si requiere alguna certificación de la que no dispone
		Usuario solicitante = consumoService.obtenerSolicitante(this.fungibleEdit);
		TipoCertificacion tc = consumoService.obtenerCertificacionNecesaria(solicitante, this.productoLineaFungibleAdd);
		if (tc != null)
		{
			facesMessages.add(StatusMessage.Severity.WARN, KEY_MSJ_FUNGIBLES_ADDLINEA_ERROR, "fungible.add.linea.error.certificacion", null, null, tc.getNombre(), solicitante.getDatosUsuario().getNombreCompleto());
			return;
		}
		// Controlar la cantidad que se pide de producto:
		if (SI.equals(this.productoLineaFungibleAdd.getControlStock())) 
		{
			BigDecimal stockDisponibleProducto = null;
			if (this.fungibleEdit.getCod() == null)
			{
				stockDisponibleProducto = productoService.getStockDisponibleByProducto(this.productoLineaFungibleAdd)
										  .subtract(this.getStockConsumidoLineasFungible(this.productoLineaFungibleAdd, BigDecimal.valueOf(0)));
			}
			else
			{
				stockDisponibleProducto = productoService.getStockDisponibleByProductoEdicionFungible(this.productoLineaFungibleAdd, this.fungibleEdit)
										  .subtract(this.getStockConsumidoLineasFungible(this.productoLineaFungibleAdd, BigDecimal.valueOf(0)));
			}
			if (this.cantidadLineaFungibleAdd.compareTo(stockDisponibleProducto) > 0)
			{
				facesMessages.add(StatusMessage.Severity.ERROR, KEY_MSJ_FUNGIBLES_ADDLINEA_ERROR, null, null, MessageFormat.format(srb.getString("fungible.add.linea.error.stock.insuficiente"), this.formatCantidad(stockDisponibleProducto)));
				return;
			}
		}
		// Controlar la cantidad que se pide con respecto al proyecto
		if (SI.equals(this.productoLineaFungibleAdd.getRequiereProyecto()))
		{
			// La cantidad disponible la obtenemos de los consumos en BBDD, y de la lista de líneas de este pedido todavía sin persistir
			BigDecimal cantidadDisponibleProyectoProducto = null;
			if (this.fungibleEdit.getCod() == null)
			{
				cantidadDisponibleProyectoProducto = proyectoService.getCantidadDisponibleByProyectoProducto(this.proyectoLineaFungibleAdd, this.productoLineaFungibleAdd)
													.subtract(this.getCantidadConsumidaProyectoLineasFungible(this.proyectoLineaFungibleAdd, this.productoLineaFungibleAdd, BigDecimal.valueOf(0)));
			}
			else
			{
				cantidadDisponibleProyectoProducto = proyectoService.getCantidadDisponibleByProyectoProductoEdicionFungible(this.proyectoLineaFungibleAdd, this.productoLineaFungibleAdd, this.fungibleEdit)
													.subtract(this.getCantidadConsumidaProyectoLineasFungible(this.proyectoLineaFungibleAdd, this.productoLineaFungibleAdd, BigDecimal.valueOf(0)));
			}
			if (this.cantidadLineaFungibleAdd.compareTo(cantidadDisponibleProyectoProducto) > 0)
			{
				facesMessages.add(StatusMessage.Severity.ERROR, KEY_MSJ_FUNGIBLES_ADDLINEA_ERROR, "fungible.add.linea.error.stock.proyecto", null, null, this.productoLineaFungibleAdd.getDescripcion(), this.proyectoLineaFungibleAdd.getNombre(), this.formatCantidad(cantidadDisponibleProyectoProducto));
				return;
			}
		}
		final LineaFungible lineaFungibleAdd = new LineaFungible();

		lineaFungibleAdd.setCantidad(this.cantidadLineaFungibleAdd);
		lineaFungibleAdd.setProducto(this.productoLineaFungibleAdd);
		lineaFungibleAdd.setProyecto(this.proyectoLineaFungibleAdd);
		if (!this.lineas.isEmpty()) 
		{
			lineaFungibleAdd.setFechaSolicitud( getLineas().get( 0 ).getFechaSolicitud() );
		}
		else 
		{
			lineaFungibleAdd.setFechaSolicitud(  new Date() );
		}
		lineaFungibleAdd.setEstado(EstadoConsumo.PENDIENTE);
		this.lineas.add(lineaFungibleAdd);
		
		StringBuilder mensaje = new StringBuilder("fungible.add.linea.ok");
		if (this.fungibleEdit.getCod() != null)
		{
			mensaje.append(KEY_MSJ_MODIFICACION);
		}
		else
		{
			mensaje.append(KEY_MSJ_CREACION);
		}
		this.productoLineaFungibleAdd = null;
		this.proyectoLineaFungibleAdd = null;
		this.cantidadLineaFungibleAdd = BigDecimal.valueOf(1.00);
		facesMessages.add(StatusMessage.Severity.WARN, this.getKeyMsj(mensaje.toString()), null, null, getDescripcionLineaFungible(lineaFungibleAdd));
	}

	// Sólo se pueden editar líneas de consumos persistidos
	public void editarLinea(RowEditEvent event) 
    {
        LineaFungible lf = (LineaFungible)event.getObject();
        // Obtenemos la cantidad original antes de modificar
        Consumo fungibleOriginal = consumoService.getConsumoById(lf.getCod());
        if (fungibleOriginal.getCantidad().compareTo(lf.getCantidad())<0)
        {
        	BigDecimal aumentoCantidad = lf.getCantidad().subtract(fungibleOriginal.getCantidad());
        	//Se ha aumentado la cantidad. Comprobamos stock de producto y de producto en proyecto
			//Controlar la cantidad que se pide de producto:
			if (SI.equals(lf.getProducto().getControlStock())) 
			{
				BigDecimal stockDisponibleProducto = productoService.getStockDisponibleByProductoEdicionFungible(lf.getProducto(), fungibleOriginal)
													 .subtract(this.getStockConsumidoLineasFungible(lf.getProducto(), aumentoCantidad));
				if (aumentoCantidad.compareTo(stockDisponibleProducto) > 0)
				{
					facesMessages.add(StatusMessage.Severity.ERROR, "fungible.editar.linea.error", null, null, MessageFormat.format(srb.getString("fungible.add.linea.error.stock.insuficiente"), this.formatCantidad(stockDisponibleProducto)));
					// Restablecemos la cantidad original antes de la modificación
					lf.setCantidad(fungibleOriginal.getCantidad());
					return;
				}
			}
			// Controlar la cantidad que se pide con respecto al proyecto
			if (SI.equals(lf.getProducto().getRequiereProyecto()) && lf.getProyecto() != null)
			{
				// La cantidad disponible la obtenemos de los consumos en BBDD, y de la lista de líneas de este pedido todavía sin persistir, tanto pendientes de añadir como de eliminar
				BigDecimal cantidadDisponibleProyectoProducto = proyectoService.getCantidadDisponibleByProyectoProductoEdicionFungible(lf.getProyecto(), lf.getProducto(), fungibleOriginal)
																.subtract(this.getCantidadConsumidaProyectoLineasFungible(lf.getProyecto(), lf.getProducto(), aumentoCantidad));
				if (aumentoCantidad.compareTo(cantidadDisponibleProyectoProducto) > 0)
				{
					facesMessages.add(StatusMessage.Severity.ERROR, "fungible.editar.linea.error", "fungible.add.linea.error.stock.proyecto.aumento", null, null, lf.getProducto().getDescripcion(), lf.getProyecto().getNombre(), this.formatCantidad(cantidadDisponibleProyectoProducto), this.formatCantidad(aumentoCantidad));
					// Restablecemos la cantidad original antes de la modificación
					lf.setCantidad(fungibleOriginal.getCantidad());
					return;
				}
			}
        }
		StringBuilder mensaje = new StringBuilder("fungible.editar.linea.ok");
		if (this.fungibleEdit.getCod() != null)
		{
			mensaje.append(KEY_MSJ_MODIFICACION);
		}
		else
		{
			mensaje.append(KEY_MSJ_CREACION);
		}
		facesMessages.add(StatusMessage.Severity.WARN, getKeyMsj(mensaje.toString()), null, null, getDescripcionLineaFungible(lf));
    }
	
	private BigDecimal getStockConsumidoLineasFungible(Producto producto, BigDecimal aumentoCantidad)
	{
		BigDecimal cantidadTotal = BigDecimal.valueOf(0);
		for (LineaFungible linea: this.lineas)
		{
			if (producto.equals(linea.getProducto()) && !EstadoConsumo.ANULADO.equals(linea.getEstado()) && !EstadoConsumo.RECHAZADO.equals(linea.getEstado()))
			{
				cantidadTotal = cantidadTotal.add(linea.getCantidad());
			}
		}
		return cantidadTotal.subtract(aumentoCantidad);
	}
	
	private BigDecimal getCantidadConsumidaProyectoLineasFungible(Proyecto proyecto, Producto producto, BigDecimal aumentoCantidad)
	{
		BigDecimal cantidadTotal = BigDecimal.valueOf(0);
		for (LineaFungible linea: this.lineas)
		{
			if (proyecto.equals(linea.getProyecto()) && producto.equals(linea.getProducto()) && !EstadoConsumo.ANULADO.equals(linea.getEstado()) && !EstadoConsumo.RECHAZADO.equals(linea.getEstado()))
			{
				cantidadTotal = cantidadTotal.add(linea.getCantidad());
			}
		}
		return cantidadTotal.subtract(aumentoCantidad);
	}
	
	public void recogerLinea(LineaFungible lf) 
	{
		lf.setEstado(EstadoConsumo.FINALIZADO);
		StringBuilder mensaje = new StringBuilder("fungible.entregar.linea.ok.modificacion");
		facesMessages.add(StatusMessage.Severity.WARN, mensaje.toString(), null, null, getDescripcionLineaFungible(lf));
	}
	
	public void eliminarLinea(LineaFungible lf) 
	{
		if (this.fungibleEdit.getCod() != null && this.lineas.size() == 1)
		{
			if (identity.tienePerfilEnServicio(TipoPerfil.SUPERVISOR, this.servicioFungible.getCod()) || identity.tienePerfilEnServicio(TipoPerfil.TECNICO, this.servicioFungible.getCod()))
			{
				facesMessages.add(StatusMessage.Severity.WARN, "fungible.eliminar.linea.error", "fungible.eliminar.linea.error.solouna.supervisor", null, null);
			}
			else
			{
				facesMessages.add(StatusMessage.Severity.WARN, "fungible.eliminar.linea.error", "fungible.eliminar.linea.error.solouna", null, null);
			}
			return;
		}
		if (lf.getCod() != null)
		{
			this.lineasEliminadas.add(lf);
		}
		this.lineas.remove(lf);
		StringBuilder mensaje = new StringBuilder("fungible.eliminar.linea.ok");
		if (this.fungibleEdit.getCod() != null)
		{
			mensaje.append(KEY_MSJ_MODIFICACION);
		}
		else
		{
			mensaje.append(KEY_MSJ_CREACION);
		}
		facesMessages.add(StatusMessage.Severity.WARN, getKeyMsj(mensaje.toString()), null, null, getDescripcionLineaFungible(lf));
	}

	private String getDescripcionLineaFungible(LineaFungible lf)
	{
		return new StringBuilder(lf.getCantidad().toString()).append(" x ").append(lf.getProducto().getDescripcion()).toString();
	}
	
	public boolean puedeRecoger(LineaFungible lf)
	{
		return (this.fungibleEdit.getFactura() == null && EstadoConsumo.PENDIENTE.equals(lf.getEstado()) && (identity.tienePerfilEnServicio(TipoPerfil.TECNICO, this.servicioFungible.getCod()) || identity.tienePerfilEnServicio(TipoPerfil.SUPERVISOR, this.servicioFungible.getCod()))); 
	}

	public boolean puedeEditarLineaPresupuesto()
	{
		return SI.equals(this.fungibleEdit.getPresupuesto()) && EstadoPresupuesto.SOLICITADO.equals(this.fungibleEdit.getEstadoPresupuesto());
	}
	
	private boolean puedeEliminarLineaPresupuesto()
	{
		return puedeEditarLineaPresupuesto() && (identity.tienePerfilEnServicio(TipoPerfil.SUPERVISOR, this.servicioFungible.getCod()) || identity.tienePerfilEnServicio(TipoPerfil.TECNICO, this.servicioFungible.getCod()));
	}
	
	public boolean puedeEliminar(LineaFungible lf) 
	{
		return this.fungibleEdit.getFactura() == null && ((EstadoConsumo.PENDIENTE.equals(lf.getEstado()) && this.operacionModificar()) || this.puedeEliminarLineaPresupuesto());
	}

	private List<Consumo> mezclaLineasFungibleConOriginal()
	{
		List<Consumo> listaLineasConsumo = new ArrayList<>(); 
		if (this.fungibleEdit.getCod() != null)
		{
			listaLineasConsumo = consumoService.getFungiblesRelacionados(this.fungibleEdit.getFechaSolicitud(), this.fungibleEdit.getUsuarioSolicitante());
		}
		// Añadimos y modificamos lineas
		Iterator<LineaFungible> itLf = this.lineas.iterator();
		while (itLf.hasNext())
		{
			Consumo consumoAux = null;
			LineaFungible lf =  itLf.next();
			if (lf.getCod() != null)
			{
				consumoAux = consumoService.getConsumoById(lf.getCod());
				listaLineasConsumo.get(listaLineasConsumo.indexOf(consumoAux)).setCantidad(lf.getCantidad());
				listaLineasConsumo.get(listaLineasConsumo.indexOf(consumoAux)).setEstado(lf.getEstado());
			}
			else
			{
				consumoAux = new Consumo();
				consumoAux.setInterno(this.fungibleEdit.getInterno());
				consumoAux.setObservaciones(this.fungibleEdit.getObservaciones());
				consumoAux.setComentarioFacturacion(this.fungibleEdit.getComentarioFacturacion());
				consumoAux.setEntidadPagadora(this.fungibleEdit.getEntidadPagadora());
				consumoAux.setGrupoInvestigacion(this.fungibleEdit.getGrupoInvestigacion());
				consumoAux.setOrganizacion(this.fungibleEdit.getOrganizacion());
				consumoAux.setTipo("F");
				consumoAux.setPresupuesto(this.fungibleEdit.getPresupuesto());
				consumoAux.setUsuarioConectado(identity.getUsuarioConectado());
				consumoAux.setUsuarioSolicitante(this.fungibleEdit.getUsuarioSolicitante() == null ? identity.getUsuarioConectado() : this.fungibleEdit.getUsuarioSolicitante());
				consumoAux.setUsuarioIrAsociado(this.fungibleEdit.getUsuarioIrAsociado());
				consumoAux.setCantidad(lf.getCantidad());
				consumoAux.setEstado(lf.getEstado());
				consumoAux.setProducto(lf.getProducto());
				consumoAux.setProyecto(lf.getProyecto());
				consumoAux.setFechaSolicitud(lf.getFechaSolicitud());
				listaLineasConsumo.add(consumoAux);		
			}
		}
		// Eliminamos líneas
		Iterator<LineaFungible> itLfE = this.lineasEliminadas.iterator();
		while (itLfE.hasNext())
		{
			LineaFungible lfE = itLfE.next();
			if (lfE.getCod() != null)
			{
				Consumo consumoAux = consumoService.getConsumoById(lfE.getCod());
				listaLineasConsumo.remove(listaLineasConsumo.get(listaLineasConsumo.indexOf(consumoAux)));
			}
		}
		return listaLineasConsumo;
	}
	

	public String guardar()  
	{
		// Obtenemos la lista de consumos a guardar
		List<Consumo> listaConsumos = this.mezclaLineasFungibleConOriginal();
		if (this.fungibleEdit.getConsumoPadre()==null) 
		{
			return guardarSinPadre(listaConsumos);
		}
		else 
		{
			return guardarConPadre(listaConsumos);
		}
	}
	
	
	private String guardarSinPadre(List<Consumo> listaConsumos)
	{
		boolean esAlta = false;
		if (listaConsumos.get(0).getCod() == null)
		{
			esAlta = true;
		}
		for (final Consumo c: listaConsumos)
		{
			// Actualizamos comentarios en todas las líneas de fungible
			c.setObservaciones(this.fungibleEdit.getObservaciones());
			c.setComentarioFacturacion(this.fungibleEdit.getComentarioFacturacion());
		}
		try 
		{
			consumoService.guardarPedidoFungible(listaConsumos, this.lineasEliminadas);
		} 
		catch ( SaiException ex ) 
		{
			log.error(LOG_FUNGIBLES_GUARDAR_ERROR, ex);
			if (esAlta)
			{
				facesMessages.add(StatusMessage.Severity.INFO, KEY_MSJ_FUNGIBLES_CREAR_ERROR, null, null, ex.getMessage());
			}
			else
			{
				facesMessages.add(StatusMessage.Severity.INFO, KEY_MSJ_FUNGIBLES_MODIFICAR_ERROR, null, null, ex.getMessage());
			}
			return null;
		}
		this.productoLineaFungibleAdd = null;
		this.proyectoLineaFungibleAdd = null;
		this.cantidadLineaFungibleAdd = BigDecimal.valueOf(1.00);
		consumoService.recargarConsumo(listaConsumos.get(0));
		if (esAlta)
		{
			mensajeService.notificacionesNuevaSolicitud(listaConsumos.get(0), this.enviaEmail);
			facesMessages.add(StatusMessage.Severity.INFO, "fungibles.crear.ok", KEY_MSJ_CONSUMOS_LINEAS, null, null, String.valueOf(this.lineas.size()));
		}
		else
		{
			facesMessages.add(StatusMessage.Severity.INFO, "fungibles.modificar.ok", KEY_MSJ_CONSUMOS_LINEAS, null, null, String.valueOf(this.lineas.size()));
		}
		return this.establecerFungibleEdit(listaConsumos.get(0));

	}
	
	private String guardarConPadre(List<Consumo> listaConsumos)
	{
		boolean esAlta = false;
		if (listaConsumos.get(0).getCod() == null)
		{
			esAlta = true;
		}
		this.fungibleEdit.getConsumoPadre().setComentarioFacturacion(this.fungibleEdit.getComentarioFacturacion());
		for (final Consumo c: listaConsumos) 
		{
			c.setConsumoPadre(this.fungibleEdit.getConsumoPadre());
			c.setEstado(EstadoConsumo.FINALIZADO);
			c.setInterno(this.fungibleEdit.getConsumoPadre().getInterno());
			c.setUsuarioSolicitante(this.fungibleEdit.getConsumoPadre().getUsuarioSolicitante());
			c.setUsuarioConectado(this.fungibleEdit.getConsumoPadre().getUsuarioConectado());
			c.setUsuarioIrAsociado(this.fungibleEdit.getConsumoPadre().getUsuarioIrAsociado() );
			c.setEntidadPagadora(this.fungibleEdit.getConsumoPadre().getEntidadPagadora());
			c.setObservaciones(this.fungibleEdit.getObservaciones());
			c.setComentarioFacturacion(this.fungibleEdit.getConsumoPadre().getComentarioFacturacion());
			if (SI.equals(fungibleEdit.getPresupuesto()))
			{
				c.setEstadoPresupuesto(this.fungibleEdit.getConsumoPadre().getEstadoPresupuesto());
			}
		}
		try 
		{
			consumoService.guardarPedidoFungibleYConsumoPadre(this.fungibleEdit.getConsumoPadre(), listaConsumos, this.lineasEliminadas);
		} 
		catch ( SaiException ex ) 
		{
			log.error(LOG_FUNGIBLES_GUARDAR_ERROR, ex);
			if (esAlta)
			{
				facesMessages.add(StatusMessage.Severity.INFO, this.getKeyMsj(KEY_MSJ_FUNGIBLES_CREAR_ERROR), null, null, ex.getMessage());
			}
			else
			{
				facesMessages.add(StatusMessage.Severity.INFO, this.getKeyMsj(KEY_MSJ_FUNGIBLES_MODIFICAR_ERROR), null, null, ex.getMessage());
			}
			return null;
		}	
		consumoService.recargarConsumo(this.fungibleEdit.getConsumoPadre());
		if (esAlta)
		{
			facesMessages.add(StatusMessage.Severity.INFO, this.getKeyMsj("fungibles.crear.ok"), KEY_MSJ_CONSUMOS_LINEAS, null, null, String.valueOf(this.lineas.size()));
		}
		else
		{
			facesMessages.add(StatusMessage.Severity.INFO, this.getKeyMsj("fungibles.modificar.ok"), KEY_MSJ_CONSUMOS_LINEAS, null, null, String.valueOf(this.lineas.size()));
		}
		this.productoLineaFungibleAdd = null;
		this.proyectoLineaFungibleAdd = null;
		this.cantidadLineaFungibleAdd = BigDecimal.valueOf(1.00);
		consumoService.recargarConsumo(listaConsumos.get(0));
		if (!accesoDesdeListado)
		{
			return solicitudPrestaciones.establecerPrestacionEdit(this.fungibleEdit.getConsumoPadre());
		}
		else
		{
			return this.establecerFungibleEdit(listaConsumos.get(0));
		}
	}

	
	public String anularFungibles() 
	{
		this.fungibleEdit.setEstado(EstadoConsumo.ANULADO);
		List<Consumo> listaConsumos = this.mezclaLineasFungibleConOriginal();
		for (final Consumo c: listaConsumos) 
		{
			c.setEstado(EstadoConsumo.ANULADO);
		}
		try 
		{
			consumoService.guardarPedidoFungible(listaConsumos, this.lineasEliminadas);
		} 
		catch ( SaiException ex ) 
		{
			log.error(LOG_FUNGIBLES_GUARDAR_ERROR, ex);
			facesMessages.add(StatusMessage.Severity.INFO, this.getKeyMsj(KEY_MSJ_FUNGIBLES_MODIFICAR_ERROR), null, null, ex.getMessage());
			return null;
		}
		facesMessages.add(StatusMessage.Severity.INFO, this.getKeyMsj("fungible.anular.todos.ok"), null, null, fungibleEdit.getProducto().getDescripcion());
		consumoService.recargarConsumo(this.fungibleEdit);
		if (this.fungibleEdit.getConsumoPadre() == null)
		{
			return this.establecerFungibleEdit(this.fungibleEdit);
		}
		else
		{
			return solicitudPrestaciones.establecerPrestacionEdit(this.fungibleEdit.getConsumoPadre());
		}
	}

	public String rechazarFungibles() 
	{
		fungibleEdit.setEstado(EstadoConsumo.RECHAZADO);
		List<Consumo> listaConsumos = this.mezclaLineasFungibleConOriginal();
		for (final Consumo c: listaConsumos) 
		{
			c.setEstado( EstadoConsumo.RECHAZADO );
		}
		try 
		{
			consumoService.guardarPedidoFungible(listaConsumos, this.lineasEliminadas);
		} 
		catch ( SaiException ex ) 
		{
			log.error(LOG_FUNGIBLES_GUARDAR_ERROR, ex);
			facesMessages.add(StatusMessage.Severity.INFO, KEY_MSJ_FUNGIBLES_MODIFICAR_ERROR, null, null, ex.getMessage());
			return null;
		}
		consumoService.recargarConsumo(this.fungibleEdit);
		mensajeService.notificacionesRechazoSolicitud(this.fungibleEdit);
		facesMessages.add(StatusMessage.Severity.INFO, "fungible.rechazar.todos.ok", null, null, fungibleEdit.getProducto().getDescripcion());
		return this.establecerFungibleEdit(this.fungibleEdit);
	}

	public String reactivarFungibles() 
	{
		this.fungibleEdit.setEstado(EstadoConsumo.PENDIENTE);
		List<Consumo> listaConsumos = mezclaLineasFungibleConOriginal();
		for(final Consumo c: listaConsumos) 
		{
			c.setEstado( EstadoConsumo.PENDIENTE );
		}
		try 
		{
			consumoService.guardarPedidoFungible(listaConsumos, this.lineasEliminadas);
		} 
		catch ( SaiException ex ) 
		{
			log.error(LOG_FUNGIBLES_GUARDAR_ERROR, ex);
			facesMessages.add(StatusMessage.Severity.INFO, KEY_MSJ_FUNGIBLES_MODIFICAR_ERROR, null, null, ex.getMessage());
			return null;
		}
		facesMessages.add(StatusMessage.Severity.INFO, "fungible.reactivar.todos.ok", null, null, fungibleEdit.getProducto().getDescripcion());
		consumoService.recargarConsumo(this.fungibleEdit);
		return this.establecerFungibleEdit(this.fungibleEdit);
	}

	public String entregarFungibles() 
	{
		List<Consumo> listaConsumos = this.mezclaLineasFungibleConOriginal();
		fungibleEdit.setEstado(EstadoConsumo.FINALIZADO);
		for(final Consumo c: listaConsumos) 
		{
			c.setEstado(EstadoConsumo.FINALIZADO);
		}
		try 
		{
			consumoService.guardarPedidoFungible(listaConsumos, this.lineasEliminadas);
		} 
		catch ( SaiException ex ) 
		{
			log.error(LOG_FUNGIBLES_GUARDAR_ERROR, ex);
			facesMessages.add(StatusMessage.Severity.INFO, KEY_MSJ_FUNGIBLES_MODIFICAR_ERROR, null, null, ex.getMessage());
			return null;
		}
		facesMessages.add(StatusMessage.Severity.INFO, "fungible.entregar.todos.ok", null, null, fungibleEdit.getProducto().getDescripcion());
		consumoService.recargarConsumo(this.fungibleEdit);
		return this.establecerFungibleEdit(this.fungibleEdit);
	}
	

	public String volverListado() 
	{
		return VOLVER_LISTADO;
	}
	
	public String volverConsumoPadre()
	{
		return solicitudPrestaciones.establecerPrestacionEdit(this.fungibleEdit.getConsumoPadre());
	}

	private boolean puedeHacerOperacion(OperacionConsumo operacion)
	{
		return consumoService.permitidaOperacion(this.fungibleEdit, this.servicioFungible, "F", operacion);
	}

	public boolean operacionGuardar()
	{
		return puedeHacerOperacion(OperacionConsumo.GUARDAR);
	}

	public boolean operacionAnular()
	{
		if (NO.equals(fungibleEdit.getPresupuesto()))
		{
			return puedeHacerOperacion(OperacionConsumo.ANULAR);
		}
		else
		{
			return operacionPresupuestoModificar();
		}
	}

	public boolean operacionRechazar()
	{
		if (this.fungibleEdit.getCod() != null && NO.equals(fungibleEdit.getPresupuesto()))
		{
			return puedeHacerOperacion(OperacionConsumo.RECHAZAR);
		}
		else
		{
			return false;
		}
	}

	public boolean operacionReactivar() {
		if (this.fungibleEdit.getCod() != null && NO.equals(fungibleEdit.getPresupuesto()))
		{
			return puedeHacerOperacion(OperacionConsumo.REACTIVAR);		
		}
		else
		{
			return false;
		}
	}

	public boolean operacionComentar()
	{
		return puedeHacerOperacion(OperacionConsumo.COMENTAR);
	}

	public boolean operacionComentarFacturacion()
	{
		return NO.equals(fungibleEdit.getPresupuesto()) && puedeHacerOperacion(OperacionConsumo.COMENTAR_FACTURACION);
	}
	
	public boolean operacionEntregar()
	{
		return NO.equals(fungibleEdit.getPresupuesto()) && puedeHacerOperacion(OperacionConsumo.ENTREGAR);
	}

	public boolean operacionModificar()
	{
		if (NO.equals(fungibleEdit.getPresupuesto())) 
		{
			return puedeHacerOperacion(OperacionConsumo.MODIFICAR);
		}
		else
		{
			return operacionPresupuestoModificar();
		}
	}

	private boolean operacionPresupuestoModificar()
	{
		return SI.equals(fungibleEdit.getPresupuesto()) && 
			   EstadoPresupuesto.SOLICITADO.equals(fungibleEdit.getEstadoPresupuesto()) && 
			   (identity.tienePerfilEnServicio(TipoPerfil.SUPERVISOR, fungibleEdit.getConsumoPadre().getProducto().getServicio().getCod()) || identity.tienePerfilEnServicio(TipoPerfil.TECNICO, fungibleEdit.getConsumoPadre().getProducto().getServicio().getCod()));
	}
	
	public boolean preguntarEnvioMailUsuario()
	{
		return consumoService.preguntarEnvioMailUsuario(this.fungibleEdit);
	}
	
    public String getDescripcionEntidad(EntidadPagadora ep)
    {
    	return tarifaService.getDescripcionEntidadPagadora( ep );
    }
    
	public String getColorEstadoLinea(String estado)
	{
		if (estado == null)
		{
			return null;
		}
		if (estado.equals(EstadoConsumo.PENDIENTE))
		{
			return "amarillo";
		}
		else if (estado.equals(EstadoConsumo.ACEPTADO) || estado.equals(EstadoConsumo.FINALIZADO)) 
		{
			return "verde";
		}
		else if (estado.equals(EstadoConsumo.ANULADO) || estado.equals(EstadoConsumo.RECHAZADO)) 
		{
			return "rojo";
		}
		return null;
	}
	
	public String formatCantidad( BigDecimal number ) 
	{
		return Utilidades.formatCantidad(number);	
	}
	
	private String getKeyMsj(String key)
	{
		if ("SI".equals(this.fungibleEdit.getPresupuesto()))
		{
			return new StringBuilder(key).append(".presupuesto").toString();
		}
		return key;
	}
	
}
