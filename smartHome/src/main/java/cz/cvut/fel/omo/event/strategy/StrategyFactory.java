package cz.cvut.fel.omo.event.strategy;

import cz.cvut.fel.omo.entity.alive.AliveEntity;

/**
 * Interface for a factory to create different types of event-generating strategies.
 */
public interface StrategyFactory {

    /**
     * Gets the default event-generating strategy.
     *
     * @return An instance of EventGeneratingStrategy representing the default strategy.
     */
    EventGeneratingStrategy getDefaultStrategy();

    /**
     * Gets the fire emergency event-generating strategy.
     *
     * @return An instance of EventGeneratingStrategy for fire emergencies.
     */
    EventGeneratingStrategy getFireStrategy();

    /**
     * Gets the flood emergency event-generating strategy.
     *
     * @return An instance of EventGeneratingStrategy for flood emergencies.
     */
    EventGeneratingStrategy getFloodStrategy();
}