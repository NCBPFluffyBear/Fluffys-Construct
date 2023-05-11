package io.ncbpfluffybear.fluffysconstruct.items.specializeditems;

import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.inventory.CustomInventory;
import io.ncbpfluffybear.fluffysconstruct.inventory.InventoryTemplate;
import io.ncbpfluffybear.fluffysconstruct.inventory.MachineInventory;
import io.ncbpfluffybear.fluffysconstruct.items.Clocked;
import io.ncbpfluffybear.fluffysconstruct.items.CustomItem;
import io.ncbpfluffybear.fluffysconstruct.items.InventoryBlock;
import io.ncbpfluffybear.fluffysconstruct.items.Placeable;
import io.ncbpfluffybear.fluffysconstruct.utils.InventoryUtils;
import io.ncbpfluffybear.fluffysconstruct.utils.MachineUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Furnace extends Placeable implements Clocked, InventoryBlock {

    private static final int PROGRESS_SLOT = 22;
    private static final int TOTAL_STAGES = 9;
    private static final int FUEL_CONSUME_RATE = 1;

    private static final int FUEL_SLOT = 31;
    private static final int FUEL_INDICATOR = 19;


    public Furnace(String key, Material material, String name, String... lore) {
        super(key, material, name, lore);
    }

    @Override
    public void onClock(Location location) {
        MachineInventory inventory = ((MachineInventory) FCPlugin.getInventoryRepository().getInventory(location));

        if (!MachineUtils.consumeFuel(location, FUEL_CONSUME_RATE)) { // Not enough fuel
            int fuelWorth = MachineUtils.getFuelWorth(inventory.getItemInSlot(FUEL_SLOT));
            if (fuelWorth == 0) { // Item in slot is not worth anything
                MachineUtils.resetTicks(location); // Reset machine progress
                inventory.setProgressPercentage(0);
                return;
            }

            InventoryUtils.consumeItem(inventory, FUEL_SLOT, 1); // Consume fuel item
            MachineUtils.addFuel(location, fuelWorth); // Add fuel to machine
        }

        inventory.setItem(FUEL_INDICATOR, getFuelItem(MachineUtils.getFuelStored(location)));
        int stage = MachineUtils.tick(location, TOTAL_STAGES);

        inventory.setProgressPercentage((int) ((double) stage / TOTAL_STAGES * 100));

        if (stage == TOTAL_STAGES) {
            Bukkit.broadcastMessage(String.valueOf("Ba bum!"));
        }

    }

    private ItemStack getFuelItem(int amount) {
        if (amount == 0) {
            return new CustomItem(Material.GLASS_PANE, amount, "&cNo Fuel");
        }
        return new CustomItem(Material.RED_STAINED_GLASS_PANE, amount, "&6Fuel");
    }

    @Override
    public CustomInventory createInventory() {
        return new MachineInventory(13, 24, PROGRESS_SLOT,
                new CustomItem(Material.BLACK_STAINED_GLASS_PANE, "&7Idle"), new CustomItem(Material.FLINT_AND_STEEL, "&cProgress"),
                InventoryTemplate.FURNACE
        );
    }

    @Override
    public void onPlace(Location location) {
        FCPlugin.getMachineProgressTracker().createProgress(location);
    }

    @Override
    public void onBreak(Location location) {
        FCPlugin.getMachineProgressTracker().deleteProgress(location);
    }

}
