package mff.java.exceptions;

public class DatabaseCommunicationException extends RuntimeException {
    /**
     * error message for repostiory exceptions
     */
    private static final String dbErrorMessage = "Error while communicating with the database.";

    public DatabaseCommunicationException() {
        super(dbErrorMessage);
    }
}
