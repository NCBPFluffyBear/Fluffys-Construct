package io.ncbpfluffybear.fluffysconstruct.items.specializeditems.smeltery;

import com.jeff_media.customblockdata.CustomBlockData;
import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.items.Placeable;
import io.ncbpfluffybear.fluffysconstruct.utils.ChatUtils;
import io.ncbpfluffybear.fluffysconstruct.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class SearedTank extends Placeable {

    public static final NamespacedKey LAVA_LEVEL = new NamespacedKey(FCPlugin.getInstance(), "lava_level"); // Byte exists or not

    private static final int LAVA_FUEL = 50;
    private static final int LAVA_MAX = 250;

    public SearedTank(String key, int id, Material material, String name, String... lore) {
        super(key, id, material, name, lore);
    }

    @Override
    public void onInteract(Block block, Player player, ItemStack item) {
        CustomBlockData data = new CustomBlockData(block, FCPlugin.getInstance());
        int lavaLevel = getLavaLevel(data);
        // Check for lava bucket
        if (item.getType() == Material.LAVA_BUCKET && item.getAmount() == 1) {
            int newLava = lavaLevel + LAVA_FUEL;
            if (newLava > LAVA_MAX) {
                ChatUtils.sendMsg(player, "ITEMS.SEARED_TANK.MAX_LAVA_CAPACITY");
                return;
            }

            item.setType(Material.BUCKET);
            player.playSound(block.getLocation(), Sound.ITEM_BUCKET_EMPTY_LAVA, 1f, 1f);
            setLavaLevel(data, newLava);
        } else {
            ChatUtils.sendMsg(player, "ITEMS.SEARED_TANK.LAVA_STORED", StringUtils.formatDecimal((double) lavaLevel / LAVA_FUEL));
        }
    }

    public static boolean consumeLava(Block block, int required) {
        CustomBlockData data = new CustomBlockData(block, FCPlugin.getInstance());
        int lavaLevel = getLavaLevel(data);
        if (lavaLevel < required) {
            return false;
        }

        setLavaLevel(data, lavaLevel - required);
        return true;
    }

    private static int getLavaLevel(CustomBlockData data) {
        return data.getOrDefault(LAVA_LEVEL, PersistentDataType.INTEGER, 0);
    }

    private static void setLavaLevel(CustomBlockData data, int level) {
        data.set(LAVA_LEVEL, PersistentDataType.INTEGER, level);
    }
}
