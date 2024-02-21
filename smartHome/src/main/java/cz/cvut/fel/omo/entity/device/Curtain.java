package cz.cvut.fel.omo.entity.device;

import cz.cvut.fel.omo.house.room.Room;
import cz.cvut.fel.omo.event.Event;
import cz.cvut.fel.omo.util.logger.EventHistory;


/**
 * The type Curtain.
 */
public class Curtain extends Device {

    private static final double CONSUMPTION_IN_MINUTE = 1;

    private int rotationDegree = 180;

    private static int entityCount = 0;

    /**
     * Instantiates a new Curtain.
     *
     * @param room the room
     */
    public Curtain(Room room) {
        super(room, "Curtain #" + entityCount++);
    }

    @Override
    public Event.EventBuilder accept(DeviceVisitor visitor) {
        rotationDegree = 90;
        return visitor.visitCurtain(this);
    }

    /**
     * Gets changed degree event.
     *
     * @param newDegree the new degree
     * @param intensity the intensity
     * @return the changed degree event
     */
    public Event.EventBuilder getChangedDegreeEvent(int newDegree, int intensity) {
        if (newDegree != rotationDegree) {
            EventHistory.getInstance().logNewIntensity(this, intensity);

            return Event.create()
                    .isUrgent(false)
                    .subject(this)
                    .room(currentRoom)
                    .name("configuring curtain to " + newDegree + " degree " + name)
                    .remainingTime(10)
                    .endFunction(() -> rotationDegree = newDegree);
        }
        return null;
    }


    /**
     * Sets degree.
     *
     * @param newDegree the new degree
     */
    public void setDegree(int newDegree) {
        rotationDegree = newDegree;
    }

    @Override
    public double getDefaultConsumptionInMinute() {
        return CONSUMPTION_IN_MINUTE;
    }

}
