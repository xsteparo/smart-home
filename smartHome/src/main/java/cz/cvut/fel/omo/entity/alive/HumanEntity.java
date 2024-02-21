package cz.cvut.fel.omo.entity.alive;

import cz.cvut.fel.omo.entity.device.DeviceVisitor;
import cz.cvut.fel.omo.entity.device.MobilePhone;
import cz.cvut.fel.omo.house.room.Room;

/**
 * Abstract class representing a human entity in the simulation.
 * Defines the core functionalities and interactions that are common to all human-like entities.
 */
public abstract class HumanEntity extends AliveEntity implements DeviceVisitor {

    /**
     * Constructs a HumanEntity with a specified name and initial room.
     *
     * @param name The name of the human entity.
     * @param room The initial room where the human entity is located.
     */
    public HumanEntity(String name, Room room) {
        super(name, room);
    }

    /**
     * Abstract method to be implemented by subclasses to provide access to the entity's mobile phone.
     * This method should return the specific mobile phone instance associated with the human entity.
     *
     * @return The mobile phone of the human entity.
     */
    public abstract MobilePhone getMobilePhone();
}