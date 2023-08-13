package io.ncbpfluffybear.fluffysconstruct.api.data;

import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.utils.FileUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigManager {

    private final YamlConfiguration cfg;

    public ConfigManager(String fileName) {
        File cfgFile = new File(FCPlugin.getInstance().getDataFolder(), fileName);
        if (!cfgFile.exists()) {
            FileUtils.copyJarResource(fileName, cfgFile);
        }

        this.cfg = YamlConfiguration.loadConfiguration(cfgFile);
    }

    public YamlConfiguration getCfg() {
        return cfg;
    }
}
