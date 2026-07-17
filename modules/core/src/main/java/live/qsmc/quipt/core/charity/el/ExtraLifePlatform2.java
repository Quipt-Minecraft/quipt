package live.qsmc.quipt.core.charity.el;


import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.charity.CharityPlatform;
import live.qsmc.quipt.core.config.files.ExtraLifeConfig;
import live.qsmc.quipt.core.heartbeat.flutter.Flutter;
import live.qsmc.quipt.core.utils.net.HttpConfig;
import live.qsmc.quipt.core.utils.net.HttpHeaders;
import live.qsmc.quipt.core.utils.net.NetworkUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.FileNotFoundException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtraLifePlatform2 /*extends CharityPlatform<ExtraLifeDonationData, ExtraLifeDonation> implements Flutter */{

//    public static final int MAX_LIMIT = 100;
//
//    private final HttpConfig GET;
//    private long LAST_HEARTBEAT = 0;
//    private int offset = 0;
//
//    public ExtraLifePlatform2(QuiptIntegration integration) {
//        super(integration, "lastlife", "Last Life");
//        ExtraLifeConfig config = Quipt.INSTANCE.configs().register(ExtraLifeConfig.class);
//
//        GET = HttpConfig.defaults(HttpHeaders.ETAG(config.etag));
//        Quipt.INSTANCE.logger().log("ExtraLifeFlutter", "Initialized");
//    }
//
//    private ExtraLifeConfig config() {
//        return Quipt.INSTANCE.configs().config(ExtraLifeConfig.class);
//    }
//
//    @Override
//    public boolean run() {
//        long now = System.currentTimeMillis();
//        if (now - LAST_HEARTBEAT >= config().seconds_per_check * 1000L) {
//            LAST_HEARTBEAT = now;
//
//            try {
//                HttpResponse<String> response = NetworkUtils.get(GET, config().api_endpoint + "teams/" + config().team_id);
//                if (response.statusCode() != 200 && response.statusCode() != 304) {
//
//                    Quipt.INSTANCE.logger().log("DonationFlutter", "Failed to fetch team data: " + response.statusCode() + " - " + response.body());
//                    return true;
//                }
//                if (response.statusCode() == 304 && offset <= 0) {
//                    Quipt.INSTANCE.logger().log("ExtraLifeFlutter", "No new donations available. Continuing...");
//                    return true; // No new donations continue running
//                }
//                String etag = response.headers().firstValue("etag").orElse("");
//                if (!etag.equals(config().etag) || offset > 0) {
//                    config().etag = etag;
//                    JSONObject teamData = new JSONObject(response.body());
//                    int allDonations = teamData.getInt("numDonations");
//                    if (config().donations < allDonations) {
//                        int diff = allDonations - config().donations;
//                        Quipt.INSTANCE.logger().log("DonationFlutter", "New donations available: " + (diff));
//                        if (diff > MAX_LIMIT) {
//                            Quipt.INSTANCE.logger().log("DonationFlutter", "Too many new donations (" + diff + "), limiting to " + MAX_LIMIT);
//                            if (offset != diff - MAX_LIMIT) offset = offset + (diff - MAX_LIMIT);
//                            diff = MAX_LIMIT;
//                        }
//
//                        sync(diff);
//                    }
//                }
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//        return true; // Continue running
//    }
//
//    private void sync(int diff) throws FileNotFoundException {
//        String url = config().api_endpoint + "teams/" + config().team_id + "/donations?limit=" + (diff) + (offset > 0 ? "&offset=" + (offset + 1) : "");
//        HttpResponse<String> donationsResponse = NetworkUtils.get(NetworkUtils.DEFAULT, url);
//        if (donationsResponse.statusCode() != 200 && donationsResponse.statusCode() != 304) {
//            Quipt.INSTANCE.logger().log("DonationFlutter", "Failed to fetch donations: " + donationsResponse.statusCode() + " - " + donationsResponse.body());
//            return;
//        }
//
//        JSONArray donationsArray = new JSONArray(donationsResponse.body());
//        JSONArray embedArray = new JSONArray();
//        final int preOffset = offset;
//        List<ExtraLifeDonation.ProcessResult<?, ?>> results = new ArrayList<>();
//        for (int i = 0; i < donationsArray.length(); i++) {
//            JSONObject donationJson = donationsArray.getJSONObject(i);
//            ExtraLifeDonation donation = this.donation(new ExtraLifeDonationData(donationJson));
//            if (config().processed(donation)) {
//                Quipt.INSTANCE.logger().warn("DonationFlutter", "Skipping already processed donation: " + donation.donationID);
//                offset = offset + 1;
//            } else {
//                ExtraLifeDonation.ProcessResult<?, ?> result = config().process(donation);
//                embedArray.put(donation.embed().json());
//                results.add(result);
//            }
//        }
//        if (offset != preOffset) {
//            config().save();
//        } else {
//            offset = Math.max(offset - diff, 0);
//        }
//
//        Bukkit.getScheduler().runTask(Utils.initializer(), () -> handleResults(results));
//
//        if (Quipt.INSTANCE.webhooks().get("donations") != null) {
//            int batchSize = 10;
//            int totalEmbeds = embedArray.length();
//            int batches = (int) Math.ceil(totalEmbeds / (double) batchSize);
//
//            for (int batchIndex = 0; batchIndex < batches; batchIndex++) {
//                int startIndex = batchIndex * batchSize;
//                int endIndex = Math.min(startIndex + batchSize, totalEmbeds);
//
//                JSONArray batchArray = new JSONArray();
//                for (int i = startIndex; i < endIndex; i++) {
//                    batchArray.put(embedArray.getJSONObject(i));
//                }
//
//                if (!batchArray.isEmpty()) {
//                    JSONObject send = new JSONObject();
//                    send.put("embeds", batchArray);
//                    Quipt.INSTANCE.webhooks().send("donations", send);
//                }
//            }
//        }
//
//        config().donations = config().donations + diff;
//        Quipt.INSTANCE.logger().log("DonationFlutter", "Total donations: " + config().donations);
//        config().save();
//    }
//
//    private void handleResults(List<ExtraLifeDonation.ProcessResult<?, ?>> results) {
//        Map<ExtraLifeParticipant, Integer> lifeMap = new HashMap<>();
//
//        if (!results.isEmpty()) {
//            Quipt.INSTANCE.logger().log("DonationFlutter", "Processed " + results.size() + " new donations.");
//            for (ExtraLifeDonation.ProcessResult<?, ?> result : results) {
//                ExtraLifeParticipant participant = (ExtraLifeParticipant) result.payload();
//                switch (result.type()) {
//                    case LIFE -> {
//                        // Don't give a life if player is already Dead
//                        if (participant.lives().lives() > 0 && participant.lives().lives() < 3) {
//                            lifeMap.put(participant, lifeMap.getOrDefault(participant, 0) + 1);
//                        }
//                    }
//                    case BUNDLE_LOOT -> {
//                        Utils.genericWebhook("donations", new Color(0xE26922), "Bundle Delivery", null, "A donation to " + participant.player().getName() + " has delivered a bundle of loot!");
//                        for (Party party : Utils.configs().PARTY_CONFIG().parties.values()) {
//                            party.deliver(participant);
//                        }
//                    }
//                    case SHULKER_LOOT -> {
//                        ConfigLocation configLocation = Utils.configs().POI_CONFIG().random();
//                        Location location = new Location(Bukkit.getWorld(configLocation.world), configLocation.x, configLocation.y, configLocation.z);
//
//                        while (!location.getBlock().getType().isAir())
//                            location.add(0, 1, 0);
//                        new ShulkerDelivery(location).start();
//                        String msg = "A donation to " + participant.player().getName() + " has spawned a shulker delivery at the " + configLocation.id() + " POI!";
//                        Utils.genericWebhook("donations", new Color(0x1471A5), "Shulker Delivery", null, msg);
//                        Bukkit.broadcast(Component.text(msg, NamedTextColor.GREEN));
//                    }
//                    case BOOGEYMAN -> {
//                        Utils.configs().PARTICIPANT_CONFIG().boogeymen().queue();
//                        Utils.genericWebhook("boogey", new Color(0xFFD738), "Boogeyman Queue", null, "A donation to " + participant.player().getName() + " has added 1 participant to the boogeyman queue!");
//                        Quipt.INSTANCE.logger().log("Donation", participant.player().getName() + " received a donation on their boogeyman incentive.");
//                    }
//                }
//            }
//            for (Map.Entry<ExtraLifeParticipant, Integer> entry : lifeMap.entrySet()) {
//                entry.getKey().lives().add(entry.getValue());
//                Utils.genericWebhook("donations", new Color(0x85FF00), "Lives", null, entry.getKey().player().getName() + " has received " + entry.getValue() + " extra life" + (entry.getValue() > 1 ? "s" : "") + " from donations! They now have " + entry.getKey().lives().get() + " life" + (entry.getKey().lives().get() > 1 ? "s" : "") + ".");
//            }
//            Utils.configs().PARTICIPANT_CONFIG().save();
//        }
//        Utils.configs().DUNGEON_MANAGER.handleNewDonationTotal(true);
//    }
//
//    @Override
//    public ExtraLifeDonation donation(ExtraLifeDonationData data) {
//        return null;
//    }
}
