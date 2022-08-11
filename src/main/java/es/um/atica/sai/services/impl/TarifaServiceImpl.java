package es.um.atica.sai.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;
import org.umu.atica.servicios.gesper.gente.entity.Departamento;
import org.umu.atica.servicios.gesper.gente.exceptions.DepartamentoException;

import es.um.atica.jdbc.exceptions.FundeWebJdbcRollBackException;
import es.um.atica.sai.das.interfaces.ConsumoDAS;
import es.um.atica.sai.das.interfaces.EntidadPagadoraDAS;
import es.um.atica.sai.das.interfaces.FacturaDAS;
import es.um.atica.sai.das.interfaces.TarifaDAS;
import es.um.atica.sai.das.interfaces.TipoTarifaDAS;
import es.um.atica.sai.das.interfaces.UsuarioDAS;
import es.um.atica.sai.dtos.EntidadRespuesta;
import es.um.atica.sai.dtos.Partida;
import es.um.atica.sai.dtos.Subtercero;
import es.um.atica.sai.dtos.TipoGasto;
import es.um.atica.sai.entities.Consumo;
import es.um.atica.sai.entities.EntidadPagadora;
import es.um.atica.sai.entities.Factura;
import es.um.atica.sai.entities.Tarifa;
import es.um.atica.sai.entities.TipoTarifa;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;
import es.um.atica.sai.paos.interfaces.SaiPao;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.TarifaService;
import es.um.atica.sai.utils.DepartamentoComparator;
import es.um.atica.seam.factories.ServicioGenteFactory;

@Stateless
@Name("tarifaService")
@SuppressWarnings("unchecked")
public class TarifaServiceImpl implements TarifaService{

	@Logger
	Log log;

	@In(value= "saiPao", create=true)
	private SaiPao saiPao;

	@In( create = true )
	private TipoTarifaDAS tipoTarifaDAS;

	@In( create = true )
	private TarifaDAS tarifaDAS;

	@In(create=true)
	private EntidadPagadoraDAS entidadPagadoraDAS;

	@In( create = true )
	private ConsumoDAS consumoDAS;

	@In( create = true )
	private FacturaDAS facturaDAS;

	@In( create = true )
	private UsuarioDAS usuarioDAS;

	@In("org.jboss.seam.security.identity")
	SaiIdentity identity;

	@Override
	public void crearTipoTarifa( TipoTarifa tt )  throws SaiException{
		tipoTarifaDAS.crear( tt );
	}

	@Override
	public void modificarTipoTarifa( TipoTarifa tt )  throws SaiException{
		tipoTarifaDAS.modificar( tt );
	}

	@Override
	public void eliminarTipoTarifa( TipoTarifa tt )  throws SaiException{
		tipoTarifaDAS.eliminar( tt );
	}

	@Override
	public List<TipoTarifa> getTiposTarifas() {
		try {
			return tipoTarifaDAS.getTarifas();
		}catch(final Exception ex) {
			return new ArrayList<>();
		}
	}

	@Override
	public void crearEntidadPagadora( EntidadPagadora entidad ) throws SaiException
	{
		if (( entidad.getCif()!=null ) && entidadPagadoraDAS.existeEntidadCif(entidad.getCif(), entidad.getCodSubtercero()))
		{
			throw new SaiException(new StringBuilder("Ya existe una entidad pagadora con el CIF ").append(entidad.getCif()).append(" y el subtercero ").append(this.getNombreSubtercero(entidad.getCif(), entidad.getCodSubtercero())).toString());
		}
		if (entidad.getUnidadAdministrativa()!=null)
		{
			if (entidadPagadoraDAS.existeEntidadUnidadAdministrativa(entidad.getUnidadAdministrativa()))
			{
				throw new SaiException(new StringBuilder("Ya existe una entidad pagadora asociada al departamento ").append(entidad.getUnidadAdministrativa()).toString());
			}
			String nombreDep = getNombreDepartamento(entidad.getUnidadAdministrativa());
			if (( nombreDep != null ) && ( nombreDep.length()>=199 ))
			{
				nombreDep = nombreDep.substring( 0,198 );
			}
			entidad.setNombre(nombreDep);
		}
		entidad.setEstado("V");
		entidadPagadoraDAS.crear( entidad );
	}

	@Override
	public void modificarEntidadPagadora( EntidadPagadora entidad ) throws SaiException
	{
		if (( entidad.getCif()!=null ) && entidadPagadoraDAS.existeOtraEntidadCif(entidad))
		{
			throw new SaiException(new StringBuilder("Ya existe otra entidad pagadora con el CIF ").append(entidad.getCif()).append(" y el subtercero ").append(this.getNombreSubtercero(entidad.getCif(), entidad.getCodSubtercero())).toString());
		}
		entidad.setEstado("V");
		entidadPagadoraDAS.modificar( entidad );
	}

	@Override
	public void eliminarEntidadPagadora(EntidadPagadora entidad) throws SaiException
	{
		if (( entidad.getConsumos() != null ) && !entidad.getConsumos().isEmpty())
		{
			throw new SaiException(new StringBuilder("Existen consumos asociados a la entidad pagadora").toString());
		}
		if (( entidad.getFacturas() != null ) && !entidad.getFacturas().isEmpty())
		{
			throw new SaiException(new StringBuilder("Existen facturas asociadas a la entidad pagadora").toString());
		}
		if (( entidad.getEntidadesIrs() != null ) && !entidad.getEntidadesIrs().isEmpty())
		{
			throw new SaiException(new StringBuilder("Existen IR's asociados a la entidad pagadora").toString());
		}
		// Si la entidad está solicitada por algún usuario la eliminamos de la fila de usuario
		usuarioDAS.eliminarEntidadPagadoraSolicitadaUsuarios( entidad );
		// Finalmente eliminamos la entidad pagadora
		entidadPagadoraDAS.eliminar( entidad );
	}


	@Override
	public List<EntidadPagadora> getEntidadesByIr(Usuario usuario)
	{
		return entidadPagadoraDAS.getEntidadesByIr(usuario);
	}

	@Override
	public List<EntidadPagadora> buscarEntidadesPagadoras()
	{
		return entidadPagadoraDAS.buscarEntidadesPagadoras();
	}

	@Override
	public List<EntidadPagadora> getListaEntidadesPagadorasActivas()
	{
		return entidadPagadoraDAS.getListaEntidadesPagadorasActivas();
	}

	@Override
	public String getDescripcionEntidadPagadora(EntidadPagadora ep)
	{
		if (ep == null)
		{
			return null;
		}
		final StringBuilder descripcion = new StringBuilder(ep.getNombre());
		if (ep.getCif() != null)
		{
			descripcion.append(" - ").append(ep.getCif()).append(" - " ).append(this.getNombreSubtercero(ep.getCif(), ep.getCodSubtercero()));
		}
		else if (ep.getUnidadAdministrativa() != null)
		{
			descripcion.append(" - ").append(ep.getUnidadAdministrativa());
		}
		return descripcion.toString();
	}

	@Override
	public String getNombreDepartamento(String codDepartamento)
	{
		try
		{
			final Departamento departamento = ServicioGenteFactory.instance().getServicioGenteUmu().getDepartamento( "SAI", codDepartamento);
			return departamento.getNombre();
		}
		catch (final Exception e )
		{
			log.error("Error obteniendo departamento: ", e);
			return null;
		}
	}

	@Override
	public List<Departamento> getListaDepartamentos()
	{
		List<Departamento> listaDepartamentos = null;
		try
		{
			listaDepartamentos = ServicioGenteFactory.instance().getServicioGenteUmu().getDepartamentos("SAI", false);
		}
		catch ( final DepartamentoException e )
		{
			log.error("No se ha podido obtener la lista de departamentos: #0", e.getMessage());
			return new ArrayList<>();
		}
		Collections.sort(listaDepartamentos, new DepartamentoComparator());
		return listaDepartamentos;
	}

	@Override
	public List<Subtercero> getSubterceros(String cif)
	{
		try
		{
			return listaMapSubtercerosToListaSubterceros(saiPao.consultaSubterceros(cif));
		} catch ( final FundeWebJdbcRollBackException e )
		{
			log.error("Error en getSubterceros: ", e);
			return new ArrayList<>();
		}
	}

	private List<Subtercero> listaMapSubtercerosToListaSubterceros(List<HashMap<String, Object>> listaMapSubterceros)
	{
		if (listaMapSubterceros == null)
		{
			return new ArrayList<>();
		}
		final List<Subtercero> listaSubterceros = new ArrayList<>();
		final Iterator<HashMap<String, Object>> itMapSubterceros = listaMapSubterceros.iterator();
		while (itMapSubterceros.hasNext())
		{
			final HashMap<String, Object> mapSubtercero = itMapSubterceros.next();
			final Subtercero subter = new Subtercero();
			subter.setCodigo(((BigDecimal)mapSubtercero.get("SUBTER")).toString());
			subter.setNombre((String)mapSubtercero.get("NOM"));
			listaSubterceros.add(subter);
		}
		return listaSubterceros;
	}

	@Override
	public Subtercero getSubterceroByCodAndCif(String cod, String cif)
	{
		if (( cod == null ) || ( cif == null ))
		{
			return null;
		}
		// Obtenemos la lista de subterceros
		final List<Subtercero> listaSubterceros = getSubterceros( cif );
		if (listaSubterceros != null)
		{
			final Iterator<Subtercero> itSubt = listaSubterceros.iterator();
			while (itSubt.hasNext())
			{
				final Subtercero subter = itSubt.next();
				if (subter.getCodigo().equals(cod))
				{
					return subter;
				}
			}
		}
		return null;
	}

	public String getNombreSubtercero(String cif, Integer codSubtercero)
	{
		if (codSubtercero == null)
		{
			return "Sin subtercero";
		}
		try 
		{
			return saiPao.getNombreSubtercero(cif, new BigDecimal(codSubtercero));
		} 
		catch ( FundeWebJdbcRollBackException ex ) 
		{
			log.error("Error obteniendo nombre de subtercero:", ex);
			return null;
		}
	}
	
	@Override
	public List<TipoGasto> getTiposGasto()
	{
		try
		{
			return listaMapToListaTipoGasto(saiPao.consultaTiposgastos());
		}
		catch ( final FundeWebJdbcRollBackException e )
		{
			log.error("Error en getTiposGasto: ", e );
			return new ArrayList<>();
		}
	}

	private List<TipoGasto> listaMapToListaTipoGasto(List<HashMap<String, Object>> listaMapTipoGasto)
	{
		if (listaMapTipoGasto == null)
		{
			return new ArrayList<>();
		}
		final List<TipoGasto> listaTiposGasto = new ArrayList<>();
		final Iterator<HashMap<String, Object>> itMapTiposGasto = listaMapTipoGasto.iterator();
		while (itMapTiposGasto.hasNext())
		{
			final HashMap<String, Object> mapTipoGasto = itMapTiposGasto.next();
			final TipoGasto tg = new TipoGasto();
			tg.setTipgasto((String)mapTipoGasto.get("TIPGASTO"));
			tg.setDescri((String)mapTipoGasto.get("DESCRI"));
			listaTiposGasto.add( tg );
		}
		return listaTiposGasto;
	}


	@Override
	public TipoGasto getTipoGastoByCod(String cod)
	{
		if (cod == null)
		{
			return null;
		}
		// Obtenemos la lista de tipos de gasto
		final List<TipoGasto> listaTiposGasto = getTiposGasto();
		if (listaTiposGasto != null)
		{
			final Iterator<TipoGasto> itTg = listaTiposGasto.iterator();
			while (itTg.hasNext())
			{
				final TipoGasto tg = itTg.next();
				if (tg.getTipgasto().equals(cod))
				{
					return tg;
				}
			}
		}
		return null;
	}

	@Override
	public List<Partida> getPartidasByEntidadPagadora(EntidadPagadora ep)
	{
		try
		{
			if (ep.getCif() == null)
			{
				return listaMapPartidasToListaPartidas(saiPao.consultaFacriPartidesSai());
			}
			else
			{
				return listaMapPartidasToListaPartidas(saiPao.consultaFacemiPartiingSai());
			}
		}
		catch(final Exception e)
		{
			log.error("Error en getPartidasByEntidadPagadora: ", e);
			return new ArrayList<>();
		}
	}

	private List<Partida> listaMapPartidasToListaPartidas(List<HashMap<String, Object>> listaMapPartidas)
	{
		if (listaMapPartidas == null)
		{
			return new ArrayList<>();
		}
		final List<Partida> listaPartidas = new ArrayList<>();
		final Iterator<HashMap<String, Object>> itMapPartidas = listaMapPartidas.iterator();
		while (itMapPartidas.hasNext())
		{
			final HashMap<String, Object> mapPartida = itMapPartidas.next();
			final Partida partida = new Partida();
			partida.setEje(((BigDecimal)mapPartida.get("EJE")).toString());
			partida.setEjeproce(((BigDecimal)mapPartida.get("EJEPROCE")).toString());
			partida.setVic((String)mapPartida.get("VIC"));
			partida.setUnid((String)mapPartida.get("UNID"));
			partida.setPro((String)mapPartida.get("PRO"));
			partida.setEco((String)mapPartida.get("ECO"));
			partida.setNumproy(((BigDecimal)mapPartida.get("NUMPROY")).toString());
			listaPartidas.add( partida );
		}
		return listaPartidas;
	}

	// Obtener precios de consumos que todavía no se han facturado (enviado a Justo) -------------------------------
	@Override
	public BigDecimal obtenerTarifaConsumo( Consumo consumo, EntidadPagadora entidad)
	{
		if ( entidad == null )
		{
			return null;
		}
		if (consumo == null || consumo.getProducto() == null || consumo.getProducto().getTarifas() == null || consumo.getProducto().getTarifas().isEmpty())
		{
			return null;
		}
		final Iterator<Tarifa> it = tarifaDAS.getTarifasByProductoCantidad( consumo.getProducto().getCod() ).iterator();
		Tarifa tarifaSeleccionada = new Tarifa();
		boolean salir = false;
		while ( it.hasNext() && !salir )
		{
			final Tarifa tarifaAux = it.next();
			if (tarifaAux.getTipoTarifa().equals(entidad.getTipoTarifa()) && consumo.getCantidad().compareTo(new BigDecimal(tarifaAux.getId().getCantidadInicial()))>= 0 && 
				(tarifaSeleccionada.getId() == null || tarifaAux.getId().getCantidadInicial() > tarifaSeleccionada.getId().getCantidadInicial()))
			{
				tarifaSeleccionada = tarifaAux;
				salir = true;
			}
		}
		if ( tarifaSeleccionada.getId() != null )
		{
			try
			{
				return saiPao.getPrecio(tarifaSeleccionada.getCodUdGasto(), tarifaSeleccionada.getCodTarifaJusto());
			}
			catch ( final FundeWebJdbcRollBackException e )
			{
				log.error("Error en obtenerTarifaConsumo: ", e );
				return null;
			}
		}
		return null;
	}

	@Override
	public void establecerImporteTarifaFactura(List<Consumo> listaConsumosFactura, EntidadPagadora entidadPagadora)
	{
		if (listaConsumosFactura != null)
		{
			final Iterator<Consumo> itConsumo = listaConsumosFactura.iterator();
			while (itConsumo.hasNext())
			{
				final Consumo consumo = itConsumo.next();
				consumo.setImporteTarifaFactura(obtenerTarifaConsumo( consumo, entidadPagadora));
				try {
					consumoDAS.guardar( consumo );
				} catch ( final Exception ex ) {
					log.error( "Error al guardar un consumo en establecerImporteTarifaFactura: " + ex.getMessage() );
				}
			}
		}
	}
	
	@Override
	public void borrarImporteTarifaFactura(List<Consumo> listaConsumosFactura)
	{
		if (listaConsumosFactura != null)
		{
			final Iterator<Consumo> itConsumo = listaConsumosFactura.iterator();
			while (itConsumo.hasNext())
			{
				final Consumo consumo = itConsumo.next();
				consumo.setImporteTarifaFactura(null);
				try {
					consumoDAS.guardar( consumo );
				} catch ( final Exception ex ) {
					log.error( "Error al guardar un consumo en borrarImporteTarifaFactura: " + ex.getMessage() );
				}
			}
		}
	}

	// Se pasa la entidad pagadora como parámetro porque existen consumos sin entidad pagadora asociada
	@Override
	public BigDecimal obtenerPrecioConsumo( Consumo consumo, EntidadPagadora entidadPagadora )
	{
		BigDecimal total = null;
		final BigDecimal tarifa = obtenerTarifaConsumo( consumo, entidadPagadora );
		final BigDecimal cantidad = consumo.getCantidad();
		BigDecimal factorConversion = consumo.getProducto().getFactor();

		// Si el consumo tiene tarifa lo calculamos
		if ( tarifa == null )
		{
			return null;
		}
		if (factorConversion == null)
		{
			factorConversion = BigDecimal.valueOf(1);
		}
		try
		{
			total =  tarifa.multiply(cantidad).multiply(factorConversion).setScale( 2, BigDecimal.ROUND_HALF_UP );
		}
		catch ( final Exception e )
		{
			log.error( "Error en obtenerPrecioConsumo: ", e.getMessage() );
		}
		return total;
	}

	// Se pasa la entidad pagadora como parámetro porque existen consumos sin entidad pagadora asociada
	@Override
	public BigDecimal obtenerPrecioConsumoConHijos( Consumo consumo, EntidadPagadora entidadPagadora )
	{
		// Obtenemos primero el precio del consumo padre (si tiene tarifa)
		BigDecimal total = null;
		total = obtenerPrecioConsumo( consumo, entidadPagadora );
		if (total == null)
		{
			total = new BigDecimal( 0 );
		}
		// Sumamos los precios de los consumos hijos
		List<Consumo> listaConsumosHijos = consumoDAS.getConsumosHijos(consumo);
		if (listaConsumosHijos != null && !listaConsumosHijos.isEmpty())
		{
			final Iterator<Consumo> itConsumo = listaConsumosHijos.iterator();
			while ( itConsumo.hasNext() )
			{
				final Consumo consumoHijo = itConsumo.next();
				final BigDecimal precioHijo = obtenerPrecioConsumo(consumoHijo, entidadPagadora);
				if ( precioHijo != null )
				{
					total = total.add( precioHijo );
				}
			}
		}
		else
		{
			return obtenerPrecioConsumo( consumo, entidadPagadora );
		}
		return total;
	}

	@Override
	public BigDecimal getImporteTotalFactura(List<Consumo> listaConsumosFactura, EntidadPagadora entidadPagadora)
	{
		// Ordenamos la lista de consumos por producto
		BigDecimal importeTotal = new BigDecimal(0);
		if (listaConsumosFactura != null)
		{
			final Iterator<Consumo> itConsumo = listaConsumosFactura.iterator();
			while (itConsumo.hasNext())
			{
				final Consumo consumo = itConsumo.next();
				final BigDecimal precioConsumo = obtenerPrecioConsumo( consumo, entidadPagadora );
				if (precioConsumo != null)
				{
					importeTotal = importeTotal.add( precioConsumo );
				}
			}
		}
		return importeTotal;
	}

	// Obtener precios de consumos ya facturados (enviados a Justo) ------------------------------------------
	@Override
	public BigDecimal obtenerPrecioConsumoEnviadoJusto(Consumo consumo)
	{
		if (consumo.getImporteTarifaFactura() == null)
		{
			return null;
		}
		try
		{
			return consumo.getImporteTarifaFactura().multiply(consumo.getCantidad()).multiply(consumo.getProducto().getFactor()).setScale( 2, BigDecimal.ROUND_HALF_UP );
		}
		catch ( final Exception e )
		{
			log.error( "Error en obtenerPrecioConsumoFacturado: ", e.getMessage() );
			return null;
		}

	}

	@Override
	public BigDecimal getImporteTotalFacturaEnviadaJusto(Factura factura)
	{
		// Ordenamos la lista de consumos por producto
		BigDecimal importeTotal = new BigDecimal(0);
		if (( factura.getConsumos()==null ) || factura.getConsumos().isEmpty())
		{
			factura = facturaDAS.find(factura.getCod());
		}
		final Iterator<Consumo> itConsumo = factura.getConsumos().iterator();
		while (itConsumo.hasNext())
		{
			final Consumo consumo = itConsumo.next();
			final BigDecimal precioConsumo = obtenerPrecioConsumoEnviadoJusto(consumo);
			if (precioConsumo != null)
			{
				importeTotal = importeTotal.add( precioConsumo );
			}
			else
			{
				// Si no podemos obtener el precio que se aplicó para algún consumo de la factura no podemos obtener el importe total
				return null;
			}
		}
		return importeTotal;
	}

	//---------------------------------------------------------------------------------------------------------------------

	@Override
	public EntidadRespuesta permitidoEnvioFacturaJusto(List<Consumo> listaConsumosFactura, EntidadPagadora entidadPagadora)
	{
		if (listaConsumosFactura == null)
		{
			return new EntidadRespuesta(false, "La factura no tiene consumos");
		}
		final Iterator<Consumo> itConsumo = listaConsumosFactura.iterator();
		while (itConsumo.hasNext())
		{
			final BigDecimal precioConsumo = obtenerPrecioConsumo(itConsumo.next(), entidadPagadora);
			if (( precioConsumo==null ) || ( new BigDecimal("0").compareTo(precioConsumo) == 0 ))
			{
				return new EntidadRespuesta(false, "Existen consumos en los que no se ha podido obtener la tarifa, o cuyo importe es 0");
			}
		}
		return new EntidadRespuesta(true, null);
	}

	@Override
	public void recargarEntidadPagadora( EntidadPagadora e ) {
		entidadPagadoraDAS.refresh( e );
	}



}
