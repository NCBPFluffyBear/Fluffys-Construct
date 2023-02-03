package io.ncbpfluffybear.fluffysconstruct.inventory.slotdata;

/**
 * Used to save and load progress bar data when the
 * inventory is opened or closed
 */
public class ProgressSlotData implements SlotData {

    private int progress;

    public ProgressSlotData() {
        this.progress = 0;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getProgress() {
        return this.progress;
    }
}
