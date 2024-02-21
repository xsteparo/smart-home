import cz.cvut.fel.omo.entity.alive.Child;
import cz.cvut.fel.omo.entity.device.Device;
import cz.cvut.fel.omo.entity.device.Toilet;
import cz.cvut.fel.omo.event.Event;
import cz.cvut.fel.omo.house.House;
import cz.cvut.fel.omo.house.room.Room;
import cz.cvut.fel.omo.timesimulator.TimeManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EntityMovementTest {

    @BeforeEach
    void setUp() {
        Room wc = new Room("Toilet room");
        Device toilet = new Toilet(wc);
        wc.setDevices(Set.of(toilet));
        House.getInstance().setRooms(List.of( wc));
        TimeManager.getInstance().setCurrentTime(700);
    }

    @Test
    void entityChangesRoomCorrectly() {
        Room startingRoom = new Room("StartingRoom");
        Room targetRoom = new Room("TargetRoom");

        Child entity = new Child("TestChild", startingRoom);

        Event event = Event.create()
                .object(entity)
                .room(targetRoom)
                .name("Moving to TargetRoom")
                .remainingTime(1)
                .build();

        entity.scheduleEvent(event);
        entity.doStep(0);

        assertEquals(targetRoom, entity.getCurrentRoom(), "Entity should be in the target room after the event");
    }
}
