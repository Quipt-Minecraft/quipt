package com.quiptmc2.paper.api.settings;

import com.quiptmc2.core.Quipt;
import com.quiptmc2.core.QuiptIntegration;
import com.quiptmc2.core.config.objects.ConfigObject;
import com.quiptmc2.core.data.registries.Registry;

public class Setting extends ConfigObject {

    private ConfigObject value;

    public Setting(QuiptIntegration integration, String id, ConfigObject defaultValue) {
        super(integration);
        super.id = id;
        this.value = defaultValue;
        Registry<ConfigObject> registry;
        if(Quipt.INSTANCE.registries().key("settings") == null) {
            registry = Quipt.INSTANCE.registries().register("settings", () -> null);
        } else registry = Quipt.INSTANCE.registries().get(Quipt.INSTANCE.registries().key("settings"), ConfigObject.class);
        if(registry.get(id).isPresent()) throw new IllegalArgumentException("Setting ID already exists");
        registry.register(id, defaultValue);
    }

//    public static Setting of(QuiptIntegration integration, String id) {
//
//    }

    public ConfigObject value() {
        return value;
    }

    public void set(ConfigObject value) {
        this.value = value;
    }

}
