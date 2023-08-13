package io.ncbpfluffybear.fluffysconstruct.setup;

import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.utils.ChatUtils;
import io.ncbpfluffybear.fluffysconstruct.utils.FileUtils;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Molten {

    private final Map<Material, List<Product>> dictionary;

    public Molten(String fileName) {
        this.dictionary = new HashMap<>();

        try {
            File file = new File(FCPlugin.getInstance().getDataFolder(), fileName);
            if (!file.exists()) {
                FileUtils.copyJarResource(fileName, file);
            }

            YamlConfiguration config = new YamlConfiguration();
            config.load(file);
            for (String key : config.getKeys(false)) {
                List<Product> products = new ArrayList<>();
                String[] outputs = config.getString(key).split(","); // Multiple molten outputs
                for (String out : outputs) {
                    String[] data = out.split(":"); // Separate category and amount
                    products.add(new Product(MoltenMaterial.valueOf(data[0]), Integer.parseInt(data[1])));
                    dictionary.put(Material.getMaterial(key), products); // Melt to multiple ores
                }
            } // TODO: Validate inputs
        } catch (IOException e) {
            ChatUtils.logError("Failed to save " + fileName + " from resources");
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            ChatUtils.logError("Failed to load " + fileName + " from disk");
            e.printStackTrace();
        }
    }

    public List<Product> getProducts(Material material) {
        return this.dictionary.get(material);
    }

    public Map<Material, List<Product>> getDictionary() {
        return this.dictionary;
    }

    public Set<Material> getMeltable() {
        return this.dictionary.keySet();
    }

    public record Product(MoltenMaterial material, int volume) {
    }

    public enum MoltenMaterial {
        IRON,
        COPPER,
        GOLD
    }

}
