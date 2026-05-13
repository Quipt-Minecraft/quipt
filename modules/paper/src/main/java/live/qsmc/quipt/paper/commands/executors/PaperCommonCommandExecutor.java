package live.qsmc.quipt.paper.commands.executors;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import live.qsmc.quipt.minecraft.api.MinecraftIntegration;
import live.qsmc.quipt.minecraft.commands.Command;
import live.qsmc.quipt.minecraft.commands.CommonCommand;
import live.qsmc.quipt.paper.QuiptPlugin;
import live.qsmc.quipt.paper.commands.PaperCommandExecutor;

public class PaperCommonCommandExecutor<C extends CommonCommand<CommandSourceStack>> extends PaperCommandExecutor {

    private final CommonCommand<CommandSourceStack> common;

    public PaperCommonCommandExecutor(QuiptPlugin plugin, Class<C> commonClass, String cmd) {
        super(plugin, cmd);
        try {
            this.common = commonClass.getConstructor(Command.class, MinecraftIntegration.class).newInstance(this, plugin.integration());
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate common command", e);
        }
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> arguments() {
        return common.arguments(this);
    }
}

