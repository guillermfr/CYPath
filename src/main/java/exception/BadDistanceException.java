package exception;

/**
 * BadDistanceException is used when a checking method checks for a distance lesser than 1
 */
public class BadDistanceException extends Exception {
    /**
     * Constructor for the class
     * @param message message to display if the exception is thrown
     */
    public BadDistanceException(String message) {
        super(message);
    }
}
