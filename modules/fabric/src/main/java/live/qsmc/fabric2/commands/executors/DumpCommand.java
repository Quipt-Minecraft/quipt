package live.qsmc.fabric2.commands.executors;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import live.qsmc.core2.Quipt;
import live.qsmc.core2.QuiptIntegration;
import live.qsmc.core2.config.files.QuiptConfig;
import live.qsmc.core2.data.JsonSerializable;
import live.qsmc.core2.data.registries.RegistryKey;
import live.qsmc.core2.utils.net.HttpConfig;
import live.qsmc.core2.utils.net.HttpHeaders;
import live.qsmc.core2.utils.net.NetworkUtils;
import live.qsmc.fabric2.QuiptMod;
import live.qsmc.fabric2.commands.CommandExecutor;
import live.qsmc.minecraft2.utils.chat.MessageUtils;
import net.kyori.adventure.text.Component;
import net.minecraft.command.permission.Permission;
import net.minecraft.command.permission.PermissionLevel;
import net.minecraft.server.command.ServerCommandSource;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class DumpCommand extends CommandExecutor {
    public DumpCommand(QuiptMod plugin) {
        super(plugin, "dump");
    }

    private final Permission permission = new Permission.Level(PermissionLevel.ADMINS);

    @Override
    public LiteralArgumentBuilder<ServerCommandSource> arguments() {
        return builder()
            .requires(sender -> sender.getPermissions().hasPermission(permission))
            .executes(context -> showUsage(context, permission))
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

                    context.getSource().sendMessage(uploadData(core, Type.REGISTRIES, config));
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

                    context.getSource().sendMessage(uploadData(data, Type.CONFIG, config));
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
        mod().integration().logger().log("Dump", "Uploaded " + type + " dump to " + shareUrl);
        mod().integration().logger().log("Dump", "Deleting local file: " + (file.delete() ? "success" : "failed"));
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
