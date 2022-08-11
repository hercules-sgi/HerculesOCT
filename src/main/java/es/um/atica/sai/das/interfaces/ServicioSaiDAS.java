package es.um.atica.sai.das.interfaces;

import java.util.List;

import javax.ejb.Local;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;

@Local
public interface ServicioSaiDAS extends DataAccessService<Servicio>{

    public static final String NAME = "servicioSaiDAS";
    
    List<Servicio> getListaServicios();
    List<Servicio> getListaServiciosConEmail();
    List<Servicio> getListaServiciosEsSupervisor(Usuario usuario);
    List<Servicio> getListaServiciosEsIr(Usuario usuario);
    List<Servicio> getListaServiciosEsIrConMiembros(Usuario usuario);
    List<Servicio> getListaServiciosEsMiembro(Usuario usuario);
    List<Servicio> getListaServiciosEsTecnico(Usuario usuario);
    List<Servicio> getListaServiciosEsTecnicoConVisibilidadSolicitudes(Usuario usuario);
    List<Servicio> getListaServiciosEsTecnicoSinVisibilidadSolicitudes(Usuario usuario);
    
    void crear(Servicio servicio) throws SaiException;
    void modificar(Servicio servicio) throws SaiException;
    void eliminar(Servicio servicio) throws SaiException;
}
