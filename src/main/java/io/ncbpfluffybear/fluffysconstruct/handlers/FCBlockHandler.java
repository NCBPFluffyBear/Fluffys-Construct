package io.ncbpfluffybear.fluffysconstruct.handlers;

import com.jeff_media.customblockdata.CustomBlockData;
import com.jeff_media.customblockdata.events.CustomBlockDataMoveEvent;
import com.jeff_media.customblockdata.events.CustomBlockDataRemoveEvent;
import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.inventory.CustomInventory;
import io.ncbpfluffybear.fluffysconstruct.items.Clocked;
import io.ncbpfluffybear.fluffysconstruct.items.FCItem;
import io.ncbpfluffybear.fluffysconstruct.items.InventoryBlock;
import io.ncbpfluffybear.fluffysconstruct.items.Placeable;
import io.ncbpfluffybear.fluffysconstruct.utils.Constants;
import io.ncbpfluffybear.fluffysconstruct.utils.ItemUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.persistence.PersistentDataType;

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

        CustomBlockData blockData = new CustomBlockData(e.getBlock(), FCPlugin.getInstance());
        blockData.set(Constants.FC_BLOCK_KEY, PersistentDataType.INTEGER, item.getId());

        Location location = e.getBlockPlaced().getLocation();

        if (item instanceof Clocked) {
            FCPlugin.getBlockRepository().addClocked(item, location);
        }

        if (item instanceof InventoryBlock) {
            FCPlugin.getBlockRepository().addInventoryBlock(location);
            CustomInventory inventory = ((InventoryBlock) item).createInventory();
            if (inventory != null) {
                inventory.setLocation(location);
                FCPlugin.getInventoryRepository().addInventory(location, inventory); // Create an empty inventory for this block
            }
        }

        ((Placeable) item).onPlace(location);

        System.out.println("PLACED " + item.getKey() + " (" + item.getId() + ")");
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
                System.out.println("Removing " + next);
                blocks.remove();
            }
        }
    }

    @EventHandler
    private void onCustomBlockRemove(CustomBlockDataRemoveEvent e) {
        System.out.println("REMOVE " + e.getCustomBlockData().get(Constants.FC_BLOCK_KEY, PersistentDataType.INTEGER));

        Block b = e.getBlock();

        FCItem item = ItemUtils.getFCItem(b);

        if (item == null) {
            return;
        }

        if (item instanceof Clocked) {
            FCPlugin.getBlockRepository().removeClocked(item, b.getLocation());
        }

        if (item instanceof InventoryBlock) {
            FCPlugin.getBlockRepository().removeInventoryBlock(b.getLocation());
        }

        ((Placeable) item).onBreak(b.getLocation()); // All placed blocks MUST be placeable

        b.getWorld().dropItem(b.getLocation(), item.getItemStack());
    }

    @EventHandler
    private void onCustomBlockMove(CustomBlockDataMoveEvent e) {
        e.setCancelled(true); // TODO This does not actually prevent block movements!
        System.out.println("MOVE " + e.getCustomBlockData().get(Constants.FC_BLOCK_KEY, PersistentDataType.INTEGER));
    }
}
