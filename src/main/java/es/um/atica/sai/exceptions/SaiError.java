package es.um.atica.sai.exceptions;


public enum SaiError implements ErrorCode {
    DATABASE_ERROR(550), 
    UNAUTHORIZED(401),
    FORBIDDEN (403),
    NOT_FOUND(404),
    NOT_ACCEPTABLE(406);
    
    
    private SaiError(int number){
        this.number = number;
    }

    private int number;
    
    @Override
    public int getNumber() {
        return number;
    }

}
