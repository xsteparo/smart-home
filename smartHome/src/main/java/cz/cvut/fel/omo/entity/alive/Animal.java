package cz.cvut.fel.omo.entity.alive;

import cz.cvut.fel.omo.house.House;
import cz.cvut.fel.omo.house.room.Room;
import cz.cvut.fel.omo.event.Event;
import cz.cvut.fel.omo.event.strategy.StrategyFactory;
import cz.cvut.fel.omo.event.strategy.animal.AnimalStrategyFactory;

public class Animal extends AliveEntity{

    /**
     * Factory for creating event strategies specific to animals.
     */
    private StrategyFactory strategyFactory;

    public Animal(String name, Room room) {
        super(name, room);
    }

    /**
     * Provides a default event for the animal when no device is involved.
     * The animal may perform various activities like barking or watching its master.
     * @return An EventBuilder to build the event.
     */
    @Override
    public Event.EventBuilder getDefaultEventWithoutDevice() {
        String[] activities = {"Barking", "Runs circles in room", "Watching on his master"};
        int randomIndex = (int) (Math.random() * activities.length);
        String selectedActivity = activities[randomIndex];

        return Event.create()
                .isUrgent(false)
                .object(this)
                .name(selectedActivity)
                .remainingTime(30)
                .room(House.getInstance().getBedroom(this));
    }

    /**
     * Provides the strategy factory that generates strategies specific to animals.
     * Lazy initialization is used - the factory is created only when first needed.
     * @return The strategy factory for the animal.
     */
    @Override
    public StrategyFactory getStrategyFactory() {
        if (strategyFactory == null) {
            strategyFactory = AnimalStrategyFactory.INSTANCE;
        }
        return strategyFactory;
    }


}
