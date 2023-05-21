package io.ncbpfluffybear.fluffysconstruct.utils;

import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.blocks.BlockRepository;
import io.ncbpfluffybear.fluffysconstruct.data.Database;
import io.ncbpfluffybear.fluffysconstruct.data.serialize.Serialize;
import io.ncbpfluffybear.fluffysconstruct.inventory.CustomInventory;
import io.ncbpfluffybear.fluffysconstruct.inventory.InventoryRepository;
import io.ncbpfluffybear.fluffysconstruct.items.Clocked;
import io.ncbpfluffybear.fluffysconstruct.items.FCItem;
import io.ncbpfluffybear.fluffysconstruct.items.InventoryBlock;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Set;

public class DatabaseUtils {

    private final Database db;

    public DatabaseUtils(Database db) {
        this.db = db;
    }

    /**
     * Load blocks to generate inventories before
     * loading inventory packages
     */
    public void loadBlocks() {
        try {
            Statement stmt = db.getConnection().createStatement();
            ResultSet result = stmt.executeQuery("SELECT * FROM blocks");

            while (result.next()) {
                Location location = Serialize.parseLocation(result.getString(1));
                int id = result.getInt(2);

                FCItem item = ItemUtils.getFCItem(id);

                if (item == null) {
                    FCPlugin.getInstance().getLogger().warning("Unknown item ID \"" + id + "\" when loading in blocks");
                    continue;
                }

                if (item instanceof Clocked) {
                    FCPlugin.getBlockRepository().addClockedBlock(item, location);
                }

                if (item instanceof InventoryBlock) {
                    FCPlugin.getBlockRepository().addInventoryBlock(item, location);
                    FCPlugin.getInventoryRepository().putInventory(location, ((InventoryBlock) item).createInventory(location)); // Apply inventory template
                }
            }

            stmt.close();
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Place inventory packages in loaded inventories
     */
    public void loadInvPackages() { // TODO: Log updates, only update changed
        try {
            Statement stmt = db.getConnection().createStatement();
            ResultSet result = stmt.executeQuery("SELECT * FROM inventories");

            while (result.next()) {
                Location location = Serialize.parseLocation(result.getString(1));
                String serializedItems = result.getString(2);

                Map<Integer, ItemStack> items = Serialize.deserializeItems(serializedItems);
                CustomInventory customInventory = FCPlugin.getInventoryRepository().getInventory(location);

                for (Map.Entry<Integer, ItemStack> pkg : items.entrySet()) {
                    customInventory.setItem(pkg.getKey(), pkg.getValue());
                }
            }

            stmt.close();
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveBlocks() {
        try {
            Statement stmt = db.getConnection().createStatement();

            // Log all clocked
            for (Map.Entry<FCItem, Set<Location>> clockedEntry : FCPlugin.getBlockRepository().getAllClocked().entrySet()) {
                for (Location location : clockedEntry.getValue()) {
                    stmt.addBatch("REPLACE INTO blocks(location, id) VALUES(\"" +
                            Serialize.serializeLocation(location) + "\", \"" +
                            clockedEntry.getKey().getId() + "\")"
                    );
                }
            }

            // Log all inventoried
            for (Map.Entry<FCItem, Set<Location>> inventoryEntry : FCPlugin.getBlockRepository().getAllInventoryLocations().entrySet()) {
                for (Location location : inventoryEntry.getValue()) {
                    stmt.addBatch("REPLACE INTO blocks(location, id) VALUES(\"" +
                            Serialize.serializeLocation(location) + "\", \"" +
                            inventoryEntry.getKey().getId() + "\")"
                    );
                }
            }

            stmt.executeBatch();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveInventories() {
        try {
            Statement stmt = db.getConnection().createStatement();

            for (CustomInventory inventory : FCPlugin.getInventoryRepository().getInventories().values()) {
                String serInv = inventory.serialize();
                String serLoc = Serialize.serializeLocation(inventory.getLocation());

                if (serInv == null) {
                    continue;
                }

                if (FCPlugin.getInventoryRepository().isRemoval(inventory.getLocation())) {
                    stmt.addBatch("DELETE FROM inventories WHERE location=\"" + serLoc + "\"");
                } else {
                    stmt.addBatch("REPLACE INTO inventories(location, data) VALUES(\"" + serLoc + "\", \"" +
                            serInv + "\")"
                    );
                }
            }

            stmt.executeBatch();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
