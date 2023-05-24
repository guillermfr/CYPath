package exception;

/**
 * BadNumberPlayersException is used when the number of players isn't equal to 2 or 4.
 */
public class BadNumberPlayersException extends Exception {
    /**
     * Constructor method for BadNumberPlayersException.
     */
    public BadNumberPlayersException() {
        super("The number of players is invalid.");
    }
}
