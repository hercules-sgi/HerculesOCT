package es.um.atica.sai.supplant.backbeans;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;
import org.umu.atica.servicios.gesper.gente.ServicioGenteUmu;
import org.umu.atica.servicios.gesper.gente.entity.Persona;

import es.um.atica.commons.utils.UtilString;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.seam.components.EnvironmentManagerBean;
import es.um.atica.seam.components.FundeWebManagerBean;
import es.um.atica.seam.security.UmuIdentity;

/**
 * Bean de respaldo para la gestion de la suplantacion de la identidad.
 * 
 * @author juanmiguel.bernal@ticarum.es
 * @author juan.ferrer@ticarum.es
 * @version 0.0.1
 */
@Name( "suplantarIdentidadBean" )
@Scope( ScopeType.PAGE )
public class SuplantarIdentidadBean extends FundeWebManagerBean implements java.io.Serializable {

    private static final long serialVersionUID = 5735369064638025346L;

    private static final Log LOG = Logging.getLog( SuplantarIdentidadBean.class );

    @In
    private UmuIdentity identity;

    @In( value = "es.um.atica.fundeweb.servicioGenteUmu", create = true )
    private ServicioGenteUmu servicioGenteUmu;

    private String identificador = null;
    private String correo = null;
    private Persona persona = null;

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador( String identificador ) {
        this.identificador = identificador;
    }

    public String getCorreo() {
        return this.correo;
    }

    public void setCorreo( String correo ) {
        if ( !UtilString.esCadenaVacia( correo ) && !correo.contains( "@" ) ) {
            correo += "@um.es";
        }
        this.correo = correo;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona( Persona persona ) {
        this.persona = persona;
    }

    public void cargarIdentidad() {
        this.getLog().debug( "Cargando identidad #0.", this.identificador );
        if ( !UtilString.esCadenaVacia( identificador ) ) {
            try {
                this.persona = null;
                this.correo = null;
                this.persona = servicioGenteUmu.getPersona( this.identificador );                
                this.correo = this.persona.getCorreo();
            }
            catch ( Exception ex ) {
                this.getLog().error( "Error al cargar la identidad de la persona.", ex );
                this.addFacesMessageFromResourceBundle( Severity.WARN, "persona.not.found", this.identificador );
            }
        }
    }

    public void cargarIdentidadByCorreo() {
        this.getLog().debug( "Cargando identidad. Correo [#0].", this.correo );
        if ( !UtilString.esCadenaVacia( this.correo ) ) {
            try {
                this.persona = null;
                this.identificador = null;                
                this.persona = servicioGenteUmu.getPersonaByUsuario( EnvironmentManagerBean.instance().getApplicationName(),
                                                                     this.correo );
                                
                this.identificador = this.persona.getIdentificador();
            }
            catch ( Exception ex ) {
                this.getLog().error( "Error al cargar la identidad de la persona a traves del correo [#0].", ex, this.correo );
                this.addFacesMessageFromResourceBundle( Severity.WARN, "persona.not.found", this.correo );
            }
        }
    }

    public String suplantar() {
        try {
            this.getLog().info( "Suplantando identidad del usuario #0.", this.correo );
            this.persona.setCorreo( this.correo );
            // guardo la identidad del suplantador
            identity.supplantUser( this.persona );
            this.addFacesMessageFromResourceBundle( Severity.INFO, "supplant.successful", this.correo );
            return "home";
        }
        catch ( Exception ex ) {
            this.getLog().error( "Error al suplantar la identidad del usuario.", ex );
            this.addFacesMessageFromResourceBundle( Severity.ERROR, "supplant.failed", this.correo );
        }
        return null;
    }

    @Observer( UmuIdentity.EVENT_SUPPLANTATION_FAILED )
    public String restaurar() {
        try {
            this.getLog().info( "Restaurando identidad del usuario #0", identity.getSupplanter().getCorreo() );
            // restauro la identidad
            ((SaiIdentity)identity).setSuplantado( false );
            identity.restoreSupplanterUser();
            this.addFacesMessageFromResourceBundle( Severity.INFO, "supplant.restore.successful", identity.getPersona()
                                                                                                          .getCorreo() );
            return "home";
        }
        catch ( Exception ex ) {
            this.getLog().error( "Error al restaurar la identidad.", ex );
            this.addFacesMessageFromResourceBundle( Severity.ERROR, "supplant.restore.failed", identity.getPersona()
                                                                                                       .getCorreo() );
        }
        return null;
    }

    @Override
    protected Log getLog() {
        return LOG;
    }

}
