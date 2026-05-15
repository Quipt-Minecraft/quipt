package live.qsmc.quipt.paper.commands;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import live.qsmc.quipt.paper.QuiptPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.permissions.Permission;

public abstract class PaperCommand extends live.qsmc.quipt.minecraft.commands.Command<CommandSourceStack, Permission> {

    private final QuiptPlugin plugin;
    private final String cmd;

    public PaperCommand(QuiptPlugin plugin, String cmd){
        super(plugin.integration(), cmd);
        this.plugin = plugin;
        this.cmd = cmd;
    }

    public QuiptPlugin plugin() {
        return plugin;
    }

    public Permission permission(String id){
        if(permissions().get(id).isEmpty())
            permissions().register(id, new Permission(id));
        return permissions().get(id).get();
    }

    public Permission permission(int id){
        return permission(String.valueOf(id));
    }

    @Override
    public void sendMessage(CommandSourceStack source, Component message) {
        source.getSender().sendMessage(message);
    }

    @Override
    public boolean hasPermission(CommandSourceStack source, Permission permission) {

        return source.getSender().hasPermission(permission);
    }

}
