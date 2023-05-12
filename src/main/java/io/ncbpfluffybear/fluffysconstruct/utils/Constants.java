package io.ncbpfluffybear.fluffysconstruct.utils;

import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import org.bukkit.NamespacedKey;

public class Constants {

    public Constants() {
        throw new InstantiationError();
    }

    public static final NamespacedKey FC_ITEM_KEY = new NamespacedKey(FCPlugin.getInstance(), "fluffysconstruct_item");
    public static final NamespacedKey FC_BLOCK_KEY = new NamespacedKey(FCPlugin.getInstance(), "fluffysconstruct_block");
    public static final NamespacedKey FC_ENTITY_KEY = new NamespacedKey(FCPlugin.getInstance(), "fluffysconstruct_entity");
    public static final String BLOCKS_FILE = "/blocks.yml";
}
