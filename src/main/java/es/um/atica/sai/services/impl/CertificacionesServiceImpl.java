package es.um.atica.sai.services.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.Stateless;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.SeamResourceBundle;
import org.primefaces.model.UploadedFile;

import es.um.atica.commons.utils.UtilDate;
import es.um.atica.sai.das.impl.CertificacionDASImpl;
import es.um.atica.sai.das.impl.ProductoTipoCertificacionDASImpl;
import es.um.atica.sai.das.impl.TipoCertificacionDASImpl;
import es.um.atica.sai.das.interfaces.CertificacionDAS;
import es.um.atica.sai.das.interfaces.ProductoTipoCertificacionDAS;
import es.um.atica.sai.das.interfaces.TipoCertificacionDAS;
import es.um.atica.sai.das.interfaces.UsuarioDAS;
import es.um.atica.sai.dtos.TipoPerfil;
import es.um.atica.sai.entities.Certificacion;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.ProductoTipoCertificacion;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.TipoCertificacion;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;
import es.um.atica.sai.paos.interfaces.SaiPao;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.CertificacionesService;
import es.um.atica.sai.utils.Utilidades;

@Stateless
@Name( CertificacionesServiceImpl.NAME )
public class CertificacionesServiceImpl implements CertificacionesService {

	public static final String NAME = "certificacionesService";

	@In( value = TipoCertificacionDASImpl.NAME, create = true )
	private TipoCertificacionDAS tipoCertificacionDAS;

	@In( value = ProductoTipoCertificacionDASImpl.NAME, create = true )
	private ProductoTipoCertificacionDAS productoTipoCertificacionDAS;

	@In( value = CertificacionDASImpl.NAME, create = true )
	private CertificacionDAS certificacionDAS;

	@In(value= UsuarioDAS.NAME, create=true)
	private UsuarioDAS usuarioDAS;

	@In (value = "org.jboss.seam.security.identity", required = false )
	private SaiIdentity identity;

	@In(value= "saiPao", create=true)
	private SaiPao saiPao;

	private final ResourceBundle srb = SeamResourceBundle.getBundle();

	@Override
	public void eliminarTipoCertificacion( TipoCertificacion t ) throws SaiException 
	{
		// Eliminamos las asociaciones con productos
		if (t.getListaProductos() != null && !t.getListaProductos().isEmpty())
		{
			Iterator<ProductoTipoCertificacion> itPtc = t.getListaProductos().iterator();
			while (itPtc.hasNext())
			{
				ProductoTipoCertificacion ptc = itPtc.next();
				productoTipoCertificacionDAS.eliminar(ptc);
			}
		}
		if (t.getListaCertificaciones() == null || t.getListaCertificaciones().isEmpty())
		{
			// Si no tiene certificaciones de usuario asociadas podemos eliminar
			tipoCertificacionDAS.eliminar(t);
		}
		else
		{
			// Si tiene certificaciones de usuario asociadas damos de baja
			t.setNombre(new StringBuilder(t.getNombre()).append("_").append(Utilidades.obtenerStringAleatorio(4)).toString());
			t.setFechaBaja(new Date());
			tipoCertificacionDAS.modificar(t);
		}
	}

	@Override
	public void guardarTipoCertificacion(TipoCertificacion t) throws SaiException 
	{
		if (t.getCod() == null)
		{
			if (tipoCertificacionDAS.existeTipoByNombre(t.getNombre()))
			{
				throw new SaiException(srb.getString("tipoCertificion.existe.mismo.nombre"));
			}
			tipoCertificacionDAS.crear(t);
		}
		else
		{
			if (tipoCertificacionDAS.existeOtroTipoByNombre(t))
			{
				throw new SaiException(srb.getString("tipoCertificion.existe.mismo.nombre"));
			}
			tipoCertificacionDAS.modificar(t);
		}
	}

	@Override
	public List<TipoCertificacion> getListaTiposCertificaciones() {
		return tipoCertificacionDAS.getListaTiposCertificaciones();
	}

	@Override
	public List<TipoCertificacion> getListaTiposCertificacionesByProducto(Producto producto) 
	{
		return tipoCertificacionDAS.getListaTiposCertificacionesByProducto(producto);
	}
	
	@Override
	public List<Certificacion> busquedaCertificaciones(){
		return certificacionDAS.busquedaCertificaciones();
	}

	@Override
	public List<Certificacion> busquedaCertificacionesByListaUsuarios(List<Usuario> listaUsuarios)
	{
		return certificacionDAS.busquedaCertificacionesByListaUsuarios( listaUsuarios );
	}

	@Override
	public List<Certificacion> getListaCertificacionesByUsuario(Usuario usuario)
	{
		return certificacionDAS.getListaCertificacionesByUsuario(usuario);
	}


	@Override
	public boolean existeCertificacionPendienteByTipoAndUsuario(TipoCertificacion tc, Usuario usuario)
	{
		return certificacionDAS.existeCertificacionPendienteByTipoAndUsuario(tc, usuario);
	}


	@Override
	public boolean existeCertificacionActivaByTipoAndUsuario(TipoCertificacion tc, Usuario usuario)
	{
		return certificacionDAS.existeCertificacionActivaByTipoAndUsuario(tc, usuario);
	}
	
	// Este método sólo se ejecutará para gestores y supervisores
	@Override
	public List<Usuario> getListaUsuariosPuedoSolicitarCertificacion()
	{
		List<Usuario> listaUsuarios;
		if (identity.tienePerfil(TipoPerfil.GESTOR))
		{
			listaUsuarios = usuarioDAS.getUsuariosGestorPuedeSolicitarCertificacion();
		}
		else
		{
			final List<Servicio> listaServicios = identity.getServiciosPerfil(TipoPerfil.SUPERVISOR);
			listaUsuarios = usuarioDAS.getUsuariosSupervisorPuedeSolicitarCertificacion(listaServicios);
			if (!listaUsuarios.contains(identity.getUsuarioConectado()))
			{
				listaUsuarios.add(identity.getUsuarioConectado());
			}
		}
		Collections.sort(listaUsuarios);
		return listaUsuarios;
	}

	public String getDescripcionEstadoCertificacion(Certificacion cert)
	{
		if ("PENDIENTE".equals(cert.getEstado()) || "DENEGADA".equals(cert.getEstado()) || cert.getFechaCaducidad() == null || UtilDate.anterior(new Date(), cert.getFechaCaducidad()))
		{
			return cert.getEstado();
		}
		else
		{
			return "CADUCADA";
		}
	}
	
	public String getColorEstadoCertificacion(String estado)
	{
		if ("PENDIENTE".equals(estado))
		{
			return "amarillo";
		}
		else if ("ACTIVA".equals(estado))
		{
			return "verde";
		}
		else if ("DENEGADA".equals(estado))
		{
			return "rojo";
		}
		else if ("CADUCADA".equals(estado))
		{
			return "lila";
		}
		return null;
	}
	
	@Override
	public void eliminarCertificacion(Certificacion c) throws SaiException
	{
		try
		{
			saiPao.eliminarCertificacion(BigDecimal.valueOf(c.getCod()), c.getFicheroName());
		}
		catch (final Exception ex)
		{
			throw new SaiException(ex.getMessage());
		}
	}

	@Override
	public void crearCertificacion(Certificacion c, UploadedFile fichero) throws SaiException
	{
		try
		{
			saiPao.crearCertificacion(BigDecimal.valueOf(c.getUsuario().getCod()), BigDecimal.valueOf(c.getTipoCertificacion().getCod()), fichero.getFileName(), fichero.getContents(), c.getFechaCaducidad());
		}
		catch (final Exception ex)
		{
			throw new SaiException(ex.getMessage());
		}
	}

	@Override
	public void modificarCertificacion(Certificacion c) throws SaiException
	{
		certificacionDAS.modificar( c );
	}

	@Override
	public void eliminarProductoTipoCertificacion( ProductoTipoCertificacion p ) throws SaiException {
		productoTipoCertificacionDAS.eliminar( p );
	}

	@Override
	public void guardarProductoTipoCertificacion( ProductoTipoCertificacion p ) throws SaiException {
		productoTipoCertificacionDAS.guardar( p );
	}
}
