package io.ncbpfluffybear.fluffysconstruct.utils;

import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import org.bukkit.NamespacedKey;

public class Keys {

    public static final NamespacedKey SYSTEM_UUID = new NamespacedKey(FCPlugin.getInstance(), "system_uuid");
    public static final NamespacedKey TANK_LOCATIONS = new NamespacedKey(FCPlugin.getInstance(), "tank_locations"); // Byte exists or not
    public static final NamespacedKey MELTED_ORES = new NamespacedKey(FCPlugin.getInstance(), "melted_ores"); // Byte exists or not

}
