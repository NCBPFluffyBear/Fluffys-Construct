package io.ncbpfluffybear.fluffysconstruct.items;

import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.inventory.CustomInventory;
import io.ncbpfluffybear.fluffysconstruct.inventory.InventoryPackage;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface InventoryBlock {

    default void createPackage(Location location) {
        InventoryPackage pack = new InventoryPackage();
        preparePackage(pack);
        FCPlugin.getPackageRepository().putPackage(location, pack);
    }

    default InventoryPackage getPackage(Location location) {
        return FCPlugin.getPackageRepository().getPackage(location);
    }

    CustomInventory onOpen(Player player, Location location);

    void preparePackage(InventoryPackage pack);

}
