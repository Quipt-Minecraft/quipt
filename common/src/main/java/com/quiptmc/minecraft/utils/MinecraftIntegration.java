package com.quiptmc.minecraft.utils;

import com.quiptmc.core.QuiptIntegration;
import com.quiptmc.core.annotations.Nullable;
import com.quiptmc.core.config.ConfigManager;
import com.quiptmc.core.config.ConfigTemplate;
import com.quiptmc.core.discord.embed.Embed;
import com.quiptmc.core.server.QuiptServer;
import com.quiptmc.core.utils.TaskScheduler;
import com.quiptmc.discord.Bot;
import com.quiptmc.discord.api.guild.QuiptGuild;
import com.quiptmc.discord.api.guild.channel.QuiptTextChannel;
import com.quiptmc.discord.api.plugins.BotPlugin;
import com.quiptmc.discord.api.plugins.BotPluginLoader;
import com.quiptmc.minecraft.files.*;
import com.quiptmc.minecraft.files.discord.AnnouncementsNestedConfig;
import com.quiptmc.minecraft.files.discord.ChannelsNestedConfig;
import com.quiptmc.minecraft.files.resource.AuthNestedConfig;
import com.quiptmc.minecraft.files.resource.HashesNestedConfig;
import com.quiptmc.minecraft.files.web.HealthReportNestedConfig;
import com.quiptmc.minecraft.listeners.QuiptPlayerListener;
import com.quiptmc.minecraft.utils.chat.MessageUtils;
import com.quiptmc.minecraft.utils.chat.placeholder.PlaceholderUtils;
import com.quiptmc.minecraft.utils.heartbeat.Flutter;
import com.quiptmc.minecraft.utils.heartbeat.HeartbeatUtils;
import com.quiptmc.minecraft.utils.loaders.ServerLoader;
import com.quiptmc.minecraft.utils.sessions.SessionManager;
import com.quiptmc.minecraft.web.CallbackHandler;
import com.quiptmc.minecraft.web.HealthReportHandler;
import com.quiptmc.minecraft.web.ResourcePackHandler;
import org.json.JSONObject;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public abstract class MinecraftIntegration extends QuiptIntegration {

    public final String NAME = "Quipt";
    private final File dataFolder;
    private final ServerLoader<?> loader;

    private ResourcePackHandler packHandler;
    private QuiptServer server;
    private ApiManager apiManager;
    private CallbackHandler callbackHandler;

    public MinecraftIntegration(@Nullable ServerLoader<?> loader) {
        this.loader = loader;
        this.dataFolder = new File("plugins/" + name());
    }

    public void enable() {

        if (!dataFolder.exists()) log("Initializer", "Creating data folder: " + dataFolder.mkdir());
        HeartbeatUtils.init(this);

        events().register(new QuiptPlayerListener());

        registerConfigs();
        SessionManager.start(this);
        PlaceholderUtils.registerPlaceholders();
        MessageUtils.start();
        apiManager = new ApiManager(this);

        ApiConfig apiConfig = ConfigManager.getConfig(this, ApiConfig.class);
        WebConfig webConfig = ConfigManager.getConfig(this, WebConfig.class);
        QuiptServer.ServerConfig serverConfig = new QuiptServer.ServerConfig(QuiptServer.ServerProtocol.HTTP, webConfig.host, webConfig.port);
        DiscordConfig discordConfig = ConfigManager.getConfig(this, DiscordConfig.class);
        ResourceConfig resourceConfig = ConfigManager.getConfig(this, ResourceConfig.class);

        server = new QuiptServer(this, serverConfig);
        if (loader().type().equals(ServerLoader.Type.PAPER)) {
            JSONObject paperProperties = ConfigManager.loadJson(new File("config/paper-global.yml"), ConfigTemplate.Extension.YAML);

            if (!paperProperties.getJSONObject("proxies").getBoolean("proxy-protocol")) {
                callbackHandler = new CallbackHandler(server);
                server.handler().handle("callback", callbackHandler, "callback/*");
            }
        }

        if (!resourceConfig.repo_url.isEmpty()) {
            packHandler = new ResourcePackHandler(server);
            server.handler().handle("resources", packHandler, "resources/*");
            packHandler.setUrl(resourceConfig.repo_url);
        }

        if (discordConfig.enable_bot) launchBot(discordConfig);

        if (webConfig.enable && webConfig.healthreport.enable)
            server.handler().handle("healthreport", new HealthReportHandler(server), "healthreport/*");

        try {
//            LocationUtils.start(this);
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        HeartbeatUtils.heartbeat(this).addFlutter(new Flutter() {
            private final long started = System.currentTimeMillis();
            private long last = 0;

            @Override
            public boolean run() {
                long now = System.currentTimeMillis();
                if (now - last >= TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES)) {
//                    apiManager.runUpdate();
                    last = now;
                }
                return true;
            }
        });


    }

    @Override
    public File dataFolder() {
        return dataFolder;
    }

    @Override
    public String name() {
        return NAME;
    }

    public ServerLoader<?> loader() {
        return loader;
    }

    @Override
    public String version() {
        return "dev";
    }

    @SuppressWarnings("unchecked")
    private void registerConfigs() {
        ResourceConfig resourceConfig = ConfigManager.registerConfig(this, ResourceConfig.class);
        resourceConfig.auth = ConfigManager.getNestedConfig(resourceConfig, AuthNestedConfig.class, "auth");
        resourceConfig.hashes = ConfigManager.getNestedConfig(resourceConfig, HashesNestedConfig.class, "hashes");
        resourceConfig.save();
        ConfigManager.registerConfig(this, JenkinsConfig.class);
        DiscordConfig discordConfig = ConfigManager.registerConfig(this, DiscordConfig.class);
        discordConfig.announcements = ConfigManager.getNestedConfig(discordConfig, AnnouncementsNestedConfig.class, "announcements");
        discordConfig.channels = ConfigManager.getNestedConfig(discordConfig, ChannelsNestedConfig.class, "channels");
        WebConfig webConfig = ConfigManager.registerConfig(this, WebConfig.class);
        webConfig.healthreport = ConfigManager.getNestedConfig(webConfig, HealthReportNestedConfig.class, "healthreport");
        webConfig.save();
        ConfigManager.registerConfig(this, MessagesConfig.class);
        ConfigManager.registerConfig(this, ApiConfig.class);
    }

    private void launchBot(DiscordConfig discordConfig) {
        logger().log("Initializer", "Starting discord bot");
        TaskScheduler.scheduleAsyncTask(() -> asyncBotLaunchThread(discordConfig), 1, TimeUnit.SECONDS);
    }

    private void asyncBotLaunchThread(DiscordConfig discordConfig) {
        Bot.start(discordConfig.json());
        File folder = new File(dataFolder(), "discord_bots");
        if (!folder.exists()) folder.mkdir();
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.getName().endsWith(".jar")) {
                BotPluginLoader loader = new BotPluginLoader();
                BotPlugin botPlugin = loader.registerPlugin(file);
                loader.enablePlugin(botPlugin);
            }
        }
        for (QuiptGuild guild : Bot.qda().getGuilds()) {
            for (QuiptTextChannel channel : guild.getTextChannels()) {
                if (channel.getName().equalsIgnoreCase(discordConfig.channels.server_status) || channel.getId().equalsIgnoreCase(discordConfig.channels.server_status)) {
                    Embed embed = new Embed();
                    embed.title("Server Status");
                    embed.description("Server has started.");
                    embed.color(Color.GREEN.getRGB());
                    channel.sendMessage(embed);
                }
            }
        }
    }

    public ResourcePackHandler packHandler() {
        return packHandler;
    }

    public ApiManager apiManager() {
        return apiManager;
    }

    @Override
    public void destroy() throws IOException {
        super.destroy();
        if (server != null) {
            try {
                server.stop();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


}
