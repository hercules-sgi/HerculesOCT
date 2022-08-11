package es.um.atica.sai.das.interfaces;

import java.util.List;

import javax.ejb.Local;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.entities.Perfil;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.entities.UsuarioPerfil;
import es.um.atica.sai.exceptions.SaiException;

@Local
public interface UsuarioPerfilDAS extends DataAccessService<UsuarioPerfil> {
    

    public static final String NAME = "usuarioPerfilDAS";

	List<UsuarioPerfil> getPerfilesUsuario(Usuario usuario);
	List<UsuarioPerfil> getPerfilesUsuarioByServicio(Usuario usuario, Servicio servicio);
	List<UsuarioPerfil> getPerfilesUsuarioIgnoraEstado(Usuario usuario);
	boolean tienePerfilIrIgnoraEstado(Usuario usuario);
	List<Perfil> getPerfilesPuedeCrearByPerfil(Perfil perfil);
	UsuarioPerfil getSupervisorServicio(Usuario usuario, Servicio servicio);
	List<UsuarioPerfil> getSupervisoresServicio(Servicio servicio);
	UsuarioPerfil getUsuarioPerfilNoRequiereServicio(UsuarioPerfil up );
    UsuarioPerfil getUsuarioPerfilSiRequiereServicioNoRequiereIr(UsuarioPerfil up );
    UsuarioPerfil getUsuarioPerfilSiRequiereServicioSiRequiereIr(UsuarioPerfil up );
	void crear(UsuarioPerfil up) throws SaiException;
	void modificar(UsuarioPerfil up) throws SaiException;
	void eliminar(UsuarioPerfil up) throws SaiException;
    
}
