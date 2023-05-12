package io.ncbpfluffybear.fluffysconstruct.items.specializeditems;

import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.inventory.CustomInventory;
import io.ncbpfluffybear.fluffysconstruct.inventory.InventoryTemplate;
import io.ncbpfluffybear.fluffysconstruct.items.FCItem;
import io.ncbpfluffybear.fluffysconstruct.items.InventoryBlock;
import io.ncbpfluffybear.fluffysconstruct.items.ItemList;
import io.ncbpfluffybear.fluffysconstruct.items.Placeable;
import io.ncbpfluffybear.fluffysconstruct.utils.ChatUtils;
import io.ncbpfluffybear.fluffysconstruct.utils.Constants;
import io.ncbpfluffybear.fluffysconstruct.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SoundGroup;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class Controller extends Placeable implements InventoryBlock {

    public Controller(String key, int id, Material material, String name, String... lore) {
        super(key, id, material, name, lore);
    }

    @Override
    public CustomInventory createInventory() {
        return new CustomInventory(InventoryTemplate.CONTROLLER);
    }

    /**
     * Search all locations for smeltery blocks
     */
    public static boolean rescanSmeltery(Player player, Location controllerLoc) {
        Block controller = controllerLoc.getBlock();
        // Scan base
        List<Block> smelteryBlocks = new ArrayList<>();
        Directional direc = (Directional) controller.getBlockData();
        BlockFace smelteryBack = direc.getFacing().getOppositeFace();
        BlockFace smelteryLeft = getLeft(smelteryBack);
        Block origin = controller.getRelative(BlockFace.DOWN).getRelative(smelteryBack); // Under controller
        for (int row = 0; row < 3; row++) { // Search all base blocks
            Block rowPtr = origin.getRelative(smelteryBack, row); // Right side head of row
            for (int col = 0; col < 3; col++) {
                Block baseBlock = rowPtr.getRelative(smelteryLeft, col); // Shift ptr left
                FCItem fcItem = ItemUtils.getFCItem(baseBlock);

                if (fcItem != ItemList.SEARED_BRICKS) {
                    ChatUtils.send(player, "&cSeared bricks missing at " + baseBlock.getX() + ", " + baseBlock.getY() + ", " + baseBlock.getZ());
                    ChatUtils.send(player, "&cBlock has been marked with a red dot");
                    FallingBlock indicator = player.getWorld().spawnFallingBlock(baseBlock.getLocation().add(0.5, 0, 0.5), Material.GLASS, (byte) 0);
                    indicator.setGlowing(true);
                    indicator.setGravity(false);
                    indicator.getPersistentDataContainer().set(Constants.FC_ENTITY_KEY, PersistentDataType.BYTE, (byte) 1);
                    Bukkit.getScheduler().runTaskLater(FCPlugin.getInstance(), indicator::remove, 20L * 5); // Kill indicator
                    return false;
                }

                smelteryBlocks.add(baseBlock);
            }
        }

        // Tag all the blocks after searching
        for (Block block : smelteryBlocks) {
            SearedBricks.setController(block, controllerLoc);
        }

        return false;
    }

    private static BlockFace getLeft(BlockFace face) {
        switch (face) {
            case NORTH:
                return BlockFace.WEST;
            case EAST:
                return BlockFace.NORTH;
            case SOUTH:
                return BlockFace.EAST;
            case WEST:
                return BlockFace.SOUTH;
        }

        return BlockFace.SELF;
    }
}
