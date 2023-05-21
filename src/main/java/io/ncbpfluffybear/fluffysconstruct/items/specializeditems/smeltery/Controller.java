package io.ncbpfluffybear.fluffysconstruct.items.specializeditems.smeltery;

import com.jeff_media.customblockdata.CustomBlockData;
import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.data.Offset;
import io.ncbpfluffybear.fluffysconstruct.inventory.CustomInventory;
import io.ncbpfluffybear.fluffysconstruct.inventory.InventoryTemplate;
import io.ncbpfluffybear.fluffysconstruct.items.CustomItem;
import io.ncbpfluffybear.fluffysconstruct.items.FCItem;
import io.ncbpfluffybear.fluffysconstruct.items.InventoryBlock;
import io.ncbpfluffybear.fluffysconstruct.items.ItemList;
import io.ncbpfluffybear.fluffysconstruct.items.Placeable;
import io.ncbpfluffybear.fluffysconstruct.utils.ChatUtils;
import io.ncbpfluffybear.fluffysconstruct.utils.EntityUtils;
import io.ncbpfluffybear.fluffysconstruct.utils.ItemUtils;
import io.ncbpfluffybear.fluffysconstruct.utils.SmelteryFootprint;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class Controller extends Placeable implements InventoryBlock {

    private static final int MAX_SMELTERY_HEIGHT = 10;
    public static final int RESCAN_SLOT = 19;

    private static final int TOTAL_FUEL_SLOT = 22;
    private static final int MELTED_METALS_SLOT = 25;

    private static final CustomItem NO_SMELTERY_ITEM = new CustomItem(Material.RED_STAINED_GLASS_PANE, "&aNo Smeltery Found", "&e>Click to rescan");
    public static final NamespacedKey VALID_SMELTERY_FLAG = new NamespacedKey(FCPlugin.getInstance(), "valid_smeltery"); // Byte exists or not

// TODO: Number key select action
    public Controller(String key, int id, Material material, String name, String... lore) {
        super(key, id, material, name, lore);
    }

    @Override
    public CustomInventory createInventory(Location location) {
        return new CustomInventory(InventoryTemplate.CONTROLLER, location);
    }

    @Override
    public boolean onOpen(CustomInventory customInventory, Player player, ItemStack item) {
        CustomBlockData data = new CustomBlockData(customInventory.getLocation().getBlock(), FCPlugin.getInstance());
        Byte openable = data.get(VALID_SMELTERY_FLAG, PersistentDataType.BYTE);
        if (openable == null) {
            return scanFootprint(player, customInventory.getLocation());
        }
        return true;
    }

    /**
     * Search all locations for smeltery blocks
     * Returns if a smeltery was successfully found
     */
    public static boolean scanFootprint(Player player, Location controllerLoc) { // TODO: Timeout for rescan?
        Block controller = controllerLoc.getBlock();
        CustomInventory inv = FCPlugin.getInventoryRepository().getInventory(controllerLoc);
        // Scan base
        List<Block> smelteryBlocks = new ArrayList<>();
        BlockFace smelteryBack = ((Directional) controller.getBlockData()).getFacing().getOppositeFace();
        BlockFace smelteryRight = getRight(smelteryBack);

        SmelteryFootprint schematic = SmelteryFootprint.THREE_BY_THREE; // TODO: Let players select their footprints

        for (Offset offset : schematic.getBase()) {
            Block baseBlock = controller.getRelative(smelteryBack, offset.back()).getRelative(smelteryRight, offset.right()).getRelative(BlockFace.UP, offset.up());
            if (ItemUtils.getFCItem(baseBlock) != ItemList.SEARED_BRICKS) {
                ChatUtils.sendMsg(player, "ITEMS.CONTROLLER.MISSING_BRICKS", baseBlock);
                inv.setItem(RESCAN_SLOT, NO_SMELTERY_ITEM);
                EntityUtils.highlightBlock(baseBlock.getLocation().add(0.5, 0, 0.5));
                new CustomBlockData(controllerLoc.getBlock(), FCPlugin.getInstance()).remove(VALID_SMELTERY_FLAG);
                return false;
            }

            smelteryBlocks.add(baseBlock);
        }

        int depth = 0;
        search:
        for (; depth < MAX_SMELTERY_HEIGHT; depth++) {
            List<Block> wallRing = new ArrayList<>();
            for (Offset offset : schematic.getWall()) {
                Block wallBlock = controller.getRelative(smelteryBack, offset.back()).getRelative(smelteryRight, offset.right()).getRelative(BlockFace.UP, depth);
                FCItem fcBlock = ItemUtils.getFCItem(wallBlock);

                if (fcBlock == ItemList.CONTROLLER) { // TODO: Multiple controllers is an issue?
                    continue; // Ignore controller
                }

                if (fcBlock != ItemList.SEARED_BRICKS) {
                    if (depth == 0) { // Only give a hint if there is no functional smeltery

                        return false;
                    }

                    break search; // Don't add these blocks
                }

                wallRing.add(wallBlock);
            }
            smelteryBlocks.addAll(wallRing);
        }

        // Tag all the blocks after searching
        for (Block block : smelteryBlocks) {
            SearedBricks.setController(block, controllerLoc);
        }

        ChatUtils.sendMsg(player, "ITEMS.CONTROLLER.SUCCESSFUL_CREATION", depth);
        inv.setItem(RESCAN_SLOT, new CustomItem(Material.LIME_STAINED_GLASS_PANE, "&aSmeltery Created", "&7Depth: " + depth));
        setValidity(controllerLoc, true);
        return true;
    }

    /**
     * Sets the controller's validity flag
     */
    public static void setValidity(Location controllerLoc, boolean valid) { // TODO Set depth 0?
        if (valid) new CustomBlockData(controllerLoc.getBlock(), FCPlugin.getInstance()).set(VALID_SMELTERY_FLAG, PersistentDataType.BYTE, (byte) 1);
        else new CustomBlockData(controllerLoc.getBlock(), FCPlugin.getInstance()).remove(VALID_SMELTERY_FLAG);
    }

    /**
     * Warns the player of an invalid footprint and shows the offending block.
     * Also sets controller validity to false.
     */
    private static void showInvalid(Location controllerLoc, CustomInventory inv, Location problemLoc, Player player) {
        ChatUtils.sendMsg(player, "ITEMS.CONTROLLER.MISSING_BRICKS", problemLoc);
        inv.setItem(RESCAN_SLOT, NO_SMELTERY_ITEM);
        EntityUtils.highlightBlock(problemLoc.add(0.5, 0, 0.5));
        setValidity(controllerLoc, false);
    }

    private static BlockFace getRight(BlockFace face) {
        return switch (face) {
            case NORTH -> BlockFace.EAST;
            case EAST -> BlockFace.SOUTH;
            case SOUTH -> BlockFace.WEST;
            case WEST -> BlockFace.NORTH;
            default -> BlockFace.SELF;
        };

    }
}
