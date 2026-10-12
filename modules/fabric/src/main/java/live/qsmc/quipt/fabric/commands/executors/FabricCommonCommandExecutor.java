package live.qsmc.quipt.fabric.commands.executors;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import live.qsmc.quipt.minecraft.commands.Command;
import live.qsmc.quipt.minecraft.commands.CommonCommand;
import live.qsmc.quipt.fabric.QuiptMod;
import live.qsmc.quipt.fabric.commands.FabricCommandExecutor;
import live.qsmc.quipt.minecraft.api.MinecraftIntegration;
import net.minecraft.server.permissions.Permission;
import net.minecraft.commands.CommandSourceStack;

public class FabricCommonCommandExecutor<C extends CommonCommand<CommandSourceStack, Permission>> extends FabricCommandExecutor {

    private final CommonCommand<CommandSourceStack, Permission> common;

    public FabricCommonCommandExecutor(QuiptMod mod, Class<C> commonClass, String cmd) {
        super(mod, cmd);
        try {
            this.common = commonClass.getConstructor(Command.class, MinecraftIntegration.class).newInstance(this, mod.integration());
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate common command", e);
        }
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> arguments() {
        return common.arguments(this);
    }
}
