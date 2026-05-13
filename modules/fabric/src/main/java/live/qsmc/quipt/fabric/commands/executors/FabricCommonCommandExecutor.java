package live.qsmc.quipt.fabric.commands.executors;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import live.qsmc.quipt.minecraft.commands.Command;
import live.qsmc.quipt.minecraft.commands.CommonCommand;
import live.qsmc.quipt.fabric.QuiptMod;
import live.qsmc.quipt.fabric.commands.FabricCommandExecutor;
import live.qsmc.quipt.minecraft.api.MinecraftIntegration;
import net.minecraft.server.command.ServerCommandSource;

public class FabricCommonCommandExecutor<C extends CommonCommand<ServerCommandSource>> extends FabricCommandExecutor {

    private final CommonCommand<ServerCommandSource> common;

    public FabricCommonCommandExecutor(QuiptMod mod, Class<C> commonClass, String cmd) {
        super(mod, cmd);
        try {
            this.common = commonClass.getConstructor(Command.class, MinecraftIntegration.class).newInstance(this, mod.integration());
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate common command", e);
        }
    }

    @Override
    public LiteralArgumentBuilder<ServerCommandSource> arguments() {
        return common.arguments(this);
    }
}
