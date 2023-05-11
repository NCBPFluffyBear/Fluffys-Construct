package io.ncbpfluffybear.fluffysconstruct;

import com.jeff_media.customblockdata.CustomBlockData;
import io.ncbpfluffybear.fluffysconstruct.blocks.BlockRepository;
import io.ncbpfluffybear.fluffysconstruct.commands.BaseCommand;
import io.ncbpfluffybear.fluffysconstruct.handlers.FCBlockHandler;
import io.ncbpfluffybear.fluffysconstruct.handlers.FCInventoryHandler;
import io.ncbpfluffybear.fluffysconstruct.inventory.InventoryRepository;
import io.ncbpfluffybear.fluffysconstruct.items.ItemRepository;
import io.ncbpfluffybear.fluffysconstruct.recipes.RecipeRepository;
import io.ncbpfluffybear.fluffysconstruct.setup.ItemSetup;
import io.ncbpfluffybear.fluffysconstruct.setup.RecipeSetup;
import io.ncbpfluffybear.fluffysconstruct.tasks.BlockClock;
import io.ncbpfluffybear.fluffysconstruct.trackers.FuelStorageTracker;
import io.ncbpfluffybear.fluffysconstruct.trackers.MachineProgress;
import io.ncbpfluffybear.fluffysconstruct.trackers.MachineProgressTracker;
import io.ncbpfluffybear.fluffysconstruct.utils.Constants;
import io.ncbpfluffybear.fluffysconstruct.utils.SaveUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class FCPlugin extends JavaPlugin {

    private static FCPlugin instance;
    private static ItemRepository itemRepository;
    private static RecipeRepository recipeRepository;
    private static BlockRepository blockRepository;
    private static InventoryRepository inventoryRepository;
    private static MachineProgressTracker machineProgressTracker;
    private static FuelStorageTracker fuelStorageTracker;
    private static File blocksFile;

    public FCPlugin() {
    }

    @Override
    public void onEnable() {
        instance = this;

        itemRepository = new ItemRepository();
        recipeRepository = new RecipeRepository();
        blockRepository = new BlockRepository();
        inventoryRepository = new InventoryRepository();
        machineProgressTracker = new MachineProgressTracker();
        fuelStorageTracker = new FuelStorageTracker();

        new ItemSetup(itemRepository).register();
        new RecipeSetup(recipeRepository).register();

        getCommand("fluffysconstruct").setExecutor(new BaseCommand());

        getServer().getPluginManager().registerEvents(new FCBlockHandler(), this);
        getServer().getPluginManager().registerEvents(new FCInventoryHandler(inventoryRepository), this);
        getServer().getScheduler().runTaskTimer(this, new BlockClock(), 20L, 20L);

        CustomBlockData.registerListener(this);

        blocksFile = new File(this.getDataFolder() + Constants.BLOCKS_FILE);

        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdir();
        }

        try {
            blocksFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FileConfiguration blocksConfig = YamlConfiguration.loadConfiguration(blocksFile);
        SaveUtils.loadBlocks(blocksConfig, blockRepository);

    }

    @Override
    public void onDisable() {
        SaveUtils.saveBlocks(blocksFile, blockRepository);
    }

    public static FCPlugin getInstance() {
        return instance;
    }

    public static ItemRepository getItemRepository() {
        return itemRepository;
    }

    public static RecipeRepository getRecipeRegistry() {
        return recipeRepository;
    }

    public static BlockRepository getBlockRepository() {
        return blockRepository;
    }

    public static InventoryRepository getInventoryRepository() {
        return inventoryRepository;
    }

    public static MachineProgressTracker getMachineProgressTracker() {
        return machineProgressTracker;
    }

    public static FuelStorageTracker getFuelStorageTracker() {
        return fuelStorageTracker;
    }
}
