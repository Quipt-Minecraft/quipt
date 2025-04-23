package com.quiptmc.minecraft.files.web;

import com.quiptmc.core.QuiptIntegration;
import com.quiptmc.core.storage.Config;
import com.quiptmc.core.storage.ConfigTemplate;
import com.quiptmc.core.storage.ConfigValue;
import com.quiptmc.core.storage.NestedConfig;

@ConfigTemplate(name = "health-report")
public class HealthReportNestedConfig<T extends Config> extends NestedConfig<T> {

    @ConfigValue
    public boolean enable = true;
    public String update = "1h";
    public String root = "/healthreport";


    public HealthReportNestedConfig(T parent, String name, QuiptIntegration integration) {
        super(parent, name, integration);
    }
}
