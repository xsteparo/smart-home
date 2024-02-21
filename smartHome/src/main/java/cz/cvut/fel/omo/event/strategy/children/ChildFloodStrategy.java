package cz.cvut.fel.omo.event.strategy.children;

import cz.cvut.fel.omo.entity.alive.AliveEntity;
import cz.cvut.fel.omo.event.Event;
import cz.cvut.fel.omo.event.strategy.EventGeneratingStrategy;

import java.util.List;

/**
 * The type Child flood strategy.
 */
public class ChildFloodStrategy implements EventGeneratingStrategy {

    @Override
    public List<Event> generate(AliveEntity entity) {
        Event swimming = Event.create()
                .isUrgent(true)
                .name("Swimming in house")
                .object(entity)
                .remainingTime(10)

                .room(entity.getCurrentRoom())
                .build();

        Event help = Event.create()
                .isUrgent(true)
                .name("Helps parents to clean house")
                .object(entity)
                .remainingTime(130)

                .room(entity.getCurrentRoom())
                .build();

        return List.of(swimming, help);
    }

    @Override
    public boolean isUrgent() {
        return true;
    }
}
