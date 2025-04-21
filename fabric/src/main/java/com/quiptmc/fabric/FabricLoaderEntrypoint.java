package com.quiptmc.fabric;

import com.quiptmc.fabric.api.QuiptEntrypoint;
import com.quiptmc.minecraft.CoreUtils;

public class FabricLoaderEntrypoint extends QuiptEntrypoint {


    @Override
    public void onInitialize() {
        CoreUtils.init(this.integration());

    }
}