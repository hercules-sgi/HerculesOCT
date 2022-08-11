package es.um.atica.sai.backbeans;

import java.util.Date;
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

import es.um.atica.commons.utils.UtilDate;
import es.um.atica.sai.dtos.EntidadRespuesta;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.MensajeService;
import es.um.atica.sai.services.interfaces.UsuarioService;


@Name("usuariosPendientes")
@Scope(ScopeType.PAGE)
public class UsuariosPendientesBean {

    @Logger
    private Log log;
    
    @In(value="org.jboss.seam.security.identity", required=true)
    SaiIdentity identity;
    
    @In(create = true, required = true)
    UsuarioService usuarioService;
    
    @In(create = true, required = true)
    MensajeService mensajeService;
    
    @In(create=true) 
    protected FacesMessages facesMessages;

    private String dniFiltrar;
    private String nombreFiltrar;
    private String emailFiltrar;
    private Date fechaCaducaDesdeFiltrar;
    private String fechaCaducaDesdeFiltrarString;
    private Date fechaCaducaHastaFiltrar;
    private String fechaCaducaHastaFiltrarString;
    private List<Usuario> listaUsuariosPendientes;
    private Usuario usuarioEdit;
    private Date minCaducidad;
    private Date maxCaducidad;

	public String getDniFiltrar() {
		return dniFiltrar;
	}
	
	public void setDniFiltrar( String dniFiltrar ) {
		this.dniFiltrar = dniFiltrar;
	}
	
	public String getNombreFiltrar() {
		return nombreFiltrar;
	}
	
	public void setNombreFiltrar( String nombreFiltrar ) {
		this.nombreFiltrar = nombreFiltrar;
	}
	
	public String getEmailFiltrar() {
		return emailFiltrar;
	}
	
	public void setEmailFiltrar( String emailFiltrar ) {
		this.emailFiltrar = emailFiltrar;
	}
	
	public Date getFechaCaducaDesdeFiltrar() {
		return fechaCaducaDesdeFiltrar;
	}
	
	public void setFechaCaducaDesdeFiltrar( Date fechaCaducaDesdeFiltrar ) {
		this.fechaCaducaDesdeFiltrar = fechaCaducaDesdeFiltrar;
		this.fechaCaducaDesdeFiltrarString = UtilDate.convertirDateFechaToString(this.fechaCaducaDesdeFiltrar);
	}
	
	public String getFechaCaducaDesdeFiltrarString() {
		return fechaCaducaDesdeFiltrarString;
	}
	
	public void setFechaCaducaDesdeFiltrarString( String fechaCaducaDesdeFiltrarString ) {
		this.fechaCaducaDesdeFiltrarString = fechaCaducaDesdeFiltrarString;
	}

	public Date getFechaCaducaHastaFiltrar() {
		return fechaCaducaHastaFiltrar;
	}
	
	public void setFechaCaducaHastaFiltrar( Date fechaCaducaHastaFiltrar ) {
		this.fechaCaducaHastaFiltrar = fechaCaducaHastaFiltrar;
		this.fechaCaducaHastaFiltrarString = UtilDate.convertirDateFechaToString(this.fechaCaducaHastaFiltrar);
	}

	
	public String getFechaCaducaHastaFiltrarString() {
		return fechaCaducaHastaFiltrarString;
	}

	
	public void setFechaCaducaHastaFiltrarString( String fechaCaducaHastaFiltrarString ) {
		this.fechaCaducaHastaFiltrarString = fechaCaducaHastaFiltrarString;
	}

	public List<Usuario> getListaUsuariosPendientes() {
		return listaUsuariosPendientes;
	}
	
	public void setListaUsuariosPendientes( List<Usuario> listaUsuariosPendientes ) {
		this.listaUsuariosPendientes = listaUsuariosPendientes;
	}

	public Usuario getUsuarioEdit() {
		return usuarioEdit;
	}
	
	public void setUsuarioEdit( Usuario usuarioEdit ) {
		this.usuarioEdit = usuarioEdit;
	}

	public Date getMinCaducidad() {
		return minCaducidad;
	}
	
	public void setMinCaducidad( Date minCaducidad ) {
		this.minCaducidad = minCaducidad;
	}
	
	public Date getMaxCaducidad() {
		return maxCaducidad;
	}
	
	public void setMaxCaducidad( Date maxCaducidad ) {
		this.maxCaducidad = maxCaducidad;
	}

	@Create
    public void inicializar() 
    {
        this.setFechaCaducaDesdeFiltrar(UtilDate.sumarDias(new Date(), -15));
        this.setFechaCaducaHastaFiltrar(UtilDate.sumarDias(new Date(), 15) );
		this.listaUsuariosPendientes=usuarioService.getUsuariosPendientes();
    }

	public void filtrar()
	{
		this.listaUsuariosPendientes=usuarioService.getUsuariosPendientes();
	}
	
	public void limpiar()
	{
        this.dniFiltrar = null;
        this.emailFiltrar = null;
        this.nombreFiltrar = null;
		inicializar();
	}
	
	public void establecerUsuarioEdit(Usuario usuario)
	{
		this.usuarioEdit = usuario;
		// Con esta caducidad mínima le pongan la caducidad que le pongan desaparecerá de la lista al validar
		this.minCaducidad = UtilDate.sumarDias(new Date(), 16);
		this.maxCaducidad = UtilDate.sumarAños(new Date(), 1);
		this.usuarioEdit.setCaduca((Date)this.maxCaducidad.clone());
	}
	
	public void validarUsuario()
	{
		this.usuarioEdit.setEstado("ALTA");
    	try 
    	{
			usuarioService.guardarUsuario(this.usuarioEdit);
    		this.facesMessages.add(StatusMessage.Severity.INFO, 
    							   "usuariospendientes.validar.ok",
    							   null, null,
    							   this.usuarioEdit.getDatosUsuario().getNombreCompleto());
    		this.filtrar();
		} 
    	catch ( SaiException e ) 
    	{
    		this.facesMessages.add(StatusMessage.Severity.ERROR, 
					   			  "usuariospendientes.validar.error",
					   			   null, null,
					   			   e.getMessage());
    		return;
		}
    	if (!usuarioService.esTrabajadorUmu(this.usuarioEdit.getDatosUsuario().getDni()))
    	{
	    	// Sólo enviamos mensaje de renovación a los externos
    		EntidadRespuesta er = mensajeService.renovacionUsuario(this.usuarioEdit);
			if ((boolean)er.getEntidad())
			{
				this.facesMessages.add(StatusMessage.Severity.INFO, 
						   			   "usuario.guardar.ok.notificar.ok",
						   			   null, null,
						   			   er.getRespuesta());
			}
			else
			{
				this.facesMessages.add(StatusMessage.Severity.WARN, 
			   			   			   "usuario.guardar.ok.notificar.error",
			   			   			   null, null,
			   			   			   er.getRespuesta());
			}
    	}
	}

    
}
