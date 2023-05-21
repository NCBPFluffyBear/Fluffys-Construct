package io.ncbpfluffybear.fluffysconstruct;

import com.jeff_media.customblockdata.CustomBlockData;
import io.ncbpfluffybear.fluffysconstruct.blocks.BlockRepository;
import io.ncbpfluffybear.fluffysconstruct.commands.BaseCommand;
import io.ncbpfluffybear.fluffysconstruct.data.ConfigManager;
import io.ncbpfluffybear.fluffysconstruct.data.Database;
import io.ncbpfluffybear.fluffysconstruct.data.Messages;
import io.ncbpfluffybear.fluffysconstruct.handlers.FCBlockHandler;
import io.ncbpfluffybear.fluffysconstruct.handlers.FCEntityHandler;
import io.ncbpfluffybear.fluffysconstruct.handlers.FCInventoryHandler;
import io.ncbpfluffybear.fluffysconstruct.inventory.InventoryRepository;
import io.ncbpfluffybear.fluffysconstruct.items.ItemRepository;
import io.ncbpfluffybear.fluffysconstruct.recipes.RecipeRepository;
import io.ncbpfluffybear.fluffysconstruct.setup.ItemSetup;
import io.ncbpfluffybear.fluffysconstruct.setup.RecipeSetup;
import io.ncbpfluffybear.fluffysconstruct.tasks.BlockClock;
import io.ncbpfluffybear.fluffysconstruct.trackers.FuelStorageTracker;
import io.ncbpfluffybear.fluffysconstruct.trackers.MachineProgressTracker;
import io.ncbpfluffybear.fluffysconstruct.utils.Constants;
import io.ncbpfluffybear.fluffysconstruct.utils.DatabaseUtils;
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
    private static Messages messages;

    private static DatabaseUtils dbUtil;
    private static ConfigManager cfg;

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
        getServer().getPluginManager().registerEvents(new FCEntityHandler(), this);
        getServer().getScheduler().runTaskTimer(this, new BlockClock(), 20L, 20L);

        CustomBlockData.registerListener(this);

        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdir();
        }

        dbUtil = new DatabaseUtils(new Database());
        cfg = new ConfigManager("config.yml");
        messages = new Messages("messages.yml");

        dbUtil.loadBlocks();
        dbUtil.loadInvPackages();
    }

    @Override
    public void onDisable() {
        dbUtil.saveBlocks();
        dbUtil.saveInventories();
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

    public static DatabaseUtils getDbUtil() {
        return dbUtil;
    }

    public static Messages getMessages() {
        return messages;
    }
}
