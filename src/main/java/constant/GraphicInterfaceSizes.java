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
     * Size of a barrier counted in gridGaps.
     * It means 2 would give a barrier as big as 2 gridGaps
     */
    public final static double BARRIER_SIZE = 1.4;

    /**
     * Size of the hit box placed vertically (respectively horizontally) around a horizontal (respectively vertical) barrier.
     * It is counted in (boxSize + gridGap)s meaning 0.5 would give the player half the size of a box and half the size of a grid gap on each side of the barrier to click.
     */
    public final static double BARRIER_HIT_BOX = 0.15;

    /**
     * Set width of the screen
     */
    public final static int SCREEN_WIDTH = 1920;

    /**
     * Set height of the screen
     */
    public final static int SCREEN_HEIGHT = 1080;
}
