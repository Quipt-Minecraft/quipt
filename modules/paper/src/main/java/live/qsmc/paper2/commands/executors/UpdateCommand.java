package live.qsmc.paper2.commands.executors;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import live.qsmc.core2.utils.net.HttpConfig;
import live.qsmc.core2.utils.net.NetworkUtils;
import live.qsmc.paper2.QuiptPlugin;
import live.qsmc.paper2.commands.CommandExecutor;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.net.http.HttpResponse;

import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;

public class UpdateCommand extends CommandExecutor {

    private final String jenkinsUrl = "https://ci.qsmc.live/";

    public UpdateCommand(QuiptPlugin plugin) {
        super(plugin, "update");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> arguments() {
        return literal(name())
            .executes(context -> showUsage(context, "quipt.admin.update"))
            .then(argument("group", StringArgumentType.word())
                .suggests((context, builder) -> {
                    //list jenkins views
                    HttpResponse<String> response = NetworkUtils.get(HttpConfig.DEFAULTS, jenkinsUrl + "api/json?tree=views[name]");
                    JSONObject json = new JSONObject(response.body());
                    for (Object view : json.getJSONArray("views")) {
                        builder.suggest(((JSONObject) view).getString("name"));
                    }
                    return builder.buildFuture();
                })
                .executes(context -> showUsage(context, "quipt.admin.update"))
                .then(argument("name", StringArgumentType.word())
                    .suggests((context, builder) -> {
                        //list jenkins jobs in view
                        String group = StringArgumentType.getString(context, "group");
                        HttpResponse<String> response = NetworkUtils.get(HttpConfig.DEFAULTS, jenkinsUrl + "view/" + group + "/api/json?tree=jobs[name]");
                        JSONObject json = new JSONObject(response.body());
                        for (Object job : json.getJSONArray("jobs")) {
                            builder.suggest(((JSONObject) job).getString("name"));
                        }
                        return builder.buildFuture();
                    })
                    .executes(context -> showUsage(context, "quipt.admin.update"))
                    .then(argument("build", StringArgumentType.word())
                        .suggests(((context, builder) -> {
                            //list jenkins builds in job
                            String group = StringArgumentType.getString(context, "group");
                            String name = StringArgumentType.getString(context, "name");
                            HttpResponse<String> response = NetworkUtils.get(HttpConfig.DEFAULTS, jenkinsUrl + "view/" + group + "/job/" + name + "/api/json?tree=builds[number]");
                            JSONObject json = new JSONObject(response.body());
                            for (Object build : json.getJSONArray("builds")) {
                                builder.suggest(((JSONObject) build).getInt("number"));
                            }
                            builder.suggest("latest");
                            return builder.buildFuture();
                        }))
                        .executes(context -> showUsage(context, "quipt.admin.update"))
                        .then(argument("artifact", StringArgumentType.word())
                            .suggests((context, builder) -> {
                                //list artifacts for this build
                                String group = StringArgumentType.getString(context, "group");
                                String name = StringArgumentType.getString(context, "name");
                                String build = StringArgumentType.getString(context, "build");
                                String buildPath = build.equals("latest") ? "lastSuccessfulBuild" : build;
                                HttpResponse<String> response = NetworkUtils.get(HttpConfig.DEFAULTS,
                                    jenkinsUrl + "view/" + group + "/job/" + name + "/" + buildPath + "/api/json?tree=artifacts[fileName]");
                                JSONObject json = new JSONObject(response.body());
                                for (Object artifact : json.getJSONArray("artifacts")) {
                                    builder.suggest(((JSONObject) artifact).getString("fileName"));
                                }
                                return builder.buildFuture();
                            })
                            .executes(context -> {
                                CommandSender sender = context.getSource().getSender();
                                String group = StringArgumentType.getString(context, "group");
                                String name = StringArgumentType.getString(context, "name");
                                String build = StringArgumentType.getString(context, "build");
                                String artifact = StringArgumentType.getString(context, "artifact");

                                if (!sender.hasPermission("quipt.admin.update"))
                                    return logError(context, "You do not have permission to use this command.");
                                if (!sender.hasPermission("quipt.admin.update." + group))
                                    return logError(context, "You do not have permission to use this command in group " + group);

                                String buildPath = build.equals("latest") ? "lastSuccessfulBuild" : build;
                                plugin().getServer().getScheduler().runTaskAsynchronously(plugin(), () -> {
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
                                            sender.sendMessage("Artifact '" + artifact + "' not found in build " + build + ".");
                                            return;
                                        }

                                        // Download selected artifact to plugins folder
                                        File pluginsDir = plugin().getDataFolder().getParentFile();
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

                                        sender.sendMessage("Downloaded " + artifact + (deleted > 0 ? " and removed " + deleted + " old version(s). Restart to apply." : ". Restart to apply."));
                                    } catch (Exception e) {
                                        sender.sendMessage("Update failed: " + e.getMessage());
                                    }
                                });

                                return Command.SINGLE_SUCCESS;
                            })))));

    }

    private String extractBaseName(String filename) {
        String name = filename.endsWith(".jar") ? filename.substring(0, filename.length() - 4) : filename;
        // Strip from the first segment that starts with a digit (version number)
        return name.replaceAll("-\\d.*$", "");
    }
}
