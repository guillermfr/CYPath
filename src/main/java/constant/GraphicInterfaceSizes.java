package constant;

/**
 * Constant class for different sizes used in the graphic interface.
 */
public class GraphicInterfaceSizes {
    /**
     * The constructor is useless for this class because we only use it for constants
     */
    private GraphicInterfaceSizes(){}

    /**
     * Constant for the gap between each boxes
     */
    public final static int GRID_GAP = 5;

    /**
     * Constant for the size of a barrier. It is counted in GRID_GAPs meaning 2 would give a barrier as big as 2 GRID_GAPs
     */
    public final static double BARRIER_SIZE = 1.4;

    /**
     * Set width of the screen
     */
    public final static int SCREEN_WIDTH = 1920;

    /**
     * Set height of the screen
     */
    public final static int SCREEN_HEIGHT = 1080;
}
