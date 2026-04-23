package live.qsmc.fabric2.commands.executors;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import live.qsmc.core2.Quipt;
import live.qsmc.core2.QuiptIntegration;
import live.qsmc.fabric2.QuiptMod;
import live.qsmc.fabric2.commands.CommandExecutor;
import net.kyori.adventure.text.Component;
import net.minecraft.server.command.ServerCommandSource;
import org.json.JSONObject;

import java.io.File;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;

public class DumpCommand extends CommandExecutor {
    public DumpCommand(QuiptMod plugin) {
        super(plugin, "dump");
    }

    @Override
    public LiteralArgumentBuilder<ServerCommandSource> arguments() {
        return builder()
//            .requires(sender -> sender.getSender().hasPermission("quipt.admin.dump"))
            .executes(context -> showUsage(context, "quipt.admin.dump"))
            .then(literal("config")
                .executes(context -> {
                    JSONObject data = new JSONObject();
                    JSONObject core = new JSONObject();
                    for(String cid : Quipt.INSTANCE.configs().all()){
                        core.put(cid, Quipt.INSTANCE.configs().config(cid).json());
                    }
                    data.put("core", core);
                    for(QuiptIntegration integration : Quipt.INSTANCE.integrations()){
                        JSONObject integrationData = new JSONObject();
                        for(String cid : integration.configs().all()){
                            integrationData.put(cid, integration.configs().config(cid).json());
                        }
                        data.put(integration.name(), integrationData);
                    }

                    File file = new File(mod().integration().folder(), "dump-" + System.currentTimeMillis() + ".json");
                    context.getSource().sendMessage(Component.text("Dumped config to " + file.getAbsolutePath()));
                    return 1;
                }));
    }
}
