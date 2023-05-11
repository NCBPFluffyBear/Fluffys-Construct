package io.ncbpfluffybear.fluffysconstruct.trackers;

public class FuelStorage {

    private int currentFuel;

    public FuelStorage() {
        this.currentFuel = 0;
    }

    /**
     * Adds fuel to storage
     */
    public void addFuel(int addAmount) {
        this.currentFuel += addAmount;
    }

    /**
     * Attempts to consume fuel from reserves
     * @return true if success, false if not enough fuel
     */
    public boolean consumeFuel(int consumeAmount) {
        if (consumeAmount > this.currentFuel) {
            return false;
        }

        this.currentFuel -= consumeAmount;

        return true;
    }

    public int getCurrentFuel() {
        return this.currentFuel;
    }

    public void reset() {
        this.currentFuel = 0;
    }

}
