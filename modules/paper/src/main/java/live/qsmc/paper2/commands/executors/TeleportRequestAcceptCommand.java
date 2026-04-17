/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package live.qsmc.paper2.commands.executors;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import live.qsmc.minecraft.utils.teleportation.LocationUtils;
import live.qsmc.paper.data.PaperPlayer;
import live.qsmc.minecraft2.utils.chat.MessageUtils;
import live.qsmc.paper2.QuiptPlugin;
import live.qsmc.paper2.commands.CommandExecutor;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;

import static io.papermc.paper.command.brigadier.Commands.literal;

public class TeleportRequestAcceptCommand extends CommandExecutor {

    public TeleportRequestAcceptCommand(QuiptPlugin integration) {
        super(integration, "teleportrequestaccept");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> arguments() {
        return literal(name()).executes(context -> {
            if(!(context.getSource().getSender() instanceof Player player)) return logError(context, MessageUtils.get("cmd.error.player_only"));
            PaperPlayer paperPlayer = PaperPlayer.of(player);
            for(LocationUtils.TeleportRequest request : LocationUtils.requests()){
                System.out.println("Checking request for " + request.target().name() + " against " + player.name());
                System.out.println("Request target: " + request.target());
                System.out.println("Player: " + paperPlayer);
                if(request.target().equals(paperPlayer)){
                    request.accept();
                    return 1;
                }
            }
            player.sendMessage(MessageUtils.get("cmd.teleportrequestaccept.no_requests"));
            return 1;
        });
    }
}
