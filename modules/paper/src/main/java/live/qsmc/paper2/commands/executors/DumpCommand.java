package live.qsmc.paper2.commands.executors;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import live.qsmc.core2.Quipt;
import live.qsmc.core2.QuiptIntegration;
import live.qsmc.paper2.QuiptPlugin;
import live.qsmc.paper2.commands.CommandExecutor;
import org.json.JSONObject;

import java.io.File;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;

public class DumpCommand extends CommandExecutor {
    public DumpCommand(QuiptPlugin plugin) {
        super(plugin, "dump");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> arguments() {
        return builder()
            .requires(sender -> sender.getSender().hasPermission("quipt.admin.dump"))
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

                    File file = new File(plugin().getDataFolder(), "dump-" + System.currentTimeMillis() + ".json");
                    context.getSource().getSender().sendMessage("Dumped config to " + file.getAbsolutePath());
                    return 1;
                }));
    }
}
