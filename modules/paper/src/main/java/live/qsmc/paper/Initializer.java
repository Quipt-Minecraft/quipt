package live.qsmc.paper;

import live.qsmc.core.config.ConfigManager;
import live.qsmc.core.config.files.*;
import live.qsmc.core.data.registries.Registry;
import live.qsmc.core.discord.embed.Embed;
import live.qsmc.core.server.QuiptServer;
import live.qsmc.core.utils.TaskScheduler;
import live.qsmc.discord.Bot;
import live.qsmc.discord.api.guild.QuiptGuild;
import live.qsmc.discord.api.guild.channel.QuiptTextChannel;
import live.qsmc.discord.api.plugins.BotPlugin;
import live.qsmc.discord.api.plugins.BotPluginLoader;
import live.qsmc.minecraft.CoreUtils;
import live.qsmc.minecraft.api.MinecraftMaterial;
import live.qsmc.minecraft.api.events.EventHandler;
import live.qsmc.minecraft.listeners.QuiptPlayerListener;
import live.qsmc.minecraft.utils.chat.MessageUtils;
import live.qsmc.minecraft.utils.chat.placeholder.PlaceholderUtils;
import live.qsmc.core.heartbeat.Flutter;
import live.qsmc.core.heartbeat.HeartbeatUtils;
import live.qsmc.minecraft.utils.loaders.ServerLoader;
import live.qsmc.minecraft.utils.sessions.SessionManager2;
import live.qsmc.minecraft.web.CallbackHandler;
import live.qsmc.minecraft.web.ResourcePackHandler;
import live.qsmc.paper.api.PaperIntegration;
import live.qsmc.paper.commands.CommandManager;
import live.qsmc.paper.listeners.EventListener;
import live.qsmc.paper.listeners.PlayerListener;
import live.qsmc.paper.listeners.SessionListener;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.io.File;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Bukkit/Paper plugin bootstrap for Quipt.
 * <p>Registers materials, listeners, commands, and starts platform-specific services.
 * Uses the inner Quipt class to bridge core functionality with the Paper runtime.</p>
 */
public final class Initializer extends JavaPlugin {

    /**
     * Entry point invoked by Paper when the plugin is enabled.
     * Initializes material registry, core integration, commands, and listeners.
     */
    @Override
    public void onEnable() {
        MinecraftMaterial.init();
        Registry<MinecraftMaterial> materialRegistry = MinecraftMaterial.registry().orElseThrow();
        for (Material bukkitMat : Material.values()) {
            if (bukkitMat.isLegacy()) continue;
            materialRegistry.register(bukkitMat.name(), new MinecraftMaterial(bukkitMat.translationKey(), bukkitMat.name(), bukkitMat.getMaxStackSize(), bukkitMat.isBlock(), bukkitMat.isItem(), bukkitMat.isAir()));
        }
        PaperIntegration quipt = new Quipt("Quipt", new ServerLoader<>(ServerLoader.Type.PAPER, this));
        CoreUtils.init(quipt);

        CommandManager.init(quipt);

        new PlayerListener(this);
        new EventListener(this);
        new SessionListener(this);


    }

    /**
     * Invoked by Paper when the plugin is disabled.
     * Ensures all configs are saved gracefully.
     */
    @Override
    public void onDisable() {
        ConfigManager.saveAll();
    }

    /**
     * Paper-specific implementation of the Quipt integration layer.
     * <p>Bridges Quipt core to the Paper runtime: registers configs, listeners,
     * sessions, optional web server and Discord bot, and periodic heartbeats.</p>
     */
    public static class Quipt extends PaperIntegration {

        private final EventHandler handler;
        private ResourcePackHandler packHandler;
        private Optional<QuiptServer> server = Optional.empty();
        private CallbackHandler callbackHandler;

        /**
         * Constructs the Paper integration wrapper.
         * @param name plugin name identifier
         * @param loader server loader used to access the Paper runtime
         */
        public Quipt(String name, ServerLoader<JavaPlugin> loader) {
            super(name, loader);
            handler = new EventHandler(this);

        }

        /**
         * Gets the Paper event handler bound to this integration.
         * @return the event handler
         */
        public final EventHandler events() {
            return handler;
        }

        /**
         * Returns the optional embedded QuiptServer instance if web services are enabled.
         * @return optional server instance
         */
        public Optional<QuiptServer> server() {
            return server;
        }

        /**
         * Called when the integration is enabled by the core.
         * Sets up listeners, configs, sessions, placeholders, messages,
         * web server handlers, and optionally the Discord bot.
         */
        @Override
        public void enable() {
            super.enable();

            events().register(new QuiptPlayerListener());
            registerConfigs();
            SessionManager2.start(this);
            PlaceholderUtils.registerPlaceholders();
            MessageUtils.start();

            ApiConfig apiConfig = ConfigManager.getConfig(this, ApiConfig.class);
            ResourceConfig resourceConfig = ConfigManager.getConfig(this, ResourceConfig.class);

            DiscordConfig discordConfig = ConfigManager.getConfig(this, DiscordConfig.class);

            WebConfig webConfig = ConfigManager.getConfig(this, WebConfig.class);
            QuiptServer.ServerConfig serverConfig = new QuiptServer.ServerConfig(QuiptServer.ServerProtocol.HTTP, webConfig.host, webConfig.port);

            server = Optional.of(new QuiptServer(this, serverConfig));
//                if (webConfig.enable && webConfig.healthreport.enable)
//                    server.get().handler().handle("healthreport", new HealthReportHandler(server.get()), "healthreport/*");

            if (!resourceConfig.repo_url.isEmpty()) {
//                packHandler = new ResourcePackHandler(server.get());
//                server.get().handler().handle("resources", packHandler, "resources/*");
                packHandler.setUrl(resourceConfig.repo_url);
            }

            if (discordConfig.enable_bot) launchBot(discordConfig);
            try {
                server.get().start();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


            HeartbeatUtils.heartbeat(this).flutter(new Flutter() {
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


        /**
         * Registers and saves all plugin configuration files used by the Paper integration.
         */
        private void registerConfigs() {
            ResourceConfig resourceConfig = ConfigManager.registerConfig(this, ResourceConfig.class);
            resourceConfig.save();
            ConfigManager.registerConfig(this, JenkinsConfig.class);
            DiscordConfig discordConfig = ConfigManager.registerConfig(this, DiscordConfig.class);
            WebConfig webConfig = ConfigManager.registerConfig(this, WebConfig.class);
            webConfig.save();
            ConfigManager.registerConfig(this, MessagesConfig.class);
            ConfigManager.registerConfig(this, ApiConfig.class);
        }

        /**
         * Schedules asynchronous startup for the Discord bot using configuration values.
         * @param discordConfig the Discord configuration
         */
        private void launchBot(DiscordConfig discordConfig) {
            logger().log("Initializer", "Starting discord bot");
            TaskScheduler.scheduleAsyncTask(() -> asyncBotLaunchThread(discordConfig), 1, TimeUnit.SECONDS);
        }

        /**
         * Performs the Discord bot startup and optional plugin loading on a background thread.
         * Connects to Discord, loads jar-based bot plugins, and posts a server status embed
         * to the configured channel if available.
         * @param discordConfig the Discord configuration to use
         */
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
                        Embed.Builder embed = Embed.builder();
                        embed.title("Server Status");
                        embed.description("Server has started.");
                        embed.color(Color.GREEN.getRGB());
                        channel.sendMessage(embed.build());
                    }
                }
            }
        }
    }

}
