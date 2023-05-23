package exception;

/**
 * BadWeightException is used when the weight of an Edge is an invalid value.
 * For example, a negative weight is considered invalid.
 */
public class BadWeightException extends Exception {
    /**
     * Constructor method for BadWeightException.
     */
    public BadWeightException() {
        super("The weight value is invalid.");
    }
}
