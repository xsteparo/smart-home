package cz.cvut.fel.omo.weather;

import cz.cvut.fel.omo.util.logger.EventHistory;
import cz.cvut.fel.omo.timesimulator.TimeManager;

/**
 * The BaseState class is an abstract class that serves as the base for weather states.
 */
public abstract class BaseState implements WeatherState {

    protected int minTemp;

    protected int rangeTemp;

    protected int minLight;

    protected int rangeLight;

    protected int startTime;

    protected int duration;

    protected Weather context = Weather.getInstance();

    /**
     * Initializes a new BaseState with the specified temperature and light parameters.
     *
     * @param minTemp    The minimum temperature.
     * @param rangeTemp  The temperature range.
     * @param minLight   The minimum light intensity.
     * @param rangeLight The light intensity range.
     */
    public BaseState(int minTemp, int rangeTemp, int minLight, int rangeLight) {
        this.minTemp = minTemp;
        this.rangeTemp = rangeTemp;
        this.minLight = minLight;
        this.rangeLight = rangeLight;
        startTime = TimeManager.getInstance().getCurrentTime();
        duration = getDuration();

    }

    @Override
    public int getTemperature() {
        return calculate(true);
    }

    @Override
    public int getLightIntensity() {
        return calculate(false);
    }

    /**
     * Calculates the current temperature or light intensity based on time and randomness.
     *
     * @param temp If true, calculates temperature; otherwise, calculates light intensity.
     * @return The calculated value.
     */
    protected int calculate(boolean temp) {
        if (finished()) {
            context.setState(nextState());
            EventHistory.getInstance().logWeather(this);
        }
        int min = temp ? minTemp : minLight;
        int range = temp ? rangeTemp : rangeLight;
        int currentTime = TimeManager.getInstance().getDayTimeInMinutes();
        int timeSinceFourAM = currentTime < 240 ? (1440 + currentTime - 240) : (currentTime - 240);

        double changeFactor;
        if (timeSinceFourAM <= 720) {
            changeFactor = (double) timeSinceFourAM / 720;
        } else {
            changeFactor = (double) (1440 - timeSinceFourAM) / 720;
        }

        int current = min + (int) (changeFactor * range);

        int randomVariance = (int) (Math.random() * 3) - 1; // -1, 0 nebo 1

        return current + randomVariance;
    }

    /**
     * Checks if the state duration has finished.
     *
     * @return True if the duration has finished; otherwise, false.
     */
    protected boolean finished() {
        return TimeManager.getInstance().getCurrentTime() - startTime > duration;
    }

    /**
     * Gets the duration of the current state.
     *
     * @return The duration value.
     */
    protected abstract int getDuration();

    /**
     * Gets the next weather state to transition to.
     *
     * @return The next weather state.
     */
    protected abstract WeatherState nextState();

}
