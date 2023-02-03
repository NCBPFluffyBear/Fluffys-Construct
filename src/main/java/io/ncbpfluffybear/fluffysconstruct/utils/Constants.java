package io.ncbpfluffybear.fluffysconstruct.utils;

import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import org.bukkit.NamespacedKey;

public class Constants {

    public Constants() {
        throw new InstantiationError();
    }

    public static final NamespacedKey FC_KEY = new NamespacedKey(FCPlugin.getInstance(), "fluffysconstruct_item");
    public static final NamespacedKey FC_BLOCKMETA_KEY = new NamespacedKey(FCPlugin.getInstance(), "fluffysconstruct_block");
    public static final String BLOCKS_FILE = "/blocks.yml";
}
