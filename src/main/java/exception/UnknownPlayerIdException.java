package exception;

/**
 * UnknownPlayerIdException is used when trying to use an id < 0 or >= 4
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
