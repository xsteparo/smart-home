package cz.cvut.fel.omo.event.strategy.adult;

import cz.cvut.fel.omo.entity.alive.Adult;
import cz.cvut.fel.omo.entity.alive.AliveEntity;
import cz.cvut.fel.omo.house.House;
import cz.cvut.fel.omo.entity.device.Device;
import cz.cvut.fel.omo.entity.device.DeviceStatus;
import cz.cvut.fel.omo.house.room.Room;
import cz.cvut.fel.omo.event.Event;
import cz.cvut.fel.omo.event.strategy.HumanDefaultStrategyBase;

import java.util.List;


/**
 * The type Adult default strategy.
 */
public class AdultDefaultStrategy extends HumanDefaultStrategyBase<Adult> {

    @Override
    protected List<Event> generateEvents(Adult adult) {
        if(adult.getCurrentRoom().isMessy()){
            return List.of(generateCleanRoomEvent(adult));
        }
        if(Math.random()> 0.8){
            return List.of(adult.getMobilePhone().accept(adult).build());
        }
        return super.generateEvents(adult);
    }

    private Event generateCleanRoomEvent(AliveEntity adult) {
        return Event.create()
                .isUrgent(false)
                .object(adult)
                .room(adult.getCurrentRoom())
                .remainingTime(20)
                .name("cleans messy room " + adult.getCurrentRoom().getName())
                .endFunction(() -> adult.getCurrentRoom().setMessy(false))
                .build();
    }

    @Override
    public boolean isUrgent() {
        return false;
    }

    @Override
    protected List<Event> createBeforeSleepRoutineEvents(Adult adult) {
        Room wc = getRandomToilet();
        Event brushTeeth = createBrushTeethEvent(adult, wc);
        Event readBook = createReadBookEvent(adult);
        return List.of(brushTeeth, readBook);
    }

    private Event createReadBookEvent(Adult adult) {
        Room bedroom = House.getInstance().getBedroom(adult);
        return Event.create()
                .isUrgent(false)
                .isSleeping(true)
                .name("Reading Book")
                .remainingTime(15)
                .room(bedroom)
                .object(adult)
                .build();
    }

    @Override
    protected Event createGoToSleepEvent(Adult aliveEntity) {
        Room bedroom = House.getInstance().getBedroom(aliveEntity);
        return Event.create()
                .isUrgent(false)
                .isSleeping(true)
                .name("Sleeping in bed")
                .remainingTime(500)
                .object(aliveEntity)
                .room(bedroom)
                .build();
    }

    @Override
    protected boolean skipDevice(Device device) {
        return device.getStatus() == DeviceStatus.BUSY
                || device.getStatus() == DeviceStatus.BROKEN
                || !device.canBeChosenForRandomInteraction();
    }
}
