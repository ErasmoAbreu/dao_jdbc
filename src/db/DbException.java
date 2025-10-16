package db;

public class DbException extends RuntimeException{
    private final long serialVersionUID=1L;
    public DbException(String message){
        super(message);
    }
}
