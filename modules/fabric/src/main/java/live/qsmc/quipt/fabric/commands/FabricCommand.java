package live.qsmc.quipt.fabric.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import live.qsmc.quipt.fabric.QuiptMod;
import live.qsmc.quipt.minecraft.utils.chat.MessageUtils;
import net.kyori.adventure.text.Component;
import net.minecraft.command.permission.Permission;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

import static net.kyori.adventure.text.Component.text;

public abstract class FabricCommand extends live.qsmc.quipt.minecraft.commands.Command<ServerCommandSource> {

    private final QuiptMod mod;

    public FabricCommand(QuiptMod mod, String cmd) {
        super(cmd);
        this.mod = mod;
    }

    public QuiptMod mod() {
        return mod;
    }

    @Override
    public void sendMessage(ServerCommandSource source, Component message) {
        source.sendMessage(message);
    }

    @Override
    public boolean hasPermission(ServerCommandSource source, String permission) {
        if (permission.isEmpty()) return true;
        return source.getPermissions().hasPermission(new Permission.Atom(Identifier.of(permission)));
    }

    public int showUsage(CommandContext<ServerCommandSource> context, Permission perm) {
        ServerCommandSource sender = context.getSource();
        StringBuilder args = new StringBuilder();
        for(ParsedCommandNode<ServerCommandSource> node : context.getNodes()){
            CommandNode<?> newNode = node.getNode();
            if(newNode instanceof LiteralCommandNode){
                args.append(newNode.getName()).append(".");
            }
        }
        return logError(context, sender.getPermissions().hasPermission(perm) ? MessageUtils.get("cmd." + args + "usage") : MessageUtils.get("cmd.error.no_perm", perm));
    }
}
