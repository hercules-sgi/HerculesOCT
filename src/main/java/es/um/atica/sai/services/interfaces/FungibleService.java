package es.um.atica.sai.services.interfaces;

import java.util.List;

import javax.ejb.Local;

import es.um.atica.sai.entities.Nivel1;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;

@Local
public interface FungibleService {
    
    public static final String NAME = "fungibleService";

    public List<Nivel1> getListaNivel1(Usuario usuario, String nombre, Servicio servicio) throws SaiException;
    List<Nivel1> buscarFungiblesBySupervisor(Long codUsuario);
    List<Nivel1> getListaFungiblesByServicio(Servicio servicio);
    void guardarFungible(Nivel1 fungible ) throws SaiException;
    void eliminarFungible(Nivel1 fungible) throws SaiException;
    Nivel1 getNivel1byId(Long cod) throws SaiException;
    
}
