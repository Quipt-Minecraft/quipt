package com.quiptmc2.paper.commands.executors.arguments;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.quiptmc2.paper.QuiptPlugin;
import com.quiptmc2.paper.commands.Command;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public abstract class CustomArgumentConverter<R, I> extends Command implements CustomArgumentType.Converted<@NotNull R, @NotNull I> {


    public CustomArgumentConverter(QuiptPlugin plugin, String cmd) {
        super(plugin, cmd);
    }
}