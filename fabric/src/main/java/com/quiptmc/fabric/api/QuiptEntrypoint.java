package com.quiptmc.fabric.api;

import com.quiptmc.minecraft.utils.MinecraftIntegration;
import com.quiptmc.minecraft.utils.loaders.ServerLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Optional;

public abstract class QuiptEntrypoint {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private MinecraftIntegration integration = null;

    public void run(EntrypointContainer<QuiptEntrypoint> entrypoint) {
        ModMetadata metadata = entrypoint.getProvider().getMetadata();
        integration = new MinecraftIntegration(metadata.getName(), new ServerLoader<>(ServerLoader.Type.FABRIC, metadata));
        LOGGER.info("Initializing {} v{} ({})...", metadata.getName(), metadata.getVersion(), metadata.getId());
        onInitialize(metadata);

    }

    public MinecraftIntegration integration() {
        return integration;
    }

    public abstract void onInitialize(ModMetadata metadata);
}
