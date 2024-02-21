package cz.cvut.fel.omo.event.strategy.animal;

import cz.cvut.fel.omo.entity.alive.AliveEntity;
import cz.cvut.fel.omo.house.House;
import cz.cvut.fel.omo.event.Event;
import cz.cvut.fel.omo.event.strategy.EventGeneratingStrategy;

import java.util.List;

/**
 * The type Animal flood strategy.
 */
public class AnimalFloodStrategy implements EventGeneratingStrategy {

    @Override
    public List<Event> generate(AliveEntity entity) {
         Event swimming = Event.create()
                .isUrgent(true)
                .object(entity)
                .name("happily floating in the water with his tongue out.")
                .remainingTime(140)
                .room(entity.getCurrentRoom())
                .endFunction(() -> House.getInstance().setStatus(House.HomeEmergencyStatus.DEFAULT))
                .build();

         Event runOut = Event.create()
                 .isUrgent(true)
                 .object(entity)
                 .name("watching the family")
                 .remainingTime(140)
                 .room(House.getInstance().getBackyard())
                 .endFunction(() -> House.getInstance().setStatus(House.HomeEmergencyStatus.DEFAULT))
                 .build();

        return List.of(swimming,runOut);
    }

    @Override
    public boolean isUrgent() {
        return true;
    }
}
