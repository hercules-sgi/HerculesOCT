package es.um.atica.sai.exceptions;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class SaiException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -7763329534100201581L;
    
    private String message;


    public SaiException(String message)
    {
    	super(message);
    }

    /*
    public SaiException( ErrorCode errorCode ) {
        super( errorCode );
       
    }

    
    public SaiException( String message, ErrorCode errorCode ) {
        super( message, errorCode );
       
    }

    public SaiException( String message, Throwable cause, ErrorCode errorCode ) {
        super( message, cause, errorCode );
       
    }

    public SaiException( Throwable cause, ErrorCode errorCode ) {
        super( cause, errorCode );
        
    }
    */
    

}
