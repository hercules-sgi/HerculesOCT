package es.um.atica.sai.backbeans.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

import es.um.atica.sai.entities.DatosUsuarioView;

@org.jboss.seam.annotations.faces.Converter(
    forClass = DatosUsuarioView.class )
@Name( DatosUsuarioViewConverter.NAME )
@BypassInterceptors
public class DatosUsuarioViewConverter implements Converter {

    public static final String NAME = "datosUsuarioViewConverter";

    @Override
    public Object getAsObject( FacesContext context, UIComponent component, String value ) {

        DatosUsuarioView result = null;

        if ( ( value == null ) || ( "".equals( value.trim() ) ) ) {
            result = null;
        }
        else {
          
            result = new DatosUsuarioView();
            result.setCod( new Long(value) );
        }

        return result;
    }

    @Override
    public String getAsString( FacesContext context, UIComponent component, Object value ) {
        String result = "";
        if ( value != null ) {
            if ( value instanceof String ) {
                result = ( String ) value;
            }
            else {
                result = ( ( DatosUsuarioView ) value ).getCod().toString();
            }
        }

        return result;

    }

}
