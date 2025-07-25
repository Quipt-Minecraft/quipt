/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.quiptmc.paper.commands.executors;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.quiptmc.paper.api.PaperIntegration;
import com.quiptmc.paper.commands.CommandExecutor;
import com.quiptmc.paper.commands.executors.arguments.WarpArgumentType;
import com.quiptmc.minecraft.utils.teleportation.points.TeleportationPoint;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.plugin.java.JavaPlugin;

import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;

public class WarpCommand extends CommandExecutor {
    public WarpCommand(PaperIntegration integration) {
        super(integration, "warp");
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> execute() {
        return literal(name()).executes(context -> showUsage(context, "cmd.usage.warp")).then(literal("set").executes(context -> {
            context.getSource().getSender().sendMessage("Test");
            return 1;
        }).then(argument("warp", new WarpArgumentType()).executes(context -> {
            TeleportationPoint warp = context.getArgument("warp", TeleportationPoint.class);
            return 1;
        }))).then(literal("remove").executes(context -> {
            context.getSource().getSender().sendMessage("Test");
            return 1;
        }).then(argument("warp", new WarpArgumentType()).executes(context -> {
            TeleportationPoint warp = context.getArgument("warp", TeleportationPoint.class);
            return 1;
        }))).then(literal("list").executes(context -> {
            context.getSource().getSender().sendMessage("Test");
            return 1;
        })).then(argument("warp", new WarpArgumentType()).executes(context -> {
            TeleportationPoint warp = context.getArgument("warp", TeleportationPoint.class);
            return 1;
        })).build();
    }
}
