package cz.cvut.fel.omo.timesimulator;

/**
 * The SimulationMember interface represents entities that can participate in a simulation and perform simulation steps.
 * Classes implementing this interface are expected to define the behavior of entities within a simulation.
 */
public interface SimulationMember {

    /**
     * Performs a simulation step for the entity.
     *
     * @param context An integer value representing the context or state of the simulation.
     */
    void doStep(int context);

}
