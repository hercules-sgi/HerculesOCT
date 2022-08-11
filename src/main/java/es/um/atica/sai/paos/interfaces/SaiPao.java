package es.um.atica.sai.paos.interfaces;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/*
import java.awt.geom.Area;
import java.math.BigDecimal;
import java.util.List;



import org.umu.atica.servicios.gesper.gente.entity.Persona;
import org.umu.atica.servicios.gesper.gente.entity.Provincia;
import org.umu.atica.servicios.gesper.gente.entity.Pueblo;

import es.um.atica.sai.dtos.Partida;
import es.um.atica.sai.dtos.Subtercero;
import es.um.atica.sai.dtos.TipoGasto;
import es.um.atica.sai.entities.ProductoJusto;
*/
import javax.ejb.Local;

import es.um.atica.jdbc.exceptions.FundeWebJdbcRollBackException;

@Local
public interface SaiPao {
    
    public static final String NAME = "saiPao";

    /**
     *
     * @param wcodFactura
     *		- 
     *
     * @return HashMap<String, Object> con valores:
     *		- wcoderror: 
     *		- wmenerror: 
     *
     * @throws FundeWebJdbcRollBackException
     *		- Indica un error de acceso a la BBDD, marca la transaccion para hacer RollBack
     */
    HashMap<String, Object> anulaFacturaJusto(BigDecimal wcodFactura) throws FundeWebJdbcRollBackException;

    /**
     *
     * @param wnombre
     *		- 
     *
     * @return List<HashMap<String, Object>>
     *		- 
     *
     * @throws FundeWebJdbcRollBackException
     *		- Indica un error de acceso a la BBDD, marca la transaccion para hacer RollBack
     */
    List<HashMap<String, Object>> getDependenciasXNombreTag(String wnombre) throws FundeWebJdbcRollBackException;

    /**
     *
     * @param wtipo
     *		- 
     * @param wcodigo
     *		- 
     * @param wbloque
     *		- 
     * @param wplanta
     *		- 
     * @param wdependencia
     *		- 
     *
     * @return String
     *		- 
     *
     * @throws FundeWebJdbcRollBackException
     *		- Indica un error de acceso a la BBDD, marca la transaccion para hacer RollBack
     */
    String getNombreDependencia(String wtipo, String wcodigo, String wbloque, String wplanta, BigDecimal wdependencia) throws FundeWebJdbcRollBackException;	

    /**
    *
    * @param wdni
    *		- 
    *
    * @return List<HashMap<String, Object>>
    *		- 
    *
    * @throws FundeWebJdbcRollBackException
    *		- Indica un error de acceso a la BBDD, marca la transaccion para hacer RollBack
    */
   List<HashMap<String, Object>> getGruposInvestigacion(String wdni) throws FundeWebJdbcRollBackException;
    
    /**
     *
     * @return List<HashMap<String, Object>>
     *		- 
     *
     * @throws FundeWebJdbcRollBackException
     *		- Indica un error de acceso a la BBDD, marca la transaccion para hacer RollBack
     */
    List<HashMap<String, Object>> getTerminalesKron() throws FundeWebJdbcRollBackException;

    /**
     *
     * @param wpatron
     *		- 
     *
     * @return List<HashMap<String, Object>>
     *		- 
     *
     * @throws FundeWebJdbcRollBackException
     *		- Indica un error de acceso a la BBDD, marca la transaccion para hacer RollBack
     */
    List<HashMap<String, Object>> busquedaUsuariosXPatron(String wpatron) throws FundeWebJdbcRollBackException;


    /**
     *
     * @return List<HashMap<String, Object>>
     *		- 
     *
     * @throws FundeWebJdbcRollBackException
     *		- Indica un error de acceso a la BBDD, marca la transaccion para hacer RollBack
     */
    List<HashMap<String, Object>> consultaFacemiPartiingSai() throws FundeWebJdbcRollBackException;

    /**
     *
     * @return List<HashMap<String, Object>>
     *		- 
     *
     * @throws FundeWebJdbcRollBackException
     *		- Indica un error de acceso a la BBDD, marca la transaccion para hacer RollBack
     */
    List<HashMap<String, Object>> consultaFacriPartidesSai() throws FundeWebJdbcRollBackException;

    /**
     *
     * @param cif
     *		- 
     *
     * @return List<HashMap<String, Object>>
     *		- 
     *
     * @throws FundeWebJdbcRollBackException
     *		- Indica un error de acceso a la BBDD, marca la transaccion para hacer RollBack
     */
    List<HashMap<String, Object>> consultaSubterceros(String cif) throws FundeWebJdbcRollBackException;

    /**
     *
     * @return List<HashMap<String, Object>>
     *		- 
     *
     * @throws FundeWebJdbcRollBackException
     *		- Indica un error de acceso a la BBDD, marca la transaccion para hacer RollBack
     */
    List<HashMap<String, Object>> consultaTiposgastos() throws FundeWebJdbcRollBackException;

    /**
     *
     * @param wcodUsuario
     *		- 
     * @param wcodTipocertificacion
     *		- 
     * @param wnombreFichero
     *		- 
     * @param wfichero
     *		- 
     * @param wfechaCaducidad
     *		- 
     * @return void
     *		- 
     *
     * @throws FundeWebJdbcRollBackException
     *		- Indica un error de acceso a la BBDD, marca la transaccion para hacer RollBack
     */
    void crearCertificacion(BigDecimal wcodUsuario, BigDecimal wcodTipocertificacion, String wnombreFichero, byte[] wfichero, Date wfechaCaducidad) throws FundeWebJdbcRollBackException;

    /**
     *
     * @param wcodCertificacion
     *		- 
     * @param wnombreFichero
     *		- 
     * @return void
     *		- 
     *
     * @throws FundeWebJdbcRollBackException
     *		- Indica un error de acceso a la BBDD, marca la transaccion para hacer RollBack
     */
    void eliminarCertificacion(BigDecimal wcodCertificacion, String wnombreFichero) throws FundeWebJdbcRollBackException;

    /**
     *
     * @param dni
     *		- 
     *
     * @return String
     *		- 
     *
     * @throws FundeWebJdbcRollBackException
     *		- Indica un error de acceso a la BBDD, marca la transaccion para hacer RollBack
     */
    String emailUsuario(String dni) throws FundeWebJdbcRollBackException;

    /**
     *
     * @param wenvia
     *		- 
     * @param wrecibe
     *		- 
     * @param wcopia
     *		- 
     * @param wasunto
     *		- 
     * @param wmensaje
     *		- 
     * @param wcopiaoculta
     *		- 
     *
     * @return BigDecimal
     *		- 
     *
     * @throws FundeWebJdbcRollBackException
     *		- Indica un error de acceso a la BBDD, marca la transaccion para hacer RollBack
     */
    BigDecimal enviaEmail(String wenvia, String wrecibe, String wcopia, String wasunto, String wmensaje, String wcopiaoculta) throws FundeWebJdbcRollBackException;


    /**
     *
     * @param wdni
     *		- 
     *
     * @return String
     *		- 
     *
     * @throws FundeWebJdbcRollBackException
     *		- Indica un error de acceso a la BBDD, marca la transaccion para hacer RollBack
     */
    String esTrabajadorUmu(String wdni) throws FundeWebJdbcRollBackException;


    /**
    *
    * @param wtipoDesglose
    *		- 
    * @param wtipo
    *		- 
    * @param wcodServicio
    *		- 
    * @param wcodProducto
    *		- 
    * @param winterno
    *		- 
    * @param wcodUsuarioIr
    *		- 
    * @param wcodUsuarioSolic
    *		- 
    * @param westado
    *		- 
    * @param westadoFacturacion
    *		- 
    * @param wfechaDesde
    *		- 
    * @param wfechaHasta
    *		- 
    *
    * @return HashMap<String, Object> con valores:
    *		- wcodError: 
    *		- wmenError: 
    *		- wblobExcel: 
    *
    * @throws FundeWebJdbcRollBackException
    *		- Indica un error de acceso a la BBDD, marca la transaccion para hacer RollBack
    */
   HashMap<String, Object> getResumenConsumosExcel(String wtipoDesglose, String wtipo, BigDecimal wcodServicio, BigDecimal wcodProducto, String winterno, BigDecimal wcodUsuarioIr, BigDecimal wcodUsuarioSolic, String westado, String westadoFacturacion, String wfechaDesde, String wfechaHasta) throws FundeWebJdbcRollBackException;

    /**
     *
     * @param wcodConsumo
     *		- 
     *
     * @return byte[]
     *		- 
     *
     * @throws FundeWebJdbcRollBackException
     *		- Indica un error de acceso a la BBDD, marca la transaccion para hacer RollBack
     */
    byte[] getConsumoHtml(BigDecimal wcodConsumo) throws FundeWebJdbcRollBackException;

    /**
     *
     * @param wemail
     *		- 
     *
     * @return String
     *		- 
     *
     * @throws FundeWebJdbcRollBackException
     *		- Indica un error de acceso a la BBDD, marca la transaccion para hacer RollBack
     */
    String getDniEmail(String wemail) throws FundeWebJdbcRollBackException;

    /**
     *
     * @param wdni
     *		- 
     *
     * @return Date
     *		- 
     *
     * @throws FundeWebJdbcRollBackException
     *		- Indica un error de acceso a la BBDD, marca la transaccion para hacer RollBack
     */
    Date getFechafinEstrabajadorumu(String wdni) throws FundeWebJdbcRollBackException;

    /**
     *
     * @param wcif
     *		- 
     * @param wcodigo
     *		- 
     *
     * @return String
     *		- 
     *
     * @throws FundeWebJdbcRollBackException
     *		- Indica un error de acceso a la BBDD, marca la transaccion para hacer RollBack
     */
    String getNombreSubtercero(String wcif, BigDecimal wcodigo) throws FundeWebJdbcRollBackException;

    /**
     *
     * @param dni
     *		- 
     *
     * @return HashMap<String, Object> con valores:
     *		- getPersona: 
     *		- nombre: 
     *		- apellidos: 
     *		- fechaNacimiento: 
     *		- domicilio: 
     *		- localidad: 
     *		- cp: 
     *		- provincia: 
     *		- telefono: 
     *		- movil: 
     *
     * @throws FundeWebJdbcRollBackException
     *		- Indica un error de acceso a la BBDD, marca la transaccion para hacer RollBack
     */
    HashMap<String, Object> getPersona(String dni) throws FundeWebJdbcRollBackException;

    /**
     *
     * @param wunid
     *		- 
     * @param wprod
     *		- 
     *
     * @return BigDecimal
     *		- 
     *
     * @throws FundeWebJdbcRollBackException
     *		- Indica un error de acceso a la BBDD, marca la transaccion para hacer RollBack
     */
    BigDecimal getPrecio(String wunid, String wprod) throws FundeWebJdbcRollBackException;

    /**
     *
     * @param prod
     *		- 
     *
     * @return List<HashMap<String, Object>>
     *		- 
     *
     * @throws FundeWebJdbcRollBackException
     *		- Indica un error de acceso a la BBDD, marca la transaccion para hacer RollBack
     */
    List<HashMap<String, Object>> getProductosSai(String prod) throws FundeWebJdbcRollBackException;

    /**
     *
     * @param wfacturasai
     *		- 
     * @param tipotarifaep
     *		- 
     * @param wserie
     *		- 
     * @param weje
     *		- 
     * @param wejeproce
     *		- 
     * @param wvic
     *		- 
     * @param wunid
     *		- 
     * @param wpro
     *		- 
     * @param weco
     *		- 
     * @param wnumproy
     *		- 
     * @param wimporte
     *		- 
     * @param wdescri
     *		- 
     * @param wcodter
     *		- 
     * @param wsubter
     *		- 
     * @param wfecemi
     *		- 
     * @param wsubtercero
     *		- 
     *
     * @return HashMap<String, Object> con valores:
     *		- wanofacemi: 
     *		- wnumfacemi: 
     *		- wcoderror: 
     *		- wmenerror: 
     *
     * @throws FundeWebJdbcRollBackException
     *		- Indica un error de acceso a la BBDD, marca la transaccion para hacer RollBack
     */
    HashMap<String, Object> guardarFacemiJusto(BigDecimal wfacturasai, BigDecimal tipotarifaep, String wserie, String weje, String wejeproce, String wvic, String wunid, String wpro, String weco, BigDecimal wnumproy, BigDecimal wimporte, String wdescri, String wcodter, String wsubter, String wfecemi, String wsubtercero) throws FundeWebJdbcRollBackException;

    /**
     *
     * @param wfacturasai
     *		- 
     * @param tipotarifaep
     *		- 
     * @param wdescripcion
     *		- 
     * @param wtipogasto
     *		- 
     * @param wimporte
     *		- 
     * @param wdnisoli
     *		- 
     * @param wunidadamori
     *		- 
     * @param worigen
     *		- 
     * @param wtiplin
     *		- 
     * @param weje
     *		- 
     * @param wejeproce
     *		- 
     * @param wvic
     *		- 
     * @param wunid
     *		- 
     * @param wpro
     *		- 
     * @param weco
     *		- 
     * @param wtipoeco
     *		- 
     * @param wnumproy
     *		- 
     *
     * @return HashMap<String, Object> con valores:
     *		- noseutiliza: 
     *		- noseutiliza2: 
     *		- wcoderror: 
     *		- wmenerror: 
     *
     * @throws FundeWebJdbcRollBackException
     *		- Indica un error de acceso a la BBDD, marca la transaccion para hacer RollBack
     */
    HashMap<String, Object> guardarFacriJusto(BigDecimal wfacturasai, BigDecimal tipotarifaep, String wdescripcion, String wtipogasto, BigDecimal wimporte, String wdnisoli, String wunidadamori, String worigen, String wtiplin, String weje, String wejeproce, String wvic, String wunid, String wpro, String weco, String wtipoeco, BigDecimal wnumproy) throws FundeWebJdbcRollBackException;


}

