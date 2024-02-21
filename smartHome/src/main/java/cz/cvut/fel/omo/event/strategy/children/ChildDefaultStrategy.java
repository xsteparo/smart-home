package cz.cvut.fel.omo.event.strategy.children;

import cz.cvut.fel.omo.entity.alive.Child;
import cz.cvut.fel.omo.house.House;
import cz.cvut.fel.omo.entity.device.Device;
import cz.cvut.fel.omo.entity.device.DeviceStatus;
import cz.cvut.fel.omo.house.room.Room;
import cz.cvut.fel.omo.event.Event;

import cz.cvut.fel.omo.event.strategy.HumanDefaultStrategyBase;
import cz.cvut.fel.omo.timesimulator.TimeManager;

import java.util.List;

/**
 * The type Child default strategy.
 */
public class ChildDefaultStrategy extends HumanDefaultStrategyBase<Child> {


    @Override
    protected List<Event> generateEvents(Child entity) {
        if(Math.random()> 0.7){
            return List.of(entity.getMobilePhone().accept(entity).build());
        }
        return super.generateEvents(entity);
    }

    @Override
    protected List<Event> createBeforeSleepRoutineEvents(Child aliveEntity) {
        Room wc = getRandomToilet();
        Event brushTeeth = createBrushTeethEvent(aliveEntity, wc);
        return List.of(brushTeeth);
    }

    @Override
    public boolean isUrgent() {
        return false;
    }



    @Override
    protected Event createGoToSleepEvent(Child aliveEntity) {
        Room bedroom = House.getInstance().getBedroom(aliveEntity);
        return Event.create()
                .isUrgent(false)
                .isSleeping(true)
                .name("Sleeping in bed")
                .remainingTime(600)
                .object(aliveEntity)
                .room(bedroom)
                .build();
    }

    protected boolean skipDevice(Device device) {
        return device.getStatus() == DeviceStatus.BUSY
                || device.getStatus() == DeviceStatus.BROKEN
                || !device.canBeChosenForRandomInteraction();
    }

}
