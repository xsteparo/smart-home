package cz.cvut.fel.omo.util;

import cz.cvut.fel.omo.Main;
import cz.cvut.fel.omo.entity.alive.Adult;
import cz.cvut.fel.omo.entity.alive.Animal;
import cz.cvut.fel.omo.entity.alive.Child;
import cz.cvut.fel.omo.entity.device.*;
import cz.cvut.fel.omo.entity.device.decorator.FlammableDevice;
import cz.cvut.fel.omo.entity.device.decorator.FloodingDevice;
import cz.cvut.fel.omo.entity.device.decorator.MessCausingDevice;
import cz.cvut.fel.omo.entity.device.sensors.*;
import cz.cvut.fel.omo.house.House;
import cz.cvut.fel.omo.house.SmartPanel;
import cz.cvut.fel.omo.house.room.Room;
import cz.cvut.fel.omo.house.room.Window;
import cz.cvut.fel.omo.timesimulator.TimeManager;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class JsonConfInit {

    public static void parseAndSetUp(String path) {

        try {

            String jsonConfig = IOUtils.toString(
                    Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream(path)),
                    StandardCharsets.UTF_8
            );
            JSONObject config = new JSONObject(jsonConfig);
            House house = House.getInstance();

            parseRooms(config);
            Room outside = setUpOutside();
            setUpCar(outside);
            setUpBackyard();
            setUpSmartPanel(config);
            parseWindows(config);
            parseFamilyMembers(config, house);
            parseDevices(config, outside);
            setUpSensors();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Room setUpOutside() {
        Room room = new Room("Outside House");
        House.getInstance().addRoom(room);
        return room;
    }


    private static void setUpCar(Room outside) {
        Car car = new Car(outside);
        House.getInstance().setCar(car);
        outside.addDevice(car);
    }

    private static void setUpSensors() {
        Sensor smokeSensor = new SmokeSensor();
        Sensor floodSensor = new FloodSensor();
        Sensor lightSensor = new LightSensor();
        Sensor temperatureSensor = new TemperatureSensor();
        TimeManager.getInstance().subscribe(smokeSensor);
        TimeManager.getInstance().subscribe(lightSensor);
        TimeManager.getInstance().subscribe(temperatureSensor);
        TimeManager.getInstance().subscribe(floodSensor);

    }

    private static void setUpBackyard() {
        Room backyard = new Room("Backyard");
        House.getInstance().setBackyard(backyard);
    }

    private static void setUpSmartPanel(JSONObject config) {
        JSONArray rooms = config.getJSONObject("house").getJSONArray("rooms");
        JSONObject roomJson = rooms.getJSONObject(0);
        Room room = House.getInstance().getRoomByName(roomJson.getString("name"));
        SmartPanel smartPanel = new SmartPanel(room);
        House.getInstance().setSmartPanel(smartPanel);
    }

    private static void parseRooms(JSONObject config) {

        JSONArray rooms = config.getJSONObject("house").getJSONArray("rooms");

        for (int i = 0; i < rooms.length(); i++) {
            JSONObject roomJson = rooms.getJSONObject(i);
            Room room = new Room(roomJson.getString("name"));
            House.getInstance().addRoom(room);
        }

    }

    private static void parseWindows(JSONObject config) {
        JSONArray windowsConfig = config.getJSONArray("windows");

        for (int i = 0; i < windowsConfig.length(); i++) {
            JSONObject windowConfig = windowsConfig.getJSONObject(i);
            String roomName = windowConfig.getString("room");

            Room room = House.getInstance().getRoomByName(roomName);
            if (room != null) {
                Curtain curtain = new Curtain(room);
                House.getInstance().getSmartPanel().addSubscriber(curtain);

                Window window = new Window();
                window.setCurtain(curtain);
                room.addWindow(window);
            } else {
                throw new JSONException("Window can't find " + roomName + " in JSON");
            }
        }
    }

    private static void parseFamilyMembers(JSONObject config, House house) {
        JSONObject familyMembersConfig = config.getJSONObject("familyMembers");

        JSONArray adultsConfig = familyMembersConfig.getJSONArray("adults");
        for (int i = 0; i < adultsConfig.length(); i++) {
            JSONObject adultConfig = adultsConfig.getJSONObject(i);
            String name = adultConfig.getString("name");
            String sleepingRoomName = adultConfig.getString("sleepingRoom");
            Room sleepingRoom = house.getRoomByName(sleepingRoomName);
            if (sleepingRoom == null) {
                throw new JSONException("Adult " + name + " can't find " + sleepingRoomName + " in JSON");
            }
            Adult adult = new Adult(name, sleepingRoom);
            house.assignBedroom(adult, sleepingRoom);
            TimeManager.getInstance().subscribe(adult);

            House.getInstance().getSmartPanel().addSubscriber(adult);
//            House.getInstance().getSmartPanel().addSubscriber(adult.getMobilePhone());


        }

        JSONArray childrenConfig = familyMembersConfig.getJSONArray("children");
        for (int i = 0; i < childrenConfig.length(); i++) {
            JSONObject childConfig = childrenConfig.getJSONObject(i);
            String name = childConfig.getString("name");
            String sleepingRoomName = childConfig.getString("sleepingRoom");
            Room sleepingRoom = house.getRoomByName(sleepingRoomName);
            if (sleepingRoom == null) {
                throw new JSONException("Children " + name + " can 't find " + sleepingRoomName + " in JSON");
            }
            Child child = new Child(name, sleepingRoom);

            house.assignBedroom(child, sleepingRoom);
            TimeManager.getInstance().subscribe(child);

            House.getInstance().getSmartPanel().addSubscriber(child);
            House.getInstance().getSmartPanel().addSubscriber(child.getMobilePhone());
        }

        JSONArray animalsConfig = familyMembersConfig.getJSONArray("dogs");
        for (int i = 0; i < animalsConfig.length(); i++) {
            JSONObject animalConfig = animalsConfig.getJSONObject(i);
            String name = animalConfig.getString("name");
            String roomName = animalConfig.getString("room");
            Room sleepingRoom = house.getRoomByName(roomName);
            if (sleepingRoom == null) {
                throw new JSONException("Dog " + name + " can 't find " + roomName + " in JSON");
            }
            Animal animal = new Animal(name, sleepingRoom);
            house.assignBedroom(animal, sleepingRoom);

            TimeManager.getInstance().subscribe(animal);
            House.getInstance().getSmartPanel().addSubscriber(animal);
        }
    }

    private static void parseDevices(JSONObject config, Room outside) {
        JSONArray devicesConfig = config.getJSONArray("devices");

        for (int i = 0; i < devicesConfig.length(); i++) {
            JSONObject deviceConfig = devicesConfig.getJSONObject(i);
            String type = deviceConfig.getString("type");
            String roomName = deviceConfig.optString("room", null);
            Room room = roomName != null ? House.getInstance().getRoomByName(roomName) : null;

            Device device = switch (type) {
                case "Microwave" -> {
                    Device microwave = new FlammableDevice(new Microwave(room));

                    House.getInstance().addMicrowave(microwave);
                    yield microwave;
                }
                case "AutomaticFeeder" -> new AutomaticFeeder(room);
                case "Dishwasher" -> new Dishwasher(room);
                case "Fridge" -> {
                    Fridge fridge = new Fridge(room);
                    TimeManager.getInstance().subscribe(fridge);
                    yield fridge;
                }
                case "Toilet" -> new Toilet(room);
                case "WashingMachine" -> new FlammableDevice(new FloodingDevice(new WashingMachine(room)));
                case "PC" -> new FlammableDevice(new MessCausingDevice(new PC(room)));
                case "SmartLitterBox" -> new SmartLitterBox(room);
                case "TV" -> new TV(room);
                case "Toaster" -> new Toaster(room);
                case "CoffeeMachine" -> new CoffeeMachine(room);

                case "GamingConsole" -> new GamingConsole(room);

                case "ElectricBicycle" -> {
                    Device bicycle = new ElectricBicycle(outside);
                    outside.addDevice(bicycle);
                    yield bicycle;
                }
                case "Heater" -> {
                    if (room.getHeater() != null) {
                        throw new JSONException("Only one heater can be placed in room");
                    }
                    Heater heater = new Heater(room);
                    room.setHeater(heater);
                    House.getInstance().getSmartPanel().addSubscriber(heater);
                    yield heater;
                }
                default -> throw new IllegalArgumentException("Unknown device type: " + type);
            };


            if (room != null) {
                room.addDevice(device);
            } else {
                House.getInstance().getBackyard().addDevice(device);
            }
            if (type.equals("Heater")) {
                TimeManager.getInstance().subscribe(device);
                House.getInstance().getSmartPanel().addSubscriber(device);
            }
            House.getInstance().getSmartPanel().addSubscriber(device);

        }
    }
}
