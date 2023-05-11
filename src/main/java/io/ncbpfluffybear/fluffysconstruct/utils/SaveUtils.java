package io.ncbpfluffybear.fluffysconstruct.utils;

import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.blocks.BlockRepository;
import io.ncbpfluffybear.fluffysconstruct.items.Clocked;
import io.ncbpfluffybear.fluffysconstruct.items.FCItem;
import io.ncbpfluffybear.fluffysconstruct.items.InventoryBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class SaveUtils {

    public SaveUtils() {
        throw new InstantiationError();
    }

    public static void loadBlocks(FileConfiguration blocksConfig, BlockRepository repository) {
        for (String key : blocksConfig.getKeys(false)) {
            Location loc = parseLocation(key);
            int id = blocksConfig.getInt(key);

            FCItem item = ItemUtils.getFCItem(id);

            if (item == null) {
                FCPlugin.getInstance().getLogger().warning("Unknown item ID \"" + id + "\" when loading in blocks (" + key + ")");
                continue;
            }

            if (item instanceof Clocked) {
                repository.addClocked(item, loc);
            }

            if (item instanceof InventoryBlock) {
                repository.addInventoryBlock(loc); // Register block
                FCPlugin.getInventoryRepository().addInventory(loc, ((InventoryBlock) item).createInventory()); // Apply inventory template
                // TODO Load saved inventory
            }
        }
    }

    public static void saveBlocks(File blocksFile, BlockRepository repository) {
        FileConfiguration newBlocksConfig = new YamlConfiguration();
        for (Map.Entry<FCItem, Set<Location>> clockedEntry : repository.getAllClocked().entrySet()) {
            for (Location loc : clockedEntry.getValue()) {
                String locKey = serializeLocation(loc);
                newBlocksConfig.set(locKey, clockedEntry.getKey().getId());
            }
        }

        try {
            newBlocksConfig.save(blocksFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Location parseLocation(String locationKey) {
        String[] splitLocation = locationKey.split(":");
        return new Location(Bukkit.getWorld(UUID.fromString(splitLocation[0])),
                Integer.parseInt(splitLocation[1]), Integer.parseInt(splitLocation[2]), Integer.parseInt(splitLocation[3])
        ); // World:X:Y:Z
    }

    public static String serializeLocation(Location loc) {
        return loc.getWorld().getUID() + ":" + loc.getBlockX() + ":" + loc.getBlockY() + ":" + loc.getBlockZ();
    }

}
