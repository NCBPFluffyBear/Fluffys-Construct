package io.ncbpfluffybear.fluffysconstruct.data;

import io.ncbpfluffybear.fluffysconstruct.utils.ChatUtils;
import io.ncbpfluffybear.fluffysconstruct.utils.StringUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.io.InputStreamReader;

public class Messages {

    private final YamlConfiguration config;

    public Messages(String msgFile) {
        try {
            config = new YamlConfiguration();
            config.load(new InputStreamReader(this.getClass().getResourceAsStream("/" + msgFile)));
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public String getMessage(String path) {
        if (!config.contains(path)) {
            ChatUtils.logError("Missing message at path " + path);
            return "ERR";
        }

        return config.getString(path);
    }

    public String getMessage(String path, Object... args) {
        return StringUtils.format(getMessage(path), args);
    }
}
