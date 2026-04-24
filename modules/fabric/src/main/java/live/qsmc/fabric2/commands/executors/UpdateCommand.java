package live.qsmc.fabric2.commands.executors;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import live.qsmc.core2.utils.net.HttpConfig;
import live.qsmc.core2.utils.net.NetworkUtils;
import live.qsmc.fabric2.QuiptMod;
import live.qsmc.fabric2.commands.CommandExecutor;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.http.HttpResponse;

public class UpdateCommand extends CommandExecutor {

    private final String jenkinsUrl = "https://ci.qsmc.live/";

    public UpdateCommand(QuiptMod mod) {
        super(mod, "update");
    }

    @Override
    public LiteralArgumentBuilder<ServerCommandSource> arguments() {
        return CommandManager.literal(name())
//            .requires(source -> source.(2))
            .executes(context -> showUsage(context, ""))
            .then(CommandManager.argument("group", StringArgumentType.word())
                .suggests((context, builder) -> {
                    HttpResponse<String> response = null;
                    try {
                        response = NetworkUtils.get(HttpConfig.DEFAULTS, jenkinsUrl + "api/json?tree=views[name]");
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    JSONObject json = new JSONObject(response.body());
                    for (Object view : json.getJSONArray("views")) {
                        builder.suggest(((JSONObject) view).getString("name"));
                    }
                    return builder.buildFuture();
                })
                .executes(context -> showUsage(context, ""))
                .then(CommandManager.argument("name", StringArgumentType.word())
                    .suggests((context, builder) -> {
                        String group = StringArgumentType.getString(context, "group");
                        HttpResponse<String> response = null;
                        try {
                            response = NetworkUtils.get(HttpConfig.DEFAULTS, jenkinsUrl + "view/" + group + "/api/json?tree=jobs[name]");
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        JSONObject json = new JSONObject(response.body());
                        for (Object job : json.getJSONArray("jobs")) {
                            builder.suggest(((JSONObject) job).getString("name"));
                        }
                        return builder.buildFuture();
                    })
                    .executes(context -> showUsage(context, ""))
                    .then(CommandManager.argument("build", StringArgumentType.word())
                        .suggests((context, builder) -> {
                            String group = StringArgumentType.getString(context, "group");
                            String name = StringArgumentType.getString(context, "name");
                            HttpResponse<String> response = null;
                            try {
                                response = NetworkUtils.get(HttpConfig.DEFAULTS, jenkinsUrl + "view/" + group + "/job/" + name + "/api/json?tree=builds[number]");
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                            JSONObject json = new JSONObject(response.body());
                            for (Object build : json.getJSONArray("builds")) {
                                builder.suggest(((JSONObject) build).getInt("number"));
                            }
                            builder.suggest("latest");
                            return builder.buildFuture();
                        })
                        .executes(context -> showUsage(context, ""))
                        .then(CommandManager.argument("artifact", StringArgumentType.word())
                            .suggests((context, builder) -> {
                                String group = StringArgumentType.getString(context, "group");
                                String name = StringArgumentType.getString(context, "name");
                                String build = StringArgumentType.getString(context, "build");
                                String buildPath = build.equals("latest") ? "lastSuccessfulBuild" : build;
                                HttpResponse<String> response = null;
                                try {
                                    response = NetworkUtils.get(HttpConfig.DEFAULTS,
                                        jenkinsUrl + "view/" + group + "/job/" + name + "/" + buildPath + "/api/json?tree=artifacts[fileName]");
                                } catch (FileNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                                JSONObject json = new JSONObject(response.body());
                                for (Object artifact : json.getJSONArray("artifacts")) {
                                    builder.suggest(((JSONObject) artifact).getString("fileName"));
                                }
                                return builder.buildFuture();
                            })
                            .executes(context -> {
                                ServerCommandSource source = context.getSource();
                                String group = StringArgumentType.getString(context, "group");
                                String name = StringArgumentType.getString(context, "name");
                                String build = StringArgumentType.getString(context, "build");
                                String artifact = StringArgumentType.getString(context, "artifact");

                                String buildPath = build.equals("latest") ? "lastSuccessfulBuild" : build;

                                // Run async to avoid blocking the server thread
                                new Thread(() -> {
                                    try {
                                        // Get artifacts list and find the relativePath for the selected artifact
                                        HttpResponse<String> artifactsResponse = NetworkUtils.get(HttpConfig.DEFAULTS,
                                            jenkinsUrl + "view/" + group + "/job/" + name + "/" + buildPath + "/api/json?tree=artifacts[fileName,relativePath]");
                                        JSONObject artifactsJson = new JSONObject(artifactsResponse.body());

                                        String relativePath = null;
                                        for (Object a : artifactsJson.getJSONArray("artifacts")) {
                                            JSONObject ao = (JSONObject) a;
                                            if (ao.getString("fileName").equals(artifact)) {
                                                relativePath = ao.getString("relativePath");
                                                break;
                                            }
                                        }

                                        if (relativePath == null) {
                                            source.sendFeedback(() -> Text.literal("Artifact '" + artifact + "' not found in build " + build + "."), false);
                                            return;
                                        }

                                        // Download selected artifact to mods folder
                                        File modsDir = new File("mods");
                                        File newJar = new File(modsDir, artifact);
                                        String downloadUrl = jenkinsUrl + "view/" + group + "/job/" + name + "/" + buildPath + "/artifact/" + relativePath;
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
                                        source.sendFeedback(() -> Text.literal("Downloaded " + artifact + (finalDeleted > 0 ? " and removed " + finalDeleted + " old version(s). Restart to apply." : ". Restart to apply.")), false);
                                    } catch (Exception e) {
                                        source.sendFeedback(() -> Text.literal("Update failed: " + e.getMessage()), false);
                                    }
                                }).start();

                                return Command.SINGLE_SUCCESS;
                            })))));
    }

    private String extractBaseName(String filename) {
        String name = filename.endsWith(".jar") ? filename.substring(0, filename.length() - 4) : filename;
        return name.replaceAll("-\\d.*$", "");
    }
}

