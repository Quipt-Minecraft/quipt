package live.qsmc.quipt.fabric.commands.executors;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import live.qsmc.quipt.core.data.annotations.Nullable;
import live.qsmc.quipt.core.utils.net.HttpConfig;
import live.qsmc.quipt.core.utils.net.NetworkUtils;
import live.qsmc.quipt.fabric.QuiptMod;
import live.qsmc.quipt.fabric.commands.CommandExecutor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.http.HttpResponse;
import java.util.zip.ZipFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static net.kyori.adventure.text.Component.text;

public class UpdateCommand extends CommandExecutor {

    private final static File DOWNLOAD_DIR = new File("mods");

    private final String repoUrl = "https://repo.qsmc.live/service/rest/";
    private final String version = "v1";
    private long lastUpdate = 0;
    private final Map<String, Map<String, Map<String, Map<String, List<VersionData>>>>> versions = new HashMap<>();
    // /update <repo> <group> <module|name> <version> <artifact>

    public UpdateCommand(QuiptMod mod) {
        super(mod, "update");
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


    @Override
    public LiteralArgumentBuilder<ServerCommandSource> arguments() {
        return literal(name())
            .requires(source -> source.getPermissions().hasPermission(permission(4)))
            .executes(context -> showUsage(context, permission(4)))
            .then(argument("repository", StringArgumentType.word())
                .suggests((context, builder) -> {
                    checkUpdate();
                    String[] repos = versions.keySet().toArray(new String[0]);
                    String input = builder.getInput().substring(builder.getStart());
                    return onlySimilar(repos, input, context, builder);
                })
                .executes(context -> showUsage(context, permission(4)))
                .then(argument("group", StringArgumentType.word())
                    .suggests((context, builder) -> {
                        String repository = StringArgumentType.getString(context, "repository");
                        checkUpdate();
                        String[] groups = versions.get(repository).keySet().toArray(new String[0]);
                        String input = builder.getInput().substring(builder.getStart());
                        return onlySimilar(groups, input, context, builder);
                    })
                    .executes(context -> showUsage(context, permission(4)))
                    .then(argument("name", StringArgumentType.word())
                        .suggests((context, builder) -> {
                            String repository = StringArgumentType.getString(context, "repository");
                            String group = StringArgumentType.getString(context, "group");
                            checkUpdate();
                            String[] options = versions.get(repository).get(group).keySet().toArray(new String[0]);
                            String input = builder.getInput().substring(builder.getStart());
                            return onlySimilar(options, input, context, builder);

                        })
                        .executes(context -> showUsage(context, permission(4)))
                        .then(argument("version", StringArgumentType.word())
                            .suggests((context, builder) -> {
                                String repository = StringArgumentType.getString(context, "repository");
                                String group = StringArgumentType.getString(context, "group");
                                String name = StringArgumentType.getString(context, "name");
                                checkUpdate();
                                String[] options = versions.get(repository).get(group).get(name).keySet().toArray(new String[0]);
                                String input = builder.getInput().substring(builder.getStart());
                                return onlySimilar(options, input, context, builder);
                            })
                            .executes(context -> showUsage(context, permission(4)))
                            .then(argument("artifact", StringArgumentType.word())
                                .suggests((context, builder) -> {
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
                                    String input = builder.getInput().substring(builder.getStart());
                                    return onlySimilar(options, input, context, builder);
                                })
                                .executes(context -> {
                                    ServerCommandSource source = context.getSource();
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
                                            source.sendMessage(text);
//                                                    .append(Component.text(baseName).clickEvent(ClickEvent.openUrl(downloadUrl)))

                                        } catch (Exception e) {
                                            source.sendMessage(text("Update failed: " + e.getMessage(), NamedTextColor.RED));
                                        }
                                    }).start();

                                    return Command.SINGLE_SUCCESS;
                                }))))));
    }

    private String extractBaseName(String filename) {
        String name = filename.endsWith(".jar") ? filename.substring(0, filename.length() - 4) : filename;
        return name.replaceAll("-\\d.*$", "");
    }

    public record VersionData(String artifact, String downloadUrl) {

    }
}

