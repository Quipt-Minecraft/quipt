package live.qsmc.paper2.commands;


import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import live.qsmc.paper2.QuiptPlugin;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;

public abstract class CommandExecutor extends Command {

    public CommandExecutor(QuiptPlugin plugin, String cmd) {
        super(plugin, cmd);
    }

    public LiteralCommandNode<CommandSourceStack> execute(){
        return arguments().build();
    }

    public abstract LiteralArgumentBuilder<CommandSourceStack> arguments();

    public LiteralArgumentBuilder<CommandSourceStack> builder() {
        return literal(name());
    }

    public LiteralArgumentBuilder<CommandSourceStack> literal(String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    public static class Builder {
        CommandExecutor cmd;
        String desc = "";
        String[] aliases = new String[]{};


        @CheckReturnValue
        public Builder(CommandExecutor executor) {
            this.cmd = executor;
        }

        @CheckReturnValue
        public Builder setDescription(String desc) {
            this.desc = desc;
            return this;
        }

        @CheckReturnValue
        public Builder setAliases(String... aliases) {
            this.aliases = aliases;
            return this;
        }

        public void register() {
            @NotNull LifecycleEventManager<@NotNull Plugin> manager = cmd.plugin().getLifecycleManager();
            manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
                final Commands commands = event.registrar();
                commands.register(cmd.execute(), desc, List.of(aliases));
            });
        }
    }
}
