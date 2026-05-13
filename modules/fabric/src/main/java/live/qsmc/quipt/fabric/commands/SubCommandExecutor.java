package live.qsmc.quipt.fabric.commands;

import live.qsmc.quipt.fabric.QuiptMod;
import net.minecraft.server.command.ServerCommandSource;

public abstract class SubCommandExecutor extends FabricCommand implements com.mojang.brigadier.Command<ServerCommandSource> {

    private final FabricCommand root;

    public SubCommandExecutor(QuiptMod mod, FabricCommand root, String cmd) {
        super(mod, cmd);
        this.root = root;
    }

    public FabricCommand root() {
        return root;
    }
}

