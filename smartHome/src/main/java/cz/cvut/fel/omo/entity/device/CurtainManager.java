package cz.cvut.fel.omo.entity.device;

import cz.cvut.fel.omo.house.House;
import cz.cvut.fel.omo.house.room.Room;
import cz.cvut.fel.omo.house.room.Window;
import cz.cvut.fel.omo.event.Event;
import cz.cvut.fel.omo.timesimulator.TimeManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Curtain manager.
 */
public class CurtainManager extends Device{

    private static int entityCount = 0;

    private static final double CONSUMPTION_IN_MINUTE = 1;

    private List<Window> windows;

    /**
     * Instantiates a new Curtain manager.
     *
     * @param room the room
     */
    public CurtainManager(Room room) {
        super(room, "CurtainManager #" + entityCount++);
        canBeChosenForRandomInteraction = false;
    }

    @Override
    public void doStep(int context) {
        if(status!=DeviceStatus.BROKEN){
            consumeEnergy();
        };
        super.doStep(context);
    }

    /**
     * Update light.
     *
     * @param intensity the intensity
     */
    public void updateLight(int intensity) {
        double curtainRotationDegree = 180 - (((double) intensity / 100) * 90);
        int degree = isSleepingTime() ? 180 : round(curtainRotationDegree);

        List<Event> events = new ArrayList<>();
        for (Window w : getAllWindowsInHouse()) {
            Event.EventBuilder eventBuilder = w.getCurtain().getChangedDegreeEvent(degree,intensity);
            if (eventBuilder != null) {
                Event event = eventBuilder.object(this).build();

                events.add(event);
            }
        }
        initiatorEvents = events;
    }

    private List<Window> getAllWindowsInHouse(){
        if(windows == null){
            windows = House.getInstance().getRooms().stream()
                    .flatMap(room -> room.getWindows().stream())
                    .collect(Collectors.toList());
        }
        return windows;
    }

    private int round(double degree) {
        if (degree <= 180 && degree > 160) {
            return 180;
        } else if (degree <= 160 && degree > 140) {
            return 150;
        } else if (degree <= 140 && degree >= 120) {
            return 120;
        }
        return 90;
    }

    private boolean isSleepingTime(){
        return TimeManager.getInstance().sleepingTime();
    }

    @Override
    public double getDefaultConsumptionInMinute() {
        return CONSUMPTION_IN_MINUTE;
    }


    @Override
    public Event.EventBuilder accept(DeviceVisitor visitor) {
        // Implementation for accepting a device visitor
        return null;
    }
}
