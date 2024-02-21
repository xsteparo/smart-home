package cz.cvut.fel.omo.entity.device;

import cz.cvut.fel.omo.house.room.Room;
import cz.cvut.fel.omo.event.Event;

/**
 * The type Toilet.
 */
public class Toilet extends Device{

    private static int entityCount = 0;

    private static final double CONSUMPTION_IN_MINUTE = 2;

    /**
     * Instantiates a new Toilet.
     *
     * @param room the room
     */
    public Toilet(Room room) {
        super(room,"Toilet #" + entityCount++);

    }

    @Override
    public Event.EventBuilder accept(DeviceVisitor visitor) {
        return visitor.visitToilet(this);
    }

    @Override
    public double getDefaultConsumptionInMinute() {
        return CONSUMPTION_IN_MINUTE;
    }
}
