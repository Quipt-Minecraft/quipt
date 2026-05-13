package live.qsmc.quipt.paper.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import live.qsmc.quipt.minecraft.utils.chat.MessageUtils;
import live.qsmc.quipt.paper.QuiptPlugin;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

import static net.kyori.adventure.text.Component.text;

public abstract class PaperCommand extends live.qsmc.quipt.minecraft.commands.Command<CommandSourceStack> {

    private final QuiptPlugin plugin;
    private final String cmd;

    public PaperCommand(QuiptPlugin plugin, String cmd){
        super(cmd);
        this.plugin = plugin;
        this.cmd = cmd;
    }

    public QuiptPlugin plugin() {
        return plugin;
    }

    @Override
    public void sendMessage(CommandSourceStack source, Component message) {
        source.getSender().sendMessage(message);
    }

    @Override
    public boolean hasPermission(CommandSourceStack source, String permission) {
        if (permission.isEmpty()) return true;
        return source.getSender().hasPermission(permission);
    }

    public int showUsage(CommandContext<CommandSourceStack> context, String perm) {
        CommandSender sender = context.getSource().getSender();
        StringBuilder args = new StringBuilder();
        for(ParsedCommandNode<CommandSourceStack> node : context.getNodes()){
            CommandNode<?> newNode = node.getNode();
            if(newNode instanceof LiteralCommandNode){
                args.append(newNode.getName()).append(".");
            }
        }
        return logError(context, (perm.equalsIgnoreCase("") || sender.hasPermission(perm)) ? MessageUtils.get("cmd." + args + "usage") : MessageUtils.get("cmd.error.no_perm", perm));
    }
}
