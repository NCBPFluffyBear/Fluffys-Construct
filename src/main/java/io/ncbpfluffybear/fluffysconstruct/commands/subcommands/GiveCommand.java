package io.ncbpfluffybear.fluffysconstruct.commands.subcommands;

import io.ncbpfluffybear.fluffysconstruct.commands.ExecutorType;
import io.ncbpfluffybear.fluffysconstruct.commands.SubCommand;
import io.ncbpfluffybear.fluffysconstruct.items.FCItem;
import io.ncbpfluffybear.fluffysconstruct.utils.ChatUtils;
import io.ncbpfluffybear.fluffysconstruct.utils.ItemUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveCommand implements SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        String itemKey = args[1].toUpperCase(); // TODO help msg
        FCItem item = ItemUtils.getFCItem(itemKey);

        if (item == null) {
            ChatUtils.sendMsg(sender, "COMMANDS.GIVE.NO_ITEM", itemKey );
            return;
        }

        ItemUtils.giveItem((Player) sender, item);
    }

    @Override
    public String getCmd() {
        return "give";
    }

    @Override
    public ExecutorType getPermittedExecutor() {
        return ExecutorType.PLAYER;
    }

    @Override
    public String getHelpMsg() {
        return "/give <item>";
    }
}
