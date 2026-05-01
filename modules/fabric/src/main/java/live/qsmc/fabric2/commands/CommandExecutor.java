package live.qsmc.fabric2.commands;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import live.qsmc.fabric2.QuiptMod;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.permission.Permission;
import net.minecraft.command.permission.Permissions;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

public abstract class CommandExecutor extends Command {

    public CommandExecutor(QuiptMod mod, String cmd) {
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
        private final CommandExecutor cmd;
        private int permissionLevel = 0;

        public Builder(CommandExecutor executor) {
            this.cmd = executor;
        }

        public Builder setPermissionLevel(int level) {
            this.permissionLevel = level;
            return this;
        }

        public void register() {
            CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
                LiteralArgumentBuilder<ServerCommandSource> builder = cmd.arguments();
                dispatcher.register(builder);
            });
        }
    }
}

