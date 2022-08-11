package es.um.atica.sai.utils;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

public class Utilidades {

	private static final String KEY = "1234567890abcdefghyjklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	private static Random r = new Random();

	public static float roundNum(float num) throws Exception{
		int redondeo=100;
		if (redondeo==1) {
			redondeo=10;
		}
		else if (redondeo==3) {
			redondeo=1000;
		}

		float valor = 0;
		valor = num;

		valor = valor*redondeo;
		valor = java.lang.Math.round(valor);
		valor = valor/redondeo;

		return valor;
	}

	public static boolean esCadenaVacia(String str) {
		return (str == null) || (str.trim().length() <= 0);
	}

	public static String strToStringNoNula(String str) {
		return esCadenaVacia(str) ? "" : str;
	}

	public static String cmpTextFilterEjbqlCompletoIgnoraTildes(String field, String value) {
		field = strToStringNoNula(field);
		value = strToStringNoNula(value);
		return new StringBuilder("lower(translate(").append(field).append(",'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ','aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC')) like concat('%', concat(lower(translate(").append(value).append(",'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ','aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC')),'%'))").toString();
	}

	public static String obtenerStringAleatorio( int length ) {
		final StringBuilder pswd = new StringBuilder("");
		for ( int i = 0; i < length; i++ )
		{
			pswd.append( KEY.charAt( r.nextInt( KEY.length() ) ) );
		}
		return pswd.toString();
	}

	public static String getStringCifrado(String textoOrigen)
	{
		MessageDigest md;
		try
		{
			md = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
		}
		catch ( final NoSuchAlgorithmException e )
		{
			return null;
		}
		md.update(textoOrigen.getBytes());
		final byte[] digest = md.digest();
		// Se escribe codificado base 64.
		final byte[] encoded = Base64.encodeBase64(digest);
		return new String(encoded);
	}


	public static void mostrarFichero(byte[] doc, String nomDocumento, String tipoFichero) throws IOException
	{
		final int longitudDoc = doc.length;
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		final ExternalContext externalContext = facesContext.getExternalContext();
		final HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
		response.setContentLength(longitudDoc);
		if ("HTML".equals(tipoFichero))
		{
			response.setHeader("Content-type", "text/html");
		}
		else if ("EXCEL".equals(tipoFichero))
		{
			response.setHeader("Content-type", "application/vnd.ms-excel");
		}
		response.setHeader("Content-disposition", new StringBuilder("inline; filename=\"").append(nomDocumento).append("\"").toString());

		try (BufferedOutputStream output = new BufferedOutputStream(response.getOutputStream(), longitudDoc);)
		{
			// Write file contents to response.
			output.write(doc, 0, longitudDoc);
			// Finalize task.
			output.flush();
		}
		catch (final Exception ex)
		{
			// No hay nada que hacer
		}

		// Inform JSF that it doesn"t need to handle response.
		// This is very important, otherwise you will get the following exception in the logs:
		// java.lang.IllegalStateException: Cannot forward after response has been committed.
		facesContext.responseComplete();
	}

	public static String formatCantidad(BigDecimal number)
	{
		if ( number != null )
		{
			Long numberlong;
			try
			{
				numberlong = number.longValueExact();
			}
			catch(final Exception ex)
			{
				// Si tiene decimales lo redondeamos a dos
				return number.setScale( 2, RoundingMode.HALF_UP ).toString();
			}
			return numberlong.toString();
		}
		return null;
	}

	public static String formatCantidadDosDecimales( BigDecimal number ) {
		if ( number != null ) {
			Long numberlong;
			try {
				numberlong = number.longValueExact();
			} catch ( final Exception ex ) {
				// Si tiene decimales lo redondeamos a dos
				return number.setScale( 2, RoundingMode.HALF_UP ).toString();
			}
			return new StringBuilder(numberlong.toString()).append(".00").toString();
		}
		return null;
	}
	
    /**
     * Devuelve la diferencia entre dos fechas
     * 
     * @param formato
     *            - "S": Devuelve la diferencia en segundos
     *            - "M": Devuelve la diferencia en minutos
     *            - "H": Devuelve la diferencia en horas
     *            - "D": Devuelve la diferencia en dias 
     *            -  Por defecto "H"
     * @return long - resultado
     */
    public static long diferenciaFechas(Date fechaInicio, Date fechaFin, String formato)
    {
        long milisInicio;
        long milisFin;
        long diferencia;

        //Instancia del calendario gregoriano
        Calendar cInicio = Calendar.getInstance();
        Calendar cFin = Calendar.getInstance();

        //Establecemos fecha de tipo Calendar
        cInicio.setTime(fechaInicio);
        cFin.setTime(fechaFin);

        // Obtenemos la diferencia en milisegundos
        milisInicio = cInicio.getTimeInMillis();
        milisFin = cFin.getTimeInMillis();
        diferencia = milisFin-milisInicio;

        // Diferencia en segundos
        long difSegundos =  Math.abs (diferencia / 1000);
        // Diferencia en minutos
        long difMinutos =  Math.abs (diferencia / (60 * 1000));
        // Diferencia en horas
        long difHoras =   (diferencia / (60 * 60 * 1000));
        // Diferencia en dias
        long difDias = Math.abs (diferencia / (24 * 60 * 60 * 1000) );

        switch (formato)
        {
            case "S":
                return difSegundos;
            case "M":
                return difMinutos;
            case "H":
                return difHoras;
            case "D":
                return difDias;
            default:
                return difHoras;
        }
    }

}
