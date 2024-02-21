package cz.cvut.fel.omo.entity.device;

import cz.cvut.fel.omo.entity.alive.AliveEntity;
import cz.cvut.fel.omo.house.room.Room;
import cz.cvut.fel.omo.event.Event;
import cz.cvut.fel.omo.util.logger.EventHistory;

import java.util.List;

/**
 * The type Microwave. It can call fire event then broke event or only broke event.
 */
public class Microwave extends BreakableDevice {

    private static int entityCount = 0;

    private static final double CONSUMPTION_IN_MINUTE = 4;

    private static final int REPAIRING_TIME = 30;

    /**
     * Instantiates a new Microwave.
     *
     * @param room the room
     */
    public Microwave(Room room) {
        super(room, "Microwave #" + entityCount++);
        canBeChosenForRandomInteraction = false;
    }

    @Override
    public Event.EventBuilder accept(DeviceVisitor visitor) {
        return visitor.visitMicrowave(this);
    }

    @Override
    protected List<Event> getRepairingEvents(AliveEntity repairer) {
        Event defrostFridge = Event.create()
                .isUrgent(false)
                .name("cleaning dirty microwave " + name)
                .object(repairer)
                .room(currentRoom)
                .remainingTime(30)
                .build();

        Event replaceParts = Event.create()
                .isUrgent(false)
                .name("disassembling microwave " + name)
                .object(repairer)
                .room(currentRoom)
                .remainingTime(40)
                .build();

        Event cleanFridge = Event.create()
                .isUrgent(false)
                .name("replaces parts in microwave " + name)
                .object(repairer)
                .room(currentRoom)
                .remainingTime(15)
                .build();

        Event testFridge = Event.create()
                .isUrgent(false)
                .name("tests that microwave is repaired " + name)
                .object(repairer)
                .room(currentRoom)
                .remainingTime(20)
                .endFunction(() -> {
                    this.status = DeviceStatus.FREE;
                    EventHistory.getInstance().logDeviceRepaired(this, repairer);
                })
                .build();

        return List.of(defrostFridge, replaceParts, cleanFridge, testFridge);
    }

    @Override
    protected int getReadingInstructionsTime() {
        return REPAIRING_TIME;
    }

    @Override
    public double getDefaultConsumptionInMinute() {
        return CONSUMPTION_IN_MINUTE;
    }
}
