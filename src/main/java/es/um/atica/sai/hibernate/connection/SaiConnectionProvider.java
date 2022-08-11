package es.um.atica.sai.hibernate.connection;

import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.service.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;

import es.um.atica.jdbc.utils.OracleUtils;
import es.um.atica.sai.security.authentication.SaiIdentity;


public class SaiConnectionProvider extends DatasourceConnectionProviderImpl{

	private static final long serialVersionUID = -6036673785228822773L;

	@Logger
    Log log;
    
	private static final String APLICACION = "SAI";
    
    public SaiConnectionProvider() {
    	//Constructor vacío
    }
    
    protected String getIdUsuario() 
    {
        try
        {
            return ((SaiIdentity) Identity.instance()).getUsuarioConectado().getDatosUsuario().getDni();
        }
        catch(Exception ex)
        {
            return null;
        }
    }
    
    @Override
    public Connection getConnection() throws SQLException 
    {
        Connection conn = super.getConnection();
        try 
        {
            return OracleUtils.setClientIdToConnection(conn, APLICACION,  getIdUsuario());
        } 
        catch (Throwable t) 
        {
            log.warn( "No se pudo establecer los datos de identificacion en la conexion.", t);
        }
        return conn;
    }

    @Override
    public void closeConnection(Connection conn) throws SQLException 
    {
        try 
        {
            OracleUtils.removeClientIdToConnection( conn );   
        } 
        catch (Throwable t) 
        {
            log.warn( "No se pudo borrar los datos de identificacion en la conexion.", t);
        }
        super.closeConnection( conn );
    }
}
