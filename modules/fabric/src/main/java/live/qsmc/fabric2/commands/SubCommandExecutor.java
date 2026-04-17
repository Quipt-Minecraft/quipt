package live.qsmc.fabric2.commands;

import live.qsmc.fabric2.QuiptMod;
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

