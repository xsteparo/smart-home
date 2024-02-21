package cz.cvut.fel.omo.entity.device.sensors;

import cz.cvut.fel.omo.house.House;
import cz.cvut.fel.omo.weather.Weather;

/**
 * A sensor for detecting the intensity of light.
 * It updates the SmartPanel of the house with the current light intensity level.
 */
public class LightSensor extends Sensor {

    public LightSensor() {
        super();
    }

    /**
     * Updates the SmartPanel with the current light intensity level.
     *
     * @param context The simulation context in which this method is called.
     */
    @Override
    public void doStep(int context) {
        int intensity = Weather.getInstance().getLightIntensity();
        House.getInstance().getSmartPanel().updateLight(intensity);
    }
}
