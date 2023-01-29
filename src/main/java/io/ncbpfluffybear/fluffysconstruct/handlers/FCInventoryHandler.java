package io.ncbpfluffybear.fluffysconstruct.handlers;

import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.items.FCItem;
import io.ncbpfluffybear.fluffysconstruct.items.InventoryBlock;
import io.ncbpfluffybear.fluffysconstruct.utils.ItemUtils;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class FCInventoryHandler implements Listener {

    @EventHandler
    private void onOpen(PlayerInteractEvent e) {
        if (!e.hasBlock()) {return;}

        Block block = e.getClickedBlock();

        if (FCPlugin.getBlockRepository().isInventoryBlock(block.getLocation())) {
            FCItem item = ItemUtils.getFCItem(block);
            ((InventoryBlock) item).onOpen(block.getLocation());
        }

    }

}
