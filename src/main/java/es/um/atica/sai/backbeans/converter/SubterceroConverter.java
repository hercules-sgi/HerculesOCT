package es.um.atica.sai.backbeans.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

import es.um.atica.sai.dtos.Subtercero;

@org.jboss.seam.annotations.faces.Converter(forClass = Subtercero.class)
@Name("subterceroConverter")
@BypassInterceptors
public class SubterceroConverter implements Converter {

    @Override
    public Object getAsObject( FacesContext context, UIComponent component, String value ) 
    {
        Subtercero result = null;
        if (value != null && !value.isEmpty())
        {
            result = new Subtercero();
            String[] datosSubtercero = value.split(" _ ");
            result.setCodigo(datosSubtercero[0]);
            result.setNombre(datosSubtercero[1] );  
        }
        return result;
    }

    @Override
    public String getAsString( FacesContext context, UIComponent component, Object value ) {
        
        String result = null;
        if ( value != null && !value.toString().isEmpty()) 
        {
            if (((Subtercero)value).getCodigo() != null && ((Subtercero)value).getNombre() != null)
            {
                result = new StringBuilder(((Subtercero)value).getCodigo()).append(" _ ").append(((Subtercero)value).getNombre()).toString();
            }
        }
        
        return result;
    }

}