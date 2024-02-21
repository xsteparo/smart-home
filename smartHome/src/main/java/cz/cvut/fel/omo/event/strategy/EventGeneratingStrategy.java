package cz.cvut.fel.omo.event.strategy;

import cz.cvut.fel.omo.entity.Entity;
import cz.cvut.fel.omo.entity.alive.AliveEntity;
import cz.cvut.fel.omo.event.Event;

import java.util.List;

/**
 * Interface for defining strategies to generate events for alive entities.
 *
 * @param <T> the type of AliveEntity for which the strategy is applicable.
 */
public interface EventGeneratingStrategy<T extends AliveEntity> {

    /**
     * Generates a list of events based on the specific strategy for the provided entity.
     *
     * @param entity The entity for which events are to be generated.
     * @return A list of generated events.
     */
    List<Event> generate(T entity);

    /**
     * Indicates whether the events generated by this strategy are urgent.
     *
     * @return true if the events are urgent, false otherwise.
     */
    boolean isUrgent();
}