package io.ncbpfluffybear.fluffysconstruct.commands;

import org.bukkit.command.CommandSender;

public interface SubCommand {

    void execute(CommandSender sender, String[] args);
    String getCmd();
    ExecutorType getPermittedExecutor();
    String getHelpMsg();

}
