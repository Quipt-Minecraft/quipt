package live.qsmc.quipt.fabric.commands;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import live.qsmc.quipt.fabric.QuiptMod;
import live.qsmc.quipt.minecraft.commands.CommandBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;

public abstract class FabricCommandExecutor extends FabricCommand implements CommandBuilder<CommandSourceStack> {


    public FabricCommandExecutor(QuiptMod mod, String cmd) {
        super(mod, cmd);
    }

    public LiteralCommandNode<CommandSourceStack> execute() {
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
        private final FabricCommandExecutor cmd;

        public Builder(FabricCommandExecutor executor) {
            this.cmd = executor;
        }

        public void register() {
            CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
                LiteralArgumentBuilder<CommandSourceStack> builder = cmd.arguments();
                dispatcher.register(builder);
            });
        }
    }
}
