package com.quiptmc.core;

import com.quiptmc.core.events.EventHandler;
import com.quiptmc.core.logger.QuiptLogger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

/**
 * The main interface for the plugin
 */
public abstract class QuiptIntegration {

    private final EventHandler handler;
    private final QuiptLogger logger = new QuiptLogger(this);

    public QuiptIntegration(){
        handler = new EventHandler(this);
    }

    /**
     * Get the event handler for the plugin
     * @return The event handler
     */
    public final EventHandler events(){
        return handler;
    }


    /**
     * Enable the plugin
     */
    public abstract void enable();

    /**
     * Log a message to the console
     *
     * @param tag         The config that the message is from
     * @param message     The message to log
     */
    public void log(String tag, String message) {
        this.logger.log("[%s] %s".formatted(tag, message));
    }

    /**
     * Get the data folder for the plugin
     *
     * @return The data folder
     */
    public abstract File dataFolder();

    /**
     * Get the name of the plugin
     *
     * @return The name of the plugin
     */
    public abstract String name();

    /**
     * Destroy the plugin
     *
     * @throws IOException If an error occurs while deleting the data folder
     */
    public void destroy() throws IOException {
        if(dataFolder() != null && dataFolder().exists()){
            Files.walk(dataFolder().toPath()).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
            log(name(), "Folder deleted");
        }
    }

    /**
     * Get the logger for the plugin
     *
     * @return The logger
     */
    public QuiptLogger logger() {
        return logger;
    }

    /**
     * Get the version of the plugin
     *
     * @return The version of the plugin
     */
    public abstract String version();
}
