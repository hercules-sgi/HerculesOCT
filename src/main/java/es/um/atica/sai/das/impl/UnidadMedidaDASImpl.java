package es.um.atica.sai.das.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.jboss.seam.annotations.Name;

import es.um.atica.jpa.das.DataAccessServiceImpl;
import es.um.atica.sai.das.interfaces.UnidadMedidaDAS;
import es.um.atica.sai.entities.UnidadMedida;

@Name(UnidadMedidaDAS.NAME)
@Stateless
@Local(UnidadMedidaDAS.class)
public class UnidadMedidaDASImpl extends  DataAccessServiceImpl<UnidadMedida> implements UnidadMedidaDAS{
    
   

    @Override
    public List<UnidadMedida> list() {
        return findByQuery("select e from UnidadMedida e order by e.descripcion asc ", null, null, null, null);    }

    @Override
    public void guardar(UnidadMedida um) {
        try{
            if(um.getCod() == null){
                super.persist( um, true);
            }else{
                super.merge( um, true );
                super.refresh( um );
                
            }
            log.info( "Unidad guardada correctamente cod=#0", um.getCod() );
         
        }catch(Exception e){
           
            log.error( "Error inesperado. No se puede guardar la unidad de medida. Error:", e );
        }
    
    }
    
    @Override
    public void eliminar( UnidadMedida um ) {
        if(um.getProductos() != null && um.getProductos().isEmpty()){
            try{
                super.delete( um, true);
               
                log.info( "Unidad de medida eliminada" );
            }catch(Exception e){
               
                log.error( "Error inesperado. No se puede eliminar la unidad de medida. Error", e );
            }
            
        }else{
            
            log.error( "No se puede eliminar la unidad de medida porque tiene productos asociados" );
            
        }
    }
    
}
