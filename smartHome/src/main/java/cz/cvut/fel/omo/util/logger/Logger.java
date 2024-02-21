package cz.cvut.fel.omo.util.logger;

/**
 * The type Logger.
 */
public class Logger {

    /**
     * To log.
     */
    public static void toLog(){
        EventHistory.getInstance().printEntityEventUsageReport();
        EventHistory.getInstance().printWeatherChangeCount();

        ConsumptionHistory.getInstance().printTotalConsumptionByDevice();
        ConsumptionHistory.getInstance().printTotalConsumption();
    }
}
