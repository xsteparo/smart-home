package cz.cvut.fel.omo.event.strategy.adult;

import cz.cvut.fel.omo.entity.alive.Adult;
import cz.cvut.fel.omo.entity.alive.AliveEntity;
import cz.cvut.fel.omo.event.strategy.EventGeneratingStrategy;
import cz.cvut.fel.omo.event.strategy.StrategyFactory;

/**
 * A factory for creating and managing strategies for adult entities.
 * Implements lazy initialization for each strategy type.
 */
public class AdultStrategyFactory implements StrategyFactory {

    public static AdultStrategyFactory INSTANCE = new AdultStrategyFactory();

    private  AdultDefaultStrategy defaultStrategy;

    private AdultFireStrategy fireStrategy;

    private AdultFloodStrategy floodStrategy;

    private AdultStrategyFactory() {}

    /**
     * Returns the singleton instance of the AdultStrategyFactory.
     *
     * @return AdultStrategyFactory instance.
     */
    public static AdultStrategyFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Provides the default strategy for adult entities.
     * Implements lazy initialization to ensure the strategy is created only when needed.
     *
     * @return The default strategy for adults.
     */
    @Override
    public EventGeneratingStrategy getDefaultStrategy() {
        return  defaultStrategy == null ? defaultStrategy = new AdultDefaultStrategy() : defaultStrategy;
    }

    /**
     * Provides the fire strategy for adult entities.
     * Implements lazy initialization to ensure the strategy is created only when needed.
     *
     * @return The fire strategy for adults.
     */
    @Override
    public EventGeneratingStrategy getFireStrategy() {
        return fireStrategy == null ? fireStrategy = new AdultFireStrategy() : fireStrategy;
    }

    /**
     * Provides the flood strategy for adult entities.
     * Implements lazy initialization to ensure the strategy is created only when needed.
     *
     * @return The flood strategy for adults.
     */
    @Override
    public EventGeneratingStrategy getFloodStrategy() {
        return floodStrategy == null ? floodStrategy = new AdultFloodStrategy() : floodStrategy;
    }
}
