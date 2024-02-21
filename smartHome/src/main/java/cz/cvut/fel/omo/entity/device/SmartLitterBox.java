package cz.cvut.fel.omo.entity.device;

import cz.cvut.fel.omo.entity.alive.Animal;
import cz.cvut.fel.omo.event.Event;
import cz.cvut.fel.omo.house.room.Room;

/**
 * The type Smart litter box.
 */
public class SmartLitterBox extends Device{

    private static int entityCount = 0;

    private static final double CONSUMPTION_IN_MINUTE = 0.5;

    /**
     * Instantiates a new Smart litter box.
     *
     * @param room the room
     */
    public SmartLitterBox(Room room) {
        super(room, "Smart Litter Box #" + entityCount++);
        isForAnimal = true;
    }

    @Override
    public double getDefaultConsumptionInMinute() {
        return CONSUMPTION_IN_MINUTE;
    }

    @Override
    public Event.EventBuilder accept(DeviceVisitor visitor) {
        return visitor.visitLitterBox(this);
    }

    @Override
    public Event.EventBuilder createEventForAnimal(Animal animal) {
        return Event.create()
                .isUrgent(false)
                .subject(this)
                .object(animal)
                .room(getCurrentRoom())
                .remainingTime(20)
                .name("Popping in " + name);
    }
}
