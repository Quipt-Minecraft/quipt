package live.qsmc.quipt.minecraft.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.core.data.registries.Registry;
import live.qsmc.quipt.minecraft.MinecraftIntegration;
import live.qsmc.quipt.minecraft.utils.chat.MessageUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;

import java.util.concurrent.CompletableFuture;

import static net.kyori.adventure.text.Component.text;

public abstract class Command<S, P> extends CommandData<S> {

    private final Registry<P> permissions;

    public Command(MinecraftIntegration<?,?,?,?,?> integration, String cmd) {
        super(integration, cmd);
        this.permissions = Quipt.INSTANCE.registries().register(integration.id() + ":" + cmd + ":permissions", () -> null);
    }

    public abstract P permission(String id);

    public abstract P permission(int id);

    public Registry<P> permissions() {
        return permissions;
    }

    public abstract boolean hasPermission(S source, P permission);

    public int showUsage(CommandContext<S> context, P perm) {
        return showUsage(context, hasPermission(context.getSource(), perm));
    }


}
