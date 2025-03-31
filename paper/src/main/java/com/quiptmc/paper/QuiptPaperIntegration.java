package com.quiptmc.paper;

import com.quiptmc.minecraft.utils.MinecraftIntegration;
import com.quiptmc.minecraft.utils.loaders.ServerLoader;
import org.jetbrains.annotations.Nullable;

public class QuiptPaperIntegration extends MinecraftIntegration {


    public QuiptPaperIntegration(@Nullable ServerLoader<?> loader) {
        super(loader);
    }

    @Override
    public void enable() {
        super.enable();


//        minecraftServer = new MinecraftServer(apiManager.getIP(), apiConfig.secret, apiConfig.endpoint + "/server_status/");
    }


}