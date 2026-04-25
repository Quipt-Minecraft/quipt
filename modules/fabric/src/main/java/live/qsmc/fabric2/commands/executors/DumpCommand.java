package live.qsmc.fabric2.commands.executors;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import live.qsmc.core2.Quipt;
import live.qsmc.core2.QuiptIntegration;
import live.qsmc.core2.utils.net.HttpConfig;
import live.qsmc.core2.utils.net.HttpHeaders;
import live.qsmc.core2.utils.net.NetworkUtils;
import live.qsmc.fabric2.QuiptMod;
import live.qsmc.fabric2.commands.CommandExecutor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minecraft.server.command.ServerCommandSource;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static net.kyori.adventure.text.Component.text;

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
                    File file = new File("temp-" + System.currentTimeMillis() + ".json");
                    try {
                        Files.writeString(file.toPath(), data.toString(4), StandardOpenOption.CREATE_NEW);
                        NetworkUtils.upload(HttpConfig.defaults(HttpHeaders.AUTHORIZATION_BEARER("abc123")), "https://api.qsmc.live/files/upload", file);
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    String shareUrl = "https://api.qsmc.live/files/download/" + file.getName();
                    Component output = text("Config dump uploaded to: ")
                        .append(text(shareUrl)
                            .clickEvent(ClickEvent.openUrl(shareUrl)))
                        .append(text("."));
                    context.getSource().sendMessage(output);
                    file.delete();
                    return 1;
                }));
    }
}
