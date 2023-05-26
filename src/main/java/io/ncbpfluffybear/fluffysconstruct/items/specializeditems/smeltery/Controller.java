package io.ncbpfluffybear.fluffysconstruct.items.specializeditems.smeltery;

import com.jeff_media.customblockdata.CustomBlockData;
import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.data.Offset;
import io.ncbpfluffybear.fluffysconstruct.data.serialize.Serialize;
import io.ncbpfluffybear.fluffysconstruct.inventory.CustomInventory;
import io.ncbpfluffybear.fluffysconstruct.inventory.InvClickHandler;
import io.ncbpfluffybear.fluffysconstruct.inventory.InventoryTemplate;
import io.ncbpfluffybear.fluffysconstruct.items.Clocked;
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
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Controller extends Placeable implements InventoryBlock, Clocked {

    private static final int MAX_SMELTERY_HEIGHT = 10;
    public static final int RESCAN_SLOT = 19;

    public static final int[] SELECTION_SLOTS = new int[]{37, 38, 39, 40, 41, 42, 43};
    public static final Material NO_SELECTION_MATERIAL = Material.GRAY_STAINED_GLASS_PANE; // Can not be in MELTABLE_ITEMS

    public static final int TOTAL_FUEL_SLOT = 22;
    public static final int MOLTEN_METALS_SLOT = 25;

    private static final Material[] MELTABLE_ITEMS = new Material[]{Material.IRON_INGOT, Material.IRON_ORE};

    private static final CustomItem NO_SMELTERY_ITEM = new CustomItem(Material.RED_STAINED_GLASS_PANE, "&aNo Smeltery Found", "&e>Click to rescan");
    private static final CustomItem NO_FUEL = new CustomItem(Material.GLASS_PANE, "&cNo Fuel");
    public static final NamespacedKey VALID_SMELTERY_FLAG = new NamespacedKey(FCPlugin.getInstance(), "valid_smeltery"); // Byte exists or not
    public static final NamespacedKey TANK_LOCATIONS = new NamespacedKey(FCPlugin.getInstance(), "tank_locations"); // Byte exists or not

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
        if (openable == null && !scanFootprint(player, customInventory.getLocation())) {
            return false;
        }

        calculateTotalLava(data, customInventory);
        scanMeltablesFromPlayer(player, customInventory);

        return true;
    }

    private void calculateTotalLava(CustomBlockData controllerData, CustomInventory customInventory) {
        Set<Location> locations = Serialize.parseLocations(controllerData.get(TANK_LOCATIONS, PersistentDataType.STRING));
        int totalLava = 0;
        for (Location location : locations) {
            totalLava += SearedTank.getLavaLevel(location);
        }
        if (totalLava == 0) {
            customInventory.setItem(TOTAL_FUEL_SLOT, NO_FUEL);
        } else {
            customInventory.setItem(TOTAL_FUEL_SLOT, new CustomItem(Material.RED_STAINED_GLASS_PANE, "&aFuel Level", "&7" + totalLava));
        }
    }

    private void scanMeltablesFromPlayer(Player player, CustomInventory customInventory) {
        for (int slot : SELECTION_SLOTS) {
            if (customInventory.getItemInSlot(slot).getType() != NO_SELECTION_MATERIAL) {
                customInventory.setItem(slot, new CustomItem(NO_SELECTION_MATERIAL, " "));
            }
        }

        Inventory playerInv = player.getInventory();
        for (Material meltable : MELTABLE_ITEMS) {
            if (playerInv.contains(meltable)) {
                int amount = 0;
                for (ItemStack found : playerInv.all(meltable).values()) {
                    amount += found.getAmount();
                }

                int nextSelectionSlot = getNextSelection(customInventory);
                if (nextSelectionSlot == -1) {
                    return; // Open inv
                }

                String materialName = meltable.getKey().getKey();
                customInventory.setItem(nextSelectionSlot, new CustomItem(meltable,
                        materialName.substring(0, 1).toUpperCase() + materialName.substring(1).toLowerCase(),
                        "&7" + amount)); // TODO: Add number key instructions

                customInventory.addClickHandler(nextSelectionSlot, new InvClickHandler.Event() {
                    @Override
                    public boolean onClick(Player player, CustomInventory customInv, int slot, ItemStack clickedItem, InventoryClickEvent event) {
                        if (event.getClick() == ClickType.NUMBER_KEY) {
                            meltItem(meltable, player, customInv, event.getHotbarButton());
                        }
                        return false;
                    }
                });
            }
        }
    }

    private void meltItem(Material meltable, Player player, CustomInventory customInventory, int hotbarButton) {
        Inventory playerInv = player.getInventory();
        Map<Integer, ? extends ItemStack> matLocations = playerInv.all(meltable);
        if (playerInv.contains(meltable)) {
            switch (hotbarButton) {
                case 1 -> consumeMaterial(matLocations, 1);
                case 2 -> consumeMaterial(matLocations, 32);
                case 3 -> consumeMaterial(matLocations, 64);
            }

            scanMeltablesFromPlayer(player, customInventory);
        }
    }

    private void consumeMaterial(Map<Integer, ? extends ItemStack> matLocations, int toConsume) {
        Iterator<? extends ItemStack> it = matLocations.values().iterator();
        while (it.hasNext()) {
            ItemStack next = it.next();
            int nextAmount = next.getAmount();
            if (toConsume > nextAmount) { // Not done looking
                toConsume -= nextAmount;
                next.setAmount(0);
            } else {
                next.setAmount(nextAmount - toConsume); // Last amount to subtract
            }
        }

        // Ignore leftovers
    }


    /**
     * Gets the next fillable selection slot
     */
    private int getNextSelection(CustomInventory inventory) { // TODO: Pages? (More than 7 meltables in inv)
        for (int slot : SELECTION_SLOTS) {
            if (inventory.getItemInSlot(slot).getType() == NO_SELECTION_MATERIAL) {
                return slot;
            }
        }
        return -1;
    }

    @Override
    public void onClock(Location location) {
        CustomInventory inventory = FCPlugin.getInventoryRepository().getInventory(location);
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
        List<Block> tanks = new ArrayList<>();
        BlockFace smelteryBack = ((Directional) controller.getBlockData()).getFacing().getOppositeFace();
        BlockFace smelteryRight = getRight(smelteryBack);

        SmelteryFootprint schematic = SmelteryFootprint.THREE_BY_THREE; // TODO: Let players select their footprints

        for (Offset offset : schematic.getBase()) {
            Block baseBlock = controller.getRelative(smelteryBack, offset.back()).getRelative(smelteryRight, offset.right()).getRelative(BlockFace.UP, offset.up());
            if (ItemUtils.getFCItem(baseBlock) != ItemList.SEARED_BRICKS) {
                showInvalid(controllerLoc, inv, baseBlock.getLocation(), player);
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

                if (!(fcBlock instanceof SearedBricks)) {
                    if (depth == 0) { // Only give a hint if there is no functional smeltery
                        showInvalid(controllerLoc, inv, wallBlock.getLocation(), player);
                        return false;
                    }

                    break search; // Don't add these blocks
                }

                wallRing.add(wallBlock);

                if (fcBlock instanceof SearedTank) {
                    tanks.add(wallBlock);
                }
            }
            smelteryBlocks.addAll(wallRing);
        }

        // Tag all the blocks after searching
        for (Block block : smelteryBlocks) {
            SearedBricks.setController(block, controllerLoc);
        }

        for (Block tank : tanks) {
            addTank(controller, tank);
        }

        ChatUtils.sendMsg(player, "ITEMS.CONTROLLER.SUCCESSFUL_CREATION", depth);
        inv.setItem(RESCAN_SLOT, new CustomItem(Material.LIME_STAINED_GLASS_PANE, "&aSmeltery Created", "&7Depth: " + depth));
        setValidity(controllerLoc, true);
        return true;
    }

    private static void addTank(Block controller, Block tank) {
        CustomBlockData data = new CustomBlockData(controller, FCPlugin.getInstance());
        String locations = data.get(TANK_LOCATIONS, PersistentDataType.STRING);
        data.set(TANK_LOCATIONS, PersistentDataType.STRING, Serialize.addLocation(locations, tank.getLocation()));
    }

    /**
     * Sets the controller's validity flag
     */
    public static void setValidity(Location controllerLoc, boolean valid) { // TODO Set depth 0?
        if (valid)
            new CustomBlockData(controllerLoc.getBlock(), FCPlugin.getInstance()).set(VALID_SMELTERY_FLAG, PersistentDataType.BYTE, (byte) 1);
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

    /**
     * Gets the clockwise blockface from a top-down view
     */
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
