package com.quiptmc.fabric.api;

import com.quiptmc.minecraft.utils.MinecraftIntegration;
import com.quiptmc.minecraft.utils.loaders.ServerLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Optional;

public abstract class QuiptEntrypoint extends MinecraftIntegration  {

    @Nullable
    private JavaMod mod = null;
    private final Logger LOGGER = LoggerFactory.getLogger(QuiptEntrypoint.class);

    public QuiptEntrypoint(ServerLoader<?> loader) {
        super(loader);
    }

    public void run(EntrypointContainer<QuiptEntrypoint> entrypoint) {
        ModContainer container = entrypoint.getProvider();
        mod = new JavaMod(container.getMetadata());
//        logger().log("Initializing {} v{} ({})...", mod.name(), mod.version(), mod.id());
        onInitialize();
        LOGGER.info("Initializing {} v{} ({})...", mod.name(), mod.version(), mod.id());
    }


    public Optional<JavaMod> mod() {
        return Optional.ofNullable(mod);
    }

    public abstract void onInitialize();
}
