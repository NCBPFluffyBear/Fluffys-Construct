package io.ncbpfluffybear.fluffysconstruct.utils;

import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

public class EntityUtils {

    public EntityUtils() {
        throw new InstantiationError();
    }

    public static void highlightBlock(Location location) {
        FallingBlock indicator = location.getWorld().spawnFallingBlock(location, Material.GLASS, (byte) 0);
        indicator.setGlowing(true);
        indicator.setVelocity(new Vector(0, 0, 0));
        indicator.setGravity(false);
        indicator.getPersistentDataContainer().set(Constants.FC_ENTITY_KEY, PersistentDataType.BYTE, (byte) 1);
        Bukkit.getScheduler().runTaskLater(FCPlugin.getInstance(), indicator::remove, 20L * 5); // Kill indicator
    }

}
