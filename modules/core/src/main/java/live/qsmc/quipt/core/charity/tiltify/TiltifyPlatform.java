package live.qsmc.quipt.core.charity.tiltify;

import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.charity.CharityPlatform;
import live.qsmc.quipt.core.charity.ProcessResult;

public class TiltifyPlatform extends CharityPlatform<TiltifyDonation> {

    public TiltifyPlatform(QuiptIntegration integration) {
        super(integration, "tiltify", "Tiltify", TiltifyDonation.class);
    }

    @Override
    public ProcessResult<TiltifyDonation> process(TiltifyDonation donation) {
        return null;
    }
}
