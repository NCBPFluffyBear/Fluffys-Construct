package io.ncbpfluffybear.fluffysconstruct.items;

import io.ncbpfluffybear.fluffysconstruct.utils.Constants;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

/**
 * Wrapper for a ItemStack
 */
public class FCItem {

    private final CustomItem item;
    private final String key;
    private final int id;

//    public FCItem(String key, ItemStack item) {
//        this.item = new CustomItem(item);
//        this.key = key;
//    }

    /**
     * All FCItems have a string key (identifier) and a numerical ID for persistent storage
     */
    public FCItem(String key, int id, Material material, String name, String... lore) {
        this.key = key;
        this.id = id;
        this.item = new CustomItem(material, name, lore);

        // Store ID in persistent data
        ItemMeta meta = this.item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(Constants.FC_ITEM_KEY, PersistentDataType.INTEGER, id);
        this.item.setItemMeta(meta);
    }

    public String getKey() {
        return this.key;
    }

    public ItemStack getItemStack() {
        return this.item.clone();
    }

    public int getId() {
        return this.id;
    }
}
