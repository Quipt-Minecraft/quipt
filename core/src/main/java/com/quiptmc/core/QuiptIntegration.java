package com.quiptmc.core;

import com.quiptmc.core.annotations.Nullable;
import com.quiptmc.core.config.ConfigManager;
import com.quiptmc.core.config.files.ApiConfig;
import com.quiptmc.core.heartbeat.Flutter;
import com.quiptmc.core.heartbeat.runnable.Heartbeat;
import com.quiptmc.core.logger.QuiptLogger;
import com.quiptmc.core.utils.net.NetworkUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * The main integration interface for the Quipt plugin system.
 * This abstract class provides core functionality for plugin management,
 * logging, and API communication.
 */
public abstract class QuiptIntegration {

    /**
     * Logger instance for this integration
     */
    private final QuiptLogger logger = new QuiptLogger(this);

    /**
     * API manager instance for handling API communications
     */
    private ApiManager apiManager = null;


    private final Shutdown shutdown = new Shutdown(this);

    /**
     * Creates a new QuiptIntegration instance
     */
    public QuiptIntegration() {
    }

    /**
     * Gets or creates the API manager instance for this integration.
     * The API manager handles all API-related communications.
     *
     * @return The API manager instance
     */
    public final ApiManager api() {
        if (apiManager == null) apiManager = new ApiManager(this);
        return apiManager;
    }

    /**
     * Called when the plugin is enabled.
     * Must be implemented by concrete classes to define initialization behavior.
     */
    public abstract void enable();

    /**
     * Logs a message to the console with a specific tag.
     *
     * @param tag     The configuration section or component that the message is from
     * @param message The message to log
     */
    public void log(String tag, String message) {
        this.logger.log("[%s] %s".formatted(tag, message));
    }

    /**
     * Gets the plugin's data folder.
     * Must be implemented by concrete classes to specify where plugin data should be stored.
     *
     * @return The data folder for this plugin
     */
    public abstract File dataFolder();

    /**
     * Gets the plugin's name.
     * Must be implemented by concrete classes to provide the plugin identifier.
     *
     * @return The name of the plugin
     */
    public abstract String name();

    /**
     * Destroys the plugin instance and removes all associated data.
     * This method will delete the plugin's data folder and all its contents.
     *
     * @throws IOException If an error occurs while deleting the data folder
     */
    public void destroy() throws Exception {
        if (dataFolder() != null && dataFolder().exists()) {
            try(Stream<Path> stream = Files.walk(dataFolder().toPath())) {
                stream.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(file -> {
                    log("%s".formatted(name()), "Deleting file: " + file.getAbsolutePath());
                    try {
                        Files.delete(file.toPath());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            } catch (IOException e) {
                log(name(), "Failed to delete empty directories: " + e.getMessage());
            }
            log(name(), "Folder deleted");
        }
    }

    public Heartbeat.FlutterTask shutdown(Flutter task){
        return shutdown.flutter(task);
    }

    public void shutdown(){
        shutdown.run();
    }

    /**
     * Gets the logger instance for this integration.
     *
     * @return The QuiptLogger instance
     */
    public QuiptLogger logger() {
        return logger;
    }

    /**
     * Gets the plugin's version.
     * Must be implemented by concrete classes to provide version information.
     *
     * @return The version string of the plugin
     */
    public abstract String version();



    private static class Shutdown extends Heartbeat {

        public Shutdown(QuiptIntegration plugin) {
            super(plugin);
        }

        @Override
        public void run() {
            for (FlutterTask task : queue())
                flutters().put(task.id(), task);
            queue().clear();
            for (int id : disposal())
                remove(id);
            disposal().clear();

            for (Map.Entry<Integer, FlutterTask> entry : flutters().entrySet()) {
                if (!entry.getValue().flutter().run()) {
                    disposal().add(entry.getKey());
                    plugin().log("Shutdown Flutter " + entry.getKey(), "There was an error during this flutter. Removing from heartbeat.");
                }
            }
        }
    }

    /**
     * Inner class that manages API communications for the integration.
     * Handles authentication, API requests, and maintains connection state.
     */
    public static class ApiManager {

        private final ApiConfig apiConfig;
        private UUID uuid = null;
        private boolean offline = false;

        /**
         * Creates a new ApiManager instance.
         *
         * @param integration The parent QuiptIntegration instance
         */
        public ApiManager(QuiptIntegration integration) {
            this.apiConfig = ConfigManager.registerConfig(integration, ApiConfig.class);
        }

        /**
         * Sends an API request to the specified endpoint.
         *
         * @param url  The API endpoint URL
         * @param data The JSON data to send with the request
         * @return An ApiResponse containing the result and raw response data
         * @throws JSONException If there's an error processing JSON data
         */
        public ApiResponse api(String url, JSONObject data) throws JSONException {
            if (offline)
                return new ApiResponse(ApiResponse.RequestResult.NO_ACTION, new JSONObject("{\"error\":\"Offline\"}"));
            JSONObject secrets = new JSONObject();
            secrets.put("secret", apiConfig.secret);
            data.put("secrets", secrets);
            @Nullable HttpResponse<String> payload = NetworkUtils.post(NetworkUtils.DEFAULT, apiConfig.endpoint + url, data);
            if (payload == null)
                return new ApiResponse(ApiResponse.RequestResult.NO_ACTION, new JSONObject("{\"error\":\"No response\"}"));
            JSONObject raw = new JSONObject(payload.body());
            return new ApiResponse(raw.optEnum(ApiResponse.RequestResult.class, "result", ApiResponse.RequestResult.NO_ACTION), raw);
        }

        /**
         * Initializes the API manager by generating or validating the UUID.
         * Sets the offline status if the API is unreachable.
         */
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
                            } else {
                                exists = false;
                            }
                            break;
                        }
                    }
                    uuid = UUID.randomUUID();
                }

                apiConfig.id = uuid.toString();
                apiConfig.save();
            }
            uuid = UUID.fromString(apiConfig.id);
        }

        /**
         * Updates the API manager state.
         * Currently empty implementation.
         */
        public void update() {
        }

        /**
         * Gets the UUID for this API manager instance.
         * Initializes the manager if not already initialized.
         *
         * @return The UUID as a string
         */
        public String uuid() {
            if (uuid == null) initialize();
            return uuid.toString();
        }

        /**
         * Record class representing an API response.
         * Contains the request result and raw response data.
         */
        public record ApiResponse(RequestResult result, JSONObject raw) {

            /**
             * Enum representing possible API request results
             */
            public enum RequestResult {
                SUCCESS,
                FAILURE,
                NO_ACTION
            }
        }
    }
}