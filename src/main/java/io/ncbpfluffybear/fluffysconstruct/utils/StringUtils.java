package io.ncbpfluffybear.fluffysconstruct.utils;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class StringUtils {

    private static final NumberFormat decimalFormat = new DecimalFormat("#0.00");

    public StringUtils() {
        throw new InstantiationError();
    }

    /**
     * Colors strings by Minecraft color code
     *
     * @param input text to be colored
     */
    public static String color(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static String formatDecimal(double number) {
        return decimalFormat.format(number);
    }

    public static String format(String format, Object... args) {
        Object[] newArgs = new Object[args.length];

        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg instanceof Location) { // Replace location with custom toString
                Location loc = (Location) arg;
                newArgs[i] = loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ();
                continue;
            }

            newArgs[i] = arg;
        }

        return String.format(format, newArgs);
    }
}
