package io.ncbpfluffybear.fluffysconstruct.items.specializeditems;

import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.inventory.CustomInventory;
import io.ncbpfluffybear.fluffysconstruct.inventory.InventoryPackage;
import io.ncbpfluffybear.fluffysconstruct.inventory.InventoryTemplate;
import io.ncbpfluffybear.fluffysconstruct.inventory.MachineInventory;
import io.ncbpfluffybear.fluffysconstruct.inventory.slotdata.ProgressSlotData;
import io.ncbpfluffybear.fluffysconstruct.items.Clocked;
import io.ncbpfluffybear.fluffysconstruct.items.CustomItem;
import io.ncbpfluffybear.fluffysconstruct.items.FCItem;
import io.ncbpfluffybear.fluffysconstruct.items.InventoryBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Furnace extends FCItem implements Clocked, InventoryBlock {

    public Furnace(String key, Material material, String name, String... lore) {
        super(key, material, name, lore);
    }

    @Override
    public void onClock(Location location) {
        // Set new values to package
        InventoryPackage pack = getPackage(location);
        ProgressSlotData progressData = ((ProgressSlotData) pack.getSlotData().get(22));
        int percentage = progressData.getProgress();
        int newPercentage = percentage + 10 > 100 ? 0 : percentage + 10;
        progressData.setProgress(newPercentage);


        // Unpack every tick to actual inventory .unpack
    }

    @Override
    public CustomInventory onOpen(Player player, Location location) {
        MachineInventory inventory = new MachineInventory(13, 31, 22,
                new CustomItem(Material.BLACK_STAINED_GLASS_PANE, "&7Idle"), new CustomItem(Material.FLINT_AND_STEEL, "&cProgress"),
                InventoryTemplate.FURNACE
        ).unpack(getPackage(location));

        inventory.open(player);
        return inventory;
    }

    @Override
    public void preparePackage(InventoryPackage pack) {
        pack.setSlotData(22, new ProgressSlotData()); // Set progress to 0
    }
}
