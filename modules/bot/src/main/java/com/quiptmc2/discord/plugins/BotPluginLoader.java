package com.quiptmc2.discord.plugins;


import com.quiptmc2.discord.Bot;
import com.quiptmc2.discord.plugins.events.BotEventHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class BotPluginLoader {

    private final Bot bot;
    private final Map<BotPlugin, ClassLoader> plugins = new HashMap<>();
    private BotEventHandler eventHandler;

    public BotPluginLoader(Bot bot) {
        this.bot = bot;
        eventHandler = new BotEventHandler(bot);
        initialize();
        enable();
    }

    public BotEventHandler eventHandler(){
        return eventHandler;
    }

    public Bot bot(){
        return bot;
    }

    public BotPlugin register(File pluginFile) {
        if (pluginFile.getName().endsWith(".jar")) {
            try {
                URLClassLoader classLoader = new URLClassLoader(new URL[]{pluginFile.toURI().toURL()});
                Properties properties = new Properties();

                InputStream inputStream = classLoader.getResourceAsStream("bot.plugin.properties");
                if (inputStream == null)
                    throw new IOException("bot.plugin.properties not found in the JAR file");

                properties.load(inputStream);
                if (!properties.containsKey("main"))
                    throw new IOException("Plugin " + pluginFile.getName() + " does not have a main class listed in bot.plugin.properties");
                if (!properties.containsKey("name"))
                    throw new IOException("Plugin " + pluginFile.getName() + " does not have a name listed in bot.plugin.properties");
                Class<?> loadedClass = classLoader.loadClass(properties.getProperty("main"));
                Object instance = loadedClass.getDeclaredConstructor(BotPluginLoader.class).newInstance(this);
                classLoader.close();
                assert instance instanceof BotPlugin;
                BotPlugin plugin = (BotPlugin) instance;
                plugin.name(properties.getProperty("name"));
                plugin.logger().log("PluginLoader", "Initialized plugin {}.", plugin.name());
                plugins.put(plugin, classLoader);
                return plugin;

            } catch (Exception e) {
                bot.logger().error("PluginLoader", "There was an error registering plugin {}.", pluginFile.getName(), e);
            }

        }
        return null;
    }

    public void enable(BotPlugin plugin) {
        bot.logger().log("PluginLoader", "Enabling plugin {}...", plugin.name());
        plugin.enable();
        bot.logger().log("PluginLoader", "Enabled {}.", plugin.name());
    }

    private void initialize() {
        File plugin_folder = new File(bot.folder(), "bot_plugins");
        if (!plugin_folder.exists())  bot.logger().log("PluginLoader", "Creating plugin folder: {}", plugin_folder.mkdir());
        bot.logger().log("PluginLoader", "Initializing plugins...");
        for (File file : Objects.requireNonNull(plugin_folder.listFiles())) {
            register(file);
        }
        bot.logger().log("PluginLoader", "Initialized {} plugins.", plugins.size());
    }

    public Set<BotPlugin> plugins() {
        return plugins.keySet();
    }

    public BotPlugin get(String name) {
        for (BotPlugin plugin : plugins()) {
            if (plugin.name().equalsIgnoreCase(name)) return plugin;
        }
        return null;
    }

    public void disable() {
        bot.logger().log("PluginLoader", "Disabling plugins...");
        for (Map.Entry<BotPlugin, ClassLoader> entry : plugins.entrySet()) {
            try {
                disable(entry.getKey());

            } catch (IOException e) {
                bot.logger().log("PluginLoader", "There was an error disabling a plugin ({}).", entry.getKey().name());
                bot.logger().log("PluginLoader", getClass().getName(), "disablePlugins", e);
            }
        }
        plugins.clear();
    }

    public void disable(BotPlugin plugin) throws IOException {
        plugin.disable();
        //todo remove any listeners
        String name = plugin.name();
        if (plugins.get(plugin) instanceof URLClassLoader urlClassLoader) urlClassLoader.close();
        bot.logger().log("PluginLoader", "Plugin {} disabled.", name);
    }

    private void enable() {
        for (BotPlugin plugin : plugins()) {
            enable(plugin);
        }
    }

    public void reload() {

        disable();
        initialize();
        enable();
    }
}

