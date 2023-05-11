package io.ncbpfluffybear.fluffysconstruct.trackers;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class MachineProgressTracker {

    private final Map<Location, MachineProgress> progress;

    public MachineProgressTracker() {
        this.progress = new HashMap<>();
    }

    public void createProgress(Location location) {
        this.progress.put(location, new MachineProgress());
    }

    public MachineProgress getProgress(Location location) {
        return this.progress.get(location);
    }

    public MachineProgress getOrCreateProgress(Location location) {
        if (!this.progress.containsKey(location)) {
            this.progress.put(location, new MachineProgress());
        }

        return this.progress.get(location);
    }

    public void deleteProgress(Location location) {
        this.progress.remove(location);
    }

}
