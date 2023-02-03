package io.ncbpfluffybear.fluffysconstruct.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public interface InvClickHandler {

    boolean onClick(Player player, int slot, ItemStack clickedItem, ClickType clickType);

}
