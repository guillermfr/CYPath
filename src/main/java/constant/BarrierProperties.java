package constant;

/**
 * Constant class for the properties of the barriers
 */
public class BarrierProperties {
    /**
     * The constructor is useless for this class because we only use it for constants
     */
    private BarrierProperties(){}

    /**
     * Constant for the size of a barrier. It is counted in GRID_GAPs meaning 2 would give a barrier as big as 2 GRID_GAPs
     */
    public final static double BARRIER_SIZE = 1.4;

    /**
     * The maximum amount of barrier players can place
     */
    public final static int BARRIER_LIMIT = 20;
}
