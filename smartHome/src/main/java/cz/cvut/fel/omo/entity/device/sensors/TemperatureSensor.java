package cz.cvut.fel.omo.entity.device.sensors;

import cz.cvut.fel.omo.house.House;
import cz.cvut.fel.omo.weather.Weather;

/**
 * A sensor for detecting the external temperature.
 * It updates the SmartPanel of the house with the current external temperature.
 */
public class TemperatureSensor extends Sensor {

    public TemperatureSensor() {
        super();
    }

    /**
     * Updates the SmartPanel with the current external temperature.
     *
     * @param context The simulation context in which this method is called.
     */
    @Override
    public void doStep(int context) {
        int temperature = Weather.getInstance().getTemperature();
        House.getInstance().getSmartPanel().updateTemperatureOutside(temperature);
    }
}