package cz.cvut.fel.omo.weather;

/**
 * The type Rainy state.
 */
public class RainyState extends BaseState {

    private static final int TWO_DAYS_IN_MINUTES = 2880;

    /**
     * Instantiates a new Rainy state.
     */
    public RainyState() {
        super(7, 3,10 , 60);
    }

    @Override
    protected int getDuration() {
        return TWO_DAYS_IN_MINUTES;
    }

    @Override
    protected WeatherState nextState() {
        return new SunnyState();
    }

}
