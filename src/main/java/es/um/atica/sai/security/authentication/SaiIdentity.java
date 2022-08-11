package es.um.atica.sai.security.authentication;



import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.log.Log;

import es.um.atica.commons.utils.UtilDate;
import es.um.atica.sai.dtos.PerfilAdmin;
import es.um.atica.sai.dtos.TipoPerfil;
import es.um.atica.sai.entities.Perfil;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.entities.UsuarioPerfil;
import es.um.atica.sai.services.interfaces.ServicioService;
import es.um.atica.sai.services.interfaces.UsuarioService;
import es.um.atica.seam.security.UmuIdentity;

@Name( "org.jboss.seam.security.identity" )
@Scope( ScopeType.SESSION )
@Install(precedence = Install.APPLICATION )
@BypassInterceptors
@Startup
public class SaiIdentity extends UmuIdentity {

    @Logger
    private Log log;
    
    private static final long serialVersionUID = -8219039150325648639L;
    
    private Usuario usuarioConectado;
    private List<PerfilAdmin> listaPerfiles = new ArrayList<>(0);
    private List<PerfilAdmin> listaPerfilesPuedeCrear = new ArrayList<>(0);
    // Tags de perfil que tiene el usuario y requieren un servicio. Util para el tratamiento del perfil especial ADMINISTRATIVO
    private List<String> listaTagsPerfilRequierenServicio = new ArrayList<>(0);
    private List<String> listaDescripcionesPerfiles = new ArrayList<>(0);
    private boolean trabajadorUmu;
    private boolean usuarioExterno;
    private boolean caducado;
    private boolean suplantado = false;
    private Long codUsuarioSuplantador;

	public Usuario getUsuarioConectado() {
		return usuarioConectado;
	}
	
	public void setUsuarioConectado( Usuario usuarioConectado ) {
		this.usuarioConectado = usuarioConectado;
	}
	
	public List<PerfilAdmin> getListaPerfiles() {
		return listaPerfiles;
	}
	
	public void setListaPerfiles( List<PerfilAdmin> listaPerfiles ) {
		this.listaPerfiles = listaPerfiles;
	}
	
	public List<PerfilAdmin> getListaPerfilesPuedeCrear() {
		return listaPerfilesPuedeCrear;
	}
	
	public void setListaPerfilesPuedeCrear( List<PerfilAdmin> listaPerfilesPuedeCrear ) {
		this.listaPerfilesPuedeCrear = listaPerfilesPuedeCrear;
	}

	public List<String> getListaTagsPerfilRequierenServicio() {
		return listaTagsPerfilRequierenServicio;
	}
	
	public void setListaTagsPerfilRequierenServicio( List<String> listaTagsPerfilRequierenServicio ) {
		this.listaTagsPerfilRequierenServicio = listaTagsPerfilRequierenServicio;
	}
	
	public List<String> getListaDescripcionesPerfiles() {
		return listaDescripcionesPerfiles;
	}
	
	public void setListaDescripcionesPerfiles( List<String> listaDescripcionesPerfiles ) {
		this.listaDescripcionesPerfiles = listaDescripcionesPerfiles;
	}
	
	public boolean isTrabajadorUmu() {
		return trabajadorUmu;
	}
	
	public void setTrabajadorUmu( boolean trabajadorUmu ) {
		this.trabajadorUmu = trabajadorUmu;
	}

	public boolean isUsuarioExterno() {
		return usuarioExterno;
	}
	
	public void setUsuarioExterno( boolean usuarioExterno ) {
		this.usuarioExterno = usuarioExterno;
	}

	public boolean isCaducado() {
		return caducado;
	}
	
	public void setCaducado( boolean caducado ) {
		this.caducado = caducado;
	}

	public Long getCodUsuarioSuplantador() {
		return codUsuarioSuplantador;
	}

	public void setCodUsuarioSuplantador( Long codUsuarioSuplantador ) {
		this.codUsuarioSuplantador = codUsuarioSuplantador;
	}

	public boolean isSuplantado() {
		return suplantado;
	}
	
	public void setSuplantado( boolean suplantado ) {
		this.suplantado = suplantado;
	}
	
	private UsuarioService getUsuarioService()
	{
		return (UsuarioService)Component.getInstance( UsuarioService.NAME );
	}
	
	private ServicioService getServicioService()
	{
		return (ServicioService)Component.getInstance( ServicioService.NAME );
	}
	
	public void cargarRoles(Usuario usuario)
	{
		log.info("Cargando roles del usuario #0", usuario.getDni());
		this.usuarioConectado = usuario;
		List<UsuarioPerfil> perfilesUsuario = getUsuarioService().getPerfilesUsuario(usuario);
		if (perfilesUsuario != null && !perfilesUsuario.isEmpty())
		{
			Iterator<UsuarioPerfil> itUp = perfilesUsuario.iterator();
			while (itUp.hasNext())
			{
				UsuarioPerfil up = itUp.next();
				StringBuilder descripcionPerfil = new StringBuilder(up.getPerfil().getNombre());
				PerfilAdmin pa = new PerfilAdmin();
				pa.setTagPerfil(up.getPerfil().getTag());
				if (up.getServicio()!=null)
				{
					
					descripcionPerfil.append(" en ").append(up.getServicio().getNombre());
					pa.setCodServicio(up.getServicio().getCod());
					if (!this.getListaTagsPerfilRequierenServicio().contains(up.getPerfil().getTag()))
					{
						this.getListaTagsPerfilRequierenServicio().add(up.getPerfil().getTag());
					}
				}
				this.listaDescripcionesPerfiles.add(descripcionPerfil.toString());
				this.listaPerfiles.add(pa);
				this.cargaPerfilesPuedeCrear(up);
			}
		}
		this.trabajadorUmu = getUsuarioService().esTrabajadorUmu(usuario.getDni());
   	 	if (this.usuarioConectado.getDatosUsuario().getEmail().contains("@um.es") || this.usuarioConectado.getDatosUsuario().getEmail().contains("@ticarum.es"))
   	 	{
   	 		this.usuarioExterno = false;
   	 	}
   	 	else
   	 	{
   	 		this.usuarioExterno = true;
   	 	}
   	 	if (this.usuarioConectado.getCaduca()!=null && UtilDate.anterior(this.usuarioConectado.getCaduca(), new Date()))
   	 	{
   	 		this.caducado = true;
   	 	}
   	 	else
   	 	{
   	 		this.caducado = false;
   	 	}
	}
	
	private void cargaPerfilesPuedeCrear(UsuarioPerfil up)
	{
		List<Perfil> perfilesPuedeCrearPerfil = getUsuarioService().getPerfilesPuedeCrearByPerfil(up.getPerfil());
		if (perfilesPuedeCrearPerfil != null && !perfilesPuedeCrearPerfil.isEmpty())
		{
			Iterator<Perfil> itP = perfilesPuedeCrearPerfil.iterator();
			while (itP.hasNext())
			{
				Perfil perfil = itP.next();
				PerfilAdmin paPc = new PerfilAdmin();
				paPc.setTagPerfil(perfil.getTag());
				if (up.getServicio()!=null)
				{
					paPc.setCodServicio(up.getServicio().getCod());
				}
				this.listaPerfilesPuedeCrear.add(paPc);
			}
		}
	}
	
	public void suplantarUsuario(Usuario usuario)
	{
		this.codUsuarioSuplantador = this.usuarioConectado.getCod();
		this.suplantado = true;
	    this.listaPerfiles = new ArrayList<>(0);
	    this.listaPerfilesPuedeCrear = new ArrayList<>(0);
	    this.listaTagsPerfilRequierenServicio = new ArrayList<>(0);
	    this.listaDescripcionesPerfiles = new ArrayList<>(0);
		this.cargarRoles( usuario );
	}
	
	public void deshacerSuplantacion()
	{
		Usuario usuarioOriginal = getUsuarioService().getUsuarioByCod(this.codUsuarioSuplantador);
		this.codUsuarioSuplantador = null;
		this.suplantado = false;
	    this.listaPerfiles = new ArrayList<>(0);
	    this.listaPerfilesPuedeCrear = new ArrayList<>(0);
	    this.listaTagsPerfilRequierenServicio = new ArrayList<>(0);
	    this.listaDescripcionesPerfiles = new ArrayList<>(0);
		this.cargarRoles(usuarioOriginal);
	}
	
	public boolean tienePerfil(String tagPerfil)
	{
        Iterator<PerfilAdmin> itPa = this.listaPerfiles.iterator();
        while (itPa.hasNext())
        {
            PerfilAdmin pa = itPa.next();
            if (TipoPerfil.SUPERGESTOR.equals(pa.getTagPerfil()) || (!TipoPerfil.SUPERGESTOR.equals(tagPerfil) && TipoPerfil.GESTOR.equals(pa.getTagPerfil())) || tagPerfil.equals(pa.getTagPerfil()))
            {
                return true;
            }
        }
        return false;
	}
	
	public boolean tienePerfilEnServicio(String tagPerfil, Long codServicio)
	{
        Iterator<PerfilAdmin> itPa = this.listaPerfiles.iterator();
        while (itPa.hasNext())
        {
            PerfilAdmin pa = itPa.next();
            if (TipoPerfil.SUPERGESTOR.equals(pa.getTagPerfil()) || (!TipoPerfil.SUPERGESTOR.equals(tagPerfil) && TipoPerfil.GESTOR.equals(pa.getTagPerfil())) || 
            	(tagPerfil.equals(pa.getTagPerfil()) && codServicio.equals(pa.getCodServicio())))
            {
                return true;
            }
        }
        return false;
	}

	/**
	 * Devuelve la lista de Servicios asociados al usuario conectado y al perfil pasado como parámetro
	 * 
	 * @param tagPerfil
	 *        - Etiqueta que identifica el tipo de perfil
	 * 
	 * @return Lista de servicios 
	 **/
	public List<Servicio> getServiciosPerfil(String tagPerfil)
	{
		if (this.tienePerfil(TipoPerfil.SUPERGESTOR) || this.tienePerfil(TipoPerfil.GESTOR) ||
		    TipoPerfil.SUPERGESTOR.equals(tagPerfil) || TipoPerfil.GESTOR.equals(tagPerfil) || TipoPerfil.ADMINISTRATIVO.equals(tagPerfil))
		{
			return getServicioService().getListaServicios();
		}
		List<Servicio> listaServicios = new ArrayList<>(0);
        Iterator<PerfilAdmin> itPa = this.listaPerfiles.iterator();
        while (itPa.hasNext())
        {
            PerfilAdmin pa = itPa.next();
            if (tagPerfil.equals(pa.getTagPerfil()) && pa.getCodServicio() != null)
            {
                Servicio servicio = getServicioService().getServicioById(pa.getCodServicio());
                listaServicios.add(servicio);
            }
        }
        Collections.sort(listaServicios);
        return listaServicios;
	}
	
	/**
	 * Devuelve la lista de Servicios asociados al usuario conectado, independientemente del perfil que se lo haya concedido,
	 * exceptuando el acceso que el perfil administrativo te concede para todos los servicios
	 * 
	 * @return Lista de servicios 
	 **/
	public List<Servicio> getServiciosUsuarioConectado()
	{
		if (this.tienePerfil(TipoPerfil.SUPERGESTOR) || this.tienePerfil(TipoPerfil.GESTOR))
		{
			return getServicioService().getListaServicios();
		}
		List<Servicio> listaServicios = new ArrayList<>(0);
	    Iterator<PerfilAdmin> itPa = this.listaPerfiles.iterator();
	    while (itPa.hasNext())
	    {
	    	PerfilAdmin pa = itPa.next();
	    	if (listaTagsPerfilRequierenServicio.contains(pa.getTagPerfil()) && pa.getCodServicio() != null)
	    	{
	    		Servicio servicio = getServicioService().getServicioById(pa.getCodServicio());
	    		if (!listaServicios.contains(servicio))
	    		{
	    			listaServicios.add(servicio);
	    		}
	    	}
	    }
	    Collections.sort(listaServicios);
	    return listaServicios;
	}
	
	
	public boolean puedeCrearPerfil(String tagPerfil)
	{
        Iterator<PerfilAdmin> itPa = this.listaPerfilesPuedeCrear.iterator();
        while (itPa.hasNext())
        {
            PerfilAdmin pa = itPa.next();
            if (TipoPerfil.SUPERGESTOR.equals(pa.getTagPerfil()) || (!TipoPerfil.SUPERGESTOR.equals(tagPerfil) && TipoPerfil.GESTOR.equals(pa.getTagPerfil())) || tagPerfil.equals(pa.getTagPerfil()))
            {
                return true;
            }
        }
        return false;
	}
	
	public boolean puedeCrearPerfilEnServicio(String tagPerfil, Long codServicio)
	{
        Iterator<PerfilAdmin> itPa = this.listaPerfilesPuedeCrear.iterator();
        while (itPa.hasNext())
        {
            PerfilAdmin pa = itPa.next();
            if (TipoPerfil.SUPERGESTOR.equals(pa.getTagPerfil()) || (!TipoPerfil.SUPERGESTOR.equals(tagPerfil) && TipoPerfil.GESTOR.equals(pa.getTagPerfil())) || 
            	(tagPerfil.equals(pa.getTagPerfil()) && codServicio.equals(pa.getCodServicio())))
            {
                return true;
            }
        }
        return false;
	}

	/**
	 * Devuelve la lista de perfiles que puede crear el usuario conectado, independientemente del perfil que se lo haya concedido,
	 * Luego se debe comprobar para qué servicio se puede crear cada perfil de esta lista
	 * 
	 * @return Lista de perfiles 
	 **/
	public List<Perfil> getPerfilesPuedeCrearUsuarioConectado()
	{
		if (this.tienePerfil(TipoPerfil.SUPERGESTOR))
		{
			return getUsuarioService().getListaPerfiles();
		}
		
		List<Perfil> listaPerfilesAux = new ArrayList<>(0);
	    Iterator<PerfilAdmin> itPa = this.listaPerfilesPuedeCrear.iterator();
	    while (itPa.hasNext())
	    {
	    	PerfilAdmin pa = itPa.next();
	    	Perfil perfil = getUsuarioService().getPerfilByTag(pa.getTagPerfil());
	    	if (!listaPerfilesAux.contains(perfil))
	    	{
	    		listaPerfilesAux.add(perfil);
	    	}
	    }
	    return listaPerfilesAux;
	}
	
	/**
	 * Devuelve la lista de Servicios en los que el usuario conectado puede crear el perfil pasado como parámetro
	 * 
	 * @param tagPerfil
	 *        - Etiqueta que identifica el tipo de perfil
	 * 
	 * @return Lista de servicios 
	 **/
	public List<Servicio> getServiciosPuedeCrearPerfil(String tagPerfil)
	{
		if (this.tienePerfil(TipoPerfil.SUPERGESTOR) || this.tienePerfil(TipoPerfil.GESTOR))
		{
			return getServicioService().getListaServicios();
		}
		List<Servicio> listaServicios = new ArrayList<>(0);
        Iterator<PerfilAdmin> itPa = this.listaPerfilesPuedeCrear.iterator();
        while (itPa.hasNext())
        {
            PerfilAdmin pa = itPa.next();
            if (tagPerfil.equals(pa.getTagPerfil()) && pa.getCodServicio() != null)
            {
                Servicio servicio = getServicioService().getServicioById(pa.getCodServicio());
                listaServicios.add(servicio);
            }
        }
        Collections.sort(listaServicios);
        return listaServicios;
	}
	
}
