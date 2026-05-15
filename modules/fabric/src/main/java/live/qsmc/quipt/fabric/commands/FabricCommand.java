package live.qsmc.quipt.fabric.commands;

import live.qsmc.quipt.fabric.QuiptMod;
import net.kyori.adventure.text.Component;
import net.minecraft.command.permission.Permission;
import net.minecraft.command.permission.PermissionLevel;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

public abstract class FabricCommand extends live.qsmc.quipt.minecraft.commands.Command<ServerCommandSource, Permission> {

    private final QuiptMod mod;

    public FabricCommand(QuiptMod mod, String cmd) {
        super(mod.integration(), cmd);
        this.mod = mod;
    }

    public Permission permission(String id){
        if(permissions().get(id).isEmpty())
            permissions().register(id, new Permission.Atom(Identifier.of(id)));
        return permissions().get(id).get();
    }

    public Permission permission(int id){
        String idStr = String.valueOf(id);
        if(permissions().get(idStr).isEmpty())
            permissions().register(idStr, new Permission.Level(PermissionLevel.fromLevel(id)));
        return permissions().get(idStr).get();
    }

    public QuiptMod mod() {
        return mod;
    }

    @Override
    public void sendMessage(ServerCommandSource source, Component message) {
        source.sendMessage(message);
    }

    @Override
    public boolean hasPermission(ServerCommandSource source, Permission permission) {
        return source.getPermissions().hasPermission(permission);
    }

}
