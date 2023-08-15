package io.ncbpfluffybear.fluffysconstruct.api.inventory;

import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.api.data.persistent.DirtyType;
import io.ncbpfluffybear.fluffysconstruct.api.items.CustomItem;
import io.ncbpfluffybear.fluffysconstruct.setup.InventoryTemplate;
import io.ncbpfluffybear.fluffysconstruct.utils.InventoryUtils;
import io.ncbpfluffybear.fluffysconstruct.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class CustomInventory {

    private final CustomItem background = new CustomItem(Material.BLACK_STAINED_GLASS_PANE, " ");

    private final String title;
    private final int size;
    private final ItemStack[] items;
    private Inventory inv;
    private final Map<Integer, InvClickHandler> clickHandlers;
    private final Location location;
    private final int[] serializeSlots;
    private boolean invClickable;

    public CustomInventory(InventoryTemplate template, Location location) {
        this.location = location;
        this.title = template.getTitle();
        this.size = template.getSize();
        this.items = new ItemStack[this.size];
        this.clickHandlers = new HashMap<>();
        this.setBackground(template.getBackground());
        for (InventoryTemplate.SlotTemplate slotTemplate : template.getDefaultItems()) {
            for (int slot : slotTemplate.getSlots()) {
                items[slot] = slotTemplate.getItem();
                clickHandlers.put(slot, slotTemplate.getHandler());
            }
        }
        this.serializeSlots = template.getSerializeSlots();
        this.invClickable = true;
    }

    /**
     * To be used after the inventory is already built.
     */
    public void setItem(int slot, ItemStack item) {
        FCPlugin.getPersistenceUtils().markBlockDirty(this.location, DirtyType.INVENTORY);
        if (this.inv == null) {
            build();
        }

        this.inv.setItem(slot, item);
    }

    private void setBackground(int[] slots) {
        FCPlugin.getPersistenceUtils().markBlockDirty(this.location, DirtyType.INVENTORY);
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

    @Nullable
    public ItemStack getItemInSlot(int slot) {
        return getInventory().getItem(slot);
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

    public boolean callClickHandler(Player player, CustomInventory customInv, InventoryClickEvent e) {
        FCPlugin.getPersistenceUtils().markBlockDirty(this.location, DirtyType.INVENTORY);
        int slot = e.getSlot();
        if (clickHandlers.containsKey(slot)) {
            InvClickHandler handler = clickHandlers.get(slot);
            if (handler instanceof InvClickHandler.Basic) {
                return ((InvClickHandler.Basic) handler).onClick(player, slot, e.getCurrentItem(), e.getClick());
            } else if (handler instanceof InvClickHandler.Event) {
                return ((InvClickHandler.Event) handler).onClick(player, customInv, slot, e.getCurrentItem(), e);
            } else if (handler instanceof InvClickHandler.Advanced) {
                return ((InvClickHandler.Advanced) handler).onClick(player, customInv, slot, e.getCurrentItem(), e.getClick());
            }
        }

        return true;
    }

    public void setInvClickable(boolean invClickable) {
        this.invClickable = invClickable;
    }

    public boolean isInvClickable() {
        return invClickable;
    }

    public Location getLocation() {
        return this.location;
    }

    /**
     * Packages items in all serializeSlots
     */
    public Map<Integer, ItemStack> getToSerialize() {
        if (serializeSlots == null) {
            return null;
        }

        Map<Integer, ItemStack> toSerialize = new HashMap<>();
        for (int slot : serializeSlots) {
            ItemStack item = getItemInSlot(slot);
            if (item == null || item.getType() == Material.AIR) {
                continue;
            }

            toSerialize.put(slot, item);
        }
        return toSerialize;
    }
}
