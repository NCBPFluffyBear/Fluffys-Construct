package io.ncbpfluffybear.fluffysconstruct.inventory;

import io.ncbpfluffybear.fluffysconstruct.inventory.slotdata.ProgressSlotData;
import io.ncbpfluffybear.fluffysconstruct.inventory.slotdata.SlotData;
import io.ncbpfluffybear.fluffysconstruct.inventory.slotdata.StackSlotData;
import io.ncbpfluffybear.fluffysconstruct.utils.InventoryUtils;
import io.ncbpfluffybear.fluffysconstruct.utils.ItemUtils;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

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
    }

    public MachineInventory unpack(InventoryPackage pack) {
        if (pack == null) {
            return this;
        } // Nothing to unpack
        for (Map.Entry<Integer, SlotData> data : pack.getSlotData().entrySet()) {
            SlotData slotData = data.getValue();

            if (slotData instanceof ProgressSlotData) {
                setProgressPercentge(((ProgressSlotData) slotData).getProgress());
            } else if (slotData instanceof StackSlotData) {
                setItem(data.getKey(), ((StackSlotData) slotData).getItem());
            }
        }

        return this;
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

    public void setProgressPercentge(int percentage) {
        setItem(this.progressSlot, ItemUtils.setDurability(this.progressStack, percentage));
    }
}
