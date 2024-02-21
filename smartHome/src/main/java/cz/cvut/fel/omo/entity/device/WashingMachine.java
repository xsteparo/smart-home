package cz.cvut.fel.omo.entity.device;

import cz.cvut.fel.omo.entity.alive.AliveEntity;
import cz.cvut.fel.omo.house.room.Room;
import cz.cvut.fel.omo.event.Event;
import cz.cvut.fel.omo.util.logger.EventHistory;

import java.util.List;

/**
 * The type Washing machine. It can cause flood event then broke event.
 */
public class WashingMachine extends BreakableDevice {

    private static int entityCount = 0;

    private static final double CONSUMPTION_IN_MINUTE = 20;

    private static final int REPAIRING_TIME = 60;

    /**
     * Instantiates a new Washing machine.
     *
     * @param room the room
     */
    public WashingMachine(Room room) {
        super(room,"WashingMachine #" + entityCount++);
    }


    @Override
    public Event.EventBuilder accept(DeviceVisitor visitor) {
        return visitor.visitWashingMachine(this);
    }

    @Override
    protected List<Event> getRepairingEvents(AliveEntity repairer) {
        Event drainTheWater = Event.create()
                .isUrgent(false)
                .name("drains the water before repairing the washing machine "+ name)
                .object(repairer)
                .room(currentRoom)
                .remainingTime(10)
                .build();
        Event disassemble = Event.create()
                .isUrgent(false)
                .name("disassembling the washing machine "+ name)
                .object(repairer)
                .room(currentRoom)
                .remainingTime(30)
                .build();
        Event repairing = Event.create()
                .isUrgent(false)
                .name("fixing pipes in the washing machine "+ name)
                .object(repairer)
                .room(currentRoom)
                .remainingTime(60)
                .build();
        Event assemble = Event.create()
                .isUrgent(false)
                .name("disassembling back the washing machine "+ name)
                .object(repairer)
                .room(currentRoom)
                .remainingTime(30)
                .endFunction(() -> {
                    this.status = DeviceStatus.FREE;
                    EventHistory.getInstance().logDeviceRepaired(this, repairer);
                })
                .build();

        return List.of(drainTheWater, disassemble, repairing, assemble);
    }

    @Override
    public int getReadingInstructionsTime() {
        return REPAIRING_TIME;
    }

    @Override
    public double getDefaultConsumptionInMinute() {
        return CONSUMPTION_IN_MINUTE;
    }

}
