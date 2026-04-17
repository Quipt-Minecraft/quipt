package live.qsmc.paper2.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import live.qsmc.minecraft2.utils.chat.MessageUtils;
import live.qsmc.paper2.QuiptPlugin;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import org.bukkit.command.CommandSender;

import java.util.concurrent.CompletableFuture;

import static net.kyori.adventure.text.Component.text;

public abstract class Command {

    private final QuiptPlugin plugin;
    private final String cmd;

    public Command(QuiptPlugin plugin, String cmd){
        this.plugin = plugin;
        this.cmd = cmd;
    }

    public String name() {
        return cmd;
    }

    public QuiptPlugin plugin() {
        return plugin;
    }

    public int logError(CommandContext<CommandSourceStack> context, String message) {
        return logError(context, text(message));
    }

    public int logError(CommandContext<CommandSourceStack> context, Component message) {
        context.getSource().getSender().sendMessage(message.style(Style.style().color(NamedTextColor.RED).build()));
        return 0;
    }

    public int showUsage(CommandContext<CommandSourceStack> context, String perm) {
        CommandSender sender = context.getSource().getSender();
        StringBuilder args = new StringBuilder();
        for(ParsedCommandNode<CommandSourceStack> node : context.getNodes()){
            CommandNode<?> newNode = node.getNode();
            if(newNode instanceof LiteralCommandNode){
                args.append(newNode.getName()).append(".");
            }
        }
        return logError(context, (perm.equalsIgnoreCase("") || sender.hasPermission(perm)) ? MessageUtils.get("cmd." + args + "usage") : MessageUtils.get("cmd.error.no_perm", perm));
    }

    public CompletableFuture<Suggestions> onlySimilar(String[] values, String argumentName, CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        String value;
        try {
            value = context.getArgument(argumentName, String.class);
        }catch (IllegalArgumentException ex){
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
