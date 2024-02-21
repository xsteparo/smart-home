package cz.cvut.fel.omo.util.logger;


import cz.cvut.fel.omo.entity.Entity;
import cz.cvut.fel.omo.entity.alive.AliveEntity;
import cz.cvut.fel.omo.entity.device.Curtain;
import cz.cvut.fel.omo.entity.device.Device;
import cz.cvut.fel.omo.entity.device.Heater;
import cz.cvut.fel.omo.entity.device.sensors.Sensor;
import cz.cvut.fel.omo.event.Event;
import cz.cvut.fel.omo.house.House;
import cz.cvut.fel.omo.timesimulator.TimeManager;
import cz.cvut.fel.omo.weather.WeatherState;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Event history.
 */
public class EventHistory {

    private static final Logger eventLogger = Logger.getLogger("eventLogger");

    private static final Logger activityLogger = Logger.getLogger("activityLogger");

    private static final Logger sensorActivityLogger = Logger.getLogger("sensorActivityLogger");

    private static final Logger managersActivityLogger = Logger.getLogger("managersActivityLogger");

    private static final Logger weatherLogger = Logger.getLogger("weatherLogger");


    private static final Logger breakableDeviceLogger = Logger.getLogger("breakableDeviceLogger");

    private static final Logger consoleLogger = Logger.getLogger("consoleLogger");


    private int weatherChangeCount = 0;

    private final Map<Entity, Map<String, Integer>> entityDeviceUsage = new HashMap<>();

    private static final EventHistory instance = new EventHistory();

    private EventHistory() {
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static EventHistory getInstance() {
        return instance;
    }


    private void buildMessage(Event event, StringBuilder logMessage) {
        logMessage.append(event.getStatus());
        logMessage.append("): ");
        logMessage.append(event.getName());
        logMessage.append(" |OBJECT: ");
        logMessage.append(event.getObject().getName());
        logMessage.append(" ROOM: ");
        logMessage.append(event.getObject().getCurrentRoom().getName());

        if (event.getSubject() != null) {
            logMessage.append(" |SUBJECT: ").append(event.getSubject().getName());
        }

        logMessage.append(" |HOME STATUS: ");
        logMessage.append(House.getInstance().getStatus());

        eventLogger.info(logMessage.toString());
        consoleLogger.info(logMessage.toString());
    }

    /**
     * Log event start.
     *
     * @param event the event
     */
    public void logEventStart(Event event) {
        Entity entity = event.getObject();
        String eventName = event.getName();

        entityDeviceUsage.putIfAbsent(entity, new HashMap<>());
        Map<String, Integer> eventUsage = entityDeviceUsage.get(entity);
        eventUsage.put(eventName, eventUsage.getOrDefault(eventName, 0) + 1);

        StringBuilder logMessage = new StringBuilder();
        logMessage.append(TimeManager.getInstance().getCurrentTimeFormatted());
        logMessage.append("***").append(event.getObject().getName()).append("***");
        logMessage.append(" STARTED EVENT(").append(event.getStatus()).append("): ");
        logMessage.append(event.getName());
        logMessage.append(" |OBJECT:").append(event.getObject().getName());
        logMessage.append(" ROOM:").append(event.getObject().getCurrentRoom().getName());

        if (event.getSubject() != null) {
            logMessage.append(" |SUBJECT:").append(event.getSubject().getName());
            logMessage.append(" SUBJECT ROOM:").append(event.getSubject().getCurrentRoom().getName());
        }

        eventLogger.info(logMessage.toString());
        consoleLogger.info(logMessage.toString());

    }

    /**
     * Log event resume.
     *
     * @param event the event
     */
    public void logEventResume(Event event) {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append(TimeManager.getInstance().getCurrentTimeFormatted());
        logMessage.append(" ***");
        logMessage.append(event.getObject().getName());
        logMessage.append("*** RESUMED EVENT(");
        buildMessage(event, logMessage);
    }


    /**
     * Log event end.
     *
     * @param event the event
     */
    public void logEventEnd(Event event) {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append(TimeManager.getInstance().getCurrentTimeFormatted());
        logMessage.append(" ***");
        logMessage.append(event.getObject().getName());
        logMessage.append("*** FINISHED EVENT(");
        buildMessage(event, logMessage);
    }

    /**
     * Log event interrupted.
     *
     * @param event the event
     */
    public void logEventInterrupted(Event event) {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append(TimeManager.getInstance().getCurrentTimeFormatted());
        logMessage.append(" ***").append(event.getObject().getName()).append("*** INTERRUPTED EVENT(")
                .append(event.getStatus()).append("): ").append(event.getName())
                .append(" |OBJECT: ").append(event.getObject().getName()).append(" ROOM: ")
                .append(event.getObject().getCurrentRoom().getName());

        if (event.getSubject() != null) {
            logMessage.append(" |SUBJECT: ").append(event.getSubject().getName());
            logMessage.append(" SUBJECT ROOM: ").append(event.getSubject().getCurrentRoom().getName());
            logMessage.append(" STATUS: ").append(event.getSubject().getStatus());
        }

        logMessage.append(" |HOME STATUS: ").append(House.getInstance().getStatus());

        eventLogger.info(logMessage.toString());
        consoleLogger.info(logMessage.toString());
    }

    /**
     * Log device breakdown.
     *
     * @param device the device
     * @param person the person
     */
    public void logDeviceBreakdown(Device device, AliveEntity person) {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append(TimeManager.getInstance().getCurrentTimeFormatted())
                .append(" ***").append(person.getName()).append("*** BROKE DOWN EVENT: ")
                .append(" |OBJECT: ").append(person.getName()).append(" ROOM: ").append(person.getCurrentRoom().getName())
                .append(" |SUBJECT: ").append(device.getName()).append(" ROOM: ")
                .append(device.getCurrentRoom().getName()).append(" STATUS: ").append(device.getStatus())
                .append(" |HOME STATUS: ").append(House.getInstance().getStatus());

        eventLogger.info(logMessage.toString());
        breakableDeviceLogger.info(logMessage.toString());
        consoleLogger.info(logMessage.toString());
    }

    /**
     * Log device repaired.
     *
     * @param device   the device
     * @param repairer the repairer
     */
    public void logDeviceRepaired(Device device, AliveEntity repairer) {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append(TimeManager.getInstance().getCurrentTimeFormatted())
                .append(" ***").append(repairer.getName()).append("*** REPAIRED ")
                .append(device.getClass().getSimpleName()).append(" BY: ").append(repairer.getName())
                .append(" |OBJECT: ").append(repairer.getName()).append(" ROOM: ").append(device.getCurrentRoom().getName())
                .append(" |SUBJECT: ").append(device.getName()).append(" ROOM: ")
                .append(" |HOME STATUS: ").append(House.getInstance().getStatus());

        eventLogger.info(logMessage.toString());
        consoleLogger.info(logMessage.toString());
    }

    /**
     * Log trouble.
     *
     * @param trouble       the trouble
     * @param device        the device
     * @param initiatorName the initiator name
     */
    public void logTrouble(String trouble, Device device, String initiatorName) {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append(TimeManager.getInstance().getCurrentTimeFormatted())
                .append(" ").append(trouble).append(" RANDOM EVENT DURING ").append(initiatorName).append(" INTERACTION WITH ").append(device.getName());

        eventLogger.info(logMessage.toString());
        breakableDeviceLogger.info(logMessage.toString());
        consoleLogger.info(logMessage.toString());
    }

    /**
     * Log weather.
     *
     * @param weatherState the weather state
     */
    public void logWeather(WeatherState weatherState) {
        weatherChangeCount++;

        StringBuilder logMessage = new StringBuilder();
        logMessage.append(TimeManager.getInstance().getCurrentTimeFormatted())
                .append(" Current weather: ")
                .append(weatherState.getClass().getSimpleName());

        eventLogger.info(logMessage.toString());
        managersActivityLogger.info(logMessage.toString());
        weatherLogger.info(logMessage.toString());
        consoleLogger.info(logMessage.toString());
    }

    /**
     * Log sensor event.
     *
     * @param sensor the sensor
     * @param status the status
     */
    public void logSensorEvent(Sensor sensor, House.HomeEmergencyStatus status) {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append(TimeManager.getInstance().getCurrentTimeFormatted())
                .append(" ").append(sensor.getClass().getSimpleName())
                .append(" detected: ").append(status);

        eventLogger.info(logMessage.toString());
        sensorActivityLogger.info(logMessage.toString());
        consoleLogger.info(logMessage.toString());
    }

    /**
     * Log no food in fridge.
     *
     * @param device the device
     */
    public void logNoFoodInFridge(Device device) {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append(TimeManager.getInstance().getCurrentTimeFormatted())
                .append(" ").append(device.getClass().getName())
                .append(" HAS NO FOOD");

        eventLogger.info(logMessage.toString());
        consoleLogger.info(logMessage.toString());
    }

    /**
     * Log new temperature.
     *
     * @param outsideTemperature the outside temperature
     * @param heater             the heater
     */
    public void logNewTemperature(int outsideTemperature, Heater heater) {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append(TimeManager.getInstance().getCurrentTimeFormatted())
                .append(" Was notified by Temperature Manager, new outside temperature border: ")
                .append(outsideTemperature)
                .append("; ").append(heater.getName());

        eventLogger.info(logMessage.toString());
        managersActivityLogger.info(logMessage.toString());
        consoleLogger.info(logMessage.toString());
    }

    /**
     * Log new intensity.
     *
     * @param curtain   the curtain
     * @param intensity the intensity
     */
    public void logNewIntensity( Curtain curtain, int intensity) {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append(TimeManager.getInstance().getCurrentTimeFormatted())
                .append(" Was notified by Curtain Manager, new outside light intensity border: ")
                .append(intensity)
                .append("%")
                .append("; ").append(curtain.getName());

        eventLogger.info(logMessage.toString());
        managersActivityLogger.info(logMessage.toString());
        consoleLogger.info(logMessage.toString());
    }

    /**
     * Print entity event usage report.
     */
    public void printEntityEventUsageReport() {
        StringBuilder logMessage = new StringBuilder();
        for (Map.Entry<Entity, Map<String, Integer>> entry : entityDeviceUsage.entrySet()) {
            logMessage.append("Entity: ").append(entry.getKey().getName()).append(" participated in the following events:\n");
            for (Map.Entry<String, Integer> eventEntry : entry.getValue().entrySet()) {
                logMessage.append("    - Event: ").append(eventEntry.getKey())
                        .append(", Times participated: ").append(eventEntry.getValue()).append("\n");
            }
        }
        activityLogger.info(logMessage.toString());
    }

    /**
     * Print weather change count.
     */
    public void printWeatherChangeCount() {
        activityLogger.info("Total weather changes: " + weatherChangeCount);
    }


}