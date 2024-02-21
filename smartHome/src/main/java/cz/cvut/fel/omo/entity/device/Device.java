package cz.cvut.fel.omo.entity.device;

import cz.cvut.fel.omo.entity.Entity;
import cz.cvut.fel.omo.entity.alive.Animal;
import cz.cvut.fel.omo.house.House;
import cz.cvut.fel.omo.house.room.Room;
import cz.cvut.fel.omo.event.Event;
import cz.cvut.fel.omo.timesimulator.TimeManager;
import cz.cvut.fel.omo.util.logger.ConsumptionHistory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Abstract base class for all devices in the simulation.
 */
public abstract class Device extends Entity {


    /**
     * The Status.
     */
    protected DeviceStatus status = DeviceStatus.FREE;

    /**
     * The Can be chosen for random interaction.
     */
    protected boolean canBeChosenForRandomInteraction = true;

    /**
     * The Is for animal.
     */
    protected boolean isForAnimal = false;

    /**
     * The Initiator events.
     */
    protected List<Event> initiatorEvents = new ArrayList<>();

    /**
     * Instantiates a new Device.
     *
     * @param room the room
     * @param name the name
     */
    public Device(Room room, String name) {
        super(room, name);
    }

    /**
     * Executes the actions for the current step of the simulation for this device.
     * Processes any events that the device is involved in as an OBJECT.
     *
     * @param context The context or time step of the simulation.
     */
    @Override
    public void doStep(int context) {
        for(Event event : initiatorEvents){
            event.proceed();
        }
    }

    /**
     * Reserves the device for use in an event. If the device is broken, it throws an exception.
     *
     * @param event The event for which the device is reserved.
     */
    public void reserve(Event event) {
        if(status == DeviceStatus.BROKEN){
            throw new RuntimeException("Problem with Broken");
        }
        status = DeviceStatus.BUSY;
    }

    /**
     * Handles the ongoing work of the device during an event.
     * Consumes energy if the device is currently busy.
     *
     * @param event The event involving the device.
     */
    public void proceedWithWork(Event event) {
        if(getStatus() == DeviceStatus.BUSY){
            consumeEnergy();
        }
    }

    /**
     * Interrupts the current event of the device.
     */
    @Override
    protected void interruptEvent() {
        for(Event event : initiatorEvents){
            event.pauseEvent();
        }
        initiatorEvents.clear();
    }

    /**
     * Free.
     */
    public void free() {
        if(status == DeviceStatus.BUSY){
            status = DeviceStatus.FREE;
        }
    }

    /**
     * Gets default consumption in minute.
     *
     * @return the default consumption in minute
     */
    public abstract double getDefaultConsumptionInMinute();

    /**
     * Consume energy.
     */
    protected void consumeEnergy(){
        double energyConsumed =  TimeManager.getInstance().getStep()* getDefaultConsumptionInMinute();
        House.getInstance().consumeEnergy(energyConsumed);
        ConsumptionHistory.getInstance().logElectricityConsumption(this, getDefaultConsumptionInMinute());
    }

    @Override
    public void setCurrentEvent(Event event) {
        super.setCurrentEvent(event);
    }

    /**
     * Is free boolean.
     *
     * @return the boolean
     */
    public boolean isFree() {
        return status == DeviceStatus.FREE;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(DeviceStatus status) {
        this.status = status;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public DeviceStatus getStatus() {
        return status;
    }

    /**
     * Creates an event specific for an animal, if applicable.
     *
     * @param animal The animal interacting with the device.
     * @return An EventBuilder to create the event, or null if not applicable.
     */
    public Event.EventBuilder createEventForAnimal(Animal animal){
        return null;
    };

    /**
     * Is for animal boolean.
     *
     * @return the boolean
     */
    public boolean isForAnimal() {
        return isForAnimal;
    }

    /**
     * Can be chosen for random interaction boolean.
     *
     * @return the boolean
     */
    public boolean canBeChosenForRandomInteraction() {
        return canBeChosenForRandomInteraction;
    }

    /**
     * Accept event . event builder.
     *
     * @param visitor the visitor
     * @return the event . event builder
     */
    public abstract Event.EventBuilder accept(DeviceVisitor visitor);

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)){
            return true;
        }
        if(obj instanceof Entity) {
            return getName().equals(((Entity) obj).getName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
