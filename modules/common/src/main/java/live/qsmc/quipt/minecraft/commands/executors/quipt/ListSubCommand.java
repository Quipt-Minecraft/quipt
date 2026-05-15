package live.qsmc.quipt.minecraft.commands.executors.quipt;

import com.mojang.brigadier.context.CommandContext;
import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.minecraft.commands.executors.QuiptCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ListSubCommand<S, P> extends QuiptSubCommand<S, P> {

    public ListSubCommand(QuiptCommand<S, P> root, String name) {
        super(root, name);
    }

    @Override
    public int run(CommandContext<S> context) {
        Component text = Component.text("Quipt integrations:").appendNewline();
        boolean first = true;
        for (QuiptIntegration integration : Quipt.INSTANCE.integrations()) {
            text = text.append(Component.text(first ? integration.name() : ", " + integration.name()));
            first = false;
        }
        root.command().log(context, text.append(Component.text(".")), NamedTextColor.GREEN, 1);
        return 1;
    }

    @Override
    public Component help() {
        return Component.text("Usage: /quipt list").appendNewline().append(Component.text("Lists all available Quipt integrations", NamedTextColor.GRAY));
    }


}