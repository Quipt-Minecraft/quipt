package live.qsmc.quipt.core.charity;

import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.charity.el.ExtraLifeDonation;
import live.qsmc.quipt.core.config.objects.ConfigObject;

public abstract class CharityDonation extends ConfigObject {
    public CharityDonation(QuiptIntegration integration) {
        super(integration);
    }

    public abstract double amount();

    public abstract String donor();

    public abstract String participant();
}
