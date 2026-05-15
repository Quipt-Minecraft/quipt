package live.qsmc.quipt.fabric.commands;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import live.qsmc.quipt.fabric.QuiptMod;
import live.qsmc.quipt.minecraft.commands.CommandBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;

public abstract class FabricCommandExecutor extends FabricCommand implements CommandBuilder<ServerCommandSource> {


    public FabricCommandExecutor(QuiptMod mod, String cmd) {
        super(mod, cmd);
    }

    public LiteralCommandNode<ServerCommandSource> execute() {
        return arguments().build();
    }

    public abstract LiteralArgumentBuilder<ServerCommandSource> arguments();

    public LiteralArgumentBuilder<ServerCommandSource> builder() {
        return literal(name());
    }

    public LiteralArgumentBuilder<ServerCommandSource> literal(String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    public <T> RequiredArgumentBuilder<ServerCommandSource, T> argument(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

    public static class Builder {
        private final FabricCommandExecutor cmd;

        public Builder(FabricCommandExecutor executor) {
            this.cmd = executor;
        }

        public void register() {
            CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
                LiteralArgumentBuilder<ServerCommandSource> builder = cmd.arguments();
                dispatcher.register(builder);
            });
        }
    }
}
