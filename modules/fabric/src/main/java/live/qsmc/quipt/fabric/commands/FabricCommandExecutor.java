package live.qsmc.quipt.fabric.commands;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.core.data.registries.Registry;
import live.qsmc.quipt.fabric.QuiptMod;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.permission.Permission;
import net.minecraft.command.permission.PermissionLevel;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

import live.qsmc.quipt.minecraft.commands.CommandBuilder;

public abstract class FabricCommandExecutor extends FabricCommand implements CommandBuilder<ServerCommandSource> {


    private final Registry<Permission> permissions;


    public FabricCommandExecutor(QuiptMod mod, String cmd) {
        super(mod, cmd);
        permissions = Quipt.INSTANCE.registries().register("cmd." + cmd + ".permissions", () -> null);
    }

    public Permission permission(String id){
        if(permissions.get(id).isEmpty())
            permissions.register(id, new Permission.Atom(Identifier.of(id)));
        return permissions.get(id).get();
    }

    public Permission permission(int id){
        String idStr = String.valueOf(id);
        if(permissions.get(idStr).isEmpty())
            permissions.register(idStr, new Permission.Level(PermissionLevel.fromLevel(id)));
        return permissions.get(idStr).get();
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
        private int permissionLevel = 0;

        public Builder(FabricCommandExecutor executor) {
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
