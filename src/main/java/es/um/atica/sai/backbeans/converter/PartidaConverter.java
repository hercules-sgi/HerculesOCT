package es.um.atica.sai.backbeans.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

import es.um.atica.sai.dtos.Partida;

@org.jboss.seam.annotations.faces.Converter(forClass = Partida.class)
@Name("partidaConverter")
@BypassInterceptors
public class PartidaConverter implements Converter {

    
    @Override
    public Object getAsObject( FacesContext context, UIComponent component, String value ) 
    {
        Partida result = null;
        if (value != null && !value.isEmpty())
        {
            result = new Partida();
            String[] datosPartida = value.split(" ");
            result.setEje(datosPartida[0]);
            result.setEjeproce(datosPartida[1]);
            result.setVic(datosPartida[2]);
            result.setUnid(datosPartida[3]);
            result.setPro(datosPartida[4]);
            result.setEco(datosPartida[5]);
            try
            {
            	result.setNumproy(datosPartida[6]);
            }
            catch (Exception ex)
            {
            	result.setNumproy( null );
            }

        }
        return result;
    }
    
    @Override
    public String getAsString( FacesContext context, UIComponent component, Object value ) {
        
        StringBuilder result = new StringBuilder("");
        if ( value != null && !value.toString().isEmpty()) 
        {
            if (((Partida)value).getEje() != null && ((Partida)value).getEjeproce() != null && ((Partida)value).getVic() != null && ((Partida)value).getUnid() != null && 
            	((Partida)value).getPro() != null && ((Partida)value).getEco() != null)
            {
                result = new StringBuilder(((Partida)value).getEje()).append(" ").append(((Partida)value).getEjeproce()).append(" ").append(((Partida)value).getVic())
                		.append(" ").append(((Partida)value).getUnid()).append(" ").append(((Partida)value).getPro()).append(" ").append(((Partida)value).getEco());
            }
            if (((Partida)value).getNumproy() != null)
            {
            	result.append(" ").append(((Partida)value).getNumproy());
            }
        }
        return result.toString();
    }

    public Partida getAsPartida(String value) 
    {
        Partida result = null;
        if (value != null && !"".equals(value))
        {
            result = new Partida();
            String[] datosPartida = value.split(" ");
            result.setEje(datosPartida[0]);
            result.setEjeproce(datosPartida[1]);
            result.setVic(datosPartida[2]);
            result.setUnid(datosPartida[3]);
            result.setPro(datosPartida[4]);
            result.setEco(datosPartida[5]);
            try
            {
            	result.setNumproy(datosPartida[6]);
            }
            catch (Exception ex)
            {
            	result.setNumproy( null );
            }
        }
        return result;
    }

}