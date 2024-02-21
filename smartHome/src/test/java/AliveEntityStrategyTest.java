import cz.cvut.fel.omo.entity.alive.Child;
import cz.cvut.fel.omo.event.Event;
import cz.cvut.fel.omo.house.House;
import cz.cvut.fel.omo.house.room.Room;
import cz.cvut.fel.omo.timesimulator.TimeManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class AliveEntityStrategyTest {

    private Room testRoom;

    @BeforeEach
    void setUp() {
        testRoom = new Room("TestRoom");
        House.getInstance().setRooms(List.of(testRoom));
        TimeManager.getInstance().setCurrentTime(700);
        House.getInstance().setBackyard(new Room("Backyard"));
    }

    @Test
    void eventIsUrgentForFireStrategy() {
        Child entity = new Child("Test", testRoom);

        entity.updateEmergencyStatus(House.HomeEmergencyStatus.FIRE);
        entity.doStep(0);

        assertFalse(entity.getPendingEvents().isEmpty(), "Should have pending events");
        assertTrue(entity.getPendingEvents().peek().isUrgent(), "Event should be urgent for fire strategy");
    }

    @Test
    void eventInterruptsCorrectly() {
        Child child = new Child("TestChild", testRoom);
        child.updateEmergencyStatus(House.HomeEmergencyStatus.DEFAULT);

        Event event = Event.create()
                .object(child)
                .room(testRoom)
                .name("Default Activity")
                .remainingTime(30)
                .build();
        child.setCurrentEvent(event);
        child.doStep(0);

        child.updateEmergencyStatus(House.HomeEmergencyStatus.FIRE);

        assertNull(child.getCurrentEvent(), "Event should be interrupted when strategy changes to FIRE");
    }
}