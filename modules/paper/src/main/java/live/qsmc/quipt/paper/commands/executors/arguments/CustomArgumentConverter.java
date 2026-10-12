package live.qsmc.quipt.paper.commands.executors.arguments;

import live.qsmc.quipt.paper.QuiptPlugin;
import live.qsmc.quipt.paper.commands.PaperCommand;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import org.jetbrains.annotations.NotNull;

public abstract class CustomArgumentConverter<R, I> extends PaperCommand implements CustomArgumentType.Converted<@NotNull R, @NotNull I> {


    public CustomArgumentConverter(QuiptPlugin plugin, String cmd) {
        super(plugin, cmd);
    }
}