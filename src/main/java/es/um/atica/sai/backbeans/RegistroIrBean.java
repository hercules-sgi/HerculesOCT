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
import org.umu.atica.servicios.gesper.gente.entity.Departamento;

import es.um.atica.sai.dtos.EntidadRespuesta;
import es.um.atica.sai.dtos.Subtercero;
import es.um.atica.sai.entities.EntidadPagadora;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.exceptions.SaiException;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.MensajeService;
import es.um.atica.sai.services.interfaces.TarifaService;
import es.um.atica.sai.services.interfaces.UsuarioService;

@Name("registroIr")
@Scope(ScopeType.CONVERSATION)
public class RegistroIrBean {
    
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
    private EntidadPagadora entidadEdit;
    private String tipoEntidad;
    private List<Departamento> listaDepartamentos;
    private List<Subtercero> listaSubterceros;
    
	public Servicio getServicio() {
		return servicio;
	}
	
	public void setServicio( Servicio servicio ) {
		this.servicio = servicio;
	}

	public EntidadPagadora getEntidadEdit() 
    {
        return entidadEdit;
    }
    
    public void setEntidadEdit( EntidadPagadora entidad ) 
    {
        this.entidadEdit = entidad;
    }
    
	public String getTipoEntidad() {
		return tipoEntidad;
	}
	
	public void setTipoEntidad( String tipoEntidad ) {
		this.tipoEntidad = tipoEntidad;
	}

	public List<Departamento> getListaDepartamentos() {
		return listaDepartamentos;
	}
	
	public void setListaDepartamentos( List<Departamento> listaDepartamentos ) {
		this.listaDepartamentos = listaDepartamentos;
	}
    
	public List<Subtercero> getListaSubterceros() {
		return listaSubterceros;
	}

	public void setListaSubterceros( List<Subtercero> listaSubterceros ) {
		this.listaSubterceros = listaSubterceros;
	}

	public String establecerServicioRegistroIr(Servicio servicio)
    {
    	this.entidadEdit = new EntidadPagadora();
    	this.servicio = servicio;
    	if (identity.isTrabajadorUmu())
    	{
    		this.tipoEntidad = null;
    		this.listaDepartamentos = tarifaService.getListaDepartamentos();
    	}
    	else
    	{
    		this.tipoEntidad = "TERCERO";
    	}
    	this.listaSubterceros = null;
    	return "registroIr";
    }
    
    public void seleccionadoTipoEntidad()
    {
   		this.entidadEdit = new EntidadPagadora();
   		this.listaSubterceros = null;
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
    	try 
    	{
    		usuarioService.solicitudRegistroIr(identity.getUsuarioConectado(), this.entidadEdit);
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
    						   null, null,  
    						   identity.getUsuarioConectado().getDatosUsuario().getNombreCompleto()); 
    	// Enviamos notificaciones mediante email
    	EntidadRespuesta er = mensajeService.registroIr(identity.getUsuarioConectado(), servicio.getEmail());
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
    	return "home";
    }
    
    public String volver() {
        return "home";
    }
    
   

	
	
}
