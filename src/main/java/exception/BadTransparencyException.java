package exception;

/**
 * BadTransparencyException is used when the transparency isn't between 0 and 100
 */
public class BadTransparencyException extends Exception {
    /**
     * Constructor method for BadSizeException.
     * @param str message to display if the exception is thrown
     */
    public BadTransparencyException(String str) {
        super(str);
    }
}
