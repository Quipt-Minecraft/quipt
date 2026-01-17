package com.quiptmc2.paper.commands.executors;


import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.quiptmc2.paper.QuiptPlugin;
import com.quiptmc2.paper.commands.CommandExecutor;
import com.quiptmc2.paper.commands.executors.arguments.CommandBuilderArgument;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import org.bukkit.entity.Player;

import java.util.List;

import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;

public class SudoCommand extends CommandExecutor {
    public SudoCommand(QuiptPlugin integration) {
        super(integration, "sudo");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> arguments() {
        return literal(name()).executes(context -> {
                    return 1;
                })
                .then(argument("target", ArgumentTypes.player())
                        .executes(context -> {
                            return 1;
                        })
                        .then(argument("command", new CommandBuilderArgument())
                                .executes(context -> {
                                    PlayerSelectorArgumentResolver targetResolver = context.getArgument("target", PlayerSelectorArgumentResolver.class);
                                    List<Player> targets = targetResolver.resolve(context.getSource());
                                    CommandBuilderArgument.ExecutableCommand command = context.getArgument("command", CommandBuilderArgument.ExecutableCommand.class);
                                    targets.forEach(player -> command.execute(player));
                                    return 1;
                                })));
    }

}
