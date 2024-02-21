import cz.cvut.fel.omo.entity.alive.Child;
import cz.cvut.fel.omo.entity.device.Device;
import cz.cvut.fel.omo.entity.device.Fridge;
import cz.cvut.fel.omo.entity.device.PC;
import cz.cvut.fel.omo.entity.device.Toilet;
import cz.cvut.fel.omo.event.Event;
import cz.cvut.fel.omo.house.House;
import cz.cvut.fel.omo.house.room.Room;
import cz.cvut.fel.omo.timesimulator.TimeManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EnergyConsumptionTest {

    @BeforeEach
    void setUp() {
        Room wc = new Room("Toilet room");
        Device toilet = new Toilet(wc);
        wc.setDevices(Set.of(toilet));
        House.getInstance().setRooms(List.of(wc));
        TimeManager.getInstance().setCurrentTime(700);
    }

    @Test
    void testFridgeAlwaysConsumeEnergy() {
        Room testRoom = new Room("TestRoom");
        Device fridge = new Fridge(testRoom);
        double initialEnergy = House.getInstance().getTotalEnergyConsumed();

        fridge.doStep(1);

        double newEnergy = House.getInstance().getTotalEnergyConsumed();
        assertTrue(newEnergy > initialEnergy, "Total energy consumption should increase after fridge usage");
    }

    @Test
    void testPCDoNotConsumeEnergyWithoutHumanInteraction() {
        Room testRoom = new Room("TestRoom");
        Device pc = new PC(testRoom);
        double initialEnergy = House.getInstance().getTotalEnergyConsumed();

        pc.doStep(1);

        double newEnergy = House.getInstance().getTotalEnergyConsumed();
        assertEquals(initialEnergy, newEnergy, "PC should not consume energy without human interaction");
    }

    @Test
    void testPCConsumeEnergyWithHumanInteraction() {
        Room testRoom = new Room("TestRoom");
        Child child = new Child("Child", testRoom);
        Device pc = new PC(testRoom);
        double initialEnergy = House.getInstance().getTotalEnergyConsumed();

        Event childUsesPC = Event.create()
                .subject(pc)
                .object(child)
                .room(testRoom)
                .name("Child uses PC")
                .remainingTime(30)
                .build();
        child.scheduleEvent(childUsesPC);
        child.doStep(1);

        double newEnergy = House.getInstance().getTotalEnergyConsumed();
        assertTrue(newEnergy > initialEnergy, "PC should consume energy with human interaction");
    }
}
