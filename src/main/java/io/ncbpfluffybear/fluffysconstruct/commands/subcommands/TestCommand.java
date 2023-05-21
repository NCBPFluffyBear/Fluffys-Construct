package io.ncbpfluffybear.fluffysconstruct.commands.subcommands;

import com.jeff_media.customblockdata.CustomBlockData;
import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.commands.ExecutorType;
import io.ncbpfluffybear.fluffysconstruct.commands.SubCommand;
import io.ncbpfluffybear.fluffysconstruct.utils.ChatUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class TestCommand implements SubCommand {

    private static final NamespacedKey TEST_KEY = new NamespacedKey(FCPlugin.getInstance(), "test_key");

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);

        int times = 1;
        if (args.length > 1) {
            times = Integer.parseInt(args[1]);
        }

        long time = System.nanoTime();

        for (int i = 0; i < times; i++) {
            Integer data = new CustomBlockData(block, FCPlugin.getInstance()).getOrDefault(TEST_KEY, PersistentDataType.INTEGER, 0);
            new CustomBlockData(block, FCPlugin.getInstance()).set(TEST_KEY, PersistentDataType.INTEGER, ++data);
        }

        long duration = System.nanoTime() - time;

        ChatUtils.broadcast("Took " + duration + " nanos");
    }

    @Override
    public String getCmd() {
        return "test";
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
