package io.ncbpfluffybear.fluffysconstruct.items.specializeditems;

import com.jeff_media.customblockdata.CustomBlockData;
import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.items.FCItem;
import io.ncbpfluffybear.fluffysconstruct.items.ItemList;
import io.ncbpfluffybear.fluffysconstruct.items.Placeable;
import io.ncbpfluffybear.fluffysconstruct.utils.ItemUtils;
import io.ncbpfluffybear.fluffysconstruct.utils.SaveUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataType;

public class SearedBricks extends Placeable {

    public static final NamespacedKey CONTROLLER_LOC_KEY = new NamespacedKey(FCPlugin.getInstance(), "fluffysconstruct_controller_location");

    public SearedBricks(String key, int id, Material material, String name, String... lore) {
        super(key, id, material, name, lore);
    }

    /**
     * Unbind the block from the controller when broken
     */
    @Override
    public void onBreak(Location location) {
        CustomBlockData data = new CustomBlockData(location.getBlock(), FCPlugin.getInstance());
        String locationStr = data.get(CONTROLLER_LOC_KEY, PersistentDataType.STRING);
        if (locationStr == null) { // No controller linked
            return;
        }

        Location controlLoc = SaveUtils.parseLocation(locationStr);
        FCItem controller = ItemUtils.getFCItem(controlLoc.getBlock());

        if (controller != ItemList.CONTROLLER) { // Controller no longer exists
            return;
        }

        Bukkit.broadcastMessage(String.valueOf(location));
    }

    /**
     * Tags the brick with the location of the controller
     */
    public static void setController(Block brick, Location controllerLoc) {
        CustomBlockData data = new CustomBlockData(brick, FCPlugin.getInstance());
        data.set(CONTROLLER_LOC_KEY, PersistentDataType.STRING, SaveUtils.serializeLocation(controllerLoc));
    }
}
