package live.qsmc.quipt.core.charity.tiltify;

import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.charity.CharityDonation;

public class TiltifyDonation extends CharityDonation {

    public TiltifyDonation(QuiptIntegration integration) {
        super(integration);
    }

    @Override
    public double amount() {
        return 0;
    }

    @Override
    public String donor() {
        return "";
    }

    @Override
    public String participant() {
        return "";
    }

}
