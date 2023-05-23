package exception;

/**
 * BadSizeException is used when the size of the graph is an invalid value.
 * A value is considered invalid if it is negative.
 */
public class BadSizeException extends Exception {
    /**
     * Constructor method for BadSizeException.
     */
    public BadSizeException() {
        super("The size of the board must be strictly greater than 0.");
    }
}
