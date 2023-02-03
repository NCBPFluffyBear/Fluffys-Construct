package io.ncbpfluffybear.fluffysconstruct.utils;

import com.jeff_media.customblockdata.CustomBlockData;
import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.items.FCItem;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
import java.util.HashMap;

public class ItemUtils {

    public ItemUtils() {
        throw new InstantiationError();
    }

    public static void giveItem(Player player, ItemStack... items) {
        for (ItemStack item : items) {
            HashMap<Integer, ItemStack> leftovers = player.getInventory().addItem(item);
            for (ItemStack leftover : leftovers.values()) {
                player.getWorld().dropItem(player.getLocation(), leftover);
            }
        }
    }

    public static void giveItem(Player player, FCItem... items) {
        for (FCItem item : items) {
            HashMap<Integer, ItemStack> leftovers = player.getInventory().addItem(item.getItemStack());
            for (ItemStack leftover : leftovers.values()) {
                player.getWorld().dropItem(player.getLocation(), leftover);
            }
        }
    }

    @Nullable
    public static FCItem getFCItem(ItemStack item) {
        if (item == null) {
            return null;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return null;
        }

        if (!meta.getPersistentDataContainer().has(Constants.FC_KEY, PersistentDataType.INTEGER)) {
            return null;
        }

        int id = meta.getPersistentDataContainer().get(Constants.FC_KEY, PersistentDataType.INTEGER);

        return FCPlugin.getItemRepository().getItemById(id);
    }

    @Nullable
    public static FCItem getFCItem(int id) {
        return FCPlugin.getItemRepository().getItemById(id);
    }

    @Nullable
    public static FCItem getFCItem(String key) {
        return FCPlugin.getItemRepository().getItemByKey(key);
    }

    @Nullable
    public static FCItem getFCItem(Block b) {
        if (!CustomBlockData.hasCustomBlockData(b, FCPlugin.getInstance())) {return null;}

        int id = new CustomBlockData(b, FCPlugin.getInstance()).get(Constants.FC_BLOCKMETA_KEY, PersistentDataType.INTEGER);

        return getFCItem(id);
    }

    /**
     * Returns a clone of the given item with the durability set to the given percentage.
     * Item must be damageable, otherwise a warning will be shown and the unmodified item will be returned.
     */
    public static ItemStack setDurability(ItemStack item, int percentage) {
        ItemStack clone = item.clone();
        ItemMeta meta = clone.getItemMeta();
        if (!(meta instanceof Damageable)) {
            FCPlugin.getInstance().getLogger().warning("Can not set the durability of " + item.getType());
            return item;
        }

        short totalDurability = clone.getType().getMaxDurability();
        int newDurability = totalDurability * percentage / 100;
        ((Damageable) meta).setDamage(newDurability);

        clone.setItemMeta(meta);
        return clone;
    }

}
