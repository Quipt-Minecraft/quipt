package live.qsmc.quipt.paper.commands;

import live.qsmc.quipt.paper.QuiptPlugin;
import io.papermc.paper.command.brigadier.CommandSourceStack;

public abstract class SubCommandExecutor extends PaperCommand implements com.mojang.brigadier.Command<CommandSourceStack> {

    private final PaperCommand root;

    public SubCommandExecutor(QuiptPlugin plugin, PaperCommand root, String cmd) {
        super(plugin, cmd);
        this.root = root;
    }

    public PaperCommand root() {
        return root;
    }

}
