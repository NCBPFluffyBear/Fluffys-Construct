package io.ncbpfluffybear.fluffysconstruct.utils;

import io.ncbpfluffybear.fluffysconstruct.inventory.CustomInventory;
import io.ncbpfluffybear.fluffysconstruct.inventory.InvClickHandler;
import org.bukkit.Bukkit;
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
            public boolean onClick(Player player, CustomInventory customInv, int slot, ItemStack clickedItem, ClickType clickType) {
                return false;
            }
        };
    }

    public static void consumeItem(CustomInventory inv, int slot) {
        consumeItem(inv, slot, 1);
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

    /**
     * Checks if an input item can be pushed to an inventory slot
     */
    public static boolean canPushOutput(CustomInventory inv, int slot, ItemStack input) {
        ItemStack currItem = inv.getItemInSlot(slot);
        if (currItem == null) {
            return true;
        }

        return currItem.hasItemMeta() == input.hasItemMeta()
                && (currItem.hasItemMeta() ? Bukkit.getItemFactory().equals(currItem.getItemMeta(), input.getItemMeta()) : true)
                && currItem.getAmount() + input.getAmount() <= currItem.getType().getMaxStackSize();
    }

    /**
     * Assumes the destination slot has already been validated using
     * {@link InventoryUtils#canPushOutput(CustomInventory, int, ItemStack)}.
     */
    public static void addOrPutItem(CustomInventory inv, int slot, ItemStack input) {
        ItemStack currItem = inv.getItemInSlot(slot);
        if (currItem == null) {
            inv.setItem(slot, input);
        } else {
            currItem.setAmount(currItem.getAmount() + input.getAmount());
        }
    }

}
