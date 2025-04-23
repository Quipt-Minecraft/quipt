package com.quiptmc.minecraft.files.resource;

import com.quiptmc.core.QuiptIntegration;
import com.quiptmc.core.storage.Config;
import com.quiptmc.core.storage.ConfigTemplate;
import com.quiptmc.core.storage.ConfigValue;
import com.quiptmc.core.storage.NestedConfig;

@ConfigTemplate(name = "auth")
public class AuthNestedConfig<T extends Config> extends NestedConfig<T> {

    @ConfigValue
    public boolean isPrivate = false;

    @ConfigValue
    public String username = "username";

    @ConfigValue
    public String password = "password";

    public AuthNestedConfig(T parent, String name, QuiptIntegration integration) {
        super(parent, name, integration);
    }


}
