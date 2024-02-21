package cz.cvut.fel.omo.event.strategy.animal;

import cz.cvut.fel.omo.entity.alive.AliveEntity;
import cz.cvut.fel.omo.house.House;
import cz.cvut.fel.omo.event.Event;
import cz.cvut.fel.omo.event.strategy.EventGeneratingStrategy;

import java.util.List;

/**
 * The type Animal fire strategy.
 */
public class AnimalFireStrategy implements EventGeneratingStrategy {

    @Override
    public List<Event> generate(AliveEntity entity) {
        return List.of( Event.create()
                .isUrgent(true)
                .object(entity)
                .name("barks in a nervous manner ")
                .remainingTime(140)
                .room(House.getInstance().getBackyard())
                .endFunction(() -> House.getInstance().setStatus(House.HomeEmergencyStatus.DEFAULT))
                .build());
    }

    @Override
    public boolean isUrgent() {
        return true;
    }
}
