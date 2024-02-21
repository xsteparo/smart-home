package cz.cvut.fel.omo.entity.device;

import cz.cvut.fel.omo.house.room.Room;
import cz.cvut.fel.omo.event.Event;

/**
 * The type Car.
 */
public class Car extends Device{

    private static int entityCount = 0;

    private static final double CONSUMPTION_IN_MINUTE = 18;

    /**
     * Instantiates a new Car.
     *
     * @param room the room
     */
    public Car(Room room) {
        super(room,"Car #" + entityCount++);
    }

    @Override
    public Event.EventBuilder accept(DeviceVisitor visitor) {
        return visitor.visitCar(this);
    }

    @Override
    public double getDefaultConsumptionInMinute() {
        return CONSUMPTION_IN_MINUTE;
    }

}
