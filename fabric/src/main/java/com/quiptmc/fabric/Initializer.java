package com.quiptmc.fabric;

import com.quiptmc.fabric.api.QuiptEntrypoint;
import com.quiptmc.minecraft.CoreUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;

public class Initializer implements ModInitializer  {



    @Override
    public void onInitialize() {
        //Load Quipt data

        //Load other Quipt mods
        FabricLoader.getInstance().getEntrypointContainers("quipt", QuiptEntrypoint.class).forEach(container -> container.getEntrypoint().run(container));
    }



}
