package cz.cvut.fel.omo.entity;

import cz.cvut.fel.omo.house.House;

/**
 * Represents an entity that is aware of and can respond to emergency situations.
 */
public interface EmergencyAwareEntity {

    /**
     * Updates the emergency status of the entity.
     *
     * @param status The current emergency status of the house.
     */
    void updateEmergencyStatus(House.HomeEmergencyStatus status);

}