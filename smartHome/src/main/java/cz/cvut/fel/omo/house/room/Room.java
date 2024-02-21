package cz.cvut.fel.omo.house.room;

import cz.cvut.fel.omo.entity.device.Device;
import cz.cvut.fel.omo.entity.device.Heater;
import cz.cvut.fel.omo.entity.device.sensors.Sensor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The type Room.
 */
public class Room {

    /**
     * The Sensors.
     */
    protected List<Sensor> sensors = new ArrayList<>();

    /**
     * The Devices.
     */
    protected Set<Device> devices = new HashSet<>();

    private List<Window> windows = new ArrayList<>();

    private final String name;

    private boolean messy;

    private Heater heater;

    /**
     * Instantiates a new Room.
     *
     * @param name the name
     */
    public Room(String name) {
        this.name = name;
    }

    /**
     * Sets devices.
     *
     * @param devices the devices
     */
    public void setDevices(Set<Device> devices) {
        this.devices = devices;
    }

    /**
     * Gets devices.
     *
     * @return the devices
     */
    public Set<Device> getDevices() {
        return devices;
    }

    /**
     * Add device.
     *
     * @param device the device
     */
    public void addDevice(Device device){
        devices.add(device);
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets messy.
     *
     * @param value the value
     */
    public void setMessy(boolean value) {
        messy = value;
    }

    /**
     * Gets windows.
     *
     * @return the windows
     */
    public List<Window> getWindows() {
        return windows;
    }

    /**
     * Add window.
     *
     * @param window the window
     */
    public void addWindow(Window window){
        windows.add(window);
    }

    /**
     * Is messy boolean.
     *
     * @return the boolean
     */
    public boolean isMessy() {
        return messy;
    }

    /**
     * Set heater room.
     *
     * @param heater the heater
     * @return the room
     */
    public Room setHeater(Heater heater){
        this.heater = heater;
        return this;
    }

    /**
     * Get heater.
     *
     * @return the heater
     */
    public Heater getHeater(){
        return heater;
    }
}
