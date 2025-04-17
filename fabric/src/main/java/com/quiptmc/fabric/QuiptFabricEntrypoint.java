package com.quiptmc.fabric;

import com.quiptmc.minecraft.CoreUtils;
import com.quiptmc.fabric.api.QuiptEntrypoint;
import com.quiptmc.minecraft.utils.loaders.ServerLoader;

public class QuiptFabricEntrypoint extends QuiptEntrypoint {

    public QuiptFabricEntrypoint() {
        super("Quipt", "1.0.0", "quipt");
    }

    @Override
    public void onInitialize() {
        CoreUtils.init(this);
    }
}
