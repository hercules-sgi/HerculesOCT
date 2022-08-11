package es.um.atica.sai.security.authentication.method;

import javax.persistence.EntityManager;

import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;

import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.utils.Utilidades;
import es.um.atica.seam.security.authentication.method.AuthenticationMethod;

public class AuthenticationMethodNoUmu extends AuthenticationMethod {

    private static final long serialVersionUID = -6390333411305030992L;
    
    private static final Log LOG = Logging.getLog( AuthenticationMethodNoUmu.class );

    public boolean authenticate() 
    {
        String email = getCredentials().getUsername();
        LOG.info( "Autenticando a usuario externo: #0", email );
        try 
        {
            Usuario usuario = (Usuario)getEntityManager().createNamedQuery(Usuario.GET_USUARIO_X_LOGIN).setParameter( "mail", email ).getSingleResult();
    
            // Si es un externo, comprobamos la contraseña
            if (!email.contains( "@um.es" ) && !email.contains("@ticarum.es")) 
            {
                String passwordCifrado = Utilidades.getStringCifrado(getCredentials().getPassword());
            	if (passwordCifrado.equals(usuario.getContrasena()))
            	{
            		return true;
            	}
            }
            return false;
        } catch (Exception ex) {
            LOG.error( "Error al autenticar al usuario NO_UMU: #0", ex, email );
            return false;
        }
    }
    
    private EntityManager getEntityManager() {
        return (EntityManager) getComponentInstance("entityManager");
    }

    @Override
    protected Log getLog() {
    
        return LOG;
    }
    
}
