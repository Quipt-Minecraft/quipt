package live.qsmc.quipt.paper.commands.executors;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import live.qsmc.quipt.minecraft.utils.chat.MessageUtils;
import live.qsmc.quipt.paper.QuiptPaper;
import live.qsmc.quipt.paper.QuiptPlugin;
import live.qsmc.quipt.paper.commands.PaperCommandExecutor;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.kyori.adventure.text.Component.text;


public class ResourcePackCommand extends PaperCommandExecutor {

    public ResourcePackCommand(QuiptPlugin plugin) {
        super(plugin, "resourcepack");
    }

    // /rp update [url]

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> arguments() {
        return literal(name())
                .executes(context -> showUsage(context, "quipt.admin.resourcepack"))
                .then(literal("update")
                        .executes(context -> {
                            QuiptPaper.instance().packHandler().updatePack();
                            return 1;
                        }))
                .then(literal("reload")
                        .executes(context -> {
                            CommandSender sender = context.getSource().getSender();
                            if (!(sender instanceof Player player))
                                return logError(context, MessageUtils.get("cmd.error.player_only"));
//            try {
//                CoreUtils.packHandler().setPack(player);
                            player.sendMessage(text("Resource pack reloaded.").color(NamedTextColor.GREEN));
//            } catch (IOException | NoSuchAlgorithmException e) {
//                CoreUtils.integration().logger().error("ResourcePackCommand", e);
//                sender.sendMessage(text("An error occurred while reloading the resource pack. Check the console for details.").color(NamedTextColor.RED));
//            }
                            return 1;
                        }));
    }
}
