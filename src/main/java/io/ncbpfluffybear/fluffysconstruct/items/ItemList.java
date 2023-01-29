package io.ncbpfluffybear.fluffysconstruct.items;

import io.ncbpfluffybear.fluffysconstruct.items.specializeditems.Furnace;
import org.bukkit.Material;

public class ItemList {

    public static final FCItem GROUT = new FCItem("GROUT", Material.CLAY, "&7&lGrout", "&fTest grout xdddd", "Line 2");
    public static final FCItem SEARED_BRICKS = new FCItem("SEARED_BRICKS", Material.POLISHED_BLACKSTONE_BRICKS, "&f&lSeared Bricks", "&fTest grout xdddd", "Line 2");
    public static final FCItem FURNACE = new Furnace("FURNACE", Material.BLAST_FURNACE, "&f&lFurnace", "Smelts FC Items!");

}
