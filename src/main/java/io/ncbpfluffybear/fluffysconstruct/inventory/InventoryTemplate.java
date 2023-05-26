package io.ncbpfluffybear.fluffysconstruct.inventory;

import io.ncbpfluffybear.fluffysconstruct.items.CustomItem;
import io.ncbpfluffybear.fluffysconstruct.items.specializeditems.Furnace;
import io.ncbpfluffybear.fluffysconstruct.items.specializeditems.smeltery.Controller;
import io.ncbpfluffybear.fluffysconstruct.utils.InventoryUtils;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public enum InventoryTemplate {

    // Chest Slots:
    // 0  1  2  3  4  5  6  7  8
    // 9  10 11 12 13 14 15 16 17
    // 18 19 20 21 22 23 24 25 26
    // 27 28 29 30 31 32 33 34 35
    // 36 37 38 39 40 41 42 43 44
    // 45 46 47 48 49 50 51 52 53

    FURNACE("&6&lFurnace", 5,
            new int[]{
                    Furnace.INPUT_SLOT, Furnace.OUTPUT_SLOT, Furnace.FUEL_SLOT
            }, new SlotTemplate[]{
            new SlotTemplate(10, new CustomItem(Material.BOOK, "&6Instructions")),
            new SlotTemplate(19, new CustomItem(Material.GLASS_PANE, "&cNo Fuel"))
    },
            new int[]{Furnace.INPUT_SLOT, Furnace.OUTPUT_SLOT, Furnace.FUEL_SLOT}
    ),
    CONTROLLER("&6&lController", 6,
            new int[0], new SlotTemplate[]{
            new SlotTemplate(Controller.SELECTION_SLOTS, new CustomItem(Controller.NO_SELECTION_MATERIAL, " ")),
            new SlotTemplate(10, new CustomItem(Material.BOOK, "&6Instructions")),
            new SlotTemplate(Controller.RESCAN_SLOT, new CustomItem(Material.ORANGE_STAINED_GLASS_PANE, "&e&lRescan Smeltery"), new InvClickHandler.Advanced() {
                @Override
                public boolean onClick(Player player, CustomInventory customInv, int slot, ItemStack clickedItem, ClickType clickType) {
                    Controller.scanFootprint(player, customInv.getLocation());
                    return false;
                }
            }),
            new SlotTemplate(Controller.TOTAL_FUEL_SLOT, new CustomItem(Material.GLASS_PANE, "No Lava Found")),
            new SlotTemplate(Controller.MOLTEN_METALS_SLOT, new CustomItem(Material.GREEN_STAINED_GLASS_PANE, "No Molten Metals"))
    },
            new int[]{Controller.RESCAN_SLOT}
    );

    private final String title;
    private final SlotTemplate[] defaultItems;
    private final int[] serializeSlots;
    private final int[] background;
    private final int size;

    InventoryTemplate(String title, int rows, int[] blankSlots, SlotTemplate[] defaultItems, int[] serializeSlots) {
        this.title = title;
        this.size = rows * 9;
        this.defaultItems = defaultItems;
        this.serializeSlots = serializeSlots;

        Set<Integer> foreground = new HashSet<>();
        Collections.addAll(foreground, ArrayUtils.toObject(blankSlots));
        for (SlotTemplate item : defaultItems) {
            Collections.addAll(foreground, ArrayUtils.toObject(item.getSlots()));
        }

        this.background = InventoryUtils.invertSlots(size, foreground.stream().mapToInt(i -> i).toArray());
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

    public SlotTemplate[] getDefaultItems() {
        return defaultItems;
    }

    public int[] getSerializeSlots() {
        return serializeSlots;
    }

    static class SlotTemplate {
        private final int[] slots;
        private final ItemStack item;
        private final InvClickHandler handler;

        private SlotTemplate(int slot, ItemStack item) {
            this.slots = new int[]{slot};
            this.item = item;
            this.handler = InventoryUtils.getDenyHandler();
        }

        private SlotTemplate(int slot, ItemStack item, InvClickHandler handler) {
            this.slots = new int[]{slot};
            this.item = item;
            this.handler = handler;
        }

        private SlotTemplate(int[] slots, ItemStack item) {
            this.slots = slots;
            this.item = item;
            this.handler = InventoryUtils.getDenyHandler();
        }

        private SlotTemplate(int[] slots, ItemStack item, InvClickHandler handler) {
            this.slots = slots;
            this.item = item;
            this.handler = handler;
        }

        public int[] getSlots() {
            return slots;
        }

        public ItemStack getItem() {
            return item;
        }

        public InvClickHandler getHandler() {
            return handler;
        }
    }
}
