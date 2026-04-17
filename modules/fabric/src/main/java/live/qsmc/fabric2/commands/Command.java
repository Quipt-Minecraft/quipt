package live.qsmc.fabric2.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import live.qsmc.fabric2.QuiptMod;
import net.minecraft.command.permission.Permission;
import net.minecraft.command.permission.PermissionLevel;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public abstract class Command {

    private final QuiptMod mod;
    private final String cmd;

    public Command(QuiptMod mod, String cmd) {
        this.mod = mod;
        this.cmd = cmd;
    }

    public String name() {
        return cmd;
    }

    public QuiptMod mod() {
        return mod;
    }

    public int logError(CommandContext<ServerCommandSource> context, String message) {
        return logError(context, Text.literal(message));
    }

    public int logError(CommandContext<ServerCommandSource> context, Text message) {
        context.getSource().sendError(Text.literal("").append(message).formatted(Formatting.RED));
        return 0;
    }

    public int showUsage(CommandContext<ServerCommandSource> context, String perm) {
        StringBuilder args = new StringBuilder();
        for (ParsedCommandNode<ServerCommandSource> node : context.getNodes()) {
            CommandNode<?> newNode = node.getNode();
            if (newNode instanceof LiteralCommandNode) {
                args.append(newNode.getName()).append(".");
            }
        }
        boolean hasPerm = perm.equalsIgnoreCase("") || context.getSource().getPermissions().hasPermission(new Permission.Atom(Identifier.of(perm)));
        String errorMessage = hasPerm ? "Usage: /" + args.toString().replace(".", " ").trim() : "You do not have permission (" + perm + ")";
        return logError(context, errorMessage);
    }

    public CompletableFuture<Suggestions> onlySimilar(String[] values, String argumentName, CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
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

