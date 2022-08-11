package es.um.atica.sai.services.interfaces;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Local;

import org.umu.atica.servicios.gesper.gente.entity.Departamento;

import es.um.atica.sai.dtos.EntidadRespuesta;
import es.um.atica.sai.dtos.Partida;
import es.um.atica.sai.dtos.Subtercero;
import es.um.atica.sai.dtos.TipoGasto;
import es.um.atica.sai.entities.Consumo;
import es.um.atica.sai.entities.EntidadPagadora;
import es.um.atica.sai.entities.Factura;
import es.um.atica.sai.entities.TipoTarifa;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;

@Local
public interface TarifaService {

	public static final String NAME = "tarifaService";
	
	void crearTipoTarifa( TipoTarifa tt ) throws SaiException;
	void modificarTipoTarifa( TipoTarifa tt ) throws SaiException;
	void eliminarTipoTarifa( TipoTarifa tt ) throws SaiException;
	List<TipoTarifa> getTiposTarifas();
    void crearEntidadPagadora(EntidadPagadora entidad) throws SaiException;
    void modificarEntidadPagadora(EntidadPagadora entidad) throws SaiException;
    void eliminarEntidadPagadora(EntidadPagadora entidad) throws SaiException;
    List<EntidadPagadora> getEntidadesByIr(Usuario usuario);
    List<EntidadPagadora> buscarEntidadesPagadoras();
    List<EntidadPagadora> getListaEntidadesPagadorasActivas();
    String getDescripcionEntidadPagadora(EntidadPagadora ep);
    String getNombreDepartamento(String codDepartamento);
    List<Departamento> getListaDepartamentos();
    List<Subtercero> getSubterceros(String cif);
    Subtercero getSubterceroByCodAndCif(String cod, String cif);
    String getNombreSubtercero(String cif, Integer codSubtercero);
    List<TipoGasto> getTiposGasto();
    TipoGasto getTipoGastoByCod(String cod);
    List<Partida> getPartidasByEntidadPagadora(EntidadPagadora ep);
    void establecerImporteTarifaFactura(List<Consumo> listaConsumosFactura, EntidadPagadora entidadPagadora);
    void borrarImporteTarifaFactura(List<Consumo> listaConsumosFactura);
    BigDecimal obtenerTarifaConsumo(Consumo consumo, EntidadPagadora entidad);
    BigDecimal obtenerPrecioConsumo( Consumo consumo, EntidadPagadora entidadPagadora );
	BigDecimal obtenerPrecioConsumoConHijos( Consumo consumo, EntidadPagadora entidadPagadora );
    BigDecimal getImporteTotalFactura(List<Consumo> listaConsumosFactura, EntidadPagadora entidadPagadora);
    BigDecimal obtenerPrecioConsumoEnviadoJusto(Consumo consumo);
    BigDecimal getImporteTotalFacturaEnviadaJusto(Factura factura);
    EntidadRespuesta permitidoEnvioFacturaJusto(List<Consumo> listaConsumosFactura, EntidadPagadora entidadPagadora);
    void recargarEntidadPagadora(EntidadPagadora e);
}
