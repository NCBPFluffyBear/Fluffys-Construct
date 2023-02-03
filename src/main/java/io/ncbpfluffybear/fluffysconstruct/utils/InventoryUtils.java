package io.ncbpfluffybear.fluffysconstruct.utils;

import io.ncbpfluffybear.fluffysconstruct.inventory.InvClickHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {

    public static InvClickHandler getDenyHandler() {
        return new InvClickHandler() {
            @Override
            public boolean onClick(Player player, int slot, ItemStack clickedItem, ClickType clickType) {
                return false;
            }
        };
    }

}
