package io.ncbpfluffybear.fluffysconstruct.items.specializeditems.smeltery;

import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.api.data.Offset;
import io.ncbpfluffybear.fluffysconstruct.api.data.persistent.blockdata.BlockDataRepository;
import io.ncbpfluffybear.fluffysconstruct.api.inventory.CustomInventory;
import io.ncbpfluffybear.fluffysconstruct.api.inventory.InvClickHandler;
import io.ncbpfluffybear.fluffysconstruct.api.items.CustomItem;
import io.ncbpfluffybear.fluffysconstruct.api.items.FCItem;
import io.ncbpfluffybear.fluffysconstruct.api.items.InfoItemBuilder;
import io.ncbpfluffybear.fluffysconstruct.data.SmelterySystem;
import io.ncbpfluffybear.fluffysconstruct.items.Clocked;
import io.ncbpfluffybear.fluffysconstruct.items.InventoryBlock;
import io.ncbpfluffybear.fluffysconstruct.items.ItemList;
import io.ncbpfluffybear.fluffysconstruct.items.Placeable;
import io.ncbpfluffybear.fluffysconstruct.setup.InventoryTemplate;
import io.ncbpfluffybear.fluffysconstruct.setup.Molten;
import io.ncbpfluffybear.fluffysconstruct.utils.ChatUtils;
import io.ncbpfluffybear.fluffysconstruct.utils.Constants;
import io.ncbpfluffybear.fluffysconstruct.utils.EntityUtils;
import io.ncbpfluffybear.fluffysconstruct.utils.ItemUtils;
import io.ncbpfluffybear.fluffysconstruct.utils.Keys;
import io.ncbpfluffybear.fluffysconstruct.utils.SmelteryFootprint;
import io.ncbpfluffybear.fluffysconstruct.utils.SmelteryUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
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
import java.util.HashSet;
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

    private static final CustomItem NO_SMELTERY_ITEM = new CustomItem(Material.RED_STAINED_GLASS_PANE, "&aNo Smeltery Found", "&e>Click to rescan");
    private static final CustomItem NO_FUEL = new CustomItem(Material.GLASS_PANE, "&cNo Fuel");

    private static final InfoItemBuilder SELECTION_ITEM = new InfoItemBuilder(Material.GLASS_PANE, "&e%s",
            "&ePress one of the following keys:", "&7<1> Melt 1", "&7<2> Melt 32", "&7<3> Melt 64",
            "&6Remaining: %d"
    );

    // TODO: Number id select action
    public Controller(String key, int id, Material material, String name, String... lore) {
        super(key, id, material, name, lore);
    }

    @Override
    public CustomInventory createInventory(Location location) {
        return new CustomInventory(InventoryTemplate.CONTROLLER, location);
    }

    @Override
    public void onPlace(Location location) {
        // Create SmelterySystem
        SmelteryUtils.createSystem(location);
    }

    @Override
    public boolean onOpen(CustomInventory customInventory, Player player, ItemStack item) {
        SmelterySystem system = SmelteryUtils.getSystem(customInventory.getLocation());
        if (!system.isActive() && !scanFootprint(player, customInventory.getLocation())) {
            return false;
        }

        updateLava(system.calculateTotalLava(), customInventory);
        reloadMeltables(player, customInventory); // TODO: Don't let multiple people access at once
        updateMelted(customInventory, system.getContents());

        return true;
    }

    private void updateLava(int totalLava, CustomInventory customInventory) {
        if (totalLava == 0) {
            customInventory.setItem(TOTAL_FUEL_SLOT, NO_FUEL);
        } else {
            customInventory.setItem(TOTAL_FUEL_SLOT, new CustomItem(Material.RED_STAINED_GLASS_PANE, "&aFuel Level", "&7" + totalLava));
        }
    }

    private void reloadMeltables(Player player, CustomInventory customInventory) {
        // Clear selection slots
        for (int slot : SELECTION_SLOTS) {
            if (customInventory.getItemInSlot(slot).getType() != NO_SELECTION_MATERIAL) {
                customInventory.setItem(slot, new CustomItem(NO_SELECTION_MATERIAL, " "));
            }
        }

        // Populate meltable menu
        Inventory playerInv = player.getInventory();
        for (Material meltable : FCPlugin.getMolten().getMeltable()) {
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
                customInventory.setItem(nextSelectionSlot, SELECTION_ITEM.setMaterial(meltable)
                        .setNameArgs(WordUtils.capitalize(materialName.replace("_", " ")))
                        .setLoreArgs(amount)
                        .build()
                );

                customInventory.addClickHandler(nextSelectionSlot, new InvClickHandler.Event() {
                    @Override
                    public boolean onClick(Player player, CustomInventory customInv, int slot, ItemStack clickedItem, InventoryClickEvent event) {
                        if (event.getClick() == ClickType.NUMBER_KEY) {
                            String systemUUID = BlockDataRepository.getDataAt(customInv.getLocation()).get(Keys.SYSTEM_UUID, PersistentDataType.STRING);
                            meltItem(meltable, player, customInv, FCPlugin.getSmelteryRepository().getSystem(systemUUID), event.getHotbarButton() + 1);
                        }
                        return false;
                    }
                });
            }
        }
    }

    private void meltItem(Material meltable, Player player, CustomInventory customInventory, SmelterySystem system, int hotbarButton) {
        Inventory playerInv = player.getInventory();
        List<Molten.Product> products = FCPlugin.getMolten().getProducts(meltable);
        Map<Integer, ? extends ItemStack> matLocations = playerInv.all(meltable);

        if (matLocations.isEmpty()) {
            return;
        }

        if (playerInv.contains(meltable)) {
            switch (hotbarButton) {
                case 1 -> {
                    if (melt(system, products, 1)) {
                        consumeMaterial(matLocations, 1);
                    }
                }
                case 2 -> {
                    if (melt(system, products, 32)) {
                        consumeMaterial(matLocations, 32);
                    }
                }
                case 3 -> {
                    if (melt(system, products, 64)) {
                        consumeMaterial(matLocations, 64);
                    }
                }
                default -> {
                    return;
                }
            }

            updateMelted(customInventory, system.getContents());
            reloadMeltables(player, customInventory);
        }
    }

    private void updateMelted(CustomInventory inventory, Map<Molten.MoltenMaterial, Integer> melted) {
        List<String> lore = new ArrayList<>();
        melted.forEach((key, value) -> lore.add("&e" + key + ": " + value + "mB"));
        inventory.setItem(MOLTEN_METALS_SLOT, new CustomItem(Material.LIME_STAINED_GLASS_PANE, "&eMelted Items", lore.toArray(new String[0])));
    }

    /**
     * Consumes items from a player's inventory
     */
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
                return;
            }
        }
    }

    private boolean melt(SmelterySystem system, List<Molten.Product> products, int multiplier) {
        if (system.melt(products, multiplier)) {
            system.getController().getWorld().playSound(system.getController(), Sound.BLOCK_FIRE_EXTINGUISH, 1f, -5f);
            return true;
        } else {
            system.getController().getWorld().playSound(system.getController(), Sound.UI_BUTTON_CLICK, 1f, -5f);
            return false;
        }
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
        Set<Location> smelteryBlocks = new HashSet<>();
        Set<Location> tanks = new HashSet<>();
        BlockFace smelteryBack = ((Directional) controller.getBlockData()).getFacing().getOppositeFace();
        BlockFace smelteryRight = getRight(smelteryBack);

        SmelteryFootprint schematic = SmelteryFootprint.THREE_BY_THREE; // TODO: Let players select their footprints

        for (Offset offset : schematic.getBase()) {
            Block baseBlock = controller.getRelative(smelteryBack, offset.back()).getRelative(smelteryRight, offset.right()).getRelative(BlockFace.UP, offset.up());
            if (ItemUtils.getFCItem(baseBlock) != ItemList.SEARED_BRICKS) {
                showInvalid(controllerLoc, inv, baseBlock.getLocation(), player);
                return false;
            }

            smelteryBlocks.add(baseBlock.getLocation());
        }

        int depth = 0;
        search:
        for (; depth < MAX_SMELTERY_HEIGHT; depth++) {
            List<Location> wallRing = new ArrayList<>();
            for (Offset offset : schematic.getWall()) {
                Block wallBlock = controller.getRelative(smelteryBack, offset.back()).getRelative(smelteryRight, offset.right()).getRelative(BlockFace.UP, depth);
                FCItem fcBlock = ItemUtils.getFCItem(wallBlock);

                if (fcBlock == ItemList.CONTROLLER) { // TODO: Multiple controllers is an issue?
                    continue; // Ignore controller
                }

                if (!(fcBlock instanceof SearedBricks)) {
                    if (depth == 0) { // Only give a hint if there is no wall at all yet
                        showInvalid(controllerLoc, inv, wallBlock.getLocation(), player);
                        return false;
                    }

                    break search; // Don't add these blocks D
                }

                wallRing.add(wallBlock.getLocation());

                if (fcBlock instanceof SearedTank) {
                    tanks.add(wallBlock.getLocation());
                }
            }
            smelteryBlocks.addAll(wallRing);
        }

        SmelterySystem system = SmelteryUtils.getSystem(controllerLoc);
        if (system == null) {
            system = SmelteryUtils.createSystem(controllerLoc);
        }
        system.addBricks(smelteryBlocks);
        system.addFuelTanks(tanks);
        system.setActive(true);
        system.setMaxVolume(schematic.getBaseVolume() * depth * Constants.BUCKET_MB);

        ChatUtils.sendMsg(player, "ITEMS.CONTROLLER.SUCCESSFUL_CREATION", depth);
        inv.setItem(RESCAN_SLOT, new CustomItem(Material.LIME_STAINED_GLASS_PANE, "&aSmeltery Created", "&7Depth: " + depth));
        return true;
    }

    /**
     * Warns the player of an invalid footprint and shows the offending block.
     * Also sets controller validity to false.
     */
    private static void showInvalid(Location controllerLoc, CustomInventory inv, Location problemLoc, Player player) {
        ChatUtils.sendMsg(player, "ITEMS.CONTROLLER.MISSING_BRICKS", problemLoc);
        inv.setItem(RESCAN_SLOT, NO_SMELTERY_ITEM);
        EntityUtils.highlightBlock(problemLoc.add(0.5, 0, 0.5));
        SmelteryUtils.getSystem(controllerLoc).setMaxVolume(0);
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
