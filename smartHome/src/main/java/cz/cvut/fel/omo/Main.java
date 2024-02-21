package cz.cvut.fel.omo;

import cz.cvut.fel.omo.timesimulator.TimeManager;
import cz.cvut.fel.omo.util.*;
import cz.cvut.fel.omo.util.logger.Logger;
import cz.cvut.fel.omo.util.logger.LoggerCleaner;

import java.util.Scanner;

/**
 * The main class of the Smart Home application.
 * Provides a user interface for selecting configuration and running the simulation.
 */
public class Main {

    /**
     * Entry point to the application.
     * Clears logs, informs the user, retrieves configuration choice, and initiates the simulation.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        // Clearing the log folder
        LoggerCleaner.clearLogsFolder("report");

        // Displaying information to the user
        notifyUser();

        // Getting the configuration file path from the user
        String jsonPath = getUserChoice();

        // Initializing and setting up the configuration
        JsonConfInit.parseAndSetUp(jsonPath);

        // Starting the Time Manager and running the simulation
        TimeManager.getInstance().run();

        // Logging the results
        Logger.toLog();
    }

    /**
     * Displays greetings and information about available devices.
     */
    private static void notifyUser() {
        greet();
        informAboutDevices();
    }

    /**
     * Retrieves the user's choice for the simulation configuration.
     *
     * @return A string containing the path to the selected configuration file.
     */
    private static String getUserChoice() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose configuration:\n1 - Large Configuration\n2 - Small Configuration");
        int choice = scanner.nextInt();

        if (choice == 1) {
            return "config/configuration_big.json";
        } else if (choice == 2) {
            return "config/configuration_small.json";
        } else {
            System.out.println("Invalid choice. Starting with the large configuration by default.");
            return "config/configuration_big.json";
        }
    }

    /**
     * Informs the user about the available types of devices for the JSON configuration.
     */
    private static void informAboutDevices() {
        StringBuilder sb = new StringBuilder();
        sb.append("Available device types for JSON: 'Microwave', 'AutomaticFeeder', 'Dishwasher', 'Fridge', 'Toilet', 'WashingMachine', 'PC', 'ElectricBicycle', 'Heater', 'SmartLitterBox', 'TV', 'GamingConsole', 'Toaster', 'CoffeeMachine'");
        System.out.println(sb.toString());
    }

    /**
     * Greets the user and provides instructions for modifying the JSON file.
     */
    private static void greet() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hello.")
                .append("\nIf you wish to change the JSON file, ensure to include a fridge and microwave. Set only one heater per room.")
                .append("\nHeaters should be located in different rooms.")
                .append("\nTo add multiple devices of the same type, use the same name and choose a room for each.")
                .append("\nPlease, only use 'dog' as an animal in your configuration, as my AnimalStrategy class is designed only for DOGS.");
        System.out.println(sb.toString());
    }
}