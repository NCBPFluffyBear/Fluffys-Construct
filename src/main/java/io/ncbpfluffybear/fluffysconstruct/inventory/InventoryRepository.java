package io.ncbpfluffybear.fluffysconstruct.inventory;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class InventoryRepository {

    private final Map<Location, CustomInventory> inventories;

    public InventoryRepository() {
        this.inventories = new HashMap<>();
    }

    /**
     * Adds an inventory to the repository
     *
     * @param location  is the location of the block hosting this inventory
     * @param inventory is the inventory object
     */
    public void addInventory(Location location, CustomInventory inventory) {
        this.inventories.put(location, inventory);
    }

    public void removeInventory(Location location) {
        this.inventories.remove(location);
    }

    public CustomInventory getInventory(Location location) {
        return this.inventories.get(location);
    }

}
