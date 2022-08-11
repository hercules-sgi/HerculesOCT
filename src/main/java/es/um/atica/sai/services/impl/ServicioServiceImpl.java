package es.um.atica.sai.services.impl;

import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import es.um.atica.sai.das.interfaces.Nivel1DAS;
import es.um.atica.sai.das.interfaces.PerfilDAS;
import es.um.atica.sai.das.interfaces.ServicioSaiDAS;
import es.um.atica.sai.das.interfaces.UsuarioDAS;
import es.um.atica.sai.das.interfaces.UsuarioPerfilDAS;
import es.um.atica.sai.dtos.TipoPerfil;
import es.um.atica.sai.entities.Nivel1;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.entities.UsuarioPerfil;
import es.um.atica.sai.exceptions.SaiException;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.ServicioService;

@Stateless
@Name(ServicioService.NAME)
public class ServicioServiceImpl implements ServicioService {

	@In(value = "servicioSaiDAS", create = true)
	private ServicioSaiDAS servicioSaiDAS;

	@In (value = UsuarioPerfilDAS.NAME, create = true)
	private UsuarioPerfilDAS usuarioPerfilDAS;
	
	@In (value = UsuarioDAS.NAME, create = true)
	private UsuarioDAS usuarioDAS;

	@In (value = PerfilDAS.NAME, create = true)
	private PerfilDAS perfilDAS;

	@In(value = Nivel1DAS.NAME, create = true)
	private Nivel1DAS nivel1DAS;

	@In (create = true)
	SaiIdentity identity;

	@Logger
	Log log;

	@Override
	public List<Servicio> getServiciosConEmail()
	{
		return servicioSaiDAS.getListaServiciosConEmail();
	}

	@Override
	public List<Servicio> getListaServiciosEsSupervisor(Usuario usuario)
	{
		return servicioSaiDAS.getListaServiciosEsSupervisor( usuario );
	}

	@Override
	public String getListaCodsServicioEsSupervisor(Usuario usuario)
	{
		return listaServiciosToListaCods(servicioSaiDAS.getListaServiciosEsSupervisor(usuario));
	}

	@Override
	public List<Servicio> getListaServiciosEsTecnico(Usuario usuario)
	{
		return servicioSaiDAS.getListaServiciosEsTecnico( usuario );
	}

	@Override
	public String getListaCodsServicioEsTecnico(Usuario usuario)
	{
		return this.listaServiciosToListaCods(servicioSaiDAS.getListaServiciosEsTecnico(usuario));
	}

	@Override
	public String getListaCodsServicioEsTecnicoConVisibilidadSolicitudes(Usuario usuario)
	{
		return this.listaServiciosToListaCods(servicioSaiDAS.getListaServiciosEsTecnicoConVisibilidadSolicitudes(usuario));
	}
	
	@Override
	public String getListaCodsServicioEsTecnicoSinVisibilidadSolicitudes(Usuario usuario)	
	{
		return this.listaServiciosToListaCods(servicioSaiDAS.getListaServiciosEsTecnicoSinVisibilidadSolicitudes(usuario));
	}
	
	@Override
	public List<Servicio> getListaServiciosEsIr(Usuario usuario)
	{
		return servicioSaiDAS.getListaServiciosEsIr( usuario );
	}

	@Override
	public List<Servicio> getListaServiciosEsIrConMiembros(Usuario usuario)
	{
		return servicioSaiDAS.getListaServiciosEsIrConMiembros( usuario );
	}

	@Override
	public String getListaCodsServicioEsIr(Usuario usuario)
	{
		return listaServiciosToListaCods(servicioSaiDAS.getListaServiciosEsIr(usuario));
	}

	private String listaServiciosToListaCods(List<Servicio> listaServicios)
	{
		if (( listaServicios == null ) || listaServicios.isEmpty())
		{
			return null;
		}
		final StringBuilder listaCods = new StringBuilder("");
		final Iterator<Servicio> itServ = listaServicios.iterator();
		while (itServ.hasNext())
		{
			final Servicio serv = itServ.next();
			listaCods.append(serv.getCod().toString()).append(",");
		}
		return listaCods.substring(0, listaCods.length()-1);
	}

	@Override
	public List<Servicio> getListaServiciosPuedoHacerSolicitud()
	{
		if (identity.tienePerfil(TipoPerfil.SUPERVISOR) || identity.tienePerfil(TipoPerfil.TECNICO))
		{
			return servicioSaiDAS.getListaServicios();
		}
		else
		{
			return identity.getServiciosUsuarioConectado();
		}
	}

	@Override
	public Servicio getServicioById( Long code ) {
		return servicioSaiDAS.find( code );

	}

	@Override
	public List<Servicio> getListaServicios()
	{
		return servicioSaiDAS.getListaServicios();
	}

	@Override
	public Servicio guardarServicio( Servicio servicio ) throws SaiException {
		final Servicio result = servicio;
		if (servicio.getCod()==null)
		{
			servicioSaiDAS.crear( servicio );
		}
		else
		{
			servicioSaiDAS.modificar( servicio );
		}
		return result;
	}

	@Override
	public List<UsuarioPerfil> getSupervisoresServicio(Servicio servicio)
	{
		return usuarioPerfilDAS.getSupervisoresServicio( servicio );
	}

	@Override
	public List<Usuario> getListaSupervisoresServicio(Servicio servicio)
	{
		return usuarioDAS.getSupervisoresServicio( servicio );
	}

	@Override
	public List<Usuario> getListaMiembrosServicio(Servicio servicio)
	{
		return usuarioDAS.getMiembrosServicio( servicio );
	}
	

	
	/*
	@Override
	public void addSupervisorToServicio( Servicio servicio, Usuario usuario ) throws SaiException
	{
		UsuarioPerfil up = usuarioPerfilDAS.getSupervisorServicio(usuario, servicio);
		if (up != null)
		{
			throw new SaiException("El usuario ya estaba dado de alta como supervisor del servicio");
		}
		else
		{
			up = new UsuarioPerfil();
			up.setUsuario( usuario );
			up.setServicio( servicio );
			up.setPerfil(perfilDAS.getPerfilByTag(TipoPerfil.SUPERVISOR));
			up.setFechaAlta(new Date());
			up.setPendienteValidarIr( "NO" );
			usuarioPerfilDAS.crear(up);
		}
	}
*/
	@Override
	public List<Nivel1> getNivelesByServicio( Servicio s ) {
		return nivel1DAS.getNivelesByServicio( s );
	}
}
