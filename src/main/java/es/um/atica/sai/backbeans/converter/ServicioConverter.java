package es.um.atica.sai.backbeans.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

import es.um.atica.sai.das.interfaces.ServicioSaiDAS;
import es.um.atica.sai.entities.Nivel1;
import es.um.atica.sai.entities.Servicio;

@org.jboss.seam.annotations.faces.Converter(forClass = Nivel1.class)
@Name(ServicioConverter.NAME)
@BypassInterceptors
public class ServicioConverter implements Converter {

    
    public static final String NAME = "servicioConverter";
    @Override
    public Object getAsObject( FacesContext context, UIComponent component, String value ) {
        
        ServicioSaiDAS servicioDAS = (ServicioSaiDAS) Component.getInstance( ServicioSaiDAS.NAME , true);
        
        Servicio result = null;
        
        if ((value == null) || ("".equals(value.trim() ))){
            result = null;
        } else {
            result = servicioDAS.find( new Long(value) );
        }
        
        return result;
    }

    @Override
    public String getAsString( FacesContext context, UIComponent component, Object value ) {
        
        String result = "";
        if ( value != null) {
            if (value instanceof String ){
                result = (String)value;
            } else {
                result = ((Servicio) value).getCod().toString();
            }
        }
        
        return result;
    }

}
