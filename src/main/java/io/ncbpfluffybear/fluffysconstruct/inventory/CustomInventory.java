package io.ncbpfluffybear.fluffysconstruct.inventory;

import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.data.Tuple;
import io.ncbpfluffybear.fluffysconstruct.inventory.slotdata.ProgressSlotData;
import io.ncbpfluffybear.fluffysconstruct.inventory.slotdata.SlotData;
import io.ncbpfluffybear.fluffysconstruct.items.CustomItem;
import io.ncbpfluffybear.fluffysconstruct.utils.InventoryUtils;
import io.ncbpfluffybear.fluffysconstruct.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class CustomInventory {

    private final CustomItem background = new CustomItem(Material.ORANGE_STAINED_GLASS_PANE, " ");

    private final String title;
    private final int size;
    private final ItemStack[] items;
    private Inventory inv;
    private final Map<Integer, InvClickHandler> clickHandlers;

    public CustomInventory(InventoryTemplate template) {
        this.title = template.getTitle();
        this.size = template.getSize();
        this.items = new ItemStack[this.size];
        this.clickHandlers = new HashMap<>();
        this.setBackground(template.getBackground());
        for (Tuple<Integer, ItemStack, InvClickHandler> itemPair : template.getItems()) {
            items[itemPair.getFirst()] = itemPair.getSecond();
            clickHandlers.put(itemPair.getFirst(), itemPair.getThird());
        }
    }

//    public InventoryBuilder(InventoryPackage invPackage) {
//
//    }

    /**
     * To be used after the inventory is already built.
     */
    public void setItem(int slot, ItemStack item) {
        if (this.inv == null) {
            build();
        }

        this.inv.setItem(slot, item);
    }

    private void setBackground(int[] slots) {
        for (int slot : slots) {
            items[slot] = background;
            addClickHandler(slot, InventoryUtils.getDenyHandler());
        }
    }

    private void build() {
        this.inv = Bukkit.createInventory(null, this.size, StringUtils.color(this.title));
        for (int i = 0; i < this.size; i++) {
            this.inv.setItem(i, this.items[i]);
        }
    }

    public Inventory getInventory() {
        if (this.inv == null) {
            build();
        }

        return this.inv;
    }

    public void open(Player... players) {
        if (this.inv == null) {
            build();
        }

        for (Player player : players) {
            player.openInventory(this.inv);
        }
    }

    public void addClickHandler(int slot, InvClickHandler handler) {
        clickHandlers.put(slot, handler);
    }

    public boolean callClickHandler(Player player, int slot, ItemStack clickedItem, ClickType clickType) {
        if (clickHandlers.containsKey(slot)) {
            return clickHandlers.get(slot).onClick(player, slot, clickedItem, clickType);
        }

        return true;
    }

}
