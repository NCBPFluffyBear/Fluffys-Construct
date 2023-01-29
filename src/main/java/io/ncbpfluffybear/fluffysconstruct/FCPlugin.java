package io.ncbpfluffybear.fluffysconstruct;

import com.jeff_media.customblockdata.CustomBlockData;
import io.ncbpfluffybear.fluffysconstruct.blocks.BlockRepository;
import io.ncbpfluffybear.fluffysconstruct.commands.BaseCommand;
import io.ncbpfluffybear.fluffysconstruct.handlers.FCBlockHandler;
import io.ncbpfluffybear.fluffysconstruct.handlers.FCInventoryHandler;
import io.ncbpfluffybear.fluffysconstruct.items.ItemRepository;
import io.ncbpfluffybear.fluffysconstruct.recipes.RecipeRepository;
import io.ncbpfluffybear.fluffysconstruct.setup.ItemSetup;
import io.ncbpfluffybear.fluffysconstruct.setup.RecipeSetup;
import io.ncbpfluffybear.fluffysconstruct.tasks.BlockClock;
import org.bukkit.plugin.java.JavaPlugin;

public class FCPlugin extends JavaPlugin {

    private static FCPlugin instance;
    private static ItemRepository itemRepository;
    private static RecipeRepository recipeRepository;
    private static BlockRepository blockRepository;

    public FCPlugin() {
    }

    @Override
    public void onEnable() {
        instance = this;

        itemRepository = new ItemRepository();
        recipeRepository = new RecipeRepository();
        blockRepository = new BlockRepository();
        new ItemSetup(itemRepository).register();
        new RecipeSetup(recipeRepository).register();

        getCommand("fluffysconstruct").setExecutor(new BaseCommand());

        getServer().getPluginManager().registerEvents(new FCBlockHandler(), this);
        getServer().getPluginManager().registerEvents(new FCInventoryHandler(), this);
        getServer().getScheduler().runTaskTimer(this, new BlockClock(), 20L, 20L);

        CustomBlockData.registerListener(this);

    }

    @Override
    public void onDisable() {
        super.onDisable();
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
}
