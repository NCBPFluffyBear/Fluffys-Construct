package io.ncbpfluffybear.fluffysconstruct.utils;

import org.bukkit.ChatColor;

public class StringUtils {

    public StringUtils() {
        throw new InstantiationError();
    }

    /**
     * Colors strings by Minecraft color code
     * @param input text to be colored
     */
    public static String color(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

}
