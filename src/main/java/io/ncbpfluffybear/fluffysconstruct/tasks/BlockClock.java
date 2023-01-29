package io.ncbpfluffybear.fluffysconstruct.tasks;

import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.items.Clocked;
import io.ncbpfluffybear.fluffysconstruct.items.FCItem;
import org.bukkit.Location;

import java.util.Map;
import java.util.Set;

/**
 * Driver for all {@link io.ncbpfluffybear.fluffysconstruct.items.Clocked} blocks
 */
public class BlockClock implements Runnable {

    @Override
    public void run() {
        for (Map.Entry<FCItem, Set<Location>> clockedSets : FCPlugin.getBlockRepository().getAllClocked().entrySet()) {
            for (Location location : clockedSets.getValue()) {
                ((Clocked) clockedSets.getKey()).trigger(location);
            }
        }
    }
}
