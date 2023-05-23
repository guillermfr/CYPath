package exception;

/**
 * UnknownDirectionException is used when trying to use a direction other than NORTH, EAST, SOUTH, WEST
 */
public class UnknownDirectionException extends Exception {
    /**
     * Constructor for the class
     * @param message message to display if the exception is thrown
     */
    public UnknownDirectionException(String message) {
        super(message);
    }
}
