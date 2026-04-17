package live.qsmc.velocity.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.CommandSource;
import live.qsmc.velocity.QuiptProxy;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;

import java.util.concurrent.CompletableFuture;

import static net.kyori.adventure.text.Component.text;

public abstract class Command {

    private final QuiptProxy proxy;
    private final String cmd;

    public Command(QuiptProxy proxy, String cmd) {
        this.proxy = proxy;
        this.cmd = cmd;
    }

    public String name() {
        return cmd;
    }

    public QuiptProxy proxy() {
        return proxy;
    }

    public int logError(CommandContext<CommandSource> context, String message) {
        return logError(context, text(message));
    }

    public int logError(CommandContext<CommandSource> context, Component message) {
        context.getSource().sendMessage(message.style(Style.style().color(NamedTextColor.RED).build()));
        return 0;
    }

    public int showUsage(CommandContext<CommandSource> context, String perm) {
        StringBuilder args = new StringBuilder();
        for (ParsedCommandNode<CommandSource> node : context.getNodes()) {
            CommandNode<?> newNode = node.getNode();
            if (newNode instanceof LiteralCommandNode) {
                args.append(newNode.getName()).append(".");
            }
        }
        boolean hasPerm = perm.equalsIgnoreCase("") || context.getSource().hasPermission(perm);
        String errorMessage = hasPerm ? "Usage: /" + args.toString().replace(".", " ").trim() : "You do not have permission (" + perm + ")";
        return logError(context, errorMessage);
    }

    public CompletableFuture<Suggestions> onlySimilar(String[] values, String argumentName, CommandContext<CommandSource> context, SuggestionsBuilder builder) {
        String value;
        try {
            value = context.getArgument(argumentName, String.class);
        } catch (IllegalArgumentException ex) {
            value = "";
        }
        return getSuggestionsCompletableFuture(values, value, builder);
    }

    public CompletableFuture<Suggestions> getSuggestionsCompletableFuture(String[] values, String input, SuggestionsBuilder builder) {
        if (input == null || input.isBlank()) {
            for (String v : values) {
                if (v == null) continue;
                builder.suggest(v);
            }
            return builder.buildFuture();
        }
        for (String v : values) {
            if (v == null) continue;
            if (v.toLowerCase().startsWith(input.toLowerCase())) {
                builder.suggest(v);
            }
        }
        return builder.buildFuture();
    }
}

