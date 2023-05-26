package io.ncbpfluffybear.fluffysconstruct.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Various Inventory click handlers for
 * different numbers of parameters
 */
public interface InvClickHandler {

    interface Basic extends InvClickHandler {
        boolean onClick(Player player, int slot, ItemStack clickedItem, ClickType clickType);
    }

    interface Event extends InvClickHandler {
        boolean onClick(Player player, CustomInventory customInv, int slot, ItemStack clickedItem, InventoryClickEvent event);
    }

    interface Advanced extends InvClickHandler {
        boolean onClick(Player player, CustomInventory customInv, int slot, ItemStack clickedItem, ClickType clickType);
    }

}
