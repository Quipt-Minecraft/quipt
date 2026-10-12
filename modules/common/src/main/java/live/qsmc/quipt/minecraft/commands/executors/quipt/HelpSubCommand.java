package live.qsmc.quipt.minecraft.commands.executors.quipt;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import live.qsmc.quipt.minecraft.commands.executors.QuiptCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Map;

public class HelpSubCommand<S, P> extends QuiptSubCommand<S, P> {

    public HelpSubCommand(QuiptCommand<S, P> root, String name) {
        super(root, name);
    }

    @Override
    public Component help() {
        return Component.text("Usage: /quipt help <sub-command>")
            .appendNewline()
            .append(Component.text("Shows help for a sub-command", NamedTextColor.GRAY));
    }

    @Override
    public int run(CommandContext<S> context) {
        String cmd = StringArgumentType.getString(context, "cmd");
        Map<String, QuiptSubCommand<S, P>> subCommands = root.subCommands();
        if (cmd == null || cmd.isEmpty())
            return root.command().logError(context, "No command specified");
        if (!subCommands.containsKey(cmd))
            return root.command().logError(context, "Unknown sub-command: " + cmd);
        QuiptSubCommand<S, P> executor = subCommands.get(cmd);
        return root.command().log(context, executor.help(), NamedTextColor.GREEN, 1);
    }
}