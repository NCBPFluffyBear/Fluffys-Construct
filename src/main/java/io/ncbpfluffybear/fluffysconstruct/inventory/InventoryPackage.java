package io.ncbpfluffybear.fluffysconstruct.inventory;

import io.ncbpfluffybear.fluffysconstruct.inventory.slotdata.SlotData;

import java.util.HashMap;
import java.util.Map;

/**
 * A compact data package containing {@link CustomInventory} contents
 * in the form of {@link SlotData}
 */
public class InventoryPackage {

    private final Map<Integer, SlotData> slotData;

    public InventoryPackage() {
        this.slotData = new HashMap<>();
    }

    public void setSlotData(int slot, SlotData data) {
        this.slotData.put(slot, data);
    }

    public Map<Integer, SlotData> getSlotData() {
        return slotData;
    }
}
