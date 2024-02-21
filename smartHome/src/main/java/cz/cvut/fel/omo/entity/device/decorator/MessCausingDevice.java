package cz.cvut.fel.omo.entity.device.decorator;

import cz.cvut.fel.omo.entity.device.BreakableDevice;
import cz.cvut.fel.omo.event.Event;
import cz.cvut.fel.omo.util.logger.EventHistory;

/**
 * A decorator class for a device that can potentially cause messiness in a room.
 * This class extends the functionality of a device to include the chance of causing mess.
 */
public class MessCausingDevice extends TroublesCausingDevice {

    /**
     * Constructs a MessCausingDevice wrapping a BreakableDevice.
     *
     * @param wrappee The BreakableDevice to be decorated.
     */
    public MessCausingDevice(BreakableDevice wrappee) {
        super(wrappee);
    }

    /**
     * Causes trouble by potentially making the room messy.
     * Has a small chance to set the current room's status to messy.
     *
     * @param event The current event being processed.
     */
    @Override
    protected void causeTrouble(Event event) {
        double random = Math.random();
        if (random > 0.995) {
            EventHistory.getInstance().logTrouble("MESSY", this, event.getObject().getName());
            currentRoom.setMessy(true);
        }
    }
}
