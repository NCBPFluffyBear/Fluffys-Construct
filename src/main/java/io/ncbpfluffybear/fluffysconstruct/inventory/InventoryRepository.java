package io.ncbpfluffybear.fluffysconstruct.inventory;

import io.ncbpfluffybear.fluffysconstruct.data.DoubleKeyedMap;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class InventoryRepository {

    private final DoubleKeyedMap<Location, Player, CustomInventory> inventories;

    public InventoryRepository() {
        this.inventories = new DoubleKeyedMap<>();
    }

    /**
     * Adds an inventory to the repository
     *
     * @param player    is the player who opened this inventory
     * @param location  is the location of the block hosting this inventory
     * @param inventory is the inventory object
     */
    public void addInventory(Location location, Player player, CustomInventory inventory) {
        this.inventories.put(location, player, inventory);
    }

    public void removeInventory(Location location) {
        this.inventories.removeFirst(location);
    }

    public void removeInventory(Player player) {
        this.inventories.removeSecond(player);
    }

    public CustomInventory getInventory(Location location) {
        return this.inventories.getFirst(location);
    }

    public CustomInventory getInventory(Player player) {
        return this.inventories.getSecond(player);
    }

    public boolean hasPlayer(Player player) {
        return this.inventories.hasSecond(player);
    }

    public boolean hasLocation(Location location) {
        return this.inventories.hasFirst(location);
    }

}
