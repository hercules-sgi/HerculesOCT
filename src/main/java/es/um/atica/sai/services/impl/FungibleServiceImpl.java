package es.um.atica.sai.services.impl;

import java.util.List;

import javax.ejb.Stateless;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import es.um.atica.sai.das.interfaces.FungibleDAS;
import es.um.atica.sai.das.interfaces.UsuarioDAS;
import es.um.atica.sai.dtos.TipoPerfil;
import es.um.atica.sai.entities.Nivel1;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.entities.Usuario;
import es.um.atica.sai.exceptions.SaiException;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.services.interfaces.FungibleService;

@Stateless
@Name(FungibleService.NAME)
public class FungibleServiceImpl implements FungibleService{
    
    @In(value = UsuarioDAS.NAME, create = true)
    private UsuarioDAS usuarioDAS;
    
    @In(value = FungibleDAS.NAME, create = true, required = true)
    private FungibleDAS fungibleDAS;
    
    @In (value = "org.jboss.seam.security.identity", required = false )
    private SaiIdentity identity;

    
    @Override
    public List<Nivel1> getListaNivel1( Usuario usuario, String nombre, Servicio servicio ) throws SaiException {
      usuarioDAS.refresh( usuario );
      
      List<Servicio> servicios = identity.getServiciosPerfil(TipoPerfil.SUPERVISOR);
      
      if (servicio != null && !servicios.contains( servicio ))
      {
          throw new SaiException("No tiene permiso");
      }
      
      if (servicio != null ){
          return fungibleDAS.list( nombre, servicio );
      } else {
          return fungibleDAS.list( nombre, servicios.toArray( new Servicio[]{} ) );
      }
    }

    public List<Nivel1> buscarFungiblesBySupervisor(Long codUsuario)
    {
    	return fungibleDAS.buscarFungiblesBySupervisor( codUsuario );
    }
    
    public List<Nivel1> getListaFungiblesByServicio(Servicio servicio)
    {
    	return fungibleDAS.getListaFungiblesByServicio( servicio );
    }
    
    @Override
    public void guardarFungible(Nivel1 fungible ) throws SaiException
    {        
    	if (fungible.getCod()==null)
    	{
    		fungibleDAS.crear( fungible );
    	}
    	else
    	{
    		fungibleDAS.modificar( fungible );
    	}
    }
    
    public void eliminarFungible(Nivel1 fungible) throws SaiException
    {
    	fungibleDAS.eliminar( fungible );
    }

    @Override
    public Nivel1 getNivel1byId( Long cod ) throws SaiException {
       
    	return fungibleDAS.find( cod );
    }

}
