package es.um.atica.sai.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.ejb.Stateless;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.SeamResourceBundle;
import org.jboss.seam.log.Log;
import org.primefaces.model.SortOrder;

import es.um.atica.commons.utils.UtilDate;
import es.um.atica.jdbc.exceptions.FundeWebJdbcRollBackException;
import es.um.atica.sai.das.impl.IntervencionDASImpl;
import es.um.atica.sai.das.interfaces.ConsumoDAS;
import es.um.atica.sai.das.interfaces.EquipoDAS;
import es.um.atica.sai.das.interfaces.HorarioDiaDAS;
import es.um.atica.sai.das.interfaces.IntervencionDAS;
import es.um.atica.sai.das.interfaces.ProductoDAS;
import es.um.atica.sai.das.interfaces.ReservaEsperaDAS;
import es.um.atica.sai.das.interfaces.ReservableHorarioDAS;
import es.um.atica.sai.das.interfaces.ReservasDAS;
import es.um.atica.sai.das.interfaces.TipoHorarioDAS;
import es.um.atica.sai.das.interfaces.TipoReservableDAS;
import es.um.atica.sai.das.interfaces.UsuarioDAS;
import es.um.atica.sai.dtos.Dependencia;
import es.um.atica.sai.dtos.EquipoProductividad;
import es.um.atica.sai.entities.Consumo;
import es.um.atica.sai.entities.Equipo;
import es.um.atica.sai.entities.HorarioDia;
import es.um.atica.sai.entities.Intervencion;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.ReservaEspera;
import es.um.atica.sai.entities.ReservableHorario;
import es.um.atica.sai.entities.Reservas;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.TipoHorario;
import es.um.atica.sai.entities.TipoReservable;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;
import es.um.atica.sai.paos.interfaces.SaiPao;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.ConsumoService;
import es.um.atica.sai.services.interfaces.MensajeService;
import es.um.atica.sai.services.interfaces.ReservableService;
import es.um.atica.sai.services.interfaces.ServicioService;

@Stateless
@Name( ReservableService.NAME )
public class ReservableServiceImpl implements ReservableService {

	@Logger
	private Log log;

	@In(value = TipoReservableDAS.NAME, create = true, required = true )
	private TipoReservableDAS tipoReservableDAS;

	@In(value = ReservableHorarioDAS.NAME, create = true)
	private ReservableHorarioDAS reservableHorarioDAS;

	@In(value = HorarioDiaDAS.NAME, create = true)
	private HorarioDiaDAS horarioDiaDAS;

	@In(value = ReservaEsperaDAS.NAME, create = true)
	private ReservaEsperaDAS reservaEsperaDAS;

	@In(value = UsuarioDAS.NAME, create = true)
	private UsuarioDAS usuarioDAS;

	@In(value = IntervencionDASImpl.NAME, create = true)
	private IntervencionDAS intervencionDAS;

	@In(value = ServicioService.NAME, create = true, required = true )
	private ServicioService servicioService;

	@In( value = MensajeServiceImpl.NAME, create = true )
	private MensajeService mensajeService;

	@In(create = true )
	private ConsumoService consumoService;

	@In(create=true)
	TipoHorarioDAS tipoHorarioDAS;

	@In(create=true)
	private SaiIdentity identity;

	@In(create = true)
	private EquipoDAS equipoDAS;

	@In(create=true)
	ReservasDAS reservasDAS;

	@In(create=true)
	ProductoDAS productoDAS;

	@In(create=true)
	ConsumoDAS consumoDAS;

	@In(value= "saiPao", create=true)
	private SaiPao saiPao;

	private final ResourceBundle srb = SeamResourceBundle.getBundle();

	@Override
	public List<Producto> getProductosByTipoReservable(TipoReservable tr)
	{
		return productoDAS.getProductosByTipoReservable(tr);
	}

	@Override
	public List<TipoReservable> buscarTiposReservables()
	{
		return tipoReservableDAS.buscar();
	}

	@Override
	public TipoReservable getTipoReservableById( Long codigo ) {
		return tipoReservableDAS.find( codigo );
	}

	@Override
	public void guardarEquipo( Equipo equipo ) throws SaiException
	{
		if ( equipo.getCod() == null )
		{
			if ( !equipoDAS.existeEquipobyDescripcion( equipo ) ) {
				equipoDAS.crear( equipo );
			} else {
				throw new SaiException( srb.getString( "equipos.guardar.error.yaexiste" ) );
			}
		}
		else
		{
			if ( !equipoDAS.existeOtroEquipobyDescripcion( equipo ) ) {
				equipoDAS.modificar( equipo );
			} else {
				throw new SaiException( srb.getString( "equipos.guardar.error.yaexiste" ) );
			}
		}
	}

	@Override
	public void eliminarEquipo( Equipo equipo ) throws SaiException
	{
		if ( ( ( equipo.getReservases() != null ) && !equipo.getReservases().isEmpty() )
				|| ( ( equipo.getListaConsumos() != null ) && !equipo.getListaConsumos().isEmpty() )
				|| ( ( equipo.getListaProductos() != null ) && !equipo.getListaProductos().isEmpty() )
				|| ( ( equipo.getListaIntervenciones() != null ) && !equipo.getListaIntervenciones().isEmpty() ) )
		{
			// No se puede eliminar, lo damos de baja
			equipo.setEstado( "BAJA" );
			equipo.setFechaBaja( new Date() );
			equipoDAS.modificar( equipo );
		}
		else {
			equipoDAS.eliminar( equipo );
		}
	}

	@Override
	public void guardarTipoReservable(TipoReservable tr) throws SaiException
	{
		if (tr.getCod()==null)
		{
			tipoReservableDAS.crear( tr );
		}
		else
		{
			tipoReservableDAS.modificar(tr);
		}
	}

	@Override
	public String getNombreDiaSemana(int dia)
	{
		switch (dia)
		{
			case 1:
				return "Domingo";
			case 2:
				return "Lunes";
			case 3:
				return "Martes";
			case 4:
				return "Miércoles";
			case 5:
				return "Jueves";
			case 6:
				return "Viernes";
			case 7:
				return "Sábado";
			default:
				return null;
		}
	}

	@Override
	public List<HorarioDia> getListaHorarioDiaByTipohorario(TipoHorario th)
	{
		return horarioDiaDAS.getListaHorarioDiaByTipohorario(th);
	}

	@Override
	public String getDescripcionHorarioDia(HorarioDia hd)
	{
		boolean tieneManana = false;
		final StringBuilder descripcion = new StringBuilder("");
		if (( hd.getHoraIniManana() != null ) && ( hd.getHoraFinManana() != null ))
		{
			descripcion.append(getNombreDiaSemana(hd.getDia())).append(" de ").append(hd.getHoraIniManana()).append(" a ").append(hd.getHoraFinManana());
			tieneManana = true;
		}
		if (( hd.getHoraIniTarde() != null ) && ( hd.getHoraFinTarde() != null ))
		{
			if (tieneManana)
			{
				descripcion.append("; De ");
			}
			else
			{
				descripcion.append(getNombreDiaSemana(hd.getDia())).append(" de ");
			}
			descripcion.append(hd.getHoraIniTarde()).append(" a ").append(hd.getHoraFinTarde());
		}
		return descripcion.toString();
	}


	@Override
	public String getDescripcionTurno(HorarioDia hd, String tipoTurno)
	{
		final StringBuilder descripcion = new StringBuilder("");
		if (tipoTurno.equals("MAÑANA"))
		{
			if (( hd.getHoraIniManana() != null ) && ( hd.getHoraFinManana() != null ))
			{
				descripcion.append("De ").append(hd.getHoraIniManana()).append(" a ").append(hd.getHoraFinManana());
			}
		}
		else
		{
			if (( hd.getHoraIniTarde() != null ) && ( hd.getHoraFinTarde() != null ))
			{
				descripcion.append("De ").append(hd.getHoraIniTarde()).append(" a ").append(hd.getHoraFinTarde());
			}
		}
		return descripcion.toString();
	}

	@Override
	public List<TipoHorario> buscarTiposHorarios()
	{
		return tipoHorarioDAS.buscar();
	}

	@Override
	public List<TipoHorario> getListaTiposHorario()
	{
		return tipoHorarioDAS.getListaTiposHorario();
	}

	@Override
	public List<TipoHorario> getListaTiposHorarioByServicio(Servicio servicio)
	{
		return tipoHorarioDAS.getListaTiposHorarioByServicio(servicio);
	}

	@Override
	public void guardarTipoHorario( TipoHorario tipoHorario ) throws SaiException
	{
		if (tipoHorario.getCod() == null)
		{
			tipoHorarioDAS.crear( tipoHorario );
		}
		else
		{
			tipoHorarioDAS.modificar( tipoHorario );
		}
	}

	@Override
	public void eliminarTipoHorario( TipoHorario tipoHorario ) throws SaiException
	{
		if (( tipoHorario.getReservableHorarios() != null ) && !tipoHorario.getReservableHorarios().isEmpty())
		{
			throw new SaiException("El tipo de horario está asociado a tipos de reservable");
		}
		if (( tipoHorario.getHorarioDias() != null ) && !tipoHorario.getHorarioDias().isEmpty())
		{
			final Iterator<HorarioDia> itHd = tipoHorario.getHorarioDias().iterator();
			while (itHd.hasNext())
			{
				final HorarioDia hd = itHd.next();
				horarioDiaDAS.eliminar(hd);
			}
		}
		tipoHorarioDAS.eliminar( tipoHorario );
	}

	@Override
	public void guardarHorarioDia(HorarioDia hd) throws SaiException
	{
		if (hd.getCod() == null)
		{
			horarioDiaDAS.crear( hd );
		}
		else
		{
			horarioDiaDAS.modificar( hd );
		}
	}

	@Override
	public void eliminarHorarioDia(HorarioDia hd) throws SaiException
	{
		horarioDiaDAS.eliminar( hd );
	}

	@Override
	public List<TipoHorario> getListaTipoHorarioByUsuario( Usuario usuario ) {
		return tipoHorarioDAS.getListaTiposHorarioByUsuario( usuario );
	}

	@Override
	public List<TipoReservable> getListaTiposReservableByTipoHorario(TipoHorario tipoHorario)
	{
		return tipoReservableDAS.getListaTiposReservableByTipoHorario(tipoHorario);
	}

	@Override
	public boolean existeHorarioDiaByReservableFecha(Equipo reservable, Date fecha)
	{
		final TipoHorario th = tipoHorarioDAS.getTipoHorarioByTipoReservableFecha(reservable.getTipoReservable(), fecha);
		if (th == null)
		{
			return false;
		}
		final HorarioDia hd = horarioDiaDAS.getHorarioDiaByTipohorarioDia(th, UtilDate.getDiaSemana(fecha));
		return hd != null;
	}

	@Override
	public HorarioDia getHorarioDiaByTipohorarioDia(TipoHorario th, int dia)
	{
		return horarioDiaDAS.getHorarioDiaByTipohorarioDia( th, dia );
	}

	@Override
	public List<Reservas> getReservasPorTurno(Equipo reservable, Date fechaInicio, Date fechaFin)
	{
		return reservasDAS.getReservasPorTurno(reservable, fechaInicio, fechaFin);
	}

	@Override
	public List<Reservas> getReservasPorTurnoByTipoReservable(TipoReservable tr, Date fechaInicio, Date fechaFin)
	{
		return reservasDAS.getReservasPorTurnoByTipoReservable(tr, fechaInicio, fechaFin);
	}

	@Override
	public List<Reservas> getReservasPorTurnoTecnico(Usuario usuarioTecnicoAsignado, Date fechaInicio, Date fechaFin)
	{
		return reservasDAS.getReservasPorTurnoTecnico(usuarioTecnicoAsignado, fechaInicio, fechaFin);
	}

	@Override
	public boolean disponibleReservable(Equipo reservable, Date fechaInicio, Date fechaFin)
	{
		final List<Reservas> reservasTurno = reservasDAS.getReservasPorTurno(reservable, fechaInicio, fechaFin);
		return (( reservasTurno == null ) || reservasTurno.isEmpty());
	}

	@Override
	public boolean disponibleTecnico(Usuario usuarioTecnico, Date fechaInicio, Date fechaFin)
	{
		final List<Reservas> reservasTecnicoTurno = reservasDAS.getReservasPorTurnoTecnico(usuarioTecnico, fechaInicio, fechaFin);
		return (( reservasTecnicoTurno == null ) || reservasTecnicoTurno.isEmpty());
	}

	@Override
	public boolean disponibleTecnicoModificacionReserva(Usuario usuarioTecnico, Long codReserva, Date fechaInicio, Date fechaFin)
	{
		final List<Reservas> reservasTecnicoTurno = reservasDAS.getReservasPorTurnoTecnicoDisponibilidadMod(usuarioTecnico, codReserva, fechaInicio, fechaFin);
		return (( reservasTecnicoTurno == null ) || reservasTecnicoTurno.isEmpty());
	}

	@Override
	public List<Reservas> getReservasPorConsumo(Consumo c)
	{
		return reservasDAS.getReservasPorConsumo(c);
	}

	@Override
	public List<Reservas> getReservasActivasPorConsumo(Consumo c)
	{
		return reservasDAS.getReservasActivasPorConsumo(c);
	}

	@Override
	public Date getMinFechaInicioReservaByConsumo(Consumo c)
	{
		final List<Reservas> listaReservas = reservasDAS.getReservasActivasPorConsumo(c);
		if (listaReservas.isEmpty())
		{
			return null;
		}
		return listaReservas.get(0).getFechaInicio();
	}


	@Override
	public List<Equipo> getReservablesByTipoReservable( TipoReservable tipoReservable ) {
		try {
			return equipoDAS.getReservablesByTipoReservable( tipoReservable );
		}catch(final Exception ex) {
			log.error( "Error al recuperar los reservables por tipo de reservable: " , ex.toString() );
			return new ArrayList<>();
		}
	}

	@Override
	public List<ReservableHorario> getListaReservableHorarioByTipoReservable(TipoReservable tr)
	{
		return reservableHorarioDAS.getListaReservableHorarioByTipoReservable( tr );
	}
	
	@Override
	public List<ReservableHorario> getListaReservableHorarioByProducto(Producto producto)
	{
		return reservableHorarioDAS.getListaReservableHorarioByProducto( producto );
	}
	
	// Equipos para prestaciones

	@Override
	public List<Equipo> getEquiposByServicio(Servicio servicio)
	{
		return equipoDAS.getEquiposByServicio(servicio);
	}

	@Override
	public List<EquipoProductividad> getInformeProductividad(Servicio servicio, List<Equipo> listaEquipos, String fechaDesde, String fechaHasta)
	{
		// Obtenemos primero el listado de equipos
		List<Equipo> listaEquiposInforme = null;
		if (( listaEquipos != null ) && !listaEquipos.isEmpty())
		{
			listaEquiposInforme = listaEquipos;
		}
		else
		{
			listaEquiposInforme = equipoDAS.getEquiposByServicio(servicio);
		}
		// Obtenemos informe
		final List<EquipoProductividad> informe = new ArrayList<>();
		for (final Equipo equipo : listaEquiposInforme)
		{
			final EquipoProductividad ep = new EquipoProductividad();
			ep.setEquipo(equipo);
			ep.setImporteCorrectivas(intervencionDAS.busquedaSumImporteByEquipoTipo(equipo, "CORRECTIVA", fechaDesde, fechaHasta));
			ep.setImportePreventivas(intervencionDAS.busquedaSumImporteByEquipoTipo(equipo, "PREVENTIVA", fechaDesde, fechaHasta));
			ep.setImporteEvolutivas(intervencionDAS.busquedaSumImporteByEquipoTipo(equipo, "EVOLUTIVA", fechaDesde, fechaHasta));
			ep.setImporteTotalIntervenciones(ep.getImporteCorrectivas().add(ep.getImportePreventivas()).add(ep.getImporteEvolutivas()));
			ep.setMinutosReserva(consumoDAS.busquedaSumMinutosReservaNoInternosByEquipo(equipo, fechaDesde, fechaHasta));
			ep.setMinutosPrestacion(consumoDAS.busquedaSumMinutosPrestacionNoInternosByEquipo(equipo, fechaDesde, fechaHasta));
			ep.setMinutosTotal(ep.getMinutosReserva()+ep.getMinutosPrestacion());
			ep.setNumUnidadesReserva(consumoDAS.busquedaNumUnidadesReservaNoInternosByEquipo(equipo, fechaDesde, fechaHasta));
			ep.setNumUnidadesPrestacion(consumoDAS.busquedaNumUnidadesPrestacionNoInternosByEquipo(equipo, fechaDesde, fechaHasta));
			ep.setNumUnidadesTotal(ep.getNumUnidadesReserva()+ep.getNumUnidadesPrestacion());
			ep.setImporteReservas(consumoService.busquedaImporteConsumosReservaByEquipo(equipo, fechaDesde, fechaHasta));
			ep.setImportePrestaciones(consumoService.busquedaImporteConsumosPrestacionByEquipo(equipo, fechaDesde, fechaHasta));
			ep.setImporteTotalConsumos(ep.getImporteReservas().add(ep.getImportePrestaciones()));
			informe.add(ep);
		}
		return informe;
	}


	@Override
	public TipoHorario getTipoHorarioByTipoReservableFecha(TipoReservable tr, Date fecha)
	{
		return tipoHorarioDAS.getTipoHorarioByTipoReservableFecha(tr, fecha);
	}


	@Override
	public TipoHorario getTipoHorarioByProductoFecha(Producto producto, Date fecha)
	{
		return tipoHorarioDAS.getTipoHorarioByProductoFecha(producto, fecha);
	}

	@Override
	public HorarioDia getHorarioDiaByProductoFecha(Producto pr, Date fecha)
	{
		HorarioDia hd = null;
		TipoHorario th = tipoHorarioDAS.getTipoHorarioByProductoFecha(pr, fecha);
		if (th != null)
		{
			hd = horarioDiaDAS.getHorarioDiaByTipohorarioDia(th, UtilDate.getDiaSemana(fecha));
		}
		return hd;
	}
	
	@Override
	public List<ReservaEspera> getReservaEsperaByReserva( Reservas r ) {
		return reservaEsperaDAS.getReservaEsperaByReserva( r );
	}

	@Override
	public void guardarReservaEspera(ReservaEspera r) throws SaiException
	{
		if (r.getCod() == null)
		{
			reservaEsperaDAS.crear( r );
		}
		else
		{
			reservaEsperaDAS.modificar( r );
		}
	}

	@Override
	public void eliminarReservaEspera( ReservaEspera r ) throws SaiException
	{
		reservaEsperaDAS.eliminar( r );
	}

	@Override
	public ReservaEspera getReservaEsperaByUsuarioReserva( Usuario u, Reservas r ) {
		return reservaEsperaDAS.getReservaEsperaByUsuarioReserva(u,r);
	}

	@Override
	public String getMinHoraInicioMananaByProductoReservable(Producto producto)
	{
		if (( producto.getTipoReservable() == null ) || "BAJA".equals(producto.getTipoReservable().getEstado()))
		{
			return null;
		}
		return horarioDiaDAS.getMinHoraInicioManana(producto.getTipoReservable());
	}

	@Override
	public String getMinHoraInicioTardeByProductoReservable(Producto producto)
	{
		if (( producto.getTipoReservable() == null ) || "BAJA".equals(producto.getTipoReservable().getEstado()))
		{
			return null;
		}
		return  horarioDiaDAS.getMinHoraInicioTarde(producto.getTipoReservable());
	}

	@Override
	public String getMaxHoraFinMananaByProductoReservable(Producto producto)
	{
		if (( producto.getTipoReservable() == null ) || "BAJA".equals(producto.getTipoReservable().getEstado()))
		{
			return null;
		}
		return  horarioDiaDAS.getMaxHoraFinManana(producto.getTipoReservable());
	}

	@Override
	public String getMaxHoraFinTardeByProductoReservable(Producto producto)
	{
		if (( producto.getTipoReservable() == null ) || "BAJA".equals(producto.getTipoReservable().getEstado()))
		{
			return null;
		}
		return  horarioDiaDAS.getMaxHoraFinTarde(producto.getTipoReservable());
	}

	@Override
	public boolean existeHorarioDia(TipoReservable tr)
	{
		return horarioDiaDAS.existeHorarioDia(tr);
	}

	@Override
	public List<Equipo> getListaReservablesByProducto(Producto producto)
	{
		final List<Equipo> listaReservables = new ArrayList<>();
		if (( producto.getTipoReservable() == null ) || "BAJA".equals(producto.getTipoReservable().getEstado()))
		{
			return listaReservables;
		}
		return equipoDAS.getReservablesByTipoReservable(producto.getTipoReservable());
	}


	@Override
	public void eliminarReservableHorario( ReservableHorario rh ) throws SaiException
	{
		reservableHorarioDAS.eliminar( rh );
	}

	@Override
	public void crearTipoReservableHorario( ReservableHorario rh ) throws SaiException
	{
		if (UtilDate.anteriorOrIgual(rh.getFechaFin(), rh.getFechaInicio()))
		{
			throw new SaiException(srb.getString("tiporeservable.add.horario.error.fechas"));
		}
		if (reservableHorarioDAS.existeTiporeservableHorarioSolapado(rh))
		{
			throw new SaiException(srb.getString("tiporeservable.add.horario.error.solapado"));
		}
		reservableHorarioDAS.crear(rh);
	}

	@Override
	public void crearProductoHorario( ReservableHorario rh ) throws SaiException
	{
		if (UtilDate.anteriorOrIgual(rh.getFechaFin(), rh.getFechaInicio()))
		{
			throw new SaiException(srb.getString("producto.add.horario.error.fechas"));
		}
		if (reservableHorarioDAS.existeProductoHorarioSolapado(rh))
		{
			throw new SaiException(srb.getString("producto.add.horario.error.solapado"));
		}
		reservableHorarioDAS.crear(rh);
	}
	
	@Override
	public List<Equipo> busquedaReservables( Long codTipo, int first, int pageSize, String sortField,
			SortOrder sortOrder,
			Map<String, Object> filters ) {
		return equipoDAS.busquedaReservables( codTipo, first, pageSize, sortField, sortOrder, filters );
	}

	@Override
	public int getCountBusquedaReservables( Long codTipo ) {
		return equipoDAS.getCountBusquedaReservables( codTipo );
	}

	@Override
	public void guardarIntervencion( Intervencion i ) throws SaiException {
		intervencionDAS.guardar( i );
	}

	@Override
	public void eliminarIntervencion( Intervencion i ) throws SaiException {
		intervencionDAS.eliminar( i );
	}

	@Override
	public List<String> getListaNombresDependenciaByPatron(String patron)
	{
		List<HashMap<String, Object>> hmDependencias;
		try
		{
			hmDependencias = saiPao.getDependenciasXNombreTag(patron);
		}
		catch ( final FundeWebJdbcRollBackException e )
		{
			log.error("Error en getListaNombresDependenciaByPatron: ", e);
			return new ArrayList<>();
		}
		return getListaNombresDependencia(hmDependencias);
	}

	@Override
	public Dependencia getDependenciaByDescripcion(String descripcion)
	{
		final String[] descripcionDependencia = descripcion.split(" _ ");
		final String[] datosDependencia = descripcionDependencia[3].split("@");
		final String tipoEspacio = datosDependencia[0];
		final String codEspacio = datosDependencia[1];
		final String bloque = datosDependencia[2];
		final String codPlanta = datosDependencia[3];
		final Integer numDep = Integer.parseInt(datosDependencia[4]);
		return new Dependencia(tipoEspacio, codEspacio, bloque, codPlanta, numDep);
	}

	@Override
	public String getNombreDependencia(String tipo, String codigo, String bloque, String planta, Integer numDep)
	{
		try
		{
			final String nombre = saiPao.getNombreDependencia( tipo, codigo, bloque, planta,
					BigDecimal.valueOf( numDep ) );
			final String[] partesNombre = nombre.split( "_" );
			final StringBuilder resultado = new StringBuilder();
			for ( int i = 0; i < ( partesNombre.length - 1 ); i++ ) {
				resultado.append( partesNombre[i] );
				resultado.append( "-" );
			}
			return resultado.toString().substring(0, resultado.length()-1);
		}
		catch(final Exception ex)
		{
			log.error("Error en getNombreDependencia: ", ex);
			return null;
		}
	}

	private List<String> getListaNombresDependencia(List<HashMap<String, Object>> hmDependencias)
	{
		final List<String> listaNombres = new ArrayList<>(0);
		if (hmDependencias == null)
		{
			return listaNombres;
		}
		try
		{
			for (final HashMap<String, Object> dep: hmDependencias)
			{
				final String nombreEspacio = (String) dep.get("ESP_NOMBRE");
				final String nombreDep = (String) dep.get("NOMBREDEP");
				final String etiquetaDep = (String) dep.get("ETIQUETA_DEP");
				final String tipoEspacio = (String) dep.get("ESP_TIES_CODIGO");
				final String codEspacio = (String) dep.get("ESP_CODIGO");
				final String bloque = (String) dep.get("BLOQUE");
				final String codPlanta = (String) dep.get("CODPLANTA");
				final BigDecimal numDepBd = (BigDecimal) dep.get("NUMDEP");
				final String numDep = numDepBd.toString();
				listaNombres.add(new StringBuilder(nombreEspacio).append(" _ ").append(nombreDep).append(" _ ").append(etiquetaDep).append(" _ ").append(tipoEspacio).append("@").append(codEspacio).append("@").append(bloque).append("@").append(codPlanta).append("@").append(numDep).toString());
			}
		}
		catch (final Exception e)
		{
			log.error("Error extrayendo lista de nombres de dependencias desde cursor:", e);
		}
		return listaNombres;
	}

	@Override
	public List<TipoReservable> getListaTiposReservableByUsuarioConectado() {
		return tipoReservableDAS.getListaTiposReservableByUsuarioConectado();
	}

	@Override
	public void refreshEquipo( Equipo e ) {
		equipoDAS.refresh( e );
	}
}
