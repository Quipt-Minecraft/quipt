package live.qsmc.quipt.minecraft.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.core.data.registries.Registry;
import live.qsmc.quipt.minecraft.api.MinecraftIntegration;
import live.qsmc.quipt.minecraft.utils.chat.MessageUtils;
import net.kyori.adventure.permission.PermissionChecker;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;

import java.util.concurrent.CompletableFuture;

import static net.kyori.adventure.text.Component.text;

public abstract class Command<S, P> {

    private final String cmd;
    private final Registry<P> permissions;

    public Command(MinecraftIntegration<?,?> integration, String cmd) {
        this.cmd = cmd;
        this.permissions = Quipt.INSTANCE.registries().register(integration.id() + ":" + cmd + ":permissions", () -> null);
    }

    public abstract P permission(String id);

    public abstract P permission(int id);

    public Registry<P> permissions() {
        return permissions;
    }


    public String name() {
        return cmd;
    }

    public abstract void sendMessage(S source, Component message);

    public abstract boolean hasPermission(S source, P permission);

    public int logError(CommandContext<S> context, String message) {
        return logError(context, text(message));
    }

    public int logError(CommandContext<S> context, Component message) {
        return log(context, message, NamedTextColor.RED, 0);
    }

    public int logSuccess(CommandContext<S> context, String message) {
        return logSuccess(context, text(message));
    }

    public int logSuccess(CommandContext<S> context, Component message) {
        return log(context, message, NamedTextColor.GREEN, 1);
    }

    public int log(CommandContext<S> context, String message, TextColor color, int value) {
        return log(context, text(message), color, value);
    }

    public int log(CommandContext<S> context, Component message, TextColor color, int value) {
        Component styled = message.style(Style.style().color(color).build());
        sendMessage(context.getSource(), styled);
        return value;
    }

    private String getUsageKey(CommandContext<S> context) {
        StringBuilder args = new StringBuilder();
        for(ParsedCommandNode<S> node : context.getNodes()){
            CommandNode<?> newNode = node.getNode();
            if(newNode instanceof LiteralCommandNode){
                args.append(newNode.getName()).append(".");
            }
        }
        return "cmd." + args + "usage";
    }

    public Component getUsage(CommandContext<S> context) {
        return MessageUtils.get(getUsageKey(context));
    }

    public int showUsage(CommandContext<S> context, P perm) {
        return logError(context, hasPermission(context.getSource(), perm) ? MessageUtils.get(getUsageKey(context)) : MessageUtils.get("cmd.error.no_perm", perm));
    }

    public CompletableFuture<Suggestions> onlySimilar(String[] values, String argumentName, CommandContext<S> context, SuggestionsBuilder builder) {
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
            if (v.toLowerCase().startsWith(input.toLowerCase()) || v.equalsIgnoreCase(input)) {
                builder.suggest(v);
            }
        }
        return builder.buildFuture();
    }
}
