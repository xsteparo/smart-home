package cz.cvut.fel.omo.weather;

/**
 * The WeatherState interface represents the state of weather in the simulation.
 */
public interface WeatherState {

    /**
     * Get the current temperature.
     *
     * @return The temperature value.
     */
    int getTemperature();

    /**
     * Get the current light intensity.
     *
     * @return The light intensity value (0-100%).
     */
    int getLightIntensity();
}
