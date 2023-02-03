package io.ncbpfluffybear.fluffysconstruct.inventory;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class PackageRepository {

    private final Map<Location, InventoryPackage> packages;

    public PackageRepository() {
        this.packages = new HashMap<>();
    }

    public InventoryPackage getPackage(Location location) {
        return this.packages.getOrDefault(location, null);
    }

    public void putPackage(Location location, InventoryPackage pack) {
        this.packages.put(location, pack);
    }

    public void removePackage(Location location) {
        this.packages.remove(location);
    }

    public Map<Location, InventoryPackage> getPackages() {
        return packages;
    }
}
