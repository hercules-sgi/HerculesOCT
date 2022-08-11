package es.um.atica.sai.das.interfaces;

import java.util.List;

import javax.ejb.Local;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.entities.Consumo;
import es.um.atica.sai.entities.ConsumoEquipo;
import es.um.atica.sai.entities.Equipo;
import es.um.atica.sai.exceptions.SaiException;

@Local
public interface ConsumoEquipoDAS extends DataAccessService<ConsumoEquipo>{
    
	public static final String NAME = "consumoEquipoDAS";
	
    List<ConsumoEquipo> getEquiposByConsumo(Consumo consumo);  
    boolean existeConsumoEquipo(Consumo consumo, Equipo equipo);
	void crear(ConsumoEquipo ce) throws SaiException;
	void modificar(ConsumoEquipo ce) throws SaiException;
	void eliminar(ConsumoEquipo ce) throws SaiException;
}
