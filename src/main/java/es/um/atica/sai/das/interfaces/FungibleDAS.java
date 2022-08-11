package es.um.atica.sai.das.interfaces;

import java.util.List;

import javax.ejb.Local;

import es.um.atica.jpa.das.DataAccessService;
import es.um.atica.sai.entities.Nivel1;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.exceptions.SaiException;

@Local
public interface FungibleDAS extends DataAccessService<Nivel1> {
    
    public static final String NAME = "fungibleDAS";

    List<Nivel1> list(String nombre, Servicio... servicio);
    
    List<Nivel1> buscarFungiblesBySupervisor(Long codUsuario);
    List<Nivel1> getListaFungiblesByServicio(Servicio servicio);
    void crear( Nivel1 fungible ) throws SaiException;
	void modificar( Nivel1 fungible ) throws SaiException;
	void eliminar( Nivel1 fungible ) throws SaiException;
}
