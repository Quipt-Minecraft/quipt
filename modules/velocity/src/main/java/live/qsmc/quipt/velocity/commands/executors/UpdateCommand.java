package live.qsmc.quipt.velocity.commands.executors;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.velocitypowered.api.command.CommandSource;
import live.qsmc.quipt.core.utils.net.HttpConfig;
import live.qsmc.quipt.core.utils.net.NetworkUtils;
import live.qsmc.quipt.velocity.QuiptProxy;
import live.qsmc.quipt.velocity.commands.CommandExecutor;
import net.kyori.adventure.text.Component;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.http.HttpResponse;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;

public class UpdateCommand extends CommandExecutor {

    private final String jenkinsUrl = "https://ci.qsmc.live/";

    public UpdateCommand(QuiptProxy proxy) {
        super(proxy, "vupdate");
    }

    private static LiteralArgumentBuilder<CommandSource> literal(String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    private static <T> RequiredArgumentBuilder<CommandSource, T> argument(String name, com.mojang.brigadier.arguments.ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

    @Override
    public LiteralArgumentBuilder<CommandSource> arguments() {
        return literal(name())
            .requires(source -> source.hasPermission("quipt.admin.update"))
            .executes(context -> showUsage(context, "quipt.admin.update"))
            .then(argument("group", StringArgumentType.word())
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
                .executes(context -> showUsage(context, "quipt.admin.update"))
                .then(argument("name", StringArgumentType.word())
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
                    .executes(context -> showUsage(context, "quipt.admin.update"))
                    .then(argument("build", StringArgumentType.word())
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
                        .executes(context -> showUsage(context, "quipt.admin.update"))
                        .then(argument("artifact", StringArgumentType.word())
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
                                CommandSource source = context.getSource();
                                String group = StringArgumentType.getString(context, "group");
                                String name = StringArgumentType.getString(context, "name");
                                String build = StringArgumentType.getString(context, "build");
                                String artifact = StringArgumentType.getString(context, "artifact");

                                if (!source.hasPermission("quipt.admin.update"))
                                    return logError(context, "You do not have permission to use this command.");
                                if (!source.hasPermission("quipt.admin.update." + group))
                                    return logError(context, "You do not have permission to use this command in group " + group);

                                String buildPath = build.equals("latest") ? "lastSuccessfulBuild" : build;

                                proxy().proxy().getScheduler().buildTask(proxy(), () -> {
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
                                            source.sendMessage(Component.text("Artifact '" + artifact + "' not found in build " + build + "."));
                                            return;
                                        }

                                        // Download selected artifact to plugins folder
                                        File pluginsDir = new File("plugins");
                                        File newJar = new File(pluginsDir, artifact);
                                        String downloadUrl = jenkinsUrl + "view/" + group + "/job/" + name + "/" + buildPath + "/artifact/" + relativePath;
                                        HttpResponse<InputStream> downloadResponse = NetworkUtils.get(HttpConfig.DEFAULTS, downloadUrl, HttpResponse.BodyHandlers.ofInputStream());
                                        NetworkUtils.save(downloadResponse, newJar);

                                        // Delete old version(s) with the same base name
                                        String baseName = extractBaseName(artifact);
                                        File[] oldJars = pluginsDir.listFiles((dir, fn) ->
                                            fn.endsWith(".jar") && !fn.equals(artifact) && extractBaseName(fn).equals(baseName));
                                        int deleted = 0;
                                        if (oldJars != null) {
                                            for (File oldJar : oldJars) {
                                                if (oldJar.delete()) deleted++;
                                            }
                                        }

                                        source.sendMessage(Component.text("Downloaded " + artifact + (deleted > 0 ? " and removed " + deleted + " old version(s). Restart to apply." : ". Restart to apply.")));
                                    } catch (Exception e) {
                                        source.sendMessage(Component.text("Update failed: " + e.getMessage()));
                                    }
                                }).schedule();

                                return Command.SINGLE_SUCCESS;
                            })))));
    }

    private String extractBaseName(String filename) {
        String name = filename.endsWith(".jar") ? filename.substring(0, filename.length() - 4) : filename;
        return name.replaceAll("-\\d.*$", "");
    }
}

