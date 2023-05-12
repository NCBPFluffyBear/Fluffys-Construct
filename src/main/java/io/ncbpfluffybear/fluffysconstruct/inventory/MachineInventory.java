package io.ncbpfluffybear.fluffysconstruct.inventory;

import io.ncbpfluffybear.fluffysconstruct.utils.InventoryUtils;
import io.ncbpfluffybear.fluffysconstruct.utils.ItemUtils;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

public class MachineInventory extends CustomInventory {

    private final int inputSlot;
    private final int outputSlot;
    private final int progressSlot;
    private final ItemStack idleStack;
    private final ItemStack progressStack;

    public MachineInventory(int inputSlot, int outputSlot, int progressSlot, ItemStack idleStack, ItemStack progressStack, InventoryTemplate template) {
        super(template);
        this.inputSlot = inputSlot;
        this.outputSlot = outputSlot;
        this.progressSlot = progressSlot;
        this.idleStack = idleStack;
        this.progressStack = progressStack;

        addClickHandler(this.progressSlot, InventoryUtils.getDenyHandler());
        setItem(progressSlot, progressStack);
    }

    @Nullable
    public ItemStack getItemInInput() {
        return super.getItemInSlot(this.inputSlot);
    }

    @Nullable
    public ItemStack getItemInOutput() {
        return super.getItemInSlot(this.outputSlot);
    }

    public int getInputSlot() {
        return this.inputSlot;
    }

    public int getOutputSlot() {
        return this.outputSlot;
    }

    public void setIdle() {
        setItem(this.progressSlot, this.idleStack);
    }

    /**
     * Sets the durability bar of an item to represent a percentage.
     * Note: 0% and 100% are considered the same (both non-operational status), so both are 100%
     * @param percentage
     */
    public void setProgressPercentage(int percentage) {
        if (percentage == 0) {
            percentage = 100;
        }
        setItem(this.progressSlot, ItemUtils.setDurability(this.progressStack, percentage));
    }
}
