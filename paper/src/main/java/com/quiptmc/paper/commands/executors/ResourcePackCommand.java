package com.quiptmc.paper.commands.executors;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.quiptmc.minecraft.CoreUtils;
import com.quiptmc.paper.commands.CommandExecutor;
import com.quiptmc.minecraft.utils.chat.MessageUtils;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static io.papermc.paper.command.brigadier.Commands.literal;
import static net.kyori.adventure.text.Component.text;


public class ResourcePackCommand extends CommandExecutor {

    public ResourcePackCommand(JavaPlugin plugin) {
        super(plugin, "resourcepack");
    }

    // /rp update [url]

    @Override
    public LiteralCommandNode<CommandSourceStack> execute() {
        return literal(name()).executes(context -> showUsage(context, "quipt.admin.resourcepack")).then(literal("update").executes(context -> {
            CoreUtils.packHandler().updatePack();
            return 1;
        })).then(literal("reload").executes(context -> {
            CommandSender sender = context.getSource().getSender();

            if (!(sender instanceof Player player))
                return logError(sender, MessageUtils.get("cmd.error.player_only"));
//            try {
//                CoreUtils.packHandler().setPack(player);
                player.sendMessage(text("Resource pack reloaded.").color(NamedTextColor.GREEN));
//            } catch (IOException | NoSuchAlgorithmException e) {
//                CoreUtils.integration().logger().error("ResourcePackCommand", e);
//                sender.sendMessage(text("An error occurred while reloading the resource pack. Check the console for details.").color(NamedTextColor.RED));
//            }
            return 1;
        })).build();
    }
}
