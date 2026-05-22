package live.qsmc.quipt.fabric.commands;

import live.qsmc.quipt.fabric.QuiptMod;
import net.kyori.adventure.text.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.Identifier;

public abstract class FabricCommand extends live.qsmc.quipt.minecraft.commands.Command<CommandSourceStack, Permission> {

    private final QuiptMod mod;

    public FabricCommand(QuiptMod mod, String cmd) {
        super(mod.integration(), cmd);
        this.mod = mod;
    }

    public Permission permission(String id){
        if(permissions().get(id).isEmpty())
            permissions().register(id, new Permission.Atom(Identifier.parse(id)));
        return permissions().get(id).get();
    }

    public Permission permission(int id){
        String idStr = String.valueOf(id);
        if(permissions().get(idStr).isEmpty())
            permissions().register(idStr, new Permission.HasCommandLevel(PermissionLevel.byId(id)));
        return permissions().get(idStr).get();
    }

    public QuiptMod mod() {
        return mod;
    }

    @Override
    public void sendMessage(CommandSourceStack source, Component message) {
        net.minecraft.network.chat.Component minecraftComponent =
            net.minecraft.network.chat.ComponentSerialization.CODEC
                .parse(
                    com.mojang.serialization.JsonOps.INSTANCE,
                    com.google.gson.JsonParser.parseString(
                        net.kyori.adventure.text.serializer.gson.GsonComponentSerializer.gson()
                            .serialize(message)
                    )
                )
                .getOrThrow();
        source.sendSystemMessage(minecraftComponent);
    }

    @Override
    public boolean hasPermission(CommandSourceStack source, Permission permission) {
        return source.permissions().hasPermission(permission);
    }

}
