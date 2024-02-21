package cz.cvut.fel.omo.house;

import cz.cvut.fel.omo.entity.EmergencyAwareEntity;
import cz.cvut.fel.omo.entity.alive.Adult;
import cz.cvut.fel.omo.entity.device.CurtainManager;
import cz.cvut.fel.omo.entity.device.TemperatureManager;
import cz.cvut.fel.omo.house.room.Room;
import cz.cvut.fel.omo.timesimulator.TimeManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the central smart control panel of the house.
 * It manages various smart devices and handles emergency statuses.
 */
public class SmartPanel {

    private final List<EmergencyAwareEntity> subscribers = new ArrayList<>();

    private final Room currentRoom;

    private CurtainManager curtainManager;

    private TemperatureManager temperatureManager;

    private House.HomeEmergencyStatus homeEmergencyStatus;

    private final List<Adult> adults = new ArrayList<>();

    /**
     * Instantiates a new Smart panel.
     *
     * @param room the room
     */
    public SmartPanel(Room room) {
        currentRoom = room;
        this.homeEmergencyStatus = House.HomeEmergencyStatus.DEFAULT;
        initManagers();
    }

    /**
     * Adds a new subscriber to the SmartPanel.
     * If the subscriber is an Adult, it is also added to the list of adults.
     *
     * @param subscriber The entity to be added as a subscriber.
     */
    public void addSubscriber(EmergencyAwareEntity subscriber) {
        subscribers.add(subscriber);
        if(subscriber instanceof  Adult){
            adults.add((Adult)subscriber);
        }
    }

    /**
     * Notifies all subscribers about the current emergency status.
     */
    public void notifySubscribers() {
        for (EmergencyAwareEntity s : subscribers) {
            s.updateEmergencyStatus(homeEmergencyStatus);
        }
    }

    /**
     * Updates the emergency status of the house and notifies subscribers.
     *
     * @param status The new emergency status to be set.
     */
    public void updateStatus(House.HomeEmergencyStatus status) {
        if (this.homeEmergencyStatus != status) {
            this.homeEmergencyStatus = status;
            notifySubscribers();
        }
    }

    private void initManagers(){
        curtainManager = new CurtainManager(currentRoom);
        curtainManager.setName("Curtain Manager");
        addSubscriber(curtainManager);
        TimeManager.getInstance().subscribe(curtainManager);
        currentRoom.addDevice(curtainManager);

        temperatureManager = new TemperatureManager(currentRoom);
        temperatureManager.setName("temperature Manager");
        addSubscriber(temperatureManager);
        TimeManager.getInstance().subscribe(temperatureManager);
        currentRoom.addDevice(temperatureManager);
    }

    /**
     * Updates the light intensity inside the house.
     * This operation is only performed if the house is not in an emergency status.
     *
     * @param intensity The new light intensity to be set.
     */
    public void updateLight(int intensity) {
        if(homeEmergencyStatus != House.HomeEmergencyStatus.DEFAULT){
            return;
        }
        curtainManager.updateLight(intensity);
    }

    /**
     * Updates the external temperature and adjusts the temperature inside the house accordingly.
     * This operation is only performed if the house is not in an emergency status.
     *
     * @param temperature The new external temperature.
     */
    public void updateTemperatureOutside(int temperature) {
        if(homeEmergencyStatus != House.HomeEmergencyStatus.DEFAULT){
            return;
        }
        temperatureManager.updateTemperature(temperature);
    }

    /**
     * Get adults list.
     *
     * @return the list
     */
    public  List<Adult> getAdults(){
        return Collections.unmodifiableList(adults);
    }

    /**
     * Gets home emergency status.
     *
     * @return the home emergency status
     */
    public House.HomeEmergencyStatus getHomeEmergencyStatus() {
        return homeEmergencyStatus;
    }

}
