package io.ncbpfluffybear.fluffysconstruct.inventory.slotdata;

import org.bukkit.inventory.ItemStack;

/**
 * Stores whole ItemStacks in a data package
 * Use only if unable to use a more simple type of data
 */
public class StackSlotData implements SlotData {

    private ItemStack item;

    public StackSlotData() {
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public ItemStack getItem() {
        return this.item;
    }
}
