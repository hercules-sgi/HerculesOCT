package es.um.atica.sai.backbeans;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.jboss.seam.log.Log;

import es.um.atica.sai.dtos.EntidadRespuesta;
import es.um.atica.sai.services.interfaces.UsuarioService;

@Name("cambioPasswordExternoNa")
@Scope(ScopeType.PAGE)
public class CambioPasswordExternoNaBean {

	@Logger
	private Log log;
	
    @In(create=true) 
    private FacesMessages facesMessages;
	
    @In(create=true)
    private UsuarioService usuarioService;
    
    private String email;
    private String passwordViejo;
    private String password1;
    private String password2;
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail( String email ) {
		this.email = email;
	}

	public String getPasswordViejo() {
		return passwordViejo;
	}
	
	public void setPasswordViejo( String passwordViejo ) {
		this.passwordViejo = passwordViejo;
	}

	public String getPassword1() {
		return password1;
	}
	
	public void setPassword1( String password1 ) {
		this.password1 = password1;
	}
	
	public String getPassword2() {
		return password2;
	}
	
	public void setPassword2( String password2 ) {
		this.password2 = password2;
	}

	
	public String restablecerPassword()
	{
		if (!this.password1.equals(this.password2))
		{
			this.facesMessages.add(StatusMessage.Severity.ERROR, "login.recuperacion.error", "login.nuevopass.error.nocoincide", null, null);
			return null;
		}
		EntidadRespuesta er = usuarioService.restablecerPasswordNoAutenticado(this.email, this.passwordViejo, this.password1);
		if (er.getEntidad() == null)
		{
			this.facesMessages.add(StatusMessage.Severity.ERROR, "login.recuperacion.error", null, null, er.getRespuesta());
			return null;
		}
		else
		{
			this.facesMessages.add(StatusMessage.Severity.INFO, null, "login.nuevopass.ok.info", er.getRespuesta(), null);
			return "login";
		}
	}

}
