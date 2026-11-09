package live.qsmc.quipt.core.charity.tiltify;

import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.charity.CharityPlatform;
import live.qsmc.quipt.core.charity.ProcessResult;
import live.qsmc.quipt.core.charity.el.ExtraLifeDonation;
import live.qsmc.quipt.core.charity.event.DonationUpdateEvent;
import live.qsmc.quipt.core.utils.net.HttpConfig;
import live.qsmc.quipt.core.utils.net.HttpHeader;
import live.qsmc.quipt.core.utils.net.NetworkUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TiltifyPlatform extends CharityPlatform<TiltifyDonation, TiltifyConfig> {

    private final TiltifyTokenManager tokenManager;
    private final ScheduledExecutorService scheduler;

    private static final long CACHE_DURATION_MS = 5 * 60 * 1000; // 5 minutes

    private long lastUpdateTime = 0;
    private BigDecimal cachedTotalDonations = BigDecimal.ZERO;
    private JSONObject cachedTeamData = null;
    private boolean isUpdating = false;

    public TiltifyPlatform(QuiptIntegration integration) {
        super(integration, "tiltify", "Tiltify", TiltifyDonation.class, TiltifyConfig.class);
        this.tokenManager = new TiltifyTokenManager(this);
        this.scheduler = Executors.newScheduledThreadPool(1, r -> {
            Thread t = new Thread(r, "Tiltify-Update-Thread");
            t.setDaemon(true);
            return t;
        });
        // Load cached data on initialization
        load();

        // Schedule periodic updates every 5 minutes
        scheduler.scheduleAtFixedRate(
                this::update,
                0, // Initial delay of 0 (run immediately)
                config().cache_interval_minutes,
                TimeUnit.MINUTES
        );
    }

    /**
     * Loads cached donation data from config.
     */
    private void load() {
        try {
            JSONObject data = config().cached_data;
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
                log("Loaded cached donation data: $" + cachedTotalDonations);
            }
        } catch (Exception e) {
            error("Error loading cached data: " + e.getMessage());
        }
    }

    /**
     * Manually trigger an update of donation data.
     */
    public void update() {
        if (isUpdating) {
            return;
        }

        isUpdating = true;
        try {
            String teamSlug = config().team_id;
            log("Updating donation data for team: " + teamSlug);

            // Try to fetch team data which includes donation totals
            JSONObject teamData = get(ApiMethod.TEAM_DATA, teamSlug);

            if (teamData != null) {
                cachedTeamData = teamData;

                // Extract amountRaised from team data
                BigDecimal amountRaised = extract(teamData.opt("total_amount_raised"));
                if (amountRaised != null && amountRaised.compareTo(BigDecimal.ZERO) >= 0) {
                    if (!Objects.equals(cachedTotalDonations, amountRaised)) {
                        double originalTotal = cachedTotalDonations.doubleValue();
                        cachedTotalDonations = amountRaised;
                        save();
                        log("Successfully updated. Total donations: $" + cachedTotalDonations);
                        detect();

                    } else
                        log("Donation amount is the same: $" + cachedTotalDonations);
                    lastUpdateTime = System.currentTimeMillis();

                } else {
                    log("Amount raised is not valid: " + amountRaised);
                }
            } else {
                log("Failed to fetch team data, using cached value: $" + cachedTotalDonations);
            }
        } catch (Exception e) {
            error(e,"Error during donation data update: " + e.getMessage());
        } finally {
            isUpdating = false;
        }
    }

    /**
     * Saves current donation data to config.
     */
    private void save() {
        JSONObject data = new JSONObject();
        data.put("totalDonations", cachedTotalDonations.toPlainString());
        data.put("lastUpdateTime", lastUpdateTime);
        if (cachedTeamData != null) {
            data.put("teamData", cachedTeamData);
        }
        config().set("cached_data", data);
    }

    private void detect() {
        JSONObject data = get(ApiMethod.TEAM_CAMPAIGNS, config().team_id);
        JSONArray campaigns = data.getJSONArray("data");
        List<ProcessResult<TiltifyDonation>> results = new ArrayList<>();



        for(int i = 0; i!=campaigns.length(); i++){
            JSONObject campaign = campaigns.getJSONObject(i);
            String id = campaign.getString("id");
            JSONObject response = get(ApiMethod.LIST_DONATIONS, id);
            JSONArray donations = response.getJSONArray("data");
            for(int j = 0; j!=donations.length(); j++){
                JSONObject donation = donations.getJSONObject(j);
                String donationId = donation.getString("id");
                if(!config().cached_donations.has(donationId)){
                    BigDecimal amount = extract(donation.opt("amount"));
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

                        TiltifyDonation tiltifyDonation = this.donation(donation, donation.getString("donationID"));
                        ProcessResult<TiltifyDonation> result = process(tiltifyDonation);
                        results.add(result);
                        log("New donation detected: " + donorName + " donated $" + amount);
                        config().cached_donations.put(donationId, donation);
                        config().save();
                    }
                }

            }
        }

        Quipt.INSTANCE.events().handle(new DonationUpdateEvent<>(this, new DonationUpdateEvent.Data<>(results)));


    }

    /**
     * Extracts a BigDecimal amount from various possible JSON representations.
     *
     * @param value The value to extract (can be String, Number, etc.)
     * @return BigDecimal amount, or null if invalid
     */
    private BigDecimal extract(Object value) {
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
            error(e, "Error extracting amount from: " + value);
            return null;
        }
    }

    private HttpHeader[] headers() {
        String token = tokenManager.get();

        if (token != null && !token.isEmpty()) {
            return new HttpHeader[]{
                    new HttpHeader("Content-Type", "application/json"),
                    new HttpHeader("Authorization", "Bearer " + token)
            };
        } else if (!config().api_key.isEmpty()) {
            return new HttpHeader[]{
                    new HttpHeader("Content-Type", "application/json"),
                    new HttpHeader("Authorization", "Bearer " + config().api_key)
            };
        } else {
            return new HttpHeader[]{
                    new HttpHeader("Content-Type", "application/json")
            };
        }
    }

    public JSONObject get(ApiMethod method, String campaignId) {
        try {
            String url = config().tiltify_base_url + method.url(campaignId);
            HttpResponse<String> response = NetworkUtils.get(
                    HttpConfig.defaults(headers()),
                    url
            );

            if (response.statusCode() == 200) {
                JSONObject responseBody = new JSONObject(response.body());
                return responseBody.optJSONObject("data");
            } else {
                error("Failed to fetch campaign data. %s %i", response.statusCode(), response.body());
                return null;
            }
        } catch (Exception e) {
            error(e, "Error fetching campaign data");
            return null;
        }
    }

    @Override
    public ProcessResult<TiltifyDonation> process(TiltifyDonation donation) {
        return null;
    }

    public enum ApiMethod {
        CAMPAIGN_DATA("/api/public/team_campaigns/%s"),
        USER_DATA("/api/public/users/%s"),
        TEAM_DATA("/api/public/teams/%s"),
        TEAM_CAMPAIGNS("/api/public/teams/%s/team_campaigns"),
        LIST_DONATIONS("/api/public/team_campaigns/%s/donations");

        String url;

        ApiMethod(String url){
            this.url = url;
        }

        public String url(Object... vars){
            return String.format(url, vars);
        }
    }
}