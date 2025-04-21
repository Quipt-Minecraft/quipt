package com.quiptmc.paper.commands.executors.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.quiptmc.core.data.registries.Registries;
import com.quiptmc.paper.entity.QuiptEntityType;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class QuiptEntityArgumentType implements CustomArgumentType.Converted<QuiptEntityType<?>, String> {

    @Override
    public QuiptEntityType<?> convert(@NotNull String nativeType) {
        return Registries.get(Registries.key("entities"), QuiptEntityType.class).get(nativeType).orElseThrow();

    }

    @Override
    public @NotNull ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S> @NotNull CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {

//        Registries.KEYS.get("entities").flatMap(key -> Registries.REGISTRAR.get(key, QuiptEntityType.class)).ifPresent(registry -> {
//            for (String entity : registry.toMap().keySet()) {
//                builder.suggest(entity);
//            }
//        });

        return builder.buildFuture();
    }
}