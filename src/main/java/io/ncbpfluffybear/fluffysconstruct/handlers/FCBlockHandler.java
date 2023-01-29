package io.ncbpfluffybear.fluffysconstruct.handlers;

import com.jeff_media.customblockdata.CustomBlockData;
import com.jeff_media.customblockdata.events.CustomBlockDataMoveEvent;
import com.jeff_media.customblockdata.events.CustomBlockDataRemoveEvent;
import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.items.Clocked;
import io.ncbpfluffybear.fluffysconstruct.items.FCItem;
import io.ncbpfluffybear.fluffysconstruct.items.InventoryBlock;
import io.ncbpfluffybear.fluffysconstruct.utils.Constants;
import io.ncbpfluffybear.fluffysconstruct.utils.ItemUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class FCBlockHandler implements Listener {

    public FCBlockHandler() {
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent e) {
        FCItem item = ItemUtils.getFCItem(e.getItemInHand());
        if (item == null) {
            System.out.println("Not FC");
            return;
        }

        CustomBlockData blockData = new CustomBlockData(e.getBlock(), FCPlugin.getInstance());
        blockData.set(Constants.FC_BLOCKMETA_KEY, PersistentDataType.INTEGER, item.getId());

        Location location = e.getBlockPlaced().getLocation();

        if (item instanceof Clocked) {
            FCPlugin.getBlockRepository().addClocked(item, location);
        }

        if (item instanceof InventoryBlock) {
            FCPlugin.getBlockRepository().addInventoryBlock(location);
        }

        System.out.println("PLACED " + item.getKey() + " (" + item.getId() + ")");
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent e) {
        Block b = e.getBlock();

        FCItem item = ItemUtils.getFCItem(b);

        if (item == null) {return;}

        if (item instanceof Clocked) {
            FCPlugin.getBlockRepository().removeClocked(item, b.getLocation());
        }

        if (item instanceof InventoryBlock) {
            FCPlugin.getBlockRepository().removeInventoryBlock(b.getLocation());
        }

        e.setDropItems(false);
        b.getWorld().dropItem(b.getLocation(), item.getItemStack());
    }

    @EventHandler
    private void onBlockExplode(BlockExplodeEvent e) {
        boolean hasCustom = false;

        for (Block b : e.blockList()) {
            if (!CustomBlockData.hasCustomBlockData(b, FCPlugin.getInstance())) {
                return;
            }

            hasCustom = true;
            PersistentDataContainer blockData = new CustomBlockData(e.getBlock(), FCPlugin.getInstance());
            int id = blockData.getOrDefault(Constants.FC_BLOCKMETA_KEY, PersistentDataType.INTEGER, -1);

            FCItem item = ItemUtils.getFCItem(id);
            if (item == null) {
                return;
            }

            b.getWorld().dropItem(b.getLocation(), item.getItemStack());
        }

        if (hasCustom) {
            e.setYield(0f);
        }
    }

    @EventHandler
    private void onCustomBlockRemove(CustomBlockDataRemoveEvent e) {
        System.out.println("REMOVE " + e.getCustomBlockData().get(Constants.FC_BLOCKMETA_KEY, PersistentDataType.INTEGER));
    }

    @EventHandler
    private void onCustomBlockMove(CustomBlockDataMoveEvent e) {
        e.setCancelled(true); // TODO This does not actually prevent block movements!
        System.out.println("MOVE " + e.getCustomBlockData().get(Constants.FC_BLOCKMETA_KEY, PersistentDataType.INTEGER));
    }
}
