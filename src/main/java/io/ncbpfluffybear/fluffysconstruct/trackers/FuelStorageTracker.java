package io.ncbpfluffybear.fluffysconstruct.trackers;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class FuelStorageTracker {

    private final Map<Location, FuelStorage> fuelStorages;

    public FuelStorageTracker() {
        this.fuelStorages = new HashMap<>();
    }

    public void createStorage(Location location) {
        this.fuelStorages.put(location, new FuelStorage());
    }

    public FuelStorage getStorage(Location location) {
        return this.fuelStorages.get(location);
    }

    public FuelStorage getOrCreateStorage(Location location) {
        if (!this.fuelStorages.containsKey(location)) {
            this.fuelStorages.put(location, new FuelStorage());
        }

        return this.fuelStorages.get(location);
    }

    public void deleteStorage(Location location) {
        this.fuelStorages.remove(location);
    }

}
