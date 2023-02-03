package io.ncbpfluffybear.fluffysconstruct.inventory;

import io.ncbpfluffybear.fluffysconstruct.data.Tuple;
import io.ncbpfluffybear.fluffysconstruct.items.CustomItem;
import io.ncbpfluffybear.fluffysconstruct.utils.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum InventoryTemplate {

    FURNACE("&6&lFurnace", 45,
            new int[]{
                    0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 12, 14, 15, 16, 17, 18, 19, 20, 21, 23,
                    24, 25, 26, 27, 28, 29, 30, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44
            }, new ArrayList<>(Arrays.asList(
            new Tuple<>(10, new CustomItem(Material.BOOK, "Instructions"), InventoryUtils.getDenyHandler())
    )));

    private final String title;
    private final int size;
    private final int[] background;
    private final List<Tuple<Integer, ItemStack, InvClickHandler>> items;

    InventoryTemplate(String title, int size, int[] background, List<Tuple<Integer, ItemStack, InvClickHandler>> items) {
        this.title = title;
        this.size = size;
        this.background = background;
        this.items = items;
    }

    public String getTitle() {
        return title;
    }

    public int getSize() {
        return size;
    }

    public int[] getBackground() {
        return background;
    }

    public List<Tuple<Integer, ItemStack, InvClickHandler>> getItems() {
        return items;
    }
}
