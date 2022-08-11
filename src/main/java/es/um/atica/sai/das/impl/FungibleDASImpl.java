package es.um.atica.sai.das.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import es.um.atica.commons.utils.UtilString;
import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.sai.das.interfaces.FungibleDAS;
import es.um.atica.sai.dtos.TipoPerfil;
import es.um.atica.sai.entities.Nivel1;
import es.um.atica.sai.entities.Servicio;
import es.um.atica.sai.exceptions.SaiException;
import es.um.atica.sai.security.authentication.SaiIdentity;
import es.um.atica.sai.utils.Utilidades;
import es.um.atica.seam.framework.Query.QueryPriority;

@Stateless
@Name(FungibleDAS.NAME)
public class FungibleDASImpl extends DataAccessServiceImpl<Nivel1> implements FungibleDAS {

    @In("org.jboss.seam.security.identity")    
    SaiIdentity identity;

	private static final String SERVICIO = "servicio";
	
	private static final String[] RESTRICCIONESBUSQUEDA = {
			   Utilidades.cmpTextFilterEjbqlCompletoIgnoraTildes("f.nombre", "#{fungiblesBean.nombreBuscar}"),
			   UtilString.cmpNumberTextFilterEjbql("f.servicio", "#{fungiblesBean.servicioBuscar}") };  
	   
	private static List<String> getListaRestriccionesbusqueda()
	{
	       return Arrays.asList(RESTRICCIONESBUSQUEDA);
	}

    @Override
    public List<Nivel1> list( String nombre, Servicio... servicio ) {
       
        CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
        
        CriteriaQuery<Nivel1> consulta = cb.createQuery(Nivel1.class);
        
        Root<Nivel1> root = consulta.from( Nivel1.class );
     
        List<Predicate> predicates = new ArrayList<>();
        
        consulta.select( root );
        Predicate serv = null;
        if (servicio.length == 1){
           serv = cb.equal( root.get( SERVICIO ), servicio[0] );
        }  else {
           serv = root.get(SERVICIO).in( servicio) ;
        }
        
        if (hasName(nombre)){
           predicates.add( cb.like( cb.upper( root.<String>get("nombre")), "%" + nombre.trim().toUpperCase() + "%" ));
        }
        
        predicates.add(serv);
        
        
        consulta.where( cb.and(predicates.toArray( new Predicate[]{} )) );
        
     
       
       return this.getEntityManager().createQuery( consulta ).getResultList();
        
    }
    
    public List<Nivel1> buscarFungiblesBySupervisor(Long codUsuario)
    {
 	   Map<String, Object> params = new HashMap<>();
 	   params.put("codusuario", codUsuario);
 	   Map<String, Object> hints = new HashMap<>();
 	   String consulta=null;
 	   if (identity.tienePerfil(TipoPerfil.GESTOR))
 	   {
 		   consulta = Nivel1.GET_FUNGIBLES;
 	   }
 	   else
 	   {
 		   consulta = Nivel1.GET_FUNGIBLES_BY_SUPERVISOR;
 	   }
 	   try
 	   {
 		   return this.findByEntityNamedQueryWithDinamicFilter(consulta, getListaRestriccionesbusqueda(), params, null, null, null, "and", hints, null, QueryPriority.BOTH, QueryPriority.QUERY );
 	   }
 	   catch (Exception ex)
 	   {
 		   log.error("Error en buscarFungibles: #0",ex.toString());
 		   return new ArrayList<>();
 	   }    	
    }
        
    public List<Nivel1> getListaFungiblesByServicio(Servicio servicio)
    {
    	try
    	{
    		return this.getEntityManager().createNamedQuery(Nivel1.GET_FUNGIBLES_BY_SERVICIO).setParameter(SERVICIO, servicio ).getResultList();
    	}
    	catch(Exception ex)
    	{
  		   log.error("Error en getListaFungiblesByServicio: #0",ex.toString());
  		   return new ArrayList<>();
    	}
    }
    
    private boolean hasName(String name) {
        return (name != null) && (!"".equals(name.trim()));
    }
   
	@Override
	public void crear( Nivel1 fungible ) throws SaiException {
		try {
			this.persist( fungible, true );
		} catch ( Exception e ) {
			log.error( "No se ha podido crear el fungible:", e );
			throw new SaiException(e.getMessage());
		}
	}

	@Override
	public void modificar( Nivel1 fungible ) throws SaiException {
		try {
			this.update( fungible, true );
		} catch ( Exception e ) {
			log.error( "No se ha podido modificar el fungible:", e );
			throw new SaiException(e.getMessage());
		}
	}

	@Override
	public void eliminar( Nivel1 fungible ) throws SaiException {
		try {
			this.delete( fungible, true );
		} catch ( Exception e ) {

			log.error( "No se ha podido eliminar el fungible:", e );
			throw new SaiException(e.getMessage());
		}
	}

}
