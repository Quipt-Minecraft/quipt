package live.qsmc.quipt.minecraft.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import live.qsmc.quipt.minecraft.api.MinecraftIntegration;

public abstract class CommonCommand<S> {

    private final Command<S> command;
    private final MinecraftIntegration<?,?> integration;

    public CommonCommand(Command<S> command, MinecraftIntegration<?,?> integration) {
        this.command = command;
        this.integration = integration;
    }

    public Command<S> command() {
        return command;
    }

    public MinecraftIntegration<?,?> integration() {
        return integration;
    }



    public abstract LiteralArgumentBuilder<S> arguments(CommandBuilder<S> builder);
}
