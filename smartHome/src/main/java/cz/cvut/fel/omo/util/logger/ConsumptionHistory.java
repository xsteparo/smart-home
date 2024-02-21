package cz.cvut.fel.omo.util.logger;

import cz.cvut.fel.omo.entity.device.Device;
import cz.cvut.fel.omo.house.House;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * The ConsumptionHistory class is responsible for tracking and logging electricity consumption
 * by devices in the house.
 */
public class ConsumptionHistory {
    private final Map<Device, Double> totalConsumptionByDevice = new HashMap<>();
    private static final ConsumptionHistory instance = new ConsumptionHistory();
    private static final Logger logger = Logger.getLogger("electricityLogger");

    private ConsumptionHistory() {
    }

    /**
     * Gets the singleton instance of ConsumptionHistory.
     *
     * @return The instance of ConsumptionHistory.
     */
    public static ConsumptionHistory getInstance() {
        return instance;
    }

    /**
     * Logs the electricity consumption of a device.
     *
     * @param device The device for which electricity consumption is logged.
     * @param watt   The amount of electricity consumed in watts.
     */
    public void logElectricityConsumption(Device device, double watt) {
        totalConsumptionByDevice.merge(device, watt, Double::sum);
    }


    /**
     * Prints the total electricity consumption for each device.
     */
    public void printTotalConsumptionByDevice() {
        totalConsumptionByDevice.forEach((device, totalWatt) ->
                logger.info(device.getName() + ": " + totalWatt + " watt"));
    }

    /**
     * Prints the total electricity consumption and cost for the house.
     */
    public void printTotalConsumption(){
        double pricePerKw = 5.0;
        double wattUsed = House.getInstance().getTotalEnergyConsumed();
        double totalCost = (wattUsed / 1000) * pricePerKw;
        String formattedCost = String.format("%.2f", totalCost);
        logger.info("Total: " + wattUsed + " watt" +  " Price: " + formattedCost + " CZK" );
    }
}
