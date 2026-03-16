package uk.ac.ucl.model;

// A general unchecked exception thrown when the server cannot process a query due to malformed input
public class UserErrorException extends RuntimeException {
    public UserErrorException(Exception e) {
        super(e);
    }
    public UserErrorException(String message) {
        super(message);
    }
}
