package live.qsmc.quipt.velocity.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.permission.PermissionSubject;
import live.qsmc.quipt.velocity.QuiptProxy;
import net.kyori.adventure.text.Component;

import static net.kyori.adventure.text.Component.text;

public abstract class VelocityCommand extends live.qsmc.quipt.minecraft.commands.Command<CommandSource, String> {

    private final QuiptProxy proxy;
    private final String cmd;

    public VelocityCommand(QuiptProxy proxy, String cmd) {
        super(proxy.integration(), cmd);
        this.proxy = proxy;
        this.cmd = cmd;
    }

    public String name() {
        return cmd;
    }

    public QuiptProxy proxy() {
        return proxy;
    }

    @Override
    public void sendMessage(CommandSource source, Component message) {
        source.sendMessage(message);
    }

    @Override
    public boolean hasPermission(CommandSource source, String permission) {
        if (permission.isEmpty()) return true;
        return source.hasPermission(permission);
    }

    @Override
    public int showUsage(CommandContext<CommandSource> context, String perm) {
        StringBuilder args = new StringBuilder();
        for (ParsedCommandNode<CommandSource> node : context.getNodes()) {
            CommandNode<?> newNode = node.getNode();
            if (newNode instanceof LiteralCommandNode) {
                args.append(newNode.getName()).append(".");
            }
        }
        boolean hasPerm = perm.equalsIgnoreCase("") || context.getSource().hasPermission(perm);
        String errorMessage = hasPerm ? "Usage: /" + args.toString().replace(".", " ").trim() : "You do not have permission (" + perm + ")";
        return logError(context, errorMessage);
    }
}
