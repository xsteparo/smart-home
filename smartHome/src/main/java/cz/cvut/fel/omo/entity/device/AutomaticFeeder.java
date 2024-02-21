package cz.cvut.fel.omo.entity.device;

import cz.cvut.fel.omo.entity.alive.Animal;
import cz.cvut.fel.omo.house.room.Room;
import cz.cvut.fel.omo.event.Event;

/**
 * Represents an automatic feeder device, designed to feed animals automatically.
 */
public class AutomaticFeeder extends Device {

    // Static counter to uniquely identify each automatic feeder instance.
    private static int entityCount = 0;

    // Constant defining the energy consumption of the automatic feeder in minutes.
    private static final double CONSUMPTION_IN_MINUTE = 0.5;

    /**
     * Constructs an AutomaticFeeder in a specified room.
     *
     * @param room The room where the automatic feeder is located.
     */
    public AutomaticFeeder(Room room) {
        super(room, "AutomaticFeeder #" + entityCount++);
        isForAnimal = true; // Indicates this device is specifically for animal use.
    }

    /**
     * Provides the default energy consumption rate of the automatic feeder.
     *
     * @return The energy consumption in minutes.
     */
    @Override
    public double getDefaultConsumptionInMinute() {
        return CONSUMPTION_IN_MINUTE;
    }

    /**
     * Defines how this device is visited by various entities.
     * This method is part of the Visitor pattern implementation and is called to create
     * a specific interaction event between the visitor (entity) and this automatic feeder.
     *
     * @param visitor The visiting entity.
     * @return An EventBuilder for creating an interaction event.
     */
    @Override
    public Event.EventBuilder accept(DeviceVisitor visitor) {
        return visitor.visitAutomaticFeeder(this);
    }

    /**
     * Creates an event specific for an animal interacting with the automatic feeder.
     * This method is used when an animal entity uses this feeder.
     *
     * @param animal The animal using the feeder.
     * @return An EventBuilder for creating the feeding event.
     */
    @Override
    public Event.EventBuilder createEventForAnimal(Animal animal) {
        return Event.create()
                .isUrgent(false)
                .subject(this)
                .object(animal)
                .room(getCurrentRoom())
                .remainingTime(20)
                .name("Eating from automatic feeder " + name);
    }
}