package io.ncbpfluffybear.fluffysconstruct.items;

import io.ncbpfluffybear.fluffysconstruct.utils.Constants;
import io.ncbpfluffybear.fluffysconstruct.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper for a ItemStack
 */
public class FCItem {

    private final CustomItem item;
    private final String key;
    private int id;

    /**
     * Makes a copy of the item
     */
    public FCItem(String key, ItemStack item) {
        this.item = new CustomItem(item);
        this.key = key;
    }

    public FCItem(String key, Material material, String name, String... lore) {
        this.key = key;
        this.item = new CustomItem(material, name, lore);
    }

    /**
     * Adds an integer key to the {@link PersistentDataContainer}
     */
    public FCItem setId(int id) {
        this.id = id;

        ItemMeta meta = this.item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(Constants.FC_KEY, PersistentDataType.INTEGER, id);
        this.item.setItemMeta(meta);

        return this;
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
