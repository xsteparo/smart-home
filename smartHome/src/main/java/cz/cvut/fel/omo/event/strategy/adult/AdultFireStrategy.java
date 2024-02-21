package cz.cvut.fel.omo.event.strategy.adult;

import cz.cvut.fel.omo.entity.alive.AliveEntity;
import cz.cvut.fel.omo.house.House;
import cz.cvut.fel.omo.event.Event;
import cz.cvut.fel.omo.event.strategy.EventGeneratingStrategy;
import cz.cvut.fel.omo.timesimulator.TimeManager;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Adult fire strategy.
 */
public class AdultFireStrategy implements EventGeneratingStrategy {

    @Override
    public boolean isUrgent() {
        return true;
    }

    @Override
    public List<Event> generate(AliveEntity adult) {
        Event inspectSituation = Event.create()
                .isUrgent(true)
                .object(adult)
                .name("Inspecting fire situation")
                .remainingTime(15)
                .room(House.getInstance().getBackyard())
                .build();

        Event extinguishFire = Event.create()
                .isUrgent(true)
                .object(adult)
                .name("Extinguishing fire")
                .remainingTime(30)
                .room(House.getInstance().getBackyard())
                .build();

        Event checkAftermath = Event.create()
                .isUrgent(true)
                .object(adult)
                .name("Checking for hidden fire sources")
                .remainingTime(20)
                .room(House.getInstance().getBackyard())
                .build();

        Event notifyEmergencyServices = Event.create()
                .isUrgent(true)
                .object(adult)
                .name("Notifying smart panel that fire is extinguished")
                .remainingTime(5)
                .room(House.getInstance().getBackyard())
                .endFunction(() -> House.getInstance().setStatus(House.HomeEmergencyStatus.DEFAULT))
                .build();


        return List.of(inspectSituation,extinguishFire,checkAftermath,notifyEmergencyServices);
    }

}
