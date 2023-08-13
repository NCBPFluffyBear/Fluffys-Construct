package io.ncbpfluffybear.fluffysconstruct.commands;

import io.ncbpfluffybear.fluffysconstruct.commands.subcommands.GiveCommand;
import io.ncbpfluffybear.fluffysconstruct.commands.subcommands.HelpCommand;
import io.ncbpfluffybear.fluffysconstruct.commands.subcommands.InspectCommand;
import io.ncbpfluffybear.fluffysconstruct.commands.subcommands.SaveCommand;
import io.ncbpfluffybear.fluffysconstruct.commands.subcommands.TestCommand;
import io.ncbpfluffybear.fluffysconstruct.utils.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class BaseCommand implements CommandExecutor {

    private final Map<String, SubCommand> commands;

    public BaseCommand() {
        commands = new HashMap<>();
        addCommands(new HelpCommand(), new GiveCommand(), new TestCommand(), new InspectCommand(), new SaveCommand()); // TODO Repository data dump command
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        SubCommand subCmd;

        if (args.length == 0) {
            subCmd = this.commands.get("help");
        } else {
            subCmd = this.commands.get(args[0].toLowerCase());
        }

        if (subCmd == null) {
            ChatUtils.send(sender, "&cThis command does not exist.");
            return true;
        }

        if (canExecute(sender, subCmd)) { // TODO implement perms similarly
            subCmd.execute(sender, args); // Find the proper command and pass on the args
        } else {
            ChatUtils.send(sender, "&cYou can not execute this command from here.");
        }

        return true;
    }

    private void addCommands(SubCommand... commands) {
        for (SubCommand cmd : commands) {
            this.commands.put(cmd.getCmd(), cmd);
        }
    }

    private boolean canExecute(CommandSender sender, SubCommand subCmd) {
        switch (subCmd.getPermittedExecutor()) {
            case ALL:
                return true;
            case PLAYER:
                return sender instanceof Player;
            case CONSOLE:
                return sender instanceof ConsoleCommandSender;
        }

        return false;
    }
}
