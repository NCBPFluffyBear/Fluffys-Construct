package io.ncbpfluffybear.fluffysconstruct.commands.subcommands;

import io.ncbpfluffybear.fluffysconstruct.commands.ExecutorType;
import io.ncbpfluffybear.fluffysconstruct.commands.SubCommand;
import io.ncbpfluffybear.fluffysconstruct.utils.ChatUtils;
import org.bukkit.command.CommandSender;

public class HelpCommand implements SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        ChatUtils.send(sender, "&6i dont wanna help u lol");
    }

    @Override
    public String getCmd() {
        return "help";
    }

    @Override
    public ExecutorType getPermittedExecutor() {
        return ExecutorType.ALL;
    }

    @Override
    public String getHelpMsg() {
        return null;
    }
}
