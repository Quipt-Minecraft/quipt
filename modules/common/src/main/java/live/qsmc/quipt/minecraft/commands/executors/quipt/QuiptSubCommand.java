package live.qsmc.quipt.minecraft.commands.executors.quipt;

import live.qsmc.quipt.minecraft.commands.Command;
import live.qsmc.quipt.minecraft.commands.executors.QuiptCommand;
import net.kyori.adventure.text.Component;

public abstract class QuiptSubCommand<S> implements com.mojang.brigadier.Command<S> {
    protected final QuiptCommand<S> root;
    protected final Command<S> command;
    protected final String name;

    public QuiptSubCommand(QuiptCommand<S> root, String name) {
        this.root = root;
        this.command = root.command();
        this.name = name;
    }

    public abstract Component help();
}
