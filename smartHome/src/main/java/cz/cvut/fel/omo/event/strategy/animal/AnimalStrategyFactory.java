package cz.cvut.fel.omo.event.strategy.animal;

import cz.cvut.fel.omo.event.strategy.EventGeneratingStrategy;
import cz.cvut.fel.omo.event.strategy.StrategyFactory;
import cz.cvut.fel.omo.event.strategy.adult.AdultDefaultStrategy;

/**
 * A factory for creating and managing strategies for animal entities.
 * Implements lazy initialization for each strategy type.
 */
public class AnimalStrategyFactory implements StrategyFactory {

    /**
     * The constant INSTANCE.
     */
    public static final AnimalStrategyFactory INSTANCE = new AnimalStrategyFactory();

    private AnimalDefaultStrategy defaultStrategy;

    private AnimalFireStrategy fireStrategy;

    private AnimalFloodStrategy floodStrategy;

    private AnimalStrategyFactory() {}

    /**
     * Provides the default strategy for animal entities.
     * Implements lazy initialization to ensure the strategy is created only when needed.
     *
     * @return The default strategy for animal.
     */
    @Override
    public EventGeneratingStrategy getDefaultStrategy() {
        return  defaultStrategy == null ? defaultStrategy = new AnimalDefaultStrategy() : defaultStrategy;
    }

    /**
     * Provides the default strategy for animal entities.
     * Implements lazy initialization to ensure the strategy is created only when needed.
     *
     * @return The fire strategy for animal.
     */
    @Override
    public EventGeneratingStrategy getFireStrategy() {
        return fireStrategy == null ? fireStrategy = new AnimalFireStrategy() : fireStrategy;
    }

    /**
     * Provides the default strategy for animal entities.
     * Implements lazy initialization to ensure the strategy is created only when needed.
     *
     * @return The flood strategy for animal.
     */
    @Override
    public EventGeneratingStrategy getFloodStrategy() {
        return floodStrategy == null ? floodStrategy = new AnimalFloodStrategy() : floodStrategy;
    }
}
