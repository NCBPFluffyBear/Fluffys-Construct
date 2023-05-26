package io.ncbpfluffybear.fluffysconstruct.items.specializeditems.smeltery;

import com.jeff_media.customblockdata.CustomBlockData;
import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.data.serialize.Serialize;
import io.ncbpfluffybear.fluffysconstruct.items.FCItem;
import io.ncbpfluffybear.fluffysconstruct.items.ItemList;
import io.ncbpfluffybear.fluffysconstruct.items.Placeable;
import io.ncbpfluffybear.fluffysconstruct.utils.ItemUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataType;

public class SearedBricks extends Placeable {

    public static final NamespacedKey CONTROLLER_LOC_KEY = new NamespacedKey(FCPlugin.getInstance(), "controller_location");

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

        Location controlLoc = Serialize.parseLocation(locationStr);
        FCItem controller = ItemUtils.getFCItem(controlLoc.getBlock());

        if (controller != ItemList.CONTROLLER) { // Controller no longer exists
            return;
        }

        CustomBlockData controllerData = new CustomBlockData(controlLoc.getBlock(), FCPlugin.getInstance());
        controllerData.remove(Controller.VALID_SMELTERY_FLAG);
        controllerData.remove(Controller.TANK_LOCATIONS);
    }

    /**
     * Tags the brick with the location of the controller
     */
    public static void setController(Block brick, Location controllerLoc) {
        CustomBlockData data = new CustomBlockData(brick, FCPlugin.getInstance());
        data.set(CONTROLLER_LOC_KEY, PersistentDataType.STRING, Serialize.serializeLocation(controllerLoc));
    }
}
