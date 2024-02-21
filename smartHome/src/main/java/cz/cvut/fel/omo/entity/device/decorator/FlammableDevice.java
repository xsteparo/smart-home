package cz.cvut.fel.omo.entity.device.decorator;

import cz.cvut.fel.omo.house.House;
import cz.cvut.fel.omo.entity.device.BreakableDevice;
import cz.cvut.fel.omo.event.Event;
import cz.cvut.fel.omo.util.logger.EventHistory;

/**
 * A decorator class for a breakable device that can potentially cause a fire.
 * This class extends the functionality of a BreakableDevice to include the chance of causing a fire.
 */
public class FlammableDevice extends TroublesCausingDevice {

    /**
     * Constructs a FlammableDevice wrapping a BreakableDevice.
     *
     * @param wrappee The BreakableDevice to be decorated.
     */
    public FlammableDevice(BreakableDevice wrappee) {
        super(wrappee);
    }

    /**
     * Causes trouble by possibly starting a fire.
     * Has a small chance to trigger a fire event in the house.
     *
     * @param event The current event being processed.
     */
    @Override
    protected void causeTrouble(Event event) {
        double random = Math.random();
        if (random > 0.995) {
            EventHistory.getInstance().logTrouble("FIRE", this, event.getObject().getName());
            House.getInstance().setStatus(House.HomeEmergencyStatus.FIRE);
            wrappee.breakDown(event);
        }
    }
}