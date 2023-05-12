package io.ncbpfluffybear.fluffysconstruct.items.specializeditems;

import io.ncbpfluffybear.fluffysconstruct.inventory.CustomInventory;
import io.ncbpfluffybear.fluffysconstruct.items.Clocked;
import io.ncbpfluffybear.fluffysconstruct.items.InventoryBlock;
import io.ncbpfluffybear.fluffysconstruct.items.Placeable;
import org.bukkit.Location;
import org.bukkit.Material;

public class Smeltery extends Placeable implements Clocked, InventoryBlock {


    public Smeltery(String key, int id, Material material, String name, String... lore) {
        super(key, id, material, name, lore);
    }

    @Override
    public void onClock(Location location) {

    }

    @Override
    public CustomInventory createInventory() {
        return null;
    }

    @Override
    public void onBreak(Location location) {

    }
}
