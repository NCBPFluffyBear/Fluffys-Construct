package io.ncbpfluffybear.fluffysconstruct.handlers;

import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.inventory.CustomInventory;
import io.ncbpfluffybear.fluffysconstruct.inventory.InventoryRepository;
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

    private final Map<Player, Location> inventories;
    private final InventoryRepository repository;

    public FCInventoryHandler(InventoryRepository repository) {
        this.inventories = new HashMap<>();
        this.repository = repository;
    }

    @EventHandler
    private void onOpen(PlayerInteractEvent e) {
        if (!e.hasBlock() || e.getAction() != Action.RIGHT_CLICK_BLOCK) { // 1st condition redundant?
            return;
        }

        Block block = e.getClickedBlock();

        if (FCPlugin.getBlockRepository().isInventoryBlock(block.getLocation())) {
            this.inventories.put(e.getPlayer(), block.getLocation());
            FCPlugin.getInventoryRepository().getInventory(block.getLocation()).open(e.getPlayer());
            e.setCancelled(true);
        }

    }

    @EventHandler
    private void onClose(InventoryCloseEvent e) {
        this.inventories.remove((Player) e.getPlayer());
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
        boolean cancelEvent = false;

        if (this.inventories.containsKey(player)) {
            CustomInventory handledInventory = this.repository.getInventory(this.inventories.get(player));
            if (e.getClickedInventory() != handledInventory.getInventory()) {
                return;
            }

            cancelEvent = !handledInventory.callClickHandler(player, e.getSlot(), e.getCurrentItem(), e.getClick());
        }

        if (cancelEvent) {
            e.setCancelled(true);
        }
    }

}
