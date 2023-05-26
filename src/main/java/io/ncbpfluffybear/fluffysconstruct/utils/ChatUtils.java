package io.ncbpfluffybear.fluffysconstruct.utils;

import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class ChatUtils {

    public ChatUtils() {
        throw new InstantiationError();
    }

    public static void send(CommandSender sender, String msg) {
        sender.sendMessage(StringUtils.color("&7[FluffysConstruct] " + msg));
    }

    /**
     * Sends a config message, targeted by the path location
     */
    public static void sendMsg(CommandSender sender, String path, Object... args) {
        sender.sendMessage(StringUtils.color("&7[FluffysConstruct] " + FCPlugin.getMessages().fromPath(path, args)));
    }

    public static void broadcast(String... msgs) {
        for (String msg : msgs) {
            Bukkit.broadcastMessage(msg);
        }
    }

    public static void logError(String error) {
        FCPlugin.getInstance().getLogger().severe(error);
    }

}
