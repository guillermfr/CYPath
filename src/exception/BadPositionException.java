package exception;

/**
 * BadPositionException is used when the x or y attributes of a Position is an invalid value.
 * For example, a negative value is considered invalid.
 */
public class BadPositionException extends Exception {
    /**
     * Constructor method for BadPositionException.
     */
    public BadPositionException() {
        super("The coordinates entered are not valid.");
    }
}
