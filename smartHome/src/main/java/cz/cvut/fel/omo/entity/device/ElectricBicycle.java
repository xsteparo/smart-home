package cz.cvut.fel.omo.entity.device;

import cz.cvut.fel.omo.house.room.Room;
import cz.cvut.fel.omo.event.Event;

/**
 * The type Electric bicycle.
 */
public class ElectricBicycle extends Device{

    private static int entityCount = 0;

    private static final double CONSUMPTION_IN_MINUTE = 1;

    /**
     * Instantiates a new Electric bicycle.
     *
     * @param room the room
     */
    public ElectricBicycle(Room room) {
        super(room, "ElectricBicycle #" + entityCount++);
    }

    @Override
    public Event.EventBuilder accept(DeviceVisitor visitor) {
        return visitor.visitElectricBicycle(this);
    }

    @Override
    public double getDefaultConsumptionInMinute() {
        return CONSUMPTION_IN_MINUTE;
    }
}
