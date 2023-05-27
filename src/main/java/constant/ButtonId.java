package constant;

/**
 * Constant class for the buttons.
 * Each constant corresponds to a button in the graphic interface.
 */
public final class ButtonId {

    /**
     * The constructor is useless for this class because we only use it for button constants.
     * So the constructor is private because we don't want this class to be instantiated.
     */
    private ButtonId(){}

    /**
     * Constant for the "New Game" button.
     */
    public static final String NEW_GAME = "buttonNewGame";

    /**
     * Constant for the "Continue" button.
     */
    public static final String CONTINUE = "buttonContinue";

    /**
     * Constant for the "Two Players" button in the "New Game" scene.
     */
    public static final String TWO_PLAYERS = "twoPlayers";

    /**
     * Constant for the "Four Players" button in the "New Game" scene.
     */
    public static final String FOUR_PLAYERS = "fourPlayers";
}
