package cz.cvut.fel.omo.entity.device;

import cz.cvut.fel.omo.entity.alive.Animal;
import cz.cvut.fel.omo.event.Event;
import cz.cvut.fel.omo.house.room.Room;

/**
 * The type Toaster.
 */
public class Toaster extends Device{

    private static int entityCount = 0;

    private static final double CONSUMPTION_IN_MINUTE = 1;

    /**
     * Instantiates a new Toaster.
     *
     * @param room the room
     */
    public Toaster(Room room) {
        super(room, "Toaster #" + entityCount++);
        isForAnimal = false;
    }

    @Override
    public double getDefaultConsumptionInMinute() {
        return CONSUMPTION_IN_MINUTE;
    }

    @Override
    public Event.EventBuilder accept(DeviceVisitor visitor) {
        return visitor.visitToaster(this);
    }

}
