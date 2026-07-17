package live.qsmc.quipt.core.charity.tiltify;

import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.charity.CharityDonation;
import live.qsmc.quipt.core.config.Config;
import live.qsmc.quipt.core.config.ConfigTemplate;
import live.qsmc.quipt.core.config.ConfigValue;
import live.qsmc.quipt.core.config.objects.ConfigMap;

import java.io.File;
import java.math.BigDecimal;

public abstract class CharityConfig<D extends CharityDonation> extends Config {

    @ConfigValue
    public ConfigMap<D> processed;

    @ConfigValue
    public int seconds_per_check = 20;

    @ConfigValue
    public int team_id = 0;

    @ConfigValue
    public int donations = 0;

    @ConfigValue
    public BigDecimal total = BigDecimal.valueOf(0.0);

    public CharityConfig(File file, String name, ConfigTemplate.Extension extension, QuiptIntegration integration) {
        super(file, name, extension, integration);
        processed = new ConfigMap<>(integration);
    }
}
