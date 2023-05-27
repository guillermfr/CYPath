package exception;

/**
 * BadBarrierEdgesException is used when the two edges given to create a barrier are not adjacent
 */
public class BadBarrierEdgesException extends Exception {
    /**
     * Constructor method for BadBarrierEdgesException.
     * @param str message to display if the exception is thrown
     */
    public BadBarrierEdgesException(String str) {
        super(str);
    }
}
