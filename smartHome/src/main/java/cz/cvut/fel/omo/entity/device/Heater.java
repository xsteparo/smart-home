package cz.cvut.fel.omo.entity.device;

import cz.cvut.fel.omo.house.room.Room;
import cz.cvut.fel.omo.event.Event;
import cz.cvut.fel.omo.util.logger.EventHistory;

/**
 * The type Heater.
 */
public class Heater extends Device {

    private static int entityCount = 0;

    private static final double CONSUMPTION_IN_MINUTE = 10;

    private int currentTemperature;

    /**
     * Instantiates a new Heater.
     *
     * @param room the room
     */
    public Heater(Room room) {
        super(room, "Heater #" + entityCount++);
        canBeChosenForRandomInteraction = false;
    }

    /**
     * Gets changed degree event.
     *
     * @param newTemperature     the new temperature
     * @param outsideTemperature the outside temperature
     * @return the changed degree event
     */
    public Event.EventBuilder getChangedDegreeEvent(int newTemperature,int outsideTemperature) {
        if (newTemperature != currentTemperature) {
            EventHistory.getInstance().logNewTemperature(outsideTemperature,this);
            return Event.create()
                    .isUrgent(false)
                    .subject(this)
                    .room(currentRoom)
                    .name("Heating to " + newTemperature + " celsius degrees " )
                    .remainingTime(10)
                    .endFunction(() -> currentTemperature = newTemperature);
        }
        return null;
    }

    @Override
    public double getDefaultConsumptionInMinute() {
        return CONSUMPTION_IN_MINUTE;
    }

    @Override
    public Event.EventBuilder accept(DeviceVisitor visitor) {
        return null;
    }
}
