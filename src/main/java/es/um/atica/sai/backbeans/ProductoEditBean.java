package es.um.atica.sai.backbeans;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.jboss.seam.log.Log;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;

import es.um.atica.commons.utils.UtilDate;
import es.um.atica.sai.dtos.ProductoJusto;
import es.um.atica.sai.dtos.TipoPerfil;
import es.um.atica.sai.entities.Anexo;
import es.um.atica.sai.entities.Equipo;
import es.um.atica.sai.entities.HorarioDia;
import es.um.atica.sai.entities.Nivel1;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.ProductoEquipo;
import es.um.atica.sai.entities.ProductoPuertakron;
import es.um.atica.sai.entities.ProductoTipoCertificacion;
import es.um.atica.sai.entities.PuertaKron;
import es.um.atica.sai.entities.PuertaKronView;
import es.um.atica.sai.entities.ReservableHorario;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.ServicioPuertakron;
import es.um.atica.sai.entities.Tarifa;
import es.um.atica.sai.entities.TecnicoProducto;
import es.um.atica.sai.entities.TipoCertificacion;
import es.um.atica.sai.entities.TipoHorario;
import es.um.atica.sai.entities.TipoReservable;
import es.um.atica.sai.entities.TipoTarifa;
import es.um.atica.sai.entities.UnidadMedida;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.CertificacionesService;
import es.um.atica.sai.services.interfaces.FungibleService;
import es.um.atica.sai.services.interfaces.KronService;
import es.um.atica.sai.services.interfaces.ProductoService;
import es.um.atica.sai.services.interfaces.ReservableService;
import es.um.atica.sai.services.interfaces.ServicioService;
import es.um.atica.sai.services.interfaces.TarifaService;
import es.um.atica.sai.services.interfaces.UsuarioService;

@Name( ProductoEditBean.NAME )
@Scope( ScopeType.CONVERSATION )
public class ProductoEditBean {

	public static final String NAME = "productoEdit";
	private static final String RETORNO_LISTADO_PRODUCTOS = "productoEditOk";
	private static final String SI = "SI";

	@Logger
	private Log log;

	@In(create = true )
	private ProductoService productoService;

	@In(create = true )
	private ServicioService servicioService;

	@In(create = true )
	private FungibleService fungibleService;

	@In(create = true )
	private UsuarioService usuarioService;

	@In(create = true )
	private ReservableService reservableService;
	
	@In(create = true )
	private KronService kronService;

	@In(create = true )
	private TarifaService tarifaService;

	@In( create = true )
	private CertificacionesService certificacionesService;

	@In(create=true)
	protected FacesMessages facesMessages;


	@In(value="org.jboss.seam.security.identity", required=true)
	SaiIdentity identity;

	private List<Servicio> listaServicios;
	private Producto productoEdit;
	private Anexo anexoAdd;
	private boolean anexoNuevo;
	private ReservableHorario reservableHorarioAdd;
	private List<Nivel1> listaTiposFungible;
	private List<UnidadMedida> listaUnidadesMedida;
	private List<TipoReservable> listaTiposReservable;
	private List<TipoHorario> listaTiposHorario;
	private List<ReservableHorario> listaReservableHorarios;
	private String tipoVinculacionKronPrestacion;
	private Date fechaLimiteVinculacionKronPrestacion;
	private String vinculacionKronReserva;
	private BigDecimal stockVisualizar;

	// Técnicos
	private List<Usuario> listaUsuariosTecnicos;
	private TecnicoProducto tecnicoProductoAdd;

	// Tarifas
	private List<ProductoJusto> listaProductosJusto;
	private List<TipoTarifa> listaTiposTarifa;
	private ProductoJusto productoJustoTarifaAdd;
	private Long cantidadInicialTarifaAdd;
	private Tarifa tarifaAdd;

	// Puertas Kron
	private List<ServicioPuertakron> listaPuertas;
	private List<ProductoPuertakron> listaPuertasProducto;
	private PuertaKron puertaAdd;

	// Tipo de certificaciones
	private List<TipoCertificacion> listaTipoCertificacion;
	private TipoCertificacion tipoCertifiacionAdd;

	// Equipos
	private List<Equipo> listaEquipos;
	private List<ProductoEquipo> listaEquiposProducto;
	private ProductoEquipo productoEquipoEdit;
	
	private Integer tabActivo;
	
	public List<Servicio> getListaServicios() {
		return listaServicios;
	}

	public void setListaServicios( List<Servicio> listaServicios ) {
		this.listaServicios = listaServicios;
	}

	public Producto getProductoEdit() {
		return productoEdit;
	}

	public void setProductoEdit( Producto productoEdit ) {
		this.productoEdit = productoEdit;
	}

	public Anexo getAnexoAdd() {
		return anexoAdd;
	}

	public void setAnexoAdd( Anexo anexoAdd ) {
		this.anexoAdd = anexoAdd;
	}
	
	public boolean isAnexoNuevo() {
		return anexoNuevo;
	}
	
	public void setAnexoNuevo( boolean anexoNuevo ) {
		this.anexoNuevo = anexoNuevo;
	}

	public ReservableHorario getReservableHorarioAdd() {
		return reservableHorarioAdd;
	}
	
	public void setReservableHorarioAdd( ReservableHorario reservableHorarioAdd ) {
		this.reservableHorarioAdd = reservableHorarioAdd;
	}

	public String getTipoVinculacionKronPrestacion() {
		return tipoVinculacionKronPrestacion;
	}

	public void setTipoVinculacionKronPrestacion( String tipoVinculacionKronPrestacion ) {
		this.tipoVinculacionKronPrestacion = tipoVinculacionKronPrestacion;
	}

	public Date getFechaLimiteVinculacionKronPrestacion() {
		return fechaLimiteVinculacionKronPrestacion;
	}

	public void setFechaLimiteVinculacionKronPrestacion( Date fechaLimiteVinculacionKronPrestacion ) {
		this.fechaLimiteVinculacionKronPrestacion = fechaLimiteVinculacionKronPrestacion;
	}

	public String getVinculacionKronReserva() {
		return vinculacionKronReserva;
	}

	public void setVinculacionKronReserva( String vinculacionKronReserva ) {
		this.vinculacionKronReserva = vinculacionKronReserva;
	}

	public List<Usuario> getListaUsuariosTecnicos() {
		return listaUsuariosTecnicos;
	}

	public void setListaUsuariosTecnicos( List<Usuario> listaUsuariosTecnicos ) {
		this.listaUsuariosTecnicos = listaUsuariosTecnicos;
	}

	public List<ProductoJusto> getListaProductosJusto() {
		return listaProductosJusto;
	}

	public void setListaProductosJusto( List<ProductoJusto> listaProductosJusto ) {
		this.listaProductosJusto = listaProductosJusto;
	}

	public List<TipoTarifa> getListaTiposTarifa() {
		return listaTiposTarifa;
	}

	public void setListaTiposTarifa( List<TipoTarifa> listaTiposTarifa ) {
		this.listaTiposTarifa = listaTiposTarifa;
	}

	public List<Nivel1> getListaTiposFungible() {
		return listaTiposFungible;
	}

	public void setListaTiposFungible( List<Nivel1> listaTiposFungible ) {
		this.listaTiposFungible = listaTiposFungible;
	}

	public List<TipoReservable> getListaTiposReservable() {
		return listaTiposReservable;
	}

	public void setListaTiposReservable( List<TipoReservable> listaTiposReservable ) {
		this.listaTiposReservable = listaTiposReservable;
	}

	public List<TipoHorario> getListaTiposHorario() {
		return listaTiposHorario;
	}
	
	public void setListaTiposHorario( List<TipoHorario> listaTiposHorario ) {
		this.listaTiposHorario = listaTiposHorario;
	}

	public List<ReservableHorario> getListaReservableHorarios() {
		return listaReservableHorarios;
	}
	
	public void setListaReservableHorarios( List<ReservableHorario> listaReservableHorarios ) {
		this.listaReservableHorarios = listaReservableHorarios;
	}

	public List<UnidadMedida> getListaUnidadesMedida() {
		return listaUnidadesMedida;
	}

	public void setListaUnidadesMedida( List<UnidadMedida> listaUnidadesMedida ) {
		this.listaUnidadesMedida = listaUnidadesMedida;
	}

	public TecnicoProducto getTecnicoProductoAdd() {
		return tecnicoProductoAdd;
	}

	public void setTecnicoProductoAdd( TecnicoProducto tecnicoProductoAdd ) {
		this.tecnicoProductoAdd = tecnicoProductoAdd;
	}

	public ProductoJusto getProductoJustoTarifaAdd() {
		return productoJustoTarifaAdd;
	}

	public void setProductoJustoTarifaAdd( ProductoJusto productoJustoTarifaAdd ) {
		this.productoJustoTarifaAdd = productoJustoTarifaAdd;
	}

	public Long getCantidadInicialTarifaAdd() {
		return cantidadInicialTarifaAdd;
	}

	public void setCantidadInicialTarifaAdd( Long cantidadInicialTarifaAdd ) {
		this.cantidadInicialTarifaAdd = cantidadInicialTarifaAdd;
	}

	public Tarifa getTarifaAdd() {
		return tarifaAdd;
	}

	public void setTarifaAdd( Tarifa tarifaAdd ) {
		this.tarifaAdd = tarifaAdd;
	}

	public List<ServicioPuertakron> getListaPuertas() {
		return listaPuertas;
	}

	public void setListaPuertas( List<ServicioPuertakron> listaPuertas ) {
		this.listaPuertas = listaPuertas;
	}

	public List<ProductoPuertakron> getListaPuertasProducto() {
		return listaPuertasProducto;
	}

	public void setListaPuertasProducto( List<ProductoPuertakron> listaPuertasProducto ) {
		this.listaPuertasProducto = listaPuertasProducto;
	}

	public PuertaKron getPuertaAdd() {
		return puertaAdd;
	}

	public void setPuertaAdd( PuertaKron puertaAdd ) {
		this.puertaAdd = puertaAdd;
	}

	public TipoCertificacion getTipoCertifiacionAdd() {
		return tipoCertifiacionAdd;
	}

	public void setTipoCertifiacionAdd( TipoCertificacion tipoCertifiacionAdd ) {
		this.tipoCertifiacionAdd = tipoCertifiacionAdd;
	}

	public List<TipoCertificacion> getListaTipoCertificacion() {
		return listaTipoCertificacion;
	}

	public void setListaTipoCertificacion( List<TipoCertificacion> listaTipoCertificacion ) {
		this.listaTipoCertificacion = listaTipoCertificacion;
	}

	public List<Equipo> getListaEquipos() {
		return listaEquipos;
	}
	
	public void setListaEquipos( List<Equipo> listaEquipos ) {
		this.listaEquipos = listaEquipos;
	}
	
	public List<ProductoEquipo> getListaEquiposProducto() {
		return listaEquiposProducto;
	}
	
	public void setListaEquiposProducto( List<ProductoEquipo> listaEquiposProducto ) {
		this.listaEquiposProducto = listaEquiposProducto;
	}
	
	public ProductoEquipo getProductoEquipoEdit() {
		return productoEquipoEdit;
	}
	
	public void setProductoEquipoEdit( ProductoEquipo productoEquipoEdit ) {
		this.productoEquipoEdit = productoEquipoEdit;
	}
	
	public Integer getTabActivo() {
		return tabActivo;
	}
	
	public void setTabActivo( Integer tabActivo ) {
		this.tabActivo = tabActivo;
	}

	public BigDecimal getStockVisualizar() {
		return stockVisualizar;
	}
	
	public void setStockVisualizar( BigDecimal stockVisualizar ) {
		this.stockVisualizar = stockVisualizar;
	}

	@Create
	public void inicializa()
	{
		this.listaServicios = identity.getServiciosPerfil(TipoPerfil.SUPERVISOR);
		this.productoEdit = new Producto();
		this.listaTipoCertificacion = certificacionesService.getListaTiposCertificaciones();
		this.anexoNuevo = false;
	}

	public String establecerProductoEdit(Producto prod)
	{
		this.productoEdit = prod;
		this.configurarEdicionProducto();
		this.tabActivo = 0;
		return NAME;
	}

	public String establecerProductoCreate()
	{
		this.productoEdit = new Producto();
		this.productoEdit.setEstado("ALTA");
		this.productoEdit.setFactor(BigDecimal.valueOf(1));
		if (this.listaServicios.size() == 1)
		{
			this.productoEdit.setServicio( this.listaServicios.get(0));
			this.seleccionadoServicioOTipo();
		}
		return NAME;
	}

	public void seleccionadoServicioOTipo()
	{
		if (this.productoEdit.getServicio() != null)
		{
			if ("F".equals(this.productoEdit.getTipo()) )
			{
				this.listaTiposFungible = fungibleService.getListaFungiblesByServicio(productoEdit.getServicio());
				this.listaUnidadesMedida = productoService.getListaUnidadMedida();
				this.productoEdit.setRequiereValidacionIr("NO");
				this.productoEdit.setPedirNivelBioseguridad("NO");
			}
			else if ("R".equals(this.productoEdit.getTipo()))
			{
				this.listaTiposReservable = productoService.getListaTiposReservableByServicio(productoEdit.getServicio());
			}
		}
	}

	private void configurarEdicionProducto()
	{
		if ("F".equals(productoEdit.getTipo()) )
		{
			configurarEdicionProductoFungible();
		}
		else
		{
			if ("R".equals(productoEdit.getTipo()))
			{
				configurarEdicionProductoReserva();
			}
			if ("P".equals(productoEdit.getTipo()))
			{
				configurarEdicionProductoPrestacion();
			}
			if (requiereConfiguracionTecnicos())
			{
				listaUsuariosTecnicos = usuarioService.getTecnicosByServicio(productoEdit.getServicio());
				tecnicoProductoAdd = new TecnicoProducto();
			}
			if ("SI".equals(vinculacionKronReserva) || ( tipoVinculacionKronPrestacion != null ))
			{
				listaPuertas = kronService.getListaServicioPuertakronByService( productoEdit.getServicio() );
				listaPuertasProducto = kronService.getListaProductoPuertakronByProducto( productoEdit );
			}
		}
		listaProductosJusto = productoService.getListaProductosJusto(productoEdit.getServicio().getAbreviatura());
		listaTiposTarifa = tarifaService.getTiposTarifas();
		tarifaAdd = new Tarifa();
	}

	private void configurarEdicionProductoFungible()
	{
		listaTiposFungible = fungibleService.getListaFungiblesByServicio(productoEdit.getServicio());
		listaUnidadesMedida = productoService.getListaUnidadMedida();
		vinculacionKronReserva = null;
		tipoVinculacionKronPrestacion = null;
		if ("SI".equals(productoEdit.getControlStock()))
		{
			this.stockVisualizar = productoService.getStockDisponibleByProducto(this.productoEdit);
		}
	}

	private void configurarEdicionProductoReserva()
	{
		listaTiposReservable = productoService.getListaTiposReservableByServicio(productoEdit.getServicio());
		if (( productoEdit.getKronReservaMinutoAntelacion() != null ) || ( productoEdit.getKronReservaMinutoPosterior() != null ))
		{
			vinculacionKronReserva = "SI";
		}
		else
		{
			vinculacionKronReserva = "NO";
		}
		tipoVinculacionKronPrestacion = null;
	}

	private void configurarEdicionProductoPrestacion()
	{
		if (productoEdit.getKronPrestacionDias()!= null)
		{
			tipoVinculacionKronPrestacion="NUMDIAS";
		}
		else if (productoEdit.getKronPrestacionFechaLimite()!= null )
		{
			tipoVinculacionKronPrestacion="FECHALIMITE";
			fechaLimiteVinculacionKronPrestacion = UtilDate.stringToDate(productoEdit.getKronPrestacionFechaLimite(), "dd/MM");
		}
		else
		{
			tipoVinculacionKronPrestacion=null;
		}
		this.listaEquipos = reservableService.getEquiposByServicio(this.productoEdit.getServicio());
		this.listaEquiposProducto = productoService.getProductoEquiposByProducto(this.productoEdit);
		this.listaTiposHorario = reservableService.getListaTiposHorarioByServicio(this.productoEdit.getServicio());
		this.listaReservableHorarios = reservableService.getListaReservableHorarioByProducto(this.productoEdit);
		vinculacionKronReserva = null;
	}

	public void seleccionadaVinculacionKronReserva()
	{
		if ("NO".equals(vinculacionKronReserva))
		{
			productoEdit.setKronReservaMinutoAntelacion(null);
			productoEdit.setKronReservaMinutoPosterior(null);
		}
	}

	public void seleccionadaTipoVinculacionKronPrestacion()
	{
		if (tipoVinculacionKronPrestacion == null)
		{
			productoEdit.setKronPrestacionDias(null);
			productoEdit.setKronPrestacionFechaLimite(null);
			fechaLimiteVinculacionKronPrestacion = null;
		}
		else if ("NUMDIAS".equals(tipoVinculacionKronPrestacion))
		{
			productoEdit.setKronPrestacionFechaLimite(null);
			fechaLimiteVinculacionKronPrestacion = null;
		}
		else if ("FECHALIMITE".equals(tipoVinculacionKronPrestacion))
		{
			productoEdit.setKronPrestacionDias(null);
		}
	}

	public void subidoFicheroAnexo(FileUploadEvent fue)
	{
		anexoAdd = new Anexo();
		anexoAdd.setTipo("PRODUCTO");
		anexoAdd.setDocumento(fue.getFile().getContents());
		anexoAdd.setNomDocumento(fue.getFile().getFileName());
		anexoAdd.setProducto(productoEdit );
		productoEdit.getAnexos().add(anexoAdd);
		this.anexoNuevo = true;
	}

	public void eliminarAnexo(Anexo anexo)
	{
		final String descripcion = anexo.getNomDocumento();
		productoEdit.getAnexos().remove(anexo);
		anexoAdd = null;
		if (anexo.getCod() != null)
		{
			try
			{
				productoService.eliminarAnexo( anexo );
			}
			catch ( final Exception e )
			{
				facesMessages.add(StatusMessage.Severity.INFO, "producto.anexo.eliminar.error", null, null, e.getMessage());
				return;
			}
		}
		facesMessages.add(StatusMessage.Severity.INFO, "producto.anexo.eliminar.ok", null, null, descripcion);
	}

	private void prepararGuardado()
	{
		if ("F".equals(productoEdit.getTipo()))
		{
			productoEdit.setPresupuesto( "NO" );
			productoEdit.setTipoReservable(null);
			productoEdit.setRequiereTecnico(null);
			productoEdit.setRequiereValidacion(null);
			productoEdit.setRequiereValidacionIr("NO");
			productoEdit.setRequiereAnexo(null);
			productoEdit.setPreguntaPersonalizada(null);
			productoEdit.setConsumoAsociado(null);
			productoEdit.setKronReservaMinutoAntelacion(null);
			productoEdit.setKronReservaMinutoPosterior(null);
			productoEdit.setKronPrestacionDias(null);
			productoEdit.setKronPrestacionFechaLimite(null);
			if ("NO".equals(productoEdit.getControlStock()))
			{
				productoEdit.setStock(null);
			}
			else
			{
				productoEdit.setStock(this.stockVisualizar.add(productoService.getStockConsumidoByProducto(this.productoEdit)));
			}
		}
		else if ("R".equals(productoEdit.getTipo()))
		{
			productoEdit.setPresupuesto( "NO" );
			productoEdit.setNivel1(null);
			productoEdit.setUnidadMedida(null);
			productoEdit.setControlStock(null);
			productoEdit.setStock(null);
			productoEdit.setConsumoAsociado(null);
			productoEdit.setKronPrestacionDias(null);
			productoEdit.setKronPrestacionFechaLimite(null);
		}
		else if ("P".equals(productoEdit.getTipo()))
		{
			productoEdit.setNivel1(null);
			productoEdit.setUnidadMedida(null);
			productoEdit.setControlStock(null);
			productoEdit.setStock(null);
			productoEdit.setTipoReservable(null);
			productoEdit.setRequiereTecnico(null);
			productoEdit.setKronReservaMinutoAntelacion(null);
			productoEdit.setKronReservaMinutoPosterior(null);
		}
	}


	public void guardarProducto()
	{
		prepararGuardado();
		if ("P".equals(productoEdit.getTipo()) && ( fechaLimiteVinculacionKronPrestacion != null ))
		{
			productoEdit.setKronPrestacionFechaLimite(UtilDate.dateToString(fechaLimiteVinculacionKronPrestacion,"dd/MM"));
		}
		try
		{
			productoService.guardarProducto(productoEdit);
		}
		catch ( final Exception ex )
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "producto.guardar.error", null, null, ex.getMessage());
			return;
		}
		productoService.recargarProducto(productoEdit);
		configurarEdicionProducto();
		final StringBuilder keyMsjDetalles = new StringBuilder("producto.guardar.ok.settarifas");
		if (requiereConfiguracionTecnicos())
		{
			keyMsjDetalles.append(".settecnicos");
		}
		if (( tipoVinculacionKronPrestacion != null ) || SI.equals(vinculacionKronReserva))
		{
			keyMsjDetalles.append(".setpuertas");
		}
		facesMessages.add(StatusMessage.Severity.INFO, "producto.guardar.ok", keyMsjDetalles.toString(), null, null);
	}

	public String volver()
	{
		return RETORNO_LISTADO_PRODUCTOS;
	}

	public void onTabChange(TabChangeEvent event) 
	{
		if (event.getTab().getId().equals("tabTarifas"))
		{
			this.tabActivo = 0;
		}
		else if (event.getTab().getId().equals("tabCertificaciones"))
		{
			this.tabActivo = 1;
		}
		else if (event.getTab().getId().equals("tabTecnicos"))
		{
			this.tabActivo = 2;
		}
		else if (event.getTab().getId().equals("tabEquipos"))
		{
			// Si se renderiza la pestaña de equipos también se renderizará la de técnicos
			this.tabActivo = 3;
		}
		else if (event.getTab().getId().equals("tabPuertasKron"))
		{
			if ("P".equals(this.productoEdit.getTipo()))
			{
				// Se han renderizado las pestañas de equipos y de técnicos
				this.tabActivo = 4;
			}
			else if (this.requiereConfiguracionTecnicos())
			{
				// Se ha renderizado la pestaña de técnicos pero no así la de equipos
				this.tabActivo = 3;
			}
			else
			{
				// No se ha renderizado la pestaña de equipos ni de técnicos
				this.tabActivo = 2;
			}
		}
		else if (event.getTab().getId().equals("tabHorariosKron"))
		{
			// Se trata de una prestación y se ha renderizado la pestaña de puertas del Kron
			this.tabActivo = 5;
		}
			
	}
	
	public void establecerProductoEquipoCreate()
	{
		this.productoEquipoEdit = new ProductoEquipo();
		this.productoEquipoEdit.setProducto(this.productoEdit);
	}
	
	public void establecerProductoEquipoEdit(ProductoEquipo pe)
	{
		this.productoEquipoEdit = pe;
	}
	
	public void guardarProductoEquipo()
	{
		try
		{
			productoService.guardarProductoEquipo(this.productoEquipoEdit);
		}
		catch ( final Exception ex )
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "producto.productoequipo.guardar.error", null, null, ex.getMessage() );
			return;
		}
		this.listaEquiposProducto = productoService.getProductoEquiposByProducto(this.productoEdit);
		facesMessages.add(StatusMessage.Severity.INFO, "producto.productoequipo.guardar.ok", null, null, this.productoEquipoEdit.getEquipo().getDescripcion());

	}
	
	public void eliminarProductoEquipo(ProductoEquipo pe)
	{
		final String nombreEquipo = pe.getEquipo().getDescripcion();
		try
		{
			productoService.eliminarProductoEquipo(pe);
		}
		catch ( final Exception ex )
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "producto.productoequipo.eliminar.error", null, null, ex.getMessage());
			return;
		}
		this.listaEquiposProducto.remove(pe);
		facesMessages.add(StatusMessage.Severity.INFO, "producto.productoequipo.eliminar.ok", null, null, nombreEquipo);
	}
	
	
	public boolean requiereConfiguracionTecnicos()
	{
		return ("P".equals(productoEdit.getTipo()) || ("R".equals(productoEdit.getTipo()) && !"NO".equals(productoEdit.getRequiereTecnico())));
	}

	public void establecerTecnicoProductoCreate()
	{
		tecnicoProductoAdd = new TecnicoProducto();
		tecnicoProductoAdd.setProducto(productoEdit);
	}

	public void establecerTecnicoProductoEdit(TecnicoProducto tp)
	{
		tecnicoProductoAdd = tp;
	}

	public void seleccionadaAsignacionAutoTecnico()
	{
		if (!"SI".equals(tecnicoProductoAdd.getAutomatico()))
		{
			tecnicoProductoAdd.setPrioridad(null);
		}
	}

	private boolean existeTecnicoAsignacionAutomatica()
	{
		for ( final TecnicoProducto t : productoEdit.getTecnicos() )
		{
			if (tecnicoProductoAdd.getCod() == null)
			{
				if (SI.equals(t.getAutomatico()))
				{
					return true;
				}
			}
			else
			{
				if (SI.equals(t.getAutomatico()) && !t.getCod().equals(tecnicoProductoAdd.getCod()))
				{
					return true;
				}
			}
		}
		return false;
	}

	public void guardarTecnicoProducto()
	{
		final boolean esAlta = tecnicoProductoAdd.getCod() == null;
		if ("P".equals(productoEdit.getTipo()) && SI.equals(tecnicoProductoAdd.getAutomatico()) && existeTecnicoAsignacionAutomatica())
		{
			facesMessages.add( StatusMessage.Severity.ERROR, "producto.add.tecnico.error", "producto.add.tecnico.error.yaexisteauto", null, null );
			return;
		}
		try
		{
			productoService.guardarTecnicoProducto(tecnicoProductoAdd);
		}
		catch ( final Exception ex )
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "producto.add.tecnico.error", null, null, ex.getMessage() );
			return;
		}
		if (esAlta)
		{
			productoEdit.getTecnicos().add(tecnicoProductoAdd);
		}
		facesMessages.add(StatusMessage.Severity.INFO, "producto.add.tecnico.ok", null, null, tecnicoProductoAdd.getUsuario().getDatosUsuario().getFullName());

	}

	public void eliminarTecnico(TecnicoProducto tecnico)
	{
		final String nombreTecnico = tecnico.getUsuario().getDatosUsuario().getFullName();
		try
		{
			productoService.eliminarTecnicoProducto( tecnico );
		}
		catch ( final Exception ex )
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "producto.del.tecnico.error", null, null, ex.getMessage());
			return;
		}
		productoEdit.getTecnicos().remove( tecnico );
		facesMessages.add(StatusMessage.Severity.INFO, "producto.del.tecnico.ok", null, null, nombreTecnico);
	}

	public BigDecimal getPrecioJusto(Tarifa tarifa)
	{
		return productoService.getPrecio(tarifa.getCodUdGasto(),tarifa.getCodTarifaJusto());
	}

	public BigDecimal getPrecioJusto(String udGasto, String codProductoJusto)
	{
		return productoService.getPrecio(udGasto, codProductoJusto);
	}

	public void establecerTarifaCreate()
	{
		tarifaAdd = new Tarifa();
		tarifaAdd.setProducto(productoEdit );
		productoJustoTarifaAdd = null;
		cantidadInicialTarifaAdd = null;
	}

	public void guardarTarifa()
	{
		tarifaAdd.setDescripcion(productoJustoTarifaAdd.getDescripcion() );
		tarifaAdd.setCodTarifaJusto(productoJustoTarifaAdd.getCodProductoJusto() );
		tarifaAdd.setCodUdGasto(productoJustoTarifaAdd.getUnidadAdministrativa() );
		try
		{
			productoService.crearTarifa(tarifaAdd, cantidadInicialTarifaAdd);
		}
		catch ( final Exception ex )
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "producto.add.tarifa.error", null, null, ex.getMessage());
			return;
		}
		productoEdit.getTarifas().add(tarifaAdd);
		facesMessages.add(StatusMessage.Severity.INFO, "producto.add.tarifa.ok", null, null, tarifaAdd.getDescripcion());
	}

	public void eliminarTarifa(Tarifa tarifa)
	{
		final String nombreTarifa = tarifa.getDescripcion();
		try
		{
			productoService.eliminarTarifa( tarifa );
		}
		catch ( final Exception ex )
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "producto.del.tarifa.error", null, null, ex.getMessage());
			return;
		}
		productoEdit.remove(tarifa);
		facesMessages.add(StatusMessage.Severity.INFO, "producto.del.tarifa.ok", null, null, nombreTarifa);
	}

	public void establecerPuertaKronCreate()
	{
		puertaAdd = null;
	}

	public void addPuertaProducto()
	{
		final ProductoPuertakron ppk = new ProductoPuertakron();
		ppk.setPuertaKron(puertaAdd);
		ppk.setProducto(productoEdit);
		try
		{
			kronService.crearProductoPuertakron(ppk);
			listaPuertasProducto.add(ppk);
			facesMessages.add(StatusMessage.Severity.INFO, "producto.puertakron.add.ok", null, null, getDescripcionPuerta(ppk.getPuertaKron().getPuertaKronView()));
		}
		catch(Exception ex)
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "producto.puertakron.add.error", null, null, ex.getMessage());
		}
	}

	public void eliminarProductoPuerta(ProductoPuertakron ppk) 
	{
		String descripcionPuerta = getDescripcionPuerta(ppk.getPuertaKron().getPuertaKronView());
		try
		{
			kronService.eliminarProductoPuertakron(ppk);
			listaPuertasProducto.remove(ppk);
			facesMessages.add(StatusMessage.Severity.INFO, "producto.puertakron.del.ok", null, null, descripcionPuerta);
		}
		catch(Exception ex)
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "producto.puertakron.del.error", null, null, ex.getMessage());
		}
	}

	private String getDescripcionPuerta(PuertaKronView pkv)
	{
		return new StringBuilder(pkv.getNombreEdificio()).append(" - ").append(pkv.getNombreLector()).toString();
	}
	
	public void establecerReservableHorarioCreate()
	{
		this.reservableHorarioAdd = new ReservableHorario();
		this.reservableHorarioAdd.setProducto(this.productoEdit);
	}

	public void eliminarReservableHorario( ReservableHorario rh )
	{
		try
		{
			reservableService.eliminarReservableHorario(rh);
		}
		catch (final Exception ex)
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "producto.rem.horario.error", null, null, ex.getMessage());
			return;
		}
		this.listaReservableHorarios.remove(rh);
		facesMessages.add(StatusMessage.Severity.INFO, "producto.rem.horario.ok", null, null, rh.getTipoHorario().getDescripcion());
	}

	public void crearReservableHorario()
	{
		try
		{
			reservableService.crearProductoHorario(reservableHorarioAdd);
		}
		catch(final Exception ex)
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "producto.add.horario.error", null, null, ex.getMessage());
			return;
		}
		this.listaReservableHorarios = reservableService.getListaReservableHorarioByProducto(this.productoEdit);
		facesMessages.add(StatusMessage.Severity.INFO, "producto.add.horario.ok", null, null, reservableHorarioAdd.getTipoHorario().getDescripcion());
	}


	public void establecerTipoCertificacionCreate() {

		tipoCertifiacionAdd = new TipoCertificacion();
	}

	public void eliminarCertificacion( ProductoTipoCertificacion tc ) {

		try {
			certificacionesService.eliminarProductoTipoCertificacion( tc );
			facesMessages.add( StatusMessage.Severity.INFO, "producto.del.certificacion.ok", null, null,
					tc.getTipoCertificacion().getNombre() );
			productoEdit.getListaTiposCertificacion().remove( tc );
		} catch ( final Exception ex ) {
			facesMessages.add( StatusMessage.Severity.ERROR, "producto.del.certificacion.error", null, null,
					ex.getMessage() );
		}
	}

	public void addTipoCertificacion() {
		try {
			final ProductoTipoCertificacion ptc = new ProductoTipoCertificacion( tipoCertifiacionAdd, productoEdit );
			certificacionesService.guardarProductoTipoCertificacion( ptc );
			facesMessages.add( StatusMessage.Severity.INFO, "producto.add.certificacion.ok", null, null,
					tipoCertifiacionAdd.getNombre() );
			productoEdit.getListaTiposCertificacion().add( ptc );
		} catch ( final Exception ex ) {
			facesMessages.add( StatusMessage.Severity.ERROR, "producto.add.certificacion.error", null, null,
					ex.getMessage() );
		}
	}

	public String getDescripcionAmpliadaEquipo(Equipo equipo)
	{
		StringBuilder descripcion = new StringBuilder(equipo.getDescripcion());
		if (equipo.getTipoReservable() != null)
		{
			descripcion.append(" (").append(equipo.getTipoReservable().getDescripcion()).append(")");
		}
		return descripcion.toString();
	}


	
	public List<HorarioDia> getListaHorarioDiaByTipohorario(TipoHorario th)
	{
		return reservableService.getListaHorarioDiaByTipohorario( th );
	}

	public String getDescripcionHorarioDia(HorarioDia hd)
	{
		return reservableService.getDescripcionHorarioDia( hd );
	}

	public String getDescripcionTipoProducto(String tipo)
	{
		return productoService.getDescripcionTipoProducto( tipo );
	}
}
