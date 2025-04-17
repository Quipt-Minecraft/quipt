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

public abstract class QuiptEntrypoint<T extends JavaMod> extends MinecraftIntegration<T>  {

    private final Logger LOGGER = LoggerFactory.getLogger(QuiptEntrypoint.class);

    public QuiptEntrypoint(String name, String version, String id) {
        super(name, new ServerLoader<>(ServerLoader.Type.FABRIC, new JavaMod(name, version, id)));
    }

    public void run(EntrypointContainer<QuiptEntrypoint> entrypoint) {
        LOGGER.info("Initializing {} v{} ({})...", mod().name(), mod().version(), mod().id());
        onInitialize();

    }


    public JavaMod mod() {
        return (JavaMod) loader().instance();
    }

    public abstract void onInitialize();
}
