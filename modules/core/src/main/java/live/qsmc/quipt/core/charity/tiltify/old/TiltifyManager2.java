package live.qsmc.quipt.core.charity.tiltify.old;

import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.charity.CharityDonation;
import live.qsmc.quipt.core.charity.CharityPlatform;
import live.qsmc.quipt.core.charity.ProcessResult;
import live.qsmc.quipt.core.charity.tiltify.TiltifyDonation;
import live.qsmc.quipt.core.config.files.TiltifyConfig;
import live.qsmc.quipt.core.charity.event.DonationUpdateEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Manages Tiltify donation data with caching and periodic updates.
 */
public class TiltifyManager2 /*extends CharityPlatform<TiltifyDonation> */{

    /*
    private static final long CACHE_DURATION_MS = 5 * 60 * 1000; // 5 minutes
    private final QuiptIntegration integration;
    private final TiltifyClient client;
    private final TiltifyConfig config;
    private final TiltifyTokenManager tokenManager;
    private final ScheduledExecutorService scheduler;

    private long lastUpdateTime = 0;
    private BigDecimal cachedTotalDonations = BigDecimal.ZERO;
    private JSONObject cachedTeamData = null;
    private boolean isUpdating = false;

    public TiltifyManager2(QuiptIntegration integration) {
        super(integration, "tiltify", "Tiltify", TiltifyDonation.class);
        this.integration = integration;
        this.config = integration.configs().config(TiltifyConfig.class);
        this.tokenManager = new TiltifyTokenManager(this);
        this.client = new TiltifyClient(config.getApiKey(), config, tokenManager);
        this.scheduler = Executors.newScheduledThreadPool(1, r -> {
            Thread t = new Thread(r, "Tiltify-Update-Thread");
            t.setDaemon(true);
            return t;
        });

        // Load cached data on initialization
        loadCache();

        // Schedule periodic updates every 5 minutes
        scheduler.scheduleAtFixedRate(
            this::update,
            0, // Initial delay of 0 (run immediately)
            config.getCacheIntervalMinutes(),
            TimeUnit.MINUTES
        );
    }

    public TiltifyClient client() {
        return client;
    }

    public TiltifyConfig config() {
        return config;
    }

    public QuiptIntegration integration(){
        return integration;
    }

    /**
     * Manually trigger an update of donation data.
     //
    public void update() {
        if (isUpdating) {
            return;
        }

        isUpdating = true;
        try {
            String teamSlug = config.getTeamId();
            integration.logger().log("Tiltify", "Updating donation data for team: " + teamSlug);

            // Try to fetch team data which includes donation totals
            JSONObject teamData = client.getTeamData(teamSlug);

            if (teamData != null) {
                cachedTeamData = teamData;

                // Extract amountRaised from team data
                BigDecimal amountRaised = extractAmount(teamData.opt("total_amount_raised"));
                if (amountRaised != null && amountRaised.compareTo(BigDecimal.ZERO) >= 0) {
                    if (!Objects.equals(cachedTotalDonations, amountRaised)) {
                        double originalTotal = cachedTotalDonations.doubleValue();
                        cachedTotalDonations = amountRaised;
                        saveCache();
                        integration.logger().log("Tiltify", "Successfully updated. Total donations: $" + cachedTotalDonations);
                        detectNewDonations();

                    } else
                        integration.logger().log("Tiltify", "Donation amount is the same: $" + cachedTotalDonations);
                    lastUpdateTime = System.currentTimeMillis();

                } else {
                    integration.logger().log("Tiltify", "Amount raised is not valid: " + amountRaised);
                }
            } else {
                integration.logger().error("Tiltify", "Failed to fetch team data, using cached value: $" + cachedTotalDonations);
            }
        } catch (Exception e) {
            integration.logger().error("Tiltify", "Error during donation data update: " + e.getMessage(), e);
        } finally {
            isUpdating = false;
        }
    }

    private void detectNewDonations() {
        JSONObject data = client.getTeamCampaigns(config.getTeamId());
        JSONArray campaigns = data.getJSONArray("data");
        for(int i = 0; i!=campaigns.length(); i++){
            JSONObject campaign = campaigns.getJSONObject(i);
            String id = campaign.getString("id");
            JSONObject response = client.listDonations(id);
            JSONArray donations = response.getJSONArray("data");
            for(int j = 0; j!=donations.length(); j++){
                JSONObject donation = donations.getJSONObject(j);
                String donationId = donation.getString("id");
                if(!config.cached_donations.has(donationId)){
                    BigDecimal amount = extractAmount(donation.opt("amount"));
                    if(amount != null && amount.compareTo(BigDecimal.ZERO) > 0){
                        String donorName = donation.optString("donor_name", "Anonymous");
                        String participant = "null";
                        if(donation.has("facts") && donation.get("facts") != JSONObject.NULL){
                            JSONArray facts = donation.getJSONArray("facts");
                            for(int k=0;k!= facts.length();k++){
                                JSONObject fact = facts.getJSONObject(k);
                                if(fact.has("usage_type") && fact.getString("usage_type").equalsIgnoreCase("campaign")){
                                    participant = fact.getString("name");
                                    break;
                                }
                            }
                        } else {
                            participant = "General Donation";
                        }


                        integration.logger().log("Tiltify", "New donation detected: " + donorName + " donated $" + amount);

                        process()

                        Quipt.INSTANCE.events().handle(new DonationUpdateEvent(new DonationUpdateEvent.Data(this, donationData)));
                        config.cached_donations.put(donationId, donation);
                        config.save();;

                    }
                }

            }
        }

    }

    /**
     * Gets the current total donations in cents (as a long).
     * Returns cached value if fresh, otherwise triggers an update.
     *
     * @return Total donations in cents
     //
    public long getTotalDonationsInCents() {
        if (cacheExpired()) {
            update();
        }
        return cachedTotalDonations.longValue();
    }

    /**
     * Gets the current total donations as a BigDecimal (in dollars).
     *
     * @return Total donations in dollars
     //
    public BigDecimal getTotalDonations() {
        if (cacheExpired()) {
            update();
        }
        return cachedTotalDonations.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Gets the current total donations formatted as a string.
     *
     * @return Formatted donation amount (e.g., "$1,234.56")
     //
    public String totalFormatted() {
        BigDecimal amount = getTotalDonations();
        return String.format("$%,.2f", amount);
    }

    /**
     * Gets the full team data object.
     *
     * @return JSONObject containing team data, or null if not available
     //
    public JSONObject teamData() {
        if (cacheExpired()) {
            update();
        }
        return cachedTeamData;
    }

    /**
     * Gets the last time the donation data was updated.
     *
     * @return Instant of last update
     //
    public Instant lastUpdate() {
        return Instant.ofEpochMilli(lastUpdateTime);
    }

    /**
     * Checks if the cached data is still fresh.
     *
     * @return true if cache has expired, false otherwise
     //
    private boolean cacheExpired() {
        return System.currentTimeMillis() - lastUpdateTime > CACHE_DURATION_MS;
    }

    /**
     * Saves current donation data to config.
     //
    private void saveCache() {
        JSONObject data = new JSONObject();
        data.put("totalDonations", cachedTotalDonations.toPlainString());
        data.put("lastUpdateTime", lastUpdateTime);
        if (cachedTeamData != null) {
            data.put("teamData", cachedTeamData);
        }
        config.setCachedData(data);
    }

    /**
     * Loads cached donation data from config.
     //
    private void loadCache() {
        try {
            JSONObject data = config.getCachedData();
            if (data != null && !data.isEmpty()) {
                if (data.has("totalDonations")) {
                    cachedTotalDonations = new BigDecimal(data.getString("totalDonations"));
                }
                if (data.has("lastUpdateTime")) {
                    lastUpdateTime = data.getLong("lastUpdateTime");
                }
                if (data.has("teamData")) {
                    cachedTeamData = data.getJSONObject("teamData");
                }
                integration.logger().log("Tiltify", "Loaded cached donation data: $" + cachedTotalDonations);
            }
        } catch (Exception e) {
            integration.logger().error("Tiltify", "Error loading cached data: " + e.getMessage());
        }
    }

    /**
     * Extracts a BigDecimal amount from various possible JSON representations.
     *
     * @param value The value to extract (can be String, Number, etc.)
     * @return BigDecimal amount, or null if invalid
     //
    private BigDecimal extractAmount(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return switch (value) {
                case Number number -> BigDecimal.valueOf(number.doubleValue());
                case String string -> new BigDecimal(string);
                case JSONObject json -> BigDecimal.valueOf(json.getDouble("value"));
                default -> new BigDecimal(value.toString());
            };
        } catch (Exception e) {
            integration.logger().error("Tiltify", "Error extracting amount from: " + value, e);
            return null;
        }
    }

    /**
     * Shuts down the background update scheduler.
     * Call this on server shutdown.
     //
    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Gets the token manager for OAuth authentication.
     *
     * @return TiltifyTokenManager instance
     //
    public TiltifyTokenManager tokenManager() {
        return tokenManager;
    }

    /**
     * Checks if OAuth is authenticated.
     *
     * @return true if valid token exists
     //
    public boolean isOAuthAuthenticated() {
        return tokenManager.isAuthenticated();
    }

    /**
     * Gets time until OAuth token expires.
     *
     * @return milliseconds until expiration, or 0 if no token
     //
    public long getTokenExpirationTime() {
        return tokenManager.getTimeUntilExpiration();
    }

    @Override
    public ProcessResult<TiltifyDonation> process(TiltifyDonation donation) {
        return null;
    }

    */
}

