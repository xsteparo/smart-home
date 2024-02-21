package cz.cvut.fel.omo.entity.device.decorator;

import cz.cvut.fel.omo.entity.device.BreakableDevice;
import cz.cvut.fel.omo.entity.device.DeviceVisitor;
import cz.cvut.fel.omo.house.House;
import cz.cvut.fel.omo.entity.device.DeviceStatus;
import cz.cvut.fel.omo.house.room.Room;
import cz.cvut.fel.omo.event.Event;

/**
 * An abstract decorator class that extends the functionality of a BreakableDevice to potentially cause trouble during its operation.
 * This class serves as the base for specific trouble-causing device implementations such as FlammableDevice or FloodingDevice.
 */
public abstract class TroublesCausingDevice extends BreakableDevice {

    protected BreakableDevice wrappee;

    /**
     * Constructs a TroublesCausingDevice wrapping a BreakableDevice.
     *
     * @param wrappee The BreakableDevice to be decorated.
     */
    public TroublesCausingDevice(BreakableDevice wrappee) {
        super(wrappee.getCurrentRoom(), wrappee.getName());
        this.wrappee = wrappee;
    }

    /**
     * Proceeds with the work of the wrapped device and potentially causes trouble.
     *
     * @param event The current event being processed.
     */
    @Override
    public void proceedWithWork(Event event) {
        wrappee.proceedWithWork(event);
        randomlyCauseTrouble(event);
    }

    /**
     * Invokes the breakdown functionality of the wrapped device.
     *
     * @param event The event causing the breakdown.
     */
    @Override
    public void breakDown(Event event) {
        wrappee.breakDown(event);
    }

    /**
     * Executes a simulation step for the wrapped device.
     *
     * @param context The context of the current simulation step.
     */
    @Override
    public void doStep(int context) {
        wrappee.doStep(context);
    }

    /**
     * Updates the emergency status of the wrapped device.
     *
     * @param status The new emergency status.
     */
    @Override
    public void updateEmergencyStatus(House.HomeEmergencyStatus status) {
        wrappee.updateEmergencyStatus(status);
    }

    /**
     * Accepts a visit from a DeviceVisitor, delegating to the wrapped device.
     *
     * @param visitor The visiting device handler.
     * @return An EventBuilder for event construction.
     */
    @Override
    public Event.EventBuilder accept(DeviceVisitor visitor) {
        return wrappee.accept(visitor).subject(this);
    }

    /**
     * Reserves the wrapped device for an event.
     *
     * @param event The event for which the device is reserved.
     */
    @Override
    public void reserve(Event event) {
        wrappee.reserve(event);
    }

    /**
     * Frees the wrapped device from its current reservation.
     */
    @Override
    public void free() {
        wrappee.free();
    }

    /**
     * Randomly triggers trouble-causing behavior based on specific conditions.
     *
     * @param event The current event being processed.
     */
    private void randomlyCauseTrouble(Event event) {
        if (canCauseTrouble() && getStatus().equals(DeviceStatus.BUSY)) {
            causeTrouble(event);
        }
    }

    /**
     * Abstract method to be implemented by subclasses for causing specific types of trouble.
     *
     * @param event The event during which the trouble is caused.
     */
    protected abstract void causeTrouble(Event event);

    /**
     * Delegates to the wrapped device to obtain its default energy consumption rate.
     *
     * @return The default energy consumption rate in minutes.
     */
    @Override
    public double getDefaultConsumptionInMinute() {
        return wrappee.getDefaultConsumptionInMinute();
    }

    /**
     * Determines whether the device can cause trouble based on the current home emergency status.
     *
     * @return True if the device can cause trouble, false otherwise.
     */
    private boolean canCauseTrouble() {
        return House.getInstance().getSmartPanel().getHomeEmergencyStatus() == House.HomeEmergencyStatus.DEFAULT;
    }
    @Override
    public String getName() {
        return wrappee.getName();
    }

    @Override
    public void setName(String name) {
        wrappee.setName(name);
    }

    @Override
    public void setCurrentRoom(Room currentRoom) {
        wrappee.setCurrentRoom(currentRoom);
    }

    @Override
    public Room getCurrentRoom() {
        return wrappee.getCurrentRoom();
    }

    @Override
    public Event getCurrentEvent() {
        return wrappee.getCurrentEvent();
    }

    @Override
    public void setCurrentEvent(Event event) {
        wrappee.setCurrentEvent(event);
    }

    @Override
    public DeviceStatus getStatus() {
        return wrappee.getStatus();
    }

    @Override
    public boolean isFree() {
        return wrappee.isFree();
    }

}
