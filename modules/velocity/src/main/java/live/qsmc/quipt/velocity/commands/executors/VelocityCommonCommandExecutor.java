package live.qsmc.quipt.velocity.commands.executors;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.velocitypowered.api.command.CommandSource;
import live.qsmc.quipt.minecraft.api.MinecraftIntegration;
import live.qsmc.quipt.minecraft.commands.Command;
import live.qsmc.quipt.minecraft.commands.CommonCommand;
import live.qsmc.quipt.velocity.QuiptProxy;
import live.qsmc.quipt.velocity.commands.VelocityCommandExecutor;

public class VelocityCommonCommandExecutor<C extends CommonCommand<CommandSource, String>> extends VelocityCommandExecutor {

    private final CommonCommand<CommandSource, String> common;

    public VelocityCommonCommandExecutor(QuiptProxy proxy, Class<C> commonClass, String cmd) {
        super(proxy, cmd);
        try {
            // The CommonCommand constructor expects (Command, MinecraftIntegration).
            // Pass this executor as the Command and the proxy's integration instance
            // (not the proxy itself) as the MinecraftIntegration parameter.
            this.common = commonClass.getConstructor(Command.class, MinecraftIntegration.class)
                    .newInstance(this, proxy.integration());
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate common command", e);
        }
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> arguments() {
        return common.arguments(this);
    }

    public String permission(String id){
        if(permissions().get(id).isEmpty())
            permissions().register(id, id);
        return permissions().get(id).get();
    }

    public String permission(int id){
        return permission(String.valueOf(id));
    }
}


