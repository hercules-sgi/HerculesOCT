package es.um.atica.sai.services.interfaces;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Local;

import es.um.atica.sai.entities.Consumo;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.Proyecto;
import es.um.atica.sai.entities.ProyectoProducto;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;

@Local
public interface ProyectoService {

    public static final String NAME = "proyectoService";
    
    void guardarProyecto(Proyecto proyecto) throws SaiException;
    void eliminarProyecto(Proyecto proyecto) throws SaiException;

    void guardarProyectoProducto(ProyectoProducto pp) throws SaiException;
    void eliminarProyectoProducto(ProyectoProducto pp) throws SaiException;
    
    List<Proyecto> buscarProyectos();
    List<Proyecto> getListaProyectosByProductoIr(Producto producto, Usuario usuarioIr);
    
    BigDecimal getCantidadConsumidaByProyecto(Proyecto proyecto);
    BigDecimal getCantidadConsumidaByProyectoProducto(Proyecto proyecto, Producto producto);
    BigDecimal getCantidadDisponibleByProyectoProducto(ProyectoProducto pp);
    BigDecimal getCantidadDisponibleByProyectoProducto(Proyecto proyecto, Producto producto);
    BigDecimal getCantidadDisponibleByProyectoProductoEdicionFungible(Proyecto proyecto, Producto producto, Consumo consumo);
    
    List<ProyectoProducto> getListaProyectoProductoByProyecto(Proyecto proyecto);
	List<Consumo> getConsumosActivosByProyecto(Proyecto proyecto);
	List<Consumo> getConsumosActivosByProyectoProducto(Proyecto proyecto, Producto producto);
    
}
