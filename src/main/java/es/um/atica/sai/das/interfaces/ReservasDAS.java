package es.um.atica.sai.das.interfaces;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.entities.Consumo;
import es.um.atica.sai.entities.Equipo;
import es.um.atica.sai.entities.Reservas;
import es.um.atica.sai.entities.TipoReservable;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;

@Local
public interface ReservasDAS extends DataAccessService<Reservas> {

    List<Reservas> getReservasPorTurno(Equipo reservable, Date fechaInicio, Date fechaFin);
    List<Reservas> getReservasPorTurnoByTipoReservable(TipoReservable tr, Date fechaInicio, Date fechaFin);
    List<Reservas> getReservasPorTurnoTecnico(Usuario usuarioTecnicoAsignado, Date fechaInicio, Date fechaFin);
    List<Reservas> getReservasPorTurnoTecnicoDisponibilidadMod(Usuario usuarioTecnicoAsignado, Long codReserva, Date fechaInicio, Date fechaFin);
    List<Reservas> getReservasPorConsumo(Consumo c);
    List<Reservas> getReservasActivasPorConsumo(Consumo c);
	void crear(Reservas reserva) throws SaiException;
	void modificar(Reservas reserva) throws SaiException;
	void eliminar(Reservas reserva) throws SaiException;
}
