package es.um.atica.sai.backbeans.converter;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.apache.commons.lang3.StringUtils;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

import es.um.atica.sai.dtos.TerminalKron;


@org.jboss.seam.annotations.faces.Converter(forClass = TerminalKron.class)
@Name("terminalKronConverter")
@BypassInterceptors
public class TerminalKronConverter implements Converter {
    
	private static final String SEPARADOR = "_$_";
	
    @Override
    public Object getAsObject( FacesContext context, UIComponent component, String value ) 
    {
        TerminalKron result = null;
        if (value != null && !value.isEmpty() && value.contains(SEPARADOR))
        {
            result = new TerminalKron();
            String[] datosTerminalKron = StringUtils.split(value, SEPARADOR);
            result.setIp(datosTerminalKron[0]);
            result.setLector(datosTerminalKron[1]);
            result.setNombreEdificio(datosTerminalKron[2]);
            result.setNombreLector(datosTerminalKron[3]);
        }
        return result;
    }

    @Override
    public String getAsString( FacesContext context, UIComponent component, Object value ) 
    {
        String result = null;
        if ( value != null && !value.toString().isEmpty() && ((TerminalKron)value).getIp() != null && ((TerminalKron)value).getLector() != null) 
        {
              result = new StringBuilder(((TerminalKron)value).getIp()).append(SEPARADOR)
                				   .append(((TerminalKron)value).getLector()).append(SEPARADOR)
                				   .append(((TerminalKron)value).getNombreEdificio()).append(SEPARADOR)
                				   .append(((TerminalKron)value).getNombreLector()).append(SEPARADOR).toString();
        }
        return result;
    }

}