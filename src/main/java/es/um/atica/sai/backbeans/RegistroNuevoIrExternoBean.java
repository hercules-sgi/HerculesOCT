package es.um.atica.sai.backbeans;

import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.jboss.seam.log.Log;

import es.um.atica.sai.dtos.EntidadRespuesta;
import es.um.atica.sai.dtos.Subtercero;
import es.um.atica.sai.entities.EntidadPagadora;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.MensajeService;
import es.um.atica.sai.services.interfaces.TarifaService;
import es.um.atica.sai.services.interfaces.UsuarioService;

@Name("registroNuevoIrExterno")
@Scope(ScopeType.CONVERSATION)
public class RegistroNuevoIrExternoBean {
    
    @Logger
    private Log log;
    
    @In(value="org.jboss.seam.security.identity", required=true)
    SaiIdentity identity;
    
    @In(create = true, required = true) 
    TarifaService tarifaService;
    
    @In(create = true, required = true) 
    UsuarioService usuarioService;
    
    @In(create = true, required = true) 
    MensajeService mensajeService;
    
    @In(create=true) 
    protected FacesMessages facesMessages;
    
    private Servicio servicio;
    private Usuario usuarioEdit;
    private String password1;
    private String password2;
    private EntidadPagadora entidadEdit;
    private List<Subtercero> listaSubterceros;
    
	public Servicio getServicio() {
		return servicio;
	}
	
	public void setServicio( Servicio servicio ) {
		this.servicio = servicio;
	}

	public Usuario getUsuarioEdit() {
		return usuarioEdit;
	}

	public void setUsuarioEdit( Usuario usuarioEdit ) {
		this.usuarioEdit = usuarioEdit;
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

	public EntidadPagadora getEntidadEdit() 
    {
        return entidadEdit;
    }
    
    public void setEntidadEdit( EntidadPagadora entidad ) 
    {
        this.entidadEdit = entidad;
    }
    
	public List<Subtercero> getListaSubterceros() {
		return listaSubterceros;
	}
	
	public void setListaSubterceros( List<Subtercero> listaSubterceros ) {
		this.listaSubterceros = listaSubterceros;
	}

	public String establecerServicioRegistroIr(Servicio servicio)
    {
    	this.usuarioEdit = new Usuario();
		this.entidadEdit = new EntidadPagadora();
		this.listaSubterceros = null;
    	this.servicio = servicio;
    	return "registroNuevoIrExterno";
    }
	
	public void blurDni()
	{
		if (this.usuarioEdit.getDni() != null)
		{
			Usuario usuarioAux = usuarioService.findUsuarioByDni(this.usuarioEdit.getDni());
			if (usuarioAux != null)
			{
	    		this.facesMessages.add(StatusMessage.Severity.ERROR, 
						  			   "registroir.externos.usuario.existe",
						  			   "registroir.externos.usuario.existe.detalles",
						  			   null, null,
						  			   this.usuarioEdit.getDni()); 
	    		this.usuarioEdit.setDni(null);
	    		return;
			}
			if (usuarioService.esTrabajadorUmu(this.usuarioEdit.getDni()))
			{
	    		this.facesMessages.add(StatusMessage.Severity.ERROR, 
			  			   			  "registroir.externos.usuario.estrabajadorumu",
			  			   			  "registroir.externos.usuario.estrabajadorumu.detalles",
			  			   			  null, null,
			  			   			  this.usuarioEdit.getDni()); 
	    		this.usuarioEdit.setDni(null);
			}
		}
	}
    
	public void introducidoCif()
	{
		if (this.entidadEdit.getCif() != null && this.entidadEdit.getCif().length() == 9 && this.entidadEdit.getCif().matches("^[A-Z0-9_]*$"))
		{
			this.listaSubterceros=tarifaService.getSubterceros(this.entidadEdit.getCif());
			if (this.listaSubterceros != null && !this.listaSubterceros.isEmpty())
			{
				this.facesMessages.add(StatusMessage.Severity.INFO, 
			  			   			  "registroir.externos.introducidocif.title",
			  			   			  "registroir.externos.introducidocif.details",
			  			   			  null, null); 
			}
		}
		else
		{
			this.listaSubterceros=null;
		}
	}
	
    public String guardar() 
    {
		if (!this.password1.equals(this.password2))
		{
			this.facesMessages.add(StatusMessage.Severity.ERROR, 
								   "registroir.guardar.error", 
								   "login.nuevopass.error.nocoincide", 
								   null, null);
			return null;
		}
    	try 
    	{
    		usuarioService.solicitudRegistroNuevoIrExterno(this.usuarioEdit, this.password1, this.entidadEdit);
    	} 
    	catch ( SaiException e ) 
    	{
    		this.facesMessages.add(StatusMessage.Severity.ERROR, 
    							  "registroir.guardar.error",
    							  null, null,  
    							  e.getMessage()); 
    		return null;
    	}
    	this.facesMessages.add(StatusMessage.Severity.INFO, 
    						   "registroir.guardar.ok",
    						   "registroir.externos.guardar.ok.detalles",
    						   null, null); 
    	// Recargamos usuario para que se cargue la vista de datos
    	usuarioService.recargarUsuario(this.usuarioEdit);
    	// Enviamos notificaciones mediante email
    	EntidadRespuesta er = mensajeService.registroNuevoIrExterno(this.usuarioEdit, password1, this.servicio.getEmail());
		if ((boolean)er.getEntidad())
		{
			this.facesMessages.add(StatusMessage.Severity.INFO, 
					   			   "registroir.notificar.ok",
					   			   null, null,
					   			   er.getRespuesta());
		}
		else
		{
			this.facesMessages.add(StatusMessage.Severity.WARN, 
		   			   			  "registroir.notificar.error",
		   			   			  null, null,
		   			   			  er.getRespuesta());
		}
    	return "login";
    }
    
    public String volver() {
        return "login";
    }
    
   

	
	
}
