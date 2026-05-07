package live.qsmc.quipt.paper.commands;

import live.qsmc.quipt.paper.QuiptPlugin;
import io.papermc.paper.command.brigadier.CommandSourceStack;

public abstract class SubCommandExecutor extends Command implements com.mojang.brigadier.Command<CommandSourceStack> {

    private final Command root;

    public SubCommandExecutor(QuiptPlugin plugin, Command root, String cmd) {
        super(plugin, cmd);
        this.root = root;
    }

    public Command root() {
        return root;
    }

}
