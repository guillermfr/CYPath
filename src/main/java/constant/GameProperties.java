package constant;

/**
 * Constant class for the properties of various game objects
 */
public class GameProperties {
    /**
     * The constructor is useless for this class because we only use it for constants
     */
    private GameProperties(){}

    /**
     * The size of the board
     */
    public final static int BOARD_SIZE = 9;

    /**
     * The maximum amount of barrier players can place
     */
    public final static int BARRIER_LIMIT = 20;

    /**
     * Path of the save files
     */
    public final static String SAVE_PATH = System.getProperty("user.dir") + "/Data/";
}
