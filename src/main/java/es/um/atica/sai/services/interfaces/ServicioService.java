package es.um.atica.sai.services.interfaces;

import java.util.List;

import javax.ejb.Local;

import es.um.atica.sai.entities.Nivel1;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.entities.UsuarioPerfil;
import es.um.atica.sai.exceptions.SaiException;

@Local
public interface ServicioService {
    
    static final String NAME = "servicioService";
    Servicio guardarServicio(Servicio servicio) throws SaiException;
    Servicio getServicioById(Long code);
    List<Servicio> getListaServicios();
    List<Servicio> getServiciosConEmail();
    List<Servicio> getListaServiciosEsSupervisor(Usuario usuario);
    String getListaCodsServicioEsSupervisor(Usuario usuario);
    List<Servicio> getListaServiciosEsIr(Usuario usuario);
    List<Servicio> getListaServiciosEsIrConMiembros(Usuario usuario);
    String getListaCodsServicioEsIr(Usuario usuario);
    List<Servicio> getListaServiciosEsTecnico(Usuario usuario);
    String getListaCodsServicioEsTecnico(Usuario usuario);
    String getListaCodsServicioEsTecnicoConVisibilidadSolicitudes(Usuario usuario);
    String getListaCodsServicioEsTecnicoSinVisibilidadSolicitudes(Usuario usuario);
    List<Servicio> getListaServiciosPuedoHacerSolicitud();
    List<UsuarioPerfil> getSupervisoresServicio(Servicio servicio);
    List<Usuario> getListaSupervisoresServicio(Servicio servicio);
    List<Usuario> getListaMiembrosServicio(Servicio servicio);
    //void addSupervisorToServicio(Servicio servicio, Usuario usuario) throws SaiException;
    List<Nivel1> getNivelesByServicio(Servicio s);
}
