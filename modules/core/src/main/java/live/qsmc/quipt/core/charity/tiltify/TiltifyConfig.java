package live.qsmc.quipt.core.charity.tiltify;

import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.config.Config;
import live.qsmc.quipt.core.config.ConfigTemplate;
import live.qsmc.quipt.core.config.ConfigValue;
import org.json.JSONObject;

import java.io.File;

/**
 * Configuration for Tiltify integration.
 * Stores API credentials and cached donation data.
 */
@ConfigTemplate(name = "tiltify", ext = ConfigTemplate.Extension.JSON)
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

}

