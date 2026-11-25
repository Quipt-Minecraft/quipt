package live.qsmc.quipt.paper.commands.executors.arguments;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import live.qsmc.quipt.minecraft.commands.CommandData;
import live.qsmc.quipt.paper.QuiptPlugin;
import live.qsmc.quipt.paper.commands.PaperCommand;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public abstract class CustomArgumentConverter<R, I> extends CommandData<CommandSourceStack> implements CustomArgumentType.Converted<@NotNull R, @NotNull I> {


    public CustomArgumentConverter(QuiptPlugin plugin, String cmd) {
        super(plugin.integration(), cmd);
    }

    @Override
    public void sendMessage(CommandSourceStack source, Component message) {
        source.getSender().sendMessage(message);
    }
}