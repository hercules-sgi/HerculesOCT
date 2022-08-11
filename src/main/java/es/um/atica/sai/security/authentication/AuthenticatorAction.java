package es.um.atica.sai.security.authentication;

import static org.jboss.seam.annotations.Install.FRAMEWORK;

import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;

import es.um.atica.seam.security.authentication.AbstractAuthenticationManagerBean;
import es.um.atica.seam.security.authentication.AbstractAuthenticatorAction;

@Name("authenticator")
@Install(precedence = FRAMEWORK)
@BypassInterceptors
public class AuthenticatorAction extends AbstractAuthenticatorAction {

	@Override
    protected boolean hasApplicationSpecificPermission() {
        return super.hasApplicationSpecificPermission();
    }

    private static final Log LOG = Logging.getLog(AuthenticatorAction.class);
	
	@Override
	protected AbstractAuthenticationManagerBean getAuthenticationManagerBean() {
		return AuthenticationManagerBean.instance();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see es.um.atica.util.FundeWebManagerBean#getLog()
	 */
	@Override
	protected Log getLog() {
		return LOG;
	}

}
