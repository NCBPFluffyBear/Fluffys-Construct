package io.ncbpfluffybear.fluffysconstruct.items;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Placeable extends FCItem { // TODO: Interface?

    public Placeable(String key, int id, Material material, String name, String... lore) {
        super(key, id, material, name, lore);
    }

    public void onBreak(Location location) {

    }

    public void onPlace(Location location) {

    }

    public void onInteract(Block block, Player player, ItemStack item) {

    }

}
