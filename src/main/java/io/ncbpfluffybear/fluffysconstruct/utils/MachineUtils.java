package io.ncbpfluffybear.fluffysconstruct.utils;

import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MachineUtils {

    public MachineUtils() {
        throw new InstantiationError();
    }

    public static int tick(Location location, int totalStages) { // TODO: Save ticks persistently. For now, ticks will restart at 0
        return FCPlugin.getMachineProgressTracker().getOrCreateProgress(location).nextStage(totalStages);
    }

    public static void resetTicks(Location location) {
        FCPlugin.getMachineProgressTracker().getOrCreateProgress(location).reset();
    }

    public static boolean consumeFuel(Location location, int consumeAmount) {
        return FCPlugin.getFuelStorageTracker().getOrCreateStorage(location).consumeFuel(consumeAmount);
    }

    public static void addFuel(Location location, int addAmount) {
        FCPlugin.getFuelStorageTracker().getOrCreateStorage(location).addFuel(addAmount);
    }

    public static int getFuelStored(Location location) {
        return FCPlugin.getFuelStorageTracker().getOrCreateStorage(location).getCurrentFuel();
    }

    /**
     * Returns how much a material is worth in fuel points
     */
    public static int getFuelWorth(ItemStack item) {
        if (item == null) {
            return 0;
        }

        switch (item.getType()) {
            case COAL:
                return 20;
            case COAL_BLOCK:
                return 180;
            default:
                return 0;
        }
    }

}
