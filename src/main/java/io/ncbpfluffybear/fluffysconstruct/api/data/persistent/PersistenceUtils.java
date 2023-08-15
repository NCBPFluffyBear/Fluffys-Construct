package io.ncbpfluffybear.fluffysconstruct.api.data.persistent;

import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.api.data.persistent.blockdata.BlockDataRepository;
import io.ncbpfluffybear.fluffysconstruct.api.data.serialize.Serialize;
import io.ncbpfluffybear.fluffysconstruct.api.inventory.CustomInventory;
import io.ncbpfluffybear.fluffysconstruct.api.items.FCItem;
import io.ncbpfluffybear.fluffysconstruct.data.SmelterySystem;
import io.ncbpfluffybear.fluffysconstruct.items.Clocked;
import io.ncbpfluffybear.fluffysconstruct.items.InventoryBlock;
import io.ncbpfluffybear.fluffysconstruct.utils.ChatUtils;
import io.ncbpfluffybear.fluffysconstruct.utils.ItemUtils;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PersistenceUtils {

    private final File dataFolder;
    private final File blockFolder;
    private final File smelteryFolder;
    private final Map<Location, Set<DirtyType>> dirtyBlocks;
    private final Set<UUID> dirtySmelteries;

    /**
     * Data is stored in the following format:
     * plugin_folder/data ->
     * world-uuid_X_Y_Z ->
     * id: FCItem ID
     * (optional) clock: serialized-clock-data
     * (optional) inventory: serialized-inv
     */
    public PersistenceUtils() {
        File pluginFolder = FCPlugin.getInstance().getDataFolder();
        this.dataFolder = new File(pluginFolder, "data");
        this.blockFolder = new File(dataFolder, "blocks");
        this.smelteryFolder = new File(dataFolder, "smelteries");

        if (smelteryFolder.mkdirs()) {
            ChatUtils.info("Created smeltery data folder at " + smelteryFolder.getPath());
        }

        if (blockFolder.mkdirs()) {
            ChatUtils.info("Created smeltery data folder at " + blockFolder.getPath());
        }

        this.dirtyBlocks = new HashMap<>();
        this.dirtySmelteries = new HashSet<>();
    }

    /**
     * Loads blocks into repositories
     */
    public void load() {
        File[] allItems = this.blockFolder.listFiles();
        if (allItems == null) {
            ChatUtils.logError("There was a problem reading data files!");
            return;
        }

        for (File itemFile : allItems) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(itemFile);
            Location location = Serialize.parseLocation(itemFile.getName());

            FCItem fcItem = ItemUtils.getFCItem(config.getInt("id"));
            FCPlugin.getBlockRepository().addFCItemAt(location, fcItem);

            if (fcItem instanceof Clocked) {
                FCPlugin.getBlockRepository().addClockedBlock(fcItem, location);
            }

            if (fcItem instanceof InventoryBlock) {
                FCPlugin.getBlockRepository().addInventoryBlock(fcItem, location);
                FCPlugin.getInventoryRepository().putInventory(location, ((InventoryBlock) fcItem).createInventory(location)); // Apply inventory template
                CustomInventory customInventory = FCPlugin.getInventoryRepository().getInventory(location);

                // Load and insert packages
                ConfigurationSection inventorySection = config.getConfigurationSection("inventory");
                if (inventorySection == null) continue; // No inventory pkg saved
                for (String key : inventorySection.getKeys(false)) {
                    ItemStack savedItem = inventorySection.getItemStack(key);
                    customInventory.setItem(Integer.parseInt(key), savedItem);
                }
            }

            if (config.contains("blockdata")) {
                BlockDataRepository.load(location, config.getConfigurationSection("blockdata"));
            }
        }

        File[] allSmelteries = this.smelteryFolder.listFiles();
        if (allSmelteries == null) {
            ChatUtils.logError("There was a problem reading data files!");
            return;
        }
        for (File smelteryFile : allSmelteries) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(smelteryFile);
            SmelterySystem system = new SmelterySystem(config.getConfigurationSection("smeltery").getValues(false));
            FCPlugin.getSmelteryRepository().addExisting(system.getUuid(), system);
        }

    }

    public void save() throws IOException, InvalidConfigurationException {
        // Wipe dirty files
        for (Map.Entry<Location, Set<DirtyType>> dirtyData : dirtyBlocks.entrySet()) {
            if (dirtyData.getValue().contains(DirtyType.WORLD)) { // File needs to be completely wiped
                File itemFile = new File(blockFolder, Serialize.serializeLocation(dirtyData.getKey()));
                try {
                    Files.deleteIfExists(itemFile.toPath());
                } catch (IOException e) {
                    ChatUtils.logError("Failed to delete file " + itemFile.getPath());
                }
            }
        }

        // Write new data
        for (Map.Entry<Location, Set<DirtyType>> dirtyData : dirtyBlocks.entrySet()) {

            FCItem fcItem = FCPlugin.getBlockRepository().getFCItemAt(dirtyData.getKey());
            File itemFile = new File(blockFolder, Serialize.serializeLocation(dirtyData.getKey()));
            YamlConfiguration config = new YamlConfiguration();

            if (itemFile.exists()) {
                config.load(itemFile);
            }

            if (dirtyData.getValue().contains(DirtyType.WORLD)) { // Append data for locations with replacement blocks
                if (fcItem == null) continue; // There is no replacement block
                config.set("id", fcItem.getId());
            }

            if (dirtyData.getValue().contains(DirtyType.INVENTORY) && fcItem instanceof InventoryBlock) {
                ConfigurationSection inventoryConfig = config.createSection("inventory");
                for (Map.Entry<Integer, ItemStack> toSerialize : FCPlugin.getInventoryRepository().getInventory(dirtyData.getKey()).getToSerialize().entrySet()) {
                    inventoryConfig.set(String.valueOf(toSerialize.getKey()), toSerialize.getValue().serialize());
                }
            }

            if (dirtyData.getValue().contains(DirtyType.BLOCKDATA)) {
                BlockDataRepository.save(dirtyData.getKey(), config);
            }

            itemFile.createNewFile();
            config.save(itemFile);
        }

        this.dirtyBlocks.clear();


        // Save SmelterySystems
        for (UUID smelteryId : dirtySmelteries) {
            SmelterySystem system = FCPlugin.getSmelteryRepository().getSystem(smelteryId);
            File smelteryFile = new File(smelteryFolder, smelteryId.toString());
            if (system == null) { // No new system has been placed
                Files.delete(smelteryFile.toPath());
                continue;
            }
            YamlConfiguration configuration = new YamlConfiguration();
            configuration.createSection("smeltery", system.serialize());
            configuration.save(smelteryFile);
        }
    }

    public void markBlockDirty(Location location, DirtyType dirtyType) {
        if (!dirtyBlocks.containsKey(location)) {
            dirtyBlocks.put(location, new HashSet<>());
        }

        dirtyBlocks.get(location).add(dirtyType);
        // ChatUtils.broadcast("Dirty @" + location.toString() + "#" + dirtyType);
    }

    public void markSmelteryDirty(UUID uuid) {
        this.dirtySmelteries.add(uuid);
    }
}
