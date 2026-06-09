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

    private final TiltifyManager manager;
    private final ReentrantReadWriteLock tokenLock = new ReentrantReadWriteLock();

    private String currentToken;
    private long tokenExpiresAt = 0;
    private boolean isTokenValid = false;

    public TiltifyTokenManager(TiltifyManager manager) {
        this.manager = manager;
        loadCachedToken();
    }

    /**
     * Gets a valid OAuth token, refreshing if necessary.
     * Thread-safe operation.
     *
     * @return Valid Bearer token, or null if unable to obtain
     */
    public String getValidToken() {
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
        return refreshToken();
    }

    /**
     * Force refresh of the OAuth token by making a new request.
     *
     * @return New valid token, or null if request fails
     */
    public String refreshToken() {
        tokenLock.writeLock().lock();
        try {
            // Verify credentials are configured
            if (manager.config().getClientId().isEmpty() || manager.config().getClientKey().isEmpty()) {
                logWarning("OAuth credentials not configured (client_id or client_key missing)");
                return null;
            }

            // Build request body
            JSONObject requestBody = new JSONObject();
            requestBody.put("client_id", manager.config().getClientId());
            requestBody.put("client_secret", manager.config().getClientKey());
            requestBody.put("grant_type", "client_credentials");

            System.out.println("[Tiltify] Requesting OAuth token...");

            // Make token request
            HttpResponse<String> response = NetworkUtils.post(
                HttpConfig.defaults(new HttpHeader("Content-Type", "application/json")),
                manager.config().getOAuthUrl(),
                requestBody
            );

            if (response.statusCode() == 200) {
                JSONObject responseBody = new JSONObject(response.body());

                // Extract token data
                String accessToken = responseBody.optString("access_token");
                int expiresIn = responseBody.optInt("expires_in", 3600); // Default 1 hour

                if (accessToken.isEmpty()) {
                    log("No access_token in OAuth response");
                    return null;
                }

                // Store token with expiration
                currentToken = accessToken;
                // Set expiration to 5 minutes before actual expiry (safety margin)
                tokenExpiresAt = System.currentTimeMillis() + (expiresIn - 300) * 1000L;
                isTokenValid = true;

                // Cache token for persistence
                saveToken(accessToken, tokenExpiresAt);

                System.out.println("[Tiltify] OAuth token obtained successfully. Expires in: " + expiresIn + "s");
                return accessToken;
            } else {
                log("Failed to obtain OAuth token", response.statusCode(), response.body());
                isTokenValid = false;
                return null;
            }
        } catch (Exception e) {
            logException("Error obtaining OAuth token", e);
            isTokenValid = false;
            return null;
        } finally {
            tokenLock.writeLock().unlock();
        }
    }

    /**
     * Loads cached token from config if available.
     */
    private void loadCachedToken() {
        try {
            JSONObject tokenData = manager.config().getTokenData();
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
                        System.out.println("[Tiltify] Loaded cached OAuth token");
                    } finally {
                        tokenLock.writeLock().unlock();
                    }
                } else {
                    System.out.println("[Tiltify] Cached token expired, will refresh");
                }
            }
        } catch (Exception e) {
            System.err.println("[Tiltify] Error loading cached token: " + e.getMessage());
        }
    }

    /**
     * Saves token to config for persistence across restarts.
     */
    private void saveToken(String token, long expiresAt) {
        try {
            JSONObject tokenData = new JSONObject();
            tokenData.put("access_token", token);
            tokenData.put("expires_at", expiresAt);
            tokenData.put("obtained_at", System.currentTimeMillis());
            manager.config().setTokenData(tokenData);
        } catch (Exception e) {
            System.err.println("[Tiltify] Error saving token: " + e.getMessage());
        }
    }

    /**
     * Invalidates the current token, forcing a refresh on next request.
     */
    public void invalidateToken() {
        tokenLock.writeLock().lock();
        try {
            currentToken = null;
            tokenExpiresAt = 0;
            isTokenValid = false;
            System.out.println("[Tiltify] OAuth token invalidated");
        } finally {
            tokenLock.writeLock().unlock();
        }
    }

    /**
     * Checks if currently authenticated.
     *
     * @return true if valid token exists and not expired
     */
    public boolean isAuthenticated() {
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
    public long getTimeUntilExpiration() {
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
    public void clearToken() {
        tokenLock.writeLock().lock();
        try {
            currentToken = null;
            tokenExpiresAt = 0;
            isTokenValid = false;
            manager.config().setTokenData(new JSONObject());
            System.out.println("[Tiltify] Token data cleared");
        } finally {
            tokenLock.writeLock().unlock();
        }
    }

    private void log(String message, int statusCode, String body) {
        manager.integration().logger().error("Tiltify", message + " (Status: {})", statusCode);
        manager.integration().logger().error("Tiltify", "Response: {}", body);
    }

    private void log(String message) {
        manager.integration().logger().log("Tiltify", message);
    }

    private void logWarning(String message) {
        manager.integration().logger().warn("Tiltify", "WARNING: " + message);
    }

    private void logException(String message, Exception e) {
        manager.integration().logger().error("Tiltify", message + ": " + e.getMessage(), e);
    }
}

