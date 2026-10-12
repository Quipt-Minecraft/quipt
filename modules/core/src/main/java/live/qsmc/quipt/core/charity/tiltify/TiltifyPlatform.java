package live.qsmc.quipt.core.charity.tiltify;

import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.charity.CharityPlatform;
import live.qsmc.quipt.core.charity.ProcessResult;
import live.qsmc.quipt.core.config.files.TiltifyConfig;

public class TiltifyPlatform extends CharityPlatform<TiltifyDonation, TiltifyConfig> {

    public TiltifyPlatform(QuiptIntegration integration) {
        super(integration, "tiltify", "Tiltify", TiltifyDonation.class, TiltifyConfig.class);
    }

    @Override
    public ProcessResult<TiltifyDonation> process(TiltifyDonation donation) {
        return null;
    }
}
