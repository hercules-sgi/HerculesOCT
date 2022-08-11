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

import es.um.atica.sai.dtos.TerminalKron;
import es.um.atica.sai.entities.PuertaKron;
import es.um.atica.sai.entities.PuertaKronView;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.ServicioPuertakron;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.services.interfaces.KronService;
import es.um.atica.sai.services.interfaces.ServicioService;
import es.um.atica.sai.services.interfaces.UsuarioService;

@Name("servicioBean")
@Scope(ScopeType.CONVERSATION)
public class ServicioBean {

    private static final String RETORNO_LISTADO_SERVICIOS = "servicioEditOk";

    @Logger
    private Log log;
    
    @In(create = true )
    private ServicioService servicioService;
    
    @In(create = true )
    private KronService kronService;

    @In(create = true )
    private UsuarioService usuarioService;

    @In(create=true) 
    protected FacesMessages facesMessages;
    
    private List<Servicio> listaServicios;
    private List<Usuario> listaSupervisores;
    private List<Usuario> listaTecnicos;
    private List<Usuario> listaIrs;
    private List<Usuario> listaMiembros;
    private Servicio servicioEdit;
	
    private TerminalKron terminalSeleccionado;
	private List<TerminalKron> listaTerminales;
    private List<ServicioPuertakron> puertasServicio;
    
	
	public List<ServicioPuertakron> getPuertasServicio() {
		return puertasServicio;
	}
	
	public void setPuertasServicio( List<ServicioPuertakron> puertasServicio ) {
		this.puertasServicio = puertasServicio;
	}

	public TerminalKron getTerminalSeleccionado() {
		return terminalSeleccionado;
	}
	
	public void setTerminalSeleccionado( TerminalKron terminalSeleccionado ) {
		this.terminalSeleccionado = terminalSeleccionado;
	}
	
	public List<TerminalKron> getListaTerminales() {
		return listaTerminales;
	}
	
	public void setListaTerminales( List<TerminalKron> listaTerminales ) {
		this.listaTerminales = listaTerminales;
	}

	public List<Servicio> getListaServicios() {
		return listaServicios;
	}
	
	public void setListaServicios( List<Servicio> listaServicios ) {
		this.listaServicios = listaServicios;
	}
	
	public List<Usuario> getListaSupervisores() {
		return listaSupervisores;
	}
	
	public void setListaSupervisores( List<Usuario> listaSupervisores ) {
		this.listaSupervisores = listaSupervisores;
	}
	
	public List<Usuario> getListaTecnicos() {
		return listaTecnicos;
	}
	
	public void setListaTecnicos( List<Usuario> listaTecnicos ) {
		this.listaTecnicos = listaTecnicos;
	}

	
	public List<Usuario> getListaIrs() {
		return listaIrs;
	}

	
	public void setListaIrs( List<Usuario> listaIrs ) {
		this.listaIrs = listaIrs;
	}

	
	public List<Usuario> getListaMiembros() {
		return listaMiembros;
	}

	
	public void setListaMiembros( List<Usuario> listaMiembros ) {
		this.listaMiembros = listaMiembros;
	}

	public Servicio getServicioEdit() {
		return servicioEdit;
	}

	public void setServicioEdit( Servicio servicioEdit ) {
		this.servicioEdit = servicioEdit;
	}


	@Create
    public void inicializa()
    {
        this.listaServicios = servicioService.getListaServicios();
    }

	public String establecerServicioCreate() 
	{
		this.servicioEdit = new Servicio();
		return "servicioEdit";
	}

    public String establecerServicioEdit(Servicio serv)
    {
        this.servicioEdit = serv;
        this.listaSupervisores = servicioService.getListaSupervisoresServicio(this.servicioEdit);
        this.listaTecnicos = usuarioService.getTecnicosByServicio(this.servicioEdit);
        this.listaIrs = usuarioService.getIrsByServicio(this.servicioEdit);
        this.listaMiembros = servicioService.getListaMiembrosServicio(this.servicioEdit);
        this.puertasServicio = kronService.getListaServicioPuertakronByService( this.servicioEdit );
        this.listaTerminales = kronService.getListaTerminalesKron();
        return "servicioEdit";
    }
    
	public String guardarServicio() 
	{
		try 
		{
			servicioService.guardarServicio( this.servicioEdit );
		} 
		catch ( Exception ex ) 
		{
    		this.facesMessages.add(StatusMessage.Severity.ERROR, 
		   			  				"servicio.guardar.error",
		   			  				null, null,
		   			  				ex.getCause().getMessage()); 
			return null;
		}
		this.facesMessages.add(StatusMessage.Severity.INFO, 
	  							"servicio.guardar.ok",
	  							null, null,
	  							this.servicioEdit.getNombre()); 
		this.listaServicios = servicioService.getListaServicios();
		return RETORNO_LISTADO_SERVICIOS;
	}

	
	public String cancelarEdicionServicio() 
	{
		return RETORNO_LISTADO_SERVICIOS;
	}

	public List<Usuario> getIrsByMiembro(Usuario usuario)
	{
		return usuarioService.getIrsByMiembroServicio(usuario, this.servicioEdit);
	}
	
	public void establecerServicioPuertakronCreate()
	{
		this.terminalSeleccionado = null;
	}
	
	public void crearServicioPuertakron() 
	{
		PuertaKron pk;
		try 
		{
			pk = kronService.getPuertaKronByTerminalKron(this.terminalSeleccionado);
		} 
		catch (Exception ex) 
		{
			log.error("No se ha podido obtener la Puerta Kron a partir del terminal", ex);
			this.facesMessages.add(StatusMessage.Severity.ERROR, "servicio.guardar.puerta.error", null, null, ex.getMessage()); 
			return;
		}
		ServicioPuertakron sp = new ServicioPuertakron();
		sp.setServicio(this.servicioEdit) ;
		sp.setPuertaKron(pk);
		try
		{
			kronService.crearServicioPuertakron( sp );
			this.facesMessages.add(StatusMessage.Severity.INFO, "servicio.guardar.puerta.ok", null, null, getDescripcionTerminal(this.terminalSeleccionado)); 
			this.puertasServicio = kronService.getListaServicioPuertakronByService( this.servicioEdit );
		}
		catch(Exception ex) 
		{
			this.facesMessages.add(StatusMessage.Severity.ERROR, "servicio.guardar.puerta.error", null, null, ex.getMessage()); 
		}
	}
	
	public void eliminarServicioPuertakron(ServicioPuertakron sp) 
	{
		String descripcionPuerta = getDescripcionPuerta(sp.getPuertaKron().getPuertaKronView());
		try
		{
			kronService.eliminarServicioPuertakron( sp );
			this.facesMessages.add(StatusMessage.Severity.INFO, "servicio.eliminar.puerta.ok", null, null, descripcionPuerta); 
			this.puertasServicio = kronService.getListaServicioPuertakronByService( this.servicioEdit );
		}
		catch(Exception ex)
		{
			this.facesMessages.add(StatusMessage.Severity.ERROR, "servicio.eliminar.puerta.error", null, null, ex.getMessage()); 
		}
	}

	private String getDescripcionPuerta(PuertaKronView pkv)
	{
		return new StringBuilder(pkv.getNombreEdificio()).append(" - ").append(pkv.getNombreLector()).toString();
	}
	
	private String getDescripcionTerminal(TerminalKron tk)
	{
		return new StringBuilder(tk.getNombreEdificio()).append(" - ").append(tk.getNombreLector()).toString();
	}
	
}
