package io.ncbpfluffybear.fluffysconstruct;

import io.ncbpfluffybear.fluffysconstruct.api.data.persistent.PersistenceUtils;
import io.ncbpfluffybear.fluffysconstruct.api.data.persistent.blockdata.BlockData;
import io.ncbpfluffybear.fluffysconstruct.api.data.persistent.blockdata.BlockDataRepository;
import io.ncbpfluffybear.fluffysconstruct.api.web.WebServer;
import io.ncbpfluffybear.fluffysconstruct.blocks.BlockRepository;
import io.ncbpfluffybear.fluffysconstruct.commands.BaseCommand;
import io.ncbpfluffybear.fluffysconstruct.api.data.ConfigManager;
import io.ncbpfluffybear.fluffysconstruct.api.data.Messages;
import io.ncbpfluffybear.fluffysconstruct.handlers.FCBlockHandler;
import io.ncbpfluffybear.fluffysconstruct.handlers.FCEntityHandler;
import io.ncbpfluffybear.fluffysconstruct.handlers.FCInventoryHandler;
import io.ncbpfluffybear.fluffysconstruct.api.inventory.InventoryRepository;
import io.ncbpfluffybear.fluffysconstruct.items.ItemRepository;
import io.ncbpfluffybear.fluffysconstruct.recipes.RecipeRepository;
import io.ncbpfluffybear.fluffysconstruct.repository.SmelteryRepository;
import io.ncbpfluffybear.fluffysconstruct.setup.ItemSetup;
import io.ncbpfluffybear.fluffysconstruct.setup.Molten;
import io.ncbpfluffybear.fluffysconstruct.setup.RecipeSetup;
import io.ncbpfluffybear.fluffysconstruct.tasks.BlockClock;
import io.ncbpfluffybear.fluffysconstruct.trackers.FuelStorageTracker;
import io.ncbpfluffybear.fluffysconstruct.trackers.MachineProgressTracker;
import io.ncbpfluffybear.fluffysconstruct.utils.ChatUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

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

    private static PersistenceUtils pu;
    private static ConfigManager cfg;
    private static Molten molten;
    private static SmelteryRepository sr;

    public FCPlugin() {
        super();
    }

    /**
     * Used for unit testing
     */
    public FCPlugin(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
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

        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdir();
        }


        sr = new SmelteryRepository();
        pu = new PersistenceUtils();
        cfg = new ConfigManager("config.yml");
        messages = new Messages("messages.yml");
        molten = new Molten("molten.yml");

        pu.load();

        try {
            WebServer webServer = new WebServer();
            webServer.start(80); // Starts webserver on new thread
            getLogger().info("Started server: " + webServer.getServer().getAddress().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDisable() {
        try {
            pu.save();
        } catch (IOException | InvalidConfigurationException e) {
            ChatUtils.logError("Error saving data to file.");
            throw new RuntimeException(e);
        }
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

    public static SmelteryRepository getSmelteryRepository() {
        return sr;
    }

    public static BlockRepository getBlockRepository() {
        return blockRepository;
    }

    public static PersistenceUtils getPersistenceUtils() {
        return pu;
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

    public static Messages getMessages() {
        return messages;
    }

    public static Molten getMolten() {
        return molten;
    }
}
