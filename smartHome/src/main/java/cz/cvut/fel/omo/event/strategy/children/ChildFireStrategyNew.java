package cz.cvut.fel.omo.event.strategy.children;

import cz.cvut.fel.omo.entity.alive.AliveEntity;
import cz.cvut.fel.omo.house.House;
import cz.cvut.fel.omo.event.Event;
import cz.cvut.fel.omo.event.strategy.EventGeneratingStrategy;
import cz.cvut.fel.omo.timesimulator.TimeManager;

import java.util.List;

/**
 * The type Child fire strategy new.
 */
public class ChildFireStrategyNew implements EventGeneratingStrategy {

    @Override
    public List<Event> generate(AliveEntity child) {
        Event stayCalm = Event.create()
                .isUrgent(true)
                .object(child)
                .name("Stay Calm")
                .remainingTime(10)
                .room(House.getInstance().getBackyard())
                .build();

        Event extinguishFire = Event.create()
                .isUrgent(true)
                .object(child)
                .name("Waiting for adult to extinguish fire")
                .remainingTime(120)
                .room(House.getInstance().getBackyard())
                .build();

        return List.of(stayCalm,extinguishFire);
    }

    @Override
    public boolean isUrgent() {
        return true;
    }
}
