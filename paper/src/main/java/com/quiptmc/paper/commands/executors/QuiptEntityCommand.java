package com.quiptmc.paper.commands.executors;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.quiptmc.paper.commands.CommandExecutor;
import com.quiptmc.paper.commands.executors.arguments.QuiptEntityArgumentType;
import com.quiptmc.paper.entity.QuiptEntityType;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;

public class QuiptEntityCommand extends CommandExecutor {

    public QuiptEntityCommand(JavaPlugin plugin) {
        super(plugin, "quipentity");
    }

    // /rp update [url]

    @Override
    public LiteralCommandNode<CommandSourceStack> execute() {
        try {
            return literal(name()).executes(context -> {
                return showUsage(context, "quipt.admin.entity");
            }).then(argument("entity", new QuiptEntityArgumentType())).executes(context -> {
                CommandSourceStack source = context.getSource();
                QuiptEntityType<?> type = context.getArgument("entity", QuiptEntityType.class);
                type.spawn(new Class<?>[]{Location.class}, source.getLocation());
                return 1;
            }).build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
