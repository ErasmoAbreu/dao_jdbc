package db;

public class DbIntegrityException extends RuntimeException{
    private final long serialVersionUID=1L;
    public DbIntegrityException(String message){
        super(message);
    }
}
