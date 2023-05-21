package io.ncbpfluffybear.fluffysconstruct.items.specializeditems;

import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.inventory.CustomInventory;
import io.ncbpfluffybear.fluffysconstruct.inventory.InventoryTemplate;
import io.ncbpfluffybear.fluffysconstruct.inventory.MachineInventory;
import io.ncbpfluffybear.fluffysconstruct.items.Clocked;
import io.ncbpfluffybear.fluffysconstruct.items.CustomItem;
import io.ncbpfluffybear.fluffysconstruct.items.FCItem;
import io.ncbpfluffybear.fluffysconstruct.items.InventoryBlock;
import io.ncbpfluffybear.fluffysconstruct.items.ItemList;
import io.ncbpfluffybear.fluffysconstruct.items.Placeable;
import io.ncbpfluffybear.fluffysconstruct.utils.InventoryUtils;
import io.ncbpfluffybear.fluffysconstruct.utils.ItemUtils;
import io.ncbpfluffybear.fluffysconstruct.utils.MachineUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class Furnace extends Placeable implements Clocked, InventoryBlock {

    private static final int TOTAL_STAGES = 10;
    private static final int FUEL_CONSUME_RATE = 1;

    private static final int PROGRESS_SLOT = 22;

    public static final int FUEL_SLOT = 31;
    private static final int FUEL_INDICATOR = 19;

    public static final int INPUT_SLOT = 13;
    public static final int OUTPUT_SLOT = 24;

    private static final Map<FCItem, FCItem> recipes = Map.of(
            ItemList.GROUT, ItemList.SEARED_BRICKS
    );


    public Furnace(String key, int id, Material material, String name, String... lore) {
        super(key, id, material, name, lore);
    }

    @Override
    public void onClock(Location location) {
        MachineInventory inventory = ((MachineInventory) FCPlugin.getInventoryRepository().getInventory(location));

        FCItem input = ItemUtils.getFCItem(inventory.getItemInInput());
        if (input == null) { // Not a FCItem
            denyProcess(location, inventory, true);
            return;
        }

        FCItem result = recipes.get(input);
        if (result == null) { // Not a recipe
            denyProcess(location, inventory, true);
            return;
        }

        if (!InventoryUtils.canPushOutput(inventory, inventory.getOutputSlot(), result.getItemStack())) { // No space for item
            denyProcess(location, inventory, true);
            return;
        }

        if (!MachineUtils.consumeFuel(location, FUEL_CONSUME_RATE)) { // Not enough fuel
            int fuelWorth = MachineUtils.getFuelWorth(inventory.getItemInSlot(FUEL_SLOT));
            if (fuelWorth == 0) { // Item in slot is not worth anything
                denyProcess(location, inventory, false);
                return;
            }

            InventoryUtils.consumeItem(inventory, FUEL_SLOT, 1); // Consume fuel item
            MachineUtils.addFuel(location, fuelWorth - 1); // Add fuel to machine, and consume one automatically
        }

        inventory.setItem(FUEL_INDICATOR, getFuelItem(MachineUtils.getFuelStored(location)));
        int stage = MachineUtils.tick(location, TOTAL_STAGES);

        inventory.setProgressPercentage((int) ((double) stage / TOTAL_STAGES * 100));

        if (stage == TOTAL_STAGES) { // Successful operation
            InventoryUtils.consumeItem(inventory, inventory.getInputSlot());
            InventoryUtils.addOrPutItem(inventory, inventory.getOutputSlot(), result.getItemStack());
        }
    }

    /**
     * Called when machine fails to perform operation, either if it has
     * no fuel, invalid input, etc.
     * Resets the GUI and tick count.
     * Optionally consumes fuel
     */
    private void denyProcess(Location location, MachineInventory inv, boolean consumeFuel) {
        if (consumeFuel) {
            MachineUtils.consumeFuel(location, FUEL_CONSUME_RATE);
        }
        MachineUtils.resetTicks(location); // Reset machine progress
        inv.setProgressPercentage(0);
    }

    private ItemStack getFuelItem(int amount) {
        if (amount == 0) {
            return new CustomItem(Material.GLASS_PANE, amount, "&cNo Fuel");
        }
        return new CustomItem(Material.RED_STAINED_GLASS_PANE, amount, "&6Fuel");
    }

    @Override
    public CustomInventory createInventory(Location location) {
        return new MachineInventory(INPUT_SLOT, OUTPUT_SLOT, PROGRESS_SLOT,
                new CustomItem(Material.BLACK_STAINED_GLASS_PANE, "&7Idle"), new CustomItem(Material.FLINT_AND_STEEL, "&cProgress"),
                InventoryTemplate.FURNACE, location
        );
    }

    @Override
    public boolean onOpen(CustomInventory customInventory, Player player) {
        return true;
    }

    @Override
    public void onPlace(Location location) {
        FCPlugin.getMachineProgressTracker().createProgress(location);
    }

    @Override
    public void onBreak(Location location) {
        FCPlugin.getMachineProgressTracker().deleteProgress(location);

        MachineInventory inventory = ((MachineInventory) FCPlugin.getInventoryRepository().getInventory(location));
        // TODO Drop items in slots
    }

}
