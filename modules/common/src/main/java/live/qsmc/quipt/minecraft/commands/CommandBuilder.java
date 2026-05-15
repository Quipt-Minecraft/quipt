package live.qsmc.quipt.minecraft.commands;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

public interface CommandBuilder<S> {
    LiteralArgumentBuilder<S> literal(String name);
    <T> RequiredArgumentBuilder<S, T> argument(String name, ArgumentType<T> type);
    LiteralArgumentBuilder<S> arguments();


}
