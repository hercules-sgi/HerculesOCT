package es.um.atica.sai.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.Stateless;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.SeamResourceBundle;
import org.jboss.seam.log.Log;

import es.um.atica.commons.utils.UtilDate;
import es.um.atica.sai.das.interfaces.ConsumoAutorizacionkronDAS;
import es.um.atica.sai.das.interfaces.ProductoPuertakronDAS;
import es.um.atica.sai.das.interfaces.PuertaKronDAS;
import es.um.atica.sai.das.interfaces.ServicioPuertakronDAS;
import es.um.atica.sai.dtos.TerminalKron;
import es.um.atica.sai.entities.Consumo;
import es.um.atica.sai.entities.ConsumoAutorizacionkron;
import es.um.atica.sai.entities.HorarioDia;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.ProductoPuertakron;
import es.um.atica.sai.entities.PuertaKron;
import es.um.atica.sai.entities.Reservas;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.ServicioPuertakron;
import es.um.atica.sai.exceptions.SaiException;
import es.um.atica.sai.paos.interfaces.SaiPao;
import es.um.atica.sai.services.interfaces.KronService;
import es.um.atica.sai.services.interfaces.ReservableService;

@Stateless
@Name(KronService.NAME)
public class KronServiceImpl implements KronService {
	
	@Logger
	private Log log;

	@In(value = ServicioPuertakronDAS.NAME, create = true)
    private ServicioPuertakronDAS servicioPuertakronDAS;
	
	@In(value = PuertaKronDAS.NAME, create = true)
    private PuertaKronDAS puertaKronDAS;
	
	@In(value = ConsumoAutorizacionkronDAS.NAME, create = true)
	private ConsumoAutorizacionkronDAS consumoAutorizacionkronDAS;
	
	@In(value = ProductoPuertakronDAS.NAME, create = true)
    private ProductoPuertakronDAS productoPuertakronDAS;
	
	@In(value = ReservableService.NAME, create = true)
    private ReservableService reservableService;
	
	@In(value= "saiPao", create=true)
	private SaiPao saiPao;
	
	ResourceBundle srb = SeamResourceBundle.getBundle();
		
	private static final String SOLICITA_ALTA = "SA";
	private static final String SOLICITA_BAJA = "SB";
	private static final String DADA_ALTA = "AL";
	private static final String DADA_BAJA = "BA";
	
	private static final String FORMATO_FECHA = "dd/MM/yyyy";
	private static final String FORMATO_FECHA_HORA = "dd/MM/yyyy HH:mm";
	
	
	@Override
	public List<ServicioPuertakron> getListaServicioPuertakronByService( Servicio s ) 
	{ 
		return servicioPuertakronDAS.getListaServicioPuertakronByServicio( s );
	}

	@Override
	public List<ProductoPuertakron> getListaProductoPuertakronByProducto( Producto p ) 
	{		
		return productoPuertakronDAS.getListaProductoPuertakronByProducto( p );
	}
	
	@Override
	public List<TerminalKron> getListaTerminalesKron()
	{
		try 
		{
			List<HashMap<String, Object>> listaHm = saiPao.getTerminalesKron();
			return listaHmToListaTerminalKron(listaHm);
		} 
		catch (Exception ex) 
		{
			log.error("Error al obtener terminales del KRON", ex);
			return new ArrayList<>();
		}
	}
	
	private List<TerminalKron> listaHmToListaTerminalKron(List<HashMap<String, Object>> listaHm)
	{
		List<TerminalKron> listaTerminales = new ArrayList<>();
		for (HashMap<String, Object> hm : listaHm)
		{
			String ip = (String)hm.get("IP");
			String lector = (String)hm.get("LECTOR");
			String nombreEdificio = (String)hm.get("EDIFICIO_NOMBRE");
			String nombreLector = (String)hm.get("DESCRI_TERMINAL");
			listaTerminales.add(new TerminalKron(ip, lector, nombreEdificio, nombreLector));
		}
		return listaTerminales;
	}

	@Override
	public PuertaKron getPuertaKronByTerminalKron(TerminalKron tk) throws SaiException
	{
		PuertaKron pk = puertaKronDAS.getPuertaKronByTerminalKron(tk);
		if (pk == null)
		{
			pk = new PuertaKron();
			pk.setExtPuertaKronIp(tk.getIp());
			pk.setExtPuertaKronLector(tk.getLector());
			puertaKronDAS.crear(pk);
			// Recagamos para que se cargue la vista PuertaKronView
			this.recargarPuertaKron(pk);
		}
		return pk;
	}
	
	public void recargarPuertaKron(PuertaKron pk)
	{
		puertaKronDAS.refresh( pk );
	}
	
	@Override
	public void crearServicioPuertakron(ServicioPuertakron spk ) throws SaiException 
	{
		if (servicioPuertakronDAS.existeServicioPuertakron(spk)) 
		{
			throw new SaiException(srb.getString("servicio.guardar.puerta.error.dup"));
		}
		servicioPuertakronDAS.crear(spk);
	}

	@Override
	public void eliminarServicioPuertakron(ServicioPuertakron spk) throws SaiException 
	{ 
		servicioPuertakronDAS.eliminar(spk);
	}
	
	@Override
	public void crearProductoPuertakron(ProductoPuertakron ppk) throws SaiException
	{
		if (productoPuertakronDAS.existeProductoPuertakron( ppk )) 
		{
			throw new SaiException(srb.getString("producto.puertakron.add.error.yaexiste"));
		}
		productoPuertakronDAS.crear(ppk );
	}

	@Override
	public void eliminarProductoPuertakron(ProductoPuertakron ppk ) throws SaiException 
	{		
		productoPuertakronDAS.eliminar(ppk);
	}


	@Override
	public void reservaAceptada(Reservas reserva) throws SaiException
	{
		if (reserva.getConsumo().getProducto().getKronReservaMinutoAntelacion() != null && reserva.getConsumo().getProducto().getKronReservaMinutoPosterior() != null)
		{
			List<ProductoPuertakron> listaPuertasProducto = this.getListaProductoPuertakronByProducto(reserva.getConsumo().getProducto());
			if (!listaPuertasProducto.isEmpty())
			{
				for (ProductoPuertakron ppk: listaPuertasProducto)
				{
					ConsumoAutorizacionkron cak = new ConsumoAutorizacionkron();
					cak.setPuertaKron(ppk.getPuertaKron());
					cak.setConsumo(reserva.getConsumo());
					cak.setFechaInicio(UtilDate.sumarMinutos(reserva.getFechaInicio(), -reserva.getConsumo().getProducto().getKronReservaMinutoAntelacion().intValue()));
					cak.setFechaFin(UtilDate.sumarMinutos(reserva.getFechaFin(), reserva.getConsumo().getProducto().getKronReservaMinutoPosterior().intValue()));
					cak.setEstado(SOLICITA_ALTA);
					if (!consumoAutorizacionkronDAS.existeAutorizacionSolicitada(cak))
					{
						consumoAutorizacionkronDAS.crear(cak);
					}
				}
			}
		}
	}
	
	@Override
	public void reservaCancelada(Reservas reserva) throws SaiException
	{
		if (reserva.getConsumo().getProducto().getKronReservaMinutoAntelacion() != null && reserva.getConsumo().getProducto().getKronReservaMinutoPosterior() != null)
		{
			List<ProductoPuertakron> listaPuertasProducto = this.getListaProductoPuertakronByProducto(reserva.getConsumo().getProducto());
			if (!listaPuertasProducto.isEmpty())
			{
				for (ProductoPuertakron ppk: listaPuertasProducto)
				{
					List<ConsumoAutorizacionkron> listaAutorizaciones = consumoAutorizacionkronDAS.getAutorizacionesByConsumoPuertaFechas(reserva.getConsumo(), 
																																		  ppk.getPuertaKron(), 
																																		  UtilDate.sumarMinutos(reserva.getFechaInicio(), -reserva.getConsumo().getProducto().getKronReservaMinutoAntelacion().intValue()), 
																																		  UtilDate.sumarMinutos(reserva.getFechaFin(), reserva.getConsumo().getProducto().getKronReservaMinutoPosterior().intValue()));
					if (!listaAutorizaciones.isEmpty())
					{
						Iterator<ConsumoAutorizacionkron> itCak = listaAutorizaciones.iterator();
						while (itCak.hasNext())
						{
							ConsumoAutorizacionkron cak = itCak.next();
							if (SOLICITA_ALTA.equals(cak.getEstado()))
							{
								// Si hay una solicitud de alta la eliminamos
								consumoAutorizacionkronDAS.eliminar(cak);
							}
							else if ((DADA_ALTA.equals(cak.getEstado()) || DADA_BAJA.equals(cak.getEstado())) && !consumoAutorizacionkronDAS.existeOtraAutorizacionIgualOtroConsumo(cak))
							{
								// Si hay una solicitud que ya ha sido dada de alta o de baja, modificamos para que se solicite una baja
								cak.setEstado(SOLICITA_BAJA);
								consumoAutorizacionkronDAS.modificar(cak);
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public void prestacionAceptada(Consumo consumo) throws SaiException
	{
		if (consumo.getProducto().getKronPrestacionDias() != null || consumo.getProducto().getKronPrestacionFechaLimite() != null)
		{
			List<ProductoPuertakron> listaPuertasProducto = this.getListaProductoPuertakronByProducto(consumo.getProducto());
			if (!listaPuertasProducto.isEmpty())
			{
				for (ProductoPuertakron ppk: listaPuertasProducto)
				{
					this.generaAutorizacionesPrestacion(consumo, ppk.getPuertaKron());
				}
			}
		}
	}
	

	private void generaAutorizacionesPrestacion(Consumo consumo, PuertaKron puerta) throws SaiException
	{
		Date fechaIndice = new Date();
		Date fechaFin = null;
		if (consumo.getProducto().getKronPrestacionDias() != null)
		{
			fechaFin = UtilDate.sumarDias(fechaIndice, consumo.getProducto().getKronPrestacionDias().intValue());
		}
		else if (consumo.getProducto().getKronPrestacionFechaLimite() != null)
		{
			fechaFin = UtilDate.stringToDate(new StringBuilder(consumo.getProducto().getKronPrestacionFechaLimite()).append("/").append(UtilDate.dateToString(fechaIndice,"yyyy")).toString(), FORMATO_FECHA);
			if (UtilDate.anteriorOrIgual(fechaFin, fechaIndice))
			{
				fechaFin = UtilDate.sumarAños(fechaFin, 1);
			}
		}
		else
		{
			return;
		}
		while (UtilDate.anteriorOrIgual(fechaIndice, fechaFin))
		{
			HorarioDia hd = reservableService.getHorarioDiaByProductoFecha(consumo.getProducto(), fechaIndice);
			if (hd != null)
			{
				if (hd.getHoraIniManana() != null && hd.getHoraFinManana() != null)
				{
					ConsumoAutorizacionkron cak = new ConsumoAutorizacionkron();
					cak.setPuertaKron(puerta);
					cak.setConsumo(consumo);
					cak.setFechaInicio(UtilDate.stringToDate(new StringBuilder(UtilDate.dateToString(fechaIndice, FORMATO_FECHA)).append(" ").append(hd.getHoraIniManana()).toString(), FORMATO_FECHA_HORA));
					cak.setFechaFin(UtilDate.stringToDate(new StringBuilder(UtilDate.dateToString(fechaIndice, FORMATO_FECHA)).append(" ").append(hd.getHoraFinManana()).toString(), FORMATO_FECHA_HORA));
					cak.setEstado(SOLICITA_ALTA);
					if (!consumoAutorizacionkronDAS.existeAutorizacionSolicitada(cak))
					{
						consumoAutorizacionkronDAS.crear(cak);
					}
				}
				if (hd.getHoraIniTarde() != null && hd.getHoraFinTarde() != null)
				{
					ConsumoAutorizacionkron cak = new ConsumoAutorizacionkron();
					cak.setPuertaKron(puerta);
					cak.setConsumo(consumo);
					cak.setFechaInicio(UtilDate.stringToDate(new StringBuilder(UtilDate.dateToString(fechaIndice, FORMATO_FECHA)).append(" ").append(hd.getHoraIniTarde()).toString(), FORMATO_FECHA_HORA));
					cak.setFechaFin(UtilDate.stringToDate(new StringBuilder(UtilDate.dateToString(fechaIndice, FORMATO_FECHA)).append(" ").append(hd.getHoraFinTarde()).toString(), FORMATO_FECHA_HORA));
					cak.setEstado(SOLICITA_ALTA);
					if (!consumoAutorizacionkronDAS.existeAutorizacionSolicitada(cak))
					{
						consumoAutorizacionkronDAS.crear(cak);
					}
				}
			}
			fechaIndice = UtilDate.sumarDias(fechaIndice, 1);
		}
	}
	
	@Override
	public void prestacionCancelada(Consumo consumo) throws SaiException
	{
		if ((consumo.getProducto().getKronPrestacionDias() != null || consumo.getProducto().getKronPrestacionFechaLimite() != null) && !this.getListaProductoPuertakronByProducto(consumo.getProducto()).isEmpty())
		{
			List<ConsumoAutorizacionkron> listaAutorizaciones = consumoAutorizacionkronDAS.getAutorizacionesByConsumo(consumo);
			if (!listaAutorizaciones.isEmpty())
			{
				Iterator<ConsumoAutorizacionkron> itCak = listaAutorizaciones.iterator();
				while (itCak.hasNext())
				{
					ConsumoAutorizacionkron cak = itCak.next();
					if (SOLICITA_ALTA.equals(cak.getEstado()))
					{
						// Si hay una solicitud de alta la eliminamos
						consumoAutorizacionkronDAS.eliminar(cak);
					}
					else if ((DADA_ALTA.equals(cak.getEstado()) || DADA_BAJA.equals(cak.getEstado())) && !consumoAutorizacionkronDAS.existeOtraAutorizacionIgualOtroConsumo(cak))
					{
						// Si hay una solicitud que ya ha sido dada de alta o de baja, modificamos para que se solicite una baja
						cak.setEstado(SOLICITA_BAJA);
						consumoAutorizacionkronDAS.modificar(cak);
					}
				}
			}
		}
	}
}
