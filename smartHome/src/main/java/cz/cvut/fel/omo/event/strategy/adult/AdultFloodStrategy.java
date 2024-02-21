package cz.cvut.fel.omo.event.strategy.adult;

import cz.cvut.fel.omo.entity.alive.AliveEntity;
import cz.cvut.fel.omo.house.House;
import cz.cvut.fel.omo.event.Event;
import cz.cvut.fel.omo.event.strategy.EventGeneratingStrategy;

import java.util.List;

/**
 * The type Adult flood strategy.
 */
public class AdultFloodStrategy  implements EventGeneratingStrategy {
    @Override
    public List<Event> generate(AliveEntity entity) {
        Event shoutForHelp = Event.create()
                .isUrgent(true)
                .object(entity)
                .name("Shouting for the family to help")
                .remainingTime(140)
                .room(entity.getCurrentRoom())
                .endFunction(() -> House.getInstance().setStatus(House.HomeEmergencyStatus.DEFAULT))
                .build();

        Event drawWater = Event.create()
                .isUrgent(true)
                .object(entity)
                .name("Draws water with buckets")
                .remainingTime(140)
                .room(entity.getCurrentRoom())
                .endFunction(() -> House.getInstance().setStatus(House.HomeEmergencyStatus.DEFAULT))
                .build();

        return List.of(shoutForHelp,drawWater);
    }

    @Override
    public boolean isUrgent() {
        return true;
    }
}
