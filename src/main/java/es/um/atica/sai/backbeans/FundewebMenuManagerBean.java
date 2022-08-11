package es.um.atica.sai.backbeans;

import static org.jboss.seam.annotations.Install.APPLICATION;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;

import es.um.atica.seam.components.MenuManagerBean;

@Name("es.um.atica.fundeweb.menuManagerBean")
@Scope(ScopeType.SESSION)
@Install(precedence = APPLICATION)
@BypassInterceptors
public class FundewebMenuManagerBean extends MenuManagerBean {

	private static final Log LOG = Logging
			.getLog(FundewebMenuManagerBean.class);

	private boolean collpased = false;
	private boolean horizontal = true;


	
	public boolean isCollpased() {
		return collpased;
	}

	public void setCollpased(boolean collpased) {
		this.collpased = collpased;
	}
	
    public boolean isHorizontal() {
        return horizontal;
    }
    
    public void setHorizontal( boolean horizontal ) {
        this.horizontal = horizontal;
    }

    public void toggle() {
	    this.collpased = !isCollpased();
	    this.getLog().debug("toggle menu: #0", isCollpased());
	}

    public void orientationHorizontal() {
        this.horizontal = true;
        this.getLog().debug("menu horizontal: #0", isHorizontal());
    }
    
    public void orientationVertical() {
        this.horizontal = false;
        this.getLog().debug("menu horizontal: #0", isHorizontal());
    }
    
	@Override
	protected Log getLog() {
		return LOG;
	}
}