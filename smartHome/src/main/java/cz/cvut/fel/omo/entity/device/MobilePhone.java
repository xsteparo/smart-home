package cz.cvut.fel.omo.entity.device;

import cz.cvut.fel.omo.entity.alive.HumanEntity;
import cz.cvut.fel.omo.house.room.Room;
import cz.cvut.fel.omo.event.Event;

/**
 * The type Mobile phone.
 */
public class MobilePhone extends Device {

    private static int entityCount = 0;

    private static final double CONSUMPTION_IN_MINUTE = 3;

    private final HumanEntity owner;

    /**
     * Instantiates a new Mobile phone.
     *
     * @param owner the owner
     */
    public MobilePhone(HumanEntity owner) {
        super(null,"MobilePhone #" + entityCount++);
        this.owner = owner;
    }

    @Override
    public double getDefaultConsumptionInMinute() {
        return CONSUMPTION_IN_MINUTE;
    }

    @Override
    public Event.EventBuilder accept(DeviceVisitor visitor) {
        return visitor.visitMobilePhone(this);
    }

    @Override
    public Room getCurrentRoom() {
        return owner.getCurrentRoom();
    }
}
