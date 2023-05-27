package exception;

/**
 * UnknownPlayerIdException is used when trying to use an id less than 0 or greater than or equal to 4.
 */
public class UnknownPlayerIdException extends Exception {
    /**
     * Constructor for the class
     * @param message message to display if the exception is thrown
     */
    public UnknownPlayerIdException(String message) {
        super(message);
    }
}
