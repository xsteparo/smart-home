import cz.cvut.fel.omo.entity.alive.Adult;
import cz.cvut.fel.omo.entity.device.Car;
import cz.cvut.fel.omo.entity.device.Fridge;
import cz.cvut.fel.omo.event.Event;
import cz.cvut.fel.omo.house.House;
import cz.cvut.fel.omo.house.room.Room;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AdultDrivesToBuyFoodTest {

    @Test
    void testAdultGoesShoppingWhenFridgeIsEmpty() {
        Room kitchen = new Room("Kitchen");
        Fridge fridge = new Fridge(kitchen);
        fridge.setRemainingFood(0);

        Car car = new Car(new Room("Outside"));
        House.getInstance().setCar(car);

        Adult adult = new Adult("John Doe", kitchen);

        Event.EventBuilder shoppingEventBuilder = fridge.accept(adult);
        assertNotNull(shoppingEventBuilder, "Shopping event should be created when fridge is empty");

        Event shoppingEvent = shoppingEventBuilder.build();
        assertEquals("Drive to shop to buy some food", shoppingEvent.getName(), "Event should be for shopping");
        assertEquals(car, shoppingEvent.getSubject(), "The subject of the event should be the car");
        assertEquals(adult, shoppingEvent.getObject(), "The object of the event should be the adult");
    }
}
