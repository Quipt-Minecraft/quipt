package live.qsmc.quipt.core.events.charity;

import live.qsmc.quipt.core.charity.CharityPlatform;
import live.qsmc.quipt.core.data.annotations.Nullable;
import live.qsmc.quipt.core.events.Event;
import live.qsmc.quipt.core.events.EventData;
import org.json.JSONObject;

public class DonationUpdateEvent extends Event<DonationUpdateEvent.Data> {


    public DonationUpdateEvent(Data original) {
        super(original);
    }

    public static class Data extends EventData {

        private final CharityPlatform platform;
        private final JSONObject raw;
        private final double amount;
        private final String donator;
        private final String participant;
        private final String comment;

        public Data(CharityPlatform platform,
                    @Nullable String donator,
                    @Nullable String participant,
                    @Nullable String comment,
                    double amount,
                    JSONObject raw) {
            this.platform = platform;
            this.donator = donator == null ? "Anonymous" : donator;
            this.participant = participant == null ? "Unknown" : participant;
            this.comment = comment == null ? "" : comment;
            this.amount = amount;
            this.raw = raw;

        }

        public CharityPlatform platform() {
            return platform;
        }

        public JSONObject raw() {
            return raw;
        }

        public String donor(){
            return donator;
        }

        public String participant(){
            return participant;
        }

        public String comment(){
            return comment;
        }

        public double amount(){
            return amount;
        }
    }
}
