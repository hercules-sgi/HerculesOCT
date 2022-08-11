package es.um.atica.sai.security.authentication;

import static org.jboss.seam.ScopeType.SESSION;
import static org.jboss.seam.annotations.Install.FRAMEWORK;

import java.sql.SQLException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.model.SelectItem;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.core.SeamResourceBundle;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.umu.atica.servicios.pci.sestertium.ServicioSestertiumUmu;

import buscador.servicios.BuscadorServiciosAtica;
import buscador.servicios.exceptions.ServiceNotFoundException;
import es.um.atica.sai.security.authentication.factories.AuthenticationFactoryNoUmu;
import es.um.atica.seam.security.CredentialsAdapter;
import es.um.atica.seam.security.UmuIdentity;
import es.um.atica.seam.security.authentication.AbstractAuthenticationManagerBean;
import es.um.atica.seam.security.authentication.credentials.CredentialsUmu;
import es.um.atica.seam.security.authentication.factories.AuthenticationFactory;
import es.um.atica.seam.security.authentication.factories.AuthenticationFactoryCard;
import es.um.atica.seam.security.authentication.factories.AuthenticationFactoryCertificateSSL;
import es.um.atica.seam.security.authentication.factories.AuthenticationFactoryNif;
import es.um.atica.seam.security.authentication.factories.AuthenticationFactoryRadius;
import es.um.atica.seam.security.authentication.factories.AuthenticationFactorySSO;
import es.um.atica.seam.security.authentication.method.AuthenticationMethod;
import es.um.atica.seam.security.authentication.method.SSOAuthenticationMethods;

@Name("authenticationManagerBean")
@Scope(SESSION)
@Install(precedence = FRAMEWORK)
@BypassInterceptors
@Startup
public class AuthenticationManagerBean extends AbstractAuthenticationManagerBean {

	private static final long serialVersionUID = -6064182119922723132L;

	/** Logger de la clase */
	private static final Log LOG = Logging.getLog(AuthenticationManagerBean.class);

	protected SelectItem[] selectItemsAutentication;

	/** Credencial actual */
	protected CredentialsAdapter credentialsAdapter;

	// Declaracion de los metodos de autenticacion validos por SSO
    private static final SSOAuthenticationMethods[] VALIDS_SSO_AUTHENTICATION_METHODS = {
        SSOAuthenticationMethods.SSO_CORREO
    };
	
	// Por defecto RADIUS
	protected final AuthenticationType defaultAuthenticationType = AuthenticationType.NO_UMU;
	protected final AuthenticationFactory defaultAuthenticationFactory = new AuthenticationFactoryNoUmu();
	protected final String defaultAuthenticationTypeLabel = SeamResourceBundle.getBundle().getString("label.tipo_acceso_correo");

	protected AuthenticationMethod authenticationMethod;
	
	protected AuthenticationType authenticationType;
	
	public enum AuthenticationType {
		RADIUS, CARD, CERTIFICATE, NIF, SSO, NO_UMU
	}
	
	// Forma de hacer accesible los metodos de autenticacion validos por SSO
    @Factory( SSOAuthenticationMethods.SSO_AUTHENTICATION_METHODS_COMPONENT_NAME )
    public SSOAuthenticationMethods[] getValidsSSOAuthenticationMethods() {
        return VALIDS_SSO_AUTHENTICATION_METHODS;
    }

	public AuthenticationManagerBean() { 
		this.credentialsAdapter = (CredentialsAdapter) this.getCredentials();
		this.activateCredentialsUmu(this.defaultAuthenticationType);

		int idx = 0;
		selectItemsAutentication = new SelectItem[AuthenticationType.values().length];
		for (AuthenticationType type : AuthenticationType.values()) {
			selectItemsAutentication[idx++] = new SelectItem(type.name(), getAuthenticationTypeLabel(type));
		}
	}

	public void activateCredentialsUmu() {
		activateCredentialsUmu( getAuthenticationType() );
	}

	/**
	 * Método para activar una credencial.<br />
	 * Si la que se desea activar, es la que está actualmente, no se hace nada y se devuelve false. En otro caso se
	 * devolverá true.
	 * 
	 * @param credencial
	 *            Clase de Credencial a activar.
	 * @return Si => se creo una nueva credencial. No => ya estaba esa misma credencial activa.
	 */
	protected boolean activateCredentialsUmu(AuthenticationType authenticationType) {
	    this.authenticationMethod = null;
	    AuthenticationType auxAuthenticationType = authenticationType;
	    if (auxAuthenticationType == null) {
	        auxAuthenticationType = this.defaultAuthenticationType;
	    }
	    
		LOG.info("Entrar en activateCredentialsUmu: #0", auxAuthenticationType.name());
		this.setAuthenticationType( auxAuthenticationType );
		this.credentialsAdapter.setCredentialsUmu( getFactoria( auxAuthenticationType ).createCredentials() );
		return true;
	}

	@Override
	public AuthenticationMethod getAuthenticationMethod() {
	    if ( this.authenticationMethod == null ) {
	        this.authenticationMethod = this.getFactoria(this.getAuthenticationType()).createAuthenticationMethod();
	    }
		return this.authenticationMethod;
	}

	protected AuthenticationFactory getFactoria(AuthenticationType authenticationType) {
		if (authenticationType == null) {
			activateCredentialsUmu( this.defaultAuthenticationType );
		}
		switch (authenticationType) {
			case CARD: {
				return new AuthenticationFactoryCard();
			}
			case CERTIFICATE: {
				return new AuthenticationFactoryCertificateSSL();
			}
			case NIF: {
				return new AuthenticationFactoryNif();
			}
			case RADIUS: {
			    return new AuthenticationFactoryRadius();
			}
			case SSO: {
				return new AuthenticationFactorySSO();
			}
			case NO_UMU: {
	            return new AuthenticationFactoryNoUmu();
	        }

			default: {
				return this.defaultAuthenticationFactory;
			}
		}
	}

	protected String getAuthenticationTypeLabel(AuthenticationType authenticationType) {
		ResourceBundle srb = SeamResourceBundle.getBundle();

		try {
			switch (authenticationType) {
				case CARD: {
					return srb.getString("label.tipo_acceso_tarjeta");
				}
				case CERTIFICATE: {
					return srb.getString("label.tipo_acceso_certificado");
				}
				case NIF: {
					return srb.getString("label.tipo_acceso_nif");
				}
				case RADIUS: {
					return srb.getString("label.tipo_acceso_correo");
				}
				case SSO: {
					return srb.getString("label.tipo_acceso_sso");
				}
				default:
				    return this.defaultAuthenticationTypeLabel;
			}
		} catch (MissingResourceException mre) {
			LOG.error("Error al obtener las etiquetas para los tipos de autenticacion.", mre);
		}
		return "";
	}

	/**
	 * Obtiene la credencial actual.
	 */
	public CredentialsUmu getCredentialsUmu() {
		return this.credentialsAdapter.getCredentialsUmu();
	}

	public AuthenticationType getAuthenticationType() {
		return authenticationType;
	}

	public void setAuthenticationType(AuthenticationType authenticationType) {
		LOG.debug("Entra en setAuthenticationType: #0 - #1", authenticationType.hashCode(), authenticationType.name());
		this.authenticationType = authenticationType;
	}
	
	public boolean isNoUmuAuthentication() {
	    return this.getAuthenticationType() == AuthenticationType.NO_UMU;
	}

	public boolean isRadiusAuthentication() {
		return this.getAuthenticationType() == AuthenticationType.RADIUS;
	}

	public boolean isCardAuthentication() {
		return this.getAuthenticationType() == AuthenticationType.CARD;
	}

	public boolean isCertificateAuthentication() {
		return this.getAuthenticationType() == AuthenticationType.CERTIFICATE;
	}

	public boolean isNifAuthentication() {
		return this.getAuthenticationType() == AuthenticationType.NIF;
	}

	public boolean isSsoAuthentication() {
		return this.getAuthenticationType() == AuthenticationType.SSO;
	}

	public SelectItem[] getSelectItemsAutentication() {
		return selectItemsAutentication;
	}

	/* ----------------- AUTENTICACION CON TARJETA --------------------- */
	public String getSestertiumApplet() {
		LOG.debug("Obteniendo el codigo HTML del APPLET Sestertium.");
		try {
			ServicioSestertiumUmu servicioSestertiumUmu = (ServicioSestertiumUmu) BuscadorServiciosAtica
					.obtenerServicio(ServicioSestertiumUmu.ID);
			return servicioSestertiumUmu.getSestertiumApplet();
		} catch (ServiceNotFoundException snfe) {
			LOG.error("Error al buscar el servicio de Radius", snfe);
			this.addFacesMessageFromResourceBundle(Severity.ERROR, "authentication.card.applet.error.message");
			return "";
		} catch (SQLException se) {
			LOG.error("Error al obtener el HTML de Appelt Sestertium:", se);
			this.addFacesMessageFromResourceBundle(Severity.ERROR, "authentication.card.applet.error.message");
			return "";
		} catch (Throwable t) {
			LOG.error("Error al obtener el HTML de Appelt Sestertium:", t);
			this.addFacesMessageFromResourceBundle(Severity.ERROR, "authentication.card.applet.error.message");
			return "";
		}
	}

	@Observer(UmuIdentity.EVENT_AUTHENTICATING_BY_CAS)
	public void activarAuthenticacionSSO() {
		LOG.debug("Entra en activarAuthenticacionSSO");
		this.activateCredentialsUmu(AuthenticationType.SSO);
	}
	
    public void activarAuthenticacionCertificadoSSL() {
        LOG.debug( "Entra en activarAuthenticacionCertificadoSSL" );
        this.activateCredentialsUmu(AuthenticationType.CERTIFICATE);
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.um.atica.util.FundeWebManager#getLog()
	 */
	@Override
	protected Log getLog() {
		return LOG;
	}

	public static AuthenticationManagerBean instance() {
		if (!Contexts.isSessionContextActive()) {
			throw new IllegalStateException("no session context active");
		}
		return (AuthenticationManagerBean) Component.getInstance(AuthenticationManagerBean.class);
	}

}
