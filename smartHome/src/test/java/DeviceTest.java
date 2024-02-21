import cz.cvut.fel.omo.entity.alive.Adult;
import cz.cvut.fel.omo.entity.alive.Child;
import cz.cvut.fel.omo.entity.device.*;
import cz.cvut.fel.omo.house.House;
import cz.cvut.fel.omo.house.room.Room;
import cz.cvut.fel.omo.event.Event;
import cz.cvut.fel.omo.timesimulator.TimeManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DeviceTest {

    private Room office;

    @BeforeEach
    void setUp() {
        office = new Room("office");
        Room wc = new Room("Toilet room");
        Device toilet = new Toilet(wc);
        wc.setDevices(Set.of(toilet));
        House.getInstance().setRooms(List.of(office, wc));
        TimeManager.getInstance().setCurrentTime(700);
    }

    @Test
    void ensureDeviceIsNotUsedByMultipleEntitiesSimultaneously() {
        Device pc = new PC(office);
        Child vasya = new Child("Vasya", office);
        Child kolya = new Child("Kolya", office);

        Event vasyaUsesPC = Event.create().subject(pc).object(vasya).room(office).name("works on PC").remainingTime(30).build();
        vasya.scheduleEvent(vasyaUsesPC);
        vasya.doStep(0);

        Event kolyaUsesPC = Event.create().subject(pc).object(kolya).room(office).name("works on PC").remainingTime(30).build();
        kolya.scheduleEvent(kolyaUsesPC);
        kolya.doStep(0);

        Event kolyaCurrentEvent = kolya.getCurrentEvent();
        if (kolyaCurrentEvent != null) {
            assertNotEquals(pc, kolyaCurrentEvent.getSubject(), "Kolya should not be using the same PC as Vasya");
        } else {
            assertNull(kolyaCurrentEvent, "Kolya should not have a current event using the PC");
        }
    }

    @Test
    void deviceStatusUpdatesCorrectly() {
        Room room = new Room("TestRoom");
        Device device = new PC(room);
        Child child = new Child("TestChild", room);

        assertEquals(DeviceStatus.FREE, device.getStatus(), "Device should initially be free");

        Event event = Event.create()
                .subject(device)
                .object(child)
                .room(room)
                .name("Using PC")
                .remainingTime(30)
                .build();

        child.scheduleEvent(event);
        child.doStep(0);

        assertEquals(DeviceStatus.BUSY, device.getStatus(), "Device should be busy while being used");
    }

    @Test
    void testDeviceBreakdown() {
        Room room = new Room("TestRoom");
        BreakableDevice device = new PC(room);
        Adult adult = new Adult("TestAdult", room);

        Event event = Event.create()
                .subject(device)
                .object(adult)
                .name("Test Event")
                .remainingTime(30)
                .room(room)
                .build();
        adult.scheduleEvent(event);

        device.breakDown(event);

        assertEquals(DeviceStatus.BROKEN, device.getStatus(), "Device status should be BROKEN after breakdown");

        assertFalse(adult.getPendingEvents().isEmpty(), "Repair events should be generated for the broken device");

        assertNull(device.getCurrentEvent(), "Device should not have a current event after breakdown");
    }

    @Test
    void testRepairEventsGenerationAfterBreakdown() {
        Room room = new Room("TestRoom");
        BreakableDevice device = new PC(room);
        Adult repairer = new Adult("Repairer", room);


        Event initialEvent = Event.create()
                .subject(device)
                .object(repairer)
                .name("Test Event")
                .remainingTime(30)
                .room(room)
                .build();
        repairer.scheduleEvent(initialEvent);

        device.breakDown(initialEvent);

        assertEquals(DeviceStatus.BROKEN, device.getStatus(), "Device should be in BROKEN status after breakdown");

        List<Event> repairEvents = repairer.getPendingEvents();
        assertFalse(repairEvents.isEmpty(), "Repair events should be generated after device breakdown");

        boolean hasReadingInstructionsEvent = repairEvents.stream()
                .anyMatch(e -> e.getName().contains("Reading instructions"));
        boolean hasActualRepairEvent = repairEvents.stream()
                .anyMatch(e -> e.getName().contains("repair") || e.getName().contains("fix"));

        assertTrue(hasReadingInstructionsEvent, "Repair events should include reading instructions");
        assertTrue(hasActualRepairEvent, "Repair events should include actual repair actions");
    }




}