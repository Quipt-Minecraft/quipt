package com.quiptmc.fabric;

import com.quiptmc.core.config.ConfigManager;
import com.quiptmc.core.config.ConfigTemplate;
import com.quiptmc.core.config.files.*;
import com.quiptmc.core.config.files.discord.AnnouncementsNestedConfig;
import com.quiptmc.core.config.files.discord.ChannelsNestedConfig;
import com.quiptmc.core.config.files.resource.AuthNestedConfig;
import com.quiptmc.core.config.files.resource.HashesNestedConfig;
import com.quiptmc.core.config.files.web.HealthReportNestedConfig;
import com.quiptmc.core.data.registries.Registry;
import com.quiptmc.core.discord.embed.Embed;
import com.quiptmc.core.server.QuiptServer;
import com.quiptmc.core.utils.TaskScheduler;
import com.quiptmc.discord.Bot;
import com.quiptmc.discord.api.guild.QuiptGuild;
import com.quiptmc.discord.api.guild.channel.QuiptTextChannel;
import com.quiptmc.discord.api.plugins.BotPlugin;
import com.quiptmc.discord.api.plugins.BotPluginLoader;
import com.quiptmc.fabric.api.FabricIntegration;
import com.quiptmc.fabric.api.QuiptEntrypoint;
import com.quiptmc.fabric.listeners.ServerListener;
import com.quiptmc.minecraft.CoreUtils;
import com.quiptmc.minecraft.api.MinecraftMaterial;
import com.quiptmc.minecraft.api.events.EventHandler;
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
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.json.JSONObject;

import java.awt.*;
import java.io.File;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Initializer extends QuiptEntrypoint implements ModInitializer {


    @Override
    public void onInitialize() {
        //Load Quipt data
        run(FabricLoader.getInstance().getModContainer("quipt").get());
        CoreUtils.init(this.integration());
        MinecraftMaterial.init();
        Registry<MinecraftMaterial> materialRegistry = MinecraftMaterial.registry().orElseThrow();


        //Load other Quipt mods
        FabricLoader.getInstance().getEntrypointContainers("quipt", QuiptEntrypoint.class).forEach(container -> container.getEntrypoint().run(container));
    }


    @Override
    public void onInitialize(ModMetadata metadata) {
        //todo register materials?
        ServerListener mainListener = new ServerListener();
        ServerLifecycleEvents.SERVER_STOPPING.register(mainListener);
        ServerLifecycleEvents.SERVER_STARTED.register(mainListener);
        ServerLifecycleEvents.SERVER_STARTING.register(mainListener);
        ServerPlayConnectionEvents.JOIN.register(mainListener);
        ServerPlayConnectionEvents.DISCONNECT.register(mainListener);
        ServerMessageEvents.CHAT_MESSAGE.register(mainListener);
    }

    public static class Quipt extends FabricIntegration {
        private final EventHandler handler;
        private ResourcePackHandler packHandler;
        private QuiptServer server;
        private CallbackHandler callbackHandler;

        public Quipt(String name, ServerLoader<ModMetadata> loader) {
            super(name, loader);
            handler = new EventHandler(this);

        }

        public final EventHandler events() {
            return handler;
        }

        public QuiptServer server() {
            return server;
        }

        @Override
        public void enable() {
            super.enable();

            events().register(new QuiptPlayerListener());

            registerConfigs();
            SessionManager.start(this);
            PlaceholderUtils.registerPlaceholders();
            MessageUtils.start();

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


    }
}
