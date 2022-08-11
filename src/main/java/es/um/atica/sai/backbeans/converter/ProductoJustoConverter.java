package es.um.atica.sai.backbeans.converter;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.apache.commons.lang3.StringUtils;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

import es.um.atica.sai.dtos.ProductoJusto;


@org.jboss.seam.annotations.faces.Converter(forClass = ProductoJusto.class)
@Name("productoJustoConverter")
@BypassInterceptors
public class ProductoJustoConverter implements Converter {
    
	private static final String SEPARADOR = "_$_";
	
    @Override
    public Object getAsObject( FacesContext context, UIComponent component, String value ) 
    {
        ProductoJusto result = null;
        if (value != null && !value.isEmpty() && value.contains(SEPARADOR))
        {
            result = new ProductoJusto();
            String[] datosProductoJusto = StringUtils.split(value, SEPARADOR);
            result.setUnidadAdministrativa(datosProductoJusto[0]);
            result.setCodProductoJusto(datosProductoJusto[1]);
            result.setDescripcion(datosProductoJusto[2]);
        }
        return result;
    }

    @Override
    public String getAsString( FacesContext context, UIComponent component, Object value ) 
    {
        String result = null;
        if ( value != null && !value.toString().isEmpty() && ((ProductoJusto)value).getUnidadAdministrativa() != null && ((ProductoJusto)value).getCodProductoJusto() != null) 
        {
              result = new StringBuilder(((ProductoJusto)value).getUnidadAdministrativa()).append(SEPARADOR)
                				   .append(((ProductoJusto)value).getCodProductoJusto()).append(SEPARADOR)
                				   .append(((ProductoJusto)value).getDescripcion()).append(SEPARADOR).toString();
        }
        return result;
    }

}