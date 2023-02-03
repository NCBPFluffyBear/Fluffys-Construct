package io.ncbpfluffybear.fluffysconstruct.handlers;

import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.inventory.CustomInventory;
import io.ncbpfluffybear.fluffysconstruct.items.FCItem;
import io.ncbpfluffybear.fluffysconstruct.items.InventoryBlock;
import io.ncbpfluffybear.fluffysconstruct.utils.ItemUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;

public class FCInventoryHandler implements Listener {

    private final HashMap<Player, CustomInventory> openInventories;

    public FCInventoryHandler() {
        this.openInventories = new HashMap<>();
    }

    @EventHandler
    private void onOpen(PlayerInteractEvent e) {
        if (!e.hasBlock() || e.getAction() != Action.RIGHT_CLICK_BLOCK) { // 1st condition redundant?
            return;
        }

        Block block = e.getClickedBlock();

        if (FCPlugin.getBlockRepository().isInventoryBlock(block.getLocation())) {
            FCItem item = ItemUtils.getFCItem(block);
            this.openInventories.put(e.getPlayer(), ((InventoryBlock) item).onOpen(e.getPlayer(), block.getLocation()));
            e.setCancelled(true);
        }

    }

    @EventHandler
    private void onClose(InventoryCloseEvent e) {
        this.openInventories.remove((Player) e.getPlayer());
    }

    /**
     * If the {@link io.ncbpfluffybear.fluffysconstruct.inventory.InvClickHandler returns true, allow item to be removed from slot.}
     */
    @EventHandler
    private void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) e.getWhoClicked();
        boolean allowEvent = true;

        if (this.openInventories.containsKey(player)) {
            CustomInventory handledInventory = this.openInventories.get(player);
            if (e.getClickedInventory() != handledInventory.getInventory()) {
                return;
            }

            allowEvent = this.openInventories.get(player).callClickHandler(player, e.getSlot(), e.getCurrentItem(), e.getClick());
        }

        e.setCancelled(!allowEvent);
    }

}
