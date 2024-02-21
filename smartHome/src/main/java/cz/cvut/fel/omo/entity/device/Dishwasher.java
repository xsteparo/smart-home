package cz.cvut.fel.omo.entity.device;

import cz.cvut.fel.omo.house.room.Room;
import cz.cvut.fel.omo.event.Event;

/**
 * The type Dishwasher.
 */
public class Dishwasher extends Device{

    private static int entityCount = 0;

    private static final double CONSUMPTION_IN_MINUTE = 15;

    /**
     * Instantiates a new Dishwasher.
     *
     * @param room the room
     */
    public Dishwasher(Room room) {
        super(room, "Dishwasher #" + entityCount++);
    }


    @Override
    public Event.EventBuilder accept(DeviceVisitor visitor) {
        return visitor.visitDishwasher(this);
    }

    @Override
    public double getDefaultConsumptionInMinute() {
        return CONSUMPTION_IN_MINUTE;
    }

}
