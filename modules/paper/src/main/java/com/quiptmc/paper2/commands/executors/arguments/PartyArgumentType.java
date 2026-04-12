package com.quiptmc.paper2.commands.executors.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.quiptmc.minecraft2.events.party.Party;
import com.quiptmc.paper2.QuiptPlugin;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class PartyArgumentType extends CustomArgumentConverter<@NotNull Party, @NotNull String> {

    public PartyArgumentType(QuiptPlugin plugin, String cmd) {
        super(plugin, cmd);
    }

    @Override
    public @NotNull Party convert(@NotNull String s) throws CommandSyntaxException {
        return plugin().integration().parties().get(s);
    }

    @Override
    public ArgumentType<@NotNull String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S> @NotNull CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, @NotNull SuggestionsBuilder builder) {

        String input = builder.getInput().substring(builder.getStart());
        String[] values = new String[plugin().integration().parties().parties.size()];
        int i = 0;
        for (Party party : plugin().integration().parties().parties.values()) {
            values[i] = party.id();
            i=i+1;
        }
        return onlySimilar(values, input, (CommandContext<CommandSourceStack>) context, builder);
    }
}
