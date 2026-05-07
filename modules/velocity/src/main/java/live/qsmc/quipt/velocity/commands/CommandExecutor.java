package live.qsmc.quipt.velocity.commands;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import live.qsmc.quipt.velocity.QuiptProxy;
import org.jetbrains.annotations.CheckReturnValue;

public abstract class CommandExecutor extends Command {

    public CommandExecutor(QuiptProxy proxy, String cmd) {
        super(proxy, cmd);
    }

    public LiteralCommandNode<CommandSource> execute() {
        return arguments().build();
    }

    public LiteralArgumentBuilder<CommandSource> literal(String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    public <T> RequiredArgumentBuilder<CommandSource, T> argument(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

    public abstract LiteralArgumentBuilder<CommandSource> arguments();

    public static class Builder {
        private final CommandExecutor cmd;
        private String[] aliases = new String[]{};

        @CheckReturnValue
        public Builder(CommandExecutor executor) {
            this.cmd = executor;
        }

        @CheckReturnValue
        public Builder setAliases(String... aliases) {
            this.aliases = aliases;
            return this;
        }

        public void register() {
            BrigadierCommand brigadierCommand = new BrigadierCommand(cmd.arguments());
            var meta = cmd.proxy().proxy().getCommandManager()
                .metaBuilder(brigadierCommand)
                .aliases(aliases)
                .build();
            cmd.proxy().proxy().getCommandManager().register(meta, brigadierCommand);
        }
    }
}

