package io.ncbpfluffybear.fluffysconstruct.items;

import io.ncbpfluffybear.fluffysconstruct.api.inventory.CustomInventory;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface InventoryBlock {

    CustomInventory createInventory(Location location);

    /**
     * Requests to open an inventory for a player.
     * Perform inventory open actions in here
     * Return whether the player may open the inventory or not.
     */
    default boolean onOpen(CustomInventory customInventory, Player player, ItemStack item) {
        return true;
    }

}
