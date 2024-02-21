package cz.cvut.fel.omo.entity.device;

import cz.cvut.fel.omo.entity.alive.Animal;
import cz.cvut.fel.omo.event.Event;
import cz.cvut.fel.omo.house.room.Room;

/**
 * The type Coffee machine.
 */
public class CoffeeMachine extends Device{

    private static int entityCount = 0;

    private static final double CONSUMPTION_IN_MINUTE = 1;

    /**
     * Instantiates a new Coffee machine.
     *
     * @param room the room
     */
    public CoffeeMachine(Room room) {
        super(room, "CoffeeMachine #" + entityCount++);
        isForAnimal = false;
    }

    @Override
    public double getDefaultConsumptionInMinute() {
        return CONSUMPTION_IN_MINUTE;
    }

    @Override
    public Event.EventBuilder accept(DeviceVisitor visitor) {
        return visitor.visitCoffeeMachine(this);
    }

}
