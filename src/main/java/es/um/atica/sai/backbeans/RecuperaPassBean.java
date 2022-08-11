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
import es.um.atica.sai.services.interfaces.MensajeService;
import es.um.atica.sai.services.interfaces.UsuarioService;

@Name("recuperaPass")
@Scope(ScopeType.PAGE)
public class RecuperaPassBean {

	@Logger
	private Log log;
	
    @In(create=true) 
    private FacesMessages facesMessages;
	
    @In(create=true)
    private UsuarioService usuarioService;
    
    @In(create=true)
    private MensajeService mensajeService;
    
    private String email;
    private String password1;
    private String password2;
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail( String email ) {
		this.email = email;
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
    
	
	public String solicitarNuevoPassword()
	{
		EntidadRespuesta er = usuarioService.generarNuevoPassword(this.email);
		if (er.getEntidad() == null)
		{
    		this.facesMessages.add(StatusMessage.Severity.ERROR, "login.recuperacion.error", null, null, er.getRespuesta());
    		return null;
		}
		// Enviamos contraseña al usuario
    	String password = (String)er.getEntidad();
    	boolean resultadoEnvioEmail = mensajeService.solicitaNuevoPassword( this.email, password );
    	if (!resultadoEnvioEmail)
		{
    		this.facesMessages.add(StatusMessage.Severity.WARN, 
									"login.recuperacion.error",
									"login.recuperacion.error.envioemail",
									null, null);
    		return null;
		}
		else
		{
    		this.facesMessages.add(StatusMessage.Severity.INFO, 
									"login.recuperacion.ok",
									"login.recuperacion.ok.info",
									null, null,
									this.email);
    		return "login";
		}
	}
	

}
