package live.qsmc.quipt.minecraft.commands.executors.quipt;

import com.mojang.brigadier.context.CommandContext;
import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.minecraft.commands.executors.QuiptCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class DumpSubCommand<S, P> extends QuiptSubCommand<S, P> {

    public DumpSubCommand(QuiptCommand<S, P> root, String name) {
        super(root, name);
    }

    @Override
    public int run(CommandContext<S> context) {
        return command.showUsage(context, root.command().permission(4));
    }

    @Override
    public Component help() {
        return Component.text("Usage: /quipt list").appendNewline().append(Component.text("Lists all available Quipt integrations", NamedTextColor.GRAY));
    }


}
