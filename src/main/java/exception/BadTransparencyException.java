package exception;

/**
 * BadTransparencyException is used when the transparency isn't between 0 and 100
 */
public class BadTransparencyException extends Exception {
    /**
     * Constructor method for BadSizeException.
     */
    public BadTransparencyException(String str) {
        super(str);
    }
}
