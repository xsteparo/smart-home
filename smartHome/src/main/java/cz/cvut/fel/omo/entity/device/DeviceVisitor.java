package cz.cvut.fel.omo.entity.device;

import cz.cvut.fel.omo.event.Event;

/**
 * Visitor interface for devices.
 * This interface allows implementing visitor classes that can perform
 * specific operations on various types of devices.
 */
public interface DeviceVisitor {

    // Visit methods for each type of device. Each method takes a specific
    // device type as a parameter and returns an EventBuilder that can
    // be used to create events related to the visited device.
    Event.EventBuilder visitAutomaticFeeder(AutomaticFeeder device);

    Event.EventBuilder visitCar(Car device);

    Event.EventBuilder visitCurtain(Curtain device);

    Event.EventBuilder visitCoffeeMachine(CoffeeMachine device);

    Event.EventBuilder visitGamingConsole(GamingConsole device);

    Event.EventBuilder visitToaster(Toaster device);

    Event.EventBuilder visitTV(TV device);

    Event.EventBuilder visitDishwasher(Dishwasher device);

    Event.EventBuilder visitElectricBicycle(ElectricBicycle device);

    Event.EventBuilder visitFridge(Fridge device);

    Event.EventBuilder visitMicrowave(Microwave device);

    Event.EventBuilder visitMobilePhone(MobilePhone device);

    Event.EventBuilder visitToilet(Toilet device);

    Event.EventBuilder visitPC(PC device);

    Event.EventBuilder visitWashingMachine(WashingMachine device);

    Event.EventBuilder visitLitterBox(SmartLitterBox smartLitterBox);
}
