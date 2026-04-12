package com.quiptmc.paper2.commands.executors.arguments;

import com.quiptmc.paper2.QuiptPlugin;
import com.quiptmc.paper2.commands.Command;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import org.jetbrains.annotations.NotNull;

public abstract class CustomArgumentConverter<R, I> extends Command implements CustomArgumentType.Converted<@NotNull R, @NotNull I> {


    public CustomArgumentConverter(QuiptPlugin plugin, String cmd) {
        super(plugin, cmd);
    }
}