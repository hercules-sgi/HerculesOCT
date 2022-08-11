package es.um.atica.sai.das.interfaces;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.entities.Producto;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.TipoHorario;
import es.um.atica.sai.entities.TipoReservable;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;

@Local
public interface TipoHorarioDAS extends DataAccessService<TipoHorario> {

    public static final String NAME = "tipoHorarioDAS";

    void crear(TipoHorario th) throws SaiException;
    void modificar(TipoHorario th) throws SaiException;
    void eliminar(TipoHorario th) throws SaiException;

    List<TipoHorario> buscar();
    List<TipoHorario> getListaTiposHorario();
    List<TipoHorario> getListaTiposHorarioByServicio(Servicio servicio);
    List<TipoHorario> getListaTiposHorarioByUsuario(Usuario usuario);
    TipoHorario getTipoHorarioByTipoReservableFecha(TipoReservable tr, Date fecha);
    TipoHorario getTipoHorarioByProductoFecha(Producto producto, Date fecha);
}
