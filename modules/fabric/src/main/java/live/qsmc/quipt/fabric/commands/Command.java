package live.qsmc.quipt.fabric.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import live.qsmc.quipt.fabric.QuiptMod;
import live.qsmc.quipt.minecraft.utils.chat.MessageUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.minecraft.command.permission.Permission;
import net.minecraft.server.command.ServerCommandSource;

import java.util.concurrent.CompletableFuture;

import static net.kyori.adventure.text.Component.text;

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
        return logError(context, text(message));
    }

    public int logError(CommandContext<ServerCommandSource> context, Component message) {
        return log(context, message, NamedTextColor.RED, 0);
    }

    public int logSuccess(CommandContext<ServerCommandSource> context, String message) {
        return logSuccess(context, text(message));
    }

    public int logSuccess(CommandContext<ServerCommandSource> context, Component message) {
        return log(context, message, NamedTextColor.GREEN, 1);
    }

    public int log(CommandContext<ServerCommandSource> context, String message, TextColor color, int value) {
        return log(context, text(message), color, value);
    }

    public int log(CommandContext<ServerCommandSource> context, Component message, TextColor color, int value) {
        context.getSource().sendMessage(text().style(Style.style().color(color).build()).append(message));
        return value;
    }

    public int showUsage(CommandContext<ServerCommandSource> context, Permission perm) {
        ServerCommandSource sender = context.getSource();
        StringBuilder args = new StringBuilder();
        for(ParsedCommandNode<ServerCommandSource> node : context.getNodes()){
            CommandNode<?> newNode = node.getNode();
            if(newNode instanceof LiteralCommandNode){
                args.append(newNode.getName()).append(".");
            }
        }
        return logError(context, sender.getPermissions().hasPermission(perm) ? MessageUtils.get("cmd." + args + "usage") : MessageUtils.get("cmd.error.no_perm", perm));
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

