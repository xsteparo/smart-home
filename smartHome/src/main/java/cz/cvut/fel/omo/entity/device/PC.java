package cz.cvut.fel.omo.entity.device;

import cz.cvut.fel.omo.entity.alive.AliveEntity;
import cz.cvut.fel.omo.house.room.Room;
import cz.cvut.fel.omo.event.Event;
import cz.cvut.fel.omo.util.logger.EventHistory;

import java.util.List;

/**
 * The type PC. It can call fire event then broke event or only broke event.
 */
public class PC extends BreakableDevice {

    private static int entityCount = 0;

    private static final double CONSUMPTION_IN_MINUTE = 8;

    /**
     * Instantiates a new Pc.
     *
     * @param room the room
     */
    public PC(Room room) {
        super(room,"PC #" + entityCount++);
    }

    @Override
    public Event.EventBuilder accept(DeviceVisitor visitor) {
        return visitor.visitPC(this);
    }

    @Override
    protected List<Event> getRepairingEvents(AliveEntity repairer) {
        Event disassemblePC = Event.create()
                .isUrgent(false)
                .name("disassembles the PC " + name)
                .object(repairer)
                .room(currentRoom)
                .remainingTime(20)
                .build();

        Event cleanPC = Event.create()
                .isUrgent(false)
                .name("cleans the PC internals " + name)
                .object(repairer)
                .room(currentRoom)
                .remainingTime(15)
                .build();

        Event replaceParts = Event.create()
                .isUrgent(false)
                .name("replaces PC parts" + name)
                .object(repairer)
                .room(currentRoom)
                .remainingTime(40)
                .build();

        Event reassemblePC = Event.create()
                .isUrgent(false)
                .name("reassembles the PC " + name)
                .object(repairer)
                .room(currentRoom)
                .remainingTime(30)
                .build();

        Event testPC = Event.create()
                .isUrgent(false)
                .name("tests the PC functionality " + name)
                .object(repairer)
                .room(currentRoom)
                .remainingTime(25)
                .endFunction(() -> {
                    this.status = DeviceStatus.FREE;
                    EventHistory.getInstance().logDeviceRepaired(this, repairer);
                })
                .build();

        return List.of(disassemblePC, cleanPC, replaceParts, reassemblePC, testPC);
    }

    @Override
    protected int getReadingInstructionsTime() {
        return 45;
    }

    @Override
    public double getDefaultConsumptionInMinute() {
        return CONSUMPTION_IN_MINUTE;
    }
}
