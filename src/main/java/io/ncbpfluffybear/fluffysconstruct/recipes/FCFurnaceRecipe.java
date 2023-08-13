package io.ncbpfluffybear.fluffysconstruct.recipes;

import io.ncbpfluffybear.fluffysconstruct.api.items.FCItem;

/**
 * A wrapper for Bukkit {@link org.bukkit.inventory.Recipe}s that can accept
 * {@link FCItem}s
 */
public class FCFurnaceRecipe {

    private final FCItem input;
    private final FCItem output;

    public FCFurnaceRecipe(FCItem output, FCItem input, float exp, int time) {
        this.input = input;
        this.output = output;
    }

    public FCItem getFCInput() {
        return input;
    }

    public FCItem getFCOutput() {
        return output;
    }
}
