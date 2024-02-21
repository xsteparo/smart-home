package cz.cvut.fel.omo.entity.alive;


import cz.cvut.fel.omo.entity.Entity;

import cz.cvut.fel.omo.entity.device.Device;
import cz.cvut.fel.omo.entity.device.DeviceStatus;
import cz.cvut.fel.omo.house.House;
import cz.cvut.fel.omo.house.room.Room;
import cz.cvut.fel.omo.event.Event;
import cz.cvut.fel.omo.event.strategy.EventGeneratingStrategy;
import cz.cvut.fel.omo.event.strategy.StrategyFactory;
import cz.cvut.fel.omo.timesimulator.TimeManager;


import java.util.*;

/**
 * Abstract class representing a living entity in the simulation.
 * Handles event generation and management based on the current strategy.
 */
public abstract class AliveEntity extends Entity {

    /**
     * The current event-generating strategy being used by the entity.
     * This strategy determines the nature and type of events that the entity will generate
     * or participate in, based on various factors such as the entity's type, the current
     * situation in the simulation, and specific environmental conditions.
     * By default, it is initialized to the default strategy provided by the entity's
     * specific strategy factory, which is typically suitable for normal, day-to-day activities.
     * However, it can be changed to other strategies as required, for example, in emergency
     * situations like fires or floods.
     */
    protected EventGeneratingStrategy currentStrategy = getStrategyFactory().getDefaultStrategy();

    /**
     * Constructs an event builder for a default event that does not involve any device.
     * This method is meant to be implemented by concrete subclasses to define
     * default events specific to different types of alive entities.
     *
     * @return An Event.EventBuilder instance for creating a default event.
     */
    public abstract Event.EventBuilder getDefaultEventWithoutDevice();

    /**
     * Provides access to the StrategyFactory associated with the entity.
     * This method is intended to be implemented by subclasses to return
     * a specific strategy factory that creates event-generating strategies
     * for different scenarios (like normal day, emergency situations, etc.).
     *
     * @return An instance of StrategyFactory appropriate for the entity.
     */
    public abstract StrategyFactory getStrategyFactory();

    protected Stack<Event> pendingEvents = new Stack<>();

    /**
     * Constructs an AliveEntity with the given name and room.
     *
     * @param name The name of the entity.
     * @param room The room where the entity is initially located.
     */
    public AliveEntity(String name, Room room) {
        super(room, name);
    }

    /**
     * Performs an action based on the current context of the simulation.
     * This includes processing events and possibly changing rooms.
     *
     * @param context The context of the simulation step.
     */
    @Override
    public void doStep(int context) {
        if (initiatorEvent == null) {
            if (!hasSuitablePendingEvent()) {
                List<Event> generatedEvents = new ArrayList<>(currentStrategy.generate(this));

                Collections.reverse(generatedEvents);
                pendingEvents.addAll(generatedEvents);
            }
            // change room for last event in stack
            if (currentRoom != pendingEvents.peek().getRoom()) {
                initiatorEvent = Event.createChangeRoomEvent(this, pendingEvents.peek().getRoom());

            } else {
                initiatorEvent = pendingEvents.pop();
            }

        }

        super.doStep(context);
    }

    /**
     * Determines if there is a suitable pending event for the entity.
     *
     * @return True if there is a suitable event, False otherwise.
     */
    protected boolean hasSuitablePendingEvent() {
        if (pendingEvents.isEmpty()) return false;
        if (currentStrategy.isUrgent()) {
            return pendingEvents.peek().isUrgent();
        } else {
            if (TimeManager.getInstance().sleepingTime()) {
                return pendingEvents.peek().isSleeping();
            }
            Device subject = pendingEvents.peek().getSubject();
            if (subject == null) {
                return true;
            } else {
                if (subject.getStatus().equals(DeviceStatus.FREE)) {
                    return true;
                }
                // pop event from stack if subject is busy or broken by another entity
                pendingEvents.pop();
                return false;
            }
        }
    }

    /**
     * Updates the emergency status of the entity and changes its strategy accordingly.
     *
     * @param status The new emergency status.
     */
    @Override
    public void updateEmergencyStatus(House.HomeEmergencyStatus status) {
        switch (status) {
            case DEFAULT -> {
                cleanUrgentPendingEvents();
                currentStrategy = getStrategyFactory().getDefaultStrategy();
            }
            case FIRE -> {
                currentStrategy = getStrategyFactory().getFireStrategy();
            }
            case FLOOD -> {
                currentStrategy = getStrategyFactory().getFloodStrategy();
            }
        }
        super.updateEmergencyStatus(status);
    }

    /**
     * Clears urgent pending events.
     */
    private void cleanUrgentPendingEvents() {
        while (!pendingEvents.isEmpty()) {
            if (!pendingEvents.peek().isUrgent()) {
                break;
            }
            pendingEvents.pop();
        }
    }

    /**
     * Interrupts the current event.
     */
    @Override
    protected void interruptEvent() {
        if (initiatorEvent != null) {

            if (!initiatorEvent.isUrgent()) {
                pendingEvents.push(initiatorEvent);
                initiatorEvent.pauseEvent();
            } else {
                initiatorEvent.endEvent();
            }
        }
    }

    /**
     * Chooses a random element from an array of strings.
     *
     * @param events Array of string events.
     * @return A randomly chosen event.
     */
    protected String chooseRandomElement(String[] events) {
        int randomIndex = (int) (Math.random() * events.length);
        return events[randomIndex];
    }

    /**
     * Schedules a new event for this entity.
     *
     * @param event The event to schedule.
     */
    public void scheduleEvent(Event event) {
        pendingEvents.push(event);
    }

    public String getName() {
        return name;
    }


    public EventGeneratingStrategy getCurrentStrategy() {
        return currentStrategy;
    }

    public Stack<Event> getPendingEvents() {
        return pendingEvents;
    }
}
