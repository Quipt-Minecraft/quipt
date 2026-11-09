package live.qsmc.quipt.core.charity.tiltify;

import live.qsmc.quipt.core.utils.net.HttpConfig;
import live.qsmc.quipt.core.utils.net.HttpHeader;
import live.qsmc.quipt.core.utils.net.NetworkUtils;
import org.json.JSONObject;

import java.net.http.HttpResponse;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Manages Tiltify OAuth 2.0 token acquisition and validation.
 * Handles token refresh and expiration automatically.
 */
public class TiltifyTokenManager {

    private final TiltifyPlatform platform;
    private final ReentrantReadWriteLock tokenLock = new ReentrantReadWriteLock();

    private String currentToken;
    private long tokenExpiresAt = 0;
    private boolean isTokenValid = false;

    public TiltifyTokenManager(TiltifyPlatform platform) {
        this.platform = platform;
        load();
    }

    /**
     * Gets a valid OAuth token, refreshing if necessary.
     * Thread-safe operation.
     *
     * @return Valid Bearer token, or null if unable to obtain
     */
    public String get() {
        tokenLock.readLock().lock();
        try {
            // Check if we have a valid cached token
            if (isTokenValid && System.currentTimeMillis() < tokenExpiresAt) {
                return currentToken;
            }
        } finally {
            tokenLock.readLock().unlock();
        }

        // Token invalid or expired, refresh it
        return refresh();
    }

    /**
     * Force refresh of the OAuth token by making a new request.
     *
     * @return New valid token, or null if request fails
     */
    public String refresh() {
        tokenLock.writeLock().lock();
        try {
            // Verify credentials are configured
            if (platform.config().client_id.isEmpty() || platform.config().client_key.isEmpty()) {
                platform.warn("OAuth credentials not configured (client_id or client_key missing)");
                return null;
            }

            // Build request body
            JSONObject requestBody = new JSONObject();
            requestBody.put("client_id", platform.config().client_id);
            requestBody.put("client_secret", platform.config().client_key);
            requestBody.put("grant_type", "client_credentials");
            platform.log("Requesting OAuth token...");


            // Make token request
            HttpResponse<String> response = NetworkUtils.post(
                HttpConfig.defaults(new HttpHeader("Content-Type", "application/json")),
                platform.config().oauth_url,
                requestBody
            );

            if (response.statusCode() == 200) {
                JSONObject responseBody = new JSONObject(response.body());

                // Extract token data
                String accessToken = responseBody.optString("access_token");
                int expiresIn = responseBody.optInt("expires_in", 3600); // Default 1 hour

                if (accessToken.isEmpty()) {
                    platform.log("No access_token in OAuth response");
                    return null;
                }

                // Store token with expiration
                currentToken = accessToken;
                // Set expiration to 5 minutes before actual expiry (safety margin)
                tokenExpiresAt = System.currentTimeMillis() + (expiresIn - 300) * 1000L;
                isTokenValid = true;

                // Cache token for persistence
                save(accessToken, tokenExpiresAt);

                platform.log("OAuth token obtained successfully. Expires in: %ds", expiresIn);
//                System.out.println("[Tiltify] OAuth token obtained successfully. Expires in: " + expiresIn + "s");
                return accessToken;
            } else {
                platform.error("Failed to obtain OAuth token", response.statusCode(), response.body());
                isTokenValid = false;
                return null;
            }
        } catch (Exception e) {
            platform.error(e, "Error obtaining OAuth token");
            isTokenValid = false;
            return null;
        } finally {
            tokenLock.writeLock().unlock();
        }
    }

    /**
     * Loads cached token from config if available.
     */
    private void load() {
        try {
            JSONObject tokenData = platform.config().token_data;
            if (tokenData == null || tokenData.isEmpty()) {
                return;
            }

            if (tokenData.has("access_token") && tokenData.has("expires_at")) {
                String token = tokenData.getString("access_token");
                long expiresAt = tokenData.getLong("expires_at");

                // Only use cached token if not expired
                if (System.currentTimeMillis() < expiresAt) {
                    tokenLock.writeLock().lock();
                    try {
                        currentToken = token;
                        tokenExpiresAt = expiresAt;
                        isTokenValid = true;
                        platform.log("Loaded cached OAuth token");
                    } finally {
                        tokenLock.writeLock().unlock();
                    }
                } else {
                    platform.log("Cached OAuth token expired, will refresh");
                }
            }
        } catch (Exception e) {
            platform.error(e, "Error loading cached token: %s", e.getMessage());
        }
    }

    /**
     * Saves token to config for persistence across restarts.
     */
    private void save(String token, long expiresAt) {
        try {
            JSONObject tokenData = new JSONObject();
            tokenData.put("access_token", token);
            tokenData.put("expires_at", expiresAt);
            tokenData.put("obtained_at", System.currentTimeMillis());
            platform.config().set("token_data", tokenData);
        } catch (Exception e) {
            platform.error(e, "Error saving token: %s", e.getMessage());
        }
    }

    /**
     * Invalidates the current token, forcing a refresh on next request.
     */
    public void invalidate() {
        tokenLock.writeLock().lock();
        try {
            currentToken = null;
            tokenExpiresAt = 0;
            isTokenValid = false;
            platform.log("OAuth token invalidated");
        } finally {
            tokenLock.writeLock().unlock();
        }
    }

    /**
     * Checks if currently authenticated.
     *
     * @return true if valid token exists and not expired
     */
    public boolean authenticated() {
        tokenLock.readLock().lock();
        try {
            return isTokenValid && System.currentTimeMillis() < tokenExpiresAt && currentToken != null;
        } finally {
            tokenLock.readLock().unlock();
        }
    }

    /**
     * Gets time until token expires in milliseconds.
     *
     * @return milliseconds until expiration, or 0 if no token
     */
    public long expiration() {
        tokenLock.readLock().lock();
        try {
            if (!isTokenValid) {
                return 0;
            }
            long remaining = tokenExpiresAt - System.currentTimeMillis();
            return Math.max(0, remaining);
        } finally {
            tokenLock.readLock().unlock();
        }
    }

    /**
     * Clears all cached token data.
     */
    public void clear() {
        tokenLock.writeLock().lock();
        try {
            currentToken = null;
            tokenExpiresAt = 0;
            isTokenValid = false;
            platform.config().set("token_data", new JSONObject());
            platform.log("Token data cleared");
        } finally {
            tokenLock.writeLock().unlock();
        }
    }
}

