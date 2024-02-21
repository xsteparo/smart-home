package cz.cvut.fel.omo.event.strategy;

import cz.cvut.fel.omo.entity.alive.AliveEntity;
import cz.cvut.fel.omo.house.House;
import cz.cvut.fel.omo.entity.device.Device;
import cz.cvut.fel.omo.house.room.Room;
import cz.cvut.fel.omo.event.Event;
import cz.cvut.fel.omo.timesimulator.TimeManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * The type Default strategy base.
 *
 * @param <T> the type parameter
 */
public abstract class DefaultStrategyBase<T extends AliveEntity> implements EventGeneratingStrategy<T> {

    private final Random random = new Random();

    @Override
    public List<Event> generate(T entity) {
        if (TimeManager.getInstance().sleepingTime()) {
            return generateSleepEvents(entity);
        }
        return generateEvents(entity);
    }

    /**
     * Generate events list.
     *
     * @param entity the entity
     * @return the list
     */
    protected List<Event> generateEvents(T entity) {
        if (random.nextInt(100) < 80) {
            Optional<Device> subjectOptional = getRandomFreeDevice(entity);

            return subjectOptional.map(subject -> generateEventsWithDevice(subject, entity))
                    .orElseGet(() -> List.of(entity.getDefaultEventWithoutDevice().build()));
        }
        return List.of(entity.getDefaultEventWithoutDevice().build());
    }

    /**
     * Generate events with device list.
     *
     * @param device the device
     * @param entity the entity
     * @return the list
     */
    protected abstract List<Event> generateEventsWithDevice(Device device, T entity);

    /**
     * Gets random free device.
     *
     * @param aliveEntity the alive entity
     * @return the random free device
     */
    protected Optional<Device> getRandomFreeDevice(T aliveEntity) {
        List<Device> devices = House.getInstance().getAllDevices();

        if (devices == null || devices.isEmpty()) return Optional.empty();
        int randomDeviceIndex = (int) ((Math.random()) * (devices.size()));
        int tmp = randomDeviceIndex;

        while (skipDevice(devices.get(randomDeviceIndex))) {
            randomDeviceIndex++;
            if (randomDeviceIndex >= devices.size()) {
                randomDeviceIndex = 0;
            }

            if (randomDeviceIndex == tmp) {
                return Optional.empty();
            }
        }
        return Optional.of(devices.get(randomDeviceIndex));
    }

    /**
     * Generate sleep events list.
     *
     * @param aliveEntity the alive entity
     * @return the list
     */
    protected List<Event> generateSleepEvents(T aliveEntity) {
        List<Event> sleepingEvents = new ArrayList<>(createBeforeSleepRoutineEvents(aliveEntity));
        sleepingEvents.add(createGoToSleepEvent(aliveEntity));
        return sleepingEvents;
    }

    /**
     * Create brush teeth event event.
     *
     * @param adult the adult
     * @param wc    the wc
     * @return the event
     */
    protected Event createBrushTeethEvent(T adult, Room wc) {
        return Event.create()
                .isUrgent(false)
                .isSleeping(true)
                .name("Brushing Teeth")
                .remainingTime(5)
                .object(adult)
                .room(wc)
                .build();
    }

    /**
     * Create before sleep routine events list.
     *
     * @param aliveEntity the alive entity
     * @return the list
     */
    protected abstract List<Event> createBeforeSleepRoutineEvents(T aliveEntity);

    /**
     * Create go to sleep event event.
     *
     * @param aliveEntity the alive entity
     * @return the event
     */
// for different sleeping remaining time
    protected abstract Event createGoToSleepEvent(T aliveEntity);

    /**
     * Gets random toilet.
     *
     * @return the random toilet
     */
    protected Room getRandomToilet() {
        List<Room> wcs = House.getInstance().getToilets();
        int index = new Random().nextInt(wcs.size());
        return wcs.get(index);
    }

    /**
     * Skip device boolean.
     *
     * @param device the device
     * @return the boolean
     */
    protected abstract boolean skipDevice(Device device);

}
