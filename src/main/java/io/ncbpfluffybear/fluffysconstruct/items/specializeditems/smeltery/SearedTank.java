package io.ncbpfluffybear.fluffysconstruct.items.specializeditems.smeltery;

import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.api.data.persistent.blockdata.BlockData;
import io.ncbpfluffybear.fluffysconstruct.api.data.persistent.blockdata.BlockDataRepository;
import io.ncbpfluffybear.fluffysconstruct.data.SmelterySystem;
import io.ncbpfluffybear.fluffysconstruct.utils.ChatUtils;
import io.ncbpfluffybear.fluffysconstruct.utils.Constants;
import io.ncbpfluffybear.fluffysconstruct.utils.Keys;
import io.ncbpfluffybear.fluffysconstruct.utils.SmelteryUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;

public class SearedTank extends SearedBricks {

    private static final int CAPACITY_MB = 4000;

    public SearedTank(String key, int id, Material material, String name, String... lore) {
        super(key, id, material, name, lore);
    }

    @Override
    public void onBreak(Location location) {
        SmelterySystem system = SmelteryUtils.getSystem(location);
        if (system == null) {
            return; // Not linked to a smeltery
        }

        system.removeFuelTank(location);
        system.setActive(false);
    }

    @Override
    public void onInteract(Block block, Player player, ItemStack item) {
        BlockData blockData = BlockDataRepository.getOrCreateDataAt(block);
        int lavaLevel = getLavaLevel(blockData);
        // Check for lava bucket
        if (item != null && item.getType() == Material.LAVA_BUCKET && item.getAmount() == 1) {
            int newLava = lavaLevel + Constants.BUCKET_MB;
            if (newLava > CAPACITY_MB) {
                ChatUtils.sendMsg(player, "ITEMS.SEARED_TANK.MAX_LAVA_CAPACITY");
                return;
            }

            item.setType(Material.BUCKET);
            player.playSound(block.getLocation(), Sound.ITEM_BUCKET_EMPTY_LAVA, 1f, 1f);
            setLavaLevel(blockData, newLava);
        } else {
            ChatUtils.sendMsg(player, "ITEMS.SEARED_TANK.LAVA_STORED", lavaLevel, CAPACITY_MB);
        }
    }

    public static boolean consumeLava(BlockData blockData, int required) {
        int lavaLevel = getLavaLevel(blockData);
        if (lavaLevel < required) {
            return false;
        }

        setLavaLevel(blockData, lavaLevel - required);
        return true;
    }

    private static int getLavaLevel(@Nonnull BlockData blockData) {
        return blockData.getOrDefault(Keys.LAVA_LEVEL, PersistentDataType.INTEGER, 0);
    }

    public static int getLavaLevel(Location location) {
        int defaultValue = 0;
        if (BlockDataRepository.hasData(location)) {
            return BlockDataRepository.getDataAt(location).getOrDefault(Keys.LAVA_LEVEL, PersistentDataType.INTEGER, defaultValue);
        }
        return defaultValue;
    }

    private static void setLavaLevel(BlockData blockData, int level) {
        blockData.set(Keys.LAVA_LEVEL, PersistentDataType.INTEGER, level);

        Block tank = blockData.getLocation().getBlock();
        if (level > 0) {
            tank.setType(Material.ORANGE_STAINED_GLASS);
        } else {
            tank.setType(Material.LIGHT_GRAY_STAINED_GLASS);
        }
    }
}
