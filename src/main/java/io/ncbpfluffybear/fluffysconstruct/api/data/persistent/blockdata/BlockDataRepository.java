package io.ncbpfluffybear.fluffysconstruct.api.data.persistent.blockdata;

import io.ncbpfluffybear.fluffysconstruct.utils.ChatUtils;
import io.ncbpfluffybear.fluffysconstruct.utils.ItemUtils;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class BlockDataRepository {

    private static final Map<Location, BlockData> data = new HashMap<>();

    public BlockDataRepository() throws InstantiationException {
        throw new InstantiationException();
    }

    public static BlockData getDataAt(Location location) {
        return data.get(location);
    }

    public static BlockData getDataAt(Block block) {
        return getDataAt(block.getLocation());
    }

    public static BlockData getOrCreateDataAt(Location location) {
        if (!data.containsKey(location)) {
            data.put(location, new BlockData(location));
        }
        return data.get(location);
    }

    public static BlockData getOrCreateDataAt(Block block) {
        return getOrCreateDataAt(block.getLocation());
    }

    public static boolean hasData(Location location) {
        return data.containsKey(location);
    }

    public static boolean hasData(Block block) {
        return hasData(block.getLocation());
    }

    public static void load(Location location, @Nonnull ConfigurationSection blockDataSection) {
        if (data.containsKey(location)) {
            ChatUtils.warn("Location " + location + " already has block data! Skipping...");
            return;
        }
        BlockData blockData = new BlockData(blockDataSection.getValues(false));
        blockData.setLocation(location);
        data.put(location, blockData);
    }

    public static void save(Location location, YamlConfiguration configuration) {
        if (!data.containsKey(location)) return;
        configuration.createSection("blockdata", data.get(location).serialize());
    }

}
