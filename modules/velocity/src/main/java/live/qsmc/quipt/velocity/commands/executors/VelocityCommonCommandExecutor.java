package live.qsmc.quipt.velocity.commands.executors;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.velocitypowered.api.command.CommandSource;
import live.qsmc.quipt.minecraft.api.MinecraftIntegration;
import live.qsmc.quipt.minecraft.commands.Command;
import live.qsmc.quipt.minecraft.commands.CommonCommand;
import live.qsmc.quipt.velocity.QuiptProxy;
import live.qsmc.quipt.velocity.commands.VelocityCommandExecutor;

public class VelocityCommonCommandExecutor<C extends CommonCommand<CommandSource>> extends VelocityCommandExecutor {

    private final CommonCommand<CommandSource> common;

    public VelocityCommonCommandExecutor(QuiptProxy proxy, Class<C> commonClass, String cmd) {
        super(proxy, cmd);
        try {
            this.common = commonClass.getConstructor(Command.class, MinecraftIntegration.class).newInstance(this, proxy);
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate common command", e);
        }
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> arguments() {
        return common.arguments(this);
    }
}


