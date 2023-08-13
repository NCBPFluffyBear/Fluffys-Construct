package io.ncbpfluffybear.fluffysconstruct.items.specializeditems.smeltery;

import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.api.data.persistent.blockdata.BlockData;
import io.ncbpfluffybear.fluffysconstruct.api.data.persistent.blockdata.BlockDataRepository;
import io.ncbpfluffybear.fluffysconstruct.api.data.serialize.Serialize;
import io.ncbpfluffybear.fluffysconstruct.api.items.FCItem;
import io.ncbpfluffybear.fluffysconstruct.data.SmelterySystem;
import io.ncbpfluffybear.fluffysconstruct.items.ItemList;
import io.ncbpfluffybear.fluffysconstruct.items.Placeable;
import io.ncbpfluffybear.fluffysconstruct.utils.ItemUtils;
import io.ncbpfluffybear.fluffysconstruct.utils.SmelteryUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataAdapterContext;
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
        SmelterySystem system = SmelteryUtils.getSystem(location);
        if (system == null) {
            return; // Not linked to a smeltery
        }

        system.removeBrick(location);
        system.setActive(false);
    }
}
