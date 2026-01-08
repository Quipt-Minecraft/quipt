package com.quiptmc.fabric.api;

import com.quiptmc.minecraft.utils.MinecraftIntegration;
import com.quiptmc.core.heartbeat.HeartbeatUtils;
import com.quiptmc.minecraft.utils.loaders.ServerLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;

public class FabricIntegration extends MinecraftIntegration<ServerLoader<ModMetadata>> {

    public FabricIntegration(String name, ServerLoader<ModMetadata> loader) {
        super(name, loader);
    }

}
