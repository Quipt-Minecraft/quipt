package live.qsmc.quipt.core.charity.tiltify;

import live.qsmc.quipt.core.config.files.TiltifyConfig;
import live.qsmc.quipt.core.utils.net.HttpConfig;
import live.qsmc.quipt.core.utils.net.HttpHeader;
import live.qsmc.quipt.core.utils.net.NetworkUtils;
import org.json.JSONObject;

import java.net.http.HttpResponse;

/**
 * Tiltify API client for fetching campaign/team donation data.
 * Handles OAuth authentication when credentials are configured.
 * Documentation: https://tiltify.com/api
 */
public class TiltifyClient {

    //    private static final String TILTIFY_API_BASE = "https://v5api.tiltify.com";
    private final String apiKey;
    private final TiltifyTokenManager tokenManager;
    private final TiltifyConfig config;

    public TiltifyClient(String apiKey, TiltifyConfig config, TiltifyTokenManager tokenManager) {
        this.apiKey = apiKey;
        this.config = config;
        this.tokenManager = tokenManager;
    }

    /**
     * Gets appropriate HTTP headers for API requests.
     * Includes Bearer token if OAuth is configured and authenticated.
     *
     * @return Array of HTTP headers
     */
    private HttpHeader[] getAuthHeaders() {
        String token = tokenManager.getValidToken();

        if (token != null && !token.isEmpty()) {
            return new HttpHeader[]{
                new HttpHeader("Content-Type", "application/json"),
                new HttpHeader("Authorization", "Bearer " + token)
            };
        } else if (!apiKey.isEmpty()) {
            return new HttpHeader[]{
                new HttpHeader("Content-Type", "application/json"),
                new HttpHeader("Authorization", "Bearer " + apiKey)
            };
        } else {
            return new HttpHeader[]{
                new HttpHeader("Content-Type", "application/json")
            };
        }
    }

    /**
     * Fetches the campaign data for a given campaign ID.
     * This endpoint is public and doesn't require authentication.
     *
     * @param campaignId The Tiltify campaign ID
     * @return JSONObject containing campaign data, including amountRaised
     */
    public JSONObject getCampaignData(String campaignId) {
        try {
            String url = config.tiltify_base_url + "/api/public/team_campaigns/" + campaignId;
            HttpResponse<String> response = NetworkUtils.get(
                HttpConfig.defaults(getAuthHeaders()),
                url
            );

            if (response.statusCode() == 200) {
                JSONObject responseBody = new JSONObject(response.body());
                return responseBody.optJSONObject("data");
            } else {
                logError("Failed to fetch campaign data", response.statusCode(), response.body());
                return null;
            }
        } catch (Exception e) {
            logException("Error fetching campaign data", e);
            return null;
        }
    }

    /**
     * Fetches the user data including their campaign(s).
     * Requires API authentication.
     *
     * @param userId The Tiltify userId
     * @return JSONObject containing user data
     */
    public JSONObject getUserData(String userId) {
        try {
            String url = config.tiltify_base_url + "/api/public/users/" + userId;
            HttpResponse<String> response = NetworkUtils.get(
                HttpConfig.defaults(getAuthHeaders()),
                url
            );

            if (response.statusCode() == 200) {
                JSONObject responseBody = new JSONObject(response.body());
                return responseBody.optJSONObject("data");
            } else {
                logError("Failed to fetch user data", response.statusCode(), response.body());
                return null;
            }
        } catch (Exception e) {
            logException("Error fetching user data", e);
            return null;
        }
    }

    /**
     * Fetches a team by slug/ID.
     * Endpoint: GET /teams/{teamId}
     *
     * @param teamId The team slug (e.g., "guild-rush-2026")
     * @return JSONObject containing team data
     */
    public JSONObject getTeamData(String teamId) {
        try {
            String url = config.tiltify_base_url + "/api/public/teams/" + teamId;
            HttpResponse<String> response = NetworkUtils.get(
                HttpConfig.defaults(getAuthHeaders()),
                url
            );

            if (response.statusCode() == 200) {
                JSONObject responseBody = new JSONObject(response.body());
                return responseBody.optJSONObject("data");
            } else {
                logError("Failed to fetch team data", response.statusCode(), response.body());
                return null;
            }
        } catch (Exception e) {
            logException("Error fetching team data", e);
            return null;
        }
    }

    /**
     * Fetches campaigns for a given team.
     * Endpoint: GET /teams/{teamId}/campaigns
     *
     * @param teamId The team slug
     * @return JSONObject containing campaigns array
     */
    public JSONObject getTeamCampaigns(String teamId) {
        try {
            String url = config.tiltify_base_url + "/api/public/teams/" + teamId + "/team_campaigns";
            HttpResponse<String> response = NetworkUtils.get(
                HttpConfig.defaults(getAuthHeaders()),
                url
            );

            if (response.statusCode() == 200) {
                JSONObject responseBody = new JSONObject(response.body());
                return responseBody;
            } else {
                logError("Failed to fetch team campaigns", response.statusCode(), response.body());
                return null;
            }
        } catch (Exception e) {
            logException("Error fetching team campaigns", e);
            return null;
        }
    }

    private void logError(String message, int statusCode, String body) {
        System.err.println("[Tiltify] " + message + " (Status: " + statusCode + ")");
        System.err.println("[Tiltify] Response: " + body);
    }

    private void logException(String message, Exception e) {
        System.err.println("[Tiltify] " + message + ": " + e.getMessage());
        e.printStackTrace();
    }

    public JSONObject listDonations(String teamCampaignId) {
        try {
            String url = config.tiltify_base_url + "/api/public/team_campaigns/" + teamCampaignId + "/donations";
            HttpResponse<String> response = NetworkUtils.get(
                HttpConfig.defaults(getAuthHeaders()),
                url
            );

            if (response.statusCode() == 200) {
                JSONObject responseBody = new JSONObject(response.body());
                return responseBody;
            } else {
                logError("Failed to fetch team campaigns", response.statusCode(), response.body());
                return null;
            }
        } catch (Exception e) {
            logException("Error fetching team campaigns", e);
            return null;
        }
    }
}

