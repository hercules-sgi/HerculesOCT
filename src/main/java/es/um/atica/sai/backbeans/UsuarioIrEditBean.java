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

import es.um.atica.sai.entities.EntidadPagadora;
import es.um.atica.sai.entities.EntidadesIr;
import es.um.atica.sai.entities.EntidadesIrId;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.TarifaService;
import es.um.atica.sai.services.interfaces.UsuarioService;

@Name("usuarioIrEdit")
@Scope(ScopeType.CONVERSATION)
public class UsuarioIrEditBean {

    @Logger
    private Log log;
    
    @In(create=true) 
    private FacesMessages facesMessages;
    
    @In( value = UsuarioService.NAME, create = true )
    private UsuarioService usuarioService;
    
    @In( value = TarifaService.NAME, create = true )
    private TarifaService tarifaService;
    
    @In(value="org.jboss.seam.security.identity")
    SaiIdentity identity;
    
    private Usuario usuarioIrEdit;
    private EntidadesIr entidadIrAdd;
    
	public Usuario getUsuarioIrEdit() {
		return usuarioIrEdit;
	}
	
	public void setUsuarioIrEdit( Usuario usuarioIrEdit ) {
		this.usuarioIrEdit = usuarioIrEdit;
	}

	public EntidadesIr getEntidadIrAdd() {
		return entidadIrAdd;
	}
	
	public void setEntidadIrAdd( EntidadesIr entidadIrAdd ) {
		this.entidadIrAdd = entidadIrAdd;
	}


	public String establecerUsuarioIrEdit(Usuario usuario)
	{
		this.usuarioIrEdit = usuario;
		this.entidadIrAdd = new EntidadesIr();
		if (this.usuarioIrEdit.getEntidadPagadoraSolicita() != null && this.usuarioIrEdit.getEntidadPagadoraSolicita().getEstado().equals("V"))
		{
			this.entidadIrAdd.setEntidadPagadora(this.usuarioIrEdit.getEntidadPagadoraSolicita());
		}
		return "usuarioIrEdit";
	}
    
    public String volver()
    {
    	return "usuariosIr";
    }
    
    public List<EntidadPagadora> getListaEntidadesPagadoras() 
    {
        return tarifaService.getListaEntidadesPagadorasActivas();
    }
    
    public List<EntidadesIr> getEntidadesUsuario()
    {
    	return usuarioService.getEntidadesIrByUsuario(this.usuarioIrEdit);
    }
    
    public void addEntidad()
    {
    	EntidadesIrId entidadIrId = new EntidadesIrId();
    	entidadIrId.setCodEntidad(this.entidadIrAdd.getEntidadPagadora().getCod());
    	entidadIrId.setCodIr(this.usuarioIrEdit.getCod());
    	this.entidadIrAdd.setUsuario(this.usuarioIrEdit);
    	this.entidadIrAdd.setId(entidadIrId);
    	try
    	{
    		usuarioService.crearEntidadesIr(this.entidadIrAdd);
    		this.facesMessages.add(StatusMessage.Severity.INFO, 
					   			   "usuario.add.entidad.ok",
					   			   null, null,
					   			   this.entidadIrAdd.getEntidadPagadora().getNombre());
    		this.entidadIrAdd = new EntidadesIr();
    	}
    	catch (SaiException e)
    	{
    		this.entidadIrAdd = new EntidadesIr();
    		this.facesMessages.add(StatusMessage.Severity.ERROR, 
		   			  			   "usuario.add.entidad.error",
		   			  			   null, null,
		   			  			   e.getMessage());
    	}
    }
    
    public void eliminarEntidad(EntidadesIr eir)
    {
    	try 
    	{
			String descripcionEntidad = eir.getEntidadPagadora().getNombre();
    		usuarioService.eliminarEntidadesIr( eir );
    		this.facesMessages.add(StatusMessage.Severity.INFO, 
		   			   			   "usuario.del.entidad.ok",
		   			   			   null, null,
		   			   			   descripcionEntidad);

		} 
    	catch ( SaiException e ) 
    	{
    		this.facesMessages.add(StatusMessage.Severity.ERROR, 
			  			  		  "usuario.del.entidad.error",
			  			  		  null, null,
			  			  		  e.getMessage());
		}	
    }
    
    public String getDescripcionEntidad(EntidadPagadora ep)
    {
    	return tarifaService.getDescripcionEntidadPagadora( ep );
    }
    
	public String getNombreSubtercero(EntidadPagadora ep)
	{
		return tarifaService.getNombreSubtercero(ep.getCif(), ep.getCodSubtercero());
	}
    
}
