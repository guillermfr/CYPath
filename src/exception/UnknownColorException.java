package exception;

/**
 * UnknownColorException is used when a player has a color other than the 4 in the game (YELLOW, BLUE, RED, GREEN)
 */
public class UnknownColorException extends Exception {
    /**
     * Constructor for the class
     * @param message message to display if the exception is thrown
     */
    public UnknownColorException(String message) {
        super(message);
    }
}
