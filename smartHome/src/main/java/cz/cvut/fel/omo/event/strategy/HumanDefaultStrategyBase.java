package cz.cvut.fel.omo.event.strategy;

import cz.cvut.fel.omo.entity.alive.HumanEntity;
import cz.cvut.fel.omo.entity.device.Device;
import cz.cvut.fel.omo.event.Event;

import java.util.List;

/**
 * Base class for default strategies applied to human entities.
 *
 * @param <T> the type of HumanEntity for which the strategy is applicable.
 */
public abstract class HumanDefaultStrategyBase<T extends HumanEntity> extends DefaultStrategyBase<T> {

    /**
     * Generates events involving a device for the specified human entity.
     *
     * @param device The device involved in the event.
     * @param entity The human entity for whom the event is generated.
     * @return A list containing the generated event.
     */
    @Override
    protected List<Event> generateEventsWithDevice(Device device, T entity) {
        return List.of(device.accept(entity).build());
    }
}