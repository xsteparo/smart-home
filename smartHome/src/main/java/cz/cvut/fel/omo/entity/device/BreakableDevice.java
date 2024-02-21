package cz.cvut.fel.omo.entity.device;

import cz.cvut.fel.omo.entity.alive.Adult;
import cz.cvut.fel.omo.entity.alive.AliveEntity;
import cz.cvut.fel.omo.house.House;
import cz.cvut.fel.omo.house.room.Room;
import cz.cvut.fel.omo.event.Event;
import cz.cvut.fel.omo.util.logger.EventHistory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract class representing devices that can undergo breakdowns.
 * These devices need to be repaired to return to normal operation.
 */
public abstract class BreakableDevice extends Device {

    public BreakableDevice(Room room, String name) {
        super(room, name);
    }

    /**
     * Processes the work for the device. If certain conditions are met (based on a random chance),
     * the device breaks down, triggering the breakdown handling process.
     *
     * @param event The event that the device is currently processing.
     */
    @Override
    public void proceedWithWork(Event event) {
        super.proceedWithWork(event);
        if (Math.random() > 0.995) {
            breakDown(event);
        }
    }

    /**
     * Handles the breakdown of the device. Changes the device's status to BROKEN
     * and logs the breakdown event. Initiates the repair process by generating repair events.
     *
     * @param event The event during which the device broke down.
     */
    public void breakDown(Event event) {
        if(status == DeviceStatus.BROKEN){
            return;
        }
        status = DeviceStatus.BROKEN;

        AliveEntity repairer = findRepairer(event);

        EventHistory.getInstance().logDeviceBreakdown(this, repairer);

        event.pauseEvent();
        if(event.getObject() instanceof  AliveEntity){
            ((AliveEntity)event.getObject()).scheduleEvent(event);
        }

        List<Event> repairingEvents = new ArrayList<>(getRepairingEvents(repairer));
        Collections.reverse(repairingEvents);
        repairingEvents.forEach(repairer::scheduleEvent);
        repairer.scheduleEvent(createReadInstrucionEvent(repairer));
    }

    /**
     * Finds a suitable entity to repair the broken device.
     * The entity involved in the event is chosen if it's an AliveEntity;
     * otherwise, a random adult from the house is selected.
     *
     * @param event The event in which the device broke down.
     * @return The entity that will repair the device.
     */
    private AliveEntity findRepairer(Event event){
        if(event.getObject() instanceof  AliveEntity){
            return (AliveEntity) event.getObject();
        }
        return findRandomReparer();
    }

    /**
     * Selects a random adult from the house to repair the device.
     *
     * @return An adult entity chosen to repair the device.
     */
    private AliveEntity findRandomReparer(){
        List<Adult> adults =  House.getInstance().getSmartPanel().getAdults();
        return adults.get( (int) (Math.random() * adults.size()));
    }

    /**
     * Creates an event for the repairer to read instructions on how to fix the broken device.
     *
     * @param repairer The entity repairing the device.
     * @return An EventBuilder to create the instruction reading event.
     */
    private Event createReadInstrucionEvent(AliveEntity repairer) {
        return Event.create()
                .isUrgent(false)
                .name("Reading instructions how to fix " + BreakableDevice.this.name)
                .object(repairer)
                .room(currentRoom)
                .remainingTime(getReadingInstructionsTime())
                .build();
    }

    /**
     * Method to be implemented in subclasses. It should return a list of events
     * that represent the steps required to repair the device.
     *
     * @param repairer The entity that will repair the device.
     * @return A list of Event objects representing the repair process.
     */
    protected  List<Event> getRepairingEvents(AliveEntity repairer){
        throw new UnsupportedOperationException(); // must be implemented in implementation classes
    }

    /**
     * Abstract method to be implemented in subclasses. It should return the time required
     * to read the instructions necessary for repairing the device.
     *
     * @return The time needed to read repair instructions.
     */
    protected  int getReadingInstructionsTime(){
        throw new UnsupportedOperationException(); // must be implemented in implementation classes
    }

}
