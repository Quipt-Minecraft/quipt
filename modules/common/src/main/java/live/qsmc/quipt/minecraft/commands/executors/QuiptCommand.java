package live.qsmc.quipt.minecraft.commands.executors;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.config.files.QuiptConfig;
import live.qsmc.quipt.core.data.JsonSerializable;
import live.qsmc.quipt.core.data.annotations.Nullable;
import live.qsmc.quipt.core.data.registries.Registries;
import live.qsmc.quipt.core.data.registries.Registry;
import live.qsmc.quipt.core.data.registries.RegistryKey;
import live.qsmc.quipt.core.utils.net.ApiResponse;
import live.qsmc.quipt.core.utils.net.HttpConfig;
import live.qsmc.quipt.core.utils.net.HttpHeaders;
import live.qsmc.quipt.core.utils.net.NetworkUtils;
import live.qsmc.quipt.minecraft.api.MinecraftIntegration;
import live.qsmc.quipt.minecraft.commands.Command;
import live.qsmc.quipt.minecraft.commands.CommandBuilder;
import live.qsmc.quipt.minecraft.commands.CommonCommand;
import live.qsmc.quipt.minecraft.commands.executors.quipt.*;
import live.qsmc.quipt.minecraft.utils.chat.MessageUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipFile;

import static net.kyori.adventure.text.Component.text;

public class QuiptCommand<S> extends CommonCommand<S> {

//    private final Registry<QuiptSubCommand<S>> SUB_COMMANDS;
    private final Map<String, QuiptSubCommand<S>> SUB_COMMANDS = new HashMap<>();
    private final String repoUrl = "https://repo.qsmc.live/service/rest/";
    private final String version = "v1";
    private long lastUpdate = 0;
    private final Map<String, Map<String, Map<String, Map<String, List<VersionData>>>>> versions = new HashMap<>();

    @SuppressWarnings("unchecked")
    public QuiptCommand(Command<S> command, MinecraftIntegration<?,?> integration) {
        super(command, integration);
//        SUB_COMMANDS = Quipt.INSTANCE.registries().register(String.valueOf(integration.identifier("quipt_sub_commands")), () -> null);
        register("list", ListSubCommand.class);
        register("help", HelpSubCommand.class);
        register("dump", DumpSubCommand.class);
        register("account", AccountSubCommand.class);
        register("update", UpdateSubCommand.class);
    }

    public Map<String, QuiptSubCommand<S>> subCommands() {
        return Map.copyOf(SUB_COMMANDS);
    }


    private <C extends QuiptSubCommand<S>> void register(String cmd, Class<C> clazz) {
        try {
            QuiptSubCommand<S> instance = clazz.getDeclaredConstructor(QuiptCommand.class, String.class).newInstance(this, cmd);
            SUB_COMMANDS.put(cmd, instance);
        } catch (Exception e) {
            throw new RuntimeException("Failed to register sub-command: " + cmd, e);
        }
    }

    public LiteralArgumentBuilder<S> arguments(CommandBuilder<S> builder) {
        return builder.literal(command().name())
            .executes(context -> command().showUsage(context, ""))
            .then(builder.literal("list")
                .requires(source -> command().hasPermission(source, ""))
                .executes(new ListSubCommand<>(this, "list")))
            .then(builder.literal("help")
                .executes(context -> command().showUsage(context, ""))
                .then(builder.argument("cmd", StringArgumentType.string())
                    .suggests((context, b) -> command().onlySimilar(SUB_COMMANDS.keySet().toArray(new String[0]), "cmd", context, b))
                    .executes(new HelpSubCommand<>(this, "help"))))
            .then(builder.literal("update")
                .requires(source -> command().hasPermission(source, ""))
                .executes(context -> command().showUsage(context, ""))
                .then(builder.argument("repository", StringArgumentType.word())
                    .suggests((context, suggestions) -> {
                        checkUpdate();
                        String[] repos = versions.keySet().toArray(new String[0]);
                        String input = suggestions.getInput().substring(suggestions.getStart());
                        return command().onlySimilar(repos, input, context, suggestions);
                    })
                    .executes(context -> command().showUsage(context, ""))
                    .then(builder.argument("group", StringArgumentType.word())
                        .suggests((context, suggestions) -> {
                            String repository = StringArgumentType.getString(context, "repository");
                            checkUpdate();
                            String[] groups = versions.get(repository).keySet().toArray(new String[0]);
                            String input = suggestions.getInput().substring(suggestions.getStart());
                            return command().onlySimilar(groups, input, context, suggestions);
                        })
                        .executes(context -> command().showUsage(context, ""))
                        .then(builder.argument("name", StringArgumentType.word())
                            .suggests((context, suggestions) -> {
                                String repository = StringArgumentType.getString(context, "repository");
                                String group = StringArgumentType.getString(context, "group");
                                checkUpdate();
                                String[] options = versions.get(repository).get(group).keySet().toArray(new String[0]);
                                String input = suggestions.getInput().substring(suggestions.getStart());
                                return command().onlySimilar(options, input, context, suggestions);

                            })
                            .executes(context -> command().showUsage(context, ""))
                            .then(builder.argument("version", StringArgumentType.word())
                                .suggests((context, suggestions) -> {
                                    String repository = StringArgumentType.getString(context, "repository");
                                    String group = StringArgumentType.getString(context, "group");
                                    String name = StringArgumentType.getString(context, "name");
                                    checkUpdate();
                                    String[] options = versions.get(repository).get(group).get(name).keySet().toArray(new String[0]);
                                    String input = suggestions.getInput().substring(suggestions.getStart());
                                    return command().onlySimilar(options, input, context, suggestions);
                                })
                                .executes(context -> command().showUsage(context, ""))
                                .then(builder.argument("artifact", StringArgumentType.word())
                                    .suggests((context, suggestions) -> {
                                        String repository = StringArgumentType.getString(context, "repository");
                                        String group = StringArgumentType.getString(context, "group");
                                        String name = StringArgumentType.getString(context, "name");
                                        String version = StringArgumentType.getString(context, "version");

                                        checkUpdate();
                                        List<VersionData> versionData = versions.get(repository).get(group).get(name).get(version);
                                        String[] options = new String[versionData.size()];
                                        for (int i = 0; i < versionData.size(); i++) {
                                            options[i] = versionData.get(i).artifact();
                                        }
                                        String input = suggestions.getInput().substring(suggestions.getStart());
                                        return command().onlySimilar(options, input, context, suggestions);
                                    })
                                    .executes(context -> {
                                        S source = context.getSource();
                                        String repository = StringArgumentType.getString(context, "repository");
                                        String group = StringArgumentType.getString(context, "group");
                                        String[] groupParts = group.split("\\.");
                                        String groupSuffix = groupParts[groupParts.length - 1];
                                        String name = StringArgumentType.getString(context, "name");
                                        String version = StringArgumentType.getString(context, "version");
                                        String artifact = StringArgumentType.getString(context, "artifact");
                                        String downloadUrl = versions.get(repository).get(group).get(name).get(version).stream().filter(v -> v.artifact().equals(artifact)).findFirst().orElseThrow().downloadUrl();

                                        String nameAndVersion = (groupSuffix.equals(name) ? name : groupSuffix + "-" + name) + "-" + version;
                                        // Run async to avoid blocking the server thread
                                        new Thread(() -> {
                                            try {
                                                // Download selected artifact to a temp file first to avoid corrupting
                                                // the existing jar if the download is interrupted mid-stream.
                                                File DOWNLOAD_DIR = integration().addons();
                                                File newJar = new File(DOWNLOAD_DIR, nameAndVersion + ".jar");
                                                File tmpJar = new File(DOWNLOAD_DIR, nameAndVersion + ".jar.tmp");
                                                tmpJar.deleteOnExit(); // clean up on JVM exit if something goes wrong
                                                HttpResponse<byte[]> downloadResponse = NetworkUtils.get(HttpConfig.DEFAULTS, downloadUrl, HttpResponse.BodyHandlers.ofByteArray());
                                                int status = downloadResponse.statusCode();
                                                if (status < 200 || status >= 300) {
                                                    throw new RuntimeException("Server returned HTTP " + status + " for " + downloadUrl);
                                                }
                                                try (FileOutputStream out = new FileOutputStream(tmpJar)) {
                                                    out.write(downloadResponse.body());
                                                }

                                                // Validate the downloaded file is a valid zip/jar before replacing
                                                try (ZipFile zip = new ZipFile(tmpJar)) {
                                                    if (zip.size() == 0) throw new RuntimeException("Downloaded jar is empty");
                                                } catch (Exception e) {
                                                    tmpJar.delete();
                                                    throw new RuntimeException("Downloaded file is not a valid jar: " + e.getMessage(), e);
                                                }

                                                // Atomically replace the target jar
                                                if (newJar.exists()) newJar.delete();
                                                if (!tmpJar.renameTo(newJar)) {
                                                    throw new RuntimeException("Failed to move downloaded jar to " + newJar.getAbsolutePath());
                                                }

                                                // Delete old version(s) with the same base name
                                                String baseName = extractBaseName(nameAndVersion);
                                                File[] oldJars = DOWNLOAD_DIR.listFiles((dir, fn) ->
                                                    fn.endsWith(".jar") && !fn.equals(newJar.getName()) && extractBaseName(fn).equals(baseName));
                                                int deleted = 0;
                                                if (oldJars != null) {
                                                    for (File oldJar : oldJars) {
                                                        if (oldJar.delete()) deleted++;
                                                    }
                                                }

                                                int finalDeleted = deleted;
                                                Component text = text("Downloaded ", NamedTextColor.GREEN)
                                                    .append(text(baseName, NamedTextColor.YELLOW)
                                                        .hoverEvent(HoverEvent.showText(text(newJar.getAbsolutePath()))))
                                                    .append(text( ".", NamedTextColor.GREEN))
                                                    .append(text(finalDeleted > 0 ?
                                                            " Removed " + finalDeleted + " old version(s). "
                                                            : " ",
                                                        NamedTextColor.GREEN))
                                                    .append(text("Restart to apply.", NamedTextColor.GOLD)
                                                        .hoverEvent(HoverEvent.showText(text("Click to run `/restart` command.")))
                                                        .clickEvent(ClickEvent.runCommand("/restart")));
                                                command().sendMessage(source, text);
//                                                    .append(Component.text(baseName).clickEvent(ClickEvent.openUrl(downloadUrl)))

                                            } catch (Exception e) {
                                                command().sendMessage(source, text("Update failed: " + e.getMessage(), NamedTextColor.RED));
                                            }
                                        }).start();

                                        return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                                    })))))))
            .then(builder.literal("account")
                .requires(sender -> command().hasPermission(sender, ""))
                .executes(context -> command().showUsage(context, ""))
                .then(builder.literal("help")
                    .requires(sender -> command().hasPermission(sender, ""))
                    .executes(context -> command().showUsage(context, "")))
                .then(builder.literal("token")
                    .requires(sender -> command().hasPermission(sender, ""))
                    .executes(context -> command().showUsage(context, ""))
                    .then(builder.argument("access_token", StringArgumentType.string())
                        .executes(context -> {
                            try {
                                String access_token = StringArgumentType.getString(context, "access_token");
                                HttpConfig httpConfig = HttpConfig.defaults(HttpHeaders.AUTHORIZATION_BEARER(access_token));
                                String apiUrl = "https://api.qsmc.live/token/validate";
                                HttpResponse<String> responseRaw = NetworkUtils.get(httpConfig, apiUrl);
                                ApiResponse<?> response = new ApiResponse<>(responseRaw);
                                if (response.isFailure()) {
                                    return command().logError(context, "Invalid access token");
                                }
                                QuiptConfig config = Quipt.INSTANCE.configs().config(QuiptConfig.class);
                                config.access_token = access_token;
                                config.save();
                                return command().logSuccess(context, "Access token is valid and has been saved.");
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }

                        })))
                .then(builder.literal("register")
                    .requires(context -> command().hasPermission(context, ""))
                    .executes(context -> command().showUsage(context, ""))
                    .then(builder.argument("username", StringArgumentType.string())
                        .executes(context -> command().showUsage(context, ""))
                        .then(builder.argument("password", StringArgumentType.string())
                            .executes(context -> command().showUsage(context, ""))
                            .then(builder.argument("email", StringArgumentType.greedyString())
                                .executes(context -> {
                                    JSONObject request = new JSONObject();
                                    request.put("username", StringArgumentType.getString(context, "username"));
                                    request.put("password", StringArgumentType.getString(context, "password"));
                                    request.put("email", StringArgumentType.getString(context, "email"));
                                    try {
                                        HttpResponse<String> response = NetworkUtils.post(HttpConfig.defaults(), "https://api.qsmc.live/account/register", request);
                                        JSONObject jsonResponse = new JSONObject(response.body());

                                        if (jsonResponse.has("error")) {
                                            return command().logError(context, "Registration failed: " + jsonResponse.getString("error"));
                                        }

                                        return command().logSuccess(context, MessageUtils.get("quipt.account.register.success"));
                                    } catch (Exception e) {
                                        return command().logError(context, "An error occurred while registering your account: " + e.getMessage());
                                    }
                                }))))))
            .then(builder.literal("dump")
                .requires(sender -> command().hasPermission(sender, ""))
                .executes(context -> command().showUsage(context, ("")))
                .then(builder.literal("registries")
                    .executes(context -> {
                        QuiptConfig config = Quipt.INSTANCE.configs().config(QuiptConfig.class);
                        if (config.access_token == null || config.access_token.isBlank())
                            return command().logError(context, "Registry dump requires an access token to be registered in the base Quipt config.");
                        JSONObject dumpRoot = new JSONObject();
                        for (RegistryKey registryKey : Quipt.INSTANCE.registries().keys()) {
                            Registry<?> registry = Quipt.INSTANCE.registries().get(registryKey);
                            JSONObject dumpRegistry = new JSONObject();
                            for (String entryKey : registry.keys()) {
                                if (registry.get(entryKey).isPresent()) {
                                    JSONObject dumpEntry;
                                    Object object = registry.get(entryKey).get();
                                    if (object instanceof JsonSerializable serializable) {
                                        dumpEntry = serializable.json();
                                    } else {
                                        dumpEntry = new JSONObject().put("value", object);
                                    }
                                    dumpRegistry.put(entryKey, dumpEntry);
                                }
                            }

                            dumpRoot.put(registryKey.key(), dumpRegistry);
                        }
                        command().sendMessage(context.getSource(), uploadData(dumpRoot, Type.REGISTRIES, config));
                        return 1;
                    }))
                .then(builder.literal("config")
                    .executes(context -> {
                        QuiptConfig config = Quipt.INSTANCE.configs().config(QuiptConfig.class);
                        if (config.access_token == null || config.access_token.isBlank())
                            return command().logError(context, "Config dump requires an access token to be registered in the base Quipt config.");
                        JSONObject data = new JSONObject();
                        for (QuiptIntegration integration : Quipt.INSTANCE.integrations()) {
                            JSONObject integrationData = new JSONObject();
                            for (String cid : integration.configs().all()) {
                                integrationData.put(cid, integration.configs().config(cid).json());
                            }
                            data.put(integration.name(), integrationData);
                        }

                        command().sendMessage(context.getSource(), uploadData(data, Type.CONFIG, config));
                        return 1;
                    })));
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
        integration().logger().log("Dump", "Uploaded " + type + " dump to " + shareUrl);
        integration().logger().log("Dump", "Deleting local file: " + (file.delete() ? "success" : "failed"));
        return MessageUtils.get("quipt.dump.success", type.toString(), shareUrl);

    }


    private enum Type {
        REGISTRIES, CONFIG;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    private String extractBaseName(String filename) {
        String name = filename.endsWith(".jar") ? filename.substring(0, filename.length() - 4) : filename;
        return name.replaceAll("-\\d.*$", "");
    }

    public record VersionData(String artifact, String downloadUrl) {

    }

    public void checkUpdate() {
        checkUpdate(null);
    }

    private void checkUpdate(@Nullable Object continuationToken) {
        long now = System.currentTimeMillis();
        if ((continuationToken != null) || now - lastUpdate > TimeUnit.MILLISECONDS.convert(2, TimeUnit.SECONDS)) {
            lastUpdate = now;
//            if (continuationToken == null) versions.clear();
            try {
                HttpResponse<String> rawResponse = NetworkUtils.get(HttpConfig.DEFAULTS, repoUrl + version + "/search" + (continuationToken == null ? "" : "?continuationToken=" + continuationToken));
                try {
                    JSONObject json = new JSONObject(rawResponse.body());
                    JSONArray items = json.getJSONArray("items");
                    for (Object o : items) {
                        if (!(o instanceof JSONObject item)) continue;
                        if (!item.getString("format").equals("maven2")) continue;
                        String repo = item.getString("repository");
                        String name = item.getString("name");
                        String version = item.getString("version");
                        String group = item.getString("group");
                        if (!versions.containsKey(repo))
                            versions.put(repo, new HashMap<>());
                        if (!versions.get(repo).containsKey(group))
                            versions.get(repo).put(group, new HashMap<>());
                        if (!versions.get(repo).get(group).containsKey(name))
                            versions.get(repo).get(group).put(name, new HashMap<>());
                        if (!versions.get(repo).get(group).get(name).containsKey(version))
                            versions.get(repo).get(group).get(name).put(version, new ArrayList<>());
                        if (item.has("assets")) {
                            JSONArray assets = item.getJSONArray("assets");
                            for (Object a : assets) {
                                if (!(a instanceof JSONObject asset)) continue;
                                JSONObject mavenData = asset.getJSONObject("maven2");
                                if (!mavenData.getString("extension").equals("jar")) continue;
                                String artifact = mavenData.getString("artifactId") + "-" + mavenData.getString("version");
                                String downloadUrl = asset.getString("downloadUrl");
                                versions.get(repo).get(group).get(name).get(version).add(new VersionData(artifact, downloadUrl));
                            }
                        }
                    }
                    if (json.has("continuationToken") && json.get("continuationToken") != null) {
                        checkUpdate(json.get("continuationToken"));
                    }
                } catch (Exception e) {
                    System.out.println(rawResponse.body());
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
