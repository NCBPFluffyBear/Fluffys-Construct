package io.ncbpfluffybear.fluffysconstruct.items;

import org.bukkit.Location;
import org.bukkit.Material;

public class Placeable extends FCItem {

    public Placeable(String key, int id, Material material, String name, String... lore) {
        super(key, id, material, name, lore);
    }

    public void onBreak(Location location) {

    }

    public void onPlace(Location location) {

    }

}
