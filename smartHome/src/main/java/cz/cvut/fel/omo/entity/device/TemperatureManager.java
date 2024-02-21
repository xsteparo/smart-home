package cz.cvut.fel.omo.entity.device;

import cz.cvut.fel.omo.house.House;
import cz.cvut.fel.omo.house.room.Room;
import cz.cvut.fel.omo.event.Event;
import cz.cvut.fel.omo.util.logger.EventHistory;
import cz.cvut.fel.omo.weather.Weather;
import cz.cvut.fel.omo.weather.WeatherState;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Temperature manager.
 */
public class TemperatureManager extends Device {

    private static int entityCount = 0;

    private static final double CONSUMPTION_IN_MINUTE = 0.5;

    private List<Heater> heaters;

    /**
     * Instantiates a new Temperature manager.
     *
     * @param room the room
     */
    public TemperatureManager(Room room) {
        super(room,"TemperatureManager #" + entityCount++);
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
     * Update temperature.
     *
     * @param temperature the temperature
     */
    public void updateTemperature(int temperature) {
        int newTemperature = calculateNewTemperature(temperature);
        List<Event> events = new ArrayList<>();
        for (Heater heater : getAllHeatersInHouse()) {
            if(heater.getStatus()!=DeviceStatus.BROKEN){
                Event.EventBuilder eventBuilder = heater.getChangedDegreeEvent(newTemperature,temperature);
                if (eventBuilder != null) {
                    Event event = eventBuilder.object(this).build();
                    events.add(event);
                }
            }

        }
        initiatorEvents = events;
    }

    private int calculateNewTemperature(int temperature) {
        if (temperature < 15) {
            return 22;
        } else if (temperature < 20) {
            return 20;
        } else {
            return 18;
        }
    }

    private List<Heater> getAllHeatersInHouse(){
        if(heaters == null){
            heaters = House.getInstance().getRooms().stream()
                    .map(Room::getHeater)
                    .filter(heater -> heater!=null)
                    .collect(Collectors.toList());
        }
        return heaters;
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
