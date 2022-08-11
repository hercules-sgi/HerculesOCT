package es.um.atica.sai.backbeans;

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
import org.jboss.seam.log.Log;

import es.um.atica.commons.utils.UtilDate;
import es.um.atica.sai.dtos.EstadisticaConsumo;
import es.um.atica.sai.dtos.TipoPerfil;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.ConsumoService;
import es.um.atica.sai.services.interfaces.ServicioService;
import es.um.atica.sai.services.interfaces.UsuarioService;
import es.um.atica.sai.utils.Utilidades;

@Name( "estadisticaConsumo" )
@Scope(ScopeType.CONVERSATION)
public class EstadisticaConsumoBean {


	@Logger
	private Log log;

	@In(create = true, required = true)
	ConsumoService consumoService;

	@In( create = true, required = true )
	UsuarioService usuarioService;

	@In( create = true, required = true )
	ServicioService servicioService;

	@In(value="org.jboss.seam.security.identity", required=true)
	SaiIdentity identity;

	private Usuario usuarioIRBuscar;
	private Usuario solicitanteBuscar;
	private Servicio servicioBuscar;
	private Date fechaSolicitudDesdeBuscar;
	private Date fechaSolicitudHastaBuscar;
	private String fechaSolicitudDesdeBuscarString;
	private String fechaSolicitudHastaBuscarString;
	private List<EstadisticaConsumo> listaEstadisticas;
	private List<Servicio> listaServicios;
	private List<Usuario> listaUsuariosIR;
	private List<Usuario> listaUsuariosSolicitantes;
	private List<Servicio> listaServiciosEstadistica;
	private boolean byProducto;
	private boolean busquedaRealizada;

	@Create
	public void inicializa() 
	{
		this.listaServicios = identity.getServiciosPerfil(TipoPerfil.SUPERVISOR);
		this.listaUsuariosIR = usuarioService.getIrsByListaServicios(this.listaServicios);
		this.listaUsuariosSolicitantes = usuarioService.getUsuariosByListaServicio(this.listaServicios);
		this.busquedaRealizada = false;
	}

	public void seleccionadoServicio() 
	{
		if (this.servicioBuscar != null) 
		{
			this.listaUsuariosIR = usuarioService.getIrsByServicio(this.servicioBuscar);
			if (this.usuarioIRBuscar != null && this.listaUsuariosIR.contains(this.usuarioIRBuscar))
			{
				this.listaUsuariosSolicitantes = usuarioService.getMiembrosByIr(this.usuarioIRBuscar);
			}
			else
			{
				this.listaUsuariosSolicitantes = usuarioService.getUsuariosByServicio(this.servicioBuscar);
			}
		}
		else
		{
			this.listaUsuariosIR = usuarioService.getIrsByListaServicios(this.listaServicios);
			if (this.usuarioIRBuscar != null && this.listaUsuariosIR.contains(this.usuarioIRBuscar))
			{
				this.listaUsuariosSolicitantes = usuarioService.getMiembrosByIr(this.usuarioIRBuscar);
			}
			else
			{
				this.listaUsuariosSolicitantes = usuarioService.getUsuariosByListaServicio(this.listaServicios);
			}
		}
	}

	public void buscar() 
	{
		if (this.byProducto) 
		{
			final List<Servicio> lista = new ArrayList<>();
			lista.add(this.servicioBuscar);
			this.listaEstadisticas = consumoService.estadisticasConsumos(this.byProducto, lista);
		} 
		else 
		{
			this.servicioBuscar = null;
			this.listaEstadisticas = consumoService.estadisticasConsumos( byProducto, listaServicios );
		}
		this.busquedaRealizada = true;
	}

	public void seleccionadoIr() 
	{
		if (this.usuarioIRBuscar != null) 
		{
			this.listaUsuariosSolicitantes = usuarioService.getMiembrosByIr(this.usuarioIRBuscar);
		}
		else if (this.servicioBuscar != null)
		{
			this.listaUsuariosSolicitantes = usuarioService.getUsuariosByServicio(this.servicioBuscar);
		}
		else
		{
			this.listaUsuariosSolicitantes = usuarioService.getUsuariosByListaServicio(this.listaServicios);
		}
	}

	public void limpiar() 
	{
		this.servicioBuscar = null;
		this.byProducto = false;
		this.usuarioIRBuscar = null;
		this.solicitanteBuscar = null;
		this.setFechaSolicitudDesdeBuscar(null);
		this.setFechaSolicitudHastaBuscar(null);
		this.listaUsuariosIR = usuarioService.getIrsByListaServicios(this.listaServicios);
		this.listaUsuariosSolicitantes = usuarioService.getUsuariosByListaServicio(this.listaServicios);
		this.listaEstadisticas = new ArrayList<>();
		this.busquedaRealizada = false;
	}

	public String redondear2Decimales( BigDecimal n ) {
		return Utilidades.formatCantidad( n );
	}

	public Usuario getUsuarioIRBuscar() {
		return usuarioIRBuscar;
	}

	public void setUsuarioIRBuscar( Usuario usuarioIRBuscar ) {
		this.usuarioIRBuscar = usuarioIRBuscar;
	}

	public Servicio getServicioBuscar() {
		return servicioBuscar;
	}

	public void setServicioBuscar( Servicio servicioBuscar ) {
		this.servicioBuscar = servicioBuscar;
	}

	public Date getFechaSolicitudDesdeBuscar() {
		return fechaSolicitudDesdeBuscar;
	}

	public void setFechaSolicitudDesdeBuscar( Date fechaSolicitudDesdeBuscar ) {
		fechaSolicitudDesdeBuscarString = UtilDate.convertirDateFechaToString( fechaSolicitudDesdeBuscar );
		this.fechaSolicitudDesdeBuscar = fechaSolicitudDesdeBuscar;
	}

	public Date getFechaSolicitudHastaBuscar() {
		return fechaSolicitudHastaBuscar;
	}

	public void setFechaSolicitudHastaBuscar( Date fechaSolicitudHastaBuscar ) {
		fechaSolicitudHastaBuscarString = UtilDate.convertirDateFechaToString( fechaSolicitudHastaBuscar );
		this.fechaSolicitudHastaBuscar = fechaSolicitudHastaBuscar;
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

	public List<EstadisticaConsumo> getListaEstadisticas() {
		return listaEstadisticas;
	}

	public void setListaEstadisticas( List<EstadisticaConsumo> listaEstadisticas ) {
		this.listaEstadisticas = listaEstadisticas;
	}

	public boolean isByProducto() {
		return byProducto;
	}

	public void setByProducto( boolean byProducto ) {
		this.byProducto = byProducto;
	}

	public Usuario getSolicitanteBuscar() {
		return solicitanteBuscar;
	}

	public void setSolicitanteBuscar( Usuario solicitanteBuscar ) {
		this.solicitanteBuscar = solicitanteBuscar;
	}

	public List<Servicio> getListaServicios() {
		return listaServicios;
	}

	public void setListaServicios( List<Servicio> listaServicios ) {
		this.listaServicios = listaServicios;
	}

	public List<Usuario> getListaUsuariosIR() {
		return listaUsuariosIR;
	}

	public void setListaUsuariosIR( List<Usuario> listaUsuariosIR ) {
		this.listaUsuariosIR = listaUsuariosIR;
	}

	public List<Usuario> getListaUsuariosSolicitantes() {
		return listaUsuariosSolicitantes;
	}

	public void setListaUsuariosSolicitantes( List<Usuario> listaUsuariosSolicitantes ) {
		this.listaUsuariosSolicitantes = listaUsuariosSolicitantes;
	}

	public List<Servicio> getListaServiciosEstadistica() {
		return listaServiciosEstadistica;
	}

	public void setListaServiciosEstadistica( List<Servicio> listaServiciosEstadistica ) {
		this.listaServiciosEstadistica = listaServiciosEstadistica;
	}

	public boolean isBusquedaRealizada() {
		return busquedaRealizada;
	}

	public void setBusquedaRealizada( boolean busquedaRealizada ) {
		this.busquedaRealizada = busquedaRealizada;
	}

}
