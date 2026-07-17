package live.qsmc.quipt.core.charity.el;

import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.charity.CharityDonation;
import live.qsmc.quipt.core.charity.ProcessResult;
import live.qsmc.quipt.core.data.JsonSerializable;
import org.json.JSONObject;

public class ExtraLifeDonation extends CharityDonation {

    public String displayName = "";
    public String donorId = "";
    public Links links = new Links();
    public boolean isRegFee = false;
    public int eventID = 0;
    public String createdDateUTC = "";
    public String recipientName = "";
    public String recipientImageURL = "";
    public int participantID = 0;
    public double amount = 0.0;
    public String avatarImageURL = "";
    public int teamID = 0;
    public String donationID = "";
    public String incentiveID = "";
    public String message = "";

    public ExtraLifeDonation(QuiptIntegration integration) {
        super(integration);
    }

    @Override
    public double amount() {
        return amount;
    }

    @Override
    public String donor() {
        return donorId;
    }

    @Override
    public String participant() {
        return participantID + "";
    }


//    @Override
//    public <D extends CharityDonation> ProcessResult<D> process() {
//        return null;
//    }


    public static class Links implements JsonSerializable {
        String recipient = "";
        String donate = "";

        public Links() {

        }

        public Links(JSONObject json) {
            this.fromJson(json);
        }
    }


}