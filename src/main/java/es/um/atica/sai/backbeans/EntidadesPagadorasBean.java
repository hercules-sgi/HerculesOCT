package es.um.atica.sai.backbeans;

import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.jboss.seam.log.Log;
import org.umu.atica.servicios.gesper.gente.entity.Departamento;

import es.um.atica.sai.entities.EntidadPagadora;
import es.um.atica.sai.exceptions.SaiException;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.TarifaService;

@Name("entidadesPagadoras")
@Scope(ScopeType.CONVERSATION)
public class EntidadesPagadorasBean {
    
    @Logger
    private Log log;
    
    @In(value="org.jboss.seam.security.identity", required=true)
    SaiIdentity identity;
    
    @In(create = true, required = true)
    TarifaService tarifaService;
    
    @In(create=true) 
    protected FacesMessages facesMessages;
    
    private String cif;
    private String codDepartamento;
    private String localidad;
    private String nombre;
    private String email;
    private String telefono;
    private String estado;
    private List<Departamento> listaDepartamentos;
    private List<EntidadPagadora> entidades;
   
    
    @Create
    public void inicializar() 
    {
        
    	buscar();
    }
    
    public void reset() {
        setCif(null);
        setCodDepartamento(null);
        setLocalidad(null);
        setNombre(null);
        setEmail(null);
        setTelefono(null);
        buscar();
    }
    
    public void buscar() 
    {
    	this.listaDepartamentos = tarifaService.getListaDepartamentos();
    	this.entidades = tarifaService.buscarEntidadesPagadoras();
    }
    
       
    public void eliminarEntidadPagadora(EntidadPagadora entidad)
    {
    	try 
    	{
			tarifaService.eliminarEntidadPagadora( entidad );
        	this.facesMessages.add(StatusMessage.Severity.INFO, 
		  			   			  "entidadpagadora.eliminar.ok",
		  			   			  null, null,  
		  			   			  entidad.getNombre()); 
        	buscar();
		}
    	catch ( SaiException e ) 
    	{
        	this.facesMessages.add(StatusMessage.Severity.ERROR, 
		   			  			   "entidadpagadora.eliminar.error",
		   			  			   null, null,  
		   			  			   e.getMessage()); 
		}
    }
    
    public String volverListado()
    {
    	buscar();
    	return "entidadesPagadoras";
    }
    
    public String getEstiloFilaEntidad(EntidadPagadora entidad)
    {
    	if (entidad.getEstado().equals("S"))
    	{
    		return "colorFilaRoja";
    	}
    	return null;
    }
    
    //GETTERS & SETTERS    
    public String getCif() {
        return cif;
    }
    
    public void setCif( String cif ) {
        this.cif = cif;
    }
    
	public String getCodDepartamento() {
		return codDepartamento;
	}
	
	public void setCodDepartamento( String codDepartamento ) {
		this.codDepartamento = codDepartamento;
	}

	public String getLocalidad() {
        return localidad;
    }
    
    public void setLocalidad( String localidad ) {
        this.localidad = localidad;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre( String nombre ) {
        this.nombre = nombre;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail( String email ) {
        this.email = email;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono( String telefono ) {
        this.telefono = telefono;
    }
    
	public List<Departamento> getListaDepartamentos() {
		return listaDepartamentos;
	}
	
	public void setListaDepartamentos( List<Departamento> listaDepartamentos ) {
		this.listaDepartamentos = listaDepartamentos;
	}

	public List<EntidadPagadora> getEntidades() {
        return entidades;
    }
    
    public void setEntidades( List<EntidadPagadora> entidades ) {
        this.entidades = entidades;
    }
	
	public String getEstado() {
		return estado;
	}
	
	public void setEstado( String estado ) {
		this.estado = estado;
	}
    
	public String getNombreSubtercero(EntidadPagadora ep)
	{
		return tarifaService.getNombreSubtercero(ep.getCif(), ep.getCodSubtercero());
	}
}
