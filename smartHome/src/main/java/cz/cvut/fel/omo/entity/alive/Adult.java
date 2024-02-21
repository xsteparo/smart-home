package cz.cvut.fel.omo.entity.alive;

import cz.cvut.fel.omo.entity.device.*;
import cz.cvut.fel.omo.house.House;
import cz.cvut.fel.omo.house.room.Room;
import cz.cvut.fel.omo.event.Event;
import cz.cvut.fel.omo.event.strategy.StrategyFactory;
import cz.cvut.fel.omo.event.strategy.adult.AdultStrategyFactory;
import cz.cvut.fel.omo.timesimulator.TimeManager;
/**
 * Represents an adult entity in the simulation.
 * This class provides specific functionalities and behaviors pertinent to an adult,
 * such as the use of a mobile phone and the ability to follow unique strategies.
 */
public class Adult extends HumanEntity{

    private StrategyFactory strategyFactory;

    private final MobilePhone mobile;

    /**
     * Constructs an Adult with a specified name and initial room.
     * Initializes the mobile phone and subscribes it to the TimeManager.
     *
     * @param name The name of the adult.
     * @param room The initial room where the adult is located.
     */
    public Adult(String name, Room room) {
        super(name,  room);
        mobile = new MobilePhone(this);
        TimeManager.getInstance().subscribe(mobile);
    }

    /**
     * Provides the strategy factory specific to the Adult class.
     * Uses the Singleton pattern to ensure a single instance of the strategy factory.
     *
     * @return An instance of AdultStrategyFactory.
     */
    @Override
    public StrategyFactory getStrategyFactory() {
        if (strategyFactory == null) {
            strategyFactory = AdultStrategyFactory.INSTANCE;
        }
        return strategyFactory;
    }

    /**
     * Generates a default event without the use of any device.
     * Chooses randomly between predefined activities such as reading a book or meditating.
     *
     * @return An EventBuilder to build the event.
     */
    @Override
    public Event.EventBuilder getDefaultEventWithoutDevice() {
        String[] activities = {"Reading book", "Meditating"};
        int randomIndex = (int) (Math.random() * activities.length);
        String selectedActivity = activities[randomIndex];

        return Event.create()
                .isUrgent(false)
                .object(this)
                .name(selectedActivity)
                .remainingTime(30)
                .room(House.getInstance().getBedroom(this));
    }

    /**
     * Gets the mobile phone associated with the adult.
     *
     * @return The mobile phone of the adult.
     */
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
                .name("Refilling automated feeder " + device.getName())
                .remainingTime(200);
    }

    @Override
    public Event.EventBuilder visitCar(Car device) {
        return Event.create()
                .subject(device)
                .object(this)
                .isUrgent(false)
                .room(device.getCurrentRoom())
                .name("Driving to hiking" + device.getName())
                .remainingTime(520);
    }

    @Override
    public Event.EventBuilder visitCurtain(Curtain device) {
        int degreeDuringEvent = 90;
        return Event.create()
                .isUrgent(false)
                .subject(device)
                .object(this)
                .room(device.getCurrentRoom() )
                .name("watching through the window thinking about life, curtain degree during event: " + degreeDuringEvent + " " + device.getName())
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
                .name("Makes espresso" + device.getName())
                .remainingTime(10);
    }

    @Override
    public Event.EventBuilder visitGamingConsole(GamingConsole device) {
        return Event.create()
                .subject(device)
                .object(this)
                .isUrgent(false)
                .room(device.getCurrentRoom())
                .name("Playing Mario on " + device.getName())
                .remainingTime(40);
    }

    @Override
    public Event.EventBuilder visitToaster(Toaster device) {
        return Event.create()
                .subject(device)
                .object(this)
                .isUrgent(false)
                .room(device.getCurrentRoom())
                .name("Roasts bread in" + device.getName())
                .remainingTime(10);
    }

    @Override
    public Event.EventBuilder visitTV(TV device) {
        return Event.create()
                .subject(device)
                .object(this)
                .isUrgent(false)
                .room(device.getCurrentRoom())
                .name("Watching movie " + device.getName())
                .remainingTime(10);
    }

    @Override
    public Event.EventBuilder visitDishwasher(Dishwasher device) {
        return Event.create()
                .isUrgent(false)
                .object(this)
                .subject(device)
                .room(device.getCurrentRoom())
                .name("Washing dishes in the dishwasher " + device.getName())
                .remainingTime(20);
    }

    @Override
    public Event.EventBuilder visitElectricBicycle(ElectricBicycle device) {
        return Event.create()
                .subject(device)
                .object(this)
                .room(device.getCurrentRoom())
                .name("riding on a bike " + device.getName())
                .remainingTime(200);
    }

    @Override
    public Event.EventBuilder visitFridge(Fridge device) {
        String[] activities = {"Picking healthy food from fridge",
                "Picking soup from fridge",
                "Picking steak from fridge"
        };
        return Event.create()
                .isUrgent(false)
                .name(chooseRandomElement(activities) + " " + device.getName())
                .subject(device)
                .object(this)
                .room(device.getCurrentRoom())
                .endFunction(()->{
                    device.setRemainingFood(device.getRemainingFood()-1);
                    Device microwave = House.getInstance().getFreeMicrowave();
                    if(microwave !=null){
                        scheduleEvent(microwave.accept(this).build());
                    }
                } )
                .remainingTime(1);
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
        String[] activities = {"doomscrolling social network feed",
                "speaks with grandfather in videochat"
        };
        return Event.create()
                .subject(device)
                .isUrgent(false)
                .object(this)
                .room(device.getCurrentRoom())
                .name(chooseRandomElement(activities) +" " + device.getName())
                .remainingTime(60);
    }

    @Override
    public Event.EventBuilder visitToilet(Toilet device) {
        return Event.create()
                .isUrgent(false)
                .name("popping in a toilet " + device.getName())
                .subject(device)
                .object(this)
                .room(device.getCurrentRoom())
                .remainingTime(30);
    }

    @Override
    public Event.EventBuilder visitPC(PC device) {
        String[] activities = {"Playing Heroes of Might and Magic III on PC",
                "Working on PC"
        };
        return Event.create()
                .isUrgent(false)
                .subject(device)
                .object(this)
                .room(device.getCurrentRoom())
                .name(chooseRandomElement(activities) + " " + device.getName())
                .remainingTime(120);
    }

    @Override
    public Event.EventBuilder visitWashingMachine(WashingMachine device) {
        return Event.create()
                .isUrgent(false)
                .name("Washing dirty t-shirt in the washing machine" + device.getName())
                .object(this)
                .subject(device)
                .room(device.getCurrentRoom())
                .remainingTime(60);
    }

    @Override
    public Event.EventBuilder visitLitterBox(SmartLitterBox device) {
        return Event.create()
                .isUrgent(false)
                .name("Changes the filler in the litter box" + device.getName())
                .object(this)
                .subject(device)
                .room(device.getCurrentRoom())
                .remainingTime(10);
    }

}
