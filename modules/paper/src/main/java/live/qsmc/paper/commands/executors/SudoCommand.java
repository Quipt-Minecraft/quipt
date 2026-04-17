package live.qsmc.paper.commands.executors;


import live.qsmc.paper.api.PaperIntegration;
import live.qsmc.paper.commands.CommandExecutor;
import live.qsmc.paper.commands.executors.arguments.CommandBuilderArgument;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import org.bukkit.entity.Player;

import java.util.List;

import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;

public class SudoCommand extends CommandExecutor {
    public SudoCommand(PaperIntegration integration) {
        super(integration, "sudo");
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> execute() {
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
                                }))).build();
    }
}
