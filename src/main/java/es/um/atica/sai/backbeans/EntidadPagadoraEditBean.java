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

import es.um.atica.sai.dtos.Subtercero;
import es.um.atica.sai.entities.EntidadPagadora;
import es.um.atica.sai.entities.TipoTarifa;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.TarifaService;

@Name("entidadPagadoraEdit")
@Scope(ScopeType.CONVERSATION)
public class EntidadPagadoraEditBean {
    
    @Logger
    private Log log;
    
    @In(value="org.jboss.seam.security.identity", required=true)
    SaiIdentity identity;
    
    @In(create = true, required = true) 
    TarifaService tarifaService;
    
    @In(create = true) 
    EntidadesPagadorasBean entidadesPagadoras;
    
    @In(create = true) 
    UsuarioIrEditBean usuarioIrEdit;
    
    @In(create=true) 
    protected FacesMessages facesMessages;
    
    private EntidadPagadora entidadEdit;
    private String tipoEntidad;
    private List<TipoTarifa> listaTiposTarifa;
    private List<Departamento> listaDepartamentos;
    private Usuario usuarioRetorno;
    
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

	public List<TipoTarifa> getListaTiposTarifa() {
		return listaTiposTarifa;
	}
	
	public void setListaTiposTarifa( List<TipoTarifa> listaTiposTarifa ) {
		this.listaTiposTarifa = listaTiposTarifa;
	}

	public List<Departamento> getListaDepartamentos() {
		return listaDepartamentos;
	}
	
	public void setListaDepartamentos( List<Departamento> listaDepartamentos ) {
		this.listaDepartamentos = listaDepartamentos;
	}
    
	public Usuario getUsuarioRetorno() {
		return usuarioRetorno;
	}

	public void setUsuarioRetorno( Usuario usuarioRetorno ) {
		this.usuarioRetorno = usuarioRetorno;
	}

	public String establecerEntidadCreate()
    {
    	this.entidadEdit = new EntidadPagadora();
    	this.usuarioRetorno = null;
    	this.tipoEntidad = null;
    	this.listaDepartamentos = tarifaService.getListaDepartamentos();
    	this.listaTiposTarifa = tarifaService.getTiposTarifas();
    	return "entidadPagadoraEdit";
    }
    
    public String establecerEntidadEdit(EntidadPagadora entidad)
    {
    	this.entidadEdit = entidad;
    	if (this.entidadEdit.getCif() != null)
    	{
    		this.tipoEntidad = "TERCERO";
    	}
    	else
    	{
    		this.tipoEntidad = "UDADMIN";
    		this.listaDepartamentos = tarifaService.getListaDepartamentos();
    	}
    	this.listaTiposTarifa = tarifaService.getTiposTarifas();
    	return "entidadPagadoraEdit";
    }
    
    public String establecerEntidadEdit(EntidadPagadora entidad, Usuario usuarioRetorno)
    {
    	this.usuarioRetorno = usuarioRetorno;
    	return this.establecerEntidadEdit(entidad);
    }
    
    public void seleccionadoTipoEntidad()
    {
   		this.entidadEdit = new EntidadPagadora();
    }
    
    public List<Subtercero> getListaSubterceros(String cif)
    {
    	return tarifaService.getSubterceros(cif);
    }
    
    public String guardar() 
    {
        if (this.entidadEdit.getCod() == null) 
        {
            try 
            {
				this.tarifaService.crearEntidadPagadora(this.entidadEdit);
			} 
            catch ( SaiException e ) 
            {
            	this.facesMessages.add(StatusMessage.Severity.ERROR, 
            						   "entidadpagadora.guardar.error",
            						   null, null,  
            						   e.getMessage()); 
            	return null;
            }
			this.facesMessages.add(StatusMessage.Severity.INFO, 
		   			  			   "entidadpagadora.guardar.ok",
		   			  			   null, null,  
		   			  			   this.entidadEdit.getNombre()); 
        }
        else 
        {
            try 
            {
				this.tarifaService.modificarEntidadPagadora(this.entidadEdit);
			} 
            catch ( SaiException e )
            {
            	this.facesMessages.add(StatusMessage.Severity.ERROR, 
  		   			  				   "entidadpagadora.modificar.error",
  		   			  				   null, null,  
  		   			  				   e.getMessage()); 
            	return null;
            }
			this.facesMessages.add(StatusMessage.Severity.INFO, 
		   			  			   "entidadpagadora.modificar.ok",
		   			  			   null, null,  
		   			  			   this.entidadEdit.getNombre()); 
        }
        if (this.usuarioRetorno == null)
        {
        	return entidadesPagadoras.volverListado();
        }
        else
        {
        	return usuarioIrEdit.establecerUsuarioIrEdit(this.usuarioRetorno);
        }
        
    }
    
    public String volver() 
    {
    	if (this.usuarioRetorno == null)
        {
        	return "entidadesPagadoras";
        }
        else
        {
        	return "usuarioIrEdit";
        }
    }
    
   

	
	
}
