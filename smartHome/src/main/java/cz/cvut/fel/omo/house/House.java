package cz.cvut.fel.omo.house;

import cz.cvut.fel.omo.entity.alive.AliveEntity;
import cz.cvut.fel.omo.entity.device.*;
import cz.cvut.fel.omo.entity.device.sensors.Sensor;
import cz.cvut.fel.omo.house.room.Room;
import cz.cvut.fel.omo.house.room.Window;
import org.json.JSONException;
import org.json.JSONMLParserConfiguration;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type House.
 */
public class House {

    private static House instance;

    private SmartPanel smartPanel;

    private Car car;

    private List<Device> devices;

    private final List<Device> microwaves = new ArrayList<>();

    private List<Room> rooms = new ArrayList<>();

    private final Map<AliveEntity, Room> bedrooms = new HashMap<>();

    private Room backyard;

    private List<Room> toilets;

    private double totalEnergyConsumed = 0;

    private HomeEmergencyStatus status = HomeEmergencyStatus.DEFAULT;

    private House() {
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static House getInstance() {
        if (instance == null) {
            instance = new House();
        }

        return instance;
    }

    /**
     * Assign bedroom.
     *
     * @param entity the entity
     * @param room   the room
     */
    public void assignBedroom(AliveEntity entity, Room room) {
        bedrooms.put(entity, room);
    }

    /**
     * Consume energy.
     *
     * @param watt the watt
     */
    public void consumeEnergy(double watt){
        totalEnergyConsumed += watt;
    }

    /**
     * Gets all devices.
     *
     * @return the all devices
     */
    public List<Device> getAllDevices() {
        if(devices==null){
            List<Device> roomDevices = rooms.stream()
                    .flatMap(room -> room.getDevices().stream())
                    .toList();

            List<Device> windowDevices = rooms.stream()
                    .flatMap(room -> room.getWindows().stream())
                    .map(Window::getCurtain)
                    .collect(Collectors.toList());

            devices = Stream.concat(roomDevices.stream(), windowDevices.stream())
                    .collect(Collectors.toList());
        }
        return devices;
    }

    /**
     * Get toilets list.
     *
     * @return the list
     */
    public List<Room> getToilets(){
        if(toilets == null){
            toilets = rooms.stream()
                    .filter(room -> room.getDevices()
                            .stream()
                            .anyMatch(Toilet.class::isInstance))
                    .collect(Collectors.toList());

            if(toilets.isEmpty()){
                System.err.println("House must have toilet device INSIDE HOUSE!");
            }
        }
        return toilets;
    }

    /**
     * Gets bedroom.
     *
     * @param entity the entity
     * @return the bedroom
     */
    public Room getBedroom(AliveEntity entity) {
        return bedrooms.get(entity);
    }

    /**
     * Sets rooms.
     *
     * @param rooms the rooms
     */
    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    /**
     * Add room.
     *
     * @param room the room
     */
    public void addRoom(Room room){
        rooms.add(room);
    }

    /**
     * Gets rooms.
     *
     * @return the rooms
     */
    public List<Room> getRooms() {
        return rooms;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(HomeEmergencyStatus status) {
        this.status = status;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public HomeEmergencyStatus getStatus() {
        return status;
    }

    /**
     * Gets backyard.
     *
     * @return the backyard
     */
    public Room getBackyard() {
        return backyard;
    }

    /**
     * Gets total energy consumed.
     *
     * @return the total energy consumed
     */
    public double getTotalEnergyConsumed() {
        return totalEnergyConsumed;
    }

    /**
     * Sets backyard.
     *
     * @param backyard the backyard
     */
    public void setBackyard(Room backyard) {
        this.backyard = backyard;
    }

    /**
     * Get room by name room.
     *
     * @param name the name
     * @return the room
     */
    public Room getRoomByName(String name){
        for(Room room: rooms){
            if(Objects.equals(room.getName(), name)){
                return room;
            }
        }
        return null;
    }

    /**
     * Get free microwave device.
     *
     * @return the device
     */
    public Device getFreeMicrowave(){
        for(Device microwave: microwaves){
            if(microwave.isFree()){
                return microwave;
            }
        }
        return null;
    }

    /**
     * The enum Home emergency status.
     */
    public enum HomeEmergencyStatus {

        /**
         * Default home emergency status.
         */
        DEFAULT,

        /**
         * Fire home emergency status.
         */
        FIRE,

        /**
         * Flood home emergency status.
         */
        FLOOD

    }

    /**
     * Get smart panel smart panel.
     *
     * @return the smart panel
     */
    public SmartPanel getSmartPanel(){
        if(smartPanel == null){
            throw  new RuntimeException("Smart Panel is not set");
        }
        return smartPanel;
    }

    /**
     * Set smart panel.
     *
     * @param smartPanel the smart panel
     */
    public void setSmartPanel(SmartPanel smartPanel){
        this.smartPanel = smartPanel;
    }

    /**
     * Get car car.
     *
     * @return the car
     */
    public Car getCar(){
        return car;
    }

    /**
     * Set car.
     *
     * @param car the car
     */
    public void setCar(Car car){
        this.car = car;
    }

    /**
     * Add microwave.
     *
     * @param microwave the microwave
     */
    public void addMicrowave(Device microwave) {
        microwaves.add(microwave);
    }

}
