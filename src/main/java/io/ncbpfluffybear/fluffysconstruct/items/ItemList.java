package io.ncbpfluffybear.fluffysconstruct.items;

import io.ncbpfluffybear.fluffysconstruct.items.specializeditems.Controller;
import io.ncbpfluffybear.fluffysconstruct.items.specializeditems.Furnace;
import io.ncbpfluffybear.fluffysconstruct.items.specializeditems.SearedBricks;
import org.bukkit.Material;

public class ItemList {

    public static final FCItem GROUT = new Placeable("GROUT", 1, Material.CLAY, "&7&lGrout", "&fTest grout xdddd", "Line 2");
    public static final FCItem SEARED_BRICKS = new SearedBricks("SEARED_BRICKS", 2, Material.POLISHED_BLACKSTONE_BRICKS, "&f&lSeared Bricks", "&fTest grout xdddd", "Line 2");
    public static final FCItem FURNACE = new Furnace("FURNACE", 3, Material.FURNACE, "&f&lFurnace", "Smelts FC Items!");
    public static final FCItem CONTROLLER = new Controller("CONTROLLER", 4, Material.BLAST_FURNACE, "&f&lController", "&7Heart of a smeltery");

}
