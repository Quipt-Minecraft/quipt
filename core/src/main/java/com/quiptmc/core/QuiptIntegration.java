package com.quiptmc.core;

import com.quiptmc.core.annotations.Nullable;
import com.quiptmc.core.config.ConfigManager;
import com.quiptmc.core.config.files.ApiConfig;
import com.quiptmc.core.logger.QuiptLogger;
import com.quiptmc.core.utils.NetworkUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.UUID;

/**
 * The main interface for the plugin
 */
public abstract class QuiptIntegration {

    private final QuiptLogger logger = new QuiptLogger(this);
    private ApiManager apiManager = null;


    public QuiptIntegration() {

    }

    /**
     * Get the event handler for the plugin
     *
     * @return The event handler
     */

    public final ApiManager api() {
        if (apiManager == null) apiManager = new ApiManager(this);
        return apiManager;
    }


    /**
     * Enable the plugin
     */
    public abstract void enable();

    /**
     * Log a message to the console
     *
     * @param tag     The config that the message is from
     * @param message The message to log
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
        if (dataFolder() != null && dataFolder().exists()) {
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
        private UUID uuid = null;
        private boolean offline = false;

        public ApiManager(QuiptIntegration integration) {
            this.integration = integration;
            this.apiConfig = ConfigManager.registerConfig(integration, ApiConfig.class);
        }

        public ApiResponse api(String url, JSONObject data) throws JSONException {
            if(offline) return new ApiResponse(ApiResponse.RequestResult.NO_ACTION, new JSONObject("{\"error\":\"Offline\"}"));
            JSONObject secrets = new JSONObject();
            secrets.put("secret", apiConfig.secret);
            data.put("secrets", secrets);
            @Nullable String payload = NetworkUtils.post(apiConfig.endpoint + url, data);
            if (payload == null)
                return new ApiResponse(ApiResponse.RequestResult.NO_ACTION, new JSONObject("{\"error\":\"No response\"}"));
            JSONObject raw = new JSONObject(payload);
            return new ApiResponse(raw.optEnum(ApiResponse.RequestResult.class, "result", ApiResponse.RequestResult.NO_ACTION), raw);
        }

        public void initialize() {
            if (apiConfig.id.equals("default")) {
                uuid = UUID.randomUUID();
                boolean exists = true;
                while (exists) {
                    ApiResponse response = api("tokens/check/" + uuid, new JSONObject());
                    if (response.result.equals(ApiResponse.RequestResult.NO_ACTION)) {
                        if (response.raw.has("error")) {
                            if (response.raw.getString("error").equals("No response")) {
                                offline = true;
                                break;
                            } else {
                                exists = false;
                                break;
                            }
                        }
                    }
                    uuid = UUID.randomUUID();
                }

                apiConfig.id = uuid.toString();
                apiConfig.save();
            }
            uuid = UUID.fromString(apiConfig.id);
        }

        public void update() {
        }

        public String uuid() {
            if (uuid == null) initialize();
            return uuid.toString();
        }

        public record ApiResponse(RequestResult result, JSONObject raw) {

            public enum RequestResult {
                SUCCESS,
                FAILURE,
                NO_ACTION
            }

        }

    }
}
