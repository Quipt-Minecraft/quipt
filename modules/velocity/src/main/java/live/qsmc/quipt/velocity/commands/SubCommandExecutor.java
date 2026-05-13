package live.qsmc.quipt.velocity.commands;

import com.velocitypowered.api.command.CommandSource;
import live.qsmc.quipt.velocity.QuiptProxy;

public abstract class SubCommandExecutor extends VelocityCommand implements com.mojang.brigadier.Command<CommandSource> {

    private final VelocityCommand root;

    public SubCommandExecutor(QuiptProxy proxy, VelocityCommand root, String cmd) {
        super(proxy, cmd);
        this.root = root;
    }

    public VelocityCommand root() {
        return root;
    }
}

