package live.qsmc.quipt.velocity.commands;

import com.velocitypowered.api.command.CommandSource;
import live.qsmc.quipt.velocity.QuiptProxy;

public abstract class SubCommandExecutor extends Command implements com.mojang.brigadier.Command<CommandSource> {

    private final Command root;

    public SubCommandExecutor(QuiptProxy proxy, Command root, String cmd) {
        super(proxy, cmd);
        this.root = root;
    }

    public Command root() {
        return root;
    }
}

