package uk.ac.ucl.model;

// Throw when a 404 page is required
public class UserErrorException extends RuntimeException {
    public UserErrorException(Exception e) {
        super(e);
    }
    public UserErrorException(String message) {
        super(message);
    }
}
