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
import org.primefaces.model.UploadedFile;

import es.um.atica.commons.utils.UtilDate;
import es.um.atica.jdbc.exceptions.FundeWebJdbcRollBackException;
import es.um.atica.jpa.das.ResultQuery;
import es.um.atica.sai.das.interfaces.AnexoDAS;
import es.um.atica.sai.das.interfaces.ConsumoCantidadDAS;
import es.um.atica.sai.das.interfaces.ConsumoDAS;
import es.um.atica.sai.das.interfaces.ConsumoEquipoDAS;
import es.um.atica.sai.das.interfaces.EstadisticaConsumoDAS;
import es.um.atica.sai.das.interfaces.FacturaDAS;
import es.um.atica.sai.das.interfaces.IrEntidadCantidadDAS;
import es.um.atica.sai.das.interfaces.IrGrupoinvestigacionCantidadDAS;
import es.um.atica.sai.das.interfaces.MuestraDAS;
import es.um.atica.sai.das.interfaces.ReservaEsperaDAS;
import es.um.atica.sai.das.interfaces.ReservasDAS;
import es.um.atica.sai.das.interfaces.TecnicoProductoDAS;
import es.um.atica.sai.das.interfaces.UsuarioDAS;
import es.um.atica.sai.dtos.ConsumoCantidad;
import es.um.atica.sai.dtos.EntidadRespuesta;
import es.um.atica.sai.dtos.EstadisticaConsumo;
import es.um.atica.sai.dtos.EstadoConsumo;
import es.um.atica.sai.dtos.EstadoPresupuesto;
import es.um.atica.sai.dtos.IrEntidadCantidad;
import es.um.atica.sai.dtos.IrGrupoinvestigacionCantidad;
import es.um.atica.sai.dtos.LineaFungible;
import es.um.atica.sai.dtos.OcupacionTecnico;
import es.um.atica.sai.dtos.OpcionSolicitudTecnico;
import es.um.atica.sai.dtos.OperacionConsumo;
import es.um.atica.sai.dtos.OperacionPresupuesto;
import es.um.atica.sai.dtos.Partida;
import es.um.atica.sai.dtos.ResumenConsumosExcelRespuesta;
import es.um.atica.sai.dtos.Subtercero;
import es.um.atica.sai.dtos.TipoPerfil;
import es.um.atica.sai.entities.Anexo;
import es.um.atica.sai.entities.Consumo;
import es.um.atica.sai.entities.ConsumoEquipo;
import es.um.atica.sai.entities.EntidadPagadora;
import es.um.atica.sai.entities.Equipo;
import es.um.atica.sai.entities.Factura;
import es.um.atica.sai.entities.GrupoInvestigacion;
import es.um.atica.sai.entities.Muestra;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.Reservas;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.TecnicoProducto;
import es.um.atica.sai.entities.TipoCertificacion;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;
import es.um.atica.sai.paos.interfaces.SaiPao;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.CertificacionesService;
import es.um.atica.sai.services.interfaces.ConsumoService;
import es.um.atica.sai.services.interfaces.KronService;
import es.um.atica.sai.services.interfaces.MensajeService;
import es.um.atica.sai.services.interfaces.ReservableService;
import es.um.atica.sai.services.interfaces.ServicioService;
import es.um.atica.sai.services.interfaces.TarifaService;
import es.um.atica.sai.services.interfaces.UsuarioService;

@Stateless
@Name( ConsumoServiceImpl.NAME )
public class ConsumoServiceImpl implements ConsumoService {

	public static final String NAME = "consumoService";

	@Logger
	Log log;

	@In(value="consumoDAS", create=true)
	private ConsumoDAS consumoDAS;

	@In(create=true)
	private ConsumoEquipoDAS consumoEquipoDAS;
	
	@In(create=true)
	private ConsumoCantidadDAS consumoCantidadDAS;
	
	@In(create=true)
	private IrEntidadCantidadDAS irEntidadCantidadDAS;
	
	@In(create=true)
	private IrGrupoinvestigacionCantidadDAS irGrupoinvestigacionCantidadDAS;

	@In(create=true)
	private ReservasDAS reservasDAS;

	@In(create=true)
	private ReservaEsperaDAS reservaEsperaDAS;

	@In(create=true)
	private MuestraDAS muestraDAS;

	@In(create = true )
	private AnexoDAS anexoDAS;

	@In(create = true )
	private FacturaDAS facturaDAS;

	@In(create = true )
	private UsuarioDAS usuarioDAS;

	@In( create = true )
	private EstadisticaConsumoDAS estadisticaConsumoDAS;

	@In(create = true )
	private TarifaService tarifaService;

	@In(create = true )
	private ServicioService servicioService;

	@In(create = true )
	private UsuarioService usuarioService;

	@In(create = true )
	private MensajeService mensajeService;

	@In(create = true)
	private ReservableService reservableService;

	@In(create = true)
	private CertificacionesService certificacionesService;

	@In( create = true, required = true )
	KronService kronService;

	@In(value= "saiPao", create=true)
	private SaiPao saiPao;

	@In(create=true)
	private TecnicoProductoDAS tecnicoDAS;

	@In("org.jboss.seam.security.identity")
	SaiIdentity identity;

	private final ResourceBundle srb = SeamResourceBundle.getBundle();
	
	private static final String FORMATO_FECHA = "dd/MM/yyyy";
	private static final String TIPO_ANEXO_CONSUMO_BIOSEG = "CONSUMO_BIOSEG";

	@Override
	public void guardarConsumo(Consumo consumo) throws SaiException
	{
		if (consumo.getCod() == null)
		{
			consumoDAS.crear(consumo);
		}
		else
		{
			consumoDAS.modificar(consumo);
		}
		if ("P".equals(consumo.getTipo()) && "NO".equals(consumo.getPresupuesto()))
		{
			if (EstadoConsumo.ACEPTADO.equals(consumo.getEstado()))
			{
				kronService.prestacionAceptada(consumo);
			}
			else if (EstadoConsumo.ANULADO.equals(consumo.getEstado()) || EstadoConsumo.RECHAZADO.equals(consumo.getEstado()))
			{
				kronService.prestacionCancelada(consumo);
			}
		}
	}

	@Override
	public void guardarEstadoPresupuestoEHijos(Consumo consumo) throws SaiException
	{
		for (Consumo ch : this.getConsumosHijos(consumo))
		{
			ch.setEstadoPresupuesto(consumo.getEstadoPresupuesto());
			guardarEstadoPresupuestoEHijos(ch);
		}
		consumoDAS.modificar(consumo);
	}
	
	
	@Override
	public void guardarListaConsumos(List<Consumo> listaConsumos)
	{
		for (final Consumo con: listaConsumos)
		{
			try
			{
				guardarConsumo( con);
			}
			catch ( final Exception ex )
			{
				log.error("Error al guardar consumo: #0", ex.getMessage() );
			}
		}
	}

	@Override
	public void guardarPedidoFungible(List<Consumo> listaConsumos, List<LineaFungible> listaLineasEliminadas) throws SaiException
	{
		// Primero eliminamos los consumos indicados en la lista de líneas eliminadas
		for (final LineaFungible lf: listaLineasEliminadas)
		{
			if (lf.getCod() != null)
			{
				final Consumo consumo = getConsumoById(lf.getCod());
				consumoDAS.eliminar( consumo );
			}
		}
		// Ahora modificamos/damos de alta la lista de consumos
		for (final Consumo con: listaConsumos)
		{
			if (con.getCod() == null)
			{
				consumoDAS.crear(con);
			}
			else
			{
				if (EstadoConsumo.FINALIZADO.equals(con.getEstado()) && con.getFechaAceptacion() == null)
				{
					con.setFechaAceptacion(new Date());
				}
				consumoDAS.modificar(con);
			}
		}
	}


	@Override
	public void guardarPedidoFungibleYConsumoPadre(Consumo consumoPadre, List<Consumo> listaConsumos, List<LineaFungible> listaLineasEliminadas) throws SaiException
	{
		// Primero eliminamos los consumos indicados en la lista de líneas eliminadas
		for (final LineaFungible lf: listaLineasEliminadas)
		{
			if (lf.getCod() != null)
			{
				final Consumo consumo = getConsumoById(lf.getCod());
				consumoDAS.eliminar( consumo );
			}
		}
		// Ahora modificamos/damos de alta la lista de consumos
		for (final Consumo con: listaConsumos)
		{
			if (con.getCod() == null)
			{
				consumoDAS.crear(con);
			}
			else
			{
				if (EstadoConsumo.FINALIZADO.equals(con.getEstado()) && con.getFechaAceptacion() == null)
				{
					con.setFechaAceptacion(new Date());
				}
				consumoDAS.modificar(con);
			}
		}
		// Finalmente guardamos las modificaciones en el padre
		consumoDAS.modificar(consumoPadre);
	}

	private void comprobacionDisponibilidadReserva(Consumo consumo, Reservas reserva) throws SaiException
	{
		if (!reservableService.disponibleReservable(reserva.getReservable(), reserva.getFechaInicio(), reserva.getFechaFin()))
		{
			throw new SaiException(new StringBuilder("No se puede reservar ")
					.append(reserva.getReservable().getDescripcion())
					.append(" el día ").append(UtilDate.dateToString(reserva.getFechaInicio(), FORMATO_FECHA))
					.append(" de ").append(UtilDate.convertirDateHoraToString(reserva.getFechaInicio(), true))
					.append(" a ").append(UtilDate.convertirDateHoraToString(reserva.getFechaFin(), true))
					.append(": Durante el transcurso de su reserva, antes de guardar su solicitud alguien lo ha reservado").toString());
		}
		if (requiereTecnico(consumo) && ( consumo.getUsuarioTecnicoAsignado() != null ) && !reservableService.disponibleTecnico(consumo.getUsuarioTecnicoAsignado(), reserva.getFechaInicioTecnico(), reserva.getFechaFinTecnico()))
		{
			throw new SaiException(new StringBuilder("No se puede reservar ")
					.append(reserva.getReservable().getDescripcion())
					.append(" el día ").append(UtilDate.dateToString(reserva.getFechaInicio(), FORMATO_FECHA))
					.append(" de ").append(UtilDate.convertirDateHoraToString(reserva.getFechaInicio(), true))
					.append(" a ").append(UtilDate.convertirDateHoraToString(reserva.getFechaFin(), true))
					.append(": Durante el transcurso de su reserva, antes de guardar su solicitud, el técnico asignado ha dejado de estar disponible").toString());
		}
	}

	private void estableceFechaFinTecnico(OcupacionTecnico ot, Date fechaFinTecnico, Date fechaFin)
	{
		// Como se puede dar el caso de que una reserva tenga una duración menor que los minutos del turno, establecemos como máximo la fecha de fin de la reserva
		if (( fechaFinTecnico != null ) && UtilDate.anterior(fechaFinTecnico, fechaFin))
		{
			ot.setFechaFinTecnico(fechaFinTecnico);
		}
		else
		{
			ot.setFechaFinTecnico(fechaFin);
		}
	}

	@Override
	public OcupacionTecnico obtenerOcupacionTecnico(Consumo consumo, Date fechaInicio, Date fechaFin)
	{
		final OcupacionTecnico ot = new OcupacionTecnico();
		if (requiereTecnico(consumo))
		{
			Date fechaFinTecnico = null;
			ot.setFechaInicioTecnico(fechaInicio);
			if ("SI".equals(consumo.getProducto().getRequiereTecnico()))
			{
				if (consumo.getProducto().getTipoReservable().getMinutosTecnico() != null)
				{
					fechaFinTecnico = UtilDate.sumarMinutos(ot.getFechaInicioTecnico(), consumo.getProducto().getTipoReservable().getMinutosTecnico());
				}
			}
			else if ("PR".equals(consumo.getProducto().getRequiereTecnico()))
			{
				if ("BA".equals(consumo.getSolicitudTecnico()) && ( consumo.getProducto().getTipoReservable().getMinutosTecnicoBajo() != null ))
				{
					fechaFinTecnico = UtilDate.sumarMinutos(ot.getFechaInicioTecnico(), consumo.getProducto().getTipoReservable().getMinutosTecnicoBajo());
				}
				else if ("ME".equals(consumo.getSolicitudTecnico()) && ( consumo.getProducto().getTipoReservable().getMinutosTecnicoMedio() != null ))
				{
					fechaFinTecnico = UtilDate.sumarMinutos(ot.getFechaInicioTecnico(), consumo.getProducto().getTipoReservable().getMinutosTecnicoMedio());
				}
				else if ("AL".equals(consumo.getSolicitudTecnico()) && ( consumo.getProducto().getTipoReservable().getMinutosTecnicoAlto() != null ))
				{
					fechaFinTecnico = UtilDate.sumarMinutos(ot.getFechaInicioTecnico(), consumo.getProducto().getTipoReservable().getMinutosTecnicoAlto());
				}
			}
			estableceFechaFinTecnico(ot, fechaFinTecnico, fechaFin);
		}
		return ot;
	}

	@Override
	public String getDescripcionOpcionSolicitudTecnico(Consumo consumo)
	{
		if ("NO".equals(consumo.getSolicitudTecnico()))
		{
			return "No requiere técnico";
		}
		else if ("BA".equals(consumo.getSolicitudTecnico()) && ( consumo.getProducto().getTipoReservable().getMinutosTecnicoBajo() != null ))
		{
			return new StringBuilder("Requiere técnico con ocupación baja (").append(consumo.getProducto().getTipoReservable().getMinutosTecnicoBajo().toString()).append(" minutos por reserva)").toString();
		}
		else if ("ME".equals(consumo.getSolicitudTecnico()) && ( consumo.getProducto().getTipoReservable().getMinutosTecnicoMedio() != null ))
		{
			return new StringBuilder("Requiere técnico con ocupación media (").append(consumo.getProducto().getTipoReservable().getMinutosTecnicoMedio().toString()).append(" minutos por reserva)").toString();
		}
		else if ("AL".equals(consumo.getSolicitudTecnico()) && ( consumo.getProducto().getTipoReservable().getMinutosTecnicoAlto() != null ))
		{
			return new StringBuilder("Requiere técnico con ocupación alta (").append(consumo.getProducto().getTipoReservable().getMinutosTecnicoAlto().toString()).append(" minutos por reserva)").toString();
		}
		else if (consumo.getProducto().getTipoReservable().getMinutosTecnico() != null)
		{
			return new StringBuilder("Requiere técnico (").append(consumo.getProducto().getTipoReservable().getMinutosTecnico().toString()).append(" minutos por reserva)").toString();
		}
		else
		{
			return new StringBuilder("Requiere técnico (").append(consumo.getProducto().getTipoReservable().getDuracionMinima().toString()).append(" minutos por reserva)").toString();
		}

	}

	@Override
	public List<OpcionSolicitudTecnico> getListaOpcionesSolicitudTecnico(Consumo consumo)
	{
		final List<OpcionSolicitudTecnico> listaOst = new ArrayList<>();
		if (!"PR".equals(consumo.getProducto().getRequiereTecnico()) || ( consumo.getProducto().getTipoReservable() == null ))
		{
			return listaOst;
		}
		if (( consumo.getCod() == null ) || identity.tienePerfilEnServicio(TipoPerfil.SUPERVISOR, consumo.getProducto().getServicio().getCod()) ||
				((EstadoConsumo.PENDIENTE.equals(consumo.getEstado()) || EstadoConsumo.PEND_VALIDACION_IR.equals(consumo.getEstado())) && (esIrSolicitud(consumo, consumo.getProducto().getServicio()) || esMiembroSolicitud(consumo, consumo.getProducto().getServicio()))))
		{
			// La opción de no requerir técnico no puede aparecerle al técnico de la solicitud en modificación, porque dejaría de ser el técnico de la solicitud y perdería los permisos sobre ella
			listaOst.add(new OpcionSolicitudTecnico("NO", "No requiero técnico"));
		}
		if (( consumo.getProducto().getTipoReservable().getMinutosTecnicoBajo() == null ) && ( consumo.getProducto().getTipoReservable().getMinutosTecnicoMedio() == null ) && ( consumo.getProducto().getTipoReservable().getMinutosTecnicoAlto() == null ))
		{
			if (consumo.getProducto().getTipoReservable().getMinutosTecnico() != null)
			{
				listaOst.add(new OpcionSolicitudTecnico("SI", new StringBuilder("Sí requiero técnico (").append(consumo.getProducto().getTipoReservable().getMinutosTecnico().toString()).append(" minutos por reserva)").toString()));
			}
			else
			{
				listaOst.add(new OpcionSolicitudTecnico("SI", new StringBuilder("Sí requiero técnico (").append(consumo.getProducto().getTipoReservable().getDuracionMinima().toString()).append(" minutos por reserva)").toString()));
			}
		}
		if (consumo.getProducto().getTipoReservable().getMinutosTecnicoBajo() != null)
		{
			listaOst.add(new OpcionSolicitudTecnico("BA", new StringBuilder("Sí.  Requiero técnico con ocupación baja (").append(consumo.getProducto().getTipoReservable().getMinutosTecnicoBajo().toString()).append(" minutos por reserva)").toString()));
		}
		if (consumo.getProducto().getTipoReservable().getMinutosTecnicoMedio() != null)
		{
			listaOst.add(new OpcionSolicitudTecnico("ME", new StringBuilder("Sí.  Requiero técnico con ocupación media (").append(consumo.getProducto().getTipoReservable().getMinutosTecnicoMedio().toString()).append(" minutos por reserva)").toString()));
		}
		if (consumo.getProducto().getTipoReservable().getMinutosTecnicoAlto() != null)
		{
			listaOst.add(new OpcionSolicitudTecnico("AL", new StringBuilder("Sí.  Requiero técnico con ocupación alta (").append(consumo.getProducto().getTipoReservable().getMinutosTecnicoAlto().toString()).append(" minutos por reserva)").toString()));
		}
		return listaOst;
	}

	@Override
	public void crearPedidoReserva(Consumo consumo, List<Reservas> listaReservas, UploadedFile ficheroAnexo, UploadedFile ficheroAnexoBioseguridad) throws SaiException
	{
		// Creamos el consumo
		if (EstadoConsumo.ACEPTADO.equals(consumo.getEstado()))
		{
			consumo.setFechaAceptacion(new Date());
		}
		consumoDAS.crear(consumo);
		// Guardamos anexos si hay
		if (ficheroAnexo != null)
		{
			final Anexo anexo = new Anexo();
			anexo.setNomDocumento(ficheroAnexo.getFileName());
			anexo.setDocumento(ficheroAnexo.getContents());
			anexo.setConsumo(consumo);
			anexo.setTipo("CONSUMO");
			anexoDAS.crear(anexo);
		}
		if (ficheroAnexoBioseguridad != null)
		{
			final Anexo anexoBioseguridad = new Anexo();
			anexoBioseguridad.setNomDocumento(ficheroAnexoBioseguridad.getFileName());
			anexoBioseguridad.setDocumento(ficheroAnexoBioseguridad.getContents());
			anexoBioseguridad.setConsumo(consumo);
			anexoBioseguridad.setTipo(TIPO_ANEXO_CONSUMO_BIOSEG);
			anexoDAS.crear(anexoBioseguridad);
		}
		// Guardamos las reservas
		for (final Reservas reserva: listaReservas)
		{
			comprobacionDisponibilidadReserva(consumo, reserva);
			reserva.setConsumo(consumo);
			if (EstadoConsumo.ACEPTADO.equals(consumo.getEstado()))
			{
				// Si el consumo se crea directamente Aceptado, todas las reservas deben estar aceptadas
				reserva.setEstado("Aceptada");
			}
			reservasDAS.crear(reserva);
			if (EstadoConsumo.ACEPTADO.equals(consumo.getEstado()))
			{
				// Si el consumo se crea directamente Aceptado, generamos las solicitudes de autorización para Kron si procede
				kronService.reservaAceptada(reserva);
			}
		}
	}

	@Override
	public void modificarPedidoReserva(Consumo consumo, UploadedFile ficheroAnexoBioseguridad, List<Reservas> listaReservas, List<Reservas> listaReservasEliminadas) throws SaiException
	{
		// Guardamos el consumo
		if (EstadoConsumo.ACEPTADO.equals(consumo.getEstado()) && consumo.getFechaAceptacion()==null)
		{
			consumo.setFechaAceptacion(new Date());
		}
		consumoDAS.modificar(consumo);
		// Comprobamos si hay un anexo sobre bioseguridad que hay que borrar
		final Anexo anexoBioseguridadCompletado = getAnexoBioseguridadByConsumo(consumo);
		if (( anexoBioseguridadCompletado != null ) && !requiereAnexoNivelBioseguridad(consumo))
		{
			anexoDAS.eliminar(anexoBioseguridadCompletado);
		}
		// Guardamos anexo de bioseguridad si hay
		if (ficheroAnexoBioseguridad != null)
		{
			final Anexo anexoBioseguridad = new Anexo();
			anexoBioseguridad.setNomDocumento(ficheroAnexoBioseguridad.getFileName());
			anexoBioseguridad.setDocumento(ficheroAnexoBioseguridad.getContents());
			anexoBioseguridad.setConsumo(consumo);
			anexoBioseguridad.setTipo(TIPO_ANEXO_CONSUMO_BIOSEG);
			anexoDAS.crear(anexoBioseguridad);
		}
		// Modificamos estado de las reservas si es necesario
		for (final Reservas reserva: listaReservas)
		{
			// No comprobamos disponibilidad porque no hay reservas nuevas y si se ha modificado el técnico, su disponibilidad se ha comprobado antes de llamar a este método
			if (EstadoConsumo.ANULADO.equals(consumo.getEstado()))
			{
				//Eliminamos la reserva kron correspondiente si la hay
				reserva.setEstado("Anulada");
				kronService.reservaCancelada(reserva);
			}
			else if (EstadoConsumo.RECHAZADO.equals(consumo.getEstado()))
			{
				//Eliminamos la reserva kron correspondiente si la hay
				reserva.setEstado("Rechazada");
				kronService.reservaCancelada(reserva);
			}
			else if (EstadoConsumo.ACEPTADO.equals(consumo.getEstado()))
			{
				// Si el consumo se valida, todas las reservas se aceptan
				reserva.setEstado("Aceptada");
				kronService.reservaAceptada(reserva);
			}
			reservasDAS.modificar(reserva);

		}
		// Eliminamos reservas eliminadas
		for (final Reservas reservaEliminar: listaReservasEliminadas)
		{
			if (EstadoConsumo.ACEPTADO.equals(consumo.getEstado()))
			{
				//Eliminamos la reserva kron correspondiente si la hay
				kronService.reservaCancelada(reservaEliminar);
			}
			mensajeService.avisosEliminacionReserva(reservaEliminar);
			reservasDAS.eliminar(reservaEliminar);
		}
	}

	@Override
	public BigDecimal busquedaImporteConsumosReservaByEquipo(Equipo equipo, String fechaDesde, String fechaHasta)
	{
		List<Consumo> listaConsumos = consumoDAS.busquedaConsumosReservaNoInternosByEquipo(equipo, fechaDesde, fechaHasta);
		return this.getImporteTotalListaConsumos(listaConsumos);
	}
	
	private BigDecimal getImporteTotalListaConsumos(List<Consumo> listaConsumos)
	{
		BigDecimal importeTotal = BigDecimal.valueOf(0);
		for (Consumo consumo: listaConsumos)
		{
			if (consumo.getImporteTarifaFactura()!=null)
			{
				importeTotal = importeTotal.add(consumo.getImporteTarifaFactura());
			}
			else if (consumo.getEntidadPagadora() != null)
			{
				BigDecimal precioConsumo = tarifaService.obtenerPrecioConsumo(consumo, consumo.getEntidadPagadora());
				if (precioConsumo != null)
				{
					importeTotal = importeTotal.add(precioConsumo);
				}
			}
		}
		return importeTotal;
	}
	
	@Override
	public void crearPedidoPrestacion(Consumo consumo, UploadedFile ficheroAnexo, UploadedFile ficheroAnexoBioseguridad) throws SaiException
	{
		// Creamos el consumo
		if (EstadoConsumo.ACEPTADO.equals(consumo.getEstado()))
		{
			consumo.setFechaAceptacion(new Date());
		}
		consumoDAS.crear(consumo);
		if ("NO".equals(consumo.getPresupuesto()) && EstadoConsumo.ACEPTADO.equals(consumo.getEstado()))
		{
			kronService.prestacionAceptada(consumo);
		}
		// Guardamos anexos si hay
		if (ficheroAnexo != null)
		{
			final Anexo anexo = new Anexo();
			anexo.setNomDocumento(ficheroAnexo.getFileName());
			anexo.setDocumento(ficheroAnexo.getContents());
			anexo.setConsumo(consumo);
			anexo.setTipo("CONSUMO");
			anexoDAS.crear(anexo);
		}
		if (ficheroAnexoBioseguridad != null)
		{
			final Anexo anexoBioseguridad = new Anexo();
			anexoBioseguridad.setNomDocumento(ficheroAnexoBioseguridad.getFileName());
			anexoBioseguridad.setDocumento(ficheroAnexoBioseguridad.getContents());
			anexoBioseguridad.setConsumo(consumo);
			anexoBioseguridad.setTipo(TIPO_ANEXO_CONSUMO_BIOSEG);
			anexoDAS.crear(anexoBioseguridad);
		}
	}

	@Override
	public void modificarPedidoPrestacion(Consumo consumo, UploadedFile ficheroAnexoBioseguridad) throws SaiException
	{
		// Modificamos el consumo
		if (EstadoConsumo.ACEPTADO.equals(consumo.getEstado()) && consumo.getFechaAceptacion()==null)
		{
			consumo.setFechaAceptacion(new Date());
			if ("NO".equals(consumo.getPresupuesto()))
			{
				kronService.prestacionAceptada(consumo);
			}
		}
		consumoDAS.modificar(consumo);
		// Comprobamos si hay un anexo sobre bioseguridad que hay que borrar
		final Anexo anexoBioseguridadCompletado = getAnexoBioseguridadByConsumo(consumo);
		if ((anexoBioseguridadCompletado != null) && !requiereAnexoNivelBioseguridad(consumo))
		{
			anexoDAS.eliminar(anexoBioseguridadCompletado);
		}
		// Guardamos anexo de bioseguridad si hay
		if (ficheroAnexoBioseguridad != null)
		{
			final Anexo anexoBioseguridad = new Anexo();
			anexoBioseguridad.setNomDocumento(ficheroAnexoBioseguridad.getFileName());
			anexoBioseguridad.setDocumento(ficheroAnexoBioseguridad.getContents());
			anexoBioseguridad.setConsumo(consumo);
			anexoBioseguridad.setTipo(TIPO_ANEXO_CONSUMO_BIOSEG);
			anexoDAS.crear(anexoBioseguridad);
		}
	}

	@Override
	public void modificarPresupuestoPrestacion(Consumo consumo, UploadedFile ficheroAnexoBioseguridad) throws SaiException
	{
		// Modificamos el consumo
		if (EstadoConsumo.ACEPTADO.equals(consumo.getEstado()) && consumo.getFechaAceptacion()==null)
		{
			consumo.setFechaAceptacion(new Date());
		}
		this.guardarEstadoPresupuestoEHijos(consumo);
		// Comprobamos si hay un anexo sobre bioseguridad que hay que borrar
		final Anexo anexoBioseguridadCompletado = getAnexoBioseguridadByConsumo(consumo);
		if (( anexoBioseguridadCompletado != null ) && !requiereAnexoNivelBioseguridad(consumo))
		{
			anexoDAS.eliminar(anexoBioseguridadCompletado);
		}
		// Guardamos anexo de bioseguridad si hay
		if (ficheroAnexoBioseguridad != null)
		{
			final Anexo anexoBioseguridad = new Anexo();
			anexoBioseguridad.setNomDocumento(ficheroAnexoBioseguridad.getFileName());
			anexoBioseguridad.setDocumento(ficheroAnexoBioseguridad.getContents());
			anexoBioseguridad.setConsumo(consumo);
			anexoBioseguridad.setTipo(TIPO_ANEXO_CONSUMO_BIOSEG);
			anexoDAS.crear(anexoBioseguridad);
		}
	}
	
	@Override
	public Consumo getConsumoById( Long consumo ) {
		return consumoDAS.find( consumo );
	}

	@Override
	public Consumo getConsumoGeneradoFromPresupuesto(Consumo presupuesto)
	{
		return consumoDAS.getConsumoGeneradoFromPresupuesto(presupuesto);
	}
	
	@Override
	public Factura getFacturaByCod(Long codFactura)
	{
		return facturaDAS.find(codFactura);
	}

	@Override
	public boolean requiereAnexoNivelBioseguridad(Consumo consumo)
	{
		return "NCB1".equals(consumo.getNivelBioseguridad()) || "NCB2".equals(consumo.getNivelBioseguridad());
	}

	@Override
	public Usuario obtenerSolicitante(Consumo consumo)
	{
		Usuario solicitante = null;
		if (consumo.getConsumoPadre() != null)
		{
			solicitante = consumo.getConsumoPadre().getUsuarioSolicitante();
		}
		else
		{
			solicitante = consumo.getUsuarioSolicitante() == null ? identity.getUsuarioConectado() : consumo.getUsuarioSolicitante();
		}
		return solicitante;
	}

	@Override
	public TipoCertificacion obtenerCertificacionNecesaria(Usuario usuario, Producto producto)
	{

		final List<TipoCertificacion> listaTiposCertificacion = certificacionesService.getListaTiposCertificacionesByProducto(producto);
		if (!listaTiposCertificacion.isEmpty())
		{
			final Iterator<TipoCertificacion> itTc = listaTiposCertificacion.iterator();
			while (itTc.hasNext())
			{
				final TipoCertificacion tc = itTc.next();
				if (!certificacionesService.existeCertificacionActivaByTipoAndUsuario(tc, usuario))
				{
					return tc;
				}
			}
		}
		return null;
	}
	
	
	private Long getCodPresupuestoOrigen(Consumo presupuesto)
	{
		if (presupuesto.getConsumoPadre() == null)
		{
			return presupuesto.getCod();
		}
		else
		{
			Consumo presupuestoAux = presupuesto.getConsumoPadre();
			Long cod = presupuestoAux.getCod();
			while (presupuestoAux.getConsumoPadre() != null)
			{
				presupuestoAux = presupuestoAux.getConsumoPadre();
				cod = presupuestoAux.getCod();
			}
			return cod;
		}
	}
	
	@Override
	public Consumo guardaPresupuestoYGeneraConsumoFromPresupuesto(Consumo presupuesto, 
																  UploadedFile ficheroAnexoBioseguridad,
																  Consumo consumoPadre,
																  Date fechaActual) throws SaiException
	{
		// Establecemos los cambios en el presupuesto e hijos (si no se ha llamado al método recursivamente)
		if (consumoPadre == null)
		{
			this.modificarPresupuestoPrestacion(presupuesto, ficheroAnexoBioseguridad);
			this.recargarConsumo(presupuesto);
		}
		// Generamos consumo a partir del presupuesto
		Consumo nuevoConsumo = new Consumo();
		nuevoConsumo.setTipo(presupuesto.getTipo());
		nuevoConsumo.setInterno(presupuesto.getInterno());
		nuevoConsumo.setOrganizacion(presupuesto.getOrganizacion());
		// Los fungibles hijos los generamos como pendientes ya que es el único caso en que un IR genera consumos hijos
		nuevoConsumo.setEstado("F".equals(presupuesto.getTipo()) ? EstadoConsumo.PENDIENTE : presupuesto.getEstado());
		nuevoConsumo.setObservaciones(presupuesto.getObservaciones());
		nuevoConsumo.setUsuarioConectado(identity.getUsuarioConectado());
		nuevoConsumo.setUsuarioSolicitante(presupuesto.getUsuarioSolicitante());
		nuevoConsumo.setUsuarioIrAsociado(presupuesto.getUsuarioIrAsociado());
		nuevoConsumo.setUsuarioTecnicoAsignado(presupuesto.getUsuarioTecnicoAsignado());
		nuevoConsumo.setEntidadPagadora(presupuesto.getEntidadPagadora());
		nuevoConsumo.setGrupoInvestigacion(presupuesto.getGrupoInvestigacion());
		nuevoConsumo.setProducto(presupuesto.getProducto());
		nuevoConsumo.setCantidad(presupuesto.getCantidad());
		nuevoConsumo.setSolicitudTecnico(presupuesto.getSolicitudTecnico());
		nuevoConsumo.setNivelBioseguridad(presupuesto.getNivelBioseguridad());
		nuevoConsumo.setProyecto(presupuesto.getProyecto());
		nuevoConsumo.setFechaSolicitud(fechaActual);
		if (EstadoConsumo.ACEPTADO.equals(nuevoConsumo.getEstado()))
		{
			nuevoConsumo.setFechaAceptacion(fechaActual);
		}
		nuevoConsumo.setPresupuesto("NO");
		nuevoConsumo.setPresupuestoOrigen(presupuesto);
		nuevoConsumo.setComentarioFacturacion(new StringBuilder("Generado a partir del presupuesto ").append(this.getCodPresupuestoOrigen(presupuesto).toString()).toString());
		nuevoConsumo.setConsumoPadre(consumoPadre);
		consumoDAS.crear(nuevoConsumo);
		if ("P".equals(nuevoConsumo.getTipo()) && EstadoConsumo.ACEPTADO.equals(nuevoConsumo.getEstado()))
		{
			kronService.prestacionAceptada(nuevoConsumo);
		}
		this.recargarConsumo(nuevoConsumo);
		// Clonamos los anexos (bioseguridad incluído)
		List<Anexo> listaAnexosPresupuesto = anexoDAS.getAnexosByConsumo(presupuesto);
		for (Anexo anexoPresupuesto: listaAnexosPresupuesto)
		{
			Anexo nuevoAnexo = new Anexo();	
			nuevoAnexo.setConsumo(nuevoConsumo);
			nuevoAnexo.setNomDocumento(anexoPresupuesto.getNomDocumento());
			nuevoAnexo.setDocumento(anexoPresupuesto.getDocumento());
			nuevoAnexo.setTipo(anexoPresupuesto.getTipo());
			anexoDAS.crear(nuevoAnexo);
		}
		// Clonamos las muestras
		for (Muestra muestraPresupuesto: presupuesto.getMuestras())
		{
			Muestra nuevaMuestra = new Muestra();
			nuevaMuestra.setConsumo(nuevoConsumo);
			nuevaMuestra.setReferencia(muestraPresupuesto.getReferencia());
			nuevaMuestra.setDescripcion(muestraPresupuesto.getDescripcion());
			nuevaMuestra.setFechaEntrega(fechaActual);
		}
		// Clonamos los consumos hijos (menos las reservas)
		for (Consumo presupuestoHijo: this.getConsumosHijos(presupuesto))
		{
			if (!"R".equals(presupuestoHijo.getTipo()))
			{
				this.guardaPresupuestoYGeneraConsumoFromPresupuesto(presupuestoHijo, null, nuevoConsumo, fechaActual); 
			}
		}
		this.recargarConsumo(nuevoConsumo);
		return nuevoConsumo;
	}

	
	@Override
	public Consumo clonaPresupuesto(Consumo presupuesto, 
									UploadedFile ficheroAnexoBioseguridad,
									Consumo presupuestoPadre,
									Date fechaActual) throws SaiException
	{
		// Establecemos los cambios en el presupuesto e hijos (si no se ha llamado al método recursivamente)
		if (presupuestoPadre == null)
		{
			this.modificarPresupuestoPrestacion(presupuesto, ficheroAnexoBioseguridad);
			this.recargarConsumo(presupuesto);
		}
		// Generamos presupuesto a partir del presupuesto
		Consumo nuevoPresupuesto = new Consumo();
		nuevoPresupuesto.setTipo(presupuesto.getTipo());
		nuevoPresupuesto.setInterno(presupuesto.getInterno());
		nuevoPresupuesto.setOrganizacion(presupuesto.getOrganizacion());
		nuevoPresupuesto.setEstado(presupuesto.getEstado());
		nuevoPresupuesto.setEstadoPresupuesto(EstadoPresupuesto.SOLICITADO);
		nuevoPresupuesto.setObservaciones(presupuesto.getObservaciones());
		nuevoPresupuesto.setUsuarioConectado(identity.getUsuarioConectado());
		nuevoPresupuesto.setUsuarioSolicitante(presupuesto.getUsuarioSolicitante());
		nuevoPresupuesto.setUsuarioIrAsociado(presupuesto.getUsuarioIrAsociado());
		nuevoPresupuesto.setUsuarioTecnicoAsignado(presupuesto.getUsuarioTecnicoAsignado());
		nuevoPresupuesto.setEntidadPagadora(presupuesto.getEntidadPagadora());
		nuevoPresupuesto.setGrupoInvestigacion(presupuesto.getGrupoInvestigacion());
		nuevoPresupuesto.setProducto(presupuesto.getProducto());
		nuevoPresupuesto.setCantidad(presupuesto.getCantidad());
		nuevoPresupuesto.setSolicitudTecnico(presupuesto.getSolicitudTecnico());
		nuevoPresupuesto.setNivelBioseguridad(presupuesto.getNivelBioseguridad());
		nuevoPresupuesto.setProyecto(presupuesto.getProyecto());
		nuevoPresupuesto.setFechaSolicitud(fechaActual);
		if (EstadoConsumo.ACEPTADO.equals(nuevoPresupuesto.getEstado()))
		{
			nuevoPresupuesto.setFechaAceptacion(fechaActual);
		}
		nuevoPresupuesto.setPresupuesto("SI");
		nuevoPresupuesto.setPresupuestoOrigen(presupuesto);
		nuevoPresupuesto.setConsumoPadre(presupuestoPadre);
		consumoDAS.crear(nuevoPresupuesto);
		this.recargarConsumo(nuevoPresupuesto);
		// Clonamos los anexos (bioseguridad incluído)
		List<Anexo> listaAnexosPresupuesto = anexoDAS.getAnexosByConsumo(presupuesto);
		for (Anexo anexoPresupuesto: listaAnexosPresupuesto)
		{
			Anexo nuevoAnexo = new Anexo();	
			nuevoAnexo.setConsumo(nuevoPresupuesto);
			nuevoAnexo.setNomDocumento(anexoPresupuesto.getNomDocumento());
			nuevoAnexo.setDocumento(anexoPresupuesto.getDocumento());
			nuevoAnexo.setTipo(anexoPresupuesto.getTipo());
			anexoDAS.crear(nuevoAnexo);
		}
		// Clonamos las muestras
		for (Muestra muestraPresupuesto: presupuesto.getMuestras())
		{
			Muestra nuevaMuestra = new Muestra();
			nuevaMuestra.setConsumo(nuevoPresupuesto);
			nuevaMuestra.setReferencia(muestraPresupuesto.getReferencia());
			nuevaMuestra.setDescripcion(muestraPresupuesto.getDescripcion());
			nuevaMuestra.setFechaEntrega(fechaActual);
		}
		// Clonamos los presupuestos hijos 
		for (Consumo presupuestoHijo: this.getConsumosHijos(presupuesto))
		{
			this.clonaPresupuesto(presupuestoHijo, null, nuevoPresupuesto, fechaActual); 
		}
		this.recargarConsumo(nuevoPresupuesto);
		return nuevoPresupuesto;
	}
	
	public void modificarEntidadPagadoraPresupuesto(Consumo presupuesto) throws SaiException
	{
		List<GrupoInvestigacion> listaGrupos = usuarioService.getGruposInvestigacionByUsuarioEntidadPagadora(presupuesto.getUsuarioIrAsociado(), presupuesto.getEntidadPagadora());
		if (!listaGrupos.isEmpty())
		{
			presupuesto.setGrupoInvestigacion(listaGrupos.get(0));
		}
		else
		{
			presupuesto.setGrupoInvestigacion(null);
		}
		for (Consumo ch : presupuesto.getConsumosHijos())
		{
			ch.setEntidadPagadora(presupuesto.getEntidadPagadora());
			modificarEntidadPagadoraPresupuesto(ch);
		}
		consumoDAS.modificar(presupuesto);
	}
	
	@Override
	public void recargarFactura(Factura factura)
	{
		facturaDAS.refresh(factura);
	}

	@Override
	public void recargarConsumo(Consumo consumo)
	{
		consumoDAS.refresh(consumo);
	}

	@Override
	public List<Consumo> getFungiblesRelacionados(Date fecha, Usuario usuarioSolicitante) {
		return consumoDAS.getFungiblesRelacionados(fecha, usuarioSolicitante);
	}

	@Override
	public List<Consumo> busquedaConsumosEstimacion()
	{
		return consumoDAS.busquedaConsumosEstimacion();
	}

	@Override
	public List<Consumo> getConsumosPendientesFacturar(Usuario investigador,
			Servicio servicio,
			EntidadPagadora entidadPagadora,
			boolean soloEstadosFacturables)
	{
		return consumoDAS.getConsumosPendientesFacturar(investigador, servicio, entidadPagadora, soloEstadosFacturables);
	}

	@Override
	public List<ConsumoCantidad> busquedaConsumoCantidad(String estadoFacturacion, int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters)
	{
		return consumoCantidadDAS.busquedaConsumoCantidad(estadoFacturacion, first, pageSize, sortField, sortOrder, filters);
	}

	@Override
	public int getCountBusquedaConsumoCantidad(String estadoFacturacion)
	{
		return consumoCantidadDAS.getCountBusquedaConsumoCantidad(estadoFacturacion);
	}

	@Override
	public Long getNumIrsConsumoResumen(String estadoFacturacion)
	{
		return consumoCantidadDAS.getNumIrsConsumoResumen(estadoFacturacion);
	}

	@Override
	public Long getNumMiembrosConsumoResumen(String estadoFacturacion)
	{
		return consumoCantidadDAS.getNumMiembrosConsumoResumen(estadoFacturacion);
	}

	@Override
	public List<IrEntidadCantidad> busquedaIrEntidadCantidad(String estadoFacturacion, int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters)
	{
		return irEntidadCantidadDAS.busquedaIrEntidadCantidad(estadoFacturacion, first, pageSize, sortField, sortOrder, filters);
	}
		
	@Override
	public int getCountBusquedaIrEntidadCantidad(String estadoFacturacion)
	{
		return irEntidadCantidadDAS.getCountBusquedaIrEntidadCantidad(estadoFacturacion);
	}

	@Override
	public List<IrGrupoinvestigacionCantidad> busquedaIrGrupoinvestigacionCantidad(String estadoFacturacion, int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters)
	{
		return irGrupoinvestigacionCantidadDAS.busquedaIrGrupoinvestigacionCantidad(estadoFacturacion, first, pageSize, sortField, sortOrder, filters);
	}
		
	@Override
	public int getCountBusquedaIrGrupoinvestigacionCantidad(String estadoFacturacion)
	{
		return irGrupoinvestigacionCantidadDAS.getCountBusquedaIrGrupoinvestigacionCantidad(estadoFacturacion);
	}
	
    public ResumenConsumosExcelRespuesta getResumenConsumosExcel(String tipoDesglose, 
    		 													 String tipo,
    		 													 Servicio servicio,
    		 													 Producto producto,
    		 													 String consumoInterno,
    		 													 Usuario usuarioInvestigador,
    		 													 Usuario usuarioSolicitante,
    		 													 String estado,
    				 											 String estadoFacturacion,
    		 													 String fechaDesdeString,
    															 String fechaHastaString)
    {
    	BigDecimal codServicioBD = servicio != null ? new BigDecimal(servicio.getCod()) : null;
    	BigDecimal codProductoBD = producto != null ? new BigDecimal(producto.getCod()) : null; 
    	BigDecimal codUsuarioInvestigadorBD = usuarioInvestigador != null ? new BigDecimal(usuarioInvestigador.getCod()) : null; 
    	BigDecimal codUsuarioSolicitanteBD = usuarioSolicitante != null ? new BigDecimal(usuarioSolicitante.getCod()) : null;
    	try 
    	{
    		HashMap<String,Object> hmResultado = saiPao.getResumenConsumosExcel(tipoDesglose, 
    																			tipo, 
    																			codServicioBD, 
    																			codProductoBD,
    																			consumoInterno, 
    																			codUsuarioInvestigadorBD, 
    																			codUsuarioSolicitanteBD,
    																			estado,
    																			estadoFacturacion,
    																			fechaDesdeString, 
    																			fechaHastaString);
    		BigDecimal codRespuesta=(BigDecimal)hmResultado.get("wcodError" );
    		String menRespuesta = (String)hmResultado.get("wmenError");
    		byte[] blobExcel = (byte[])hmResultado.get("wblobExcel"); 
    		return new ResumenConsumosExcelRespuesta(codRespuesta.intValue(), menRespuesta, blobExcel);
    	}
    	catch ( Exception e ) 
    	{
    		log.error("Error en getResumenConsumosExcel: ", e );
    		return new ResumenConsumosExcelRespuesta(-2, new StringBuilder("Error inesperado: ").append(e.getMessage()).toString(), null);
    	}
    }
	
	@Override
	public List<Factura> busquedaFactura(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters)
	{
		return facturaDAS.busquedaFactura(first, pageSize, sortField, sortOrder, filters);
	}

	@Override
	public int getCountBusquedaFactura()
	{
		return facturaDAS.getCountBusquedaFactura();
	}

	@Override
	public void eliminarMuestra( Muestra m ) throws SaiException
	{
		muestraDAS.eliminar( m );
	}

	@Override
	public void guardarMuestra( Muestra m ) throws SaiException {
		muestraDAS.crear( m );
	}


	@Override
	public void eliminarAnexo( Anexo a ) throws SaiException{
		log.info("Eliminando anexo");
		anexoDAS.eliminar( a );
	}

	@Override
	public void guardarAnexo( Anexo a ) throws SaiException{
		log.info( "Guardando anexo" );
		anexoDAS.crear( a );

	}

	@Override
	public void crearFactura( Factura factura, List<Consumo> listaConsumosFactura ) throws SaiException
	{
		// Guardamos la factura
		facturaDAS.crear( factura );
		// Asignamos la factura a los consumos
		if (( listaConsumosFactura != null ) && !listaConsumosFactura.isEmpty())
		{
			final Iterator<Consumo> itConsumosFactura = listaConsumosFactura.iterator();
			while (itConsumosFactura.hasNext())
			{
				final Consumo consumo = itConsumosFactura.next();
				consumo.setFactura(factura);
				consumoDAS.guardar( consumo );
			}
		}
	}

	@Override
	public void modificarFactura( Factura factura, List<Consumo> listaConsumosFactura ) throws SaiException
	{
		// Si actualmente existen consumos asociados a la factura debemos eliminarlos para asignarles la nueva lista
		if (( factura.getConsumos() != null ) && !factura.getConsumos().isEmpty())
		{
			final Iterator<Consumo> itConsumosOriginales = factura.getConsumos().iterator();
			while (itConsumosOriginales.hasNext())
			{
				final Consumo consumoOriginal = itConsumosOriginales.next();
				consumoOriginal.setFactura(null);
				consumoDAS.guardar( consumoOriginal );
			}
		}
		// Modificamos la factura
		facturaDAS.modificar(factura);
		// Asignamos la factura a los consumos
		final Iterator<Consumo> itConsumosFactura = listaConsumosFactura.iterator();
		while (itConsumosFactura.hasNext())
		{
			final Consumo consumo = itConsumosFactura.next();
			consumo.setFactura(factura);
			consumoDAS.guardar( consumo );
		}
	}

	@Override
	public void eliminarFactura(Factura factura) throws SaiException
	{
		// Si actualmente existen consumos asociados a la factura debemos eliminarlos
		facturaDAS.refresh(factura);
		if (( factura.getConsumos() != null ) && !factura.getConsumos().isEmpty())
		{
			final Iterator<Consumo> itConsumosOriginales = factura.getConsumos().iterator();
			while (itConsumosOriginales.hasNext())
			{
				final Consumo consumoOriginal = itConsumosOriginales.next();
				consumoOriginal.setFactura(null);
				consumoDAS.guardar( consumoOriginal );
			}
		}
		// Eliminamos la factura
		facturaDAS.eliminar(factura);
	}

	@Override
	public EntidadRespuesta enviarFacturaJusto(Factura factura, BigDecimal importe, Partida partida, Subtercero subtercero)
	{
		EntidadRespuesta er = null;
		BigDecimal numproy = null;
		if (partida.getNumproy()!= null) 
		{
			numproy = new BigDecimal(partida.getNumproy());
		}
		StringBuilder concepto = new StringBuilder("Factura SAI");
		if (factura.getEntidadPagadora().getCif() == null)
		{
			// Tenemos que generar una factura de relación interna (FACRI)
			concepto = concepto.append(" RI");
			if (factura.getConcepto()!=null)
			{
				concepto=concepto.append(": ").append(factura.getConcepto());
			}

			HashMap<String, Object> resultado;
			try
			{
				resultado = saiPao.guardarFacriJusto(new BigDecimal(factura.getCod()),
						new BigDecimal(factura.getEntidadPagadora().getTipoTarifa().getCod()),
						concepto.toString(),
						factura.getTipoGasto(),
						importe,
						factura.getInvestigador().getDni(),
						"E70A",
						"SAI",
						"D",
						partida.getEje(),
						partida.getEjeproce(),
						partida.getVic(),
						partida.getUnid(),
						partida.getPro(),
						partida.getEco(),
						"G",
						numproy);
			}
			catch ( final FundeWebJdbcRollBackException e )
			{
				log.error("Error en enviarFacturaJusto: ", e );
				return new EntidadRespuesta(null, e.getCause().getMessage());
			}
			er = new EntidadRespuesta(resultado.get("wcoderror"), (String)resultado.get("wmenerror"));
		}
		else
		{
			// Tenemos que generar una factura normal (FACEMI)
			if (factura.getConcepto()!=null)
			{
				concepto=concepto.append(": ").append(factura.getConcepto());
			}

			HashMap<String, Object> resultado;
			try
			{
				resultado = saiPao.guardarFacemiJusto(new BigDecimal(factura.getCod()),
						new BigDecimal(factura.getEntidadPagadora().getTipoTarifa().getCod()),
						factura.getSerie(),
						Integer.toString(UtilDate.getAño(factura.getFechaGeneracion())),
						Integer.toString(UtilDate.getAño(factura.getFechaGeneracion())),
						partida.getVic(),
						partida.getUnid(),
						partida.getPro(),
						partida.getEco(),
						numproy,
						importe,
						concepto.toString(),
						factura.getEntidadPagadora().getCif(),
						subtercero.getCodigo(),
						UtilDate.dateToString(factura.getFechaGeneracion(), FORMATO_FECHA),
						subtercero.getNombre());
			}
			catch ( final FundeWebJdbcRollBackException e )
			{
				log.error("Error en enviarFacturaJusto: ", e );
				return new EntidadRespuesta(null, e.getCause().getMessage());
			}
			er = new EntidadRespuesta(resultado.get("wcoderror"), (String)resultado.get("wmenerror"));
		}
		return er;
	}

	public EntidadRespuesta anularFacturaJusto(Factura factura)
	{
		HashMap<String, Object> resultado;
		try 
		{
			resultado = saiPao.anulaFacturaJusto(BigDecimal.valueOf(factura.getCod()));
		} 
		catch ( FundeWebJdbcRollBackException ex ) 
		{
			log.error("Error en anularFacturaJusto: ", ex );
			return new EntidadRespuesta(null, ex.getCause().getMessage());
		}
		return new EntidadRespuesta(resultado.get("wcoderror"), (String)resultado.get("wmenerror"));
	}
	
	@Override
	public List<Consumo> getConsumosHijos( Consumo c ) {
		return consumoDAS.getConsumosHijos(c);
	}

	@Override
	public List<Consumo> getPresupuestosHijosTipoReservaByConsumoGenerado(Consumo c)
	{
		return consumoDAS.getPresupuestosHijosTipoReservaByConsumoGenerado(c);
	}
	
	@Override
	public List<ConsumoEquipo> getEquiposByConsumo(Consumo consumo)
	{
		return consumoEquipoDAS.getEquiposByConsumo(consumo);
	}

	@Override
	public void crearConsumoEquipo(ConsumoEquipo ce) throws SaiException
	{
		if (consumoEquipoDAS.existeConsumoEquipo(ce.getConsumo(), ce.getEquipo()))
		{
			throw new SaiException(srb.getString("prestaciones.equipo.add.error.yaexiste"));
		}
		consumoEquipoDAS.crear( ce );
	}
	
	@Override
	public void eliminarConsumoEquipo(ConsumoEquipo ce) throws SaiException
	{
		consumoEquipoDAS.eliminar( ce );
	}
	
	@Override
	public BigDecimal busquedaImporteConsumosPrestacionByEquipo(Equipo equipo, String fechaDesde, String fechaHasta)
	{
		List<Consumo> listaConsumos = consumoDAS.busquedaConsumosPrestacionNoInternosByEquipo(equipo, fechaDesde, fechaHasta);
		return this.getImporteTotalListaConsumos(listaConsumos);
	}
	
	@Override
	public byte[] getConsumoHtml(Long codConsumo)
	{
		try
		{
			return saiPao.getConsumoHtml(new BigDecimal(codConsumo));
		}
		catch ( final FundeWebJdbcRollBackException e )
		{
			log.error("Error en getConsumoHtml(#0): #1", codConsumo, e.getMessage());
			return null;
		}
	}

	@Override
	public TecnicoProducto getTecnicoById( long codTecnico ) {
		return tecnicoDAS.getTecnicoById( codTecnico );
	}


	@Override
	public boolean esTecnicoSolicitud(Consumo consumo, Servicio servicio)
	{
		return identity.tienePerfilEnServicio(TipoPerfil.TECNICO, servicio.getCod()) && identity.getUsuarioConectado().equals(consumo.getUsuarioTecnicoAsignado());
	}

	@Override
	public boolean esMiembroSolicitud(Consumo consumo, Servicio servicio)
	{
		return identity.tienePerfilEnServicio(TipoPerfil.MIEMBRO, servicio.getCod()) && identity.getUsuarioConectado().equals(consumo.getUsuarioSolicitante());
	}

	@Override
	public boolean esIrSolicitud(Consumo consumo, Servicio servicio)
	{
		return identity.tienePerfilEnServicio(TipoPerfil.IR, servicio.getCod()) && identity.getUsuarioConectado().equals(consumo.getUsuarioIrAsociado());
	}

	@Override
	public boolean permitidaOperacionPresupuesto(Consumo c, Servicio s, OperacionPresupuesto operacion)
	{
		// Las operaciones de presupuesto sólo se realizan sobre prestaciones
		final boolean esSupervisorServicio = identity.tienePerfilEnServicio(TipoPerfil.SUPERVISOR, s.getCod());
		final boolean esTecnicoSolicitud = esTecnicoSolicitud(c, s);
		final boolean esIrSolicitud = esIrSolicitud(c, s);
		final boolean esMiembroSolicitud = esMiembroSolicitud(c, s);
		
		if (operacion.equals(OperacionPresupuesto.MODIFICAR))
		{
			return EstadoPresupuesto.SOLICITADO.equals(c.getEstadoPresupuesto()) && (esSupervisorServicio || esTecnicoSolicitud);
		}
		if (operacion.equals(OperacionPresupuesto.ADD_ANEXO) || operacion.equals(OperacionPresupuesto.ANULAR))
		{
			return EstadoPresupuesto.SOLICITADO.equals(c.getEstadoPresupuesto()) && (esSupervisorServicio || esTecnicoSolicitud || esIrSolicitud || esMiembroSolicitud);
		}
		if (operacion.equals(OperacionPresupuesto.ENVIAR_A_IR))
		{
			return EstadoPresupuesto.SOLICITADO.equals(c.getEstadoPresupuesto()) && (esSupervisorServicio || esTecnicoSolicitud) && c.getUsuarioTecnicoAsignado()!= null;
		}
		if (operacion.equals(OperacionPresupuesto.RECHAZAR_POR_IR) || operacion.equals(OperacionPresupuesto.ACEPTAR_POR_IR))
		{
			return EstadoPresupuesto.ENVIADO_A_IR.equals(c.getEstadoPresupuesto()) && esIrSolicitud;
		}
		if (operacion.equals(OperacionPresupuesto.REABRIR))
		{
			return EstadoPresupuesto.ENVIADO_A_IR.equals(c.getEstadoPresupuesto()) && (esIrSolicitud || esMiembroSolicitud);
		}
		if (operacion.equals(OperacionPresupuesto.CLONAR))
		{
			return (EstadoPresupuesto.SOLICITADO.equals(c.getEstadoPresupuesto()) || EstadoPresupuesto.ENVIADO_A_IR.equals(c.getEstadoPresupuesto())) && (esSupervisorServicio || esTecnicoSolicitud);
		}
		if (operacion.equals(OperacionPresupuesto.REACTIVAR))
		{
			return EstadoPresupuesto.RECHAZADO_POR_IR.equals(c.getEstadoPresupuesto()) && esSupervisorServicio;
		}
		return false;
	}
	
	@Override
	public boolean permitidaOperacion(Consumo c, Servicio s, String tipo, OperacionConsumo operacion)
	{
		if ("F".equals(tipo))
		{
			return permitidaOperacionFungible(c, s, operacion);
		}
		else if ("P".equals(tipo))
		{
			return permitidaOperacionPrestacion(c, s, operacion);
		}
		else if ("R".equals(tipo))
		{
			return permitidaOperacionReserva(c, s, operacion);
		}
		return false;
	}
		
	private boolean permitidaOperacionFungible(Consumo c, Servicio s, OperacionConsumo operacion)
	{
		if (operacion.equals(OperacionConsumo.GUARDAR) || ( c.getCod() == null ))
		{
			return true;
		}
		final boolean esSupervisorServicio = identity.tienePerfilEnServicio(TipoPerfil.SUPERVISOR, s.getCod());
		final boolean esTecnicoServicio = identity.tienePerfilEnServicio(TipoPerfil.TECNICO, s.getCod());
		final boolean esIrSolicitud = esIrSolicitud(c, s);
		final boolean esMiembroSolicitud = esMiembroSolicitud(c, s);
		if (operacion.equals(OperacionConsumo.MODIFICAR))
		{
			return (EstadoConsumo.PENDIENTE.equals(c.getEstado()) && (esIrSolicitud || esMiembroSolicitud) && c.getConsumoPadre() == null) ||
				   ((EstadoConsumo.PENDIENTE.equals(c.getEstado()) || EstadoConsumo.FINALIZADO.equals(c.getEstado())) && (esSupervisorServicio || esTecnicoServicio) && c.getFactura() == null);
		}
		else if (operacion.equals(OperacionConsumo.ANULAR))
		{
			return EstadoConsumo.PENDIENTE.equals(c.getEstado()) && (esIrSolicitud || esMiembroSolicitud) && c.getFactura() == null && c.getConsumoPadre() == null;
		}
		else if (operacion.equals(OperacionConsumo.ENTREGAR))
		{
			return EstadoConsumo.PENDIENTE.equals(c.getEstado()) && (esSupervisorServicio || esTecnicoServicio);
		}
		else if (operacion.equals(OperacionConsumo.RECHAZAR))
		{
			return (EstadoConsumo.PENDIENTE.equals(c.getEstado()) || EstadoConsumo.FINALIZADO.equals(c.getEstado())) &&
					(esSupervisorServicio || esTecnicoServicio) &&
					c.getFactura() == null;
		}
		else if (operacion.equals(OperacionConsumo.REACTIVAR))
		{
			return (EstadoConsumo.RECHAZADO.equals(c.getEstado()) || EstadoConsumo.FINALIZADO.equals(c.getEstado())) && esSupervisorServicio;
		}
		else if (operacion.equals( OperacionConsumo.COMENTAR ))
		{
			return esSupervisorServicio || esTecnicoServicio;
		}
		else if (operacion.equals(OperacionConsumo.COMENTAR_FACTURACION))
		{
			return (esTecnicoServicio || esSupervisorServicio) && (c.getFactura() == null || c.getFactura().getNumeroJusto() == null);
		}

		return false;
	}

	private boolean permitidaOperacionPrestacion(Consumo c, Servicio s, OperacionConsumo operacion)
	{
		if (operacion.equals(OperacionConsumo.GUARDAR) || ( c.getCod() == null ))
		{
			return true;
		}
		final boolean esSupervisorServicio = identity.tienePerfilEnServicio(TipoPerfil.SUPERVISOR, s.getCod());
		final boolean esTecnicoSolicitud = esTecnicoSolicitud(c, s);
		final boolean esIrSolicitud = esIrSolicitud(c, s);
		final boolean esMiembroSolicitud = esMiembroSolicitud(c, s);
		if (operacion.equals(OperacionConsumo.MODIFICAR) || operacion.equals(OperacionConsumo.ANEXO) || operacion.equals(OperacionConsumo.MUESTRA))
		{
			return ((EstadoConsumo.PENDIENTE.equals(c.getEstado()) || EstadoConsumo.PEND_VALIDACION_IR.equals(c.getEstado())) && (esIrSolicitud || esMiembroSolicitud || esSupervisorServicio || esTecnicoSolicitud)) ||
					(EstadoConsumo.ACEPTADO.equals(c.getEstado()) && (esSupervisorServicio || esTecnicoSolicitud) && ( c.getFactura() == null )) ||
					(EstadoConsumo.FINALIZADO_PARCIAL.equals(c.getEstado()) && esSupervisorServicio && ( c.getFactura() == null ));
		}
		else if (operacion.equals(OperacionConsumo.SUBCONSUMO))
		{
			return (EstadoConsumo.PENDIENTE.equals(c.getEstado()) || EstadoConsumo.ACEPTADO.equals(c.getEstado())) && (esSupervisorServicio || esTecnicoSolicitud) && ( c.getFactura() == null );
		}
		else if (operacion.equals(OperacionConsumo.ACEPTAR))
		{
			return EstadoConsumo.PENDIENTE.equals(c.getEstado()) && (esSupervisorServicio || esTecnicoSolicitud) && ( c.getUsuarioTecnicoAsignado()!= null );
		}
		else if (operacion.equals(OperacionConsumo.ANULAR))
		{
			return (EstadoConsumo.PENDIENTE.equals(c.getEstado()) || EstadoConsumo.PEND_VALIDACION_IR.equals(c.getEstado())) &&
					(esIrSolicitud || esMiembroSolicitud) &&
					c.getFactura() == null;
		}
		else if (operacion.equals(OperacionConsumo.VALIDAR))
		{
			return EstadoConsumo.PEND_VALIDACION_IR.equals(c.getEstado()) && esIrSolicitud;
		}
		else if (operacion.equals(OperacionConsumo.RECHAZAR))
		{
			return (EstadoConsumo.PENDIENTE.equals(c.getEstado()) || EstadoConsumo.ACEPTADO.equals(c.getEstado())) && (esSupervisorServicio || esTecnicoSolicitud) && ( c.getFactura() == null );
		}
		else if (operacion.equals( OperacionConsumo.SET_EQUIPOS ))
		{
			return (EstadoConsumo.ACEPTADO.equals(c.getEstado()) && (esSupervisorServicio || esTecnicoSolicitud));
		}
		else if (operacion.equals( OperacionConsumo.FINALIZAR ))
		{
			return (EstadoConsumo.FINALIZADO_PARCIAL.equals(c.getEstado()) && esSupervisorServicio) || (EstadoConsumo.ACEPTADO.equals(c.getEstado()) && (esSupervisorServicio || esTecnicoSolicitud));
		}
		else if (operacion.equals(OperacionConsumo.REACTIVAR))
		{
			return EstadoConsumo.FINALIZADO.equals(c.getEstado()) && esSupervisorServicio && ( c.getFactura() == null );
		}
		else if (operacion.equals( OperacionConsumo.READMITIR ))
		{
			return EstadoConsumo.RECHAZADO.equals(c.getEstado()) && esSupervisorServicio;
		}
		else if (operacion.equals(OperacionConsumo.COMENTAR))
		{
			return esMiembroSolicitud || esIrSolicitud || esTecnicoSolicitud || esSupervisorServicio;
		}
		else if (operacion.equals(OperacionConsumo.COMENTAR_FACTURACION))
		{
			return (esTecnicoSolicitud || esSupervisorServicio) && (( c.getFactura() == null ) || ( c.getFactura().getNumeroJusto() == null ));
		}
		return false;
	}

	private boolean permitidaOperacionReserva(Consumo c, Servicio s, OperacionConsumo operacion)
	{
		if (operacion.equals(OperacionConsumo.GUARDAR) || ( c.getCod()==null ))
		{
			return true;
		}
		final boolean esSupervisorServicio = identity.tienePerfilEnServicio(TipoPerfil.SUPERVISOR, s.getCod());
		final boolean esTecnicoSolicitud = esTecnicoSolicitud(c, s);
		final boolean esIrSolicitud = esIrSolicitud(c, s);
		final boolean esMiembroSolicitud = esMiembroSolicitud(c, s);
		if (operacion.equals(OperacionConsumo.MODIFICAR))
		{
			return (((EstadoConsumo.PENDIENTE.equals(c.getEstado()) || EstadoConsumo.PEND_VALIDACION_IR.equals(c.getEstado())) && (esIrSolicitud || esMiembroSolicitud || esSupervisorServicio || esTecnicoSolicitud)) ||
					((EstadoConsumo.ACEPTADO.equals(c.getEstado()) || EstadoConsumo.FINALIZADO.equals(c.getEstado())) && (esSupervisorServicio || esTecnicoSolicitud)) ||
					(EstadoConsumo.ACEPTADO.equals(c.getEstado()) && "NO".equals(c.getProducto().getRequiereValidacion()) && (esIrSolicitud || esMiembroSolicitud))) &&
					c.getFactura() == null;
		}
		else if (operacion.equals(OperacionConsumo.ANULAR))
		{
			return (EstadoConsumo.PENDIENTE.equals(c.getEstado()) || EstadoConsumo.PEND_VALIDACION_IR.equals(c.getEstado()) || (EstadoConsumo.ACEPTADO.equals(c.getEstado()) && (!requiereTecnico(c) || "NO".equals(c.getProducto().getRequiereValidacion())) && c.getProducto().getTipoReservable().getHorasAnulacion() !=null && UtilDate.posterior(reservableService.getMinFechaInicioReservaByConsumo(c), UtilDate.sumarHoras(new Date(), c.getProducto().getTipoReservable().getHorasAnulacion())))) &&
					(esIrSolicitud || esMiembroSolicitud) &&
					c.getFactura() == null;
		}
		else if (operacion.equals(OperacionConsumo.RECHAZAR))
		{
			return (EstadoConsumo.PENDIENTE.equals(c.getEstado()) || EstadoConsumo.ACEPTADO.equals(c.getEstado()) || EstadoConsumo.FINALIZADO.equals(c.getEstado())) && (esSupervisorServicio || esTecnicoSolicitud) && ( c.getFactura() == null );
		}
		else if (operacion.equals(OperacionConsumo.VALIDAR))
		{
			return EstadoConsumo.PEND_VALIDACION_IR.equals(c.getEstado()) && esIrSolicitud;
		}
		else if (operacion.equals(OperacionConsumo.ACEPTAR))
		{
			return EstadoConsumo.PENDIENTE.equals(c.getEstado()) &&
					(esSupervisorServicio || esTecnicoSolicitud) &&
					((requiereTecnico(c) && ( c.getUsuarioTecnicoAsignado()!= null )) || !requiereTecnico(c));
		}
		else if (operacion.equals(OperacionConsumo.COMENTAR))
		{
			return esMiembroSolicitud || esIrSolicitud || esTecnicoSolicitud || esSupervisorServicio;
		}
		else if (operacion.equals(OperacionConsumo.COMENTAR_FACTURACION))
		{
			return (esTecnicoSolicitud || esSupervisorServicio) && (( c.getFactura() == null ) || ( c.getFactura().getNumeroJusto() == null ));
		}
		return false;
	}

	public boolean preguntarEnvioMailUsuario(Consumo consumo)
	{
		return consumo.getCod() == null && consumo.getConsumoPadre() == null && 
			   consumo.getUsuarioSolicitante() != null && consumo.getUsuarioSolicitante() != identity.getUsuarioConectado();
	}
	
	public boolean avisarIrNuevaSolicitudMiembro(Usuario usuarioMiembro, Usuario usuarioIr, Servicio servicio)
	{
		return usuarioDAS.avisarIrNuevaSolicitudMiembro(usuarioMiembro, usuarioIr, servicio);
	}
	
	/*
	 *
	 * ATENCIÓN NO BORRAR ESTE METODO COMENTADO, QUE LA PARTE DEL IR ES MUY JODIDA Y PUEDE QUE LUEGO CAMBIEN DE OPINION EN LOS REQUISITOS

    public String getSqlBusquedaConsumos()
    {
    	StringBuilder sql = new StringBuilder("SELECT con FROM Consumo con");
    	if (identity.tienePerfil(TipoPerfil.SUPERGESTOR) || identity.tienePerfil(TipoPerfil.GESTOR))
    	{
    		return sql.toString();
    	}
    	String serviciosEsSupervisor = null;
    	List<Servicio> listaServiciosEsIrConMiembros = null;
    	String serviciosEsTecnico = null;
    	boolean whereAdded = false;

    	if (identity.tienePerfil(TipoPerfil.SUPERVISOR))
    	{
    		serviciosEsSupervisor = servicioService.getListaCodsServicioEsSupervisor(identity.getUsuarioConectado());
    		if (serviciosEsSupervisor != null)
    		{
    			sql.append(" WHERE (con.producto.servicio.cod IN (").append(serviciosEsSupervisor).append(")");
    			whereAdded = true;
    		}
    	}
    	if (identity.tienePerfil(TipoPerfil.IR))
    	{
    		listaServiciosEsIrConMiembros = servicioService.getListaServiciosEsIrConMiembros( identity.getUsuarioConectado() );
    		int numServiciosIrAnadidos=0;
    		if (listaServiciosEsIrConMiembros != null && !listaServiciosEsIrConMiembros.isEmpty())
    		{
    			Iterator<Servicio> itServ = listaServiciosEsIrConMiembros.iterator();
    			while (itServ.hasNext())
    			{
    				Servicio servicio = itServ.next();
    				String miembrosIrServicio = usuarioService.getListaCodsMiembroByIrServicio(identity.getUsuarioConectado(), servicio);
    				if (serviciosEsSupervisor == null && !whereAdded)
   					{
    						sql.append(" WHERE ((");
    						whereAdded=true;
    				}
    				else if (numServiciosIrAnadidos == 0)
    				{
    					sql.append(" OR (");
    				}
    				sql.append(" (con.producto.servicio.cod=").append(servicio.getCod()).append(" AND con.usuarioSolicitante.cod IN (").append(miembrosIrServicio).append("))");
    				numServiciosIrAnadidos++;
    				if (numServiciosIrAnadidos < listaServiciosEsIrConMiembros.size())
    				{
    					sql.append(" OR");
    				}
    				else
    				{
    					sql.append(")");
    				}
    			}
    		}
    	}
    	if (identity.tienePerfil(TipoPerfil.TECNICO))
    	{
    		serviciosEsTecnico = servicioService.getListaCodsServicioEsTecnico(identity.getUsuarioConectado());
    		if (!whereAdded)
			{
				sql.append(" WHERE (");
				whereAdded=true;
			}
    		else
    		{
    			sql.append(" OR");
    		}
    		sql.append(" (con.producto.servicio.cod IN (").append(serviciosEsTecnico).append(") AND (con.usuarioTecnicoAsignado.cod=").append(identity.getUsuarioConectado().getCod().toString()).append(" OR con.tipo='F'))");
    	}
		if (!whereAdded)
		{
			sql.append(" WHERE (");
		}
		else
		{
			sql.append(" OR");
		}
		sql.append(" con.usuarioSolicitante.cod=").append(identity.getUsuarioConectado().getCod().toString()).append(")");

    	return sql.toString();
    }
	 */

	@Override
	public String getSqlBusquedaConsumos(String presupuesto, boolean consultarTodasParaTecnico)
	{
		final StringBuilder sql = new StringBuilder("SELECT con FROM Consumo con");
		if (identity.tienePerfil(TipoPerfil.SUPERGESTOR) || identity.tienePerfil(TipoPerfil.GESTOR))
		{
			if ("SI".equals(presupuesto))
			{
				sql.append(" WHERE con.consumoPadre IS NULL");
			}
			// Añadimos el ORDER BY para que luego con el QueryPriority.BOTH siempre ordene en segunda instancia por código, así no es aleatoria la ordenación cuando los elementos son iguales y no falla la paginación
			sql.append(" ORDER BY con.cod");
			return sql.toString();
		}
		String serviciosEsSupervisor = null;
		String serviciosEsTecnicoConVisibilidadSolicitudes = null;
		String serviciosEsTecnico = null;
		boolean whereAdded = false;
		if (identity.tienePerfil(TipoPerfil.SUPERVISOR))
		{
			serviciosEsSupervisor = servicioService.getListaCodsServicioEsSupervisor(identity.getUsuarioConectado());
			if (serviciosEsSupervisor != null)
			{
				sql.append(" WHERE (");
				sql.append(" con.producto.servicio.cod IN (").append(serviciosEsSupervisor).append(")");
				whereAdded = true;
			}
		}
		if (identity.tienePerfil(TipoPerfil.IR))
		{
			if (!whereAdded)
			{
				sql.append(" WHERE (");
				whereAdded=true;
			}
			else
			{
				sql.append(" OR");
			}
			sql.append(" con.usuarioIrAsociado.cod=").append(identity.getUsuarioConectado().getCod().toString());
		}
		if (identity.tienePerfil(TipoPerfil.TECNICO))
		{
			serviciosEsTecnico = servicioService.getListaCodsServicioEsTecnico(identity.getUsuarioConectado());
			if (serviciosEsTecnico != null)
			{
				if (!whereAdded)
				{
					sql.append(" WHERE (");
					whereAdded=true;
				}
				else
				{
					sql.append(" OR");
				}
				sql.append(" (con.producto.servicio.cod IN (").append(serviciosEsTecnico).append(") AND (con.usuarioTecnicoAsignado.cod=").append(identity.getUsuarioConectado().getCod().toString()).append(" OR con.tipo='F'))");
			}
			if (consultarTodasParaTecnico)
			{
				serviciosEsTecnicoConVisibilidadSolicitudes = servicioService.getListaCodsServicioEsTecnicoConVisibilidadSolicitudes(identity.getUsuarioConectado());
				if (serviciosEsTecnicoConVisibilidadSolicitudes != null)
				{
					if (!whereAdded)
					{
						sql.append(" WHERE (");
						whereAdded=true;
					}
					else
					{
						sql.append(" OR");
					}
					sql.append(" (con.producto.servicio.cod IN (").append(serviciosEsTecnicoConVisibilidadSolicitudes).append("))");
				}
			}
		}
		if (!whereAdded)
		{
			sql.append(" WHERE (");
		}
		else
		{
			sql.append(" OR");
		}
		sql.append(" con.usuarioSolicitante.cod=").append(identity.getUsuarioConectado().getCod().toString());
		sql.append(")");
		if ("SI".equals(presupuesto))
		{
			sql.append(" AND con.consumoPadre IS NULL");
		}
		// Añadimos el ORDER BY para que luego con el QueryPriority.BOTH siempre ordene en segunda instancia por código, así no es aleatoria la ordenación cuando los elementos son iguales y no falla la paginación
		sql.append(" ORDER BY con.cod");
		return sql.toString();
	}


	@Override
	public ResultQuery<Consumo> busquedaConsumos(String presupuesto, boolean consultarTodasParaTecnico, int first, int pageSize, String sortField, SortOrder sortOrder)
	{
		return consumoDAS.busquedaConsumos(getSqlBusquedaConsumos(presupuesto, consultarTodasParaTecnico), first, pageSize, sortField, sortOrder);
	}

	@Override
	public Long getCountBusquedaConsumos(String presupuesto, boolean consultarTodasParaTecnico)
	{
		return consumoDAS.getCountBusquedaConsumos(getSqlBusquedaConsumos(presupuesto, consultarTodasParaTecnico));
	}

	@Override
	public String getDescripcionTipoConsumo(String tipo)
	{
		if ("F".equals( tipo ))
		{
			return "Fungible";
		}
		else if ("P".equals(tipo))
		{
			return "Prestación";
		}
		else if ("R".equals( tipo ))
		{
			return "Reserva";
		}
		else
		{
			return "Desconocido";
		}
	}

	@Override
	public List<Anexo> getAnexosByConsumo( Consumo c )
	{
		return anexoDAS.getAnexosByConsumo( c );
	}

	@Override
	public Anexo getAnexoBioseguridadByConsumo( Consumo c )
	{
		final List<Anexo> listaAnexos = anexoDAS.getAnexosBioseguridadByConsumo( c );
		if (listaAnexos.isEmpty())
		{
			return null;
		}
		return listaAnexos.get(0);
	}

	@Override
	public List<Anexo> getAnexosConsumoByConsumo( Consumo c )
	{
		return anexoDAS.getAnexosConsumoByConsumo( c );
	}


	@Override
	public Anexo getAnexoById( Long cod )
	{
		return anexoDAS.find( cod );
	}

	@Override
	public Anexo getAnexoGeneralBioseguridad()
	{
		return anexoDAS.getAnexoByTag("BIOSEGURIDAD");
	}

	@Override
	public Usuario obtenerTecnicoAsignarPrestacion(Consumo consumo)
	{
		final List<Usuario> listaUsuariosTecnicosAuto = usuarioDAS.getTecnicosAutomaticosByProducto(consumo.getProducto());
		if (( listaUsuariosTecnicosAuto != null ) && !listaUsuariosTecnicosAuto.isEmpty())
		{
			return listaUsuariosTecnicosAuto.get(0);
		}
		return null;
	}

	@Override
	public Usuario obtenerTecnicoAsignarReserva(Consumo consumo, List<Reservas> listaReservas)
	{
		final List<Usuario> listaUsuariosTecnicosAuto = usuarioDAS.getTecnicosAutomaticosByProducto(consumo.getProducto());
		if (( listaUsuariosTecnicosAuto != null ) && !listaUsuariosTecnicosAuto.isEmpty())
		{
			final Iterator<Usuario> itUsuario = listaUsuariosTecnicosAuto.iterator();
			while (itUsuario.hasNext())
			{
				final Usuario usuarioTecnico = itUsuario.next();
				if (disponibleTecnicoListaReservas(usuarioTecnico, listaReservas))
				{
					return usuarioTecnico;
				}
			}
		}
		return null;
	}

	@Override
	public boolean disponibleTecnicoListaReservas(Usuario usuarioTecnico, List<Reservas> listaReservas)
	{
		final Iterator<Reservas> itReserva = listaReservas.iterator();
		while (itReserva.hasNext())
		{
			final Reservas reserva = itReserva.next();
			if (!reservableService.disponibleTecnico(usuarioTecnico, reserva.getFechaInicioTecnico(), reserva.getFechaFinTecnico()))
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean requiereTecnico(Consumo consumo)
	{
		return ("P".equals(consumo.getProducto().getTipo()) ||
				("R".equals(consumo.getProducto().getTipo()) && ("SI".equals(consumo.getProducto().getRequiereTecnico()) || ("PR".equals(consumo.getProducto().getRequiereTecnico()) && !"NO".equals(consumo.getSolicitudTecnico())))));
	}

	@Override
	public String getColorEstadoConsumo(Consumo consumo)
	{
		if (consumo == null)
		{
			return null;
		}
		final String estadoConsumo = getDescripcionEstadoConsumo(consumo);
		if (EstadoConsumo.PEND_VALIDACION_IR.equals(estadoConsumo))
		{
			return "naranja";
		}
		if (EstadoConsumo.PENDIENTE.equals(estadoConsumo) && ( consumo.getUsuarioTecnicoAsignado()==null )) 
		{
			return "amarillo";
		}
		else if (EstadoConsumo.PENDIENTE.equals(estadoConsumo) && ( consumo.getUsuarioTecnicoAsignado()!=null )) 
		{
			return "lila";
		}
		else if (EstadoConsumo.ACEPTADO.equals(estadoConsumo)) 
		{
			return "verde";
		}
		else if (EstadoConsumo.ANULADO.equals(estadoConsumo) || EstadoConsumo.RECHAZADO.equals(estadoConsumo)) 
		{
			return "rojo";
		}
		else if (EstadoConsumo.FINALIZADO_PARCIAL.equals(estadoConsumo)) 
		{
			return "azul";
		}
		return null;
	}

	@Override
	public String getColorEstadoPresupuesto(Consumo consumo)
	{
		if (consumo == null)
		{
			return null;
		}
		if (EstadoPresupuesto.SOLICITADO.equals(consumo.getEstadoPresupuesto()))
		{
			return "amarillo";
		}
		if (EstadoPresupuesto.ANULADO_POR_USUARIO.equals(consumo.getEstadoPresupuesto()) ||EstadoPresupuesto.ANULADO_POR_ACTI.equals(consumo.getEstadoPresupuesto()) || EstadoPresupuesto.RECHAZADO_POR_IR.equals(consumo.getEstadoPresupuesto())) 
		{
			return "rojo";
		}
		else if (EstadoPresupuesto.ENVIADO_A_IR.equals(consumo.getEstadoPresupuesto())) 
		{
			return "azul";
		}
		else if (EstadoPresupuesto.ACEPTADO_POR_IR.equals(consumo.getEstadoPresupuesto())) 
		{
			return "verde";
		}
		return null;
	}
	
	@Override
	public List<EstadisticaConsumo> estadisticasConsumos( boolean byProducto, List<Servicio> listaServicios ) {
		return estadisticaConsumoDAS.estadisticasConsumos( byProducto, listaServicios );
	}

	
	@Override
	public String getDescripcionEstadoConsumo(Consumo consumo)
	{
		if (consumo == null)
		{
			return null;
		}
		// Las prestaciones en estado Finalizado Parcial deben aparecer como Aceptado para miembros e IR
		if ("F".equals(consumo.getTipo()) || "R".equals(consumo.getTipo()) || 
			identity.tienePerfilEnServicio(TipoPerfil.SUPERVISOR, consumo.getProducto().getServicio().getCod()) || 
			identity.tienePerfilEnServicio(TipoPerfil.TECNICO, consumo.getProducto().getServicio().getCod()) || 
			!EstadoConsumo.FINALIZADO_PARCIAL.equals(consumo.getEstado()))
		{
			return consumo.getEstado();
		}
		else
		{
			return EstadoConsumo.ACEPTADO;
		}
	}
	

}
