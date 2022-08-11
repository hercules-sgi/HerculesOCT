package es.um.atica.sai.services.interfaces;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.primefaces.model.SortOrder;

import es.um.atica.sai.dtos.ProductoJusto;
import es.um.atica.sai.entities.Anexo;
import es.um.atica.sai.entities.Consumo;
import es.um.atica.sai.entities.Equipo;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.ProductoEquipo;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.Tarifa;
import es.um.atica.sai.entities.TecnicoProducto;
import es.um.atica.sai.entities.TipoReservable;
import es.um.atica.sai.entities.UnidadMedida;
import es.um.atica.sai.exceptions.SaiException;

@Local
public interface ProductoService {

    public static final String NAME = "productoService";
    
    void guardarProducto(Producto producto) throws SaiException;
    void eliminarProducto(Producto producto) throws SaiException;

    List<Producto> getProductosByServicio(Servicio servicio);
    List<Producto> getProductosByListaServicios(List<Servicio> listaServicios);
	List<Producto> getProductosByServicioTipo(Servicio servicio, 
											  String tipo, 
											  boolean soloObtenerNoRequierenProyecto, 
											  boolean soloObtenerAdmitenPresupuesto);
	
    List<UnidadMedida> getListaUnidadMedida();
    
    void eliminarUnidadMedida(UnidadMedida um) throws SaiException;
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    UnidadMedida guardarUnidadMedida(UnidadMedida unidadMedida) throws SaiException;
    
    UnidadMedida getUnidadMedidabyId(Long cod) throws SaiException;

    abstract boolean udMedidaCanBeDeleted( UnidadMedida unidadMedida );
    
    List<ProductoJusto> getListaProductosJusto(String abreviatura);
    BigDecimal getPrecio(String udGasto, String codProductoJusto);
    String getDescripcionTipoProducto(String tipo);
    
    List<Producto> busquedaProductos(Long codUsuario, int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters);
    int getCountBusquedaProductos(Long codUsuario);
    
    void eliminarAnexo(Anexo anexo) throws SaiException;
    void guardarAnexo(Anexo anexo) throws SaiException;
    
    
    List<TipoReservable> getListaTiposReservableByServicio(Servicio servicio);
    void eliminarTecnicoProducto(TecnicoProducto tec) throws SaiException;
    void guardarTecnicoProducto(TecnicoProducto tec) throws SaiException;
    void eliminarTarifa(Tarifa tar) throws SaiException;
    void crearTarifa(Tarifa tar, Long cantidadInicial) throws SaiException;
    void recargarProducto(Producto producto);
    List<Producto> getReservables(Servicio servicio);
    
    List<ProductoEquipo> getProductoEquiposByProducto(Producto producto);    
    Integer getMinutosUsoByProductoEquipo(Producto producto, Equipo equipo);
    List<Equipo> getEquiposByProducto(Producto producto);
	void guardarProductoEquipo(ProductoEquipo pe) throws SaiException;
	void eliminarProductoEquipo(ProductoEquipo pe) throws SaiException;

	BigDecimal getStockConsumidoByProducto(Producto producto);
	BigDecimal getStockDisponibleByProducto(Producto producto);
	BigDecimal getStockDisponibleByProductoEdicionFungible(Producto producto, Consumo consumo);
    
}
