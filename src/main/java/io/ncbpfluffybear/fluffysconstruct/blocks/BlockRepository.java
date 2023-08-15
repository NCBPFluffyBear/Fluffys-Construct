package io.ncbpfluffybear.fluffysconstruct.blocks;

import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.api.items.FCItem;
import org.bukkit.Location;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BlockRepository {

    private final Map<Location, FCItem> placedBlocks;

    private final Map<FCItem, Set<Location>> clockedBlocks; // All clocked blocks
    private final Map<FCItem, Set<Location>> inventoryBlocks;

    public BlockRepository() {
        this.placedBlocks = new HashMap<>();
        this.clockedBlocks = new HashMap<>();
        this.inventoryBlocks = new HashMap<>();
    }

    public void addFCItemAt(Location location, FCItem fcItem) {
        this.placedBlocks.put(location, fcItem);
    }

    public void removeFCItemAt(Location location) {
        this.placedBlocks.remove(location);
    }

    @Nullable
    public FCItem getFCItemAt(Location location) {
        return placedBlocks.get(location);
    }

    public void addClockedBlock(FCItem fcItem, Location location) {
        if (!this.clockedBlocks.containsKey(fcItem)) {
            this.clockedBlocks.put(fcItem, new HashSet<>());
        }

        this.clockedBlocks.get(fcItem).add(location);
    }

    public void removeClockedBlock(FCItem fcItem, Location location) {
        if (!this.clockedBlocks.containsKey(fcItem)) {
            FCPlugin.getInstance().getLogger().warning("No clocked blocks for id " + fcItem.getKey());
            return;
        }

        if (!this.clockedBlocks.get(fcItem).remove(location)) {
            FCPlugin.getInstance().getLogger().warning("No block was clocked at " + location.toString() + " for id " + fcItem.getKey());
        }
    }

    public Map<FCItem, Set<Location>> getAllClocked() {
        return this.clockedBlocks;
    }

    public void addInventoryBlock(FCItem fcItem, Location location) {
        if (!this.inventoryBlocks.containsKey(fcItem)) {
            this.inventoryBlocks.put(fcItem, new HashSet<>());
        }

        this.inventoryBlocks.get(fcItem).add(location);
    }

    public void removeInventoryBlock(FCItem fcItem, Location location) {
        if (!this.inventoryBlocks.containsKey(fcItem)) {
            FCPlugin.getInstance().getLogger().warning("No inventory blocks for id " + fcItem.getKey());
            return;
        }

        if (!this.inventoryBlocks.get(fcItem).remove(location)) {
            FCPlugin.getInstance().getLogger().warning("No block has an inventory at " + location.toString() + " for id " + fcItem.getKey());
        }
    }

    public Map<FCItem, Set<Location>> getAllInventoryLocations() {
        return this.inventoryBlocks;
    }
}
