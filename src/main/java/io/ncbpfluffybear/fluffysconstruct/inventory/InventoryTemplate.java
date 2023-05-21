package io.ncbpfluffybear.fluffysconstruct.inventory;

import io.ncbpfluffybear.fluffysconstruct.data.Tuple;
import io.ncbpfluffybear.fluffysconstruct.items.CustomItem;
import io.ncbpfluffybear.fluffysconstruct.items.specializeditems.smeltery.Controller;
import io.ncbpfluffybear.fluffysconstruct.items.specializeditems.Furnace;
import io.ncbpfluffybear.fluffysconstruct.utils.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum InventoryTemplate {

    // Chest Slots:
    // 0  1  2  3  4  5  6  7  8
    // 9  10 11 12 13 14 15 16 17
    // 18 19 20 21 22 23 24 25 26
    // 27 28 29 30 31 32 33 34 35
    // 36 37 38 39 40 41 42 43 44
    // 45 46 47 48 49 50 51 52 53

    FURNACE("&6&lFurnace", 45,
            new int[]{
                    0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 12, 14, 15, 16, 17, 18, 20, 21, 23, 25, 26, 27, 28, 29, 30, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44
            }, new ArrayList<>(Arrays.asList(
            new Tuple<>(10, new CustomItem(Material.BOOK, "&6Instructions"), InventoryUtils.getDenyHandler()),
            new Tuple<>(19, new CustomItem(Material.GLASS_PANE, "&cNo Fuel"), InventoryUtils.getDenyHandler()))),
            new int[] {Furnace.INPUT_SLOT, Furnace.OUTPUT_SLOT, Furnace.FUEL_SLOT}
    ),
    CONTROLLER("&6&lController", 45,
            new int[]{
                    0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 12, 14, 15, 16, 17, 18, 20, 21, 23, 25, 26, 27, 28, 29, 30, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44
            }, new ArrayList<>(Arrays.asList(
            new Tuple<>(10, new CustomItem(Material.BOOK, "&6Instructions"), InventoryUtils.getDenyHandler()),
            new Tuple<>(Controller.RESCAN_SLOT, new CustomItem(Material.ORANGE_STAINED_GLASS_PANE, "&e&lRescan Smeltery"), new InvClickHandler() {
                @Override
                public boolean onClick(Player player, CustomInventory customInv, int slot, ItemStack clickedItem, ClickType clickType) {
                    Controller.scanFootprint(player, customInv.getLocation());
                    return false;
                }
            }))),
            new int[] {Controller.RESCAN_SLOT}
    );

    private final String title;
    private final int size;
    private final int[] background; // TODO: Change to foreground?
    private final List<Tuple<Integer, ItemStack, InvClickHandler>> items;
    private final int[] serializeSlots;

    InventoryTemplate(String title, int size, int[] background, List<Tuple<Integer, ItemStack, InvClickHandler>> items, int[] serializeSlots) {
        this.title = title;
        this.size = size;
        this.background = background;
        this.items = items;
        this.serializeSlots = serializeSlots;
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

    public int[] getSerializeSlots() {
        return serializeSlots;
    }
}
