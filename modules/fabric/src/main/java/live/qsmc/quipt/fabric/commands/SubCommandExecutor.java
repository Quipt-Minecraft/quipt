package live.qsmc.quipt.fabric.commands;

import live.qsmc.quipt.fabric.QuiptMod;
import net.minecraft.server.command.ServerCommandSource;

public abstract class SubCommandExecutor extends Command implements com.mojang.brigadier.Command<ServerCommandSource> {

    private final Command root;

    public SubCommandExecutor(QuiptMod mod, Command root, String cmd) {
        super(mod, cmd);
        this.root = root;
    }

    public Command root() {
        return root;
    }
}

