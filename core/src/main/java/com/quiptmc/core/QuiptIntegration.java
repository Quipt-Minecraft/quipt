package com.quiptmc.core;

import com.quiptmc.core.config.ConfigManager;
import com.quiptmc.core.config.files.ApiConfig;
import com.quiptmc.core.data.Metadata;
import com.quiptmc.core.data.registries.Registries;
import com.quiptmc.core.data.registries.Registry;
import com.quiptmc.core.events.EventHandler;
import com.quiptmc.core.logger.QuiptLogger;
import com.quiptmc.core.utils.NetworkUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

/**
 * The main interface for the plugin
 */
public abstract class QuiptIntegration {

    private final QuiptLogger logger = new QuiptLogger(this);
    private final ApiManager apiManager = new ApiManager(this);

    public QuiptIntegration(){

    }

    /**
     * Get the event handler for the plugin
     * @return The event handler
     */

    public final ApiManager api(){
        return apiManager;
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

    public class ApiManager {

        private final QuiptIntegration integration;
        private final ApiConfig apiConfig;
        private final long started;

        public ApiManager(QuiptIntegration integration) {
            this.integration = integration;
            this.apiConfig = ConfigManager.getConfig(integration, ApiConfig.class);
            this.started = System.currentTimeMillis();
        }

        public ApiResponse api(String url, JSONObject data) throws JSONException {
            JSONObject raw = new JSONObject(NetworkUtils.post(url, data));
            return new ApiResponse(raw.optEnum(ApiResponse.RequestResult.class, "result", ApiResponse.RequestResult.NO_ACTION), raw);
        }

        public record ApiResponse(RequestResult result, JSONObject raw) {

            public enum RequestResult {
                SUCCESS, FAILURE, NO_ACTION
            }

        }

    }
}
