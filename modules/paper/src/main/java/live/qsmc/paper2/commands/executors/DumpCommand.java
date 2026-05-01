package live.qsmc.paper2.commands.executors;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import live.qsmc.core2.Quipt;
import live.qsmc.core2.QuiptIntegration;
import live.qsmc.core2.config.files.QuiptConfig;
import live.qsmc.core2.data.JsonSerializable;
import live.qsmc.core2.data.registries.RegistryKey;
import live.qsmc.core2.utils.net.HttpConfig;
import live.qsmc.core2.utils.net.HttpHeaders;
import live.qsmc.core2.utils.net.NetworkUtils;
import live.qsmc.minecraft2.utils.chat.MessageUtils;
import live.qsmc.paper2.QuiptPlugin;
import live.qsmc.paper2.commands.CommandExecutor;
import net.kyori.adventure.text.Component;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class DumpCommand extends CommandExecutor {
    public DumpCommand(QuiptPlugin plugin) {
        super(plugin, "dump");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> arguments() {
        return builder()
            .requires(sender -> sender.getSender().hasPermission("quipt.admin.dump"))
            .executes(context -> showUsage(context, "quipt.admin.dump"))
            .then(literal("registries")
                .executes(context -> {
                    QuiptConfig config = Quipt.INSTANCE.configs().config(QuiptConfig.class);
                    if (config.access_token == null || config.access_token.isBlank())
                        return logError(context, "Registry dump requires an access token to be registered in the base Quipt config.");
                    JSONObject core = new JSONObject();
                    for (RegistryKey key : Quipt.INSTANCE.registries().keys()) {
                        JSONArray registryEntries = new JSONArray();
                        for (Object object : Quipt.INSTANCE.registries().get(key).values()) {
                            if (object instanceof JsonSerializable serializable) {
                                registryEntries.put(serializable.json());
                            } else {
                                registryEntries.put(object);
                            }
                        }
                        core.put(key.key(), registryEntries);
                    }

                    context.getSource().getSender().sendMessage(uploadData(core, Type.REGISTRIES, config));
                    return 1;
                }))
            .then(literal("config")
                .executes(context -> {
                    QuiptConfig config = Quipt.INSTANCE.configs().config(QuiptConfig.class);
                    if (config.access_token == null || config.access_token.isBlank())
                        return logError(context, "Config dump requires an access token to be registered in the base Quipt config.");
                    JSONObject data = new JSONObject();
                    for (QuiptIntegration integration : Quipt.INSTANCE.integrations()) {
                        JSONObject integrationData = new JSONObject();
                        for (String cid : integration.configs().all()) {
                            integrationData.put(cid, integration.configs().config(cid).json());
                        }
                        data.put(integration.name(), integrationData);
                    }

                    context.getSource().getSender().sendMessage(uploadData(data, Type.CONFIG, config));
                    return 1;
                }));
    }

    private Component uploadData(JSONObject data, Type type, QuiptConfig config) {
        String path = "/dump/" + type + "/";
        File file = new File(type + "_dump-" + System.currentTimeMillis() + ".json");
        try {
            Files.writeString(file.toPath(), data.toString(4), StandardOpenOption.CREATE_NEW);
            NetworkUtils.upload(HttpConfig.defaults(HttpHeaders.AUTHORIZATION_BEARER(config.access_token)), "https://api.qsmc.live/files/upload?path=" + path, file);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        String shareUrl = "https://api.qsmc.live/files/download" + path + file.getName();
        plugin().integration().logger().log("Dump", "Uploaded " + type + " dump to " + shareUrl);
        plugin().integration().logger().log("Dump", "Deleting local file: " + (file.delete() ? "success" : "failed"));
        return MessageUtils.get("quipt.dump.success", type.toString(), shareUrl);

    }

    private enum Type {
        REGISTRIES, CONFIG;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}
