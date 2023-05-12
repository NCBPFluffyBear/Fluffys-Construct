package io.ncbpfluffybear.fluffysconstruct.trackers;

public class MachineProgress {

    private int currentStage;

    public MachineProgress() {
        this.currentStage = 0;
    }

    public int nextStage(int totalStages) {
        if (this.currentStage++ >= totalStages) {
            this.currentStage = 1;
        }

        return this.currentStage;
    }

    public void reset() {
        if (this.currentStage != 0) {
            this.currentStage = 0;
        }
    }

}
