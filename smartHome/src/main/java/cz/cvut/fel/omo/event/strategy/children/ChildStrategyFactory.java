package cz.cvut.fel.omo.event.strategy.children;

import cz.cvut.fel.omo.entity.alive.AliveEntity;
import cz.cvut.fel.omo.event.strategy.EventGeneratingStrategy;
import cz.cvut.fel.omo.event.strategy.StrategyFactory;

/**
 * A factory for creating and managing strategies for child entities.
 * Implements lazy initialization for each strategy type.
 */
public class ChildStrategyFactory implements StrategyFactory {

    /**
     * The constant INSTANCE.
     */
    public static ChildStrategyFactory INSTANCE = new ChildStrategyFactory();

    private ChildDefaultStrategy defaultStrategy;

    private ChildFireStrategyNew fireStrategy;

    private ChildFloodStrategy floodStrategy;

    private ChildStrategyFactory() {}


    /**
     * Provides the default strategy for child entities.
     * Implements lazy initialization to ensure the strategy is created only when needed.
     *
     * @return The default strategy for child.
     */
    @Override
    public EventGeneratingStrategy getDefaultStrategy() {
        return defaultStrategy == null ? defaultStrategy = new ChildDefaultStrategy() : defaultStrategy;
    }

    /**
     * Provides the default strategy for child entities.
     * Implements lazy initialization to ensure the strategy is created only when needed.
     *
     * @return The fire strategy for child.
     */
    @Override
    public EventGeneratingStrategy getFireStrategy() {
        return fireStrategy == null ? fireStrategy = new ChildFireStrategyNew() : fireStrategy;
    }

    /**
     * Provides the default strategy for child entities.
     * Implements lazy initialization to ensure the strategy is created only when needed.
     *
     * @return The flood strategy for child.
     */
    @Override
    public EventGeneratingStrategy getFloodStrategy() {
        return floodStrategy == null ? floodStrategy = new ChildFloodStrategy() : floodStrategy;
    }
}
