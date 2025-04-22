package com.quiptmc.fabric;

import com.quiptmc.fabric.api.QuiptEntrypoint;
import com.quiptmc.minecraft.CoreUtils;
import net.fabricmc.loader.api.metadata.ModMetadata;

public class FabricLoaderEntrypoint extends QuiptEntrypoint {


    @Override
    public void onInitialize(ModMetadata metadata) {
        CoreUtils.init(this.integration());



    }
}