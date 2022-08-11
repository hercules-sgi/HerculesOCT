package es.um.atica.sai.backbeans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import es.um.atica.commons.utils.UtilDate;
import es.um.atica.sai.dtos.EstadoConsumo;
import es.um.atica.sai.dtos.EstadoPresupuesto;
import es.um.atica.sai.dtos.OperacionConsumo;
import es.um.atica.sai.dtos.OperacionPresupuesto;
import es.um.atica.sai.dtos.TipoPerfil;
import es.um.atica.sai.entities.Anexo;
import es.um.atica.sai.entities.Consumo;
import es.um.atica.sai.entities.ConsumoEquipo;
import es.um.atica.sai.entities.EntidadPagadora;
import es.um.atica.sai.entities.Equipo;
import es.um.atica.sai.entities.GrupoInvestigacion;
import es.um.atica.sai.entities.Muestra;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.Proyecto;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.TipoCertificacion;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.ConsumoService;
import es.um.atica.sai.services.interfaces.KronService;
import es.um.atica.sai.services.interfaces.MensajeService;
import es.um.atica.sai.services.interfaces.ProductoService;
import es.um.atica.sai.services.interfaces.ProyectoService;
import es.um.atica.sai.services.interfaces.ServicioService;
import es.um.atica.sai.services.interfaces.TarifaService;
import es.um.atica.sai.services.interfaces.UsuarioService;
import es.um.atica.sai.utils.Utilidades;

@Name("solicitudPrestaciones")
@Scope( ScopeType.CONVERSATION )
public class SolicitudPrestacionesBean {
	@Logger
	private Log log;

	@In(create = true, required = true)
	ServicioService servicioService;

	@In(create = true, required = true)
	UsuarioService usuarioService;

	@In(create = true, required = true)
	TarifaService tarifaService;

	@In(create = true, required = true)
	KronService kronService;

	@In(create = true, required = true)
	ConsumoService consumoService;

	@In(create = true, required = true)
	ProductoService productoService;
	
	@In(create = true, required = true)
	ProyectoService proyectoService;

	@In(create = true, required = true)
	MensajeService mensajeService;

	@In(create = true)
	SolicitudFungiblesBean solicitudFungibles;
	
	@In(create = true)
	SolicitudReservasBean solicitudReservas;
	
	@In(create=true)
	protected FacesMessages facesMessages;

	@In(value="org.jboss.seam.security.identity", required=true)
	SaiIdentity identity;

	private static final String SALTO_LINEA = "</br>";
	private static final String ABRE_NEGRITA = "<b>";
	private static final String CIERRA_NEGRITA = "</b>";
	private static final String ABRE_CORCHETE = "[";
	private static final String CIERRA_CORCHETE = "]";
	private static final String FORMATO_FECHAHORA = "dd/MM/yyyy HH:mm";
	private static final String KEY_MSJ_COMENTARIO_ADD_ERROR = "reservas.comentario.add.error";
	private static final String KEY_MSJ_CAMBIOESTADO_DETALLES = "prestaciones.cambioestado.detalles";
	private static final String KEY_MSJ_PRODUCTO_SELECCION_ERROR = "prestaciones.producto.seleccion.error";
	private static final String KEY_MSJ_PRESTACIONES_CREAR_ERROR = "prestaciones.crear.error";
	private static final String KEY_MSJ_PRESTACIONES_MODIFICAR_ERROR = "prestaciones.modificar.error";
	private static final String KEY_MSJ_PRESTACIONES_GUARDAR_ERROR_REQUIEREANEXOBIOSEG = "reservas.guardar.error.requiereanexo.bioseguridad";
	private static final String VOLVER_LISTADO = "misSolicitudes";
	private static final String EDIT_PRESTACION = "editPrestacion";
	private static final String SI = "SI";
	private static final String NO = "NO";

	@RequestParameter
	private String esPresupuesto;
	private Consumo prestacionEdit;
	private List<Servicio> listaServicios;
	private Servicio servicioPrestacion;
	private List<Usuario> listaUsuariosSolicitantes;
	private List<Usuario> listaUsuariosIr;
	private List<Usuario> listaUsuariosTecnicos;
	private List<EntidadPagadora> listaEntidades;
	private List<GrupoInvestigacion> listaGruposInvestigacion;
	private List<Producto> listaProductos;
	private List<Equipo> listaEquipos;
	private List<Proyecto> listaProyectos;
	private UploadedFile ficheroAnexoPrestacion;
	private UploadedFile ficheroAnexoBioseguridad;
	private Anexo anexoBioseguridadRellenar;
	private Anexo anexoBioseguridadCompletado;
	private String comentarioAnexoAdd;
	private Muestra muestraAdd;
	private ConsumoEquipo consumoEquipoAdd;
	private String comentarioAdd;
	private String enviaEmail;
	private boolean tecnicoPendiente;
	private Long codUsuarioTecnicoAsignado;
	private Long codProductoAntesDeEdicion;
	private BigDecimal cantidadAntesDeEdicion;
	private String tipoConsumoAddHijo;
	private boolean accesoDesdeListado;
	private String modoComentarioPresupuesto;
	
	public String getEsPresupuesto() {
		return esPresupuesto;
	}
	
	public void setEsPresupuesto( String esPresupuesto ) {
		this.esPresupuesto = esPresupuesto;
	}

	public Consumo getPrestacionEdit() {
		return prestacionEdit;
	}

	public void setPrestacionEdit( Consumo prestacionEdit ) {
		this.prestacionEdit = prestacionEdit;
	}
	
	public List<Servicio> getListaServicios() {
		return listaServicios;
	}
	
	public void setListaServicios( List<Servicio> listaServicios ) {
		this.listaServicios = listaServicios;
	}
	
	public Servicio getServicioPrestacion() {
		return servicioPrestacion;
	}
	
	public void setServicioPrestacion( Servicio servicioPrestacion ) {
		this.servicioPrestacion = servicioPrestacion;
	}
	
	public List<Usuario> getListaUsuariosSolicitantes() {
		return listaUsuariosSolicitantes;
	}
	
	public void setListaUsuariosSolicitantes( List<Usuario> listaUsuariosSolicitantes ) {
		this.listaUsuariosSolicitantes = listaUsuariosSolicitantes;
	}
	
	public List<Usuario> getListaUsuariosIr() {
		return listaUsuariosIr;
	}
	
	public void setListaUsuariosIr( List<Usuario> listaUsuariosIr ) {
		this.listaUsuariosIr = listaUsuariosIr;
	}
	
	public List<Usuario> getListaUsuariosTecnicos() {
		return listaUsuariosTecnicos;
	}
	
	public void setListaUsuariosTecnicos( List<Usuario> listaUsuariosTecnicos ) {
		this.listaUsuariosTecnicos = listaUsuariosTecnicos;
	}
	
	public List<EntidadPagadora> getListaEntidades() {
		return listaEntidades;
	}
	
	public void setListaEntidades( List<EntidadPagadora> listaEntidades ) {
		this.listaEntidades = listaEntidades;
	}
	
	public List<GrupoInvestigacion> getListaGruposInvestigacion() {
		return listaGruposInvestigacion;
	}
	
	public void setListaGruposInvestigacion( List<GrupoInvestigacion> listaGruposInvestigacion ) {
		this.listaGruposInvestigacion = listaGruposInvestigacion;
	}

	public List<Producto> getListaProductos() {
		return listaProductos;
	}
	
	public void setListaProductos( List<Producto> listaProductos ) {
		this.listaProductos = listaProductos;
	}
	
	public List<Equipo> getListaEquipos() {
		return listaEquipos;
	}
	
	public void setListaEquipos( List<Equipo> listaEquipos ) {
		this.listaEquipos = listaEquipos;
	}

	public List<Proyecto> getListaProyectos() {
		return listaProyectos;
	}
	
	public void setListaProyectos( List<Proyecto> listaProyectos ) {
		this.listaProyectos = listaProyectos;
	}

	public UploadedFile getFicheroAnexoPrestacion() {
		return ficheroAnexoPrestacion;
	}
	
	public void setFicheroAnexoPrestacion( UploadedFile ficheroAnexoPrestacion ) {
		this.ficheroAnexoPrestacion = ficheroAnexoPrestacion;
	}
	
	public UploadedFile getFicheroAnexoBioseguridad() {
		return ficheroAnexoBioseguridad;
	}
	
	public void setFicheroAnexoBioseguridad( UploadedFile ficheroAnexoBioseguridad ) {
		this.ficheroAnexoBioseguridad = ficheroAnexoBioseguridad;
	}
	
	public Anexo getAnexoBioseguridadRellenar() {
		return anexoBioseguridadRellenar;
	}
	
	public void setAnexoBioseguridadRellenar( Anexo anexoBioseguridadRellenar ) {
		this.anexoBioseguridadRellenar = anexoBioseguridadRellenar;
	}

	public Anexo getAnexoBioseguridadCompletado() {
		return anexoBioseguridadCompletado;
	}
	
	public void setAnexoBioseguridadCompletado( Anexo anexoBioseguridadCompletado ) {
		this.anexoBioseguridadCompletado = anexoBioseguridadCompletado;
	}

	public String getComentarioAnexoAdd() {
		return comentarioAnexoAdd;
	}
	
	public void setComentarioAnexoAdd( String comentarioAnexoAdd ) {
		this.comentarioAnexoAdd = comentarioAnexoAdd;
	}

	public Muestra getMuestraAdd() {
		return muestraAdd;
	}
	
	public void setMuestraAdd( Muestra muestraAdd ) {
		this.muestraAdd = muestraAdd;
	}

	public ConsumoEquipo getConsumoEquipoAdd() {
		return consumoEquipoAdd;
	}

	public void setConsumoEquipoAdd( ConsumoEquipo consumoEquipoAdd ) {
		this.consumoEquipoAdd = consumoEquipoAdd;
	}

	public String getComentarioAdd() {
		return comentarioAdd;
	}
	
	public void setComentarioAdd( String comentarioAdd ) {
		this.comentarioAdd = comentarioAdd;
	}

	public String getEnviaEmail() {
		return enviaEmail;
	}
	
	public void setEnviaEmail( String enviaEmail ) {
		this.enviaEmail = enviaEmail;
	}
	
	public boolean isTecnicoPendiente() {
		return tecnicoPendiente;
	}
	
	public void setTecnicoPendiente( boolean tecnicoPendiente ) {
		this.tecnicoPendiente = tecnicoPendiente;
	}

	public Long getCodUsuarioTecnicoAsignado() {
		return codUsuarioTecnicoAsignado;
	}
	
	public void setCodUsuarioTecnicoAsignado( Long codUsuarioTecnicoAsignado ) {
		this.codUsuarioTecnicoAsignado = codUsuarioTecnicoAsignado;
	}

	public Long getCodProductoAntesDeEdicion() {
		return codProductoAntesDeEdicion;
	}
	
	public void setCodProductoAntesDeEdicion( Long codProductoAntesDeEdicion ) {
		this.codProductoAntesDeEdicion = codProductoAntesDeEdicion;
	}

	public BigDecimal getCantidadAntesDeEdicion() {
		return cantidadAntesDeEdicion;
	}
	
	public void setCantidadAntesDeEdicion( BigDecimal cantidadAntesDeEdicion ) {
		this.cantidadAntesDeEdicion = cantidadAntesDeEdicion;
	}
	
	public String getTipoConsumoAddHijo() {
		return tipoConsumoAddHijo;
	}
	
	public void setTipoConsumoAddHijo( String tipoConsumoAddHijo ) {
		this.tipoConsumoAddHijo = tipoConsumoAddHijo;
	}

	public boolean isAccesoDesdeListado() {
		return accesoDesdeListado;
	}
	
	public void setAccesoDesdeListado( boolean accesoDesdeListado ) {
		this.accesoDesdeListado = accesoDesdeListado;
	}

	public String getModoComentarioPresupuesto() {
		return modoComentarioPresupuesto;
	}
	
	public void setModoComentarioPresupuesto( String modoComentarioPresupuesto ) {
		this.modoComentarioPresupuesto = modoComentarioPresupuesto;
	}

	
	@Create
	public void inicializar()
	{
		this.listaServicios = servicioService.getListaServiciosPuedoHacerSolicitud();
		this.servicioPrestacion = null;
		this.prestacionEdit = new Consumo();
		this.prestacionEdit.setTipo("P");
		this.prestacionEdit.setCantidad(BigDecimal.valueOf(1));
		if ("SI".equals(this.esPresupuesto))
		{
			this.prestacionEdit.setPresupuesto("SI" );
			this.prestacionEdit.setInterno(NO);
		}
		else
		{
			this.prestacionEdit.setPresupuesto(NO);
		}
		this.enviaEmail = NO;
		this.comentarioAdd = null;
		this.accesoDesdeListado = false;
		if (this.listaServicios.size()==1)
		{
			this.servicioPrestacion = this.listaServicios.get(0);
			this.seleccionadoServicio();
		}
	}

	private void inicializacionPrestacionEdit(Consumo consumo)
	{
		this.prestacionEdit = consumo;
		this.ficheroAnexoBioseguridad = null;
		this.anexoBioseguridadRellenar = null;
		this.anexoBioseguridadCompletado = null;
		this.listaUsuariosTecnicos = usuarioService.getTecnicosByProducto( prestacionEdit.getProducto());
		if (this.prestacionEdit.getUsuarioTecnicoAsignado() == null)
		{
			this.tecnicoPendiente = true;
		}
		else
		{
			this.tecnicoPendiente = false;
			this.codUsuarioTecnicoAsignado = prestacionEdit.getUsuarioTecnicoAsignado().getCod();
		}
		this.codProductoAntesDeEdicion = this.prestacionEdit.getProducto().getCod();
		this.cantidadAntesDeEdicion = this.prestacionEdit.getCantidad();
		this.servicioPrestacion = this.prestacionEdit.getProducto().getServicio();
		if (SI.equals(this.prestacionEdit.getPresupuesto()))
		{
			this.listaEntidades = tarifaService.getEntidadesByIr(prestacionEdit.getUsuarioIrAsociado());
		}
		final boolean soloObtenerProductosNoRequierenProyecto = "SI".equals(prestacionEdit.getInterno());
		final boolean soloObtenerProductosAdmitenPresupuesto = "SI".equals(prestacionEdit.getPresupuesto()) && prestacionEdit.getConsumoPadre()==null;
		this.listaProductos = productoService.getProductosByServicioTipo(servicioPrestacion, "P", soloObtenerProductosNoRequierenProyecto, soloObtenerProductosAdmitenPresupuesto);
		if (SI.equals(prestacionEdit.getProducto().getRequiereProyecto()) && ( prestacionEdit.getUsuarioIrAsociado() != null ))
		{
			this.listaProyectos = proyectoService.getListaProyectosByProductoIr(prestacionEdit.getProducto(), prestacionEdit.getUsuarioIrAsociado());
		}
		if (consumoService.requiereAnexoNivelBioseguridad(prestacionEdit))
		{
			this.anexoBioseguridadRellenar = consumoService.getAnexoGeneralBioseguridad();
			this.anexoBioseguridadCompletado = consumoService.getAnexoBioseguridadByConsumo(prestacionEdit);
		}
		this.muestraAdd = new Muestra();
		this.consumoEquipoAdd = new ConsumoEquipo();
		this.listaEquipos = productoService.getEquiposByProducto(this.prestacionEdit.getProducto());
	}
	
	public String establecerPrestacionEdit(Consumo consumo)
	{
		inicializacionPrestacionEdit(consumo);
		this.accesoDesdeListado = true;
		return EDIT_PRESTACION;
	}
	
	public String establecerPrestacionEditFromConsumoPadre(Consumo consumo)
	{
		consumoService.recargarConsumo(consumo);
		inicializacionPrestacionEdit(consumo);
		this.accesoDesdeListado = false;
		return EDIT_PRESTACION;
	}

	public String establecerPrestacionCreateFromConsumoPadre(Consumo consumoPadre)
	{
		inicializar();
		this.servicioPrestacion = consumoPadre.getProducto().getServicio();
		this.prestacionEdit.setConsumoPadre(consumoPadre);
		this.prestacionEdit.setPresupuesto(consumoPadre.getPresupuesto());
		this.seleccionadoServicio();
		return EDIT_PRESTACION;
	}
	
	public void seleccionadoServicio() 
	{
		if (this.servicioPrestacion != null)
		{
			if (identity.tienePerfilEnServicio(TipoPerfil.TECNICO, servicioPrestacion.getCod()) || identity.tienePerfilEnServicio( TipoPerfil.SUPERVISOR, servicioPrestacion.getCod() ))
			{
				this.listaUsuariosSolicitantes=usuarioService.getUsuariosPuedoSolicitarEnSuNombreByServicio(servicioPrestacion);
				this.prestacionEdit.setInterno(SI);
			}
			else
			{
				this.prestacionEdit.setInterno(NO);
				this.listaUsuariosIr = usuarioService.getListaIrsPuedoAsignarEnSolicitud( identity.getUsuarioConectado(), servicioPrestacion);
				if (this.listaUsuariosIr.size() == 1)
				{
					this.prestacionEdit.setUsuarioIrAsociado(this.listaUsuariosIr.get(0));
				}
			}
			final boolean soloObtenerProductosNoRequierenProyecto = "SI".equals(prestacionEdit.getInterno());
			final boolean soloObtenerProductosAdmitenPresupuesto = "SI".equals(prestacionEdit.getPresupuesto()) && prestacionEdit.getConsumoPadre()==null;
			this.listaProductos = productoService.getProductosByServicioTipo(servicioPrestacion, "P", soloObtenerProductosNoRequierenProyecto, soloObtenerProductosAdmitenPresupuesto);
			//Si el servicio no tiene prestaciones, notificamos al usuario
			if (( listaProductos == null ) || listaProductos.isEmpty()) 
			{
				facesMessages.add(StatusMessage.Severity.WARN, 
								  this.getKeyMsj("prestaciones.servicio.seleccion.error"), 
								  this.getKeyMsj("prestaciones.servicio.seleccion.error.sinproductos"), 
								  null, 
								  null, 
								  servicioPrestacion.getNombre());
			}
		}
		else
		{
			this.prestacionEdit.setInterno(SI);
		}
		this.prestacionEdit.setUsuarioSolicitante(identity.getUsuarioConectado());
		// Si se trata de un presupuesto, nunca puede ser consumo interno
		if ("SI".equals(this.prestacionEdit.getPresupuesto()))
		{
			this.prestacionEdit.setInterno(NO);
			this.seleccionadoUsuarioSolicitante();
		}
		else
		{
			if (this.listaUsuariosIr==null || (this.prestacionEdit.getUsuarioIrAsociado()!=null && !this.listaUsuariosIr.contains(this.prestacionEdit.getUsuarioIrAsociado())))
			{
				this.prestacionEdit.setUsuarioIrAsociado(null);
				this.seleccionadoUsuarioIr();
			}
			this.prestacionEdit.setEntidadPagadora(null);
			if (this.prestacionEdit.getUsuarioIrAsociado()!=null)
			{
				this.seleccionadoUsuarioIr();
			}
		}
		this.prestacionEdit.setProducto(null);
	}

	public void seleccionadoConsumoInterno() 
	{
		if (NO.equals(this.prestacionEdit.getInterno())) 
		{
			if (this.servicioPrestacion != null) 
			{
				this.listaUsuariosSolicitantes = usuarioService.getUsuariosByServicio(servicioPrestacion);
				if (this.prestacionEdit.getUsuarioSolicitante() != null && this.listaUsuariosSolicitantes.contains(this.prestacionEdit.getUsuarioSolicitante()))
				{
					this.seleccionadoUsuarioSolicitante();
				}
			}
			this.listaProductos = productoService.getProductosByServicioTipo(servicioPrestacion, "P", false, false);
		}
		else 
		{
			this.prestacionEdit.setUsuarioSolicitante(identity.getUsuarioConectado());
			this.prestacionEdit.setUsuarioIrAsociado(null);
			this.prestacionEdit.setEntidadPagadora(null);
			if (this.prestacionEdit.getProducto() != null)
			{
				// Si ya había producto seleccionado y cambiamos el solicitante tenemos que comprobar si tiene la certificación necesaria
				seleccionadoUsuarioSolicitante();
			}
			this.listaProductos = productoService.getProductosByServicioTipo(servicioPrestacion, "P", true, false);
		}
	}

	
	public void seleccionadoUsuarioSolicitante() 
	{
		if (this.prestacionEdit.getUsuarioSolicitante()!= null)  
		{
			this.listaUsuariosIr = usuarioService.getListaIrsPuedoAsignarEnSolicitud(prestacionEdit.getUsuarioSolicitante(),servicioPrestacion);
			if (this.listaUsuariosIr.size() == 1)
			{
				this.prestacionEdit.setUsuarioIrAsociado(this.listaUsuariosIr.get(0));
			}
			if (this.prestacionEdit.getProducto() != null)
			{
				// Si ya había producto seleccionado y cambiamos el solicitante tenemos que comprobar si tiene la certificación necesaria
				seleccionadoProducto();
			}
		}
		if (this.listaUsuariosIr==null || (this.prestacionEdit.getUsuarioIrAsociado()!=null && !this.listaUsuariosIr.contains(this.prestacionEdit.getUsuarioIrAsociado())))
		{
			this.prestacionEdit.setUsuarioIrAsociado(null);
			this.seleccionadoUsuarioIr();
		}
		this.prestacionEdit.setEntidadPagadora(null);
		if (this.prestacionEdit.getUsuarioIrAsociado()!=null)
		{
			this.seleccionadoUsuarioIr();
		}
	}
	
	public void seleccionadoUsuarioIr() 
	{
		if (this.prestacionEdit.getUsuarioIrAsociado() != null) 
		{
			this.listaEntidades = tarifaService.getEntidadesByIr(prestacionEdit.getUsuarioIrAsociado());
			if (this.listaEntidades.size()==1)
			{
				this.prestacionEdit.setEntidadPagadora(this.listaEntidades.get(0));
				this.seleccionadaEntidadPagadora();
			}
			if (this.prestacionEdit.getProducto() != null)
			{
				seleccionadoProducto();
			}
		}
		if (this.listaEntidades==null || (this.prestacionEdit.getEntidadPagadora() != null && !this.listaEntidades.contains(this.prestacionEdit.getEntidadPagadora())))
		{
			this.prestacionEdit.setEntidadPagadora(null);
			this.seleccionadaEntidadPagadora();
		}
	}
	
	public void seleccionadaEntidadPagadora()
	{
		if (this.prestacionEdit.getEntidadPagadora() != null)
		{
			this.listaGruposInvestigacion = usuarioService.getGruposInvestigacionByUsuarioEntidadPagadora(this.prestacionEdit.getUsuarioIrAsociado(), this.prestacionEdit.getEntidadPagadora());
			if (this.listaGruposInvestigacion.size()==1)
			{
				this.prestacionEdit.setGrupoInvestigacion(this.listaGruposInvestigacion.get(0));
			}
		}
		else
		{
			this.listaGruposInvestigacion = new ArrayList<>();
			this.prestacionEdit.setGrupoInvestigacion(null);
		}
	}
	
	public void seleccionadoProducto()
	{
		ficheroAnexoPrestacion = null;
		prestacionEdit.setNivelBioseguridad(null);
		ficheroAnexoBioseguridad = null;
		prestacionEdit.setObservaciones(null);
		
		if (prestacionEdit.getProducto() != null)
		{
			// Comprobamos si requiere proyecto
			if (SI.equals(prestacionEdit.getProducto().getRequiereProyecto()))
			{
				if (prestacionEdit.getUsuarioIrAsociado() != null)
				{
					listaProyectos = proyectoService.getListaProyectosByProductoIr(prestacionEdit.getProducto(), prestacionEdit.getUsuarioIrAsociado());
					if (listaProyectos.isEmpty())
					{
						facesMessages.add(StatusMessage.Severity.WARN, KEY_MSJ_PRODUCTO_SELECCION_ERROR, "prestaciones.producto.seleccion.error.sinproyecto", null, null, prestacionEdit.getProducto().getDescripcion(), prestacionEdit.getUsuarioIrAsociado().getDatosUsuario().getNombreCompleto());
						prestacionEdit.setProducto(null);
						return;
					}
					else if (listaProyectos.size() == 1)
					{
						prestacionEdit.setProyecto(listaProyectos.get(0));
					}
				}
				else
				{
					facesMessages.add(StatusMessage.Severity.WARN, KEY_MSJ_PRODUCTO_SELECCION_ERROR, "prestaciones.producto.seleccion.error.sinproyecto.sinir", null, null, prestacionEdit.getProducto().getDescripcion());
					prestacionEdit.setProducto(null);
					return;
				}
			}
			else
			{
				prestacionEdit.setProyecto(null);
			}
			// Comprobamos si requiere alguna certificación de la que no dispone
			final Usuario solicitante = consumoService.obtenerSolicitante(prestacionEdit);
			final TipoCertificacion tc = consumoService.obtenerCertificacionNecesaria(solicitante, prestacionEdit.getProducto());
			if (tc != null)
			{
				facesMessages.add(StatusMessage.Severity.WARN, KEY_MSJ_PRODUCTO_SELECCION_ERROR, "prestaciones.producto.seleccion.error.certificacion", null, null, tc.getNombre(), solicitante.getDatosUsuario().getNombreCompleto());
				prestacionEdit.setProducto(null);
				return;
			}
			if (prestacionEdit.getCod() != null)
			{
				// Se ha cambiado de producto en modo edición
				listaUsuariosTecnicos = usuarioService.getTecnicosByProducto(prestacionEdit.getProducto());
				if (prestacionEdit.getUsuarioTecnicoAsignado() == null)
				{
					tecnicoPendiente = true;
				}
				else
				{
					if (listaUsuariosTecnicos.contains(prestacionEdit.getUsuarioTecnicoAsignado()))
					{
						tecnicoPendiente = false;
						codUsuarioTecnicoAsignado = prestacionEdit.getUsuarioTecnicoAsignado().getCod();
					}
					else
					{
						prestacionEdit.setUsuarioTecnicoAsignado(null);
						codUsuarioTecnicoAsignado = null;
						tecnicoPendiente = true;
					}
				}
			}
			this.listaEquipos = productoService.getEquiposByProducto(this.prestacionEdit.getProducto());
		}
	}
	
	public void seleccionadoNivelBioseguridad()
	{
		if (( prestacionEdit.getNivelBioseguridad() == null ) || NO.equals(prestacionEdit.getNivelBioseguridad()))
		{
			ficheroAnexoBioseguridad = null;
			if (prestacionEdit.getCod() == null)
			{
				// Si estamos en modo edición no borramos todavía
				anexoBioseguridadCompletado = null;
			}
		}
		else
		{
			anexoBioseguridadRellenar = consumoService.getAnexoGeneralBioseguridad();
		}
	}
	
	public void subidoFicheroAnexoBioseguridad( FileUploadEvent event ) 
	{
		ficheroAnexoBioseguridad = event.getFile();
	}

	public void subidoFicheroAnexo( FileUploadEvent event ) 
	{
		ficheroAnexoPrestacion = event.getFile();
	}
	
	public List<Anexo> getAnexosPrestacion() 
	{
		return consumoService.getAnexosConsumoByConsumo(prestacionEdit);
	}
	
	public StreamedContent descargarFicheroAnexo(Anexo anexo) 
	{
		if (anexo.getDocumento() != null) 
		{
			final InputStream stream = new ByteArrayInputStream(anexo.getDocumento());
			return new DefaultStreamedContent(stream, null, anexo.getNomDocumento());
		}
		else 
		{
			facesMessages.add(StatusMessage.Severity.WARN, "prestacion.no.anexo", null, null, anexo.getNomDocumento());
			return null;
		}
	}

	public void guardarAnexo()
	{
		// Persistimos el anexo directamente en BBDD
		final Anexo anexoAdd = new Anexo();
		anexoAdd.setNomDocumento(ficheroAnexoPrestacion.getFileName());
		anexoAdd.setDocumento(ficheroAnexoPrestacion.getContents());
		anexoAdd.setConsumo(prestacionEdit);
		anexoAdd.setComentario(comentarioAnexoAdd);
		anexoAdd.setTipo("CONSUMO");
		try
		{
			consumoService.guardarAnexo(anexoAdd);
		}
		catch (final Exception ex)
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "prestaciones.anexo.add.error", null, null, ex.getMessage());
			return;
		}
		prestacionEdit.getAnexos().add(anexoAdd);
		ficheroAnexoPrestacion = null;
		comentarioAnexoAdd = null;
		facesMessages.add(StatusMessage.Severity.INFO, "prestaciones.anexo.add.ok", null, null, anexoAdd.getNomDocumento());
		
	}

	public void eliminarAnexoBioseguridadCompletado()
	{
		// Eliminamos el anexo directamente en BBDD
		try 
		{
			consumoService.eliminarAnexo(anexoBioseguridadCompletado);
		}
		catch (final Exception ex)
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "prestaciones.anexo.del.error", null, null, ex.getMessage());
			return;
		}
		facesMessages.add(StatusMessage.Severity.INFO, "prestaciones.anexo.del.ok", null, null, anexoBioseguridadCompletado.getNomDocumento());
		anexoBioseguridadCompletado = null;
	}
	
	public void eliminarAnexo(Anexo anexo ) 
	{
		// Eliminamos el anexo directamente en BBDD
		try 
		{
			consumoService.eliminarAnexo(anexo);
		}
		catch (final Exception ex)
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "prestaciones.anexo.del.error", null, null, ex.getMessage());
			return;
		}
		facesMessages.add(StatusMessage.Severity.INFO, "prestaciones.anexo.del.ok", null, null, anexo.getNomDocumento());
	}

	
	public void guardarMuestra() 
	{
		// Persistimos la muestra directamente en BBDD
		muestraAdd.setConsumo(prestacionEdit);
		muestraAdd.setFechaEntrega(new Date());
		try 
		{
			consumoService.guardarMuestra(muestraAdd);
		}
		catch (final Exception ex)
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "prestaciones.muestra.add.error", null, null, ex.getMessage());
			return;
		}
		prestacionEdit.getMuestras().add(muestraAdd);
		facesMessages.add(StatusMessage.Severity.INFO, "prestaciones.muestra.add.ok", null, null, muestraAdd.getReferencia());
		muestraAdd = new Muestra();
	}
	
	public void eliminarMuestra(Muestra muestra) 
	{
		// Eliminamos la muestra directamente en BBDD
		try 
		{
			consumoService.eliminarMuestra(muestra);
		}
		catch (final Exception ex)
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "prestaciones.muestra.del.error", null, null, ex.getMessage());
			return;
		}			
		prestacionEdit.getMuestras().remove(muestra);
		facesMessages.add(StatusMessage.Severity.INFO, "prestaciones.muestra.del.ok", null, null, muestra.getReferencia());
	}


	public void guardarConsumoEquipo() 
	{
		// Persistimos ConsumoEquipo directamente en BBDD
		consumoEquipoAdd.setConsumo(prestacionEdit);
		try 
		{
			consumoService.crearConsumoEquipo(consumoEquipoAdd);
		}
		catch (final Exception ex)
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "prestaciones.equipo.add.error", null, null, ex.getMessage());
			return;
		}
		prestacionEdit.getListaEquipos().add(consumoEquipoAdd);
		facesMessages.add(StatusMessage.Severity.INFO, "prestaciones.equipo.add.ok", null, null, consumoEquipoAdd.getEquipo().getDescripcion());
		consumoEquipoAdd = new ConsumoEquipo();
	}
	
	public void eliminarConsumoEquipo(ConsumoEquipo ce) 
	{
		// Eliminamos ConsumoEquipo directamente en BBDD
		try 
		{
			consumoService.eliminarConsumoEquipo(ce);
		}
		catch (final Exception ex)
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "prestaciones.equipo.del.error", null, null, ex.getMessage());
			return;
		}			
		prestacionEdit.getListaEquipos().remove(ce);
		facesMessages.add(StatusMessage.Severity.INFO, "prestaciones.equipo.del.ok", null, null, ce.getEquipo().getDescripcion());
	}

	
	public void guardarBitacoraMail() 
	{
		guardarBitacoraUsuario();
		mensajeService.enviarUsuarioComentarioSolicitud(prestacionEdit);
	}

	public void guardarBitacoraUsuario() 
	{
		try 
		{
			final StringBuilder comentario = new StringBuilder("");
			if (prestacionEdit.getBitacoraUsuario() != null)
			{
				comentario.append(prestacionEdit.getBitacoraUsuario());
			}
			comentario.append(ABRE_NEGRITA).append(ABRE_CORCHETE).append(UtilDate.dateToString(new Date(), FORMATO_FECHAHORA)).append(CIERRA_CORCHETE).append(" ").append(identity.getUsuarioConectado().getDatosUsuario().getEmail()).append(": ").append(CIERRA_NEGRITA)
					  .append(comentarioAdd).append(SALTO_LINEA);
			if (comentario.length()>4000)
			{
				facesMessages.add(StatusMessage.Severity.WARN, KEY_MSJ_COMENTARIO_ADD_ERROR, "reservas.comentario.add.error.bitacorallena", null, null);
				return;
			}
			prestacionEdit.setBitacoraUsuario(comentario.toString());
			consumoService.guardarConsumo(prestacionEdit);
			if (identity.getUsuarioConectado().equals(prestacionEdit.getUsuarioSolicitante()) || 
				identity.getUsuarioConectado().equals(prestacionEdit.getUsuarioIrAsociado()))
			{
				mensajeService.enviarTecnicoOSupervisorComentarioSolicitud(prestacionEdit);
			}
			facesMessages.add(StatusMessage.Severity.INFO, "reservas.comentario.add.ok", null, null, comentarioAdd);
			comentarioAdd = null;
		} 
		catch ( final Exception ex ) 
		{
			facesMessages.add(StatusMessage.Severity.ERROR, KEY_MSJ_COMENTARIO_ADD_ERROR, null, null, ex.getMessage());
		}
	}

	public void guardarBitacora() 
	{
		try 
		{
			final StringBuilder comentario = new StringBuilder("");
			if (prestacionEdit.getBitacora() != null)
			{
				comentario.append(prestacionEdit.getBitacora());
			}
			comentario.append(ABRE_NEGRITA).append(ABRE_CORCHETE).append(UtilDate.dateToString(new Date(), FORMATO_FECHAHORA)).append(CIERRA_CORCHETE).append(" ").append(identity.getUsuarioConectado().getDatosUsuario().getEmail()).append(": ")
					  .append(CIERRA_NEGRITA).append(comentarioAdd).append(SALTO_LINEA);
			if (comentario.length()>4000)
			{
				facesMessages.add(StatusMessage.Severity.WARN, KEY_MSJ_COMENTARIO_ADD_ERROR, "reservas.comentario.add.error.bitacorallena", null, null);
				return;
			}
			prestacionEdit.setBitacora(comentario.toString());
			consumoService.guardarConsumo(prestacionEdit);
			facesMessages.add(StatusMessage.Severity.INFO, "reservas.comentario.add.ok", null, null, comentarioAdd);
			comentarioAdd = null;
		} 
		catch ( final Exception ex ) 
		{
			facesMessages.add(StatusMessage.Severity.ERROR, KEY_MSJ_COMENTARIO_ADD_ERROR, null, null, ex.getMessage() );
		}
	}

	private String getEstadoCrearPedidoYEstablecerTecnico()
	{
		if (SI.equals(prestacionEdit.getProducto().getRequiereValidacionIr()) && !identity.getUsuarioConectado().equals(prestacionEdit.getUsuarioIrAsociado()) && !identity.tienePerfilEnServicio(TipoPerfil.SUPERVISOR, servicioPrestacion.getCod()) && !identity.tienePerfilEnServicio(TipoPerfil.TECNICO, servicioPrestacion.getCod()))
		{
			return EstadoConsumo.PEND_VALIDACION_IR;
		}
		final Usuario usuarioTecnicoAsignar = consumoService.obtenerTecnicoAsignarPrestacion(prestacionEdit);
		prestacionEdit.setUsuarioTecnicoAsignado(usuarioTecnicoAsignar);
		// Establecemos la prestación a Pendiente si requiere validación o si no se asigna técnico automáticamente
		if (SI.equals(prestacionEdit.getProducto().getRequiereValidacion()) || ( usuarioTecnicoAsignar == null ))
		{
			return EstadoConsumo.PENDIENTE;
		}
		else
		{
			return EstadoConsumo.ACEPTADO;
		}
	}
	
	private boolean pasaComprobacionesCreacion()
	{
		if (SI.equals(prestacionEdit.getProducto().getRequiereAnexo()) && (ficheroAnexoPrestacion == null))
		{
			facesMessages.add(StatusMessage.Severity.WARN, this.getKeyMsj(KEY_MSJ_PRESTACIONES_CREAR_ERROR), "reservas.guardar.error.requiereanexo", null, null);
			return false;
		}
		if (consumoService.requiereAnexoNivelBioseguridad(prestacionEdit) && (ficheroAnexoBioseguridad == null))
		{
			facesMessages.add(StatusMessage.Severity.WARN, this.getKeyMsj(KEY_MSJ_PRESTACIONES_CREAR_ERROR), KEY_MSJ_PRESTACIONES_GUARDAR_ERROR_REQUIEREANEXOBIOSEG, null, null);
			return false;
		}
		if (SI.equals(prestacionEdit.getProducto().getRequiereProyecto()))
		{
			final BigDecimal cantidadDisponibleProyectoProducto = proyectoService.getCantidadDisponibleByProyectoProducto(prestacionEdit.getProyecto(), prestacionEdit.getProducto());
			if (prestacionEdit.getCantidad().compareTo(cantidadDisponibleProyectoProducto) > 0)
			{
				facesMessages.add(StatusMessage.Severity.ERROR,this.getKeyMsj(KEY_MSJ_PRESTACIONES_CREAR_ERROR), "prestaciones.guardar.error.stock.proyecto", null, null, prestacionEdit.getProducto().getDescripcion(), prestacionEdit.getProyecto().getNombre(), formatCantidad(cantidadDisponibleProyectoProducto));
				return false;
			}
		}
		return true;
	}
	
	public String crearSolicitud()
	{
		if (!pasaComprobacionesCreacion())
		{
			return null;
		}
		prestacionEdit.setFechaSolicitud(new Date());
		prestacionEdit.setEstado(getEstadoCrearPedidoYEstablecerTecnico());
		prestacionEdit.setUsuarioConectado(identity.getUsuarioConectado());
		if (comentarioAdd!=null && !comentarioAdd.isEmpty())
		{
			prestacionEdit.setBitacoraUsuario(new StringBuilder(ABRE_NEGRITA).append(ABRE_CORCHETE).append(UtilDate.dateToString(new Date(), FORMATO_FECHAHORA)).append(CIERRA_CORCHETE).append(" ")
													.append(identity.getUsuarioConectado().getDatosUsuario().getEmail()).append(": ").append(CIERRA_NEGRITA)
													.append(comentarioAdd).append(SALTO_LINEA).toString());
		}
		if (prestacionEdit.getConsumoPadre() != null)
		{
			prestacionEdit.setEstado(EstadoConsumo.ACEPTADO);
			prestacionEdit.setInterno(prestacionEdit.getConsumoPadre().getInterno());
			prestacionEdit.setUsuarioSolicitante(prestacionEdit.getConsumoPadre().getUsuarioSolicitante());
			prestacionEdit.setUsuarioConectado(prestacionEdit.getConsumoPadre().getUsuarioConectado());
			prestacionEdit.setUsuarioIrAsociado(prestacionEdit.getConsumoPadre().getUsuarioIrAsociado());
			prestacionEdit.setUsuarioTecnicoAsignado(prestacionEdit.getConsumoPadre().getUsuarioTecnicoAsignado());
			prestacionEdit.setEntidadPagadora(prestacionEdit.getConsumoPadre().getEntidadPagadora());
		}
		if (SI.equals(prestacionEdit.getPresupuesto()))
		{
			prestacionEdit.setEstadoPresupuesto(EstadoPresupuesto.SOLICITADO);
		}
		try 
		{
			consumoService.crearPedidoPrestacion(prestacionEdit, ficheroAnexoPrestacion, ficheroAnexoBioseguridad);
		} 
		catch ( final SaiException ex ) 
		{
			log.error("Error al crear consumo de tipo prestacion: ", ex);
			facesMessages.add(StatusMessage.Severity.ERROR, this.getKeyMsj(KEY_MSJ_PRESTACIONES_CREAR_ERROR), null, null, ex.getMessage());
			return null;
		}
		facesMessages.add(StatusMessage.Severity.INFO, this.getKeyMsj("prestaciones.crear.ok"), "prestaciones.guardar.ok.detalles", null, null, prestacionEdit.getProducto().getDescripcion(), prestacionEdit.getCantidad().toString());
		consumoService.recargarConsumo(prestacionEdit);
		if (prestacionEdit.getConsumoPadre() != null)
		{
			consumoService.recargarConsumo(prestacionEdit.getConsumoPadre());
			return establecerPrestacionEdit(prestacionEdit.getConsumoPadre());
		}
		else
		{
			mensajeService.notificacionesNuevaSolicitud(prestacionEdit, enviaEmail);
		}
		return establecerPrestacionEdit(prestacionEdit);
	}

	private boolean pasaComprobacionesModificacion()
	{
		if (consumoService.requiereAnexoNivelBioseguridad(prestacionEdit) && ficheroAnexoBioseguridad == null && anexoBioseguridadCompletado == null)
		{
			facesMessages.add(StatusMessage.Severity.WARN, this.getKeyMsj(KEY_MSJ_PRESTACIONES_MODIFICAR_ERROR), KEY_MSJ_PRESTACIONES_GUARDAR_ERROR_REQUIEREANEXOBIOSEG, null, null);
			return false;
		}
		if (SI.equals(prestacionEdit.getProducto().getRequiereProyecto()))
		{
			
			BigDecimal cantidadDisponibleProyectoProducto = null;
			if (!codProductoAntesDeEdicion.equals(prestacionEdit.getProducto().getCod()))
			{
				// Se ha cambiado el producto
				cantidadDisponibleProyectoProducto = proyectoService.getCantidadDisponibleByProyectoProducto(prestacionEdit.getProyecto(), prestacionEdit.getProducto());
				if (prestacionEdit.getCantidad().compareTo(cantidadDisponibleProyectoProducto) > 0)
				{
					facesMessages.add(StatusMessage.Severity.ERROR, this.getKeyMsj(KEY_MSJ_PRESTACIONES_MODIFICAR_ERROR), "prestaciones.guardar.error.stock.proyecto", null, null, prestacionEdit.getProducto().getDescripcion(), prestacionEdit.getProyecto().getNombre(), formatCantidad(cantidadDisponibleProyectoProducto));
					return false;
				}
			}
			else if (cantidadAntesDeEdicion.compareTo(prestacionEdit.getCantidad())<0)
	        {
				// No se ha cambiado el producto pero ha aumentado la cantidad
				final BigDecimal aumentoCantidad = prestacionEdit.getCantidad().subtract(cantidadAntesDeEdicion);
				cantidadDisponibleProyectoProducto = proyectoService.getCantidadDisponibleByProyectoProducto(prestacionEdit.getProyecto(), prestacionEdit.getProducto());
				if (aumentoCantidad.compareTo(cantidadDisponibleProyectoProducto) > 0)
				{
					facesMessages.add(StatusMessage.Severity.ERROR, this.getKeyMsj(KEY_MSJ_PRESTACIONES_MODIFICAR_ERROR), "prestaciones.guardar.error.stock.proyecto.aumento", null, null, prestacionEdit.getProducto().getDescripcion(), prestacionEdit.getProyecto().getNombre(), formatCantidad(cantidadDisponibleProyectoProducto), formatCantidad(aumentoCantidad));
					return false;
				}
	        }
		}
		return true;
	}
	
	public String modificarSolicitud()
	{
		if (!pasaComprobacionesModificacion())
		{
			return null;
		}
		// Comprobamos asignación de técnico
		boolean nuevoTecnicoEstablecido = false;
		if (prestacionEdit.getConsumoPadre() == null &&
			prestacionEdit.getUsuarioTecnicoAsignado() != null &&
			(tecnicoPendiente || !prestacionEdit.getUsuarioTecnicoAsignado().getCod().equals(codUsuarioTecnicoAsignado)))
		{
			// Se acaba de establecer un técnico o se ha modificado el que había
			nuevoTecnicoEstablecido = true;
			// Modificamos el estado del consumo si es necesario en función de la selección de técnico
			if (prestacionEdit.getEstado().equals(EstadoConsumo.PENDIENTE) && ( prestacionEdit.getUsuarioTecnicoAsignado()!=null ))
			{
				// Se ha establecido un técnico
				prestacionEdit.setEstado(EstadoConsumo.ACEPTADO);
			}
		}
		else if (( prestacionEdit.getUsuarioTecnicoAsignado() != null ) && operacionAceptar())
		{
			// El técnico se asignó directamente en la creación de solicitud y el usuario conectado tiene permiso para aceptar. Se debe aceptar automáticamente al guardar
			prestacionEdit.setEstado(EstadoConsumo.ACEPTADO);
		}
		try 
		{
			consumoService.modificarPedidoPrestacion(prestacionEdit, ficheroAnexoBioseguridad);
		} 
		catch ( final SaiException ex ) 
		{
			log.error("Error al modificar consumo de tipo prestacion: ", ex);
			facesMessages.add(StatusMessage.Severity.ERROR, this.getKeyMsj(KEY_MSJ_PRESTACIONES_MODIFICAR_ERROR), null, null, ex.getMessage());
			return null;
		}
		mensajeService.notificacionesModificacionSolicitud(prestacionEdit, false, false, nuevoTecnicoEstablecido, codUsuarioTecnicoAsignado);
		facesMessages.add(StatusMessage.Severity.INFO, this.getKeyMsj("prestaciones.modificar.ok"), "prestaciones.guardar.ok.detalles", null, null, prestacionEdit.getProducto().getDescripcion(), prestacionEdit.getCantidad().toString());
		if (prestacionEdit.getConsumoPadre() != null)
		{
			consumoService.recargarConsumo(prestacionEdit.getConsumoPadre());
			return establecerPrestacionEdit(prestacionEdit.getConsumoPadre());
		}
		else
		{
			return establecerPrestacionEdit(prestacionEdit);
		}
	}

	public void validarIr() 
	{
		if (consumoService.requiereAnexoNivelBioseguridad(prestacionEdit) && ficheroAnexoBioseguridad == null && anexoBioseguridadCompletado == null)
		{
			facesMessages.add(StatusMessage.Severity.WARN, KEY_MSJ_PRESTACIONES_MODIFICAR_ERROR, KEY_MSJ_PRESTACIONES_GUARDAR_ERROR_REQUIEREANEXOBIOSEG, null, null);
			return;
		}
		final Usuario usuarioTecnicoAsignar = consumoService.obtenerTecnicoAsignarPrestacion(prestacionEdit);
		prestacionEdit.setUsuarioTecnicoAsignado(usuarioTecnicoAsignar);
		// Establecemos la prestación a Pendiente si requiere validación o si no se asigna técnico automáticamente
		if ("SI".equals(prestacionEdit.getProducto().getRequiereValidacion()) || ( usuarioTecnicoAsignar == null )) 
		{
			prestacionEdit.setEstado(EstadoConsumo.PENDIENTE);
		}
		else 
		{
			prestacionEdit.setEstado(EstadoConsumo.ACEPTADO);
		}
		try 
		{
			consumoService.modificarPedidoPrestacion(prestacionEdit, ficheroAnexoBioseguridad);
		}
		catch (final Exception ex) 
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "prestaciones.validarir.error", null, null, ex.getMessage());
			return;
		}
		mensajeService.notificacionesValidacionIrSolicitud(prestacionEdit);
		facesMessages.add(StatusMessage.Severity.INFO, "prestaciones.validarir.ok", KEY_MSJ_CAMBIOESTADO_DETALLES, null, null, prestacionEdit.getCod().toString(), prestacionEdit.getEstado());
		inicializacionPrestacionEdit(prestacionEdit);
	}
	
	public void validar()
	{
		if (consumoService.requiereAnexoNivelBioseguridad(prestacionEdit) && ficheroAnexoBioseguridad == null && anexoBioseguridadCompletado == null)
		{
			facesMessages.add(StatusMessage.Severity.WARN, KEY_MSJ_PRESTACIONES_MODIFICAR_ERROR, KEY_MSJ_PRESTACIONES_GUARDAR_ERROR_REQUIEREANEXOBIOSEG, null, null);
			return;
		}
		// Comprobamos asignación de técnico para notificaciones
		boolean nuevoTecnicoEstablecido = false;
		if (prestacionEdit.getConsumoPadre() == null &&
			prestacionEdit.getUsuarioTecnicoAsignado() != null &&
			(tecnicoPendiente || !prestacionEdit.getUsuarioTecnicoAsignado().getCod().equals(codUsuarioTecnicoAsignado)))
		{
			// Se acaba de establecer un técnico o se ha modificado el que había
			nuevoTecnicoEstablecido = true;
		}
		prestacionEdit.setEstado(EstadoConsumo.ACEPTADO);
		try 
		{
			consumoService.modificarPedidoPrestacion(prestacionEdit, ficheroAnexoBioseguridad);
		}
		catch (final Exception ex) 
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "prestaciones.validar.error", null, null, ex.getMessage());
			return;
		}
		mensajeService.notificacionesAceptacionSolicitud(this.prestacionEdit, nuevoTecnicoEstablecido, codUsuarioTecnicoAsignado);
		facesMessages.add(StatusMessage.Severity.INFO, "prestaciones.validar.ok", KEY_MSJ_CAMBIOESTADO_DETALLES, null, null, prestacionEdit.getCod().toString(), prestacionEdit.getEstado());
		inicializacionPrestacionEdit(prestacionEdit);
	}
	
	public void reactivar()
	{
		prestacionEdit.setEstado(EstadoConsumo.ACEPTADO);
		prestacionEdit.setFechaResolucion(null);
		try 
		{
			consumoService.guardarConsumo(prestacionEdit);
		}
		catch (final Exception ex) 
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "prestaciones.reactivar.error", null, null, ex.getMessage());
			return;
		}
		facesMessages.add(StatusMessage.Severity.INFO, "prestaciones.reactivar.ok", KEY_MSJ_CAMBIOESTADO_DETALLES, null, null, prestacionEdit.getCod().toString(), prestacionEdit.getEstado());
		inicializacionPrestacionEdit(prestacionEdit);
	}
	
	public void readmitir()
	{
		prestacionEdit.setEstado(EstadoConsumo.ACEPTADO);
		try 
		{
			consumoService.guardarConsumo(prestacionEdit);
		}
		catch (final Exception ex) 
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "prestaciones.readmitir.error", null, null, ex.getMessage());
			return;
		}
		facesMessages.add(StatusMessage.Severity.INFO, "prestaciones.readmitir.ok", KEY_MSJ_CAMBIOESTADO_DETALLES, null, null, prestacionEdit.getCod().toString(), prestacionEdit.getEstado());
		inicializacionPrestacionEdit(prestacionEdit);
	}
	
	public boolean esFinalizacionParcial() 
	{
		return consumoService.esTecnicoSolicitud(prestacionEdit, servicioPrestacion ) &&
			   !identity.tienePerfilEnServicio(TipoPerfil.SUPERVISOR, servicioPrestacion.getCod()) &&
			   "SI".equals(prestacionEdit.getProducto().getRequiereValidacion()); 
	}
	
	public void finalizar()
	{
		if (consumoService.requiereAnexoNivelBioseguridad(prestacionEdit) && (ficheroAnexoBioseguridad == null) && (anexoBioseguridadCompletado == null))
		{
			facesMessages.add(StatusMessage.Severity.WARN, KEY_MSJ_PRESTACIONES_MODIFICAR_ERROR, KEY_MSJ_PRESTACIONES_GUARDAR_ERROR_REQUIEREANEXOBIOSEG, null, null);
			return;
		}
		if (!this.listaEquipos.isEmpty() && this.prestacionEdit.getListaEquipos().isEmpty())
		{
			facesMessages.add(StatusMessage.Severity.WARN, "prestaciones.finalizar.error", "prestaciones.finalizar.error.equiporequerido" , null, null);
			return;
		}
		if (esFinalizacionParcial())
		{
			prestacionEdit.setEstado(EstadoConsumo.FINALIZADO_PARCIAL);
		}
		else
		{
			prestacionEdit.setEstado(EstadoConsumo.FINALIZADO);
			prestacionEdit.setFechaResolucion(new Date());
		}
		try 
		{
			consumoService.modificarPedidoPrestacion(prestacionEdit, ficheroAnexoBioseguridad);
		}
		catch (final Exception ex) 
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "prestaciones.finalizar.error", null, null, ex.getMessage());
			return;
		}
		consumoService.recargarConsumo(prestacionEdit);
		mensajeService.notificacionesFinalizacionSolicitud(prestacionEdit);
		facesMessages.add(StatusMessage.Severity.INFO, "prestaciones.finalizar.ok", KEY_MSJ_CAMBIOESTADO_DETALLES, null, null, prestacionEdit.getCod().toString(), prestacionEdit.getEstado());
		inicializacionPrestacionEdit(prestacionEdit);
	}
	
	public String anular()
	{
		prestacionEdit.setEstado(EstadoConsumo.ANULADO);
		try 
		{
			consumoService.guardarConsumo(prestacionEdit);
		}
		catch (final Exception ex) 
		{
			facesMessages.add(StatusMessage.Severity.ERROR, this.getKeyMsj("prestaciones.anular.error"), null, null, ex.getMessage());
			return null;
		}
		facesMessages.add(StatusMessage.Severity.INFO, this.getKeyMsj("prestaciones.anular.ok"), KEY_MSJ_CAMBIOESTADO_DETALLES, null, null, prestacionEdit.getCod().toString(), prestacionEdit.getEstado());
		if (prestacionEdit.getConsumoPadre() == null)
		{
			return establecerPrestacionEdit(prestacionEdit);
		}
		else
		{
			return establecerPrestacionEdit(prestacionEdit.getConsumoPadre());
		}
	}
	
	public void rechazar()
	{
		prestacionEdit.setEstado(EstadoConsumo.RECHAZADO);
		try 
		{
			consumoService.guardarConsumo(prestacionEdit);
		}
		catch (final Exception ex) 
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "prestaciones.rechazar.error", null, null, ex.getMessage());
			return;
		}
		mensajeService.notificacionesRechazoSolicitud(prestacionEdit);
		facesMessages.add(StatusMessage.Severity.INFO, "prestaciones.rechazar.ok", KEY_MSJ_CAMBIOESTADO_DETALLES, null, null, prestacionEdit.getCod().toString(), prestacionEdit.getEstado());
		inicializacionPrestacionEdit(prestacionEdit);
	}
	
	public void anularPresupuesto()
	{
		if (identity.getUsuarioConectado().equals(this.prestacionEdit.getUsuarioSolicitante()) || identity.getUsuarioConectado().equals(this.prestacionEdit.getUsuarioIrAsociado()))
		{
			this.prestacionEdit.setEstadoPresupuesto(EstadoPresupuesto.ANULADO_POR_USUARIO);
		}
		else
		{
			this.prestacionEdit.setEstadoPresupuesto(EstadoPresupuesto.ANULADO_POR_ACTI);
		}
		try 
		{
			consumoService.guardarEstadoPresupuestoEHijos(prestacionEdit);
		}
		catch (final Exception ex) 
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "prestaciones.anular.error", null, null, ex.getMessage());
			return;
		}
		facesMessages.add(StatusMessage.Severity.INFO, "prestaciones.anular.ok", KEY_MSJ_CAMBIOESTADO_DETALLES, null, null, prestacionEdit.getCod().toString(), prestacionEdit.getEstadoPresupuesto());
		inicializacionPrestacionEdit(prestacionEdit);
	}
	
	public void enviarPresupuestoIr()
	{
		if (consumoService.requiereAnexoNivelBioseguridad(prestacionEdit) && ficheroAnexoBioseguridad == null && anexoBioseguridadCompletado == null)
		{
			facesMessages.add(StatusMessage.Severity.WARN, this.getKeyMsj(KEY_MSJ_PRESTACIONES_MODIFICAR_ERROR), KEY_MSJ_PRESTACIONES_GUARDAR_ERROR_REQUIEREANEXOBIOSEG, null, null);
			return;
		}
		prestacionEdit.setEstado(EstadoConsumo.ACEPTADO);
		prestacionEdit.setEstadoPresupuesto(EstadoPresupuesto.ENVIADO_A_IR);
		try 
		{
			consumoService.modificarPresupuestoPrestacion(prestacionEdit, ficheroAnexoBioseguridad);
		}
		catch (final Exception ex) 
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "prestaciones.presupuesto.enviarir.error", null, null, ex.getMessage());
			return;
		}
		mensajeService.enviarIrMasSolicitantePresupuestoFinalizado(this.prestacionEdit);
		facesMessages.add(StatusMessage.Severity.INFO, "prestaciones.presupuesto.enviarir.ok", KEY_MSJ_CAMBIOESTADO_DETALLES, null, null, prestacionEdit.getCod().toString(), prestacionEdit.getEstadoPresupuesto());
		inicializacionPrestacionEdit(prestacionEdit);
	}
	
	public void estableceModoComentarioReabrirRechazar(String modo)
	{
		this.modoComentarioPresupuesto = modo;
		this.comentarioAdd = null;
	}
	
	public void rechazarPresupuesto()
	{
		prestacionEdit.setEstadoPresupuesto(EstadoPresupuesto.RECHAZADO_POR_IR);
		try 
		{
			consumoService.modificarPresupuestoPrestacion(prestacionEdit, ficheroAnexoBioseguridad);
		}
		catch (final Exception ex) 
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "prestaciones.presupuesto.rechazar.error", null, null, ex.getMessage());
			return;
		}
		mensajeService.enviarTecnicoYSupervisoresRechazoPresupuesto(prestacionEdit, this.comentarioAdd);
		guardarBitacoraUsuario();
		facesMessages.add(StatusMessage.Severity.INFO, "prestaciones.presupuesto.rechazar.ok", KEY_MSJ_CAMBIOESTADO_DETALLES, null, null, prestacionEdit.getCod().toString(), prestacionEdit.getEstadoPresupuesto());
		inicializacionPrestacionEdit(prestacionEdit);
	}
	
	public void reabrirPresupuesto()
	{
		prestacionEdit.setEstadoPresupuesto(EstadoPresupuesto.SOLICITADO);
		try 
		{
			consumoService.modificarPresupuestoPrestacion(prestacionEdit, ficheroAnexoBioseguridad);
		}
		catch (final Exception ex) 
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "prestaciones.presupuesto.reabrir.error", null, null, ex.getMessage());
			return;
		}
		mensajeService.enviarTecnicoReabrirPresupuesto(prestacionEdit, this.comentarioAdd);
		guardarBitacoraUsuario();
		facesMessages.add(StatusMessage.Severity.INFO, "prestaciones.presupuesto.reabrir.ok", KEY_MSJ_CAMBIOESTADO_DETALLES, null, null, prestacionEdit.getCod().toString(), prestacionEdit.getEstadoPresupuesto());
		inicializacionPrestacionEdit(prestacionEdit);
	}
	
	public void reactivarPresupuesto()
	{
		prestacionEdit.setEstadoPresupuesto(EstadoPresupuesto.SOLICITADO);
		try 
		{
			consumoService.modificarPresupuestoPrestacion(prestacionEdit, ficheroAnexoBioseguridad);
		}
		catch (final Exception ex) 
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "prestaciones.presupuesto.reactivar.error", null, null, ex.getMessage());
			return;
		}
		facesMessages.add(StatusMessage.Severity.INFO, "prestaciones.presupuesto.reactivar.ok", KEY_MSJ_CAMBIOESTADO_DETALLES, null, null, prestacionEdit.getCod().toString(), prestacionEdit.getEstadoPresupuesto());
		inicializacionPrestacionEdit(prestacionEdit);
	}
	
	public String aceptarPresupuesto()
	{
		prestacionEdit.setEstado(EstadoConsumo.ACEPTADO);
		prestacionEdit.setEstadoPresupuesto(EstadoPresupuesto.ACEPTADO_POR_IR);
		Consumo nuevoConsumo = null;
		try 
		{
			nuevoConsumo = consumoService.guardaPresupuestoYGeneraConsumoFromPresupuesto(prestacionEdit, ficheroAnexoBioseguridad, null, new Date());
		}
		catch (final Exception ex) 
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "prestaciones.presupuesto.aceptar.error", null, null, ex.getMessage());
			return null;
		}
		mensajeService.enviarTecnicoAceptarPresupuesto(this.prestacionEdit);
		facesMessages.add(StatusMessage.Severity.INFO, "prestaciones.presupuesto.aceptar.ok", "prestaciones.presupuesto.aceptar.ok.detalles", null, null, nuevoConsumo.getCod().toString());
		return establecerPrestacionEdit(nuevoConsumo);
	}
	
	public String clonarPresupuesto()
	{
		Consumo nuevoPresupuesto = null;
		try 
		{
			nuevoPresupuesto = consumoService.clonaPresupuesto(prestacionEdit, ficheroAnexoBioseguridad, null, new Date());
		}
		catch (final Exception ex) 
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "prestaciones.presupuesto.clonar.error", null, null, ex.getMessage());
			return null;
		}
		facesMessages.add(StatusMessage.Severity.INFO, "prestaciones.presupuesto.clonar.ok", "prestaciones.presupuesto.clonar.ok.detalles", null, null, nuevoPresupuesto.getCod().toString());
		return establecerPrestacionEdit(nuevoPresupuesto);
	}
	
	public void modificadaEntidadPagadoraPresupuesto()
	{
		try
		{
			consumoService.modificarEntidadPagadoraPresupuesto(prestacionEdit);
		}
		catch (final Exception ex) 
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "prestaciones.presupuesto.modentidad.error", null, null, ex.getMessage());
			return;
		}
		facesMessages.add(StatusMessage.Severity.INFO, "prestaciones.presupuesto.modentidad.ok", "prestaciones.presupuesto.modentidad.ok.detalles", null, null, prestacionEdit.getEntidadPagadora().getNombre());
		inicializacionPrestacionEdit(prestacionEdit);
	}
	
	public String volverListado() 
	{
		return VOLVER_LISTADO;
	}
	

	
	public String addConsumoHijo() 
	{
		if ("P".equals(tipoConsumoAddHijo))
		{
			tipoConsumoAddHijo = null;
			return establecerPrestacionCreateFromConsumoPadre(prestacionEdit);
		}
		else if ("R".equals(tipoConsumoAddHijo))
		{
			tipoConsumoAddHijo = null;
			return solicitudReservas.establecerReservaCreateFromConsumoPadre(prestacionEdit);
		}
		else if ("F".equals(tipoConsumoAddHijo))
		{
			tipoConsumoAddHijo = null;
			return solicitudFungibles.establecerFungibleCreateFromConsumoPadre(prestacionEdit);
		}
		return null;
	}
	
	public String volverConsumoPadre()
	{
		return establecerPrestacionEdit(prestacionEdit.getConsumoPadre());
	}
	
	public BigDecimal getTarifaConsumo(Consumo consumo) 
	{
		return tarifaService.obtenerTarifaConsumo(consumo, prestacionEdit.getEntidadPagadora());
	}
	
	public BigDecimal getPrecioConsumo(Consumo consumo) 
	{
		return tarifaService.obtenerPrecioConsumo(consumo, prestacionEdit.getEntidadPagadora());
	}

	public BigDecimal getPrecioConsumoConHijos()
	{
		return tarifaService.obtenerPrecioConsumoConHijos(prestacionEdit, prestacionEdit.getEntidadPagadora());
	}
	
	public List<Consumo> getConsumosHijos()
	{
		return consumoService.getConsumosHijos(this.prestacionEdit);
	}
	
	public List<Consumo> getPresupuestosHijosDeTipoReserva()
	{
		return consumoService.getPresupuestosHijosTipoReservaByConsumoGenerado(this.prestacionEdit);
	}
	
    public String getDescripcionEntidad(EntidadPagadora ep)
    {
    	return tarifaService.getDescripcionEntidadPagadora( ep );
    }
	
	public String getDescripcionTipoConsumo(String tipo)
	{
		return consumoService.getDescripcionTipoConsumo( tipo );
	}
    
	public String getColorEstado(Consumo consumo)
	{
		return consumoService.getColorEstadoConsumo(consumo);
	}
	
	public String getColorEstadoPresupuesto(Consumo consumo)
	{
		return consumoService.getColorEstadoPresupuesto(consumo);
	}
	
	public String formatCantidad( BigDecimal number ) 
	{
		return Utilidades.formatCantidad(number);
	}
	
	public boolean permitidoModificarProducto()
	{
		return this.prestacionEdit.getCod() == null ||
			   (NO.equals(this.prestacionEdit.getPresupuesto()) && (this.prestacionEdit.getFactura() == null && (EstadoConsumo.PENDIENTE.equals(this.prestacionEdit.getEstado()) || EstadoConsumo.ACEPTADO.equals(this.prestacionEdit.getEstado())) && identity.tienePerfilEnServicio(TipoPerfil.SUPERVISOR, this.servicioPrestacion.getCod()))) ||
			   (SI.equals(this.prestacionEdit.getPresupuesto()) && EstadoPresupuesto.SOLICITADO.equals(this.prestacionEdit.getEstado()));
	}
	
	public boolean esTecnicoSolicitud()
	{
		if (servicioPrestacion == null)
		{
			return false;
		}
		return consumoService.esTecnicoSolicitud(prestacionEdit, servicioPrestacion);
	}	
	
	private boolean puedeHacerOperacion(OperacionConsumo operacion)
	{
		return consumoService.permitidaOperacion(prestacionEdit, servicioPrestacion, "P", operacion);
	}

	public boolean operacionGuardar()
	{
		return puedeHacerOperacion(OperacionConsumo.GUARDAR);
	}

	public boolean operacionAnular()
	{
		if (NO.equals(prestacionEdit.getPresupuesto()))
		{
			return puedeHacerOperacion(OperacionConsumo.ANULAR);
		}
		else if (prestacionEdit.getConsumoPadre() != null)
		{
			return operacionPresupuestoHijoModificar();
		}
		else
		{
			// La operación de anulación de presupuesto padre tiene su propio método
			return false;
		}
	}

	private boolean operacionPresupuestoHijoModificar()
	{
		return SI.equals(prestacionEdit.getPresupuesto()) && 
			   EstadoPresupuesto.SOLICITADO.equals(prestacionEdit.getEstadoPresupuesto()) && 
			   (identity.tienePerfilEnServicio(TipoPerfil.SUPERVISOR, prestacionEdit.getConsumoPadre().getProducto().getServicio().getCod()) || 
				(identity.tienePerfilEnServicio(TipoPerfil.TECNICO, prestacionEdit.getConsumoPadre().getProducto().getServicio().getCod()) && identity.getUsuarioConectado().equals(prestacionEdit.getConsumoPadre().getUsuarioTecnicoAsignado())));
	}
	
	
	public boolean operacionModificar()
	{
		if (NO.equals(prestacionEdit.getPresupuesto())) 
		{
			return puedeHacerOperacion(OperacionConsumo.MODIFICAR); 
		}
		else if (prestacionEdit.getConsumoPadre() != null)
		{
			return operacionPresupuestoHijoModificar();
		}
		else
		{
			return operacionPresupuestoModificar();
		}
	}

	public boolean operacionAnexo()
	{
		return puedeHacerOperacion(OperacionConsumo.ANEXO);
	}

	public boolean operacionSubconsumo()
	{
		return puedeHacerOperacion(OperacionConsumo.SUBCONSUMO);
	}

	public boolean operacionAceptar()
	{
		return NO.equals(prestacionEdit.getPresupuesto()) && puedeHacerOperacion(OperacionConsumo.ACEPTAR);
	}

	public boolean operacionSetEquipos()
	{
		return NO.equals(prestacionEdit.getPresupuesto()) && puedeHacerOperacion(OperacionConsumo.SET_EQUIPOS);
	}
	
	public boolean operacionMuestra()
	{
		return puedeHacerOperacion(OperacionConsumo.MUESTRA);
	}

	public boolean operacionFinalizar()
	{
		return NO.equals(prestacionEdit.getPresupuesto()) && puedeHacerOperacion(OperacionConsumo.FINALIZAR);
	}

	public boolean operacionRechazar()
	{
		return NO.equals(prestacionEdit.getPresupuesto()) && puedeHacerOperacion(OperacionConsumo.RECHAZAR);
	}

	public boolean operacionReactivar()
	{
		return NO.equals(prestacionEdit.getPresupuesto()) && puedeHacerOperacion(OperacionConsumo.REACTIVAR);
	}

	public boolean operacionReadmitir()
	{
		return NO.equals(prestacionEdit.getPresupuesto()) && puedeHacerOperacion(OperacionConsumo.READMITIR);
	}

	public boolean operacionValidar()
	{
		return NO.equals(prestacionEdit.getPresupuesto()) && puedeHacerOperacion(OperacionConsumo.VALIDAR);
	}

	public boolean operacionComentar()
	{
		return puedeHacerOperacion(OperacionConsumo.COMENTAR);
	}
		
	public boolean operacionComentarFacturacion()
	{
		return NO.equals(prestacionEdit.getPresupuesto()) && puedeHacerOperacion(OperacionConsumo.COMENTAR_FACTURACION);
	}

	public boolean operacionPresupuestoModificar()
	{
		return SI.equals(prestacionEdit.getPresupuesto()) && prestacionEdit.getConsumoPadre()==null && consumoService.permitidaOperacionPresupuesto(this.prestacionEdit, this.servicioPrestacion, OperacionPresupuesto.MODIFICAR);
	}
	
	public boolean operacionPresupuestoAddanexo()
	{
		return SI.equals(prestacionEdit.getPresupuesto()) && prestacionEdit.getConsumoPadre()==null && consumoService.permitidaOperacionPresupuesto(this.prestacionEdit, this.servicioPrestacion, OperacionPresupuesto.ADD_ANEXO);
	}
	
	public boolean operacionPresupuestoAnular()
	{
		return SI.equals(prestacionEdit.getPresupuesto()) && prestacionEdit.getConsumoPadre()==null && consumoService.permitidaOperacionPresupuesto(this.prestacionEdit, this.servicioPrestacion, OperacionPresupuesto.ANULAR);
	}
	
	public boolean operacionPresupuestoEnviarAIr()
	{
		return SI.equals(prestacionEdit.getPresupuesto()) && prestacionEdit.getConsumoPadre()==null && consumoService.permitidaOperacionPresupuesto(this.prestacionEdit, this.servicioPrestacion, OperacionPresupuesto.ENVIAR_A_IR);
	}
	
	public boolean operacionPresupuestoRechazarPorIr()
	{
		return SI.equals(prestacionEdit.getPresupuesto()) && prestacionEdit.getConsumoPadre()==null && consumoService.permitidaOperacionPresupuesto(this.prestacionEdit, this.servicioPrestacion, OperacionPresupuesto.RECHAZAR_POR_IR);
	}

	public boolean operacionPresupuestoAceptarPorIr()
	{
		return SI.equals(prestacionEdit.getPresupuesto()) && prestacionEdit.getConsumoPadre()==null && consumoService.permitidaOperacionPresupuesto(this.prestacionEdit, this.servicioPrestacion, OperacionPresupuesto.ACEPTAR_POR_IR);
	}
	
	public boolean operacionPresupuestoReabrir()
	{
		return SI.equals(prestacionEdit.getPresupuesto()) && prestacionEdit.getConsumoPadre()==null && consumoService.permitidaOperacionPresupuesto(this.prestacionEdit, this.servicioPrestacion, OperacionPresupuesto.REABRIR);
	}
	
	public boolean operacionPresupuestoClonar()
	{
		return SI.equals(prestacionEdit.getPresupuesto()) && prestacionEdit.getConsumoPadre()==null && consumoService.permitidaOperacionPresupuesto(this.prestacionEdit, this.servicioPrestacion, OperacionPresupuesto.CLONAR);
	}
	
	public boolean operacionPresupuestoReactivar()
	{
		return SI.equals(prestacionEdit.getPresupuesto()) && prestacionEdit.getConsumoPadre()==null && consumoService.permitidaOperacionPresupuesto(this.prestacionEdit, this.servicioPrestacion, OperacionPresupuesto.REACTIVAR);
	}
	
	public boolean requiereAnexoNivelBioseguridad()
	{
		return consumoService.requiereAnexoNivelBioseguridad(prestacionEdit);
	}
	
	public boolean mostrarImportesTotales()
	{
		return this.prestacionEdit.getCod() != null && SI.equals(this.prestacionEdit.getPresupuesto()) && this.prestacionEdit.getEntidadPagadora()!=null && 
			   ((identity.tienePerfilEnServicio(TipoPerfil.SUPERVISOR, this.servicioPrestacion.getCod()) || this.esTecnicoSolicitud()) || 
				(EstadoPresupuesto.ACEPTADO_POR_IR.equals(this.prestacionEdit.getEstadoPresupuesto()) || EstadoPresupuesto.ENVIADO_A_IR.equals(this.prestacionEdit.getEstadoPresupuesto()) || EstadoPresupuesto.RECHAZADO_POR_IR.equals(this.prestacionEdit.getEstadoPresupuesto())));
	}
	
    public String getDescripcionEstadoConsumo()
    {
    	return consumoService.getDescripcionEstadoConsumo(prestacionEdit);
    }
    
	public boolean preguntarEnvioMailUsuario()
	{
		return consumoService.preguntarEnvioMailUsuario(prestacionEdit);
	}
	
	public Integer getMinutosUsoByEquipo(Equipo equipo)
	{
		return productoService.getMinutosUsoByProductoEquipo(this.prestacionEdit.getProducto(), equipo);
	}
	
	private String getKeyMsj(String key)
	{
		StringBuilder keyMod = new StringBuilder(key);
		if ("SI".equals(this.prestacionEdit.getPresupuesto()))
		{
			keyMod.append(".presupuesto");
			if (this.prestacionEdit.getConsumoPadre() != null)
			{
				keyMod.append(".hijo");
			}
		}
		return keyMod.toString();
	}
	
	public Consumo getConsumoGeneradoFromPresupuesto()
	{
		return consumoService.getConsumoGeneradoFromPresupuesto(this.prestacionEdit);
	}

}
