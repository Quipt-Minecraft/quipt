package live.qsmc.quipt.core.config.files;

import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.config.Config;
import live.qsmc.quipt.core.config.ConfigTemplate;
import live.qsmc.quipt.core.config.ConfigValue;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

/**
 * Configuration for Tiltify integration.
 * Stores API credentials and cached donation data.
 */
@ConfigTemplate(name = "tiltify_config", ext = ConfigTemplate.Extension.JSON)
public class TiltifyConfig extends Config {

    @ConfigValue
    public String api_key = "";

    @ConfigValue
    public String team_id = "guild-rush-2026";

    @ConfigValue
    public long cache_interval_minutes = 5;

    @ConfigValue
    public String tiltify_base_url = "https://v5api.tiltify.com";

    @ConfigValue
    public String oauth_url = "https://v5api.tiltify.com/oauth/token";

    @ConfigValue
    public String client_id = "";

    @ConfigValue
    public String client_key = "";

    @ConfigValue
    public JSONObject cached_data = new JSONObject();

    @ConfigValue
    public JSONObject token_data = new JSONObject();

    @ConfigValue
    public JSONObject cached_donations = new JSONObject();

    public TiltifyConfig(File file, String name, ConfigTemplate.Extension extension, QuiptIntegration integration) {
        super(file, name, extension, integration);
    }

    public String getApiKey() {
        return api_key;
    }

    public String getTeamId() {
        return team_id;
    }

    public long getCacheIntervalMinutes() {
        return cache_interval_minutes;
    }

    public String getTiltifyBaseUrl() {
        return tiltify_base_url;
    }

    public String getOAuthUrl() {
        return oauth_url;
    }

    public String getClientId() {
        return client_id;
    }

    public String getClientKey() {
        return client_key;
    }

    public JSONObject getCachedData() {
        return cached_data;
    }

    public void setCachedData(JSONObject data) {
        this.cached_data = data;
        save();
    }

    public JSONObject getTokenData() {
        return token_data;
    }

    public void setTokenData(JSONObject data) {
        this.token_data = data;
        save();
    }
}

