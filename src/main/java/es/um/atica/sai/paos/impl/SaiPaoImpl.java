package es.um.atica.sai.paos.impl;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.sql.DataSource;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import es.um.atica.jdbc.exceptions.FundeWebJdbcRollBackException;
import es.um.atica.jdbc.pao.ServicioGenericoPao;
import es.um.atica.sai.paos.interfaces.SaiPao;
import es.um.atica.sai.security.authentication.SaiIdentity;

@Local( SaiPao.class )
@Stateless
@Name( SaiPao.NAME)
public class SaiPaoImpl extends ServicioGenericoPao implements SaiPao {

	
    @Logger
    private Log log;

    @Resource(lookup = "jdbc/Sai DataSource")
    private transient DataSource dataSource;

    @In(create=true)
    SaiIdentity identity;
    
    private static final String APLICACION = "SAI";
    
    /**
     * {@inheritDoc}
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public HashMap<String, Object> anulaFacturaJusto(BigDecimal wcodFactura) throws FundeWebJdbcRollBackException {
    	Connection connection = null;
    	CallableStatement callableStatement = null;
    	HashMap<String, Object> resultCall = null;
    	StringBuilder sb = new StringBuilder();
    	sb.append("Llamando a anulaFacturaJusto. Parametros de entrada:")
    	  .append("\n\t Parametro 1: wcodFactura - NUMERIC - BigDecimal. Valor: #0");
    	this.getLog().debug(sb.toString(), wcodFactura);
    	sb = null;
    	try {
    	    connection = getConnectionWithClientId(dataSource, APLICACION, getIdUsuario());
    	    callableStatement = connection.prepareCall("{ call SAI.SAI_WEB.anula_factura_justo(?, ?, ?) }");

    	    if (wcodFactura == null) {
    	        callableStatement.setNull(1, java.sql.Types.NUMERIC);
    	    }
    	    else {
    	        callableStatement.setBigDecimal(1, wcodFactura);
    	    }
    	    callableStatement.registerOutParameter(2, java.sql.Types.NUMERIC);
    	    callableStatement.registerOutParameter(3, java.sql.Types.VARCHAR);
    	    callableStatement.execute();

    	    resultCall = new HashMap<String, Object>();
    	    resultCall.put("wcoderror", callableStatement.getBigDecimal(2));
    	    resultCall.put("wmenerror", callableStatement.getString(3));
    	    sb = new StringBuilder();
    	    sb.append("Llamando a anulaFacturaJusto. Parametros de Salida:")
    	      .append("\n\t Parametro 2: wcoderror - NUMERIC - BigDecimal. Valor: #0")
    	      .append("\n\t Parametro 3: wmenerror - VARCHAR2 - String. Valor: #1");
    	    this.getLog().debug(sb.toString(),
    	                        ((BigDecimal) resultCall.get("wcoderror")),
    	                        ((String) resultCall.get("wmenerror")));
    	    sb = null;
    	} catch (Exception e) {
    	    this.getLog().error("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	    throw new FundeWebJdbcRollBackException("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	}
    	finally {
    	    close(connection, callableStatement);
    	}
    	return resultCall;
    }
    
    /**
     * {@inheritDoc}
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<HashMap<String, Object>> getDependenciasXNombreTag(String wnombre) throws FundeWebJdbcRollBackException {
    	Connection connection = null;
    	CallableStatement callableStatement = null;
    	List<HashMap<String, Object>> resultCall = null;
    	ResultSet getDependenciasXNombreTag = null; 
    	StringBuilder sb = new StringBuilder();
    	sb.append("Llamando a getDependenciasXNombreTag. Parametros de entrada:")
    	  .append("\n\t Parametro 2: wnombre - VARCHAR2 - String. Valor: #0");
    	this.getLog().debug(sb.toString(), wnombre);
    	sb = null;
    	try {
    	    connection = dataSource.getConnection();
    	    callableStatement = connection.prepareCall("{ ? = call SAI.SAI_WEB.get_dependencias_x_nombre_tag(?) }");

    	    if (wnombre == null) {
    	        callableStatement.setNull(2, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(2, wnombre);
    	    }
    	    callableStatement.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
    	    callableStatement.execute();

    	    getDependenciasXNombreTag = (ResultSet) callableStatement.getObject(1);
    	    resultCall = resultSetToList(getDependenciasXNombreTag);
    	    sb = new StringBuilder();
    	    sb.append("Llamando a getDependenciasXNombreTag. Parametros de Salida:")
    	      .append("\n\t Parametro 1: resultCall - REF_CURSOR - ResultSet. Valor: #0");
    	    this.getLog().debug(sb.toString(), resultCall == null ? 0 : resultCall.size());
    	    sb = null;
    	} catch (Exception e) {
    	    this.getLog().error("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	    throw new FundeWebJdbcRollBackException("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	}
    	finally {
    	    close(getDependenciasXNombreTag);
    	    close(connection, callableStatement);
    	}
    	return resultCall;
    }

    /**
     * {@inheritDoc}
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<HashMap<String, Object>> getGruposInvestigacion(String wdni) throws FundeWebJdbcRollBackException {
    	Connection connection = null;
    	CallableStatement callableStatement = null;
    	List<HashMap<String, Object>> resultCall = null;
    	ResultSet getGruposInvestigacion = null; 
    	StringBuilder sb = new StringBuilder();
    	sb.append("Llamando a getGruposInvestigacion. Parametros de entrada:")
    	  .append("\n\t Parametro 2: wdni - VARCHAR2 - String. Valor: #0");
    	this.getLog().debug(sb.toString(), wdni);
    	sb = null;
    	try {
    	    connection = dataSource.getConnection();
    	    callableStatement = connection.prepareCall("{ ? = call SAI.SAI_WEB.get_grupos_investigacion(?) }");

    	    if (wdni == null) {
    	        callableStatement.setNull(2, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(2, wdni);
    	    }
    	    callableStatement.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
    	    callableStatement.execute();

    	    getGruposInvestigacion = (ResultSet) callableStatement.getObject(1);
    	    resultCall = resultSetToList(getGruposInvestigacion);
    	    sb = new StringBuilder();
    	    sb.append("Llamando a getGruposInvestigacion. Parametros de Salida:")
    	      .append("\n\t Parametro 1: resultCall - REF_CURSOR - ResultSet. Valor: #0");
    	    this.getLog().debug(sb.toString(), resultCall == null ? 0 : resultCall.size());
    	    sb = null;
    	} catch (Exception e) {
    	    this.getLog().error("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	    throw new FundeWebJdbcRollBackException("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	}
    	finally {
    	    close(getGruposInvestigacion);
    	    close(connection, callableStatement);
    	}
    	return resultCall;
    }
    
    /**
     * {@inheritDoc}
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String getNombreDependencia(String wtipo, String wcodigo, String wbloque, String wplanta, BigDecimal wdependencia) throws FundeWebJdbcRollBackException {
    	Connection connection = null;
    	CallableStatement callableStatement = null;
    	String resultCall = null;
    	StringBuilder sb = new StringBuilder();
    	sb.append("Llamando a getNombreDependencia. Parametros de entrada:")
    	  .append("\n\t Parametro 2: wtipo - VARCHAR2 - String. Valor: #0")
    	  .append("\n\t Parametro 3: wcodigo - VARCHAR2 - String. Valor: #1")
    	  .append("\n\t Parametro 4: wbloque - VARCHAR2 - String. Valor: #2")
    	  .append("\n\t Parametro 5: wplanta - VARCHAR2 - String. Valor: #3")
    	  .append("\n\t Parametro 6: wdependencia - NUMERIC - BigDecimal. Valor: #4");
    	this.getLog().debug(sb.toString(),
    	                    wtipo,
    	                    wcodigo,
    	                    wbloque,
    	                    wplanta,
    	                    wdependencia);
    	sb = null;
    	try {
    	    connection = dataSource.getConnection();
    	    callableStatement = connection.prepareCall("{ ? = call SAI.SAI_WEB.get_nombre_dependencia(?, ?, ?, ?, ?) }");

    	    if (wtipo == null) {
    	        callableStatement.setNull(2, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(2, wtipo);
    	    }
    	    if (wcodigo == null) {
    	        callableStatement.setNull(3, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(3, wcodigo);
    	    }
    	    if (wbloque == null) {
    	        callableStatement.setNull(4, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(4, wbloque);
    	    }
    	    if (wplanta == null) {
    	        callableStatement.setNull(5, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(5, wplanta);
    	    }
    	    if (wdependencia == null) {
    	        callableStatement.setNull(6, java.sql.Types.NUMERIC);
    	    }
    	    else {
    	        callableStatement.setBigDecimal(6, wdependencia);
    	    }
    	    callableStatement.registerOutParameter(1, java.sql.Types.VARCHAR);
    	    callableStatement.execute();

    	    resultCall = callableStatement.getString(1);
    	    sb = new StringBuilder();
    	    sb.append("Llamando a getNombreDependencia. Parametros de Salida:")
    	      .append("\n\t Parametro 1: resultCall - VARCHAR2 - String. Valor: #0");
    	    this.getLog().debug(sb.toString(), resultCall);
    	    sb = null;
    	} catch (Exception e) {
    	    this.getLog().error("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	    throw new FundeWebJdbcRollBackException("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	}
    	finally {
    	    close(connection, callableStatement);
    	}
    	return resultCall;
    }

    
    /**
     * {@inheritDoc}
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<HashMap<String, Object>> busquedaUsuariosXPatron(String wpatron) throws FundeWebJdbcRollBackException {
    	Connection connection = null;
    	CallableStatement callableStatement = null;
    	List<HashMap<String, Object>> resultCall = null;
    	ResultSet busquedaUsuariosXPatron = null; 
    	StringBuilder sb = new StringBuilder();
    	sb.append("Llamando a busquedaUsuariosXPatron. Parametros de entrada:")
    	  .append("\n\t Parametro 2: wpatron - VARCHAR2 - String. Valor: #0");
    	this.getLog().debug(sb.toString(), wpatron);
    	sb = null;
    	try {
    	    connection = dataSource.getConnection();
    	    callableStatement = connection.prepareCall("{ ? = call SAI.SAI_WEB.busqueda_usuarios_x_patron(?) }");

    	    if (wpatron == null) {
    	        callableStatement.setNull(2, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(2, wpatron);
    	    }
    	    callableStatement.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
    	    callableStatement.execute();

    	    busquedaUsuariosXPatron = (ResultSet) callableStatement.getObject(1);
    	    resultCall = resultSetToList(busquedaUsuariosXPatron);
    	    sb = new StringBuilder();
    	    sb.append("Llamando a busquedaUsuariosXPatron. Parametros de Salida:")
    	      .append("\n\t Parametro 1: resultCall - REF_CURSOR - ResultSet. Valor: #0");
    	    this.getLog().debug(sb.toString(), resultCall == null ? 0 : resultCall.size());
    	    sb = null;
    	} catch (Exception e) {
    	    this.getLog().error("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	    throw new FundeWebJdbcRollBackException("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	}
    	finally {
    	    close(busquedaUsuariosXPatron);
    	    close(connection, callableStatement);
    	}
    	return resultCall;
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<HashMap<String, Object>> consultaFacemiPartiingSai() throws FundeWebJdbcRollBackException {
    	Connection connection = null;
    	CallableStatement callableStatement = null;
    	List<HashMap<String, Object>> resultCall = null;
    	ResultSet rcDatos = null; 
    	StringBuffer sb = new StringBuffer();
    	sb.append("Llamando a consultaFacemiPartiingSai. No tiene parametros de entrada.");
    	this.getLog().debug(sb.toString());
    	sb = null;
    	try {
    	    connection = dataSource.getConnection();
    	    callableStatement = connection.prepareCall("{ call SAI.SAI_WEB.consulta_facemi_partiing_sai(?) }");

    	    callableStatement.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
    	    callableStatement.execute();

    	    rcDatos = (ResultSet) callableStatement.getObject(1);
    	    resultCall = resultSetToList(rcDatos);
    	    sb = new StringBuffer();
    	    sb.append("Llamando a consultaFacemiPartiingSai. Parametros de Salida:")
    	      .append("\n\t Parametro 1: resultCall - REF_CURSOR - ResultSet. Valor: #0");
    	    this.getLog().debug(sb.toString(), resultCall == null ? 0 : resultCall.size());
    	    sb = null;
    	} catch (Exception e) {
    	    this.getLog().error("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	    throw new FundeWebJdbcRollBackException("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	}
    	finally {
    	    close(rcDatos);
    	    close(connection, callableStatement);
    	}
    	return resultCall;
    }

    /**
     * {@inheritDoc}
     */
    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<HashMap<String, Object>> consultaFacriPartidesSai() throws FundeWebJdbcRollBackException {
    	Connection connection = null;
    	CallableStatement callableStatement = null;
    	List<HashMap<String, Object>> resultCall = null;
    	ResultSet rcDatos = null; 
    	StringBuffer sb = new StringBuffer();
    	sb.append("Llamando a consultaFacriPartidesSai. No tiene parametros de entrada.");
    	this.getLog().debug(sb.toString());
    	sb = null;
    	try {
    	    connection = dataSource.getConnection();
    	    callableStatement = connection.prepareCall("{ call SAI.SAI_WEB.consulta_facri_partides_sai(?) }");

    	    callableStatement.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
    	    callableStatement.execute();

    	    rcDatos = (ResultSet) callableStatement.getObject(1);
    	    resultCall = resultSetToList(rcDatos);
    	    sb = new StringBuffer();
    	    sb.append("Llamando a consultaFacriPartidesSai. Parametros de Salida:")
    	      .append("\n\t Parametro 1: resultCall - REF_CURSOR - ResultSet. Valor: #0");
    	    this.getLog().debug(sb.toString(), resultCall == null ? 0 : resultCall.size());
    	    sb = null;
    	} catch (Exception e) {
    	    this.getLog().error("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	    throw new FundeWebJdbcRollBackException("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	}
    	finally {
    	    close(rcDatos);
    	    close(connection, callableStatement);
    	}
    	return resultCall;
    }

    /**
     * {@inheritDoc}
     */
    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<HashMap<String, Object>> consultaSubterceros(String cif) throws FundeWebJdbcRollBackException {
    	Connection connection = null;
    	CallableStatement callableStatement = null;
    	List<HashMap<String, Object>> resultCall = null;
    	ResultSet rcDatos = null; 
    	StringBuffer sb = new StringBuffer();
    	sb.append("Llamando a consultaSubterceros. Parametros de entrada:")
    	  .append("\n\t Parametro 2: cif - VARCHAR2 - String. Valor: #0");
    	this.getLog().debug(sb.toString(), cif);
    	sb = null;
    	try {
    	    connection = dataSource.getConnection();
    	    callableStatement = connection.prepareCall("{ call SAI.SAI_WEB.consulta_subterceros(?, ?) }");

    	    if (cif == null) {
    	        callableStatement.setNull(2, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(2, cif);
    	    }
    	    callableStatement.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
    	    callableStatement.execute();

    	    rcDatos = (ResultSet) callableStatement.getObject(1);
    	    resultCall = resultSetToList(rcDatos);
    	    sb = new StringBuffer();
    	    sb.append("Llamando a consultaSubterceros. Parametros de Salida:")
    	      .append("\n\t Parametro 1: resultCall - REF_CURSOR - ResultSet. Valor: #0");
    	    this.getLog().debug(sb.toString(), resultCall == null ? 0 : resultCall.size());
    	    sb = null;
    	} catch (Exception e) {
    	    this.getLog().error("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	    throw new FundeWebJdbcRollBackException("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	}
    	finally {
    	    close(rcDatos);
    	    close(connection, callableStatement);
    	}
    	return resultCall;
    }

    /**
     * {@inheritDoc}
     */
    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<HashMap<String, Object>> consultaTiposgastos() throws FundeWebJdbcRollBackException {
    	Connection connection = null;
    	CallableStatement callableStatement = null;
    	List<HashMap<String, Object>> resultCall = null;
    	ResultSet rcDatos = null; 
    	StringBuffer sb = new StringBuffer();
    	sb.append("Llamando a consultaTiposgastos. No tiene parametros de entrada.");
    	this.getLog().debug(sb.toString());
    	sb = null;
    	try {
    	    connection = dataSource.getConnection();
    	    callableStatement = connection.prepareCall("{ call SAI.SAI_WEB.consulta_tiposgastos(?) }");

    	    callableStatement.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
    	    callableStatement.execute();

    	    rcDatos = (ResultSet) callableStatement.getObject(1);
    	    resultCall = resultSetToList(rcDatos);
    	    sb = new StringBuffer();
    	    sb.append("Llamando a consultaTiposgastos. Parametros de Salida:")
    	      .append("\n\t Parametro 1: resultCall - REF_CURSOR - ResultSet. Valor: #0");
    	    this.getLog().debug(sb.toString(), resultCall == null ? 0 : resultCall.size());
    	    sb = null;
    	} catch (Exception e) {
    	    this.getLog().error("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	    throw new FundeWebJdbcRollBackException("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	}
    	finally {
    	    close(rcDatos);
    	    close(connection, callableStatement);
    	}
    	return resultCall;
    }

    
    /**
     * {@inheritDoc}
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void crearCertificacion(BigDecimal wcodUsuario, BigDecimal wcodTipocertificacion, String wnombreFichero, byte[] wfichero, Date wfechaCaducidad) throws FundeWebJdbcRollBackException {
    	Connection connection = null;
    	CallableStatement callableStatement = null;
    	StringBuilder sb = new StringBuilder();
    	sb.append("Llamando a crearCertificacion. Parametros de entrada:")
    	  .append("\n\t Parametro 1: wcodUsuario - NUMERIC - BigDecimal. Valor: #0")
    	  .append("\n\t Parametro 2: wcodTipocertificacion - NUMERIC - BigDecimal. Valor: #1")
    	  .append("\n\t Parametro 3: wnombreFichero - VARCHAR2 - String. Valor: #2")
    	  .append("\n\t Parametro 4: wfichero - BLOB - byte[]. Valor: #3")
    	  .append("\n\t Parametro 5: wfechaCaducidad - DATE - Date. Valor: #4");
    	this.getLog().debug(sb.toString(),
    	                    wcodUsuario,
    	                    wcodTipocertificacion,
    	                    wnombreFichero,
    	                    wfichero == null ? 0 : wfichero.length,
    	                    wfechaCaducidad);
    	sb = null;
    	try {
    	    connection = getConnectionWithClientId(dataSource, APLICACION, getIdUsuario());
    	    callableStatement = connection.prepareCall("{ call SAI.SAI_WEB.crear_certificacion(?, ?, ?, ?, ?) }");

    	    if (wcodUsuario == null) {
    	        callableStatement.setNull(1, java.sql.Types.NUMERIC);
    	    }
    	    else {
    	        callableStatement.setBigDecimal(1, wcodUsuario);
    	    }
    	    if (wcodTipocertificacion == null) {
    	        callableStatement.setNull(2, java.sql.Types.NUMERIC);
    	    }
    	    else {
    	        callableStatement.setBigDecimal(2, wcodTipocertificacion);
    	    }
    	    if (wnombreFichero == null) {
    	        callableStatement.setNull(3, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(3, wnombreFichero);
    	    }
    	    callableStatement.setBytes(4, wfichero);
    	    if (wfechaCaducidad == null) {
    	        callableStatement.setNull(5, java.sql.Types.DATE);
    	    }
    	    else {
    	        callableStatement.setDate(5, javaDateToSqlDate(wfechaCaducidad));
    	    }
    	    callableStatement.execute();

    	    sb = new StringBuilder();
    	    sb.append("Llamando a crearCertificacion. No tiene parametros de salida.");
    	    this.getLog().debug(sb.toString());
    	    sb = null;
    	} catch (Exception e) {
    	    this.getLog().error("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	    throw new FundeWebJdbcRollBackException("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	}
    	finally {
    	    close(connection, callableStatement);
    	}
    }

    /**
     * {@inheritDoc}
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void eliminarCertificacion(BigDecimal wcodCertificacion, String wnombreFichero) throws FundeWebJdbcRollBackException {
    	Connection connection = null;
    	CallableStatement callableStatement = null;
    	StringBuilder sb = new StringBuilder();
    	sb.append("Llamando a eliminarCertificacion. Parametros de entrada:")
    	  .append("\n\t Parametro 1: wcodCertificacion - NUMERIC - BigDecimal. Valor: #0")
    	  .append("\n\t Parametro 2: wnombreFichero - VARCHAR2 - String. Valor: #1");
    	this.getLog().debug(sb.toString(),
    	                    wcodCertificacion,
    	                    wnombreFichero);
    	sb = null;
    	try {
    	    connection = getConnectionWithClientId(dataSource, APLICACION, getIdUsuario());
    	    callableStatement = connection.prepareCall("{ call SAI.SAI_WEB.eliminar_certificacion(?, ?) }");

    	    if (wcodCertificacion == null) {
    	        callableStatement.setNull(1, java.sql.Types.NUMERIC);
    	    }
    	    else {
    	        callableStatement.setBigDecimal(1, wcodCertificacion);
    	    }
    	    if (wnombreFichero == null) {
    	        callableStatement.setNull(2, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(2, wnombreFichero);
    	    }
    	    callableStatement.execute();

    	    sb = new StringBuilder();
    	    sb.append("Llamando a eliminarCertificacion. No tiene parametros de salida.");
    	    this.getLog().debug(sb.toString());
    	    sb = null;
    	} catch (Exception e) {
    	    this.getLog().error("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	    throw new FundeWebJdbcRollBackException("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	}
    	finally {
    	    close(connection, callableStatement);
    	}
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String emailUsuario(String dni) throws FundeWebJdbcRollBackException {
    	Connection connection = null;
    	CallableStatement callableStatement = null;
    	String resultCall = null;
    	StringBuffer sb = new StringBuffer();
    	sb.append("Llamando a emailUsuario. Parametros de entrada:")
    	  .append("\n\t Parametro 2: dni - VARCHAR2 - String. Valor: #0");
    	this.getLog().debug(sb.toString(), dni);
    	sb = null;
    	try {
    	    connection = dataSource.getConnection();
    	    callableStatement = connection.prepareCall("{ ? = call SAI.SAI_WEB.email_usuario(?) }");

    	    if (dni == null) {
    	        callableStatement.setNull(2, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(2, dni);
    	    }
    	    callableStatement.registerOutParameter(1, java.sql.Types.VARCHAR);
    	    callableStatement.execute();

    	    resultCall = callableStatement.getString(1);
    	    sb = new StringBuffer();
    	    sb.append("Llamando a emailUsuario. Parametros de Salida:")
    	      .append("\n\t Parametro 1: resultCall - VARCHAR2 - String. Valor: #0");
    	    this.getLog().debug(sb.toString(), resultCall);
    	    sb = null;
    	} catch (Exception e) {
    	    this.getLog().error("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	    throw new FundeWebJdbcRollBackException("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	}
    	finally {
    	    close(connection, callableStatement);
    	}
    	return resultCall;
    }

    /**
     * {@inheritDoc}
     */
    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public BigDecimal enviaEmail(String wenvia, String wrecibe, String wcopia, String wasunto, String wmensaje, String wcopiaoculta) throws FundeWebJdbcRollBackException {
    	Connection connection = null;
    	CallableStatement callableStatement = null;
    	BigDecimal resultCall = null;
    	StringBuffer sb = new StringBuffer();
    	sb.append("Llamando a enviaEmail. Parametros de entrada:")
    	  .append("\n\t Parametro 2: wenvia - VARCHAR2 - String. Valor: #0")
    	  .append("\n\t Parametro 3: wrecibe - VARCHAR2 - String. Valor: #1")
    	  .append("\n\t Parametro 4: wcopia - VARCHAR2 - String. Valor: #2")
    	  .append("\n\t Parametro 5: wasunto - VARCHAR2 - String. Valor: #3")
    	  .append("\n\t Parametro 6: wmensaje - VARCHAR2 - String. Valor: #4")
    	  .append("\n\t Parametro 7: wcopiaoculta - VARCHAR2 - String. Valor: #5");
    	this.getLog().debug(sb.toString(),
    	                    wenvia,
    	                    wrecibe,
    	                    wcopia,
    	                    wasunto,
    	                    wmensaje,
    	                    wcopiaoculta);
    	sb = null;
    	try {
    	    connection = dataSource.getConnection();
    	    callableStatement = connection.prepareCall("{ ? = call SAI.SAI_WEB.envia_email(?, ?, ?, ?, ?, ?) }");

    	    if (wenvia == null) {
    	        callableStatement.setNull(2, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(2, wenvia);
    	    }
    	    if (wrecibe == null) {
    	        callableStatement.setNull(3, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(3, wrecibe);
    	    }
    	    if (wcopia == null) {
    	        callableStatement.setNull(4, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(4, wcopia);
    	    }
    	    if (wasunto == null) {
    	        callableStatement.setNull(5, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(5, wasunto);
    	    }
    	    if (wmensaje == null) {
    	        callableStatement.setNull(6, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(6, wmensaje);
    	    }
    	    if (wcopiaoculta == null) {
    	        callableStatement.setNull(7, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(7, wcopiaoculta);
    	    }
    	    callableStatement.registerOutParameter(1, java.sql.Types.NUMERIC);
    	    callableStatement.execute();

    	    resultCall = callableStatement.getBigDecimal(1);
    	    sb = new StringBuffer();
    	    sb.append("Llamando a enviaEmail. Parametros de Salida:")
    	      .append("\n\t Parametro 1: resultCall - NUMERIC - BigDecimal. Valor: #0");
    	    this.getLog().debug(sb.toString(), resultCall);
    	    sb = null;
    	} catch (Exception e) {
    	    this.getLog().error("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	    throw new FundeWebJdbcRollBackException("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	}
    	finally {
    	    close(connection, callableStatement);
    	}
    	return resultCall;
    }

    /**
     * {@inheritDoc}
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String esTrabajadorUmu(String wdni) throws FundeWebJdbcRollBackException {
    	Connection connection = null;
    	CallableStatement callableStatement = null;
    	String resultCall = null;
    	StringBuilder sb = new StringBuilder();
    	sb.append("Llamando a esTrabajadorUmu. Parametros de entrada:")
    	  .append("\n\t Parametro 2: wdni - VARCHAR2 - String. Valor: #0");
    	this.getLog().debug(sb.toString(), wdni);
    	sb = null;
    	try {
    	    connection = dataSource.getConnection();
    	    callableStatement = connection.prepareCall("{ ? = call SAI.SAI_WEB.es_trabajador_umu(?) }");

    	    if (wdni == null) {
    	        callableStatement.setNull(2, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(2, wdni);
    	    }
    	    callableStatement.registerOutParameter(1, java.sql.Types.VARCHAR);
    	    callableStatement.execute();

    	    resultCall = callableStatement.getString(1);
    	    sb = new StringBuilder();
    	    sb.append("Llamando a esTrabajadorUmu. Parametros de Salida:")
    	      .append("\n\t Parametro 1: resultCall - VARCHAR2 - String. Valor: #0");
    	    this.getLog().debug(sb.toString(), resultCall);
    	    sb = null;
    	} catch (Exception e) {
    	    this.getLog().error("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	    throw new FundeWebJdbcRollBackException("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	}
    	finally {
    	    close(connection, callableStatement);
    	}
    	return resultCall;
    }

    /**
     * {@inheritDoc}
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public HashMap<String, Object> getResumenConsumosExcel(String wtipoDesglose, String wtipo, BigDecimal wcodServicio, BigDecimal wcodProducto, String winterno, BigDecimal wcodUsuarioIr, BigDecimal wcodUsuarioSolic, String westado, String westadoFacturacion, String wfechaDesde, String wfechaHasta) throws FundeWebJdbcRollBackException {
    	Connection connection = null;
    	CallableStatement callableStatement = null;
    	HashMap<String, Object> resultCall = null;
    	StringBuilder sb = new StringBuilder();
    	sb.append("Llamando a getResumenConsumosExcel. Parametros de entrada:")
    	  .append("\n\t Parametro 1: wtipoDesglose - VARCHAR2 - String. Valor: #0")
    	  .append("\n\t Parametro 2: wtipo - VARCHAR2 - String. Valor: #1")
    	  .append("\n\t Parametro 3: wcodServicio - NUMERIC - BigDecimal. Valor: #2")
    	  .append("\n\t Parametro 4: wcodProducto - NUMERIC - BigDecimal. Valor: #3")
    	  .append("\n\t Parametro 5: winterno - VARCHAR2 - String. Valor: #4")
    	  .append("\n\t Parametro 6: wcodUsuarioIr - NUMERIC - BigDecimal. Valor: #5")
    	  .append("\n\t Parametro 7: wcodUsuarioSolic - NUMERIC - BigDecimal. Valor: #6")
    	  .append("\n\t Parametro 8: westado - VARCHAR2 - String. Valor: #7")
    	  .append("\n\t Parametro 9: westadoFacturacion - VARCHAR2 - String. Valor: #8")
    	  .append("\n\t Parametro 10: wfechaDesde - VARCHAR2 - String. Valor: #9");
    	this.getLog().debug(sb.toString(),
    	                    wtipoDesglose,
    	                    wtipo,
    	                    wcodServicio,
    	                    wcodProducto,
    	                    winterno,
    	                    wcodUsuarioIr,
    	                    wcodUsuarioSolic,
    	                    westado,
    	                    westadoFacturacion,
    	                    wfechaDesde);
    	sb = new StringBuilder()
    	  .append("\n\t Parametro 11: wfechaHasta - VARCHAR2 - String. Valor: #0");
    	this.getLog().debug(sb.toString(),
    	                    wfechaHasta);
    	sb = null;
    	try {
    	    connection = dataSource.getConnection();
    	    callableStatement = connection.prepareCall("{ call SAI.SAI_WEB.get_resumen_consumos_excel(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");

    	    if (wtipoDesglose == null) {
    	        callableStatement.setNull(1, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(1, wtipoDesglose);
    	    }
    	    if (wtipo == null) {
    	        callableStatement.setNull(2, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(2, wtipo);
    	    }
    	    if (wcodServicio == null) {
    	        callableStatement.setNull(3, java.sql.Types.NUMERIC);
    	    }
    	    else {
    	        callableStatement.setBigDecimal(3, wcodServicio);
    	    }
    	    if (wcodProducto == null) {
    	        callableStatement.setNull(4, java.sql.Types.NUMERIC);
    	    }
    	    else {
    	        callableStatement.setBigDecimal(4, wcodProducto);
    	    }
    	    if (winterno == null) {
    	        callableStatement.setNull(5, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(5, winterno);
    	    }
    	    if (wcodUsuarioIr == null) {
    	        callableStatement.setNull(6, java.sql.Types.NUMERIC);
    	    }
    	    else {
    	        callableStatement.setBigDecimal(6, wcodUsuarioIr);
    	    }
    	    if (wcodUsuarioSolic == null) {
    	        callableStatement.setNull(7, java.sql.Types.NUMERIC);
    	    }
    	    else {
    	        callableStatement.setBigDecimal(7, wcodUsuarioSolic);
    	    }
    	    if (westado == null) {
    	        callableStatement.setNull(8, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(8, westado);
    	    }
    	    if (westadoFacturacion == null) {
    	        callableStatement.setNull(9, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(9, westadoFacturacion);
    	    }
    	    if (wfechaDesde == null) {
    	        callableStatement.setNull(10, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(10, wfechaDesde);
    	    }
    	    if (wfechaHasta == null) {
    	        callableStatement.setNull(11, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(11, wfechaHasta);
    	    }
    	    callableStatement.registerOutParameter(12, java.sql.Types.NUMERIC);
    	    callableStatement.registerOutParameter(13, java.sql.Types.VARCHAR);
    	    callableStatement.registerOutParameter(14, java.sql.Types.BLOB);
    	    callableStatement.execute();

    	    resultCall = new HashMap<String, Object>();
    	    resultCall.put("wcodError", callableStatement.getBigDecimal(12));
    	    resultCall.put("wmenError", callableStatement.getString(13));
    	    resultCall.put("wblobExcel", callableStatement.getBytes(14));
    	    sb = new StringBuilder();
    	    sb.append("Llamando a getResumenConsumosExcel. Parametros de Salida:")
    	      .append("\n\t Parametro 12: wcodError - NUMERIC - BigDecimal. Valor: #0")
    	      .append("\n\t Parametro 13: wmenError - VARCHAR2 - String. Valor: #1")
    	      .append("\n\t Parametro 14: wblobExcel - BLOB - byte[]. Valor: #2");
    	    this.getLog().debug(sb.toString(),
    	                        ((BigDecimal) resultCall.get("wcodError")),
    	                        ((String) resultCall.get("wmenError")),
    	                        (resultCall.get("wblobExcel")) == null ? 0 : (resultCall.get("wblobExcel")).getClass().toString());
    	    sb = null;
    	} catch (Exception e) {
    	    this.getLog().error("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	    throw new FundeWebJdbcRollBackException("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	}
    	finally {
    	    close(connection, callableStatement);
    	}
    	return resultCall;
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public byte[] getConsumoHtml(BigDecimal wcodConsumo) throws FundeWebJdbcRollBackException {
    	Connection connection = null;
    	CallableStatement callableStatement = null;
    	byte[] resultCall = null;
    	StringBuilder sb = new StringBuilder();
    	sb.append("Llamando a getConsumoHtml. Parametros de entrada:")
    	  .append("\n\t Parametro 2: wcodConsumo - NUMERIC - BigDecimal. Valor: #0");
    	this.getLog().debug(sb.toString(), wcodConsumo);
    	sb = null;
    	try {
    	    connection = dataSource.getConnection();
    	    callableStatement = connection.prepareCall("{ ? = call SAI.SAI_WEB.get_consumo_html(?) }");

    	    if (wcodConsumo == null) {
    	        callableStatement.setNull(2, java.sql.Types.NUMERIC);
    	    }
    	    else {
    	        callableStatement.setBigDecimal(2, wcodConsumo);
    	    }
    	    callableStatement.registerOutParameter(1, java.sql.Types.BLOB);
    	    callableStatement.execute();

    	    resultCall = callableStatement.getBytes(1);
    	    sb = new StringBuilder();
    	    sb.append("Llamando a getConsumoHtml. Parametros de Salida:")
    	      .append("\n\t Parametro 1: resultCall - BLOB - byte[]. Valor: #0");
    	    this.getLog().debug(sb.toString(), resultCall == null ? 0 : resultCall.length);
    	    sb = null;
    	} catch (Exception e) {
    	    this.getLog().error("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	    throw new FundeWebJdbcRollBackException("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	}
    	finally {
    	    close(connection, callableStatement);
    	}
    	return resultCall;
    }    
    
    /**
     * {@inheritDoc}
     */
    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String getDniEmail(String wemail) throws FundeWebJdbcRollBackException {
    	Connection connection = null;
    	CallableStatement callableStatement = null;
    	String resultCall = null;
    	StringBuffer sb = new StringBuffer();
    	sb.append("Llamando a getDniEmail. Parametros de entrada:")
    	  .append("\n\t Parametro 2: wemail - VARCHAR2 - String. Valor: #0");
    	this.getLog().debug(sb.toString(), wemail);
    	sb = null;
    	try {
    	    connection = dataSource.getConnection();
    	    callableStatement = connection.prepareCall("{ ? = call SAI.SAI_WEB.get_dni_email(?) }");

    	    if (wemail == null) {
    	        callableStatement.setNull(2, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(2, wemail);
    	    }
    	    callableStatement.registerOutParameter(1, java.sql.Types.VARCHAR);
    	    callableStatement.execute();

    	    resultCall = callableStatement.getString(1);
    	    sb = new StringBuffer();
    	    sb.append("Llamando a getDniEmail. Parametros de Salida:")
    	      .append("\n\t Parametro 1: resultCall - VARCHAR2 - String. Valor: #0");
    	    this.getLog().debug(sb.toString(), resultCall);
    	    sb = null;
    	} catch (Exception e) {
    	    this.getLog().error("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	    throw new FundeWebJdbcRollBackException("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	}
    	finally {
    	    close(connection, callableStatement);
    	}
    	return resultCall;
    }

    /**
     * {@inheritDoc}
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Date getFechafinEstrabajadorumu(String wdni) throws FundeWebJdbcRollBackException {
    	Connection connection = null;
    	CallableStatement callableStatement = null;
    	Date resultCall = null;
    	StringBuilder sb = new StringBuilder();
    	sb.append("Llamando a getFechafinEstrabajadorumu. Parametros de entrada:")
    	  .append("\n\t Parametro 2: wdni - VARCHAR2 - String. Valor: #0");
    	this.getLog().debug(sb.toString(), wdni);
    	sb = null;
    	try {
    	    connection = dataSource.getConnection();
    	    callableStatement = connection.prepareCall("{ ? = call SAI.SAI_WEB.get_fechafin_estrabajadorumu(?) }");

    	    if (wdni == null) {
    	        callableStatement.setNull(2, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(2, wdni);
    	    }
    	    callableStatement.registerOutParameter(1, java.sql.Types.DATE);
    	    callableStatement.execute();

    	    resultCall = callableStatement.getDate(1);
    	    sb = new StringBuilder();
    	    sb.append("Llamando a getFechafinEstrabajadorumu. Parametros de Salida:")
    	      .append("\n\t Parametro 1: resultCall - DATE - Date. Valor: #0");
    	    this.getLog().debug(sb.toString(), resultCall);
    	    sb = null;
    	} catch (Exception e) {
    	    this.getLog().error("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	    throw new FundeWebJdbcRollBackException("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	}
    	finally {
    	    close(connection, callableStatement);
    	}
    	return resultCall;
    }

    /**
     * {@inheritDoc}
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String getNombreSubtercero(String wcif, BigDecimal wcodigo) throws FundeWebJdbcRollBackException {
    	Connection connection = null;
    	CallableStatement callableStatement = null;
    	String resultCall = null;
    	StringBuilder sb = new StringBuilder();
    	sb.append("Llamando a getNombreSubtercero. Parametros de entrada:")
    	  .append("\n\t Parametro 2: wcif - VARCHAR2 - String. Valor: #0")
    	  .append("\n\t Parametro 3: wcodigo - NUMERIC - BigDecimal. Valor: #1");
    	this.getLog().debug(sb.toString(),
    	                    wcif,
    	                    wcodigo);
    	sb = null;
    	try {
    	    connection = dataSource.getConnection();
    	    callableStatement = connection.prepareCall("{ ? = call SAI.SAI_WEB.get_nombre_subtercero(?, ?) }");

    	    if (wcif == null) {
    	        callableStatement.setNull(2, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(2, wcif);
    	    }
    	    if (wcodigo == null) {
    	        callableStatement.setNull(3, java.sql.Types.NUMERIC);
    	    }
    	    else {
    	        callableStatement.setBigDecimal(3, wcodigo);
    	    }
    	    callableStatement.registerOutParameter(1, java.sql.Types.VARCHAR);
    	    callableStatement.execute();

    	    resultCall = callableStatement.getString(1);
    	    sb = new StringBuilder();
    	    sb.append("Llamando a getNombreSubtercero. Parametros de Salida:")
    	      .append("\n\t Parametro 1: resultCall - VARCHAR2 - String. Valor: #0");
    	    this.getLog().debug(sb.toString(), resultCall);
    	    sb = null;
    	} catch (Exception e) {
    	    this.getLog().error("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	    throw new FundeWebJdbcRollBackException("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	}
    	finally {
    	    close(connection, callableStatement);
    	}
    	return resultCall;
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public HashMap<String, Object> getPersona(String dni) throws FundeWebJdbcRollBackException {
    	Connection connection = null;
    	CallableStatement callableStatement = null;
    	HashMap<String, Object> resultCall = null;
    	StringBuffer sb = new StringBuffer();
    	sb.append("Llamando a getPersona. Parametros de entrada:")
    	  .append("\n\t Parametro 2: dni - VARCHAR2 - String. Valor: #0");
    	this.getLog().debug(sb.toString(), dni);
    	sb = null;
    	try {
    	    connection = dataSource.getConnection();
    	    callableStatement = connection.prepareCall("{ ? = call SAI.SAI_WEB.get_persona(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");

    	    if (dni == null) {
    	        callableStatement.setNull(2, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(2, dni);
    	    }
    	    callableStatement.registerOutParameter(1, java.sql.Types.NUMERIC);
    	    callableStatement.registerOutParameter(3, java.sql.Types.VARCHAR);
    	    callableStatement.registerOutParameter(4, java.sql.Types.VARCHAR);
    	    callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
    	    callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
    	    callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
    	    callableStatement.registerOutParameter(8, java.sql.Types.VARCHAR);
    	    callableStatement.registerOutParameter(9, java.sql.Types.VARCHAR);
    	    callableStatement.registerOutParameter(10, java.sql.Types.VARCHAR);
    	    callableStatement.registerOutParameter(11, java.sql.Types.VARCHAR);
    	    callableStatement.execute();

    	    resultCall = new HashMap<String, Object>();
    	    resultCall.put("getPersona", callableStatement.getBigDecimal(1));
    	    resultCall.put("nombre", callableStatement.getString(3));
    	    resultCall.put("apellidos", callableStatement.getString(4));
    	    resultCall.put("fechaNacimiento", callableStatement.getString(5));
    	    resultCall.put("domicilio", callableStatement.getString(6));
    	    resultCall.put("localidad", callableStatement.getString(7));
    	    resultCall.put("cp", callableStatement.getString(8));
    	    resultCall.put("provincia", callableStatement.getString(9));
    	    resultCall.put("telefono", callableStatement.getString(10));
    	    resultCall.put("movil", callableStatement.getString(11));
    	    sb = new StringBuffer();
    	    sb.append("Llamando a getPersona. Parametros de Salida:")
    	      .append("\n\t Parametro 1: getPersona - NUMERIC - BigDecimal. Valor: #0")
    	      .append("\n\t Parametro 3: nombre - VARCHAR2 - String. Valor: #1")
    	      .append("\n\t Parametro 4: apellidos - VARCHAR2 - String. Valor: #2")
    	      .append("\n\t Parametro 5: fechaNacimiento - VARCHAR2 - String. Valor: #3")
    	      .append("\n\t Parametro 6: domicilio - VARCHAR2 - String. Valor: #4")
    	      .append("\n\t Parametro 7: localidad - VARCHAR2 - String. Valor: #5")
    	      .append("\n\t Parametro 8: cp - VARCHAR2 - String. Valor: #6")
    	      .append("\n\t Parametro 9: provincia - VARCHAR2 - String. Valor: #7")
    	      .append("\n\t Parametro 10: telefono - VARCHAR2 - String. Valor: #8")
    	      .append("\n\t Parametro 11: movil - VARCHAR2 - String. Valor: #9");
    	    this.getLog().debug(sb.toString(),
    	                        (resultCall.get("getPersona")),
    	                        (resultCall.get("nombre")),
    	                        (resultCall.get("apellidos")),
    	                        (resultCall.get("fechaNacimiento")),
    	                        (resultCall.get("domicilio")),
    	                        (resultCall.get("localidad")),
    	                        (resultCall.get("cp")),
    	                        (resultCall.get("provincia")),
    	                        (resultCall.get("telefono")),
    	                        (resultCall.get("movil")));
    	    sb = null;
    	} catch (Exception e) {
    	    this.getLog().error("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	    throw new FundeWebJdbcRollBackException("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	}
    	finally {
    	    close(connection, callableStatement);
    	}
    	return resultCall;
    }

    /**
     * {@inheritDoc}
     */
    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public BigDecimal getPrecio(String wunid, String wprod) throws FundeWebJdbcRollBackException {
    	Connection connection = null;
    	CallableStatement callableStatement = null;
    	BigDecimal resultCall = null;
    	StringBuffer sb = new StringBuffer();
    	sb.append("Llamando a getPrecio. Parametros de entrada:")
    	  .append("\n\t Parametro 2: wunid - VARCHAR2 - String. Valor: #0")
    	  .append("\n\t Parametro 3: wprod - VARCHAR2 - String. Valor: #1");
    	this.getLog().debug(sb.toString(),
    	                    wunid,
    	                    wprod);
    	sb = null;
    	try {
    	    connection = dataSource.getConnection();
    	    callableStatement = connection.prepareCall("{ ? = call SAI.SAI_WEB.get_precio(?, ?) }");

    	    if (wunid == null) {
    	        callableStatement.setNull(2, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(2, wunid);
    	    }
    	    if (wprod == null) {
    	        callableStatement.setNull(3, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(3, wprod);
    	    }
    	    callableStatement.registerOutParameter(1, java.sql.Types.NUMERIC);
    	    callableStatement.execute();

    	    resultCall = callableStatement.getBigDecimal(1);
    	    sb = new StringBuffer();
    	    sb.append("Llamando a getPrecio. Parametros de Salida:")
    	      .append("\n\t Parametro 1: resultCall - NUMERIC - BigDecimal. Valor: #0");
    	    this.getLog().debug(sb.toString(), resultCall);
    	    sb = null;
    	} catch (Exception e) {
    	    this.getLog().error("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	    throw new FundeWebJdbcRollBackException("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	}
    	finally {
    	    close(connection, callableStatement);
    	}
    	return resultCall;
    }

    /**
     * {@inheritDoc}
     */
    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<HashMap<String, Object>> getProductosSai(String prod) throws FundeWebJdbcRollBackException {
    	Connection connection = null;
    	CallableStatement callableStatement = null;
    	List<HashMap<String, Object>> resultCall = null;
    	ResultSet rcDatos = null; 
    	StringBuffer sb = new StringBuffer();
    	sb.append("Llamando a getProductosSai. Parametros de entrada:")
    	  .append("\n\t Parametro 2: prod - VARCHAR2 - String. Valor: #0");
    	this.getLog().debug(sb.toString(), prod);
    	sb = null;
    	try {
    	    connection = dataSource.getConnection();
    	    callableStatement = connection.prepareCall("{ call SAI.SAI_WEB.get_productos_sai(?, ?) }");

    	    if (prod == null) {
    	        callableStatement.setNull(2, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(2, prod);
    	    }
    	    callableStatement.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
    	    callableStatement.execute();

    	    rcDatos = (ResultSet) callableStatement.getObject(1);
    	    resultCall = resultSetToList(rcDatos);
    	    sb = new StringBuffer();
    	    sb.append("Llamando a getProductosSai. Parametros de Salida:")
    	      .append("\n\t Parametro 1: resultCall - REF_CURSOR - ResultSet. Valor: #0");
    	    this.getLog().debug(sb.toString(), resultCall == null ? 0 : resultCall.size());
    	    sb = null;
    	} catch (Exception e) {
    	    this.getLog().error("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	    throw new FundeWebJdbcRollBackException("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	}
    	finally {
    	    close(rcDatos);
    	    close(connection, callableStatement);
    	}
    	return resultCall;
    }

    /**
     * {@inheritDoc}
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<HashMap<String, Object>> getTerminalesKron() throws FundeWebJdbcRollBackException {
    	Connection connection = null;
    	CallableStatement callableStatement = null;
    	List<HashMap<String, Object>> resultCall = null;
    	ResultSet getTerminalesKron = null; 
    	StringBuilder sb = new StringBuilder();
    	sb.append("Llamando a getTerminalesKron. No tiene parametros de entrada.");
    	this.getLog().debug(sb.toString());
    	sb = null;
    	try {
    	    connection = dataSource.getConnection();
    	    callableStatement = connection.prepareCall("{ ? = call SAI.SAI_WEB.get_terminales_kron() }");

    	    callableStatement.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
    	    callableStatement.execute();

    	    getTerminalesKron = (ResultSet) callableStatement.getObject(1);
    	    resultCall = resultSetToList(getTerminalesKron);
    	    sb = new StringBuilder();
    	    sb.append("Llamando a getTerminalesKron. Parametros de Salida:")
    	      .append("\n\t Parametro 1: resultCall - REF_CURSOR - ResultSet. Valor: #0");
    	    this.getLog().debug(sb.toString(), resultCall == null ? 0 : resultCall.size());
    	    sb = null;
    	} catch (Exception e) {
    	    this.getLog().error("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	    throw new FundeWebJdbcRollBackException("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	}
    	finally {
    	    close(getTerminalesKron);
    	    close(connection, callableStatement);
    	}
    	return resultCall;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public HashMap<String, Object> guardarFacemiJusto(BigDecimal wfacturasai, BigDecimal tipotarifaep, String wserie, String weje, String wejeproce, String wvic, String wunid, String wpro, String weco, BigDecimal wnumproy, BigDecimal wimporte, String wdescri, String wcodter, String wsubter, String wfecemi, String wsubtercero) throws FundeWebJdbcRollBackException {
    	Connection connection = null;
    	CallableStatement callableStatement = null;
    	HashMap<String, Object> resultCall = null;
    	StringBuffer sb = new StringBuffer();
    	sb.append("Llamando a guardarFacemiJusto. Parametros de entrada:")
    	  .append("\n\t Parametro 1: wfacturasai - NUMERIC - BigDecimal. Valor: #0")
    	  .append("\n\t Parametro 2: tipotarifaep - NUMERIC - BigDecimal. Valor: #1")
    	  .append("\n\t Parametro 3: wserie - VARCHAR2 - String. Valor: #2")
    	  .append("\n\t Parametro 4: weje - VARCHAR2 - String. Valor: #3")
    	  .append("\n\t Parametro 5: wejeproce - VARCHAR2 - String. Valor: #4")
    	  .append("\n\t Parametro 6: wvic - VARCHAR2 - String. Valor: #5")
    	  .append("\n\t Parametro 7: wunid - VARCHAR2 - String. Valor: #6")
    	  .append("\n\t Parametro 8: wpro - VARCHAR2 - String. Valor: #7")
    	  .append("\n\t Parametro 9: weco - VARCHAR2 - String. Valor: #8")
    	  .append("\n\t Parametro 10: wnumproy - NUMERIC - BigDecimal. Valor: #9");
    	this.getLog().debug(sb.toString(),
    	                    wfacturasai,
    	                    tipotarifaep,
    	                    wserie,
    	                    weje,
    	                    wejeproce,
    	                    wvic,
    	                    wunid,
    	                    wpro,
    	                    weco,
    	                    wnumproy);
    	sb = new StringBuffer()
    	  .append("\n\t Parametro 11: wimporte - NUMERIC - BigDecimal. Valor: #0")
    	  .append("\n\t Parametro 12: wdescri - VARCHAR2 - String. Valor: #1")
    	  .append("\n\t Parametro 13: wcodter - VARCHAR2 - String. Valor: #2")
    	  .append("\n\t Parametro 14: wsubter - VARCHAR2 - String. Valor: #3")
    	  .append("\n\t Parametro 15: wfecemi - VARCHAR2 - String. Valor: #4")
    	  .append("\n\t Parametro 16: wsubtercero - VARCHAR2 - String. Valor: #5");
    	this.getLog().debug(sb.toString(),
    	                    wimporte,
    	                    wdescri,
    	                    wcodter,
    	                    wsubter,
    	                    wfecemi,
    	                    wsubtercero);
    	sb = null;
    	try {
    	    connection = getConnectionWithClientId(dataSource, APLICACION, getIdUsuario());
    	    callableStatement = connection.prepareCall("{ call SAI.SAI_WEB.guardar_facemi_justo(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");

    	    if (wfacturasai == null) {
    	        callableStatement.setNull(1, java.sql.Types.NUMERIC);
    	    }
    	    else {
    	        callableStatement.setBigDecimal(1, wfacturasai);
    	    }
    	    if (tipotarifaep == null) {
    	        callableStatement.setNull(2, java.sql.Types.NUMERIC);
    	    }
    	    else {
    	        callableStatement.setBigDecimal(2, tipotarifaep);
    	    }
    	    if (wserie == null) {
    	        callableStatement.setNull(3, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(3, wserie);
    	    }
    	    if (weje == null) {
    	        callableStatement.setNull(4, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(4, weje);
    	    }
    	    if (wejeproce == null) {
    	        callableStatement.setNull(5, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(5, wejeproce);
    	    }
    	    if (wvic == null) {
    	        callableStatement.setNull(6, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(6, wvic);
    	    }
    	    if (wunid == null) {
    	        callableStatement.setNull(7, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(7, wunid);
    	    }
    	    if (wpro == null) {
    	        callableStatement.setNull(8, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(8, wpro);
    	    }
    	    if (weco == null) {
    	        callableStatement.setNull(9, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(9, weco);
    	    }
    	    if (wnumproy == null) {
    	        callableStatement.setNull(10, java.sql.Types.NUMERIC);
    	    }
    	    else {
    	        callableStatement.setBigDecimal(10, wnumproy);
    	    }
    	    if (wimporte == null) {
    	        callableStatement.setNull(11, java.sql.Types.NUMERIC);
    	    }
    	    else {
    	        callableStatement.setBigDecimal(11, wimporte);
    	    }
    	    if (wdescri == null) {
    	        callableStatement.setNull(12, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(12, wdescri);
    	    }
    	    if (wcodter == null) {
    	        callableStatement.setNull(13, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(13, wcodter);
    	    }
    	    if (wsubter == null) {
    	        callableStatement.setNull(14, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(14, wsubter);
    	    }
    	    if (wfecemi == null) {
    	        callableStatement.setNull(15, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(15, wfecemi);
    	    }
    	    if (wsubtercero == null) {
    	        callableStatement.setNull(16, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(16, wsubtercero);
    	    }
    	    callableStatement.registerOutParameter(17, java.sql.Types.VARCHAR);
    	    callableStatement.registerOutParameter(18, java.sql.Types.VARCHAR);
    	    callableStatement.registerOutParameter(19, java.sql.Types.NUMERIC);
    	    callableStatement.registerOutParameter(20, java.sql.Types.VARCHAR);
    	    callableStatement.execute();

    	    resultCall = new HashMap<String, Object>();
    	    resultCall.put("wanofacemi", callableStatement.getString(17));
    	    resultCall.put("wnumfacemi", callableStatement.getString(18));
    	    resultCall.put("wcoderror", callableStatement.getBigDecimal(19));
    	    resultCall.put("wmenerror", callableStatement.getString(20));
    	    sb = new StringBuffer();
    	    sb.append("Llamando a guardarFacemiJusto. Parametros de Salida:")
    	      .append("\n\t Parametro 17: wanofacemi - VARCHAR2 - String. Valor: #0")
    	      .append("\n\t Parametro 18: wnumfacemi - VARCHAR2 - String. Valor: #1")
    	      .append("\n\t Parametro 19: wcoderror - NUMERIC - BigDecimal. Valor: #2")
    	      .append("\n\t Parametro 20: wmenerror - VARCHAR2 - String. Valor: #3");
    	    this.getLog().debug(sb.toString(),
    	                        (resultCall.get("wanofacemi")),
    	                        (resultCall.get("wnumfacemi")),
    	                        (resultCall.get("wcoderror")),
    	                        (resultCall.get("wmenerror")));
    	    sb = null;
    	} catch (Exception e) {
    	    this.getLog().error("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	    throw new FundeWebJdbcRollBackException("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	}
    	finally {
    	    close(connection, callableStatement);
    	}
    	return resultCall;
    }

    /**
     * {@inheritDoc}
     */
    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public HashMap<String, Object> guardarFacriJusto(BigDecimal wfacturasai, BigDecimal tipotarifaep, String wdescripcion, String wtipogasto, BigDecimal wimporte, String wdnisoli, String wunidadamori, String worigen, String wtiplin, String weje, String wejeproce, String wvic, String wunid, String wpro, String weco, String wtipoeco, BigDecimal wnumproy) throws FundeWebJdbcRollBackException {
    	Connection connection = null;
    	CallableStatement callableStatement = null;
    	HashMap<String, Object> resultCall = null;
    	StringBuffer sb = new StringBuffer();
    	sb.append("Llamando a guardarFacriJusto. Parametros de entrada:")
    	  .append("\n\t Parametro 1: wfacturasai - NUMERIC - BigDecimal. Valor: #0")
    	  .append("\n\t Parametro 2: tipotarifaep - NUMERIC - BigDecimal. Valor: #1")
    	  .append("\n\t Parametro 3: wdescripcion - VARCHAR2 - String. Valor: #2")
    	  .append("\n\t Parametro 4: wtipogasto - VARCHAR2 - String. Valor: #3")
    	  .append("\n\t Parametro 5: wimporte - NUMERIC - BigDecimal. Valor: #4")
    	  .append("\n\t Parametro 6: wdnisoli - VARCHAR2 - String. Valor: #5")
    	  .append("\n\t Parametro 7: wunidadamori - VARCHAR2 - String. Valor: #6")
    	  .append("\n\t Parametro 8: worigen - VARCHAR2 - String. Valor: #7")
    	  .append("\n\t Parametro 9: wtiplin - VARCHAR2 - String. Valor: #8")
    	  .append("\n\t Parametro 10: weje - VARCHAR2 - String. Valor: #9");
    	this.getLog().debug(sb.toString(),
    	                    wfacturasai,
    	                    tipotarifaep,
    	                    wdescripcion,
    	                    wtipogasto,
    	                    wimporte,
    	                    wdnisoli,
    	                    wunidadamori,
    	                    worigen,
    	                    wtiplin,
    	                    weje);
    	sb = new StringBuffer()
    	  .append("\n\t Parametro 11: wejeproce - VARCHAR2 - String. Valor: #0")
    	  .append("\n\t Parametro 12: wvic - VARCHAR2 - String. Valor: #1")
    	  .append("\n\t Parametro 13: wunid - VARCHAR2 - String. Valor: #2")
    	  .append("\n\t Parametro 14: wpro - VARCHAR2 - String. Valor: #3")
    	  .append("\n\t Parametro 15: weco - VARCHAR2 - String. Valor: #4")
    	  .append("\n\t Parametro 16: wtipoeco - VARCHAR2 - String. Valor: #5")
    	  .append("\n\t Parametro 17: wnumproy - NUMERIC - BigDecimal. Valor: #6");
    	this.getLog().debug(sb.toString(),
    	                    wejeproce,
    	                    wvic,
    	                    wunid,
    	                    wpro,
    	                    weco,
    	                    wtipoeco,
    	                    wnumproy);
    	sb = null;
    	try {
    	    connection = getConnectionWithClientId(dataSource, APLICACION, getIdUsuario());
    	    callableStatement = connection.prepareCall("{ call SAI.SAI_WEB.guardar_facri_justo(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");

    	    if (wfacturasai == null) {
    	        callableStatement.setNull(1, java.sql.Types.NUMERIC);
    	    }
    	    else {
    	        callableStatement.setBigDecimal(1, wfacturasai);
    	    }
    	    if (tipotarifaep == null) {
    	        callableStatement.setNull(2, java.sql.Types.NUMERIC);
    	    }
    	    else {
    	        callableStatement.setBigDecimal(2, tipotarifaep);
    	    }
    	    if (wdescripcion == null) {
    	        callableStatement.setNull(3, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(3, wdescripcion);
    	    }
    	    if (wtipogasto == null) {
    	        callableStatement.setNull(4, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(4, wtipogasto);
    	    }
    	    if (wimporte == null) {
    	        callableStatement.setNull(5, java.sql.Types.NUMERIC);
    	    }
    	    else {
    	        callableStatement.setBigDecimal(5, wimporte);
    	    }
    	    if (wdnisoli == null) {
    	        callableStatement.setNull(6, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(6, wdnisoli);
    	    }
    	    if (wunidadamori == null) {
    	        callableStatement.setNull(7, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(7, wunidadamori);
    	    }
    	    if (worigen == null) {
    	        callableStatement.setNull(8, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(8, worigen);
    	    }
    	    if (wtiplin == null) {
    	        callableStatement.setNull(9, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(9, wtiplin);
    	    }
    	    if (weje == null) {
    	        callableStatement.setNull(10, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(10, weje);
    	    }
    	    if (wejeproce == null) {
    	        callableStatement.setNull(11, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(11, wejeproce);
    	    }
    	    if (wvic == null) {
    	        callableStatement.setNull(12, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(12, wvic);
    	    }
    	    if (wunid == null) {
    	        callableStatement.setNull(13, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(13, wunid);
    	    }
    	    if (wpro == null) {
    	        callableStatement.setNull(14, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(14, wpro);
    	    }
    	    if (weco == null) {
    	        callableStatement.setNull(15, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(15, weco);
    	    }
    	    if (wtipoeco == null) {
    	        callableStatement.setNull(16, java.sql.Types.VARCHAR);
    	    }
    	    else {
    	        callableStatement.setString(16, wtipoeco);
    	    }
    	    if (wnumproy == null) {
    	        callableStatement.setNull(17, java.sql.Types.NUMERIC);
    	    }
    	    else {
    	        callableStatement.setBigDecimal(17, wnumproy);
    	    }
    	    callableStatement.registerOutParameter(18, java.sql.Types.VARCHAR);
    	    callableStatement.registerOutParameter(19, java.sql.Types.VARCHAR);
    	    callableStatement.registerOutParameter(20, java.sql.Types.NUMERIC);
    	    callableStatement.registerOutParameter(21, java.sql.Types.VARCHAR);
    	    callableStatement.execute();

    	    resultCall = new HashMap<String, Object>();
    	    resultCall.put("noseutiliza", callableStatement.getString(18));
    	    resultCall.put("noseutiliza2", callableStatement.getString(19));
    	    resultCall.put("wcoderror", callableStatement.getBigDecimal(20));
    	    resultCall.put("wmenerror", callableStatement.getString(21));
    	    sb = new StringBuffer();
    	    sb.append("Llamando a guardarFacriJusto. Parametros de Salida:")
    	      .append("\n\t Parametro 18: noseutiliza - VARCHAR2 - String. Valor: #0")
    	      .append("\n\t Parametro 19: noseutiliza2 - VARCHAR2 - String. Valor: #1")
    	      .append("\n\t Parametro 20: wcoderror - NUMERIC - BigDecimal. Valor: #2")
    	      .append("\n\t Parametro 21: wmenerror - VARCHAR2 - String. Valor: #3");
    	    this.getLog().debug(sb.toString(),
    	                        (resultCall.get("noseutiliza")),
    	                        (resultCall.get("noseutiliza2")),
    	                        (resultCall.get("wcoderror")),
    	                        (resultCall.get("wmenerror")));
    	    sb = null;
    	} catch (Exception e) {
    	    this.getLog().error("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	    throw new FundeWebJdbcRollBackException("Error al ejecutar la funcion o procedimiento PL/SQL.", e);
    	}
    	finally {
    	    close(connection, callableStatement);
    	}
    	return resultCall;
    }

    @Override
    protected Log getLog() {
    	return log;
    }

    @Override
    protected DataSource getDataSource() {
    	return this.dataSource;
    }
    
    private String getIdUsuario()
    {
    	try
    	{
    		return identity.getUsuarioConectado().getDatosUsuario().getDni();
    	}
    	catch(Exception ex)
        {
            return null;
        }
    }

}
