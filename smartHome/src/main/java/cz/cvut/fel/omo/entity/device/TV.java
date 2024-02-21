package cz.cvut.fel.omo.entity.device;

import cz.cvut.fel.omo.entity.alive.Animal;
import cz.cvut.fel.omo.event.Event;
import cz.cvut.fel.omo.house.room.Room;

/**
 * The type Tv.
 */
public class TV extends Device {

    private static int entityCount = 0;

    private static final double CONSUMPTION_IN_MINUTE = 5;

    /**
     * Instantiates a new Tv.
     *
     * @param room the room
     */
    public TV(Room room) {
        super(room, "TV #" + entityCount++);
        isForAnimal = false;
    }

    @Override
    public double getDefaultConsumptionInMinute() {
        return CONSUMPTION_IN_MINUTE;
    }


    @Override
    public Event.EventBuilder accept(DeviceVisitor visitor) {
        return visitor.visitTV(this);
    }

}
