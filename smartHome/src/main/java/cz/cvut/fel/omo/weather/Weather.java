package cz.cvut.fel.omo.weather;

/**
 * The type Weather.
 */
public class Weather {

    private static Weather instance;

    private WeatherState state;

    private Weather() {
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static Weather getInstance() {
        if (instance == null) {
            instance = new Weather();
            instance.setState(new CloudyState());
        }
        return instance;
    }

    /**
     * Gets temperature.
     *
     * @return the temperature
     */
    public int getTemperature() {return state.getTemperature();}

    /**
     * Gets light intensity.
     *
     * @return the light intensity
     */
    public int getLightIntensity() {
        return state.getLightIntensity();
    }

    /**
     * Sets state.
     *
     * @param state the state
     */
    public void setState(WeatherState state) {
        this.state = state;
    }

}
