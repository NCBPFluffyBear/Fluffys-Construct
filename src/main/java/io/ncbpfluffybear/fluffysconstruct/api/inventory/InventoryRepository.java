package io.ncbpfluffybear.fluffysconstruct.api.inventory;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InventoryRepository {

    private final Map<Location, CustomInventory> inventories;
    private final Set<Location> toRemove; // For database updates

    public InventoryRepository() {
        this.inventories = new HashMap<>();
        this.toRemove = new HashSet<>();
    }

    /**
     * Adds an inventory to the repository
     *
     * @param location  is the location of the block hosting this inventory
     * @param inventory is the inventory object
     */
    public void putInventory(Location location, CustomInventory inventory) {
        this.inventories.put(location, inventory);
        this.toRemove.remove(location);
    }

    public void removeInventory(Location location) {
        this.inventories.remove(location);
        this.toRemove.add(location);
    }

    public boolean hasInventory(Location location) {
        return this.inventories.containsKey(location);
    }

    public CustomInventory getInventory(Location location) {
        return this.inventories.get(location);
    }

    public Map<Location, CustomInventory> getInventories() {
        return inventories;
    }

    public void addRemoval(Location location) {
        toRemove.add(location);
    }

    public void undoRemoval(Location location) {
        toRemove.remove(location);
    }

    public boolean isRemoval(Location location) {
        return toRemove.contains(location);
    }
}
