package es.um.atica.sai.backbeans;

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
import org.primefaces.event.TabChangeEvent;

import es.um.atica.sai.dtos.TipoPerfil;
import es.um.atica.sai.entities.Equipo;
import es.um.atica.sai.entities.HorarioDia;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.ReservableHorario;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.TipoHorario;
import es.um.atica.sai.entities.TipoReservable;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.ReservableService;
import es.um.atica.sai.services.interfaces.ServicioService;

@Name(value = TipoReservableEditBean.NAME )
@Scope(value = ScopeType.CONVERSATION)
public class TipoReservableEditBean {

	public static final String NAME = "tipoReservableEdit";

	@In(create=true)
	protected FacesMessages facesMessages;

	@In(value = ReservableService.NAME, required = true, create = true )
	ReservableService reservableService;

	@In(value = ServicioService.NAME, create = true)
	ServicioService servicioService;

	@In(create=true)
	private SaiIdentity identity;

	@Logger
	private Log log;

	private static final String KEY_TIPORESERVABLE_ADDHORARIO_ERROR = "tiporeservable.add.horario.error";
	private static final String KEY_TIPORESERVABLE_GUARDAR_ERROR = "tiporeservable.guardar.error";

	private TipoReservable tipoReservableEdit;
	private ReservableHorario reservableHorarioAdd;
	private List<Servicio> listaServicios;
	private List<TipoHorario> listaTiposHorario;
	private List<Producto> listaProductos;
	private List<ReservableHorario> listaReservableHorarios;
	private String retorno;
	private Integer tabActivo;

	public TipoReservable getTipoReservableEdit() {
		return tipoReservableEdit;
	}

	public void setTipoReservableEdit( TipoReservable tipoReservableEdit ) {
		this.tipoReservableEdit = tipoReservableEdit;
	}

	public ReservableHorario getReservableHorarioAdd() {
		return reservableHorarioAdd;
	}

	public void setHorario( ReservableHorario reservableHorarioAdd ) {
		this.reservableHorarioAdd = reservableHorarioAdd;
	}

	public List<Servicio> getListaServicios() {
		return listaServicios;
	}

	public void setListaServicios( List<Servicio> listaServicios ) {
		this.listaServicios = listaServicios;
	}

	public List<TipoHorario> getListaTiposHorario() {
		return listaTiposHorario;
	}

	public void setListaTiposHorario( List<TipoHorario> listaTiposHorario ) {
		this.listaTiposHorario = listaTiposHorario;
	}

	public List<Producto> getListaProductos() {
		return listaProductos;
	}

	public void setListaProductos( List<Producto> listaProductos ) {
		this.listaProductos = listaProductos;
	}

	public List<ReservableHorario> getListaReservableHorarios() {
		return listaReservableHorarios;
	}
	
	public void setListaReservableHorarios( List<ReservableHorario> listaReservableHorarios ) {
		this.listaReservableHorarios = listaReservableHorarios;
	}

	public String getRetorno() {
		return retorno;
	}

	public void setRetorno( String retorno ) {
		this.retorno = retorno;
	}
	
	public Integer getTabActivo() {
		return tabActivo;
	}
	
	public void setTabActivo( Integer tabActivo ) {
		this.tabActivo = tabActivo;
	}

	@Create
	public void inicializar()
	{
		reservableHorarioAdd = new ReservableHorario();
		listaServicios = identity.getServiciosPerfil(TipoPerfil.SUPERVISOR);
	}

	public String establecerTipoReservableCreate()
	{
		tipoReservableEdit = new TipoReservable();
		tipoReservableEdit.setEstado("ALTA");
		if (listaServicios.size() == 1)
		{
			tipoReservableEdit.setServicio(listaServicios.get(0));
		}
		return NAME;
	}

	public String establecerTipoReservableEdit(TipoReservable tr)
	{
		tipoReservableEdit = tr;
		listaTiposHorario = reservableService.getListaTiposHorarioByServicio(tr.getServicio());
		listaProductos = reservableService.getProductosByTipoReservable(tr);
		listaReservableHorarios = reservableService.getListaReservableHorarioByTipoReservable(tr);
		this.tabActivo = 0;
		return NAME;
	}

	private boolean esValidaConfiguracionMinutos()
	{
		if ((( tipoReservableEdit.getMinutosTecnico() != null ) && ( tipoReservableEdit.getDuracionMinima().compareTo(tipoReservableEdit.getMinutosTecnico())<0 )) ||
				(( tipoReservableEdit.getMinutosTecnicoBajo() != null ) && ( tipoReservableEdit.getDuracionMinima().compareTo(tipoReservableEdit.getMinutosTecnicoBajo())<0 )) ||
				(( tipoReservableEdit.getMinutosTecnicoMedio() != null ) && ( tipoReservableEdit.getDuracionMinima().compareTo(tipoReservableEdit.getMinutosTecnicoMedio())<0 )) ||
				(( tipoReservableEdit.getMinutosTecnicoAlto() != null ) && ( tipoReservableEdit.getDuracionMinima().compareTo(tipoReservableEdit.getMinutosTecnicoAlto())<0 )))
		{
			facesMessages.add(StatusMessage.Severity.WARN, KEY_TIPORESERVABLE_GUARDAR_ERROR, "tiporeservable.guardar.error.turnomenortecnico", null, null);
			return false;
		}
		if ((( tipoReservableEdit.getMinutosTecnicoBajo() != null ) && ( tipoReservableEdit.getMinutosTecnicoMedio() != null ) && ( tipoReservableEdit.getMinutosTecnicoAlto() != null ) && (( tipoReservableEdit.getMinutosTecnicoBajo().compareTo(tipoReservableEdit.getMinutosTecnicoMedio())>0 ) || ( tipoReservableEdit.getMinutosTecnicoMedio().compareTo(tipoReservableEdit.getMinutosTecnicoAlto())>0 ))) ||
				(( tipoReservableEdit.getMinutosTecnicoBajo() != null ) && ( tipoReservableEdit.getMinutosTecnicoMedio() != null ) && ( tipoReservableEdit.getMinutosTecnicoBajo().compareTo(tipoReservableEdit.getMinutosTecnicoMedio())>0 )) ||
				(( tipoReservableEdit.getMinutosTecnicoBajo() != null ) && ( tipoReservableEdit.getMinutosTecnicoAlto() != null ) && ( tipoReservableEdit.getMinutosTecnicoBajo().compareTo(tipoReservableEdit.getMinutosTecnicoAlto())>0 )) ||
				(( tipoReservableEdit.getMinutosTecnicoMedio() != null ) && ( tipoReservableEdit.getMinutosTecnicoAlto() != null ) && ( tipoReservableEdit.getMinutosTecnicoMedio().compareTo(tipoReservableEdit.getMinutosTecnicoAlto())>0 )))
		{
			facesMessages.add(StatusMessage.Severity.WARN, KEY_TIPORESERVABLE_GUARDAR_ERROR, "tiporeservable.guardar.error.ordenbajamediaalta", null, null);
			return false;
		}
		return true;
	}
	public void guardar()
	{
		if (!esValidaConfiguracionMinutos())
		{
			return;
		}
		final boolean eraNuevo = tipoReservableEdit.getCod() == null;
		tipoReservableEdit.setReservaMultiple("NO");
		try
		{
			reservableService.guardarTipoReservable(tipoReservableEdit);

		}
		catch (final Exception ex)
		{
			log.error( "Error al guardar un tipo de Reservable #0", ex.getMessage() );
			facesMessages.add(StatusMessage.Severity.ERROR, KEY_TIPORESERVABLE_GUARDAR_ERROR, null, null, ex.getMessage());
			return;
		}
		if (eraNuevo)
		{
			facesMessages.add(StatusMessage.Severity.INFO, "tiporeservable.guardar.ok.nuevo", null, null, tipoReservableEdit.getDescripcion());
			listaTiposHorario = reservableService.getListaTiposHorarioByServicio(tipoReservableEdit.getServicio());
		}
		else
		{
			facesMessages.add(StatusMessage.Severity.INFO, "tiporeservable.guardar.ok", null, null, tipoReservableEdit.getDescripcion());
		}
	}

	public void establecerReservableHorarioCreate()
	{
		this.reservableHorarioAdd = new ReservableHorario();
		this.reservableHorarioAdd.setTipoReservable(this.tipoReservableEdit);
	}

	public void eliminarReservableHorario( ReservableHorario rh )
	{
		try
		{
			reservableService.eliminarReservableHorario(rh);
		}
		catch (final Exception ex)
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "tiporeservable.rem.horario.error", null, null, ex.getMessage());
			return;
		}
		this.listaReservableHorarios.remove(rh);
		facesMessages.add(StatusMessage.Severity.INFO, "tiporeservable.rem.horario.ok", null, null, rh.getTipoHorario().getDescripcion());
	}

	public void crearReservableHorario()
	{
		try
		{
			reservableService.crearTipoReservableHorario(reservableHorarioAdd);
		}
		catch(final Exception ex)
		{
			facesMessages.add(StatusMessage.Severity.ERROR, KEY_TIPORESERVABLE_ADDHORARIO_ERROR, null, null, ex.getMessage());
			return;
		}
		this.listaReservableHorarios = reservableService.getListaReservableHorarioByTipoReservable(this.tipoReservableEdit);
		facesMessages.add(StatusMessage.Severity.INFO, "tiporeservable.add.horario.ok", null, null, reservableHorarioAdd.getTipoHorario().getDescripcion());
	}

	public void eliminarReservable(Equipo reservable)
	{
		try
		{
			reservableService.eliminarEquipo( reservable );
		}
		catch(final Exception ex)
		{
			facesMessages.add(StatusMessage.Severity.ERROR, "tiporeservable.reservable.eliminar.error", null, null, ex.getMessage());
			return;
		}
		facesMessages.add(StatusMessage.Severity.INFO, "tiporeservable.reservable.eliminar.ok", null, null, reservable.getDescripcion());

	}

	public List<Equipo> getListaReservables()
	{
		return reservableService.getReservablesByTipoReservable(tipoReservableEdit);
	}

	public List<HorarioDia> getListaHorarioDiaByTipohorario(TipoHorario th)
	{
		return reservableService.getListaHorarioDiaByTipohorario( th );
	}

	public String getDescripcionHorarioDia(HorarioDia hd)
	{
		return reservableService.getDescripcionHorarioDia( hd );
	}

	public void onTabChange(TabChangeEvent event) 
	{
		if (event.getTab().getId().equals("tabHorarios"))
		{
			this.tabActivo = 0;
		}
		else if (event.getTab().getId().equals("tabReservables"))
		{
			this.tabActivo = 1;
		}
		else if (event.getTab().getId().equals("tabProductos"))
		{
			this.tabActivo = 2;
		}
	}
	
	public String volver()
	{
		if (retorno != null)
		{
			return retorno;
		}
		return "tiposReservables";
	}
}
