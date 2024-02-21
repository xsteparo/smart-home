import cz.cvut.fel.omo.entity.alive.Adult;
import cz.cvut.fel.omo.event.strategy.adult.AdultFireStrategy;
import cz.cvut.fel.omo.house.House;
import cz.cvut.fel.omo.house.SmartPanel;
import cz.cvut.fel.omo.house.room.Room;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class SmartPanelTest {

    @Test
    void testEmergencyStatusUpdate() {
        Room room = new Room("TestRoom");
        SmartPanel smartPanel = new SmartPanel(room);
        House.getInstance().setSmartPanel(smartPanel);

        Adult adult = new Adult("TestAdult", new Room("TestRoom"));
        smartPanel.addSubscriber(adult);

        assertEquals(House.HomeEmergencyStatus.DEFAULT, smartPanel.getHomeEmergencyStatus(), "Initial status should be DEFAULT");

        smartPanel.updateStatus(House.HomeEmergencyStatus.FIRE);

        assertEquals(House.HomeEmergencyStatus.FIRE, smartPanel.getHomeEmergencyStatus(), "House status should be updated to FIRE");
        assertTrue(adult.getCurrentStrategy() instanceof AdultFireStrategy, "Adult's strategy should be updated to fire strategy");
    }
}
