package cz.cvut.fel.omo.entity.device.decorator;

import cz.cvut.fel.omo.house.House;
import cz.cvut.fel.omo.entity.device.BreakableDevice;
import cz.cvut.fel.omo.event.Event;
import cz.cvut.fel.omo.util.logger.EventHistory;

/**
 * A decorator class for a breakable device that can potentially cause flooding.
 * This class extends the functionality of a BreakableDevice to include the chance of causing a flood.
 */
public class FloodingDevice extends TroublesCausingDevice {

    /**
     * Constructs a FloodingDevice wrapping a BreakableDevice.
     *
     * @param wrappee The BreakableDevice to be decorated.
     */
    public FloodingDevice(BreakableDevice wrappee) {
        super(wrappee);
    }

    /**
     * Causes trouble by possibly causing flooding.
     * Has a small chance to trigger a flood event in the house.
     *
     * @param event The current event being processed.
     */
    @Override
    protected void causeTrouble(Event event) {
        double random = Math.random();
        if (random > 0.995) {
            EventHistory.getInstance().logTrouble("FLOOD", this, event.getObject().getName());
            House.getInstance().setStatus(House.HomeEmergencyStatus.FLOOD);
            wrappee.breakDown(event);
        }
    }
}
