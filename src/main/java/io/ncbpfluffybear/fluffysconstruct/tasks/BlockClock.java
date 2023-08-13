package io.ncbpfluffybear.fluffysconstruct.tasks;

import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.items.Clocked;
import io.ncbpfluffybear.fluffysconstruct.api.items.FCItem;
import io.ncbpfluffybear.fluffysconstruct.utils.ChatUtils;
import io.ncbpfluffybear.fluffysconstruct.utils.ItemUtils;
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
                try {
                    ((Clocked) clockedSets.getKey()).onClock(location);
                } catch (ClassCastException e) {
                    FCItem fcItem = ItemUtils.getFCItem(location.getBlock());
                    ChatUtils.broadcast("Tried to clock a " + clockedSets.getKey().getKey() + " but got a " + (fcItem != null ? fcItem.getKey() : "N/A"));
                }
            }
        }
    }
}
