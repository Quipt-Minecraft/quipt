package com.quiptmc.minecraft.api;

import com.quiptmc.core.QuiptIntegration;
import com.quiptmc.core.config.ConfigManager;
import com.quiptmc.core.utils.NetworkUtils;
import com.quiptmc.minecraft.files.ApiConfig;
import org.json.JSONException;
import org.json.JSONObject;

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
