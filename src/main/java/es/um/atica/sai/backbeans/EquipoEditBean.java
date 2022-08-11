package es.um.atica.sai.backbeans;

import java.math.BigDecimal;
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

import es.um.atica.sai.dtos.Dependencia;
import es.um.atica.sai.dtos.TipoPerfil;
import es.um.atica.sai.entities.Equipo;
import es.um.atica.sai.entities.Intervencion;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.TipoReservable;
import es.um.atica.sai.exceptions.SaiException;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.ProductoService;
import es.um.atica.sai.services.interfaces.ReservableService;

@Name( EquipoEditBean.NAME )
@Scope( ScopeType.CONVERSATION )
public class EquipoEditBean {

	public static final String NAME = "equipoEdit";

	@Logger
	private Log log;

	@In(create = true )
	private ReservableService reservableService;

	@In(create = true )
	private ProductoService productoService;

	@In(create=true)
	protected FacesMessages facesMessages;

	@In(value="org.jboss.seam.security.identity", required=true)
	SaiIdentity identity;

	private Equipo equipoEdit;
	private Intervencion intervencionEdit;
	private String retorno;
	private String tipo;
	private List<Servicio> listaServicios;
	private List<TipoReservable> listaTiposReservable;
	private String descripcionDependencia;
	private Dependencia dependencia;

	public List<Servicio> getListaServicios() {
		return listaServicios;
	}

	public void setListaServicios( List<Servicio> listaServicios ) {
		this.listaServicios = listaServicios;
	}

	public Equipo getEquipoEdit() {
		return equipoEdit;
	}

	public void setEquipoEdit( Equipo equipoEdit ) {
		this.equipoEdit = equipoEdit;
	}

	public Intervencion getIntervencionEdit() {
		return intervencionEdit;
	}

	public void setIntervencionEdit( Intervencion intervencionEdit ) {
		this.intervencionEdit = intervencionEdit;
	}

	public String getRetorno() {
		return retorno;
	}

	public void setRetorno( String retorno ) {
		this.retorno = retorno;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo( String tipo ) {
		this.tipo = tipo;
	}

	public String getDescripcionDependencia() {
		return descripcionDependencia;
	}

	public void setDescripcionDependencia( String descripcionDependencia ) {
		this.descripcionDependencia = descripcionDependencia;
	}

	public Dependencia getDependencia() {
		return dependencia;
	}

	public void setDependencia( Dependencia dependencia ) {
		this.dependencia = dependencia;
	}


	public List<TipoReservable> getListaTiposReservable() {
		return listaTiposReservable;
	}


	public void setListaTiposReservable( List<TipoReservable> listaTiposReservable ) {
		this.listaTiposReservable = listaTiposReservable;
	}

	@Create
	public void inicializa() {
		listaServicios = identity.getServiciosPerfil(TipoPerfil.SUPERVISOR);
		equipoEdit = new Equipo();
	}

	public String establecerEquipoCreate() 
	{
		this.equipoEdit = new Equipo();
		this.descripcionDependencia = "";
		this.dependencia = null;
		this.tipo = null;
		return NAME;
	}
	
	public String establecerEquipoEdit( Equipo equipo ) 
	{
		this.equipoEdit = equipo;
		this.tipo = equipo.getTipoReservable() == null ? "E" : "R";
		if (this.equipoEdit.getCodigoDep() != null ) 
		{
			descripcionDependencia = reservableService.getNombreDependencia(this.equipoEdit.getTipoDep(),
																			this.equipoEdit.getCodigoDep(), 
																			this.equipoEdit.getBloqueDep(), 
																			this.equipoEdit.getPlantaDep(),
																			this.equipoEdit.getNumDep());
		}
		this.seleccionadoServicio();
		return NAME;
	}

	public void seleccionadoServicio()
	{
		if (this.equipoEdit.getServicio() == null)
		{
			this.listaTiposReservable = null;
		}
		else
		{
			this.listaTiposReservable = productoService.getListaTiposReservableByServicio(this.equipoEdit.getServicio());
		}
	}

	public List<String> buscaDependenciasXNombreTag( String patron ) {
		return reservableService.getListaNombresDependenciaByPatron( patron );
	}

	public void obtenerDependencia() {
		dependencia = reservableService.getDependenciaByDescripcion( descripcionDependencia );
		equipoEdit.setCodigoDep( dependencia.getCodigo() );
		equipoEdit.setBloqueDep( dependencia.getBloque() );
		equipoEdit.setNumDep( dependencia.getNumDep() );
		equipoEdit.setPlantaDep( dependencia.getPlanta() );
		equipoEdit.setTipoDep( dependencia.getTipo() );
	}


	public void guardarEquipo() {
		try {
			if ( equipoEdit.getFechaBaja() == null ) {
				equipoEdit.setEstado( "ALTA" );
			}
			if ( ( descripcionDependencia == null ) || descripcionDependencia.isEmpty() ) {
				equipoEdit.setCodigoDep( null );
				equipoEdit.setBloqueDep( null );
				equipoEdit.setNumDep( null );
				equipoEdit.setPlantaDep( null );
				equipoEdit.setTipoDep( null );
			}
			if ( tipo.equals( "E" ) ) {
				equipoEdit.setTipoReservable( null );
			}
			reservableService.guardarEquipo( equipoEdit );
			facesMessages.add( StatusMessage.Severity.INFO, "equipos.guardar.ok", null, null,
					equipoEdit.getDescripcion() );
		} catch ( final SaiException ex ) {
			reservableService.refreshEquipo( equipoEdit );
			facesMessages.add( StatusMessage.Severity.ERROR, "equipos.guardar.error", null, null, ex.getMessage() );
		}
	}

	public String volver() {
		equipoEdit = null;
		if ( retorno != null ) {
			return retorno;
		}
		return "listar";
	}

	public void establecerIntervencionEdit( Intervencion i ) {
		intervencionEdit = i;
	}

	public void establecerIntervencionCreate() {
		intervencionEdit = new Intervencion();
	}

	public void eliminarIntervencion( Intervencion i ) {
		try {
			reservableService.eliminarIntervencion( i );
			reservableService.refreshEquipo( equipoEdit );
			facesMessages.add( StatusMessage.Severity.INFO, "equipos.intervenciones.eliminar.ok", null, null,
					i.getDescripcion() );
		} catch ( final SaiException ex ) {
			facesMessages.add( StatusMessage.Severity.ERROR, "equipos.intervenciones.eliminar.error", null, null,
					ex.getMessage() );
		}

	}

	public void guardarIntervencion() {
		try {
			intervencionEdit.setEquipo( equipoEdit );
			reservableService.guardarIntervencion( intervencionEdit );
			reservableService.refreshEquipo( equipoEdit );
			facesMessages.add( StatusMessage.Severity.INFO, "equipos.intervenciones.guardar.ok", null, null,
					intervencionEdit.getDescripcion() );
		} catch ( final SaiException ex ) {
			facesMessages.add( StatusMessage.Severity.ERROR, "equipos.intervenciones.guardar.error", null, null,
					ex.getMessage() );
		}
	}

	public String formatearPrecio( BigDecimal precio ) {
		final String p = precio.toString();
		if ( p.contains( "." ) ) {
			return p;
		}
		final StringBuilder sb = new StringBuilder( p ).append( ".00" );
		return sb.toString();
	}

}
