package cz.cvut.fel.omo.entity.device.sensors;


import cz.cvut.fel.omo.house.House;
import cz.cvut.fel.omo.util.logger.EventHistory;

/**
 * A sensor that detects smoke and potential fire hazards within the house.
 * The sensor updates the house's status to FIRE if smoke/fire is detected and reverts it back to DEFAULT when the fire is extinguished.
 */
public class SmokeSensor extends Sensor {
    boolean fireDetected = false;

    /**
     * Monitors for smoke/fire conditions and updates the house status accordingly.
     *
     * @param context The simulation context in which this method is called.
     */
    @Override
    public void doStep(int context) {
        boolean newMeasurement = House.getInstance().getStatus() == House.HomeEmergencyStatus.FIRE;
        if (newMeasurement != fireDetected) {
            fireDetected = newMeasurement;
            EventHistory.getInstance().logSensorEvent(this, House.getInstance().getStatus());
            House.getInstance().getSmartPanel().updateStatus(fireDetected ? House.HomeEmergencyStatus.FIRE : House.HomeEmergencyStatus.DEFAULT);
        }
    }
}