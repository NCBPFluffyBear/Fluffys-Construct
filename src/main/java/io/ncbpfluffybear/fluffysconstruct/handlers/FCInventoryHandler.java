package io.ncbpfluffybear.fluffysconstruct.handlers;

import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.inventory.CustomInventory;
import io.ncbpfluffybear.fluffysconstruct.inventory.InventoryRepository;
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

public class FCInventoryHandler implements Listener {

    private final InventoryRepository repository;

    public FCInventoryHandler(InventoryRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    private void onOpen(PlayerInteractEvent e) {
        if (!e.hasBlock() || e.getAction() != Action.RIGHT_CLICK_BLOCK) { // 1st condition redundant?
            return;
        }

        Block block = e.getClickedBlock();

        if (FCPlugin.getBlockRepository().isInventoryBlock(block.getLocation())) {
            FCItem item = ItemUtils.getFCItem(block);
            CustomInventory inventory = ((InventoryBlock) item).onOpen(e.getPlayer(), block.getLocation());
            this.repository.addInventory(block.getLocation(), e.getPlayer(), inventory);
            e.setCancelled(true);
        }

    }

    @EventHandler
    private void onClose(InventoryCloseEvent e) {
        this.repository.removeInventory((Player) e.getPlayer());
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

        if (this.repository.hasPlayer(player)) {
            CustomInventory handledInventory = this.repository.getInventory(player);
            if (e.getClickedInventory() != handledInventory.getInventory()) {
                return;
            }

            allowEvent = handledInventory.callClickHandler(player, e.getSlot(), e.getCurrentItem(), e.getClick());
        }

        e.setCancelled(!allowEvent);
    }

}
