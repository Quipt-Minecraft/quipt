package com.quiptmc.minecraft.files.discord;

import com.quiptmc.core.QuiptIntegration;
import com.quiptmc.core.storage.Config;
import com.quiptmc.core.storage.ConfigValue;
import com.quiptmc.core.storage.NestedConfig;

public class ChannelsNestedConfig<T extends Config> extends NestedConfig<T> {

    @ConfigValue
    public String player_status = "";

    @ConfigValue
    public String server_status = "";

    public ChannelsNestedConfig(T parent, String name, QuiptIntegration integration) {
        super(parent, name, integration);
    }
}
