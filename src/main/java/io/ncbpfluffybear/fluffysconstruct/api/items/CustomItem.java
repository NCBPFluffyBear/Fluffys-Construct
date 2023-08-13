package io.ncbpfluffybear.fluffysconstruct.api.items;

import io.ncbpfluffybear.fluffysconstruct.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CustomItem extends ItemStack {

    public CustomItem(Material material, String name, String... lore) {
        super(material);
        ItemMeta meta = this.getItemMeta();

        meta.setDisplayName(StringUtils.color(name));
        List<String> newLore = new ArrayList<>();
        for (String line : lore) {
            newLore.add(StringUtils.color(line));
        }
        meta.setLore(newLore);
        this.setItemMeta(meta);
    }

    public CustomItem(Material material, int amount, String name, String... lore) {
        this(material, name, lore);
        this.setAmount(amount);
    }

    public CustomItem(ItemStack item) {
        super(item);
    }

}
