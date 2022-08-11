package es.um.atica.sai.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.ejb.Stateless;
import javax.validation.constraints.NotNull;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.SeamResourceBundle;
import org.jboss.seam.log.Log;
import org.primefaces.model.SortOrder;

import es.um.atica.jdbc.exceptions.FundeWebJdbcRollBackException;
import es.um.atica.sai.das.interfaces.AnexoDAS;
import es.um.atica.sai.das.interfaces.ConsumoDAS;
import es.um.atica.sai.das.interfaces.ProductoDAS;
import es.um.atica.sai.das.interfaces.ProductoEquipoDAS;
import es.um.atica.sai.das.interfaces.TarifaDAS;
import es.um.atica.sai.das.interfaces.TecnicoProductoDAS;
import es.um.atica.sai.das.interfaces.TipoReservableDAS;
import es.um.atica.sai.das.interfaces.UnidadMedidaDAS;
import es.um.atica.sai.dtos.ProductoJusto;
import es.um.atica.sai.entities.Anexo;
import es.um.atica.sai.entities.Consumo;
import es.um.atica.sai.entities.Equipo;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.ProductoEquipo;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.Tarifa;
import es.um.atica.sai.entities.TarifaId;
import es.um.atica.sai.entities.TecnicoProducto;
import es.um.atica.sai.entities.TipoReservable;
import es.um.atica.sai.entities.UnidadMedida;
import es.um.atica.sai.exceptions.SaiException;
import es.um.atica.sai.paos.interfaces.SaiPao;
import es.um.atica.sai.services.interfaces.ProductoService;
import es.um.atica.sai.services.interfaces.TarifaService;

@Stateless
@Name( ProductoService.NAME )
public class ProductoServiceImpl implements ProductoService {

    @Logger
    private Log log;

    @In(create = true )
    private ProductoDAS productoDAS;

    @In(create = true )
    private AnexoDAS anexoDAS;
    
    @In(create = true )
    private TecnicoProductoDAS tecnicoDAS;

    @In(create = true )
    private TarifaDAS tarifaDAS;

    @In(create = true )
    private UnidadMedidaDAS unidadMedidaDAS;

    @In(create = true )
    private TipoReservableDAS tipoReservableDAS;
    
	@In(create = true)
	private ProductoEquipoDAS productoEquipoDAS;
    
    @In(create = true )
    private ConsumoDAS consumoDAS;
	
    @In(create = true )
    private TarifaService tarifaService;
    
    @In(create = true)
    private SaiPao saiPao; 

    private final ResourceBundle srb = SeamResourceBundle.getBundle();
    
    @Override
    public void guardarProducto(Producto producto) throws SaiException
    {
    	if (producto.getCod() == null)
    	{
    		productoDAS.crear(producto);
    	}
    	else
    	{
    		productoDAS.modificar(producto);
    	}
    	if (producto.getAnexos()!=null && !producto.getAnexos().isEmpty())
    	{
    		this.guardarAnexo(producto.getAnexos().get(0));
    	}
    }
    
    @Override
	public void eliminarProducto(Producto producto) throws SaiException
    {
    	if (producto.getConsumos() != null && !producto.getConsumos().isEmpty())
    	{
    		throw new SaiException("Tiene consumos asociados");
    	}
    	if (producto.getTarifas()!=null && !producto.getTarifas().isEmpty())
    	{
    		// Eliminamos tarifas asociadas
    		Iterator<Tarifa> itTarifas = producto.getTarifas().iterator();
    		while (itTarifas.hasNext())
    		{
    			Tarifa tarifa = itTarifas.next();
    			tarifaDAS.eliminar( tarifa );
    		}
    	}
    	if (producto.getTecnicos()!=null && !producto.getTecnicos().isEmpty())
    	{
    		// Eliminamos técnicos asociados
    		Iterator<TecnicoProducto> itTecnicos = producto.getTecnicos().iterator();
    		while (itTecnicos.hasNext())
    		{
    			TecnicoProducto tecnico = itTecnicos.next();
    			tecnicoDAS.eliminar( tecnico );
    		}
    	}
    	if (producto.getAnexos() != null && !producto.getAnexos().isEmpty())
    	{
    		// Eliminamos anexos asociados (Aunque haya una lista, sólo hay uno)
    		anexoDAS.eliminar(producto.getAnexos().get(0));
    	}
    	// Eliminamos producto
    	productoDAS.eliminar( producto );
    }

    @Override
    public List<Producto> getProductosByServicio(Servicio servicio) 
    {
    	return productoDAS.getProductosByServicio(servicio);
    }
    
    @Override 
    public List<Producto> getProductosByListaServicios(List<Servicio> listaServicios)
    {
    	return productoDAS.getProductosByListaServicios(listaServicios);
    }
    
    @Override
    public List<Producto> getProductosByServicioTipo(Servicio servicio, 
    												 String tipo, 
    												 boolean soloObtenerNoRequierenProyecto, 
    												 boolean soloObtenerAdmitenPresupuesto) 
    {
    	return productoDAS.getProductosByServicioTipo(servicio, tipo, soloObtenerNoRequierenProyecto, soloObtenerAdmitenPresupuesto);
    }

    @Override
    public List<UnidadMedida> getListaUnidadMedida() {
        try {
            return unidadMedidaDAS.list();
        }
        catch ( Exception e ) 
        {
        	return new ArrayList<>();
        }
    }

 
    @Override
    public void eliminarUnidadMedida( UnidadMedida um ) throws SaiException{

        if ( udMedidaCanBeDeleted( um ) ) {
        	unidadMedidaDAS.eliminar(  um );
        } else {
            throw new SaiException("Está asociada a productos");
        }
    }

    @Override
    public boolean udMedidaCanBeDeleted( UnidadMedida unidadMedida ) {
        return unidadMedida.isDeleteable();
    }

    @Override
    public UnidadMedida getUnidadMedidabyId( @NotNull Long cod ) throws SaiException {

        return unidadMedidaDAS.find( cod );
    }

    @Override
    public UnidadMedida guardarUnidadMedida( @NotNull UnidadMedida unidadMedida ) throws SaiException {
        UnidadMedida result = unidadMedida;
		unidadMedidaDAS.guardar( unidadMedida );
        return result;
    }

    @Override
	public List<ProductoJusto> getListaProductosJusto(String abreviatura)
    {
    	try 
    	{
			return listaMapProductosJustoToListaProductosJusto(saiPao.getProductosSai( abreviatura ));
		} 
    	catch ( FundeWebJdbcRollBackException e ) 
    	{
    		log.error("Error en getListaProductosJusto: ", e);
    		return new ArrayList<>();
    	}
    }

    private List<ProductoJusto> listaMapProductosJustoToListaProductosJusto(List<HashMap<String, Object>> listaMapProductosJusto)
    {
    	if (listaMapProductosJusto == null)
    	{
    		return new ArrayList<>();
    	}
    	List<ProductoJusto> listaProductosJusto = new ArrayList<>();
    	Iterator<HashMap<String, Object>> itMapProductosJusto = listaMapProductosJusto.iterator();
    	while (itMapProductosJusto.hasNext())
    	{
    		HashMap<String, Object> mapProductoJusto = itMapProductosJusto.next();
    		ProductoJusto productoJusto = new ProductoJusto();
    		productoJusto.setDescripcion((String)mapProductoJusto.get("DESCRIPCION"));
    		productoJusto.setUnidadAdministrativa((String)mapProductoJusto.get("UNIDAD"));
    		productoJusto.setPrecio((BigDecimal)mapProductoJusto.get("IMPORTE"));
    		productoJusto.setCodProductoJusto((String)mapProductoJusto.get("COD_PRODUCTO_JUSTO"));
    		listaProductosJusto.add( productoJusto );
    	}
    	return listaProductosJusto;
    }
    
    @Override
	public BigDecimal getPrecio(String udGasto, String codProductoJusto)
    {
       	try 
       	{
			return saiPao.getPrecio(udGasto,codProductoJusto);
		} 
       	catch ( FundeWebJdbcRollBackException e ) 
       	{
       		log.error("Error en getPrecio: ", e );
       		return null;
       	}
    }

	@Override
	public String getDescripcionTipoProducto(String tipo)
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
	public List<Producto> busquedaProductos(Long codUsuario, int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters)
    {
    	return productoDAS.busquedaProductos( codUsuario, first, pageSize, sortField, sortOrder, filters );
    }
    
    @Override
	public int getCountBusquedaProductos(Long codUsuario)
    {
    	return productoDAS.getCountBusquedaProductos( codUsuario );
    }
    
    @Override
	public void eliminarAnexo(Anexo anexo) throws SaiException
    {
    	anexoDAS.eliminar( anexo );
    }
    
    @Override
	public void guardarAnexo(Anexo anexo) throws SaiException
    {
    	if (anexo.getCod() == null)
    	{
    		anexoDAS.crear(anexo);
    	}
    	else
    	{
    		anexoDAS.modificar( anexo );
    	}
    }
    
    @Override
	public List<TipoReservable> getListaTiposReservableByServicio(Servicio servicio)
    {
    	return tipoReservableDAS.getListaTiposReservableByServicio( servicio );
    }
    
    @Override
	public void eliminarTecnicoProducto(TecnicoProducto tec) throws SaiException
    {
    	tecnicoDAS.eliminar( tec );
    }
    
    @Override
	public void guardarTecnicoProducto(TecnicoProducto tec) throws SaiException
    {
    	if (tec.getCod() == null)
    	{
	    	if (tecnicoDAS.existeTecnico( tec ))
	    	{
	    		throw new SaiException("El técnico ya está asociado al producto");
	    	}
	    	tecnicoDAS.crear( tec );
    	}
    	else
    	{
    		tecnicoDAS.modificar( tec );
    	}
    }

    @Override
	public void eliminarTarifa(Tarifa tar) throws SaiException
    {
    	tarifaDAS.eliminar( tar );
    }
    
    @Override
	public void crearTarifa(Tarifa tar, Long cantidadInicial) throws SaiException
    {
    	if (tarifaDAS.existeTarifa(tar, cantidadInicial))
    	{
    		throw new SaiException("Ya existe una tarifa de ese tipo con la misma cantidad mínima asociada al producto");
    	}
    	TarifaId tarId = new TarifaId();
    	tarId.setCantidadInicial( cantidadInicial );
    	tarId.setCodProducto(tar.getProducto().getCod());
    	tarId.setCodTipoTarifa(tar.getTipoTarifa().getCod());
    	tar.setId( tarId );
    	tarifaDAS.crear( tar );
    }

    
    @Override
	public void recargarProducto(Producto producto)
    {
    	productoDAS.refresh( producto );
    }

	@Override
	public List<Producto> getReservables(Servicio servicio) {		
		return productoDAS.getReservables(servicio);
	}
	
    @Override
	public List<ProductoEquipo> getProductoEquiposByProducto(Producto producto)
	{
    	return productoEquipoDAS.getProductoEquiposByProducto( producto );
	}
    
    @Override
    public Integer getMinutosUsoByProductoEquipo(Producto producto, Equipo equipo)
    {
    	if (producto == null || equipo == null)
    	{
    		return null;
    	}
    	ProductoEquipo pe = productoEquipoDAS.getProductoEquipoByProductoEquipo( producto, equipo );
    	if (pe == null)
    	{
    		return null;
    	}
    	return pe.getMinutos();
    }
    
    @Override
    public List<Equipo> getEquiposByProducto(Producto producto)
    {
    	return productoEquipoDAS.getEquiposByProducto( producto );
    }
    
    @Override
	public void guardarProductoEquipo(ProductoEquipo pe) throws SaiException
	{
		if (pe.getCod() == null)
		{
			if (productoEquipoDAS.existeProductoEquipo(pe.getProducto(), pe.getEquipo()))
			{
				throw new SaiException(srb.getString("producto.productoequipo.guardar.error.yaexiste"));
			}
			productoEquipoDAS.crear(pe);
		}
		else
		{
			productoEquipoDAS.modificar(pe);
		}
	}
	
    @Override
    public void eliminarProductoEquipo(ProductoEquipo pe) throws SaiException
    {
    	productoEquipoDAS.eliminar(pe);
    }

    @Override
    public BigDecimal getStockConsumidoByProducto(Producto producto)
    {
    	return consumoDAS.getStockConsumidoByProducto(producto);
    }
    
    @Override
    public BigDecimal getStockDisponibleByProducto(Producto producto)
    {
    	BigDecimal stockConsumido = consumoDAS.getStockConsumidoByProducto(producto);
    	return producto.getStock().subtract(stockConsumido);
    }
    
    @Override
    public BigDecimal getStockDisponibleByProductoEdicionFungible(Producto producto, Consumo consumo)
    {
    	List<Long> listaCodsConsumo = new ArrayList<>();
   		List<Consumo> listaFungiblesRelacionados = consumoDAS.getFungiblesRelacionados(consumo.getFechaSolicitud(), consumo.getUsuarioSolicitante());
   		for (Consumo con: listaFungiblesRelacionados)
   		{
   			listaCodsConsumo.add(con.getCod());
   		}
    	BigDecimal stockConsumido = consumoDAS.getStockConsumidoByProductoEdicion(producto, listaCodsConsumo);
    	return producto.getStock().subtract(stockConsumido);
    }
}
