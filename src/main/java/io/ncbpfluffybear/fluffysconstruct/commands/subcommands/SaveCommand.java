package io.ncbpfluffybear.fluffysconstruct.commands.subcommands;

import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.commands.ExecutorType;
import io.ncbpfluffybear.fluffysconstruct.commands.SubCommand;
import io.ncbpfluffybear.fluffysconstruct.utils.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;

public class SaveCommand implements SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        ChatUtils.send(sender, "Saving");
        try {
            FCPlugin.getPersistenceUtils().save();
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getCmd() {
        return "save";
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
