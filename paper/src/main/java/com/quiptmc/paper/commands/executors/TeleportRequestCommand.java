/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.quiptmc.paper.commands.executors;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.quiptmc.paper.commands.CommandExecutor;
import com.quiptmc.minecraft.utils.chat.MessageUtils;
import com.quiptmc.minecraft.utils.teleportation.LocationUtils;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;

public class TeleportRequestCommand extends CommandExecutor {

    public TeleportRequestCommand(JavaPlugin plugin) {
        super(plugin, "teleportrequest");
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> execute() {
        return literal(name()).executes(context -> showUsage(context, "quipt.cmd.teleportrequest"))
                .then(argument("player", ArgumentTypes.player())
                        .executes(context -> {
                            if(!(context.getSource().getSender() instanceof Player player)) return logError(context, MessageUtils.get("cmd.error.player_only"));
                            PlayerSelectorArgumentResolver targetSelector = context.getArgument("player", PlayerSelectorArgumentResolver.class);
                            targetSelector.resolve(context.getSource()).forEach(target -> {
//                                LocationUtils.request(player, target);
                            });
//                            target.sendMessage(text("test"));
                            return 1;
                        })).build();
    }
}
