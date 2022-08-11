package es.um.atica.sai.backbeans;

import java.util.List;

import javax.faces.context.FacesContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.jboss.seam.log.Log;
import org.primefaces.component.datatable.DataTable;

import es.um.atica.sai.backbeans.lazydatamodel.UsuarioLazyDataModel;
import es.um.atica.sai.entities.Perfil;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.UsuarioService;

@Name("usuarios")
@Scope(ScopeType.CONVERSATION)
public class UsuariosBean {

    @Logger
    private Log log;
    
    @In(create=true) 
    private FacesMessages facesMessages;
    
    @In( value = UsuarioService.NAME, create = true )
    private UsuarioService usuarioService;

    @In(value="org.jboss.seam.security.identity")
    SaiIdentity identity;
    
    private String dniBuscar;
    private String nombreBuscar;
    private String emailBuscar;
    private String estadoBuscar;
    private String tagPerfilBuscar;
    private UsuarioLazyDataModel usuariosLdm;
	
    private int first;
    private int pageSize;
    
	public String getDniBuscar() {
		return dniBuscar;
	}
	
	public void setDniBuscar( String dniBuscar ) {
		this.dniBuscar = dniBuscar;
	}
	
	public String getNombreBuscar() {
		return nombreBuscar;
	}
	
	public void setNombreBuscar( String nombreBuscar ) {
		this.nombreBuscar = nombreBuscar;
	}

	public String getEmailBuscar() {
		return emailBuscar;
	}
	
	public void setEmailBuscar( String emailBuscar ) {
		this.emailBuscar = emailBuscar;
	}
	
	public String getEstadoBuscar() {
		return estadoBuscar;
	}
	
	public void setEstadoBuscar( String estadoBuscar ) {
		this.estadoBuscar = estadoBuscar;
	}

	public String getTagPerfilBuscar() {
		return tagPerfilBuscar;
	}

	public void setTagPerfilBuscar( String tagPerfilBuscar ) {
		this.tagPerfilBuscar = tagPerfilBuscar;
	}

	public UsuarioLazyDataModel getUsuariosLdm() {
		return usuariosLdm;
	}
	
	public void setUsuariosLdm( UsuarioLazyDataModel usuariosLdm ) {
		this.usuariosLdm = usuariosLdm;
	}
    
	public int getFirst() {
		return first;
	}
	
	public void setFirst( int first ) {
		this.first = first;
	}
	
	public int getPageSize() {
		return pageSize;
	}
	
	public void setPageSize( int pageSize ) {
		this.pageSize = pageSize;
	}

	@Create
    public void inicializa()
    {
    	this.pageSize = 10;
		buscar();
    }
    
    public void buscar()
    {
    	DataTable tablaListaUsuarios = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formListadoUsuarios:listaUsuarios");
    	if (tablaListaUsuarios != null)
    	{
    		tablaListaUsuarios.setFirst(0);
    	}
    	this.usuariosLdm = new UsuarioLazyDataModel();
    }
    
    public void limpiarBusqueda()
    {
    	this.dniBuscar = null;
    	this.nombreBuscar = null;
    	this.emailBuscar = null;
    	this.estadoBuscar = null;
    	this.tagPerfilBuscar = null;
    	this.setFirst(0);
    	buscar();
    }
    
    public List<String> getListaPerfiles(Usuario usuario)
    {
    	return usuarioService.getListaDescripcionesUsuarioPerfilPuedeVerUsuarioConectado( usuario );
    }
    
    public String suplantarUsuario(Usuario usuario)
    {
    	identity.suplantarUsuario(usuario);
		this.facesMessages.add(StatusMessage.Severity.INFO, 
	  			   			  "suplantacion.ok",
	  			   			  null, null,  
	  			   			  identity.getUsuarioConectado().getDatosUsuario().getNombreCompleto());
    	return "home";
    }
    
    public String deshacerSuplantacion()
    {
    	identity.deshacerSuplantacion();
		this.facesMessages.add(StatusMessage.Severity.INFO, 
							   "suplantacion.restaurar.ok",
							   null, null,  
							   identity.getUsuarioConectado().getDatosUsuario().getNombreCompleto());
    	return "home";
    }
    
    public List<Perfil> getListaPerfilesPuedeFiltrar()
    {
    	return usuarioService.getPerfilesPuedeFiltrar();
    }
}
