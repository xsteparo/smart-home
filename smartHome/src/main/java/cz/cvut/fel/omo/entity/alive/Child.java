package cz.cvut.fel.omo.entity.alive;

import cz.cvut.fel.omo.entity.device.*;
import cz.cvut.fel.omo.house.House;
import cz.cvut.fel.omo.house.room.Room;
import cz.cvut.fel.omo.event.Event;
import cz.cvut.fel.omo.event.strategy.StrategyFactory;
import cz.cvut.fel.omo.event.strategy.children.ChildStrategyFactory;
import cz.cvut.fel.omo.timesimulator.TimeManager;


/**
 * Represents a child entity in the simulation.
 * Provides child-specific functionalities and behaviors, including interactions with various devices.
 */
public class Child extends HumanEntity {

    private StrategyFactory strategyFactory;

    private final MobilePhone mobile;

    /**
     * Constructs a Child with a specified name and initial room.
     * Initializes the mobile phone and subscribes it to the TimeManager.
     *
     * @param name The name of the child.
     * @param room The initial room where the child is located.
     */
    public Child(String name, Room room) {
        super(name, room);
        mobile = new MobilePhone(this);
        TimeManager.getInstance().subscribe(mobile);
    }

    /**
     * Provides the strategy factory specific to the Child class.
     * Uses the Singleton pattern to ensure a single instance of the strategy factory.
     *
     * @return An instance of ChildStrategyFactory.
     */
    public StrategyFactory getStrategyFactory() {
        if (strategyFactory == null) {
            strategyFactory = ChildStrategyFactory.INSTANCE;
        }
        return strategyFactory;
    }

    /**
     * Generates a default event without the use of any device.
     * Chooses randomly between predefined activities such as playing with toys or drawing.
     *
     * @return An EventBuilder to build the event.
     */
    @Override
    public Event.EventBuilder getDefaultEventWithoutDevice() {
        String[] activities = {"Playing with toys", "Drawing"};

        return Event.create()
                .isUrgent(false)
                .object(this)
                .name(chooseRandomElement(activities))
                .remainingTime(30)
                .room(House.getInstance().getBedroom(this));
    }


    @Override
    public MobilePhone getMobilePhone() {
        return mobile;
    }

    @Override
    public Event.EventBuilder visitAutomaticFeeder(AutomaticFeeder device) {
        return Event.create()
                .subject(device)
                .isUrgent(false)
                .object(this)
                .room(device.getCurrentRoom())
                .name("Refilling automated feeder " + device.getName() )
                .remainingTime(200);
    }


    @Override
    public Event.EventBuilder visitCar(Car device) {
        return Event.create()
                .subject(device)
                .object(this)
                .isUrgent(false)
                .room(device.getCurrentRoom())
                .name("Listening to music in the car "+ device.getName())
                .remainingTime(520);
    }

    @Override
    public Event.EventBuilder visitCurtain(Curtain device) {
        int degreeDuringEvent = 90;
        return Event.create()
                .isUrgent(false)
                .subject(device)
                .object(this)
                .room(device.getCurrentRoom())
                .name("watching friends playing outside from the window, curtain degree during event: " + degreeDuringEvent + " " + device.getName())
                .remainingTime(10)
                .endFunction(() -> device.setDegree(degreeDuringEvent));
    }

    @Override
    public Event.EventBuilder visitCoffeeMachine(CoffeeMachine device) {
        return Event.create()
                .subject(device)
                .object(this)
                .isUrgent(false)
                .room(device.getCurrentRoom())
                .name("Makes cappuccino in " + device.getName())
                .remainingTime(10);
    }

    @Override
    public Event.EventBuilder visitGamingConsole(GamingConsole device) {
        return Event.create()
                .subject(device)
                .object(this)
                .isUrgent(false)
                .room(device.getCurrentRoom())
                .name("Playing Dark Souls 1 on " + device.getName())
                .remainingTime(120);
    }

    @Override
    public Event.EventBuilder visitToaster(Toaster device) {
        return Event.create()
                .subject(device)
                .object(this)
                .isUrgent(false)
                .room(device.getCurrentRoom())
                .name("Roasts bread in " + device.getName())
                .remainingTime(10);
    }

    @Override
    public Event.EventBuilder visitTV(TV device) {
        return Event.create()
                .subject(device)
                .object(this)
                .isUrgent(false)
                .room(device.getCurrentRoom())
                .name("Watching National Geographic on " + device.getName())
                .remainingTime(10);
    }

    @Override
    public Event.EventBuilder visitDishwasher(Dishwasher device) {
        String[] activities = {"Washing pots in dishwasher",
                "Washing plates in dishwasher",
                "Washing mugs in dishwasher"
        };
        return Event.create()
                .isUrgent(false)
                .subject(device)
                .object(this)
                .room(device.getCurrentRoom())
                .remainingTime(30)
                .name(chooseRandomElement(activities) + " " + device.getName());
    }

    @Override
    public Event.EventBuilder visitElectricBicycle(ElectricBicycle device) {
        String[] activities = {"Doing tricks on bicycle",
                "riding on a bicycle"
        };

        return Event.create()
                .isUrgent(false)
                .subject(device)
                .object(this)
                .room(device.getCurrentRoom())
                .remainingTime(30)
                .name(chooseRandomElement(activities) + " " + device.getName());
    }

    @Override
    public Event.EventBuilder visitFridge(Fridge device) {
        String[] activities = {"Picking burger from fridge",
                "Picking cake from fridge",
                "Picking meet with potatoes from fridge"
        };
        return Event.create()
                .isUrgent(false)
                .subject(device)
                .object(this)
                .room(device.getCurrentRoom())
                .remainingTime(1)
                .name(chooseRandomElement(activities) + " " + device.getName())
                .endFunction(() -> {
                    device.setRemainingFood(device.getRemainingFood() - 1);
                    Device microwave = House.getInstance().getFreeMicrowave();
                    if(microwave!=null){
                        scheduleEvent(microwave.accept(this).build());
                    }
                });
    }

    @Override
    public Event.EventBuilder visitMicrowave(Microwave device) {
        return Event.create()
                .isUrgent(false)
                .object(this)
                .name("heating up food in the microwave " + device.getName())
                .subject(device)
                .room(device.getCurrentRoom())
                .remainingTime(1);
    }

    @Override
    public Event.EventBuilder visitMobilePhone(MobilePhone device) {
        String[] activities = {"Playing chess on phone",
                "Listens music on Spotify phone",
                "Watching YouTube on phone"
        };

        return Event.create()
                .isUrgent(false)
                .subject(device)
                .object(this)
                .room(device.getCurrentRoom())
                .remainingTime(20)
                .name(chooseRandomElement(activities) + " " + device.getName());
    }

    @Override
    public Event.EventBuilder visitToilet(Toilet device) {
        String[] activities = {"Pissing in the toilet",
                "Pooping in the toilet",
        };

        return Event.create()
                .isUrgent(false)
                .subject(device)
                .object(this)
                .room(device.getCurrentRoom())
                .remainingTime(10)
                .name(chooseRandomElement(activities) + " " + device.getName());
    }

    @Override
    public Event.EventBuilder visitPC(PC device) {
        String[] activities = {"Playing Dota 2 on PC",
                "Implementing OMO semestral project on PC",
                "Works in Blender on PC",
                "Creating music in FL Studio"
        };

        return Event.create()
                .isUrgent(false)
                .subject(device)
                .object(this)
                .room(device.getCurrentRoom())
                .remainingTime(60)
                .name(chooseRandomElement(activities) + " " + device.getName());
    }

    @Override
    public Event.EventBuilder visitWashingMachine(WashingMachine device) {
        return Event.create()
                .isUrgent(false)
                .subject(device)
                .object(this)
                .room(device.getCurrentRoom())
                .name("Washing his sport suit into washing machine" + device.getName())
                .remainingTime(10);
    }

    @Override
    public Event.EventBuilder visitLitterBox(SmartLitterBox device) {
        return Event.create()
                .isUrgent(false)
                .name("Turned on the odor destruction mode on " + device.getName())
                .object(this)
                .subject(device)
                .room(device.getCurrentRoom())
                .remainingTime(10);
    }


}
