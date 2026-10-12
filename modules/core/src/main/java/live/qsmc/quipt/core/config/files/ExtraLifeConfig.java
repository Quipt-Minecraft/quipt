package live.qsmc.quipt.core.config.files;


import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.charity.el.ExtraLifeDonation;
import live.qsmc.quipt.core.charity.tiltify.CharityConfig;
import live.qsmc.quipt.core.config.ConfigTemplate;
import live.qsmc.quipt.core.config.ConfigValue;

import java.io.File;

@ConfigTemplate(name = "donations", ext = ConfigTemplate.Extension.JSON)
public class ExtraLifeConfig extends CharityConfig<ExtraLifeDonation> {

    @ConfigValue
    public String api_endpoint = "https://www.extra-life.org/api/";



    @ConfigValue
    public String etag = "null";

    public ExtraLifeConfig(File file, String name, ConfigTemplate.Extension extension, QuiptIntegration integration) {
        super(file, name, extension, integration);
    }

//    public ProcessResult<ExtraLifeDonation> process(ExtraLifeDonation donation) {
//        processed.put(donation);
//        total = BigDecimal.valueOf(total.doubleValue() + donation.amount);
//        return donation.process();
//    }

    public boolean processed(ExtraLifeDonation donation) {
        return processed.contains(donation.id);
    }
}
