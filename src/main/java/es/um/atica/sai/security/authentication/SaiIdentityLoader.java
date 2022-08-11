package es.um.atica.sai.security.authentication;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.services.interfaces.UsuarioService;
import es.um.atica.seam.security.UmuIdentityLoader;

@Name("es.um.atica.security.umuIdentityLoader")
@Install(precedence = Install.APPLICATION)
@BypassInterceptors
public class SaiIdentityLoader extends UmuIdentityLoader {
   
    @Override
    protected void cargarDatosUsuario() 
    {
        String credentials = this.getIdentity().getCredentials().getUsername();
        Usuario usuario = ((UsuarioService)Component.getInstance(UsuarioService.NAME)).autenticacionUsuario(credentials);
        SaiIdentity saiIdentity = (SaiIdentity) this.getIdentity();
        if (usuario == null)
        {
        	saiIdentity.logout();
        	return;
        }
        saiIdentity.cargarRoles( usuario );
      }

    @Override
    protected void cargarRoles() {

    }

}
