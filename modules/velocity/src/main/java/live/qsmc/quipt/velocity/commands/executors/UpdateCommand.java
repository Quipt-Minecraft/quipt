package live.qsmc.quipt.velocity.commands.executors;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.velocitypowered.api.command.CommandSource;
import live.qsmc.quipt.core.data.annotations.Nullable;
import live.qsmc.quipt.core.utils.net.HttpConfig;
import live.qsmc.quipt.core.utils.net.NetworkUtils;
import live.qsmc.quipt.velocity.QuiptProxy;
import live.qsmc.quipt.velocity.commands.CommandExecutor;
import net.kyori.adventure.text.Component;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;

import static net.kyori.adventure.text.Component.text;

public class UpdateCommand extends CommandExecutor {

    private final String repoUrl = "https://repo.qsmc.live/service/rest/";
    private final String version = "v1";
    private long lastUpdate = 0;
    private final Map<String, Map<String, Map<String, List<VersionData>>>> versions = new HashMap<>();

    public UpdateCommand(QuiptProxy proxy) {
        super(proxy, "vupdate");
    }





    public void checkUpdate() {
        checkUpdate(null);
    }

    private void checkUpdate(@Nullable Object continuationToken) {
        long now = System.currentTimeMillis();
        if ((continuationToken != null) || now - lastUpdate > TimeUnit.MILLISECONDS.convert(2, TimeUnit.SECONDS)) {
            lastUpdate = now;
            if (continuationToken == null) versions.clear();
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
                        if (!versions.containsKey(repo))
                            versions.put(repo, new HashMap<>());
                        if (!versions.get(repo).containsKey(name))
                            versions.get(repo).put(name, new HashMap<>());
                        if (!versions.get(repo).get(name).containsKey(version))
                            versions.get(repo).get(name).put(version, new ArrayList<>());
                        if (item.has("assets")) {
                            JSONArray assets = item.getJSONArray("assets");
                            for (Object a : assets) {
                                if (!(a instanceof JSONObject asset)) continue;
                                JSONObject mavenData = asset.getJSONObject("maven2");
                                if (!mavenData.getString("extension").equals("jar")) continue;
                                String artifact = mavenData.getString("artifactId") + "-" + mavenData.getString("version");
                                String downloadUrl = asset.getString("downloadUrl");
                                versions.get(repo).get(name).get(version).add(new VersionData(artifact, downloadUrl));
                            }
                        }
                        versions.get(repo).get(name).get(version);
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


    @Override
    public LiteralArgumentBuilder<CommandSource> arguments() {
        return literal(name())
            .requires(source -> source.hasPermission("quipt.admin.update"))
            .executes(context -> showUsage(context, "quipt.admin.update"))
            .then(argument("repository", StringArgumentType.word())
                .suggests((context, builder) -> {
                    checkUpdate();
                    String[] repos = versions.keySet().toArray(new String[0]);
                    String input = builder.getInput().substring(builder.getStart());
                    return onlySimilar(repos, input, context, builder);
                })
                .executes(context -> showUsage(context, "quipt.admin.update"))
                .then(argument("name", StringArgumentType.word())
                    .suggests((context, builder) -> {
                        String repository = StringArgumentType.getString(context, "repository");
                        checkUpdate();
                        String[] options = versions.get(repository).keySet().toArray(new String[0]);
                        String input = builder.getInput().substring(builder.getStart());
                        return onlySimilar(options, input, context, builder);

                    })
                    .executes(context -> showUsage(context, "quipt.admin.update"))
                    .then(argument("version", StringArgumentType.word())
                        .suggests((context, builder) -> {
                            String repository = StringArgumentType.getString(context, "repository");
                            String name = StringArgumentType.getString(context, "name");
                            checkUpdate();
                            String[] options = versions.get(repository).get(name).keySet().toArray(new String[0]);
                            String input = builder.getInput().substring(builder.getStart());
                            return onlySimilar(options, input, context, builder);
                        })
                        .executes(context -> showUsage(context, "quipt.admin.update"))
                        .then(argument("artifact", StringArgumentType.word())
                            .suggests((context, builder) -> {
                                String repository = StringArgumentType.getString(context, "repository");
                                String name = StringArgumentType.getString(context, "name");
                                String version = StringArgumentType.getString(context, "version");

                                checkUpdate();
                                List<VersionData> versionData = versions.get(repository).get(name).get(version);
                                String[] options = new String[versionData.size()];
                                for (int i = 0; i < versionData.size(); i++) {
                                    options[i] = versionData.get(i).artifact();
                                }
                                String input = builder.getInput().substring(builder.getStart());
                                return onlySimilar(options, input, context, builder);
                            })
                            .executes(context -> {
                                CommandSource source = context.getSource();
                                String repository = StringArgumentType.getString(context, "repository");
                                String name = StringArgumentType.getString(context, "name");
                                String version = StringArgumentType.getString(context, "version");
                                String artifact = StringArgumentType.getString(context, "artifact");
                                String downloadUrl = versions.get(repository).get(name).get(version).stream().filter(v -> v.artifact().equals(artifact)).findFirst().orElseThrow().downloadUrl();

                                // Run async to avoid blocking the server thread
                                new Thread(() -> {
                                    try {
                                        // Download selected artifact to mods folder
                                        File modsDir = new File("mods");
                                        File newJar = new File(modsDir, artifact);
                                        HttpResponse<InputStream> downloadResponse = NetworkUtils.get(HttpConfig.DEFAULTS, downloadUrl, HttpResponse.BodyHandlers.ofInputStream());
                                        NetworkUtils.save(downloadResponse, newJar);

                                        // Delete old version(s) with the same base name
                                        String baseName = extractBaseName(artifact);
                                        File[] oldJars = modsDir.listFiles((dir, fn) ->
                                            fn.endsWith(".jar") && !fn.equals(artifact) && extractBaseName(fn).equals(baseName));
                                        int deleted = 0;
                                        if (oldJars != null) {
                                            for (File oldJar : oldJars) {
                                                if (oldJar.delete()) deleted++;
                                            }
                                        }

                                        int finalDeleted = deleted;
                                        source.sendMessage(text("Downloaded " + artifact + (finalDeleted > 0 ? " and removed " + finalDeleted + " old version(s). Restart to apply." : ". Restart to apply.")));
                                    } catch (Exception e) {
                                        source.sendMessage(text("Update failed: " + e.getMessage()));
                                    }
                                }).start();

                                return Command.SINGLE_SUCCESS;
                            })))));
    }

    private String extractBaseName(String filename) {
        String name = filename.endsWith(".jar") ? filename.substring(0, filename.length() - 4) : filename;
        return name.replaceAll("-\\d.*$", "");
    }

    private record VersionData(String artifact, String downloadUrl) {

    }
}

