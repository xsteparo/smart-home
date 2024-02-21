package cz.cvut.fel.omo.timesimulator;

/**
 * The TimePublisher interface defines methods for subscribing, unsubscribing, and notifying subscribers
 * about simulation time updates.
 */
public interface TimePublisher {

    /**
     * Subscribes a SimulationMember to receive simulation time updates.
     *
     * @param subscriber The SimulationMember to subscribe.
     */
    void subscribe(SimulationMember subscriber);

    /**
     * Unsubscribes a SimulationMember from receiving simulation time updates.
     *
     * @param subscriber The SimulationMember to unsubscribe.
     */
    void unsubscribe(SimulationMember subscriber);

    /**
     * Notifies all subscribed SimulationMembers about the current simulation time.
     */
    void notifySubscribers();
}