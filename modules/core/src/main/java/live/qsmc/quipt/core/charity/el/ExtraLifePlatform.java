package live.qsmc.quipt.core.charity.el;

import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.charity.CharityDonation;
import live.qsmc.quipt.core.charity.CharityPlatform;
import live.qsmc.quipt.core.charity.ProcessResult;
import live.qsmc.quipt.core.charity.event.DonationUpdateEvent;
import live.qsmc.quipt.core.config.files.ExtraLifeConfig;
import live.qsmc.quipt.core.heartbeat.flutter.Flutter;
import live.qsmc.quipt.core.utils.net.HttpConfig;
import live.qsmc.quipt.core.utils.net.HttpHeaders;
import live.qsmc.quipt.core.utils.net.NetworkUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static live.qsmc.quipt.core.charity.ProcessResult.Type.INVALID;
import static live.qsmc.quipt.core.charity.ProcessResult.Type.SUCCESS;

public class ExtraLifePlatform extends CharityPlatform<ExtraLifeDonation> implements Flutter  {

    public static final int MAX_LIMIT = 100;

    private final HttpConfig GET;
    private long LAST_HEARTBEAT = 0;
    private int offset = 0;

    public ExtraLifePlatform(QuiptIntegration integration) {
        super(integration, "extralife", "Extra Life", ExtraLifeDonation.class);

        ExtraLifeConfig config = Quipt.INSTANCE.configs().register(ExtraLifeConfig.class);

        GET = HttpConfig.defaults(HttpHeaders.ETAG(config.etag));
        Quipt.INSTANCE.logger().log("ExtraLifeFlutter", "Initialized");
    }

    private ExtraLifeConfig config() {
        return Quipt.INSTANCE.configs().config(ExtraLifeConfig.class);
    }

    @Override
    public boolean run() {
        long now = System.currentTimeMillis();
        if (now - LAST_HEARTBEAT >= config().seconds_per_check * 1000L) {
            LAST_HEARTBEAT = now;

            try {
                HttpResponse<String> response = NetworkUtils.get(GET, config().api_endpoint + "teams/" + config().team_id);
                if (response.statusCode() != 200 && response.statusCode() != 304) {

                    Quipt.INSTANCE.logger().log("DonationFlutter", "Failed to fetch team data: " + response.statusCode() + " - " + response.body());
                    return true;
                }
                if (response.statusCode() == 304 && offset <= 0) {
                    Quipt.INSTANCE.logger().log("ExtraLifeFlutter", "No new donations available. Continuing...");
                    return true; // No new donations continue running
                }
                String etag = response.headers().firstValue("etag").orElse("");
                if (!etag.equals(config().etag) || offset > 0) {
                    config().etag = etag;
                    JSONObject teamData = new JSONObject(response.body());
                    int allDonations = teamData.getInt("numDonations");
                    if (config().donations < allDonations) {
                        int diff = allDonations - config().donations;
                        Quipt.INSTANCE.logger().log("DonationFlutter", "New donations available: " + (diff));
                        if (diff > MAX_LIMIT) {
                            Quipt.INSTANCE.logger().log("DonationFlutter", "Too many new donations (" + diff + "), limiting to " + MAX_LIMIT);
                            if (offset != diff - MAX_LIMIT) offset = offset + (diff - MAX_LIMIT);
                            diff = MAX_LIMIT;
                        }

                        sync(diff);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return true; // Continue running
    }

    private void sync(int diff) throws FileNotFoundException {
        String url = config().api_endpoint + "teams/" + config().team_id + "/donations?limit=" + (diff) + (offset > 0 ? "&offset=" + (offset + 1) : "");
        HttpResponse<String> donationsResponse = NetworkUtils.get(NetworkUtils.DEFAULT, url);
        if (donationsResponse.statusCode() != 200 && donationsResponse.statusCode() != 304) {
            Quipt.INSTANCE.logger().log("DonationFlutter", "Failed to fetch donations: " + donationsResponse.statusCode() + " - " + donationsResponse.body());
            return;
        }

        JSONArray donationsArray = new JSONArray(donationsResponse.body());
//        JSONArray embedArray = new JSONArray();
        final int preOffset = offset;
        List<ProcessResult<ExtraLifeDonation>> results = new ArrayList<>();
        for (int i = 0; i < donationsArray.length(); i++) {
            JSONObject donationJson = donationsArray.getJSONObject(i);
            ExtraLifeDonation donation = this.donation(donationJson);
            if (config().processed(donation)) {
                Quipt.INSTANCE.logger().warn("DonationFlutter", "Skipping already processed donation: " + donation.donationID);
                offset = offset + 1;
            } else {
                ProcessResult<ExtraLifeDonation> result = process(donation);
//                embedArray.put(donation.embed().json());
                results.add(result);
            }
        }
        if (offset != preOffset) {
            config().save();
        } else {
            offset = Math.max(offset - diff, 0);
        }
        DonationUpdateEvent.Data<ExtraLifeDonation> data = new DonationUpdateEvent.Data<>(results);
        Quipt.INSTANCE.events().handle(new DonationUpdateEvent<>(this, data));

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

        config().donations = config().donations + diff;
        Quipt.INSTANCE.logger().log("DonationFlutter", "Total donations: " + config().donations);
        config().save();
    }

    @Override
    public ProcessResult<ExtraLifeDonation> process(ExtraLifeDonation donation) {
        if(config().processed(donation)) {
            Quipt.INSTANCE.logger().warn("DonationFlutter", "Skipping already processed donation: " + donation.donationID);
            return new ProcessResult<>(INVALID, donation);
        }
        config().processed.put(donation);
        config().total = BigDecimal.valueOf(config().total.doubleValue() + donation.amount);
        config().save();
        return new ProcessResult<>(SUCCESS, donation);
    }
}
