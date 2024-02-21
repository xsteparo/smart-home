package cz.cvut.fel.omo.entity.device;

import cz.cvut.fel.omo.entity.Entity;
import cz.cvut.fel.omo.entity.alive.AliveEntity;
import cz.cvut.fel.omo.house.House;
import cz.cvut.fel.omo.house.room.Room;
import cz.cvut.fel.omo.event.Event;
import cz.cvut.fel.omo.util.logger.EventHistory;

import java.util.List;

/**
 * Represents a smart fridge device, capable of storing food and being prone to breakdowns.
 */
public class Fridge extends BreakableDevice {

    private static int entityCount = 0;

    private static final double CONSUMPTION_IN_MINUTE = 1.5;

    private static final int REPAIRING_TIME = 50;

    private int remainingFood;

    /**
     * Instantiates a new Fridge.
     *
     * @param room the room
     */
    public Fridge(Room room) {
        super(room, "Fridge #" + entityCount++);
        remainingFood = 10;
    }

    /**
     * Performs actions for the current simulation step. Consumes energy if the fridge is not broken.
     *
     * @param context The context or time step of the simulation.
     */
    @Override
    public void doStep(int context) {
        if(status!=DeviceStatus.BROKEN){
            consumeEnergy();
        };
    }

    /**
     * Accepts a visit from a device visitor, typically to perform an interaction specific to the fridge.
     *
     * @param visitor The visitor interacting with the fridge.
     * @return An EventBuilder instance for further customization of the event.
     */
    @Override
    public Event.EventBuilder accept(DeviceVisitor visitor) {
        if(remainingFood>0){
            return visitor.visitFridge(this);
        }
        EventHistory.getInstance().logNoFoodInFridge(this);
        return createBuyFoodEvent(visitor);
    }

    /**
     * Creates an event for buying food when the fridge is empty.
     *
     * @param visitor The visitor who will execute the food buying event.
     * @return An EventBuilder for creating the food buying event.
     */
    private Event.EventBuilder createBuyFoodEvent(DeviceVisitor visitor){
        Car car = House.getInstance().getCar();
        if(car == null){
            throw new RuntimeException("No car, no food, we all will die");
        }
        return Event.create()
                .isUrgent(false)
                .name("Drive to shop to buy some food")
                .subject(car)
                .object((Entity) visitor)
                .room(car.getCurrentRoom())
                .endFunction(()-> remainingFood = 10)
                .remainingTime(100);
    }

    @Override
    protected List<Event> getRepairingEvents(AliveEntity repairer) {
        Event defrostFridge = Event.create()
                .isUrgent(false)
                .name("defrosts the fridge")
                .object(repairer)
                .room(currentRoom)
                .remainingTime(30)
                .build();

        Event replaceParts = Event.create()
                .isUrgent(false)
                .name("replaces faulty fridge parts")
                .object(repairer)
                .room(currentRoom)
                .remainingTime(40)
                .build();

        Event cleanFridge = Event.create()
                .isUrgent(false)
                .name("cleans the fridge interior")
                .object(repairer)
                .room(currentRoom)
                .remainingTime(15)
                .build();

        Event testFridge = Event.create()
                .isUrgent(false)
                .name("tests the fridge functionality")
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

    /**
     * Get remaining food int.
     *
     * @return the int
     */
    public int getRemainingFood(){
        return remainingFood;
    }

    /**
     * Set remaining food.
     *
     * @param remainingFood the remaining food
     */
    public void setRemainingFood(int remainingFood){
        this.remainingFood = remainingFood;
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
