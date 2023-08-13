package io.ncbpfluffybear.fluffysconstruct.commands.subcommands;

import io.ncbpfluffybear.fluffysconstruct.FCPlugin;
import io.ncbpfluffybear.fluffysconstruct.api.data.persistent.blockdata.BlockData;
import io.ncbpfluffybear.fluffysconstruct.api.data.persistent.blockdata.BlockDataRepository;
import io.ncbpfluffybear.fluffysconstruct.api.items.FCItem;
import io.ncbpfluffybear.fluffysconstruct.commands.ExecutorType;
import io.ncbpfluffybear.fluffysconstruct.commands.SubCommand;
import io.ncbpfluffybear.fluffysconstruct.data.SmelterySystem;
import io.ncbpfluffybear.fluffysconstruct.setup.Molten;
import io.ncbpfluffybear.fluffysconstruct.utils.ChatUtils;
import io.ncbpfluffybear.fluffysconstruct.utils.ItemUtils;
import io.ncbpfluffybear.fluffysconstruct.utils.Keys;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.RayTraceResult;

import java.util.Map;

public class InspectCommand implements SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = ((Player) sender);
        RayTraceResult trace = player.rayTraceBlocks(3);
        FCItem item = trace != null ? ItemUtils.getFCItem(trace.getHitBlock()) : null;

        if (item == null) {
            ChatUtils.sendMsg(sender, "COMMANDS.INSPECT.INVALID_BLOCK");
            return;
        }

        if (!BlockDataRepository.hasData(trace.getHitBlock().getLocation())) {
            ChatUtils.send(player, "COMMANDS.INSPECT.NO_DATA");
            return;
        }

        if (args.length == 1) {
            BlockData blockData = BlockDataRepository.getDataAt(trace.getHitBlock().getLocation());
            blockData.getAll().forEach((k, v) -> {
                ChatUtils.sendMsg(player, "COMMANDS.INSPECT.CUSTOM_DATA", k, v.getClass().getSimpleName(), String.valueOf(v));
            });
        }

        switch (args[1]) {
            case "smeltery":
                BlockData blockData = BlockDataRepository.getDataAt(trace.getHitBlock().getLocation());
                if (!blockData.has(Keys.SYSTEM_UUID, PersistentDataType.STRING)) {
                    ChatUtils.sendMsg(player, "COMMANDS.INSPECT.NOT_SMELTERY");
                    return;
                }

                String uuid = blockData.get(Keys.SYSTEM_UUID, PersistentDataType.STRING);
                SmelterySystem system = FCPlugin.getSmelteryRepository().getSystem(uuid);
                ((Map<Molten.MoltenMaterial, Integer>) system.serialize().get("contents")).forEach((k, v) -> {
                    ChatUtils.send(player, k + ": " + v);
                });
        }
    }

    @Override
    public String getCmd() {
        return "inspect";
    }

    @Override
    public ExecutorType getPermittedExecutor() {
        return ExecutorType.PLAYER;
    }

    @Override
    public String getHelpMsg() {
        return "/inspect";
    }
}
