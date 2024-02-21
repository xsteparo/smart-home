package cz.cvut.fel.omo.weather;

/**
 * The type Cloudy state.
 */
public class CloudyState extends BaseState{

    private static final int THREE_DAYS_IN_MINUTES = 4340;

    /**
     * Instantiates a new Cloudy state.
     */
    public CloudyState() {
        super( 8, 11, 10, 70);
    }

    @Override
    protected int getDuration() {
        return THREE_DAYS_IN_MINUTES;
    }

    @Override
    protected WeatherState nextState() {
        return new RainyState();
    }

}
