package io.ncbpfluffybear.fluffysconstruct.blocks;

import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.items.FCItem;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BlockRepository {

    private final Map<FCItem, Set<Location>> clockedBlocks; // All clocked blocks
    private final Set<Location> inventoryBlocks;

    public BlockRepository() {
        this.clockedBlocks = new HashMap<>();
        this.inventoryBlocks = new HashSet<>();
    }

    public void addClocked(FCItem fcItem, Location location) {
        if (!this.clockedBlocks.containsKey(fcItem)) {
            this.clockedBlocks.put(fcItem, new HashSet<>());
        }

        this.clockedBlocks.get(fcItem).add(location);
    }

    public void removeClocked(FCItem fcItem, Location location) {
        if (!this.clockedBlocks.containsKey(fcItem)) {
            FCPlugin.getInstance().getLogger().warning("No clocked blocks for key " + fcItem.getKey());
            return;
        }

        if (!this.clockedBlocks.get(fcItem).remove(location)) {
            FCPlugin.getInstance().getLogger().warning("No block was clocked at " + location.toString() + " for key " + fcItem.getKey());
        }
    }

    public Map<FCItem, Set<Location>> getAllClocked() {
        return this.clockedBlocks;
    }

    public void addInventoryBlock(Location location) {
        this.inventoryBlocks.add(location);
    }

    public boolean isInventoryBlock(Location location) {
        return this.inventoryBlocks.contains(location);
    }

    public void removeInventoryBlock(Location location) {
        if (!this.inventoryBlocks.remove(location)) {
            FCPlugin.getInstance().getLogger().warning("No block had an inventory at " + location.toString());
        }
    }
}
