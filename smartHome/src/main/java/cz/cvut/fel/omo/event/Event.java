package cz.cvut.fel.omo.event;

import cz.cvut.fel.omo.entity.Entity;
import cz.cvut.fel.omo.entity.alive.AliveEntity;
import cz.cvut.fel.omo.entity.device.Device;
import cz.cvut.fel.omo.entity.device.DeviceStatus;
import cz.cvut.fel.omo.house.room.Room;
import cz.cvut.fel.omo.util.logger.EventHistory;
import cz.cvut.fel.omo.timesimulator.TimeManager;

/**
 * The type Event.
 */
public class Event {

    private final Entity object;

    private final Device subject;

    private int remainingTime;

    private final int duration;

    private final String name;

    private final EndFunction endFunction;

    private Status status;

    private final Room room;

    private final boolean isUrgent;

    private final boolean isSleeping;


    /**
     * Instantiates a new Event.
     *
     * @param eventBuilder the event builder
     */
    protected Event(EventBuilder eventBuilder) {
        isUrgent = eventBuilder.isUrgent;
        object = eventBuilder.object;
        subject = eventBuilder.subject;
        room = eventBuilder.room;
        remainingTime = eventBuilder.remainingTime;
        name = eventBuilder.name;
        endFunction = eventBuilder.endFunction;
        isSleeping = eventBuilder.isSleeping;
        status = Status.IN_PROGRESS;
        duration = remainingTime;
    }

    /**
     * Proceed.
     */
    public void proceed() {
        if(status == Status.IN_PROGRESS && remainingTime == duration){
            EventHistory.getInstance().logEventStart(this);
        }
        if (status == Status.INTERRUPTED) {
            status = Status.IN_PROGRESS;
            EventHistory.getInstance().logEventResume(this);
        }

        if (subject != null) {
            subject.reserve(this);
            subject.proceedWithWork(this);
        }

        if(status != Status.INTERRUPTED){
            remainingTime -= TimeManager.getInstance().getStep();
        }

        if (remainingTime <= 0) {
            endEvent();
        }
    }

    /**
     * End event.
     */
    public void endEvent() {
        if (endFunction != null ) {
             endFunction.end();
        }

        if (subject != null && subject.getStatus() != DeviceStatus.BROKEN) {
            subject.free();
        }

        object.setCurrentEvent(null);
        EventHistory.getInstance().logEventEnd(this);
        status = Status.FINISHED;
    }

    /**
     * Pause event.
     */
    public void pauseEvent() {
        if (subject != null) {
            subject.free();
        }
        status = Status.INTERRUPTED;
        EventHistory.getInstance().logEventInterrupted(this);
        object.setCurrentEvent(null);
    }

    /**
     * Gets object.
     *
     * @return the object
     */
    public Entity getObject() {
        return object;
    }

    /**
     * Gets subject.
     *
     * @return the subject
     */
    public Device getSubject() {
        return subject;
    }

    /**
     * Sets remaining time.
     *
     * @param remainingTime the remaining time
     */
    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    /**
     * Gets remaining time.
     *
     * @return the remaining time
     */
    public int getRemainingTime() {
        return remainingTime;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Gets room.
     *
     * @return the room
     */
    public Room getRoom() {
        return room;
    }

    /**
     * Is urgent boolean.
     *
     * @return the boolean
     */
    public boolean isUrgent() {
        return isUrgent;
    }

    /**
     * Is sleeping boolean.
     *
     * @return the boolean
     */
    public boolean isSleeping() {
        return isSleeping;
    }

    /**
     * The enum Status.
     */
    public enum Status {
        /**
         * In progress status.
         */
        IN_PROGRESS,
        /**
         * Finished status.
         */
        FINISHED,
        /**
         * Interrupted status.
         */
        INTERRUPTED
    }


    /**
     * Functional interface representing a function to be executed at the end of an event.
     */
    @FunctionalInterface
    public interface EndFunction {
        /**
         * Defines the operation to be performed at the end of an event.
         */
        void end();
    }

    /**
     * Static method to create an event for changing the room of an alive entity.
     *
     * @param aliveEntity The alive entity who will change rooms.
     * @param room        The target room to which the entity will move.
     * @return An instance of Event representing the room change.
     */
    public static Event createChangeRoomEvent(AliveEntity aliveEntity, Room room) {
        return Event.create()
                .object(aliveEntity)
                .subject(null)
                .remainingTime(1)
                .name("goes to " + room.getName())
                .room(room)
                .endFunction(() -> aliveEntity.setCurrentRoom(room))
                .build();
    }

    /**
     * Static method to create a new instance of EventBuilder.
     * It initiates the building process of an Event object.
     *
     * @return An instance of EventBuilder for constructing an Event.
     */
    public static EventBuilder create() {
        return new EventBuilder();
    }

    /**
     * Inner class for building Event objects using the Builder pattern.
     * This class allows for setting various parameters of an Event object in a fluent manner.
     */
    public static class EventBuilder {


        private boolean isUrgent = true;

        private boolean isSleeping;

        private Entity object;

        private Device subject;

        private int remainingTime;

        private String name;

        private Room room;

        private EndFunction endFunction;

        /**
         * Is urgent event builder.
         *
         * @param isResumable the is resumable
         * @return the event builder
         */
        public EventBuilder isUrgent(boolean isResumable) {
            this.isUrgent = isResumable;
            return this;
        }

        /**
         * Is sleeping event builder.
         *
         * @param isSleeping the is sleeping
         * @return the event builder
         */
        public EventBuilder isSleeping(boolean isSleeping) {
            this.isSleeping = isSleeping;
            return this;
        }

        /**
         * Object event builder.
         *
         * @param entity the entity
         * @return the event builder
         */
        public EventBuilder object(Entity entity) {
            object = entity;
            return this;
        }

        /**
         * Subject event builder.
         *
         * @param device the device
         * @return the event builder
         */
        public EventBuilder subject(Device device) {
            subject = device;
            return this;
        }


        /**
         * Remaining time event builder.
         *
         * @param remainingTime the remaining time
         * @return the event builder
         */
        public EventBuilder remainingTime(int remainingTime) {
            this.remainingTime = remainingTime;
            return this;
        }

        /**
         * Name event builder.
         *
         * @param name the name
         * @return the event builder
         */
        public EventBuilder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Room event builder.
         *
         * @param room the room
         * @return the event builder
         */
        public EventBuilder room(Room room) {
            this.room = room;
            return this;
        }

        /**
         * End function event builder.
         *
         * @param endFunction the end function
         * @return the event builder
         */
        public EventBuilder endFunction(EndFunction endFunction) {
            this.endFunction = endFunction;
            return this;
        }

        /**
         * Build event.
         *
         * @return the event
         */
        public Event build() {

            return new Event(this);
        }
    }
}
