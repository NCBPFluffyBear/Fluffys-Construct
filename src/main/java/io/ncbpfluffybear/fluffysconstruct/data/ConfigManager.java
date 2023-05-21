package io.ncbpfluffybear.fluffysconstruct.data;

import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Level;

public class ConfigManager {

    private final YamlConfiguration cfg;

    public ConfigManager(String fileName) {
        File cfgFile = new File(FCPlugin.getInstance().getDataFolder(), fileName);
        if (!cfgFile.exists()) {
            saveResource(fileName, cfgFile);
        }

        this.cfg = YamlConfiguration.loadConfiguration(cfgFile);
    }

    public void saveResource(String resName, File destFile) {
        try(InputStream res = this.getClass().getResourceAsStream( "/" + resName)) {
            if (res == null) {
                Bukkit.getLogger().log(Level.SEVERE, "No local resource found named " + resName);
                return;
            }
            Files.copy(res, destFile.toPath());
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to copy " + resName, e);
        }
    }

    public YamlConfiguration getCfg() {
        return cfg;
    }
}
