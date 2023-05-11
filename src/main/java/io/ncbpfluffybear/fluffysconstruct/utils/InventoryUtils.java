package io.ncbpfluffybear.fluffysconstruct.utils;

import io.ncbpfluffybear.fluffysconstruct.inventory.CustomInventory;
import io.ncbpfluffybear.fluffysconstruct.inventory.InvClickHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class InventoryUtils {

    public static InvClickHandler getDenyHandler() {
        return new InvClickHandler() {
            @Override
            public boolean onClick(Player player, int slot, ItemStack clickedItem, ClickType clickType) {
                return false;
            }
        };
    }

    public static void consumeItem(CustomInventory inv, int slot, int amount) {
        ItemStack item = inv.getItemInSlot(slot);
        item.setAmount(item.getAmount() - amount);
    }

    public static void setLore(CustomInventory inv, int slot, String... lore) {
        ItemStack item = inv.getItemInSlot(slot);
        ItemMeta meta = item.getItemMeta();

        meta.setLore(new ArrayList<>(Arrays.asList(lore)));
        item.setItemMeta(meta);
    }

}
