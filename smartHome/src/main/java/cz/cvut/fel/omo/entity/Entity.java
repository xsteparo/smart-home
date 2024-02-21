package cz.cvut.fel.omo.entity;

import cz.cvut.fel.omo.house.House;
import cz.cvut.fel.omo.house.room.Room;
import cz.cvut.fel.omo.event.Event;
import cz.cvut.fel.omo.timesimulator.SimulationMember;

/**
 * Represents a generic entity in the smart home environment.
 * This class is an abstract base for all entities involved in the simulation.
 */
public abstract class Entity implements SimulationMember, EmergencyAwareEntity {

    /**
     * The Name.
     */
    protected String name;

    /**
     * The Current room.
     */
    protected Room currentRoom;

    /**
     * The Initiator event.
     */
    protected Event initiatorEvent;

    @Override
    public void doStep(int context) {
        if(initiatorEvent != null ){
            initiatorEvent.proceed();
        }
    }

    /**
     * Responds to a change in emergency status by interrupting the current event.
     *
     * @param status The new emergency status.
     */
    @Override
    public void updateEmergencyStatus(House.HomeEmergencyStatus status) {
        interruptEvent();
    }

    /**
     * Interrupt event.
     */
    protected void interruptEvent(){
        if(initiatorEvent != null){
            initiatorEvent.pauseEvent();
        }
    }

    /**
     * Instantiates a new Entity.
     *
     * @param currentRoom the current room
     * @param name        the name
     */
    public Entity(Room currentRoom, String name) {
        this.currentRoom = currentRoom;
        this.name = name;
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
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets current room.
     *
     * @param currentRoom the current room
     */
    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    /**
     * Gets current room.
     *
     * @return the current room
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Gets current event.
     *
     * @return the current event
     */
    public Event getCurrentEvent() {
        return initiatorEvent;
    }

    /**
     * Sets current event.
     *
     * @param event the event
     */
    public void setCurrentEvent(Event event) {
        initiatorEvent = event;
    }

}
