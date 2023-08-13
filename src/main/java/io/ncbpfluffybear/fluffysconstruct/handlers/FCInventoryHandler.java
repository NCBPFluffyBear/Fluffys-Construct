package io.ncbpfluffybear.fluffysconstruct.handlers;

import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.api.inventory.CustomInventory;
import io.ncbpfluffybear.fluffysconstruct.api.inventory.InvClickHandler;
import io.ncbpfluffybear.fluffysconstruct.api.inventory.InventoryRepository;
import io.ncbpfluffybear.fluffysconstruct.api.items.FCItem;
import io.ncbpfluffybear.fluffysconstruct.items.InventoryBlock;
import io.ncbpfluffybear.fluffysconstruct.utils.ItemUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;

public class FCInventoryHandler implements Listener {

    private final Map<Player, Location> openInventories;
    private final InventoryRepository repository;

    public FCInventoryHandler(InventoryRepository repository) {
        this.openInventories = new HashMap<>();
        this.repository = repository;
    }

    /**
     * Attempts to open an inventory for a player
     */
    @EventHandler
    private void onOpen(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Block block = e.getClickedBlock();

        if (FCPlugin.getInventoryRepository().hasInventory(block.getLocation())) {
            this.openInventories.put(e.getPlayer(), block.getLocation());
            CustomInventory inv = repository.getInventory(block.getLocation());
            FCItem fcItem = ItemUtils.getFCItem(block);
            if (((InventoryBlock) fcItem).onOpen(inv, e.getPlayer(), e.getItem())) {
                inv.open(e.getPlayer());
            }
            e.setCancelled(true);
        }

    }

    @EventHandler
    private void onClose(InventoryCloseEvent e) {
        this.openInventories.remove((Player) e.getPlayer());
    }

    /**
     * If the {@link InvClickHandler returns true, allow item to be removed from slot.}
     */
    @EventHandler
    private void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) e.getWhoClicked();
        boolean cancelEvent = false;

        if (this.openInventories.containsKey(player)) {
            CustomInventory customInv = this.repository.getInventory(this.openInventories.get(player));
            if (e.getClickedInventory() != customInv.getInventory() && !customInv.isInvClickable()) {
                e.setCancelled(true); // Deny player inv clicks
                return;
            }

            cancelEvent = !customInv.callClickHandler(player, customInv, e); //TODO BLock event if an error occurs
        }

        if (cancelEvent) {
            e.setCancelled(true);
        }
    }

}
