package cz.cvut.fel.omo.entity.device.sensors;

import cz.cvut.fel.omo.house.House;
import cz.cvut.fel.omo.util.logger.EventHistory;

/**
 * A sensor that detects flooding conditions within the house.
 * The sensor updates the house's status to FLOOD if flooding is detected and reverts it back to DEFAULT when the flood subsides.
 */
public class FloodSensor extends Sensor {

    boolean floodDetected = false;

    /**
     * Monitors for flood conditions and updates the house status accordingly.
     *
     * @param context The simulation context in which this method is called.
     */
    @Override
    public void doStep(int context) {
        boolean newMeasurement = House.getInstance().getStatus() == House.HomeEmergencyStatus.FLOOD;
        if (newMeasurement != floodDetected) {
            floodDetected = newMeasurement;
            EventHistory.getInstance().logSensorEvent(this, House.getInstance().getStatus());
            House.getInstance().getSmartPanel().updateStatus(floodDetected ? House.HomeEmergencyStatus.FLOOD : House.HomeEmergencyStatus.DEFAULT);
        }
    }
}