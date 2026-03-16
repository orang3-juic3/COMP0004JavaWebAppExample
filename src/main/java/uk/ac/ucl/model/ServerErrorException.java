package uk.ac.ucl.model;

public class ServerErrorException extends RuntimeException {
    public ServerErrorException(Exception e) {
        super(e);
    }
}
