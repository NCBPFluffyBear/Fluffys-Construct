package io.ncbpfluffybear.fluffysconstruct.utils;

import org.bukkit.command.CommandSender;

public class ChatUtils {

    public ChatUtils() {
        throw new InstantiationError();
    }

    public static void send(CommandSender sender, String msg) {
        sender.sendMessage(StringUtils.color("&7[FluffysConstruct] " + msg));
    }

}
