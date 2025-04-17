package com.quiptmc.fabric;

import com.quiptmc.fabric.api.QuiptEntrypoint;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class Initializer implements ModInitializer  {



    @Override
    public void onInitialize() {



        FabricLoader.getInstance().getEntrypointContainers("quipt", QuiptEntrypoint.class).forEach(container -> container.getEntrypoint().run(container));
    }

}
