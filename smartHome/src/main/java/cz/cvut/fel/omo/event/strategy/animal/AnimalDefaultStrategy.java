package cz.cvut.fel.omo.event.strategy.animal;


import cz.cvut.fel.omo.entity.alive.Animal;
import cz.cvut.fel.omo.entity.device.Device;
import cz.cvut.fel.omo.entity.device.DeviceStatus;
import cz.cvut.fel.omo.event.Event;
import cz.cvut.fel.omo.event.strategy.DefaultStrategyBase;
import cz.cvut.fel.omo.timesimulator.TimeManager;

import java.util.List;

public class AnimalDefaultStrategy extends DefaultStrategyBase<Animal> {

    private final TimeManager timeManager = TimeManager.getInstance();

    @Override
    public boolean isUrgent() {
        return false;
    }

    @Override
    protected List<Event> generateEventsWithDevice(Device device, Animal entity) {
        return List.of(device.createEventForAnimal(entity).build());
    }

    @Override
    protected List<Event> createBeforeSleepRoutineEvents(Animal aliveEntity) {
        return List.of(selfGroomingEvent(aliveEntity));
    }

    @Override
    protected Event createGoToSleepEvent(Animal aliveEntity) {
        return Event.create()
                .isUrgent(false)
                .isSleeping(true)
                .name("Sleeping")
                .remainingTime(450)
                .object(aliveEntity)
                .room(aliveEntity.getCurrentRoom())
                .build();
    }


    private Event selfGroomingEvent(Animal entity) {
        return Event.create()
                .isUrgent(false)
                .isSleeping(true)
                .name("Grooming before bed")
                .remainingTime(15)
                .object(entity)
                .room(entity.getCurrentRoom())
                .build();
    }

    @Override
    protected boolean skipDevice(Device device) {
        return !device.isForAnimal()
                || device.getStatus().equals(DeviceStatus.BUSY)
                || !device.canBeChosenForRandomInteraction();
    }
}
