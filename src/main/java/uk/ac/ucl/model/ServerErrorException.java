package uk.ac.ucl.model;

// A general unchecked exception thrown when the server cannot process a query, and it is not due to malformed input
public class ServerErrorException extends RuntimeException {
    public ServerErrorException(Exception e) {
        super(e);
    }
}
