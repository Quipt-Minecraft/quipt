package com.quiptmc.fabric;

import com.quiptmc.minecraft.CoreUtils;
import com.quiptmc.fabric.api.QuiptEntrypoint;
import com.quiptmc.minecraft.utils.loaders.ServerLoader;

public class QuiptFabricEntrypoint extends QuiptEntrypoint {

    @Override
    public void onInitialize() {
        CoreUtils.init(new QuiptFabricIntegration(new ServerLoader<>(ServerLoader.Type.FABRIC, mod().orElse(null))));
    }
}
