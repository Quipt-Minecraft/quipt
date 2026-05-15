package live.qsmc.quipt.minecraft.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import live.qsmc.quipt.minecraft.api.MinecraftIntegration;

public abstract class CommonCommand<S, P> {

    private final Command<S, P> command;
    private final MinecraftIntegration<?,?> integration;

    public CommonCommand(Command<S, P> command, MinecraftIntegration<?,?> integration) {
        this.command = command;
        this.integration = integration;
    }

    public Command<S, P> command() {
        return command;
    }

    public MinecraftIntegration<?,?> integration() {
        return integration;
    }



    public abstract LiteralArgumentBuilder<S> arguments(CommandBuilder<S> builder);
}
