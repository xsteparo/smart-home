package cz.cvut.fel.omo.timesimulator;


import cz.cvut.fel.omo.house.House;

import java.util.ArrayList;
import java.util.List;

/**
 * The TimeManager class manages the simulation time, allowing entities to subscribe and receive simulation updates.
 * It controls the flow of time within the simulation and notifies subscribers of each simulation step.
 */
public class TimeManager implements TimePublisher, Runnable {

    private final List<SimulationMember> subscribers = new ArrayList<>();

    private final int step;

    private int currentTime;

    private final int finishTime;

    private static TimeManager instance;

    /**
     * Constructs a TimeManager instance with the specified time step and finish time.
     *
     * @param step       The time step in minutes.
     * @param finishTime The finish time when the simulation should end.
     */
    private TimeManager(int step, int finishTime) {
        this.step = step;
        currentTime = 0;
        this.finishTime = finishTime;
    }

    /**
     * Gets the singleton instance of the TimeManager with default parameters.
     *
     * @return The TimeManager instance.
     */
    public static TimeManager getInstance() {
        if (instance == null) {
            instance = new TimeManager(10, 50000);
        }
        return instance;
    }

    /**
     * Subscribes a simulation member to receive simulation updates.
     *
     * @param subscriber The SimulationMember to subscribe.
     */
    @Override
    public void subscribe(SimulationMember subscriber) {
        subscribers.add(subscriber);
    }

    /**
     * Unsubscribes a simulation member from receiving simulation updates.
     *
     * @param subscriber The SimulationMember to unsubscribe.
     */
    @Override
    public void unsubscribe(SimulationMember subscriber) {
        subscribers.remove(subscriber);
    }

    /**
     * Notifies all subscribed SimulationMembers of the current simulation state.
     */
    @Override
    public void notifySubscribers() {
        for (SimulationMember s : subscribers) {
            s.doStep(getDayTimeInMinutes());
        }
    }

    /**
     * Executes the simulation loop, advancing time and notifying subscribers until the finish time is reached.
     */
    @Override
    public void run() {
        while (currentTime <= finishTime) {
            notifySubscribers();
            incrementTime();
        }
    }

    /**
     * Sleeping time boolean.
     *
     * @return the boolean
     */
    public boolean sleepingTime() {
        return getDayTimeInMinutes() >= 1400 || getDayTimeInMinutes() < 380;
    }

    /**
     * Gets day time in minutes.
     *
     * @return the day time in minutes
     */
    public int getDayTimeInMinutes() {
        return (currentTime % 1440);
    }

    /**
     * Increment time.
     */
    public void incrementTime() {
        currentTime += step;
    }

    /**
     * Gets current time formatted.
     *
     * @return the current time formatted
     */
    public String getCurrentTimeFormatted() {
        int day = currentTime / 1440;
        int hours = (currentTime % 1440) / 60;
        int minute = (currentTime % 1440) % 60;
        return "|DAY: " + day + "| " + "HOUR : " + hours + "| " + "MINUTE : " + minute + "|";
    }

    /**
     * Gets step.
     *
     * @return the step
     */
    public int getStep() {
        return step;
    }

    /**
     * Gets current time.
     *
     * @return the current time
     */
    public int getCurrentTime() {
        return currentTime;
    }

    /**
     * Sets current time.
     *
     * @param currentTime the current time
     */
    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

}
