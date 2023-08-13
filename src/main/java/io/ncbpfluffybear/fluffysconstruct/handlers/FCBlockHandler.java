package io.ncbpfluffybear.fluffysconstruct.handlers;

import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.api.data.persistent.DirtyType;
import io.ncbpfluffybear.fluffysconstruct.api.inventory.CustomInventory;
import io.ncbpfluffybear.fluffysconstruct.api.items.FCItem;
import io.ncbpfluffybear.fluffysconstruct.items.Clocked;
import io.ncbpfluffybear.fluffysconstruct.items.InventoryBlock;
import io.ncbpfluffybear.fluffysconstruct.items.Placeable;
import io.ncbpfluffybear.fluffysconstruct.utils.ChatUtils;
import io.ncbpfluffybear.fluffysconstruct.utils.ItemUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Iterator;

public class FCBlockHandler implements Listener {

    public FCBlockHandler() {
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent e) {
        FCItem item = ItemUtils.getFCItem(e.getItemInHand());

        if (item == null) { // Not FC Item
            return;
        }

        if (!(item instanceof Placeable)) { // Block non-placeables
            e.setCancelled(true);
            return;
        }

        FCPlugin.getBlockRepository().addFCItemAt(e.getBlock().getLocation(), item);
        FCPlugin.getPersistenceUtils().markDirty(e.getBlock().getLocation(), DirtyType.WORLD);

        Location location = e.getBlockPlaced().getLocation();

        if (item instanceof Clocked) {
            FCPlugin.getBlockRepository().addClockedBlock(item, location);
        }

        if (item instanceof InventoryBlock) {
            FCPlugin.getBlockRepository().addInventoryBlock(item, location);
            CustomInventory inventory = ((InventoryBlock) item).createInventory(location);
            if (inventory != null) {
                FCPlugin.getInventoryRepository().putInventory(location, inventory); // Create an empty inventory for this block
            }
            FCPlugin.getPersistenceUtils().markDirty(e.getBlock().getLocation(), DirtyType.INVENTORY);
        }

        ((Placeable) item).onPlace(location);

        ChatUtils.broadcast("PLACED " + item.getKey() + " (" + item.getId() + ")");
    }

    /**
     * Prevent FC Blocks from dropping their default drops
     */
    @EventHandler
    private void onBlockBreak(BlockBreakEvent e) {
        Block b = e.getBlock();

        FCItem item = ItemUtils.getFCItem(b);

        if (item != null) {
            e.setDropItems(false);
        }
    }

    @EventHandler
    private void onBlockExplode(BlockExplodeEvent e) {
        Iterator<Block> blocks = e.blockList().iterator();

        while (blocks.hasNext()) {
            Block next = blocks.next();
            if (ItemUtils.getFCItem(next) != null) {
                blocks.remove();
            }
        }
    }

    @EventHandler
    private void onCustomBlockRemove(BlockBreakEvent e) {
        FCItem fcItem = FCPlugin.getBlockRepository().getFCItemAt(e.getBlock().getLocation());
        if (fcItem == null) return;

        ChatUtils.broadcast("REMOVE " + fcItem.getKey());

        Location location = e.getBlock().getLocation();

        FCItem item = ItemUtils.getFCItem(e.getBlock());

        if (item == null) {
            return;
        }

        if (item instanceof Clocked) {
            FCPlugin.getBlockRepository().removeClockedBlock(item, location);
        }

        if (item instanceof InventoryBlock) {
            FCPlugin.getBlockRepository().removeInventoryBlock(item, location);
            FCPlugin.getInventoryRepository().removeInventory(location);
        }

        ((Placeable) item).onBreak(location); // All placed blocks MUST be placeable

        location.getWorld().dropItem(location, item.getItemStack());
        FCPlugin.getPersistenceUtils().markDirty(e.getBlock().getLocation(), DirtyType.WORLD);
    }

    @EventHandler
    private void onOpen(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Block block = e.getClickedBlock();
        FCItem fcBlock = ItemUtils.getFCItem(block);

        if (fcBlock instanceof Placeable) {
            ((Placeable) fcBlock).onInteract(block, e.getPlayer(), e.getItem());
        }

    }
}
