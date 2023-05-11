package io.ncbpfluffybear.fluffysconstruct.items;

import org.bukkit.Location;
import org.bukkit.Material;

public abstract class Placeable extends FCItem {

    public Placeable(String key, Material material, String name, String... lore) {
        super(key, material, name, lore);
    }

    public abstract void onBreak(Location location);

    public void onPlace(Location location) {

    }

}
