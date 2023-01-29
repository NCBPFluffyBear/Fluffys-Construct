package io.ncbpfluffybear.fluffysconstruct.items.specializeditems;

import io.ncbpfluffybear.fluffysconstruct.items.Clocked;
import io.ncbpfluffybear.fluffysconstruct.items.FCItem;
import io.ncbpfluffybear.fluffysconstruct.items.InventoryBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Furnace extends FCItem implements Clocked, InventoryBlock {

    public Furnace(String key, Material material, String name, String... lore) {
        super(key, material, name, lore);
    }

    @Override
    public void trigger(Location location) {
        location.getWorld().dropItem(location, new ItemStack(Material.COAL));
    }

    @Override
    public void onOpen(Location location) {

    }
}
