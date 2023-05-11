package io.ncbpfluffybear.fluffysconstruct.trackers;

public class MachineProgress {

    private int currentStage;

    public MachineProgress() {
        this.currentStage = 0;
    }

    public int nextStage(int totalStages) {
        if (this.currentStage++ >= totalStages) {
            this.currentStage = 0;
        }

        return this.currentStage;
    }

    public void reset() {
        this.currentStage = 0;
    }

}
