package cz.cvut.fel.omo.weather;

/**
 * The type Sunny state.
 */
public class SunnyState extends BaseState {

    private static final int ONE_WEEK_IN_MINUTES = 10080;

    /**
     * Instantiates a new Sunny state.
     */
    public SunnyState() {
        super(20, 15, 25,84);
    }

    @Override
    protected int getDuration() {
        return ONE_WEEK_IN_MINUTES;
    }

    @Override
    protected WeatherState nextState() {
        return new CloudyState();
    }

}
