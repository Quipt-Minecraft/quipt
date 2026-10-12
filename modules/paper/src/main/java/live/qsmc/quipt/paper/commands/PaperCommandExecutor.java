package live.qsmc.quipt.paper.commands;


import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import live.qsmc.quipt.minecraft.commands.CommandBuilder;
import live.qsmc.quipt.paper.QuiptPlugin;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class PaperCommandExecutor extends PaperCommand implements CommandBuilder<CommandSourceStack> {

    public PaperCommandExecutor(QuiptPlugin plugin, String cmd) {
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

    public <T> RequiredArgumentBuilder<CommandSourceStack, T> argument(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

    public static class Builder {
        PaperCommandExecutor cmd;
        String desc = "";
        String[] aliases = new String[]{};


        @CheckReturnValue
        public Builder(PaperCommandExecutor executor) {
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
