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
import org.jboss.seam.log.Log;
import org.primefaces.component.datatable.DataTable;

import es.um.atica.sai.backbeans.lazydatamodel.UsuarioIrLazyDataModel;
import es.um.atica.sai.entities.EntidadPagadora;
import es.um.atica.sai.entities.EntidadesIr;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.TarifaService;
import es.um.atica.sai.services.interfaces.UsuarioService;

@Name("usuariosIr")
@Scope(ScopeType.CONVERSATION)
public class UsuariosIrBean {

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
    
    private String dniBuscar;
    private String nombreBuscar;
    private String emailBuscar;
    private UsuarioIrLazyDataModel usuariosIrLdm;
	
    
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
	
	public UsuarioIrLazyDataModel getUsuariosIrLdm() {
		return usuariosIrLdm;
	}
	
	public void setUsuariosIrLdm( UsuarioIrLazyDataModel usuariosIrLdm ) {
		this.usuariosIrLdm = usuariosIrLdm;
	}

	@Create
    public void inicializa()
    {
    	buscar();
    }
    
    public void buscar()
    {
    	DataTable tablaListaUsuarios = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formListadoUsuarios:listaUsuarios");
    	if (tablaListaUsuarios != null)
    	{
    		tablaListaUsuarios.setFirst(0);
    	}
    	this.usuariosIrLdm = new UsuarioIrLazyDataModel();
    }
    
    public void limpiarBusqueda()
    {
    	this.dniBuscar = null;
    	this.nombreBuscar = null;
    	this.emailBuscar = null;
    	buscar();
    }
    
    public List<EntidadesIr> getEntidadesUsuario(Usuario usuario)
    {
    	return usuarioService.getEntidadesIrByUsuario(usuario);
    }
    
    public String getDescripcionEntidad(EntidadPagadora ep)
    {
    	return tarifaService.getDescripcionEntidadPagadora( ep );
    }
    
 }
